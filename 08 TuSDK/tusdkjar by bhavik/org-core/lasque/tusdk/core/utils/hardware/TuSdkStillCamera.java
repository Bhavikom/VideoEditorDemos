package org.lasque.tusdk.core.utils.hardware;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.ShutterCallback;
import android.widget.RelativeLayout;
import java.util.List;
import org.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
import org.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface.TuSdkAutoFocusCallback;
import org.lasque.tusdk.core.seles.sources.SelesStillCamera;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkStillCamera
  extends SelesStillCamera
  implements TuSdkStillCameraInterface
{
  private final TuSdkStillCameraAdapter<TuSdkStillCamera> b;
  private Camera.ShutterCallback c = new Camera.ShutterCallback()
  {
    public void onShutter() {}
  };
  
  public TuSdkStillCameraAdapter<?> adapter()
  {
    return this.b;
  }
  
  public TuSdkStillCameraAdapter.CameraState getState()
  {
    return this.b.getState();
  }
  
  public void setCameraListener(TuSdkStillCameraInterface.TuSdkStillCameraListener paramTuSdkStillCameraListener)
  {
    this.b.setCameraListener(paramTuSdkStillCameraListener);
  }
  
  public float getRegionRatio()
  {
    return this.b.getRegionRatio();
  }
  
  public TuSdkStillCamera(Context paramContext, CameraConfigs.CameraFacing paramCameraFacing, RelativeLayout paramRelativeLayout)
  {
    super(paramContext, paramCameraFacing);
    a();
    this.b = new TuSdkStillCameraAdapter(paramContext, paramRelativeLayout, this);
  }
  
  private void a()
  {
    setOutputImageOrientation(InterfaceOrientation.Portrait);
    setHorizontallyMirrorFrontFacingCamera(true);
  }
  
  protected void onDestroy()
  {
    this.b.onDestroy();
    super.onDestroy();
  }
  
  public void pauseCameraCapture()
  {
    super.pauseCameraCapture();
    this.b.pauseCameraCapture();
  }
  
  public void resumeCameraCapture()
  {
    super.resumeCameraCapture();
    this.b.resumeCameraCapture();
  }
  
  protected void onMainThreadStart()
  {
    super.onMainThreadStart();
    this.b.onMainThreadStart();
  }
  
  public void stopCameraCapture()
  {
    if (inputCamera() != null) {
      this.b.stopCameraCapture();
    }
    super.stopCameraCapture();
  }
  
  protected void onInitConfig(Camera paramCamera)
  {
    if (paramCamera == null) {
      return;
    }
    super.onInitConfig(paramCamera);
    Camera.Parameters localParameters = paramCamera.getParameters();
    if (localParameters == null) {
      return;
    }
    int[] arrayOfInt = new int[2];
    localParameters.getPreviewFpsRange(arrayOfInt);
    this.b.setRendererFPS(Math.max(arrayOfInt[0] / 1000, arrayOfInt[1] / 1000));
  }
  
  protected void onCameraStarted()
  {
    super.onCameraStarted();
    this.b.onCameraStarted();
  }
  
  public void onCameraFaceDetection(List<TuSdkFace> paramList, TuSdkSize paramTuSdkSize)
  {
    super.onCameraFaceDetection(paramList, paramTuSdkSize);
    this.b.onCameraFaceDetection(paramList, paramTuSdkSize);
  }
  
  public void switchFilter(String paramString)
  {
    Runnable localRunnable = this.b.switchFilter(paramString);
    if (localRunnable == null) {
      return;
    }
    runOnDraw(localRunnable);
  }
  
  public void autoFocus(SelesBaseCameraInterface.TuSdkAutoFocusCallback paramTuSdkAutoFocusCallback)
  {
    if ((inputCamera() == null) || (!canSupportAutoFocus()) || (!this.b.isCreatedSurface()))
    {
      if (paramTuSdkAutoFocusCallback != null) {
        paramTuSdkAutoFocusCallback.onAutoFocus(false, this);
      }
      return;
    }
    super.autoFocus(paramTuSdkAutoFocusCallback);
  }
  
  protected Camera.ShutterCallback getShutterCallback()
  {
    if (this.b.isDisableCaptureSound()) {
      return null;
    }
    return this.c;
  }
  
  public void captureImage()
  {
    if (inputCamera() == null) {
      return;
    }
    this.b.captureImage();
  }
  
  protected void onTakePictured(byte[] paramArrayOfByte)
  {
    super.onTakePictured(paramArrayOfByte);
    this.b.onTakePictured(paramArrayOfByte);
  }
  
  protected Bitmap decodeToBitmapAtAsync(byte[] paramArrayOfByte)
  {
    Bitmap localBitmap = super.decodeToBitmapAtAsync(paramArrayOfByte);
    return this.b.decodeToBitmapAtAsync(paramArrayOfByte, localBitmap);
  }
  
  public ImageOrientation capturePhotoOrientation()
  {
    boolean bool = (isHorizontallyMirrorFrontFacingCamera()) && (!isDisableMirrorFrontFacing());
    InterfaceOrientation localInterfaceOrientation = this.b.getDeviceOrient();
    if (((isFrontFacingCameraPresent()) && (!bool)) || ((isBackFacingCameraPresent()) && (isHorizontallyMirrorRearFacingCamera()))) {
      localInterfaceOrientation = InterfaceOrientation.getWithDegrees(this.b.getDeviceOrient().viewDegree());
    }
    return TuSdkCameraOrientationImpl.computerOutputOrientation(inputCameraInfo(), localInterfaceOrientation, isHorizontallyMirrorRearFacingCamera(), bool, getOutputImageOrientation());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\hardware\TuSdkStillCamera.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */