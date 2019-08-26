package org.lasque.tusdk.video.editor;

import android.text.TextUtils;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.combo.Face2DComboFilterWrap;
import org.lasque.tusdk.core.utils.TLog;

public final class TuSdkMediaFilterEffectData
  extends TuSdkMediaEffectData
{
  private String a;
  
  public TuSdkMediaFilterEffectData(String paramString)
  {
    setMediaEffectType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter);
    if (!FilterManager.shared().isFilterEffect(paramString))
    {
      TLog.e("Invalid filter effect code ：%s", new Object[] { paramString });
      setVaild(false);
      return;
    }
    this.a = paramString;
    setVaild(!TextUtils.isEmpty(paramString));
    if (!isVaild()) {
      TLog.e("Invalid filter effect code ：%s", new Object[] { paramString });
    }
  }
  
  public String getFilterCode()
  {
    return this.a;
  }
  
  public synchronized FilterWrap getFilterWrap()
  {
    if (this.mFilterWrap == null)
    {
      FilterOption localFilterOption = FilterLocalPackage.shared().option(this.a);
      this.mFilterWrap = Face2DComboFilterWrap.creat(localFilterOption);
      this.mFilterWrap.processImage();
    }
    return this.mFilterWrap;
  }
  
  public TuSdkMediaEffectData clone()
  {
    if (!isVaild()) {
      return null;
    }
    TuSdkMediaFilterEffectData localTuSdkMediaFilterEffectData = new TuSdkMediaFilterEffectData(this.a);
    localTuSdkMediaFilterEffectData.setAtTimeRange(getAtTimeRange());
    localTuSdkMediaFilterEffectData.setVaild(true);
    localTuSdkMediaFilterEffectData.setMediaEffectType(getMediaEffectType());
    localTuSdkMediaFilterEffectData.setIsApplied(false);
    return localTuSdkMediaFilterEffectData;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaFilterEffectData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */