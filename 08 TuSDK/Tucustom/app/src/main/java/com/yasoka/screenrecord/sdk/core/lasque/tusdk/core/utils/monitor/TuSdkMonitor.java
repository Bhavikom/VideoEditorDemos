// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.monitor;

//import org.lasque.tusdk.core.utils.TuSdkThreadExecutor;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkThreadExecutor;

public class TuSdkMonitor
{
    private static TuSdkMonitor a;
    private static TuSdkThreadExecutor b;
    private static TuSdkGLMonitor c;
    
    private TuSdkMonitor() {
        TuSdkMonitor.b = new TuSdkThreadExecutor();
        TuSdkMonitor.c = new TuSdkGLMonitor(TuSdkMonitor.b);
    }
    
    public static TuSdkGLMonitor glMonitor() {
        return TuSdkMonitor.c;
    }
    
    public static TuSdkMonitor setEnableCheckFrameImage(final boolean enableCheckFrameImage) {
        TuSdkMonitor.c.setEnableCheckFrameImage(enableCheckFrameImage);
        return TuSdkMonitor.a;
    }
    
    public static TuSdkMonitor setEnableCheckGLError(final boolean enableCheckGLError) {
        TuSdkMonitor.c.setEnableCheckGLError(enableCheckGLError);
        return TuSdkMonitor.a;
    }
    
    static {
        TuSdkMonitor.a = new TuSdkMonitor();
    }
}
