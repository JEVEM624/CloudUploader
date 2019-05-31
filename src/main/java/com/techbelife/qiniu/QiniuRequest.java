package com.techbelife.qiniu;

import com.techbelife.util.Base64;
import com.techbelife.util.PropertiesUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class QiniuRequest {
    private static final String REGION = PropertiesUtils.getProperty("qiniu.region");
    private static final String URI = UploadUrl.getUri(REGION);


    public static int post(MultipartEntityBuilder body) throws IOException {
        HttpPost httpPost = new HttpPost(URI);
        body.addTextBody("action", URI);
        CloseableHttpClient client = HttpClients.createDefault();
        httpPost.setEntity(body.build());
        HttpResponse response = client.execute(httpPost);
        int status = response.getStatusLine().getStatusCode();
        client.close();
        return status;
    }

    public static Map bput(String ctx, String host, int offset, String token, byte[] bytes) throws IOException {
        String url = host + "/bput/" + ctx + "/" + offset;
        return post(url, bytes, token);
    }

    public static Map mkblk(byte[] bytes, long blockLength, String token) throws IOException {
        String url = URI + "/mkblk/" + blockLength;
        return post(url, bytes, token);
    }

    public static Map post(String url, byte[] bytes, String token) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/octet-stream");
        httpPost.addHeader("Authorization", "UpToken " + token);
        httpPost.setEntity(new ByteArrayEntity(bytes));
        CloseableHttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(httpPost);
        HashMap<String, String> res = new HashMap<>(2);
        res.put("status", String.valueOf(response.getStatusLine().getStatusCode()));
        res.put("body", EntityUtils.toString(response.getEntity()));
        client.close();
        return res;
    }

    public static int mkfile(String ctx, long fileSize, String token, String scope) throws IOException {
        scope = Base64.safeEncode(scope);
        HttpPost httpPost = new HttpPost(URI + "/mkfile/" + fileSize + "/key/" + scope);
        httpPost.addHeader("Authorization", "UpToken " + token);
        httpPost.addHeader("Content-Type", "text/plain");
        httpPost.setEntity(new StringEntity(ctx));
        CloseableHttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(httpPost);
        client.close();
        return response.getStatusLine().getStatusCode();
    }
}
