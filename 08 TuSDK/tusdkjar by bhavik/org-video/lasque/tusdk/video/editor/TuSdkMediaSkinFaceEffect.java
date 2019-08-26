package org.lasque.tusdk.video.editor;

import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.TuSDKSkinMoistWrap;
import org.lasque.tusdk.core.seles.tusdk.TuSDKSkinNaturalWrap;

public final class TuSdkMediaSkinFaceEffect
  extends TuSdkMediaEffectData
{
  private boolean a;
  
  public TuSdkMediaSkinFaceEffect(boolean paramBoolean)
  {
    this.a = paramBoolean;
    setMediaEffectType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSkinFace);
    setVaild(true);
  }
  
  public synchronized FilterWrap getFilterWrap()
  {
    if (this.mFilterWrap == null)
    {
      if (this.a) {
        this.mFilterWrap = new TuSDKSkinNaturalWrap();
      } else {
        this.mFilterWrap = new TuSDKSkinMoistWrap();
      }
      this.mFilterWrap.processImage();
    }
    return this.mFilterWrap;
  }
  
  public TuSdkMediaEffectData clone()
  {
    if (!isVaild()) {
      return null;
    }
    TuSdkMediaSkinFaceEffect localTuSdkMediaSkinFaceEffect = new TuSdkMediaSkinFaceEffect(this.a);
    localTuSdkMediaSkinFaceEffect.setAtTimeRange(getAtTimeRange());
    localTuSdkMediaSkinFaceEffect.setIsApplied(false);
    if ((localTuSdkMediaSkinFaceEffect.getFilterWrap() != null) && (this.mFilterWrap != null)) {
      localTuSdkMediaSkinFaceEffect.getFilterWrap().getFilterParameter().syncArgs(this.mFilterWrap.getFilterParameter());
    }
    return localTuSdkMediaSkinFaceEffect;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaSkinFaceEffect.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */