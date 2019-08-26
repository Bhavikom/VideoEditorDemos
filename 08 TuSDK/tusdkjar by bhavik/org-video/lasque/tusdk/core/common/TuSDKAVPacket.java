package org.lasque.tusdk.core.common;

import java.nio.ByteBuffer;

public class TuSDKAVPacket
{
  public static final int AV_VIDEO_TYPE = 1;
  public static final int AV_AUDIO_TYPE = 2;
  private ByteBuffer a;
  private long b;
  private int c;
  private int d;
  private int e;
  
  public TuSDKAVPacket(ByteBuffer paramByteBuffer, long paramLong, int paramInt)
  {
    setByteBuffer(paramByteBuffer);
    setSampleTimeUs(paramLong);
    setPacketType(paramInt);
  }
  
  public TuSDKAVPacket(int paramInt)
  {
    this.a = ByteBuffer.allocate(paramInt);
  }
  
  public void setByteBuffer(ByteBuffer paramByteBuffer)
  {
    this.a = paramByteBuffer;
  }
  
  public ByteBuffer getByteBuffer()
  {
    return this.a;
  }
  
  public void setSampleTimeUs(long paramLong)
  {
    this.b = paramLong;
  }
  
  public long getSampleTimeUs()
  {
    return this.b;
  }
  
  public void setChunkSize(int paramInt)
  {
    this.c = paramInt;
  }
  
  public int getChunkSize()
  {
    return this.c;
  }
  
  public void setPacketType(int paramInt)
  {
    this.d = paramInt;
  }
  
  public int getPacketType()
  {
    return this.d;
  }
  
  public int getFlags()
  {
    return this.e;
  }
  
  public void setFlags(int paramInt)
  {
    this.e = paramInt;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\common\TuSDKAVPacket.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */