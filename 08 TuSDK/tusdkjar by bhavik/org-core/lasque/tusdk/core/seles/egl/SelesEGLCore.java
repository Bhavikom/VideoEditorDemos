package org.lasque.tusdk.core.seles.egl;

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.os.Build.VERSION;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class SelesEGLCore
{
  public static final int FLAG_NONE = 0;
  public static final int FLAG_RECORDABLE = 1;
  public static final int FLAG_TRY_GLES3 = 2;
  public static final int EGL_RECORDABLE_ANDROID;
  private EGLDisplay a = EGL14.EGL_NO_DISPLAY;
  private EGLContext b = EGL14.EGL_NO_CONTEXT;
  private EGLConfig c = null;
  private int d = -1;
  private long e;
  
  @TargetApi(26)
  private static int a()
  {
    return 12610;
  }
  
  public long getThreadID()
  {
    return this.e;
  }
  
  public SelesEGLCore(EGLContext paramEGLContext)
  {
    this(paramEGLContext, 0);
  }
  
  public SelesEGLCore(EGLContext paramEGLContext, int paramInt)
  {
    if (this.a != EGL14.EGL_NO_DISPLAY)
    {
      TLog.w("%s EGL already set up", new Object[] { "SelesEGLCore" });
      return;
    }
    if (paramEGLContext == null) {
      paramEGLContext = EGL14.EGL_NO_CONTEXT;
    }
    this.a = EGL14.eglGetDisplay(0);
    if (this.a == EGL14.EGL_NO_DISPLAY)
    {
      this.a = null;
      TLog.w("%s Unable to get EGL display", new Object[] { "SelesEGLCore" });
      return;
    }
    int[] arrayOfInt1 = new int[2];
    if (!EGL14.eglInitialize(this.a, arrayOfInt1, 0, arrayOfInt1, 1))
    {
      this.a = null;
      TLog.w("%s Unable to initialize EGL", new Object[] { "SelesEGLCore" });
      return;
    }
    boolean bool = false;
    if ((paramInt & 0x2) != 0) {
      bool = a(paramEGLContext, paramInt, 3);
    }
    if (!bool) {
      bool = a(paramEGLContext, paramInt, 2);
    }
    if (!bool)
    {
      TLog.w("%s Unable to Create ELGS Context", new Object[] { "SelesEGLCore" });
      return;
    }
    this.e = Thread.currentThread().getId();
    int[] arrayOfInt2 = new int[1];
    EGL14.eglQueryContext(this.a, this.b, 12440, arrayOfInt2, 0);
  }
  
  private boolean a(EGLContext paramEGLContext, int paramInt1, int paramInt2)
  {
    EGLConfig localEGLConfig = a(paramInt1, paramInt2);
    if (localEGLConfig == null) {
      return false;
    }
    int[] arrayOfInt = { 12440, paramInt2, 12344 };
    EGLContext localEGLContext = EGL14.eglCreateContext(this.a, localEGLConfig, paramEGLContext, arrayOfInt, 0);
    if (EGL14.eglGetError() != 12288) {
      return false;
    }
    this.c = localEGLConfig;
    this.b = localEGLContext;
    this.d = paramInt2;
    return true;
  }
  
  private EGLConfig a(int paramInt1, int paramInt2)
  {
    int i = 4;
    if (paramInt2 > 2) {
      i |= 0x40;
    }
    int[] arrayOfInt1 = { 12325, 0, 12326, 0, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, i, 12344, 0, 12344 };
    if ((paramInt1 & 0x1) != 0)
    {
      arrayOfInt1[(arrayOfInt1.length - 3)] = EGL_RECORDABLE_ANDROID;
      arrayOfInt1[(arrayOfInt1.length - 2)] = 1;
    }
    EGLConfig[] arrayOfEGLConfig = new EGLConfig[1];
    int[] arrayOfInt2 = new int[1];
    if (!EGL14.eglChooseConfig(this.a, arrayOfInt1, 0, arrayOfEGLConfig, 0, arrayOfEGLConfig.length, arrayOfInt2, 0))
    {
      TLog.w("%s Unable to find find RGBA8888 | version %d EGLConfig", new Object[] { "SelesEGLCore", Integer.valueOf(paramInt2) });
      return null;
    }
    return arrayOfEGLConfig[0];
  }
  
  public void destroy()
  {
    if (this.a == null) {
      return;
    }
    if (this.a != EGL14.EGL_NO_DISPLAY)
    {
      SelesContext.destroyContext(SelesContext.currentEGLContext());
      makeNothingCurrent();
      _cleanEGLWhenDestory();
      EGL14.eglDestroyContext(this.a, this.b);
      EGL14.eglReleaseThread();
      EGL14.eglTerminate(this.a);
    }
    this.a = EGL14.EGL_NO_DISPLAY;
    this.b = EGL14.EGL_NO_CONTEXT;
    this.c = null;
  }
  
  protected void _cleanEGLWhenDestory() {}
  
  public void releaseSurface(EGLSurface paramEGLSurface)
  {
    if ((emptyEGLDisplay()) || (paramEGLSurface == null) || (paramEGLSurface == EGL14.EGL_NO_SURFACE)) {
      return;
    }
    EGL14.eglDestroySurface(this.a, paramEGLSurface);
  }
  
  /* Error */
  protected void finalize()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 42	org/lasque/tusdk/core/seles/egl/SelesEGLCore:a	Landroid/opengl/EGLDisplay;
    //   4: getstatic 38	android/opengl/EGL14:EGL_NO_DISPLAY	Landroid/opengl/EGLDisplay;
    //   7: if_acmpeq +21 -> 28
    //   10: ldc 14
    //   12: iconst_1
    //   13: anewarray 32	java/lang/Object
    //   16: dup
    //   17: iconst_0
    //   18: ldc 20
    //   20: aastore
    //   21: invokestatic 88	org/lasque/tusdk/core/utils/TLog:w	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   24: aload_0
    //   25: invokevirtual 83	org/lasque/tusdk/core/seles/egl/SelesEGLCore:destroy	()V
    //   28: aload_0
    //   29: invokespecial 71	java/lang/Object:finalize	()V
    //   32: goto +10 -> 42
    //   35: astore_1
    //   36: aload_0
    //   37: invokespecial 71	java/lang/Object:finalize	()V
    //   40: aload_1
    //   41: athrow
    //   42: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	43	0	this	SelesEGLCore
    //   35	6	1	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   0	28	35	finally
  }
  
  public EGLSurface createWindowSurface(Object paramObject)
  {
    if (emptyEGLDisplay()) {
      return null;
    }
    int[] arrayOfInt = { 12344 };
    EGLSurface localEGLSurface = EGL14.eglCreateWindowSurface(this.a, this.c, paramObject, arrayOfInt, 0);
    a("eglCreateWindowSurface");
    if (localEGLSurface == null) {
      TLog.w("%s surface was null", new Object[] { "SelesEGLCore" });
    }
    return localEGLSurface;
  }
  
  public EGLSurface createOffscreenSurface(int paramInt1, int paramInt2)
  {
    if (emptyEGLDisplay()) {
      return null;
    }
    int[] arrayOfInt = { 12375, paramInt1, 12374, paramInt2, 12344 };
    EGLSurface localEGLSurface = EGL14.eglCreatePbufferSurface(this.a, this.c, arrayOfInt, 0);
    a("eglCreatePbufferSurface");
    if (localEGLSurface == null) {
      TLog.w("%s surface was null", new Object[] { "SelesEGLCore" });
    }
    return localEGLSurface;
  }
  
  public boolean makeCurrent(EGLSurface paramEGLSurface)
  {
    return makeCurrent(paramEGLSurface, paramEGLSurface);
  }
  
  public boolean makeCurrent(EGLSurface paramEGLSurface1, EGLSurface paramEGLSurface2)
  {
    if (emptyEGLDisplay())
    {
      TLog.w("%s NOTE: makeCurrent w/o display EGLDisplay is empty", new Object[] { "SelesEGLCore" });
      return false;
    }
    if ((this.b == null) || (this.b == EGL14.EGL_NO_CONTEXT))
    {
      TLog.w("%s NOTE: makeCurrent w/o display EGLContext is empty", new Object[] { "SelesEGLCore" });
      return false;
    }
    if (!EGL14.eglMakeCurrent(this.a, paramEGLSurface1, paramEGLSurface2, this.b))
    {
      TLog.w("%s eglMakeCurrent(draw,read) failed", new Object[] { "SelesEGLCore" });
      return false;
    }
    SelesContext.createEGLContext(SelesContext.currentEGLContext());
    return true;
  }
  
  public void makeNothingCurrent()
  {
    if (emptyEGLDisplay()) {
      return;
    }
    if (!EGL14.eglMakeCurrent(this.a, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)) {
      TLog.w("%s eglMakeCurrent failed", new Object[] { "SelesEGLCore" });
    }
  }
  
  public boolean swapBuffers(EGLSurface paramEGLSurface)
  {
    if (emptyEGLDisplay())
    {
      TLog.w("%s swapBuffers EGLDisplay is empty", new Object[] { "SelesEGLCore" });
      return false;
    }
    return EGL14.eglSwapBuffers(this.a, paramEGLSurface);
  }
  
  public void setPresentationTime(EGLSurface paramEGLSurface, long paramLong)
  {
    EGLExt.eglPresentationTimeANDROID(this.a, paramEGLSurface, paramLong);
  }
  
  public boolean isCurrent(EGLSurface paramEGLSurface)
  {
    return (this.b.equals(EGL14.eglGetCurrentContext())) && (paramEGLSurface.equals(EGL14.eglGetCurrentSurface(12377)));
  }
  
  public int querySurface(EGLSurface paramEGLSurface, int paramInt)
  {
    int[] arrayOfInt = new int[1];
    EGL14.eglQuerySurface(this.a, paramEGLSurface, paramInt, arrayOfInt, 0);
    return arrayOfInt[0];
  }
  
  public int getGlVersion()
  {
    return this.d;
  }
  
  public boolean emptyEGLDisplay()
  {
    return (this.a == null) || (this.a == EGL14.EGL_NO_DISPLAY);
  }
  
  private boolean a(String paramString)
  {
    int i;
    if ((i = EGL14.eglGetError()) == 12288) {
      return true;
    }
    TLog.w("%s: %s checkEglError[0x%s]", new Object[] { "SelesEGLCore", paramString, Integer.toHexString(i) });
    return false;
  }
  
  public static void logCurrent(String paramString)
  {
    EGLDisplay localEGLDisplay = EGL14.eglGetCurrentDisplay();
    EGLContext localEGLContext = EGL14.eglGetCurrentContext();
    EGLSurface localEGLSurface = EGL14.eglGetCurrentSurface(12377);
    TLog.d("%s Current EGL (%s): display=%s, context=%s, surface=%s", new Object[] { "SelesEGLCore", paramString, localEGLDisplay, localEGLContext, localEGLSurface });
  }
  
  static
  {
    if (Build.VERSION.SDK_INT < 26) {
      EGL_RECORDABLE_ANDROID = 12610;
    } else {
      EGL_RECORDABLE_ANDROID = a();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\egl\SelesEGLCore.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */