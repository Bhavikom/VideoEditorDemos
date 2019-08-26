package org.lasque.tusdk.core.media.codec.decoder;

import android.media.MediaCodec.BufferInfo;

public abstract interface TuSdkDecoderListener
{
  public abstract void onDecoderUpdated(MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void onDecoderCompleted(Exception paramException);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\decoder\TuSdkDecoderListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */