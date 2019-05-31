package com.techbelife.Interface;

import com.techbelife.util.PropertiesUtils;


public interface UploadProcessor extends Runnable {
    @Override
    void run();

    void setUploadQueue(UploadQueue queue);
}
