// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
//import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
//import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record.TuSdkRecordSurface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class TuSdkMediaFileCuterSync implements TuSdkMediaFileSync
{
    private long a;
    private long b;
    private boolean c;
    private TuSdkAudioEncodecOperation d;
    private final TuSdkMediaFileCuterTimeline e;
    private TuSdkAudioResample f;
    private _AudioEncodecSync g;
    private _VideoEncodecSync h;
    private _AudioDecodecSync i;
    private _VideoDecodecSync j;
    private TuSdkAudioRender k;
    
    public TuSdkMediaFileCuterSync() {
        this.a = System.nanoTime();
        this.c = false;
        this.e = new TuSdkMediaFileCuterTimeline();
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
        if (this.j == null) {
            this.j = new _VideoDecodecSync();
        }
        return this.j;
    }
    
    @Override
    public TuSdkAudioDecodecSync getAudioDecodecSync() {
        if (this.i == null) {
            this.i = new _AudioDecodecSync();
        }
        return this.i;
    }
    
    @Override
    public TuSdkAudioEncodecSync getAudioEncodecSync() {
        if (this.g == null) {
            this.g = new _AudioEncodecSync();
        }
        return this.g;
    }
    
    @Override
    public TuSdkVideoEncodecSync getVideoEncodecSync() {
        if (this.h == null) {
            this.h = new _VideoEncodecSync();
        }
        return this.h;
    }
    
    @Override
    public void release() {
        if (this.c) {
            return;
        }
        this.c = true;
        this.a();
        if (this.g != null) {
            this.g.release();
            this.g = null;
        }
        if (this.h != null) {
            this.h.release();
        }
    }
    
    private void a() {
        if (this.i != null) {
            this.i.release();
            this.i = null;
        }
        if (this.j != null) {
            this.j.release();
            this.j = null;
        }
    }
    
    public void setAudioMixerRender(final TuSdkAudioRender k) {
        this.k = k;
    }
    
    @Override
    protected void finalize() {
        this.release();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    @Override
    public long benchmarkUs() {
        return this.b / 1000L;
    }
    
    @Override
    public void setBenchmarkEnd() {
        this.b = System.nanoTime() - this.a;
    }
    
    @Override
    public void addAudioEncodecOperation(final TuSdkAudioEncodecOperation d) {
        this.d = d;
    }
    
    @Override
    public void setTimeline(final TuSdkMediaTimeline tuSdkMediaTimeline) {
        this.e.fresh(tuSdkMediaTimeline);
    }
    
    @Override
    public long totalDurationUs() {
        return this.e.getOutputDurationUs();
    }
    
    @Override
    public long processedUs() {
        if (this.h == null) {
            return 0L;
        }
        return this.h.getLastTimeUs();
    }
    
    @Override
    public float calculateProgress() {
        float a = 0.0f;
        if (this.totalDurationUs() > 0L) {
            a = this.processedUs() / (float)this.totalDurationUs();
        }
        return Math.min(Math.max(a, 0.0f), 1.0f);
    }
    
    @Override
    public long lastVideoDecodecTimestampNs() {
        if (this.j == null) {
            return 0L;
        }
        return this.j.outputTimeUs() * 1000L;
    }
    
    @Override
    public boolean isEncodecCompleted() {
        return this.isVideoEncodeCompleted() && this.isAudioEncodeCompleted();
    }
    
    @Override
    public void syncVideoDecodeCompleted() {
        if (this.j == null) {
            return;
        }
        this.j.syncVideoDecodeCompleted();
    }
    
    @Override
    public boolean isVideoDecodeCompleted() {
        return this.j == null || this.j.isVideoDecodeCompleted();
    }
    
    @Override
    public void syncAudioDecodeCompleted() {
        if (this.i == null) {
            return;
        }
        this.i.syncAudioDecodeCompleted();
    }
    
    @Override
    public boolean isAudioDecodeCompleted() {
        return this.i == null || this.i.isAudioDecodeCompleted();
    }
    
    @Override
    public boolean isAudioDecodeCrashed() {
        return this.i != null && this.i.isAudioDecodeCrashed();
    }
    
    public boolean isAudioEncodeCompleted() {
        return this.g == null || this.g.isAudioEncodeCompleted();
    }
    
    public boolean isVideoEncodeCompleted() {
        return this.h == null || this.h.isVideoEncodeCompleted();
    }
    
    @Override
    public void syncVideoEncodecDrawFrame(final long n, final boolean b, final TuSdkRecordSurface tuSdkRecordSurface, final TuSdkEncodeSurface tuSdkEncodeSurface) {
        if (this.h == null) {
            return;
        }
        this.h.syncVideoEncodecDrawFrame(n, b, tuSdkRecordSurface, tuSdkEncodeSurface);
    }
    
    private void a(final TuSdkMediaTimeSliceEntity timeSlice) {
        if (this.h == null) {
            return;
        }
        this.h.setTimeSlice(timeSlice);
    }
    
    private void a(final TuSdkAudioResample tuSdkAudioResample) {
        if (tuSdkAudioResample == null) {
            return;
        }
        this.f = tuSdkAudioResample;
        if (this.i != null) {
            this.i.setAudioResample(tuSdkAudioResample);
        }
    }
    
    private class _VideoEncodecSync extends TuSdkVideoEncodecSyncBase
    {
        private TuSdkMediaTimeSliceEntity b;
        
        public void setTimeSlice(final TuSdkMediaTimeSliceEntity b) {
            this.b = b;
        }
        
        @Override
        protected boolean isLastDecodeFrame(final long n) {
            return this.b == null || (this.b.next == null && this.getInputIntervalUs() >= 1L && Math.abs(this.b.outputEndUs - n) < this.getInputIntervalUs());
        }
        
        @Override
        protected boolean needSkip(final long n) {
            if (this.b == null) {
                return true;
            }
            if (!this.hadLockVideoTimestampUs(n)) {
                return true;
            }
            final int overviewOutput = this.b.overviewOutput(n);
            if (overviewOutput != 0) {
                if (overviewOutput > 0) {
                    this.b = this.b.next;
                }
                return true;
            }
            return false;
        }
    }
    
    private class _VideoDecodecSync extends TuSdkVideoDecodecSyncBase
    {
        private TuSdkMediaTimeSliceEntity b;
        private TuSdkMediaTimeSliceEntity c;
        private boolean d;
        
        private _VideoDecodecSync() {
            this.d = false;
        }
        
        private void a(final TuSdkMediaExtractor tuSdkMediaExtractor) {
            if (this.b == null || tuSdkMediaExtractor == null) {
                return;
            }
            this.d = false;
            this.b = this.b.next;
            if (this.b != null) {
                tuSdkMediaExtractor.seekTo(this.b.startUs, 0);
            }
        }
        
        @Override
        public void syncVideoDecodecInfo(final TuSdkVideoInfo tuSdkVideoInfo, final TuSdkMediaExtractor tuSdkMediaExtractor) {
            if (tuSdkVideoInfo == null || tuSdkMediaExtractor == null) {
                return;
            }
            super.syncVideoDecodecInfo(tuSdkVideoInfo, tuSdkMediaExtractor);
            TuSdkMediaFileCuterSync.this.e.setInputDurationUs(tuSdkVideoInfo.durationUs);
            TuSdkMediaFileCuterSync.this.e.fixTimeSlices(tuSdkMediaExtractor, false);
            if (TuSdkMediaFileCuterSync.this.e.getOutputDurationUs() < 1L) {
                TLog.w("%s cuter the timeline is too short.", "TuSdkMediaFileCuterSync");
            }
            final TuSdkMediaTimeSliceEntity firstSlice = TuSdkMediaFileCuterSync.this.e.firstSlice();
            this.b = firstSlice;
            this.c = firstSlice;
            TuSdkMediaFileCuterSync.this.a(this.b);
            if (this.b != null) {
                tuSdkMediaExtractor.seekTo(this.b.startUs, 0);
            }
        }
        
        @Override
        public boolean syncVideoDecodecExtractor(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec) {
            if (tuSdkMediaExtractor == null) {
                return true;
            }
            if (this.b == null) {
                TuSdkMediaUtils.putEosToCoder(tuSdkMediaExtractor, tuSdkMediaCodec);
                return true;
            }
            final long sampleTime = tuSdkMediaExtractor.getSampleTime();
            final boolean putBufferToCoderUntilEnd = TuSdkMediaUtils.putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec, false);
            this.mFrameIntervalUs = tuSdkMediaExtractor.getFrameIntervalUs();
            final int overview = this.b.overview(sampleTime);
            if (!this.d && overview < 0) {
                this.d = true;
                tuSdkMediaExtractor.seekTo(this.b.startUs, 0);
            }
            if (overview > 0) {
                this.a(tuSdkMediaExtractor);
            }
            else if (putBufferToCoderUntilEnd || tuSdkMediaExtractor.getSampleTime() < 0L) {
                this.mMaxFrameTimeUs = tuSdkMediaExtractor.seekTo(this.mDurationUs);
                this.a(tuSdkMediaExtractor);
            }
            return false;
        }
        
        @Override
        public void syncVideoDecodecOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkVideoInfo tuSdkVideoInfo) {
            final _VideoEncodecSync d = TuSdkMediaFileCuterSync.this.h;
            if (bufferInfo == null || this.c == null || bufferInfo.size < 1 || d == null) {
                return;
            }
            final long presentationTimeUs = bufferInfo.presentationTimeUs;
            bufferInfo.presentationTimeUs = this.c.calOutputTimeUs(presentationTimeUs);
            final int overview = this.c.overview(presentationTimeUs);
            if (overview != 0) {
                if (overview > 0) {
                    this.c = this.c.next;
                }
                return;
            }
            final long currentTimeMillis = System.currentTimeMillis();
            while (!this.isInterrupted() && d.hasLocked()) {
                if (System.currentTimeMillis() - currentTimeMillis > 500L) {
                    d.clearLocker();
                    break;
                }
            }
            this.mPreviousTimeUs = this.mOutputTimeUs;
            d.lockVideoTimestampUs(this.mOutputTimeUs = bufferInfo.presentationTimeUs);
        }
    }
    
    private class _AudioEncodecSync extends TuSdkAudioEncodecSyncBase
    {
        @Override
        public void syncAudioEncodecInfo(final TuSdkAudioInfo tuSdkAudioInfo) {
            super.syncAudioEncodecInfo(tuSdkAudioInfo);
            TuSdkMediaFileCuterSync.this.a(this.getAudioResample());
        }
    }
    
    private class _AudioDecodecSync extends TuSdkAudioDecodecSyncBase
    {
        private boolean b;
        private long c;
        private TuSdkMediaTimeSliceEntity d;
        private TuSdkMediaTimeSliceEntity e;
        private TuSdkAudioInfo f;
        private TuSdkAudioRender.TuSdkAudioRenderCallback g;
        
        private _AudioDecodecSync() {
            this.b = false;
            this.c = -1L;
            this.g = new TuSdkAudioRender.TuSdkAudioRenderCallback() {
                @Override
                public boolean isEncodec() {
                    return false;
                }
                
                @Override
                public TuSdkAudioInfo getAudioInfo() {
                    return _AudioDecodecSync.this.f;
                }
                
                @Override
                public void returnRenderBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                    _AudioDecodecSync.this.a(byteBuffer, bufferInfo);
                }
            };
        }
        
        private void a(final TuSdkMediaExtractor tuSdkMediaExtractor) {
            if (this.d == null || tuSdkMediaExtractor == null) {
                return;
            }
            this.a(tuSdkMediaExtractor, this.d = this.d.next);
        }
        
        private void a(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity) {
            if (tuSdkMediaTimeSliceEntity == null || tuSdkMediaExtractor == null) {
                return;
            }
            tuSdkMediaTimeSliceEntity.audioEndUs = tuSdkMediaExtractor.seekTo(tuSdkMediaTimeSliceEntity.endUs, !tuSdkMediaTimeSliceEntity.isReverse());
            tuSdkMediaTimeSliceEntity.audioStartUs = tuSdkMediaExtractor.seekTo(tuSdkMediaTimeSliceEntity.startUs, tuSdkMediaTimeSliceEntity.isReverse());
        }
        
        private void a(final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity) {
            if (this.mAudioResample == null || tuSdkMediaTimeSliceEntity == null) {
                return;
            }
            synchronized (this.mLocker) {
                this.mAudioResample.changeSpeed(tuSdkMediaTimeSliceEntity.speed);
                this.mAudioResample.changeSequence(tuSdkMediaTimeSliceEntity.isReverse() && tuSdkMediaTimeSliceEntity.isAudioReverse());
            }
        }
        
        @Override
        public void syncAudioDecodecInfo(final TuSdkAudioInfo f, final TuSdkMediaExtractor tuSdkMediaExtractor) {
            if (f == null || tuSdkMediaExtractor == null) {
                return;
            }
            super.syncAudioDecodecInfo(f, tuSdkMediaExtractor);
            this.f = f;
            while (!this.isInterrupted()) {
                if (this.mAudioResample != null) {
                    if (!TuSdkMediaFileCuterSync.this.e.isFixTimeSlices()) {
                        continue;
                    }
                    break;
                }
            }
            if (TuSdkMediaFileCuterSync.this.e.isFixTimeSlices()) {
                final TuSdkMediaTimeSliceEntity firstSlice = TuSdkMediaFileCuterSync.this.e.firstSlice();
                this.d = firstSlice;
                this.e = firstSlice;
                if (this.d != null) {
                    this.c = this.d.startUs;
                    this.a(tuSdkMediaExtractor, this.d);
                }
            }
            if (this.mAudioResample != null) {
                this.mAudioResample.changeFormat(f);
                this.a(this.d);
            }
        }
        
        @Override
        public boolean syncAudioDecodecExtractor(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec) {
            if (tuSdkMediaExtractor == null) {
                return true;
            }
            if (this.d == null) {
                TuSdkMediaUtils.putEosToCoder(tuSdkMediaExtractor, tuSdkMediaCodec);
                return true;
            }
            final long sampleTime = tuSdkMediaExtractor.getSampleTime();
            final boolean putBufferToCoderUntilEnd = TuSdkMediaUtils.putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec, false);
            this.mFrameIntervalUs = tuSdkMediaExtractor.getFrameIntervalUs();
            if (this.d.overview(sampleTime) > 0) {
                this.a(tuSdkMediaExtractor);
            }
            else if (putBufferToCoderUntilEnd || tuSdkMediaExtractor.getSampleTime() < 0L) {
                this.mMaxFrameTimeUs = tuSdkMediaExtractor.seekTo(this.mDurationUs);
                this.a(tuSdkMediaExtractor);
            }
            return false;
        }
        
        @Override
        public void syncAudioDecodecOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkAudioInfo tuSdkAudioInfo) {
            final TuSdkAudioEncodecOperation b = TuSdkMediaFileCuterSync.this.d;
            final TuSdkAudioResample mAudioResample = this.mAudioResample;
            final TuSdkMediaTimeSliceEntity e = this.e;
            if (b == null || mAudioResample == null || e == null || bufferInfo == null || bufferInfo.size < 1) {
                return;
            }
            final long presentationTimeUs = bufferInfo.presentationTimeUs;
            bufferInfo.presentationTimeUs = e.calOutputTimeUs(presentationTimeUs);
            final int overview = e.overview(presentationTimeUs);
            if (overview != 0) {
                if (overview > 0) {
                    this.a(this.e = e.next);
                }
                return;
            }
            if (!this.b) {
                this.b = true;
                if (bufferInfo.presentationTimeUs > this.c) {
                    b.autoFillMuteDataWithinEnd(this.c, bufferInfo.presentationTimeUs);
                }
            }
            mAudioResample.queueInputBuffer(byteBuffer, TuSdkMediaUtils.cloneBufferInfo(bufferInfo));
        }
        
        @Override
        public void syncAudioResampleOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            if (TLog.LOG_AUDIO_DECODEC_INFO) {
                TuSdkCodecCapabilities.logBufferInfo(String.format("%s resampleOutputBuffer", "TuSdkMediaFileCuterSync"), bufferInfo);
            }
            final TuSdkAudioRender c = TuSdkMediaFileCuterSync.this.k;
            if (c == null || !c.onAudioSliceRender(byteBuffer, bufferInfo, this.g)) {
                this.a(byteBuffer, bufferInfo);
            }
        }
        
        private void a(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            final TuSdkAudioEncodecOperation b = TuSdkMediaFileCuterSync.this.d;
            if (b == null || bufferInfo == null || bufferInfo.size < 1) {
                return;
            }
            this.mPreviousTimeUs = this.mLastTimeUs;
            this.mLastTimeUs = bufferInfo.presentationTimeUs;
            byteBuffer.position(bufferInfo.offset);
            byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
            while (!this.isInterrupted() && b.writeBuffer(byteBuffer, bufferInfo) == 0) {}
        }
    }
}
