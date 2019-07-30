package com.techbelife.upyun;

import com.techbelife.util.MD5Util;
import com.techbelife.util.PropertiesUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class UpyunRequest {
    private final static String BUCKET = PropertiesUtils.getProperty("upyun.bucket");
    private final static String URL = "https://v0.api.upyun.com";

    public static int put(byte[] data, String fileName) throws Exception {
        String uri = URL+"/" + BUCKET + "/" + fileName;
        HttpPut httpPut = new HttpPut(uri);
        httpPut.setEntity(new ByteArrayEntity(data));
        setDate(httpPut);
        setContentMd5(httpPut);
        setAuth(httpPut);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(httpPut);
        System.out.println(EntityUtils.toString(response.getEntity()));
        return response.getStatusLine().getStatusCode();
    }


    public static String initUpload(File file)throws Exception{
        String url=URL+"/"+BUCKET+"/"+file.getName();
        HttpPut httpPut=new HttpPut(url);
        httpPut.setHeader("X-Upyun-Multi-Disorder", "true");
        httpPut.setHeader("X-Upyun-Multi-Stage", "initiate");
        httpPut.setHeader("X-Upyun-Multi-Length", String.valueOf(file.length()));
        String md5=MD5Util.md5String("");
        httpPut.setHeader("Content-MD5", md5);
        setDate(httpPut);
        setAuth(httpPut);
        CloseableHttpClient client=HttpClients.createDefault();
        HttpResponse response=client.execute(httpPut);
        if (response.getStatusLine().getStatusCode()==204){
            return response.getFirstHeader("X-Upyun-Multi-Uuid").getValue();
        }
        return "";
    }
    public static int put(String fullFileName,String uuid,int seq,byte[]data)throws Exception{
        String url=URL+"/"+BUCKET+"/"+fullFileName;
        HttpPut httpPut=new HttpPut(url);
        httpPut.setHeader("X-Upyun-Multi-Stage", "upload");
        httpPut.setHeader("X-Upyun-Multi-Uuid", uuid);
        httpPut.setHeader("X-Upyun-Part-Id", String.valueOf(seq));
        httpPut.setEntity(new ByteArrayEntity(data));
        setContentMd5(httpPut);
        setDate(httpPut);
        setAuth(httpPut);
        CloseableHttpClient client=HttpClients.createDefault();
        HttpResponse response=client.execute(httpPut);
        return response.getStatusLine().getStatusCode();
    }
    public static int completeUpload(String fullFileName,String uuid)throws Exception{
        String url=URL+"/"+BUCKET+"/"+fullFileName;
        HttpPut httpPut=new HttpPut(url);
        httpPut.setHeader("X-Upyun-Multi-Stage", "complete");
        httpPut.setHeader("X-Upyun-Multi-Uuid", uuid);
        String md5=MD5Util.md5String("");
        httpPut.setHeader("Content-MD5", md5);
        setDate(httpPut);
        setAuth(httpPut);
        CloseableHttpClient client=HttpClients.createDefault();
        HttpResponse response=client.execute(httpPut);
        return response.getStatusLine().getStatusCode();
    }


    private static void setDate(HttpEntityEnclosingRequestBase request) {
        String date = getDate();
        request.setHeader("Date", date);
        return;
    }

    private static void setContentMd5(HttpEntityEnclosingRequestBase request) throws NoSuchAlgorithmException, IOException {
        String md5 = MD5Util.md5String(request.getEntity().getContent());
        request.setHeader("Content-MD5", md5);
        return;
    }

    private static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date());
    }

    private static void setAuth(HttpEntityEnclosingRequestBase request) throws Exception {
        String token = UploadTokenUtils.getUploadToken(request);
        request.setHeader("Authorization", token);
        return;
    }

}
