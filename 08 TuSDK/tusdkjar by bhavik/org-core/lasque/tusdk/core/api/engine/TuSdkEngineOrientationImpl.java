package org.lasque.tusdk.core.api.engine;

import android.hardware.Camera.CameraInfo;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkEngineOrientationImpl
  implements TuSdkEngineOrientation
{
  private TuSdkSize a = TuSdkSize.create(0);
  private TuSdkSize b = TuSdkSize.create(0);
  private CameraConfigs.CameraFacing c = CameraConfigs.CameraFacing.Front;
  private InterfaceOrientation d = InterfaceOrientation.Portrait;
  private ImageOrientation e = ImageOrientation.Up;
  private ImageOrientation f = ImageOrientation.Up;
  private ImageOrientation g = ImageOrientation.Up;
  private boolean h = false;
  private boolean i = true;
  private boolean j;
  private boolean k;
  private float l;
  private InterfaceOrientation m = InterfaceOrientation.Portrait;
  private ImageOrientation n = ImageOrientation.Up;
  
  public TuSdkSize getInputSize()
  {
    return this.a;
  }
  
  public void setInputSize(int paramInt1, int paramInt2)
  {
    if ((this.a.width != paramInt1) || (this.a.height != paramInt2))
    {
      this.a = TuSdkSize.create(paramInt1, paramInt2);
      a();
    }
  }
  
  public CameraConfigs.CameraFacing getCameraFacing()
  {
    return this.c;
  }
  
  public void setCameraFacing(CameraConfigs.CameraFacing paramCameraFacing)
  {
    if (paramCameraFacing == null) {
      return;
    }
    this.c = paramCameraFacing;
    a();
  }
  
  public void switchCameraFacing()
  {
    setCameraFacing(this.c == CameraConfigs.CameraFacing.Front ? CameraConfigs.CameraFacing.Back : CameraConfigs.CameraFacing.Front);
  }
  
  public InterfaceOrientation getInterfaceOrientation()
  {
    return this.d;
  }
  
  public void setInterfaceOrientation(InterfaceOrientation paramInterfaceOrientation)
  {
    if (paramInterfaceOrientation == null) {
      return;
    }
    this.d = paramInterfaceOrientation;
    a();
  }
  
  public void setInputOrientation(ImageOrientation paramImageOrientation)
  {
    if (paramImageOrientation == null) {
      return;
    }
    this.e = paramImageOrientation;
    a();
  }
  
  public void setOutputOrientation(ImageOrientation paramImageOrientation)
  {
    if (paramImageOrientation == null) {
      return;
    }
    this.f = paramImageOrientation;
    a();
  }
  
  public void setHorizontallyMirrorFrontFacingCamera(boolean paramBoolean)
  {
    this.j = paramBoolean;
    a();
  }
  
  public void setHorizontallyMirrorRearFacingCamera(boolean paramBoolean)
  {
    this.k = paramBoolean;
    a();
  }
  
  public void release() {}
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  private void a()
  {
    if (this.e != null)
    {
      this.g = this.e;
    }
    else
    {
      Camera.CameraInfo localCameraInfo = CameraHelper.firstCameraInfo(this.c);
      this.g = TuSdkCameraOrientationImpl.computerOutputOrientation(localCameraInfo, ContextUtils.getInterfaceRotation(TuSdkContext.context()), this.k, this.j, InterfaceOrientation.Portrait);
    }
    this.b = this.a.transforOrientation(this.g);
  }
  
  public ImageOrientation getInputRotation()
  {
    return this.g;
  }
  
  public ImageOrientation getOutputOrientation()
  {
    return this.f;
  }
  
  public TuSdkSize getOutputSize()
  {
    return this.b;
  }
  
  public float getDeviceAngle()
  {
    return this.l;
  }
  
  public void setDeviceAngle(float paramFloat)
  {
    this.l = paramFloat;
  }
  
  public InterfaceOrientation getDeviceOrient()
  {
    return this.m;
  }
  
  public void setDeviceOrient(InterfaceOrientation paramInterfaceOrientation)
  {
    this.m = paramInterfaceOrientation;
  }
  
  public boolean isOriginalCaptureOrientation()
  {
    return this.i;
  }
  
  public void setOriginalCaptureOrientation(boolean paramBoolean)
  {
    this.i = paramBoolean;
  }
  
  public void setOutputCaptureMirrorEnabled(boolean paramBoolean)
  {
    this.h = paramBoolean;
  }
  
  public boolean isOutputCaptureMirrorEnabled()
  {
    return this.h;
  }
  
  public ImageOrientation getYuvOutputOrienation()
  {
    return this.n;
  }
  
  public void setYuvOutputOrienation(ImageOrientation paramImageOrientation)
  {
    if (paramImageOrientation == null) {
      return;
    }
    this.n = paramImageOrientation;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\engine\TuSdkEngineOrientationImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */