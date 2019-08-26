package org.lasque.tusdk.core.media.camera;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;

public final class TuSdkCameraImpl
  implements TuSdkCamera
{
  private final SelesSurfaceReceiver a = new SelesSurfaceReceiver();
  private TuSdkCamera.TuSdkCameraStatus b;
  private TuSdkCamera.TuSdkCameraListener c;
  private Camera.PreviewCallback d;
  private SurfaceTexture.OnFrameAvailableListener e;
  private TuSdkCameraBuilder f;
  private TuSdkCameraParameters g;
  private TuSdkCameraOrientation h;
  private TuSdkCameraFocus i;
  private TuSdkCameraSize j;
  private TuSdkCameraShot k;
  private boolean l = false;
  private boolean m = false;
  private float n;
  private Camera.PreviewCallback o = new Camera.PreviewCallback()
  {
    public void onPreviewFrame(byte[] paramAnonymousArrayOfByte, Camera paramAnonymousCamera)
    {
      if (TuSdkCamera.TuSdkCameraStatus.CAMERA_PAUSE_PREVIEW == TuSdkCameraImpl.m(TuSdkCameraImpl.this)) {
        return;
      }
      if (TuSdkCameraImpl.h(TuSdkCameraImpl.this) != null)
      {
        TuSdkCameraImpl.h(TuSdkCameraImpl.this).onPreviewFrame(paramAnonymousArrayOfByte, paramAnonymousCamera);
        return;
      }
      paramAnonymousCamera.addCallbackBuffer(paramAnonymousArrayOfByte);
    }
  };
  private SurfaceTexture.OnFrameAvailableListener p = new SurfaceTexture.OnFrameAvailableListener()
  {
    public void onFrameAvailable(SurfaceTexture paramAnonymousSurfaceTexture)
    {
      if (TuSdkCamera.TuSdkCameraStatus.CAMERA_PAUSE_PREVIEW == TuSdkCameraImpl.m(TuSdkCameraImpl.this)) {
        return;
      }
      if (TuSdkCamera.TuSdkCameraStatus.CAMERA_START == TuSdkCameraImpl.m(TuSdkCameraImpl.this)) {
        TuSdkCameraImpl.a(TuSdkCameraImpl.this, TuSdkCamera.TuSdkCameraStatus.CAMERA_START_PREVIEW);
      }
      if (TuSdkCameraImpl.a(TuSdkCameraImpl.this, paramAnonymousSurfaceTexture)) {
        return;
      }
      if (paramAnonymousSurfaceTexture != null) {
        paramAnonymousSurfaceTexture.updateTexImage();
      }
    }
  };
  private GLSurfaceView.Renderer q = new GLSurfaceView.Renderer()
  {
    public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig)
    {
      GLES20.glDisable(2929);
      TuSdkCameraImpl.this.initInGLThread();
    }
    
    public void onSurfaceChanged(GL10 paramAnonymousGL10, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      GLES20.glViewport(0, 0, paramAnonymousInt1, paramAnonymousInt2);
    }
    
    public void onDrawFrame(GL10 paramAnonymousGL10)
    {
      GLES20.glClear(16640);
      TuSdkCameraImpl.this.newFrameReadyInGLThread();
    }
  };
  
  public TuSdkCamera.TuSdkCameraStatus getCameraStatus()
  {
    return this.b;
  }
  
  public void setCameraListener(TuSdkCamera.TuSdkCameraListener paramTuSdkCameraListener)
  {
    this.c = paramTuSdkCameraListener;
  }
  
  public void setPreviewCallback(Camera.PreviewCallback paramPreviewCallback)
  {
    if (a("setPreviewCallback")) {
      return;
    }
    this.d = paramPreviewCallback;
  }
  
  public void setSurfaceListener(SurfaceTexture.OnFrameAvailableListener paramOnFrameAvailableListener)
  {
    if (a("setSurfaceListener")) {
      return;
    }
    this.e = paramOnFrameAvailableListener;
  }
  
  public void setCameraBuilder(TuSdkCameraBuilder paramTuSdkCameraBuilder)
  {
    if (a("setCameraBuilder")) {
      return;
    }
    this.f = paramTuSdkCameraBuilder;
  }
  
  public void setCameraParameters(TuSdkCameraParameters paramTuSdkCameraParameters)
  {
    if (a("setCameraParameters")) {
      return;
    }
    this.g = paramTuSdkCameraParameters;
  }
  
  public void setCameraOrientation(TuSdkCameraOrientation paramTuSdkCameraOrientation)
  {
    if (a("setCameraOrientation")) {
      return;
    }
    this.h = paramTuSdkCameraOrientation;
  }
  
  public void setCameraFocus(TuSdkCameraFocus paramTuSdkCameraFocus)
  {
    if (a("setCameraFocus")) {
      return;
    }
    this.i = paramTuSdkCameraFocus;
  }
  
  public void setCameraSize(TuSdkCameraSize paramTuSdkCameraSize)
  {
    if (a("setCameraSize")) {
      return;
    }
    this.j = paramTuSdkCameraSize;
  }
  
  public void setCameraShot(TuSdkCameraShot paramTuSdkCameraShot)
  {
    if (a("setCameraShot")) {
      return;
    }
    this.k = paramTuSdkCameraShot;
  }
  
  private boolean a(String paramString)
  {
    if (this.l)
    {
      TLog.w("%s %s has released.", new Object[] { "TuSdkCameraImpl", paramString });
      return true;
    }
    if (this.m)
    {
      TLog.w("%s %s need before prepare.", new Object[] { "TuSdkCameraImpl", paramString });
      return true;
    }
    return false;
  }
  
  public boolean prepare()
  {
    if (this.m)
    {
      TLog.w("%s prepare is allready.", new Object[] { "TuSdkCameraImpl" });
      return false;
    }
    if (this.a == null)
    {
      TLog.w("%s prepare need setSurfaceHolder first.", new Object[] { "TuSdkCameraImpl" });
      return false;
    }
    this.m = true;
    if (this.f == null) {
      this.f = new TuSdkCameraBuilderImpl();
    }
    if (this.g == null) {
      this.g = new TuSdkCameraParametersImpl();
    }
    if (this.h == null) {
      this.h = new TuSdkCameraOrientationImpl();
    }
    if (this.i == null) {
      this.i = new TuSdkCameraFocusImpl();
    }
    if (this.j == null) {
      this.j = new TuSdkCameraSizeImpl();
    }
    if (this.k == null) {
      this.k = new TuSdkCameraShotImpl();
    }
    return true;
  }
  
  public boolean rotateCamera()
  {
    if (this.l)
    {
      TLog.w("%s rotateCamera has released.", new Object[] { "TuSdkCameraImpl" });
      return false;
    }
    CameraConfigs.CameraFacing localCameraFacing = CameraConfigs.CameraFacing.Back;
    if (localCameraFacing == this.f.getFacing()) {
      localCameraFacing = CameraConfigs.CameraFacing.Front;
    }
    return startPreview(localCameraFacing);
  }
  
  public CameraConfigs.CameraFacing getFacing()
  {
    return this.f.getFacing();
  }
  
  public boolean startPreview()
  {
    return startPreview(this.f.getFacing());
  }
  
  public boolean startPreview(final CameraConfigs.CameraFacing paramCameraFacing)
  {
    if (this.l)
    {
      TLog.w("%s startPreview has released.", new Object[] { "TuSdkCameraImpl" });
      return false;
    }
    if (paramCameraFacing == null)
    {
      TLog.e("%s startPreview need a CameraFacing", new Object[] { "TuSdkCameraImpl" });
      return false;
    }
    if (!this.m)
    {
      TLog.w("%s startPreview need prepare first.", new Object[] { "TuSdkCameraImpl" });
      return false;
    }
    stopPreview();
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        long l = System.currentTimeMillis();
        SurfaceTexture localSurfaceTexture;
        while ((!TuSdkCameraImpl.a(TuSdkCameraImpl.this).isInited()) || ((localSurfaceTexture = TuSdkCameraImpl.a(TuSdkCameraImpl.this).requestSurfaceTexture()) == null)) {
          if (System.currentTimeMillis() - l > 2000L)
          {
            TLog.e("%s startPreview failed, request surfaceTexture timeout: %s", new Object[] { "TuSdkCameraImpl", TuSdkCameraImpl.a(TuSdkCameraImpl.this) });
            TuSdkCameraImpl.this.stopPreview();
            return;
          }
        }
        if (!TuSdkCameraImpl.b(TuSdkCameraImpl.this).open(paramCameraFacing))
        {
          TLog.e("%s startPreview failed, can not open Camera: %s", new Object[] { "TuSdkCameraImpl", TuSdkCameraImpl.b(TuSdkCameraImpl.this) });
          TuSdkCameraImpl.this.stopPreview();
          return;
        }
        TuSdkCameraImpl.c(TuSdkCameraImpl.this).configure(TuSdkCameraImpl.b(TuSdkCameraImpl.this));
        TuSdkCameraImpl.d(TuSdkCameraImpl.this).configure(TuSdkCameraImpl.b(TuSdkCameraImpl.this));
        TuSdkCameraImpl.e(TuSdkCameraImpl.this).configure(TuSdkCameraImpl.b(TuSdkCameraImpl.this));
        TuSdkCameraImpl.f(TuSdkCameraImpl.this).configure(TuSdkCameraImpl.b(TuSdkCameraImpl.this), TuSdkCameraImpl.d(TuSdkCameraImpl.this), TuSdkCameraImpl.e(TuSdkCameraImpl.this));
        TuSdkCameraImpl.g(TuSdkCameraImpl.this).configure(TuSdkCameraImpl.b(TuSdkCameraImpl.this));
        if (TuSdkCameraImpl.h(TuSdkCameraImpl.this) != null)
        {
          TuSdkCameraImpl.i(TuSdkCameraImpl.this);
          TuSdkCameraImpl.b(TuSdkCameraImpl.this).setPreviewCallbackWithBuffer(TuSdkCameraImpl.j(TuSdkCameraImpl.this));
        }
        if (TuSdkCameraImpl.k(TuSdkCameraImpl.this) != null) {
          localSurfaceTexture.setOnFrameAvailableListener(TuSdkCameraImpl.l(TuSdkCameraImpl.this));
        }
        TuSdkCameraImpl.b(TuSdkCameraImpl.this).setPreviewTexture(localSurfaceTexture);
        if (!TuSdkCameraImpl.b(TuSdkCameraImpl.this).startPreview())
        {
          TLog.e("%s startPreview error, can not open Camera: %s", new Object[] { "TuSdkCameraImpl", TuSdkCameraImpl.b(TuSdkCameraImpl.this) });
          TuSdkCameraImpl.this.stopPreview();
          return;
        }
        TuSdkCameraImpl.a(TuSdkCameraImpl.this, TuSdkCamera.TuSdkCameraStatus.CAMERA_START);
      }
    });
    return true;
  }
  
  public void stopPreview()
  {
    if (!this.m) {
      return;
    }
    this.f.releaseCamera();
    a(TuSdkCamera.TuSdkCameraStatus.CAMERA_STOP);
  }
  
  public boolean pausePreview()
  {
    if ((!this.m) || (this.l) || ((this.b != TuSdkCamera.TuSdkCameraStatus.CAMERA_START_PREVIEW) && (this.b != TuSdkCamera.TuSdkCameraStatus.CAMERA_SHOTED)))
    {
      TLog.w("%s pausePreview had incorrect status: %s, release: %b", new Object[] { "TuSdkCameraImpl", this.b, Boolean.valueOf(this.l) });
      return false;
    }
    a(TuSdkCamera.TuSdkCameraStatus.CAMERA_PAUSE_PREVIEW);
    return true;
  }
  
  public boolean resumePreview()
  {
    if ((!this.m) || (this.l) || (this.b != TuSdkCamera.TuSdkCameraStatus.CAMERA_PAUSE_PREVIEW))
    {
      TLog.w("%s resumePreview had incorrect status: %s, release: %b", new Object[] { "TuSdkCameraImpl", this.b, Boolean.valueOf(this.l) });
      return false;
    }
    a(false);
    return true;
  }
  
  private boolean a(boolean paramBoolean)
  {
    a(TuSdkCamera.TuSdkCameraStatus.CAMERA_START_PREVIEW);
    this.f.startPreview();
    a();
    a(null);
    return true;
  }
  
  public boolean shotPhoto()
  {
    if ((!this.m) || (this.l) || (this.b != TuSdkCamera.TuSdkCameraStatus.CAMERA_START_PREVIEW))
    {
      TLog.w("%s captureImage had incorrect status: %s, release: %b", new Object[] { "TuSdkCameraImpl", this.b, Boolean.valueOf(this.l) });
      return false;
    }
    boolean bool = this.i.allowFocusToShot();
    a(TuSdkCamera.TuSdkCameraStatus.CAMERA_PREPARE_SHOT);
    if (bool) {
      this.i.autoFocus(new TuSdkCameraFocus.TuSdkCameraFocusListener()
      {
        public void onFocusStart(TuSdkCameraFocus paramAnonymousTuSdkCameraFocus) {}
        
        public void onAutoFocus(boolean paramAnonymousBoolean, TuSdkCameraFocus paramAnonymousTuSdkCameraFocus)
        {
          TuSdkCameraImpl.a(TuSdkCameraImpl.this, paramAnonymousBoolean);
        }
      });
    } else {
      b(false);
    }
    return true;
  }
  
  public void release()
  {
    if (this.l) {
      return;
    }
    this.l = true;
    stopPreview();
    this.a.destroy();
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  private void a(TuSdkCamera.TuSdkCameraStatus paramTuSdkCameraStatus)
  {
    this.b = paramTuSdkCameraStatus;
    this.g.changeStatus(paramTuSdkCameraStatus);
    this.h.changeStatus(paramTuSdkCameraStatus);
    this.i.changeStatus(paramTuSdkCameraStatus);
    this.j.changeStatus(paramTuSdkCameraStatus);
    this.k.changeStatus(paramTuSdkCameraStatus);
    if (this.c != null) {
      this.c.onStatusChanged(paramTuSdkCameraStatus, this);
    }
    if (paramTuSdkCameraStatus == TuSdkCamera.TuSdkCameraStatus.CAMERA_START_PREVIEW)
    {
      this.a.setInputRotation(this.h.previewOrientation());
      this.a.setInputSize(this.j.previewOptimalSize());
    }
  }
  
  private void a()
  {
    if ((this.d == null) || (this.f == null)) {
      return;
    }
    this.f.addCallbackBuffer(new byte[this.j.previewBufferLength()]);
    this.f.addCallbackBuffer(new byte[this.j.previewBufferLength()]);
  }
  
  private void b(boolean paramBoolean)
  {
    final TuSdkResult localTuSdkResult = new TuSdkResult();
    localTuSdkResult.imageOrientation = this.h.captureOrientation();
    localTuSdkResult.outputSize = this.j.getOutputSize();
    localTuSdkResult.imageSizeRatio = this.n;
    this.k.takeJpegPicture(localTuSdkResult, new TuSdkCameraShot.TuSdkCameraShotResultListener()
    {
      public void onShotResule(byte[] paramAnonymousArrayOfByte)
      {
        localTuSdkResult.imageData = paramAnonymousArrayOfByte;
        TuSdkCameraImpl.a(TuSdkCameraImpl.this, localTuSdkResult);
      }
    });
  }
  
  private void a(TuSdkResult paramTuSdkResult)
  {
    this.k.processData(paramTuSdkResult);
    if (paramTuSdkResult.imageData == null)
    {
      startPreview();
      return;
    }
    a(TuSdkCamera.TuSdkCameraStatus.CAMERA_SHOTED);
    if (this.k.isAutoReleaseAfterCaptured()) {
      stopPreview();
    } else {
      pausePreview();
    }
  }
  
  private boolean a(SurfaceTexture paramSurfaceTexture)
  {
    if (this.e != null)
    {
      this.e.onFrameAvailable(paramSurfaceTexture);
      return true;
    }
    return false;
  }
  
  public void addTarget(SelesContext.SelesInput paramSelesInput, int paramInt)
  {
    this.a.addTarget(paramSelesInput, paramInt);
  }
  
  public void removeTarget(SelesContext.SelesInput paramSelesInput)
  {
    this.a.removeTarget(paramSelesInput);
  }
  
  public GLSurfaceView.Renderer getExtenalRenderer()
  {
    return this.q;
  }
  
  public void initInGLThread()
  {
    this.a.initInGLThread();
  }
  
  public void updateSurfaceTexImage()
  {
    if (this.l) {
      return;
    }
    if (TuSdkCamera.TuSdkCameraStatus.CAMERA_START_PREVIEW == this.b)
    {
      this.a.forceUpdateSurfaceTexImage();
      return;
    }
    this.a.updateSurfaceTexImage();
  }
  
  public void newFrameReadyInGLThread(long paramLong)
  {
    if (this.l) {
      return;
    }
    this.a.newFrameReadyInGLThread(paramLong);
  }
  
  public long newFrameReadyInGLThread()
  {
    if (this.l) {
      return -1L;
    }
    long l1 = System.nanoTime();
    updateSurfaceTexImage();
    newFrameReadyInGLThread(l1);
    return l1;
  }
  
  public void setShotRegionRatio(float paramFloat)
  {
    this.n = paramFloat;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\camera\TuSdkCameraImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */