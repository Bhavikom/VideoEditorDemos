package org.lasque.tusdk.core.api.engine;

import android.graphics.RectF;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.seles.sources.SelesOutput;
import org.lasque.tusdk.core.seles.sources.SelesTextureReceiver;
import org.lasque.tusdk.core.seles.sources.SelesYuvDataReceiver;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkEngineInputYUVDataImpl
  implements TuSdkEngineInputImage
{
  private TuSdkEngineOrientation a;
  private TuSdkEngineProcessor b;
  private SelesYuvDataReceiver c = new SelesYuvDataReceiver();
  private SelesTextureReceiver d = new SelesTextureReceiver();
  
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
    if (this.d == null)
    {
      TLog.w("%s bindEngineProcessor has released.", new Object[] { "TuSdkEngineInputYUVDataImpl" });
      return;
    }
    this.d.addTarget(this.b.getInput(), 0);
  }
  
  public void setTextureCoordinateBuilder(SelesVerticeCoordinateCorpBuilder paramSelesVerticeCoordinateCorpBuilder)
  {
    if (this.d == null) {
      return;
    }
    this.d.setTextureCoordinateBuilder(paramSelesVerticeCoordinateCorpBuilder);
  }
  
  public void setPreCropRect(RectF paramRectF)
  {
    if (this.d == null) {
      return;
    }
    this.d.setPreCropRect(paramRectF);
  }
  
  public SelesOutput getOutput()
  {
    return this.d;
  }
  
  public TuSdkEngineInputYUVDataImpl()
  {
    this.c.addTarget(this.d, 0);
  }
  
  public void release()
  {
    if (this.c != null)
    {
      this.c.destroy();
      this.c = null;
    }
    if (this.d != null)
    {
      this.d.destroy();
      this.d = null;
    }
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public void processFrame(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, long paramLong)
  {
    if (paramArrayOfByte == null)
    {
      TLog.w("%s processFrame need yuv data.", new Object[] { "TuSdkEngineInputYUVDataImpl" });
      return;
    }
    if (this.c == null)
    {
      TLog.w("%s processFrame has released.", new Object[] { "TuSdkEngineInputYUVDataImpl" });
      return;
    }
    if (this.b == null)
    {
      TLog.w("%s processFrame need bindEngineProcessor first.", new Object[] { "TuSdkEngineInputYUVDataImpl" });
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
    this.c.processFrameData(paramArrayOfByte);
    this.c.newFrameReady(paramLong);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\engine\TuSdkEngineInputYUVDataImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */