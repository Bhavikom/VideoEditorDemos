package org.lasque.tusdk.core.media.camera;

import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.hardware.Camera.PreviewCallback;
import android.opengl.GLSurfaceView.Renderer;
import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;

public abstract interface TuSdkCamera
  extends TuSdkRecordSurface
{
  public abstract TuSdkCameraStatus getCameraStatus();
  
  public abstract void setCameraListener(TuSdkCameraListener paramTuSdkCameraListener);
  
  public abstract void setPreviewCallback(Camera.PreviewCallback paramPreviewCallback);
  
  public abstract void setSurfaceListener(SurfaceTexture.OnFrameAvailableListener paramOnFrameAvailableListener);
  
  public abstract void setCameraBuilder(TuSdkCameraBuilder paramTuSdkCameraBuilder);
  
  public abstract void setCameraParameters(TuSdkCameraParameters paramTuSdkCameraParameters);
  
  public abstract void setCameraOrientation(TuSdkCameraOrientation paramTuSdkCameraOrientation);
  
  public abstract void setCameraFocus(TuSdkCameraFocus paramTuSdkCameraFocus);
  
  public abstract void setCameraSize(TuSdkCameraSize paramTuSdkCameraSize);
  
  public abstract void setCameraShot(TuSdkCameraShot paramTuSdkCameraShot);
  
  public abstract boolean prepare();
  
  public abstract boolean rotateCamera();
  
  public abstract CameraConfigs.CameraFacing getFacing();
  
  public abstract boolean startPreview();
  
  public abstract boolean startPreview(CameraConfigs.CameraFacing paramCameraFacing);
  
  public abstract boolean pausePreview();
  
  public abstract boolean resumePreview();
  
  public abstract boolean shotPhoto();
  
  public abstract void stopPreview();
  
  public abstract long newFrameReadyInGLThread();
  
  public abstract GLSurfaceView.Renderer getExtenalRenderer();
  
  public abstract void release();
  
  public static abstract interface TuSdkCameraListener
  {
    public abstract void onStatusChanged(TuSdkCamera.TuSdkCameraStatus paramTuSdkCameraStatus, TuSdkCamera paramTuSdkCamera);
  }
  
  public static enum TuSdkCameraStatus
  {
    private TuSdkCameraStatus() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\camera\TuSdkCamera.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */