package com.techbelife.upyun;

import com.techbelife.util.PropertiesUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class SimpleUploader implements Runnable {
    private final static int retryTime = Integer.valueOf(PropertiesUtils.getProperty("retrytimes"));

    File file;

    public SimpleUploader(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        try {
            int times = 0;
            do {
                InputStream is = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                is.read(data);
                int code = UpyunRequest.put(data, file.getName());
                if (code==200){
                    System.out.println(file.getName()+" upload success");
                    return;
                }
                times++;
            } while (times<retryTime);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
