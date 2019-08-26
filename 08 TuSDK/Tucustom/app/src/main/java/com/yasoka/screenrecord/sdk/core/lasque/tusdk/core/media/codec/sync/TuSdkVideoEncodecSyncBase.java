// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

//import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
//import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record.TuSdkRecordSurface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import java.util.ArrayList;
import java.util.List;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;

public abstract class TuSdkVideoEncodecSyncBase implements TuSdkVideoEncodecSync
{
    protected boolean mReleased;
    private TuSdkVideoInfo a;
    protected long mLastTimeUs;
    protected int mFrameRates;
    protected long mFrameCounts;
    protected long mPreviousTimeUs;
    protected long mFrameIntervalUs;
    private boolean b;
    private final List<Long> c;
    protected final Object mSyncLock;
    
    public TuSdkVideoEncodecSyncBase() {
        this.mReleased = false;
        this.mLastTimeUs = 0L;
        this.mFrameRates = 0;
        this.mFrameCounts = 0L;
        this.mPreviousTimeUs = -1L;
        this.mFrameIntervalUs = 0L;
        this.b = false;
        this.c = new ArrayList<Long>();
        this.mSyncLock = new Object();
    }
    
    public boolean hasLocked() {
        return this.c.size() > 0;
    }
    
    public void clearLocker() {
        synchronized (this.mSyncLock) {
            this.c.clear();
        }
    }
    
    public void lockVideoTimestampUs(final long l) {
        synchronized (this.mSyncLock) {
            this.c.add(l);
        }
    }
    
    public void unlockVideoTimestampUs(final long l) {
        synchronized (this.mSyncLock) {
            this.c.remove(l);
        }
    }
    
    public boolean hadLockVideoTimestampUs(final long l) {
        boolean contains = false;
        synchronized (this.mSyncLock) {
            contains = this.c.contains(l);
        }
        return contains;
    }
    
    public long getLastTimeUs() {
        return this.mLastTimeUs;
    }
    
    public boolean isInterrupted() {
        return ThreadHelper.isInterrupted() || this.mReleased;
    }
    
    @Override
    public boolean isVideoEncodeCompleted() {
        return this.b;
    }
    
    @Override
    public void release() {
        this.mReleased = true;
    }
    
    @Override
    public void syncEncodecVideoInfo(final TuSdkVideoInfo a) {
        this.a = a;
        if (a != null) {
            this.mFrameRates = a.frameRates;
        }
    }
    
    @Override
    public void syncVideoEncodecOutputBuffer(final TuSdkMediaMuxer tuSdkMediaMuxer, final int n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        TuSdkMediaUtils.processOutputBuffer(tuSdkMediaMuxer, n, byteBuffer, bufferInfo);
    }
    
    @Override
    public void syncVideoEncodecUpdated(final MediaCodec.BufferInfo bufferInfo) {
        if (bufferInfo == null) {
            return;
        }
        this.unlockVideoTimestampUs(bufferInfo.presentationTimeUs);
        if (TLog.LOG_VIDEO_ENCODEC_INFO) {
            TuSdkCodecCapabilities.logBufferInfo(String.format("%s syncVideoEncodecUpdated", "TuSdkVideoEncodecSyncBase"), bufferInfo);
        }
    }
    
    @Override
    public void syncVideoEncodecCompleted() {
        if (TLog.LOG_VIDEO_ENCODEC_INFO) {
            TLog.d("%s syncVideoEncodecCompleted", "TuSdkVideoEncodecSyncBase");
        }
        this.b = true;
    }
    
    protected long calculateEncodeTimestampUs(final int n, final long n2) {
        if (n < 1) {
            return 0L;
        }
        return n2 * 1000000L / n;
    }
    
    protected abstract boolean isLastDecodeFrame(final long p0);
    
    protected long getInputIntervalUs() {
        return this.mFrameIntervalUs;
    }
    
    protected boolean needSkip(final long n) {
        return false;
    }
    
    public void syncVideoEncodecDrawFrame(final long n, final boolean b, final TuSdkRecordSurface tuSdkRecordSurface, final TuSdkEncodeSurface tuSdkEncodeSurface) {
        if (tuSdkRecordSurface == null || tuSdkEncodeSurface == null || this.mReleased) {
            return;
        }
        tuSdkRecordSurface.updateSurfaceTexImage();
        if (b) {
            this.clearLocker();
            return;
        }
        final long n2 = n / 1000L;
        if (this.needSkip(n2)) {
            this.unlockVideoTimestampUs(n2);
            this.mPreviousTimeUs = -1L;
            this.mFrameIntervalUs = 0L;
            return;
        }
        if (this.mPreviousTimeUs < 0L) {
            this.mPreviousTimeUs = n2;
        }
        this.mFrameIntervalUs = n2 - this.mPreviousTimeUs;
        this.mPreviousTimeUs = n2;
        long mLastTimeUs = this.calculateEncodeTimestampUs(this.mFrameRates, this.mFrameCounts);
        if (mLastTimeUs < 1L) {
            this.renderToEncodec(mLastTimeUs, n2, tuSdkRecordSurface, tuSdkEncodeSurface);
            return;
        }
        long n3 = mLastTimeUs * 1000L;
        while (mLastTimeUs < n2) {
            this.lockVideoTimestampUs(mLastTimeUs);
            this.mLastTimeUs = mLastTimeUs;
            ++this.mFrameCounts;
            tuSdkEncodeSurface.duplicateFrameReadyInGLThread(n3);
            tuSdkEncodeSurface.swapBuffers(n3);
            mLastTimeUs = this.calculateEncodeTimestampUs(this.mFrameRates, this.mFrameCounts);
            n3 = mLastTimeUs * 1000L;
        }
        if (this.isLastDecodeFrame(n2)) {
            this.renderToEncodec(mLastTimeUs, n2, tuSdkRecordSurface, tuSdkEncodeSurface);
            return;
        }
        if (mLastTimeUs > n2 && this.getInputIntervalUs() > 0L && mLastTimeUs > n2 + this.getInputIntervalUs()) {
            this.unlockVideoTimestampUs(n2);
            return;
        }
        this.renderToEncodec(mLastTimeUs, n2, tuSdkRecordSurface, tuSdkEncodeSurface);
    }
    
    protected void renderToEncodec(final long mLastTimeUs, final long n, final TuSdkRecordSurface tuSdkRecordSurface, final TuSdkEncodeSurface tuSdkEncodeSurface) {
        if (mLastTimeUs != n) {
            this.lockVideoTimestampUs(mLastTimeUs);
            this.unlockVideoTimestampUs(n);
        }
        final long n2 = mLastTimeUs * 1000L;
        tuSdkRecordSurface.newFrameReadyInGLThread(n2);
        tuSdkEncodeSurface.newFrameReadyInGLThread(n2);
        this.mLastTimeUs = mLastTimeUs;
        ++this.mFrameCounts;
        tuSdkEncodeSurface.swapBuffers(n2);
    }
}
