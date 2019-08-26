package org.lasque.tusdk.core.encoder.audio;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;

public abstract interface TuSDKAudioDataEncoderInterface
{
  public abstract boolean initEncoder(TuSDKAudioEncoderSetting paramTuSDKAudioEncoderSetting);
  
  public abstract void start();
  
  public abstract void stop();
  
  public abstract void queueAudio(byte[] paramArrayOfByte);
  
  public abstract void onAudioEncoderStarted(MediaFormat paramMediaFormat);
  
  public abstract void onAudioEncoderFrameDataAvailable(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void setDelegate(TuSDKAudioDataEncoderDelegate paramTuSDKAudioDataEncoderDelegate);
  
  public static abstract interface TuSDKAudioDataEncoderDelegate
  {
    public abstract void onAudioEncoderStarted(MediaFormat paramMediaFormat);
    
    public abstract void onAudioEncoderStoped();
    
    public abstract void onAudioEncoderFrameDataAvailable(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
    
    public abstract void onAudioEncoderCodecConfig(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\encoder\audio\TuSDKAudioDataEncoderInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */