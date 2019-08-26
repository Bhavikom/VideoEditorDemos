package org.lasque.tusdk.core.seles.sources;

import android.graphics.Color;
import android.media.MediaFormat;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSdkMixerRender;
import org.lasque.tusdk.api.engine.TuSdkFilterEngine;
import org.lasque.tusdk.api.engine.TuSdkFilterEngineImpl;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting.VideoQuality;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileCuterImpl;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorSync;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.AlbumHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.video.editor.TuSdkMediaAudioEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaStickerAudioEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaTimeEffect;

public class TuSdkEditorSaverImpl
  implements TuSdkEditorSaver
{
  private TuSdkMediaFileCuterImpl a;
  private TuSdkFilterEngine b = new TuSdkFilterEngineImpl(false, true);
  private List<TuSdkMediaEffectData> c;
  private TuSdkEditorAudioMixerImpl d;
  private TuSdkMediaTimeline e;
  private SelesWatermark f;
  private File g;
  private TuSdkEditorSaver.TuSdkEditorSaverOptions h;
  private int i = -1;
  private TuSdkMediaTimeEffect j;
  private TuSdkMediaFileDirectorSync k;
  private boolean l = true;
  private float m = 0.0F;
  private float n = 0.0F;
  private float o = 0.0F;
  private float p = 0.0F;
  private float q = 1.0F;
  private int r = 0;
  private List<TuSdkEditorSaver.TuSdkSaverProgressListener> s = new ArrayList();
  private TuSdkMediaProgress t = new TuSdkMediaProgress()
  {
    public void onProgress(float paramAnonymousFloat, TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      Iterator localIterator = TuSdkEditorSaverImpl.a(TuSdkEditorSaverImpl.this).iterator();
      while (localIterator.hasNext())
      {
        TuSdkEditorSaver.TuSdkSaverProgressListener localTuSdkSaverProgressListener = (TuSdkEditorSaver.TuSdkSaverProgressListener)localIterator.next();
        localTuSdkSaverProgressListener.onProgress(paramAnonymousFloat);
      }
    }
    
    public void onCompleted(Exception paramAnonymousException, TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource, int paramAnonymousInt)
    {
      if (TuSdkEditorSaverImpl.a(TuSdkEditorSaverImpl.this).size() == 0) {
        return;
      }
      TuSdkEditorSaverImpl.a(TuSdkEditorSaverImpl.this, paramAnonymousException == null ? 3 : 4);
      if ((paramAnonymousException == null) && (TuSdkEditorSaverImpl.b(TuSdkEditorSaverImpl.this).c))
      {
        localObject = ImageSqlHelper.saveMp4ToAlbum(TuSdk.appContext().getContext(), new File(paramAnonymousTuSdkMediaDataSource.getPath()));
        ImageSqlHelper.notifyRefreshAblum(TuSdk.appContext().getContext(), (ImageSqlInfo)localObject);
      }
      Object localObject = TuSdkEditorSaverImpl.a(TuSdkEditorSaverImpl.this).iterator();
      while (((Iterator)localObject).hasNext())
      {
        TuSdkEditorSaver.TuSdkSaverProgressListener localTuSdkSaverProgressListener = (TuSdkEditorSaver.TuSdkSaverProgressListener)((Iterator)localObject).next();
        if (paramAnonymousException == null) {
          localTuSdkSaverProgressListener.onCompleted(paramAnonymousTuSdkMediaDataSource);
        } else {
          localTuSdkSaverProgressListener.onError(paramAnonymousException);
        }
      }
    }
  };
  private TuSdkSurfaceRender u = new TuSdkSurfaceRender()
  {
    public void onSurfaceCreated()
    {
      TuSdkEditorSaverImpl.c(TuSdkEditorSaverImpl.this).onSurfaceCreated();
      TuSdkEditorSaverImpl.d(TuSdkEditorSaverImpl.this);
    }
    
    public void onSurfaceChanged(int paramAnonymousInt1, int paramAnonymousInt2)
    {
      TuSdkEditorSaverImpl.c(TuSdkEditorSaverImpl.this).onSurfaceChanged(paramAnonymousInt1, paramAnonymousInt2);
    }
    
    public void onSurfaceDestory()
    {
      TuSdkEditorSaverImpl.c(TuSdkEditorSaverImpl.this).release();
    }
    
    public int onDrawFrame(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, long paramAnonymousLong)
    {
      long l1 = paramAnonymousLong / 1000L;
      long l2 = l1;
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = TuSdkEditorSaverImpl.e(TuSdkEditorSaverImpl.this).getTimeLine().sliceWithOutputTimeUs(l1);
      if ((TuSdkEditorSaverImpl.f(TuSdkEditorSaverImpl.this) != null) && (localTuSdkMediaTimeSliceEntity != null))
      {
        long l3 = TuSdkEditorSaverImpl.e(TuSdkEditorSaverImpl.this).lastVideoDecodecTimestampNs() / 1000L;
        l2 = TuSdkEditorSaverImpl.f(TuSdkEditorSaverImpl.this).calcOutputTimeUs(l3, localTuSdkMediaTimeSliceEntity, TuSdkEditorSaverImpl.e(TuSdkEditorSaverImpl.this).getTimeLine().getFinalSlices());
      }
      else
      {
        l2 = TuSdkEditorSaverImpl.e(TuSdkEditorSaverImpl.this).getTimeLine().sliceWithCalcModeOutputTimeUs(l1);
      }
      return TuSdkEditorSaverImpl.c(TuSdkEditorSaverImpl.this).processFrame(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3, l2 * 1000L);
    }
    
    public void onDrawFrameCompleted() {}
  };
  
  public TuSdkEditorSaverImpl()
  {
    a(1);
  }
  
  public void setOptions(TuSdkEditorSaver.TuSdkEditorSaverOptions paramTuSdkEditorSaverOptions)
  {
    this.h = paramTuSdkEditorSaverOptions;
  }
  
  protected void setTimeline(TuSdkMediaTimeline paramTuSdkMediaTimeline)
  {
    this.e = paramTuSdkMediaTimeline;
  }
  
  protected void setMediaDataList(List<TuSdkMediaEffectData> paramList)
  {
    if (paramList == null) {
      return;
    }
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
      localArrayList.add(localTuSdkMediaEffectData.clone());
      if ((localTuSdkMediaEffectData instanceof TuSdkMediaStickerAudioEffectData)) {
        StatisticsManger.appendComponent(9445382L);
      } else if ((localTuSdkMediaEffectData instanceof TuSdkMediaAudioEffectData)) {
        StatisticsManger.appendComponent(9445380L);
      }
    }
    this.c = localArrayList;
  }
  
  protected void setAudioMixerRender(TuSdkEditorAudioMixerImpl paramTuSdkEditorAudioMixerImpl)
  {
    this.d = paramTuSdkEditorAudioMixerImpl;
  }
  
  public void addSaverProgressListener(TuSdkEditorSaver.TuSdkSaverProgressListener paramTuSdkSaverProgressListener)
  {
    if (paramTuSdkSaverProgressListener == null) {
      return;
    }
    this.s.add(paramTuSdkSaverProgressListener);
  }
  
  public void removeProgressListener(TuSdkEditorSaver.TuSdkSaverProgressListener paramTuSdkSaverProgressListener)
  {
    if (paramTuSdkSaverProgressListener == null) {
      return;
    }
    this.s.remove(paramTuSdkSaverProgressListener);
  }
  
  public void removeAllProgressListener()
  {
    this.s.clear();
  }
  
  public int getStatus()
  {
    return this.i;
  }
  
  public void setOutputRatio(float paramFloat, boolean paramBoolean)
  {
    this.m = paramFloat;
    this.l = paramBoolean;
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize, boolean paramBoolean)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize()))
    {
      TLog.w("%s output size is %s", new Object[] { paramTuSdkSize, "TuSdkEditorSaver" });
      return;
    }
    if (this.a != null) {
      this.a.setOutputSize(paramTuSdkSize);
    }
    this.l = paramBoolean;
  }
  
  public void setCanvasColor(int paramInt)
  {
    setCanvasColor(Color.red(paramInt) / 255.0F, Color.green(paramInt) / 255.0F, Color.blue(paramInt) / 255.0F, Color.alpha(paramInt) / 255.0F);
  }
  
  public void setCanvasColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.n = paramFloat1;
    this.o = paramFloat2;
    this.p = paramFloat3;
    this.q = paramFloat4;
  }
  
  public boolean startSave()
  {
    if ((this.h == null) || (!this.h.check()))
    {
      TLog.e("%s Saver Options is invalid", new Object[] { "TuSdkEditorSaver" });
      return false;
    }
    this.k = new TuSdkMediaFileDirectorSync();
    this.k.getTimeLine().setProgressOutputMode(this.r);
    this.a = new TuSdkMediaFileCuterImpl(this.k);
    this.a.setMediaDataSource(this.h.mediaDataSource);
    this.a.setEnableClip(this.l);
    this.a.setOutputRatio(this.m);
    this.a.setOutputVideoFormat(b());
    this.a.setOutputAudioFormat(c());
    if (this.h.mWaterImageBitmap != null)
    {
      e().setImage(this.h.mWaterImageBitmap, this.h.isRecycleWaterImage);
      e().setWaterPostion(this.h.b);
      e().setScale(this.h.mWaterImageScale);
      if (this.f != null) {
        this.a.setWatermark(this.f);
      }
    }
    this.a.setSurfaceRender(this.u);
    if ((this.d != null) && (this.d.getMixerAudioRender() != null))
    {
      this.d.getMixerAudioRender().seekTo(0L);
      this.a.setAudioMixerRender(this.d.getMixerAudioRender());
      if (this.d.getMixerAudioRender().getTrunkVolume() != 1.0F) {
        StatisticsManger.appendComponent(9445381L);
      }
    }
    if (this.e != null) {
      this.a.setTimeline(this.e);
    }
    this.a.setOutputFilePath(a().getAbsolutePath());
    a(2);
    return this.a.run(this.t);
  }
  
  protected void setTimeEffect(TuSdkMediaTimeEffect paramTuSdkMediaTimeEffect)
  {
    this.j = paramTuSdkMediaTimeEffect;
  }
  
  protected void setCalcMode(int paramInt)
  {
    this.r = paramInt;
  }
  
  public void stopSave()
  {
    this.a.stop();
    a(5);
  }
  
  public void destroy()
  {
    if (this.a != null) {
      this.a.stop();
    }
    this.s.clear();
    this.b.release();
    if ((this.h != null) && (this.h.e)) {
      this.h.mediaDataSource.deleted();
    }
    this.u = null;
    this.a = null;
  }
  
  private File a()
  {
    if (this.h.f != null)
    {
      this.g = this.h.f;
      return this.g;
    }
    if (this.h.c)
    {
      if (StringHelper.isNotBlank(this.h.d)) {
        this.g = AlbumHelper.getAlbumVideoFile(this.h.d);
      } else {
        this.g = AlbumHelper.getAlbumVideoFile();
      }
    }
    else {
      this.g = new File(TuSdk.getAppTempPath(), String.format("lsq_%s.mp4", new Object[] { StringHelper.timeStampString() }));
    }
    return this.g;
  }
  
  private MediaFormat b()
  {
    TuSDKVideoEncoderSetting localTuSDKVideoEncoderSetting = this.h.a;
    TuSdkSize localTuSdkSize1 = TuSdkSize.create(localTuSDKVideoEncoderSetting.videoSize);
    TuSDKVideoInfo localTuSDKVideoInfo = TuSDKMediaUtils.getVideoInfo(this.h.mediaDataSource);
    if (this.m != 0.0F)
    {
      int i1 = localTuSDKVideoInfo.width > localTuSDKVideoInfo.height ? localTuSDKVideoInfo.width : localTuSDKVideoInfo.height;
      int i2 = (int)(i1 / this.m);
      TuSdkSize localTuSdkSize2 = TuSdkSize.create(i1, i2);
      localTuSdkSize1.width = localTuSdkSize2.width;
      localTuSdkSize1.height = localTuSdkSize2.height;
      setOutputSize(localTuSdkSize2, this.l);
    }
    if (((localTuSDKVideoInfo.videoOrientation == ImageOrientation.Right) || (localTuSDKVideoInfo.videoOrientation == ImageOrientation.Left)) && (localTuSdkSize1.width > localTuSdkSize1.height))
    {
      localTuSdkSize1.width = localTuSDKVideoEncoderSetting.videoSize.height;
      localTuSdkSize1.height = localTuSDKVideoEncoderSetting.videoSize.width;
    }
    MediaFormat localMediaFormat = TuSdkMediaFormat.buildSafeVideoEncodecFormat(localTuSdkSize1.width, localTuSdkSize1.height, localTuSDKVideoEncoderSetting.videoQuality.getFps(), localTuSDKVideoEncoderSetting.videoQuality.getBitrate(), 2130708361, 0, localTuSDKVideoEncoderSetting.mediacodecAVCIFrameInterval);
    return localMediaFormat;
  }
  
  private MediaFormat c()
  {
    MediaFormat localMediaFormat = TuSDKMediaUtils.getAudioFormat(this.h.mediaDataSource);
    if ((localMediaFormat == null) || (localMediaFormat.getInteger("channel-count") > 1)) {
      return TuSdkMediaFormat.buildSafeAudioEncodecFormat();
    }
    return TuSdkMediaFormat.buildSafeAudioEncodecFormat(localMediaFormat.getInteger("sample-rate"), localMediaFormat.getInteger("channel-count"), 96000, 2);
  }
  
  private void d()
  {
    if ((this.c == null) || (this.c.size() == 0)) {
      return;
    }
    Iterator localIterator = this.c.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
      this.b.addMediaEffectData(localTuSdkMediaEffectData);
    }
  }
  
  private void a(int paramInt)
  {
    this.i = paramInt;
  }
  
  private synchronized SelesWatermark e()
  {
    if (this.f == null) {
      this.f = new SelesWatermarkImpl(true);
    }
    return this.f;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\sources\TuSdkEditorSaverImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */