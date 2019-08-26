package org.lasque.tusdk.core.seles.tusdk;

import java.util.List;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesParameters.TileStickerInterface;
import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDK2DImageFilter;
import org.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;

public class TuSDK2DImageFilterWrap
  extends FilterWrap
  implements SelesParameters.TileStickerInterface
{
  private TuSDK2DImageFilter a = new TuSDK2DImageFilter();
  
  public static TuSDK2DImageFilterWrap creat()
  {
    TuSDK2DImageFilterWrap localTuSDK2DImageFilterWrap = new TuSDK2DImageFilterWrap();
    return localTuSDK2DImageFilterWrap;
  }
  
  protected TuSDK2DImageFilterWrap()
  {
    FilterOption localFilterOption = FilterLocalPackage.shared().option("Normal");
    changeOption(localFilterOption);
    a();
  }
  
  private void a()
  {
    this.mLastFilter = (this.mFilter = this.a);
  }
  
  public void addTarget(SelesContext.SelesInput paramSelesInput, int paramInt)
  {
    super.addTarget(paramSelesInput, paramInt);
  }
  
  public void updateTileStickers(List<TuSdkImage2DSticker> paramList)
  {
    this.a.updateStickers(paramList);
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof FilterWrap))) {
      return false;
    }
    return paramObject == this;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\TuSDK2DImageFilterWrap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */