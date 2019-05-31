package com.techbelife.qiniu;

import com.techbelife.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;


public class SliceUpload {
    private ThreadPoolExecutor threadPoolExecutor;
    private static final int BLOCK_SIZE = 4194304;
    private FileUtils fileUtils = new FileUtils();

    public SliceUpload(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void submitFile(File file) {
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        try {
            List<String> fileList = fileUtils.splitBySize(file, BLOCK_SIZE);
            CountDownLatch countDownLatch = new CountDownLatch(fileList.size());
            String token = UploadTokenUtils.getUplpadToken(file.getName());
            for (int i = 0; i < fileList.size(); i++) {
                BlockUploader blockUploader = new BlockUploader(countDownLatch, treeMap, fileList.get(i));
                threadPoolExecutor.submit(blockUploader);
            }
            MakeFile makeFile = new MakeFile(countDownLatch, treeMap, file, token);
            threadPoolExecutor.submit(makeFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
