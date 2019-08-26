package org.lasque.tusdk.core.seles.egl;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

public class SelesEGLContext
{
  private EGLContext a = currentEGLContext();
  private long b = Thread.currentThread().getId();
  private String c = String.format("%s-%d", new Object[] { this.a, Long.valueOf(this.b) });
  
  public EGLContext getEGLContext()
  {
    return this.a;
  }
  
  public long getThreadID()
  {
    return this.b;
  }
  
  public String getHashKey()
  {
    return this.c;
  }
  
  public boolean equalsCurrent()
  {
    boolean bool = (this.a.equals(currentEGLContext())) && (this.b == Thread.currentThread().getId());
    return bool;
  }
  
  public boolean equalsCurrentThread()
  {
    boolean bool = this.b == Thread.currentThread().getId();
    return bool;
  }
  
  public static boolean equalsCurrent(EGLContext paramEGLContext)
  {
    if (paramEGLContext == null) {
      return false;
    }
    return paramEGLContext.equals(currentEGLContext());
  }
  
  public static EGLContext currentEGLContext()
  {
    EGL10 localEGL10 = (EGL10)EGLContext.getEGL();
    EGLContext localEGLContext = localEGL10.eglGetCurrentContext();
    return localEGLContext;
  }
  
  public static GL10 currentGL()
  {
    EGLContext localEGLContext = currentEGLContext();
    if ((localEGLContext == null) || (localEGLContext == EGL10.EGL_NO_CONTEXT)) {
      return null;
    }
    return (GL10)localEGLContext.getGL();
  }
  
  public static String currentHashKey()
  {
    return currentHashKey(currentEGLContext());
  }
  
  public static String currentHashKey(EGLContext paramEGLContext)
  {
    return String.format("%s-%d", new Object[] { paramEGLContext, Long.valueOf(Thread.currentThread().getId()) });
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof SelesEGLContext))) {
      return false;
    }
    SelesEGLContext localSelesEGLContext = (SelesEGLContext)paramObject;
    boolean bool = (this.a == localSelesEGLContext.a) && (this.b == localSelesEGLContext.b);
    return bool;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("SelesEGLContext").append("{");
    localStringBuffer.append("EGL: ").append(this.a).append(", ");
    localStringBuffer.append("Thread: ").append(this.b).append(", ");
    localStringBuffer.append("Current EGL: ").append(currentEGLContext()).append(",");
    localStringBuffer.append("Current Thread: ").append(Thread.currentThread().getId()).append(",");
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\egl\SelesEGLContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */