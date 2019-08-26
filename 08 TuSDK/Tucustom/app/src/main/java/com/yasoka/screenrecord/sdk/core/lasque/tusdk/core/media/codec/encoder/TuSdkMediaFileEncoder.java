// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder;

//import org.lasque.tusdk.core.TuSdk;
import android.opengl.EGLContext;
//import org.lasque.tusdk.core.utils.FileHelper;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.seles.sources.SelesWatermark;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
//import org.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import android.graphics.RectF;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaEncodecSync;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkMediaEncodecSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesWatermark;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.io.File;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public class TuSdkMediaFileEncoder
{
    public static final int TRANS_STATE_UNINITIALIZED = -1;
    public static final int TRANS_STATE_STARTED = 0;
    public static final int TRANS_STATE_STOPPED = 1;
    private int a;
    private TuSdkMediaDataSource b;
    private File c;
    private TuSdkSurfaceRender d;
    private TuSdkAudioRender e;
    private TuSdkVideoSurfaceEncoder f;
    private TuSdkAudioEncoder g;
    private TuSdkMediaFileMuxer h;
    private TuSdkMediaEncodecSync i;
    private TuSdkEncoderListener j;
    private TuSdkVideoSurfaceEncoderListener k;
    private ImageOrientation l;
    private RectF m;
    
    public TuSdkMediaFileEncoder() {
        this.a = -1;
    }
    
    public TuSdkAudioEncoder getAudioEncoder() {
        return this.g;
    }
    
    public TuSdkVideoSurfaceEncoder getVideoEncoder() {
        return this.f;
    }
    
    public boolean hasVideoEncoder() {
        return this.f != null;
    }
    
    public boolean hasAudioEncoder() {
        return this.g != null;
    }
    
    public TuSdkMediaDataSource getOutputDataSource() {
        return this.b;
    }
    
    public TuSdkFilterBridge getFilterBridge() {
        if (this.f == null) {
            TLog.w("%s getFilterBridge need setOutputVideoFormat first.", "TuSdkMediaFileEncoder");
            return null;
        }
        return this.f.getFilterBridge();
    }
    
    public void setFilterBridge(final TuSdkFilterBridge filterBridge) {
        if (this.f == null) {
            TLog.w("%s setFilterBridge need setOutputVideoFormat first.", "TuSdkMediaFileEncoder");
            return;
        }
        this.f.setFilterBridge(filterBridge);
    }
    
    public void disconnect() {
        if (this.f == null) {
            TLog.w("%s disconnect need setOutputVideoFormat first.", "TuSdkMediaFileEncoder");
            return;
        }
        this.f.disconnect();
    }
    
    public void setSurfacePause(final boolean pause) {
        if (this.f == null) {
            TLog.w("%s setPause need setOutputVideoFormat first.", "TuSdkMediaFileEncoder");
            return;
        }
        this.f.setPause(pause);
    }
    
    public TuSdkSize getOutputSize() {
        if (this.f == null) {
            TLog.w("%s getOutputSize need setOutputVideoFormat first", "TuSdkMediaFileEncoder");
        }
        return this.f.getOutputSize();
    }
    
    public TuSdkEncodecOperation getVideoOperation() {
        if (this.f == null) {
            TLog.w("%s getAudioOperation need setOutputAudioFormat first", "TuSdkMediaFileEncoder");
        }
        return this.f.getOperation();
    }
    
    public TuSdkAudioEncodecOperation getAudioOperation() {
        if (this.g == null) {
            TLog.w("%s getAudioOperation need setOutputAudioFormat first", "TuSdkMediaFileEncoder");
        }
        return this.g.getOperation();
    }
    
    public void setWatermark(final SelesWatermark watermark) {
        if (this.f == null) {
            return;
        }
        this.f.setWatermark(watermark);
    }
    
    public void setOutputOrientation(final ImageOrientation imageOrientation) {
        if (imageOrientation == null) {
            return;
        }
        this.l = imageOrientation;
        if (this.f != null) {
            this.f.setOutputOrientation(imageOrientation);
        }
    }
    
    public void setCanvasRect(final RectF rectF) {
        if (rectF == null) {
            return;
        }
        this.m = rectF;
        if (this.f != null) {
            this.f.setCanvasRect(rectF);
        }
    }
    
    public void setOutputFilePath(final String s) {
        if (this.a != -1) {
            TLog.w("%s setOutputFilePath need before prepare.", "TuSdkMediaFileEncoder");
            return;
        }
        if (StringHelper.isEmpty(s)) {
            TLog.w("%s setOutputFilePath need a valid file path: %s", "TuSdkMediaFileEncoder", s);
            return;
        }
        this.b = new TuSdkMediaDataSource(s);
    }
    
    public int setOutputVideoFormat(final MediaFormat outputFormat) {
        if (this.a != -1) {
            TLog.w("%s setOutputVideoFormat need before prepare.", "TuSdkMediaFileEncoder");
            return -1;
        }
        this.f = new TuSdkVideoSurfaceEncoder();
        final int setOutputFormat = this.f.setOutputFormat(outputFormat);
        if (setOutputFormat != 0) {
            this.f = null;
            TLog.w("%s setOutputVideoFormat has a error code: %d, %s", "TuSdkMediaFileEncoder", setOutputFormat, outputFormat);
            return setOutputFormat;
        }
        return 0;
    }
    
    public int setOutputAudioFormat(final MediaFormat outputFormat) {
        if (this.a != -1) {
            TLog.w("%s setOutputAudioFormat need before prepare.", "TuSdkMediaFileEncoder");
            return -1;
        }
        this.g = new TuSdkAudioEncoder();
        final int setOutputFormat = this.g.setOutputFormat(outputFormat);
        if (setOutputFormat != 0) {
            this.g = null;
            TLog.w("%s setOutputAudioFormat has a error code: %d, %s", "TuSdkMediaFileEncoder", setOutputFormat, outputFormat);
            return setOutputFormat;
        }
        return 0;
    }
    
    public TuSdkAudioInfo getOutputAudioInfo() {
        if (this.g == null) {
            return null;
        }
        return this.g.getAudioInfo();
    }
    
    public void setSurfaceRender(final TuSdkSurfaceRender tuSdkSurfaceRender) {
        this.d = tuSdkSurfaceRender;
        if (this.f != null) {
            this.f.setSurfaceRender(tuSdkSurfaceRender);
        }
    }
    
    public void setAudioRender(final TuSdkAudioRender tuSdkAudioRender) {
        this.e = tuSdkAudioRender;
        if (this.g != null) {
            this.g.setAudioRender(tuSdkAudioRender);
        }
    }
    
    public void setMediaSync(final TuSdkMediaEncodecSync i) {
        if (i == null) {
            return;
        }
        this.i = i;
        if (this.f != null) {
            this.f.setMediaSync(i.getVideoEncodecSync());
        }
        if (this.g != null) {
            this.g.setMediaSync(i.getAudioEncodecSync());
        }
    }
    
    public void setListener(final TuSdkVideoSurfaceEncoderListener tuSdkVideoSurfaceEncoderListener, final TuSdkEncoderListener tuSdkEncoderListener) {
        this.k = tuSdkVideoSurfaceEncoderListener;
        this.j = tuSdkEncoderListener;
        if (this.f != null) {
            this.f.setListener(tuSdkVideoSurfaceEncoderListener);
        }
        if (this.g != null) {
            this.g.setListener(tuSdkEncoderListener);
        }
    }
    
    public void release() {
        if (this.a == 1) {
            return;
        }
        this.a = 1;
        if (this.f != null) {
            this.f.release();
        }
        if (this.g != null) {
            this.g.release();
        }
        if (this.h != null) {
            this.h.release();
        }
        FileHelper.delete(this.c);
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
    
    public boolean cleanTemp() {
        return FileHelper.rename(this.c, new File(this.b.getPath()));
    }
    
    public boolean prepare(final EGLContext eglContext) {
        return this.prepare(eglContext, false);
    }
    
    public boolean prepare(final EGLContext eglContext, final boolean b) {
        if (this.a > -1) {
            return false;
        }
        if (this.f == null && this.g == null) {
            TLog.w("%s prepare need setOutputVideoFormat or setOutputAudioFormat first.", "TuSdkMediaFileEncoder");
            return false;
        }
        if (this.b == null) {
            TLog.w("%s setOutputFilePath need a valid file path: %s", "TuSdkMediaFileEncoder", this.b);
            return false;
        }
        if (this.i == null) {
            TLog.w("%s prepare need setMediaSync first.", "TuSdkMediaFileEncoder");
            return false;
        }
        this.a = 0;
        this.c = new File(TuSdk.getAppTempPath(), String.format("lsq_media_tmp_%d.tmp", System.currentTimeMillis()));
        (this.h = new TuSdkMediaFileMuxer()).setPath(this.c.getAbsolutePath());
        this.h.setMediaMuxerFormat(0);
        if (this.f != null) {
            this.f.setSurfaceRender(this.d);
            this.f.setListener(this.k);
            this.f.setMediaSync(this.i.getVideoEncodecSync());
            this.f.setOutputOrientation(this.l);
            this.f.setCanvasRect(this.m);
            this.f.prepare(eglContext, b);
            this.h.setVideoOperation(this.f.getOperation());
        }
        if (this.g != null) {
            this.g.setAudioRender(this.e);
            this.g.setListener(this.j);
            this.g.setMediaSync(this.i.getAudioEncodecSync());
            this.g.prepare();
            this.h.setAudioOperation(this.g.getOperation());
        }
        return this.h.prepare();
    }
    
    public void signalVideoEndOfInputStream() {
        if (this.f == null) {
            return;
        }
        this.f.signalEndOfInputStream();
    }
    
    public void requestVideoKeyFrame() {
        if (this.f == null) {
            return;
        }
        this.f.requestKeyFrame();
    }
    
    public void requestVideoRender(final long n) {
        if (this.f == null) {
            TLog.e("%s video encoder is null!", "TuSdkMediaFileEncoder");
            return;
        }
        this.f.requestRender(n);
    }
    
    public void signalAudioEndOfInputStream(final long n) {
        if (this.g == null) {
            return;
        }
        this.g.signalEndOfInputStream(n);
    }
    
    public void autoFillAudioMuteData(final long n, final long n2, final boolean b) {
        if (this.g == null) {
            return;
        }
        this.g.autoFillMuteData(n, n2, b);
    }
}
