package org.lasque.tusdk.core.seles.tusdk;

import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinMoistFilter;

public final class TuSDKSkinMoistWrap
  extends FilterWrap
{
  public TuSDKSkinMoistWrap()
  {
    this.mFilter = (this.mLastFilter = new TuSDKSkinMoistFilter());
  }
  
  public static TuSDKSkinMoistWrap creat()
  {
    TuSDKSkinMoistWrap localTuSDKSkinMoistWrap = new TuSDKSkinMoistWrap();
    return localTuSDKSkinMoistWrap;
  }
  
  protected void changeOption(FilterOption paramFilterOption) {}
  
  public FilterWrap clone()
  {
    TuSDKSkinMoistWrap localTuSDKSkinMoistWrap = new TuSDKSkinMoistWrap();
    localTuSDKSkinMoistWrap.mFilter.setParameter(this.mFilter.getParameter());
    return localTuSDKSkinMoistWrap;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\TuSDKSkinMoistWrap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */