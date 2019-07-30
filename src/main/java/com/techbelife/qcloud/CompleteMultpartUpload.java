package com.techbelife.qcloud;

import java.util.Map;
import java.util.concurrent.CountDownLatch;


public class CompleteMultpartUpload implements Runnable {
    private final static int OK=200;
    private CountDownLatch countDownLatch;
    private String uploadId;
    private Map<String,String> map;
    private String fileName;

    public CompleteMultpartUpload(String name, CountDownLatch countDownLatch, String uploadId, Map<String, String> map) {
        this.countDownLatch = countDownLatch;
        this.uploadId = uploadId;
        this.map=map;
        this.fileName=name;
    }

    @Override
    public void run() {
        try {
            countDownLatch.await();
            StringBuffer sb=new StringBuffer();
            sb.append("<CompleteMultipartUpload>");
            for (Map.Entry<String,String> entry:map.entrySet()){
                sb.append("<Part><PartNumber>");
                sb.append(Integer.valueOf(entry.getKey()));
                sb.append("</PartNumber>");
                sb.append("<ETag>");
                sb.append(entry.getValue());
                sb.append("</ETag></Part>");
            }
            sb.append("</CompleteMultipartUpload>");
            int code=QcloudRequest.completeMultipartUpload(fileName,sb.toString(),uploadId);
            if (code==OK){
                System.out.println(fileName+" upload success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
