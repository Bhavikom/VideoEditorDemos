package org.lasque.tusdk.api.engine;

import java.nio.IntBuffer;
import org.lasque.tusdk.core.api.engine.TuSdkEngineOrientation;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;

public abstract interface TuSdkImageEngine
{
  public abstract void release();
  
  public abstract void setFaceAligments(FaceAligment[] paramArrayOfFaceAligment);
  
  public abstract void setEngineRotation(TuSdkEngineOrientation paramTuSdkEngineOrientation);
  
  public abstract void setFilter(FilterWrap paramFilterWrap);
  
  public abstract void setListener(TuSdkPictureDataCompletedListener paramTuSdkPictureDataCompletedListener);
  
  public abstract void asyncProcessPictureData(byte[] paramArrayOfByte);
  
  public abstract void asyncProcessPictureData(byte[] paramArrayOfByte, InterfaceOrientation paramInterfaceOrientation);
  
  public static abstract interface TuSdkPictureDataCompletedListener
  {
    public abstract void onPictureDataCompleted(IntBuffer paramIntBuffer, TuSdkSize paramTuSdkSize);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\engine\TuSdkImageEngine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */