package com.techbelife.aliyun;

import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;


public class SimpleUpload {
    private ThreadPoolExecutor threadPoolExecutor;

    public SimpleUpload(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void submitFile(File file) {
        threadPoolExecutor.submit(new SimpleUploader(file));
    }
}
