package com.techbelife.aliyun;


import com.techbelife.Exception.AuthException;
import com.techbelife.util.Base64;
import com.techbelife.util.MD5Util;
import com.techbelife.util.PropertiesUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class AliyunRequest {
    private static final String URL = PropertiesUtils.getProperty("aliyun.ossDomain");
    private static final int OK = 200;

    public static int put(byte[] bytes, String fileName) throws IOException {
        String date = getDate();
        byte[] md5Bytes = MD5Util.md5(bytes);
        String md5 = Base64.EncodeToString(md5Bytes);
        String token = UploadTokenUtils.getUploadToken("PUT", fileName, date, md5);
        HttpPut httpPut = new HttpPut("https://" + URL + "/" + fileName);
        httpPut.addHeader("date", date);
        httpPut.addHeader("Authorization", "OSS " + token);
        if (md5.length() != 0) {
            httpPut.addHeader("Content-MD5", md5);
        }
        httpPut.setEntity(new ByteArrayEntity(bytes));
        CloseableHttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(httpPut);
        return response.getStatusLine().getStatusCode();
    }

    public static String put(String fullFileName, File file, int seq, String uploadId) throws IOException, FileNotFoundException, NoSuchAlgorithmException {
        String date = getDate();
        FileInputStream is = new FileInputStream(file);
        while (is == null) {
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                is = null;
            }
        }
        byte[] data = new byte[(int) file.length()];
        is.read(data);
        byte[] md5Bytes = MD5Util.md5(data);
        String md5 = Base64.EncodeToString(md5Bytes);
        String resource = fullFileName + "?partNumber=" + seq + "&uploadId=" + uploadId;

        String token = UploadTokenUtils.getUploadToken("PUT", resource, date, md5);
        HttpPut httpPut = new HttpPut("https://" + URL + "/" + resource);
        httpPut.addHeader("date", date);
        httpPut.addHeader("Authorization", "OSS " + token);
        httpPut.addHeader("Content-MD5", md5);
        httpPut.setEntity(new ByteArrayEntity(data));
        CloseableHttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(httpPut);
        is.close();
        if (response.getStatusLine().getStatusCode() != OK) {
            response = client.execute(httpPut);
        }
        return response.getHeaders("ETag")[0].getValue();
    }

    public static String getUploadId(String fileName) throws IOException, ParserConfigurationException, SAXException, AuthException {
        String date = getDate();
        HttpPost httpPost = new HttpPost("https://" + URL + "/" + fileName + "?uploads");
        String token = UploadTokenUtils.getUploadToken("POST", fileName + "?uploads", date);
        httpPost.addHeader("date", date);
        httpPost.addHeader("Authorization", "OSS " + token);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(httpPost);
        if (response.getStatusLine().getStatusCode() != OK) {
            throw new AuthException();
        }
        InputStream is = response.getEntity().getContent();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        Element rootElement = document.getDocumentElement();
        NodeList uploadIdNode = rootElement.getElementsByTagName("UploadId");
        String uploadId = uploadIdNode.item(0).getTextContent();
        return uploadId;
    }

    public static void completeMultipartUpload(String fileName, String content, String uploadId) throws IOException, AuthException {
        String date = getDate();
        String contentType = "text/xml;charset=utf-8";
        HttpPost httpPost = new HttpPost("https://" + URL + "/" + fileName + "?uploadId=" + uploadId);
        String token = UploadTokenUtils.getUploadToken("POST", fileName + "?uploadId=" + uploadId, date, "", contentType);
        httpPost.addHeader("date", date);
        httpPost.addHeader("Content-Type", contentType);
        httpPost.addHeader("Authorization", "OSS " + token);
        httpPost.setEntity(new StringEntity(content));
        CloseableHttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(httpPost);
        if (response.getStatusLine().getStatusCode() != OK) {
            throw new AuthException();
        }

        return;
    }

    private static String getDate() {
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(cd.getTime());
    }
}
