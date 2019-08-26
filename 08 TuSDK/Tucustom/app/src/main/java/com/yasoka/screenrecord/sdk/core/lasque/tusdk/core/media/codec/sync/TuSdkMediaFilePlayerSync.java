// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackImpl;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrack;
//import org.lasque.tusdk.core.utils.TLog;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrack;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class TuSdkMediaFilePlayerSync implements TuSdkMediaDecodecSync
{
    private boolean a;
    private _AudioPlaySyncKey b;
    private _VideoPlaySyncKey c;
    
    public TuSdkMediaFilePlayerSync() {
        this.a = false;
    }
    
    @Override
    public TuSdkVideoDecodecSync buildVideoDecodecSync() {
        return this.getVideoDecodecSync();
    }
    
    @Override
    public TuSdkAudioDecodecSync buildAudioDecodecSync() {
        return this.getAudioDecodecSync();
    }
    
    @Override
    public TuSdkVideoDecodecSync getVideoDecodecSync() {
        if (this.c == null) {
            this.c = new _VideoPlaySyncKey();
        }
        return this.c;
    }
    
    @Override
    public TuSdkAudioDecodecSync getAudioDecodecSync() {
        if (this.b == null) {
            this.b = new _AudioPlaySyncKey();
        }
        return this.b;
    }
    
    @Override
    public void release() {
        this.a = true;
        if (this.c != null) {
            this.c.release();
            this.c = null;
        }
        if (this.b != null) {
            this.b.release();
            this.b = null;
        }
    }
    
    public long lastVideoTimestampUs() {
        if (this.c == null) {
            return 0L;
        }
        return this.c.outputTimeUs();
    }
    
    public long totalVideoDurationUs() {
        if (this.c == null) {
            return 0L;
        }
        return this.c.durationUs() - this.c.frameIntervalUs();
    }
    
    public boolean isPause() {
        return this.c != null && this.c.isPause();
    }
    
    public void setPause() {
        if (this.c != null) {
            this.c.setPause();
        }
        if (this.b != null) {
            this.b.setPause();
        }
    }
    
    public void setPlay() {
        if (this.c != null) {
            this.c.setPlay();
        }
        if (this.b != null) {
            this.b.setPlay();
        }
    }
    
    public void setReset() {
        if (this.c != null) {
            this.c.setReset();
        }
        if (this.b != null) {
            this.b.setReset();
        }
    }
    
    public void pauseSave() {
        if (this.c != null) {
            this.c.pauseSave();
        }
        if (this.b != null) {
            this.b.pauseSave();
        }
    }
    
    public void resumeSave() {
        if (this.c != null) {
            this.c.resumeSave();
        }
        if (this.b != null) {
            this.b.resumeSave();
        }
    }
    
    public boolean isSupportPrecise() {
        return this.c != null && this.c.isSupportPrecise();
    }
    
    public void setSpeed(final float n) {
        if (!this.isSupportPrecise()) {
            TLog.w("%s setSpeed unsupport this media.", "TuSdkMediaFilePlayerSync");
            return;
        }
        if (n <= 0.0f) {
            TLog.w("%s setSpeed need greater than 0.", "TuSdkMediaFilePlayerSync");
            return;
        }
        if (this.c != null) {
            this.c.setSpeed(n);
        }
        if (this.b != null) {
            this.b.setSpeed(n);
        }
    }
    
    public float speed() {
        if (this.c != null) {
            return this.c.speed();
        }
        return 1.0f;
    }
    
    public void setReverse(final boolean b) {
        if (!this.isSupportPrecise()) {
            TLog.w("%s setReverse unsupport this media.", "TuSdkMediaFilePlayerSync");
            return;
        }
        if (this.c != null) {
            this.c.setReverse(b);
        }
        if (this.b != null) {
            this.b.setReverse(b);
        }
    }
    
    public boolean isReverse() {
        return this.c != null && this.c.isReverse();
    }
    
    private void a() {
        if (this.c != null) {
            this.c.syncRestart();
        }
        if (this.b != null) {
            this.b.syncRestart();
        }
    }
    
    public boolean syncNeedRestart() {
        if (this.b()) {
            this.a();
            return true;
        }
        return false;
    }
    
    private boolean b() {
        return (this.c != null && this.c.isNeedRestart()) || (this.b != null && this.b.isNeedRestart());
    }
    
    public boolean isVideoEos(final long n) {
        return this.c != null && this.c.isVideoEos(n);
    }
    
    public boolean isAudioEos(final long n) {
        return this.b != null && this.b.isAudioEos(n);
    }
    
    public void syncFlushAndSeekto(final long n) {
        if (this.c != null) {
            this.c.syncFlushAndSeekto(n);
        }
        if (this.b != null) {
            this.b.syncFlushAndSeekto(n);
        }
    }
    
    public void syncVideoDecodeCompleted() {
        if (this.c == null) {
            return;
        }
        this.c.syncVideoDecodeCompleted();
    }
    
    public void syncAudioDecodeCompleted() {
        if (this.b == null) {
            return;
        }
        this.b.syncAudioDecodeCompleted();
    }
    
    public boolean isAudioDecodeCrashed() {
        return this.b != null && this.b.isAudioDecodeCrashed();
    }
    
    private class _AudioPlaySyncKey extends TuSdkAudioDecodecSyncBase
    {
        private TuSdkAudioTrack b;
        private float c;
        private boolean d;
        
        private _AudioPlaySyncKey() {
            this.c = 1.0f;
            this.d = false;
        }
        
        @Override
        public void setPause() {
            super.setPause();
            if (this.b != null) {
                this.b.pause();
                this.b.flush();
            }
        }
        
        @Override
        public void setPlay() {
            super.setPlay();
            if (this.b != null) {
                this.b.play();
            }
        }
        
        @Override
        public void release() {
            super.release();
            if (this.b != null) {
                this.b.release();
                this.b = null;
            }
            if (this.mAudioResample != null) {
                this.mAudioResample.release();
                this.mAudioResample = null;
            }
        }
        
        public void setReset() {
            this.setSpeed(1.0f);
            this.setReverse(false);
        }
        
        public void setSpeed(final float c) {
            if (c <= 0.0f) {
                TLog.w("%s setSpeed need greater than 0.", "TuSdkMediaFilePlayerSync");
                return;
            }
            if (this.c == c) {
                return;
            }
            this.pauseSave();
            this.c = c;
            if (this.mAudioResample != null) {
                this.mAudioResample.changeSpeed(c);
            }
            this.resumeSave();
        }
        
        public float speed() {
            return this.c;
        }
        
        public void setReverse(final boolean d) {
            if (this.d == d) {
                return;
            }
            this.pauseSave();
            this.d = d;
            if (this.mAudioResample != null) {
                this.mAudioResample.changeSequence(this.d);
            }
            this.resumeSave();
        }
        
        public boolean isReverse() {
            return this.d;
        }
        
        public boolean isAudioEos(final long n) {
            return (this.d && this.mMinFrameTimeUs != -1L && this.mMinFrameTimeUs == n) || (!this.d && this.mMaxFrameTimeUs != -1L && this.mMaxFrameTimeUs == n);
        }
        
        @Override
        public boolean syncAudioDecodecExtractor(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec) {
            if (this.mReleased || tuSdkMediaExtractor == null || tuSdkMediaCodec == null) {
                return true;
            }
            if (this.mFlushAndSeekto > -1L) {
                tuSdkMediaCodec.flush();
                tuSdkMediaExtractor.seekTo(this.mFlushAndSeekto);
                this.mFlushAndSeekto = -1L;
            }
            final long sampleTime = tuSdkMediaExtractor.getSampleTime();
            final boolean putBufferToCoderUntilEnd = TuSdkMediaUtils.putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec, false);
            this.mFrameIntervalUs = tuSdkMediaExtractor.getFrameIntervalUs();
            if (this.d) {
                if (this.mMinFrameTimeUs == sampleTime) {
                    tuSdkMediaExtractor.seekTo(this.mMinFrameTimeUs);
                    return false;
                }
                tuSdkMediaExtractor.seekTo(sampleTime - 1L, 0);
                return false;
            }
            else {
                if (putBufferToCoderUntilEnd || tuSdkMediaExtractor.getSampleTime() < 0L) {
                    this.mMaxFrameTimeUs = tuSdkMediaExtractor.seekTo(this.mDurationUs);
                    return false;
                }
                return false;
            }
        }
        
        @Override
        public void syncAudioDecodecInfo(final TuSdkAudioInfo tuSdkAudioInfo, final TuSdkMediaExtractor tuSdkMediaExtractor) {
            if (tuSdkAudioInfo == null || tuSdkMediaExtractor == null) {
                return;
            }
            super.syncAudioDecodecInfo(tuSdkAudioInfo, tuSdkMediaExtractor);
            (this.b = new TuSdkAudioTrackImpl(tuSdkAudioInfo)).play();
            (this.mAudioResample = new TuSdkAudioResampleHardImpl(tuSdkAudioInfo)).setMediaSync(this);
            this.mAudioResample.changeSpeed(this.speed());
        }
        
        @Override
        public void syncAudioDecodecOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkAudioInfo tuSdkAudioInfo) {
            if (bufferInfo == null || bufferInfo.size < 1 || this.mAudioResample == null || TuSdkMediaFilePlayerSync.this.c == null) {
                return;
            }
            if (!TuSdkMediaFilePlayerSync.this.c.syncWithVideo()) {
                return;
            }
            this.mAudioResample.queueInputBuffer(byteBuffer, bufferInfo);
        }
        
        @Override
        public void syncAudioResampleOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            if (TLog.LOG_AUDIO_DECODEC_INFO) {
                TuSdkCodecCapabilities.logBufferInfo(String.format("%s resampleOutputBuffer", "TuSdkMediaFilePlayerSync"), bufferInfo);
            }
            final TuSdkAudioTrack b = this.b;
            if (b == null || bufferInfo == null || bufferInfo.size < 1) {
                return;
            }
            this.mPreviousTimeUs = this.mLastTimeUs;
            this.mLastTimeUs = bufferInfo.presentationTimeUs;
            byteBuffer.position(bufferInfo.offset);
            byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
            b.write(byteBuffer);
        }
    }
    
    private class _VideoPlaySyncKey extends TuSdkVideoDecodecSyncBase
    {
        private float b;
        private boolean c;
        
        private _VideoPlaySyncKey() {
            this.b = 1.0f;
            this.c = false;
        }
        
        public void setReset() {
            this.setSpeed(1.0f);
            this.setReverse(false);
        }
        
        public void setSpeed(final float b) {
            if (b <= 0.0f) {
                TLog.w("%s setSpeed need greater than 0.", "TuSdkMediaFilePlayerSync");
                return;
            }
            if (this.b == b) {
                return;
            }
            this.pauseSave();
            this.b = b;
            this.resumeSave();
        }
        
        public float speed() {
            return this.b;
        }
        
        public void setReverse(final boolean c) {
            if (this.c == c) {
                return;
            }
            this.pauseSave();
            this.c = c;
            this.resumeSave();
        }
        
        public boolean isReverse() {
            return this.c;
        }
        
        private long a(final long n) {
            if (n < 0L) {
                return -1L;
            }
            if (this.b <= 1.0f || this.mFrameIntervalUs == 0L) {
                return n;
            }
            return (long)Math.floor(n + this.mFrameIntervalUs * this.b);
        }
        
        private long b(final long n) {
            if (n < 0L) {
                return -1L;
            }
            if (this.b <= 1.0f || this.mFrameIntervalUs == 0L) {
                return n - this.mFrameIntervalUs;
            }
            return (long)Math.ceil(n - this.mFrameIntervalUs * this.b);
        }
        
        public boolean isVideoEos(final long n) {
            return (this.c && this.mMinFrameTimeUs != -1L && this.mMinFrameTimeUs == n) || (!this.c && this.mMaxFrameTimeUs != -1L && this.mMaxFrameTimeUs == n);
        }
        
        @Override
        public boolean syncVideoDecodecExtractor(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec) {
            if (this.mReleased || tuSdkMediaExtractor == null || tuSdkMediaCodec == null) {
                return true;
            }
            if (this.mFlushAndSeekto > -1L) {
                tuSdkMediaCodec.flush();
                tuSdkMediaExtractor.seekTo(this.mFlushAndSeekto);
                this.mFlushAndSeekto = -1L;
            }
            final long sampleTime = tuSdkMediaExtractor.getSampleTime();
            final boolean putBufferToCoderUntilEnd = TuSdkMediaUtils.putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec, false);
            this.mFrameIntervalUs = tuSdkMediaExtractor.getFrameIntervalUs();
            if (this.c) {
                if (this.mMinFrameTimeUs == sampleTime) {
                    tuSdkMediaExtractor.seekTo(this.mMinFrameTimeUs);
                    return false;
                }
                tuSdkMediaExtractor.seekTo(this.b(sampleTime), 0);
                return false;
            }
            else {
                if (putBufferToCoderUntilEnd || tuSdkMediaExtractor.getSampleTime() < 0L) {
                    this.mMaxFrameTimeUs = Math.max(tuSdkMediaExtractor.seekTo(this.mDurationUs), sampleTime);
                    return false;
                }
                if (this.b > 1.0f) {
                    tuSdkMediaExtractor.seekTo(this.a(sampleTime), 2);
                }
                return false;
            }
        }
        
        private long a(final MediaCodec.BufferInfo bufferInfo) {
            final long n;
            synchronized (this.mLocker) {
                if (this.mRelativeStartNs < 0L) {
                    this.mOutputTimeUs = bufferInfo.presentationTimeUs;
                    this.mRelativeStartNs = System.nanoTime();
                }
                this.mPreviousTimeUs = this.mOutputTimeUs;
                this.mOutputTimeUs = bufferInfo.presentationTimeUs;
                final long mRelativeStartNs = this.mRelativeStartNs + (long)Math.abs((this.mOutputTimeUs - this.mPreviousTimeUs) * 1000L / this.speed());
                this.mRelativeStartNs = mRelativeStartNs;
                n = mRelativeStartNs;
            }
            return n;
        }
        
        @Override
        public void syncVideoDecodecOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkVideoInfo tuSdkVideoInfo) {
            if (bufferInfo == null || bufferInfo.size < 1) {
                return;
            }
            this.syncPlay(this.a(bufferInfo));
        }
    }
}
