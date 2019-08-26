package org.lasque.tusdk.core.seles.filters;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

public class SelesFrameDelayFilter
  extends SelesFilter
{
  private final List<SelesFramebuffer> a = new ArrayList();
  private int b = 0;
  private SelesContext.SelesInput c;
  private int d;
  private SelesContext.SelesInput e;
  private int f;
  private final Map<SelesContext.SelesInput, Integer> g = new LinkedHashMap();
  
  public int getDelaySize()
  {
    return this.b;
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  public void setDelaySize(int paramInt)
  {
    if (paramInt < 0) {
      return;
    }
    this.b = paramInt;
    flush();
  }
  
  public void setFirstTarget(SelesContext.SelesInput paramSelesInput, int paramInt)
  {
    this.c = paramSelesInput;
    this.d = paramInt;
  }
  
  public void setLastTarget(SelesContext.SelesInput paramSelesInput, int paramInt)
  {
    this.e = paramSelesInput;
    this.f = paramInt;
  }
  
  protected void onDestroy()
  {
    flush();
    super.onDestroy();
  }
  
  public void flush()
  {
    ArrayList localArrayList = new ArrayList(this.a);
    this.a.clear();
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      SelesFramebuffer localSelesFramebuffer = (SelesFramebuffer)localIterator.next();
      localSelesFramebuffer.unlock();
    }
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    if (this.mFirstInputFramebuffer == null) {
      return;
    }
    if ((this.c != null) && (this.c.isEnabled()) && (this.c != getTargetToIgnoreForUpdates()))
    {
      this.c.setInputFramebuffer(this.mFirstInputFramebuffer, this.d);
      this.c.setInputRotation(this.mInputRotation, this.d);
      this.c.setInputSize(this.mFirstInputFramebuffer.getSize(), this.d);
      this.c.newFrameReady(paramLong, this.d);
    }
    if (this.b > 0)
    {
      super.newFrameReady(paramLong, paramInt);
      return;
    }
    runPendingOnDrawTasks();
    this.mOutputFramebuffer = this.mFirstInputFramebuffer;
    this.mFirstInputFramebuffer = null;
    informTargetsAboutNewFrame(paramLong);
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    if (framebufferForOutput() == null) {
      return;
    }
    SelesFramebuffer localSelesFramebuffer = this.mOutputFramebuffer;
    if (this.b > 0)
    {
      if (this.a.size() < this.b) {
        this.mOutputFramebuffer.lock();
      } else {
        this.mOutputFramebuffer = ((SelesFramebuffer)this.a.remove(0));
      }
      this.a.add(localSelesFramebuffer);
    }
    this.g.clear();
    Iterator localIterator = this.mTargets.iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = (SelesContext.SelesInput)localIterator.next();
      if (((SelesContext.SelesInput)localObject).isEnabled()) {
        if (localObject != getTargetToIgnoreForUpdates())
        {
          int i = this.mTargets.indexOf(localObject);
          int j = ((Integer)this.mTargetTextureIndices.get(i)).intValue();
          this.g.put(localObject, Integer.valueOf(j));
          setInputFramebufferForTarget((SelesContext.SelesInput)localObject, j);
          if (this.b == 0) {
            ((SelesContext.SelesInput)localObject).setInputRotation(this.mInputRotation, j);
          }
          ((SelesContext.SelesInput)localObject).setInputSize(this.mInputTextureSize, j);
        }
      }
    }
    if ((this.e != null) && (this.e.isEnabled()) && (this.e != getTargetToIgnoreForUpdates()))
    {
      this.g.put(this.e, Integer.valueOf(this.f));
      this.e.setInputFramebuffer(localSelesFramebuffer, this.f);
      if (this.b == 0) {
        this.e.setInputRotation(this.mInputRotation, this.f);
      }
      this.e.setInputSize(this.mInputTextureSize, this.f);
    }
    if (framebufferForOutput() != null) {
      framebufferForOutput().unlock();
    }
    removeOutputFramebuffer();
    localIterator = this.g.entrySet().iterator();
    while (localIterator.hasNext())
    {
      localObject = (Map.Entry)localIterator.next();
      ((SelesContext.SelesInput)((Map.Entry)localObject).getKey()).newFrameReady(paramLong, ((Integer)((Map.Entry)localObject).getValue()).intValue());
    }
    if ((this.c != null) && (this.c.isEnabled()) && (this.c != getTargetToIgnoreForUpdates())) {
      this.c.setCurrentlyReceivingMonochromeInput(true);
    }
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    if (this.b > 0)
    {
      super.setInputSize(paramTuSdkSize, paramInt);
      return;
    }
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return;
    }
    this.mInputTextureSize = paramTuSdkSize;
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    checkGLError(getClass().getSimpleName());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
  
  public void useNextFrameForImageCapture()
  {
    TLog.d("%s unsupport useNextFrameForImageCapture", new Object[] { "SelesFrameDelayFilter" });
  }
  
  public IntBuffer imageBufferFromCurrentlyProcessedOutput(TuSdkSize paramTuSdkSize)
  {
    TLog.d("%s unsupport imageBufferFromCurrentlyProcessedOutput", new Object[] { "SelesFrameDelayFilter" });
    return null;
  }
  
  public SelesContext.SelesInput getLastTarget()
  {
    return this.e;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\filters\SelesFrameDelayFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */