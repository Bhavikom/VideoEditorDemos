package org.lasque.tusdk.core.delegate;

import android.media.MediaCodec.BufferInfo;
import org.lasque.tusdk.core.decoder.TuSDKMediaDecoder.TuSDKMediaDecoderError;
import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;

public abstract interface TuSDKVideoSurfaceDecodeDelegate
{
  public abstract void onDecoderError(TuSDKMediaDecoder.TuSDKMediaDecoderError paramTuSDKMediaDecoderError);
  
  public abstract void onVideoInfoReady(TuSDKVideoInfo paramTuSDKVideoInfo);
  
  public abstract void onProgressChanged(long paramLong, float paramFloat);
  
  public abstract void onVideoDecoderNewFrameAvailable(int paramInt, MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void onDecoderComplete();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\delegate\TuSDKVideoSurfaceDecodeDelegate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */