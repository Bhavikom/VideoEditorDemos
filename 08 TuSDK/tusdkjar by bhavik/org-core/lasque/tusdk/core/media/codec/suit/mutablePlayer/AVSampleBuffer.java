package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.utils.TLog;

public class AVSampleBuffer
{
  public static final int AV_BUFFER_FLAG_NONE = -1;
  public static final int AV_BUFFER_FLAG_DATA = 0;
  public static final int AV_BUFFER_FLAG_FORMAT = 1;
  public static final int AV_BUFFER_FLAG_DECODE_ONLY = 2;
  private ByteBuffer a;
  private MediaCodec.BufferInfo b;
  private MediaFormat c;
  private int d = -1;
  private long e;
  
  AVSampleBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, MediaFormat paramMediaFormat)
  {
    this(paramByteBuffer, paramBufferInfo, paramMediaFormat, 0);
  }
  
  AVSampleBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, MediaFormat paramMediaFormat, int paramInt)
  {
    this.a = paramByteBuffer;
    this.b = paramBufferInfo;
    this.c = paramMediaFormat;
    this.d = paramInt;
  }
  
  AVSampleBuffer(MediaFormat paramMediaFormat)
  {
    this.c = paramMediaFormat;
    this.d = 1;
  }
  
  public void setRenderTimeUs(long paramLong)
  {
    this.e = paramLong;
  }
  
  public long renderTimeUs()
  {
    return this.e;
  }
  
  public MediaFormat format()
  {
    return this.c;
  }
  
  public ByteBuffer buffer()
  {
    return this.a;
  }
  
  public MediaCodec.BufferInfo info()
  {
    return this.b;
  }
  
  public boolean isKeyFrame()
  {
    if (this.b == null)
    {
      TLog.w("%s : isKeyFrame return false. because info is null.", new Object[] { this });
      return false;
    }
    return (this.b.flags & 0x1) != 0;
  }
  
  public boolean isDecodeOnly()
  {
    return this.d == 2;
  }
  
  public boolean isFormat()
  {
    return this.d == 1;
  }
  
  public boolean isData()
  {
    return this.d == 0;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVSampleBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */