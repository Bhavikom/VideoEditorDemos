package org.lasque.tusdk.core.seles.sources;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.AutoFocusMoveCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Build.VERSION;
import java.util.Calendar;
import org.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraAntibanding;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraAutoFocus;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFlash;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract class SelesStillCamera
  extends SelesVideoCamera
  implements SelesStillCameraInterface
{
  private boolean b;
  private TuSdkSize c;
  private boolean d = false;
  private boolean e;
  private boolean f;
  private int g;
  private float h = 0.75F;
  private CameraConfigs.CameraFlash i;
  private CameraConfigs.CameraAutoFocus j;
  private long k;
  private CameraConfigs.CameraAntibanding l;
  private float m;
  private float n;
  private Runnable o = null;
  private boolean p = false;
  
  public boolean isCapturePhoto()
  {
    return this.b;
  }
  
  private void a(boolean paramBoolean)
  {
    this.b = paramBoolean;
    onCapturePhotoStateChanged(this.b);
  }
  
  private int a()
  {
    TuSdkSize localTuSdkSize = ContextUtils.getScreenSize(getContext());
    if ((this.g < 1) || (this.g > localTuSdkSize.maxSide())) {
      this.g = localTuSdkSize.maxSide();
    }
    return this.g;
  }
  
  public void setPreviewMaxSize(int paramInt)
  {
    this.g = paramInt;
  }
  
  public final TuSdkSize getOutputSize()
  {
    if (this.c == null) {
      this.c = ContextUtils.getScreenSize(getContext());
    }
    return this.c;
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return;
    }
    this.c = paramTuSdkSize.limitSize();
    if (inputCamera() == null) {
      return;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    CameraHelper.setPictureSize(getContext(), localParameters, this.c);
    inputCamera().setParameters(localParameters);
  }
  
  public float getRegionRatio()
  {
    return getOutputSize().getRatioFloat();
  }
  
  public boolean isUnifiedParameters()
  {
    return this.d;
  }
  
  public void setUnifiedParameters(boolean paramBoolean)
  {
    this.d = paramBoolean;
  }
  
  public boolean isAutoReleaseAfterCaptured()
  {
    return this.e;
  }
  
  public void setAutoReleaseAfterCaptured(boolean paramBoolean)
  {
    this.e = paramBoolean;
  }
  
  public boolean isDisableMirrorFrontFacing()
  {
    return this.f;
  }
  
  public void setDisableMirrorFrontFacing(boolean paramBoolean)
  {
    this.f = paramBoolean;
  }
  
  public float getPreviewEffectScale()
  {
    return this.h;
  }
  
  public void setPreviewEffectScale(float paramFloat)
  {
    if (paramFloat <= 0.0F) {
      return;
    }
    if (paramFloat > 1.0F) {
      this.h = 1.0F;
    }
    this.h = paramFloat;
  }
  
  public long getLastFocusTime()
  {
    return this.k;
  }
  
  public CameraConfigs.CameraFlash getFlashMode()
  {
    if (inputCamera() == null) {
      return CameraConfigs.CameraFlash.Off;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    if (localParameters != null) {
      return CameraHelper.getFlashMode(localParameters);
    }
    if (this.i == null) {
      this.i = CameraConfigs.CameraFlash.Off;
    }
    return this.i;
  }
  
  public void setFlashMode(CameraConfigs.CameraFlash paramCameraFlash)
  {
    if (paramCameraFlash == null) {
      return;
    }
    this.i = paramCameraFlash;
    if ((!CameraHelper.canSupportFlash(getContext())) || (inputCamera() == null)) {
      return;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setFlashMode(localParameters, paramCameraFlash);
    inputCamera().setParameters(localParameters);
  }
  
  public boolean canSupportFlash()
  {
    if ((inputCamera() == null) || (!CameraHelper.canSupportFlash(getContext()))) {
      return false;
    }
    return CameraHelper.supportFlash(inputCamera().getParameters());
  }
  
  public void setAntibandingMode(CameraConfigs.CameraAntibanding paramCameraAntibanding)
  {
    this.l = paramCameraAntibanding;
    if (inputCamera() == null) {
      return;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setAntibanding(localParameters, this.l);
    inputCamera().setParameters(localParameters);
  }
  
  public CameraConfigs.CameraAntibanding getAntiBandingMode()
  {
    if (inputCamera() == null) {
      return this.l;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    if (localParameters != null) {
      return CameraHelper.antiBandingType(localParameters.getAntibanding());
    }
    return this.l;
  }
  
  public void autoMetering(PointF paramPointF) {}
  
  public void setFocusMode(CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF)
  {
    if (paramCameraAutoFocus == null) {
      return;
    }
    this.j = paramCameraAutoFocus;
    if (inputCamera() == null) {
      return;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setFocusMode(localParameters, this.j, getCenterIfNull(paramPointF), this.mOutputRotation);
    inputCamera().setParameters(localParameters);
  }
  
  public void setFocusPoint(PointF paramPointF)
  {
    if (inputCamera() == null) {
      return;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setFocusPoint(localParameters, getCenterIfNull(paramPointF), this.mOutputRotation);
    inputCamera().setParameters(localParameters);
  }
  
  public CameraConfigs.CameraAutoFocus getFocusMode()
  {
    if (inputCamera() == null) {
      return this.j;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    if (localParameters != null) {
      return CameraHelper.focusModeType(localParameters.getFocusMode());
    }
    return this.j;
  }
  
  public boolean canSupportAutoFocus()
  {
    if (inputCamera() == null) {
      return false;
    }
    boolean bool = false;
    try
    {
      bool = CameraHelper.canSupportAutofocus(getContext(), inputCamera().getParameters());
    }
    catch (RuntimeException localRuntimeException)
    {
      bool = false;
      localRuntimeException.printStackTrace();
    }
    return bool;
  }
  
  public void cancelAutoFocus()
  {
    if ((inputCamera() == null) || (!CameraHelper.canSupportAutofocus(getContext()))) {
      return;
    }
    inputCamera().cancelAutoFocus();
  }
  
  public void autoFocus(CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF, SelesBaseCameraInterface.TuSdkAutoFocusCallback paramTuSdkAutoFocusCallback)
  {
    setFocusMode(paramCameraAutoFocus, paramPointF);
    autoFocus(paramTuSdkAutoFocusCallback);
  }
  
  public void cancelAutoFocusTimer()
  {
    if (this.o == null) {
      return;
    }
    ThreadHelper.cancel(this.o);
    this.o = null;
  }
  
  public void doFocusCallback(SelesBaseCameraInterface.TuSdkAutoFocusCallback paramTuSdkAutoFocusCallback)
  {
    cancelAutoFocusTimer();
    paramTuSdkAutoFocusCallback.onAutoFocus(this.p, this);
    cancelAutoFocus();
  }
  
  public void autoFocus(final SelesBaseCameraInterface.TuSdkAutoFocusCallback paramTuSdkAutoFocusCallback)
  {
    if ((inputCamera() == null) || (!canSupportAutoFocus()))
    {
      if (paramTuSdkAutoFocusCallback != null) {
        paramTuSdkAutoFocusCallback.onAutoFocus(false, this);
      }
      return;
    }
    this.k = Calendar.getInstance().getTimeInMillis();
    Camera.AutoFocusCallback local1 = null;
    this.p = false;
    if (paramTuSdkAutoFocusCallback != null) {
      local1 = new Camera.AutoFocusCallback()
      {
        public void onAutoFocus(boolean paramAnonymousBoolean, Camera paramAnonymousCamera)
        {
          SelesStillCamera.a(SelesStillCamera.this, paramAnonymousBoolean);
          SelesStillCamera.this.doFocusCallback(paramTuSdkAutoFocusCallback);
        }
      };
    }
    try
    {
      inputCamera().autoFocus(local1);
      this.o = new Runnable()
      {
        public void run()
        {
          SelesStillCamera.this.doFocusCallback(paramTuSdkAutoFocusCallback);
        }
      };
      ThreadHelper.postDelayed(this.o, 1500L);
    }
    catch (Exception localException)
    {
      TLog.e("autoFocus", new Object[] { localException });
    }
  }
  
  @TargetApi(16)
  public void setAutoFocusMoveCallback(Camera.AutoFocusMoveCallback paramAutoFocusMoveCallback)
  {
    if ((inputCamera() != null) || (!CameraHelper.canSupportAutofocus(getContext()))) {
      inputCamera().setAutoFocusMoveCallback(paramAutoFocusMoveCallback);
    }
  }
  
  protected PointF getCenterIfNull(PointF paramPointF)
  {
    if (paramPointF == null) {
      paramPointF = new PointF(0.5F, 0.5F);
    }
    return paramPointF;
  }
  
  public SelesStillCamera(Context paramContext, CameraConfigs.CameraFacing paramCameraFacing)
  {
    super(paramContext, paramCameraFacing);
    if (ContextUtils.getScreenSize(paramContext).maxSide() < 1000) {
      this.h = 0.85F;
    } else {
      this.h = 0.75F;
    }
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
    if (isUnifiedParameters()) {
      CameraHelper.unifiedParameters(localParameters);
    }
    CameraHelper.setPreviewSize(getContext(), localParameters, a(), getPreviewEffectScale(), getPreviewRatio());
    TuSdkSize localTuSdkSize = getOutputSize().limitSize();
    CameraHelper.setPictureSize(getContext(), localParameters, localTuSdkSize, getOutputPictureRatio());
    CameraHelper.setFocusMode(localParameters, CameraHelper.focusModes);
    this.j = CameraHelper.focusModeType(localParameters.getFocusMode());
    CameraHelper.setFlashMode(localParameters, getFlashMode());
    CameraHelper.setAntibanding(localParameters, getAntiBandingMode());
    if (Build.VERSION.SDK_INT >= 14) {
      CameraHelper.setFocusArea(localParameters, getCenterIfNull(null), null, isBackFacingCameraPresent());
    }
    paramCamera.setParameters(localParameters);
  }
  
  public void setPreviewRatio(float paramFloat)
  {
    this.m = paramFloat;
  }
  
  public float getPreviewRatio()
  {
    return this.m;
  }
  
  public void setOutputPictureRatio(float paramFloat)
  {
    this.n = paramFloat;
  }
  
  protected float getOutputPictureRatio()
  {
    return this.n;
  }
  
  public void stopCameraCapture()
  {
    this.b = false;
    if (inputCamera() != null) {
      this.j = null;
    }
    cancelAutoFocusTimer();
    super.stopCameraCapture();
  }
  
  protected void onCapturePhotoStateChanged(boolean paramBoolean) {}
  
  public ImageOrientation capturePhotoOrientation()
  {
    if ((!isDisableMirrorFrontFacing()) || (isBackFacingCameraPresent()) || (!isHorizontallyMirrorFrontFacingCamera())) {
      return this.mOutputRotation;
    }
    return TuSdkCameraOrientationImpl.computerOutputOrientation(getContext(), inputCameraInfo(), isHorizontallyMirrorRearFacingCamera(), false, getOutputImageOrientation());
  }
  
  protected void onTakePictured(byte[] paramArrayOfByte)
  {
    if (isAutoReleaseAfterCaptured()) {
      stopCameraCapture();
    } else {
      pauseCameraCapture();
    }
  }
  
  protected void onTakePictureFailed()
  {
    startCameraCapture();
  }
  
  protected Camera.ShutterCallback getShutterCallback()
  {
    return null;
  }
  
  protected Bitmap decodeToBitmapAtAsync(byte[] paramArrayOfByte)
  {
    Bitmap localBitmap = BitmapHelper.imageDecode(paramArrayOfByte, true);
    return localBitmap;
  }
  
  public void capturePhotoAsJPEGData(final SelesStillCameraInterface.CapturePhotoAsJPEGDataCallback paramCapturePhotoAsJPEGDataCallback)
  {
    final boolean bool = (!isCapturing()) || (this.b);
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        if (bool)
        {
          if (paramCapturePhotoAsJPEGDataCallback != null) {
            paramCapturePhotoAsJPEGDataCallback.onCapturePhotoAsJPEGData(null);
          }
          return;
        }
        SelesStillCamera.b(SelesStillCamera.this, true);
      }
    });
    if (bool) {
      return;
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        SelesStillCamera.a(SelesStillCamera.this, paramCapturePhotoAsJPEGDataCallback);
      }
    });
  }
  
  public void capturePhotoAsBitmap(SelesOutInput paramSelesOutInput, SelesStillCameraInterface.CapturePhotoAsBitmapCallback paramCapturePhotoAsBitmapCallback)
  {
    capturePhotoAsBitmap(paramSelesOutInput, capturePhotoOrientation(), paramCapturePhotoAsBitmapCallback);
  }
  
  public void capturePhotoAsBitmap(final SelesOutInput paramSelesOutInput, final ImageOrientation paramImageOrientation, final SelesStillCameraInterface.CapturePhotoAsBitmapCallback paramCapturePhotoAsBitmapCallback)
  {
    capturePhotoAsJPEGData(new SelesStillCameraInterface.CapturePhotoAsJPEGDataCallback()
    {
      public void onCapturePhotoAsJPEGData(final byte[] paramAnonymousArrayOfByte)
      {
        if (paramAnonymousArrayOfByte == null)
        {
          if (paramCapturePhotoAsBitmapCallback != null) {
            paramCapturePhotoAsBitmapCallback.onCapturePhotoAsBitmap(null);
          }
          return;
        }
        ThreadHelper.runThread(new Runnable()
        {
          public void run()
          {
            SelesStillCamera.a(SelesStillCamera.this, paramAnonymousArrayOfByte, SelesStillCamera.5.this.b, SelesStillCamera.5.this.c, SelesStillCamera.5.this.a);
          }
        });
      }
    });
  }
  
  private void a(final SelesStillCameraInterface.CapturePhotoAsJPEGDataCallback paramCapturePhotoAsJPEGDataCallback)
  {
    try
    {
      inputCamera().takePicture(getShutterCallback(), null, new Camera.PictureCallback()
      {
        public void onPictureTaken(byte[] paramAnonymousArrayOfByte, Camera paramAnonymousCamera)
        {
          SelesStillCamera.b(SelesStillCamera.this, false);
          SelesStillCamera.this.onTakePictured(paramAnonymousArrayOfByte);
          if (paramCapturePhotoAsJPEGDataCallback != null) {
            paramCapturePhotoAsJPEGDataCallback.onCapturePhotoAsJPEGData(paramAnonymousArrayOfByte);
          }
        }
      });
    }
    catch (RuntimeException localRuntimeException)
    {
      TLog.e(localRuntimeException, "takePictureAtAsync", new Object[0]);
      ThreadHelper.post(new Runnable()
      {
        public void run()
        {
          SelesStillCamera.b(SelesStillCamera.this, false);
          SelesStillCamera.this.onTakePictureFailed();
          if (paramCapturePhotoAsJPEGDataCallback != null) {
            paramCapturePhotoAsJPEGDataCallback.onCapturePhotoAsJPEGData(null);
          }
        }
      });
    }
  }
  
  private void a(byte[] paramArrayOfByte, final SelesOutInput paramSelesOutInput, final ImageOrientation paramImageOrientation, final SelesStillCameraInterface.CapturePhotoAsBitmapCallback paramCapturePhotoAsBitmapCallback)
  {
    final Bitmap localBitmap = decodeToBitmapAtAsync(paramArrayOfByte);
    if (localBitmap == null)
    {
      if (paramCapturePhotoAsBitmapCallback != null) {
        ThreadHelper.post(new Runnable()
        {
          public void run()
          {
            if (paramCapturePhotoAsBitmapCallback != null) {
              paramCapturePhotoAsBitmapCallback.onCapturePhotoAsBitmap(null);
            }
          }
        });
      }
      return;
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        SelesStillCamera.a(SelesStillCamera.this, localBitmap, paramSelesOutInput, paramImageOrientation, paramCapturePhotoAsBitmapCallback);
      }
    });
  }
  
  private void a(Bitmap paramBitmap, SelesOutInput paramSelesOutInput, ImageOrientation paramImageOrientation, final SelesStillCameraInterface.CapturePhotoAsBitmapCallback paramCapturePhotoAsBitmapCallback)
  {
    if ((paramSelesOutInput != null) && (paramBitmap != null))
    {
      localObject = BitmapHelper.computerScaleSize(paramBitmap, getOutputSize(), false, false);
      SelesPicture localSelesPicture = new SelesPicture(paramBitmap, false);
      localSelesPicture.setScaleSize((TuSdkSize)localObject);
      float f1 = getRegionRatio();
      if (getRegionRatio() == 0.0F) {
        f1 = getOutputSize().getRatioFloat();
      }
      Rect localRect = RectHelper.computerMinMaxSideInRegionRatio(localSelesPicture.getScaleSize(), f1);
      localSelesPicture.setOutputRect(localRect);
      localSelesPicture.setInputRotation(paramImageOrientation);
      localSelesPicture.addTarget(paramSelesOutInput, 0);
      localSelesPicture.processImage();
      paramBitmap = localSelesPicture.imageFromCurrentlyProcessedOutput();
    }
    final Object localObject = paramBitmap;
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        if (paramCapturePhotoAsBitmapCallback != null) {
          paramCapturePhotoAsBitmapCallback.onCapturePhotoAsBitmap(localObject);
        }
      }
    });
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesStillCamera.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */