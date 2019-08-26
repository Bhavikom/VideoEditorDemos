package org.lasque.tusdk.core.seles.extend;

import android.graphics.RectF;
import org.lasque.tusdk.core.struct.TuSdkSize;

public abstract interface SelesVerticeCoordinateCorpBuilder
  extends SelesVerticeCoordinateBuilder
{
  public abstract void setCropRect(RectF paramRectF);
  
  public abstract void setPreCropRect(RectF paramRectF);
  
  public abstract void setEnableClip(boolean paramBoolean);
  
  public abstract TuSdkSize setOutputRatio(float paramFloat);
  
  public abstract float getOutputRatio(float paramFloat);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\extend\SelesVerticeCoordinateCorpBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */