package org.lasque.tusdk.core.seles.sources;

import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract interface SelesSurfaceHolder
{
  public abstract void setInputSize(TuSdkSize paramTuSdkSize);
  
  public abstract void setInputRotation(ImageOrientation paramImageOrientation);
  
  public abstract void setTextureCoordinateBuilder(SelesVerticeCoordinateCorpBuilder paramSelesVerticeCoordinateCorpBuilder);
  
  public abstract void setPreCropRect(RectF paramRectF);
  
  public abstract boolean isInited();
  
  public abstract SurfaceTexture requestSurfaceTexture();
  
  public abstract long getSurfaceTexTimestampNs();
  
  public abstract void setSurfaceTexTimestampNs(long paramLong);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesSurfaceHolder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */