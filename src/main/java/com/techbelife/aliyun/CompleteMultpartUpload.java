package com.techbelife.aliyun;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


public class CompleteMultpartUpload implements Runnable {
    private String uploadId;
    private CountDownLatch countDownLatch;
    private HashMap<Integer, String> etags;
    private String fileName;

    public CompleteMultpartUpload(String fileName, String uploadId, CountDownLatch countDownLatch, HashMap<Integer, String> etags) {
        this.uploadId = uploadId;
        this.countDownLatch = countDownLatch;
        this.etags = etags;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            countDownLatch.await();
            StringBuilder sb = new StringBuilder();
            sb.append("<CompleteMultipartUpload>");
            for (Map.Entry<Integer, String> entry : etags.entrySet()) {
                sb.append("<Part><PartNumber>");
                sb.append(entry.getKey());
                sb.append("</PartNumber><ETag>");
                sb.append(entry.getValue());
                sb.append("</ETag></Part>");
            }
            sb.append("</CompleteMultipartUpload>");
            try {
                AliyunRequest.completeMultipartUpload(fileName, sb.toString(), uploadId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {

        }

    }
}
