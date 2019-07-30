package com.techbelife.qcloud;

import com.techbelife.util.PropertiesUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class SimpleUploader implements Runnable {
    private File file;
    private static final int retryTime = Integer.valueOf(PropertiesUtils.getProperty("retrytimes"));

    public SimpleUploader(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        try {
            upload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upload() throws IOException {
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        is.read(bytes);
        int time = 0;
        while (time < retryTime) {
            int code = QcloudRequest.put(bytes, file.getName());
            if (code == 200) {
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
