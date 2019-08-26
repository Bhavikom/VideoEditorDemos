// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.video.editor.TuSdkMediaRepeatTimeEffect;
//import org.lasque.tusdk.video.editor.TuSdkMediaSlowTimeEffect;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.utils.TLog;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateFillModeBuilder;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorPlayerSync;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
import android.opengl.GLES20;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.Iterator;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.video.editor.TuSdkMediaReversalTimeEffect;
import android.content.Context;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaDirectorPlayerListener;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.video.editor.TuSdkMediaTimeEffect;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import java.util.ArrayList;
import java.util.List;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
import android.opengl.GLSurfaceView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaDirectorPlayerListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileDirectorPlayerImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorPlayerSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateFillModeBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaRepeatTimeEffect;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaReversalTimeEffect;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaSlowTimeEffect;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaTimeEffect;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
//import org.lasque.tusdk.core.seles.output.SelesView;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileDirectorPlayerImpl;

public class TuSdkEditorPlayerImpl implements TuSdkEditorPlayer
{
    private TuSdkMediaFileDirectorPlayerImpl a;
    private TuSdkMediaDataSource tuSdkMediaDataSource;
    private TuSdkEditorEffectorImpl c;
    private TuSdkEditorAudioMixerImpl d;
    private SelesView e;
    private boolean f;
    private int g;
    private boolean h;
    private TuSdkMediaFileCuterTimeline i;
    private GLSurfaceView.Renderer j;
    private TuSdkSurfaceDraw k;
    private List<TuSdkProgressListener> l;
    private List<TuSdkPreviewSizeChangeListener> m;
    private ArrayList<TuSdkMediaTimeSlice> n;
    private TuSdkMediaTimeEffect o;
    private long p;
    private boolean q;
    private float r;
    private TuSdkSize s;
    private float t;
    private float u;
    private float v;
    private float w;
    private TuSdkMediaDirectorPlayerListener x;
    private GLSurfaceView.Renderer y;
    private TuSdkSurfaceDraw z;
    private List<TuSdkMediaTimeSliceEntity> A;
    
    public TuSdkEditorPlayerImpl(final Context context) {
        this.f = false;
        this.g = 0;
        this.h = false;
        this.l = new ArrayList<TuSdkProgressListener>();
        this.m = new ArrayList<TuSdkPreviewSizeChangeListener>();
        this.p = 0L;
        this.q = true;
        this.t = 0.0f;
        this.u = 0.0f;
        this.v = 0.0f;
        this.w = 1.0f;
        this.x = (TuSdkMediaDirectorPlayerListener)new TuSdkMediaDirectorPlayerListener() {
            public void onProgress(final long n, final long n2, final TuSdkMediaDataSource tuSdkMediaDataSource, final TuSdkMediaFileCuterTimeline tuSdkMediaFileCuterTimeline) {
                long calcOutputTimeUs;
                long inputTotalTimeUs;
                float n3;
                if (TuSdkEditorPlayerImpl.this.g == 1 && TuSdkEditorPlayerImpl.this.o != null) {
                    TuSdkEditorPlayerImpl.this.p = (calcOutputTimeUs = TuSdkEditorPlayerImpl.this.o.calcOutputTimeUs(n, tuSdkMediaFileCuterTimeline.sliceWithOutputTimeUs(n), tuSdkMediaFileCuterTimeline.getFinalSlices()));
                    inputTotalTimeUs = TuSdkEditorPlayerImpl.this.o.getInputTotalTimeUs();
                    n3 = calcOutputTimeUs / (float)inputTotalTimeUs;
                    if (TuSdkEditorPlayerImpl.this.o instanceof TuSdkMediaReversalTimeEffect && n == n2) {
                        n3 = 0.0f;
                    }
                }
                else {
                    calcOutputTimeUs = n;
                    inputTotalTimeUs = n2;
                    n3 = n / (float)n2;
                }
                if (n3 >= 1.0f) {
                    n3 = 1.0f;
                    StatisticsManger.appendComponent(9445378L);
                }
                for (final TuSdkProgressListener tuSdkProgressListener : TuSdkEditorPlayerImpl.this.l) {
                    if (tuSdkProgressListener == null) {
                        continue;
                    }
                    tuSdkProgressListener.onProgress(calcOutputTimeUs, inputTotalTimeUs, n3);
                }
                if (n >= n2 && TuSdkEditorPlayerImpl.this.d != null) {
                    TuSdkEditorPlayerImpl.this.d.getMixerAudioRender().seekTo(0L);
                }
            }
            
            public void onStateChanged(final int n) {
                for (final TuSdkProgressListener tuSdkProgressListener : TuSdkEditorPlayerImpl.this.l) {
                    if (tuSdkProgressListener == null) {
                        continue;
                    }
                    tuSdkProgressListener.onStateChanged(n);
                }
            }
            
            public void onFrameAvailable() {
                if (TuSdkEditorPlayerImpl.this.e != null) {
                    TuSdkEditorPlayerImpl.this.e.requestRender();
                }
            }
            
            public void onProgress(final long n, final TuSdkMediaDataSource tuSdkMediaDataSource, final long n2) {
            }
            
            public void onCompleted(final Exception ex, final TuSdkMediaDataSource tuSdkMediaDataSource) {
            }
        };
        this.y = (GLSurfaceView.Renderer)new GLSurfaceView.Renderer() {
            public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
                GLES20.glDisable(2929);
                TuSdkEditorPlayerImpl.this.a.initInGLThread();
                if (TuSdkEditorPlayerImpl.this.c != null) {
                    TuSdkEditorPlayerImpl.this.c.onSurfaceCreated();
                }
            }
            
            public void onSurfaceChanged(final GL10 gl10, final int n, final int n2) {
                GLES20.glViewport(0, 0, n, n2);
                if (TuSdkEditorPlayerImpl.this.c != null) {
                    TuSdkEditorPlayerImpl.this.c.onSurfaceChanged(n, n2);
                }
            }
            
            public void onDrawFrame(final GL10 gl10) {
                GLES20.glClear(16640);
                if (TuSdkEditorPlayerImpl.this.a != null) {
                    TuSdkEditorPlayerImpl.this.a.newFrameReadyInGLThread();
                }
            }
        };
        this.z = (TuSdkSurfaceDraw)new TuSdkSurfaceDraw() {
            public int onDrawFrame(final int n, final int n2, final int n3, final long n4) {
                if (TuSdkEditorPlayerImpl.this.c == null) {
                    return n;
                }
                return TuSdkEditorPlayerImpl.this.c.processFrame(n, n2, n3, n4);
            }
            
            public void onDrawFrameCompleted() {
            }
        };
        (this.a = new TuSdkMediaFileDirectorPlayerImpl()).setListener((TuSdkMediaPlayerListener)this.x);
        this.a.setEffectFrameCalc((TuSdkMediaFileDirectorPlayerSync.TuSdkEffectFrameCalc)new TuSdkMediaFileDirectorPlayerSync.TuSdkEffectFrameCalc() {
            public long calcEffectFrameUs(final long n, final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity) {
                final TuSdkMediaTimeSliceEntity sliceWithOutputTimeUs = TuSdkEditorPlayerImpl.this.a.getTimeLine().sliceWithOutputTimeUs(n);
                if (TuSdkEditorPlayerImpl.this.o != null) {
                    final long calcOutputTimeUs = TuSdkEditorPlayerImpl.this.o.calcOutputTimeUs(n, sliceWithOutputTimeUs, TuSdkEditorPlayerImpl.this.a.getTimeLine().getFinalSlices());
                    TuSdkEditorPlayerImpl.this.p = calcOutputTimeUs;
                    return calcOutputTimeUs;
                }
                if (n > TuSdkEditorPlayerImpl.this.getTotalTimeUs()) {
                    return TuSdkEditorPlayerImpl.this.getTotalTimeUs();
                }
                return n;
            }
        });
        this.a(context);
    }
    
    private void a(final Context context) {
        (this.e = new SelesView(context)).setFillMode(SelesView.SelesFillModeType.PreserveAspectRatio);
        this.e.setOnDisplayChangeListener((SelesVerticeCoordinateFillModeBuilder.OnDisplaySizeChangeListener)new SelesVerticeCoordinateFillModeBuilder.OnDisplaySizeChangeListener() {
            public void onDisplaySizeChanged(final TuSdkSize tuSdkSize) {
                final Iterator<TuSdkPreviewSizeChangeListener> iterator = TuSdkEditorPlayerImpl.this.m.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onPreviewSizeChanged(tuSdkSize);
                }
            }
        });
    }
    
    @Override
    public void setPreviewContainer(final ViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }
        viewGroup.addView((View)this.e, 0, new ViewGroup.LayoutParams(-1, -1));
    }
    
    @Override
    public void setDataSource(final TuSdkMediaDataSource b) {
        this.tuSdkMediaDataSource = b;
    }
    
    @Override
    public void setEditTimeSlice(final ArrayList<TuSdkMediaTimeSlice> n) {
        this.n = n;
    }
    
    @Override
    public void enableFirstFramePause(final boolean f) {
        this.f = f;
    }
    
    @Override
    public void setCanvasColor(final float t, final float u, final float v, final float w) {
        this.t = t;
        this.u = u;
        this.v = v;
        this.w = w;
    }
    
    @Override
    public void setCanvasColor(final int n) {
        this.setCanvasColor(Color.red(n) / 255.0f, Color.green(n) / 255.0f, Color.blue(n) / 255.0f, Color.alpha(n) / 255.0f);
    }
    
    public float getCanvasColorRed() {
        return this.t;
    }
    
    public float getCanvasColorGreen() {
        return this.u;
    }
    
    public float getCanvasColorBlue() {
        return this.v;
    }
    
    public float getCanvasColorAlpha() {
        return this.w;
    }
    
    @Override
    public void setBackGround(final int backgroundColor) {
        if (this.e == null) {
            return;
        }
        this.e.setBackgroundColor(backgroundColor);
    }
    
    protected void initInGLThread() {
        if (this.a == null) {
            return;
        }
        this.a.initInGLThread();
    }
    
    protected void newFrameReadyInGLThread() {
        if (this.a == null) {
            return;
        }
        this.a.newFrameReadyInGLThread();
    }
    
    protected boolean loadVideo() {
        if (this.tuSdkMediaDataSource == null || !this.tuSdkMediaDataSource.isValid()) {
            TLog.e("%s data source is invalid !!!", new Object[] { "TuSdkEditorPlayer" });
            return false;
        }
        this.a.setMediaDataSource(this.tuSdkMediaDataSource);
        this.e.setRenderer((this.j == null) ? this.y : this.j);
        this.a.setSurfaceDraw((this.k == null) ? this.z : this.k);
        final boolean load = this.a.load(this.f);
        if (load || this.e != null) {
            this.a.getFilterBridge().addTarget((SelesContext.SelesInput)this.e, 0);
        }
        if (this.n != null) {
            this.i = new TuSdkMediaFileCuterTimeline((List)this.n);
            this.a.preview((TuSdkMediaTimeline)this.i);
        }
        this.a.setCanvasColor(this.t, this.u, this.v, this.w);
        return load;
    }
    
    @Override
    public void startPreview() {
        if (this.a != null) {
            this.a.resume();
        }
        StatisticsManger.appendComponent(9445377L);
    }
    
    protected TuSdkMediaTimeline getTimelineEffect() {
        return (TuSdkMediaTimeline)this.a.getTimeLine();
    }
    
    protected TuSdkMediaTimeEffect getTimeEffect() {
        return this.o;
    }
    
    @Override
    public boolean isReversing() {
        return this.h;
    }
    
    @Override
    public void pausePreview() {
        if (this.a == null) {
            return;
        }
        this.a.pause();
    }
    
    @Override
    public boolean isPause() {
        return this.a != null && this.a.isPause();
    }
    
    @Override
    public void seekTimeUs(final long n) {
        if (this.g == 0) {
            this.seekOutputTimeUs(n);
        }
        else {
            this.seekInputTimeUs(n);
        }
    }
    
    @Override
    public void seekOutputTimeUs(final long n) {
        if (!this.isPause()) {
            this.pausePreview();
        }
        if (this.a == null) {
            return;
        }
        this.a.seekTo(n);
        if (this.d != null) {
            this.d.getMixerAudioRender().seekTo(n);
        }
    }
    
    @Override
    public void seekInputTimeUs(long n) {
        if (!this.isPause()) {
            this.pausePreview();
        }
        n = ((n > this.getOutputTotalTimeUS()) ? this.getOutputTotalTimeUS() : n);
        if (this.a.getTimeLine() == null) {
            this.seekOutputTimeUs(n);
            return;
        }
        final TuSdkMediaTimeSliceEntity sliceWithInputTimeUs = this.a.getTimeLine().sliceWithInputTimeUs(n);
        if (sliceWithInputTimeUs == null) {
            this.seekOutputTimeUs(n);
            return;
        }
        long n2;
        if (this.o instanceof TuSdkMediaSlowTimeEffect) {
            n2 = this.a.getTimeLine().sliceWithCalcModeOutputTimeUs(n);
        }
        else if (this.o instanceof TuSdkMediaRepeatTimeEffect) {
            n2 = sliceWithInputTimeUs.calOutputHaveRepetTimeUs(n, this.a.getTimeLine());
        }
        else if (this.o instanceof TuSdkMediaReversalTimeEffect) {
            sliceWithInputTimeUs.calOutputTimeUs(n);
            n2 = sliceWithInputTimeUs.calOutputTimeUs(n);
        }
        else if (this.n != null) {
            n2 = sliceWithInputTimeUs.calRealTimeOutputTimeUs(n);
        }
        else {
            n2 = sliceWithInputTimeUs.calOutputTimeUs(n);
        }
        this.seekOutputTimeUs(n2);
    }
    
    @Override
    public long getCurrentTimeUs() {
        return (this.g == 0) ? this.getCurrentOutputTimeUs() : this.getCurrentInputTimeUs();
    }
    
    @Override
    public long getTotalTimeUs() {
        return (this.g == 0) ? this.getOutputTotalTimeUS() : this.getInputTotalTimeUs();
    }
    
    @Override
    public long getCurrentInputTimeUs() {
        if (this.a == null) {
            return -1L;
        }
        if (this.o != null) {
            return this.p;
        }
        return this.a.decodeFrameTimeUs();
    }
    
    @Override
    public long getCurrentOutputTimeUs() {
        return this.a.outputTimeUs();
    }
    
    @Override
    public long getOutputTotalTimeUS() {
        if (this.a == null) {
            return -1L;
        }
        return this.a.durationUs();
    }
    
    @Override
    public long getInputTotalTimeUs() {
        if (this.a == null) {
            return -1L;
        }
        if (this.o != null && this.o instanceof TuSdkMediaSlowTimeEffect) {
            return this.o.getInputTotalTimeUs();
        }
        return this.a.inputDurationUs();
    }
    
    private void a(final boolean b) {
        if (this.a == null) {
            return;
        }
        this.q = b;
        this.a.setEnableClip(b);
    }
    
    public boolean isEnableClip() {
        return this.q;
    }
    
    public TuSdkSize getOutputSize() {
        return this.s;
    }
    
    @Override
    public void setOutputRatio(final float n, final boolean b) {
        if (this.a == null) {
            return;
        }
        this.r = n;
        this.a(b);
        this.s = this.a.setOutputRatio(n);
    }
    
    public float getOutputRatio() {
        return this.r;
    }
    
    @Override
    public void setOutputSize(final TuSdkSize tuSdkSize, final boolean b) {
        if (this.a == null) {
            return;
        }
        this.a(b);
        this.s = tuSdkSize;
        this.a.setOutputSize(tuSdkSize);
    }
    
    @Override
    public void setVideoSoundVolume(final float volume) {
        if (this.a == null) {
            return;
        }
        this.a.setVolume(volume);
    }
    
    protected void setRender(final GLSurfaceView.Renderer j) {
        if (j == null) {
            TLog.e("%s renderer is null. ", new Object[] { "TuSdkEditorPlayer" });
            return;
        }
        this.j = j;
    }
    
    protected void setSurfaceDraw(final TuSdkSurfaceDraw k) {
        if (k == null) {
            TLog.e("%s surfaceDraw is null. ", new Object[] { "TuSdkEditorPlayer" });
            return;
        }
        this.k = k;
    }
    
    protected void setAudioRender(final TuSdkAudioRender audioRender) {
        if (audioRender == null) {
            TLog.e("%s audioRender is null ", new Object[] { "TuSdkEditorPlayer" });
            return;
        }
        if (this.a == null) {
            return;
        }
        this.a.setAudioRender(audioRender);
    }
    
    protected void setAudioMixerRender(final TuSdkAudioRender audioMixerRender) {
        if (audioMixerRender == null) {
            TLog.e("%s audioMixerRender is null ", new Object[] { "TuSdkEditorPlayer" });
            return;
        }
        if (this.a == null) {
            return;
        }
        this.a.setAudioMixerRender(audioMixerRender);
    }
    
    protected void setEffector(final TuSdkEditorEffectorImpl c) {
        this.c = c;
    }
    
    protected void setAudioMixer(final TuSdkEditorAudioMixerImpl d) {
        this.d = d;
    }
    
    @Override
    public void addProgressListener(final TuSdkProgressListener tuSdkProgressListener) {
        if (tuSdkProgressListener == null) {
            return;
        }
        this.l.add(tuSdkProgressListener);
    }
    
    @Override
    public void removeProgressListener(final TuSdkProgressListener tuSdkProgressListener) {
        if (tuSdkProgressListener == null) {
            return;
        }
        this.l.remove(tuSdkProgressListener);
    }
    
    @Override
    public void removeAllProgressListener() {
        this.l.clear();
    }
    
    @Override
    public void addPreviewSizeChangeListener(final TuSdkPreviewSizeChangeListener tuSdkPreviewSizeChangeListener) {
        if (tuSdkPreviewSizeChangeListener == null) {
            return;
        }
        this.m.add(tuSdkPreviewSizeChangeListener);
    }
    
    @Override
    public void removePreviewSizeChangeListener(final TuSdkPreviewSizeChangeListener tuSdkPreviewSizeChangeListener) {
        if (tuSdkPreviewSizeChangeListener == null && this.m.size() == 0) {
            return;
        }
        this.m.remove(tuSdkPreviewSizeChangeListener);
    }
    
    @Override
    public void removeAllPreviewSizeChangeListener() {
        if (this.m.size() == 0) {
            return;
        }
        this.m.clear();
    }
    
    protected void setProgressOutputMode(final int n) {
        if (this.a == null) {
            return;
        }
        this.g = n;
        this.a.setProgressOutputMode(n);
    }
    
    protected int getProgressOutputMode() {
        return this.g;
    }
    
    @Override
    public void setTimeEffect(final TuSdkMediaTimeEffect o) {
        if (this.a == null) {
            return;
        }
        if (o == null) {
            this.clearTimeEffect();
            return;
        }
        this.o = o;
        if (this.A == null) {
            this.A = new ArrayList<TuSdkMediaTimeSliceEntity>();
            for (final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity : this.a.getTimeLine().getFinalSlices()) {
                final TuSdkMediaTimeSliceEntity clone = new TuSdkMediaTimeSliceEntity((TuSdkMediaTimeSlice)tuSdkMediaTimeSliceEntity).clone();
                clone.outputStartUs = tuSdkMediaTimeSliceEntity.outputStartUs;
                clone.outputEndUs = tuSdkMediaTimeSliceEntity.outputEndUs;
                this.A.add(clone);
            }
        }
        this.h = (o instanceof TuSdkMediaReversalTimeEffect);
        o.setRealTimeSlices(this.A);
        this.i = new TuSdkMediaFileCuterTimeline((List)o.getTimeSlickList());
        this.a.preview((TuSdkMediaTimeline)this.i);
    }
    
    @Override
    public void clearTimeEffect() {
        if (this.a == null) {
            return;
        }
        if (this.i == null) {
            return;
        }
        this.h = false;
        this.o = null;
        TuSdkMediaTimeline tuSdkMediaTimeline;
        if (this.n != null) {
            tuSdkMediaTimeline = new TuSdkMediaTimeline((List)this.n);
        }
        else {
            tuSdkMediaTimeline = new TuSdkMediaTimeline(0.0f, (float)this.getOutputTotalTimeUS());
        }
        this.i.fresh(tuSdkMediaTimeline);
        this.a.preview((TuSdkMediaTimeline)this.i);
    }
    
    public void reset() {
        if (this.a == null) {
            return;
        }
        this.a.reset();
    }
    
    @Override
    public void destroy() {
        if (!this.isPause()) {
            this.a.pause();
        }
        this.l.clear();
        this.a.release();
        this.a = null;
        this.e = null;
    }
}
