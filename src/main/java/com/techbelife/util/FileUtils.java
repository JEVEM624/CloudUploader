package com.techbelife.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class FileUtils {
    private static final String tmpFolder = PropertiesUtils.getProperty("tmp") + "/";

    static {
        File folder = new File(tmpFolder);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public List<String> splitBySize(File file, int byteSize) throws IOException {
        List<String> parts = new ArrayList<>();
        int count = (int) Math.ceil(file.length() / (double) byteSize);

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(count, count * 3, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(count * 2));
        for (int i = 0; i < count; i++) {
            String partFileName = file.getName() + "." + (i + 1);
            threadPool.execute(new SplitRunnable(byteSize, i * byteSize,
                    partFileName, file));
            parts.add(partFileName);
        }

        return parts;
    }

    private class SplitRunnable implements Runnable {
        int byteSize;
        String partFileName;
        File originFile;
        int startPos;

        public SplitRunnable(int byteSize, int startPos, String partFileName,
                             File file) {
            this.startPos = startPos;
            this.byteSize = byteSize;
            this.partFileName = partFileName;
            this.originFile = file;
        }

        @Override
        public void run() {
            RandomAccessFile randomAccessFile;
            OutputStream outputStream;
            try {
                randomAccessFile = new RandomAccessFile(originFile, "r");
                byte[] b = new byte[byteSize];
                randomAccessFile.seek(startPos);
                int s = randomAccessFile.read(b);
                outputStream = new FileOutputStream(tmpFolder + partFileName);
                outputStream.write(b, 0, s);
                outputStream.flush();
                outputStream.close();
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
