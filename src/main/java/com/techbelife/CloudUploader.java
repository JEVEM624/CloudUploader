package com.techbelife;

import com.techbelife.Interface.UploadProcessor;
import com.techbelife.Interface.UploadQueue;
import com.techbelife.util.PropertiesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class CloudUploader {

    List<UploadQueue> queues = new ArrayList<>();

    private CloudUploader() {
        String list = PropertiesUtils.getProperty("cloud");
        String[] clouds = list.split(",");
        for (String cloud : clouds) {
            UploadQueue queue = UploadQueueFactory.getQueue(cloud);
            if (queue != null) {
                UploadProcessor uploadProcessor = UploadProcessorFactory.getProcessor(cloud);
                uploadProcessor.setUploadQueue(queue);
                new Thread(uploadProcessor).start();
                queues.add(queue);
            } else {
                System.out.println(cloud + "Loading error");
            }
        }
    }

    private static class holder {
        private static CloudUploader INSTANCE = new CloudUploader();
    }

    public static CloudUploader getInstance() {
        return holder.INSTANCE;
    }

    public void put(String path) {
        File file=new File(path);
        if (file.isDirectory()) {
            LinkedList<File> directoryList = new LinkedList<>();
            directoryList.add(file);
            while (!directoryList.isEmpty()) {
                File[] files = directoryList.removeFirst().listFiles();
                for (File tmpFile : files) {
                    if (tmpFile.isDirectory()) {
                        directoryList.add(tmpFile.getAbsoluteFile());
                    } else {
                        for (UploadQueue queue : queues) {
                            queue.add(tmpFile.getPath());
                        }
                    }
                }
            }
        } else {
            for (UploadQueue queue : queues) {
                queue.add(path);
            }
        }


    }

    public void put(List<String> paths) {
        for (UploadQueue queue : queues) {
            queue.add(paths);
        }
    }
}
