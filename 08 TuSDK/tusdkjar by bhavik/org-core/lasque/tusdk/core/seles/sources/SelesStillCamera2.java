package org.lasque.tusdk.core.seles.sources;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraCaptureSession.StateCallback;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.util.Size;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.Camera2Helper;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraAntibanding;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraAutoFocus;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFlash;
import org.lasque.tusdk.core.utils.hardware.TuSdkFace;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

@TargetApi(21)
public abstract class SelesStillCamera2
  extends SelesVideoCamera2
  implements SelesStillCameraInterface
{
  private CameraDevice b;
  private CameraCaptureSession c;
  private CaptureRequest.Builder d;
  private CaptureRequest.Builder e;
  private ImageReader f;
  private boolean g;
  private TuSdkSize h;
  private boolean i = false;
  private boolean j;
  private boolean k;
  private int l;
  private float m = 0.75F;
  private CameraConfigs.CameraFlash n;
  private CameraConfigs.CameraAutoFocus o;
  private long p;
  private CameraConfigs.CameraAntibanding q;
  private boolean r;
  private SelesBaseCameraInterface.TuSdkAutoFocusCallback s;
  private CameraDevice.StateCallback t = new CameraDevice.StateCallback()
  {
    public void onOpened(CameraDevice paramAnonymousCameraDevice)
    {
      TLog.d("mCameraStateCallback : %s [Thread: %s]", new Object[] { "onOpened", Thread.currentThread().getName() });
      SelesStillCamera2.a(SelesStillCamera2.this, paramAnonymousCameraDevice);
      SelesStillCamera2.a(SelesStillCamera2.this);
    }
    
    public void onDisconnected(CameraDevice paramAnonymousCameraDevice)
    {
      TLog.d("mCameraStateCallback : %s", new Object[] { "onDisconnected" });
    }
    
    public void onError(CameraDevice paramAnonymousCameraDevice, int paramAnonymousInt)
    {
      TLog.d("mCameraStateCallback : %s [%s]", new Object[] { "onError", Integer.valueOf(paramAnonymousInt) });
    }
    
    public void onClosed(CameraDevice paramAnonymousCameraDevice)
    {
      super.onClosed(paramAnonymousCameraDevice);
      TLog.d("mCameraStateCallback : %s", new Object[] { "onClosed" });
    }
  };
  private CameraCaptureSession.StateCallback u = new CameraCaptureSession.StateCallback()
  {
    public void onConfigured(CameraCaptureSession paramAnonymousCameraCaptureSession)
    {
      try
      {
        SelesStillCamera2.a(SelesStillCamera2.this, paramAnonymousCameraCaptureSession);
        paramAnonymousCameraCaptureSession.setRepeatingRequest(SelesStillCamera2.b(SelesStillCamera2.this).build(), SelesStillCamera2.this.getPreviewSessionCallback(), SelesStillCamera2.this.mHandler);
      }
      catch (CameraAccessException localCameraAccessException)
      {
        TLog.e(localCameraAccessException, "mSessionPreviewStateCallback onConfigured error", new Object[0]);
      }
    }
    
    public void onConfigureFailed(CameraCaptureSession paramAnonymousCameraCaptureSession)
    {
      TLog.d("mSessionPreviewStateCallback : %s", new Object[] { "onConfigureFailed" });
    }
  };
  private CameraCaptureSession.CaptureCallback v = new CameraCaptureSession.CaptureCallback()
  {
    public void onCaptureCompleted(CameraCaptureSession paramAnonymousCameraCaptureSession, CaptureRequest paramAnonymousCaptureRequest, TotalCaptureResult paramAnonymousTotalCaptureResult)
    {
      super.onCaptureCompleted(paramAnonymousCameraCaptureSession, paramAnonymousCaptureRequest, paramAnonymousTotalCaptureResult);
      SelesStillCamera2.a(SelesStillCamera2.this, paramAnonymousTotalCaptureResult);
      SelesStillCamera2.b(SelesStillCamera2.this, paramAnonymousTotalCaptureResult);
    }
  };
  private Integer w;
  private Integer x;
  private CameraCaptureSession.StateCallback y = new CameraCaptureSession.StateCallback()
  {
    public void onConfigured(CameraCaptureSession paramAnonymousCameraCaptureSession)
    {
      if (SelesStillCamera2.c(SelesStillCamera2.this) == null) {
        return;
      }
      try
      {
        paramAnonymousCameraCaptureSession.capture(SelesStillCamera2.c(SelesStillCamera2.this).build(), null, SelesStillCamera2.this.mHandler);
      }
      catch (Exception localException)
      {
        TLog.e(localException, "mSessionCaptureStateCallback onConfigured", new Object[0]);
      }
    }
    
    public void onConfigureFailed(CameraCaptureSession paramAnonymousCameraCaptureSession)
    {
      TLog.d("mSessionCaptureStateCallback onConfigureFailed", new Object[0]);
    }
  };
  private CameraCaptureSession.CaptureCallback z = new CameraCaptureSession.CaptureCallback()
  {
    public void onCaptureCompleted(CameraCaptureSession paramAnonymousCameraCaptureSession, CaptureRequest paramAnonymousCaptureRequest, TotalCaptureResult paramAnonymousTotalCaptureResult)
    {
      super.onCaptureCompleted(paramAnonymousCameraCaptureSession, paramAnonymousCaptureRequest, paramAnonymousTotalCaptureResult);
      SelesStillCamera2.this.updatePreview();
    }
  };
  
  public CameraDevice getCameraDevice()
  {
    return this.b;
  }
  
  public CaptureRequest.Builder getPreviewBuilder()
  {
    return this.d;
  }
  
  public boolean isCapturePhoto()
  {
    return this.g;
  }
  
  private void a(boolean paramBoolean)
  {
    this.g = paramBoolean;
    onCapturePhotoStateChanged(this.g);
  }
  
  private int a()
  {
    TuSdkSize localTuSdkSize = ContextUtils.getScreenSize(getContext());
    if ((this.l < 1) || (this.l > localTuSdkSize.maxSide())) {
      this.l = localTuSdkSize.maxSide();
    }
    return this.l;
  }
  
  public void setPreviewMaxSize(int paramInt)
  {
    this.l = paramInt;
  }
  
  public final TuSdkSize getOutputSize()
  {
    if (this.h == null) {
      this.h = ContextUtils.getScreenSize(getContext());
    }
    return this.h;
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return;
    }
    this.h = paramTuSdkSize.limitSize();
    c();
  }
  
  private ImageReader b()
  {
    if (this.f == null) {
      this.f = buildJpegImageReader();
    }
    return this.f;
  }
  
  private void c()
  {
    if (this.c != null) {
      try
      {
        this.c.stopRepeating();
      }
      catch (CameraAccessException localCameraAccessException)
      {
        TLog.e(localCameraAccessException, "changeOuputSize", new Object[0]);
      }
    }
    if (this.f != null) {
      this.f.close();
    }
    this.f = buildJpegImageReader();
    continuePreview();
  }
  
  protected ImageReader buildJpegImageReader()
  {
    if (getCameraCharacter() == null) {
      return this.f;
    }
    StreamConfigurationMap localStreamConfigurationMap = Camera2Helper.streamConfigurationMap(getCameraCharacter());
    Size localSize = Camera2Helper.pictureOptimalSize(getContext(), localStreamConfigurationMap.getOutputSizes(256), getOutputSize());
    if (localSize == null) {
      return null;
    }
    ImageReader localImageReader = ImageReader.newInstance(localSize.getWidth(), localSize.getHeight(), 256, 1);
    return localImageReader;
  }
  
  public float getRegionRatio()
  {
    return getOutputSize().getRatioFloat();
  }
  
  public boolean isUnifiedParameters()
  {
    return this.i;
  }
  
  public void setUnifiedParameters(boolean paramBoolean)
  {
    this.i = paramBoolean;
  }
  
  public boolean isAutoReleaseAfterCaptured()
  {
    return this.j;
  }
  
  public void setAutoReleaseAfterCaptured(boolean paramBoolean)
  {
    this.j = paramBoolean;
  }
  
  public boolean isDisableMirrorFrontFacing()
  {
    return this.k;
  }
  
  public void setDisableMirrorFrontFacing(boolean paramBoolean)
  {
    this.k = paramBoolean;
  }
  
  public float getPreviewEffectScale()
  {
    return this.m;
  }
  
  public void setPreviewEffectScale(float paramFloat)
  {
    if (paramFloat <= 0.0F) {
      return;
    }
    if (paramFloat > 1.0F) {
      this.m = 1.0F;
    }
    this.m = paramFloat;
  }
  
  public long getLastFocusTime()
  {
    return this.p;
  }
  
  public boolean isEnableFaceTrace()
  {
    return _isEnableFaceTrace();
  }
  
  public void setEnableFaceTrace(boolean paramBoolean)
  {
    this.r = paramBoolean;
    if (paramBoolean) {
      g();
    } else {
      h();
    }
  }
  
  public boolean _isEnableFaceTrace()
  {
    return this.r;
  }
  
  public CameraConfigs.CameraFlash getFlashMode()
  {
    if (getCameraCharacter() == null) {
      return CameraConfigs.CameraFlash.Off;
    }
    if (this.d != null) {
      return Camera2Helper.getFlashMode(this.d);
    }
    if (this.n == null) {
      this.n = CameraConfigs.CameraFlash.Off;
    }
    return this.n;
  }
  
  public void setFlashMode(CameraConfigs.CameraFlash paramCameraFlash)
  {
    if (paramCameraFlash == null) {
      return;
    }
    this.n = paramCameraFlash;
    if ((!canSupportFlash()) || (this.d == null)) {
      return;
    }
    Camera2Helper.setFlashMode(this.d, paramCameraFlash);
    updatePreview();
  }
  
  public boolean canSupportFlash()
  {
    if ((getCameraCharacter() == null) || (!Camera2Helper.canSupportFlash(getContext()))) {
      return false;
    }
    return Camera2Helper.supportFlash(getCameraCharacter());
  }
  
  public void autoMetering(PointF paramPointF) {}
  
  public void setFocusMode(CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF)
  {
    if (paramCameraAutoFocus == null) {
      return;
    }
    this.o = paramCameraAutoFocus;
    if ((getCameraCharacter() == null) || (this.d == null)) {
      return;
    }
    Camera2Helper.setFocusMode(getCameraCharacter(), this.d, this.o, getCenterIfNull(paramPointF), this.mOutputRotation);
    updatePreview();
  }
  
  public void setFocusPoint(PointF paramPointF)
  {
    if ((getCameraCharacter() == null) || (this.d == null)) {
      return;
    }
    Camera2Helper.setFocusPoint(getCameraCharacter(), this.d, getCenterIfNull(paramPointF), this.mOutputRotation);
    updatePreview();
  }
  
  public CameraConfigs.CameraAutoFocus getFocusMode()
  {
    if (this.d == null) {
      return this.o;
    }
    return Camera2Helper.focusModeType(this.d);
  }
  
  public boolean canSupportAutoFocus()
  {
    if (getCameraCharacter() == null) {
      return false;
    }
    return (Camera2Helper.canSupportAutofocus(getContext())) && (Camera2Helper.canSupportAutofocus(getCameraCharacter()));
  }
  
  public void cancelAutoFocus()
  {
    if ((this.d == null) || (!canSupportAutoFocus())) {
      return;
    }
    this.d.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(2));
    updatePreview();
  }
  
  public void autoFocus(CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF, SelesBaseCameraInterface.TuSdkAutoFocusCallback paramTuSdkAutoFocusCallback)
  {
    setFocusMode(paramCameraAutoFocus, paramPointF);
    autoFocus(paramTuSdkAutoFocusCallback);
  }
  
  public void autoFocus(SelesBaseCameraInterface.TuSdkAutoFocusCallback paramTuSdkAutoFocusCallback)
  {
    if (!canSupportAutoFocus()) {
      return;
    }
    this.s = paramTuSdkAutoFocusCallback;
    this.p = Calendar.getInstance().getTimeInMillis();
  }
  
  protected PointF getCenterIfNull(PointF paramPointF)
  {
    if (paramPointF == null) {
      paramPointF = new PointF(0.5F, 0.5F);
    }
    return paramPointF;
  }
  
  protected abstract void playSystemShutter();
  
  public void setAntibandingMode(CameraConfigs.CameraAntibanding paramCameraAntibanding) {}
  
  public CameraConfigs.CameraAntibanding getAntiBandingMode()
  {
    return this.q;
  }
  
  public SelesStillCamera2(Context paramContext, CameraConfigs.CameraFacing paramCameraFacing)
  {
    super(paramContext, paramCameraFacing);
    if (ContextUtils.getScreenSize(paramContext).maxSide() < 1000) {
      this.m = 0.85F;
    } else {
      this.m = 0.75F;
    }
  }
  
  protected TuSdkSize computerPreviewOptimalSize()
  {
    StreamConfigurationMap localStreamConfigurationMap = Camera2Helper.streamConfigurationMap(getCameraCharacter());
    Size localSize = Camera2Helper.previewOptimalSize(getContext(), localStreamConfigurationMap.getOutputSizes(SurfaceTexture.class), a(), getPreviewEffectScale());
    return Camera2Helper.createSize(localSize);
  }
  
  protected void onCameraStarted()
  {
    super.onCameraStarted();
    g();
  }
  
  public void stopCameraCapture()
  {
    super.stopCameraCapture();
    this.g = false;
    this.o = null;
    this.d = null;
    this.e = null;
    this.s = null;
    if (this.c != null)
    {
      this.c.close();
      this.c = null;
    }
    if (this.b != null)
    {
      this.b.close();
      this.b = null;
    }
    if (this.f != null)
    {
      this.f.close();
      this.f = null;
    }
  }
  
  public void resumeCameraCapture()
  {
    updatePreview();
    super.resumeCameraCapture();
    g();
  }
  
  public void pauseCameraCapture()
  {
    super.pauseCameraCapture();
    if (this.c != null) {
      try
      {
        this.c.stopRepeating();
      }
      catch (CameraAccessException localCameraAccessException)
      {
        TLog.e(localCameraAccessException, "pauseCameraCapture", new Object[0]);
      }
    }
  }
  
  protected void onCapturePhotoStateChanged(boolean paramBoolean) {}
  
  protected CameraDevice.StateCallback getCameraStateCallback()
  {
    return this.t;
  }
  
  private List<Surface> d()
  {
    ArrayList localArrayList = new ArrayList();
    if (getPreviewSurface() != null) {
      localArrayList.add(getPreviewSurface());
    }
    if (b() != null) {
      localArrayList.add(b().getSurface());
    }
    return localArrayList;
  }
  
  private void e()
  {
    try
    {
      this.d = this.b.createCaptureRequest(1);
      this.d.addTarget(getPreviewSurface());
      this.d.set(CaptureRequest.CONTROL_MODE, Integer.valueOf(1));
      this.d.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(1));
      this.d.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(1));
      this.d.set(CaptureRequest.CONTROL_AWB_MODE, Integer.valueOf(1));
      if (_isEnableFaceTrace()) {
        this.d.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, Integer.valueOf(1));
      }
      onCounfigPreview(this.d);
      this.b.createCaptureSession(d(), getSessionPreviewStateCallback(), this.mHandler);
    }
    catch (CameraAccessException localCameraAccessException)
    {
      TLog.e(localCameraAccessException, "startPreview Error", new Object[0]);
    }
  }
  
  protected void onCounfigPreview(CaptureRequest.Builder paramBuilder) {}
  
  protected CameraCaptureSession.StateCallback getSessionPreviewStateCallback()
  {
    return this.u;
  }
  
  protected CameraCaptureSession.CaptureCallback getPreviewSessionCallback()
  {
    return this.v;
  }
  
  private void f()
  {
    this.w = null;
    this.x = null;
  }
  
  private void a(TotalCaptureResult paramTotalCaptureResult)
  {
    if ((this.s == null) || (paramTotalCaptureResult == null)) {
      return;
    }
    Integer localInteger1 = (Integer)paramTotalCaptureResult.get(CaptureResult.CONTROL_AF_MODE);
    if ((localInteger1 == null) || (localInteger1.intValue() == 0)) {
      return;
    }
    final Integer localInteger2 = (Integer)paramTotalCaptureResult.get(CaptureResult.CONTROL_AF_STATE);
    Integer localInteger3 = (Integer)paramTotalCaptureResult.get(CaptureResult.CONTROL_AE_STATE);
    if ((localInteger2 == null) || (localInteger2.intValue() == 0))
    {
      this.d.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(1));
      updatePreview();
      return;
    }
    if ((localInteger2 == null) || (localInteger3 == null) || (localInteger2.intValue() == 0) || (localInteger3.intValue() == 0) || ((this.w == localInteger2) && (this.x == localInteger3))) {
      return;
    }
    this.w = localInteger2;
    this.x = localInteger3;
    int i1 = 0;
    switch (localInteger3.intValue())
    {
    case 2: 
    case 3: 
    case 4: 
    case 5: 
      i1 = 1;
      break;
    }
    if (i1 == 0) {
      return;
    }
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        SelesStillCamera2.this.observableAutofocus(localInteger2.intValue());
      }
    });
  }
  
  protected void observableAutofocus(int paramInt)
  {
    if ((paramInt == 0) || (this.s == null)) {
      return;
    }
    switch (paramInt)
    {
    case 1: 
    case 3: 
      break;
    case 2: 
    case 4: 
      b(true);
      break;
    case 5: 
    case 6: 
      b(false);
      break;
    }
  }
  
  private void b(boolean paramBoolean)
  {
    if (this.s == null) {
      return;
    }
    this.s.onAutoFocus(paramBoolean, this);
    this.s = null;
    f();
    this.d.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(0));
    this.d.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, Integer.valueOf(0));
    updatePreview();
  }
  
  protected void updatePreview()
  {
    if (this.c == null) {
      return;
    }
    try
    {
      this.c.setRepeatingRequest(this.d.build(), getPreviewSessionCallback(), this.mHandler);
    }
    catch (Exception localException)
    {
      TLog.e(localException, "updatePreview error", new Object[0]);
    }
  }
  
  protected void continuePreview()
  {
    if (this.b == null) {
      return;
    }
    try
    {
      this.b.createCaptureSession(d(), getSessionPreviewStateCallback(), this.mHandler);
    }
    catch (Exception localException)
    {
      TLog.e(localException, "continuePreview error", new Object[0]);
    }
  }
  
  public ImageOrientation capturePhotoOrientation()
  {
    if ((!isDisableMirrorFrontFacing()) || (isBackFacingCameraPresent()) || (!isHorizontallyMirrorFrontFacingCamera())) {
      return this.mOutputRotation;
    }
    return computerOutputOrientation(getContext(), getCameraCharacter(), isHorizontallyMirrorRearFacingCamera(), false, getOutputImageOrientation());
  }
  
  private void g()
  {
    if ((this.c == null) || (this.d == null) || (!isCapturing()) || (!_isEnableFaceTrace()) || (!Camera2Helper.canSupportFaceDetection(getCameraCharacter()))) {
      return;
    }
    this.d.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, Integer.valueOf(1));
    updatePreview();
  }
  
  private void h()
  {
    if ((this.c == null) || (this.d == null)) {
      return;
    }
    this.d.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, Integer.valueOf(0));
    updatePreview();
  }
  
  private void b(TotalCaptureResult paramTotalCaptureResult)
  {
    Integer localInteger = (Integer)paramTotalCaptureResult.get(CaptureResult.STATISTICS_FACE_DETECT_MODE);
    if ((localInteger == null) || (localInteger.intValue() == 0)) {
      return;
    }
    Face[] arrayOfFace = (Face[])paramTotalCaptureResult.get(CaptureResult.STATISTICS_FACES);
    final List localList = Camera2Helper.transforFaces(getCameraCharacter(), this.mInputTextureSize, arrayOfFace, this.mOutputRotation);
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        SelesStillCamera2.this.onCameraFaceDetection(localList, SelesStillCamera2.this.mInputTextureSize.transforOrientation(SelesStillCamera2.this.mOutputRotation));
      }
    });
  }
  
  public void onCameraFaceDetection(List<TuSdkFace> paramList, TuSdkSize paramTuSdkSize) {}
  
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
  
  protected Bitmap decodeToBitmapAtAsync(byte[] paramArrayOfByte)
  {
    Bitmap localBitmap = BitmapHelper.imageDecode(paramArrayOfByte, true);
    return localBitmap;
  }
  
  public void capturePhotoAsJPEGData(final SelesStillCameraInterface.CapturePhotoAsJPEGDataCallback paramCapturePhotoAsJPEGDataCallback)
  {
    final boolean bool = (!isCapturing()) || (this.g);
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
        SelesStillCamera2.a(SelesStillCamera2.this, true);
      }
    });
    if (bool) {
      return;
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        SelesStillCamera2.a(SelesStillCamera2.this, paramCapturePhotoAsJPEGDataCallback);
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
            SelesStillCamera2.a(SelesStillCamera2.this, paramAnonymousArrayOfByte, SelesStillCamera2.8.this.b, SelesStillCamera2.8.this.c, SelesStillCamera2.8.this.a);
          }
        });
      }
    });
  }
  
  protected CameraCaptureSession.StateCallback getSessionCaptureStateCallback()
  {
    return this.y;
  }
  
  protected void onImageCaptured(ImageReader paramImageReader, final SelesStillCameraInterface.CapturePhotoAsJPEGDataCallback paramCapturePhotoAsJPEGDataCallback)
  {
    Image localImage = paramImageReader.acquireLatestImage();
    ByteBuffer localByteBuffer = localImage.getPlanes()[0].getBuffer();
    final byte[] arrayOfByte = new byte[localByteBuffer.remaining()];
    localByteBuffer.get(arrayOfByte);
    localImage.close();
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        SelesStillCamera2.a(SelesStillCamera2.this, false);
        SelesStillCamera2.this.onTakePictured(arrayOfByte);
        if (paramCapturePhotoAsJPEGDataCallback != null) {
          paramCapturePhotoAsJPEGDataCallback.onCapturePhotoAsJPEGData(arrayOfByte);
        }
      }
    });
  }
  
  private void a(final SelesStillCameraInterface.CapturePhotoAsJPEGDataCallback paramCapturePhotoAsJPEGDataCallback)
  {
    if ((b() == null) || (this.b == null) || (this.c == null))
    {
      b(paramCapturePhotoAsJPEGDataCallback);
      return;
    }
    b().setOnImageAvailableListener(new ImageReader.OnImageAvailableListener()
    {
      public void onImageAvailable(final ImageReader paramAnonymousImageReader)
      {
        SelesStillCamera2.this.playSystemShutter();
        ThreadHelper.runThread(new Runnable()
        {
          public void run()
          {
            SelesStillCamera2.this.onImageCaptured(paramAnonymousImageReader, SelesStillCamera2.11.this.a);
          }
        });
      }
    }, this.mHandler);
    try
    {
      this.e = this.b.createCaptureRequest(2);
      this.e.addTarget(b().getSurface());
      this.e.set(CaptureRequest.CONTROL_MODE, Integer.valueOf(1));
      i();
      onConfigCapture(this.e);
      this.c.stopRepeating();
      this.c.capture(this.e.build(), this.z, this.mHandler);
    }
    catch (Exception localException)
    {
      TLog.e(localException, "takePictureAtAsync", new Object[0]);
      b(paramCapturePhotoAsJPEGDataCallback);
    }
  }
  
  protected void onConfigCapture(CaptureRequest.Builder paramBuilder) {}
  
  private void i()
  {
    Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
    Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.CONTROL_AWB_MODE);
    Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.SENSOR_EXPOSURE_TIME);
    Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION);
    Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.LENS_FOCUS_DISTANCE);
    Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.CONTROL_EFFECT_MODE);
    Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.SENSOR_SENSITIVITY);
    Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.CONTROL_AF_REGIONS);
    Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.CONTROL_AE_REGIONS);
    Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.CONTROL_SCENE_MODE);
    Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.SCALER_CROP_REGION);
  }
  
  private void b(final SelesStillCameraInterface.CapturePhotoAsJPEGDataCallback paramCapturePhotoAsJPEGDataCallback)
  {
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        SelesStillCamera2.a(SelesStillCamera2.this, false);
        SelesStillCamera2.this.onTakePictureFailed();
        if (paramCapturePhotoAsJPEGDataCallback != null) {
          paramCapturePhotoAsJPEGDataCallback.onCapturePhotoAsJPEGData(null);
        }
      }
    });
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
        SelesStillCamera2.a(SelesStillCamera2.this, localBitmap, paramSelesOutInput, paramImageOrientation, paramCapturePhotoAsBitmapCallback);
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
      Rect localRect = RectHelper.computerMinMaxSideInRegionRatio(localSelesPicture.getScaleSize(), getRegionRatio());
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


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesStillCamera2.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */