package com.meishe.sdkdemo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常捕获
 * Created by ms on 2018/9/18.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    // 存储异常和参数信息
    private Map<String, String> paramsMap = new HashMap<>();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private String TAG = this.getClass().getSimpleName();
    private static CrashHandler mInstance;

    private CrashHandler() {

    }

    public static synchronized CrashHandler getInstance() {
        if (null == mInstance) {
            mInstance = new CrashHandler();
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(thread, ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "uncaughtException: " + e.getMessage());
            }
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private boolean handleException(Thread thread, Throwable ex) {
        if (ex == null) {
            return false;
        }

        paramsMap.put("thread name", thread.getName());

        //收集设备参数信息
        collectDeviceInfo(mContext);

        //保存日志文件
        saveCrashInfo2File(ex);

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "程序发生异常！", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        return true;
    }

    // 收集设备参数信息
    public void collectDeviceInfo(Context ctx) {
        //获取versionName,versionCode
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                paramsMap.put("versionName", versionName);
                paramsMap.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "collectDeviceInfo: an error occured when collect package info", e);
        }
        //获取所有系统信息
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                paramsMap.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "collectDeviceInfo: an error occured when collect crash info", e);
            }
        }
    }

    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = format.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = PathUtils.getLogDir() + File.separator;
                Log.d(TAG, "saveCrashInfo2File: " + path);
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            Log.e(TAG, sb.toString());
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "saveCrashInfo2File: an error occured while writing file...", e);
        }
        return null;
    }
}

