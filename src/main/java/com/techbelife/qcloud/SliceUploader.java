package com.techbelife.qcloud;

import com.techbelife.util.MD5Util;
import com.techbelife.util.PropertiesUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


public class SliceUploader implements Runnable {
    private static final String TMP_PATH = PropertiesUtils.getProperty("tmp")+"\\";
    private String fileName;
    private CountDownLatch countDownLatch;
    private String uploadId;
    private Map map;
    public SliceUploader(String fileName, CountDownLatch countDownLatch, String uploadId, Map<String, String> map) {
        this.fileName = fileName;
        this.countDownLatch = countDownLatch;
        this.uploadId = uploadId;
        this.map=map;
    }

    @Override
    public void run() {
        try {
            File file = new File(TMP_PATH + fileName);
            InputStream is = new FileInputStream(file);
            String seq = fileName.substring(fileName.lastIndexOf(".") + 1);
            String fullFileName = fileName.substring(0, fileName.lastIndexOf("."));
            byte[] data = new byte[is.available()];
            is.read(data);
            String md5= MD5Util.md5String(data);
            map.put(seq, md5);
            QcloudRequest.uploadPart(data, fullFileName, seq, uploadId);
            is.close();
            file.delete();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        countDownLatch.countDown();
    }
}
