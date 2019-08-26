package org.lasque.tusdk.video.editor;

import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.TuSDKPlasticFaceWrap;

public class TuSdkMediaPlasticFaceEffect
  extends TuSdkMediaEffectData
{
  public TuSdkMediaPlasticFaceEffect()
  {
    setMediaEffectType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypePlasticFace);
    setVaild(true);
  }
  
  public synchronized FilterWrap getFilterWrap()
  {
    if (this.mFilterWrap == null)
    {
      this.mFilterWrap = new TuSDKPlasticFaceWrap();
      this.mFilterWrap.processImage();
    }
    return this.mFilterWrap;
  }
  
  public TuSdkMediaEffectData clone()
  {
    if (!isVaild()) {
      return null;
    }
    TuSdkMediaPlasticFaceEffect localTuSdkMediaPlasticFaceEffect = new TuSdkMediaPlasticFaceEffect();
    localTuSdkMediaPlasticFaceEffect.setAtTimeRange(getAtTimeRange());
    localTuSdkMediaPlasticFaceEffect.setVaild(true);
    localTuSdkMediaPlasticFaceEffect.setMediaEffectType(getMediaEffectType());
    localTuSdkMediaPlasticFaceEffect.setIsApplied(false);
    localTuSdkMediaPlasticFaceEffect.mFilterWrap = getFilterWrap().clone();
    if ((localTuSdkMediaPlasticFaceEffect.getFilterWrap() != null) && (this.mFilterWrap != null)) {
      localTuSdkMediaPlasticFaceEffect.getFilterWrap().getFilter().setParameter(this.mFilterWrap.getFilter().getParameter());
    }
    return localTuSdkMediaPlasticFaceEffect;
  }
  
  public void setEyeSize(float paramFloat)
  {
    submitParameter("eyeSize", paramFloat);
  }
  
  public void setChinSize(float paramFloat)
  {
    submitParameter("chinSize", paramFloat);
  }
  
  public void setNoseSize(float paramFloat)
  {
    submitParameter("noseSize", paramFloat);
  }
  
  public void setMouthWidth(float paramFloat)
  {
    submitParameter("mouthWidth", paramFloat);
  }
  
  public void setArchEyebrow(float paramFloat)
  {
    submitParameter("archEyebrow", paramFloat);
  }
  
  public void setEyeDis(float paramFloat)
  {
    submitParameter("eyeDis", paramFloat);
  }
  
  public void setEyeAngle(float paramFloat)
  {
    submitParameter("eyeAngle", paramFloat);
  }
  
  public void setJawSize(float paramFloat)
  {
    submitParameter("jawSize", paramFloat);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaPlasticFaceEffect.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */