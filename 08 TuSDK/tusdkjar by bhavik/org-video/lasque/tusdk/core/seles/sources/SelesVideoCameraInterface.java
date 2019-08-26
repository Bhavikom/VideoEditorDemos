package org.lasque.tusdk.core.seles.sources;

import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter.CameraState;

public abstract interface SelesVideoCameraInterface
  extends SelesBaseCameraInterface
{
  public abstract TuSdkStillCameraAdapter.CameraState getState();
  
  public abstract void setRendererFPS(int paramInt);
  
  public abstract void switchFilter(String paramString);
  
  public abstract int getDeviceAngle();
  
  public abstract void updateFaceFeatures(FaceAligment[] paramArrayOfFaceAligment, float paramFloat);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\sources\SelesVideoCameraInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */