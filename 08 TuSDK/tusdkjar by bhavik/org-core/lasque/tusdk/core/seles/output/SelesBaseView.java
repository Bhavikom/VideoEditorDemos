package org.lasque.tusdk.core.seles.output;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.opengl.GLSurfaceView.Renderer;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import java.nio.IntBuffer;
import java.util.concurrent.Semaphore;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesEGLContextFactory;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.view.TuSdkViewInterface;

public abstract class SelesBaseView
  extends FrameLayout
  implements SelesContext.SelesInput, SelesViewInterface, TuSdkViewInterface
{
  private SelesSurfaceView a;
  private boolean b = true;
  protected TuSdkSize mSizeInPixels;
  private SelesSurfacePusher c;
  private SelesVerticeCoordinateBuilder d;
  
  public boolean isCreatedSurface()
  {
    return this.a.isCreated();
  }
  
  public SelesBaseView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initView(paramContext, paramAttributeSet);
  }
  
  public SelesBaseView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView(paramContext, paramAttributeSet);
  }
  
  public SelesBaseView(Context paramContext)
  {
    super(paramContext);
    initView(paramContext, null);
  }
  
  protected void initView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this.a = new SelesSurfaceView(paramContext, paramAttributeSet);
    addView(this.a);
    this.a.setEGLContextClientVersion(2);
    this.a.setEGLContextFactory(new SelesEGLContextFactory(2));
    this.mSizeInPixels = new TuSdkSize();
    this.c = buildWindowDisplay();
    this.d = buildVerticeCoordinateBuilder();
    if (this.c != null) {
      this.c.setTextureCoordinateBuilder(this.d);
    }
  }
  
  protected abstract SelesSurfacePusher buildWindowDisplay();
  
  protected abstract SelesVerticeCoordinateBuilder buildVerticeCoordinateBuilder();
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramInt3 - paramInt1, paramInt4 - paramInt2);
    a(localTuSdkSize);
  }
  
  private void a(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize.equals(this.mSizeInPixels)) || (!paramTuSdkSize.isSize())) {
      return;
    }
    this.mSizeInPixels = paramTuSdkSize;
    if (this.d != null) {
      this.d.setOutputSize(paramTuSdkSize.copy());
    }
  }
  
  public void loadView() {}
  
  public void viewDidLoad() {}
  
  public void viewNeedRest() {}
  
  public void viewWillDestory()
  {
    setRenderModeDirty();
  }
  
  public void setZOrderOnTop(Boolean paramBoolean)
  {
    if (this.a != null) {
      this.a.setZOrderOnTop(paramBoolean.booleanValue());
    }
  }
  
  public void setZOrderMediaOverlay(Boolean paramBoolean)
  {
    if (this.a != null) {
      this.a.setZOrderMediaOverlay(paramBoolean.booleanValue());
    }
  }
  
  public void setRenderer(GLSurfaceView.Renderer paramRenderer)
  {
    if (paramRenderer == null) {
      return;
    }
    this.a.setRenderer(paramRenderer);
    setRenderModeDirty();
    requestRender();
  }
  
  public void setRenderMode(int paramInt)
  {
    this.a.setRenderMode(paramInt);
  }
  
  public int getRenderMode()
  {
    return this.a.getRenderMode();
  }
  
  public void setRenderModeDirty()
  {
    setRenderMode(0);
  }
  
  public void setRenderModeContinuously()
  {
    setRenderMode(1);
  }
  
  public void setEnableRenderer(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  public boolean isEnableRenderer()
  {
    return this.b;
  }
  
  public void requestRender()
  {
    this.a.requestRender();
  }
  
  public void onPause()
  {
    this.a.onPause();
  }
  
  public void onResume()
  {
    this.a.onResume();
  }
  
  public int getRendererFPS()
  {
    return this.a.getRendererFPS();
  }
  
  public void setRendererFPS(int paramInt)
  {
    this.a.setRendererFPS(paramInt);
  }
  
  public void setEnableFixedFrameRate(boolean paramBoolean)
  {
    this.a.setEnableFixedFrameRate(paramBoolean);
  }
  
  public void setBackgroundColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (this.c == null) {
      return;
    }
    this.c.setBackgroundColor(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void setBackgroundColor(int paramInt)
  {
    super.setBackgroundColor(paramInt);
    setBackgroundColor(Color.red(paramInt) / 255.0F, Color.green(paramInt) / 255.0F, Color.blue(paramInt) / 255.0F, Color.alpha(paramInt) / 255.0F);
  }
  
  public void mountAtGLThread(Runnable paramRunnable)
  {
    if (this.c == null) {
      return;
    }
    this.c.runOnDraw(paramRunnable);
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    if (!isEnableRenderer()) {
      return;
    }
    if (this.c == null) {
      return;
    }
    this.c.newFrameReady(paramLong, paramInt);
  }
  
  public int nextAvailableTextureIndex()
  {
    if (this.c == null) {
      return 0;
    }
    return this.c.nextAvailableTextureIndex();
  }
  
  public void setInputFramebuffer(SelesFramebuffer paramSelesFramebuffer, int paramInt)
  {
    if (paramSelesFramebuffer == null) {
      return;
    }
    if (this.c == null) {
      return;
    }
    this.c.setInputFramebuffer(paramSelesFramebuffer, paramInt);
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    if (this.c == null) {
      return;
    }
    this.c.setInputRotation(paramImageOrientation, paramInt);
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    if (this.c == null) {
      return;
    }
    this.c.setInputSize(paramTuSdkSize, paramInt);
  }
  
  public TuSdkSize maximumOutputSize()
  {
    if (this.c == null)
    {
      TuSdkSize localTuSdkSize = TuSdkSize.create(getWidth(), getHeight());
      if (localTuSdkSize.isSize()) {
        return localTuSdkSize;
      }
      return this.mSizeInPixels;
    }
    return this.c.maximumOutputSize();
  }
  
  public Bitmap imageFromCurrentlyProcessedOutput()
  {
    if (this.c == null) {
      return null;
    }
    final Bitmap localBitmap = Bitmap.createBitmap(this.c.getInputImageSize().width, this.c.getInputImageSize().height, Bitmap.Config.ARGB_8888);
    final Semaphore localSemaphore = new Semaphore(0);
    this.c.mountAtGLThread(new Runnable()
    {
      public void run()
      {
        TLog.d("image capture", new Object[0]);
        GL10 localGL10 = SelesContext.currentGL();
        if (localGL10 != null)
        {
          IntBuffer localIntBuffer = IntBuffer.allocate(localBitmap.getWidth() * localBitmap.getHeight());
          localGL10.glReadPixels(0, 0, localBitmap.getWidth(), localBitmap.getHeight(), 6408, 5121, localIntBuffer);
          localBitmap.copyPixelsFromBuffer(localIntBuffer);
        }
        localSemaphore.release();
      }
    });
    requestRender();
    try
    {
      localSemaphore.acquire();
    }
    catch (InterruptedException localInterruptedException)
    {
      TLog.e(localInterruptedException, "imageFromCurrentlyProcessedOutput", new Object[0]);
    }
    return localBitmap;
  }
  
  public void endProcessing() {}
  
  public boolean isShouldIgnoreUpdatesToThisTarget()
  {
    return false;
  }
  
  public boolean wantsMonochromeInput()
  {
    return false;
  }
  
  public void setCurrentlyReceivingMonochromeInput(boolean paramBoolean) {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\output\SelesBaseView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */