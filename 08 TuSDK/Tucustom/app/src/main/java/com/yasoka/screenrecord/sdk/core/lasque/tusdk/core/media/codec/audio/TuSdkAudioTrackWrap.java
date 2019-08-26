// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
//import org.lasque.tusdk.core.utils.ReflectUtils;
import android.media.AudioTrack;
import android.media.AudioTimestamp;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ReflectUtils;

import java.lang.reflect.Method;

public class TuSdkAudioTrackWrap
{
    private TuSdkAudioTrackImpl a;
    private TuSdkAudioInfo b;
    private long c;
    private Method d;
    private long e;
    private AudioTimestamp f;
    private boolean g;
    
    public TuSdkAudioTrackWrap() {
        this.c = 0L;
        this.g = false;
    }
    
    @TargetApi(19)
    public void setAudioTrack(final TuSdkAudioTrackImpl a, final TuSdkAudioInfo b) {
        this.a = a;
        this.b = b;
        this.d = ReflectUtils.getMethod(AudioTrack.class, "getLatency", (Class<?>[])null);
        this.f = new AudioTimestamp();
    }
    
    public long getVideoDisplayTimeUs() {
        if (this.g) {
            return System.nanoTime() / 1000L;
        }
        final int playbackHeadPosition = this.a.getPlaybackHeadPosition();
        if (this.d != null) {
            try {
                this.e = (int)this.d.invoke(this.a, (Object[])null) * 1000L / 2L;
                this.e = Math.max(this.e, 0L);
            }
            catch (Exception ex) {
                this.d = null;
            }
        }
        return this.c + playbackHeadPosition * 1000000L / this.b.sampleRate - this.e;
    }
    
    public void pause() {
    }
    
    public void resume() {
    }
    
    public void reset() {
    }
    
    public void release() {
        this.g = true;
    }
    
    public void setAudioBufferPts(final long c) {
        this.c = c;
    }
}
