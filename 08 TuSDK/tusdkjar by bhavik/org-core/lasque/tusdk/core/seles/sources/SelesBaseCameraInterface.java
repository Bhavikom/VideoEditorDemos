package org.lasque.tusdk.core.seles.sources;

import android.graphics.PointF;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraAntibanding;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraAutoFocus;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFlash;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract interface SelesBaseCameraInterface
{
  public abstract boolean isCapturing();
  
  public abstract boolean isCapturePaused();
  
  public abstract ImageOrientation capturePhotoOrientation();
  
  public abstract long getLastFocusTime();
  
  public abstract TuSdkSize getOutputSize();
  
  public abstract boolean isFrontFacingCameraPresent();
  
  public abstract boolean isBackFacingCameraPresent();
  
  public abstract void captureImage();
  
  public abstract void startCameraCapture();
  
  public abstract void stopCameraCapture();
  
  public abstract void pauseCameraCapture();
  
  public abstract void resumeCameraCapture();
  
  public abstract void destroy();
  
  public abstract void rotateCamera();
  
  public abstract void setFlashMode(CameraConfigs.CameraFlash paramCameraFlash);
  
  public abstract CameraConfigs.CameraFlash getFlashMode();
  
  public abstract void setAntibandingMode(CameraConfigs.CameraAntibanding paramCameraAntibanding);
  
  public abstract CameraConfigs.CameraAntibanding getAntiBandingMode();
  
  public abstract boolean canSupportFlash();
  
  public abstract void setOutputSize(TuSdkSize paramTuSdkSize);
  
  public abstract void setPreviewMaxSize(int paramInt);
  
  public abstract void setPreviewEffectScale(float paramFloat);
  
  public abstract void setUnifiedParameters(boolean paramBoolean);
  
  public abstract void setDisableMirrorFrontFacing(boolean paramBoolean);
  
  public abstract void setEnableFaceTrace(boolean paramBoolean);
  
  public abstract void autoMetering(PointF paramPointF);
  
  public abstract boolean canSupportAutoFocus();
  
  public abstract void cancelAutoFocus();
  
  public abstract void autoFocus(CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF, TuSdkAutoFocusCallback paramTuSdkAutoFocusCallback);
  
  public abstract void autoFocus(TuSdkAutoFocusCallback paramTuSdkAutoFocusCallback);
  
  public abstract void addTarget(SelesContext.SelesInput paramSelesInput);
  
  public abstract void addTarget(SelesContext.SelesInput paramSelesInput, int paramInt);
  
  public abstract void removeTarget(SelesContext.SelesInput paramSelesInput);
  
  public static abstract interface TuSdkAutoFocusCallback
  {
    public abstract void onAutoFocus(boolean paramBoolean, SelesBaseCameraInterface paramSelesBaseCameraInterface);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesBaseCameraInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */