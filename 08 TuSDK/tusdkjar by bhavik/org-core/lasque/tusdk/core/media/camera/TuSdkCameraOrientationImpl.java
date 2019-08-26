package org.lasque.tusdk.core.media.camera;

import android.content.Context;
import android.hardware.Camera.CameraInfo;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener;
import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener.TuSdkOrientationDegreeDelegate;
import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener.TuSdkOrientationDelegate;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkCameraOrientationImpl
  implements TuSdkCameraOrientation
{
  private ImageOrientation a = ImageOrientation.Up;
  private InterfaceOrientation b = InterfaceOrientation.Portrait;
  private boolean c;
  private boolean d;
  private boolean e;
  private final TuSdkOrientationEventListener f = new TuSdkOrientationEventListener(TuSdkContext.context());
  private TuSdkCameraBuilder g;
  
  public InterfaceOrientation getOutputImageOrientation()
  {
    return this.b;
  }
  
  public void setOutputImageOrientation(InterfaceOrientation paramInterfaceOrientation)
  {
    if (paramInterfaceOrientation == null) {
      return;
    }
    this.b = paramInterfaceOrientation;
  }
  
  public boolean isHorizontallyMirrorFrontFacingCamera()
  {
    return this.c;
  }
  
  public void setHorizontallyMirrorFrontFacingCamera(boolean paramBoolean)
  {
    this.c = paramBoolean;
  }
  
  public boolean isHorizontallyMirrorRearFacingCamera()
  {
    return this.d;
  }
  
  public void setHorizontallyMirrorRearFacingCamera(boolean paramBoolean)
  {
    this.d = paramBoolean;
  }
  
  public boolean isDisableMirrorFrontFacing()
  {
    return this.e;
  }
  
  public void setDisableMirrorFrontFacing(boolean paramBoolean)
  {
    this.e = paramBoolean;
  }
  
  public ImageOrientation previewOrientation()
  {
    return this.a;
  }
  
  public void setDeviceOrientListener(TuSdkOrientationEventListener.TuSdkOrientationDelegate paramTuSdkOrientationDelegate, TuSdkOrientationEventListener.TuSdkOrientationDegreeDelegate paramTuSdkOrientationDegreeDelegate)
  {
    this.f.setDelegate(paramTuSdkOrientationDelegate, paramTuSdkOrientationDegreeDelegate);
  }
  
  public void configure(TuSdkCameraBuilder paramTuSdkCameraBuilder)
  {
    if (paramTuSdkCameraBuilder == null)
    {
      TLog.e("%s configure builder is empty.", new Object[] { "TuSdkCameraOrientationImpl" });
      return;
    }
    this.g = paramTuSdkCameraBuilder;
    this.f.enable();
    this.a = a();
  }
  
  public void changeStatus(TuSdkCamera.TuSdkCameraStatus paramTuSdkCameraStatus)
  {
    if ((paramTuSdkCameraStatus == TuSdkCamera.TuSdkCameraStatus.CAMERA_START_PREVIEW) || (paramTuSdkCameraStatus == TuSdkCamera.TuSdkCameraStatus.CAMERA_PREPARE_SHOT)) {
      this.f.enable();
    } else {
      this.f.disable();
    }
  }
  
  private ImageOrientation a()
  {
    if (this.g == null) {
      return ImageOrientation.Up;
    }
    return computerOutputOrientation(TuSdkContext.context(), this.g.getInfo(), isHorizontallyMirrorRearFacingCamera(), isHorizontallyMirrorFrontFacingCamera(), getOutputImageOrientation());
  }
  
  public ImageOrientation captureOrientation()
  {
    if (this.g == null) {
      return ImageOrientation.Up;
    }
    boolean bool = (isHorizontallyMirrorFrontFacingCamera()) && (!isDisableMirrorFrontFacing());
    InterfaceOrientation localInterfaceOrientation = this.f.getOrien();
    if (((!this.g.isBackFacingCameraPresent()) && (!bool)) || ((this.g.isBackFacingCameraPresent()) && (isHorizontallyMirrorRearFacingCamera()))) {
      localInterfaceOrientation = InterfaceOrientation.getWithDegrees(this.f.getDeviceAngle());
    }
    return computerOutputOrientation(this.g.getInfo(), localInterfaceOrientation, isHorizontallyMirrorRearFacingCamera(), bool, getOutputImageOrientation());
  }
  
  public static ImageOrientation computerOutputOrientation(Context paramContext, Camera.CameraInfo paramCameraInfo, boolean paramBoolean1, boolean paramBoolean2, InterfaceOrientation paramInterfaceOrientation)
  {
    return computerOutputOrientation(paramCameraInfo, ContextUtils.getInterfaceRotation(paramContext), paramBoolean1, paramBoolean2, paramInterfaceOrientation);
  }
  
  public static ImageOrientation computerOutputOrientation(Camera.CameraInfo paramCameraInfo, InterfaceOrientation paramInterfaceOrientation1, boolean paramBoolean1, boolean paramBoolean2, InterfaceOrientation paramInterfaceOrientation2)
  {
    if (paramInterfaceOrientation1 == null) {
      paramInterfaceOrientation1 = InterfaceOrientation.Portrait;
    }
    if (paramInterfaceOrientation2 == null) {
      paramInterfaceOrientation2 = InterfaceOrientation.Portrait;
    }
    int i = 0;
    int j = 1;
    if (paramCameraInfo != null)
    {
      i = paramCameraInfo.orientation;
      j = paramCameraInfo.facing == 0 ? 1 : 0;
    }
    int k = paramInterfaceOrientation1.getDegree();
    if (paramInterfaceOrientation2 != null) {
      k += paramInterfaceOrientation2.getDegree();
    }
    if (j != 0)
    {
      localInterfaceOrientation = InterfaceOrientation.getWithDegrees(i - k);
      if (paramBoolean1)
      {
        switch (1.a[localInterfaceOrientation.ordinal()])
        {
        case 1: 
          return ImageOrientation.DownMirrored;
        case 2: 
          return ImageOrientation.LeftMirrored;
        case 3: 
          return ImageOrientation.RightMirrored;
        }
        return ImageOrientation.UpMirrored;
      }
      switch (1.a[localInterfaceOrientation.ordinal()])
      {
      case 1: 
        return ImageOrientation.Up;
      case 2: 
        return ImageOrientation.Left;
      case 3: 
        return ImageOrientation.Right;
      }
      return ImageOrientation.Down;
    }
    InterfaceOrientation localInterfaceOrientation = InterfaceOrientation.getWithDegrees(i + k);
    if (paramBoolean2)
    {
      switch (1.a[localInterfaceOrientation.ordinal()])
      {
      case 1: 
        return ImageOrientation.UpMirrored;
      case 2: 
        return ImageOrientation.LeftMirrored;
      case 3: 
        return ImageOrientation.RightMirrored;
      }
      return ImageOrientation.DownMirrored;
    }
    switch (1.a[localInterfaceOrientation.ordinal()])
    {
    case 1: 
      return ImageOrientation.Down;
    case 2: 
      return ImageOrientation.Left;
    case 3: 
      return ImageOrientation.Right;
    }
    return ImageOrientation.Up;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\camera\TuSdkCameraOrientationImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */