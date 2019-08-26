package org.lasque.tusdk.core.media.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import java.io.IOException;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;

public class TuSdkCameraBuilderImpl
  implements TuSdkCameraBuilder
{
  private Camera.CameraInfo a;
  private Camera b;
  private CameraConfigs.CameraFacing c = CameraConfigs.CameraFacing.Back;
  
  public Camera.CameraInfo getInfo()
  {
    return this.a;
  }
  
  public Camera getOrginCamera()
  {
    return this.b;
  }
  
  public Camera.Parameters getParameters()
  {
    if (this.b == null) {
      return null;
    }
    return this.b.getParameters();
  }
  
  public CameraConfigs.CameraFacing getFacing()
  {
    return this.c;
  }
  
  public boolean isBackFacingCameraPresent()
  {
    return this.c == CameraConfigs.CameraFacing.Back;
  }
  
  public boolean open(CameraConfigs.CameraFacing paramCameraFacing)
  {
    if (paramCameraFacing == null)
    {
      TLog.e("%s open need a CameraFacing", new Object[] { "TuSdkCameraBuilderImpl" });
      return false;
    }
    this.c = paramCameraFacing;
    return open();
  }
  
  public boolean open()
  {
    releaseCamera();
    this.a = CameraHelper.firstCameraInfo(this.c);
    if (this.a == null)
    {
      TLog.e("%s open can not find any camera info: %s", new Object[] { "TuSdkCameraBuilderImpl", this.c });
      return false;
    }
    this.c = CameraHelper.getCameraFacing(this.a);
    this.b = CameraHelper.getCamera(this.a);
    if (this.b == null)
    {
      TLog.e("%s open can not find any camera: %s", new Object[] { "TuSdkCameraBuilderImpl", this.a });
      return false;
    }
    return true;
  }
  
  public void releaseCamera()
  {
    if (this.b == null) {
      return;
    }
    try
    {
      this.b.setPreviewCallback(null);
      this.b.cancelAutoFocus();
      this.b.stopPreview();
      this.b.release();
    }
    catch (Exception localException)
    {
      TLog.e(localException, "%s releaseCamera has error, ignore.", new Object[] { "TuSdkCameraBuilderImpl" });
    }
    finally
    {
      this.b = null;
    }
  }
  
  public boolean startPreview()
  {
    if (this.b == null)
    {
      TLog.w("%s startPreview need after open.", new Object[] { "TuSdkCameraBuilderImpl" });
      return false;
    }
    try
    {
      this.b.startPreview();
    }
    catch (Exception localException)
    {
      TLog.e(localException, "%s startPreview has error.", new Object[] { "TuSdkCameraBuilderImpl" });
    }
    return true;
  }
  
  public void setPreviewTexture(SurfaceTexture paramSurfaceTexture)
  {
    if (this.b == null) {
      return;
    }
    try
    {
      this.b.setPreviewTexture(paramSurfaceTexture);
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "%s setPreviewTexture failed.", new Object[] { "TuSdkCameraBuilderImpl" });
    }
  }
  
  public void setPreviewCallbackWithBuffer(Camera.PreviewCallback paramPreviewCallback)
  {
    if (this.b == null) {
      return;
    }
    this.b.setPreviewCallbackWithBuffer(paramPreviewCallback);
  }
  
  public void addCallbackBuffer(byte[] paramArrayOfByte)
  {
    if (this.b == null) {
      return;
    }
    this.b.addCallbackBuffer(paramArrayOfByte);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\camera\TuSdkCameraBuilderImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */