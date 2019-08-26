package org.lasque.tusdk.core.api.engine;

import android.graphics.RectF;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;

public abstract interface TuSdkEngine
{
  public abstract void release();
  
  public abstract void setEngineOrientation(TuSdkEngineOrientation paramTuSdkEngineOrientation);
  
  public abstract void setEngineInputImage(TuSdkEngineInputImage paramTuSdkEngineInputImage);
  
  public abstract void setEngineProcessor(TuSdkEngineProcessor paramTuSdkEngineProcessor);
  
  public abstract void setEngineOutputImage(TuSdkEngineOutputImage paramTuSdkEngineOutputImage);
  
  public abstract void setInputTextureCoordinateBuilder(SelesVerticeCoordinateCorpBuilder paramSelesVerticeCoordinateCorpBuilder);
  
  public abstract void setInputPreCropRect(RectF paramRectF);
  
  public abstract boolean prepareInGlThread();
  
  public abstract void processFrame(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong);
  
  public abstract void processFrame(int paramInt1, int paramInt2, int paramInt3, long paramLong);
  
  public abstract void processFrame(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, long paramLong);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\engine\TuSdkEngine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */