package org.lasque.tusdk.core.seles.tusdk;

import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinNaturalFilter;

public final class TuSDKSkinNaturalWrap
  extends FilterWrap
{
  public TuSDKSkinNaturalWrap()
  {
    this.mFilter = (this.mLastFilter = new TuSDKSkinNaturalFilter());
  }
  
  public static TuSDKSkinNaturalWrap creat()
  {
    TuSDKSkinNaturalWrap localTuSDKSkinNaturalWrap = new TuSDKSkinNaturalWrap();
    return localTuSDKSkinNaturalWrap;
  }
  
  protected void changeOption(FilterOption paramFilterOption) {}
  
  public FilterWrap clone()
  {
    TuSDKSkinNaturalWrap localTuSDKSkinNaturalWrap = new TuSDKSkinNaturalWrap();
    localTuSDKSkinNaturalWrap.mFilter.setParameter(this.mFilter.getParameter());
    return localTuSDKSkinNaturalWrap;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\TuSDKSkinNaturalWrap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */