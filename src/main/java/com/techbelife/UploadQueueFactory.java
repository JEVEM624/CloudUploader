package com.techbelife;

import com.techbelife.Interface.UploadQueue;
import com.techbelife.aliyun.AliyunUploadQueue;
import com.techbelife.qcloud.QcloudUploadQueue;
import com.techbelife.qiniu.QiniuUploadQueue;
import com.techbelife.upyun.UpyunUploadQueue;


public class UploadQueueFactory {
    public static UploadQueue getQueue(String cloud) {
        cloud = cloud.toLowerCase();
        if ("qiniu".equals(cloud)) {
            return new QiniuUploadQueue();
        }
        if ("aliyun".equals(cloud)) {
            return new AliyunUploadQueue();
        }
        if("qcloud".equals(cloud)){
            return new QcloudUploadQueue();
        }
        if ("upyun".equals(cloud)){
            return new UpyunUploadQueue();
        }
        return null;
    }
}
