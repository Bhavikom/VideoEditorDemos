package org.lasque.tusdk.core.utils.hardware;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.media.MediaActionSound;
import android.util.Range;
import android.widget.RelativeLayout;
import java.util.List;
import org.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface.TuSdkAutoFocusCallback;
import org.lasque.tusdk.core.seles.sources.SelesStillCamera2;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

@TargetApi(21)
public class TuSdkStillCamera2
  extends SelesStillCamera2
  implements TuSdkStillCameraInterface
{
  private final TuSdkStillCameraAdapter<TuSdkStillCamera2> b;
  private MediaActionSound c;
  
  public TuSdkStillCameraAdapter<?> adapter()
  {
    return this.b;
  }
  
  public TuSdkStillCameraAdapter.CameraState getState()
  {
    return this.b.getState();
  }
  
  public float getRegionRatio()
  {
    return this.b.getRegionRatio();
  }
  
  public void setCameraListener(TuSdkStillCameraInterface.TuSdkStillCameraListener paramTuSdkStillCameraListener)
  {
    this.b.setCameraListener(paramTuSdkStillCameraListener);
  }
  
  private void a()
  {
    this.c = new MediaActionSound();
    this.c.load(0);
  }
  
  protected final void playSystemShutter()
  {
    if ((this.c == null) || (this.b.isDisableCaptureSound())) {
      return;
    }
    this.c.play(0);
  }
  
  public TuSdkStillCamera2(Context paramContext, CameraConfigs.CameraFacing paramCameraFacing, RelativeLayout paramRelativeLayout)
  {
    super(paramContext, paramCameraFacing);
    a();
    b();
    this.b = new TuSdkStillCameraAdapter(paramContext, paramRelativeLayout, this);
  }
  
  private void b()
  {
    setOutputImageOrientation(InterfaceOrientation.Portrait);
    setHorizontallyMirrorFrontFacingCamera(true);
  }
  
  protected void onDestroy()
  {
    this.b.onDestroy();
    if (this.c != null)
    {
      this.c.release();
      this.c = null;
    }
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
    if (getCameraDevice() != null) {
      this.b.stopCameraCapture();
    }
    super.stopCameraCapture();
  }
  
  protected void onCounfigPreview(CaptureRequest.Builder paramBuilder)
  {
    super.onCounfigPreview(paramBuilder);
    if (paramBuilder == null) {
      return;
    }
    Range localRange = (Range)paramBuilder.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
    if (localRange != null) {
      this.b.setRendererFPS(((Integer)localRange.getUpper()).intValue());
    }
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
    if ((!canSupportAutoFocus()) || (!this.b.isCreatedSurface()))
    {
      if (paramTuSdkAutoFocusCallback != null) {
        paramTuSdkAutoFocusCallback.onAutoFocus(false, this);
      }
      return;
    }
    super.autoFocus(paramTuSdkAutoFocusCallback);
  }
  
  public void captureImage()
  {
    if (getCameraDevice() == null) {
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
    return computerOutputOrientation(getCameraCharacter(), localInterfaceOrientation, isHorizontallyMirrorRearFacingCamera(), bool, getOutputImageOrientation());
  }
  
  public void setPreviewRatio(float paramFloat) {}
  
  public void setOutputPictureRatio(float paramFloat) {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\hardware\TuSdkStillCamera2.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */