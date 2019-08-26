// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder;

import android.media.MediaMetadataRetriever;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoSurfaceDecodecOperation;
//import org.lasque.tusdk.core.media.codec.exception.TuSdkTaskExitException;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import android.graphics.SurfaceTexture;
import android.view.Surface;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkVideoDecodecSync;
//import org.lasque.tusdk.core.seles.sources.SelesSurfaceHolder;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
//import org.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.exception.TuSdkTaskExitException;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkVideoDecodecSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoSurfaceDecodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesSurfaceHolder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class TuSdkVideoFileSurfaceDecoder
{
    private TuSdkDecodecOperation a;
    private TuSdkMediaExtractor b;
    private TuSdkMediaDataSource c;
    private boolean d;
    private TuSdkVideoInfo e;
    private SelesSurfaceHolder f;
    private TuSdkDecoderListener g;
    private TuSdkVideoDecodecSync h;
    private TuSdkCodecOutput.TuSdkDecodecVideoSurfaceOutput i;
    
    public TuSdkVideoFileSurfaceDecoder() {
        this.d = false;
        this.i = new TuSdkCodecOutput.TuSdkDecodecVideoSurfaceOutput() {
            @Override
            public boolean canSupportMediaInfo(final int n, final MediaFormat mediaFormat) {
                final int checkVideoDecodec = TuSdkMediaFormat.checkVideoDecodec(mediaFormat);
                if (checkVideoDecodec != 0) {
                    TLog.w("%s can not support decodec Video file [error code: 0x%x]: %s - MediaFormat: %s", "TuSdkVideoFileSurfaceDecoder", checkVideoDecodec, TuSdkVideoFileSurfaceDecoder.this.c, mediaFormat);
                    return false;
                }
                TuSdkVideoFileSurfaceDecoder.this.e.setMediaFormat(mediaFormat);
                if (TuSdkVideoFileSurfaceDecoder.this.h != null) {
                    TuSdkVideoFileSurfaceDecoder.this.h.syncVideoDecodecInfo(TuSdkVideoFileSurfaceDecoder.this.e, TuSdkVideoFileSurfaceDecoder.this.b);
                }
                return true;
            }
            
            @Override
            public Surface requestSurface() {
                if (TuSdkVideoFileSurfaceDecoder.this.f == null || !TuSdkVideoFileSurfaceDecoder.this.f.isInited()) {
                    return null;
                }
                final SurfaceTexture requestSurfaceTexture = TuSdkVideoFileSurfaceDecoder.this.f.requestSurfaceTexture();
                if (requestSurfaceTexture == null) {
                    return null;
                }
                TuSdkVideoFileSurfaceDecoder.this.f.setInputSize(TuSdkVideoFileSurfaceDecoder.this.e.size);
                TuSdkVideoFileSurfaceDecoder.this.f.setInputRotation(TuSdkVideoFileSurfaceDecoder.this.e.orientation);
                return new Surface(requestSurfaceTexture);
            }
            
            @Override
            public void outputFormatChanged(final MediaFormat corp) {
                if (TuSdkVideoFileSurfaceDecoder.this.e == null) {
                    return;
                }
                TuSdkVideoFileSurfaceDecoder.this.e.setCorp(corp);
                TLog.d("%s outputFormatChanged: %s | %s", "TuSdkVideoFileSurfaceDecoder", corp, TuSdkVideoFileSurfaceDecoder.this.e);
                if (TuSdkVideoFileSurfaceDecoder.this.f == null) {
                    return;
                }
                TuSdkVideoFileSurfaceDecoder.this.f.setInputSize(TuSdkVideoFileSurfaceDecoder.this.e.codecSize);
                TuSdkVideoFileSurfaceDecoder.this.f.setPreCropRect(TuSdkVideoFileSurfaceDecoder.this.e.codecCrop);
            }
            
            @Override
            public boolean processExtractor(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec) {
                if (TuSdkVideoFileSurfaceDecoder.this.h != null) {
                    return TuSdkVideoFileSurfaceDecoder.this.h.syncVideoDecodecExtractor(tuSdkMediaExtractor, tuSdkMediaCodec);
                }
                return TuSdkMediaUtils.putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec);
            }
            
            @Override
            public void processOutputBuffer(final TuSdkMediaExtractor tuSdkMediaExtractor, final int n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkVideoFileSurfaceDecoder.this.h != null) {
                    TuSdkVideoFileSurfaceDecoder.this.h.syncVideoDecodecOutputBuffer(byteBuffer, bufferInfo, TuSdkVideoFileSurfaceDecoder.this.e);
                }
            }
            
            @Override
            public void updated(final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkVideoFileSurfaceDecoder.this.d) {
                    this.onCatchedException(new TuSdkTaskExitException(String.format("%s stopped", "TuSdkVideoFileSurfaceDecoder")));
                    return;
                }
                if (TuSdkVideoFileSurfaceDecoder.this.f != null) {
                    TuSdkVideoFileSurfaceDecoder.this.f.setSurfaceTexTimestampNs(TuSdkVideoFileSurfaceDecoder.this.h.calcEffectFrameTimeUs(bufferInfo.presentationTimeUs) * 1000L);
                }
                TuSdkVideoFileSurfaceDecoder.this.g.onDecoderUpdated(bufferInfo);
                if (TuSdkVideoFileSurfaceDecoder.this.h != null) {
                    TuSdkVideoFileSurfaceDecoder.this.h.syncVideoDecodecUpdated(bufferInfo);
                }
            }
            
            @Override
            public boolean updatedToEOS(final MediaCodec.BufferInfo bufferInfo) {
                TuSdkVideoFileSurfaceDecoder.this.g.onDecoderCompleted(null);
                return true;
            }
            
            @Override
            public void onCatchedException(final Exception ex) {
                if (TuSdkVideoFileSurfaceDecoder.this.h != null) {
                    TuSdkVideoFileSurfaceDecoder.this.h.syncVideoDecodeCrashed(ex);
                }
                TuSdkVideoFileSurfaceDecoder.this.g.onDecoderCompleted(ex);
            }
        };
    }
    
    public TuSdkVideoInfo getVideoInfo() {
        return this.e;
    }
    
    public void setMediaDataSource(final TuSdkMediaDataSource c) {
        this.c = c;
    }
    
    public void setSurfaceHolder(final SelesSurfaceHolder f) {
        if (f == null) {
            TLog.w("%s setSurfaceHolder can not empty.", "TuSdkVideoFileSurfaceDecoder");
            return;
        }
        if (this.b != null) {
            TLog.w("%s setSurfaceHolder need before start.", "TuSdkVideoFileSurfaceDecoder");
            return;
        }
        this.f = f;
    }
    
    public void setListener(final TuSdkDecoderListener g) {
        if (g == null) {
            TLog.w("%s setListener can not empty.", "TuSdkVideoFileSurfaceDecoder");
            return;
        }
        if (this.b != null) {
            TLog.w("%s setListener need before start.", "TuSdkVideoFileSurfaceDecoder");
            return;
        }
        this.g = g;
    }
    
    public void setMediaSync(final TuSdkVideoDecodecSync h) {
        this.h = h;
    }
    
    public void release() {
        if (this.d) {
            return;
        }
        this.d = true;
        if (this.b != null) {
            this.b.release();
            this.b = null;
        }
        this.a = null;
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
    
    public boolean restart() {
        this.release();
        this.d = false;
        return this.start();
    }
    
    public boolean start() {
        if (this.d) {
            TLog.w("%s has released.", "TuSdkVideoFileSurfaceDecoder");
            return false;
        }
        if (this.b != null) {
            TLog.w("%s has been running.", "TuSdkVideoFileSurfaceDecoder");
            return false;
        }
        if (this.c == null || !this.c.isValid()) {
            TLog.w("%s file path is not exists.", "TuSdkVideoFileSurfaceDecoder");
            return false;
        }
        if (this.g == null) {
            TLog.w("%s need setListener first.", "TuSdkVideoFileSurfaceDecoder");
            return false;
        }
        if (this.f == null) {
            TLog.w("%s need setSurfaceHolder first.", "TuSdkVideoFileSurfaceDecoder");
            return false;
        }
        this.a = new TuSdkVideoSurfaceDecodecOperation(this.i);
        this.b = new TuSdkMediaFileExtractor().setDecodecOperation(this.a).setDataSource(this.c);
        ((TuSdkMediaFileExtractor)this.b).setThreadType(1);
        final MediaMetadataRetriever mediaMetadataRetriever = this.c.getMediaMetadataRetriever();
        this.e = new TuSdkVideoInfo();
        this.e.size = TuSdkMediaFormat.getVideoKeySize(mediaMetadataRetriever);
        this.e.orientation = TuSdkMediaFormat.getVideoRotation(mediaMetadataRetriever);
        mediaMetadataRetriever.release();
        this.b.play();
        return true;
    }
    
    public boolean isPlaying() {
        return this.b != null && this.b.isPlaying();
    }
    
    public void pause() {
        if (this.b == null) {
            return;
        }
        this.b.pause();
    }
    
    public void resume() {
        if (this.b == null) {
            return;
        }
        this.b.resume();
    }
    
    public void flush() {
        if (this.a == null || this.d) {
            return;
        }
        this.a.flush();
    }
    
    public void seekTo(long durationUs, int n) {
        if (this.b == null) {
            return;
        }
        if (this.e != null && durationUs > this.e.durationUs) {
            durationUs = this.e.durationUs;
            n = 2;
        }
        this.b.seekTo(durationUs, n);
    }
    
    public long getSampleTime() {
        if (this.b == null) {
            return -1L;
        }
        return this.b.getSampleTime();
    }
    
    public boolean advance() {
        return this.b != null && this.b.advance();
    }
}
