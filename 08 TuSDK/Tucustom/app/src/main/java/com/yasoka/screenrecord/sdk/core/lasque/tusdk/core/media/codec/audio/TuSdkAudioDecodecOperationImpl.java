// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import android.media.MediaCodec;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
//import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class TuSdkAudioDecodecOperationImpl implements TuSdkAudioDecodecOperation
{
    private int a;
    private TuSdkMediaCodec b;
    private boolean c;
    private ByteBuffer[] d;
    private final long e = 10000L;
    private TuSdkCodecOutput.TuSdkDecodecOutput f;
    private TuSdkMediaExtractor g;
    private MediaFormat h;
    private TuSdkAudioInfo i;
    private TuSdkAudioRender j;
    private boolean k;
    private TuSdkAudioRender.TuSdkAudioRenderCallback l;

    @Override
    public void setAudioRender(TuSdkAudioRender j) {
        this.j = j;
    }

    @Override
    public TuSdkAudioInfo getAudioInfo() {
        return this.i;
    }

    public TuSdkAudioDecodecOperationImpl(final TuSdkCodecOutput.TuSdkDecodecOutput f) {
        this.a = -1;
        this.k = false;
        this.l = new TuSdkAudioRender.TuSdkAudioRenderCallback() {
            @Override
            public boolean isEncodec() {
                return false;
            }
            
            @Override
            public TuSdkAudioInfo getAudioInfo() {
                return TuSdkAudioDecodecOperationImpl.this.i;
            }
            
            @Override
            public void returnRenderBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                TuSdkAudioDecodecOperationImpl.this.a(byteBuffer, bufferInfo);
            }
        };
        if (f == null) {
            throw new NullPointerException(String.format("%s init failed, codecOutput is NULL", "TuSdkAudioDecodecOperationImpl"));
        }
        this.f = f;
    }
    
    @Override
    public void flush() {
        if (this.b == null || this.k) {
            return;
        }
        this.b.flush();
    }

    @Override
    public boolean decodecInit(TuSdkMediaExtractor g) {
        this.a = TuSdkMediaUtils.getMediaTrackIndex(g, "audio/");
        if (this.a < 0) {
            this.decodecException(new TuSdkNoMediaTrackException(String.format("%s decodecInit can not find media track: %s", "TuSdkAudioDecodecOperationImpl", "audio/")));
            TLog.e("%s Audio decodecInit mTrackIndex reulst false", "TuSdkAudioDecodecOperationImpl");
            return false;
        }
        this.h = g.getTrackFormat(this.a);
        g.selectTrack(this.a);
        if (!this.f.canSupportMediaInfo(this.a, this.h)) {
            TLog.e("%s decodecInit can not Support MediaInfo: %s", "TuSdkAudioDecodecOperationImpl", this.h);
            return false;
        }
        this.i = new TuSdkAudioInfo(this.h);
        this.b = TuSdkMediaCodecImpl.createDecoderByType(this.h.getString("mime"));
        if (this.b.configureError() != null || !this.b.configure(this.h, null, null, 0)) {
            this.decodecException(this.b.configureError());
            this.b = null;
            TLog.e("%s Audio decodecInit mMediaCodec reulst false", "TuSdkAudioDecodecOperationImpl");
            return false;
        }
        this.g = g;
        this.b.start();
        this.d = this.b.getOutputBuffers();
        this.c = false;
        return true;
    }
    @Override
    public boolean decodecProcessUntilEnd(final TuSdkMediaExtractor tuSdkMediaExtractor) {
        final TuSdkMediaCodec b = this.b;
        if (b == null) {
            return true;
        }
        if (!this.c) {
            this.c = this.f.processExtractor(tuSdkMediaExtractor, b);
        }
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        final int dequeueOutputBuffer = b.dequeueOutputBuffer(bufferInfo, 10000L);
        switch (dequeueOutputBuffer) {
            case -2: {
                this.f.outputFormatChanged(b.getOutputFormat());
                break;
            }
            case -1: {
                break;
            }
            case -3: {
                this.d = b.getOutputBuffers();
                break;
            }
            default: {
                if (dequeueOutputBuffer < 0) {
                    break;
                }
                if (bufferInfo.size > 0) {
                    final ByteBuffer byteBuffer = this.d[dequeueOutputBuffer];
                    if (this.j == null || !this.j.onAudioSliceRender(byteBuffer, bufferInfo, this.l)) {
                        this.f.processOutputBuffer(tuSdkMediaExtractor, this.a, byteBuffer, bufferInfo);
                    }
                }
                if (!this.k) {
                    b.releaseOutputBuffer(dequeueOutputBuffer, false);
                }
                this.f.updated(bufferInfo);
                break;
            }
        }
        if ((bufferInfo.flags & 0x4) != 0x0) {
            TLog.d("%s process Audio Buffer Stream end", "TuSdkAudioDecodecOperationImpl");
            return this.f.updatedToEOS(bufferInfo);
        }
        return false;
    }
    
    private void a(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.k || this.f == null) {
            return;
        }
        this.f.processOutputBuffer(this.g, this.a, byteBuffer, bufferInfo);
    }
    
    @Override
    public void decodecRelease() {
        this.k = true;
        if (this.b == null) {
            return;
        }
        this.b.stop();
        this.b.release();
        this.b = null;
    }
    
    @Override
    protected void finalize() {
        this.decodecRelease();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    @Override
    public void decodecException(final Exception ex) {
        this.f.onCatchedException(ex);
    }
}
