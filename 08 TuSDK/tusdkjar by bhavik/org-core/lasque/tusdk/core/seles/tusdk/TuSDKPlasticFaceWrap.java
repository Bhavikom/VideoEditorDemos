package org.lasque.tusdk.core.seles.tusdk;

import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.SelesParameters.FilterFacePositionInterface;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSdkPlasticFace;

public class TuSDKPlasticFaceWrap
  extends FilterWrap
  implements SelesParameters.FilterFacePositionInterface
{
  public TuSDKPlasticFaceWrap()
  {
    this.mFilter = (this.mLastFilter = new TuSdkPlasticFace());
  }
  
  public static TuSDKPlasticFaceWrap creat()
  {
    TuSDKPlasticFaceWrap localTuSDKPlasticFaceWrap = new TuSDKPlasticFaceWrap();
    return localTuSDKPlasticFaceWrap;
  }
  
  protected void changeOption(FilterOption paramFilterOption) {}
  
  public void updateFaceFeatures(FaceAligment[] paramArrayOfFaceAligment, float paramFloat)
  {
    if ((this.mFilter == null) || (!(this.mFilter instanceof SelesParameters.FilterFacePositionInterface))) {
      return;
    }
    ((SelesParameters.FilterFacePositionInterface)this.mFilter).updateFaceFeatures(paramArrayOfFaceAligment, paramFloat);
  }
  
  public FilterWrap clone()
  {
    TuSDKPlasticFaceWrap localTuSDKPlasticFaceWrap = new TuSDKPlasticFaceWrap();
    localTuSDKPlasticFaceWrap.mFilter.setParameter(this.mFilter.getParameter());
    return localTuSDKPlasticFaceWrap;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\TuSDKPlasticFaceWrap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */