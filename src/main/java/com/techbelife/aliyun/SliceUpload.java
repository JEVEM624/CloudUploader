package com.techbelife.aliyun;

import com.techbelife.Exception.AuthException;
import com.techbelife.util.FileUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;


public class SliceUpload {
    private static final int SLICE_LENGTH = 10485760;
    private static final long BIG_FILE_LENGTH = 10485760000L;
    ThreadPoolExecutor threadPoolExecutor;
    FileUtils fileUtils = new FileUtils();

    public SliceUpload(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void submitFile(File file) {
        try {
            String uploadid = InitMultPartUpload(file);
            List<String> fileList;
            if (file.length() < BIG_FILE_LENGTH) {
                fileList = fileUtils.splitBySize(file, SLICE_LENGTH);
            } else {
                int sileceLength = (int) Math.ceil(file.length() / 10000);
                fileList = fileUtils.splitBySize(file, sileceLength);
            }
            HashMap<Integer, String> eTags = new HashMap<>(fileList.size());
            CountDownLatch countDownLatch = new CountDownLatch(fileList.size());
            for (String i : fileList) {
                SliceUploader sliceUploader = new SliceUploader(i, uploadid, countDownLatch, eTags);
                threadPoolExecutor.execute(sliceUploader);
            }
            CompleteMultpartUpload completeMultpartUpload = new CompleteMultpartUpload(file.getName(), uploadid, countDownLatch, eTags);
            threadPoolExecutor.execute(completeMultpartUpload);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthException e) {
            e.printStackTrace();
        }


    }

    private String InitMultPartUpload(File file) throws IOException, ParserConfigurationException, SAXException, AuthException {
        return AliyunRequest.getUploadId(file.getName());
    }
}
