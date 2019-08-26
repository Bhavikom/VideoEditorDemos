// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
//import org.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import java.util.concurrent.locks.ReentrantLock;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;

public abstract class TuSdkAudioDecodecSyncBase implements TuSdkAudioDecodecSync, TuSdkAudioResampleSync
{
    protected boolean mReleased;
    private TuSdkAudioInfo a;
    protected final Object mLocker;
    private ReentrantLock b;
    private Object c;
    protected TuSdkAudioResample mAudioResample;
    protected TuSdkAudioPitch mAudioPitch;
    private boolean d;
    private boolean e;
    private boolean f;
    protected long mDurationUs;
    protected long mFrameIntervalUs;
    protected long mLastTimeUs;
    protected long mPreviousTimeUs;
    protected long mMaxFrameTimeUs;
    protected long mMinFrameTimeUs;
    protected long mFlushAndSeekto;
    private volatile boolean g;
    private volatile boolean h;
    
    public TuSdkAudioDecodecSyncBase() {
        this.mReleased = false;
        this.mLocker = new Object();
        this.b = new ReentrantLock(true);
        this.c = new Object();
        this.d = false;
        this.e = false;
        this.f = false;
        this.mDurationUs = 0L;
        this.mFrameIntervalUs = 0L;
        this.mPreviousTimeUs = 0L;
        this.mMaxFrameTimeUs = -1L;
        this.mMinFrameTimeUs = -1L;
        this.mFlushAndSeekto = -1L;
        this.g = false;
        this.h = true;
    }
    
    @Override
    public void release() {
        this.mReleased = true;
    }
    
    public long lastTimestampUs() {
        return this.mLastTimeUs;
    }
    
    public long durationUs() {
        return this.mDurationUs;
    }
    
    public long frameIntervalUs() {
        return this.mFrameIntervalUs;
    }
    
    public void syncFlushAndSeekto(final long mFlushAndSeekto) {
        this.mFlushAndSeekto = mFlushAndSeekto;
    }
    
    public void setAudioResample(final TuSdkAudioResample mAudioResample) {
        if (mAudioResample == null) {
            return;
        }
        mAudioResample.setMediaSync(this);
        this.mAudioResample = mAudioResample;
    }
    
    @Override
    public void syncAudioDecodeCompleted() {
        this.d = true;
    }
    
    @Override
    public boolean isAudioDecodeCompleted() {
        return this.d;
    }
    
    @Override
    public boolean isAudioDecodeCrashed() {
        return this.e;
    }
    
    @Override
    public boolean hasAudioDecodeTrack() {
        return this.f;
    }
    
    @Override
    public void syncAudioDecodeCrashed(final Exception ex) {
        if (ex == null) {
            return;
        }
        this.e = true;
        if (ex instanceof TuSdkNoMediaTrackException) {
            this.f = false;
        }
    }
    
    public boolean isPause() {
        return this.g;
    }
    
    public boolean isPauseSave() {
        return this.h;
    }
    
    public void setPuaseLocker(final Object c) {
        this.c = c;
    }
    
    public void setPause() {
        synchronized (this.c) {
            this.g = true;
            if (this.mAudioResample != null) {
                this.mAudioResample.reset();
            }
            if (this.mAudioPitch != null) {
                this.mAudioPitch.reset();
            }
        }
    }
    
    public void setPlay() {
        synchronized (this.c) {
            this.g = false;
        }
    }
    
    public void syncRestart() {
        this.d = false;
    }
    
    public void pauseSave() {
        this.b.tryLock();
        this.h = this.g;
        this.setPause();
        if (this.b.getHoldCount() > 0) {
            this.b.unlock();
        }
    }
    
    public void resumeSave() {
        this.b.tryLock();
        this.resume(this.h);
        if (this.b.getHoldCount() > 0) {
            this.b.unlock();
        }
    }
    
    public void resume(final boolean b) {
        if (b) {
            this.setPause();
        }
        else {
            this.setPlay();
        }
    }
    
    public void resetIsPauseSave() {
        this.h = false;
    }
    
    protected void flush(final TuSdkMediaCodec tuSdkMediaCodec) {
        if (tuSdkMediaCodec == null) {
            return;
        }
        tuSdkMediaCodec.flush();
        this.save();
    }
    
    protected void save() {
        this.pauseSave();
        this.resumeSave();
    }
    
    public boolean isNeedRestart() {
        return this.d || (this.f && this.e);
    }
    
    protected boolean isInterrupted() {
        return ThreadHelper.isInterrupted() || this.mReleased;
    }
    
    protected void syncPause() {
        while (!this.isInterrupted() && this.g) {}
    }
    
    @Override
    public void syncAudioDecodecInfo(final TuSdkAudioInfo a, final TuSdkMediaExtractor tuSdkMediaExtractor) {
        if (a == null || tuSdkMediaExtractor == null) {
            return;
        }
        this.f = true;
        this.a = a;
        this.mDurationUs = a.durationUs;
        this.mMinFrameTimeUs = tuSdkMediaExtractor.getSampleTime();
    }
    
    @Override
    public boolean syncAudioDecodecExtractor(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec) {
        if (this.mReleased || tuSdkMediaExtractor == null || tuSdkMediaCodec == null) {
            return true;
        }
        final boolean putBufferToCoderUntilEnd = TuSdkMediaUtils.putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec);
        this.mFrameIntervalUs = tuSdkMediaExtractor.getFrameIntervalUs();
        return putBufferToCoderUntilEnd;
    }
    
    @Override
    public void syncAudioDecodecOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkAudioInfo tuSdkAudioInfo) {
        if (bufferInfo == null || bufferInfo.size < 1 || this.mAudioResample == null) {
            return;
        }
        this.mAudioResample.queueInputBuffer(byteBuffer, bufferInfo);
    }
    
    @Override
    public void syncAudioDecodecUpdated(final MediaCodec.BufferInfo bufferInfo) {
        if (TLog.LOG_AUDIO_DECODEC_INFO) {
            TuSdkCodecCapabilities.logBufferInfo(String.format("%s syncAudioDecodecUpdated", "TuSdkAudioDecodecSyncBase"), bufferInfo);
        }
        this.syncPause();
    }
    
    @Override
    public void syncAudioResampleOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (TLog.LOG_AUDIO_DECODEC_INFO) {
            TuSdkCodecCapabilities.logBufferInfo(String.format("%s resampleOutputBuffer", "TuSdkAudioDecodecSyncBase"), bufferInfo);
        }
        if (bufferInfo == null || bufferInfo.size < 1) {
            return;
        }
        this.mPreviousTimeUs = this.mLastTimeUs;
        this.mLastTimeUs = bufferInfo.presentationTimeUs;
        byteBuffer.position(bufferInfo.offset);
        byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
    }
}
