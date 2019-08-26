package com.meishe.sdkdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.meishe.sdkdemo.MSApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by ms on 2018/7/20.
 */
public class MediaScannerUtil {
    private static final String TAG = "MediaScannerUtil";
    private static final MediaScannerClient client = new MediaScannerClient();
    private static MediaScannerConnection mediaScanConn = new MediaScannerConnection(MSApplication.getmContext().getApplicationContext(), client);
    private static List<MediaScannerCallBack> callBackList = new ArrayList<>();

    private static final Queue<Entity> sPendingScanList = new ConcurrentLinkedQueue<>();

    private static class MediaScannerClient implements MediaScannerConnection.MediaScannerConnectionClient {
        public void onMediaScannerConnected() {
            scanOrDisconnect();
        }

        public void onScanCompleted(String path, Uri uri) {
            scanOrDisconnect();
        }
    }

    private static void scanOrDisconnect() {
        Entity entity = sPendingScanList.poll();
        if (entity != null) {
            Log.e(TAG, String.format("scanFile, path =-> %s", entity.path));
            mediaScanConn.scanFile(entity.path, entity.type);
        } else {
            mediaScanConn.disconnect();
            Log.e(TAG, String.format("onScanCompleted and disconnect"));
        }
    }

    /**
     * 扫描文件标签信息
     *
     * @param filePath 文件路径
     * @param fileType 文件类型
     */

    public static void scanFile(String filePath, String fileType) {
        if(filePath == null || filePath.isEmpty()) {
            return;
        }
        scan(new Entity(filePath, fileType));
    }

    public static void scan(Entity entity) {
        sPendingScanList.add(entity);
        mediaScanConn.connect();
    }

    public static void scan(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        scan(new File(path));
    }

    public static void scan(File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (null != files && files.length > 0) {
                    for (File f : files) {
                        scan(f);
                    }
                }
            } else {
                scan(new Entity(file.getAbsolutePath()));
            }
        }
    }

    public static void unScanFile() {
        //断开会话
        MediaScannerUtil.mediaScanConn.disconnect();
    }

    public static void addCallBack(MediaScannerCallBack callBack) {
        if (callBack == null) {
            return;
        }

        MediaScannerUtil.callBackList.add(callBack);
    }

    public static void removeCallBack(MediaScannerCallBack callBack) {
        if (callBack == null) {
            return;
        }

        MediaScannerUtil.callBackList.remove(callBack);
    }


    public static void scanFileAsync(Context ctx, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        ctx.sendBroadcast(scanIntent);
    }

    public static class Entity {
        String path;
        String type;

        public Entity(String path, String type) {
            this.path = path;
            this.type = type;
        }

        public Entity(String path) {
            this.path = path;
        }
    }

    public static abstract class MediaScannerCallBack {

        public abstract void onScanCompleted();

    }
}
