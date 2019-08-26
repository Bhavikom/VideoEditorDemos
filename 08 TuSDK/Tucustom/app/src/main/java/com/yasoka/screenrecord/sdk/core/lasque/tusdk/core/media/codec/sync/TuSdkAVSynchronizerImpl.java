// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackWrap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackWrap;

public class TuSdkAVSynchronizerImpl extends TuSdkAVSynchronizer
{
    private boolean a;
    private long b;
    private long c;
    private long d;
    private TuSdkAudioTrackWrap e;
    
    public TuSdkAVSynchronizerImpl() {
        this.a = true;
        this.b = 0L;
        this.d = -1L;
    }
    
    public void setHaveAudio(final boolean a) {
        this.a = a;
    }
    
    public void setAudioTrackWarp(final TuSdkAudioTrackWrap e) {
        this.e = e;
    }
    
    @Override
    public long getVideoDisplayTimeUs() {
        return this.a ? this.b() : this.a();
    }
    
    public void setRelativeStartNs(final long c) {
        this.c = c;
    }
    
    private long a() {
        long n = this.mVideoBufferTimeUs - this.b;
        if (n < 0L) {
            n = 0L;
        }
        return this.c += n * 1000L;
    }
    
    private long b() {
        if (this.d < 0L) {
            this.d = this.getVideoBufferTimeUs() - this.getAudioBufferTimeUs();
        }
        long n = this.getVideoBufferTimeUs() - this.e.getVideoDisplayTimeUs();
        if (n > 66666L) {
            n = 66666L;
        }
        return System.nanoTime() + n * 1000L + this.d;
    }
    
    @Override
    public void setVideoBufferTimeUs(final long videoBufferTimeUs) {
        if (this.mVideoBufferTimeUs != -1L) {
            this.b = this.mVideoBufferTimeUs;
        }
        super.setVideoBufferTimeUs(videoBufferTimeUs);
    }
    
    @Override
    public void setAudioBufferTimeUs(final long audioBufferTimeUs) {
        super.setAudioBufferTimeUs(audioBufferTimeUs);
    }
    
    public void reset() {
        if (!this.a) {
            this.b = 0L;
        }
    }
}
