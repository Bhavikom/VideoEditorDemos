// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret;

import android.graphics.Bitmap;

public class SdkAOFile
{
    private static final boolean a;
    private String b;
    private long c;
    
    public SdkAOFile(final String b, final boolean b2) {
        this.b = b;
        this.c = jniLoadFile(this.b, b2);
    }
    
    public Bitmap loadImage(final String s) {
        return jniLoadImage(this.c, s);
    }
    
    public String loadText(final String s) {
        return jniLoadText(this.c, s);
    }
    
    public byte[] loadBinary(final String s) {
        return jniLoadBinary(this.c, s);
    }
    
    public void release() {
        jniRelease(this.c);
    }
    
    private static native long jniLoadFile(final String p0, final boolean p1);
    
    private static native Bitmap jniLoadImage(final long p0, final String p1);
    
    private static native String jniLoadText(final long p0, final String p1);
    
    private static native byte[] jniLoadBinary(final long p0, final String p1);
    
    private static native void jniRelease(final long p0);
    
    static {
        a = SdkValid.isInit;
    }
}
