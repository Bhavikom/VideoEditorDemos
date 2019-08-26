package org.lasque.tusdk.core.seles.egl;

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.EGLSurface;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class SelesEGLCoreOffscreen
  extends SelesEGLCore
{
  private EGLSurface a = EGL14.EGL_NO_SURFACE;
  private TuSdkSize b;
  
  public TuSdkSize getSurfaceSize()
  {
    if (this.b == null)
    {
      TLog.w("%s getSurfaceSize need attachSurface first", new Object[] { "SelesEGLCoreOffscreen" });
      return null;
    }
    return this.b;
  }
  
  public boolean isSurfaceAttached()
  {
    return (this.a != null) && (this.a != EGL14.EGL_NO_SURFACE);
  }
  
  public SelesEGLCoreOffscreen(EGLContext paramEGLContext)
  {
    super(paramEGLContext, 0);
  }
  
  public SelesEGLCoreOffscreen(EGLContext paramEGLContext, TuSdkSize paramTuSdkSize)
  {
    this(paramEGLContext);
    attachSurface(paramTuSdkSize);
  }
  
  public boolean attachSurface(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize()))
    {
      TLog.d("%s attachSurface need size and the side > 0, Size: %s", new Object[] { "SelesEGLCoreOffscreen", paramTuSdkSize });
      return false;
    }
    return attachSurface(paramTuSdkSize.width, paramTuSdkSize.height);
  }
  
  public boolean attachSurface(int paramInt1, int paramInt2)
  {
    if (this.a != EGL14.EGL_NO_SURFACE)
    {
      TLog.w("%s surface already created", new Object[] { "SelesEGLCoreOffscreen" });
      return false;
    }
    this.a = createOffscreenSurface(paramInt1, paramInt2);
    if (this.a == null) {
      return false;
    }
    this.b = TuSdkSize.create(paramInt1, paramInt2);
    return makeCurrent(this.a);
  }
  
  public boolean swapBuffers()
  {
    return swapBuffers(this.a);
  }
  
  public void setPresentationTime(long paramLong)
  {
    setPresentationTime(this.a, paramLong);
  }
  
  protected void _cleanEGLWhenDestory()
  {
    releaseSurface(this.a);
    this.a = EGL14.EGL_NO_SURFACE;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\egl\SelesEGLCoreOffscreen.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */