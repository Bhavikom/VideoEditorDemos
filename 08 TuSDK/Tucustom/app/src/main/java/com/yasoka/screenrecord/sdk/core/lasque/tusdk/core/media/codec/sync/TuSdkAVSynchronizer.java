// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

public abstract class TuSdkAVSynchronizer
{
    protected long mAudioBufferTimeUs;
    protected long mVideoBufferTimeUs;
    
    public TuSdkAVSynchronizer() {
        this.mAudioBufferTimeUs = -1L;
        this.mVideoBufferTimeUs = -1L;
    }
    
    public long getAudioBufferTimeUs() {
        return this.mAudioBufferTimeUs;
    }
    
    public void setAudioBufferTimeUs(final long mAudioBufferTimeUs) {
        this.mAudioBufferTimeUs = mAudioBufferTimeUs;
    }
    
    public long getVideoBufferTimeUs() {
        return this.mVideoBufferTimeUs;
    }
    
    public void setVideoBufferTimeUs(final long mVideoBufferTimeUs) {
        this.mVideoBufferTimeUs = mVideoBufferTimeUs;
    }
    
    public abstract long getVideoDisplayTimeUs();
}
