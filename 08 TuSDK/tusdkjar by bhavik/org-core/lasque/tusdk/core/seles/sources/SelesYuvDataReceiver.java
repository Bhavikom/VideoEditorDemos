package org.lasque.tusdk.core.seles.sources;

import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.secret.ColorSpaceConvert;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesYuvDataReceiver
  extends SelesOutput
{
  private boolean a;
  private ImageOrientation b = ImageOrientation.Up;
  private IntBuffer c;
  private boolean d;
  private final Map<SelesContext.SelesInput, Integer> e = new LinkedHashMap();
  
  protected void onDestroy()
  {
    a();
  }
  
  private void a()
  {
    if (this.mOutputFramebuffer != null)
    {
      this.mOutputFramebuffer.enableReferenceCounting();
      this.mOutputFramebuffer.unlock();
      this.mOutputFramebuffer = null;
    }
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize()) || (paramTuSdkSize.equals(this.mInputTextureSize))) {
      return;
    }
    this.mInputTextureSize = TuSdkSize.create(paramTuSdkSize);
    this.a = true;
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation)
  {
    if ((paramImageOrientation == null) || (paramImageOrientation == this.b)) {
      return;
    }
    this.b = paramImageOrientation;
    this.a = true;
  }
  
  public void processFrameData(final byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
    {
      TLog.w("%s processFrameNV21Data need data.", new Object[] { "SelesYuvDataReceiver" });
      return;
    }
    if (this.d) {
      return;
    }
    this.d = true;
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesYuvDataReceiver.a(SelesYuvDataReceiver.this);
        if (SelesYuvDataReceiver.this.mOutputFramebuffer == null)
        {
          TLog.e("%s newFrameReady need setInputSize[%s] or setInputRotation[%s] first.", new Object[] { "SelesYuvDataReceiver", SelesYuvDataReceiver.this.mInputTextureSize, SelesYuvDataReceiver.b(SelesYuvDataReceiver.this) });
          return;
        }
        SelesYuvDataReceiver.this.colorConvert(paramArrayOfByte, SelesYuvDataReceiver.this.mInputTextureSize.width, SelesYuvDataReceiver.this.mInputTextureSize.height, SelesYuvDataReceiver.c(SelesYuvDataReceiver.this).array());
        SelesYuvDataReceiver.this.mOutputFramebuffer.freshTextureRgba(SelesYuvDataReceiver.c(SelesYuvDataReceiver.this));
      }
    });
  }
  
  private void b()
  {
    if (!this.a) {
      return;
    }
    this.c = IntBuffer.allocate(this.mInputTextureSize.width * this.mInputTextureSize.height);
    a();
    this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.TEXTURE, this.mInputTextureSize);
    this.mOutputFramebuffer.bindTextureRgbaHolder(false);
  }
  
  protected void colorConvert(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    ColorSpaceConvert.nv21ToRgba(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfInt);
  }
  
  public void newFrameReady(long paramLong)
  {
    runPendingOnDrawTasks();
    a(paramLong);
    this.d = false;
  }
  
  private void a(long paramLong)
  {
    this.e.clear();
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
          this.e.put(localObject, Integer.valueOf(j));
          setInputFramebufferForTarget((SelesContext.SelesInput)localObject, j);
          ((SelesContext.SelesInput)localObject).setInputRotation(this.b, j);
          ((SelesContext.SelesInput)localObject).setInputSize(this.mInputTextureSize, j);
        }
      }
    }
    localIterator = this.e.entrySet().iterator();
    while (localIterator.hasNext())
    {
      localObject = (Map.Entry)localIterator.next();
      ((SelesContext.SelesInput)((Map.Entry)localObject).getKey()).newFrameReady(paramLong, ((Integer)((Map.Entry)localObject).getValue()).intValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesYuvDataReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */