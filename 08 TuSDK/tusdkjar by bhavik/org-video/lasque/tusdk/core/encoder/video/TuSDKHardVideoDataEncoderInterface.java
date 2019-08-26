package org.lasque.tusdk.core.encoder.video;

import android.view.Surface;
import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;

public abstract interface TuSDKHardVideoDataEncoderInterface
{
  public abstract boolean initCodec(TuSDKVideoEncoderSetting paramTuSDKVideoEncoderSetting);
  
  public abstract Surface getInputSurface();
  
  public abstract boolean requestKeyFrame();
  
  public abstract void flush();
  
  public abstract void drainEncoder(boolean paramBoolean);
  
  public abstract void release();
  
  public abstract void setDelegate(TuSDKVideoDataEncoderDelegate paramTuSDKVideoDataEncoderDelegate);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\encoder\video\TuSDKHardVideoDataEncoderInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */