package org.lasque.tusdk.core.seles.sources;

import android.content.Context;
import android.view.ViewGroup;
import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting.VideoQuality;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkMovieEditorImpl
  implements TuSdkMovieEditor
{
  private Context a;
  private ViewGroup b;
  private TuSdkEditorTranscoderImpl c;
  private TuSdkEditorPlayerImpl d;
  private TuSdkEditorEffectorImpl e;
  private TuSdkEditorAudioMixerImpl f;
  private TuSdkEditorSaverImpl g;
  private TuSdkMediaDataSource h;
  private TuSdkMediaDataSource i;
  private TuSdkMovieEditor.TuSdkMovieEditorOptions j;
  private TuSDKVideoEncoderSetting k;
  private TuSdkEditorTranscoder.TuSdkTranscoderProgressListener l = new TuSdkEditorTranscoder.TuSdkTranscoderProgressListener()
  {
    public void onProgressChanged(float paramAnonymousFloat) {}
    
    public void onLoadComplete(TuSDKVideoInfo paramAnonymousTuSDKVideoInfo, TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource)
    {
      TuSdkMovieEditorImpl.a(TuSdkMovieEditorImpl.this, paramAnonymousTuSDKVideoInfo, paramAnonymousTuSdkMediaDataSource);
    }
    
    public void onError(Exception paramAnonymousException)
    {
      TLog.e(paramAnonymousException);
    }
  };
  
  public TuSdkMovieEditorImpl(Context paramContext, ViewGroup paramViewGroup, TuSdkMovieEditor.TuSdkMovieEditorOptions paramTuSdkMovieEditorOptions)
  {
    this.a = paramContext;
    this.b = paramViewGroup;
    this.j = paramTuSdkMovieEditorOptions;
    setDataSource(paramTuSdkMovieEditorOptions.videoDataSource);
  }
  
  private void a()
  {
    ((TuSdkEditorEffectorImpl)getEditorEffector()).setAudioMixer(getEditorMixer());
    ((TuSdkEditorPlayerImpl)getEditorPlayer()).setEffector((TuSdkEditorEffectorImpl)getEditorEffector());
    ((TuSdkEditorPlayerImpl)getEditorPlayer()).setAudioMixer((TuSdkEditorAudioMixerImpl)getEditorMixer());
  }
  
  public Context getContext()
  {
    return this.a;
  }
  
  public void setVideoPath(String paramString)
  {
    setDataSource(new TuSdkMediaDataSource(paramString));
  }
  
  public void setDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    this.h = paramTuSdkMediaDataSource;
  }
  
  public void setEnableTranscode(boolean paramBoolean)
  {
    if (this.c == null) {
      getEditorTransCoder();
    }
    this.c.setEnableTranscode(paramBoolean);
  }
  
  public void loadVideo()
  {
    a();
    if ((this.h == null) || (!this.h.isValid()))
    {
      TLog.e("%s the data source is invalid. dataSource = %s ", new Object[] { "TuSdkMovieEditorImpl", this.h });
      return;
    }
    getEditorTransCoder().setVideoDataSource(this.h);
    getEditorTransCoder().setTimeRange(this.j.cutTimeRange);
    getEditorTransCoder().addTransCoderProgressListener(this.l);
    getEditorTransCoder().setCanvasRect(this.j.canvasRect);
    getEditorTransCoder().setCropRect(this.j.cropRect);
    getEditorTransCoder().startTransCoder();
  }
  
  public void saveVideo()
  {
    if (!getEditorPlayer().isPause()) {
      getEditorPlayer().pausePreview();
    }
    TuSDKVideoEncoderSetting localTuSDKVideoEncoderSetting = getVideoEncoderSetting();
    TuSDKVideoInfo localTuSDKVideoInfo = this.c.getInputVideoInfo();
    if (localTuSDKVideoInfo != null)
    {
      localTuSDKVideoEncoderSetting.mediacodecAVCIFrameInterval = (localTuSDKVideoInfo.iFrameInterval == 0 ? 1 : localTuSDKVideoInfo.iFrameInterval);
      if (localTuSDKVideoEncoderSetting.videoSize == null)
      {
        if (this.c.getInputVideoInfo().videoOrientation != ImageOrientation.Up) {
          localObject = TuSdkSize.create(this.c.getInputVideoInfo().height, this.c.getInputVideoInfo().width);
        } else {
          localObject = TuSdkSize.create(this.c.getInputVideoInfo().width, this.c.getInputVideoInfo().height);
        }
        localTuSDKVideoEncoderSetting.videoSize = (this.j.outputSize == null ? localObject : this.j.outputSize);
      }
      if (localTuSDKVideoEncoderSetting.videoQuality == null) {
        localTuSDKVideoEncoderSetting.videoQuality = TuSDKVideoEncoderSetting.VideoQuality.RECORD_MEDIUM1;
      }
    }
    Object localObject = new TuSdkEditorSaver.TuSdkEditorSaverOptions();
    ((TuSdkEditorSaver.TuSdkEditorSaverOptions)localObject).mediaDataSource = this.i;
    ((TuSdkEditorSaver.TuSdkEditorSaverOptions)localObject).a = localTuSDKVideoEncoderSetting;
    ((TuSdkEditorSaver.TuSdkEditorSaverOptions)localObject).c = this.j.saveToAlbum.booleanValue();
    ((TuSdkEditorSaver.TuSdkEditorSaverOptions)localObject).d = this.j.saveToAlbumName;
    ((TuSdkEditorSaver.TuSdkEditorSaverOptions)localObject).mWaterImageBitmap = this.j.waterImage;
    ((TuSdkEditorSaver.TuSdkEditorSaverOptions)localObject).mWaterImageScale = this.j.waterImageScale;
    ((TuSdkEditorSaver.TuSdkEditorSaverOptions)localObject).isRecycleWaterImage = this.j.isRecycleWaterImage;
    ((TuSdkEditorSaver.TuSdkEditorSaverOptions)localObject).b = this.j.watermarkPosition;
    ((TuSdkEditorSaver.TuSdkEditorSaverOptions)localObject).f = this.j.movieOutputFilePath;
    ((TuSdkEditorSaverImpl)getEditorSaver()).setMediaDataList(getEditorEffector().getAllMediaEffectData());
    ((TuSdkEditorSaverImpl)getEditorSaver()).setTimeline(((TuSdkEditorPlayerImpl)getEditorPlayer()).getTimelineEffect());
    ((TuSdkEditorSaverImpl)getEditorSaver()).setTimeEffect(((TuSdkEditorPlayerImpl)getEditorPlayer()).getTimeEffect());
    ((TuSdkEditorSaverImpl)getEditorSaver()).setCalcMode(((TuSdkEditorPlayerImpl)getEditorPlayer()).getProgressOutputMode());
    ((TuSdkEditorSaverImpl)getEditorSaver()).setAudioMixerRender((TuSdkEditorAudioMixerImpl)getEditorMixer());
    getEditorSaver().setOptions((TuSdkEditorSaver.TuSdkEditorSaverOptions)localObject);
    ((TuSdkEditorSaverImpl)getEditorSaver()).setOutputRatio(((TuSdkEditorPlayerImpl)getEditorPlayer()).getOutputRatio(), ((TuSdkEditorPlayerImpl)getEditorPlayer()).isEnableClip());
    ((TuSdkEditorSaverImpl)getEditorSaver()).setOutputSize(((TuSdkEditorPlayerImpl)getEditorPlayer()).getOutputSize(), ((TuSdkEditorPlayerImpl)getEditorPlayer()).isEnableClip());
    ((TuSdkEditorSaverImpl)getEditorSaver()).setCanvasColor(((TuSdkEditorPlayerImpl)getEditorPlayer()).getCanvasColorRed(), ((TuSdkEditorPlayerImpl)getEditorPlayer()).getCanvasColorGreen(), ((TuSdkEditorPlayerImpl)getEditorPlayer()).getCanvasColorBlue(), ((TuSdkEditorPlayerImpl)getEditorPlayer()).getCanvasColorAlpha());
    ((TuSdkEditorSaverImpl)getEditorSaver()).startSave();
    StatisticsManger.appendComponent(9445376L);
  }
  
  public TuSdkEditorTranscoder getEditorTransCoder()
  {
    if (this.c == null) {
      this.c = new TuSdkEditorTranscoderImpl();
    }
    return this.c;
  }
  
  public TuSdkEditorPlayer getEditorPlayer()
  {
    if (this.d == null) {
      this.d = new TuSdkEditorPlayerImpl(this.a);
    }
    return this.d;
  }
  
  public TuSdkEditorEffector getEditorEffector()
  {
    if (this.e == null) {
      this.e = new TuSdkEditorEffectorImpl();
    }
    return this.e;
  }
  
  public TuSdkEditorAudioMixer getEditorMixer()
  {
    if (this.f == null) {
      this.f = new TuSdkEditorAudioMixerImpl();
    }
    return this.f;
  }
  
  public TuSdkEditorSaver getEditorSaver()
  {
    if (this.g == null) {
      this.g = new TuSdkEditorSaverImpl();
    }
    return this.g;
  }
  
  public void onDestroy()
  {
    getEditorTransCoder().destroy();
    getEditorPlayer().destroy();
    getEditorEffector().destroy();
    getEditorMixer().destroy();
    getEditorSaver().destroy();
  }
  
  private void a(TuSDKVideoInfo paramTuSDKVideoInfo, TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    this.i = paramTuSdkMediaDataSource;
    getEditorPlayer().setPreviewContainer(this.b);
    getEditorPlayer().setDataSource(paramTuSdkMediaDataSource);
    getEditorPlayer().enableFirstFramePause(this.j.enableFirstFramePause);
    this.d.setProgressOutputMode(this.j.timelineType.getType());
    getEditorEffector().setInputImageOrientation(paramTuSDKVideoInfo.videoOrientation);
    getEditorMixer().setDataSource(paramTuSdkMediaDataSource);
    ((TuSdkEditorPlayerImpl)getEditorPlayer()).setAudioMixerRender(((TuSdkEditorAudioMixerImpl)getEditorMixer()).getMixerAudioRender());
    ((TuSdkEditorAudioMixerImpl)getEditorMixer()).setIncludeMasterSound(this.j.includeAudioInVideo);
    this.d.loadVideo();
  }
  
  public TuSDKVideoEncoderSetting getVideoEncoderSetting()
  {
    if (this.k == null)
    {
      this.k = TuSDKVideoEncoderSetting.getDefaultRecordSetting();
      this.k.videoQuality = null;
      this.k.videoSize = null;
    }
    return this.k;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\sources\TuSdkMovieEditorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */