// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import java.util.Iterator;
import java.nio.ByteBuffer;
import android.media.MediaCrypto;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaFormat;
import android.view.Surface;
import android.media.MediaCodec;
import java.util.List;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class AVAssetTrackCodecDecoder implements AVAssetTrackDecoder
{
    private List<AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput> a;
    private MediaCodec b;
    private Surface c;
    private MediaFormat d;
    private boolean e;
    private AVAssetTrackOutputSouce f;
    private long g;
    private PositionSeeker h;
    
    public AVAssetTrackCodecDecoder(final AVAssetTrackOutputSouce f) {
        this.h = new PositionSeeker();
        if (f == null || f.inputTrack() == null) {
            TLog.i("%s No tracks are available in the data source.", f);
            return;
        }
        this.f = f;
        this.a = new ArrayList<AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput>();
        this.d = this.f.inputTrack().mediaFormat();
    }
    
    public void setOutputSurface(final Surface c) {
        if (this.f.inputTrack() == null || this.f.inputTrack().mediaType() != AVMediaType.AVMediaTypeVideo) {
            TLog.i("%s Only video tracks support surface.", this);
            return;
        }
        this.c = c;
    }
    
    protected void maybeInitCodec() {
        if (this.b != null) {
            return;
        }
        try {
            final String string = TuSdkMediaFormat.getString(this.d, "mime", null);
            if (string == null) {
                TLog.e("%s \uff1aThe mCodecFormat is invalid. ", this);
                return;
            }
            (this.b = MediaCodec.createDecoderByType(string)).configure(this.d, this.c, (MediaCrypto)null, 0);
            this.b.start();
            this.e = false;
        }
        catch (Exception ex) {}
    }
    
    protected void releaseCodec() {
        if (this.b == null) {
            return;
        }
        this.b.stop();
        this.b.release();
        this.b = null;
    }
    
    @Override
    public void onInputFormatChanged(final MediaFormat d) {
        this.d = d;
        this.releaseCodec();
        this.maybeInitCodec();
    }
    
    @Override
    public long durationTimeUs() {
        return this.f.durationTimeUs();
    }
    
    @Override
    public long outputTimeUs() {
        return this.g;
    }
    
    @Override
    public boolean seekTo(final long n, final boolean b) {
        return this.h.a(n, b);
    }
    
    @Override
    public boolean isDecodeCompleted() {
        return this.e;
    }
    
    @Override
    public void reset() {
        this.g = 0L;
        this.f.reset();
        this.releaseCodec();
        this.d = this.f.inputTrack().mediaFormat();
    }
    
    @Override
    public boolean renderOutputBuffers() {
        if (this.f.isOutputDone()) {
            return false;
        }
        this.maybeInitCodec();
        while (this.drainOutputBuffer()) {}
        while (this.feedInputBuffer()) {}
        return true;
    }
    
    @Override
    public boolean renderOutputBuffer() {
        if (this.f.isOutputDone()) {
            return false;
        }
        this.maybeInitCodec();
        if (this.drainOutputBuffer()) {}
        while (this.feedInputBuffer()) {}
        return true;
    }
    
    @Override
    public boolean feedInputBuffer() {
        try {
            if (this.b == null) {
                return false;
            }
            final AVSampleBuffer sampleBuffer = this.f.readSampleBuffer(0);
            if (this.f.isOutputDone() || sampleBuffer == null) {
                final int dequeueInputBuffer = this.b.dequeueInputBuffer(0L);
                if (dequeueInputBuffer >= 0) {
                    this.b.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                }
                while (this.drainOutputBuffer()) {}
                return false;
            }
            if ((this.d != null && this.d != sampleBuffer.format()) || sampleBuffer.isFormat()) {
                while (this.drainOutputBuffer()) {}
                this.onInputFormatChanged(sampleBuffer.format());
                if (sampleBuffer.isFormat() && !sampleBuffer.isKeyFrame()) {
                    this.f.advance();
                }
                return true;
            }
            final int dequeueInputBuffer2 = this.b.dequeueInputBuffer(0L);
            if (dequeueInputBuffer2 < 0) {
                return false;
            }
            switch (dequeueInputBuffer2) {
                case -1: {
                    return false;
                }
                default: {
                    final ByteBuffer byteBuffer = this.b.getInputBuffers()[dequeueInputBuffer2];
                    byteBuffer.position(0);
                    byteBuffer.put(sampleBuffer.buffer());
                    this.b.queueInputBuffer(dequeueInputBuffer2, sampleBuffer.info().offset, sampleBuffer.info().size, sampleBuffer.info().presentationTimeUs, sampleBuffer.info().flags);
                    this.f.advance();
                    return true;
                }
            }
        }
        catch (Exception ex) {
            TLog.e(ex);
            return false;
        }
    }
    
    @Override
    public boolean drainOutputBuffer() {
        try {
            if (this.b == null) {
                return false;
            }
            final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            final int dequeueOutputBuffer = this.b.dequeueOutputBuffer(bufferInfo, 0L);
            if ((bufferInfo.flags & 0x4) != 0x0) {
                this.b.releaseOutputBuffer(dequeueOutputBuffer, false);
                this.releaseCodec();
                this.e = true;
                return false;
            }
            switch (dequeueOutputBuffer) {
                case -3: {
                    return true;
                }
                case -1: {
                    return false;
                }
                case -2: {
                    this.a(this.b.getOutputFormat());
                    return true;
                }
                default: {
                    if (dequeueOutputBuffer < 0 || bufferInfo.size <= 0) {
                        return true;
                    }
                    final ByteBuffer byteBuffer = this.b.getOutputBuffers()[dequeueOutputBuffer];
                    final MediaCodec.BufferInfo bufferInfo2 = new MediaCodec.BufferInfo();
                    bufferInfo2.presentationTimeUs = bufferInfo.presentationTimeUs;
                    bufferInfo2.size = bufferInfo.size;
                    bufferInfo2.flags = bufferInfo.flags;
                    bufferInfo2.offset = bufferInfo.offset;
                    final AVSampleBuffer avSampleBuffer = new AVSampleBuffer(byteBuffer, bufferInfo, null);
                    if (this.f.isDecodeOnly(avSampleBuffer.info().presentationTimeUs)) {
                        this.b.releaseOutputBuffer(dequeueOutputBuffer, false);
                        return true;
                    }
                    final boolean a = this.h.a();
                    this.h.b();
                    this.a(avSampleBuffer, dequeueOutputBuffer);
                    return !a;
                }
            }
        }
        catch (Exception ex) {
            TLog.e(ex);
            return false;
        }
    }
    
    @TargetApi(21)
    private void a(final AVSampleBuffer avSampleBuffer, final int n) {
        if (n < 0) {
            TLog.i("maybeRender index < 0", new Object[0]);
            return;
        }
        avSampleBuffer.setRenderTimeUs(this.g = this.f.calOutputTimeUs(avSampleBuffer.info().presentationTimeUs));
        if (this.f.inputTrack().mediaType() == AVMediaType.AVMediaTypeAudio) {
            this.a(avSampleBuffer);
            this.b.releaseOutputBuffer(n, true);
        }
        else {
            this.b.releaseOutputBuffer(n, true);
            this.a(avSampleBuffer);
        }
    }
    
    private void a(final AVSampleBuffer avSampleBuffer) {
        final Iterator<AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput> iterator = this.targets().iterator();
        while (iterator.hasNext()) {
            iterator.next().newFrameReady(avSampleBuffer);
        }
    }
    
    private void a(final MediaFormat mediaFormat) {
        final Iterator<AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput> iterator = this.targets().iterator();
        while (iterator.hasNext()) {
            iterator.next().outputFormatChaned(mediaFormat, this.f.inputTrack());
        }
    }
    
    @Override
    public List<AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput> targets() {
        return this.a;
    }
    
    @Override
    public void addTarget(final AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput avAssetTrackSampleBufferInput, final int n) {
        this.a.add(n, avAssetTrackSampleBufferInput);
    }
    
    @Override
    public void addTarget(final AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput avAssetTrackSampleBufferInput) {
        this.a.add(avAssetTrackSampleBufferInput);
    }
    
    @Override
    public void removeTarget(final AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput avAssetTrackSampleBufferInput) {
        this.a.remove(avAssetTrackSampleBufferInput);
    }
    
    private class PositionSeeker
    {
        private long b;
        
        PositionSeeker() {
            this.b();
        }
        
        private boolean a() {
            return this.b > 0L;
        }
        
        private boolean a(final long b, final boolean b2) {
            if (!b2) {
                return this.a(b, 2);
            }
            this.b = b;
            AVAssetTrackCodecDecoder.this.f.seekTo(b, 0);
            AVAssetTrackCodecDecoder.this.maybeInitCodec();
            AVAssetTrackCodecDecoder.this.b.flush();
            while (AVAssetTrackCodecDecoder.this.h.a() && AVAssetTrackCodecDecoder.this.b != null) {
                while (AVAssetTrackCodecDecoder.this.feedInputBuffer()) {}
                while (AVAssetTrackCodecDecoder.this.drainOutputBuffer()) {}
            }
            return this.b < 0L;
        }
        
        private boolean a(final long b, final int n) {
            this.b = b;
            AVAssetTrackCodecDecoder.this.f.seekTo(b, n);
            return AVAssetTrackCodecDecoder.this.renderOutputBuffers();
        }
        
        private void b() {
            this.b = -1L;
        }
    }
}
