package com.techbelife;



public class Main {
    public static void main(String[] args) {
        //example
        CloudUploader cloudUploader = CloudUploader.getInstance();
        cloudUploader.put("/example.jpg");
        cloudUploader.put("/example.txt");
        cloudUploader.put("/example.zip");
        cloudUploader.put("/example/");
    }
}
