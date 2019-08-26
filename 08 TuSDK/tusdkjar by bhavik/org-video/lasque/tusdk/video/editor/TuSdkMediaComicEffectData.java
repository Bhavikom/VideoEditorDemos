package org.lasque.tusdk.video.editor;

import android.text.TextUtils;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkMediaComicEffectData
  extends TuSdkMediaEffectData
{
  private String a;
  
  public TuSdkMediaComicEffectData(String paramString)
  {
    setMediaEffectType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeComic);
    if (!FilterManager.shared().isConmicEffectFilter(paramString))
    {
      TLog.e("Please enter a valid comic code.", new Object[0]);
      setVaild(false);
      return;
    }
    this.a = paramString;
    setVaild(!TextUtils.isEmpty(paramString));
  }
  
  public TuSdkMediaEffectData clone()
  {
    if (!isVaild()) {
      return null;
    }
    TuSdkMediaComicEffectData localTuSdkMediaComicEffectData = new TuSdkMediaComicEffectData(this.a);
    localTuSdkMediaComicEffectData.setAtTimeRange(getAtTimeRange());
    localTuSdkMediaComicEffectData.setVaild(true);
    localTuSdkMediaComicEffectData.setMediaEffectType(getMediaEffectType());
    localTuSdkMediaComicEffectData.setIsApplied(false);
    return localTuSdkMediaComicEffectData;
  }
  
  public synchronized FilterWrap getFilterWrap()
  {
    if (this.mFilterWrap == null)
    {
      FilterOption localFilterOption = FilterLocalPackage.shared().option(this.a);
      this.mFilterWrap = FilterWrap.creat(localFilterOption);
      this.mFilterWrap.processImage();
    }
    return this.mFilterWrap;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaComicEffectData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */