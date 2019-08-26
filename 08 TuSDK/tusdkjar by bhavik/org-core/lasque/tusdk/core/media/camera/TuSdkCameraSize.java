package org.lasque.tusdk.core.media.camera;

import org.lasque.tusdk.core.struct.TuSdkSize;

public abstract interface TuSdkCameraSize
{
  public abstract void configure(TuSdkCameraBuilder paramTuSdkCameraBuilder);
  
  public abstract void changeStatus(TuSdkCamera.TuSdkCameraStatus paramTuSdkCameraStatus);
  
  public abstract TuSdkSize previewOptimalSize();
  
  public abstract int previewBufferLength();
  
  public abstract TuSdkSize getOutputSize();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\camera\TuSdkCameraSize.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */