package org.lasque.tusdk.core.api.engine;

import android.graphics.RectF;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.seles.sources.SelesOutput;
import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkEngineInputSurfaceImpl
  implements TuSdkEngineInputImage
{
  private TuSdkEngineOrientation a;
  private TuSdkEngineProcessor b;
  private _SelesSurfaceReceiver c = new _SelesSurfaceReceiver();
  
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
      TLog.w("%s bindEngineProcessor has released.", new Object[] { "TuSdkEngineInputSurfaceImpl" });
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
      TLog.w("%s processFrame has released.", new Object[] { "TuSdkEngineInputSurfaceImpl" });
      return;
    }
    if (this.b == null)
    {
      TLog.w("%s processFrame need bindEngineProcessor first.", new Object[] { "TuSdkEngineInputSurfaceImpl" });
      return;
    }
    this.b.willProcessFrame(paramLong);
    if (this.a != null)
    {
      this.c.setInputRotation(this.a.getInputRotation());
      this.c.setInputSize(this.a.getInputSize());
    }
    else
    {
      this.c.setInputSize(TuSdkSize.create(paramInt2, paramInt3));
    }
    this.c.setSurface(paramInt1);
    if (!this.c.isInited()) {
      this.c.initInGLThread();
    }
    this.c.newFrameReadyInGLThread(paramLong);
  }
  
  private class _SelesSurfaceReceiver
    extends SelesSurfaceReceiver
  {
    public _SelesSurfaceReceiver() {}
    
    protected void initSurfaceFBO() {}
    
    public void setSurface(int paramInt)
    {
      if ((this.mSurfaceFBO == null) || (this.mSurfaceFBO.getTexture() != paramInt)) {
        this.mSurfaceFBO = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.HOLDER, this.mInputTextureSize, paramInt);
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\engine\TuSdkEngineInputSurfaceImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */