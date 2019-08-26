// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import java.util.HashMap;
//import org.lasque.tusdk.core.utils.hardware.HardwareHelper;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
//import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
//import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import java.util.Map;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
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
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.HardwareHelper;

@TargetApi(18)
public class TuSdkMediaFileDirectorSync implements TuSdkMediaFileSync
{
    private static final Map<String, String> a;
    private static final Map<String, String> b;
    private long c;
    private long d;
    private boolean e;
    private TuSdkAudioEncodecOperation f;
    private TuSdkMediaFileCuterTimeline g;
    private TuSdkAudioResample h;
    private _AudioEncodecSync i;
    private _VideoEncodecSync j;
    private _AudioDecodecSync k;
    private _VideoDecodecSync l;
    private TuSdkAudioRender m;
    
    public TuSdkMediaFileDirectorSync() {
        this.c = System.nanoTime();
        this.e = false;
        this.g = new TuSdkMediaFileCuterTimeline();
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
        if (this.l == null) {
            this.l = new _VideoDecodecSync();
        }
        return this.l;
    }
    
    @Override
    public TuSdkAudioDecodecSync getAudioDecodecSync() {
        if (this.k == null) {
            this.k = new _AudioDecodecSync();
        }
        return this.k;
    }
    
    @Override
    public TuSdkAudioEncodecSync getAudioEncodecSync() {
        if (this.i == null) {
            this.i = new _AudioEncodecSync();
        }
        return this.i;
    }
    
    @Override
    public TuSdkVideoEncodecSync getVideoEncodecSync() {
        if (this.j == null) {
            this.j = new _VideoEncodecSync();
        }
        return this.j;
    }
    
    @Override
    public void release() {
        if (this.e) {
            return;
        }
        this.e = true;
        this.a();
        if (this.i != null) {
            this.i.release();
            this.i = null;
        }
        if (this.j != null) {
            this.j.release();
        }
    }
    
    private void a() {
        if (this.k != null) {
            this.k.release();
            this.k = null;
        }
        if (this.l != null) {
            this.l.release();
            this.l = null;
        }
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
        return this.d / 1000L;
    }
    
    @Override
    public void setBenchmarkEnd() {
        this.d = System.nanoTime() - this.c;
    }
    
    @Override
    public void addAudioEncodecOperation(final TuSdkAudioEncodecOperation f) {
        this.f = f;
    }
    
    public void setAudioMixerRender(final TuSdkAudioRender m) {
        this.m = m;
    }
    
    @Override
    public void setTimeline(final TuSdkMediaTimeline tuSdkMediaTimeline) {
        this.g = (TuSdkMediaFileCuterTimeline)tuSdkMediaTimeline;
    }
    
    public TuSdkMediaFileCuterTimeline getTimeLine() {
        return this.g;
    }
    
    @Override
    public long totalDurationUs() {
        return this.g.getOutputDurationUs();
    }
    
    @Override
    public long processedUs() {
        if (this.j == null) {
            return 0L;
        }
        return this.j.getLastTimeUs();
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
        if (this.l == null) {
            return 0L;
        }
        return this.l.outputTimeUs() * 1000L;
    }
    
    @Override
    public boolean isEncodecCompleted() {
        return this.isVideoEncodeCompleted() && this.isAudioEncodeCompleted();
    }
    
    @Override
    public void syncVideoDecodeCompleted() {
        if (this.l == null) {
            return;
        }
        this.l.syncVideoDecodeCompleted();
    }
    
    @Override
    public boolean isVideoDecodeCompleted() {
        return this.l == null || this.l.isVideoDecodeCompleted();
    }
    
    @Override
    public void syncAudioDecodeCompleted() {
        if (this.k == null) {
            return;
        }
        this.k.syncAudioDecodeCompleted();
    }
    
    @Override
    public boolean isAudioDecodeCompleted() {
        return this.k == null || this.k.isAudioDecodeCompleted();
    }
    
    @Override
    public boolean isAudioDecodeCrashed() {
        return this.k != null && this.k.isAudioDecodeCrashed();
    }
    
    public boolean isAudioEncodeCompleted() {
        return this.i == null || this.i.isAudioEncodeCompleted();
    }
    
    public boolean isVideoEncodeCompleted() {
        return this.j == null || this.j.isVideoEncodeCompleted();
    }
    
    @Override
    public void syncVideoEncodecDrawFrame(final long n, final boolean b, final TuSdkRecordSurface tuSdkRecordSurface, final TuSdkEncodeSurface tuSdkEncodeSurface) {
        if (this.j == null) {
            return;
        }
        this.j.syncVideoEncodecDrawFrame(n, b, tuSdkRecordSurface, tuSdkEncodeSurface);
    }
    
    private void a(final TuSdkMediaTimeSliceEntity timeSlice) {
        if (this.j == null) {
            return;
        }
        this.j.setTimeSlice(timeSlice);
    }
    
    private void a(final TuSdkAudioResample tuSdkAudioResample) {
        if (tuSdkAudioResample == null) {
            return;
        }
        this.h = tuSdkAudioResample;
        if (this.k != null) {
            this.k.setAudioResample(tuSdkAudioResample);
        }
    }
    
    private boolean b() {
        boolean matchDeviceModelAndManuFacturer = false;
        for (final Map.Entry<String, String> entry : TuSdkMediaFileDirectorSync.a.entrySet()) {
            matchDeviceModelAndManuFacturer = HardwareHelper.isMatchDeviceModelAndManuFacturer(entry.getKey(), entry.getValue());
            if (matchDeviceModelAndManuFacturer) {
                break;
            }
        }
        return matchDeviceModelAndManuFacturer;
    }
    
    private boolean c() {
        boolean matchDeviceModelAndManuFacturer = false;
        for (final Map.Entry<String, String> entry : TuSdkMediaFileDirectorSync.b.entrySet()) {
            matchDeviceModelAndManuFacturer = HardwareHelper.isMatchDeviceModelAndManuFacturer(entry.getKey(), entry.getValue());
            if (matchDeviceModelAndManuFacturer) {
                break;
            }
        }
        return matchDeviceModelAndManuFacturer;
    }
    
    static {
        a = new HashMap<String, String>();
        b = new HashMap<String, String>();
        TuSdkMediaFileDirectorSync.a.put("PADM00", "OPPO");
        TuSdkMediaFileDirectorSync.a.put("PACT00", "OPPO");
        TuSdkMediaFileDirectorSync.a.put("MI 6", "Xiaomi");
        TuSdkMediaFileDirectorSync.b.put("OD103", "Smartisan");
        TuSdkMediaFileDirectorSync.b.put("OS105", "Smartisan");
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
        private TuSdkMediaTimeSliceEntity c;
        private TuSdkMediaTimeSliceEntity d;
        private boolean e;
        private TuSdkMediaExtractor f;
        boolean a;
        private long g;
        
        private _VideoDecodecSync() {
            this.e = false;
            this.a = false;
            this.g = 0L;
        }
        
        private void a(final TuSdkMediaExtractor tuSdkMediaExtractor) {
            if (this.c == null || tuSdkMediaExtractor == null) {
                return;
            }
            this.e = false;
            this.c = this.c.next;
            if (this.c != null) {
                tuSdkMediaExtractor.seekTo(this.c.startUs, this.isSupportPrecise() ? 2 : 0);
            }
        }
        
        private long a(final long n) {
            if (n < 0L || this.c == null) {
                return -1L;
            }
            if (this.c.speed <= 1.0f || this.mFrameIntervalUs == 0L) {
                return n;
            }
            return (long)Math.floor(n + this.mFrameIntervalUs * this.c.speed);
        }
        
        private long b(final long n) {
            if (n < 0L || this.c == null) {
                return -1L;
            }
            if (this.c.speed <= 1.0f || this.mFrameIntervalUs == 0L) {
                return n - this.mFrameIntervalUs;
            }
            return (long)Math.ceil(n - this.mFrameIntervalUs * this.c.speed);
        }
        
        @Override
        public void syncVideoDecodecInfo(final TuSdkVideoInfo tuSdkVideoInfo, final TuSdkMediaExtractor f) {
            if (tuSdkVideoInfo == null || f == null) {
                return;
            }
            super.syncVideoDecodecInfo(tuSdkVideoInfo, f);
            this.f = f;
            TuSdkMediaFileDirectorSync.this.g.setInputDurationUs(tuSdkVideoInfo.durationUs);
            TuSdkMediaFileDirectorSync.this.g.fixTimeSlices(f, this.isSupportPrecise(), true);
            if (TuSdkMediaFileDirectorSync.this.g.getOutputDurationUs() < 1L) {
                TLog.w("%s cuter the timeline is too short.", "TuSdkMediaFileDirectorSync");
            }
            final TuSdkMediaTimeSliceEntity firstSlice = TuSdkMediaFileDirectorSync.this.g.firstSlice();
            this.c = firstSlice;
            this.d = firstSlice;
            TuSdkMediaFileDirectorSync.this.a(this.c);
            if (this.c != null) {
                f.seekTo(this.c.startUs, this.isSupportPrecise() ? 2 : 0);
            }
        }
        
        @Override
        public boolean syncVideoDecodecExtractor(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec) {
            if (tuSdkMediaExtractor == null) {
                return true;
            }
            if (this.c == null) {
                TuSdkMediaUtils.putEosToCoder(tuSdkMediaExtractor, tuSdkMediaCodec);
                return true;
            }
            final long sampleTime = tuSdkMediaExtractor.getSampleTime();
            final int overview = this.c.overview(sampleTime);
            final boolean putBufferToCoderUntilEnd = TuSdkMediaUtils.putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec, false);
            this.mFrameIntervalUs = tuSdkMediaExtractor.getFrameIntervalUs();
            if (!this.isSupportPrecise() && !this.e && overview < 0) {
                this.e = true;
                tuSdkMediaExtractor.seekTo(this.c.startUs, 0);
            }
            else if (overview > 0) {
                this.a(tuSdkMediaExtractor);
            }
            else if (this.c.isReverse()) {
                if (this.mMinFrameTimeUs == sampleTime) {
                    this.a(tuSdkMediaExtractor);
                    return false;
                }
                long b = this.b(sampleTime);
                if (TuSdkMediaFileDirectorSync.this.c()) {
                    b -= 110L;
                }
                tuSdkMediaExtractor.seekTo(b, 0);
            }
            else if (putBufferToCoderUntilEnd || tuSdkMediaExtractor.getSampleTime() < 0L) {
                this.mMaxFrameTimeUs = tuSdkMediaExtractor.seekTo(this.mDurationUs);
                this.a(tuSdkMediaExtractor);
            }
            else if (this.isSupportPrecise() && this.c.speed > 1.0f) {
                tuSdkMediaExtractor.seekTo(this.a(sampleTime), 2);
            }
            return false;
        }
        
        @Override
        public void syncVideoDecodecOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkVideoInfo tuSdkVideoInfo) {
            final _VideoEncodecSync f = TuSdkMediaFileDirectorSync.this.j;
            if (bufferInfo == null || this.d == null || bufferInfo.size < 1 || f == null || this.f == null) {
                return;
            }
            final long presentationTimeUs = bufferInfo.presentationTimeUs;
            final int overview = this.d.overview(presentationTimeUs);
            if (this.f.getSampleTime() < presentationTimeUs && this.c != null && !this.c.isReverse() && this.d != null && !this.d.isReverse()) {
                this.d = this.c;
                return;
            }
            if (this.a && this.f.getSampleTime() > presentationTimeUs) {
                this.a = false;
            }
            if ((this.f.getSampleTime() <= presentationTimeUs && !this.d.isReverse() && TuSdkMediaFileDirectorSync.this.b() && !this.a) || overview > 0) {
                this.d = this.d.next;
                this.a = true;
                return;
            }
            bufferInfo.presentationTimeUs = this.d.calOutputTimeUs(presentationTimeUs);
            final long currentTimeMillis = System.currentTimeMillis();
            while (!this.isInterrupted() && f.hasLocked()) {
                if (System.currentTimeMillis() - currentTimeMillis > 500L) {
                    f.clearLocker();
                    break;
                }
            }
            this.mPreviousTimeUs = this.mOutputTimeUs;
            f.lockVideoTimestampUs(this.mOutputTimeUs = bufferInfo.presentationTimeUs);
        }
    }
    
    private class _AudioEncodecSync extends TuSdkAudioEncodecSyncBase
    {
        @Override
        public void syncAudioEncodecInfo(final TuSdkAudioInfo tuSdkAudioInfo) {
            super.syncAudioEncodecInfo(tuSdkAudioInfo);
            TuSdkMediaFileDirectorSync.this.a(this.getAudioResample());
        }
        
        public TuSdkAudioInfo getEncodeAudioInfo() {
            return this.mAudioInfo;
        }
    }
    
    private class _AudioDecodecSync extends TuSdkAudioDecodecSyncBase implements TuSdkAudioPitchSync
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
            if (tuSdkMediaTimeSliceEntity.isReverse()) {
                if (tuSdkMediaTimeSliceEntity.isAudioReverse()) {
                    tuSdkMediaTimeSliceEntity.audioEndUs = tuSdkMediaExtractor.seekTo(tuSdkMediaTimeSliceEntity.endUs, !tuSdkMediaTimeSliceEntity.isReverse());
                    tuSdkMediaTimeSliceEntity.audioStartUs = tuSdkMediaExtractor.seekTo(tuSdkMediaTimeSliceEntity.startUs, tuSdkMediaTimeSliceEntity.isReverse());
                }
                else {
                    tuSdkMediaTimeSliceEntity.audioEndUs = tuSdkMediaExtractor.seekTo(tuSdkMediaTimeSliceEntity.startUs, tuSdkMediaTimeSliceEntity.isReverse());
                    tuSdkMediaTimeSliceEntity.audioStartUs = tuSdkMediaExtractor.seekTo(tuSdkMediaTimeSliceEntity.endUs, !tuSdkMediaTimeSliceEntity.isReverse());
                }
            }
            else {
                tuSdkMediaTimeSliceEntity.audioEndUs = tuSdkMediaExtractor.seekTo(tuSdkMediaTimeSliceEntity.endUs, !tuSdkMediaTimeSliceEntity.isReverse());
                if (tuSdkMediaTimeSliceEntity.audioEndUs < 0L) {
                    tuSdkMediaTimeSliceEntity.audioEndUs = tuSdkMediaExtractor.seekTo(this.f.durationUs, !tuSdkMediaTimeSliceEntity.isReverse());
                }
                tuSdkMediaTimeSliceEntity.audioStartUs = tuSdkMediaExtractor.seekTo(tuSdkMediaTimeSliceEntity.startUs, tuSdkMediaTimeSliceEntity.isReverse());
            }
        }
        
        private void a(final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity) {
            if (this.mAudioResample == null || this.mAudioPitch == null || tuSdkMediaTimeSliceEntity == null) {
                return;
            }
            synchronized (this.mLocker) {
                this.mAudioPitch.flush();
                this.mAudioPitch.changeSpeed(tuSdkMediaTimeSliceEntity.speed);
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
            if (this.mAudioPitch == null) {
                (this.mAudioPitch = new TuSdkAudioPitchHardImpl(TuSdkMediaFileDirectorSync.this.i.getEncodeAudioInfo())).changeFormat(TuSdkMediaFileDirectorSync.this.i.getEncodeAudioInfo());
                this.mAudioPitch.setMediaSync(this);
            }
            while (!this.isInterrupted()) {
                if (this.mAudioResample != null) {
                    if (!TuSdkMediaFileDirectorSync.this.g.isFixTimeSlices()) {
                        continue;
                    }
                    break;
                }
            }
            if (TuSdkMediaFileDirectorSync.this.g.isFixTimeSlices()) {
                final TuSdkMediaTimeSliceEntity firstSlice = TuSdkMediaFileDirectorSync.this.g.firstSlice();
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
            TLog.d("timeSlice = " + this.d, new Object[0]);
            if (this.d == null) {
                TuSdkMediaUtils.putEosToCoder(tuSdkMediaExtractor, tuSdkMediaCodec);
                return true;
            }
            final long sampleTime = tuSdkMediaExtractor.getSampleTime();
            final boolean putBufferToCoderUntilEnd = TuSdkMediaUtils.putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec, false);
            this.mFrameIntervalUs = tuSdkMediaExtractor.getFrameIntervalUs();
            if (this.d.overviewAudio(sampleTime) > 0) {
                this.a(tuSdkMediaExtractor);
            }
            else if (this.d.isReverse() && this.d.isAudioReverse()) {
                if (this.mMinFrameTimeUs == sampleTime) {
                    this.a(tuSdkMediaExtractor);
                    return false;
                }
                tuSdkMediaExtractor.seekTo(sampleTime - 1L, 0);
            }
            else if (putBufferToCoderUntilEnd || tuSdkMediaExtractor.getSampleTime() < 0L) {
                this.mMaxFrameTimeUs = tuSdkMediaExtractor.seekTo(this.mDurationUs);
                this.a(tuSdkMediaExtractor);
            }
            return false;
        }
        
        @Override
        public void syncAudioDecodecOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkAudioInfo tuSdkAudioInfo) {
            final TuSdkAudioEncodecOperation c = TuSdkMediaFileDirectorSync.this.f;
            final TuSdkAudioResample mAudioResample = this.mAudioResample;
            final TuSdkMediaTimeSliceEntity e = this.e;
            if (c == null || mAudioResample == null || e == null || bufferInfo == null || bufferInfo.size < 1) {
                return;
            }
            final long presentationTimeUs = bufferInfo.presentationTimeUs;
            bufferInfo.presentationTimeUs = e.calOutputAudioTimeUs(presentationTimeUs);
            final int overviewAudio = this.e.overviewAudio(presentationTimeUs);
            if (overviewAudio < 0) {
                return;
            }
            if (overviewAudio <= 0) {
                if (!this.b) {
                    this.b = true;
                    if (bufferInfo.presentationTimeUs > this.c) {
                        c.autoFillMuteDataWithinEnd(this.c, bufferInfo.presentationTimeUs);
                    }
                }
                final MediaCodec.BufferInfo cloneBufferInfo = TuSdkMediaUtils.cloneBufferInfo(bufferInfo);
                cloneBufferInfo.presentationTimeUs = e.calOutputOrginTimeUs(presentationTimeUs);
                mAudioResample.queueInputBuffer(byteBuffer, cloneBufferInfo);
                return;
            }
            this.e = this.e.next;
            if (this.e == null) {
                return;
            }
            this.a(this.e);
            if (this.e.overviewAudio(presentationTimeUs) == 0) {
                bufferInfo.presentationTimeUs = this.e.calOutputTimeUs(presentationTimeUs);
            }
        }
        
        @Override
        public void syncAudioResampleOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            if (TLog.LOG_AUDIO_DECODEC_INFO) {
                TuSdkCodecCapabilities.logBufferInfo(String.format("%s resampleOutputBuffer", "TuSdkMediaFileDirectorSync"), bufferInfo);
            }
            this.mAudioPitch.queueInputBuffer(byteBuffer, bufferInfo);
        }
        
        private void a(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            final TuSdkAudioEncodecOperation c = TuSdkMediaFileDirectorSync.this.f;
            if (c == null || bufferInfo == null || bufferInfo.size < 1) {
                return;
            }
            this.mPreviousTimeUs = this.mLastTimeUs;
            this.mLastTimeUs = bufferInfo.presentationTimeUs;
            byteBuffer.position(bufferInfo.offset);
            byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
            while (!this.isInterrupted() && c.writeBuffer(byteBuffer, bufferInfo) == 0) {}
        }
        
        @Override
        public void syncAudioPitchOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            final TuSdkAudioRender d = TuSdkMediaFileDirectorSync.this.m;
            if (d == null || !d.onAudioSliceRender(byteBuffer, bufferInfo, this.g)) {
                this.a(byteBuffer, bufferInfo);
            }
        }
    }
}
