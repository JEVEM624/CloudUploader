package com.techbelife.upyun;

import com.techbelife.Interface.FileProcessor;
import com.techbelife.Interface.UploadProcessor;
import com.techbelife.Interface.UploadQueue;



public class UpyunUploadProcessor implements UploadProcessor {
    private UploadQueue fileQueue;
    private FileProcessor fileProcessor;

    public UpyunUploadProcessor() {
        fileProcessor = new UpyunFileProcessor();
    }

    @Override
    public void run() {
        while (true) {
            String path = fileQueue.get();
            fileProcessor.uploadFile(path);
        }
    }

    @Override
    public void setUploadQueue(UploadQueue queue) {
        this.fileQueue = queue;
    }
}
