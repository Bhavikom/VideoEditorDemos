package org.lasque.tusdk.core.seles.sources;

import android.graphics.SurfaceTexture;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract interface SelesVideoCamera2Engine
{
  public abstract boolean canInitCamera();
  
  public abstract boolean onInitCamera();
  
  public abstract TuSdkSize previewOptimalSize();
  
  public abstract void onCameraWillOpen(SurfaceTexture paramSurfaceTexture);
  
  public abstract void onCameraStarted();
  
  public abstract ImageOrientation previewOrientation();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesVideoCamera2Engine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */