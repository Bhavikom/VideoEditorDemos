// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaListener;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkMediaListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class TuSdkAudioResampleHardImpl implements TuSdkAudioResample
{
    private static final boolean a;
    private TuSdkAudioInfo b;
    private TuSdkAudioInfo c;
    private long d;
    private boolean e;
    private TuSdkAudioResampleSync f;
    private float g;
    private boolean h;
    private TuSdkMediaListener i;
    
    public TuSdkAudioResampleHardImpl(final TuSdkAudioInfo c) {
        this.e = false;
        this.g = 1.0f;
        this.h = false;
        this.i = new TuSdkMediaListener() {
            @Override
            public void onMediaOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkAudioResampleHardImpl.this.f == null) {
                    return;
                }
                TuSdkAudioResampleHardImpl.this.f.syncAudioResampleOutputBuffer(byteBuffer, bufferInfo);
            }
        };
        if (c == null) {
            throw new NullPointerException(String.format("%s outputInfo is empty.", "TuSdkAudioResampleHardImpl"));
        }
        this.d = jniInit(c.channelCount, c.bitWidth, c.sampleRate, this.i);
        if (this.d == 0L) {
            throw new NullPointerException(String.format("%s Create hard failed.", "TuSdkAudioResampleHardImpl"));
        }
        this.c = c;
    }
    
    @Override
    public void release() {
        if (this.e) {
            return;
        }
        this.e = true;
        jniResampleCommand(this.d, 0, 0L);
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
    public void setMediaSync(TuSdkAudioResampleSync f) {
        this.f = f;
    }

    @Override
    public void changeFormat(final TuSdkAudioInfo b) {
        if (b == null) {
            TLog.w("%s changeFormat need inputInfo.", "TuSdkAudioResampleHardImpl");
            return;
        }
        this.b = b;
        jniChangeFormat(this.d, b.channelCount, b.bitWidth, b.sampleRate);
    }
    
    @Override
    public void changeSpeed(final float g) {
        if (g <= 0.0f || this.g == g) {
            return;
        }
        this.g = g;
        jniChangeSpeed(this.d, g);
    }
    
    @Override
    public void changeSequence(final boolean h) {
        if (this.h == h) {
            return;
        }
        this.h = h;
        jniChangeSequence(this.d, h);
    }
    
    @Override
    public boolean needResample() {
        return jniResampleCommand(this.d, 64, 0L) > 0L;
    }
    
    @Override
    public void reset() {
        this.g = 1.0f;
        this.h = false;
        jniResampleCommand(this.d, 16, 0L);
    }
    
    @Override
    public void flush() {
        jniResampleCommand(this.d, 32, 0L);
    }
    
    @Override
    public void setStartPrefixTimeUs(final long n) {
        jniResampleCommand(this.d, 80, n);
    }
    
    @Override
    public long getLastInputTimeUs() {
        return jniResampleCommand(this.d, 96, 0L);
    }
    
    @Override
    public long getPrefixTimeUs() {
        return jniResampleCommand(this.d, 112, 0L);
    }
    
    @Override
    public boolean queueInputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (!SdkValid.shared.audioResampleEffectsSupport()) {
            TLog.e("You are not allowed to use resample effect , please see https://tutucloud.com", new Object[0]);
            return false;
        }
        return jniQueueInputBuffer(this.d, byteBuffer, bufferInfo.offset, bufferInfo.size, bufferInfo.flags, bufferInfo.presentationTimeUs);
    }
    
    private static native long jniInit(final int p0, final int p1, final int p2, final TuSdkMediaListener p3);
    
    private static native void jniChangeFormat(final long p0, final int p1, final int p2, final int p3);
    
    private static native void jniChangeSpeed(final long p0, final float p1);
    
    private static native void jniChangeSequence(final long p0, final boolean p1);
    
    private static native long jniResampleCommand(final long p0, final int p1, final long p2);
    
    private static native boolean jniQueueInputBuffer(final long p0, final ByteBuffer p1, final int p2, final int p3, final int p4, final long p5);
    
    static {
        a = SdkValid.isInit;
    }
}
