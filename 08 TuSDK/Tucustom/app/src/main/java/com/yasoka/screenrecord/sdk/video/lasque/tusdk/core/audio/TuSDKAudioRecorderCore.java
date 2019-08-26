// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.audio;

//import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoder;
import android.annotation.TargetApi;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioEngine;
//import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine;
//import org.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;
//import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;
import android.media.AudioRecord;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioEngine;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoder;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;

public class TuSDKAudioRecorderCore implements TuSDKAudioRecorderInterface
{
    private Object a;
    private AudioRecordThread b;
    private AudioRecord c;
    private TuSDKAudioEffects d;
    private byte[] e;
    private boolean f;
    protected TuSDKAudioDataEncoderInterface mAudioEncoder;
    private TuSdkAudioRecorderDelegate g;
    private TuSDKAudioCaptureSetting h;
    private TuSDKAudioEncoderSetting i;
    private long j;
    private TuSdkAudioPitchEngine k;
    private TuSdkAudioEngine.TuSdKAudioEngineOutputBufferDelegate l;
    
    public TuSDKAudioRecorderCore() {
        this(TuSDKAudioCaptureSetting.defaultCaptureSetting(), TuSDKAudioEncoderSetting.defaultEncoderSetting());
    }
    
    public TuSDKAudioRecorderCore(final TuSDKAudioCaptureSetting tuSDKAudioCaptureSetting, final TuSDKAudioEncoderSetting tuSDKAudioEncoderSetting) {
        this.a = new Object();
        this.d = null;
        this.f = true;
        this.j = 0L;
        this.l = new TuSdkAudioEngine.TuSdKAudioEngineOutputBufferDelegate() {
            @Override
            public void onProcess(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                TuSDKAudioRecorderCore.this.onRawAudioFrameDataAvailable(byteBuffer.array());
            }
        };
        this.a((tuSDKAudioCaptureSetting == null) ? TuSDKAudioCaptureSetting.defaultCaptureSetting() : tuSDKAudioCaptureSetting);
        this.a((tuSDKAudioEncoderSetting == null) ? TuSDKAudioEncoderSetting.defaultEncoderSetting() : tuSDKAudioEncoderSetting);
        this.prepare();
        final TuSdkAudioInfo tuSdkAudioInfo = new TuSdkAudioInfo();
        tuSdkAudioInfo.bitWidth = ((this.getCaptureSetting().audioRecoderFormat == 2) ? 16 : 8);
        tuSdkAudioInfo.channelCount = ((this.getCaptureSetting().audioRecoderChannelConfig == 12) ? 2 : 1);
        tuSdkAudioInfo.sampleRate = this.getCaptureSetting().audioRecoderSampleRate;
        (this.k = new TuSdkAudioPitchEngine(tuSdkAudioInfo, false)).setOutputBufferDelegate(this.l);
    }
    
    public void setInputAudioInfo(final TuSdkAudioInfo tuSdkAudioInfo) {
        this.k.changeAudioInfo(tuSdkAudioInfo);
    }
    
    public void setSoundType(final TuSdkAudioPitchEngine.TuSdkSoundPitchType soundPitchType) {
        if (soundPitchType == null) {
            return;
        }
        this.k.setSoundPitchType(soundPitchType);
    }
    
    public void setSoundTypeChangeListener(final TuSdkAudioPitchEngine.TuSdkAudioEnginePitchTypeChangeDelegate soundTypeChangeListener) {
        this.k.setSoundTypeChangeListener(soundTypeChangeListener);
    }
    
    protected boolean prepare() {
        return (!this.isEnableAudioEncoder() || this.getAudioEncoder() == null || this.mAudioEncoder.initEncoder(this.getEncoderSetting())) && this.b(this.getCaptureSetting());
    }
    
    public boolean isPrepared() {
        if (this.c == null) {
            return false;
        }
        if (1 != this.c.getState()) {
            TLog.e("TuSDKAudioRecorderCore | Please check the recording permission", new Object[0]);
            return false;
        }
        if (0 != this.c.setPositionNotificationPeriod(this.h.audioRecoderSliceSize)) {
            TLog.e("AudioRecord.SUCCESS != audioRecord.setPositionNotificationPeriod(" + this.h.audioRecoderSliceSize + ")", new Object[0]);
            return false;
        }
        return true;
    }
    
    public TuSDKAudioCaptureSetting getCaptureSetting() {
        if (this.h == null) {
            this.h = new TuSDKAudioCaptureSetting();
        }
        return this.h;
    }
    
    private void a(final TuSDKAudioCaptureSetting h) {
        this.h = h;
    }
    
    public TuSDKAudioEncoderSetting getEncoderSetting() {
        if (this.i == null) {
            this.i = new TuSDKAudioEncoderSetting();
        }
        return this.i;
    }
    
    private void a(final TuSDKAudioEncoderSetting i) {
        this.i = i;
    }
    
    public void setEnableAudioEncoder(final boolean f) {
        this.f = f;
    }
    
    public boolean isEnableAudioEncoder() {
        return this.f;
    }
    
    private boolean b(final TuSDKAudioCaptureSetting tuSDKAudioCaptureSetting) {
        synchronized (this.a) {
            if (this.c == null) {
                this.c = new AudioRecord(tuSDKAudioCaptureSetting.audioRecoderSource, tuSDKAudioCaptureSetting.audioRecoderSampleRate, tuSDKAudioCaptureSetting.audioRecoderChannelConfig, tuSDKAudioCaptureSetting.audioRecoderFormat, AudioRecord.getMinBufferSize(tuSDKAudioCaptureSetting.audioRecoderSampleRate, tuSDKAudioCaptureSetting.audioRecoderChannelConfig, tuSDKAudioCaptureSetting.audioRecoderFormat) * 4);
                this.e = new byte[tuSDKAudioCaptureSetting.audioRecoderBufferSize];
            }
            return this.isPrepared();
        }
    }
    
    @TargetApi(16)
    private int a() {
        if (this.c == null || this.getCaptureSetting().audioRecoderSource != 7) {
            return -1;
        }
        return this.c.getAudioSessionId();
    }
    
    private void b() {
        if (this.getCaptureSetting().audioRecoderSource != 7) {
            return;
        }
        if ((!this.getCaptureSetting().shouldEnableAec && !this.getCaptureSetting().shouldEnableNs) || this.a() == -1) {
            return;
        }
        if (this.d == null) {
            this.d = TuSDKAudioEffects.a();
        }
        if (this.d == null) {
            return;
        }
        this.d.setAEC(this.getCaptureSetting().shouldEnableAec);
        this.d.setNS(this.getCaptureSetting().shouldEnableNs);
        this.d.enable(this.a());
    }
    
    @Override
    public void startRecording() {
        synchronized (this.a) {
            if (!this.isPrepared() || this.isRecording()) {
                return;
            }
            try {
                if (this.c != null) {
                    this.j = System.nanoTime() / 1000L;
                    this.c.startRecording();
                    if (!this.isRecording()) {
                        this.c = null;
                        TLog.e("TuSDKAudioRecorderCore | Please check the recording permission", new Object[0]);
                        return;
                    }
                    this.b();
                }
                if (this.isEnableAudioEncoder() && this.mAudioEncoder != null) {
                    this.mAudioEncoder.start();
                }
                if (this.g != null && 3 == this.c.getRecordingState()) {
                    this.g.onAudioStarted();
                }
                (this.b = new AudioRecordThread()).start();
            }
            catch (Error obj) {
                TLog.e("%s | " + obj, new Object[0]);
            }
        }
    }
    
    @Override
    public void stopRecording() {
        synchronized (this.a) {
            if (!this.isPrepared()) {
                return;
            }
            if (this.c != null && 1 == this.c.getState()) {
                this.c.stop();
            }
            if (this.mAudioEncoder != null) {
                this.mAudioEncoder.stop();
            }
            if (this.b != null) {
                this.b.quit();
            }
            if (this.d != null) {
                this.d.release();
                this.d = null;
            }
        }
    }
    
    @Override
    public void mute(final boolean b) {
        if (b) {
            this.stopRecording();
        }
        else {
            if (this.isRecording()) {
                return;
            }
            this.startRecording();
        }
    }
    
    @Override
    public boolean isRecording() {
        synchronized (this.a) {
            return this.c != null && this.c.getRecordingState() == 3;
        }
    }
    
    private void a(final byte[] array) {
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        bufferInfo.size = array.length;
        bufferInfo.presentationTimeUs = System.nanoTime() / 1000L - this.j;
        this.k.processInputBuffer(ByteBuffer.wrap(array), bufferInfo);
    }
    
    protected void onRawAudioFrameDataAvailable(final byte[] array) {
        if (this.isEnableAudioEncoder() && this.mAudioEncoder != null) {
            this.mAudioEncoder.queueAudio(array);
        }
        if (this.getDelegate() != null) {
            this.getDelegate().onRawAudioFrameDataAvailable(array);
        }
    }
    
    public TuSdkAudioRecorderDelegate getDelegate() {
        return this.g;
    }
    
    public void setDelegate(final TuSdkAudioRecorderDelegate g) {
        this.g = g;
    }
    
    @Override
    public TuSDKAudioDataEncoderInterface getAudioEncoder() {
        if (this.mAudioEncoder == null) {
            this.mAudioEncoder = new TuSDKAudioDataEncoder();
        }
        return this.mAudioEncoder;
    }
    
    public void setAudioEncoder(final TuSDKAudioDataEncoderInterface mAudioEncoder) {
        this.mAudioEncoder = mAudioEncoder;
    }
    
    protected class AudioRecordThread extends Thread
    {
        private boolean b;
        
        public AudioRecordThread() {
            this.b = true;
            this.b = true;
        }
        
        public void quit() {
            this.b = false;
        }
        
        @Override
        public void run() {
            while (this.b && !Thread.interrupted()) {
                final int read = TuSDKAudioRecorderCore.this.c.read(TuSDKAudioRecorderCore.this.e, 0, TuSDKAudioRecorderCore.this.e.length);
                if (this.b && read > 0) {
                    TuSDKAudioRecorderCore.this.a(TuSDKAudioRecorderCore.this.e);
                }
            }
        }
    }
    
    public interface TuSdkAudioRecorderDelegate
    {
        void onRawAudioFrameDataAvailable(final byte[] p0);
        
        void onAudioStarted();
    }
}
