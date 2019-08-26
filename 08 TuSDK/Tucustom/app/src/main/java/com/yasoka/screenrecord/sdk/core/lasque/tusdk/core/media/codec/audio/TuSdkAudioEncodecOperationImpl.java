// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import android.media.MediaFormat;
import android.media.MediaCodec;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;

@TargetApi(18)
public class TuSdkAudioEncodecOperationImpl implements TuSdkAudioEncodecOperation
{
    private TuSdkMediaCodec a;
    private boolean b;
    private int c;
    private final long d = 10000L;
    private TuSdkCodecOutput.TuSdkEncodecOutput e;
    private ByteBuffer[] f;
    private ByteBuffer[] g;
    private boolean h;
    private TuSdkMediaMuxer i;
    private int j;
    private ByteBuffer k;
    private TuSdkAudioInfo l;
    private TuSdkAudioRender m;
    private long n;
    private TuSdkAudioRender.TuSdkAudioRenderCallback o;
    
    public TuSdkAudioEncodecOperationImpl(final TuSdkCodecOutput.TuSdkEncodecOutput e) {
        this.c = -1;
        this.h = false;
        this.j = -1;
        this.n = 0L;
        this.o = new TuSdkAudioRender.TuSdkAudioRenderCallback() {
            @Override
            public boolean isEncodec() {
                return true;
            }
            
            @Override
            public TuSdkAudioInfo getAudioInfo() {
                return TuSdkAudioEncodecOperationImpl.this.l;
            }
            
            @Override
            public void returnRenderBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                TuSdkAudioEncodecOperationImpl.this.a(TuSdkAudioEncodecOperationImpl.this.j, byteBuffer, bufferInfo);
            }
        };
        if (e == null) {
            throw new NullPointerException(String.format("%s init failed, encodecOutput is NULL", "TuSdkAudioEncodecOperationImpl"));
        }
        this.e = e;
    }
    
    @Override
    public TuSdkAudioInfo getAudioInfo() {
        return this.l;
    }
    
    @Override
    public void setAudioRender(final TuSdkAudioRender m) {
        this.m = m;
    }
    
    public int setMediaFormat(final MediaFormat mediaFormat) {
        final int checkAudioEncodec = TuSdkMediaFormat.checkAudioEncodec(mediaFormat);
        if (checkAudioEncodec != 0) {
            return checkAudioEncodec;
        }
        final TuSdkMediaCodec encoderByType = TuSdkMediaCodecImpl.createEncoderByType(mediaFormat.getString("mime"));
        if (encoderByType.configureError() != null) {
            TLog.e(encoderByType.configureError(), "%s setMediaFormat create MediaCodec failed", "TuSdkAudioEncodecOperationImpl");
            return 256;
        }
        this.l = new TuSdkAudioInfo(mediaFormat);
        (this.a = encoderByType).configure(mediaFormat, null, null, 1);
        return 0;
    }
    
    @Override
    public void encodecRelease() {
        this.h = true;
        if (this.a == null) {
            return;
        }
        this.a.stop();
        this.a.release();
        this.a = null;
    }
    
    @Override
    protected void finalize() {
        this.encodecRelease();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    @Override
    public void encodecException(final Exception ex) {
        if (this.e == null) {
            TLog.e(ex, "%s the Thread catch exception, The thread wil exit.", "TuSdkAudioEncodecOperationImpl");
            return;
        }
        this.e.onCatchedException(ex);
    }
    
    @Override
    public void flush() {
        if (this.a != null) {
            this.a.flush();
        }
    }
    
    @Override
    public boolean isEncodecStarted() {
        return this.a != null && this.a.isStarted();
    }
    
    @Override
    public boolean encodecInit(final TuSdkMediaMuxer i) {
        this.i = i;
        final TuSdkMediaCodec a = this.a;
        final TuSdkCodecOutput.TuSdkEncodecOutput e = this.e;
        if (a == null || e == null) {
            TLog.w("%s decodecInit need setMediaFormat first", "TuSdkAudioEncodecOperationImpl");
            return false;
        }
        a.start();
        this.f = a.getInputBuffers();
        this.g = a.getOutputBuffers();
        this.b = false;
        return true;
    }
    
    @Override
    public boolean encodecProcessUntilEnd(final TuSdkMediaMuxer tuSdkMediaMuxer) {
        final TuSdkMediaCodec a = this.a;
        final TuSdkCodecOutput.TuSdkEncodecOutput e = this.e;
        if (a == null || e == null) {
            return true;
        }
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        final int dequeueOutputBuffer = a.dequeueOutputBuffer(bufferInfo, 10000L);
        switch (dequeueOutputBuffer) {
            case -2: {
                TLog.d("[debug] out put format changed", new Object[0]);
                this.l = new TuSdkAudioInfo(a.getOutputFormat());
                this.c = tuSdkMediaMuxer.addTrack(a.getOutputFormat());
                e.outputFormatChanged(a.getOutputFormat());
                break;
            }
            case -1: {
                break;
            }
            case -3: {
                TLog.d("%s process Output Buffers Changed", "TuSdkAudioEncodecOperationImpl");
                this.g = a.getOutputBuffers();
                break;
            }
            default: {
                if (dequeueOutputBuffer < 0) {
                    break;
                }
                if (bufferInfo.size > 0) {
                    e.processOutputBuffer(tuSdkMediaMuxer, this.c, this.g[dequeueOutputBuffer], bufferInfo);
                }
                if (!this.h) {
                    a.releaseOutputBuffer(dequeueOutputBuffer, false);
                }
                e.updated(bufferInfo);
                break;
            }
        }
        return (bufferInfo.flags & 0x4) != 0x0 && e.updatedToEOS(bufferInfo);
    }
    
    @Override
    public int writeBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        final TuSdkMediaCodec a = this.a;
        if (this.h || a == null || this.f == null) {
            TLog.w("%s writeBuffer can not run after release.", "TuSdkAudioEncodecOperationImpl");
            return -1;
        }
        if (bufferInfo == null) {
            TLog.w("%s writeBuffer can not allow empty input: buffer[%s], bufferInfo[%s]", "TuSdkAudioEncodecOperationImpl", byteBuffer, bufferInfo);
            return -1;
        }
        final int dequeueInputBuffer = a.dequeueInputBuffer(10000L);
        if (dequeueInputBuffer < 0) {
            return 0;
        }
        if (byteBuffer == null || bufferInfo.size < 1) {
            a.queueInputBuffer(dequeueInputBuffer, 0, 0, bufferInfo.presentationTimeUs, 4);
            return 2;
        }
        this.j = dequeueInputBuffer;
        if (this.m == null || !this.m.onAudioSliceRender(byteBuffer, bufferInfo, this.o)) {
            this.a(dequeueInputBuffer, byteBuffer, bufferInfo);
        }
        this.j = -1;
        return 1;
    }
    
    private void a(final int n, final ByteBuffer src, final MediaCodec.BufferInfo bufferInfo) {
        final TuSdkMediaCodec a = this.a;
        if (this.h || a == null || this.f == null || n < 0) {
            return;
        }
        final ByteBuffer byteBuffer = this.f[n];
        if (src.limit() > byteBuffer.limit()) {
            try {
                final int limit = src.limit();
                bufferInfo.size = Math.min(bufferInfo.size, byteBuffer.capacity());
                src.position(bufferInfo.offset);
                src.limit(bufferInfo.offset + bufferInfo.size);
                byteBuffer.clear();
                if (src.limit() < byteBuffer.limit()) {
                    byteBuffer.limit(src.limit());
                }
                byteBuffer.put(src);
                a.queueInputBuffer(n, 0, bufferInfo.size, bufferInfo.presentationTimeUs, 0);
                long n2 = limit - byteBuffer.limit();
                int size = bufferInfo.size;
                int capacity = byteBuffer.capacity();
                do {
                    final int dequeueInputBuffer = a.dequeueInputBuffer(10000L);
                    if (dequeueInputBuffer > -1) {
                        final ByteBuffer byteBuffer2 = this.f[dequeueInputBuffer];
                        src.position(size);
                        if (n2 > byteBuffer2.capacity()) {
                            capacity += byteBuffer2.capacity();
                            src.limit(capacity);
                            size = capacity;
                        }
                        else {
                            src.limit(limit);
                        }
                        byteBuffer2.clear();
                        byteBuffer2.put(src);
                        bufferInfo.presentationTimeUs += (long)((limit - byteBuffer.limit()) / (float)this.l.sampleRate * 1000000.0f);
                        a.queueInputBuffer(dequeueInputBuffer, 0, limit - byteBuffer.limit(), bufferInfo.presentationTimeUs, 0);
                        n2 -= byteBuffer2.limit();
                    }
                } while (!Thread.interrupted() && n2 > 0L);
            }
            catch (Exception ex) {
                TLog.e(ex);
            }
        }
        else {
            bufferInfo.size = Math.min(bufferInfo.size, byteBuffer.capacity());
            src.position(bufferInfo.offset);
            src.limit(bufferInfo.offset + bufferInfo.size);
            byteBuffer.clear();
            if (src.limit() < byteBuffer.limit()) {
                byteBuffer.limit(src.limit());
            }
            byteBuffer.put(src);
            a.queueInputBuffer(n, 0, bufferInfo.size, bufferInfo.presentationTimeUs, 0);
        }
        ++this.n;
    }
    
    private long a(final long n) {
        if (this.l == null) {
            return 0L;
        }
        return n * 1024000000L / this.l.sampleRate;
    }
    
    @Override
    public void signalEndOfInputStream(final long presentationTimeUs) {
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        bufferInfo.size = 0;
        bufferInfo.presentationTimeUs = presentationTimeUs;
        while (this.writeBuffer(null, bufferInfo) == 0) {}
    }
    
    @Override
    public void autoFillMuteData(final long n, final long presentationTimeUs, final boolean b) {
        final TuSdkMediaCodec a = this.a;
        if (n > presentationTimeUs || this.h || a == null || this.l == null) {
            return;
        }
        if (this.k == null) {
            this.k = ByteBuffer.allocate(this.l.channelCount * 128 * this.l.bitWidth);
        }
        long presentationTimeUs2 = n;
        long n2 = 0L;
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        bufferInfo.size = this.k.capacity();
        bufferInfo.offset = 0;
        while (!ThreadHelper.isInterrupted() && presentationTimeUs2 < presentationTimeUs) {
            bufferInfo.presentationTimeUs = presentationTimeUs2;
            while (!ThreadHelper.isInterrupted() && this.writeBuffer(this.k, bufferInfo) == 0) {}
            presentationTimeUs2 = this.a(n2) + n;
            ++n2;
        }
        bufferInfo.presentationTimeUs = presentationTimeUs;
        if (b) {
            this.signalEndOfInputStream(presentationTimeUs);
            return;
        }
        while (!ThreadHelper.isInterrupted() && this.writeBuffer(this.k, bufferInfo) == 0) {}
    }
    
    @Override
    public void autoFillMuteDataWithinEnd(final long n, final long n2) {
        if (this.l == null) {
            return;
        }
        this.autoFillMuteData(n, n2 - this.l.intervalUs, false);
    }
}
