package org.lasque.tusdk.video.editor;

import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.TuSDKMediaTransitionWrap;
import org.lasque.tusdk.core.seles.tusdk.TuSDKMediaTransitionWrap.TuSDKMediaTransitionType;

public class TuSdkMediaTransitionEffectData
  extends TuSdkMediaEffectData
{
  private TuSDKMediaTransitionWrap.TuSDKMediaTransitionType a;
  
  public TuSdkMediaTransitionEffectData(TuSDKMediaTransitionWrap.TuSDKMediaTransitionType paramTuSDKMediaTransitionType)
  {
    this.a = paramTuSDKMediaTransitionType;
    setMediaEffectType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeTransition);
    setVaild(true);
    this.mFilterWrap = new TuSDKMediaTransitionWrap(paramTuSDKMediaTransitionType);
  }
  
  public final TuSDKMediaTransitionWrap.TuSDKMediaTransitionType getEffectCode()
  {
    return this.a;
  }
  
  public TuSdkMediaEffectData clone()
  {
    TuSdkMediaTransitionEffectData localTuSdkMediaTransitionEffectData = new TuSdkMediaTransitionEffectData(this.a);
    localTuSdkMediaTransitionEffectData.setAtTimeRange(getAtTimeRange());
    localTuSdkMediaTransitionEffectData.setVaild(true);
    localTuSdkMediaTransitionEffectData.setMediaEffectType(getMediaEffectType());
    localTuSdkMediaTransitionEffectData.setIsApplied(false);
    return localTuSdkMediaTransitionEffectData;
  }
  
  public FilterWrap getFilterWrap()
  {
    if (this.mFilterWrap == null) {
      this.mFilterWrap = new TuSDKMediaTransitionWrap(this.a);
    }
    return this.mFilterWrap;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaTransitionEffectData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */