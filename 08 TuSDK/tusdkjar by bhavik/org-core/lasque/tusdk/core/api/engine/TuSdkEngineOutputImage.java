package org.lasque.tusdk.core.api.engine;

import java.util.List;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.type.ColorFormatType;

public abstract interface TuSdkEngineOutputImage
{
  public abstract void release();
  
  public abstract void willProcessFrame(long paramLong);
  
  public abstract void setEngineRotation(TuSdkEngineOrientation paramTuSdkEngineOrientation);
  
  public abstract int getTerminalTexture();
  
  public abstract void setYuvOutputImageFormat(ColorFormatType paramColorFormatType);
  
  public abstract List<SelesContext.SelesInput> getInputs();
  
  public abstract void setEnableOutputYUVData(boolean paramBoolean);
  
  public abstract void snatchFrame(byte[] paramArrayOfByte);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\engine\TuSdkEngineOutputImage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */