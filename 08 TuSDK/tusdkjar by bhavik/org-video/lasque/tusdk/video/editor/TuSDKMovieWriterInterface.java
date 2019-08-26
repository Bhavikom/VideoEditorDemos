package org.lasque.tusdk.video.editor;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;

@TargetApi(18)
public abstract interface TuSDKMovieWriterInterface
{
  public abstract boolean start();
  
  public abstract boolean stop();
  
  public abstract long getDurationTime();
  
  public abstract int addVideoTrack(MediaFormat paramMediaFormat);
  
  public abstract int addAudioTrack(MediaFormat paramMediaFormat);
  
  public abstract void writeSampleData(int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void writeVideoSampleData(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void writeAudioSampleData(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void writeSampleData(ByteBufferData paramByteBufferData);
  
  public abstract void setOrientationHint(int paramInt);
  
  public static enum MovieWriterOutputFormat
  {
    int a;
    
    private MovieWriterOutputFormat(int paramInt)
    {
      this.a = paramInt;
    }
    
    public int getOutputFormat()
    {
      return this.a;
    }
  }
  
  public static class ByteBufferData
  {
    public int trackIndex;
    public ByteBuffer buffer;
    public MediaCodec.BufferInfo bufferInfo;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSDKMovieWriterInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */