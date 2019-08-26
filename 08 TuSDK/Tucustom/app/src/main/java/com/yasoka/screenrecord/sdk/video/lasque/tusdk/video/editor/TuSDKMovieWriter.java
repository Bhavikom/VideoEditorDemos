// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import android.media.MediaFormat;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.utils.TLog;
import java.io.IOException;
import android.media.MediaMuxer;
import android.annotation.SuppressLint;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaUtils;

@SuppressLint({ "InlinedApi" })
public class TuSDKMovieWriter implements TuSDKMovieWriterInterface
{
    public static final int INVALID_TRACK_INDEX = -1;
    private volatile State a;
    private MediaMuxer b;
    private int c;
    private int d;
    private String e;
    private MovieWriterOutputFormat f;
    private boolean g;
    private TuSDKMovieWriterDelegate h;
    protected boolean mIsFirstAudioWrite;
    private boolean i;
    private long j;
    private long k;
    private long l;
    private long m;
    public static final byte[] AAC_MUTE_BYTES;
    
    public TuSDKMovieWriter(final String e, final MovieWriterOutputFormat f) {
        this.a = State.UnKnow;
        this.c = -1;
        this.d = -1;
        this.g = false;
        this.mIsFirstAudioWrite = true;
        this.i = false;
        this.j = 0L;
        this.k = 0L;
        this.l = 0L;
        this.m = 0L;
        try {
            this.b = new MediaMuxer(e, f.getOutputFormat());
            this.e = e;
            this.f = f;
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static TuSDKMovieWriter create(final String s, final MovieWriterOutputFormat movieWriterOutputFormat) {
        return new TuSDKMovieWriter(s, movieWriterOutputFormat);
    }
    
    public void setDelegate(final TuSDKMovieWriterDelegate h) {
        this.h = h;
    }
    
    @Override
    public void setOrientationHint(final int n) {
        if (this.b == null || this.a == State.Started) {
            return;
        }
        if (n != 0 && n != 90 && n != 180 && n != 270) {
            TLog.e("Unsupported angle: " + n, new Object[0]);
            return;
        }
        this.b.setOrientationHint(n);
    }
    
    @Override
    public boolean start() {
        if (this.b == null || this.isStarted()) {
            return false;
        }
        this.b.start();
        this.a = State.Started;
        this.i = false;
        return true;
    }
    
    @Override
    public boolean stop() {
        if (this.b == null || !this.isStarted() || !this.i) {
            return false;
        }
        this.a = State.Stopped;
        this.b.stop();
        this.b.release();
        this.b = null;
        this.c = -1;
        this.d = -1;
        this.i = false;
        return true;
    }
    
    public boolean isStarted() {
        return this.a == State.Started;
    }
    
    public boolean isStoped() {
        return this.a == State.Stopped;
    }
    
    public MovieWriterOutputFormat getOutputFormat() {
        return this.f;
    }
    
    public String getOutputFilePath() {
        return this.e;
    }
    
    @Override
    public long getDurationTime() {
        return Math.max(this.m - this.l / 1000L / 1000L, 0L);
    }
    
    @Override
    public void writeSampleData(final int trackIndex, final ByteBuffer buffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.b == null || this.a != State.Started) {
            return;
        }
        final ByteBufferData byteBufferData = new ByteBufferData();
        byteBufferData.trackIndex = trackIndex;
        byteBufferData.buffer = buffer;
        byteBufferData.bufferInfo = bufferInfo;
        this.writeSampleData(byteBufferData);
    }
    
    public void setWriteMuteAudioPlaceholderData(final boolean g) {
        this.g = g;
    }
    
    @Override
    public void writeSampleData(final ByteBufferData byteBufferData) {
        if (this.b == null || this.a != State.Started) {
            return;
        }
        if (this.a() && byteBufferData.trackIndex == this.d) {
            this.a(byteBufferData);
        }
        else if (byteBufferData.trackIndex == this.c) {
            this.b(byteBufferData);
        }
    }
    
    @Override
    public void writeAudioSampleData(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.hasAudioTrack()) {
            this.writeSampleData(this.d, byteBuffer, bufferInfo);
        }
    }
    
    @Override
    public void writeVideoSampleData(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.hasVideoTrack()) {
            this.writeSampleData(this.c, byteBuffer, bufferInfo);
        }
    }
    
    public boolean hasVideoTrack() {
        return this.c != -1;
    }
    
    public boolean hasAudioTrack() {
        return this.d != -1;
    }
    
    private int a(final MediaFormat mediaFormat) {
        if (this.b == null) {
            return -1;
        }
        return this.b.addTrack(mediaFormat);
    }
    
    public boolean canAddVideoTrack() {
        return !this.hasVideoTrack() && !this.isStarted() && this.b != null;
    }
    
    @Override
    public int addVideoTrack(final MediaFormat mediaFormat) {
        if (this.b == null) {
            return -1;
        }
        if (this.c != -1) {
            throw new IllegalArgumentException("The video track already exists");
        }
        return this.c = this.a(mediaFormat);
    }
    
    public boolean canAddAudioTrack() {
        return !this.hasAudioTrack() && !this.isStarted() && this.b != null;
    }
    
    @Override
    public int addAudioTrack(final MediaFormat mediaFormat) {
        if (this.b == null) {
            return -1;
        }
        if (this.d != -1) {
            throw new IllegalArgumentException("The audio track already exists");
        }
        return this.d = this.a(mediaFormat);
    }
    
    public long getLastAudioPresentationTimeUs() {
        return (this.j > 0L) ? this.j : this.m;
    }
    
    public long getBeginVideoPresentationTimeUs() {
        return this.l;
    }
    
    public long getLastVideoPresentationTimeUs() {
        return this.m;
    }
    
    public void setAudioStartTimeUs(final long j) {
        this.mIsFirstAudioWrite = false;
        this.j = j;
        this.k = 0L;
    }
    
    private void a(final long presentationTimeUs) {
        if (this.d == -1) {
            return;
        }
        final ByteBuffer wrap = ByteBuffer.wrap(TuSDKMovieWriter.AAC_MUTE_BYTES);
        final ByteBufferData byteBufferData = new ByteBufferData();
        byteBufferData.trackIndex = this.d;
        byteBufferData.buffer = wrap;
        byteBufferData.bufferInfo = new MediaCodec.BufferInfo();
        byteBufferData.bufferInfo.presentationTimeUs = presentationTimeUs;
        byteBufferData.bufferInfo.size = TuSDKMovieWriter.AAC_MUTE_BYTES.length;
        byteBufferData.bufferInfo.offset = 0;
        this.a(byteBufferData);
        final ByteBufferData byteBufferData2 = new ByteBufferData();
        byteBufferData2.trackIndex = this.d;
        byteBufferData2.buffer = wrap;
        byteBufferData2.bufferInfo = new MediaCodec.BufferInfo();
        byteBufferData2.bufferInfo.presentationTimeUs = this.getLastAudioPresentationTimeUs() + 100L;
        byteBufferData2.bufferInfo.size = TuSDKMovieWriter.AAC_MUTE_BYTES.length;
        byteBufferData2.bufferInfo.offset = 0;
        this.a(byteBufferData2);
    }
    
    private void a(final ByteBufferData byteBufferData) {
        if (this.mIsFirstAudioWrite) {
            this.mIsFirstAudioWrite = false;
            this.j = this.l;
            this.k = this.l - byteBufferData.bufferInfo.presentationTimeUs;
        }
        final MediaCodec.BufferInfo bufferInfo = byteBufferData.bufferInfo;
        bufferInfo.presentationTimeUs += this.k;
        if (byteBufferData.bufferInfo.presentationTimeUs < this.j) {
            byteBufferData.bufferInfo.presentationTimeUs = TuSDKMediaUtils.getSafePts(this.j, byteBufferData.bufferInfo.presentationTimeUs);
        }
        this.j = byteBufferData.bufferInfo.presentationTimeUs;
        this.b.writeSampleData(byteBufferData.trackIndex, byteBufferData.buffer, byteBufferData.bufferInfo);
        this.i = true;
    }
    
    private void b(final ByteBufferData byteBufferData) {
        byteBufferData.bufferInfo.presentationTimeUs = TuSDKMediaUtils.getSafePts(this.m, byteBufferData.bufferInfo.presentationTimeUs);
        this.m = byteBufferData.bufferInfo.presentationTimeUs;
        if (this.l <= 0L) {
            this.l = this.m;
        }
        if (this.g) {
            this.a(this.m);
        }
        if (this.h != null) {
            final float n = (float)this.m;
            final float n2 = (float)this.l;
            if (this.l == this.m) {
                this.h.onFirstVideoSampleDataWrited(this.l);
            }
            this.h.onProgressChanged((n - n2) / 1000.0f / 1000.0f, this.m);
        }
        this.b.writeSampleData(byteBufferData.trackIndex, byteBufferData.buffer, byteBufferData.bufferInfo);
        this.i = true;
    }
    
    private boolean a() {
        return !this.hasVideoTrack() || (this.hasAudioTrack() && this.l > 0L);
    }
    
    static {
        AAC_MUTE_BYTES = new byte[] { 33, 33, 69, 0, 20, 80, 1, 70, -1, -31, 10, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 94 };
    }
    
    public enum State
    {
        UnKnow, 
        Started, 
        Stopped;
    }
    
    public interface TuSDKMovieWriterDelegate
    {
        void onFirstVideoSampleDataWrited(final long p0);
        
        void onProgressChanged(final float p0, final long p1);
    }
}
