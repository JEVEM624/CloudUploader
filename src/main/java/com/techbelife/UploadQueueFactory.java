package com.techbelife;

import com.techbelife.Interface.UploadQueue;
import com.techbelife.qiniu.QiniuUploadQueue;


public class UploadQueueFactory {
    public static UploadQueue getQueue(String cloud) {
        cloud = cloud.toLowerCase();
        if ("qiniu".equals(cloud)) {
            return new QiniuUploadQueue();
        }
        return null;
    }
}
