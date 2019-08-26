// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

//import org.lasque.tusdk.core.media.codec.decoder.TuSdkDecoderListener;
import java.io.IOException;
import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder.TuSdkAudioFileDecoder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder.TuSdkDecoderListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioDecodecSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioDecodecSyncBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderEntry;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import java.io.FileNotFoundException;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioDecodecSync;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import java.io.FileOutputStream;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
//import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderEntry;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
//import org.lasque.tusdk.core.media.codec.decoder.TuSdkAudioFileDecoder;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioDecodecSyncBase;

public class TuSDKAudioRenderDecoder extends TuSdkAudioDecodecSyncBase
{
    private TuSdkAudioFileDecoder a;
    private TuSdkAudioResample b;
    private TuSDKAudioRenderEntry c;
    private TuSdkAudioInfo d;
    private String e;
    private FileOutputStream f;
    
    public TuSDKAudioRenderDecoder(final TuSDKAudioRenderEntry c, final TuSdkAudioInfo d, final String e) {
        this.c = c;
        this.d = d;
        this.e = e;
        this.a();
        this.b();
        this.c();
    }
    
    private void a() {
        (this.a = new TuSdkAudioFileDecoder()).setMediaDataSource((TuSdkMediaDataSource)this.c);
        this.a.setMediaSync((TuSdkAudioDecodecSync)this);
    }
    
    private void b() {
        this.setAudioResample(this.b = (TuSdkAudioResample)new TuSdkAudioResampleHardImpl(this.d));
    }
    
    private void c() {
        try {
            this.f = new FileOutputStream(this.e);
        }
        catch (FileNotFoundException ex) {
            TLog.e((Throwable)ex);
        }
    }
    
    public void syncAudioDecodecInfo(final TuSdkAudioInfo tuSdkAudioInfo, final TuSdkMediaExtractor tuSdkMediaExtractor) {
        super.syncAudioDecodecInfo(tuSdkAudioInfo, tuSdkMediaExtractor);
        if (this.b != null) {
            this.b.changeFormat(tuSdkAudioInfo);
        }
    }
    
    public void syncAudioDecodecOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkAudioInfo tuSdkAudioInfo) {
        super.syncAudioDecodecOutputBuffer(byteBuffer, bufferInfo, tuSdkAudioInfo);
    }
    
    public void syncAudioResampleOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        try {
            if (this.a(bufferInfo.presentationTimeUs)) {
                this.f.getChannel().write(byteBuffer);
            }
        }
        catch (IOException ex) {
            TLog.e("%s out put raw file error!", new Object[] { "TuSDKAudioRenderDecoder" });
            TLog.e((Throwable)ex);
        }
        super.syncAudioResampleOutputBuffer(byteBuffer, bufferInfo);
    }
    
    private boolean a(final long n) {
        return this.c.getCutTimeRange() == null || this.c.getCutTimeRange().contains(n);
    }
    
    public void setDecodeListener(final TuSdkDecoderListener listener) {
        if (listener == null) {
            return;
        }
        this.a.setListener(listener);
    }
    
    public void start() {
        this.a.start();
    }
    
    public void release() {
        super.release();
    }
}
