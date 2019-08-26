package org.lasque.tusdk.core.seles.output;

import android.opengl.GLES20;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.sources.SelesWatermark;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesSurfacePusherAsync
  extends SelesFilter
  implements SelesSurfaceDisplay
{
  private final SelesSurfaceDisplay a = new SelesSurfacePusher();
  private SelesFramebuffer b;
  private final Object c = new Object();
  
  public void setTextureCoordinateBuilder(SelesVerticeCoordinateBuilder paramSelesVerticeCoordinateBuilder)
  {
    this.a.setTextureCoordinateBuilder(paramSelesVerticeCoordinateBuilder);
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  public void setWatermark(SelesWatermark paramSelesWatermark)
  {
    this.a.setWatermark(paramSelesWatermark);
  }
  
  protected void onDestroy()
  {
    if (this.b != null)
    {
      this.b.unlock();
      this.b = null;
    }
    if (this.mOutputFramebuffer != null)
    {
      this.mOutputFramebuffer.unlock();
      this.mOutputFramebuffer = null;
    }
    if (this.mFirstInputFramebuffer != null)
    {
      this.mFirstInputFramebuffer.unlock();
      this.mFirstInputFramebuffer = null;
    }
    this.a.destroy();
    super.onDestroy();
  }
  
  public void setPusherRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    this.a.setInputRotation(paramImageOrientation, paramInt);
  }
  
  public void newFrameReadyInGLThread(long paramLong)
  {
    runPendingOnDrawTasks();
  }
  
  public void duplicateFrameReadyInGLThread(long paramLong)
  {
    synchronized (this.c)
    {
      this.a.duplicateFrameReadyInGLThread(paramLong);
    }
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    if (this.mOutputFramebuffer == null) {
      return;
    }
    synchronized (this.c)
    {
      SelesFramebuffer localSelesFramebuffer = this.b;
      this.b = this.mOutputFramebuffer;
      if (!this.mUsingNextFrameForImageCapture) {
        this.mOutputFramebuffer = null;
      }
      if (localSelesFramebuffer != null) {
        localSelesFramebuffer.unlock();
      }
      this.a.setInputSize(outputFrameSize(), 0);
      this.a.setInputFramebuffer(this.b, 0);
      this.a.newFrameReady(paramLong, 0);
    }
  }
  
  public void newFrameReady(final long paramLong, int paramInt)
  {
    GLES20.glFinish();
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesSurfacePusherAsync.a(SelesSurfacePusherAsync.this, paramLong, this.b);
      }
    });
  }
  
  private void a(long paramLong, int paramInt)
  {
    super.newFrameReady(paramLong, paramInt);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    checkGLError(getClass().getSimpleName());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\output\SelesSurfacePusherAsync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */