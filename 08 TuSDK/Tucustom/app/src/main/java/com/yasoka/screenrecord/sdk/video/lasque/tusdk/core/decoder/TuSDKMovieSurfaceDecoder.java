// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

import android.os.Message;
import android.os.Looper;
import android.os.Handler;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.media.MediaCodec;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import android.view.Surface;
//import org.lasque.tusdk.core.delegate.TuSDKAudioPacketDelegate;
//import org.lasque.tusdk.core.delegate.TuSDKVideoSurfaceDecodeDelegate;
//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.delegate.TuSDKAudioPacketDelegate;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.delegate.TuSDKVideoSurfaceDecodeDelegate;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;

@TargetApi(21)
public class TuSDKMovieSurfaceDecoder extends TuSDKMediaDecoder
{
    public static final int TIMEOUT_USEC = 500;
    public static final int INVALID_SEEKTIME_FLAG = -1;
    private TuSDKVideoInfo c;
    private TuSdkTimeRange d;
    private long e;
    private boolean f;
    private volatile boolean g;
    private boolean h;
    private EventHandler i;
    private MovieDecoderThread j;
    private TuSDKVideoSurfaceDecodeDelegate k;
    private TuSDKAudioPacketDelegate l;
    private long m;
    private Surface n;
    int a;
    long b;
    
    public TuSDKMovieSurfaceDecoder(final TuSDKMediaDataSource tuSDKMediaDataSource) {
        super(tuSDKMediaDataSource);
        this.f = false;
        this.m = -1L;
        this.a = 0;
        this.b = 0L;
        this.i = new EventHandler();
    }
    
    public TuSDKVideoInfo getVideoInfo() {
        return this.c;
    }
    
    @Override
    public MediaCodec getVideoDecoder() {
        return this.mVideoDecoder;
    }
    
    @Override
    public MediaCodec getAudioDecoder() {
        return null;
    }
    
    public TuSDKVideoSurfaceDecodeDelegate getVideoDelegte() {
        return this.k;
    }
    
    public void setVideoDelegate(final TuSDKVideoSurfaceDecodeDelegate k) {
        this.k = k;
    }
    
    public void setAudioPacketDelegate(final TuSDKAudioPacketDelegate l) {
        this.l = l;
    }
    
    public long getVideoDurationTimeUS() {
        if (this.c == null) {
            return 0L;
        }
        if (this.getTimeRange() != null && this.getTimeRange().isValid()) {
            return Math.min(this.c.durationTimeUs, this.getTimeRange().durationTimeUS());
        }
        return this.c.durationTimeUs;
    }
    
    public long getVideoDuration() {
        return this.getVideoDurationTimeUS() / 1000000L;
    }
    
    public TuSdkTimeRange getTimeRange() {
        return this.d;
    }
    
    public void seekTimeUs(final long m) {
        this.m = m;
        this.e = 0L;
        this.i.removeMessages(6);
        this.i.sendEmptyMessage(6);
    }
    
    private void a() {
        if (this.g && this.m != -1L) {
            this.i();
        }
    }
    
    @Override
    public long getCurrentSampleTimeUs() {
        if (this.m != -1L) {
            return this.m;
        }
        if (this.b()) {
            return Math.max(this.getTimeRange().getStartTimeUS(), this.e);
        }
        return this.e;
    }
    
    private long a(final int n) {
        return n * 1000000L / this.c.fps;
    }
    
    public long getComputePresentationTimeUs() {
        return this.b;
    }
    
    private boolean b() {
        return this.getTimeRange() != null && this.getTimeRange().isValid();
    }
    
    public TuSdkSize getVideoSize() {
        if (this.c != null) {
            return TuSdkSize.create(this.c.width, this.c.height);
        }
        return TuSdkSize.create(0);
    }
    
    public void setLooping(final boolean f) {
        this.f = f;
    }
    
    public void prepare(final Surface n, final TuSdkTimeRange d, final boolean h) {
        this.d = d;
        this.h = h;
        this.n = n;
    }
    
    @Override
    public TuSDKMovieReader createMovieReader() {
        if (this.mDataSource == null) {
            TLog.e("Please set the data source", new Object[0]);
            this.onDecoderError(TuSDKMediaDecoderError.InvalidDataSource);
            return null;
        }
        if (!this.mDataSource.isValid()) {
            TLog.e("Unable to read media file: %s", new Object[] { this.mDataSource.getFilePath() });
            this.onDecoderError(TuSDKMediaDecoderError.InvalidDataSource);
            return null;
        }
        return new TuSDKMovieReader(this.mDataSource);
    }
    
    @Override
    protected void onDecoderError(final TuSDKMediaDecoderError tuSDKMediaDecoderError) {
        super.onDecoderError(tuSDKMediaDecoderError);
        this.g = false;
        if (this.k != null) {
            this.k.onDecoderError(tuSDKMediaDecoderError);
        }
    }
    
    protected void onDecoderComplete() {
        this.m = -1L;
        this.e = 0L;
        this.a = 0;
        if (this.k != null) {
            this.k.onDecoderComplete();
        }
    }
    
    @Override
    public void start() {
        this.i.removeMessages(2);
        this.i.sendEmptyMessage(2);
    }
    
    private void c() {
        if (this.g) {
            return;
        }
        this.mMovieReader = this.createMovieReader();
        if (this.mMovieReader == null) {
            return;
        }
        this.mMovieReader.setTimeRange(this.getTimeRange());
        this.c = this.mMovieReader.getVideoInfo();
        if (this.c != null && TuSdkSize.create(this.c.width, this.c.height).maxSide() >= 3500) {
            this.onDecoderError(TuSDKMediaDecoderError.UnsupportedVideoFormat);
            return;
        }
        if (this.getVideoDelegte() != null) {
            this.getVideoDelegte().onVideoInfoReady(this.c);
        }
        if (this.h && this.findAudioTrack() != -1 && this.l != null) {
            this.l.onAudioInfoReady(this.getAudioTrackFormat());
        }
        this.mVideoDecoder = this.createVideoDecoder(this.n);
        if (this.mVideoDecoder == null) {
            return;
        }
        this.g = true;
        this.a = 0;
        this.mVideoDecoder.start();
        (this.j = new MovieDecoderThread()).start();
    }
    
    public void pause() {
        this.i.removeMessages(2);
        this.i.sendEmptyMessage(3);
    }
    
    private void d() {
        this.m = this.e;
        this.e();
    }
    
    @Override
    public void stop() {
        this.i.removeMessages(2);
        this.i.sendEmptyMessage(4);
    }
    
    private void e() {
        if (!this.g) {
            return;
        }
        this.g = false;
        this.e = 0L;
        this.a = 0;
        if (this.j != null) {
            this.j.interrupt();
            try {
                this.j.join();
                this.j = null;
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        super.stop();
    }
    
    @Override
    public void destroy() {
        this.i.removeCallbacksAndMessages((Object)null);
        this.i.sendEmptyMessage(5);
    }
    
    private void f() {
        this.e();
        this.destroyMediaReader();
        this.k = null;
        this.c = null;
    }
    
    private void g() {
        if (this.getVideoDelegte() == null) {
            return;
        }
        final float progress = this.getProgress();
        if (this.b()) {
            this.getVideoDelegte().onProgressChanged(this.getCurrentSampleTimeUs(), progress);
        }
        else {
            this.getVideoDelegte().onProgressChanged(this.getCurrentSampleTimeUs(), progress);
        }
    }
    
    public float getProgress() {
        float n = this.getCurrentSampleTimeUs() / (float)this.getVideoDurationTimeUS();
        if (this.b()) {
            n = (this.getCurrentSampleTimeUs() - this.getTimeRange().getStartTimeUS()) / (float)this.getVideoDurationTimeUS();
        }
        return n;
    }
    
    private boolean a(final long n) {
        final ByteBuffer[] inputBuffers = this.mVideoDecoder.getInputBuffers();
        final int dequeueInputBuffer = this.mVideoDecoder.dequeueInputBuffer(500L);
        if (dequeueInputBuffer >= 0) {
            long sampleTime;
            do {
                this.b = this.a(this.a++);
                final int sampleData = this.mMovieReader.readSampleData(inputBuffers[dequeueInputBuffer], 0);
                if (sampleData < 0) {
                    this.mVideoDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, this.mMovieReader.getSampleTime(), 4);
                    return true;
                }
                sampleTime = this.mMovieReader.getSampleTime();
                if (sampleTime > this.e) {
                    this.e = sampleTime;
                    this.g();
                }
                if (!this.mMovieReader.isVideoSampleTrackIndex()) {
                    TLog.w("WEIRD: got sample from track " + this.mMovieReader.getSampleTrackIndex() + ", expected " + this.mMovieReader.findVideoTrack(), new Object[0]);
                }
                if (sampleTime < n) {
                    continue;
                }
                this.mVideoDecoder.queueInputBuffer(dequeueInputBuffer, 0, sampleData, sampleTime, 0);
            } while (sampleTime < n);
        }
        return false;
    }
    
    @TargetApi(21)
    private boolean h() {
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        final int dequeueOutputBuffer = this.mVideoDecoder.dequeueOutputBuffer(bufferInfo, 500L);
        switch (dequeueOutputBuffer) {
            case -2: {
                this.c.syncCodecCrop(this.mVideoDecoder.getOutputFormat());
                if (this.k != null) {
                    this.k.onVideoInfoReady(this.c);
                    break;
                }
                break;
            }
        }
        if (dequeueOutputBuffer >= 0) {
            if ((bufferInfo.flags & 0x4) != 0x0) {
                if (!this.h && this.getVideoDelegte() != null) {
                    this.onDecoderComplete();
                }
                return true;
            }
            this.mVideoDecoder.releaseOutputBuffer(dequeueOutputBuffer, bufferInfo.size != 0);
            this.k.onVideoDecoderNewFrameAvailable(dequeueOutputBuffer, bufferInfo);
            if (this.b() && this.getCurrentSampleTimeUs() >= this.getTimeRange().getEndTimeUS()) {
                this.unselectVideoTrack();
                if (!this.h && this.getVideoDelegte() != null) {
                    this.onDecoderComplete();
                }
                return true;
            }
        }
        return false;
    }
    
    private long i() {
        if (this.m != -1L) {
            final long m = this.m;
            this.seekTo(m - 5000000L, 0);
            return m;
        }
        if (this.getTimeRange() != null && this.getTimeRange().isValid()) {
            this.seekTo(this.getTimeRange().getStartTimeUS() - 5000000L, 0);
            return this.getTimeRange().getStartTimeUS();
        }
        return 0L;
    }
    
    private void j() {
        if (!this.g) {
            return;
        }
        this.selectVideoTrack();
        final long max = Math.max(0L, this.i());
        boolean h = false;
        boolean a = false;
        while (!h) {
            if (!this.g) {
                return;
            }
            if (!a) {
                a = this.a(max);
            }
            if (h) {
                continue;
            }
            h = this.h();
        }
    }
    
    @Override
    public void seekTo(final long n, final int n2) {
        super.seekTo(n, n2);
        this.m = -1L;
    }
    
    private void k() {
        if (!this.g) {
            return;
        }
        this.selectAudioTrack();
        this.i();
        while (this.g) {
            final ByteBuffer allocate = ByteBuffer.allocate(262144);
            final int sampleData = this.mMovieReader.readSampleData(allocate, 0);
            if (sampleData <= 0) {
                this.unselectAudioTrack();
                if (this.getVideoDelegte() != null) {
                    this.getVideoDelegte().onDecoderComplete();
                }
            }
            else {
                final long sampleTime = this.mMovieReader.getSampleTime();
                if (!this.b() || sampleTime < this.getTimeRange().getEndTimeUS()) {
                    final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                    bufferInfo.offset = 0;
                    bufferInfo.size = sampleData;
                    bufferInfo.presentationTimeUs = sampleTime;
                    if (this.l == null) {
                        continue;
                    }
                    this.l.onAudioPacketAvailable(sampleTime, allocate, bufferInfo);
                    continue;
                }
                this.unselectAudioTrack();
                if (this.getVideoDelegte() != null) {
                    this.getVideoDelegte().onDecoderComplete();
                }
            }
        }
    }
    
    private class MovieDecoderThread extends Thread
    {
        @Override
        public void run() {
            try {
                while (TuSDKMovieSurfaceDecoder.this.g) {
                    TuSDKMovieSurfaceDecoder.this.j();
                    TuSDKMovieSurfaceDecoder.this.mVideoDecoder.flush();
                    if (!TuSDKMovieSurfaceDecoder.this.f) {
                        break;
                    }
                }
                if (TuSDKMovieSurfaceDecoder.this.h && TuSDKMovieSurfaceDecoder.this.g) {
                    TuSDKMovieSurfaceDecoder.this.k();
                }
            }
            catch (Throwable t) {
                TLog.e("The video cannot be decoded", new Object[0]);
                TuSDKMovieSurfaceDecoder.this.onDecoderError(TuSDKMediaDecoderError.UnsupportedVideoFormat);
            }
        }
    }
    
    private class EventHandler extends Handler
    {
        EventHandler() {
            super(Looper.getMainLooper());
        }
        
        public void handleMessage(final Message message) {
            switch (message.what) {
                case 2: {
                    TuSDKMovieSurfaceDecoder.this.c();
                    break;
                }
                case 3: {
                    TuSDKMovieSurfaceDecoder.this.d();
                    break;
                }
                case 4: {
                    TuSDKMovieSurfaceDecoder.this.e();
                    break;
                }
                case 5: {
                    TuSDKMovieSurfaceDecoder.this.f();
                    break;
                }
                case 6: {
                    TuSDKMovieSurfaceDecoder.this.a();
                    break;
                }
            }
        }
    }
    
    public enum TuSDKMovieDecoderError
    {
        UnsupportedVideoFormat;
    }
}
