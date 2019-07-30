package com.techbelife.upyun;

import com.techbelife.Interface.FileProcessor;


import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class UpyunFileProcessor implements FileProcessor {
    private static final long MAX_SIMPLE_UPLOAD_SIZE = 10485760;
    private ThreadPoolExecutor threadPoolExecutor;
    private SimpleUpload simpleUpload;
    private SliceUpload sliceUpload;

    public UpyunFileProcessor() {
        threadPoolExecutor = new ThreadPoolExecutor(uploadThreadNums, uploadThreadNums, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        simpleUpload = new SimpleUpload(threadPoolExecutor);
        sliceUpload = new SliceUpload(threadPoolExecutor);
    }

    @Override
    public void uploadFile(String path) {
        File file = new File(path);
        if (file.length() < MAX_SIMPLE_UPLOAD_SIZE) {
            simpleUpload.submit(file);
        } else {
            sliceUpload.submit(file);
        }
    }
}
