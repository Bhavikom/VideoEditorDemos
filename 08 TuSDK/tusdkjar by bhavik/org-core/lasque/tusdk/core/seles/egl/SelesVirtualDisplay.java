package org.lasque.tusdk.core.seles.egl;

import android.annotation.TargetApi;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;
import java.util.concurrent.Semaphore;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class SelesVirtualDisplay
{
  private final HandlerThread a = new HandlerThread("com.tutuclould.SelesVirtualDisplay");
  private final Handler b;
  private SelesRenderer c;
  private SelesEGLCoreSingleSurface d;
  private boolean e = false;
  private long f = 0L;
  private boolean g = false;
  private boolean h = false;
  private volatile Semaphore i = new Semaphore(0);
  
  public void setSyncRender(boolean paramBoolean)
  {
    this.h = paramBoolean;
  }
  
  public long lastRenderTimestampNs()
  {
    return this.f;
  }
  
  public void setRenderer(SelesRenderer paramSelesRenderer)
  {
    if (paramSelesRenderer == null) {
      return;
    }
    this.c = paramSelesRenderer;
    if ((this.d == null) || (!this.e)) {
      return;
    }
    postRender(new Runnable()
    {
      public void run()
      {
        SelesVirtualDisplay.a(SelesVirtualDisplay.this);
        SelesVirtualDisplay.b(SelesVirtualDisplay.this);
      }
    });
  }
  
  public SelesVirtualDisplay()
  {
    this.a.start();
    this.b = new Handler(this.a.getLooper());
  }
  
  public void release()
  {
    if (this.g) {
      return;
    }
    this.g = true;
    postRender(new Runnable()
    {
      public void run()
      {
        SelesVirtualDisplay.c(SelesVirtualDisplay.this);
      }
    });
    this.e = false;
    this.a.quitSafely();
  }
  
  public void clearTask()
  {
    this.b.removeMessages(0);
  }
  
  private void a()
  {
    if (this.c != null) {
      this.c.onSurfaceDestory(d());
    }
    if (this.d != null)
    {
      this.d.destroy();
      this.d = null;
    }
  }
  
  private void b()
  {
    if ((this.c == null) || (this.d == null)) {
      return;
    }
    GL10 localGL10 = d();
    if (localGL10 == null) {
      return;
    }
    this.c.onSurfaceCreated(localGL10, null);
  }
  
  private void c()
  {
    if ((this.c == null) || (this.d == null) || (!this.e)) {
      return;
    }
    TuSdkSize localTuSdkSize = this.d.getSurfaceSize();
    if (localTuSdkSize == null) {
      return;
    }
    GL10 localGL10 = d();
    if (localGL10 == null) {
      return;
    }
    this.c.onSurfaceChanged(localGL10, localTuSdkSize.width, localTuSdkSize.height);
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public void buildWindowContext(final android.opengl.EGLContext paramEGLContext)
  {
    postRender(new Runnable()
    {
      public void run()
      {
        SelesVirtualDisplay.a(SelesVirtualDisplay.this, paramEGLContext);
      }
    });
  }
  
  private void a(android.opengl.EGLContext paramEGLContext)
  {
    if (this.d != null)
    {
      TLog.w("%s buildWindowContext exist", new Object[] { "SelesVirtualDisplay" });
      return;
    }
    this.d = new SelesEGLCoreSingleSurface(paramEGLContext, 1);
  }
  
  public void attachWindowSurface(final Surface paramSurface, final boolean paramBoolean)
  {
    postRender(new Runnable()
    {
      public void run()
      {
        if (SelesVirtualDisplay.d(SelesVirtualDisplay.this) == null) {
          SelesVirtualDisplay.a(SelesVirtualDisplay.this, null);
        }
        if (SelesVirtualDisplay.e(SelesVirtualDisplay.this))
        {
          TLog.w("%s attachWindowSurface Surface can not duplicate attach", new Object[] { "SelesVirtualDisplay" });
          return;
        }
        SelesVirtualDisplay.a(SelesVirtualDisplay.this, SelesVirtualDisplay.d(SelesVirtualDisplay.this).attachWindowSurface(paramSurface, paramBoolean));
        SelesVirtualDisplay.a(SelesVirtualDisplay.this);
        SelesVirtualDisplay.b(SelesVirtualDisplay.this);
      }
    });
  }
  
  public void buildOffsetContext(final android.opengl.EGLContext paramEGLContext)
  {
    postRender(new Runnable()
    {
      public void run()
      {
        SelesVirtualDisplay.b(SelesVirtualDisplay.this, paramEGLContext);
      }
    });
  }
  
  private void b(android.opengl.EGLContext paramEGLContext)
  {
    if (this.d != null)
    {
      TLog.w("%s buildOffsetContext exist", new Object[] { "SelesVirtualDisplay" });
      return;
    }
    this.d = new SelesEGLCoreSingleSurface(paramEGLContext);
  }
  
  public void attachOffscreenSurface(final int paramInt1, final int paramInt2)
  {
    postRender(new Runnable()
    {
      public void run()
      {
        if (SelesVirtualDisplay.d(SelesVirtualDisplay.this) == null) {
          SelesVirtualDisplay.b(SelesVirtualDisplay.this, null);
        }
        if (SelesVirtualDisplay.e(SelesVirtualDisplay.this))
        {
          TLog.w("%s attachOffscreenSurface Surface can not duplicate attach", new Object[] { "SelesVirtualDisplay" });
          return;
        }
        SelesVirtualDisplay.a(SelesVirtualDisplay.this, SelesVirtualDisplay.d(SelesVirtualDisplay.this).attachOffscreenSurface(paramInt1, paramInt2));
        SelesVirtualDisplay.a(SelesVirtualDisplay.this);
        SelesVirtualDisplay.b(SelesVirtualDisplay.this);
      }
    });
  }
  
  public boolean requestRender()
  {
    return requestRender(System.nanoTime());
  }
  
  public boolean requestRender(long paramLong)
  {
    return requestRender(paramLong, null);
  }
  
  public boolean requestRender(long paramLong, Runnable paramRunnable)
  {
    return requestRender(paramLong, paramRunnable, null);
  }
  
  public boolean requestRender(long paramLong, Runnable paramRunnable1, Runnable paramRunnable2)
  {
    if (this.c == null)
    {
      TLog.w("%s requestRender need setRenderer first", new Object[] { "SelesVirtualDisplay" });
      return false;
    }
    if (this.d == null)
    {
      TLog.w("%s requestRender need buildContext", new Object[] { "SelesVirtualDisplay" });
      return false;
    }
    if (!this.e)
    {
      TLog.w("%s requestRender need Surface Attached", new Object[] { "SelesVirtualDisplay" });
      return false;
    }
    this.f = paramLong;
    postRender(paramRunnable1);
    postRender(new Runnable()
    {
      public void run()
      {
        GL10 localGL10 = SelesVirtualDisplay.f(SelesVirtualDisplay.this);
        if (localGL10 == null) {
          return;
        }
        SelesVirtualDisplay.g(SelesVirtualDisplay.this).onDrawFrame(localGL10);
        if (SelesVirtualDisplay.h(SelesVirtualDisplay.this)) {
          SelesVirtualDisplay.i(SelesVirtualDisplay.this).release();
        }
      }
    });
    if (this.h) {
      try
      {
        this.i.acquire();
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.printStackTrace();
      }
    }
    postRender(paramRunnable2);
    return true;
  }
  
  public void setPresentationTime(long paramLong)
  {
    if (this.d == null)
    {
      TLog.w("%s setPresentationTime EglCore is empty", new Object[] { "SelesVirtualDisplay" });
      return;
    }
    this.d.setPresentationTime(paramLong);
  }
  
  public boolean swapBuffers()
  {
    if (this.d == null)
    {
      TLog.w("%s swapBuffers EglCore is empty", new Object[] { "SelesVirtualDisplay" });
      return false;
    }
    return this.d.swapBuffers();
  }
  
  public boolean swapBuffers(long paramLong)
  {
    setPresentationTime(paramLong);
    return swapBuffers();
  }
  
  public void requestSwapBuffers(final long paramLong)
  {
    postRender(new Runnable()
    {
      public void run()
      {
        SelesVirtualDisplay.this.swapBuffers(paramLong);
      }
    });
  }
  
  public void postRender(Runnable paramRunnable)
  {
    if ((paramRunnable == null) || (!this.a.isAlive())) {
      return;
    }
    this.b.post(paramRunnable);
  }
  
  private GL10 d()
  {
    EGL10 localEGL10 = (EGL10)javax.microedition.khronos.egl.EGLContext.getEGL();
    if (localEGL10 == null) {
      return null;
    }
    javax.microedition.khronos.egl.EGLContext localEGLContext = localEGL10.eglGetCurrentContext();
    if (localEGLContext == null) {
      return null;
    }
    GL10 localGL10 = (GL10)localEGLContext.getGL();
    return localGL10;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\egl\SelesVirtualDisplay.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */