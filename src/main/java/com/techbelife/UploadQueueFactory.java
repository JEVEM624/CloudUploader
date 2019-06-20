package com.techbelife;

import com.techbelife.Interface.UploadQueue;
import com.techbelife.aliyun.AliyunUploadQueue;
import com.techbelife.qiniu.QiniuUploadQueue;


public class UploadQueueFactory {
    public static UploadQueue getQueue(String cloud) {
        cloud = cloud.toLowerCase();
        if ("qiniu".equals(cloud)) {
            return new QiniuUploadQueue();
        }
        if ("aliyun".equals(cloud)) {
            return new AliyunUploadQueue();
        }
        return null;
    }
}
