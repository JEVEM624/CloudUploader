package com.techbelife.Interface;

import com.techbelife.util.PropertiesUtils;


public interface FileProcessor {
    int uploadThreadNums = Integer.valueOf(PropertiesUtils.getProperty("thread.nums"));

    void uploadFile(String path);
}
