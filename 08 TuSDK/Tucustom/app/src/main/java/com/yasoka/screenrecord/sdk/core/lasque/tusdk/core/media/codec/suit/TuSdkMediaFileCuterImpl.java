// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit;

//import org.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileEncoder;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaDecodecSync;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaEncodecSync;
import android.graphics.SurfaceTexture;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorSync;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import java.util.List;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import android.graphics.Color;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.RectF;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
//import org.lasque.tusdk.core.media.codec.exception.TuSdkTaskExitException;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaCodec;
//import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
//import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
//import org.lasque.tusdk.core.media.codec.encoder.TuSdkVideoSurfaceEncoderListenerImpl;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileCuterSync;
//import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncoderListener;
//import org.lasque.tusdk.core.media.codec.decoder.TuSdkDecoderListener;
//import org.lasque.tusdk.core.media.codec.encoder.TuSdkVideoSurfaceEncoderListener;
//import org.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFileDecoder;
//import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileSync;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder.TuSdkDecoderListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFileDecoder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkEncoderListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileSuitEncoderBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkVideoSurfaceEncoderListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkVideoSurfaceEncoderListenerImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.exception.TuSdkTaskExitException;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileCuterSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileSuitEncoderBase;

@TargetApi(18)
public class TuSdkMediaFileCuterImpl extends TuSdkMediaFileSuitEncoderBase implements TuSdkMediaFileCuter
{
    private final TuSdkMediaFileSync a;
    private final SelesVerticeCoordinateCorpBuilder b;
    private TuSdkMediaDataSource c;
    private float d;
    private float e;
    private float f;
    private float g;
    private SelesSurfaceReceiver h;
    private TuSdkMediaFileDecoder i;
    private TuSdkVideoSurfaceEncoderListener j;
    private TuSdkDecoderListener k;
    private TuSdkDecoderListener l;
    private TuSdkEncoderListener m;
    
    public TuSdkMediaFileCuterImpl() {
        this(new TuSdkMediaFileCuterSync());
    }
    
    public TuSdkMediaFileCuterImpl(final TuSdkMediaFileSync a) {
        this.b = new SelesVerticeCoordinateCropBuilderImpl(false);
        this.d = 0.0f;
        this.e = 0.0f;
        this.f = 0.0f;
        this.g = 1.0f;
        this.j = new TuSdkVideoSurfaceEncoderListenerImpl() {
            @Override
            public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
                if (TuSdkMediaFileCuterImpl.this.h == null) {
                    return;
                }
                TuSdkMediaFileCuterImpl.this.h.initInGLThread();
            }
            
            @Override
            public void onEncoderDrawFrame(final long n, final boolean b) {
                TuSdkMediaFileCuterImpl.this.a.syncVideoEncodecDrawFrame(n, b, TuSdkMediaFileCuterImpl.this.h, TuSdkMediaFileCuterImpl.this.mEncoder.getVideoEncoder());
            }
            
            @Override
            public void onEncoderUpdated(final MediaCodec.BufferInfo bufferInfo) {
                TuSdkMediaFileCuterImpl.this.a(false);
            }
            
            @Override
            public void onEncoderCompleted(final Exception ex) {
                if (ex == null) {
                    TLog.d("%s encodec Video updatedToEOS", "TuSdkMediaFileCuterImpl");
                    TuSdkMediaFileCuterImpl.this.a(true);
                }
                else {
                    TLog.e(ex, "%s VideoEncoderListener thread catch exception, The thread will exit.", "TuSdkMediaFileCuterImpl");
                }
                TuSdkMediaFileCuterImpl.this.a(ex);
            }
        };
        this.k = new TuSdkDecoderListener() {
            @Override
            public void onDecoderUpdated(final MediaCodec.BufferInfo bufferInfo) {
            }
            
            @Override
            public void onDecoderCompleted(final Exception ex) {
                if (ex != null) {
                    TuSdkMediaFileCuterImpl.this.a(ex);
                    return;
                }
                TLog.d("%s VideoDecoderListenerprocess buffer stream end", "TuSdkMediaFileCuterImpl");
                TuSdkMediaFileCuterImpl.this.h();
                TuSdkMediaFileCuterImpl.this.c();
            }
        };
        this.l = new TuSdkDecoderListener() {
            @Override
            public void onDecoderUpdated(final MediaCodec.BufferInfo bufferInfo) {
            }
            
            @Override
            public void onDecoderCompleted(final Exception ex) {
                if (ex != null && ex instanceof TuSdkTaskExitException) {
                    TuSdkMediaFileCuterImpl.this.a(ex);
                    return;
                }
                if (ex != null) {
                    TLog.e(ex, "%s AudioDecoderListener catch a exception, skip audio and ignore.", "TuSdkMediaFileCuterImpl");
                }
                if (!TuSdkMediaFileCuterImpl.this.a.isAudioDecodeCrashed()) {
                    TLog.d("%s AudioDecoderListener process buffer stream end", "TuSdkMediaFileCuterImpl");
                }
                TuSdkMediaFileCuterImpl.this.e();
            }
        };
        this.m = new TuSdkEncoderListener() {
            @Override
            public void onEncoderUpdated(final MediaCodec.BufferInfo bufferInfo) {
                if (TLog.LOG_AUDIO_ENCODEC_INFO) {
                    TuSdkCodecCapabilities.logBufferInfo("AudioEncoderListener updated", bufferInfo);
                }
            }
            
            @Override
            public void onEncoderCompleted(final Exception ex) {
                if (ex == null) {
                    TLog.d("%s encodec Audio updatedToEOS", "TuSdkMediaFileCuterImpl");
                }
                else {
                    TLog.e(ex, "%s AudioEncoderListener thread catch exception, The thread will exit.", "TuSdkMediaFileCuterImpl");
                }
                TuSdkMediaFileCuterImpl.this.a(ex);
            }
        };
        this.a = a;
    }
    
    @Override
    public void setMediaDataSource(final TuSdkMediaDataSource c) {
        this.c = c;
    }

    @Override
    public void setSurfaceRender(TuSdkSurfaceRender p0) {

    }

    @Override
    public void setCropRect(final RectF cropRect) {
        if (cropRect == null) {
            return;
        }
        this.b.setCropRect(cropRect);
    }
    
    @Override
    public void setEnableClip(final boolean enableClip) {
        this.b.setEnableClip(enableClip);
    }
    
    @Override
    public void setOutputRatio(final float outputRatio) {
        this.b.setOutputRatio(outputRatio);
    }
    
    @Override
    public void setOutputSize(final TuSdkSize outputSize) {
        if (!outputSize.isSize()) {
            return;
        }
        this.b.setOutputSize(outputSize);
    }
    
    @Override
    public void setCanvasColor(final int n) {
        this.setCanvasColor(Color.red(n) / 255.0f, Color.green(n) / 255.0f, Color.blue(n) / 255.0f, Color.alpha(n) / 255.0f);
    }
    
    @Override
    public void setCanvasColor(final float d, final float e, final float f, final float g) {
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
    }
    
    @Override
    public void setTimeline(final TuSdkMediaTimeline timeline) {
        if (this.mState != -1) {
            TLog.w("%s setTimeline need before run.", "TuSdkMediaFileCuterImpl");
            return;
        }
        this.a.setTimeline(timeline);
    }
    
    @Override
    public void setTimeSlices(final List<TuSdkMediaTimeSlice> list) {
        if (this.mState != -1) {
            TLog.w("%s setTimeSlices need before run.", "TuSdkMediaFileCuterImpl");
            return;
        }
        this.a.setTimeline(new TuSdkMediaTimeline(list));
    }
    
    @Override
    public void setTimeSlice(final TuSdkMediaTimeSlice tuSdkMediaTimeSlice) {
        if (this.mState != -1) {
            TLog.w("%s setTimeSlice need before run.", "TuSdkMediaFileCuterImpl");
            return;
        }
        this.a.setTimeline(new TuSdkMediaTimeline(tuSdkMediaTimeSlice));
    }
    
    @Override
    public void setTimeSlice(final long n, final long n2) {
        this.setTimeSlice(new TuSdkMediaTimeSlice(n, n2));
    }
    
    @Override
    public void setTimeSliceDuration(final long n, final long n2) {
        this.setTimeSlice(n, n + n2);
    }
    
    @Override
    public void setTimeSliceScaling(final float n, final float n2) {
        if (this.mState != -1) {
            TLog.w("%s setTimeSlices need before run.", "TuSdkMediaFileCuterImpl");
            return;
        }
        this.a.setTimeline(new TuSdkMediaTimeline(n, n2));
    }
    
    @Override
    public void setTimeSliceDurationScaling(final float n, final float n2) {
        this.setTimeSliceScaling(n, n + n2);
    }
    
    @Override
    public void setAudioRender(final TuSdkAudioRender tuSdkAudioRender) {
        super.setAudioRender(tuSdkAudioRender);
        if (this.i != null) {
            this.i.setAudioRender(tuSdkAudioRender);
        }
    }
    
    @Override
    public void setAudioMixerRender(final TuSdkAudioRender tuSdkAudioRender) {
        if (this.a == null) {
            return;
        }
        if (this.a instanceof TuSdkMediaFileDirectorSync) {
            ((TuSdkMediaFileDirectorSync)this.a).setAudioMixerRender(tuSdkAudioRender);
        }
        else if (this.a instanceof TuSdkMediaFileCuterSync) {
            ((TuSdkMediaFileCuterSync)this.a).setAudioMixerRender(tuSdkAudioRender);
        }
    }
    
    @Override
    public boolean run(final TuSdkMediaProgress tuSdkMediaProgress) {
        if (this.c == null || !this.c.isValid()) {
            TLog.w("%s run need a input file path.", "TuSdkMediaFileCuterImpl");
            return false;
        }
        return super.run(tuSdkMediaProgress);
    }
    
    @Override
    public void stop() {
        if (this.mState == 1) {
            TLog.w("%s already stoped.", "TuSdkMediaFileCuterImpl");
            return;
        }
        this.mState = 1;
        this.d();
        this.f();
        if (this.h != null) {
            this.h.destroy();
            this.h = null;
        }
        this.mEncoder.release();
        this.a.release();
    }

    @Override
    public void setOutputOrientation(ImageOrientation p0) {

    }

    private void a(final boolean b) {
        ThreadHelper.post(new Runnable() {
            final /* synthetic */ float a = b ? 1.0f : TuSdkMediaFileCuterImpl.this.a.calculateProgress();
            
            @Override
            public void run() {
                if (TuSdkMediaFileCuterImpl.this.mProgress == null) {
                    return;
                }
                TuSdkMediaFileCuterImpl.this.mProgress.onProgress(this.a, TuSdkMediaFileCuterImpl.this.c, 1, 1);
            }
        });
    }
    
    private void a(final Exception ex) {
        if (ex == null) {
            if (!this.a.isEncodecCompleted()) {
                return;
            }
            this.mEncoder.cleanTemp();
        }
        this.a.setBenchmarkEnd();
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                TuSdkMediaFileCuterImpl.this.stop();
                if (TuSdkMediaFileCuterImpl.this.mProgress == null) {
                    return;
                }
                TuSdkMediaFileCuterImpl.this.mProgress.onCompleted(ex, TuSdkMediaFileCuterImpl.this.mEncoder.getOutputDataSource(), 1);
            }
        });
        TLog.d("%s runCompleted: %f / %f", "TuSdkMediaFileCuterImpl", this.a.benchmarkUs() / 1000000.0f, this.a.totalDurationUs() / 1000000.0f);
    }
    
    @Override
    protected boolean _init() {
        if (!this.a()) {
            TLog.w("%s init Encodec Environment failed.", "TuSdkMediaFileCuterImpl");
            return false;
        }
        this.b();
        return true;
    }
    
    private boolean a() {
        this.b.setOutputSize(this.mEncoder.getOutputSize());
        (this.h = new SelesSurfaceReceiver()).setTextureCoordinateBuilder(this.b);
        this.h.addTarget(this.mEncoder.getFilterBridge(), 0);
        this.h.setCanvasColor(this.d, this.e, this.f, this.g);
        this.h.setSurfaceTextureListener((SurfaceTexture.OnFrameAvailableListener)new SurfaceTexture.OnFrameAvailableListener() {
            public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
                TuSdkMediaFileCuterImpl.this.mEncoder.requestVideoRender(TuSdkMediaFileCuterImpl.this.a.lastVideoDecodecTimestampNs());
            }
        });
        this.a.addAudioEncodecOperation(this.mEncoder.getAudioOperation());
        this.mEncoder.setSurfaceRender(this.mSurfaceRender);
        this.mEncoder.setAudioRender(this.mAudioRender);
        this.mEncoder.setMediaSync(this.a);
        this.mEncoder.setListener(this.j, this.m);
        return this.mEncoder.prepare(null);
    }
    
    private void b() {
        this.mEncoder.requestVideoKeyFrame();
        (this.i = new TuSdkMediaFileDecoder(true, this.mEncoder.hasAudioEncoder())).setMediaDataSource(this.c);
        this.i.setMediaSync(this.a);
        this.i.setSurfaceReceiver(this.h);
        this.i.setAudioRender(this.mAudioRender);
        this.i.setListener(this.k, this.l);
        this.i.prepare();
        if (!this.i.isVideoStared()) {
            this.c();
            return;
        }
        if (this.mEncoder.hasAudioEncoder() && !this.i.isAudioStared()) {
            this.e();
        }
    }
    
    private void c() {
        if (this.a.isVideoDecodeCompleted()) {
            return;
        }
        this.mEncoder.signalVideoEndOfInputStream();
        this.d();
        this.a.syncVideoDecodeCompleted();
    }
    
    private void d() {
        if (this.i == null) {
            return;
        }
        this.i.releaseVideoDecoder();
    }
    
    private void e() {
        if (this.a.isAudioDecodeCompleted()) {
            return;
        }
        if (!this.a.isAudioDecodeCrashed()) {
            this.g();
        }
        this.f();
        this.a.syncAudioDecodeCompleted();
    }
    
    private void f() {
        if (this.i == null) {
            return;
        }
        this.i.releaseAudioDecoder();
    }
    
    private void g() {
        this.mEncoder.signalAudioEndOfInputStream(this.a.totalDurationUs());
    }
    
    private void h() {
        if (!this.a.isAudioDecodeCrashed()) {
            return;
        }
        this.mEncoder.autoFillAudioMuteData(0L, this.a.totalDurationUs(), true);
    }
    
    public TuSdkMediaTimeline getTimeLine() {
        if (this.a instanceof TuSdkMediaFileDirectorSync) {
            return ((TuSdkMediaFileDirectorSync)this.a).getTimeLine();
        }
        return null;
    }
}
