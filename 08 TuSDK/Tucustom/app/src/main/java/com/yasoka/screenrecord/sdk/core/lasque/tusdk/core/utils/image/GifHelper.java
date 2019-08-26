// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image;

import java.io.FileNotFoundException;
import java.io.IOException;
//import org.lasque.tusdk.core.secret.SdkValid;
import android.graphics.Bitmap;
import android.net.Uri;
import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;

import java.io.FileDescriptor;

public final class GifHelper
{
    private static final String TAG = "GifHelper";
    private static final boolean isInit;
    private volatile long gifInfoPtr;
    private int mWidth;
    private int mHeight;
    private int mFrameCount;
    
    private GifHelper(final long gifInfoPtr, final int mWidth, final int mHeight, final int mFrameCount) {
        this.gifInfoPtr = gifInfoPtr;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.mFrameCount = mFrameCount;
    }
    
    public int getWidth() {
        return this.mWidth;
    }
    
    public int getHeight() {
        return this.mHeight;
    }
    
    public int getFrameCount() {
        return this.mFrameCount;
    }
    
    public static GifHelper parseFile(final String s) {
        return openGifFileJNI(s);
    }
    
    public static GifHelper parseFd(final FileDescriptor fileDescriptor) {
        return openGifFdJNI(fileDescriptor, 0L);
    }
    
    public static GifHelper openAssetFileDescriptor(final AssetFileDescriptor assetFileDescriptor) {
        try {
            return openGifFdJNI(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset());
        }
        finally {
            try {
                assetFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static GifHelper openURI(final ContentResolver contentResolver, final Uri uri) {
        if ("file".equals(uri.getScheme())) {
            return openGifFileJNI(uri.getPath());
        }
        try {
            return openAssetFileDescriptor(contentResolver.openAssetFileDescriptor(uri, "r"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public synchronized long renderFrame(final Bitmap bitmap) {
        return renderGifFrameJNI(this.gifInfoPtr, bitmap);
    }
    
    public synchronized void recycle() {
        freeGifJNI(this.gifInfoPtr);
        this.gifInfoPtr = 0L;
    }
    
    public synchronized boolean isRecycled() {
        return this.gifInfoPtr == 0L;
    }
    
    public synchronized boolean reset() {
        return resetGifJNI(this.gifInfoPtr);
    }
    
    public synchronized long restoreRemainder() {
        return restoreGifRemainderJNI(this.gifInfoPtr);
    }
    
    public synchronized void saveRemainder() {
        saveGifRemainderJNI(this.gifInfoPtr);
    }
    
    public void setSpeed(float v) {
        if (v <= 0.0f || Float.isNaN(v)) {
            throw new IllegalArgumentException("Speed factor is not positive");
        }
        if (v < 4.656613E-10f) {
            v = 4.656613E-10f;
        }
        synchronized (this) {
            setGifSpeedFactorJNI(this.gifInfoPtr, v);
        }
    }
    
    public synchronized int getDuration() {
        return getGifDurationJNI(this.gifInfoPtr);
    }
    
    public synchronized int getCurrentPosition() {
        return getGifCurrentPositionJNI(this.gifInfoPtr);
    }
    
    public synchronized boolean isAnimationCompleted() {
        return isGifAnimationCompletedJNI(this.gifInfoPtr);
    }
    
    public int getFrameDuration(final int n) {
        if (n < 0 || n >= this.getFrameCount()) {
            throw new IndexOutOfBoundsException("Frame index is out of bounds");
        }
        synchronized (this) {
            return getGifFrameDurationJNI(this.gifInfoPtr, n);
        }
    }
    
    public synchronized int getLoopCount() {
        return getGifLoopCountJNI(this.gifInfoPtr);
    }
    
    public void setLoopCount(final int n) {
        if (n < 0 || n > 65535) {
            throw new IllegalArgumentException("Loop count of range <0, 65535>");
        }
        synchronized (this) {
            setGifLoopCountJNI(this.gifInfoPtr, n);
        }
    }
    
    public synchronized int getCurrentFrameIndex() {
        return getGifCurrentFrameIndexJNI(this.gifInfoPtr);
    }
    
    public synchronized int getCurrentLoop() {
        return getGifCurrentLoopJNI(this.gifInfoPtr);
    }
    
    public synchronized int getErrorCode() {
        return getGifErrorCodeJNI(this.gifInfoPtr);
    }
    
    @Override
    protected void finalize() {
        try {
            this.recycle();
        }
        finally {
            try {
                super.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
    
    static native GifHelper openGifFileJNI(final String p0);
    
    static native GifHelper openGifFdJNI(final FileDescriptor p0, final long p1);
    
    private static native long renderGifFrameJNI(final long p0, final Bitmap p1);
    
    private static native boolean resetGifJNI(final long p0);
    
    private static native void setGifSpeedFactorJNI(final long p0, final float p1);
    
    private static native void saveGifRemainderJNI(final long p0);
    
    private static native long restoreGifRemainderJNI(final long p0);
    
    private static native void freeGifJNI(final long p0);
    
    private static native String getGifCommentJNI(final long p0);
    
    private static native boolean isGifAnimationCompletedJNI(final long p0);
    
    private static native int getGifLoopCountJNI(final long p0);
    
    private static native void setGifLoopCountJNI(final long p0, final int p1);
    
    private static native int getGifDurationJNI(final long p0);
    
    private static native int getGifCurrentPositionJNI(final long p0);
    
    private static native int getGifErrorCodeJNI(final long p0);
    
    private static native int getGifCurrentLoopJNI(final long p0);
    
    private static native int getGifCurrentFrameIndexJNI(final long p0);
    
    private static native int getGifFrameDurationJNI(final long p0, final int p1);
    
    static {
        isInit = SdkValid.isInit;
    }
    
    public class GifIOException extends IOException
    {
        public GifError reason;
    }
    
    public enum GifError
    {
        NO_ERROR(0, "No error"), 
        OPEN_FAILED(101, "Failed to open given input"), 
        READ_FAILED(102, "Failed to read from given input"), 
        NOT_GIF_FILE(103, "Data is not in GIF format"), 
        NO_SCRN_DSCR(104, "No screen descriptor detected"), 
        NO_IMAG_DSCR(105, "No image descriptor detected"), 
        NO_COLOR_MAP(106, "Neither global nor local color map found"), 
        WRONG_RECORD(107, "Wrong record type detected"), 
        DATA_TOO_BIG(108, "Number of pixels bigger than width * height"), 
        NOT_ENOUGH_MEM(109, "Failed to allocate required memory"), 
        CLOSE_FAILED(110, "Failed to close given input"), 
        NOT_READABLE(111, "Given file was not opened for read"), 
        IMAGE_DEFECT(112, "Image is defective, decoding aborted"), 
        EOF_TOO_SOON(113, "Image EOF detected before image complete"), 
        NO_FRAMES(1000, "No frames found, at least one frame required"), 
        INVALID_SCR_DIMS(1001, "Invalid screen size, dimensions must be positive"), 
        INVALID_IMG_DIMS(1002, "Invalid image size, dimensions must be positive"), 
        IMG_NOT_CONFINED(1003, "Image size exceeds screen size"), 
        REWIND_FAILED(1004, "Input source rewind has failed, animation is stopped"), 
        INVALID_BYTE_BUFFER(1005, "Invalid and/or indirect byte buffer specified"), 
        UNKNOWN(-1, "Unknown error");
        
        public final String description;
        int a;
        
        private GifError(final int a, final String description) {
            this.a = a;
            this.description = description;
        }
        
        public static GifError fromCode(final int a) {
            for (final GifError gifError : values()) {
                if (gifError.a == a) {
                    return gifError;
                }
            }
            final GifError unknown = GifError.UNKNOWN;
            unknown.a = a;
            return unknown;
        }
        
        public int getErrorCode() {
            return this.a;
        }
        
        @Override
        public String toString() {
            return String.format("GifError %d: %s", this.a, this.description);
        }
    }
}
