package com.lansosdk.videoeditor;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


public class LSOLog {


    private static String TAG="LanSongSDK";
    public static void i(String var2){
        Log.i(TAG,var2);
    }
    public static void d(String var2){
                Log.d(TAG,var2);
    }
    public static void w(String var2){
            Log.w(TAG,var2);
    }
    public static void e(String var2){
            Log.e(TAG,var2);
    }
    public static void e(String msg, Throwable tr) {
            Log.e(TAG,msg,tr);
    }

}
