package org.lasque.tusdk.core.seles;

import android.annotation.TargetApi;
import android.opengl.GLSurfaceView.EGLContextFactory;
import android.os.Build.VERSION;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import org.lasque.tusdk.core.utils.TLog;

public class SelesEGLContextFactory
  implements GLSurfaceView.EGLContextFactory
{
  public static final int EGL_CONTEXT_CLIENT_VERSION;
  private int a;
  private EGLContext b = EGL10.EGL_NO_CONTEXT;
  
  @TargetApi(17)
  private static int a()
  {
    return 12440;
  }
  
  public SelesEGLContextFactory(int paramInt)
  {
    this(paramInt, EGL10.EGL_NO_CONTEXT);
  }
  
  public SelesEGLContextFactory(int paramInt, EGLContext paramEGLContext)
  {
    this.a = paramInt;
    if (paramEGLContext != null) {
      this.b = paramEGLContext;
    }
  }
  
  public EGLContext createContext(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig)
  {
    int[] arrayOfInt = { EGL_CONTEXT_CLIENT_VERSION, this.a, 12344 };
    EGLContext localEGLContext = paramEGL10.eglCreateContext(paramEGLDisplay, paramEGLConfig, this.b, this.a != 0 ? arrayOfInt : null);
    if (paramEGL10.eglGetError() != 12288) {
      return null;
    }
    SelesContext.createEGLContext(localEGLContext);
    return localEGLContext;
  }
  
  public void destroyContext(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLContext paramEGLContext)
  {
    int[] arrayOfInt = new int[1];
    boolean bool = paramEGL10.eglQueryContext(paramEGLDisplay, paramEGLContext, 12440, arrayOfInt);
    if (!bool) {
      return;
    }
    if (paramEGL10.eglGetError() != 12288) {
      return;
    }
    paramEGL10.eglMakeCurrent(paramEGLDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, paramEGLContext);
    SelesContext.destroyContext(paramEGLContext);
    paramEGL10.eglMakeCurrent(paramEGLDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
    if (!paramEGL10.eglDestroyContext(paramEGLDisplay, paramEGLContext)) {
      TLog.e("SelesEGLContextFactory - tid: %s | display: %s | context: %s | eglDestroyContex: %s", new Object[] { Long.valueOf(Thread.currentThread().getId()), paramEGLDisplay, paramEGLContext, Integer.valueOf(paramEGL10.eglGetError()) });
    }
  }
  
  static
  {
    if (Build.VERSION.SDK_INT < 17) {
      EGL_CONTEXT_CLIENT_VERSION = 12440;
    } else {
      EGL_CONTEXT_CLIENT_VERSION = a();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\SelesEGLContextFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */