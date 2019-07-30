package com.techbelife.qcloud;

import com.techbelife.util.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;


public class SliceUpload {
    private static final int SLICE_LENGTH = 10485760;
    private static final long BIG_FILE_LENGTH = 104152956928L;
    private CountDownLatch countDownLatch;
    ThreadPoolExecutor threadPoolExecutor;
    FileUtils fileUtils = new FileUtils();

    public SliceUpload(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void submitFile(File file) {
        try {
            String uploadId = QcloudRequest.getUploadId(file.getName());
            List<String> fileList;
            //小于97G分为10M每块上传，大于97G分为10000块上传
            if (file.length() < BIG_FILE_LENGTH) {
                fileList = fileUtils.splitBySize(file, SLICE_LENGTH);
            } else {
                int sileceLength = (int) Math.ceil(file.length() / 10000);
                fileList = fileUtils.splitBySize(file, sileceLength);
            }
            Map<String,String> map=new HashMap<>(fileList.size());

            CountDownLatch countDownLatch = new CountDownLatch(fileList.size());
            for (String i : fileList) {
                SliceUploader sliceUploader=new SliceUploader(i,countDownLatch,uploadId,map);
                threadPoolExecutor.execute(sliceUploader);
            }
            CompleteMultpartUpload completeMultpartUpload=new CompleteMultpartUpload(file.getName(),countDownLatch, uploadId,map);
            threadPoolExecutor.execute(completeMultpartUpload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
