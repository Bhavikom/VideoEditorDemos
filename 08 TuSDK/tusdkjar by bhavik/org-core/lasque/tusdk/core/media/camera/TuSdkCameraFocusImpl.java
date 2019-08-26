package org.lasque.tusdk.core.media.camera;

import android.annotation.TargetApi;
import android.graphics.PointF;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.AutoFocusMoveCallback;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.Parameters;
import android.os.Build.VERSION;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraAutoFocus;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkCameraFocusImpl
  implements TuSdkCameraFocus
{
  public static final long FOCUS_SAMPLING_DISTANCE_MS = 2000L;
  private TuSdkCamera.TuSdkCameraStatus a;
  private boolean b;
  private boolean c;
  private TuSdkCameraFocus.TuSdkCameraFocusFaceListener d;
  private Runnable e = null;
  private boolean f = false;
  private long g;
  private CameraConfigs.CameraAutoFocus h;
  private boolean i;
  private boolean j;
  private TuSdkCameraFocus.TuSdkCameraFocusListener k;
  private TuSdkCameraBuilder l;
  private TuSdkCameraOrientation m;
  private TuSdkCameraSize n;
  
  public void setFaceListener(TuSdkCameraFocus.TuSdkCameraFocusFaceListener paramTuSdkCameraFocusFaceListener)
  {
    this.d = paramTuSdkCameraFocusFaceListener;
    if (paramTuSdkCameraFocusFaceListener == null) {
      b();
    } else {
      a();
    }
  }
  
  @TargetApi(14)
  private void a()
  {
    if ((!this.c) || (this.d == null) || (this.b) || (this.a != TuSdkCamera.TuSdkCameraStatus.CAMERA_START_PREVIEW)) {
      return;
    }
    Camera localCamera = i();
    final TuSdkSize localTuSdkSize = g();
    if ((localCamera == null) || (localTuSdkSize == null)) {
      return;
    }
    b();
    this.b = true;
    localCamera.setFaceDetectionListener(new Camera.FaceDetectionListener()
    {
      public void onFaceDetection(Camera.Face[] paramAnonymousArrayOfFace, Camera paramAnonymousCamera)
      {
        List localList = CameraHelper.transforFaces(paramAnonymousArrayOfFace, TuSdkCameraFocusImpl.a(TuSdkCameraFocusImpl.this));
        TuSdkCameraFocusImpl.b(TuSdkCameraFocusImpl.this).onFocusFaceDetection(localList, localTuSdkSize.transforOrientation(TuSdkCameraFocusImpl.a(TuSdkCameraFocusImpl.this)));
      }
    });
    try
    {
      localCamera.startFaceDetection();
    }
    catch (Exception localException)
    {
      this.b = false;
      TLog.e(localException, "%s startFaceDetection failed, ignore and try again.", new Object[] { "TuSdkCameraFocusImpl" });
    }
  }
  
  @TargetApi(14)
  private void b()
  {
    Camera localCamera = i();
    if ((localCamera == null) || (!this.b)) {
      return;
    }
    this.b = false;
    try
    {
      localCamera.setFaceDetectionListener(null);
      localCamera.stopFaceDetection();
    }
    catch (Exception localException)
    {
      TLog.e(localException, "%s stopFaceDetection failed, ignore and try again.", new Object[] { "TuSdkCameraFocusImpl" });
    }
  }
  
  public void autoMetering(PointF paramPointF)
  {
    Camera.Parameters localParameters = h();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setFocusPoint(localParameters, a(paramPointF), f());
    a(localParameters);
  }
  
  public boolean isDisableFocusBeep()
  {
    return this.i;
  }
  
  public void setDisableFocusBeep(boolean paramBoolean)
  {
    this.i = paramBoolean;
  }
  
  public boolean isDisableContinueFoucs()
  {
    return this.j;
  }
  
  public void setDisableContinueFoucs(boolean paramBoolean)
  {
    this.j = paramBoolean;
  }
  
  public void setFocusListener(TuSdkCameraFocus.TuSdkCameraFocusListener paramTuSdkCameraFocusListener)
  {
    this.k = paramTuSdkCameraFocusListener;
  }
  
  public void setFocusMode(CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF)
  {
    if (paramCameraAutoFocus == null) {
      return;
    }
    this.h = paramCameraAutoFocus;
    Camera.Parameters localParameters = h();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setFocusMode(localParameters, this.h, a(paramPointF), f());
    a(localParameters);
  }
  
  public void setFocusPoint(PointF paramPointF)
  {
    Camera.Parameters localParameters = h();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setFocusPoint(localParameters, a(paramPointF), f());
    a(localParameters);
  }
  
  public CameraConfigs.CameraAutoFocus getFocusMode()
  {
    Camera.Parameters localParameters = h();
    if (localParameters == null) {
      return this.h;
    }
    return CameraHelper.focusModeType(localParameters.getFocusMode());
  }
  
  public boolean canSupportAutoFocus()
  {
    boolean bool;
    try
    {
      bool = CameraHelper.canSupportAutofocus(TuSdkContext.context(), h());
    }
    catch (RuntimeException localRuntimeException)
    {
      bool = false;
      TLog.e(localRuntimeException, "%s canSupportAutoFocus catch error, ignore.", new Object[] { "TuSdkCameraFocusImpl" });
    }
    return bool;
  }
  
  private void c()
  {
    if ((i() == null) || (!CameraHelper.canSupportAutofocus(TuSdkContext.context()))) {
      return;
    }
    i().cancelAutoFocus();
  }
  
  private PointF a(PointF paramPointF)
  {
    if (paramPointF == null) {
      paramPointF = new PointF(0.5F, 0.5F);
    }
    return paramPointF;
  }
  
  @TargetApi(16)
  public void setAutoFocusMoveCallback(Camera.AutoFocusMoveCallback paramAutoFocusMoveCallback)
  {
    if ((i() == null) || (!CameraHelper.canSupportAutofocus(TuSdkContext.context()))) {
      return;
    }
    i().setAutoFocusMoveCallback(paramAutoFocusMoveCallback);
  }
  
  private void d()
  {
    if (this.e == null) {
      return;
    }
    ThreadHelper.cancel(this.e);
    this.e = null;
  }
  
  private void a(TuSdkCameraFocus.TuSdkCameraFocusListener paramTuSdkCameraFocusListener)
  {
    d();
    if ((this.k != null) && (!this.k.equals(paramTuSdkCameraFocusListener))) {
      this.k.onAutoFocus(this.f, this);
    }
    paramTuSdkCameraFocusListener.onAutoFocus(this.f, this);
    c();
  }
  
  public void autoFocus(CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF, TuSdkCameraFocus.TuSdkCameraFocusListener paramTuSdkCameraFocusListener)
  {
    if (this.a == TuSdkCamera.TuSdkCameraStatus.CAMERA_PREPARE_SHOT) {
      return;
    }
    setFocusMode(paramCameraAutoFocus, paramPointF);
    autoFocus(paramTuSdkCameraFocusListener);
  }
  
  public void autoFocus(final TuSdkCameraFocus.TuSdkCameraFocusListener paramTuSdkCameraFocusListener)
  {
    if ((this.k != null) && (!this.k.equals(paramTuSdkCameraFocusListener))) {
      this.k.onFocusStart(this);
    } else if (paramTuSdkCameraFocusListener != null) {
      paramTuSdkCameraFocusListener.onFocusStart(this);
    }
    Camera localCamera = i();
    if ((localCamera == null) || (!canSupportAutoFocus()))
    {
      if (paramTuSdkCameraFocusListener != null) {
        paramTuSdkCameraFocusListener.onAutoFocus(false, this);
      }
      return;
    }
    this.g = System.currentTimeMillis();
    Camera.AutoFocusCallback local2 = null;
    this.f = false;
    if (paramTuSdkCameraFocusListener != null) {
      local2 = new Camera.AutoFocusCallback()
      {
        public void onAutoFocus(boolean paramAnonymousBoolean, Camera paramAnonymousCamera)
        {
          TuSdkCameraFocusImpl.a(TuSdkCameraFocusImpl.this, paramAnonymousBoolean);
          TuSdkCameraFocusImpl.a(TuSdkCameraFocusImpl.this, paramTuSdkCameraFocusListener);
        }
      };
    }
    try
    {
      localCamera.autoFocus(local2);
      this.e = new Runnable()
      {
        public void run()
        {
          TuSdkCameraFocusImpl.a(TuSdkCameraFocusImpl.this, paramTuSdkCameraFocusListener);
        }
      };
      ThreadHelper.postDelayed(this.e, 1500L);
    }
    catch (Exception localException)
    {
      TLog.e(localException, "%s autoFocus failed, ignore and try again.", new Object[] { "TuSdkCameraFocusImpl" });
    }
  }
  
  private boolean e()
  {
    return System.currentTimeMillis() - this.g > 2000L;
  }
  
  public boolean allowFocusToShot()
  {
    return (e()) && (canSupportAutoFocus());
  }
  
  public void configure(TuSdkCameraBuilder paramTuSdkCameraBuilder, TuSdkCameraOrientation paramTuSdkCameraOrientation, TuSdkCameraSize paramTuSdkCameraSize)
  {
    if (paramTuSdkCameraBuilder == null)
    {
      TLog.e("%s configure builder[%s] or orientation[%s] or size[%s] is empty.", new Object[] { "TuSdkCameraFocusImpl", paramTuSdkCameraBuilder, paramTuSdkCameraOrientation, paramTuSdkCameraSize });
      return;
    }
    this.l = paramTuSdkCameraBuilder;
    this.m = paramTuSdkCameraOrientation;
    this.n = paramTuSdkCameraSize;
    Camera.Parameters localParameters = h();
    if (localParameters == null)
    {
      TLog.e("%s configure Camera.Parameters is empty.", new Object[] { "TuSdkCameraFocusImpl" });
      return;
    }
    CameraHelper.setFocusMode(localParameters, CameraHelper.focusModes);
    this.h = CameraHelper.focusModeType(localParameters.getFocusMode());
    this.c = CameraHelper.canSupportFaceDetection(localParameters);
    if (Build.VERSION.SDK_INT >= 14) {
      CameraHelper.setFocusArea(localParameters, a(null), null, paramTuSdkCameraBuilder.isBackFacingCameraPresent());
    }
    a(localParameters);
  }
  
  public void changeStatus(TuSdkCamera.TuSdkCameraStatus paramTuSdkCameraStatus)
  {
    this.a = paramTuSdkCameraStatus;
    if (paramTuSdkCameraStatus == TuSdkCamera.TuSdkCameraStatus.CAMERA_START_PREVIEW)
    {
      a();
    }
    else
    {
      d();
      b();
      this.e = null;
      this.f = false;
    }
  }
  
  private ImageOrientation f()
  {
    if (this.m == null) {
      return ImageOrientation.Up;
    }
    return this.m.previewOrientation();
  }
  
  private TuSdkSize g()
  {
    if (this.n == null) {
      return null;
    }
    return this.n.previewOptimalSize();
  }
  
  private Camera.Parameters h()
  {
    if (this.l == null) {
      return null;
    }
    return this.l.getParameters();
  }
  
  private void a(Camera.Parameters paramParameters)
  {
    if ((this.l == null) || (this.l.getOrginCamera() == null)) {
      return;
    }
    this.l.getOrginCamera().setParameters(paramParameters);
  }
  
  private Camera i()
  {
    if (this.l == null) {
      return null;
    }
    return this.l.getOrginCamera();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\camera\TuSdkCameraFocusImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */