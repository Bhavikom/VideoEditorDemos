// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit;

//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaDecodecSync;
import android.graphics.SurfaceTexture;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.exception.TuSdkTaskExitException;
import android.media.MediaCodec;
import android.opengl.GLES20;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
//import org.lasque.tusdk.core.media.codec.decoder.TuSdkDecoderListener;
import android.opengl.GLSurfaceView;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
//import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
//import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
//import org.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFileDecoder;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFilePlayerSync;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder.TuSdkDecoderListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFileDecoder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.exception.TuSdkTaskExitException;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkMediaFilePlayerSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;

@TargetApi(16)
public class TuSdkMediaFilePlayerImpl implements TuSdkMediaFilePlayer
{
    private int a;
    private final TuSdkMediaFilePlayerSync b;
    private TuSdkSurfaceDraw c;
    private TuSdkAudioRender d;
    private TuSdkMediaFileDecoder e;
    private SelesSurfaceReceiver f;
    private TuSdkFilterBridge g;
    private TuSdkMediaPlayerListener h;
    private TuSdkMediaDataSource i;
    private boolean j;
    private long k;
    private GLSurfaceView.Renderer l;
    private TuSdkDecoderListener m;
    private TuSdkDecoderListener n;
    
    public TuSdkMediaFilePlayerImpl() {
        this.a = -1;
        this.b = new TuSdkMediaFilePlayerSync();
        this.g = new TuSdkFilterBridge();
        this.k = -1L;
        this.l = (GLSurfaceView.Renderer)new GLSurfaceView.Renderer() {
            public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
                GLES20.glDisable(2929);
                TuSdkMediaFilePlayerImpl.this.initInGLThread();
            }
            
            public void onSurfaceChanged(final GL10 gl10, final int n, final int n2) {
                GLES20.glViewport(0, 0, n, n2);
            }
            
            public void onDrawFrame(final GL10 gl10) {
                GLES20.glClear(16640);
                TuSdkMediaFilePlayerImpl.this.newFrameReadyInGLThread();
            }
        };
        this.m = new TuSdkDecoderListener() {
            @Override
            public void onDecoderUpdated(final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkMediaFilePlayerImpl.this.b.isVideoEos(bufferInfo.presentationTimeUs)) {
                    this.onDecoderCompleted(null);
                    return;
                }
                TuSdkMediaFilePlayerImpl.this.a(false);
            }
            
            @Override
            public void onDecoderCompleted(final Exception ex) {
                TuSdkMediaFilePlayerImpl.this.pause();
                TuSdkMediaFilePlayerImpl.this.b.syncVideoDecodeCompleted();
                if (ex != null) {
                    TuSdkMediaFilePlayerImpl.this.a((ex instanceof TuSdkTaskExitException) ? null : ex);
                }
                TuSdkMediaFilePlayerImpl.this.a(true);
                TLog.d("%s VideoDecoderListener process buffer stream end", "TuSdkMediaFilePlayerImpl");
            }
        };
        this.n = new TuSdkDecoderListener() {
            @Override
            public void onDecoderUpdated(final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkMediaFilePlayerImpl.this.b.isAudioEos(bufferInfo.presentationTimeUs)) {
                    this.onDecoderCompleted(null);
                }
            }
            
            @Override
            public void onDecoderCompleted(final Exception ex) {
                if (ex != null) {
                    if (ex instanceof TuSdkTaskExitException) {
                        TuSdkMediaFilePlayerImpl.this.a(null);
                        return;
                    }
                    TLog.e(ex, "%s AudioDecoderListener catch a exception, skip audio and ignore.", "TuSdkMediaFilePlayerImpl");
                }
                TuSdkMediaFilePlayerImpl.this.b.syncAudioDecodeCompleted();
                if (!TuSdkMediaFilePlayerImpl.this.b.isAudioDecodeCrashed()) {
                    TLog.d("%s AudioDecoderListener process buffer stream end", "TuSdkMediaFilePlayerImpl");
                }
            }
        };
    }

    @Override
    public void setSurfaceDraw(TuSdkSurfaceDraw tuSdkSurfaceDraw) {
        this.c = tuSdkSurfaceDraw;
        if (this.g != null) {
            this.g.setSurfaceDraw(tuSdkSurfaceDraw);
        }
    }

    @Override
    public void setAudioRender(TuSdkAudioRender tuSdkAudioRender) {
        this.d = tuSdkAudioRender;
        if (this.e != null) {
            this.e.setAudioRender(tuSdkAudioRender);
        }
    }

    @Override
    public void setListener(TuSdkMediaPlayerListener h) {
        this.h = h;
    }


    @Override
    public void setMediaDataSource(TuSdkMediaDataSource i) {
        if (i == null || !i.isValid()) {
            TLog.w("%s setMediaDataSource not exists: %s", "TuSdkMediaFilePlayerImpl", i);
            return;
        }
        this.i = i;
    }

    @Override
    public TuSdkFilterBridge getFilterBridge() {
        return this.g;
    }
    
    @Override
    public GLSurfaceView.Renderer getExtenalRenderer() {
        return this.l;
    }
    
    @Override
    public boolean load(final boolean j) {
        if (this.a != -1) {
            TLog.w("%s repeated loading is not allowed.", "TuSdkMediaFilePlayerImpl");
            return false;
        }
        this.j = j;
        this.a = 0;
        return this.c();
    }
    
    @Override
    public void initInGLThread() {
        if (this.f == null || this.a != 0) {
            TLog.w("%s initInGLThread need after load, before release.", "TuSdkMediaFilePlayerImpl");
            return;
        }
        this.a = 1;
        this.a();
        this.f.initInGLThread();
    }
    
    @Override
    public void newFrameReadyInGLThread() {
        if (this.f == null || this.a != 1) {
            TLog.w("%s newFrameReadyInGLThread need after load, before release.", "TuSdkMediaFilePlayerImpl");
            return;
        }
        this.f.updateSurfaceTexImage(this.f.getSurfaceTexTimestampNs());
    }
    
    @Override
    public void release() {
        if (this.a == 2) {
            TLog.w("%s already released.", "TuSdkMediaFilePlayerImpl");
            return;
        }
        this.a = 2;
        this.a();
        this.b.release();
        if (this.f != null) {
            this.f.destroy();
            this.f = null;
        }
        if (this.g != null) {
            this.g.destroy();
            this.g = null;
        }
        if (this.e != null) {
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
    
    private void a() {
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                if (TuSdkMediaFilePlayerImpl.this.h != null) {
                    TuSdkMediaFilePlayerImpl.this.h.onStateChanged(TuSdkMediaFilePlayerImpl.this.b.isPause() ? 1 : 0);
                }
            }
        });
    }
    
    private void a(final boolean b) {
        final long elapsedUs = this.elapsedUs();
        final long durationUs = this.durationUs();
        if (durationUs < 1L) {
            return;
        }
        if (this.j) {
            this.j = false;
            this.pause();
        }
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                if (TuSdkMediaFilePlayerImpl.this.h == null) {
                    return;
                }
                TuSdkMediaFilePlayerImpl.this.h.onProgress(elapsedUs, TuSdkMediaFilePlayerImpl.this.i, durationUs);
            }
        });
    }
    
    private void a(final Exception ex) {
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                TuSdkMediaFilePlayerImpl.this.release();
                if (TuSdkMediaFilePlayerImpl.this.h == null) {
                    return;
                }
                TuSdkMediaFilePlayerImpl.this.h.onCompleted(ex, TuSdkMediaFilePlayerImpl.this.i);
            }
        });
    }
    
    @Override
    public long durationUs() {
        return this.b.totalVideoDurationUs();
    }
    
    @Override
    public long elapsedUs() {
        return this.b.lastVideoTimestampUs();
    }
    
    @Override
    public boolean isSupportPrecise() {
        return this.b.isSupportPrecise();
    }
    
    @Override
    public boolean isPause() {
        return this.a != 1 || this.b.isPause();
    }
    
    @Override
    public void pause() {
        if (this.isPause()) {
            return;
        }
        this.b.setPause();
        this.a();
    }
    
    @Override
    public void resume() {
        if (this.a != 1 || !this.b.isPause()) {
            return;
        }
        this.b();
        if (this.k > -1L) {
            this.b.syncFlushAndSeekto(this.k);
            this.k = -1L;
        }
        this.b.setPlay();
        this.a();
    }
    
    private void b() {
        if (!this.b.syncNeedRestart()) {
            return;
        }
        this.k = (this.b.isReverse() ? this.b.totalVideoDurationUs() : 0L);
    }
    
    @Override
    public void reset() {
        this.b.setReset();
    }
    
    @Override
    public void setSpeed(final float speed) {
        this.b.setSpeed(speed);
    }
    
    @Override
    public float speed() {
        return this.b.speed();
    }
    
    @Override
    public void setReverse(final boolean reverse) {
        this.b.setReverse(reverse);
    }
    
    @Override
    public boolean isReverse() {
        return this.b.isReverse();
    }
    
    @Override
    public long seekToPercentage(float n) {
        if (n < 0.0f) {
            n = 0.0f;
        }
        else if (n > 1.0f) {
            n = 1.0f;
        }
        final long n2 = (long)(n * this.durationUs());
        this.seekTo(n2);
        return n2;
    }
    
    @Override
    public void seekTo(final long n) {
        this.a(n, 2);
    }
    
    private void a(final long n, final int n2) {
        if (this.a != 1) {
            return;
        }
        this.b.pauseSave();
        if (this.e != null) {
            this.k = this.e.seekTo(n, n2);
        }
        this.b.resumeSave();
    }
    
    private boolean c() {
        (this.f = new SelesSurfaceReceiver()).setTextureCoordinateBuilder(new SelesVerticeCoordinateCropBuilderImpl(false));
        this.f.addTarget(this.g, 0);
        this.f.setSurfaceTextureListener((SurfaceTexture.OnFrameAvailableListener)new SurfaceTexture.OnFrameAvailableListener() {
            public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
                if (TuSdkMediaFilePlayerImpl.this.h != null) {
                    TuSdkMediaFilePlayerImpl.this.h.onFrameAvailable();
                }
            }
        });
        (this.e = new TuSdkMediaFileDecoder(true, true)).setMediaDataSource(this.i);
        this.e.setMediaSync(this.b);
        this.e.setSurfaceReceiver(this.f);
        this.e.setAudioRender(this.d);
        this.e.setListener(this.m, this.n);
        this.e.prepare();
        if (!this.e.isVideoStared()) {
            this.a(new Exception(String.format("%s VideoFileDecoder start failed", "TuSdkMediaFilePlayerImpl")));
            return false;
        }
        if (!this.e.isAudioStared()) {
            this.e.releaseAudioDecoder();
        }
        return true;
    }
}
