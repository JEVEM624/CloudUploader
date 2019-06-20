package com.techbelife.aliyun;

import com.techbelife.util.Base64;
import com.techbelife.util.HMACSHA1Utils;
import com.techbelife.util.PropertiesUtils;


public class UploadTokenUtils {
    private static final String AK = PropertiesUtils.getProperty("aliyun.AK");
    private static final String SK = PropertiesUtils.getProperty("aliyun.SK");
    private static final String BUCKET = PropertiesUtils.getProperty("aliyun.bucket");

    public static String getUploadToken(String method, String resource, String date, String md5, String contentType) {
        String text = method + "\n"
                + (md5.length() != 0 ? (md5) : "\n")
                + (contentType.length() != 0 ? (contentType) : "") + "\n"
                + date + "\n"
                + "/" + BUCKET + "/" + resource;
        byte[] bytes = null;
        try {
            bytes = HMACSHA1Utils.HmacSHA1Encrypt(text, SK);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return AK + ":" + Base64.EncodeToString(bytes);
    }

    public static String getUploadToken(String method, String resource, String date) {
        return getUploadToken(method, resource, date, "", "");
    }

    public static String getUploadToken(String method, String resource, String date, String md5) {
        return getUploadToken(method, resource, date, md5, "");
    }
}
