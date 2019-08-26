package org.lasque.tusdk.core.seles.extend;

import android.graphics.RectF;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract interface SelesVerticeCoordinateBuilder
{
  public abstract TuSdkSize outputSize();
  
  public abstract void setOutputSize(TuSdkSize paramTuSdkSize);
  
  public abstract void setCanvasRect(RectF paramRectF);
  
  public abstract boolean calculate(TuSdkSize paramTuSdkSize, ImageOrientation paramImageOrientation, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\extend\SelesVerticeCoordinateBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */