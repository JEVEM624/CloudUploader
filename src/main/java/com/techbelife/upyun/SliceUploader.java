package com.techbelife.upyun;

import com.techbelife.util.PropertiesUtils;

import java.io.*;
import java.util.concurrent.CountDownLatch;


public class SliceUploader implements Runnable {

    private static final String TMP_PATH = PropertiesUtils.getProperty("tmp");
    String fileName;
    CountDownLatch countDownLatch;
    String uuid;

    public SliceUploader(String fileName, CountDownLatch countDownLatch, String uuid) {
        this.fileName = fileName;
        this.countDownLatch = countDownLatch;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        try {
            String path = TMP_PATH + "/" + fileName;
            String fullFileName = fileName.substring(0, fileName.lastIndexOf("."));
            int seq = Integer.valueOf(fileName.substring(fullFileName.length() + 1))-1;
            int code = 0;
            File file = new File(path);
            byte[] data = new byte[(int) file.length()];
            InputStream is = new FileInputStream(file);
            is.read(data);
            while (code != 204) {
                code = UpyunRequest.put(fullFileName, uuid, seq, data);
            }
            is.close();
            file.delete();
            countDownLatch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
