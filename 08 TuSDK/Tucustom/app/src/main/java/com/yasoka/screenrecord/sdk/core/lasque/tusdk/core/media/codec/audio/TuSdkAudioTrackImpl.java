// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
import android.media.AudioTimestamp;
import android.os.Build;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.AudioTrack;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

public class TuSdkAudioTrackImpl implements TuSdkAudioTrack
{
    private TuSdkAudioInfo a;
    private AudioTrack b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private byte[] h;
    
    public TuSdkAudioTrackImpl() {
        this.c = 3;
        this.d = 2;
        this.f = 1;
        this.g = 0;
        this.c = 3;
        this.d = 2;
        this.f = 1;
    }
    
    public TuSdkAudioTrackImpl(final TuSdkAudioInfo audioInfo) {
        this();
        this.setAudioInfo(audioInfo);
    }
    
    public void setAudioInfo(final TuSdkAudioInfo a) {
        if (a == null) {
            return;
        }
        if (this.b != null) {
            TLog.w("%s not allowed to repeat setAudioInfo ", "TuSdkAudioTrackImpl");
            return;
        }
        this.a = a;
        this.d = ((this.a.bitWidth == 8) ? 3 : 2);
        this.e = ((this.a.channelCount < 2) ? 4 : 12);
        final int n = AudioTrack.getMinBufferSize(this.a.sampleRate, this.e, this.d) * 4;
        final int n2 = a.channelCount * 2;
        this.g = n / n2 * n2;
        if (this.g < 1) {
            TLog.w("%s setAudioInfo existence of invalid parameters: %s", "TuSdkAudioTrackImpl", this.a);
            return;
        }
        this.b = new AudioTrack(this.c, this.a.sampleRate, this.e, this.d, this.g, this.f);
    }
    
    protected AudioTrack getRealAudioTrack() {
        return this.b;
    }
    
    public int getBufferSize() {
        return this.g;
    }
    
    @Override
    public int write(final ByteBuffer byteBuffer) {
        if (this.b == null || byteBuffer == null) {
            return -1;
        }
        if (Build.VERSION.SDK_INT < 21) {
            if (this.h == null || this.h.length < byteBuffer.limit()) {
                this.h = new byte[byteBuffer.limit()];
            }
            byteBuffer.get(this.h, 0, byteBuffer.limit());
            return this.b.write(this.h, 0, Math.min(byteBuffer.limit(), this.g));
        }
        return this.b.write(byteBuffer, Math.min(byteBuffer.limit(), this.g), AudioTrack.MODE_STATIC);
    }
    
    @Override
    public int getPlaybackHeadPosition() {
        if (this.b == null) {
            return 0;
        }
        return this.b.getPlaybackHeadPosition();
    }
    
    @TargetApi(19)
    @Override
    public boolean getTimestamp(final AudioTimestamp audioTimestamp) {
        return this.b != null && audioTimestamp != null && this.b.getTimestamp(audioTimestamp);
    }
    
    @Override
    public void play() {
        if (this.b == null || this.b.getState() != 1) {
            return;
        }
        this.b.play();
    }
    
    @Override
    public void pause() {
        if (this.b == null || this.b.getState() != 1) {
            return;
        }
        this.b.pause();
    }
    
    @Override
    public int setVolume(float max) {
        if (this.b == null) {
            return 1;
        }
        max = Math.max(Math.min(AudioTrack.getMaxVolume(), max), AudioTrack.getMinVolume());
        if (Build.VERSION.SDK_INT < 21) {
            return this.b.setStereoVolume(max, max);
        }
        return this.b.setVolume(max);
    }
    
    @Override
    public void flush() {
        if (this.b == null || this.b.getState() != 1) {
            return;
        }
        this.b.flush();
    }
    
    @Override
    public void release() {
        if (this.b == null) {
            return;
        }
        try {
            this.b.release();
        }
        catch (Exception ex) {}
        this.b = null;
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
}
