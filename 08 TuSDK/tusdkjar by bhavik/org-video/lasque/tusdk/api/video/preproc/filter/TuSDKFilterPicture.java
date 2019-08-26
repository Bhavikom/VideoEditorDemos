package org.lasque.tusdk.api.video.preproc.filter;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.concurrent.Semaphore;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import org.lasque.tusdk.core.seles.sources.SelesOutput;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public final class TuSDKFilterPicture
  extends SelesOutput
  implements GLSurfaceView.Renderer
{
  private boolean a;
  private ImageOrientation b;
  private IntBuffer c;
  
  public TuSDKFilterPicture(Bitmap paramBitmap)
  {
    this(paramBitmap, false);
  }
  
  public TuSDKFilterPicture(Bitmap paramBitmap, boolean paramBoolean)
  {
    this(paramBitmap, paramBoolean, false);
  }
  
  public TuSDKFilterPicture(final Bitmap paramBitmap, boolean paramBoolean1, final boolean paramBoolean2)
  {
    if (paramBitmap == null)
    {
      TLog.e("SelesPicture:image is null", new Object[0]);
      return;
    }
    this.b = ImageOrientation.Up;
    this.a = false;
    setShouldSmoothlyScaleOutput(paramBoolean1);
    this.mInputTextureSize = TuSdkSize.create(paramBitmap);
    if (this.mInputTextureSize.minSide() <= 0)
    {
      TLog.e("Passed image must not be empty - it should be at least 1px tall and wide", new Object[0]);
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        TuSDKFilterPicture.a(TuSDKFilterPicture.this, SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.TEXTURE_ACTIVE, TuSDKFilterPicture.a(TuSDKFilterPicture.this)));
        TuSDKFilterPicture.b(TuSDKFilterPicture.this).disableReferenceCounting();
        GLES20.glBindTexture(3553, TuSDKFilterPicture.c(TuSDKFilterPicture.this).getTexture());
        if (TuSDKFilterPicture.this.isShouldSmoothlyScaleOutput()) {
          GLES20.glTexParameteri(3553, 10241, 9987);
        }
        GLUtils.texImage2D(3553, 0, paramBitmap, 0);
        if (TuSDKFilterPicture.this.isShouldSmoothlyScaleOutput()) {
          GLES20.glGenerateMipmap(3553);
        }
        GLES20.glBindTexture(3553, 0);
        if (paramBoolean2) {
          paramBitmap.recycle();
        }
      }
    });
  }
  
  public TuSDKFilterPicture(final ByteBuffer paramByteBuffer, final int paramInt1, final int paramInt2)
  {
    if (paramByteBuffer == null)
    {
      TLog.e("SelesPicture:singleChannalData is null", new Object[0]);
      return;
    }
    this.a = false;
    this.mInputTextureSize = TuSdkSize.create(paramInt1, paramInt2);
    runOnDraw(new Runnable()
    {
      public void run()
      {
        TuSDKFilterPicture.b(TuSDKFilterPicture.this, SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.TEXTURE_ACTIVE, TuSDKFilterPicture.d(TuSDKFilterPicture.this)));
        TuSDKFilterPicture.e(TuSDKFilterPicture.this).disableReferenceCounting();
        GLES20.glBindTexture(3553, TuSDKFilterPicture.f(TuSDKFilterPicture.this).getTexture());
        if (TuSDKFilterPicture.this.isShouldSmoothlyScaleOutput()) {
          GLES20.glTexParameteri(3553, 10241, 9987);
        }
        GLES20.glTexImage2D(3553, 0, 6409, paramInt1, paramInt2, 0, 6409, 5121, paramByteBuffer);
        if (TuSDKFilterPicture.this.isShouldSmoothlyScaleOutput()) {
          GLES20.glGenerateMipmap(3553);
        }
        GLES20.glBindTexture(3553, 0);
      }
    });
  }
  
  protected void onDestroy()
  {
    if (this.mOutputFramebuffer != null)
    {
      this.mOutputFramebuffer.enableReferenceCounting();
      this.mOutputFramebuffer.unlock();
    }
    this.mOutputFramebuffer = null;
  }
  
  public void removeAllTargets()
  {
    super.removeAllTargets();
    this.a = false;
  }
  
  public void setOutputRotation(ImageOrientation paramImageOrientation)
  {
    this.b = paramImageOrientation;
  }
  
  public void processImage()
  {
    a(null);
  }
  
  private void a(final Runnable paramRunnable)
  {
    this.a = true;
    runOnDraw(new Runnable()
    {
      public void run()
      {
        int i = 0;
        int j = TuSDKFilterPicture.g(TuSDKFilterPicture.this).size();
        SelesContext.SelesInput localSelesInput;
        int k;
        while (i < j)
        {
          localSelesInput = (SelesContext.SelesInput)TuSDKFilterPicture.h(TuSDKFilterPicture.this).get(i);
          k = ((Integer)TuSDKFilterPicture.i(TuSDKFilterPicture.this).get(i)).intValue();
          localSelesInput.setCurrentlyReceivingMonochromeInput(false);
          localSelesInput.setInputRotation(TuSDKFilterPicture.j(TuSDKFilterPicture.this), k);
          localSelesInput.setInputSize(TuSDKFilterPicture.k(TuSDKFilterPicture.this), k);
          localSelesInput.setInputFramebuffer(TuSDKFilterPicture.this.framebufferForOutput(), k);
          i++;
        }
        i = 0;
        j = TuSDKFilterPicture.l(TuSDKFilterPicture.this).size();
        while (i < j)
        {
          localSelesInput = (SelesContext.SelesInput)TuSDKFilterPicture.m(TuSDKFilterPicture.this).get(i);
          k = ((Integer)TuSDKFilterPicture.n(TuSDKFilterPicture.this).get(i)).intValue();
          localSelesInput.newFrameReady(0L, k);
          i++;
        }
        if (paramRunnable != null) {
          paramRunnable.run();
        }
      }
    });
  }
  
  public IntBuffer bufferFromCurrentlyProcessedOutput()
  {
    this.c = null;
    final Semaphore localSemaphore = new Semaphore(0);
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        SelesEGL10Core localSelesEGL10Core = SelesEGL10Core.create(TuSDKFilterPicture.this.outputImageSize());
        localSelesEGL10Core.setRenderer(TuSDKFilterPicture.this);
        TuSDKFilterPicture.a(TuSDKFilterPicture.this, localSelesEGL10Core.getImageBuffer());
        localSelesEGL10Core.destroy();
        localSemaphore.release();
      }
    });
    try
    {
      localSemaphore.acquire();
    }
    catch (InterruptedException localInterruptedException)
    {
      TLog.e(localInterruptedException, "imageFromCurrentlyProcessedOutput", new Object[0]);
    }
    return this.c;
  }
  
  public TuSdkSize outputImageSize()
  {
    TuSdkSize localTuSdkSize = new TuSdkSize(this.mInputTextureSize.width, this.mInputTextureSize.height);
    if ((this.b != null) && (this.b.isTransposed()))
    {
      localTuSdkSize.width = this.mInputTextureSize.height;
      localTuSdkSize.height = this.mInputTextureSize.width;
    }
    return localTuSdkSize;
  }
  
  public void addTarget(SelesContext.SelesInput paramSelesInput, int paramInt)
  {
    super.addTarget(paramSelesInput, paramInt);
    if (paramSelesInput == null) {
      return;
    }
    if (paramInt > 0) {
      paramSelesInput.mountAtGLThread(new Runnable()
      {
        public void run()
        {
          TuSDKFilterPicture.this.processImage();
          TuSDKFilterPicture.o(TuSDKFilterPicture.this);
        }
      });
    }
    if (this.a)
    {
      paramSelesInput.setInputSize(this.mInputTextureSize, paramInt);
      paramSelesInput.newFrameReady(0L, paramInt);
    }
  }
  
  public void onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig)
  {
    GLES20.glDisable(2929);
  }
  
  public void onSurfaceChanged(GL10 paramGL10, int paramInt1, int paramInt2) {}
  
  public void onDrawFrame(GL10 paramGL10)
  {
    runPendingOnDrawTasks();
  }
  
  public void mountAtGLThread(Runnable paramRunnable)
  {
    if (paramRunnable == null) {
      return;
    }
    runOnDraw(paramRunnable);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\video\preproc\filter\TuSDKFilterPicture.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */