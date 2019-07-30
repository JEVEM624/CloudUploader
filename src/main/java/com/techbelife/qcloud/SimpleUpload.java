package com.techbelife.qcloud;

import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;


public class SimpleUpload {
    private ThreadPoolExecutor threadPoolExecutor;

    public SimpleUpload(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }
    public void submitFile(File file){
        SimpleUploader simpleUploader=new SimpleUploader(file);
        threadPoolExecutor.execute(simpleUploader);
        return;
    }
}
