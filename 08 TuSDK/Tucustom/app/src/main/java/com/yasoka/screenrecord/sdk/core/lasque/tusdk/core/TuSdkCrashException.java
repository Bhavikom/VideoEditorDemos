// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core;

import android.os.Process;
import android.widget.Toast;
import android.os.Looper;
//import org.lasque.tusdk.core.utils.TLog;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

public class TuSdkCrashException extends Exception implements Thread.UncaughtExceptionHandler
{
    private Context a;
    private Thread.UncaughtExceptionHandler b;
    
    public static void bindExceptionHandler(final Context context) {
        Thread.setDefaultUncaughtExceptionHandler(new TuSdkCrashException(context));
    }
    
    private TuSdkCrashException(final Context a) {
        this.a = a;
        this.b = Thread.getDefaultUncaughtExceptionHandler();
    }
    
    protected void initException() {
    }
    
    @Override
    public void uncaughtException(final Thread thread, final Throwable t) {
        if (!this.a(t) && this.b != null) {
            this.b.uncaughtException(thread, t);
        }
    }
    
    private boolean a(final Throwable t) {
        if (t == null) {
            return false;
        }
        TLog.enableLogging("TuSdk");
        TLog.e(t);
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(TuSdkCrashException.this.a,
                        (CharSequence)"\u5e94\u7528\u7a0b\u5e8f\u53d1\u751f\u9519\u8bef\uff0c \u5373\u5c06\u9000\u51fa", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        try {
            Thread.sleep(3000L);
        }
        catch (InterruptedException ex) {
            TLog.e(ex);
        }
        Process.killProcess(Process.myPid());
        System.exit(0);
        return true;
    }
}
