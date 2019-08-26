package org.lasque.tusdk.core.media.camera;

import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract interface TuSdkCameraOrientation
{
  public abstract void configure(TuSdkCameraBuilder paramTuSdkCameraBuilder);
  
  public abstract void changeStatus(TuSdkCamera.TuSdkCameraStatus paramTuSdkCameraStatus);
  
  public abstract ImageOrientation previewOrientation();
  
  public abstract ImageOrientation captureOrientation();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\camera\TuSdkCameraOrientation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */