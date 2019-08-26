// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
//import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
//import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record.TuSdkRecordSurface;

@TargetApi(18)
public class TuSdkMediaFileTrascoderSync implements TuSdkMediaDecodecSync, TuSdkMediaEncodecSync
{
    private long a;
    private long b;
    private boolean c;
    private int d;
    private int e;
    private boolean f;
    private long g;
    private long h;
    private long i;
    private long j;
    private long k;
    private long l;
    private TuSdkAudioEncodecOperation m;
    private TuSdkAudioResample n;
    private _AudioEncodecSync o;
    private _VideoEncodecSync p;
    private _AudioDecodecSync q;
    private _VideoDecodecSync r;
    
    public TuSdkMediaFileTrascoderSync() {
        this.a = System.nanoTime();
        this.c = false;
        this.d = -1;
        this.e = 0;
        this.f = false;
        this.g = 0L;
        this.h = 0L;
        this.i = 0L;
        this.j = 0L;
        this.k = 0L;
        this.l = 0L;
    }
    
    @Override
    public TuSdkAudioDecodecSync buildAudioDecodecSync() {
        if (this.q != null) {
            this.q.release();
            this.q = null;
        }
        (this.q = new _AudioDecodecSync()).setAudioResample(this.n);
        return this.q;
    }
    
    @Override
    public TuSdkVideoDecodecSync buildVideoDecodecSync() {
        if (this.r != null) {
            this.r.release();
            this.r = null;
        }
        return this.r = new _VideoDecodecSync();
    }
    
    @Override
    public TuSdkVideoDecodecSync getVideoDecodecSync() {
        return this.r;
    }
    
    @Override
    public TuSdkAudioDecodecSync getAudioDecodecSync() {
        return this.q;
    }
    
    @Override
    public TuSdkAudioEncodecSync getAudioEncodecSync() {
        if (this.o == null) {
            this.o = new _AudioEncodecSync();
        }
        return this.o;
    }
    
    @Override
    public TuSdkVideoEncodecSync getVideoEncodecSync() {
        if (this.p == null) {
            this.p = new _VideoEncodecSync();
        }
        return this.p;
    }
    
    @Override
    public void release() {
        this.c = true;
        this.a();
        if (this.o != null) {
            this.o.release();
            this.o = null;
        }
        if (this.p != null) {
            this.p.release();
            this.p = null;
        }
    }
    
    private void a() {
        if (this.q != null) {
            this.q.release();
            this.q = null;
        }
        if (this.r != null) {
            this.r.release();
            this.r = null;
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
    
    public void addAudioEncodecOperation(final TuSdkAudioEncodecOperation m) {
        this.m = m;
    }
    
    public void setBenchmarkEnd() {
        this.b = System.nanoTime() - this.a;
    }
    
    public long benchmarkUs() {
        return this.b / 1000L;
    }
    
    public long totalDurationUs() {
        return this.g + Math.max(this.h, this.l);
    }
    
    public long lastVideoEndTimeUs() {
        return this.g;
    }
    
    public long lastVideoDecodecTimestampNs() {
        return this.k * 1000L;
    }
    
    public void setTotal(final int e) {
        this.e = e;
    }
    
    public int total() {
        return this.e;
    }
    
    public boolean isLast() {
        return this.d + 1 >= this.e;
    }
    
    public int lastIndex() {
        return this.d;
    }
    
    public boolean isEncodecCompleted() {
        return this.isVideoEncodeCompleted() && this.isAudioEncodeCompleted();
    }
    
    public float calculateProgress() {
        float a = 0.0f;
        if (this.h > 0L) {
            a = (this.d + (this.k - this.g) / (float)this.h) / this.e;
        }
        return Math.min(Math.max(a, 0.0f), 1.0f);
    }
    
    public boolean syncDecodecNext() {
        if (this.d > -1 && (!this.isAudioDecodeCompleted() || !this.isVideoDecodeCompleted())) {
            return false;
        }
        if (this.isLast()) {
            return false;
        }
        this.a();
        ++this.d;
        this.f = false;
        this.g += Math.max(this.h, this.l);
        this.j = 0L;
        if (this.p != null) {
            this.p.clearLocker();
        }
        return true;
    }
    
    public void syncVideoDecodeCompleted() {
        if (this.r == null) {
            return;
        }
        this.r.syncVideoDecodeCompleted();
    }
    
    public boolean isVideoDecodeCompleted() {
        return this.r == null || this.r.isVideoDecodeCompleted();
    }
    
    public void syncAudioDecodeCompleted() {
        if (this.q == null) {
            return;
        }
        this.q.syncAudioDecodeCompleted();
    }
    
    public boolean isAudioDecodeCompleted() {
        return this.q == null || this.q.isAudioDecodeCompleted();
    }
    
    public boolean isAudioDecodeCrashed() {
        return this.q != null && this.q.isAudioDecodeCrashed();
    }
    
    public boolean isAudioEncodeCompleted() {
        return this.o == null || this.o.isAudioEncodeCompleted();
    }
    
    public boolean isVideoEncodeCompleted() {
        return this.p == null || this.p.isVideoEncodeCompleted();
    }
    
    public void syncVideoEncodecDrawFrame(final long n, final boolean b, final TuSdkRecordSurface tuSdkRecordSurface, final TuSdkEncodeSurface tuSdkEncodeSurface) {
        if (this.p == null) {
            return;
        }
        this.p.syncVideoEncodecDrawFrame(n, b, tuSdkRecordSurface, tuSdkEncodeSurface);
    }
    
    private void a(final TuSdkAudioResample tuSdkAudioResample) {
        if (tuSdkAudioResample == null) {
            return;
        }
        this.n = tuSdkAudioResample;
        if (this.q != null) {
            this.q.setAudioResample(tuSdkAudioResample);
        }
    }
    
    private class _VideoEncodecSync extends TuSdkVideoEncodecSyncBase
    {
        @Override
        protected boolean isLastDecodeFrame(final long n) {
            return TuSdkMediaFileTrascoderSync.this.i >= 1L && TuSdkMediaFileTrascoderSync.this.j >= 1L && TuSdkMediaFileTrascoderSync.this.i - n < TuSdkMediaFileTrascoderSync.this.j;
        }
        
        @Override
        protected long getInputIntervalUs() {
            return TuSdkMediaFileTrascoderSync.this.j;
        }
    }
    
    private class _VideoDecodecSync extends TuSdkVideoDecodecSyncBase
    {
        @Override
        public boolean syncVideoDecodecExtractor(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec) {
            final boolean syncVideoDecodecExtractor = super.syncVideoDecodecExtractor(tuSdkMediaExtractor, tuSdkMediaCodec);
            TuSdkMediaFileTrascoderSync.this.j = this.mFrameIntervalUs;
            return syncVideoDecodecExtractor;
        }
        
        @Override
        public void syncVideoDecodecInfo(final TuSdkVideoInfo tuSdkVideoInfo, final TuSdkMediaExtractor tuSdkMediaExtractor) {
            if (tuSdkVideoInfo == null || tuSdkMediaExtractor == null) {
                return;
            }
            super.syncVideoDecodecInfo(tuSdkVideoInfo, tuSdkMediaExtractor);
            TuSdkMediaFileTrascoderSync.this.h = tuSdkVideoInfo.durationUs;
            TuSdkMediaFileTrascoderSync.this.i = TuSdkMediaFileTrascoderSync.this.g + tuSdkVideoInfo.durationUs;
        }
        
        @Override
        public void syncVideoDecodecOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkVideoInfo tuSdkVideoInfo) {
            final _VideoEncodecSync d = TuSdkMediaFileTrascoderSync.this.p;
            if (bufferInfo == null || bufferInfo.size < 1 || d == null) {
                return;
            }
            while (!this.isInterrupted() && d.hasLocked()) {}
            this.mPreviousTimeUs = this.mOutputTimeUs;
            bufferInfo.presentationTimeUs += TuSdkMediaFileTrascoderSync.this.g;
            TuSdkMediaFileTrascoderSync.this.k = (this.mOutputTimeUs = bufferInfo.presentationTimeUs);
            d.lockVideoTimestampUs(this.mOutputTimeUs);
        }
    }
    
    private class _AudioEncodecSync extends TuSdkAudioEncodecSyncBase
    {
        @Override
        public void syncAudioEncodecInfo(final TuSdkAudioInfo tuSdkAudioInfo) {
            super.syncAudioEncodecInfo(tuSdkAudioInfo);
            TuSdkMediaFileTrascoderSync.this.a(this.getAudioResample());
        }
    }
    
    private class _AudioDecodecSync extends TuSdkAudioDecodecSyncBase
    {
        @Override
        public void syncAudioDecodecInfo(final TuSdkAudioInfo tuSdkAudioInfo, final TuSdkMediaExtractor tuSdkMediaExtractor) {
            if (tuSdkAudioInfo == null || tuSdkMediaExtractor == null) {
                return;
            }
            super.syncAudioDecodecInfo(tuSdkAudioInfo, tuSdkMediaExtractor);
            TuSdkMediaFileTrascoderSync.this.l = tuSdkAudioInfo.durationUs;
            while (!this.isInterrupted() && this.mAudioResample == null) {}
            if (this.mAudioResample != null) {
                this.mAudioResample.changeFormat(tuSdkAudioInfo);
            }
        }
        
        @Override
        public void syncAudioDecodecOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkAudioInfo tuSdkAudioInfo) {
            final TuSdkAudioEncodecOperation a = TuSdkMediaFileTrascoderSync.this.m;
            final TuSdkAudioResample mAudioResample = this.mAudioResample;
            if (a == null || mAudioResample == null || bufferInfo == null || bufferInfo.size < 1) {
                return;
            }
            this.mPreviousTimeUs = this.mLastTimeUs;
            bufferInfo.presentationTimeUs += TuSdkMediaFileTrascoderSync.this.g;
            this.mLastTimeUs = bufferInfo.presentationTimeUs;
            if (!TuSdkMediaFileTrascoderSync.this.f) {
                TuSdkMediaFileTrascoderSync.this.f = true;
                if (bufferInfo.presentationTimeUs > TuSdkMediaFileTrascoderSync.this.g) {
                    a.autoFillMuteDataWithinEnd(TuSdkMediaFileTrascoderSync.this.g, bufferInfo.presentationTimeUs);
                }
            }
            mAudioResample.queueInputBuffer(byteBuffer, bufferInfo);
        }
        
        @Override
        public void syncAudioDecodeCrashed(final Exception ex) {
            super.syncAudioDecodeCrashed(ex);
            if (ex == null) {
                return;
            }
            TuSdkMediaFileTrascoderSync.this.l = 0L;
        }
        
        @Override
        public void syncAudioResampleOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            TuSdkCodecCapabilities.logBufferInfo(String.format("%s resampleOutputBuffer", "TuSdkMediaFileTrascoderSync"), bufferInfo);
            final TuSdkAudioEncodecOperation a = TuSdkMediaFileTrascoderSync.this.m;
            if (a == null || bufferInfo == null || bufferInfo.size < 1) {
                return;
            }
            byteBuffer.position(bufferInfo.offset);
            byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
            while (!this.isInterrupted() && a.writeBuffer(byteBuffer, bufferInfo) == 0) {}
        }
    }
}
