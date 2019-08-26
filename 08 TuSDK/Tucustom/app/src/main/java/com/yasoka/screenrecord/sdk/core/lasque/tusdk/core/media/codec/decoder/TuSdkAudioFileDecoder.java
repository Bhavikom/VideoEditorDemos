// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder;

//import org.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioDecodecOperationImpl;
//import org.lasque.tusdk.core.media.codec.exception.TuSdkTaskExitException;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import android.media.MediaFormat;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioDecodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioDecodecOperationImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.exception.TuSdkTaskExitException;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioDecodecSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioDecodecSync;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioDecodecOperation;

public class TuSdkAudioFileDecoder
{
    private TuSdkAudioDecodecOperation a;
    private TuSdkMediaExtractor b;
    private TuSdkMediaDataSource c;
    private boolean d;
    private TuSdkAudioInfo e;
    private TuSdkDecoderListener f;
    private TuSdkAudioDecodecSync g;
    private TuSdkAudioRender h;
    private TuSdkCodecOutput.TuSdkDecodecOutput i;
    
    public TuSdkAudioFileDecoder() {
        this.d = false;
        this.i = new TuSdkCodecOutput.TuSdkDecodecOutput() {
            @Override
            public boolean canSupportMediaInfo(final int n, final MediaFormat mediaFormat) {
                final int checkAudioDecodec = TuSdkMediaFormat.checkAudioDecodec(mediaFormat);
                if (checkAudioDecodec != 0) {
                    TLog.w("%s can not support decodec Audio file [error code: %d]: %s - MediaFormat: %s", "TuSdkAudioFileDecoder", checkAudioDecodec, TuSdkAudioFileDecoder.this.c, mediaFormat);
                    return false;
                }
                TuSdkAudioFileDecoder.this.e = new TuSdkAudioInfo(mediaFormat);
                if (TuSdkAudioFileDecoder.this.g != null) {
                    TuSdkAudioFileDecoder.this.g.syncAudioDecodecInfo(TuSdkAudioFileDecoder.this.e, TuSdkAudioFileDecoder.this.b);
                }
                return true;
            }
            
            @Override
            public void outputFormatChanged(final MediaFormat mediaFormat) {
                TLog.d("%s outputFormatChanged: %s | %s", "TuSdkAudioFileDecoder", mediaFormat, TuSdkAudioFileDecoder.this.e);
            }
            
            @Override
            public boolean processExtractor(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec) {
                if (TuSdkAudioFileDecoder.this.g != null) {
                    return TuSdkAudioFileDecoder.this.g.syncAudioDecodecExtractor(tuSdkMediaExtractor, tuSdkMediaCodec);
                }
                return TuSdkMediaUtils.putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec);
            }
            
            @Override
            public void processOutputBuffer(final TuSdkMediaExtractor tuSdkMediaExtractor, final int n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkAudioFileDecoder.this.g != null) {
                    TuSdkAudioFileDecoder.this.g.syncAudioDecodecOutputBuffer(byteBuffer, bufferInfo, TuSdkAudioFileDecoder.this.e);
                }
            }
            
            @Override
            public void updated(final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkAudioFileDecoder.this.d) {
                    this.onCatchedException(new TuSdkTaskExitException(String.format("%s stopped", "TuSdkAudioFileDecoder")));
                    return;
                }
                TuSdkAudioFileDecoder.this.f.onDecoderUpdated(bufferInfo);
                if (TuSdkAudioFileDecoder.this.g != null) {
                    TuSdkAudioFileDecoder.this.g.syncAudioDecodecUpdated(bufferInfo);
                }
            }
            
            @Override
            public boolean updatedToEOS(final MediaCodec.BufferInfo bufferInfo) {
                TuSdkAudioFileDecoder.this.f.onDecoderCompleted(null);
                return true;
            }
            
            @Override
            public void onCatchedException(final Exception ex) {
                if (TuSdkAudioFileDecoder.this.g != null) {
                    TuSdkAudioFileDecoder.this.g.syncAudioDecodeCrashed(ex);
                }
                TuSdkAudioFileDecoder.this.f.onDecoderCompleted(ex);
            }
        };
    }
    
    public TuSdkAudioInfo getAudioInfo() {
        return this.e;
    }
    
    public void setMediaDataSource(final TuSdkMediaDataSource c) {
        this.c = c;
    }
    
    public void setListener(final TuSdkDecoderListener f) {
        if (f == null) {
            TLog.w("%s setListener can not empty.", "TuSdkAudioFileDecoder");
            return;
        }
        if (this.b != null) {
            TLog.w("%s setListener need before start.", "TuSdkAudioFileDecoder");
            return;
        }
        this.f = f;
    }
    
    public void setMediaSync(final TuSdkAudioDecodecSync g) {
        this.g = g;
    }
    
    public void setAudioRender(final TuSdkAudioRender tuSdkAudioRender) {
        this.h = tuSdkAudioRender;
        if (this.a != null) {
            this.a.setAudioRender(tuSdkAudioRender);
        }
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
            TLog.w("%s has released.", "TuSdkAudioFileDecoder");
            return false;
        }
        if (this.b != null) {
            TLog.w("%s has been running.", "TuSdkAudioFileDecoder");
            return false;
        }
        if (this.c == null || !this.c.isValid()) {
            TLog.w("%s file path is not exists.", "TuSdkAudioFileDecoder");
            return false;
        }
        if (this.f == null) {
            TLog.w("%s need setListener first.", "TuSdkAudioFileDecoder");
            return false;
        }
        (this.a = new TuSdkAudioDecodecOperationImpl(this.i)).setAudioRender(this.h);
        this.b = new TuSdkMediaFileExtractor().setDecodecOperation(this.a).setDataSource(this.c);
        ((TuSdkMediaFileExtractor)this.b).setThreadType(2);
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
}
