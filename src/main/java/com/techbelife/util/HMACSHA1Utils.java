package com.techbelife.util;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class HMACSHA1Utils {
    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {

        byte[] data = encryptKey.getBytes(ENCODING);

        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);

        Mac mac = Mac.getInstance(MAC_NAME);

        mac.init(secretKey);

        byte[] text = encryptText.getBytes(ENCODING);

        byte[] encode = mac.doFinal(text);

        return encode;
    }

}
