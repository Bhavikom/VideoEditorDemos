// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record;

import android.opengl.EGLContext;
import android.opengl.EGL14;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkMicRecord;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileRecorderImpl;
import java.io.File;
//import org.lasque.tusdk.core.utils.JVMUtils;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
//import org.lasque.tusdk.core.seles.SelesContext;
import android.opengl.GLES20;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.opengl.GLSurfaceView;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
//import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.seles.sources.SelesWatermark;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileRecorder;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioRecord;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioRecord;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkMicRecord;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileRecorder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileRecorderImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesWatermark;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.JVMUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;

@TargetApi(18)
public class TuSdkCameraRecorder implements TuSdkMediaRecordHub
{
    private TuSdkMediaRecordHubStatus a;
    private TuSdkMediaRecordHubListener b;
    private TuSdkAudioRecord c;
    private TuSdkAudioPitch d;
    private TuSdkAudioPitch e;
    private TuSdkMediaFileRecorder f;
    private TuSdkRecordSurface g;
    private SelesWatermark h;
    private MediaFormat i;
    private MediaFormat j;
    private TuSdkSurfaceRender k;
    private TuSdkAudioRender l;
    private final TuSdkFilterBridge m;
    private boolean n;
    private float o;
    private float p;
    private boolean q;
    private TuSdkAudioRecord.TuSdkAudioRecordListener r;
    private TuSdkAudioPitchSync s;
    private TuSdkAudioPitchSync t;
    private TuSdkMediaFileRecorder.TuSdkMediaFileRecorderProgress u;
    private GLSurfaceView.Renderer v;
    
    public TuSdkCameraRecorder() {
        this.a = TuSdkMediaRecordHubStatus.UNINITIALIZED;
        this.m = new TuSdkFilterBridge();
        this.n = false;
        this.o = 1.0f;
        this.p = 1.0f;
        this.r = new TuSdkAudioRecord.TuSdkAudioRecordListener() {
            @Override
            public void onAudioRecordOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkCameraRecorder.this.e != null) {
                    TuSdkCameraRecorder.this.e.queueInputBuffer(byteBuffer, bufferInfo);
                }
            }
            
            @Override
            public void onAudioRecordError(final int i) {
                TLog.e("%s AudioRecordError  code  is :%s", "TuSdkCameraRecorder", i);
            }
        };
        this.s = new TuSdkAudioPitchSync() {
            @Override
            public void syncAudioPitchOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                final TuSdkMediaFileRecorder b = TuSdkCameraRecorder.this.f;
                if (b == null) {
                    return;
                }
                b.newFrameReadyWithAudio(byteBuffer, bufferInfo);
            }
            
            @Override
            public void release() {
            }
        };
        this.t = new TuSdkAudioPitchSync() {
            @Override
            public void syncAudioPitchOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkCameraRecorder.this.d != null) {
                    TuSdkCameraRecorder.this.d.queueInputBuffer(byteBuffer, bufferInfo);
                }
            }
            
            @Override
            public void release() {
            }
        };
        this.u = new TuSdkMediaFileRecorder.TuSdkMediaFileRecorderProgress() {
            @Override
            public void onProgress(final long n, final TuSdkMediaDataSource tuSdkMediaDataSource) {
                if (TuSdkCameraRecorder.this.b != null) {
                    TuSdkCameraRecorder.this.b.onProgress(n, tuSdkMediaDataSource);
                }
            }
            
            @Override
            public void onCompleted(final Exception ex, final TuSdkMediaDataSource tuSdkMediaDataSource, final TuSdkMediaTimeline tuSdkMediaTimeline) {
                if (TuSdkCameraRecorder.this.f != null) {
                    TuSdkCameraRecorder.this.f.disconnect();
                    TuSdkCameraRecorder.this.f = null;
                }
                TuSdkCameraRecorder.this.a(TuSdkMediaRecordHubStatus.STOP);
                if (TuSdkCameraRecorder.this.b != null) {
                    TuSdkCameraRecorder.this.b.onCompleted(ex, tuSdkMediaDataSource, tuSdkMediaTimeline);
                }
            }
        };
        this.v = (GLSurfaceView.Renderer)new GLSurfaceView.Renderer() {
            public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
                GLES20.glDisable(2929);
                TuSdkCameraRecorder.this.initInGLThread();
            }
            
            public void onSurfaceChanged(final GL10 gl10, final int n, final int n2) {
                GLES20.glViewport(0, 0, n, n2);
            }
            
            public void onDrawFrame(final GL10 gl10) {
                GLES20.glClear(16640);
                TuSdkCameraRecorder.this.newFrameReadyInGLThread();
            }
        };
    }
    
    @Override
    public void appendRecordSurface(final TuSdkRecordSurface g) {
        if (this.a != TuSdkMediaRecordHubStatus.UNINITIALIZED) {
            TLog.w("%s appendRecordSurface need before start.", "TuSdkCameraRecorder");
            return;
        }
        this.g = g;
        if (this.g != null) {
            this.g.addTarget(this.m, 0);
        }
    }
    
    @Override
    public void setOutputVideoFormat(final MediaFormat i) {
        if (this.a != TuSdkMediaRecordHubStatus.UNINITIALIZED) {
            TLog.w("%s setOutputVideoFormat need before start.", "TuSdkCameraRecorder");
            return;
        }
        this.i = i;
    }
    
    @Override
    public void setOutputAudioFormat(final MediaFormat j) {
        if (this.a != TuSdkMediaRecordHubStatus.UNINITIALIZED) {
            TLog.w("%s setOutputAudioFormat need before start.", "TuSdkCameraRecorder");
            return;
        }
        this.j = j;
    }
    
    @Override
    public void setSurfaceRender(final TuSdkSurfaceRender k) {
        this.k = k;
        this.m.setSurfaceDraw(this.k);
    }
    
    @Override
    public void addTarget(final SelesContext.SelesInput selesInput, final int n) {
        this.m.addTarget(selesInput, n);
    }
    
    @Override
    public void removeTarget(final SelesContext.SelesInput selesInput) {
        this.m.removeTarget(selesInput);
    }
    
    @Override
    public void setAudioRender(final TuSdkAudioRender l) {
        this.l = l;
    }
    
    @Override
    public void setRecordListener(final TuSdkMediaRecordHubListener b) {
        this.b = b;
    }
    
    @Override
    public void setWatermark(final SelesWatermark h) {
        this.h = h;
    }
    
    private void a(final TuSdkMediaRecordHubStatus a) {
        this.a = a;
        if (this.b != null) {
            this.b.onStatusChanged(a, this);
        }
    }
    
    @Override
    public TuSdkMediaRecordHubStatus getState() {
        return this.a;
    }
    
    private boolean a() {
        switch (this.a.ordinal()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: {
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean b() {
        switch (this.a.ordinal()) {
            case 1:
            case 2:
            case 4:
            case 5:
            case 6: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public void release() {
        if (this.a == TuSdkMediaRecordHubStatus.RELEASED) {
            return;
        }
        this.a(TuSdkMediaRecordHubStatus.RELEASED);
        if (this.f != null) {
            this.f.release();
            this.f = null;
        }
        this.c();
        TLog.e("[debug] %s ============ release record ", "TuSdkCameraRecorder");
        if (this.m != null) {
            this.m.destroy();
        }
        JVMUtils.runGC();
    }
    
    private void c() {
        if (this.c != null) {
            this.c.stop();
            this.c.release();
            this.c = null;
        }
        if (this.d != null) {
            this.d.reset();
            this.d.release();
            this.d = null;
        }
        if (this.e != null) {
            this.e.reset();
            this.e.release();
            this.e = null;
        }
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
    public boolean start(final File file) {
        switch (this.a.ordinal()) {
            case 1:
            case 2: {
                if (this.f != null) {
                    TLog.w("%s start need wait stop compeleted.", "TuSdkCameraRecorder");
                    return false;
                }
                if (file == null) {
                    TLog.w("%s start need put outputFile.", "TuSdkCameraRecorder");
                    return false;
                }
                if (file.exists()) {
                    TLog.w("%s start with outputFile exists.", "TuSdkCameraRecorder");
                    return false;
                }
                if (this.g == null) {
                    TLog.w("%s start need appendRecordSurface first.", "TuSdkCameraRecorder");
                    return false;
                }
                if (this.i == null) {
                    TLog.w("%s start need setOutputVideoFormat first.", "TuSdkCameraRecorder");
                    return false;
                }
                ThreadHelper.runThread(new Runnable() {
                    @Override
                    public void run() {
                        TuSdkCameraRecorder.this.f = new TuSdkMediaFileRecorderImpl();
                        TuSdkCameraRecorder.this.f.setOutputVideoFormat(TuSdkCameraRecorder.this.i);
                        TuSdkCameraRecorder.this.f.setOutputAudioFormat(TuSdkCameraRecorder.this.j);
                        TuSdkCameraRecorder.this.f.setSurfaceRender(TuSdkCameraRecorder.this.k);
                        TuSdkCameraRecorder.this.f.setAudioRender(TuSdkCameraRecorder.this.l);
                        TuSdkCameraRecorder.this.f.changeSpeed(TuSdkCameraRecorder.this.o);
                        TuSdkCameraRecorder.this.f.setOutputFilePath(file.getAbsolutePath());
                        TuSdkCameraRecorder.this.f.setRecordProgress(TuSdkCameraRecorder.this.u);
                        TuSdkCameraRecorder.this.f.setFilterBridge(TuSdkCameraRecorder.this.m);
                        if (TuSdkCameraRecorder.this.h != null) {
                            TuSdkCameraRecorder.this.f.setWatermark(TuSdkCameraRecorder.this.h);
                        }
                        if (TuSdkCameraRecorder.this.f.getOutputAudioInfo() != null) {
                            TuSdkCameraRecorder.this.c = new TuSdkMicRecord();
                            TuSdkCameraRecorder.this.c.setAudioInfo(TuSdkCameraRecorder.this.f.getOutputAudioInfo());
                            TuSdkCameraRecorder.this.c.setListener(TuSdkCameraRecorder.this.r);
                            TuSdkCameraRecorder.this.c.startRecording();
                            TuSdkCameraRecorder.this.d = new TuSdkAudioPitchHardImpl(TuSdkCameraRecorder.this.f.getOutputAudioInfo());
                            TuSdkCameraRecorder.this.d.changePitch(TuSdkCameraRecorder.this.p);
                            TuSdkCameraRecorder.this.d.setMediaSync(TuSdkCameraRecorder.this.s);
                            TuSdkCameraRecorder.this.e = new TuSdkAudioPitchHardImpl(TuSdkCameraRecorder.this.f.getOutputAudioInfo());
                            TuSdkCameraRecorder.this.e.changeSpeed(TuSdkCameraRecorder.this.o);
                            TuSdkCameraRecorder.this.e.setMediaSync(TuSdkCameraRecorder.this.t);
                        }
                        TuSdkCameraRecorder.this.a(TuSdkMediaRecordHubStatus.START);
                        if (TuSdkCameraRecorder.this.q) {
                            TuSdkCameraRecorder.this.pause();
                        }
                    }
                });
                return true;
            }
            default: {
                TLog.w("%s start had incorrect status: %s", "TuSdkCameraRecorder", this.a);
                return false;
            }
        }
    }
    
    @Override
    public void stop() {
        switch (this.a.ordinal()) {
            case 4:
            case 7: {
                TLog.e("[debug] %s ============ stop record ", "TuSdkCameraRecorder");
                if (this.f == null) {
                    this.a(TuSdkMediaRecordHubStatus.STOP);
                }
                else {
                    this.a(TuSdkMediaRecordHubStatus.PREPARE_STOP);
                    this.f.stopRecord();
                }
                this.c();
            }
            default: {
                TLog.w("%s stop had incorrect status: %s", "TuSdkCameraRecorder", this.a);
            }
        }
    }
    
    @Override
    public boolean pause() {
        this.q = true;
        if (this.a != TuSdkMediaRecordHubStatus.START_RECORD) {
            TLog.w("%s pause had incorrect status: %s", "TuSdkCameraRecorder", this.a);
            return false;
        }
        this.a(TuSdkMediaRecordHubStatus.PAUSE_RECORD);
        if (this.f != null) {
            this.f.pauseRecord();
        }
        if (this.c != null) {
            this.c.stop();
        }
        this.q = false;
        return true;
    }
    
    @Override
    public boolean resume() {
        if (this.a != TuSdkMediaRecordHubStatus.PAUSE_RECORD) {
            TLog.w("%s resume had incorrect status: %s", "TuSdkCameraRecorder", this.a);
            return false;
        }
        if (this.c != null) {
            this.c.startRecording();
        }
        if (this.f != null) {
            this.f.resumeRecord();
        }
        this.a(TuSdkMediaRecordHubStatus.START_RECORD);
        return true;
    }
    
    @Override
    public void reset() {
        if (!this.b()) {
            TLog.w("%s reset had incorrect status: %s", "TuSdkCameraRecorder", this.a);
            return;
        }
        if (this.d != null) {
            this.d.reset();
        }
        if (this.e != null) {
            this.e.reset();
        }
        if (this.f != null) {
            this.f.changeSpeed(1.0f);
        }
        if (this.f != null) {
            this.f.release();
        }
        if (this.c != null) {
            this.c.release();
        }
        if (this.e != null) {
            this.e.release();
        }
        if (this.d != null) {
            this.d.reset();
        }
        this.f = null;
        this.c = null;
        this.d = null;
        this.e = null;
        this.a(TuSdkMediaRecordHubStatus.UNINITIALIZED);
    }
    
    @Override
    public void changeSpeed(final float o) {
        if (o <= 0.0f || this.o == o) {
            return;
        }
        if (!this.b()) {
            TLog.w("%s changeSpeed had incorrect status: %s", "TuSdkCameraRecorder", this.a);
            return;
        }
        this.o = o;
        if (this.e != null) {
            this.e.changeSpeed(this.o);
        }
        if (this.f != null) {
            this.f.changeSpeed(this.o);
        }
    }
    
    @Override
    public void changePitch(final float p) {
        if (this.p <= 0.0f || this.p == p) {
            return;
        }
        if (!this.b()) {
            TLog.w("%s changePitch had incorrect status: %s", "TuSdkCameraRecorder", this.a);
            return;
        }
        this.p = p;
        if (this.d != null) {
            this.d.changePitch(this.p);
        }
        if (this.f != null) {
            this.f.changeSpeed(1.0f);
        }
    }
    
    @Override
    public GLSurfaceView.Renderer getExtenalRenderer() {
        return this.v;
    }
    
    @Override
    public void initInGLThread() {
        if (this.g == null) {
            return;
        }
        this.g.initInGLThread();
    }
    
    @Override
    public void newFrameReadyInGLThread() {
        if (this.g == null) {
            return;
        }
        final long nanoTime = System.nanoTime();
        this.g.updateSurfaceTexImage();
        if (this.n) {
            return;
        }
        this.n = true;
        this.g.newFrameReadyInGLThread(nanoTime);
        this.a(this.f, nanoTime);
        this.n = false;
    }
    
    private void a(final TuSdkMediaFileRecorder tuSdkMediaFileRecorder, final long n) {
        if (tuSdkMediaFileRecorder == null || !this.a()) {
            return;
        }
        if (this.a == TuSdkMediaRecordHubStatus.START) {
            this.a(TuSdkMediaRecordHubStatus.PREPARE_RECORD);
            ThreadHelper.runThread(new Runnable() {
                final /* synthetic */ EGLContext b = EGL14.eglGetCurrentContext();
                
                @Override
                public void run() {
                    tuSdkMediaFileRecorder.startRecord(this.b);
                    tuSdkMediaFileRecorder.newFrameReadyInGLThread(n);
                    TuSdkCameraRecorder.this.a(TuSdkMediaRecordHubStatus.START_RECORD);
                    if (TuSdkCameraRecorder.this.q) {
                        TuSdkCameraRecorder.this.pause();
                    }
                }
            });
            return;
        }
        tuSdkMediaFileRecorder.newFrameReadyInGLThread(n);
    }
}
