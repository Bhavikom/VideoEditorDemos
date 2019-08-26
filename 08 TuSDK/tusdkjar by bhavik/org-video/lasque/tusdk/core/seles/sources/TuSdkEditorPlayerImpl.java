package org.lasque.tusdk.core.seles.sources;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSdkMixerRender;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import org.lasque.tusdk.core.api.extend.TuSdkMediaDirectorPlayerListener;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileDirectorPlayerImpl;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorPlayerSync.TuSdkEffectFrameCalc;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateFillModeBuilder.OnDisplaySizeChangeListener;
import org.lasque.tusdk.core.seles.output.SelesView;
import org.lasque.tusdk.core.seles.output.SelesView.SelesFillModeType;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.video.editor.TuSdkMediaRepeatTimeEffect;
import org.lasque.tusdk.video.editor.TuSdkMediaReversalTimeEffect;
import org.lasque.tusdk.video.editor.TuSdkMediaSlowTimeEffect;
import org.lasque.tusdk.video.editor.TuSdkMediaTimeEffect;

public class TuSdkEditorPlayerImpl
  implements TuSdkEditorPlayer
{
  private TuSdkMediaFileDirectorPlayerImpl a = new TuSdkMediaFileDirectorPlayerImpl();
  private TuSdkMediaDataSource b;
  private TuSdkEditorEffectorImpl c;
  private TuSdkEditorAudioMixerImpl d;
  private SelesView e;
  private boolean f = false;
  private int g = 0;
  private boolean h = false;
  private TuSdkMediaFileCuterTimeline i;
  private GLSurfaceView.Renderer j;
  private TuSdkSurfaceDraw k;
  private List<TuSdkEditorPlayer.TuSdkProgressListener> l = new ArrayList();
  private List<TuSdkEditorPlayer.TuSdkPreviewSizeChangeListener> m = new ArrayList();
  private ArrayList<TuSdkMediaTimeSlice> n;
  private TuSdkMediaTimeEffect o;
  private long p = 0L;
  private boolean q = true;
  private float r;
  private TuSdkSize s;
  private float t = 0.0F;
  private float u = 0.0F;
  private float v = 0.0F;
  private float w = 1.0F;
  private TuSdkMediaDirectorPlayerListener x = new TuSdkMediaDirectorPlayerListener()
  {
    public void onProgress(long paramAnonymousLong1, long paramAnonymousLong2, TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource, TuSdkMediaFileCuterTimeline paramAnonymousTuSdkMediaFileCuterTimeline)
    {
      float f = 0.0F;
      long l1 = 0L;
      long l2 = 0L;
      if ((TuSdkEditorPlayerImpl.a(TuSdkEditorPlayerImpl.this) == 1) && (TuSdkEditorPlayerImpl.b(TuSdkEditorPlayerImpl.this) != null))
      {
        TuSdkEditorPlayerImpl.a(TuSdkEditorPlayerImpl.this, l1 = TuSdkEditorPlayerImpl.b(TuSdkEditorPlayerImpl.this).calcOutputTimeUs(paramAnonymousLong1, paramAnonymousTuSdkMediaFileCuterTimeline.sliceWithOutputTimeUs(paramAnonymousLong1), paramAnonymousTuSdkMediaFileCuterTimeline.getFinalSlices()));
        l2 = TuSdkEditorPlayerImpl.b(TuSdkEditorPlayerImpl.this).getInputTotalTimeUs();
        f = (float)l1 / (float)l2;
        if (((TuSdkEditorPlayerImpl.b(TuSdkEditorPlayerImpl.this) instanceof TuSdkMediaReversalTimeEffect)) && (paramAnonymousLong1 == paramAnonymousLong2)) {
          f = 0.0F;
        }
      }
      else
      {
        l1 = paramAnonymousLong1;
        l2 = paramAnonymousLong2;
        f = (float)paramAnonymousLong1 / (float)paramAnonymousLong2;
      }
      if (f >= 1.0F)
      {
        f = 1.0F;
        StatisticsManger.appendComponent(9445378L);
      }
      Iterator localIterator = TuSdkEditorPlayerImpl.c(TuSdkEditorPlayerImpl.this).iterator();
      while (localIterator.hasNext())
      {
        TuSdkEditorPlayer.TuSdkProgressListener localTuSdkProgressListener = (TuSdkEditorPlayer.TuSdkProgressListener)localIterator.next();
        if (localTuSdkProgressListener != null) {
          localTuSdkProgressListener.onProgress(l1, l2, f);
        }
      }
      if ((paramAnonymousLong1 >= paramAnonymousLong2) && (TuSdkEditorPlayerImpl.d(TuSdkEditorPlayerImpl.this) != null)) {
        TuSdkEditorPlayerImpl.d(TuSdkEditorPlayerImpl.this).getMixerAudioRender().seekTo(0L);
      }
    }
    
    public void onStateChanged(int paramAnonymousInt)
    {
      Iterator localIterator = TuSdkEditorPlayerImpl.c(TuSdkEditorPlayerImpl.this).iterator();
      while (localIterator.hasNext())
      {
        TuSdkEditorPlayer.TuSdkProgressListener localTuSdkProgressListener = (TuSdkEditorPlayer.TuSdkProgressListener)localIterator.next();
        if (localTuSdkProgressListener != null) {
          localTuSdkProgressListener.onStateChanged(paramAnonymousInt);
        }
      }
    }
    
    public void onFrameAvailable()
    {
      if (TuSdkEditorPlayerImpl.e(TuSdkEditorPlayerImpl.this) != null) {
        TuSdkEditorPlayerImpl.e(TuSdkEditorPlayerImpl.this).requestRender();
      }
    }
    
    public void onProgress(long paramAnonymousLong1, TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource, long paramAnonymousLong2) {}
    
    public void onCompleted(Exception paramAnonymousException, TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource) {}
  };
  private GLSurfaceView.Renderer y = new GLSurfaceView.Renderer()
  {
    public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig)
    {
      GLES20.glDisable(2929);
      TuSdkEditorPlayerImpl.f(TuSdkEditorPlayerImpl.this).initInGLThread();
      if (TuSdkEditorPlayerImpl.g(TuSdkEditorPlayerImpl.this) != null) {
        TuSdkEditorPlayerImpl.g(TuSdkEditorPlayerImpl.this).onSurfaceCreated();
      }
    }
    
    public void onSurfaceChanged(GL10 paramAnonymousGL10, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      GLES20.glViewport(0, 0, paramAnonymousInt1, paramAnonymousInt2);
      if (TuSdkEditorPlayerImpl.g(TuSdkEditorPlayerImpl.this) != null) {
        TuSdkEditorPlayerImpl.g(TuSdkEditorPlayerImpl.this).onSurfaceChanged(paramAnonymousInt1, paramAnonymousInt2);
      }
    }
    
    public void onDrawFrame(GL10 paramAnonymousGL10)
    {
      GLES20.glClear(16640);
      if (TuSdkEditorPlayerImpl.f(TuSdkEditorPlayerImpl.this) != null) {
        TuSdkEditorPlayerImpl.f(TuSdkEditorPlayerImpl.this).newFrameReadyInGLThread();
      }
    }
  };
  private TuSdkSurfaceDraw z = new TuSdkSurfaceDraw()
  {
    public int onDrawFrame(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, long paramAnonymousLong)
    {
      if (TuSdkEditorPlayerImpl.g(TuSdkEditorPlayerImpl.this) == null) {
        return paramAnonymousInt1;
      }
      return TuSdkEditorPlayerImpl.g(TuSdkEditorPlayerImpl.this).processFrame(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3, paramAnonymousLong);
    }
    
    public void onDrawFrameCompleted() {}
  };
  private List<TuSdkMediaTimeSliceEntity> A;
  
  public TuSdkEditorPlayerImpl(Context paramContext)
  {
    this.a.setListener(this.x);
    this.a.setEffectFrameCalc(new TuSdkMediaFileDirectorPlayerSync.TuSdkEffectFrameCalc()
    {
      public long calcEffectFrameUs(long paramAnonymousLong, TuSdkMediaTimeSliceEntity paramAnonymousTuSdkMediaTimeSliceEntity)
      {
        TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = TuSdkEditorPlayerImpl.f(TuSdkEditorPlayerImpl.this).getTimeLine().sliceWithOutputTimeUs(paramAnonymousLong);
        long l1 = paramAnonymousLong;
        if (TuSdkEditorPlayerImpl.b(TuSdkEditorPlayerImpl.this) == null)
        {
          if (l1 > TuSdkEditorPlayerImpl.this.getTotalTimeUs()) {
            return TuSdkEditorPlayerImpl.this.getTotalTimeUs();
          }
          return l1;
        }
        long l2 = TuSdkEditorPlayerImpl.b(TuSdkEditorPlayerImpl.this).calcOutputTimeUs(l1, localTuSdkMediaTimeSliceEntity, TuSdkEditorPlayerImpl.f(TuSdkEditorPlayerImpl.this).getTimeLine().getFinalSlices());
        TuSdkEditorPlayerImpl.a(TuSdkEditorPlayerImpl.this, l2);
        return l2;
      }
    });
    a(paramContext);
  }
  
  private void a(Context paramContext)
  {
    this.e = new SelesView(paramContext);
    this.e.setFillMode(SelesView.SelesFillModeType.PreserveAspectRatio);
    this.e.setOnDisplayChangeListener(new SelesVerticeCoordinateFillModeBuilder.OnDisplaySizeChangeListener()
    {
      public void onDisplaySizeChanged(TuSdkSize paramAnonymousTuSdkSize)
      {
        Iterator localIterator = TuSdkEditorPlayerImpl.h(TuSdkEditorPlayerImpl.this).iterator();
        while (localIterator.hasNext())
        {
          TuSdkEditorPlayer.TuSdkPreviewSizeChangeListener localTuSdkPreviewSizeChangeListener = (TuSdkEditorPlayer.TuSdkPreviewSizeChangeListener)localIterator.next();
          localTuSdkPreviewSizeChangeListener.onPreviewSizeChanged(paramAnonymousTuSdkSize);
        }
      }
    });
  }
  
  public void setPreviewContainer(ViewGroup paramViewGroup)
  {
    if (paramViewGroup == null) {
      return;
    }
    ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, -1);
    paramViewGroup.addView(this.e, 0, localLayoutParams);
  }
  
  public void setDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    this.b = paramTuSdkMediaDataSource;
  }
  
  public void setEditTimeSlice(ArrayList<TuSdkMediaTimeSlice> paramArrayList)
  {
    this.n = paramArrayList;
  }
  
  public void enableFirstFramePause(boolean paramBoolean)
  {
    this.f = paramBoolean;
  }
  
  public void setCanvasColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.t = paramFloat1;
    this.u = paramFloat2;
    this.v = paramFloat3;
    this.w = paramFloat4;
  }
  
  public void setCanvasColor(int paramInt)
  {
    setCanvasColor(Color.red(paramInt) / 255.0F, Color.green(paramInt) / 255.0F, Color.blue(paramInt) / 255.0F, Color.alpha(paramInt) / 255.0F);
  }
  
  public float getCanvasColorRed()
  {
    return this.t;
  }
  
  public float getCanvasColorGreen()
  {
    return this.u;
  }
  
  public float getCanvasColorBlue()
  {
    return this.v;
  }
  
  public float getCanvasColorAlpha()
  {
    return this.w;
  }
  
  public void setBackGround(int paramInt)
  {
    if (this.e == null) {
      return;
    }
    this.e.setBackgroundColor(paramInt);
  }
  
  protected void initInGLThread()
  {
    if (this.a == null) {
      return;
    }
    this.a.initInGLThread();
  }
  
  protected void newFrameReadyInGLThread()
  {
    if (this.a == null) {
      return;
    }
    this.a.newFrameReadyInGLThread();
  }
  
  protected boolean loadVideo()
  {
    if ((this.b == null) || (!this.b.isValid()))
    {
      TLog.e("%s data source is invalid !!!", new Object[] { "TuSdkEditorPlayer" });
      return false;
    }
    this.a.setMediaDataSource(this.b);
    this.e.setRenderer(this.j == null ? this.y : this.j);
    this.a.setSurfaceDraw(this.k == null ? this.z : this.k);
    boolean bool = this.a.load(this.f);
    if ((bool) || (this.e != null)) {
      this.a.getFilterBridge().addTarget(this.e, 0);
    }
    if (this.n != null)
    {
      this.i = new TuSdkMediaFileCuterTimeline(this.n);
      this.a.preview(this.i);
    }
    this.a.setCanvasColor(this.t, this.u, this.v, this.w);
    return bool;
  }
  
  public void startPreview()
  {
    if (this.a != null) {
      this.a.resume();
    }
    StatisticsManger.appendComponent(9445377L);
  }
  
  protected TuSdkMediaTimeline getTimelineEffect()
  {
    return this.a.getTimeLine();
  }
  
  protected TuSdkMediaTimeEffect getTimeEffect()
  {
    return this.o;
  }
  
  public boolean isReversing()
  {
    return this.h;
  }
  
  public void pausePreview()
  {
    if (this.a == null) {
      return;
    }
    this.a.pause();
  }
  
  public boolean isPause()
  {
    if (this.a == null) {
      return false;
    }
    return this.a.isPause();
  }
  
  public void seekTimeUs(long paramLong)
  {
    if (this.g == 0) {
      seekOutputTimeUs(paramLong);
    } else {
      seekInputTimeUs(paramLong);
    }
  }
  
  public void seekOutputTimeUs(long paramLong)
  {
    if (!isPause()) {
      pausePreview();
    }
    if (this.a == null) {
      return;
    }
    this.a.seekTo(paramLong);
    if (this.d != null) {
      this.d.getMixerAudioRender().seekTo(paramLong);
    }
  }
  
  public void seekInputTimeUs(long paramLong)
  {
    if (!isPause()) {
      pausePreview();
    }
    paramLong = paramLong > getOutputTotalTimeUS() ? getOutputTotalTimeUS() : paramLong;
    if (this.a.getTimeLine() == null)
    {
      seekOutputTimeUs(paramLong);
      return;
    }
    TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = this.a.getTimeLine().sliceWithInputTimeUs(paramLong);
    if (localTuSdkMediaTimeSliceEntity == null)
    {
      seekOutputTimeUs(paramLong);
      return;
    }
    long l1;
    if ((this.o instanceof TuSdkMediaSlowTimeEffect))
    {
      l1 = this.a.getTimeLine().sliceWithCalcModeOutputTimeUs(paramLong);
    }
    else if ((this.o instanceof TuSdkMediaRepeatTimeEffect))
    {
      l1 = localTuSdkMediaTimeSliceEntity.calOutputHaveRepetTimeUs(paramLong, this.a.getTimeLine());
    }
    else if ((this.o instanceof TuSdkMediaReversalTimeEffect))
    {
      localTuSdkMediaTimeSliceEntity.calOutputTimeUs(paramLong);
      l1 = localTuSdkMediaTimeSliceEntity.calOutputTimeUs(paramLong);
    }
    else if (this.n != null)
    {
      l1 = localTuSdkMediaTimeSliceEntity.calRealTimeOutputTimeUs(paramLong);
    }
    else
    {
      l1 = localTuSdkMediaTimeSliceEntity.calOutputTimeUs(paramLong);
    }
    seekOutputTimeUs(l1);
  }
  
  public long getCurrentTimeUs()
  {
    return this.g == 0 ? getCurrentOutputTimeUs() : getCurrentInputTimeUs();
  }
  
  public long getTotalTimeUs()
  {
    return this.g == 0 ? getOutputTotalTimeUS() : getInputTotalTimeUs();
  }
  
  public long getCurrentInputTimeUs()
  {
    if (this.a == null) {
      return -1L;
    }
    if (this.o != null) {
      return this.p;
    }
    return this.a.decodeFrameTimeUs();
  }
  
  public long getCurrentOutputTimeUs()
  {
    return this.a.outputTimeUs();
  }
  
  public long getOutputTotalTimeUS()
  {
    if (this.a == null) {
      return -1L;
    }
    return this.a.durationUs();
  }
  
  public long getInputTotalTimeUs()
  {
    if (this.a == null) {
      return -1L;
    }
    if ((this.o != null) && ((this.o instanceof TuSdkMediaSlowTimeEffect))) {
      return this.o.getInputTotalTimeUs();
    }
    return this.a.inputDurationUs();
  }
  
  private void a(boolean paramBoolean)
  {
    if (this.a == null) {
      return;
    }
    this.q = paramBoolean;
    this.a.setEnableClip(paramBoolean);
  }
  
  public boolean isEnableClip()
  {
    return this.q;
  }
  
  public TuSdkSize getOutputSize()
  {
    return this.s;
  }
  
  public void setOutputRatio(float paramFloat, boolean paramBoolean)
  {
    if (this.a == null) {
      return;
    }
    this.r = paramFloat;
    a(paramBoolean);
    this.s = this.a.setOutputRatio(paramFloat);
  }
  
  public float getOutputRatio()
  {
    return this.r;
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize, boolean paramBoolean)
  {
    if (this.a == null) {
      return;
    }
    a(paramBoolean);
    this.s = paramTuSdkSize;
    this.a.setOutputSize(paramTuSdkSize);
  }
  
  public void setVideoSoundVolume(float paramFloat)
  {
    if (this.a == null) {
      return;
    }
    this.a.setVolume(paramFloat);
  }
  
  protected void setRender(GLSurfaceView.Renderer paramRenderer)
  {
    if (paramRenderer == null)
    {
      TLog.e("%s renderer is null. ", new Object[] { "TuSdkEditorPlayer" });
      return;
    }
    this.j = paramRenderer;
  }
  
  protected void setSurfaceDraw(TuSdkSurfaceDraw paramTuSdkSurfaceDraw)
  {
    if (paramTuSdkSurfaceDraw == null)
    {
      TLog.e("%s surfaceDraw is null. ", new Object[] { "TuSdkEditorPlayer" });
      return;
    }
    this.k = paramTuSdkSurfaceDraw;
  }
  
  protected void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    if (paramTuSdkAudioRender == null)
    {
      TLog.e("%s audioRender is null ", new Object[] { "TuSdkEditorPlayer" });
      return;
    }
    if (this.a == null) {
      return;
    }
    this.a.setAudioRender(paramTuSdkAudioRender);
  }
  
  protected void setAudioMixerRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    if (paramTuSdkAudioRender == null)
    {
      TLog.e("%s audioMixerRender is null ", new Object[] { "TuSdkEditorPlayer" });
      return;
    }
    if (this.a == null) {
      return;
    }
    this.a.setAudioMixerRender(paramTuSdkAudioRender);
  }
  
  protected void setEffector(TuSdkEditorEffectorImpl paramTuSdkEditorEffectorImpl)
  {
    this.c = paramTuSdkEditorEffectorImpl;
  }
  
  protected void setAudioMixer(TuSdkEditorAudioMixerImpl paramTuSdkEditorAudioMixerImpl)
  {
    this.d = paramTuSdkEditorAudioMixerImpl;
  }
  
  public void addProgressListener(TuSdkEditorPlayer.TuSdkProgressListener paramTuSdkProgressListener)
  {
    if (paramTuSdkProgressListener == null) {
      return;
    }
    this.l.add(paramTuSdkProgressListener);
  }
  
  public void removeProgressListener(TuSdkEditorPlayer.TuSdkProgressListener paramTuSdkProgressListener)
  {
    if (paramTuSdkProgressListener == null) {
      return;
    }
    this.l.remove(paramTuSdkProgressListener);
  }
  
  public void removeAllProgressListener()
  {
    this.l.clear();
  }
  
  public void addPreviewSizeChangeListener(TuSdkEditorPlayer.TuSdkPreviewSizeChangeListener paramTuSdkPreviewSizeChangeListener)
  {
    if (paramTuSdkPreviewSizeChangeListener == null) {
      return;
    }
    this.m.add(paramTuSdkPreviewSizeChangeListener);
  }
  
  public void removePreviewSizeChangeListener(TuSdkEditorPlayer.TuSdkPreviewSizeChangeListener paramTuSdkPreviewSizeChangeListener)
  {
    if ((paramTuSdkPreviewSizeChangeListener == null) && (this.m.size() == 0)) {
      return;
    }
    this.m.remove(paramTuSdkPreviewSizeChangeListener);
  }
  
  public void removeAllPreviewSizeChangeListener()
  {
    if (this.m.size() == 0) {
      return;
    }
    this.m.clear();
  }
  
  protected void setProgressOutputMode(int paramInt)
  {
    if (this.a == null) {
      return;
    }
    this.g = paramInt;
    this.a.setProgressOutputMode(paramInt);
  }
  
  protected int getProgressOutputMode()
  {
    return this.g;
  }
  
  public void setTimeEffect(TuSdkMediaTimeEffect paramTuSdkMediaTimeEffect)
  {
    if (this.a == null) {
      return;
    }
    if (paramTuSdkMediaTimeEffect == null)
    {
      clearTimeEffect();
      return;
    }
    this.o = paramTuSdkMediaTimeEffect;
    if (this.A == null)
    {
      this.A = new ArrayList();
      localObject = this.a.getTimeLine().getFinalSlices().iterator();
      while (((Iterator)localObject).hasNext())
      {
        TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity1 = (TuSdkMediaTimeSliceEntity)((Iterator)localObject).next();
        TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity2 = new TuSdkMediaTimeSliceEntity(localTuSdkMediaTimeSliceEntity1);
        TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity3 = localTuSdkMediaTimeSliceEntity2.clone();
        localTuSdkMediaTimeSliceEntity3.outputStartUs = localTuSdkMediaTimeSliceEntity1.outputStartUs;
        localTuSdkMediaTimeSliceEntity3.outputEndUs = localTuSdkMediaTimeSliceEntity1.outputEndUs;
        this.A.add(localTuSdkMediaTimeSliceEntity3);
      }
    }
    this.h = (paramTuSdkMediaTimeEffect instanceof TuSdkMediaReversalTimeEffect);
    paramTuSdkMediaTimeEffect.setRealTimeSlices(this.A);
    Object localObject = paramTuSdkMediaTimeEffect.getTimeSlickList();
    this.i = new TuSdkMediaFileCuterTimeline((List)localObject);
    this.a.preview(this.i);
  }
  
  public void clearTimeEffect()
  {
    if (this.a == null) {
      return;
    }
    if (this.i == null) {
      return;
    }
    this.h = false;
    this.o = null;
    TuSdkMediaTimeline localTuSdkMediaTimeline;
    if (this.n != null) {
      localTuSdkMediaTimeline = new TuSdkMediaTimeline(this.n);
    } else {
      localTuSdkMediaTimeline = new TuSdkMediaTimeline(0.0F, (float)getOutputTotalTimeUS());
    }
    this.i.fresh(localTuSdkMediaTimeline);
    this.a.preview(this.i);
  }
  
  public void reset()
  {
    if (this.a == null) {
      return;
    }
    this.a.reset();
  }
  
  public void destroy()
  {
    if (!isPause()) {
      this.a.pause();
    }
    this.l.clear();
    this.a.release();
    this.a = null;
    this.e = null;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\sources\TuSdkEditorPlayerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */