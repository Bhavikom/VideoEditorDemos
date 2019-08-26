// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder;

//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.seles.sources.SelesSurfaceHolder;
//import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaDecodecSync;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkMediaDecodecSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public class TuSdkMediaFileDecoder
{
    protected boolean mReleased;
    private TuSdkVideoFileSurfaceDecoder a;
    private TuSdkAudioFileDecoder b;
    private TuSdkMediaDecodecSync c;
    private boolean d;
    private boolean e;
    
    public boolean isVideoStared() {
        return this.d;
    }
    
    public boolean isAudioStared() {
        return this.e;
    }
    
    public void setMediaSync(final TuSdkMediaDecodecSync c) {
        if (c == null) {
            return;
        }
        this.c = c;
        if (this.a != null) {
            this.a.setMediaSync(c.buildVideoDecodecSync());
        }
        if (this.b != null) {
            this.b.setMediaSync(c.buildAudioDecodecSync());
        }
    }
    
    public void setMediaDataSource(final TuSdkMediaDataSource tuSdkMediaDataSource) {
        if (this.a != null) {
            this.a.setMediaDataSource(tuSdkMediaDataSource);
        }
        if (this.b != null) {
            this.b.setMediaDataSource(tuSdkMediaDataSource);
        }
    }
    
    public void setSurfaceReceiver(final SelesSurfaceReceiver surfaceHolder) {
        if (this.a != null) {
            this.a.setSurfaceHolder(surfaceHolder);
        }
    }
    
    public void setAudioRender(final TuSdkAudioRender audioRender) {
        if (this.b != null) {
            this.b.setAudioRender(audioRender);
        }
    }
    
    public void setListener(final TuSdkDecoderListener listener, final TuSdkDecoderListener listener2) {
        if (this.a != null) {
            this.a.setListener(listener);
        }
        if (this.b != null) {
            this.b.setListener(listener2);
        }
    }
    
    public TuSdkMediaFileDecoder(final boolean b, final boolean b2) {
        this.mReleased = false;
        this.d = false;
        this.e = false;
        if (b) {
            this.a = new TuSdkVideoFileSurfaceDecoder();
        }
        if (b2) {
            this.b = new TuSdkAudioFileDecoder();
        }
    }
    
    public void release() {
        this.mReleased = true;
        this.releaseAudioDecoder();
        this.releaseVideoDecoder();
    }
    
    public void releaseVideoDecoder() {
        if (this.a == null) {
            return;
        }
        this.a.release();
        this.a = null;
        if (this.b == null) {
            this.mReleased = true;
        }
    }
    
    public void releaseAudioDecoder() {
        if (this.b == null) {
            return;
        }
        this.b.release();
        this.b = null;
        if (this.a == null) {
            this.mReleased = true;
        }
    }
    
    public long seekTo(long sampleTime, final int n) {
        if (this.mReleased) {
            return -1L;
        }
        if (this.a != null) {
            this.a.seekTo(sampleTime, n);
            sampleTime = this.a.getSampleTime();
        }
        if (this.b != null) {
            this.b.seekTo(sampleTime, n);
        }
        return sampleTime;
    }
    
    public void prepare() {
        if (this.a != null && this.a.start()) {
            this.d = true;
        }
        if (this.b != null && this.b.start()) {
            this.e = true;
        }
    }
    
    public void flush() {
        if (this.a != null) {
            this.b.flush();
        }
    }
}
