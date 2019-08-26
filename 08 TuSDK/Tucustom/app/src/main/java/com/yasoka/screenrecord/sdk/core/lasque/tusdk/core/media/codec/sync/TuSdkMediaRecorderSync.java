// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

import android.opengl.GLES20;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;

@TargetApi(16)
public class TuSdkMediaRecorderSync implements TuSdkMediaEncodecSync
{
    private boolean a;
    private boolean b;
    private final TuSdkMediaTimeline c;
    private float d;
    private float e;
    private TuSdkAudioEncodecOperation f;
    private _AudioEncodecSync g;
    private _VideoEncodecSync h;
    
    public TuSdkMediaRecorderSync() {
        this.a = false;
        this.b = false;
        this.c = new TuSdkMediaTimeline(0L);
        this.d = 1.0f;
        this.e = 1.0f;
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
        this.a = true;
        if (this.g != null) {
            this.g.release();
        }
        if (this.h != null) {
            this.h.release();
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
    
    public boolean isEncodecCompleted() {
        return this.isVideoEncodeCompleted() && this.isAudioEncodeCompleted();
    }
    
    public long getAudioEncodecTimeUs() {
        if (this.g == null) {
            return -1L;
        }
        return this.g.lastStandardPtsUs();
    }
    
    public long getVideoEncodecTimeUs() {
        if (this.h == null) {
            return -1L;
        }
        return this.h.lastStandardPtsUs();
    }
    
    public void setAudioOperation(final TuSdkAudioEncodecOperation f) {
        this.f = f;
    }
    
    public void changeSpeed(final float d) {
        if (d <= 0.0f || this.d == d) {
            return;
        }
        this.e = this.d;
        this.d = d;
    }
    
    public void pauseRecord() {
        if (this.b) {
            return;
        }
        this.b = true;
    }
    
    public void resumeRecord() {
        if (!this.b) {
            return;
        }
        this.b = false;
        final long videoEncodecTimeUs = this.getVideoEncodecTimeUs();
        final long audioEncodecTimeUs = this.getAudioEncodecTimeUs();
        if (this.h != null) {
            this.c.append(this.h.getPreviousStartTimeUs());
            this.h.reset(audioEncodecTimeUs);
        }
        if (this.g != null) {
            this.g.reset(videoEncodecTimeUs);
        }
    }
    
    public TuSdkMediaTimeline endOfTimeline() {
        if (this.h == null) {
            return null;
        }
        this.c.setInputDurationUs(this.getVideoEncodecTimeUs());
        return this.c;
    }
    
    public boolean isAudioEncodeCompleted() {
        return this.g == null || this.g.isAudioEncodeCompleted();
    }
    
    public boolean isVideoEncodeCompleted() {
        return this.h == null || this.h.isVideoEncodeCompleted();
    }
    
    public void syncVideoEncodecDrawFrame(final long n, final boolean b, final TuSdkEncodeSurface tuSdkEncodeSurface) {
        if (this.h == null) {
            return;
        }
        this.h.syncVideoEncodecDrawFrame(n, b, tuSdkEncodeSurface);
    }
    
    public void syncAudioEncodecFrame(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.g == null) {
            return;
        }
        if (this.h != null && !this.h.isEncodecStarted()) {
            return;
        }
        this.g.syncAudioEncodecFrame(byteBuffer, bufferInfo);
    }
    
    private class _VideoEncodecSync implements TuSdkVideoEncodecSync
    {
        private boolean b;
        private int c;
        private long d;
        private long e;
        private long f;
        private boolean g;
        private long h;
        private long i;
        private long j;
        private long k;
        private boolean l;
        
        private _VideoEncodecSync() {
            this.b = false;
            this.c = 0;
            this.d = 0L;
            this.e = 0L;
            this.f = 0L;
            this.g = false;
            this.h = 0L;
            this.i = -1L;
            this.j = -1L;
            this.k = 0L;
            this.l = false;
        }
        
        public long getPreviousStartTimeUs() {
            return this.k;
        }
        
        public void reset(final long j) {
            this.i = -1L;
            this.j = j;
        }
        
        public boolean isEncodecStarted() {
            return this.i > 0L;
        }
        
        public boolean isInterrupted() {
            return ThreadHelper.isInterrupted() || this.b;
        }
        
        @Override
        public boolean isVideoEncodeCompleted() {
            return this.g;
        }
        
        @Override
        public void release() {
            this.b = true;
            TuSdkMediaRecorderSync.this.e = 1.0f;
        }
        
        private long a(final long n) {
            if (this.c < 1) {
                return 0L;
            }
            return n * 1000000L / this.c;
        }
        
        public long lastStandardPtsUs() {
            return this.h;
        }
        
        public long nextStandardPtsUs() {
            return this.a(this.d + 1L);
        }
        
        @Override
        public void syncEncodecVideoInfo(final TuSdkVideoInfo tuSdkVideoInfo) {
            if (tuSdkVideoInfo != null) {
                this.c = tuSdkVideoInfo.frameRates;
            }
            this.l = true;
        }
        
        @Override
        public void syncVideoEncodecOutputBuffer(final TuSdkMediaMuxer tuSdkMediaMuxer, final int n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            if (bufferInfo.presentationTimeUs == 1L) {
                return;
            }
            TuSdkMediaUtils.processOutputBuffer(tuSdkMediaMuxer, n, byteBuffer, bufferInfo);
        }
        
        @Override
        public void syncVideoEncodecUpdated(final MediaCodec.BufferInfo bufferInfo) {
            if (bufferInfo == null) {
                return;
            }
            if (TLog.LOG_VIDEO_ENCODEC_INFO) {
                TuSdkCodecCapabilities.logBufferInfo(String.format("%s syncVideoEncodecUpdated", "TuSdkMediaRecorderSync"), bufferInfo);
            }
        }
        
        @Override
        public void syncVideoEncodecCompleted() {
            if (TLog.LOG_VIDEO_ENCODEC_INFO) {
                TLog.d("%s syncVideoEncodecCompleted", "TuSdkMediaRecorderSync");
            }
            this.g = true;
        }
        
        private boolean a() {
            return TuSdkMediaRecorderSync.this.b;
        }
        
        private void a(final TuSdkEncodeSurface tuSdkEncodeSurface) {
            if (this.j < 0L) {
                return;
            }
            if (this.nextStandardPtsUs() < this.j) {
                return;
            }
            long n = this.a(this.d);
            while (this.nextStandardPtsUs() < this.j) {
                tuSdkEncodeSurface.duplicateFrameReadyInGLThread(n * 1000L);
                tuSdkEncodeSurface.swapBuffers(n * 1000L);
                ++this.d;
                n = this.a(this.d);
            }
        }
        
        public void syncVideoEncodecDrawFrame(long n, final boolean b, final TuSdkEncodeSurface tuSdkEncodeSurface) {
            if (tuSdkEncodeSurface == null || this.b) {
                return;
            }
            if (b) {
                return;
            }
            if (!this.l) {
                tuSdkEncodeSurface.newFrameReadyInGLThread(1000L);
                tuSdkEncodeSurface.swapBuffers(1000L);
                return;
            }
            final long i = n / 1000L;
            if (this.i < 0L) {
                this.a(tuSdkEncodeSurface);
                tuSdkEncodeSurface.requestKeyFrame();
                this.k = this.h;
                this.i = i;
            }
            final long e = (long)Math.floor((i - this.i) / TuSdkMediaRecorderSync.this.d) + this.k;
            this.e = e;
            if (TuSdkMediaRecorderSync.this.e != TuSdkMediaRecorderSync.this.d) {
                tuSdkEncodeSurface.flush();
                tuSdkEncodeSurface.requestKeyFrame();
                TuSdkMediaRecorderSync.this.e = TuSdkMediaRecorderSync.this.d;
            }
            final long n2 = e;
            if (n2 < 1L) {
                tuSdkEncodeSurface.requestKeyFrame();
                tuSdkEncodeSurface.newFrameReadyInGLThread(0L);
                ++this.d;
                tuSdkEncodeSurface.swapBuffers(0L);
                return;
            }
            n = n2 * 1000L;
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            tuSdkEncodeSurface.newFrameReadyInGLThread(n);
            if (this.a()) {
                tuSdkEncodeSurface.requestKeyFrame();
                ++this.d;
                tuSdkEncodeSurface.swapBuffers(n);
                this.h = n2;
                return;
            }
            ++this.d;
            this.h = n2;
            tuSdkEncodeSurface.swapBuffers(n);
        }
    }
    
    private class _AudioEncodecSync extends TuSdkAudioEncodecSyncBase
    {
        private long b;
        private long c;
        
        private _AudioEncodecSync() {
            this.b = -1L;
            this.c = -1L;
        }
        
        public void reset(final long c) {
            this.b = -1L;
            this.c = c;
        }
        
        public void syncAudioEncodecFrame(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            final TuSdkAudioEncodecOperation a = TuSdkMediaRecorderSync.this.f;
            if (byteBuffer == null || bufferInfo.size < 1 || a == null || this.mReleased) {
                return;
            }
            if (this.b < 0L) {
                this.b = bufferInfo.presentationTimeUs;
                this.a(a);
            }
            bufferInfo.presentationTimeUs = this.getAndAddCountPtsUs();
            TuSdkCodecCapabilities.logBufferInfo("[debug] in audio sync", bufferInfo);
            while (!this.isInterrupted() && a.writeBuffer(byteBuffer, bufferInfo) == 0) {}
        }
        
        private void a(final TuSdkAudioEncodecOperation tuSdkAudioEncodecOperation) {
            if (this.c < 0L) {
                return;
            }
            if (this.nextStandardPtsUs() > this.c) {
                return;
            }
            tuSdkAudioEncodecOperation.autoFillMuteData(this.lastStandardPtsUs(), this.getAndAddCountPtsUs(this.c), false);
        }
    }
}
