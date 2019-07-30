package com.techbelife.upyun;

import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;


public class SimpleUpload {

    ThreadPoolExecutor threadPoolExecutor;

    public SimpleUpload(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }
    public void submit(File file){
        SimpleUploader uploader=new SimpleUploader(file);
        threadPoolExecutor.execute(uploader);
    }
}
