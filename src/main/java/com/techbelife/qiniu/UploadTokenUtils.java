package com.techbelife.qiniu;

import com.alibaba.fastjson.JSONObject;
import com.techbelife.util.Base64;
import com.techbelife.util.HMACSHA1Utils;
import com.techbelife.util.PropertiesUtils;


public class UploadTokenUtils {
    private static String AK = PropertiesUtils.getProperty("qiniu.AK");
    private static String SK = PropertiesUtils.getProperty("qiniu.SK");
    private static String BUCKET = PropertiesUtils.getProperty("qiniu.bucket");
    private static final int EFFECTIVE_TIME = 3600;

    private UploadTokenUtils() {
    }

    public static String getUplpadToken(String fileName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("scope", BUCKET + ":" + fileName);
        jsonObject.put("deadline", System.currentTimeMillis() / 1000 + EFFECTIVE_TIME);
        String encodePutPolicy = getEncodePutPolicy(jsonObject);
        String encodeSign = getEncodeSign(encodePutPolicy);
        String uploadToken = AK + ":" + encodeSign + ":" + encodePutPolicy;
        return uploadToken;
    }

    public static String getEncodeSign(String encodedPutPolicy) {
        String sign = null;
        try {
            byte[] sha1 = HMACSHA1Utils.HmacSHA1Encrypt(encodedPutPolicy, SK);
            sign = Base64.safeEncodeToString(sha1);
        } catch (Exception e) {
            e.getMessage();
        }
        return sign;
    }

    public static String getEncodePutPolicy(JSONObject jsonObject) {
        return Base64.safeEncode(jsonObject.toString());
    }
}
