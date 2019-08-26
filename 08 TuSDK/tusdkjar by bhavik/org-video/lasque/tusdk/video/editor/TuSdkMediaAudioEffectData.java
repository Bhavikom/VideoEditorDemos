package org.lasque.tusdk.video.editor;

import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderEntry;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkMediaAudioEffectData
  extends TuSdkMediaEffectData
{
  private TuSDKAudioRenderEntry a;
  private float b;
  
  public TuSdkMediaAudioEffectData(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    if ((paramTuSdkMediaDataSource == null) || (!paramTuSdkMediaDataSource.isValid()))
    {
      TLog.e("%s : Invalid audio data", new Object[] { this });
      setVaild(false);
      return;
    }
    this.a = new TuSDKAudioRenderEntry(paramTuSdkMediaDataSource);
    setMediaEffectType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio);
    setVaild(true);
  }
  
  public TuSdkMediaAudioEffectData(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null)
    {
      TLog.w("%s : Invalid audio data", new Object[] { this });
      setVaild(false);
      return;
    }
    this.a = new TuSDKAudioRenderEntry(paramTuSdkAudioInfo);
    setVaild(true);
  }
  
  public final TuSDKAudioRenderEntry getAudioEntry()
  {
    return this.a;
  }
  
  public void setAtTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
  {
    if (!isVaild()) {
      return;
    }
    super.setAtTimeRange(paramTuSdkTimeRange);
    this.a.setTimeRange(paramTuSdkTimeRange);
  }
  
  public final void setVolume(float paramFloat)
  {
    if (!isVaild()) {
      return;
    }
    this.b = paramFloat;
    this.a.setVolume(paramFloat);
  }
  
  public final float getVolume()
  {
    return this.b;
  }
  
  public TuSdkMediaEffectData clone()
  {
    TuSdkMediaAudioEffectData localTuSdkMediaAudioEffectData = new TuSdkMediaAudioEffectData(this.a);
    localTuSdkMediaAudioEffectData.setAtTimeRange(getAtTimeRange());
    localTuSdkMediaAudioEffectData.setVaild(true);
    localTuSdkMediaAudioEffectData.setMediaEffectType(getMediaEffectType());
    localTuSdkMediaAudioEffectData.setIsApplied(false);
    return localTuSdkMediaAudioEffectData;
  }
  
  public synchronized FilterWrap getFilterWrap()
  {
    if (this.mFilterWrap == null)
    {
      FilterOption localFilterOption = FilterLocalPackage.shared().option("Normal");
      this.mFilterWrap = FilterWrap.creat(localFilterOption);
      this.mFilterWrap.processImage();
    }
    return this.mFilterWrap;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaAudioEffectData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */