package org.lasque.tusdk.api.video.preproc.filter;

import android.opengl.GLSurfaceView.EGLContextFactory;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.seles.SelesEGLContextFactory;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKGLContextMaker
{
  private EGL10 a;
  private EGLDisplay b;
  private EGLConfig[] c;
  private EGLConfig d;
  private EGLContext e;
  private GL10 f;
  private GLSurfaceView.EGLContextFactory g;
  private EGLSurface h;
  
  public void bindGLContext(TuSdkSize paramTuSdkSize, EGLContext paramEGLContext)
  {
    int[] arrayOfInt1 = new int[2];
    int[] arrayOfInt2 = { 12375, paramTuSdkSize.width, 12374, paramTuSdkSize.height, 12344 };
    this.a = ((EGL10)EGLContext.getEGL());
    this.b = this.a.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
    this.a.eglInitialize(this.b, arrayOfInt1);
    this.d = a();
    this.g = new SelesEGLContextFactory(2, paramEGLContext);
    this.e = this.g.createContext(this.a, this.b, this.d);
    this.h = this.a.eglCreatePbufferSurface(this.b, this.d, arrayOfInt2);
    this.a.eglMakeCurrent(this.b, this.h, this.h, this.e);
    this.f = ((GL10)this.e.getGL());
  }
  
  private EGLConfig a()
  {
    int[] arrayOfInt1 = { 12325, 0, 12326, 0, 12324, 8, 12323, 8, 12322, 8, 12321, 0, 12352, 4, 12344 };
    int[] arrayOfInt2 = new int[1];
    this.a.eglChooseConfig(this.b, arrayOfInt1, null, 0, arrayOfInt2);
    int i = arrayOfInt2[0] > 0 ? arrayOfInt2[0] : 1;
    this.c = new EGLConfig[i];
    this.a.eglChooseConfig(this.b, arrayOfInt1, this.c, i, arrayOfInt2);
    return this.c[0];
  }
  
  public void destory()
  {
    if (this.a != null)
    {
      this.a.eglMakeCurrent(this.b, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
      this.a.eglDestroySurface(this.b, this.h);
      this.g.destroyContext(this.a, this.b, this.e);
      this.a.eglTerminate(this.b);
      this.b = null;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\video\preproc\filter\TuSDKGLContextMaker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */