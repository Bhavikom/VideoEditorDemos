package org.lasque.tusdk.core.api.engine;

import android.graphics.RectF;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.seles.sources.SelesOutput;

public abstract interface TuSdkEngineInputImage
{
  public abstract void release();
  
  public abstract void setEngineRotation(TuSdkEngineOrientation paramTuSdkEngineOrientation);
  
  public abstract void bindEngineProcessor(TuSdkEngineProcessor paramTuSdkEngineProcessor);
  
  public abstract void setTextureCoordinateBuilder(SelesVerticeCoordinateCorpBuilder paramSelesVerticeCoordinateCorpBuilder);
  
  public abstract void setPreCropRect(RectF paramRectF);
  
  public abstract void processFrame(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, long paramLong);
  
  public abstract SelesOutput getOutput();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\engine\TuSdkEngineInputImage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */