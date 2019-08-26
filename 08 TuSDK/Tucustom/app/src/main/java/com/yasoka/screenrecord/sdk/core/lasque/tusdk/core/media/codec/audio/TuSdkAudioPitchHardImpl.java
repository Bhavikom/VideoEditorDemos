// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaListener;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkMediaListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class TuSdkAudioPitchHardImpl implements TuSdkAudioPitch
{
    private static final boolean a;
    private TuSdkAudioInfo b;
    private long c;
    private boolean d;
    private TuSdkAudioPitchSync e;
    private float f;
    private float g;
    private TuSdkMediaListener h;
    
    public TuSdkAudioPitchHardImpl(final TuSdkAudioInfo b) {
        this.d = false;
        this.f = 1.0f;
        this.g = 1.0f;
        this.h = new TuSdkMediaListener() {
            @Override
            public void onMediaOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkAudioPitchHardImpl.this.e == null) {
                    return;
                }
                TuSdkAudioPitchHardImpl.this.e.syncAudioPitchOutputBuffer(byteBuffer, bufferInfo);
            }
        };
        if (b == null) {
            throw new NullPointerException(String.format("%s outputInfo is empty.", "TuSdkAudioResampleHardImpl"));
        }
        this.c = jniInit(b.channelCount, b.bitWidth, b.sampleRate, this.h);
        if (this.c == 0L) {
            throw new NullPointerException(String.format("%s Create hard failed.", "TuSdkAudioResampleHardImpl"));
        }
        this.b = b;
    }
    
    @Override
    public void release() {
        if (this.d) {
            return;
        }
        this.d = true;
        jniPitchCommand(this.c, 0, 0L);
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
    
    @Override
    public void setMediaSync(final TuSdkAudioPitchSync e) {
        this.e = e;
    }
    
    @Override
    public void changeFormat(final TuSdkAudioInfo b) {
        if (b == null) {
            TLog.w("%s changeFormat need inputInfo.", "TuSdkAudioResampleHardImpl");
            return;
        }
        this.b = b;
        jniChangeFormat(this.c, b.channelCount, b.bitWidth, b.sampleRate);
    }
    
    @Override
    public void changePitch(final float g) {
        if (!SdkValid.shared.audioPitchEffectsSupport()) {
            TLog.e("You are not allowed to use audio pitch effect , please see https://tutucloud.com", new Object[0]);
            return;
        }
        if (g <= 0.0f || this.g == g) {
            return;
        }
        this.g = g;
        this.f = 1.0f;
        jniChangePitch(this.c, g);
    }
    
    @Override
    public void changeSpeed(final float f) {
        if (f <= 0.0f || this.f == f) {
            return;
        }
        this.f = f;
        this.g = 1.0f;
        jniChangeSpeed(this.c, f);
    }
    
    @Override
    public boolean needPitch() {
        return jniPitchCommand(this.c, 64, 0L) > 0L;
    }
    
    @Override
    public void reset() {
        this.g = 1.0f;
        this.f = 1.0f;
        jniPitchCommand(this.c, 16, 0L);
    }
    
    @Override
    public void flush() {
        jniPitchCommand(this.c, 32, 0L);
    }
    
    @Override
    public boolean queueInputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        return jniQueueInputBuffer(this.c, byteBuffer, bufferInfo.offset, bufferInfo.size, bufferInfo.flags, bufferInfo.presentationTimeUs);
    }
    
    private static native long jniInit(final int p0, final int p1, final int p2, final Object p3);
    
    private static native void jniChangeFormat(final long p0, final int p1, final int p2, final int p3);
    
    private static native void jniChangeSpeed(final long p0, final float p1);
    
    private static native void jniChangePitch(final long p0, final float p1);
    
    private static native long jniPitchCommand(final long p0, final int p1, final long p2);
    
    private static native boolean jniQueueInputBuffer(final long p0, final ByteBuffer p1, final int p2, final int p3, final int p4, final long p5);
    
    static {
        a = SdkValid.isInit;
    }
}
