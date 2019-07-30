package com.techbelife.qcloud;

import com.techbelife.util.HMACSHA1Utils;
import com.techbelife.util.PropertiesUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;


public class UploadTokenUtils {
    private final static String SK = PropertiesUtils.getProperty("qcloud.SK");
    private final static String SI = PropertiesUtils.getProperty("qcloud.SI");

    public static String getUploadToken(HttpEntityEnclosingRequestBase httpRequest) {
        try {
            String httpMethod = httpRequest.getMethod().toLowerCase();
            String uri = httpRequest.getURI().getPath();
            long startTimestamp = System.currentTimeMillis() / 1000;
            long endTimestamp = startTimestamp + 600;
            String keyTime = startTimestamp + ";" + endTimestamp;
            String signKey = HMACSHA1Utils.HmacSHA1HEX(keyTime, SK).toLowerCase();
            String httpString = httpMethod + "\n" + uri + "\n\n\n";
            String stringToSign = "sha1\n" + keyTime + "\n" + DigestUtils.sha1Hex(httpString.getBytes()) + "\n";
            String signature = HMACSHA1Utils.HmacSHA1HEX(stringToSign, signKey).toLowerCase();
            return "q-sign-algorithm=sha1&q-ak=" + SI + "&q-sign-time=" + keyTime + "&q-key-time=" + keyTime + "&q-header-list=&q-url-param-list=&q-signature=" + signature;
        } catch (Exception e) {
            return "";
        }
    }

}
