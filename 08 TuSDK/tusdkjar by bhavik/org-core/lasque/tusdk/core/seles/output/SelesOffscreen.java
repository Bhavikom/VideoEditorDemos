package org.lasque.tusdk.core.seles.output;

import android.graphics.Rect;
import android.opengl.GLES20;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.egl.EGLContext;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import org.lasque.tusdk.core.seles.filters.SelesCropFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ThreadQueue;

public class SelesOffscreen
  extends SelesCropFilter
{
  private final ThreadQueue a = new ThreadQueue("com.tusdk.SelesAsyncOutput");
  private SelesEGL10Core b;
  private SelesOffscreenDelegate c;
  private boolean d;
  
  public void setDelegate(SelesOffscreenDelegate paramSelesOffscreenDelegate)
  {
    this.c = paramSelesOffscreenDelegate;
  }
  
  public boolean isWorking()
  {
    return this.d;
  }
  
  public void stopWork()
  {
    this.d = false;
  }
  
  public void resetEnabled()
  {
    this.d = false;
    setEnabled(true);
  }
  
  public void startWork()
  {
    if ((isEnabled()) || (this.d)) {
      return;
    }
    this.d = true;
    setEnabled(true);
  }
  
  public SelesOffscreen()
  {
    setEnabled(false);
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    this.a.post(new Runnable()
    {
      public void run()
      {
        if (SelesOffscreen.a(SelesOffscreen.this) != null) {
          SelesOffscreen.a(SelesOffscreen.this).destroy();
        }
        SelesOffscreen.a(SelesOffscreen.this, null);
      }
    });
    this.a.release();
  }
  
  private void a()
  {
    if ((this.mInputTextureSize == null) || (!this.mInputTextureSize.isSize())) {
      return;
    }
    final EGLContext localEGLContext = SelesContext.currentEGLContext();
    if (localEGLContext == null) {
      return;
    }
    this.a.post(new Runnable()
    {
      public void run()
      {
        SelesOffscreen.a(SelesOffscreen.this, localEGLContext);
      }
    });
  }
  
  private void a(EGLContext paramEGLContext)
  {
    this.d = true;
    if (this.b != null)
    {
      this.b.setOutputRect(new Rect(0, 0, this.mInputTextureSize.width, this.mInputTextureSize.height));
      return;
    }
    this.b = SelesEGL10Core.create(getOutputSize(), paramEGLContext);
    runPendingOnDrawTasks();
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    TuSdkSize localTuSdkSize = this.mInputTextureSize;
    super.setInputSize(paramTuSdkSize, paramInt);
    if (!localTuSdkSize.equals(this.mInputTextureSize)) {
      a();
    }
  }
  
  public void newFrameReady(final long paramLong, int paramInt)
  {
    if (this.mFirstInputFramebuffer != null) {
      setEnabled(false);
    }
    GLES20.glFinish();
    this.d = true;
    this.a.post(new Runnable()
    {
      public void run()
      {
        SelesOffscreen.this.asyncNewFrameReady(paramLong, this.b);
      }
    });
  }
  
  protected void asyncNewFrameReady(long paramLong, int paramInt)
  {
    super.newFrameReady(paramLong, paramInt);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    if (this.c != null) {
      setEnabled(this.c.onFrameRendered(this));
    }
    this.d = false;
  }
  
  public IntBuffer renderBuffer()
  {
    if (this.b == null) {
      return null;
    }
    return this.b.getImageBuffer();
  }
  
  public static abstract interface SelesOffscreenDelegate
  {
    public abstract boolean onFrameRendered(SelesOffscreen paramSelesOffscreen);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\output\SelesOffscreen.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */