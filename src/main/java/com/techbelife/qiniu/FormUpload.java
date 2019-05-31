package com.techbelife.qiniu;

import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;


public class FormUpload {
    private ThreadPoolExecutor threadPoolExecutor;

    public FormUpload(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void submitFile(File file) {
        FormUploader formUploader = new FormUploader(file);
        threadPoolExecutor.submit(formUploader);
    }
}
