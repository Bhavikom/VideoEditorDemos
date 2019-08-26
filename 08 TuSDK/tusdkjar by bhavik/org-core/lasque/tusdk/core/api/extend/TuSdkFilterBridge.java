package org.lasque.tusdk.core.api.extend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkFilterBridge
  extends SelesOutInput
{
  private TuSdkSurfaceDraw a;
  private SelesFramebuffer b;
  private final List<SelesContext.SelesInput> c = new ArrayList();
  
  public void setSurfaceDraw(TuSdkSurfaceDraw paramTuSdkSurfaceDraw)
  {
    this.a = paramTuSdkSurfaceDraw;
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    if (paramTuSdkSize == null) {
      return;
    }
    this.mInputTextureSize = paramTuSdkSize;
  }
  
  public void setInputFramebuffer(SelesFramebuffer paramSelesFramebuffer, int paramInt)
  {
    if (paramSelesFramebuffer == null) {
      return;
    }
    this.b = paramSelesFramebuffer;
    this.b.lock();
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
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    runPendingOnDrawTasks();
    SelesFramebuffer localSelesFramebuffer = this.b;
    TuSdkSize localTuSdkSize = this.mInputTextureSize;
    if ((localSelesFramebuffer == null) || (!localTuSdkSize.isSize())) {
      return;
    }
    int i = localSelesFramebuffer.getTexture();
    if (this.a != null) {
      i = this.a.onDrawFrame(i, localTuSdkSize.width, localTuSdkSize.height, paramLong);
    }
    if (i == localSelesFramebuffer.getTexture())
    {
      this.mOutputFramebuffer = localSelesFramebuffer;
    }
    else
    {
      localSelesFramebuffer.unlock();
      this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.HOLDER, localTuSdkSize, i);
    }
    this.b = null;
    a(paramLong, localSelesFramebuffer, localTuSdkSize);
  }
  
  private void a(long paramLong, SelesFramebuffer paramSelesFramebuffer, TuSdkSize paramTuSdkSize)
  {
    this.c.clear();
    Iterator localIterator = this.mTargets.iterator();
    SelesContext.SelesInput localSelesInput;
    int i;
    int j;
    while (localIterator.hasNext())
    {
      localSelesInput = (SelesContext.SelesInput)localIterator.next();
      if (localSelesInput.isEnabled()) {
        if (localSelesInput != getTargetToIgnoreForUpdates())
        {
          this.c.add(localSelesInput);
          i = this.mTargets.indexOf(localSelesInput);
          j = ((Integer)this.mTargetTextureIndices.get(i)).intValue();
          setInputFramebufferForTarget(localSelesInput, j);
          localSelesInput.setInputSize(paramTuSdkSize, j);
        }
      }
    }
    if (this.mOutputFramebuffer != null)
    {
      this.mOutputFramebuffer.unlock();
      this.mOutputFramebuffer = null;
    }
    localIterator = this.c.iterator();
    while (localIterator.hasNext())
    {
      localSelesInput = (SelesContext.SelesInput)localIterator.next();
      i = this.mTargets.indexOf(localSelesInput);
      j = ((Integer)this.mTargetTextureIndices.get(i)).intValue();
      localSelesInput.newFrameReady(paramLong, j);
    }
    if (this.a != null) {
      this.a.onDrawFrameCompleted();
    }
  }
  
  public int nextAvailableTextureIndex()
  {
    return 0;
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt) {}
  
  public TuSdkSize maximumOutputSize()
  {
    return this.mInputTextureSize;
  }
  
  public void endProcessing() {}
  
  public boolean wantsMonochromeInput()
  {
    return false;
  }
  
  public void setCurrentlyReceivingMonochromeInput(boolean paramBoolean) {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\extend\TuSdkFilterBridge.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */