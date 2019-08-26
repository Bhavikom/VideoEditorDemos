// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import java.util.HashMap;
import android.os.Environment;
//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
//import org.lasque.tusdk.core.seles.SelesContext;
import android.util.Log;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;

import java.util.LinkedList;
import java.util.Map;
import java.io.FileOutputStream;

public class TLog
{
    private static volatile boolean a;
    private static volatile String b;
    public static boolean LOG_AUDIO_DECODEC_INFO;
    public static boolean LOG_AUDIO_ENCODEC_INFO;
    public static boolean LOG_VIDEO_DECODEC_INFO;
    public static boolean LOG_VIDEO_ENCODEC_INFO;
    public static boolean LOG_MEDIA_MUXER_INFO;
    public static boolean LOG_CURRENT_FPS;
    public static boolean LOG_TO_GLOBAL_FILE;
    private static FileOutputStream c;
    private static Object d;
    private static String e;
    private static Map<String, LinkedList<Long>> f;
    
    public static void enableLogging(final String b) {
        TLog.b = b;
        TLog.a = true;
    }
    
    public static void disableLogging() {
        TLog.a = false;
    }
    
    public static void enableLog2File(final boolean log_TO_GLOBAL_FILE) {
        TLog.LOG_TO_GLOBAL_FILE = log_TO_GLOBAL_FILE;
    }
    
    public static void d(final String s, final Object... array) {
        a(3, null, s, array);
    }
    
    public static void i(final String s, final Object... array) {
        a(4, null, s, array);
    }
    
    public static void w(final String s, final Object... array) {
        a(5, null, s, array);
    }
    
    public static void e(final Throwable t) {
        a(6, t, null, new Object[0]);
    }
    
    public static void e(final String s, final Object... array) {
        a(6, null, s, array);
    }
    
    public static void e(final Throwable t, final String s, final Object... array) {
        a(6, t, s, array);
    }
    
    public static void dump(final int i, String format, final Object... args) {
        if (!TLog.LOG_TO_GLOBAL_FILE) {
            return;
        }
        synchronized (TLog.d) {
            if (!TLog.LOG_TO_GLOBAL_FILE) {
                return;
            }
            if (args.length > 0) {
                format = String.format(format, args);
            }
            final String string = " " + i + " :  " + format + "\n";
            try {
                a().write(StringHelper.timeStampString().getBytes());
                a().write(string.getBytes());
            }
            catch (Exception ex) {
                ex.printStackTrace();
                e("write log filed !!!", new Object[0]);
            }
        }
    }
    
    public static void dump(final String s, final Object... array) {
        dump(3, s, array);
    }
    
    private static void a(final int n, final Throwable t, String format, final Object... args) {
        if (!TLog.a) {
            return;
        }
        if (args.length > 0) {
            format = String.format(format, args);
        }
        String format2;
        if (t == null) {
            format2 = format;
        }
        else {
            format2 = String.format("%1$s\n%2$s", (format == null) ? t.getMessage() : format, Log.getStackTraceString(t));
        }
        dump(n, format, args);
        Log.println(n, TLog.b, format2);
    }
    
    private static FileOutputStream a() {
        if (!TLog.LOG_TO_GLOBAL_FILE) {
            return null;
        }
        if (TLog.c == null) {
            try {
                TLog.c = new FileOutputStream(TLog.e);
                final StringBuilder sb = new StringBuilder();
                sb.append("\n");
                sb.append("Vender : " + TuSdkDeviceInfo.getVender() + "\n");
                sb.append("Model : " + TuSdkDeviceInfo.getModel() + "\n");
                sb.append("OSVersion : " + TuSdkDeviceInfo.getOSVersion() + "\n");
                sb.append("Core :3.1.1\n");
                sb.append("GPUInfo : " + SelesContext.getGpuInfo() + "\n");
                sb.append("CPUInfo : " + SelesContext.getCpuType() + "\n");
                sb.append("isSupportGL2 : " + SelesContext.isSupportGL2() + "\n");
                sb.append("MaxTextureSize : " + SelesContext.getMaxTextureSize() + "\n");
                sb.append("GPURank:" + TuSdkGPU.getGpuType().toString() + "\n\n\n");
                TLog.c.write(sb.toString().getBytes());
            }
            catch (Exception ex) {
                ex.printStackTrace();
                e("open out put file failed!", new Object[0]);
            }
        }
        return TLog.c;
    }
    
    public static void fps(final String str) {
        if (TLog.LOG_CURRENT_FPS) {
            d("[debug]" + str + "FPS:" + a(a(str)), new Object[0]);
        }
    }
    
    private static double a(final LinkedList<Long> list) {
        final long nanoTime = System.nanoTime();
        final double n = (nanoTime - list.getFirst()) / 1.0E9;
        list.addLast(nanoTime);
        if (list.size() > 100) {
            list.removeFirst();
        }
        return (n > 0.0) ? (list.size() / n) : 0.0;
    }
    
    private static LinkedList<Long> a(final String s) {
        LinkedList<Long> list = TLog.f.get(s);
        if (list == null) {
            list = new LinkedList<Long>() {
                {
                    this.add(System.nanoTime());
                }
            };
            TLog.f.put(s, list);
        }
        return list;
    }
    
    static {
        TLog.a = false;
        TLog.LOG_AUDIO_DECODEC_INFO = false;
        TLog.LOG_AUDIO_ENCODEC_INFO = false;
        TLog.LOG_VIDEO_DECODEC_INFO = false;
        TLog.LOG_VIDEO_ENCODEC_INFO = false;
        TLog.LOG_MEDIA_MUXER_INFO = false;
        TLog.LOG_CURRENT_FPS = false;
        TLog.LOG_TO_GLOBAL_FILE = false;
        TLog.d = new Object();
        TLog.e = Environment.getExternalStorageDirectory().getPath() + "/TuSdk/LOG_" + StringHelper.timeStampString() + ".log";
        TLog.f = new HashMap<String, LinkedList<Long>>();
    }
}
