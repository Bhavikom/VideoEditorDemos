package org.lasque.tusdk.core.media.codec.encoder;

import android.media.MediaCodec.BufferInfo;

public abstract interface TuSdkEncoderListener
{
  public abstract void onEncoderUpdated(MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void onEncoderCompleted(Exception paramException);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\encoder\TuSdkEncoderListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */