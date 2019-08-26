package com.meishe.sdkdemo.edit.watermark;

import android.util.Log;

import com.meishe.sdkdemo.utils.PathUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by CaoZhiChao on 2018/10/18 14:14
 */
public class FileUtil {
    private static final String TAG = "FileUtil";
    private static String waterMarkCacheFileName = "/cache.txt";

    /*
     * Java文件操作 获取文件扩展名
     * */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /*
     * Java文件操作 获取文件名
     * */
    public static String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }

    /*
     * Java文件操作 获取除了文件名以外的部分
     * */
    public static String getFilePathExceptName(String pathandname) {
        int end = pathandname.lastIndexOf("/");
        int start = 0;
        if (end != -1) {
            return pathandname.substring(start, end);
        } else {
            return null;
        }
    }

    public static String readWaterMarkCacheFile() {
        String path = PathUtils.getWatermarkCafDirectoryDir();
        if (path == null) {
            Log.d(TAG, "水印 path is null!");
            return null;
        }
        File file = new File(path + waterMarkCacheFileName);
        if (!file.exists()) {
            Log.i(TAG, "水印草稿文件不存在！！！");
            return null;
        }
        try {
            FileInputStream in = new FileInputStream(file);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeWaterMarkCacheFile(String message) {
        String path = PathUtils.getWatermarkCafDirectoryDir();
        if (path == null) {
            Log.d(TAG, "水印 path is null!");
        }
        File file = new File(path + waterMarkCacheFileName);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(message.getBytes());
            fileOutputStream.close();
            Log.i(TAG, "水印草稿保存成功！！！");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "草稿文件创建失败！！！");
        }
    }
}
