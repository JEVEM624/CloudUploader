package com.techbelife.qiniu;

import com.alibaba.fastjson.JSONObject;
import com.techbelife.util.Crc32;
import com.techbelife.util.PropertiesUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;


public class BlockUploader implements Runnable {
    private static final String TMP_FLODER = PropertiesUtils.getProperty("tmp") + "\\";
    private static final int SLICE_LENGTH = 1048576;
    private static final String OK = "200";
    TreeMap<Integer, String> treeMap;
    String uploadFileName;
    String fileName;
    CountDownLatch countDownLatch;

    @Override
    public void run() {
        try {
            File file = new File(TMP_FLODER + uploadFileName);
            int seq = Integer.valueOf(uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1));
            FileInputStream is = new FileInputStream(file);
            byte[] bytes = new byte[SLICE_LENGTH];
            int length = is.read(bytes);
            int offset = length;
            String[] next = null;
            if (offset != -1) {
                if (offset < SLICE_LENGTH) {
                    byte[] b = new byte[length];
                    System.arraycopy(bytes, 0, b, 0, length - 1);
                    bytes = b;
                }
                next = mkblk(bytes, file.length());
            }
            while ((length = is.read(bytes)) != -1) {
                if (length < SLICE_LENGTH) {
                    byte[] b = new byte[length];
                    System.arraycopy(bytes, 0, b, 0, length - 1);
                    bytes = b;
                }
                next = updateSlice(next[0], next[1], bytes, offset);
                offset = offset + length;
            }
            treeMap.put(seq, next[0]);
            is.close();
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }

    }

    public BlockUploader(CountDownLatch countDownLatch, TreeMap<Integer, String> treeMap, String uploadFileName) {
        this.treeMap = treeMap;
        this.countDownLatch = countDownLatch;
        this.uploadFileName = uploadFileName;
        String[] tmp = uploadFileName.split("\\.");

        for (int i = 0; i < tmp.length - 2; i++) {
            this.fileName = this.fileName + tmp[i] + ".";
        }
        this.fileName = this.fileName + tmp[tmp.length - 2];
    }

    private String[] updateSlice(String ctx, String host, byte[] bytes, int offset) throws IOException {
        Map map = null;
        String token = UploadTokenUtils.getUplpadToken(fileName);
        map = QiniuRequest.bput(ctx, host, offset, token, bytes);
        JSONObject jsonObject = JSONObject.parseObject((String) map.get("body"));
        long crc32 = Crc32.bytes(bytes);
        while (!OK.equals(map.get("status")) || !String.valueOf(crc32).equals(jsonObject.getString("crc32"))) {

            map = QiniuRequest.bput(ctx, host, offset, token, bytes);
            jsonObject = JSONObject.parseObject((String) map.get("body"));
        }
        String[] res = new String[2];
        res[0] = jsonObject.getString("ctx");
        res[1] = jsonObject.getString("host");
        return res;
    }

    private String[] mkblk(byte[] bytes, long blockLength) throws IOException {

        Map map = null;
        String token = UploadTokenUtils.getUplpadToken(fileName);
        map = QiniuRequest.mkblk(bytes, blockLength, token);
        JSONObject jsonObject = JSONObject.parseObject((String) map.get("body"));
        long crc32 = Crc32.bytes(bytes);
        while (!OK.equals(map.get("status")) || !String.valueOf(crc32).equals(jsonObject.getString("crc32"))) {
            map = QiniuRequest.mkblk(bytes,
                    blockLength, token);
            jsonObject = JSONObject.parseObject((String) map.get("body"));
        }
        String[] res = new String[2];
        res[0] = jsonObject.getString("ctx");
        res[1] = jsonObject.getString("host");
        return res;
    }

}
