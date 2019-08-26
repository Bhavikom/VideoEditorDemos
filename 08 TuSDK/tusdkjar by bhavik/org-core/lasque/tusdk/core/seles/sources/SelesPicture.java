package org.lasque.tusdk.core.seles.sources;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import java.nio.ByteBuffer;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.TuSdkSemaphore;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public final class SelesPicture
  extends SelesOutput
  implements GLSurfaceView.Renderer
{
  private boolean a;
  private ImageOrientation b = ImageOrientation.Up;
  private Rect c;
  protected TuSdkSemaphore mImageUpdateSemaphore;
  private Bitmap d;
  
  public SelesPicture(Bitmap paramBitmap)
  {
    this(paramBitmap, false);
  }
  
  public SelesPicture(Bitmap paramBitmap, boolean paramBoolean)
  {
    this(paramBitmap, paramBoolean, false);
  }
  
  public SelesPicture(final Bitmap paramBitmap, boolean paramBoolean1, final boolean paramBoolean2)
  {
    if (paramBitmap == null)
    {
      TLog.e("SelesPicture:image is null", new Object[0]);
      return;
    }
    this.a = false;
    setShouldSmoothlyScaleOutput(paramBoolean1);
    this.mImageUpdateSemaphore = new TuSdkSemaphore(0);
    this.mImageUpdateSemaphore.signal();
    this.mInputTextureSize = TuSdkSize.create(paramBitmap);
    if (this.mInputTextureSize.minSide() <= 0)
    {
      TLog.e("%s Passed image must not be empty - it should be at least 1px tall and wide", new Object[] { "SelesPicture" });
      return;
    }
    this.mInputTextureSize = SelesContext.sizeThatFitsWithinATexture(this.mInputTextureSize.copy());
    this.c = new Rect(0, 0, this.mInputTextureSize.width, this.mInputTextureSize.height);
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesPicture.this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.TEXTURE_ACTIVE, SelesPicture.this.mInputTextureSize);
        SelesPicture.this.mOutputFramebuffer.bindTexture(paramBitmap, SelesPicture.this.isShouldSmoothlyScaleOutput(), paramBoolean2);
      }
    });
  }
  
  public SelesPicture(final ByteBuffer paramByteBuffer, final int paramInt1, final int paramInt2)
  {
    if (paramByteBuffer == null)
    {
      TLog.e("SelesPicture:singleChannalData is null", new Object[0]);
      return;
    }
    this.a = false;
    this.mInputTextureSize = TuSdkSize.create(paramInt1, paramInt2);
    this.c = new Rect(0, 0, paramInt1, paramInt2);
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesPicture.this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.TEXTURE_ACTIVE, SelesPicture.this.mInputTextureSize);
        SelesPicture.this.mOutputFramebuffer.bindTextureLuminance(paramByteBuffer, paramInt1, paramInt2, SelesPicture.this.isShouldSmoothlyScaleOutput());
      }
    });
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation)
  {
    if (paramImageOrientation == null) {
      return;
    }
    this.b = paramImageOrientation;
  }
  
  public void setScaleSize(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (paramTuSdkSize.width > this.mInputTextureSize.width) || (paramTuSdkSize.height > this.mInputTextureSize.height)) {
      return;
    }
    this.mInputTextureSize = paramTuSdkSize;
  }
  
  public TuSdkSize getScaleSize()
  {
    return this.mInputTextureSize;
  }
  
  public void setOutputRect(Rect paramRect)
  {
    if ((paramRect == null) || (paramRect.width() <= 0) || (paramRect.height() <= 0) || (paramRect.right > this.mInputTextureSize.width) || (paramRect.bottom > this.mInputTextureSize.height)) {
      return;
    }
    this.c = paramRect;
  }
  
  public Rect getOutputRect()
  {
    Rect localRect = RectHelper.rotationWithRotation(this.c, this.mInputTextureSize, this.b);
    return localRect;
  }
  
  public TuSdkSize outputImageSize()
  {
    return TuSdkSize.create(getOutputRect());
  }
  
  protected void onDestroy()
  {
    if (this.mOutputFramebuffer != null)
    {
      SelesContext.returnFramebufferToCache(this.mOutputFramebuffer);
      this.mOutputFramebuffer = null;
    }
    if (this.mImageUpdateSemaphore != null)
    {
      this.mImageUpdateSemaphore.release();
      this.mImageUpdateSemaphore = null;
    }
  }
  
  public void removeAllTargets()
  {
    super.removeAllTargets();
    this.a = false;
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
          SelesPicture.this.processImage();
          SelesPicture.this.runPendingOnDrawTasks();
        }
      });
    }
    if (this.a)
    {
      paramSelesInput.setInputSize(getScaleSize(), paramInt);
      paramSelesInput.newFrameReady(0L, paramInt);
    }
  }
  
  public boolean processImage()
  {
    return a(null);
  }
  
  private boolean a(final Runnable paramRunnable)
  {
    if (this.mImageUpdateSemaphore == null) {
      return false;
    }
    this.a = true;
    if (!this.mImageUpdateSemaphore.waitSignal(0L)) {
      return false;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        int i = 0;
        int j = SelesPicture.this.mTargets.size();
        while (i < j)
        {
          SelesContext.SelesInput localSelesInput = (SelesContext.SelesInput)SelesPicture.this.mTargets.get(i);
          int k = ((Integer)SelesPicture.this.mTargetTextureIndices.get(i)).intValue();
          localSelesInput.setCurrentlyReceivingMonochromeInput(false);
          localSelesInput.setInputRotation(SelesPicture.a(SelesPicture.this), k);
          localSelesInput.setInputSize(SelesPicture.this.getScaleSize(), k);
          localSelesInput.setInputFramebuffer(SelesPicture.this.framebufferForOutput(), k);
          localSelesInput.newFrameReady(System.nanoTime(), k);
          i++;
        }
        if (SelesPicture.this.mImageUpdateSemaphore != null) {
          SelesPicture.this.mImageUpdateSemaphore.signal();
        }
        if (paramRunnable != null) {
          paramRunnable.run();
        }
      }
    });
    return true;
  }
  
  public boolean processImageUpToFilter()
  {
    processImage();
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        SelesEGL10Core localSelesEGL10Core = SelesEGL10Core.create(SelesPicture.this.mInputTextureSize.transforOrientation(SelesPicture.a(SelesPicture.this)));
        GLES20.glDisable(2929);
        SelesPicture.this.runPendingOnDrawTasks();
        localSelesEGL10Core.destroy();
      }
    });
    return true;
  }
  
  public Bitmap imageFromCurrentlyProcessedOutput()
  {
    if (this.mImageUpdateSemaphore == null) {
      return null;
    }
    this.d = null;
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        SelesEGL10Core localSelesEGL10Core = SelesEGL10Core.create(SelesPicture.this.mInputTextureSize.transforOrientation(SelesPicture.a(SelesPicture.this)));
        localSelesEGL10Core.setOutputRect(SelesPicture.this.getOutputRect());
        GLES20.glDisable(2929);
        SelesPicture.this.runPendingOnDrawTasks();
        SelesPicture.a(SelesPicture.this, localSelesEGL10Core.getBitmap());
        if (SelesPicture.this.mImageUpdateSemaphore != null) {
          SelesPicture.this.mImageUpdateSemaphore.signal();
        }
        localSelesEGL10Core.destroy();
      }
    });
    if (!this.mImageUpdateSemaphore.waitSignal(2, 10000L))
    {
      TLog.w("%s imageFromCurrentlyProcessedOutput timeout", new Object[] { "SelesPicture" });
      return this.d;
    }
    if (this.mImageUpdateSemaphore != null) {
      this.mImageUpdateSemaphore.signal();
    }
    return this.d;
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


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesPicture.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */