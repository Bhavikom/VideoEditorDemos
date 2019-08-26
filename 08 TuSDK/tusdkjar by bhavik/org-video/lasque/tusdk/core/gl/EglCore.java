package org.lasque.tusdk.core.gl;

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(17)
public class EglCore
{
  private EGLDisplay a;
  private EGLConfig b;
  private EGLContext c;
  private EGLSurface d;
  
  public EglCore(int paramInt)
  {
    changeDisplay(paramInt);
  }
  
  public EglCore()
  {
    this(0);
  }
  
  public void changeDisplay(int paramInt)
  {
    this.a = EGL14.eglGetDisplay(paramInt);
    int[] arrayOfInt = new int[2];
    EGL14.eglInitialize(this.a, arrayOfInt, 0, arrayOfInt, 1);
  }
  
  public EGLConfig getConfig(EGLConfigAttrs paramEGLConfigAttrs)
  {
    EGLConfig[] arrayOfEGLConfig = new EGLConfig[1];
    int[] arrayOfInt = new int[1];
    EGL14.eglChooseConfig(this.a, paramEGLConfigAttrs.a(), 0, arrayOfEGLConfig, 0, 1, arrayOfInt, 0);
    if (arrayOfInt[0] > 0)
    {
      if (paramEGLConfigAttrs.isDefault()) {
        this.b = arrayOfEGLConfig[0];
      }
      return arrayOfEGLConfig[0];
    }
    return null;
  }
  
  public EGLConfig getDefaultConfig()
  {
    return this.b;
  }
  
  public EGLSurface getDefaultSurface()
  {
    return this.d;
  }
  
  public EGLContext getDefaultContext()
  {
    return this.c;
  }
  
  public EGLContext createContext(EGLConfig paramEGLConfig, EGLContext paramEGLContext, EGLContextAttrs paramEGLContextAttrs)
  {
    EGLContext localEGLContext = EGL14.eglCreateContext(this.a, paramEGLConfig, paramEGLContext, paramEGLContextAttrs.a(), 0);
    if (paramEGLContextAttrs.isDefault()) {
      this.c = localEGLContext;
    }
    return localEGLContext;
  }
  
  public EGLSurface createWindowSurface(EGLConfig paramEGLConfig, Object paramObject)
  {
    return EGL14.eglCreateWindowSurface(this.a, paramEGLConfig, paramObject, new int[] { 12344 }, 0);
  }
  
  public EGLSurface createWindowSurface(Object paramObject)
  {
    this.d = EGL14.eglCreateWindowSurface(this.a, this.b, paramObject, new int[] { 12344 }, 0);
    return this.d;
  }
  
  public EGLSurface createPBufferSurface(EGLConfig paramEGLConfig, int paramInt1, int paramInt2)
  {
    return EGL14.eglCreatePbufferSurface(this.a, paramEGLConfig, new int[] { 12375, paramInt1, 12374, paramInt2, 12344 }, 0);
  }
  
  public boolean createGLESWithSurface(EGLConfigAttrs paramEGLConfigAttrs, EGLContextAttrs paramEGLContextAttrs, Object paramObject)
  {
    EGLConfig localEGLConfig = getConfig(paramEGLConfigAttrs.surfaceType(4).makeDefault(true));
    if (localEGLConfig == null)
    {
      TLog.i("getConfig failed : " + EGL14.eglGetError(), new Object[0]);
      return false;
    }
    this.c = createContext(localEGLConfig, EGL14.EGL_NO_CONTEXT, paramEGLContextAttrs.makeDefault(true));
    if (this.c == EGL14.EGL_NO_CONTEXT)
    {
      TLog.i("createContext failed : " + EGL14.eglGetError(), new Object[0]);
      return false;
    }
    this.d = createWindowSurface(paramObject);
    if (this.d == EGL14.EGL_NO_SURFACE)
    {
      TLog.i("createWindowSurface failed : " + EGL14.eglGetError(), new Object[0]);
      return false;
    }
    if (!EGL14.eglMakeCurrent(this.a, this.d, this.d, this.c))
    {
      TLog.i("eglMakeCurrent failed : " + EGL14.eglGetError(), new Object[0]);
      return false;
    }
    return true;
  }
  
  public boolean makeCurrent(EGLSurface paramEGLSurface1, EGLSurface paramEGLSurface2, EGLContext paramEGLContext)
  {
    if (!EGL14.eglMakeCurrent(this.a, paramEGLSurface1, paramEGLSurface2, paramEGLContext)) {
      TLog.i("eglMakeCurrent failed : " + EGL14.eglGetError(), new Object[0]);
    }
    return true;
  }
  
  public boolean makeCurrent(EGLSurface paramEGLSurface, EGLContext paramEGLContext)
  {
    return makeCurrent(paramEGLSurface, paramEGLSurface, paramEGLContext);
  }
  
  public boolean makeCurrent(EGLSurface paramEGLSurface)
  {
    return makeCurrent(paramEGLSurface, this.c);
  }
  
  public boolean makeCurrent()
  {
    return makeCurrent(this.d, this.c);
  }
  
  @TargetApi(18)
  public void setPresentationTime(EGLSurface paramEGLSurface, long paramLong)
  {
    EGLExt.eglPresentationTimeANDROID(this.a, paramEGLSurface, paramLong);
  }
  
  public EGLSurface createGLESWithPBuffer(EGLConfigAttrs paramEGLConfigAttrs, EGLContextAttrs paramEGLContextAttrs, int paramInt1, int paramInt2)
  {
    EGLConfig localEGLConfig = getConfig(paramEGLConfigAttrs.surfaceType(1));
    if (localEGLConfig == null)
    {
      TLog.i("getConfig failed : " + EGL14.eglGetError(), new Object[0]);
      return null;
    }
    EGLContext localEGLContext = createContext(localEGLConfig, EGL14.EGL_NO_CONTEXT, paramEGLContextAttrs);
    if (localEGLContext == EGL14.EGL_NO_CONTEXT)
    {
      TLog.i("createContext failed : " + EGL14.eglGetError(), new Object[0]);
      return null;
    }
    EGLSurface localEGLSurface = createPBufferSurface(localEGLConfig, paramInt1, paramInt2);
    if (localEGLSurface == EGL14.EGL_NO_SURFACE)
    {
      TLog.i("createWindowSurface failed : " + EGL14.eglGetError(), new Object[0]);
      return null;
    }
    if (!EGL14.eglMakeCurrent(this.a, localEGLSurface, localEGLSurface, localEGLContext))
    {
      TLog.i("eglMakeCurrent failed : " + EGL14.eglGetError(), new Object[0]);
      return null;
    }
    return localEGLSurface;
  }
  
  public void swapBuffers(EGLSurface paramEGLSurface)
  {
    EGL14.eglSwapBuffers(this.a, paramEGLSurface);
  }
  
  public boolean destroyGLES(EGLSurface paramEGLSurface, EGLContext paramEGLContext)
  {
    EGL14.eglMakeCurrent(this.a, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
    if (paramEGLSurface != null) {
      EGL14.eglDestroySurface(this.a, paramEGLSurface);
    }
    if (paramEGLContext != null) {
      EGL14.eglDestroyContext(this.a, paramEGLContext);
    }
    EGL14.eglTerminate(this.a);
    TLog.i("gl destroy gles", new Object[0]);
    return true;
  }
  
  public void destroySurface(EGLSurface paramEGLSurface)
  {
    EGL14.eglDestroySurface(this.a, paramEGLSurface);
  }
  
  public EGLDisplay getDisplay()
  {
    return this.a;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\gl\EglCore.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */