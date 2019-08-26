package org.lasque.tusdk.core.seles.tusdk;

import android.graphics.PointF;
import org.lasque.tusdk.core.seles.tusdk.particle.TuSDKParticleFilterInterface;
import org.lasque.tusdk.core.utils.TLog;

public class TuSDK2DParticleFilterWrap
  extends FilterWrap
{
  public static TuSDK2DParticleFilterWrap creat(FilterOption paramFilterOption)
  {
    if (paramFilterOption == null)
    {
      TLog.e("Can not found FilterOption", new Object[0]);
      return null;
    }
    return new TuSDK2DParticleFilterWrap(paramFilterOption);
  }
  
  protected TuSDK2DParticleFilterWrap(FilterOption paramFilterOption)
  {
    changeOption(paramFilterOption);
  }
  
  public boolean hasParticleFilter()
  {
    return (this.mFilter != null) && ((this.mFilter instanceof TuSDKParticleFilterInterface));
  }
  
  public void updateParticleEmitPosition(PointF paramPointF)
  {
    if (!hasParticleFilter()) {
      return;
    }
    ((TuSDKParticleFilterInterface)this.mFilter).updateParticleEmitPosition(paramPointF);
  }
  
  public void setParticleSize(float paramFloat)
  {
    if (!hasParticleFilter()) {
      return;
    }
    ((TuSDKParticleFilterInterface)this.mFilter).setParticleSize(paramFloat);
  }
  
  public void setParticleColor(int paramInt)
  {
    if (!hasParticleFilter()) {
      return;
    }
    ((TuSDKParticleFilterInterface)this.mFilter).setParticleColor(paramInt);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\TuSDK2DParticleFilterWrap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */