package com.techbelife.qiniu;

import com.techbelife.util.Crc32;
import com.techbelife.util.PropertiesUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;


public class FormUploader implements Runnable {
    private static final int retryTime = Integer.valueOf(PropertiesUtils.getProperty("retrytimes"));
    private File file;

    public FormUploader(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        try {
            upload(file);
        } catch (IOException e) {
            System.out.println("I/O error");
        }
    }

    public void upload(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        byte[] by = new byte[is.available()];
        is.read(by);
        MultipartEntityBuilder body = MultipartEntityBuilder
                .create()
                .setCharset(Charset.forName(HTTP.UTF_8))
                .addTextBody("key", file.getName())
                .addTextBody("token", UploadTokenUtils.getUplpadToken(file.getName()))
                .addTextBody("crc32", String.valueOf(Crc32.bytes(by)))
                .addBinaryBody("file", by);
        int time = 0;
        while (time < retryTime) {
            int status = QiniuRequest.post(body);
            if (status == 200) {
                System.out.println(file.getName()+" upload success");
                is.close();
                return;
            }
        }
        is.close();
        return;
    }
}
