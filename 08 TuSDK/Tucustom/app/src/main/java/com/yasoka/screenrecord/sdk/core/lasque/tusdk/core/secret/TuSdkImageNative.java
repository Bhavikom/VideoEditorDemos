// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret;

import java.nio.Buffer;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import java.io.OutputStream;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;

public class TuSdkImageNative
{
    private static final boolean a;
    
    private static int a(int max) {
        if (max < 1) {
            max = 100;
        }
        max = Math.max(Math.min(max, 100), 1);
        return max;
    }
    
    public static boolean imageCompress(final Bitmap bitmap, final OutputStream outputStream) {
        return imageCompress(bitmap, outputStream, 95);
    }
    
    public static boolean imageCompress(final Bitmap bitmap, final OutputStream outputStream, final int n) {
        return imageCompress(bitmap, outputStream, n, true);
    }
    
    public static boolean imageCompress(final Bitmap bitmap, final OutputStream outputStream, final int n, final boolean b) {
        if (bitmap == null || bitmap.isRecycled() || outputStream == null || !TuSdkGPU.isSupporTurbo()) {
            return false;
        }
        final String compressBitmap2JNI = compressBitmap2JNI(bitmap, outputStream, a(n), b, new byte[4096]);
        if ("1".equalsIgnoreCase(compressBitmap2JNI)) {
            return true;
        }
        TLog.e("saveImage: %s | %s", bitmap.getConfig(), compressBitmap2JNI);
        return false;
    }
    
    public static int getClipHistList(final Bitmap bitmap, final int n, final int n2, final float n3, final byte[] array) {
        return getBitmapClipHistListJNI(bitmap, n, n2, n3, array);
    }
    
    public static int getYUVHistgrameRange(final byte[] array, final int n, final int n2, final int n3, final float[] array2) {
        return getYUVHistgrameRangeJNI(array, n, n2, n3, array2);
    }
    
    public static void glReadPixels(final int n, final int n2) {
        glReadPixelsJNI(n, n2);
    }
    
    public static void copyBuffer(final Buffer buffer, final int n, final int n2, final Buffer buffer2) {
        if (buffer == null || buffer2 == null || n < 4 || n2 < 4) {
            return;
        }
        copyBufferJNI(buffer, n, n2, buffer2);
    }
    
    private static native String compressBitmap2JNI(final Bitmap p0, final OutputStream p1, final int p2, final boolean p3, final byte[] p4);
    
    private static native int getBitmapClipHistListJNI(final Bitmap p0, final int p1, final int p2, final float p3, final byte[] p4);
    
    private static native int getYUVHistgrameRangeJNI(final byte[] p0, final int p1, final int p2, final int p3, final float[] p4);
    
    private static native void glReadPixelsJNI(final int p0, final int p1);
    
    private static native void copyBufferJNI(final Buffer p0, final int p1, final int p2, final Buffer p3);
    
    static {
        a = SdkValid.isInit;
    }
}
