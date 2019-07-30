package com.techbelife.upyun;

import com.techbelife.util.FileUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;


public class SliceUpload {
    ThreadPoolExecutor threadPoolExecutor;
    FileUtils fileUtils;
    public SliceUpload(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.fileUtils=new FileUtils();
    }
    public void submit(File file) {
        try {
            String uuid=UpyunRequest.initUpload(file);
            List<String> fileList = fileUtils.splitBySize(file, 1048576);
            CountDownLatch countDownLatch=new CountDownLatch(fileList.size());
            for (int i = 0; i < fileList.size(); i++) {
                String fileName=fileList.get(i);
                SliceUploader sliceUploader=new SliceUploader(fileName, countDownLatch, uuid);
                threadPoolExecutor.execute(sliceUploader);
            }
            CompleteUpload completeUpload=new CompleteUpload(countDownLatch, uuid, file.getName());
            threadPoolExecutor.execute(completeUpload);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
