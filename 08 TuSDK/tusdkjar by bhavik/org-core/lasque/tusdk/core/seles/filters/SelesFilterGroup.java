package org.lasque.tusdk.core.seles.filters;

import android.graphics.Bitmap;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesFilterGroup
  extends SelesOutInput
{
  protected List<SelesOutInput> mFilters = new ArrayList();
  protected boolean mIsEndProcessing;
  private SelesOutInput a;
  private List<SelesOutInput> b;
  private SelesOutInput c;
  
  public SelesOutInput getTerminalFilter()
  {
    return this.a;
  }
  
  public void setTerminalFilter(SelesOutInput paramSelesOutInput)
  {
    this.a = paramSelesOutInput;
  }
  
  public List<SelesOutInput> getInitialFilters()
  {
    return this.b;
  }
  
  public void setInitialFilters(SelesOutInput... paramVarArgs)
  {
    if (paramVarArgs == null) {
      return;
    }
    this.b = new ArrayList(Arrays.asList(paramVarArgs));
  }
  
  public SelesOutInput getInputFilterToIgnoreForUpdates()
  {
    return this.c;
  }
  
  public void setInputFilterToIgnoreForUpdates(SelesOutInput paramSelesOutInput)
  {
    this.c = paramSelesOutInput;
  }
  
  protected void onDestroy() {}
  
  public void addFilter(SelesOutInput paramSelesOutInput)
  {
    if ((paramSelesOutInput == null) || (this.mFilters.contains(paramSelesOutInput))) {
      return;
    }
    this.mFilters.add(paramSelesOutInput);
  }
  
  public SelesOutInput filterAtIndex(int paramInt)
  {
    return (SelesOutInput)this.mFilters.get(paramInt);
  }
  
  public int filterCount()
  {
    return this.mFilters.size();
  }
  
  public void useNextFrameForImageCapture()
  {
    this.a.useNextFrameForImageCapture();
  }
  
  public IntBuffer imageBufferFromCurrentlyProcessedOutput(TuSdkSize paramTuSdkSize)
  {
    return this.a.imageBufferFromCurrentlyProcessedOutput(paramTuSdkSize);
  }
  
  public Bitmap imageFromCurrentlyProcessedOutput()
  {
    return this.a.imageFromCurrentlyProcessedOutput();
  }
  
  public void setTargetToIgnoreForUpdates(SelesContext.SelesInput paramSelesInput)
  {
    this.a.setTargetToIgnoreForUpdates(paramSelesInput);
  }
  
  public void addTarget(SelesContext.SelesInput paramSelesInput, int paramInt)
  {
    this.a.addTarget(paramSelesInput, paramInt);
  }
  
  public void removeTarget(SelesContext.SelesInput paramSelesInput)
  {
    this.a.removeTarget(paramSelesInput);
  }
  
  public void removeAllTargets()
  {
    this.a.removeAllTargets();
  }
  
  public List<SelesContext.SelesInput> targets()
  {
    return this.a.targets();
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      SelesOutInput localSelesOutInput = (SelesOutInput)localIterator.next();
      if (localSelesOutInput != getInputFilterToIgnoreForUpdates()) {
        localSelesOutInput.newFrameReady(paramLong, paramInt);
      }
    }
  }
  
  public void setInputFramebuffer(SelesFramebuffer paramSelesFramebuffer, int paramInt)
  {
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      SelesOutInput localSelesOutInput = (SelesOutInput)localIterator.next();
      localSelesOutInput.setInputFramebuffer(paramSelesFramebuffer, paramInt);
    }
  }
  
  public int nextAvailableTextureIndex()
  {
    return 0;
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      SelesOutInput localSelesOutInput = (SelesOutInput)localIterator.next();
      localSelesOutInput.setInputSize(paramTuSdkSize, paramInt);
    }
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      SelesOutInput localSelesOutInput = (SelesOutInput)localIterator.next();
      localSelesOutInput.setInputRotation(paramImageOrientation, paramInt);
    }
  }
  
  public void forceProcessingAtSize(TuSdkSize paramTuSdkSize)
  {
    Iterator localIterator = this.mFilters.iterator();
    while (localIterator.hasNext())
    {
      SelesOutInput localSelesOutInput = (SelesOutInput)localIterator.next();
      localSelesOutInput.forceProcessingAtSize(paramTuSdkSize);
    }
  }
  
  public void forceProcessingAtSizeRespectingAspectRatio(TuSdkSize paramTuSdkSize)
  {
    Iterator localIterator = this.mFilters.iterator();
    while (localIterator.hasNext())
    {
      SelesOutInput localSelesOutInput = (SelesOutInput)localIterator.next();
      localSelesOutInput.forceProcessingAtSizeRespectingAspectRatio(paramTuSdkSize);
    }
  }
  
  public TuSdkSize maximumOutputSize()
  {
    return new TuSdkSize();
  }
  
  public void endProcessing()
  {
    if (!this.mIsEndProcessing)
    {
      this.mIsEndProcessing = true;
      Iterator localIterator = this.b.iterator();
      while (localIterator.hasNext())
      {
        SelesOutInput localSelesOutInput = (SelesOutInput)localIterator.next();
        localSelesOutInput.endProcessing();
      }
    }
  }
  
  public boolean wantsMonochromeInput()
  {
    boolean bool = true;
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      SelesOutInput localSelesOutInput = (SelesOutInput)localIterator.next();
      bool = (bool) && (localSelesOutInput.wantsMonochromeInput());
    }
    return bool;
  }
  
  public void setCurrentlyReceivingMonochromeInput(boolean paramBoolean)
  {
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      SelesOutInput localSelesOutInput = (SelesOutInput)localIterator.next();
      localSelesOutInput.setCurrentlyReceivingMonochromeInput(paramBoolean);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\filters\SelesFilterGroup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */