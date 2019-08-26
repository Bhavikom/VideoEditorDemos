package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

public class AVTimeRange
{
  private long a;
  private long b;
  
  public long durationUs()
  {
    return this.a;
  }
  
  public long startUs()
  {
    return this.b;
  }
  
  public long endUs()
  {
    return this.b + this.a;
  }
  
  public String toString()
  {
    return "[ startUs : " + startUs() + "  endUs : " + endUs() + " ]";
  }
  
  public boolean containsTimeUs(long paramLong)
  {
    return (paramLong >= startUs()) && (paramLong <= endUs());
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof AVTimeRange)) {
      return false;
    }
    AVTimeRange localAVTimeRange = (AVTimeRange)paramObject;
    return (localAVTimeRange.b == this.b) && (localAVTimeRange.a == this.a);
  }
  
  public static AVTimeRange AVTimeRangeMake(long paramLong1, long paramLong2)
  {
    AVTimeRange localAVTimeRange = new AVTimeRange();
    localAVTimeRange.b = paramLong1;
    localAVTimeRange.a = paramLong2;
    return localAVTimeRange;
  }
  
  public static boolean AVTimeRangeEqual(AVTimeRange paramAVTimeRange1, AVTimeRange paramAVTimeRange2)
  {
    if ((paramAVTimeRange1 == null) && (paramAVTimeRange2 == null)) {
      return true;
    }
    if (paramAVTimeRange1 != null) {
      return paramAVTimeRange1.equals(paramAVTimeRange2);
    }
    return paramAVTimeRange2.equals(paramAVTimeRange1);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVTimeRange.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */