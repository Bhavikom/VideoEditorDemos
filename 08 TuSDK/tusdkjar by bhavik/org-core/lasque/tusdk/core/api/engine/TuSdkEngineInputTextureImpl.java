package org.lasque.tusdk.core.api.engine;

import android.graphics.RectF;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.seles.sources.SelesOutput;
import org.lasque.tusdk.core.seles.sources.SelesTextureReceiver;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkEngineInputTextureImpl
  implements TuSdkEngineInputImage
{
  private TuSdkEngineOrientation a;
  private TuSdkEngineProcessor b;
  private SelesTextureReceiver c = new SelesTextureReceiver();
  private SelesFramebuffer d;
  
  public void setEngineRotation(TuSdkEngineOrientation paramTuSdkEngineOrientation)
  {
    if (paramTuSdkEngineOrientation == null) {
      return;
    }
    this.a = paramTuSdkEngineOrientation;
  }
  
  public void bindEngineProcessor(TuSdkEngineProcessor paramTuSdkEngineProcessor)
  {
    if (paramTuSdkEngineProcessor == null) {
      return;
    }
    this.b = paramTuSdkEngineProcessor;
    if (this.c == null)
    {
      TLog.w("%s bindEngineProcessor has released.", new Object[] { "TuSdkEngineInputTextureImpl" });
      return;
    }
    this.c.addTarget(this.b.getInput(), 0);
  }
  
  public void setTextureCoordinateBuilder(SelesVerticeCoordinateCorpBuilder paramSelesVerticeCoordinateCorpBuilder)
  {
    if (this.c == null) {
      return;
    }
    this.c.setTextureCoordinateBuilder(paramSelesVerticeCoordinateCorpBuilder);
  }
  
  public void setPreCropRect(RectF paramRectF)
  {
    if (this.c == null) {
      return;
    }
    this.c.setPreCropRect(paramRectF);
  }
  
  public SelesOutput getOutput()
  {
    return this.c;
  }
  
  public void release()
  {
    if (this.c != null)
    {
      this.c.destroy();
      this.c = null;
    }
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public void processFrame(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, long paramLong)
  {
    if (this.c == null)
    {
      TLog.w("%s processFrame has released.", new Object[] { "TuSdkEngineInputTextureImpl" });
      return;
    }
    if (this.b == null)
    {
      TLog.w("%s processFrame need bindEngineProcessor first.", new Object[] { "TuSdkEngineInputTextureImpl" });
      return;
    }
    this.b.willProcessFrame(paramLong);
    if ((this.d == null) || (this.d.getTexture() != paramInt1)) {
      this.d = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.HOLDER, null, paramInt1);
    }
    this.c.setInputFramebuffer(this.d, 0);
    if (this.a != null)
    {
      this.c.setInputRotation(this.a.getInputRotation(), 0);
      this.c.setInputSize(this.a.getInputSize(), 0);
    }
    else
    {
      this.c.setInputSize(TuSdkSize.create(paramInt2, paramInt3), 0);
    }
    this.c.newFrameReadyInGLThread(paramLong);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\engine\TuSdkEngineInputTextureImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */