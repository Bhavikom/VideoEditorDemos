// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

import android.os.Message;
import android.os.Handler;
import android.graphics.Rect;
import android.graphics.ImageFormat;
import android.media.Image;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.media.MediaCodec;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
//import org.lasque.tusdk.core.delegate.TuSDKAudioPacketDelegate;
//import org.lasque.tusdk.core.delegate.TuSDKVideoFrameDecodeDelegate;
//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.delegate.TuSDKAudioPacketDelegate;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.delegate.TuSDKVideoFrameDecodeDelegate;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;

@TargetApi(21)
public class TuSDKMovieFrameDecoder extends TuSDKMediaDecoder
{
    public static final int COLOR_FormatI420 = 1;
    public static final int COLOR_FormatNV21 = 2;
    public static final int TIMEOUT_USEC = 500;
    public static final int INVALID_SEEKTIME_FLAG = -1;
    private TuSDKVideoInfo a;
    private TuSdkTimeRange b;
    private long c;
    private boolean d;
    private volatile boolean e;
    private boolean f;
    private EventHandler g;
    private MovieDecoderThread h;
    private TuSDKVideoFrameDecodeDelegate i;
    private TuSDKAudioPacketDelegate j;
    private long k;
    
    public TuSDKMovieFrameDecoder(final TuSDKMediaDataSource tuSDKMediaDataSource) {
        super(tuSDKMediaDataSource);
        this.d = false;
        this.k = -1L;
        this.g = new EventHandler();
    }
    
    public TuSDKVideoInfo getVideoInfo() {
        return this.a;
    }
    
    @Override
    public MediaCodec getVideoDecoder() {
        return this.mVideoDecoder;
    }
    
    @Override
    public MediaCodec getAudioDecoder() {
        return null;
    }
    
    public TuSDKVideoFrameDecodeDelegate getVideoDelegate() {
        return this.i;
    }
    
    public void setVideoDelegate(final TuSDKVideoFrameDecodeDelegate i) {
        this.i = i;
    }
    
    public void setAudioPacketDelegate(final TuSDKAudioPacketDelegate j) {
        this.j = j;
    }
    
    public long getVideoDurationTimeUS() {
        if (this.a == null) {
            return 0L;
        }
        return this.a.durationTimeUs;
    }
    
    public long getVideoDuration() {
        return this.getVideoDurationTimeUS() / 1000000L;
    }
    
    public TuSdkTimeRange getTimeRange() {
        return this.b;
    }
    
    public void seekTimeUs(final long k) {
        this.k = k;
        this.c = 0L;
        this.g.removeMessages(6);
        this.g.sendEmptyMessage(6);
    }
    
    private void a() {
        if (this.e && this.k != -1L) {
            this.i();
        }
    }
    
    @Override
    public long getCurrentSampleTimeUs() {
        if (this.k != -1L) {
            return this.k;
        }
        if (this.b()) {
            return Math.max(this.getTimeRange().getStartTimeUS(), this.c);
        }
        return this.c;
    }
    
    private boolean b() {
        return this.getTimeRange() != null && this.getTimeRange().isValid();
    }
    
    public TuSdkSize getVideoSize() {
        if (this.a != null) {
            return TuSdkSize.create(this.a.width, this.a.height);
        }
        return TuSdkSize.create(0);
    }
    
    public void setLooping(final boolean d) {
        this.d = d;
    }
    
    public void prepare(final TuSdkTimeRange b, final boolean f) {
        this.b = b;
        this.f = f;
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
        if (this.i != null) {
            this.i.onDecoderError(tuSDKMediaDecoderError);
        }
    }
    
    protected void onDecoderComplete() {
        this.k = -1L;
        this.c = 0L;
        if (this.i != null) {
            this.i.onDecoderComplete();
        }
    }
    
    @Override
    public void start() {
        this.g.removeMessages(2);
        this.g.sendEmptyMessage(2);
    }
    
    private void c() {
        if (this.e) {
            return;
        }
        this.mMovieReader = this.createMovieReader();
        if (this.mMovieReader == null) {
            return;
        }
        this.mMovieReader.setTimeRange(this.getTimeRange());
        this.a = this.mMovieReader.getVideoInfo();
        if (this.getVideoDelegate() != null) {
            this.getVideoDelegate().onVideoInfoReady(this.a);
        }
        if (this.f && this.findAudioTrack() != -1 && this.j != null) {
            this.j.onAudioInfoReady(this.getAudioTrackFormat());
        }
        this.mVideoDecoder = this.createVideoDecoder(null);
        if (this.mVideoDecoder == null) {
            return;
        }
        this.e = true;
        this.mVideoDecoder.start();
        (this.h = new MovieDecoderThread()).start();
    }
    
    public void pause() {
        this.g.removeMessages(2);
        this.g.sendEmptyMessage(3);
    }
    
    private void d() {
        this.k = this.c;
        this.e();
    }
    
    @Override
    public void stop() {
        this.g.removeMessages(2);
        this.g.sendEmptyMessage(4);
    }
    
    private void e() {
        if (!this.e) {
            return;
        }
        this.e = false;
        this.c = 0L;
        if (this.h != null) {
            this.h.interrupt();
            try {
                this.h.join();
                this.h = null;
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        super.stop();
    }
    
    @Override
    public void destroy() {
        this.g.removeCallbacksAndMessages((Object)null);
        this.g.sendEmptyMessage(5);
    }
    
    private void f() {
        this.e();
        this.destroyMediaReader();
        this.i = null;
        this.a = null;
    }
    
    private void g() {
        if (this.getVideoDelegate() == null) {
            return;
        }
        final float progress = this.getProgress();
        if (this.b()) {
            this.getVideoDelegate().onProgressChanged(this.getCurrentSampleTimeUs(), (float)((this.getCurrentSampleTimeUs() - this.getTimeRange().getStartTimeUS()) / 1000000L), progress);
        }
        else {
            this.getVideoDelegate().onProgressChanged(this.getCurrentSampleTimeUs(), (float)(this.getCurrentSampleTimeUs() / 1000000L), progress);
        }
    }
    
    public float getProgress() {
        float n = this.getCurrentSampleTimeUs() / (float)this.getVideoDurationTimeUS();
        if (this.b()) {
            n = (float)((this.getCurrentSampleTimeUs() - this.getTimeRange().getStartTimeUS()) / this.getTimeRange().durationTimeUS());
        }
        return n;
    }
    
    private boolean a(final long n) {
        final ByteBuffer[] inputBuffers = this.mVideoDecoder.getInputBuffers();
        final int dequeueInputBuffer = this.mVideoDecoder.dequeueInputBuffer(500L);
        if (dequeueInputBuffer >= 0) {
            long sampleTime;
            do {
                final int sampleData = this.mMovieReader.readSampleData(inputBuffers[dequeueInputBuffer], 0);
                if (sampleData < 0) {
                    this.mVideoDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, this.mMovieReader.getSampleTime(), 4);
                    return true;
                }
                sampleTime = this.mMovieReader.getSampleTime();
                this.c = sampleTime;
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
        if (dequeueOutputBuffer >= 0) {
            if ((bufferInfo.flags & 0x4) != 0x0) {
                if (!this.f && this.getVideoDelegate() != null) {
                    this.onDecoderComplete();
                }
                return true;
            }
            final boolean b = bufferInfo.size != 0;
            this.g();
            this.onVideoDecoderNewFrameAvailable(dequeueOutputBuffer, bufferInfo);
            this.mVideoDecoder.releaseOutputBuffer(dequeueOutputBuffer, b);
            if (this.b() && this.getCurrentSampleTimeUs() >= this.getTimeRange().getEndTimeUS()) {
                this.unselectVideoTrack();
                if (!this.f && this.getVideoDelegate() != null) {
                    this.onDecoderComplete();
                }
                return true;
            }
        }
        return false;
    }
    
    private long i() {
        if (this.k != -1L) {
            final long k = this.k;
            this.seekTo(k - 5000000L, 0);
            return k;
        }
        if (this.getTimeRange() != null && this.getTimeRange().isValid()) {
            this.seekTo(this.getTimeRange().getStartTimeUS() - 5000000L, 0);
            return this.getTimeRange().getStartTimeUS();
        }
        return 0L;
    }
    
    private void j() {
        if (!this.e) {
            return;
        }
        this.selectVideoTrack();
        final long max = Math.max(0L, this.i());
        boolean h = false;
        boolean a = false;
        while (!h) {
            if (!this.e) {
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
        this.k = -1L;
    }
    
    private void k() {
        if (!this.e) {
            return;
        }
        this.selectAudioTrack();
        this.i();
        while (this.e) {
            final ByteBuffer allocate = ByteBuffer.allocate(262144);
            final int sampleData = this.mMovieReader.readSampleData(allocate, 0);
            if (sampleData <= 0) {
                this.unselectAudioTrack();
                if (this.getVideoDelegate() != null) {
                    this.getVideoDelegate().onDecoderComplete();
                }
            }
            else {
                final long sampleTime = this.mMovieReader.getSampleTime();
                if (!this.b() || sampleTime < this.getTimeRange().getEndTimeUS()) {
                    final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                    bufferInfo.offset = 0;
                    bufferInfo.size = sampleData;
                    bufferInfo.presentationTimeUs = sampleTime;
                    if (this.j == null) {
                        continue;
                    }
                    this.j.onAudioPacketAvailable(sampleTime, allocate, bufferInfo);
                    continue;
                }
                this.unselectAudioTrack();
                if (this.getVideoDelegate() != null) {
                    this.getVideoDelegate().onDecoderComplete();
                }
            }
        }
    }
    
    protected void onVideoDecoderNewFrameAvailable(final int n, final MediaCodec.BufferInfo bufferInfo) {
        if (this.i == null) {
            return;
        }
        this.i.onVideoDecoderNewFrameAvailable(this.a(this.mVideoDecoder.getOutputImage(n), 2), bufferInfo);
    }
    
    @TargetApi(19)
    private static boolean a(final Image image) {
        switch (image.getFormat()) {
            case 17:
            case 35:
            case 842094169: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @TargetApi(21)
    private byte[] a(final Image image, final int n) {
        if (n != 1 && n != 2) {
            throw new IllegalArgumentException("only support COLOR_FormatI420 and COLOR_FormatNV21");
        }
        if (!a(image)) {
            throw new RuntimeException("can't convert Image to byte array, format " + image.getFormat());
        }
        final Rect cropRect = image.getCropRect();
        final int format = image.getFormat();
        final int width = cropRect.width();
        final int height = cropRect.height();
        final Image.Plane[] planes = image.getPlanes();
        final byte[] dst = new byte[width * height * ImageFormat.getBitsPerPixel(format) / 8];
        final byte[] dst2 = new byte[planes[0].getRowStride()];
        int offset = 0;
        int n2 = 1;
        for (int i = 0; i < planes.length; ++i) {
            switch (i) {
                case 0: {
                    offset = 0;
                    n2 = 1;
                    break;
                }
                case 1: {
                    if (n == 1) {
                        offset = width * height;
                        n2 = 1;
                        break;
                    }
                    if (n == 2) {
                        offset = width * height + 1;
                        n2 = 2;
                        break;
                    }
                    break;
                }
                case 2: {
                    if (n == 1) {
                        offset = (int)(width * height * 1.25);
                        n2 = 1;
                        break;
                    }
                    if (n == 2) {
                        offset = width * height;
                        n2 = 2;
                        break;
                    }
                    break;
                }
            }
            final ByteBuffer buffer = planes[i].getBuffer();
            final int rowStride = planes[i].getRowStride();
            final int pixelStride = planes[i].getPixelStride();
            final int n3 = (i != 0) ? 1 : 0;
            final int n4 = width >> n3;
            final int n5 = height >> n3;
            buffer.position(rowStride * (cropRect.top >> n3) + pixelStride * (cropRect.left >> n3));
            for (int j = 0; j < n5; ++j) {
                int n6;
                if (pixelStride == 1 && n2 == 1) {
                    n6 = n4;
                    buffer.get(dst, offset, n6);
                    offset += n6;
                }
                else {
                    n6 = (n4 - 1) * pixelStride + 1;
                    buffer.get(dst2, 0, n6);
                    for (int k = 0; k < n4; ++k) {
                        dst[offset] = dst2[k * pixelStride];
                        offset += n2;
                    }
                }
                if (j < n5 - 1) {
                    buffer.position(buffer.position() + rowStride - n6);
                }
            }
        }
        return dst;
    }
    
    private class MovieDecoderThread extends Thread
    {
        @Override
        public void run() {
            while (TuSDKMovieFrameDecoder.this.e) {
                TuSDKMovieFrameDecoder.this.j();
                TuSDKMovieFrameDecoder.this.mVideoDecoder.flush();
                if (!TuSDKMovieFrameDecoder.this.d) {
                    break;
                }
            }
            if (TuSDKMovieFrameDecoder.this.f && TuSDKMovieFrameDecoder.this.e) {
                TuSDKMovieFrameDecoder.this.k();
            }
        }
    }
    
    private class EventHandler extends Handler
    {
        public void handleMessage(final Message message) {
            switch (message.what) {
                case 2: {
                    TuSDKMovieFrameDecoder.this.c();
                    break;
                }
                case 3: {
                    TuSDKMovieFrameDecoder.this.d();
                    break;
                }
                case 4: {
                    TuSDKMovieFrameDecoder.this.e();
                    break;
                }
                case 5: {
                    TuSDKMovieFrameDecoder.this.f();
                    break;
                }
                case 6: {
                    TuSDKMovieFrameDecoder.this.a();
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
