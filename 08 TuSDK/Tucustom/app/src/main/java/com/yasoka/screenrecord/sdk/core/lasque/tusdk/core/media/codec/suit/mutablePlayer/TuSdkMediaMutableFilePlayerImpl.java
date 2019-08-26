// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
//import junit.framework.Assert;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.annotation.TargetApi;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackImpl;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import android.graphics.SurfaceTexture;
import android.view.Surface;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.util.Iterator;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.Arrays;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import java.util.ArrayList;
import android.opengl.GLES20;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrack;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
//import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
//import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrack;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;

import java.util.List;

public class TuSdkMediaMutableFilePlayerImpl implements TuSdkMediaMutableFilePlayer
{
    private List<AVAsset> a;
    private TuSdkMediaPlayerStatus b;
    private TuSdkSurfaceDraw c;
    private AudioRender d;
    private VideoRender e;
    private long f;
    private SelesSurfaceReceiver g;
    private TuSdkFilterBridge h;
    private TuSdkMediaPlayerListener i;
    private boolean j;
    private boolean k;
    private AVAssetTrackOutputSouce l;
    private AVAssetTrackCodecDecoder m;
    private AVAssetTrackOutputSouce n;
    private AVAssetTrackCodecDecoder o;
    private TuSdkVideoInfo p;
    private AVMediaSyncClock q;
    private TuSdkAudioTrack r;
    private TuSdkAudioPitch s;
    private TuSdkAudioResample t;
    private float u;
    private GLSurfaceView.Renderer v;
    
    public TuSdkMediaMutableFilePlayerImpl() {
        this.b = TuSdkMediaPlayerStatus.Unknown;
        this.d = new AudioRender();
        this.e = new VideoRender();
        this.f = -1L;
        this.h = new TuSdkFilterBridge();
        this.u = 0.0f;
        this.v = (GLSurfaceView.Renderer)new GLSurfaceView.Renderer() {
            public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
                GLES20.glDisable(2929);
                TuSdkMediaMutableFilePlayerImpl.this.initInGLThread();
            }
            
            public void onSurfaceChanged(final GL10 gl10, final int n, final int n2) {
                GLES20.glViewport(0, 0, n, n2);
            }
            
            public void onDrawFrame(final GL10 gl10) {
                TuSdkMediaMutableFilePlayerImpl.this.newFrameReadyInGLThread();
            }
        };
        this.a = new ArrayList<AVAsset>(2);
        this.q = new AVMediaSyncClock();
    }
    
    @Override
    public final int maxInputSize() {
        return 9;
    }
    
    @Override
    public void setOutputRatio(final float u) {
        this.u = u;
        if (this.g != null) {
            this.g.setOutputSize(this.preferredOutputSize());
        }
    }
    
    @Override
    public final void setMediaDataSource(final TuSdkMediaDataSource tuSdkMediaDataSource) {
        this.setMediaDataSources(Arrays.asList(tuSdkMediaDataSource));
    }
    
    @Override
    public final void setMediaDataSources(final List<TuSdkMediaDataSource> list) {
        if (this.b != TuSdkMediaPlayerStatus.Unknown) {
            TLog.e("%s : The data source is not allowed to be set again after load", this);
            return;
        }
        if (list == null || list.size() == 0) {
            TLog.w("%s setMediaDataSource not exists: %s", "TuSdkMediaMutableFilePlayerImpl", list);
            return;
        }
        if (list.size() > this.maxInputSize()) {
            TLog.w("The maximum number of video supported is %d", this.maxInputSize());
        }
        for (final TuSdkMediaDataSource tuSdkMediaDataSource : (list.size() > this.maxInputSize()) ? list.subList(0, this.maxInputSize() - 1) : list) {
            if (!tuSdkMediaDataSource.isValid()) {
                TLog.e("%s :This data source is invalid", tuSdkMediaDataSource);
            }
            else {
                this.a.add(new AVAssetDataSource(tuSdkMediaDataSource));
            }
        }
    }

    private List<AVAssetTrack> a(AVMediaType var1) {
        ArrayList var2 = new ArrayList();
        Iterator var3 = this.a.iterator();

        while(var3.hasNext()) {
            AVAsset var4 = (AVAsset)var3.next();
            List var5 = var4.tracksWithMediaType(var1);
            var2.addAll(var5);
        }

        return var2;
    }
    
    public TuSdkSize preferredOutputSize() {
        final List<AVAssetTrack> a = this.a(AVMediaType.AVMediaTypeVideo);
        if (a.size() == 0) {
            return null;
        }
        final TuSdkSize presentSize = a.get(0).presentSize();
        if (this.u > 0.0f) {
            final int min = Math.min(presentSize.width, presentSize.height);
            return TuSdkSize.create(min, (int)(min * this.u));
        }
        return presentSize;
    }
    
    public void setEnableClip(final boolean enableClip) {
        if (this.g != null) {
            this.g.setEnableClip(enableClip);
        }
    }
    
    public void setOutputSize(final TuSdkSize outputSize) {
        if (this.g != null) {
            this.g.setOutputSize(outputSize);
        }
    }
    
    @Override
    public void setSurfaceDraw(final TuSdkSurfaceDraw tuSdkSurfaceDraw) {
        this.c = tuSdkSurfaceDraw;
        if (this.h != null) {
            this.h.setSurfaceDraw(tuSdkSurfaceDraw);
        }
    }
    
    @Override
    public void setAudioRender(final TuSdkAudioRender tuSdkAudioRender) {
    }
    
    @Override
    public void setListener(final TuSdkMediaPlayerListener i) {
        this.i = i;
    }
    
    @Override
    public TuSdkFilterBridge getFilterBridge() {
        return this.h;
    }
    
    @Override
    public GLSurfaceView.Renderer getExtenalRenderer() {
        return this.v;
    }
    
    @Override
    public boolean load(final boolean j) {
        if (this.b != TuSdkMediaPlayerStatus.Unknown) {
            TLog.w("%s repeated loading is not allowed.", "TuSdkMediaMutableFilePlayerImpl");
            return false;
        }
        this.j = j;
        return this.g();
    }
    
    private boolean a() {
        while (!this.g.isInited()) {}
        final SurfaceTexture requestSurfaceTexture = this.g.requestSurfaceTexture();
        final Surface surface = new Surface(requestSurfaceTexture);
        requestSurfaceTexture.setOnFrameAvailableListener((SurfaceTexture.OnFrameAvailableListener)new SurfaceTexture.OnFrameAvailableListener() {
            public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
                TuSdkMediaMutableFilePlayerImpl.this.i.onFrameAvailable();
            }
        });
        this.a(surface);
        if (this.l == null) {
            return false;
        }
        this.g.setInputRotation(this.l.inputTrack().orientation());
        this.g.setInputSize(this.l.inputTrack().naturalSize());
        this.g.setOutputSize(this.preferredOutputSize());
        this.b();
        return true;
    }
    
    private void a(final Surface outputSurface) {
        final ArrayList list = new ArrayList<Object>(1);
        final ArrayList list2 = new ArrayList<Object>(1);
        for (final AVAsset avAsset : this.a) {
            final List<AVAssetTrack> tracksWithMediaType = avAsset.tracksWithMediaType(AVMediaType.AVMediaTypeVideo);
            final List<AVAssetTrack> tracksWithMediaType2 = avAsset.tracksWithMediaType(AVMediaType.AVMediaTypeAudio);
            if (tracksWithMediaType.size() > 0 && tracksWithMediaType2.size() > 0) {
                list.addAll(tracksWithMediaType);
                list2.addAll(tracksWithMediaType2);
            }
        }
        if (list.size() == 0 || list2.size() == 0) {
            TLog.e("%s No tracks are available in the data source.", this);
            return;
        }
        this.l = new AVAssetTrackPipeMediaExtractor(list);
        (this.m = new AVAssetTrackCodecDecoder(this.l)).setOutputSurface(outputSurface);
        this.m.addTarget(this.e);
        this.n = new AVAssetTrackPipeMediaExtractor(list2);
        (this.o = new AVAssetTrackCodecDecoder(this.n)).addTarget(this.d);
        final TuSdkAudioInfo tuSdkAudioInfo = new TuSdkAudioInfo(this.n.inputTrack().mediaFormat());
        (this.r = new TuSdkAudioTrackImpl(tuSdkAudioInfo)).play();
        (this.t = new TuSdkAudioResampleHardImpl(tuSdkAudioInfo)).setMediaSync(this.d);
        (this.s = new TuSdkAudioPitchHardImpl(tuSdkAudioInfo)).changeSpeed(this.q.getSpeed());
        this.s.setMediaSync(this.d);
    }
    
    private boolean b() {
        if (this.k) {
            return false;
        }
        this.e.a(new Runnable() {
            @Override
            public void run() {
                TuSdkMediaMutableFilePlayerImpl.this.e.a();
            }
        });
        return true;
    }
    
    @TargetApi(14)
    @Override
    public void initInGLThread() {
        if (this.g == null) {
            TLog.w("%s initInGLThread need after load, before release.", "TuSdkMediaMutableFilePlayerImpl");
            return;
        }
        this.g.initInGLThread();
        this.e.a(new Runnable() {
            @Override
            public void run() {
                TuSdkMediaMutableFilePlayerImpl.this.a();
            }
        });
    }
    
    @Override
    public void newFrameReadyInGLThread() {
        if (this.g == null) {
            TLog.w("%s newFrameReadyInGLThread need after load, before release." + this.g, "TuSdkMediaMutableFilePlayerImpl");
            return;
        }
        this.g.updateSurfaceTexImage(this.g.getSurfaceTexTimestampNs());
    }
    
    @Override
    public void release() {
        if (this.b == TuSdkMediaPlayerStatus.Unknown) {
            TLog.w("%s already released.", "TuSdkMediaMutableFilePlayerImpl");
            return;
        }
        this.e.a(new Runnable() {
            @Override
            public void run() {
                if (TuSdkMediaMutableFilePlayerImpl.this.g != null) {
                    TuSdkMediaMutableFilePlayerImpl.this.g.destroy();
                    TuSdkMediaMutableFilePlayerImpl.this.g = null;
                }
                if (TuSdkMediaMutableFilePlayerImpl.this.h != null) {
                    TuSdkMediaMutableFilePlayerImpl.this.h.destroy();
                    TuSdkMediaMutableFilePlayerImpl.this.h = null;
                }
                TuSdkMediaMutableFilePlayerImpl.this.e.release();
            }
        });
        this.d.b(new Runnable() {
            @Override
            public void run() {
                TuSdkMediaMutableFilePlayerImpl.this.d.release();
            }
        });
        this.b = TuSdkMediaPlayerStatus.Unknown;
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
    
    private void a(final long n) {
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                if (TuSdkMediaMutableFilePlayerImpl.this.f != -1L && Math.abs(n - TuSdkMediaMutableFilePlayerImpl.this.f) > 500000L) {
                    return;
                }
                TuSdkMediaMutableFilePlayerImpl.this.f = -1L;
                TuSdkMediaMutableFilePlayerImpl.this.i.onProgress(TuSdkMediaMutableFilePlayerImpl.this.m.outputTimeUs(), null, TuSdkMediaMutableFilePlayerImpl.this.m.durationTimeUs());
            }
        });
    }
    
    private void c() {
        this.d();
        this.f();
    }
    
    private void a(final TuSdkMediaPlayerStatus b) {
        if (this.b == b) {
            return;
        }
        this.b = b;
        if (this.i == null) {
            return;
        }
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                TuSdkMediaMutableFilePlayerImpl.this.i.onStateChanged(b.ordinal());
            }
        });
    }
    
    @Override
    public long durationUs() {
        if (this.m == null) {
            return 0L;
        }
        return this.m.durationTimeUs();
    }
    
    @Override
    public long elapsedUs() {
        return this.m.outputTimeUs();
    }
    
    @Override
    public boolean isSupportPrecise() {
        return false;
    }
    
    @Override
    public boolean isPause() {
        return this.b != TuSdkMediaPlayerStatus.Playing;
    }
    
    @Override
    public void pause() {
        this.e.a(new Runnable() {
            @Override
            public void run() {
                TuSdkMediaMutableFilePlayerImpl.this.d();
            }
        });
    }
    
    private void d() {
        if (this.b != TuSdkMediaPlayerStatus.Playing) {
            return;
        }
        this.a(TuSdkMediaPlayerStatus.ReadyToPlay);
        this.q.stop();
        this.d.a();
    }
    
    @Override
    public void resume() {
        this.e();
    }
    
    private void e() {
        if ((this.b == TuSdkMediaPlayerStatus.Playing || this.f > 0L) && Math.abs(this.f - this.durationUs()) > 50000L) {
            return;
        }
        this.a(TuSdkMediaPlayerStatus.Playing);
        this.e.a(new Runnable() {
            @Override
            public void run() {
                TuSdkMediaMutableFilePlayerImpl.this.q.start();
                if (TuSdkMediaMutableFilePlayerImpl.this.m.renderOutputBuffer()) {
                    TuSdkMediaMutableFilePlayerImpl.this.d.a(new Runnable() {
                        @Override
                        public void run() {
                            TuSdkMediaMutableFilePlayerImpl.this.d.render();
                        }
                    });
                    TuSdkMediaMutableFilePlayerImpl.this.e.render();
                }
            }
        });
    }
    
    @Override
    public void reset() {
        this.f();
    }
    
    private void f() {
        if (this.m == null || this.b == TuSdkMediaPlayerStatus.Unknown) {
            return;
        }
        this.a(TuSdkMediaPlayerStatus.ReadyToPlay);
        this.f = -1L;
        this.d.a(new Runnable() {
            @Override
            public void run() {
                TuSdkMediaMutableFilePlayerImpl.this.d.reset();
            }
        });
        this.e.reset();
    }
    
    @Override
    public void setSpeed(final float f) {
        //Assert.assertEquals(String.format("Unsupported playback speed : %f", f), true, f > 0.0f && f <= 4.0f);
        this.e.b(new Runnable() {
            @Override
            public void run() {
                if (TuSdkMediaMutableFilePlayerImpl.this.speed() == f) {
                    return;
                }
                TuSdkMediaMutableFilePlayerImpl.this.q.setSpeed(f);
                if (TuSdkMediaMutableFilePlayerImpl.this.d != null) {
                    TuSdkMediaMutableFilePlayerImpl.this.d.a(f);
                }
            }
        });
    }
    
    @Override
    public float speed() {
        return this.q.getSpeed();
    }
    
    @Override
    public void setReverse(final boolean b) {
        TLog.e("%s \uff1a Sorry, reverse mode is not supported", this);
    }
    
    @Override
    public boolean isReverse() {
        return false;
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
    public void seekTo(final long f) {
        if (this.b == TuSdkMediaPlayerStatus.Unknown) {
            return;
        }
        this.f = f;
        this.e.c(new Runnable() {
            @Override
            public void run() {
                TuSdkMediaMutableFilePlayerImpl.this.d();
                TuSdkMediaMutableFilePlayerImpl.this.m.seekTo(f, true);
                TuSdkMediaMutableFilePlayerImpl.this.d.b(new Runnable() {
                    @Override
                    public void run() {
                        TuSdkMediaMutableFilePlayerImpl.this.d.b();
                        TuSdkMediaMutableFilePlayerImpl.this.o.seekTo(f, true);
                        TuSdkMediaMutableFilePlayerImpl.this.f = -1L;
                    }
                });
            }
        });
    }
    
    @TargetApi(14)
    private boolean g() {
        (this.g = new SelesSurfaceReceiver()).setTextureCoordinateBuilder(new SelesVerticeCoordinateCropBuilderImpl(false));
        this.g.addTarget(this.h, 0);
        return true;
    }
    
    private class AudioRender extends InternalRender implements AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput, TuSdkAudioPitchSync, TuSdkAudioResampleSync
    {
        private void a() {
            if (TuSdkMediaMutableFilePlayerImpl.this.r == null) {
                return;
            }
            TuSdkMediaMutableFilePlayerImpl.this.r.pause();
        }
        
        private void b() {
            if (TuSdkMediaMutableFilePlayerImpl.this.r != null) {
                TuSdkMediaMutableFilePlayerImpl.this.r.flush();
            }
            if (TuSdkMediaMutableFilePlayerImpl.this.t != null) {
                TuSdkMediaMutableFilePlayerImpl.this.t.reset();
            }
        }
        
        @Override
        public void render() {
            if (TuSdkMediaMutableFilePlayerImpl.this.o == null || TuSdkMediaMutableFilePlayerImpl.this.b != TuSdkMediaPlayerStatus.Playing) {
                TLog.i(" audio play paused Status \uff1a %s", TuSdkMediaMutableFilePlayerImpl.this.b);
                return;
            }
            if (TuSdkMediaMutableFilePlayerImpl.this.r != null) {
                TuSdkMediaMutableFilePlayerImpl.this.r.play();
            }
            if (TuSdkMediaMutableFilePlayerImpl.this.o.renderOutputBuffers()) {
                this.a(new Runnable() {
                    @Override
                    public void run() {
                        AudioRender.this.render();
                    }
                });
            }
        }
        
        @Override
        public void newFrameReady(final AVSampleBuffer avSampleBuffer) {
            if (avSampleBuffer.isDecodeOnly()) {
                return;
            }
            if (TuSdkMediaMutableFilePlayerImpl.this.b != TuSdkMediaPlayerStatus.Playing) {
                return;
            }
            TuSdkMediaMutableFilePlayerImpl.this.q.lock(avSampleBuffer.renderTimeUs(), 0L);
            avSampleBuffer.info().presentationTimeUs = avSampleBuffer.renderTimeUs();
            TuSdkMediaMutableFilePlayerImpl.this.t.queueInputBuffer(avSampleBuffer.buffer(), avSampleBuffer.info());
        }
        
        @Override
        public void outputFormatChaned(final MediaFormat mediaFormat, final AVAssetTrack avAssetTrack) {
            final TuSdkAudioInfo tuSdkAudioInfo = new TuSdkAudioInfo(mediaFormat);
            TuSdkMediaMutableFilePlayerImpl.this.t.reset();
            TuSdkMediaMutableFilePlayerImpl.this.t.changeFormat(tuSdkAudioInfo);
        }
        
        private void a(final float n) {
            this.b(new Runnable() {
                @Override
                public void run() {
                    if (TuSdkMediaMutableFilePlayerImpl.this.r != null) {
                        TuSdkMediaMutableFilePlayerImpl.this.r.flush();
                    }
                    if (TuSdkMediaMutableFilePlayerImpl.this.s != null) {
                        TuSdkMediaMutableFilePlayerImpl.this.s.reset();
                        TuSdkMediaMutableFilePlayerImpl.this.s.changeSpeed(n);
                    }
                }
            });
        }
        
        @Override
        public void syncAudioResampleOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            TuSdkMediaMutableFilePlayerImpl.this.s.queueInputBuffer(byteBuffer, bufferInfo);
        }
        
        @Override
        public void syncAudioPitchOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            byteBuffer.position(bufferInfo.offset);
            byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
            TuSdkMediaMutableFilePlayerImpl.this.r.write(byteBuffer);
        }
        
        @Override
        public void reset() {
            if (TuSdkMediaMutableFilePlayerImpl.this.o == null || TuSdkMediaMutableFilePlayerImpl.this.b == TuSdkMediaPlayerStatus.Unknown) {
                return;
            }
            TuSdkMediaMutableFilePlayerImpl.this.o.reset();
        }
    }
    
    private abstract class InternalRender
    {
        AVMediaProcessQueue b;
        
        private InternalRender() {
            this.b = new AVMediaProcessQueue();
        }
        
        void a(final Runnable runnable) {
            this.b.runAsynchronouslyOnProcessingQueue(runnable);
        }
        
        void b(final Runnable runnable) {
            this.b.runSynchronouslyOnProcessingQueue(runnable);
        }
        
        void c(final Runnable runnable) {
            this.b.clearAll();
            this.b.runAsynchronouslyOnProcessingQueue(runnable);
        }
        
        public abstract void render();
        
        public abstract void reset();
        
        public void release() {
            this.b.quit();
            this.reset();
        }
    }
    
    public enum TuSdkMediaPlayerStatus
    {
        Unknown, 
        Failed, 
        ReadyToPlay, 
        Playing, 
        Completed;
    }
    
    private class VideoRender extends InternalRender implements AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput
    {
        @Override
        public void newFrameReady(final AVSampleBuffer avSampleBuffer) {
            if (avSampleBuffer.isDecodeOnly()) {
                return;
            }
            TuSdkMediaMutableFilePlayerImpl.this.k = true;
            TuSdkMediaMutableFilePlayerImpl.this.q.lock(avSampleBuffer.renderTimeUs(), 0L);
            TuSdkMediaMutableFilePlayerImpl.this.a(avSampleBuffer.renderTimeUs());
        }
        
        @Override
        public void outputFormatChaned(final MediaFormat corp, final AVAssetTrack avAssetTrack) {
            TuSdkMediaMutableFilePlayerImpl.this.p = new TuSdkVideoInfo(corp);
            TuSdkMediaMutableFilePlayerImpl.this.p.setCorp(corp);
            TuSdkMediaMutableFilePlayerImpl.this.g.setInputSize(TuSdkMediaMutableFilePlayerImpl.this.p.codecSize);
            TuSdkMediaMutableFilePlayerImpl.this.g.setPreCropRect(TuSdkMediaMutableFilePlayerImpl.this.p.codecCrop);
            TuSdkMediaMutableFilePlayerImpl.this.g.setInputRotation(avAssetTrack.orientation());
        }
        
        @Override
        public void render() {
            if (TuSdkMediaMutableFilePlayerImpl.this.m == null || TuSdkMediaMutableFilePlayerImpl.this.b != TuSdkMediaPlayerStatus.Playing) {
                TLog.i("%s : play paused", this);
                return;
            }
            if (TuSdkMediaMutableFilePlayerImpl.this.m.renderOutputBuffers()) {
                this.a(new Runnable() {
                    @Override
                    public void run() {
                        VideoRender.this.render();
                    }
                });
            }
            else {
                TLog.i("%s : play done", this);
                TuSdkMediaMutableFilePlayerImpl.this.f();
            }
        }
        
        private void a() {
            if (TuSdkMediaMutableFilePlayerImpl.this.m == null || TuSdkMediaMutableFilePlayerImpl.this.b == TuSdkMediaPlayerStatus.Playing) {
                return;
            }
            if (TuSdkMediaMutableFilePlayerImpl.this.k) {
                TuSdkMediaMutableFilePlayerImpl.this.a(TuSdkMediaPlayerStatus.ReadyToPlay);
                if (!TuSdkMediaMutableFilePlayerImpl.this.j) {
                    TuSdkMediaMutableFilePlayerImpl.this.resume();
                }
                return;
            }
            if (TuSdkMediaMutableFilePlayerImpl.this.m.renderOutputBuffer()) {
                this.a(new Runnable() {
                    @Override
                    public void run() {
                        VideoRender.this.a();
                    }
                });
            }
            else {
                TuSdkMediaMutableFilePlayerImpl.this.c();
            }
        }
        
        @Override
        public void reset() {
            TuSdkMediaMutableFilePlayerImpl.this.q.stop();
            TuSdkMediaMutableFilePlayerImpl.this.m.reset();
        }
    }
}
