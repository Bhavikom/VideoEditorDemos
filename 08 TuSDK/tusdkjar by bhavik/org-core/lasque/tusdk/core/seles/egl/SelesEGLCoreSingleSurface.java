package org.lasque.tusdk.core.seles.egl;

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.EGLSurface;
import android.view.Surface;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class SelesEGLCoreSingleSurface
  extends SelesEGLCore
{
  private EGLSurface a = EGL14.EGL_NO_SURFACE;
  private Surface b;
  private boolean c = false;
  private TuSdkSize d;
  
  public TuSdkSize getSurfaceSize()
  {
    if (this.d == null)
    {
      TLog.w("%s getSurfaceSize need attachSurface first", new Object[] { "SelesEGLCoreSingleSurface" });
      return null;
    }
    return this.d;
  }
  
  public boolean isSurfaceAttached()
  {
    return (this.a != null) && (this.a != EGL14.EGL_NO_SURFACE);
  }
  
  public SelesEGLCoreSingleSurface(EGLContext paramEGLContext)
  {
    this(paramEGLContext, 0);
  }
  
  public SelesEGLCoreSingleSurface(EGLContext paramEGLContext, int paramInt)
  {
    super(paramEGLContext, paramInt);
  }
  
  public boolean attachWindowSurface(Surface paramSurface, boolean paramBoolean)
  {
    if (this.a != EGL14.EGL_NO_SURFACE)
    {
      TLog.w("%s surface already created", new Object[] { "SelesEGLCoreSingleSurface" });
      return false;
    }
    this.a = createWindowSurface(paramSurface);
    if (this.a == null) {
      return false;
    }
    int i = querySurface(this.a, 12375);
    int j = querySurface(this.a, 12374);
    this.d = TuSdkSize.create(i, j);
    this.c = paramBoolean;
    return makeCurrent(this.a);
  }
  
  public boolean attachOffscreenSurface(int paramInt1, int paramInt2)
  {
    if (this.a != EGL14.EGL_NO_SURFACE)
    {
      TLog.w("%s surface already created", new Object[] { "SelesEGLCoreSingleSurface" });
      return false;
    }
    this.a = createOffscreenSurface(paramInt1, paramInt2);
    if (this.a == null) {
      return false;
    }
    this.d = TuSdkSize.create(paramInt1, paramInt2);
    return makeCurrent(this.a);
  }
  
  public void setPresentationTime(long paramLong)
  {
    setPresentationTime(this.a, paramLong);
  }
  
  public boolean swapBuffers()
  {
    return swapBuffers(this.a);
  }
  
  public void destroy()
  {
    if ((this.b != null) && (this.c)) {
      this.b.release();
    }
    this.b = null;
    super.destroy();
  }
  
  protected void _cleanEGLWhenDestory()
  {
    releaseSurface(this.a);
    this.a = EGL14.EGL_NO_SURFACE;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\egl\SelesEGLCoreSingleSurface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */