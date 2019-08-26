package org.lasque.tusdk.core.media.codec;

public abstract interface TuSdkDecodecOperation
{
  public abstract void flush();
  
  public abstract boolean decodecInit(TuSdkMediaExtractor paramTuSdkMediaExtractor);
  
  public abstract boolean decodecProcessUntilEnd(TuSdkMediaExtractor paramTuSdkMediaExtractor);
  
  public abstract void decodecRelease();
  
  public abstract void decodecException(Exception paramException);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\TuSdkDecodecOperation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */