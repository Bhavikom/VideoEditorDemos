package org.lasque.tusdk.core.media.camera;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;

public class TuSdkCameraSizeImpl
  implements TuSdkCameraSize
{
  private int a;
  private float b;
  private float c;
  private TuSdkSize d;
  private int e;
  private float f;
  private TuSdkSize g;
  private TuSdkCameraBuilder h;
  
  private int a()
  {
    TuSdkSize localTuSdkSize = ContextUtils.getScreenSize(TuSdkContext.context());
    if ((this.a < 1) || (this.a > localTuSdkSize.maxSide())) {
      this.a = localTuSdkSize.maxSide();
    }
    return this.a;
  }
  
  public void setPreviewMaxSize(int paramInt)
  {
    this.a = paramInt;
  }
  
  public float getPreviewEffectScale()
  {
    return this.b;
  }
  
  public void setPreviewEffectScale(float paramFloat)
  {
    if (paramFloat <= 0.0F) {
      return;
    }
    if (paramFloat > 1.0F) {
      this.b = 1.0F;
    }
    this.b = paramFloat;
  }
  
  public void setPreviewRatio(float paramFloat)
  {
    this.c = paramFloat;
  }
  
  public float getPreviewRatio()
  {
    return this.c;
  }
  
  public TuSdkSize previewOptimalSize()
  {
    return this.d;
  }
  
  public int previewBufferLength()
  {
    return this.e;
  }
  
  public void setOutputPictureRatio(float paramFloat)
  {
    this.f = paramFloat;
  }
  
  public float getOutputPictureRatio()
  {
    return this.f;
  }
  
  public TuSdkSize getOutputSize()
  {
    if (this.g == null) {
      this.g = ContextUtils.getScreenSize(TuSdkContext.context());
    }
    return this.g;
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return;
    }
    this.g = paramTuSdkSize.limitSize();
    Camera.Parameters localParameters = b();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setPictureSize(TuSdkContext.context(), localParameters, this.g);
    a(localParameters);
  }
  
  public TuSdkCameraSizeImpl()
  {
    if (ContextUtils.getScreenSize(TuSdkContext.context()).maxSide() < 1000) {
      this.b = 0.85F;
    } else {
      this.b = 0.75F;
    }
  }
  
  public void configure(TuSdkCameraBuilder paramTuSdkCameraBuilder)
  {
    if (paramTuSdkCameraBuilder == null)
    {
      TLog.e("%s configure builder is empty.", new Object[] { "TuSdkCameraSizeImpl" });
      return;
    }
    this.h = paramTuSdkCameraBuilder;
    Camera.Parameters localParameters = b();
    if (localParameters == null)
    {
      TLog.e("%s configure Camera.Parameters is empty.", new Object[] { "TuSdkCameraSizeImpl" });
      return;
    }
    CameraHelper.setPreviewSize(TuSdkContext.context(), localParameters, a(), getPreviewEffectScale(), getPreviewRatio());
    TuSdkSize localTuSdkSize = getOutputSize().limitSize();
    CameraHelper.setPictureSize(TuSdkContext.context(), localParameters, localTuSdkSize, getOutputPictureRatio());
    a(localParameters);
    this.d = CameraHelper.createSize(localParameters.getPreviewSize());
    if (this.d == null)
    {
      TLog.e("%s configure can not set preview size", new Object[] { "TuSdkCameraSizeImpl" });
      return;
    }
    int i = this.d.width * this.d.height;
    int j = ImageFormat.getBitsPerPixel(localParameters.getPreviewFormat());
    this.e = (i * j / 8);
  }
  
  public void changeStatus(TuSdkCamera.TuSdkCameraStatus paramTuSdkCameraStatus) {}
  
  private Camera.Parameters b()
  {
    if (this.h == null) {
      return null;
    }
    return this.h.getParameters();
  }
  
  private void a(Camera.Parameters paramParameters)
  {
    if ((this.h == null) || (this.h.getOrginCamera() == null)) {
      return;
    }
    this.h.getOrginCamera().setParameters(paramParameters);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\camera\TuSdkCameraSizeImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */