// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api;

//import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;

import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKVideoInfo;

public class TuSDKPostProcessJNI
{
    public static boolean runVideoCommands(final String[] array) {
        return runVideoCommandsJNI(array) == 0;
    }
    
    private static native int runVideoCommandsJNI(final String[] p0);
    
    public static native void readVideoInfo(final String p0, final TuSDKVideoInfo p1);
    
    public static native int fastStart(final String p0, final String p1);
    
    static {
        System.loadLibrary("tusdk-video");
    }
}
