package org.lasque.tusdk.core.seles.sources;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.Parameters;
import android.os.Build.VERSION;
import java.io.IOException;
import java.util.List;
import org.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.hardware.TuSdkFace;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesVideoCamera
  extends SelesVideoCameraBase
{
  private Camera.CameraInfo b;
  private boolean c;
  private CameraConfigs.CameraFacing d;
  private boolean e;
  private boolean f;
  private SelesVideoCameraEngine g = new SelesVideoCameraEngine()
  {
    public boolean canInitCamera()
    {
      SelesVideoCamera.a(SelesVideoCamera.this, CameraHelper.firstCameraInfo(SelesVideoCamera.a(SelesVideoCamera.this)));
      if (SelesVideoCamera.b(SelesVideoCamera.this) == null)
      {
        TLog.e("The device can not find any camera info: %s", new Object[] { SelesVideoCamera.b(SelesVideoCamera.this) });
        return false;
      }
      return true;
    }
    
    public Camera onInitCamera()
    {
      if (SelesVideoCamera.b(SelesVideoCamera.this) == null) {
        return null;
      }
      Camera localCamera = CameraHelper.getCamera(SelesVideoCamera.b(SelesVideoCamera.this));
      if (localCamera == null)
      {
        Camera.CameraInfo localCameraInfo = new Camera.CameraInfo();
        int i = Camera.getNumberOfCameras();
        for (int j = 0; j < i; j++)
        {
          Camera.getCameraInfo(j, localCameraInfo);
          if (localCameraInfo.facing == 1)
          {
            localCamera = Camera.open(j);
            break;
          }
        }
        if (localCamera == null)
        {
          TLog.e("No front-facing camera found; opening default", new Object[0]);
          localCamera = Camera.open();
        }
      }
      if (localCamera == null)
      {
        TLog.e("The device can not find init camera: %s", new Object[] { SelesVideoCamera.b(SelesVideoCamera.this) });
        return null;
      }
      SelesVideoCamera.this.onInitConfig(localCamera);
      return localCamera;
    }
    
    public TuSdkSize previewOptimalSize()
    {
      if (SelesVideoCamera.this.inputCamera() == null) {
        return null;
      }
      return CameraHelper.createSize(SelesVideoCamera.this.inputCamera().getParameters().getPreviewSize());
    }
    
    public void onCameraWillOpen(SurfaceTexture paramAnonymousSurfaceTexture)
    {
      if (SelesVideoCamera.this.inputCamera() == null) {
        return;
      }
      try
      {
        SelesVideoCamera.this.inputCamera().setPreviewTexture(paramAnonymousSurfaceTexture);
      }
      catch (IOException localIOException)
      {
        TLog.e(localIOException, "onCameraWillOpen", new Object[0]);
      }
    }
    
    public void onCameraStarted() {}
    
    public ImageOrientation previewOrientation()
    {
      return SelesVideoCamera.this.computerOutputOrientation();
    }
  };
  
  public Camera.CameraInfo inputCameraInfo()
  {
    return this.b;
  }
  
  public Camera.Parameters inputCameraParameters()
  {
    if (inputCamera() == null) {
      return null;
    }
    return inputCamera().getParameters();
  }
  
  public CameraConfigs.CameraFacing cameraPosition()
  {
    Camera.CameraInfo localCameraInfo = inputCameraInfo();
    return CameraHelper.cameraPosition(localCameraInfo);
  }
  
  public boolean isFrontFacingCameraPresent()
  {
    return cameraPosition() == CameraConfigs.CameraFacing.Front;
  }
  
  public boolean isBackFacingCameraPresent()
  {
    return cameraPosition() == CameraConfigs.CameraFacing.Back;
  }
  
  public boolean isEnableFaceTrace()
  {
    return _isEnableFaceTrace();
  }
  
  public void setEnableFaceTrace(boolean paramBoolean)
  {
    this.c = paramBoolean;
    if (paramBoolean) {
      a();
    } else {
      b();
    }
  }
  
  public boolean _isEnableFaceTrace()
  {
    return this.c;
  }
  
  public boolean isPreviewStarted()
  {
    return this.e;
  }
  
  public SelesVideoCamera(Context paramContext, CameraConfigs.CameraFacing paramCameraFacing)
  {
    super(paramContext);
    this.d = (paramCameraFacing == null ? CameraConfigs.CameraFacing.Back : paramCameraFacing);
    super.setCameraEngine(this.g);
  }
  
  @Deprecated
  public void setCameraEngine(SelesVideoCameraEngine paramSelesVideoCameraEngine) {}
  
  protected void onPreviewStarted()
  {
    super.onPreviewStarted();
    this.e = true;
    a();
  }
  
  public void stopCameraCapture()
  {
    super.stopCameraCapture();
    b();
    this.e = false;
    this.f = false;
    this.b = null;
  }
  
  protected void onInitConfig(Camera paramCamera) {}
  
  protected void onCameraStarted()
  {
    super.onCameraStarted();
  }
  
  private void a()
  {
    if ((Build.VERSION.SDK_INT > 13) && (_isEnableFaceTrace())) {
      c();
    }
  }
  
  private void b()
  {
    if (Build.VERSION.SDK_INT > 13) {
      d();
    }
  }
  
  @TargetApi(14)
  private void c()
  {
    if ((inputCamera() == null) || (!isPreviewStarted()) || (this.f) || (!CameraHelper.canSupportFaceDetection(inputCamera().getParameters()))) {
      return;
    }
    d();
    this.f = true;
    inputCamera().setFaceDetectionListener(new Camera.FaceDetectionListener()
    {
      public void onFaceDetection(Camera.Face[] paramAnonymousArrayOfFace, Camera paramAnonymousCamera)
      {
        List localList = CameraHelper.transforFaces(paramAnonymousArrayOfFace, SelesVideoCamera.this.mOutputRotation);
        SelesVideoCamera.this.onCameraFaceDetection(localList, SelesVideoCamera.this.mInputTextureSize.transforOrientation(SelesVideoCamera.this.mOutputRotation));
      }
    });
    try
    {
      inputCamera().startFaceDetection();
    }
    catch (Exception localException)
    {
      this.f = false;
      TLog.e(localException, "startFaceDetection", new Object[0]);
    }
  }
  
  public void onCameraFaceDetection(List<TuSdkFace> paramList, TuSdkSize paramTuSdkSize) {}
  
  @TargetApi(14)
  private void d()
  {
    if ((inputCamera() == null) || (!this.f)) {
      return;
    }
    this.f = false;
    try
    {
      inputCamera().setFaceDetectionListener(null);
      inputCamera().stopFaceDetection();
    }
    catch (Exception localException)
    {
      TLog.e(localException, "stopFaceDetection", new Object[0]);
    }
  }
  
  public void pauseCameraCapture()
  {
    super.pauseCameraCapture();
    b();
  }
  
  public void resumeCameraCapture()
  {
    super.resumeCameraCapture();
  }
  
  public void rotateCamera()
  {
    int i = CameraHelper.cameraCounts();
    if ((!isCapturing()) || (i < 2)) {
      return;
    }
    this.d = (this.d == CameraConfigs.CameraFacing.Front ? CameraConfigs.CameraFacing.Back : CameraConfigs.CameraFacing.Front);
    startCameraCapture();
  }
  
  protected ImageOrientation computerOutputOrientation()
  {
    return TuSdkCameraOrientationImpl.computerOutputOrientation(getContext(), this.b, isHorizontallyMirrorRearFacingCamera(), isHorizontallyMirrorFrontFacingCamera(), getOutputImageOrientation());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesVideoCamera.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */