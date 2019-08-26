package org.lasque.tusdk.modules.components.camera;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFlash;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter.CameraState;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraInterface;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraInterface.TuSdkStillCameraListener;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.impl.activity.TuResultFragment;
import org.lasque.tusdk.impl.view.widget.RegionHandler;
import org.lasque.tusdk.modules.components.ComponentActType;

public abstract class TuCameraFragmentBase
  extends TuResultFragment
{
  private TuSdkStillCameraInterface a;
  private int b;
  private int c = 31;
  private int d;
  private TuSdkStillCameraInterface.TuSdkStillCameraListener e = new TuSdkStillCameraInterface.TuSdkStillCameraListener()
  {
    public void onStillCameraStateChanged(TuSdkStillCameraInterface paramAnonymousTuSdkStillCameraInterface, TuSdkStillCameraAdapter.CameraState paramAnonymousCameraState)
    {
      TuCameraFragmentBase.this.onCameraStateChangedImpl(paramAnonymousTuSdkStillCameraInterface, paramAnonymousCameraState);
      if (paramAnonymousCameraState != TuSdkStillCameraAdapter.CameraState.StateStarted) {
        return;
      }
      StatisticsManger.appendComponent(paramAnonymousTuSdkStillCameraInterface.isFrontFacingCameraPresent() ? ComponentActType.camera_action_faceing_front : ComponentActType.camera_action_faceing_back);
    }
    
    public void onStillCameraTakedPicture(TuSdkStillCameraInterface paramAnonymousTuSdkStillCameraInterface, TuSdkResult paramAnonymousTuSdkResult)
    {
      TuCameraFragmentBase.this.onCameraTakedPictureImpl(paramAnonymousTuSdkStillCameraInterface, paramAnonymousTuSdkResult);
      StatisticsManger.appendComponent(ComponentActType.camera_action_take_picture);
    }
    
    public void onFilterChanged(SelesOutInput paramAnonymousSelesOutInput)
    {
      TuCameraFragmentBase.this.onFilterChanged(paramAnonymousSelesOutInput);
    }
  };
  
  public TuSdkStillCameraInterface getCamera()
  {
    return this.a;
  }
  
  public abstract float getCameraViewRatio();
  
  public abstract RelativeLayout getCameraView();
  
  public abstract CameraConfigs.CameraFacing getAvPostion();
  
  protected abstract void configCamera(TuSdkStillCameraInterface paramTuSdkStillCameraInterface);
  
  protected abstract void onCameraStateChangedImpl(TuSdkStillCameraInterface paramTuSdkStillCameraInterface, TuSdkStillCameraAdapter.CameraState paramCameraState);
  
  protected abstract void onCameraTakedPictureImpl(TuSdkStillCameraInterface paramTuSdkStillCameraInterface, TuSdkResult paramTuSdkResult);
  
  protected void loadView(ViewGroup paramViewGroup) {}
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.cameraFragment);
  }
  
  protected void initCameraView()
  {
    this.a = TuSdk.camera(getActivity(), getAvPostion(), getCameraView());
    addOrientationListener();
    this.a.setCameraListener(this.e);
    configCamera(this.a);
    this.a.startCameraCapture();
  }
  
  public void onDestroyView()
  {
    super.onDestroyView();
    if (this.a != null)
    {
      this.a.destroy();
      this.a = null;
    }
  }
  
  protected void onFilterChanged(SelesOutInput paramSelesOutInput) {}
  
  protected void asyncProcessingIfNeedSave(TuSdkResult paramTuSdkResult)
  {
    if (getWaterMarkOption() != null)
    {
      Bitmap localBitmap = paramTuSdkResult.image;
      paramTuSdkResult.image = addWaterMarkToImage(paramTuSdkResult.image);
      BitmapHelper.recycled(localBitmap);
    }
    super.asyncProcessingIfNeedSave(paramTuSdkResult);
  }
  
  @TargetApi(23)
  public String[] getRequiredPermissions()
  {
    String[] arrayOfString = { "android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.ACCESS_FINE_LOCATION" };
    return arrayOfString;
  }
  
  protected void handleFlashModel(CameraConfigs.CameraFlash paramCameraFlash)
  {
    if (paramCameraFlash == null) {
      return;
    }
    long l = ComponentActType.camera_action_flash_auto;
    if (paramCameraFlash == CameraConfigs.CameraFlash.On) {
      l = ComponentActType.camera_action_flash_on;
    } else if (paramCameraFlash == CameraConfigs.CameraFlash.Off) {
      l = ComponentActType.camera_action_flash_off;
    }
    if (this.a != null) {
      this.a.setFlashMode(paramCameraFlash);
    }
    StatisticsManger.appendComponent(l);
  }
  
  protected void handleCaptureButton()
  {
    if (this.a != null) {
      this.a.captureImage();
    }
  }
  
  protected void handleCaptureWithVolume()
  {
    if (this.a != null) {
      this.a.captureImage();
    }
    StatisticsManger.appendComponent(ComponentActType.camera_action_capture_with_volume);
  }
  
  protected void handleSwitchButton()
  {
    if (this.a != null) {
      this.a.rotateCamera();
    }
  }
  
  protected boolean handleSwitchFilter(String paramString)
  {
    if (this.a != null)
    {
      this.a.switchFilter(paramString);
      return true;
    }
    return false;
  }
  
  protected void handleCloseButton()
  {
    dismissActivityWithAnim();
  }
  
  public final int getRatioType()
  {
    return this.c;
  }
  
  public final void setRatioType(int paramInt)
  {
    this.c = paramInt;
    if (this.b == 0) {
      this.b = RatioType.radioType(TuSdkContext.getScreenSize().minMaxRatio());
    }
    if ((this.b != 1) && (this.b == (this.b & paramInt))) {
      this.c = ((paramInt | 0x1) ^ this.b);
    }
  }
  
  public float getCurrentRatio()
  {
    if (getCameraViewRatio() > 0.0F) {
      return getCameraViewRatio();
    }
    if (this.d > 0) {
      return RatioType.ratio(this.d);
    }
    return 0.0F;
  }
  
  public int getCurrentRatioType()
  {
    return this.d;
  }
  
  protected float getPreviewOffsetTopPercent(int paramInt)
  {
    return -1.0F;
  }
  
  protected void setCurrentRatioType(int paramInt)
  {
    long l;
    switch (paramInt)
    {
    case 16: 
      l = ComponentActType.camera_action_ratio_9_16;
      break;
    case 8: 
      l = ComponentActType.camera_action_ratio_3_4;
      break;
    case 4: 
      l = ComponentActType.camera_action_ratio_2_3;
      break;
    case 2: 
      l = ComponentActType.camera_action_ratio_1_1;
      break;
    default: 
      l = ComponentActType.camera_action_ratio_orgin;
    }
    this.d = paramInt;
    StatisticsManger.appendComponent(l);
  }
  
  protected void handleCameraRatio()
  {
    if (this.a == null) {
      return;
    }
    int i = RatioType.nextRatioType(getRatioType(), this.d);
    setCurrentRatioType(i);
    this.a.adapter().getRegionHandler().setOffsetTopPercent(getPreviewOffsetTopPercent(this.d));
    this.a.adapter().changeRegionRatio(RatioType.ratio(i));
  }
  
  protected void handleGuideLineButton()
  {
    if (this.a == null) {
      return;
    }
    boolean bool = this.a.adapter().isDisplayGuideLine();
    this.a.adapter().setDisplayGuideLine(!bool);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\camera\TuCameraFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */