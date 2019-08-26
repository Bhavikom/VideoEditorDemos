// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

public class JVMUtils
{
    public static void runGC() {
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                Runtime.getRuntime().gc();
                try {
                    Thread.sleep(100L);
                }
                catch (InterruptedException ex) {
                    TLog.e(ex);
                }
                System.runFinalization();
            }
        });
    }
    
    public static float[] getMemoryInfo() {
        return new float[] { Runtime.getRuntime().maxMemory() * 1.0f / 1048576.0f, Runtime.getRuntime().totalMemory() * 1.0f / 1048576.0f, Runtime.getRuntime().freeMemory() * 1.0f / 1048576.0f };
    }
}
