package com.techbelife.qiniu;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;


public class MakeFile implements Runnable {
    private CountDownLatch countDownLatch;
    private TreeMap treeMap;
    private File file;

    public MakeFile(CountDownLatch countDownLatch, TreeMap treeMap, File file, String token) {
        this.countDownLatch = countDownLatch;
        this.treeMap = treeMap;
        this.file = file;
    }

    @Override
    public void run() {
        try {
            countDownLatch.await();
            StringBuffer ctx = new StringBuffer();
            Iterator<Map.Entry<Integer, String>> it = treeMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, String> entry = it.next();
                ctx.append(entry.getValue());
                ctx.append(",");
            }
            ctx.deleteCharAt(ctx.length() - 1);
            String token = UploadTokenUtils.getUplpadToken(file.getName());
            if (200==QiniuRequest.mkfile(ctx.toString(), file.length(), token, file.getName())){
                System.out.println(file.getName()+" upload success");
                return;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
