package com.meishe.sdkdemo.utils;

import android.util.Log;

import com.meishe.sdkdemo.BuildConfig;

/**
 * Created by CaoZhiChao on 2018/8/30 17:52
 */
public class Logger {

    private static final String TAG = "Logger";

    public static int LOGLEVEL = BuildConfig.DEBUG ? 5 : 0;
    public static boolean VERBOSE = LOGLEVEL > 4;
    public static boolean DEBUG = LOGLEVEL > 3;
    public static boolean INFO = LOGLEVEL > 2;
    public static boolean WARN = LOGLEVEL > 1;
    public static boolean ERROR = LOGLEVEL > 0;

    public static void setDebugMode(boolean debugMode) {
        LOGLEVEL = debugMode ? 5 : 0;
        VERBOSE = LOGLEVEL > 4;
        DEBUG = LOGLEVEL > 3;
        INFO = LOGLEVEL > 2;
        WARN = LOGLEVEL > 1;
        ERROR = LOGLEVEL > 0;
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg == null ? "" : msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.d(tag, msg == null ? "" : msg, tr);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg == null ? "" : msg);
        }
    }

    public static void d(String msg, Throwable tr) {
        if (DEBUG) {
            Log.d(TAG, msg == null ? "" : msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (ERROR) {
            Log.e(tag, msg == null ? "" : msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (ERROR) {
            Log.e(tag, msg == null ? "" : msg, tr);
        }
    }

    public static void e(String msg) {
        if (ERROR) {
            Log.e(TAG, msg == null ? "" : msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (ERROR) {
            Log.e(TAG, msg == null ? "" : msg, tr);
        }

    }
}
