package org.lasque.tusdk.video.editor;

import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveMegrimFilter;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkMediaSceneEffectData
  extends TuSdkMediaEffectData
{
  private String a;
  
  public TuSdkMediaSceneEffectData(String paramString)
  {
    this.a = paramString;
    setMediaEffectType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeScene);
    setVaild(FilterManager.shared().isSceneEffectFilter(paramString));
    if (!isVaild()) {
      TLog.e("Invalid scene effect code ：%s", new Object[] { paramString });
    }
  }
  
  public final String getEffectCode()
  {
    return this.a;
  }
  
  public TuSdkMediaEffectData clone()
  {
    TuSdkMediaSceneEffectData localTuSdkMediaSceneEffectData = new TuSdkMediaSceneEffectData(this.a);
    localTuSdkMediaSceneEffectData.setAtTimeRange(getAtTimeRange());
    localTuSdkMediaSceneEffectData.setVaild(true);
    localTuSdkMediaSceneEffectData.setMediaEffectType(getMediaEffectType());
    localTuSdkMediaSceneEffectData.setIsApplied(false);
    return localTuSdkMediaSceneEffectData;
  }
  
  public synchronized FilterWrap getFilterWrap()
  {
    if (this.mFilterWrap == null)
    {
      FilterOption localFilterOption = FilterLocalPackage.shared().option(this.a);
      this.mFilterWrap = FilterWrap.creat(localFilterOption);
      this.mFilterWrap.processImage();
      if ((this.mFilterWrap.getFilter() instanceof TuSDKLiveMegrimFilter)) {
        ((TuSDKLiveMegrimFilter)this.mFilterWrap.getFilter()).enableSeprarate();
      }
    }
    return this.mFilterWrap;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaSceneEffectData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */