package com.techbelife;

import com.techbelife.Interface.UploadProcessor;
import com.techbelife.aliyun.AliyunUploadProcessor;
import com.techbelife.qcloud.QcloudUploadProcessor;
import com.techbelife.qiniu.QiniuUploadProcessor;
import com.techbelife.upyun.UpyunUploadProcessor;


public class UploadProcessorFactory {
    public static UploadProcessor getProcessor(String cloud) {
        cloud = cloud.toLowerCase();
        if ("qiniu".equals(cloud)) {
            return new QiniuUploadProcessor();
        }
        if ("aliyun".equals(cloud)) {
            return new AliyunUploadProcessor();
        }
        if ("qcloud".equals(cloud)){
            return new QcloudUploadProcessor();
        }
        if ("upyun".equals(cloud)){
            return new UpyunUploadProcessor();
        }
        return null;
    }
}
