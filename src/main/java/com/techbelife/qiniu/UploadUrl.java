package com.techbelife.qiniu;


public class UploadUrl {
    public static final String z0 = "http://up.qiniup.com";
    public static final String z1 = "http://up-z1.qiniup.com";
    public static final String z2 = "http://up-z2.qiniup.com";
    public static final String na0 = "http://up-na0.qiniup.com";
    public static final String as0 = "http://up-as0.qiniup.com";

    public static String getUri(String region) {
        switch (region) {
            case "z0":
                return z0;
            case "z1":
                return z1;
            case "z2":
                return z2;
            case "na0":
                return na0;
            case "as0":
                return as0;
        }
        return "";
    }

    private UploadUrl() {

    }

}
