package org.lasque.tusdk.core.seles.extend;

import org.lasque.tusdk.core.seles.output.SelesView.SelesFillModeType;
import org.lasque.tusdk.core.struct.TuSdkSize;

public abstract interface SelesVerticeCoordinateFillModeBuilder
  extends SelesVerticeCoordinateBuilder
{
  public abstract void setFillMode(SelesView.SelesFillModeType paramSelesFillModeType);
  
  public abstract void setOnDisplaySizeChangeListener(OnDisplaySizeChangeListener paramOnDisplaySizeChangeListener);
  
  public static abstract interface OnDisplaySizeChangeListener
  {
    public abstract void onDisplaySizeChanged(TuSdkSize paramTuSdkSize);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\extend\SelesVerticeCoordinateFillModeBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */