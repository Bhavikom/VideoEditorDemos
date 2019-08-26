package org.lasque.tusdk.core.seles.tusdk.particle;

import android.graphics.PointF;

public abstract interface TuSDKParticleFilterInterface
{
  public abstract void updateParticleEmitPosition(PointF paramPointF);
  
  public abstract void setParticleSize(float paramFloat);
  
  public abstract void setParticleColor(int paramInt);
  
  public abstract void setActive(boolean paramBoolean);
  
  public abstract void reset();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\particle\TuSDKParticleFilterInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */