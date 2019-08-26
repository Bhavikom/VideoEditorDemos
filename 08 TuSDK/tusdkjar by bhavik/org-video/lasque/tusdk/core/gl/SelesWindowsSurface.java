package org.lasque.tusdk.core.gl;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.view.Surface;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class SelesWindowsSurface
{
  public static final int FLAG_RECORDABLE = 1;
  public static final int FLAG_TRY_GLES3 = 2;
  private EGLDisplay a = EGL14.EGL_NO_DISPLAY;
  private EGLContext b = EGL14.EGL_NO_CONTEXT;
  private EGLConfig c = null;
  private int d = -1;
  private EGLSurface e = EGL14.EGL_NO_SURFACE;
  private Surface f;
  private boolean g;
  
  public SelesWindowsSurface(EGLContext paramEGLContext, int paramInt)
  {
    if (this.a == EGL14.EGL_NO_DISPLAY) {
      throw new RuntimeException("unable to get EGL14 display");
    }
    int[] arrayOfInt1 = new int[2];
    if (!EGL14.eglInitialize(this.a, arrayOfInt1, 0, arrayOfInt1, 1))
    {
      this.a = null;
      throw new RuntimeException("unable to initialize EGL14");
    }
    EGLConfig localEGLConfig = a(1, 2);
    if (localEGLConfig == null) {
      throw new RuntimeException("Unable to find a suitable EGLConfig");
    }
    int[] arrayOfInt2 = { 12440, 2, 12344 };
    EGLContext localEGLContext = EGL14.eglCreateContext(this.a, localEGLConfig, paramEGLContext, arrayOfInt2, 0);
    a("eglCreateContext");
    this.c = localEGLConfig;
    this.b = localEGLContext;
    this.d = 2;
  }
  
  private EGLConfig a(int paramInt1, int paramInt2)
  {
    int i = 4;
    if (paramInt2 >= 3) {
      i |= 0x40;
    }
    int[] arrayOfInt1 = { 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 0, 12326, 0, 12352, i, 12344, 0, 12344 };
    if ((paramInt1 & 0x1) != 0)
    {
      arrayOfInt1[(arrayOfInt1.length - 3)] = 12610;
      arrayOfInt1[(arrayOfInt1.length - 2)] = 1;
    }
    EGLConfig[] arrayOfEGLConfig = new EGLConfig[1];
    int[] arrayOfInt2 = new int[1];
    if (!EGL14.eglChooseConfig(this.a, arrayOfInt1, 0, arrayOfEGLConfig, 0, arrayOfEGLConfig.length, arrayOfInt2, 0))
    {
      TLog.w("unable to find RGB8888 / " + paramInt2 + " EGLConfig", new Object[0]);
      return null;
    }
    return arrayOfEGLConfig[0];
  }
  
  private void a(String paramString)
  {
    int i;
    if ((i = EGL14.eglGetError()) != 12288) {
      throw new RuntimeException(paramString + ": EGL error: 0x" + Integer.toHexString(i));
    }
  }
  
  public void attachSurface(Surface paramSurface, boolean paramBoolean)
  {
    if (this.e != EGL14.EGL_NO_SURFACE) {
      throw new IllegalStateException("surface already created");
    }
    this.e = a(paramSurface);
    this.g = paramBoolean;
  }
  
  private EGLSurface a(Object paramObject)
  {
    if ((!(paramObject instanceof Surface)) && (!(paramObject instanceof SurfaceTexture))) {
      throw new RuntimeException("invalid surface: " + paramObject);
    }
    int[] arrayOfInt = { 12344 };
    EGLSurface localEGLSurface = EGL14.eglCreateWindowSurface(this.a, this.c, paramObject, arrayOfInt, 0);
    a("eglCreateWindowSurface");
    if (localEGLSurface == null) {
      throw new RuntimeException("surface was null");
    }
    return localEGLSurface;
  }
  
  public void makeCurrent()
  {
    if (this.a == EGL14.EGL_NO_DISPLAY) {
      TLog.d("NOTE: makeCurrent w/o display", new Object[0]);
    }
    if (!EGL14.eglMakeCurrent(this.a, this.e, this.e, this.b)) {
      throw new RuntimeException("eglMakeCurrent failed");
    }
  }
  
  public int getGlVersion()
  {
    return this.d;
  }
  
  public void setPresentationTime(long paramLong)
  {
    EGLExt.eglPresentationTimeANDROID(this.a, this.e, paramLong);
  }
  
  public boolean swapBuffers()
  {
    boolean bool = EGL14.eglSwapBuffers(this.a, this.e);
    if (!bool) {
      TLog.d("WARNING: swapBuffers() failed", new Object[0]);
    }
    return bool;
  }
  
  public void release()
  {
    a();
    if (this.f != null)
    {
      if (this.g) {
        this.f.release();
      }
      this.f = null;
    }
  }
  
  private void a()
  {
    EGL14.eglDestroySurface(this.a, this.e);
    this.e = EGL14.EGL_NO_SURFACE;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\gl\SelesWindowsSurface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */