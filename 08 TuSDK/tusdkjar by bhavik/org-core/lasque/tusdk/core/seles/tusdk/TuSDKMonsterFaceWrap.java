package org.lasque.tusdk.core.seles.tusdk;

import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.SelesParameters.FilterFacePositionInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMonsterFace;
import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMonsterNoseFallFace;
import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMonsterSnakeFace;
import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMonsterSquareFace;

public class TuSDKMonsterFaceWrap
  extends FilterWrap
  implements SelesParameters.FilterFacePositionInterface
{
  private TuSDKMonsterFaceType a;
  
  public TuSDKMonsterFaceWrap(TuSDKMonsterFaceType paramTuSDKMonsterFaceType)
  {
    this.a = TuSDKMonsterFaceType.TuSDKMonsterFaceTypeBigNose;
    this.mFilter = (this.mLastFilter = a(paramTuSDKMonsterFaceType));
  }
  
  private TuSDKMonsterFaceWrap() {}
  
  private SelesFilter a(TuSDKMonsterFaceType paramTuSDKMonsterFaceType)
  {
    switch (1.a[paramTuSDKMonsterFaceType.ordinal()])
    {
    case 1: 
      return new TuSDKMonsterNoseFallFace();
    case 2: 
      localObject = new TuSDKMonsterSnakeFace();
      ((TuSDKMonsterSnakeFace)localObject).setMonsterFaceType(2);
      return (SelesFilter)localObject;
    case 3: 
      localObject = new TuSDKMonsterSnakeFace();
      ((TuSDKMonsterSnakeFace)localObject).setMonsterFaceType(1);
      return (SelesFilter)localObject;
    case 4: 
      return new TuSDKMonsterSquareFace();
    case 5: 
      localObject = new TuSDKMonsterFace();
      ((TuSDKMonsterFace)localObject).setMonsterFaceType(1);
      return (SelesFilter)localObject;
    case 6: 
      localObject = new TuSDKMonsterFace();
      ((TuSDKMonsterFace)localObject).setMonsterFaceType(3);
      return (SelesFilter)localObject;
    }
    Object localObject = new TuSDKMonsterFace();
    ((TuSDKMonsterFace)localObject).setMonsterFaceType(2);
    return (SelesFilter)localObject;
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
    TuSDKMonsterFaceWrap localTuSDKMonsterFaceWrap = new TuSDKMonsterFaceWrap(this.a);
    return localTuSDKMonsterFaceWrap;
  }
  
  public static enum TuSDKMonsterFaceType
  {
    private TuSDKMonsterFaceType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\TuSDKMonsterFaceWrap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */