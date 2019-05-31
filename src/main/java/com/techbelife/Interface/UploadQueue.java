package com.techbelife.Interface;


import java.util.List;


public interface UploadQueue {
    void add(String path);

    String get();

    void add(List<String> paths);
}
