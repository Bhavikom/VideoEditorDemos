package org.lasque.tusdk.core.api.engine;

import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract interface TuSdkEngineOrientation
{
  public abstract void release();
  
  public abstract void setInputSize(int paramInt1, int paramInt2);
  
  public abstract TuSdkSize getInputSize();
  
  public abstract CameraConfigs.CameraFacing getCameraFacing();
  
  public abstract void setCameraFacing(CameraConfigs.CameraFacing paramCameraFacing);
  
  public abstract void switchCameraFacing();
  
  public abstract InterfaceOrientation getInterfaceOrientation();
  
  public abstract void setInterfaceOrientation(InterfaceOrientation paramInterfaceOrientation);
  
  public abstract void setInputOrientation(ImageOrientation paramImageOrientation);
  
  public abstract void setOutputOrientation(ImageOrientation paramImageOrientation);
  
  public abstract void setHorizontallyMirrorFrontFacingCamera(boolean paramBoolean);
  
  public abstract void setHorizontallyMirrorRearFacingCamera(boolean paramBoolean);
  
  public abstract ImageOrientation getInputRotation();
  
  public abstract ImageOrientation getOutputOrientation();
  
  public abstract TuSdkSize getOutputSize();
  
  public abstract float getDeviceAngle();
  
  public abstract void setDeviceAngle(float paramFloat);
  
  public abstract InterfaceOrientation getDeviceOrient();
  
  public abstract void setDeviceOrient(InterfaceOrientation paramInterfaceOrientation);
  
  public abstract boolean isOriginalCaptureOrientation();
  
  public abstract void setOriginalCaptureOrientation(boolean paramBoolean);
  
  public abstract boolean isOutputCaptureMirrorEnabled();
  
  public abstract void setOutputCaptureMirrorEnabled(boolean paramBoolean);
  
  public abstract ImageOrientation getYuvOutputOrienation();
  
  public abstract void setYuvOutputOrienation(ImageOrientation paramImageOrientation);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\engine\TuSdkEngineOrientation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */