package org.lasque.tusdk.core.media.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;

public abstract interface TuSdkCameraBuilder
{
  public abstract Camera.CameraInfo getInfo();
  
  public abstract Camera getOrginCamera();
  
  public abstract Camera.Parameters getParameters();
  
  public abstract CameraConfigs.CameraFacing getFacing();
  
  public abstract boolean isBackFacingCameraPresent();
  
  public abstract boolean open(CameraConfigs.CameraFacing paramCameraFacing);
  
  public abstract boolean open();
  
  public abstract void releaseCamera();
  
  public abstract boolean startPreview();
  
  public abstract void setPreviewTexture(SurfaceTexture paramSurfaceTexture);
  
  public abstract void setPreviewCallbackWithBuffer(Camera.PreviewCallback paramPreviewCallback);
  
  public abstract void addCallbackBuffer(byte[] paramArrayOfByte);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\camera\TuSdkCameraBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */