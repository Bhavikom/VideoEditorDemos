package org.lasque.tusdk.core.seles.egl;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.opengl.GLSurfaceView.EGLContextFactory;
import android.opengl.GLSurfaceView.Renderer;
import java.nio.IntBuffer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesEGLContextFactory;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

public class SelesEGL10Core
{
  private GLSurfaceView.Renderer a;
  private TuSdkSize b;
  private Rect c;
  private EGL10 d;
  private EGLDisplay e;
  private EGLConfig[] f;
  private EGLConfig g;
  private EGLContext h;
  private EGLSurface i;
  private GL10 j;
  private GLSurfaceView.EGLContextFactory k;
  private IntBuffer l;
  private long m;
  
  public TuSdkSize getSize()
  {
    return this.b;
  }
  
  public long getThreadID()
  {
    return this.m;
  }
  
  public static SelesEGL10Core create(TuSdkSize paramTuSdkSize)
  {
    return new SelesEGL10Core(paramTuSdkSize, null);
  }
  
  public static SelesEGL10Core create(TuSdkSize paramTuSdkSize, EGLContext paramEGLContext)
  {
    return new SelesEGL10Core(paramTuSdkSize, paramEGLContext);
  }
  
  private SelesEGL10Core(TuSdkSize paramTuSdkSize, EGLContext paramEGLContext)
  {
    if ((paramTuSdkSize == null) || (paramTuSdkSize.minSide() < 1))
    {
      TLog.e("SelesEGL10Core: Passed image must not be empty - it should be at least 1px tall and wide : %s", new Object[] { paramTuSdkSize });
      return;
    }
    this.b = paramTuSdkSize;
    this.c = new Rect(0, 0, paramTuSdkSize.width, paramTuSdkSize.height);
    int[] arrayOfInt1 = new int[2];
    int[] arrayOfInt2 = { 12375, this.b.width, 12374, this.b.height, 12344 };
    this.d = ((EGL10)EGLContext.getEGL());
    this.e = this.d.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
    this.d.eglInitialize(this.e, arrayOfInt1);
    this.g = a();
    this.k = new SelesEGLContextFactory(2, paramEGLContext);
    this.h = this.k.createContext(this.d, this.e, this.g);
    this.i = this.d.eglCreatePbufferSurface(this.e, this.g, arrayOfInt2);
    this.d.eglMakeCurrent(this.e, this.i, this.i, this.h);
    this.j = ((GL10)this.h.getGL());
    this.m = Thread.currentThread().getId();
  }
  
  public void setRenderer(GLSurfaceView.Renderer paramRenderer)
  {
    this.a = paramRenderer;
    if (Thread.currentThread().getId() != this.m)
    {
      TLog.e("setRenderer: This thread does not own the OpenGL context.", new Object[0]);
      return;
    }
    this.a.onSurfaceCreated(this.j, this.g);
    this.a.onSurfaceChanged(this.j, this.b.width, this.b.height);
  }
  
  protected void finalize()
  {
    destroy();
    super.finalize();
  }
  
  public void destroy()
  {
    if (this.e == null) {
      return;
    }
    try
    {
      this.d.eglDestroySurface(this.e, this.i);
      this.k.destroyContext(this.d, this.e, this.h);
      this.d.eglTerminate(this.e);
      this.e = null;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  private EGLConfig a()
  {
    int[] arrayOfInt1 = { 12325, 0, 12326, 0, 12324, 8, 12323, 8, 12322, 8, 12321, 0, 12352, 4, 12344 };
    int[] arrayOfInt2 = new int[1];
    this.d.eglChooseConfig(this.e, arrayOfInt1, null, 0, arrayOfInt2);
    int n = arrayOfInt2[0] > 0 ? arrayOfInt2[0] : 1;
    this.f = new EGLConfig[n];
    this.d.eglChooseConfig(this.e, arrayOfInt1, this.f, n, arrayOfInt2);
    return this.f[0];
  }
  
  public Bitmap getBitmap()
  {
    IntBuffer localIntBuffer = getImageBuffer();
    if (localIntBuffer == null) {
      return null;
    }
    Bitmap localBitmap = Bitmap.createBitmap(this.c.width(), this.c.height(), Bitmap.Config.ARGB_8888);
    localBitmap.copyPixelsFromBuffer(localIntBuffer);
    return localBitmap;
  }
  
  public IntBuffer getImageBuffer()
  {
    if (Thread.currentThread().getId() != this.m)
    {
      TLog.e("getBitmap: This thread does not own the OpenGL context.", new Object[0]);
      return null;
    }
    if (this.a != null) {
      this.a.onDrawFrame(this.j);
    }
    return b();
  }
  
  private IntBuffer b()
  {
    if (this.l == null) {
      this.l = IntBuffer.allocate(this.c.width() * this.c.height());
    }
    this.l.position(0);
    this.j.glReadPixels(this.c.left, this.c.top, this.c.width(), this.c.height(), 6408, 5121, this.l);
    return this.l;
  }
  
  public void setOutputRect(Rect paramRect)
  {
    if ((paramRect == null) || (paramRect.isEmpty())) {
      return;
    }
    if ((this.c != null) && (this.c.equals(paramRect))) {
      return;
    }
    this.c = paramRect;
    if (this.l != null) {
      this.l = IntBuffer.allocate(this.c.width() * this.c.height());
    }
  }
  
  public static void checkGLError(String paramString)
  {
    GL10 localGL10 = SelesContext.currentGL();
    int n = localGL10.glGetError();
    if (n == 0) {
      return;
    }
    TLog.e("%s %s: checkGLError[0x%s]", new Object[] { paramString, "SelesEGL10Core", Integer.toHexString(n) });
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\egl\SelesEGL10Core.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */