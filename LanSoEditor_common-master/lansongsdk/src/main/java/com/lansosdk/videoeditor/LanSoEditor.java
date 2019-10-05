package com.lansosdk.videoeditor;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.util.Calendar;

public class LanSoEditor {

    private static boolean isLoaded = false;

    public static void initSDK(Context context, String str) {
        loadLibraries(); // 拿出来单独加载库文件.


        setLanSongSDK1();
        initSo(context, str);
        LanSoEditor.setTempFileDir(Environment.getExternalStorageDirectory().getPath() + "/lansongBox/");


        printSDKVersion();
    }

    public static void unInitSDK(){
        unInitSo();
    }
    /**
     * 设置默认产生文件的文件夹,
     * 默认是:/sdcard/lansongBox/
     * @param tmpDir  设置临时文件夹的完整路径
     */
    public static void setTempFileDir(String tmpDir) {
        LanSongFileUtil.FileCacheDir = tmpDir;
    }

    /**
     * 设置临时文件夹的路径
     * 并设置文件名的前缀和后缀 我们默认是以当前时间年月日时分秒毫秒:yymmddhhmmss_ms为当前文件名字.
     * 你可以给这个名字增加一个前缀和后缀.比如xiaomi5_yymmddhhmmss_ms_version54.mp4等.
     * @param tmpDir  设置临时文件夹的完整路径
     * @param prefix  设置文件的前缀
     * @param subfix  设置文件的后缀.
     */
    public static void setTempFileDir(String tmpDir,String prefix,String subfix) {
        if(tmpDir!=null && prefix!=null && subfix!=null){

            if (!tmpDir.endsWith("/")) {
                tmpDir += "/";
            }

            LanSongFileUtil.FileCacheDir = tmpDir;
            LanSongFileUtil.mTmpFilePreFix=prefix;
            LanSongFileUtil.mTmpFileSubFix=subfix;
        }
    }


    //----------------------------------------------------------------------------------------
    private static void printSDKVersion()
    {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;

        String nativeVersion="* \tnative version:"+VideoEditor.getSDKVersion()+ " ;  ABI: "+VideoEditor.getCurrentNativeABI()+ " ; type:"+VideoEditor.getLanSongSDKType()
                + "; Limited time: year:"+VideoEditor.getLimitYear()+ " month:" +VideoEditor.getLimitMonth();

        String deviceInfo="* \tSystem Time is:Year:"+year+ " Month:"+month + " Build.MODEL:--->" + Build.MODEL+"<---VERSION:"+getAndroidVersion();


        LSOLog.i("********************LanSongSDK**********************");
        LSOLog.i("* ");
        LSOLog.i(deviceInfo);
        LSOLog.i(nativeVersion);
        LSOLog.i("* ");
        LSOLog.i("*************************************************************");
    }
    private static String getAndroidVersion(){
        switch (Build.VERSION.SDK_INT){
            case 28:
                return "Androdi-9.0";
            case 27:
                return "Androdi-8.1";
            case 26:
                return "Androdi-8.0";
            case 25:
                return "Androdi-7.1.1";
            case 24:
                return "Androdi-7.0";
            case 23:
                return "Androdi-6.0";
            case 22:
                return "Androdi-5.1";
            case 21:
                return "Androdi-5.0";
            case 20:
                return "Androdi-4.4W";
            case 19:
                return "Androdi-4.4";
            case 18:
                return "Androdi-4.3";
            default:
                return "unknow-API="+Build.VERSION.SDK_INT;
        }
    }
    private static synchronized void loadLibraries() {
        if (isLoaded)
            return;


        System.loadLibrary("LanSongffmpeg");
        System.loadLibrary("LanSongdisplay");
        System.loadLibrary("LanSongplayer");

        isLoaded = true;
    }

    private static void initSo(Context context, String str) {
        nativeInit(context, context.getAssets(), str);
    }
    private static void unInitSo() {
        nativeUninit();
    }




    private static native void nativeInit(Context ctx, AssetManager ass,String filename);
    private static native void nativeUninit();
    ////LSTODO 特定用户使用, 发布删除;
    private static native void setLanSongSDK1();
}
