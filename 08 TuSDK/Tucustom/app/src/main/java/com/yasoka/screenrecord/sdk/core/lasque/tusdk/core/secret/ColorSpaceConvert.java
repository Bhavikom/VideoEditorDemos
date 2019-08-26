// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret;

public class ColorSpaceConvert
{
    private static final boolean a;
    
    public static void nv21ToRgba(final byte[] array, final int n, final int n2, final int[] array2) {
        yuvNv21ToRgbaJNI(array, n, n2, array2);
    }
    
    public static void rgbaToNv21(final int[] array, final int n, final int n2, final byte[] array2) {
        yuvRgbaToNv21JNI(array, n, n2, array2);
    }
    
    public static void rgbaToYv12(final int[] array, final int n, final int n2, final byte[] array2) {
        yuvRgbaToYv12JNI(array, n, n2, array2);
    }
    
    public static void rgbaToI420(final int[] array, final int n, final int n2, final byte[] array2) {
        yuvRgbaToI420JNI(array, n, n2, array2);
    }
    
    public static void nv21ToYuv420sp(final byte[] array, final byte[] array2, final int n) {
        yuvNv21ToNv12JNI(array, array2, n);
    }
    
    public static void nv21TOYuv420p(final byte[] array, final byte[] array2, final int n) {
        yuvNv21ToI420JNI(array, array2, n);
    }
    
    public static void nv21Transform(final byte[] array, final byte[] array2, final int n, final int n2, final int n3) {
        yuvNV21TransformJNI(array, array2, n, n2, n3);
    }
    
    public static void copyAndFlipGrayData(final byte[] array, final byte[] array2, final int n, final int n2, final int n3) {
        yuvSinglePlaneCopyFlipJNI(array, n, n2, array2, n3);
    }
    
    private static native void yuvNv21ToRgbaJNI(final byte[] p0, final int p1, final int p2, final int[] p3);
    
    private static native void yuvRgbaToNv21JNI(final int[] p0, final int p1, final int p2, final byte[] p3);
    
    private static native void yuvRgbaToYv12JNI(final int[] p0, final int p1, final int p2, final byte[] p3);
    
    private static native void yuvRgbaToI420JNI(final int[] p0, final int p1, final int p2, final byte[] p3);
    
    private static native void yuvNv21ToNv12JNI(final byte[] p0, final byte[] p1, final int p2);
    
    private static native void yuvNv21ToI420JNI(final byte[] p0, final byte[] p1, final int p2);
    
    private static native void yuvNV21TransformJNI(final byte[] p0, final byte[] p1, final int p2, final int p3, final int p4);
    
    private static native void yuvSinglePlaneCopyFlipJNI(final byte[] p0, final int p1, final int p2, final byte[] p3, final int p4);
    
    static {
        a = SdkValid.isInit;
    }
}
