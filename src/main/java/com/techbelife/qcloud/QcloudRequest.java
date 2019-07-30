package com.techbelife.qcloud;


import com.techbelife.util.Base64;
import com.techbelife.util.MD5Util;
import com.techbelife.util.PropertiesUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
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
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class QcloudRequest {
    private static final String URL = PropertiesUtils.getProperty("qcloud.ossDomain");

    public static int put(byte[] bytes, String fileName) throws IOException {
        HttpPut httpPut = new HttpPut("https://" + URL + "/" + fileName);
        httpPut.setEntity(new ByteArrayEntity(bytes));
        setRequest(httpPut);
        setContentMd5(httpPut, bytes);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(httpPut);
        return response.getStatusLine().getStatusCode();
    }

    public static String getUploadId(String fileName) throws IOException, SAXException, ParserConfigurationException {
        String url = "https://" + URL + "/" + fileName + "?uploads";
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = HttpClients.createDefault();
        setRequest(httpPost);
        HttpResponse response = client.execute(httpPost);
        InputStream is = response.getEntity().getContent();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        Element rootElement = document.getDocumentElement();
        NodeList uploadIdNode = rootElement.getElementsByTagName("UploadId");
        String uploadId = uploadIdNode.item(0).getTextContent();
        return uploadId;
    }

    public static int uploadPart(byte[] bytes, String fullFileName, String partNumber, String uploadId) throws IOException {
        String url = "https://" + URL + "/" + fullFileName + "?partNumber=" + partNumber + "&uploadId=" + uploadId;
        HttpPut httpPut = new HttpPut(url);
        setRequest(httpPut);
        setContentMd5(httpPut, bytes);
        httpPut.setEntity(new ByteArrayEntity(bytes));
        CloseableHttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(httpPut);
        int code = response.getStatusLine().getStatusCode();
        return code;
    }

    public static int completeMultipartUpload(String fileName, String content, String uploadId)throws IOException {
        HttpPost httpPost = new HttpPost("https://" + URL + "/" + fileName + "?uploadId=" + uploadId);
        setRequest(httpPost);
        httpPost.setEntity(new StringEntity(content));
        CloseableHttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(httpPost);
        int code =response.getStatusLine().getStatusCode();
        return code;
    }

    private static String getDate() {
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(cd.getTime());
    }

    private static void setRequest(HttpEntityEnclosingRequestBase request) {
        String date = getDate();
        request.setHeader("date", date);
        request.setHeader("Content-Type", "application/octet-stream");
        String auth = UploadTokenUtils.getUploadToken(request);
        request.setHeader("Authorization", auth);
    }
    private static void setContentMd5(HttpEntityEnclosingRequestBase requestBase,byte[]data){
        byte[] md5Bytes = MD5Util.md5(data);
        String md5 = Base64.EncodeToString(md5Bytes);
        requestBase.setHeader("Content-MD5", md5);
        return;
    }
}
