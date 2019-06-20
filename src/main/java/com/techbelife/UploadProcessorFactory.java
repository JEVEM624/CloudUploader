package com.techbelife;

import com.techbelife.Interface.UploadProcessor;
import com.techbelife.aliyun.AliyunUploadProcessor;
import com.techbelife.qiniu.QiniuUploadProcessor;


public class UploadProcessorFactory {
    public static UploadProcessor getProcessor(String cloud) {
        cloud = cloud.toLowerCase();
        if ("qiniu".equals(cloud)) {
            return new QiniuUploadProcessor();
        }
        if ("aliyun".equals(cloud)) {
            return new AliyunUploadProcessor();
        }
        return null;
    }
}
