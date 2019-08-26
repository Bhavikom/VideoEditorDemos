// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFrameInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
//import org.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
//import org.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFrameInfo;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;

public class TuSdkVideoDecodecSyncBase implements TuSdkVideoDecodecSync
{
    protected boolean mReleased;
    private TuSdkVideoInfo a;
    protected final Object mLocker;
    private boolean b;
    private boolean c;
    private boolean d;
    private TuSdkMediaFrameInfo e;
    protected long mDurationUs;
    protected long mOutputTimeUs;
    protected long mPreviousTimeUs;
    protected long mFrameIntervalUs;
    private boolean f;
    private boolean g;
    protected long mMaxFrameTimeUs;
    protected long mMinFrameTimeUs;
    protected long mFlushAndSeekto;
    protected long mRelativeStartNs;
    
    public TuSdkVideoDecodecSyncBase() {
        this.mReleased = false;
        this.mLocker = new Object();
        this.b = false;
        this.c = false;
        this.d = false;
        this.mDurationUs = 0L;
        this.mOutputTimeUs = 0L;
        this.mPreviousTimeUs = 0L;
        this.mFrameIntervalUs = 0L;
        this.f = false;
        this.mMaxFrameTimeUs = -1L;
        this.mMinFrameTimeUs = -1L;
        this.mFlushAndSeekto = -1L;
        this.mRelativeStartNs = -1L;
    }
    
    @Override
    public void release() {
        this.mReleased = true;
    }
    
    public long outputTimeUs() {
        return this.mOutputTimeUs;
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
    
    @Override
    public void syncVideoDecodeCompleted() {
        this.b = true;
    }
    
    @Override
    public boolean isVideoDecodeCompleted() {
        return this.b;
    }
    
    @Override
    public boolean isVideoDecodeCrashed() {
        return this.c;
    }
    
    @Override
    public boolean hasVideoDecodeTrack() {
        return this.d;
    }
    
    @Override
    public void syncVideoDecodeCrashed(final Exception ex) {
        if (ex == null) {
            return;
        }
        this.c = true;
        if (ex instanceof TuSdkNoMediaTrackException) {
            this.d = false;
        }
    }

    @Override
    public void syncVideoDecodecInfo(final TuSdkVideoInfo a, final TuSdkMediaExtractor tuSdkMediaExtractor) {
        if (a == null || tuSdkMediaExtractor == null) {
            return;
        }
        this.a = a;
        this.mDurationUs = a.durationUs;
        this.d = true;
        this.mMinFrameTimeUs = tuSdkMediaExtractor.getSampleTime();
        this.e = tuSdkMediaExtractor.getFrameInfo();
    }

    @Override
    public boolean syncVideoDecodecExtractor(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec) {
        if (this.mReleased || tuSdkMediaExtractor == null || tuSdkMediaCodec == null) {
            return true;
        }
        final boolean putBufferToCoderUntilEnd = TuSdkMediaUtils.putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec);
        this.mFrameIntervalUs = tuSdkMediaExtractor.getFrameIntervalUs();
        return putBufferToCoderUntilEnd;
    }

    public TuSdkMediaFrameInfo getFrameInfo() {
        return this.e;
    }
    
    public boolean isSupportPrecise() {
        return this.e != null && this.e.supportAllKeys();
    }
    
    public boolean isPause() {
        return this.f;
    }
    
    public void setPause() {
        this.f = true;
    }
    
    public void setPlay() {
        this.mRelativeStartNs = -1L;
        this.f = false;
    }
    
    public void syncRestart() {
        this.b = false;
    }
    
    public void pauseSave() {
        this.g = this.f;
        this.setPause();
    }
    
    public void resumeSave() {
        this.resume(this.g);
    }
    
    public void resume(final boolean b) {
        this.setPlay();
        if (b) {
            this.setPause();
        }
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
        return this.b || (this.d && this.c);
    }
    
    public boolean isInterrupted() {
        return ThreadHelper.isInterrupted() || this.mReleased;
    }
    
    protected void syncPause() {
        while (!this.isInterrupted() && this.f) {}
    }
    
    protected boolean syncPlay(final long n) {
        while (!this.isInterrupted() && n > System.nanoTime()) {
            if (this.mRelativeStartNs < 0L || this.f) {
                return false;
            }
        }
        return true;
    }
    
    public boolean syncWithVideo() {
        while (!this.isInterrupted() && this.mRelativeStartNs < 0L) {
            if (this.f) {
                return false;
            }
        }
        return true;
    }
    

    
    @Override
    public void syncVideoDecodecOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkVideoInfo tuSdkVideoInfo) {
        if (bufferInfo == null || bufferInfo.size < 1) {
            return;
        }
        this.mPreviousTimeUs = this.mOutputTimeUs;
        this.mOutputTimeUs = bufferInfo.presentationTimeUs;
        byteBuffer.position(bufferInfo.offset);
        byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
    }
    
    @Override
    public void syncVideoDecodecUpdated(final MediaCodec.BufferInfo bufferInfo) {
        if (TLog.LOG_VIDEO_DECODEC_INFO) {
            TuSdkCodecCapabilities.logBufferInfo(String.format("%s syncVideoDecodecUpdated", "TuSdkVideoDecodecSyncBase"), bufferInfo);
        }
        this.syncPause();
    }
    
    @Override
    public long calcInputTimeUs(final long n) {
        return n;
    }
    
    @Override
    public long calcEffectFrameTimeUs(final long n) {
        return n;
    }
}
