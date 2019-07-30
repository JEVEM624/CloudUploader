package com.techbelife.upyun;

import java.util.concurrent.CountDownLatch;


public class CompleteUpload implements Runnable {
    CountDownLatch countDownLatch;
    String uuid;
    String fullFileName;

    public CompleteUpload(CountDownLatch countDownLatch, String uuid, String fullFileName) {
        this.countDownLatch = countDownLatch;
        this.uuid = uuid;
        this.fullFileName = fullFileName;
    }

    @Override
    public void run() {
        try {
            countDownLatch.await();
            UpyunRequest.completeUpload(fullFileName, uuid);
            System.out.println(fullFileName+" upload success");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
