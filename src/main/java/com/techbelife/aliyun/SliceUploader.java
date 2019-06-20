package com.techbelife.aliyun;

import com.techbelife.util.PropertiesUtils;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;


public class SliceUploader implements Runnable {
    private String uploadId;
    private String fileName;
    private CountDownLatch countDownLatch;
    private HashMap eTags;
    private static final String TMP_FLODER = PropertiesUtils.getProperty("tmp") + "\\";


    public SliceUploader(String fileName, String uploadId, CountDownLatch countDownLatch, HashMap eTags) {
        this.fileName = fileName;
        this.uploadId = uploadId;
        this.countDownLatch = countDownLatch;
        this.eTags = eTags;

    }

    @Override
    public void run() {
        try {
            File file = null;
            while (file == null) {
                file = new File(TMP_FLODER + fileName);
            }
            int seq = Integer.valueOf(fileName.substring(fileName.lastIndexOf(".") + 1));
            String fullFileName = fileName.substring(0, fileName.lastIndexOf("."));
            String etag = AliyunRequest.put(fullFileName, file, seq, uploadId);
            eTags.put(seq, etag);
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();
    }


}
