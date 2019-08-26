package org.lasque.tusdk.video.editor;

public class TuSdkTimeRange
{
  public static final long BASE_TIME_US = 1000000L;
  private long a = 0L;
  private long b = 0L;
  
  public static TuSdkTimeRange makeRange(float paramFloat1, float paramFloat2)
  {
    TuSdkTimeRange localTuSdkTimeRange = new TuSdkTimeRange();
    localTuSdkTimeRange.setStartTimeUs((paramFloat1 * 1000000.0F));
    localTuSdkTimeRange.setEndTimeUs((paramFloat2 * 1000000.0F));
    return localTuSdkTimeRange;
  }
  
  public static TuSdkTimeRange makeTimeUsRange(long paramLong1, long paramLong2)
  {
    TuSdkTimeRange localTuSdkTimeRange = new TuSdkTimeRange();
    localTuSdkTimeRange.setStartTimeUs(paramLong1);
    localTuSdkTimeRange.setEndTimeUs(paramLong2);
    return localTuSdkTimeRange;
  }
  
  public boolean isValid()
  {
    return (this.a >= 0L) && (this.b > this.a);
  }
  
  public float duration()
  {
    if (!isValid()) {
      return 0.0F;
    }
    return (float)(this.b - this.a) / 1000000.0F;
  }
  
  public long durationTimeUS()
  {
    if (!isValid()) {
      return 0L;
    }
    return this.b - this.a;
  }
  
  public long getStartTimeUS()
  {
    return this.a;
  }
  
  public void setStartTimeUs(long paramLong)
  {
    this.a = paramLong;
  }
  
  public long getEndTimeUS()
  {
    return this.b;
  }
  
  public void setEndTimeUs(long paramLong)
  {
    this.b = paramLong;
  }
  
  public float getStartTime()
  {
    return (float)getStartTimeUS() / 1000000.0F;
  }
  
  public void setStartTime(float paramFloat)
  {
    setStartTimeUs((paramFloat * 1000000.0F));
  }
  
  public float getEndTime()
  {
    return (float)getEndTimeUS() / 1000000.0F;
  }
  
  public void setEndTime(float paramFloat)
  {
    setEndTimeUs((paramFloat * 1000000.0F));
  }
  
  public boolean contains(TuSdkTimeRange paramTuSdkTimeRange)
  {
    if ((paramTuSdkTimeRange == null) || (!paramTuSdkTimeRange.isValid()) || (!isValid())) {
      return false;
    }
    return (paramTuSdkTimeRange.a >= this.a) && (paramTuSdkTimeRange.a < this.b) && (paramTuSdkTimeRange.b <= this.b);
  }
  
  public boolean contains(long paramLong)
  {
    if (!isValid()) {
      return false;
    }
    return (this.a <= paramLong) && (this.b >= paramLong);
  }
  
  public TuSdkTimeRange convertTo(TuSdkTimeRange paramTuSdkTimeRange)
  {
    if ((paramTuSdkTimeRange == null) || (!paramTuSdkTimeRange.isValid()) || (!isValid())) {
      return this;
    }
    return makeTimeUsRange(paramTuSdkTimeRange.a + this.a, paramTuSdkTimeRange.b + this.b);
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if (!(paramObject instanceof TuSdkTimeRange)) {
      return false;
    }
    if (paramObject == this) {
      return true;
    }
    TuSdkTimeRange localTuSdkTimeRange = (TuSdkTimeRange)paramObject;
    return (localTuSdkTimeRange.a == this.a) && (localTuSdkTimeRange.b == this.b);
  }
  
  public String toString()
  {
    return "Range startTimeUs = " + this.a + " endTimeUs = " + this.b + "  durationTimeUS = " + durationTimeUS();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkTimeRange.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */