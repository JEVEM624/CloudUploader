package com.techbelife;

import com.techbelife.Interface.UploadProcessor;
import com.techbelife.qiniu.QiniuUploadProcessor;


public class UploadProcessorFactory {
    public static UploadProcessor getProcessor(String cloud) {
        cloud = cloud.toLowerCase();
        if ("qiniu".equals(cloud)) {
            return new QiniuUploadProcessor();
        }
        return null;
    }
}
