package org.lasque.tusdk.core.seles.sources;

import android.graphics.Bitmap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.WaterMarkPosition;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract interface SelesWatermark
{
  public abstract void setWaterPostion(TuSdkWaterMarkOption.WaterMarkPosition paramWaterMarkPosition);
  
  public abstract void setScale(float paramFloat);
  
  public abstract void setPadding(float paramFloat);
  
  public abstract void setImage(Bitmap paramBitmap, boolean paramBoolean);
  
  public abstract void drawInGLThread(long paramLong, TuSdkSize paramTuSdkSize, ImageOrientation paramImageOrientation);
  
  public abstract void destroy();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesWatermark.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */