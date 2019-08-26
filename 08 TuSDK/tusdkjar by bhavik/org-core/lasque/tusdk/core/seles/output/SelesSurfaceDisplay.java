package org.lasque.tusdk.core.seles.output;

import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import org.lasque.tusdk.core.seles.sources.SelesWatermark;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract interface SelesSurfaceDisplay
  extends SelesContext.SelesInput
{
  public abstract void setEnabled(boolean paramBoolean);
  
  public abstract void setTextureCoordinateBuilder(SelesVerticeCoordinateBuilder paramSelesVerticeCoordinateBuilder);
  
  public abstract void setBackgroundColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  public abstract void setWatermark(SelesWatermark paramSelesWatermark);
  
  public abstract void destroy();
  
  public abstract void setPusherRotation(ImageOrientation paramImageOrientation, int paramInt);
  
  public abstract void newFrameReadyInGLThread(long paramLong);
  
  public abstract void duplicateFrameReadyInGLThread(long paramLong);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\output\SelesSurfaceDisplay.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */