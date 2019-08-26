package org.lasque.tusdk.core.seles.sources;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLES20;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesTextureOptions;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract class SelesOutput
{
  private final BlockingQueue<Runnable> a = new LinkedBlockingQueue();
  protected SelesFramebuffer mOutputFramebuffer;
  protected final List<SelesContext.SelesInput> mTargets = new ArrayList();
  protected final List<Integer> mTargetTextureIndices = new ArrayList();
  protected TuSdkSize mInputTextureSize = new TuSdkSize();
  protected TuSdkSize mCachedMaximumOutputSize = new TuSdkSize();
  protected TuSdkSize mForcedMaximumSize = new TuSdkSize();
  protected boolean mOverrideInputSize;
  protected boolean mAllTargetsWantMonochromeData = true;
  protected boolean mUsingNextFrameForImageCapture = false;
  private boolean b;
  private boolean c;
  private SelesContext.SelesInput d;
  private boolean e = true;
  private SelesFramebuffer.SelesTextureOptions f = new SelesFramebuffer.SelesTextureOptions();
  
  public boolean isShouldSmoothlyScaleOutput()
  {
    return this.b;
  }
  
  public void setShouldSmoothlyScaleOutput(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  public boolean isShouldIgnoreUpdatesToThisTarget()
  {
    return this.c;
  }
  
  public void setShouldIgnoreUpdatesToThisTarget(boolean paramBoolean)
  {
    this.c = paramBoolean;
  }
  
  public SelesContext.SelesInput getTargetToIgnoreForUpdates()
  {
    return this.d;
  }
  
  public void setTargetToIgnoreForUpdates(SelesContext.SelesInput paramSelesInput)
  {
    this.d = paramSelesInput;
  }
  
  public boolean isEnabled()
  {
    return this.e;
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    this.e = paramBoolean;
  }
  
  public SelesFramebuffer.SelesTextureOptions getOutputTextureOptions()
  {
    return this.f;
  }
  
  public void setOutputTextureOptions(SelesFramebuffer.SelesTextureOptions paramSelesTextureOptions)
  {
    this.f = paramSelesTextureOptions;
  }
  
  protected void finalize()
  {
    destroy();
    removeAllTargets();
    super.finalize();
  }
  
  public final void destroy()
  {
    onDestroy();
  }
  
  protected abstract void onDestroy();
  
  public void setInputFramebufferForTarget(SelesContext.SelesInput paramSelesInput, int paramInt)
  {
    if (paramSelesInput != null) {
      paramSelesInput.setInputFramebuffer(framebufferForOutput(), paramInt);
    } else {
      TLog.e("%s setInputFramebufferForTarget target is null", new Object[] { getClass() });
    }
  }
  
  public SelesFramebuffer framebufferForOutput()
  {
    return this.mOutputFramebuffer;
  }
  
  public void removeOutputFramebuffer()
  {
    this.mOutputFramebuffer = null;
  }
  
  public void notifyTargetsAboutNewOutputTexture()
  {
    List localList = targets();
    int i = 0;
    int j = localList.size();
    while (i < j)
    {
      SelesContext.SelesInput localSelesInput = (SelesContext.SelesInput)localList.get(i);
      if (localSelesInput.isEnabled())
      {
        int k = ((Integer)this.mTargetTextureIndices.get(i)).intValue();
        setInputFramebufferForTarget(localSelesInput, k);
      }
      i++;
    }
  }
  
  public List<SelesContext.SelesInput> targets()
  {
    return new ArrayList(this.mTargets);
  }
  
  public void addTarget(SelesContext.SelesInput paramSelesInput)
  {
    if (paramSelesInput == null)
    {
      TLog.e("%s addTarget newTarget is null", new Object[] { getClass() });
      return;
    }
    int i = paramSelesInput.nextAvailableTextureIndex();
    addTarget(paramSelesInput, i);
    if (paramSelesInput.isShouldIgnoreUpdatesToThisTarget()) {
      this.d = paramSelesInput;
    }
  }
  
  public void addTarget(final SelesContext.SelesInput paramSelesInput, final int paramInt)
  {
    if (paramSelesInput == null)
    {
      TLog.e("%s addTarget:newTarget:textureLocation is null", new Object[] { getClass() });
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        if (SelesOutput.this.mTargets.contains(paramSelesInput)) {
          return;
        }
        SelesOutput.this.mCachedMaximumOutputSize = new TuSdkSize();
        SelesOutput.this.setInputFramebufferForTarget(paramSelesInput, paramInt);
        SelesOutput.this.mTargets.add(paramSelesInput);
        SelesOutput.this.mTargetTextureIndices.add(Integer.valueOf(paramInt));
        SelesOutput.this.mAllTargetsWantMonochromeData = ((SelesOutput.this.mAllTargetsWantMonochromeData) && (paramSelesInput.wantsMonochromeInput()));
      }
    });
  }
  
  public void removeTarget(final SelesContext.SelesInput paramSelesInput)
  {
    if (paramSelesInput == null) {
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        if (!SelesOutput.this.mTargets.contains(paramSelesInput)) {
          return;
        }
        if (paramSelesInput.equals(SelesOutput.a(SelesOutput.this))) {
          SelesOutput.a(SelesOutput.this, null);
        }
        SelesOutput.this.mCachedMaximumOutputSize = new TuSdkSize();
        int i = SelesOutput.this.mTargets.indexOf(paramSelesInput);
        int j = ((Integer)SelesOutput.this.mTargetTextureIndices.get(i)).intValue();
        paramSelesInput.setInputSize(new TuSdkSize(), j);
        paramSelesInput.setInputRotation(ImageOrientation.Up, j);
        SelesOutput.this.mTargetTextureIndices.remove(i);
        SelesOutput.this.mTargets.remove(paramSelesInput);
        paramSelesInput.endProcessing();
      }
    });
  }
  
  public void removeAllTargets()
  {
    this.mCachedMaximumOutputSize = new TuSdkSize();
    runOnDraw(new Runnable()
    {
      public void run()
      {
        int i = 0;
        int j = SelesOutput.this.mTargets.size();
        while (i < j)
        {
          SelesContext.SelesInput localSelesInput = (SelesContext.SelesInput)SelesOutput.this.mTargets.get(i);
          int k = ((Integer)SelesOutput.this.mTargetTextureIndices.get(i)).intValue();
          localSelesInput.setInputSize(new TuSdkSize(), k);
          localSelesInput.setInputRotation(ImageOrientation.Up, k);
          i++;
        }
        SelesOutput.this.mTargets.clear();
        SelesOutput.this.mTargetTextureIndices.clear();
        SelesOutput.this.mAllTargetsWantMonochromeData = true;
      }
    });
  }
  
  public void forceProcessingAtSize(TuSdkSize paramTuSdkSize) {}
  
  public void forceProcessingAtSizeRespectingAspectRatio(TuSdkSize paramTuSdkSize) {}
  
  public void useNextFrameForImageCapture() {}
  
  public IntBuffer imageBufferFromCurrentlyProcessedOutput(TuSdkSize paramTuSdkSize)
  {
    return null;
  }
  
  public Bitmap imageFromCurrentlyProcessedOutput()
  {
    TuSdkSize localTuSdkSize = TuSdkSize.create(0);
    IntBuffer localIntBuffer = imageBufferFromCurrentlyProcessedOutput(localTuSdkSize);
    if (localIntBuffer == null) {
      return null;
    }
    Bitmap localBitmap = Bitmap.createBitmap(localTuSdkSize.width, localTuSdkSize.height, Bitmap.Config.ARGB_8888);
    localBitmap.copyPixelsFromBuffer(localIntBuffer);
    return localBitmap;
  }
  
  public Bitmap imageByFilteringImage(Bitmap paramBitmap)
  {
    if (!(this instanceof SelesContext.SelesInput)) {
      return null;
    }
    SelesPicture localSelesPicture = new SelesPicture(paramBitmap);
    useNextFrameForImageCapture();
    localSelesPicture.addTarget((SelesContext.SelesInput)this, 0);
    localSelesPicture.processImageUpToFilter();
    Bitmap localBitmap = imageFromCurrentlyProcessedOutput();
    localSelesPicture.removeTarget((SelesContext.SelesInput)this);
    return localBitmap;
  }
  
  public boolean providesMonochromeOutput()
  {
    return false;
  }
  
  public void setOutputOption(SelesFramebuffer.SelesTextureOptions paramSelesTextureOptions)
  {
    if (paramSelesTextureOptions == null) {
      return;
    }
    this.f = paramSelesTextureOptions;
    if ((this.mOutputFramebuffer != null) && (this.mOutputFramebuffer.getTexture() > 0))
    {
      GLES20.glBindTexture(3553, this.mOutputFramebuffer.getTexture());
      GLES20.glTexParameteri(3553, 10242, this.f.wrapS);
      GLES20.glTexParameteri(3553, 10243, this.f.wrapT);
      GLES20.glBindTexture(3553, 0);
    }
  }
  
  protected void runPendingOnDrawTasks()
  {
    while (!this.a.isEmpty()) {
      try
      {
        ((Runnable)this.a.take()).run();
      }
      catch (InterruptedException localInterruptedException)
      {
        TLog.e(localInterruptedException, "SelesOutput: %s", new Object[] { getClass() });
      }
    }
  }
  
  protected boolean isOnDrawTasksEmpty()
  {
    return this.a.isEmpty();
  }
  
  protected void runOnDraw(Runnable paramRunnable)
  {
    if (paramRunnable == null) {
      return;
    }
    synchronized (this.a)
    {
      this.a.add(paramRunnable);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesOutput.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */