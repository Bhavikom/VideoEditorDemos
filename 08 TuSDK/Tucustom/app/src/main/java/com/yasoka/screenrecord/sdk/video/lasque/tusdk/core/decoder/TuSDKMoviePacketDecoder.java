// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
//import org.lasque.tusdk.api.transcoder.TuSDKMovieTranscoder;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaCodec;
//import org.lasque.tusdk.core.common.TuSDKMediaUtils;
//import org.lasque.tusdk.core.common.TuSDKAVPacket;
import java.io.File;
//import org.lasque.tusdk.core.video.TuSDKVideoResult;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
//import org.lasque.tusdk.core.delegate.TuSDKVideoSaveDelegate;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.delegate.TuSDKVideoLoadDelegate;
//import org.lasque.tusdk.core.delegate.TuSDKVideoSurfaceDecodeDelegate;
//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
import android.view.Surface;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.transcoder.TuSDKMovieTranscoder;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKAVPacket;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaUtils;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.delegate.TuSDKVideoLoadDelegate;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.delegate.TuSDKVideoSaveDelegate;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.delegate.TuSDKVideoSurfaceDecodeDelegate;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.video.TuSDKVideoResult;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;

@TargetApi(16)
public class TuSDKMoviePacketDecoder extends TuSDKMediaDecoder<TuSDKMoviePacketReader>
{
    public static final int TIMEOUT_USEC = 5000;
    public static final int INVALID_SEEKTIME_FLAG = -1;
    private Surface a;
    private TuSdkTimeRange b;
    private long c;
    private TuSDKVideoSpeedControl d;
    private boolean e;
    private TuSDKVideoInfo f;
    private TuSDKVideoInfo g;
    private volatile State h;
    private TuSDKVideoSurfaceDecodeDelegate i;
    private TuSDKVideoLoadDelegate j;
    private long k;
    private TuSDKMoviePacketReader.ReadMode l;
    private TuSDKVideoTimeEffectController m;
    private boolean n;
    private String o;
    private boolean p;
    private TuSdkSize q;
    private boolean r;
    private TuSDKVideoSaveDelegate s;
    private TuSDKMoviePacketReader.TuSDKMovieReaderPacketDelegate t;
    
    public TuSDKMoviePacketDecoder(final String s) {
        this(TuSDKMediaDataSource.create(s));
    }
    
    public TuSDKMoviePacketDecoder(final TuSDKMediaDataSource tuSDKMediaDataSource) {
        super(tuSDKMediaDataSource);
        this.e = false;
        this.h = State.Idle;
        this.k = -1L;
        this.l = TuSDKMoviePacketReader.ReadMode.SequenceMode;
        this.m = TuSDKVideoTimeEffectController.create(TuSDKVideoTimeEffectController.TimeEffectMode.NoMode);
        this.n = true;
        this.p = true;
        this.r = false;
        this.s = new TuSDKVideoSaveDelegate() {
            @Override
            public void onProgressChaned(final float n) {
                if (TuSDKMoviePacketDecoder.this.j == null) {
                    return;
                }
                ThreadHelper.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        TuSDKMoviePacketDecoder.this.j.onProgressChaned(n);
                    }
                });
            }
            
            @Override
            public void onSaveResult(final TuSDKVideoResult tuSDKVideoResult) {
                if (tuSDKVideoResult != null) {
                    if (TuSDKMoviePacketDecoder.this.h == State.Terminated) {
                        new File(tuSDKVideoResult.videoPath.getAbsolutePath()).delete();
                        return;
                    }
                    TuSDKMoviePacketDecoder.this.o = tuSDKVideoResult.videoPath.getAbsolutePath();
                    TuSDKMoviePacketDecoder.this.start();
                    if (TuSDKMoviePacketDecoder.this.getVideoInfo() == null) {
                        TuSDKMoviePacketDecoder.this.onDecoderError(TuSDKMediaDecoderError.UnsupportedVideoFormat);
                        return;
                    }
                    if (TuSDKMoviePacketDecoder.this.j == null) {
                        return;
                    }
                    ThreadHelper.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            TuSDKMoviePacketDecoder.this.j.onLoadComplete(TuSDKMoviePacketDecoder.this.getVideoInfo());
                        }
                    });
                }
                else {
                    TuSDKMoviePacketDecoder.this.h = State.Terminated;
                    TuSDKMoviePacketDecoder.this.onDecoderError(TuSDKMediaDecoderError.UnsupportedVideoFormat);
                }
            }
            
            @Override
            public void onResultFail(final Exception ex) {
            }
        };
        this.t = new TuSDKMoviePacketReader.TuSDKMovieReaderPacketDelegate() {
            @Override
            public void onAVPacketAvailable(final TuSDKAVPacket tuSDKAVPacket) {
                if (tuSDKAVPacket == null || !TuSDKMoviePacketDecoder.this.isDecoding()) {
                    return;
                }
                TuSDKMoviePacketDecoder.this.k = -1L;
                if (tuSDKAVPacket.getPacketType() == 1) {
                    TuSDKMoviePacketDecoder.this.a(tuSDKAVPacket);
                    if (TuSDKMoviePacketDecoder.this.p) {
                        TuSDKMoviePacketDecoder.this.c();
                    }
                    else {
                        while (!TuSDKMoviePacketDecoder.this.c()) {}
                    }
                }
            }
            
            @Override
            public void onReadComplete() {
                if (TuSDKMoviePacketDecoder.this.d != null) {
                    TuSDKMoviePacketDecoder.this.d.reset();
                }
                if (TuSDKMoviePacketDecoder.this.mVideoDecoder != null) {
                    TuSDKMoviePacketDecoder.this.mVideoDecoder.flush();
                }
                TuSDKMoviePacketDecoder.this.onDecoderComplete();
                if (TuSDKMoviePacketDecoder.this.e && TuSDKMoviePacketDecoder.this.mMovieReader != null) {
                    ((TuSDKMoviePacketReader)TuSDKMoviePacketDecoder.this.mMovieReader).start();
                }
            }
        };
    }
    
    public TuSDKVideoInfo getVideoInfo() {
        return this.f;
    }
    
    public TuSDKVideoInfo getOriginalVideoInfo() {
        if (this.g == null) {
            this.g = TuSDKMediaUtils.getVideoInfo(this.mDataSource);
        }
        return this.g;
    }
    
    @Override
    public MediaCodec getVideoDecoder() {
        return this.mVideoDecoder;
    }
    
    @Override
    public MediaCodec getAudioDecoder() {
        return null;
    }
    
    public void setEnableTranscoding(final boolean n) {
        this.n = n;
    }
    
    public void setTranscodingOutputSize(final TuSdkSize q) {
        this.q = q;
    }
    
    public String getProcessedFilePath() {
        return this.o;
    }
    
    public TuSDKVideoSpeedControl getVideoSpeedControl() {
        if (this.d == null) {
            this.d = new TuSDKVideoSpeedControl();
        }
        return this.d;
    }
    
    public TuSDKVideoSurfaceDecodeDelegate getVideoDelegate() {
        return this.i;
    }
    
    public void setVideoDelegate(final TuSDKVideoSurfaceDecodeDelegate i) {
        this.i = i;
    }
    
    public void setLoadDelegate(final TuSDKVideoLoadDelegate j) {
        this.j = j;
    }
    
    public void setReadMode(final TuSDKMoviePacketReader.ReadMode readMode) {
        this.l = readMode;
        if (this.mMovieReader != null) {
            ((TuSDKMoviePacketReader)this.mMovieReader).setReadMode(readMode);
        }
    }
    
    public TuSDKMoviePacketReader.ReadMode getReadMode() {
        return this.l;
    }
    
    public void setTimeEffectController(final TuSDKVideoTimeEffectController m) {
        this.m = m;
        if (this.mMovieReader != null) {
            ((TuSDKMoviePacketReader)this.mMovieReader).setTimeEffectController(this.m);
        }
    }
    
    public long getVideoDurationTimeUS() {
        if (this.getVideoInfo() == null) {
            return 0L;
        }
        return this.getVideoInfo().durationTimeUs;
    }
    
    public TuSdkTimeRange getTimeRange() {
        return this.b;
    }
    
    @Override
    public long getCurrentSampleTimeUs() {
        if (this.k != -1L) {
            return this.k;
        }
        return this.c;
    }
    
    public TuSdkSize getVideoSize() {
        if (this.getVideoInfo() != null) {
            return TuSdkSize.create(this.getVideoInfo().width, this.getVideoInfo().height);
        }
        return TuSdkSize.create(0);
    }
    
    public void setLooping(final boolean e) {
        this.e = e;
    }
    
    public void setDoRender(final boolean p) {
        this.p = p;
    }
    
    protected void setState(final State h) {
        this.h = h;
    }
    
    public State getState() {
        return this.h;
    }
    
    protected boolean isDecoding() {
        return this.h == State.Decoding;
    }
    
    public void prepare(final Surface a, final TuSdkTimeRange b) {
        this.a = a;
        this.b = b;
    }
    
    @Override
    protected void onDecoderError(final TuSDKMediaDecoderError tuSDKMediaDecoderError) {
        if (this.i != null) {
            this.i.onDecoderError(tuSDKMediaDecoderError);
        }
    }
    
    protected void onDecoderComplete() {
        this.k = -1L;
        this.c = 0L;
        if (!this.e) {
            this.setState(State.Terminated);
        }
        if (this.i != null) {
            this.i.onDecoderComplete();
        }
    }
    
    public void loadVideoCover() {
        this.r = true;
        this.start();
    }
    
    @Override
    public void start() {
        this.c = 0L;
        if (this.n && this.o == null) {
            this.b();
            return;
        }
        if (this.isDecoding()) {
            return;
        }
        this.mMovieReader = this.createMovieReader();
        if (this.mMovieReader == null) {
            return;
        }
        this.mVideoDecoder = this.createVideoDecoder(this.a);
        if (this.mVideoDecoder == null) {
            return;
        }
        if (this.f == null) {
            this.f = ((TuSDKMoviePacketReader)this.mMovieReader).getVideoInfo();
            if (this.n) {
                final TuSDKVideoInfo originalVideoInfo = this.getOriginalVideoInfo();
                this.f.bitrate = originalVideoInfo.bitrate;
                this.f.existAudioTrack = originalVideoInfo.existAudioTrack;
            }
            if (this.i != null) {
                this.i.onVideoInfoReady(this.f);
            }
        }
        if (this.findVideoTrack() == -1) {
            TLog.e("No video track found", new Object[0]);
            this.destroyMediaReader();
            return;
        }
        if (this.f == null || this.f.width <= 0) {
            TLog.e("Invalid video size", new Object[0]);
            return;
        }
        if (this.getVideoSpeedControl() != null) {
            this.getVideoSpeedControl().reset();
        }
        this.setState(State.Decoding);
        this.mVideoDecoder.start();
        ((TuSDKMoviePacketReader)this.mMovieReader).setReadAudioPacketEnable(false);
        ((TuSDKMoviePacketReader)this.mMovieReader).setTimeEffectController(this.m);
        ((TuSDKMoviePacketReader)this.mMovieReader).setReadMode(this.l);
        ((TuSDKMoviePacketReader)this.mMovieReader).setDelegate(this.t);
        if (this.k != -1L) {
            ((TuSDKMoviePacketReader)this.mMovieReader).seekTo(this.k);
        }
        ((TuSDKMoviePacketReader)this.mMovieReader).start();
        this.k = -1L;
    }
    
    public void pause() {
        if (!this.isDecoding()) {
            return;
        }
        this.k = this.c;
        this.setState(State.Idle);
        ((TuSDKMoviePacketReader)this.mMovieReader).stop();
        super.stop();
    }
    
    @Override
    public void stop() {
        if (this.h == State.Idle) {
            return;
        }
        if (this.h != State.Terminated || this.e) {
            this.c = 0L;
        }
        this.setState(State.Idle);
        this.r = false;
        if (this.mMovieReader != null) {
            ((TuSDKMoviePacketReader)this.mMovieReader).stop();
        }
        super.stop();
    }
    
    @Override
    public void seekTo(final long k, final int n) {
        super.seekTo(this.k = k, n);
    }
    
    @Override
    public TuSDKMoviePacketReader createMovieReader() {
        if (this.n) {
            if (this.o == null || !new File(this.o).exists()) {
                TLog.e("Please set a valid data source.", new Object[0]);
                this.onDecoderError(TuSDKMediaDecoderError.InvalidDataSource);
                return null;
            }
            return new TuSDKMoviePacketReader(TuSDKMediaDataSource.create(this.o));
        }
        else {
            if (this.mDataSource == null || !this.mDataSource.isValid()) {
                TLog.e("Please set a valid data source", new Object[0]);
                this.onDecoderError(TuSDKMediaDecoderError.InvalidDataSource);
                return null;
            }
            return new TuSDKMoviePacketReader(this.mDataSource);
        }
    }
    
    @Override
    public void destroy() {
        this.stop();
        this.destroyMediaReader();
        this.setState(State.Terminated);
        if (this.o != null) {
            new File(this.o).delete();
        }
        this.i = null;
    }
    
    private void a() {
        if (this.i == null || this.r || !this.isDecoding()) {
            return;
        }
        this.i.onProgressChanged(this.getCurrentSampleTimeUs(), this.getProgress());
    }
    
    public float getProgress() {
        return this.getCurrentSampleTimeUs() / (float)this.getVideoDurationTimeUS();
    }
    
    private void b() {
        if (this.h == State.Processing) {
            return;
        }
        this.setState(State.Processing);
        final TuSDKMovieTranscoder tuSDKMovieTranscoder = new TuSDKMovieTranscoder(this.mDataSource);
        final TuSDKVideoEncoderSetting defaultRecordSetting = TuSDKVideoEncoderSetting.getDefaultRecordSetting();
        defaultRecordSetting.videoSize = this.q;
        defaultRecordSetting.videoQuality = null;
        defaultRecordSetting.enableAllKeyFrame = true;
        tuSDKMovieTranscoder.setTimeRange(this.getTimeRange());
        tuSDKMovieTranscoder.setVideoEncoderSetting(defaultRecordSetting);
        tuSDKMovieTranscoder.setSaveDelegate(this.s);
        tuSDKMovieTranscoder.startRecording();
    }
    
    protected void onVideoDecoderNewFrameAvailable(final int n, final MediaCodec.BufferInfo bufferInfo) {
        if (bufferInfo.size > 0 && this.i != null) {
            if (this.r) {
                this.setState(State.Processing);
                this.stop();
            }
            this.i.onVideoDecoderNewFrameAvailable(n, bufferInfo);
        }
    }
    
    private boolean a(final TuSDKAVPacket tuSDKAVPacket) {
        final ByteBuffer[] inputBuffers = this.mVideoDecoder.getInputBuffers();
        final int dequeueInputBuffer = this.mVideoDecoder.dequeueInputBuffer(5000L);
        if (dequeueInputBuffer >= 0) {
            final ByteBuffer byteBuffer = inputBuffers[dequeueInputBuffer];
            if (tuSDKAVPacket == null) {
                this.mVideoDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                return true;
            }
            byteBuffer.put(tuSDKAVPacket.getByteBuffer());
            final long sampleTimeUs = tuSDKAVPacket.getSampleTimeUs();
            this.c = sampleTimeUs;
            this.a();
            this.mVideoDecoder.queueInputBuffer(dequeueInputBuffer, 0, tuSDKAVPacket.getChunkSize(), sampleTimeUs, 0);
        }
        return false;
    }
    
    @TargetApi(21)
    private boolean c() {
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        final int dequeueOutputBuffer = this.mVideoDecoder.dequeueOutputBuffer(bufferInfo, 5000L);
        switch (dequeueOutputBuffer) {
            case -2: {
                this.f.syncCodecCrop(this.mVideoDecoder.getOutputFormat());
                if (this.i != null) {
                    this.i.onVideoInfoReady(this.f);
                    break;
                }
                break;
            }
        }
        if ((bufferInfo.flags & 0x4) != 0x0) {
            this.onDecoderComplete();
            return true;
        }
        final boolean b = bufferInfo.size > 0;
        if (dequeueOutputBuffer >= 0) {
            this.mVideoDecoder.releaseOutputBuffer(dequeueOutputBuffer, b);
            this.onVideoDecoderNewFrameAvailable(dequeueOutputBuffer, bufferInfo);
            if (b && this.getVideoSpeedControl() != null) {
                this.getVideoSpeedControl().preRender(bufferInfo.presentationTimeUs);
            }
        }
        return !b;
    }
    
    public interface VideoSpeedControlInterface
    {
        void setEnable(final boolean p0);
        
        void setFrameRate(final int p0);
        
        void preRender(final long p0);
        
        void reset();
    }
    
    public enum State
    {
        Idle, 
        Processing, 
        Decoding, 
        Terminated;
    }
}
