package org.lasque.tusdk.core.api.engine;

import org.lasque.tusdk.core.seles.SelesContext.SelesInput;

public abstract interface TuSdkEngineProcessor
{
  public abstract void release();
  
  public abstract void setEngineRotation(TuSdkEngineOrientation paramTuSdkEngineOrientation);
  
  public abstract void bindEngineOutput(TuSdkEngineOutputImage paramTuSdkEngineOutputImage);
  
  public abstract void willProcessFrame(long paramLong);
  
  public abstract SelesContext.SelesInput getInput();
  
  public static abstract interface TuSdkVideoProcesserFaceDetectionDelegate
  {
    public abstract void onFaceDetectionResult(TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionResultType paramTuSdkVideoProcesserFaceDetectionResultType, int paramInt);
  }
  
  public static enum TuSdkVideoProcesserFaceDetectionResultType
  {
    private TuSdkVideoProcesserFaceDetectionResultType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\engine\TuSdkEngineProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */