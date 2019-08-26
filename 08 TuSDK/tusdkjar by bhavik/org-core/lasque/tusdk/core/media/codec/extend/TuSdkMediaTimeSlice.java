package org.lasque.tusdk.core.media.codec.extend;

import java.io.Serializable;

public class TuSdkMediaTimeSlice
  implements Serializable
{
  public long startUs = -1L;
  public long endUs = -1L;
  public float speed = 1.0F;
  public int overlapIndex = -1;
  
  public TuSdkMediaTimeSlice() {}
  
  public TuSdkMediaTimeSlice(long paramLong1, long paramLong2)
  {
    this(paramLong1, paramLong2, 1.0F);
  }
  
  public TuSdkMediaTimeSlice(long paramLong1, long paramLong2, float paramFloat)
  {
    this.startUs = paramLong1;
    this.endUs = paramLong2;
    this.speed = paramFloat;
  }
  
  public TuSdkMediaTimeSlice(long paramLong1, long paramLong2, float paramFloat, int paramInt)
  {
    this.startUs = paramLong1;
    this.endUs = paramLong2;
    this.speed = paramFloat;
    this.overlapIndex = paramInt;
  }
  
  public TuSdkMediaTimeSlice clone()
  {
    TuSdkMediaTimeSlice localTuSdkMediaTimeSlice = new TuSdkMediaTimeSlice(this.startUs, this.endUs, this.speed, this.overlapIndex);
    return localTuSdkMediaTimeSlice;
  }
  
  public long reduce()
  {
    return this.endUs - this.startUs;
  }
  
  public boolean isReverse()
  {
    return this.startUs > this.endUs;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof TuSdkMediaTimeSlice))) {
      return false;
    }
    TuSdkMediaTimeSlice localTuSdkMediaTimeSlice = (TuSdkMediaTimeSlice)paramObject;
    return (localTuSdkMediaTimeSlice.startUs == this.startUs) && (localTuSdkMediaTimeSlice.endUs == this.endUs) && (localTuSdkMediaTimeSlice.speed == this.speed);
  }
  
  public int getOverlapIndex()
  {
    return this.overlapIndex;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("TuSdkMediaTimeSlice").append("{ \n");
    localStringBuffer.append("startUs: ").append(this.startUs).append(", \n");
    localStringBuffer.append("endUs: ").append(this.endUs).append(", \n");
    localStringBuffer.append("speed: ").append(this.speed).append(", \n");
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\extend\TuSdkMediaTimeSlice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */