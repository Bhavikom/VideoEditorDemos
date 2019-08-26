package org.lasque.tusdk.core.seles.tusdk;

import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFadeInFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFlashLightFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFlipFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFlyInFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFocusFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLivePullInFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLivePullInFilter.PullInDirection;
import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveSpreadInFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveStackUpFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveZoomFilter;

public class TuSDKMediaTransitionWrap
  extends FilterWrap
  implements SelesParameters.FilterParameterInterface
{
  private TuSDKMediaTransitionType a;
  
  public SelesParameters getParameter()
  {
    return this.mFilter.getParameter();
  }
  
  public void setParameter(SelesParameters paramSelesParameters) {}
  
  public void submitParameter()
  {
    this.mFilter.submitParameter();
  }
  
  public TuSDKMediaTransitionWrap(TuSDKMediaTransitionType paramTuSDKMediaTransitionType)
  {
    this.a = paramTuSDKMediaTransitionType;
    this.mFilter = (this.mLastFilter = a(paramTuSDKMediaTransitionType));
  }
  
  private SelesOutInput a(TuSDKMediaTransitionType paramTuSDKMediaTransitionType)
  {
    switch (1.a[paramTuSDKMediaTransitionType.ordinal()])
    {
    case 1: 
      return new TuSDKLiveFadeInFilter();
    case 2: 
      return new TuSDKLiveFlyInFilter();
    case 3: 
      return new TuSDKLivePullInFilter(TuSDKLivePullInFilter.PullInDirection.RIGHT);
    case 4: 
      return new TuSDKLivePullInFilter(TuSDKLivePullInFilter.PullInDirection.LEFT);
    case 5: 
      return new TuSDKLivePullInFilter(TuSDKLivePullInFilter.PullInDirection.TOP);
    case 6: 
      return new TuSDKLivePullInFilter(TuSDKLivePullInFilter.PullInDirection.BOTTOM);
    case 7: 
      return new TuSDKLiveSpreadInFilter();
    case 8: 
      return new TuSDKLiveFlashLightFilter();
    case 9: 
      return new TuSDKLiveFlipFilter();
    case 10: 
      return new TuSDKLiveFocusFilter(0.0F);
    case 11: 
      return new TuSDKLiveFocusFilter(1.0F);
    case 12: 
      return new TuSDKLiveStackUpFilter();
    case 13: 
      return new TuSDKLiveZoomFilter();
    }
    return null;
  }
  
  public FilterWrap clone()
  {
    TuSDKMediaTransitionWrap localTuSDKMediaTransitionWrap = new TuSDKMediaTransitionWrap(this.a);
    return localTuSDKMediaTransitionWrap;
  }
  
  public static enum TuSDKMediaTransitionType
  {
    private TuSDKMediaTransitionType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\TuSDKMediaTransitionWrap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */