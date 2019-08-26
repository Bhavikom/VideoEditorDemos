// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.processor;

//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchSoftImpl;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchSoftImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;

public final class TuSdkAudioPitchEngine implements TuSdkAudioEngine
{
    private TuSdkAudioPitch a;
    private TuSdkSoundPitchType b;
    private TuSdkAudioEnginePitchTypeChangeDelegate c;
    private TuSdKAudioEngineOutputBufferDelegate d;
    private TuSdkAudioPitchSync e;
    
    public TuSdkAudioPitchEngine(final TuSdkAudioInfo tuSdkAudioInfo) {
        this(tuSdkAudioInfo, true);
    }
    
    public TuSdkAudioPitchEngine(TuSdkAudioInfo tuSdkAudioInfo, final boolean b) {
        this.b = TuSdkSoundPitchType.Normal;
        this.e = (TuSdkAudioPitchSync)new TuSdkAudioPitchSync() {
            public void syncAudioPitchOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                TuSdkAudioPitchEngine.this.processOutputBuffer(byteBuffer, bufferInfo);
            }
            
            public void release() {
            }
        };
        if (tuSdkAudioInfo == null) {
            tuSdkAudioInfo = new TuSdkAudioInfo();
        }
        (this.a = this.a(tuSdkAudioInfo, b)).setMediaSync(this.e);
    }
    
    private TuSdkAudioPitch a(final TuSdkAudioInfo tuSdkAudioInfo, final boolean b) {
        if (b) {
            return (TuSdkAudioPitch)new TuSdkAudioPitchHardImpl(tuSdkAudioInfo);
        }
        return (TuSdkAudioPitch)new TuSdkAudioPitchSoftImpl(tuSdkAudioInfo);
    }
    
    @Override
    public void changeAudioInfo(final TuSdkAudioInfo tuSdkAudioInfo) {
        if (tuSdkAudioInfo == null) {
            TLog.e(" %s change the AudioInfo is null !!!", new Object[] { "TuSdkAudioPitchEngine" });
            return;
        }
        this.a.changeFormat(tuSdkAudioInfo);
    }
    
    public void setSoundPitchType(final TuSdkSoundPitchType b) {
        if (this.b == null) {
            return;
        }
        this.b = b;
        if (b.b != 1.0f) {
            this.a.changePitch(b.b);
        }
        if (b.a != 1.0f) {
            this.a.changeSpeed(b.a);
        }
        this.a(b);
    }
    
    public TuSdkSoundPitchType getSoundType() {
        return this.b;
    }
    
    public void setOutputBufferDelegate(final TuSdKAudioEngineOutputBufferDelegate d) {
        this.d = d;
    }
    
    public void setSoundTypeChangeListener(final TuSdkAudioEnginePitchTypeChangeDelegate c) {
        this.c = c;
    }
    
    @Override
    public void processInputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        this.a.queueInputBuffer(byteBuffer, bufferInfo);
    }
    
    protected void processOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.d == null) {
            return;
        }
        this.d.onProcess(byteBuffer, bufferInfo);
    }
    
    public void flush() {
        this.a.flush();
        this.a.reset();
    }
    
    @Override
    public void reset() {
        this.a.reset();
        this.setSoundPitchType(TuSdkSoundPitchType.Normal);
    }
    
    @Override
    public void release() {
        this.a.release();
    }
    
    private void a(final TuSdkSoundPitchType tuSdkSoundPitchType) {
        if (this.c == null) {
            return;
        }
        this.c.onSoundTypeChanged(tuSdkSoundPitchType);
    }
    
    public interface TuSdkAudioEnginePitchTypeChangeDelegate
    {
        void onSoundTypeChanged(final TuSdkSoundPitchType p0);
    }
    
    public enum TuSdkSoundPitchType
    {
        Normal(1.0f, 1.0f), 
        Monster(1.0f, 0.6f), 
        Uncle(1.0f, 0.8f), 
        Girl(1.0f, 1.5f), 
        Lolita(1.0f, 2.0f);
        
        float a;
        float b;
        
        private TuSdkSoundPitchType(final float a, final float b) {
            this.a = a;
            this.b = b;
        }
        
        public float getSpeed() {
            return this.a;
        }
        
        public float getPitch() {
            return this.b;
        }
    }
}
