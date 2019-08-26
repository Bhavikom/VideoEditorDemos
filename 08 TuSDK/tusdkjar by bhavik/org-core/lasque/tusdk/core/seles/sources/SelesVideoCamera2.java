package org.lasque.tusdk.core.seles.sources;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.Camera2Helper;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

@TargetApi(21)
public abstract class SelesVideoCamera2
  extends SelesVideoCamera2Base
{
  private CameraManager b;
  private String c;
  private CameraCharacteristics d;
  protected final Handler mHandler;
  private HandlerThread e;
  private CameraConfigs.CameraFacing f;
  private Surface g;
  private SelesVideoCamera2Engine h = new SelesVideoCamera2Engine()
  {
    public boolean canInitCamera()
    {
      SelesVideoCamera2.a(SelesVideoCamera2.this, Camera2Helper.firstCameraId(SelesVideoCamera2.this.getContext(), SelesVideoCamera2.a(SelesVideoCamera2.this)));
      if (SelesVideoCamera2.b(SelesVideoCamera2.this) == null)
      {
        TLog.e("The device can not find any camera2 info: %s", new Object[] { SelesVideoCamera2.a(SelesVideoCamera2.this) });
        return false;
      }
      return true;
    }
    
    public boolean onInitCamera()
    {
      SelesVideoCamera2.a(SelesVideoCamera2.this, Camera2Helper.cameraCharacter(SelesVideoCamera2.c(SelesVideoCamera2.this), SelesVideoCamera2.b(SelesVideoCamera2.this)));
      if (SelesVideoCamera2.d(SelesVideoCamera2.this) == null)
      {
        TLog.e("The device can not find init camera2: %s", new Object[] { SelesVideoCamera2.b(SelesVideoCamera2.this) });
        return false;
      }
      SelesVideoCamera2.this.onInitConfig(SelesVideoCamera2.d(SelesVideoCamera2.this));
      return true;
    }
    
    public TuSdkSize previewOptimalSize()
    {
      return SelesVideoCamera2.this.computerPreviewOptimalSize();
    }
    
    public void onCameraWillOpen(SurfaceTexture paramAnonymousSurfaceTexture)
    {
      if (paramAnonymousSurfaceTexture == null) {
        return;
      }
      SelesVideoCamera2.a(SelesVideoCamera2.this, new Surface(paramAnonymousSurfaceTexture));
      try
      {
        SelesVideoCamera2.c(SelesVideoCamera2.this).openCamera(SelesVideoCamera2.b(SelesVideoCamera2.this), SelesVideoCamera2.this.getCameraStateCallback(), SelesVideoCamera2.this.mHandler);
      }
      catch (CameraAccessException localCameraAccessException)
      {
        TLog.e(localCameraAccessException, "SelesVideoCamera2 asyncInitCamera", new Object[0]);
      }
    }
    
    public void onCameraStarted() {}
    
    public ImageOrientation previewOrientation()
    {
      return SelesVideoCamera2.e(SelesVideoCamera2.this);
    }
  };
  
  public String getCameraId()
  {
    return this.c;
  }
  
  public CameraCharacteristics getCameraCharacter()
  {
    return this.d;
  }
  
  public Surface getPreviewSurface()
  {
    return this.g;
  }
  
  public CameraConfigs.CameraFacing cameraPosition()
  {
    CameraCharacteristics localCameraCharacteristics = getCameraCharacter();
    return Camera2Helper.cameraPosition(localCameraCharacteristics);
  }
  
  public boolean isFrontFacingCameraPresent()
  {
    return cameraPosition() == CameraConfigs.CameraFacing.Front;
  }
  
  public boolean isBackFacingCameraPresent()
  {
    return cameraPosition() == CameraConfigs.CameraFacing.Back;
  }
  
  public SelesVideoCamera2(Context paramContext, CameraConfigs.CameraFacing paramCameraFacing)
  {
    super(paramContext);
    this.f = (paramCameraFacing == null ? CameraConfigs.CameraFacing.Back : paramCameraFacing);
    this.b = Camera2Helper.cameraManager(paramContext);
    this.e = new HandlerThread("TuSDK_L_Camera");
    this.e.start();
    this.mHandler = new Handler(this.e.getLooper());
    super.setCameraEngine(this.h);
  }
  
  @Deprecated
  public void setCameraEngine(SelesVideoCamera2Engine paramSelesVideoCamera2Engine) {}
  
  protected void onDestroy()
  {
    super.onDestroy();
    a();
  }
  
  private void a()
  {
    if (this.e == null) {
      return;
    }
    try
    {
      this.e.quitSafely();
      this.e.join();
      this.e = null;
    }
    catch (InterruptedException localInterruptedException)
    {
      TLog.e(localInterruptedException, "release Handler error", new Object[0]);
    }
  }
  
  protected void onInitConfig(CameraCharacteristics paramCameraCharacteristics) {}
  
  protected void onCameraStarted()
  {
    super.onCameraStarted();
  }
  
  public void rotateCamera()
  {
    int i = Camera2Helper.cameraCounts(getContext());
    if ((!isCapturing()) || (i < 2)) {
      return;
    }
    this.f = (this.f == CameraConfigs.CameraFacing.Front ? CameraConfigs.CameraFacing.Back : CameraConfigs.CameraFacing.Front);
    startCameraCapture();
  }
  
  protected abstract CameraDevice.StateCallback getCameraStateCallback();
  
  protected abstract TuSdkSize computerPreviewOptimalSize();
  
  private ImageOrientation b()
  {
    return computerOutputOrientation(getContext(), this.d, isHorizontallyMirrorRearFacingCamera(), isHorizontallyMirrorFrontFacingCamera(), getOutputImageOrientation());
  }
  
  public static ImageOrientation computerOutputOrientation(Context paramContext, CameraCharacteristics paramCameraCharacteristics, boolean paramBoolean1, boolean paramBoolean2, InterfaceOrientation paramInterfaceOrientation)
  {
    return computerOutputOrientation(paramCameraCharacteristics, ContextUtils.getInterfaceRotation(paramContext), paramBoolean1, paramBoolean2, paramInterfaceOrientation);
  }
  
  public static ImageOrientation computerOutputOrientation(CameraCharacteristics paramCameraCharacteristics, InterfaceOrientation paramInterfaceOrientation1, boolean paramBoolean1, boolean paramBoolean2, InterfaceOrientation paramInterfaceOrientation2)
  {
    if (paramInterfaceOrientation1 == null) {
      paramInterfaceOrientation1 = InterfaceOrientation.Portrait;
    }
    if (paramInterfaceOrientation2 == null) {
      paramInterfaceOrientation2 = InterfaceOrientation.Portrait;
    }
    int i = 0;
    int j = 1;
    if (paramCameraCharacteristics != null)
    {
      i = ((Integer)paramCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
      j = ((Integer)paramCameraCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue() == 1 ? 1 : 0;
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
        switch (2.a[localInterfaceOrientation.ordinal()])
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
      switch (2.a[localInterfaceOrientation.ordinal()])
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
      switch (2.a[localInterfaceOrientation.ordinal()])
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
    switch (2.a[localInterfaceOrientation.ordinal()])
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


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesVideoCamera2.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */