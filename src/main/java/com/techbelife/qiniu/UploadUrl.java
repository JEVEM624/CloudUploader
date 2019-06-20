package com.techbelife.qiniu;


public class UploadUrl {
    public static final String z0 = "https://up.qiniup.com";
    public static final String z1 = "https://up-z1.qiniup.com";
    public static final String z2 = "https://up-z2.qiniup.com";
    public static final String na0 = "https://up-na0.qiniup.com";
    public static final String as0 = "https://up-as0.qiniup.com";

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
