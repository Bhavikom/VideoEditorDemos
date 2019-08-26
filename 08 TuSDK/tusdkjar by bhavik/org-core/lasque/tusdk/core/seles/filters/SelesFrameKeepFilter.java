package org.lasque.tusdk.core.seles.filters;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesFramebuffer;

public class SelesFrameKeepFilter
  extends SelesFilter
{
  private final ArrayList<SelesFramebuffer> a = new ArrayList();
  private int b = 1;
  
  public SelesFrameKeepFilter()
  {
    super("varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      SelesFramebuffer localSelesFramebuffer = (SelesFramebuffer)localIterator.next();
      localSelesFramebuffer.unlock();
    }
    this.a.clear();
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    if (this.a.size() < this.b)
    {
      this.mOutputFramebuffer = this.mFirstInputFramebuffer;
      this.mFirstInputFramebuffer.lock();
    }
    else
    {
      this.mOutputFramebuffer = ((SelesFramebuffer)this.a.get(0));
      this.a.remove(0);
    }
    this.a.add(this.mFirstInputFramebuffer);
    runPendingOnDrawTasks();
    Iterator localIterator = this.mTargets.iterator();
    while (localIterator.hasNext())
    {
      SelesContext.SelesInput localSelesInput = (SelesContext.SelesInput)localIterator.next();
      if (localSelesInput != getTargetToIgnoreForUpdates())
      {
        int i = this.mTargets.indexOf(localSelesInput);
        int j = ((Integer)this.mTargetTextureIndices.get(i)).intValue();
        localSelesInput.setInputRotation(this.mInputRotation, j);
      }
    }
    informTargetsAboutNewFrame(paramLong);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2) {}
  
  public int getKeepSize()
  {
    return this.b;
  }
  
  public void setKeepSize(int paramInt)
  {
    if ((paramInt == this.b) || (paramInt < 1)) {
      return;
    }
    int i;
    int j;
    if (paramInt > this.b)
    {
      i = paramInt - this.b;
      for (j = 0; j < i; j++) {}
    }
    else
    {
      i = this.b - paramInt;
      for (j = 0; j < i; j++)
      {
        SelesFramebuffer localSelesFramebuffer = (SelesFramebuffer)this.a.get(this.a.size() - 1);
        this.a.remove(this.a.size() - 1);
        localSelesFramebuffer.unlock();
      }
    }
    this.b = paramInt;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\filters\SelesFrameKeepFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */