// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

import java.util.HashMap;
import android.media.MediaCodec;
//import org.lasque.tusdk.core.utils.hardware.HardwareHelper;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import java.nio.ByteOrder;
//import org.lasque.tusdk.core.utils.TLog;
import java.nio.ByteBuffer;
import android.media.AudioRecord;
import java.util.Map;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.HardwareHelper;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;

@TargetApi(16)
public class TuSdkMicRecord implements TuSdkAudioRecord, TuSdkAudioResampleSync
{
    private static final Map<String, String> a;
    private AudioRecord b;
    private TuSdkAudioInfo c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;
    private ByteBuffer i;
    private TuSdkAudioRecordListener j;
    private int k;
    private TuSdkAudioEffects l;
    private TuSdkAudioResampleHardImpl m;
    
    public TuSdkMicRecord() {
        this.d = 3;
        this.e = 2;
        this.f = 7;
        this.h = 0;
        this.d = 3;
        this.e = 2;
        this.f = 7;
    }
    
    public TuSdkMicRecord(final TuSdkAudioInfo audioInfo) {
        this();
        this.setAudioInfo(audioInfo);
    }
    
    @Override
    public void setListener(final TuSdkAudioRecordListener j) {
        this.j = j;
    }
    
    @Override
    public void setAudioInfo(final TuSdkAudioInfo c) {
        if (c == null) {
            return;
        }
        this.release();
        (this.m = new TuSdkAudioResampleHardImpl(c)).setMediaSync(this);
        this.c = c;
        final TuSdkAudioInfo clone = c.clone();
        this.e = ((this.c.bitWidth == 8) ? 3 : 2);
        if (this.c.channelCount < 2 || this.b()) {
            this.g = 16;
            clone.channelCount = 1;
        }
        else {
            this.g = 12;
            clone.channelCount = 2;
        }
        this.m.changeFormat(clone);
        this.k = 1024 * ((this.c.channelCount < 2) ? 1 : 2) * (this.c.bitWidth / 8);
        final int n = AudioRecord.getMinBufferSize(this.c.sampleRate, this.g, this.e) * 4;
        final int n2 = c.channelCount * 2;
        this.h = n / n2 * n2;
        if (this.h < 1) {
            TLog.w("%s setAudioInfo existence of invalid parameters: %s", "TuSdkMicRecord", this.c);
            if (this.j != null) {
                this.j.onAudioRecordError(2001);
            }
            return;
        }
        this.b = new AudioRecord(this.f, this.c.sampleRate, this.g, this.e, this.h);
        this.i = ByteBuffer.allocateDirect(this.h).order(ByteOrder.nativeOrder());
        if (this.b.getState() != 1) {
            TLog.e("%s can not init, please check the recording permission.", "TuSdkMicRecord");
            if (this.j != null) {
                this.j.onAudioRecordError(2002);
            }
            this.release();
            return;
        }
        (this.l = new TuSdkAudioEffectsImpl(this.b.getAudioSessionId())).enableAcousticEchoCanceler();
        this.l.enableNoiseSuppressor();
    }
    
    @Override
    public void startRecording() {
        if (this.b == null || this.b.getState() != 1) {
            return;
        }
        try {
            this.b.startRecording();
            this.a();
        }
        catch (Exception ex) {
            TLog.e(ex, "%s startRecording failed.", "TuSdkMicRecord");
        }
    }
    
    @Override
    public void stop() {
        if (this.b == null || this.b.getState() != 1) {
            return;
        }
        try {
            this.b.stop();
        }
        catch (Exception ex) {
            TLog.e(ex, "%s stop failed.", "TuSdkMicRecord");
        }
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
        if (this.l != null) {
            this.l.release();
            this.l = null;
        }
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
    
    private void a() {
        ThreadHelper.runThread(new Runnable() {
            final /* synthetic */ AudioRecord a = TuSdkMicRecord.this.b;
            final /* synthetic */ ByteBuffer b = TuSdkMicRecord.this.i;
            
            @Override
            public void run() {
                if (this.a == null || this.b == null) {
                    return;
                }
                while (this.a.getRecordingState() == 3) {
                    TuSdkMicRecord.this.a(this.b, TuSdkMicRecord.this.a(this.a, this.b));
                }
            }
        });
    }
    
    private int a(final AudioRecord audioRecord, final ByteBuffer byteBuffer) {
        int read = 0;
        try {
            byteBuffer.clear();
            read = audioRecord.read(byteBuffer, this.k);
        }
        catch (Exception ex) {
            TLog.e(ex, "%s read failed.", "TuSdkMicRecord");
        }
        if (read < 0) {
            TLog.e("%s AudioRecord error: %d, if stop can ignore.", "TuSdkMicRecord", read);
        }
        return read;
    }
    
    private boolean b() {
        boolean matchDeviceModelAndManuFacturer = false;
        for (final Map.Entry<String, String> entry : TuSdkMicRecord.a.entrySet()) {
            matchDeviceModelAndManuFacturer = HardwareHelper.isMatchDeviceModelAndManuFacturer(entry.getKey(), entry.getValue());
            if (matchDeviceModelAndManuFacturer) {
                break;
            }
        }
        return matchDeviceModelAndManuFacturer;
    }
    
    private void a(final ByteBuffer byteBuffer, final int size) {
        if (size < 1 || this.j == null) {
            return;
        }
        byteBuffer.position(0);
        byteBuffer.limit(size);
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        bufferInfo.size = size;
        bufferInfo.presentationTimeUs = System.nanoTime() / 1000L;
        this.m.queueInputBuffer(byteBuffer, bufferInfo);
    }
    
    @Override
    public void syncAudioResampleOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        this.j.onAudioRecordOutputBuffer(byteBuffer, bufferInfo);
    }
    
    static {
        (a = new HashMap<String, String>()).put("PAFM00", "OPPO");
    }
}
