package com.techbelife.qcloud;

import com.techbelife.Interface.UploadQueue;

import java.io.File;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public class QcloudUploadQueue implements UploadQueue {
    private Queue<String> fileQueue = new LinkedBlockingQueue<>();

    @Override
    public void add(String path) {
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("file does not exist");
            return;
        }
        fileQueue.add(path);
    }

    @Override
    public String get() {
        while (fileQueue.isEmpty()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println(e.getStackTrace());
            }
        }
        return fileQueue.poll();
    }

    @Override
    public void add(List<String> paths) {
        for (String s : paths) {
            add(s);
        }
    }
}
