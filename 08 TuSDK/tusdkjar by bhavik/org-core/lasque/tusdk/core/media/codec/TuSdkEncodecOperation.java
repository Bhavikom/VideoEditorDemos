package org.lasque.tusdk.core.media.codec;

public abstract interface TuSdkEncodecOperation
{
  public abstract boolean isEncodecStarted();
  
  public abstract boolean encodecInit(TuSdkMediaMuxer paramTuSdkMediaMuxer);
  
  public abstract boolean encodecProcessUntilEnd(TuSdkMediaMuxer paramTuSdkMediaMuxer);
  
  public abstract void encodecRelease();
  
  public abstract void encodecException(Exception paramException);
  
  public abstract void flush();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\TuSdkEncodecOperation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */