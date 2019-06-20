package com.techbelife.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesUtils {

    private static Properties props;
    private static boolean dev = false;

    static {
        loadProps();
    }

    synchronized static private void loadProps() {
        System.out.println("Start profile file content");
        props = new Properties();
        InputStream in = null;
        try {
            if (dev) {
                in = PropertiesUtils.class.getClassLoader().getResourceAsStream("dev.properties");
            } else {
                in = PropertiesUtils.class.getClassLoader().getResourceAsStream("CloudUploader.properties");
            }
            props.load(in);
        } catch (FileNotFoundException e) {
            System.out.println("Profile not found");
        } catch (IOException e) {
            System.out.println("IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                System.out.println("File flow closes abnormally");
            }
        }
        System.out.println("Configuration file loading is complete");
    }

    public static String getProperty(String key) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }

}