package com.techbelife.aliyun;

import com.techbelife.util.PropertiesUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;


public class SimpleUploader implements Runnable {
    private File file;
    private static final int retryTime = Integer.valueOf(PropertiesUtils.getProperty("retrytimes"));

    public SimpleUploader(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        try {
            upload(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void upload(File file) throws IOException, NoSuchAlgorithmException {
        InputStream is = new FileInputStream(file);
        byte[] by = new byte[is.available()];
        is.read(by);
        int time = 0;
        while (time < retryTime) {
            int status = AliyunRequest.put(by, file.getName());
            if (status == 200) {
                System.out.println(file.getName() + " upload success");
                is.close();
                return;
            }
            time++;
        }
        is.close();
        return;
    }
}
