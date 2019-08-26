package org.lasque.tusdk.video.editor;

import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.TuSDKMonsterFaceWrap;
import org.lasque.tusdk.core.seles.tusdk.TuSDKMonsterFaceWrap.TuSDKMonsterFaceType;

public class TuSDKMediaMonsterFaceEffect
  extends TuSdkMediaEffectData
{
  private TuSDKMonsterFaceWrap.TuSDKMonsterFaceType a;
  
  public TuSDKMediaMonsterFaceEffect(TuSDKMonsterFaceWrap.TuSDKMonsterFaceType paramTuSDKMonsterFaceType)
  {
    setMediaEffectType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace);
    this.a = paramTuSDKMonsterFaceType;
    setVaild(true);
  }
  
  private TuSDKMediaMonsterFaceEffect() {}
  
  public TuSdkMediaEffectData clone()
  {
    TuSDKMediaMonsterFaceEffect localTuSDKMediaMonsterFaceEffect = new TuSDKMediaMonsterFaceEffect(this.a);
    return localTuSDKMediaMonsterFaceEffect;
  }
  
  public synchronized FilterWrap getFilterWrap()
  {
    if (this.mFilterWrap == null)
    {
      this.mFilterWrap = new TuSDKMonsterFaceWrap(this.a);
      this.mFilterWrap.processImage();
    }
    return this.mFilterWrap;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSDKMediaMonsterFaceEffect.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */