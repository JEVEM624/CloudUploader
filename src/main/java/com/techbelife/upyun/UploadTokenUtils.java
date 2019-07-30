package com.techbelife.upyun;

import com.techbelife.util.Base64;
import com.techbelife.util.HMACSHA1Utils;
import com.techbelife.util.MD5Util;
import com.techbelife.util.PropertiesUtils;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;


public class UploadTokenUtils {

    private final static String PASSWORD= PropertiesUtils.getProperty("upyun.password");
    private final static String OPERATOR=PropertiesUtils.getProperty("upyun.operator");
    public static String getUploadToken(HttpEntityEnclosingRequestBase request)throws Exception {
        String md5= MD5Util.md5String(PASSWORD);
        String text=request.getMethod()
                +"&"+request.getURI().getPath()
                +"&"+request.getFirstHeader("Date").getValue()
                +"&"+request.getFirstHeader("Content-MD5").getValue();

        byte[]sha1=HMACSHA1Utils.HmacSHA1Encrypt(text, md5);

        String signature= Base64.EncodeToString(sha1);

        return "UPYUN "+OPERATOR+":"+signature;
    }
}
