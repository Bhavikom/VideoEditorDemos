package org.lasque.tusdk.core.encoder.video;

import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;

public abstract interface TuSDKVideoDataEncoderInterface
{
  public abstract TuSDKVideoEncoderSetting getVideoEncoderSetting();
  
  public abstract void setVideoEncoderSetting(TuSDKVideoEncoderSetting paramTuSDKVideoEncoderSetting);
  
  public abstract void setDelegate(TuSDKVideoDataEncoderDelegate paramTuSDKVideoDataEncoderDelegate);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\encoder\video\TuSDKVideoDataEncoderInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */