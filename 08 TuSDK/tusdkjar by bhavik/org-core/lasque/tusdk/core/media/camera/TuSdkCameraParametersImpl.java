package org.lasque.tusdk.core.media.camera;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraAntibanding;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFlash;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraWhiteBalance;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;

public class TuSdkCameraParametersImpl
  implements TuSdkCameraParameters
{
  private boolean a = false;
  private CameraConfigs.CameraAntibanding b;
  private CameraConfigs.CameraWhiteBalance c;
  private CameraConfigs.CameraFlash d;
  private TuSdkCameraBuilder e;
  
  public boolean isUnifiedParameters()
  {
    return this.a;
  }
  
  public void setUnifiedParameters(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
  
  public void setAntibandingMode(CameraConfigs.CameraAntibanding paramCameraAntibanding)
  {
    this.b = paramCameraAntibanding;
    Camera.Parameters localParameters = a();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setAntibanding(localParameters, this.b);
    a(localParameters);
  }
  
  public CameraConfigs.CameraAntibanding getAntiBandingMode()
  {
    Camera.Parameters localParameters = a();
    if (localParameters == null) {
      return this.b;
    }
    return CameraHelper.antiBandingType(localParameters.getAntibanding());
  }
  
  public void setWhiteBalance(CameraConfigs.CameraWhiteBalance paramCameraWhiteBalance)
  {
    this.c = paramCameraWhiteBalance;
    Camera.Parameters localParameters = a();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setWhiteBalance(localParameters, this.c);
    a(localParameters);
  }
  
  public CameraConfigs.CameraWhiteBalance getWhiteBalance()
  {
    Camera.Parameters localParameters = a();
    if (localParameters == null) {
      return this.c;
    }
    return CameraHelper.whiteBalance(localParameters.getAntibanding());
  }
  
  public CameraConfigs.CameraFlash getFlashMode()
  {
    Camera.Parameters localParameters = a();
    if (localParameters == null) {
      return CameraConfigs.CameraFlash.Off;
    }
    return CameraHelper.getFlashMode(localParameters);
  }
  
  public void setFlashMode(CameraConfigs.CameraFlash paramCameraFlash)
  {
    if (paramCameraFlash == null) {
      return;
    }
    this.d = paramCameraFlash;
    if (!CameraHelper.canSupportFlash(TuSdkContext.context())) {
      return;
    }
    Camera.Parameters localParameters = a();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setFlashMode(localParameters, paramCameraFlash);
    a(localParameters);
  }
  
  public boolean canSupportFlash()
  {
    if (!CameraHelper.canSupportFlash(TuSdkContext.context())) {
      return false;
    }
    return CameraHelper.supportFlash(a());
  }
  
  public void configure(TuSdkCameraBuilder paramTuSdkCameraBuilder)
  {
    if (paramTuSdkCameraBuilder == null)
    {
      TLog.e("%s configure builder is empty.", new Object[] { "TuSdkCameraParametersImpl" });
      return;
    }
    this.e = paramTuSdkCameraBuilder;
    Camera.Parameters localParameters = a();
    if (localParameters == null)
    {
      TLog.e("%s configure Camera.Parameters is empty.", new Object[] { "TuSdkCameraParametersImpl" });
      return;
    }
    if (isUnifiedParameters()) {
      CameraHelper.unifiedParameters(localParameters);
    }
    CameraHelper.setFlashMode(localParameters, getFlashMode());
    CameraHelper.setAntibanding(localParameters, getAntiBandingMode());
    CameraHelper.setWhiteBalance(localParameters, getWhiteBalance());
    a(localParameters);
  }
  
  public void changeStatus(TuSdkCamera.TuSdkCameraStatus paramTuSdkCameraStatus) {}
  
  private Camera.Parameters a()
  {
    if (this.e == null) {
      return null;
    }
    return this.e.getParameters();
  }
  
  private void a(Camera.Parameters paramParameters)
  {
    if ((this.e == null) || (this.e.getOrginCamera() == null)) {
      return;
    }
    this.e.getOrginCamera().setParameters(paramParameters);
  }
  
  public void logParameters()
  {
    CameraHelper.logParameters(a());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\camera\TuSdkCameraParametersImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */