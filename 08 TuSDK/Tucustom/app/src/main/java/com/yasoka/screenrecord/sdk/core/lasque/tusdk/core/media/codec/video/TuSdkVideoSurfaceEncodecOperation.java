// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video;

import android.media.MediaCodec;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import android.os.Bundle;
import android.os.Build;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.utils.TLog;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import android.view.Surface;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;

@TargetApi(18)
public class TuSdkVideoSurfaceEncodecOperation implements TuSdkEncodecOperation
{
    private Surface a;
    private TuSdkMediaCodec b;
    private boolean c;
    private int d;
    private final long e = 10000L;
    private TuSdkCodecOutput.TuSdkEncodecOutput f;
    private ByteBuffer[] g;
    private TuSdkVideoSurfaceEncodecOperationPatch h;
    private final TuSdkVideoInfo i;
    private boolean j;
    
    public TuSdkVideoSurfaceEncodecOperation(final TuSdkCodecOutput.TuSdkEncodecOutput f) {
        this.c = false;
        this.d = -1;
        this.i = new TuSdkVideoInfo();
        this.j = false;
        if (f == null) {
            throw new NullPointerException(String.format("%s init failed, encodecOutput is NULL", "TuSdkVideoSurfaceEncodecOperation"));
        }
        this.f = f;
    }
    
    public Surface getSurface() {
        if (this.a == null) {
            TLog.w("%s requestSurface need setMediaFormat first", "TuSdkVideoSurfaceEncodecOperation");
            return null;
        }
        return this.a;
    }
    
    public TuSdkVideoSurfaceEncodecOperationPatch getCodecPatch() {
        if (this.h == null) {
            this.h = new TuSdkVideoSurfaceEncodecOperationPatch();
        }
        return this.h;
    }
    
    public void setCodecPatch(final TuSdkVideoSurfaceEncodecOperationPatch h) {
        this.h = h;
    }
    
    public TuSdkVideoInfo getVideoInfo() {
        return this.i;
    }
    
    public int setMediaFormat(final MediaFormat encodecInfo) {
        final int checkVideoEncodec = TuSdkMediaFormat.checkVideoEncodec(encodecInfo);
        if (checkVideoEncodec != 0) {
            return checkVideoEncodec;
        }
        TuSdkMediaCodec b = null;
        if (this.getCodecPatch() != null) {
            this.c = this.getCodecPatch().patchRequestKeyFrame(encodecInfo);
            b = this.getCodecPatch().patchMediaCodec();
        }
        if (b == null) {
            b = TuSdkMediaCodecImpl.createEncoderByType(encodecInfo.getString("mime"));
        }
        if (b.configureError() != null) {
            TLog.e(b.configureError(), "%s setMediaFormat create MediaCodec failed", "TuSdkVideoSurfaceEncodecOperation");
            return 256;
        }
        (this.b = b).configure(encodecInfo, null, null, 1);
        this.a = b.createInputSurface();
        this.i.setEncodecInfo(encodecInfo);
        return 0;
    }
    
    public void requestKeyFrame() {
        if (this.b == null || this.j || Build.VERSION.SDK_INT < 19) {
            return;
        }
        final Bundle parameters = new Bundle();
        parameters.putInt("request-sync", 0);
        this.b.setParameters(parameters);
    }
    
    public void notifyNewFrameReady() {
        if (this.c) {
            this.requestKeyFrame();
        }
    }
    
    public void signalEndOfInputStream() {
        if (this.b == null || this.j) {
            return;
        }
        this.b.signalEndOfInputStream();
    }
    
    @Override
    public void encodecRelease() {
        this.j = true;
        if (this.b == null) {
            return;
        }
        this.b.stop();
        this.b.release();
        this.b = null;
    }
    
    @Override
    protected void finalize() {
        this.encodecRelease();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    @Override
    public boolean isEncodecStarted() {
        return this.b != null && this.b.isStarted();
    }
    
    @Override
    public void encodecException(final Exception ex) {
        if (this.f == null) {
            TLog.e(ex, "%s the Thread catch exception, The thread wil exit.", "TuSdkVideoSurfaceEncodecOperation");
            return;
        }
        this.f.onCatchedException(ex);
    }
    
    @Override
    public void flush() {
        if (this.b != null) {
            this.b.flush();
        }
    }
    
    @Override
    public boolean encodecInit(final TuSdkMediaMuxer tuSdkMediaMuxer) {
        final TuSdkMediaCodec b = this.b;
        final TuSdkCodecOutput.TuSdkEncodecOutput f = this.f;
        if (b == null || f == null) {
            TLog.w("%s decodecInit need setMediaFormat first", "TuSdkVideoSurfaceEncodecOperation");
            return false;
        }
        b.start();
        this.g = b.getOutputBuffers();
        return true;
    }
    
    @Override
    public boolean encodecProcessUntilEnd(final TuSdkMediaMuxer tuSdkMediaMuxer) {
        final TuSdkMediaCodec b = this.b;
        final TuSdkCodecOutput.TuSdkEncodecOutput f = this.f;
        if (b == null || f == null) {
            return true;
        }
        if (this.c) {
            this.requestKeyFrame();
        }
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        final int dequeueOutputBuffer = b.dequeueOutputBuffer(bufferInfo, 10000L);
        switch (dequeueOutputBuffer) {
            case -2: {
                this.i.setMediaFormat(b.getOutputFormat());
                this.d = tuSdkMediaMuxer.addTrack(b.getOutputFormat());
                f.outputFormatChanged(b.getOutputFormat());
                break;
            }
            case -1: {
                break;
            }
            case -3: {
                this.g = b.getOutputBuffers();
                break;
            }
            default: {
                if (dequeueOutputBuffer < 0) {
                    break;
                }
                if (bufferInfo.size > 0) {
                    f.processOutputBuffer(tuSdkMediaMuxer, this.d, this.g[dequeueOutputBuffer], bufferInfo);
                }
                if (!this.j) {
                    b.releaseOutputBuffer(dequeueOutputBuffer, false);
                }
                f.updated(bufferInfo);
                break;
            }
        }
        return (bufferInfo.flags & 0x4) != 0x0 && f.updatedToEOS(bufferInfo);
    }
}
