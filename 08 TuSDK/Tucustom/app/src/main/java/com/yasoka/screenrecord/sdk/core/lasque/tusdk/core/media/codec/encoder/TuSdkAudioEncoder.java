// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder;

//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperationImpl;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaFormat;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperationImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSync;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;

public class TuSdkAudioEncoder
{
    private int a;
    private TuSdkAudioEncodecOperation b;
    private TuSdkEncoderListener c;
    private TuSdkAudioEncodecSync d;
    private TuSdkAudioRender e;
    private TuSdkCodecOutput.TuSdkEncodecOutput f;
    
    public TuSdkAudioEncoder() {
        this.a = -1;
        this.f = new TuSdkCodecOutput.TuSdkEncodecOutput() {
            @Override
            public void outputFormatChanged(final MediaFormat mediaFormat) {
                TLog.d("%s encodec Audio outputFormatChanged: %s", "TuSdkAudioEncoder", mediaFormat);
                if (TuSdkAudioEncoder.this.d != null && TuSdkAudioEncoder.this.b != null) {
                    TuSdkAudioEncoder.this.d.syncAudioEncodecInfo(TuSdkAudioEncoder.this.b.getAudioInfo());
                }
            }
            
            @Override
            public void processOutputBuffer(final TuSdkMediaMuxer tuSdkMediaMuxer, final int n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkAudioEncoder.this.d != null) {
                    TuSdkAudioEncoder.this.d.syncAudioEncodecOutputBuffer(tuSdkMediaMuxer, n, byteBuffer, bufferInfo);
                }
                else {
                    TuSdkMediaUtils.processOutputBuffer(tuSdkMediaMuxer, n, byteBuffer, bufferInfo);
                }
            }
            
            @Override
            public void updated(final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkAudioEncoder.this.d != null) {
                    TuSdkAudioEncoder.this.d.syncAudioEncodecUpdated(bufferInfo);
                }
                TuSdkAudioEncoder.this.c.onEncoderUpdated(bufferInfo);
            }
            
            @Override
            public boolean updatedToEOS(final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkAudioEncoder.this.d != null) {
                    TuSdkAudioEncoder.this.d.syncAudioEncodecCompleted();
                }
                TuSdkAudioEncoder.this.c.onEncoderCompleted(null);
                return true;
            }
            
            @Override
            public void onCatchedException(final Exception ex) {
                if (TuSdkAudioEncoder.this.d != null) {
                    TuSdkAudioEncoder.this.d.syncAudioEncodecCompleted();
                }
                TuSdkAudioEncoder.this.c.onEncoderCompleted(ex);
            }
        };
    }
    
    public void setListener(final TuSdkEncoderListener c) {
        if (c == null) {
            TLog.w("%s setListener can not empty.", "TuSdkAudioEncoder");
            return;
        }
        if (this.a != -1) {
            TLog.w("%s setListener need before prepare.", "TuSdkAudioEncoder");
            return;
        }
        this.c = c;
    }
    
    public int setOutputFormat(final MediaFormat mediaFormat) {
        if (this.a != -1) {
            TLog.w("%s setOutputFormat need before prepare.", "TuSdkAudioEncoder");
            return -1;
        }
        final TuSdkAudioEncodecOperationImpl b = new TuSdkAudioEncodecOperationImpl(this.f);
        final int setMediaFormat = b.setMediaFormat(mediaFormat);
        if (setMediaFormat != 0) {
            TLog.w("%s setOutputFormat has a error code: %d, %s", "TuSdkAudioEncoder", setMediaFormat, mediaFormat);
            return setMediaFormat;
        }
        (this.b = b).setAudioRender(this.e);
        return 0;
    }
    
    public TuSdkAudioInfo getAudioInfo() {
        if (this.b == null) {
            return null;
        }
        return this.b.getAudioInfo();
    }
    
    public TuSdkAudioEncodecOperation getOperation() {
        if (this.b == null) {
            TLog.w("%s getOperation need setOutputFormat first", "TuSdkAudioEncoder");
        }
        return this.b;
    }
    
    public void setMediaSync(final TuSdkAudioEncodecSync d) {
        this.d = d;
    }
    
    public void setAudioRender(final TuSdkAudioRender tuSdkAudioRender) {
        this.e = tuSdkAudioRender;
        if (this.b != null) {
            this.b.setAudioRender(tuSdkAudioRender);
        }
    }
    
    public void release() {
        if (this.a == 1) {
            return;
        }
        this.a = 1;
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
    
    public boolean prepare() {
        if (this.a > -1) {
            TLog.w("%s has prepared.", "TuSdkAudioEncoder");
            return false;
        }
        if (this.b == null) {
            TLog.w("%s run need set setOutputFormat first.", "TuSdkAudioEncoder");
            return false;
        }
        if (this.c == null) {
            TLog.w("%s need setListener first.", "TuSdkAudioEncoder");
            return false;
        }
        this.a = 0;
        return true;
    }
    
    public void signalEndOfInputStream(final long n) {
        if (this.b != null) {
            this.b.signalEndOfInputStream(n);
        }
    }
    
    public void autoFillMuteData(final long n, final long n2, final boolean b) {
        if (this.b != null) {
            this.b.autoFillMuteData(n, n2, b);
        }
    }
}
