package org.lasque.tusdk.core.utils.hardware;

import android.media.MediaCodec.BufferInfo;
import org.lasque.tusdk.core.utils.TLog;

public class TuSDKFrameSpeedRateController
{
  private float a = 1.0F;
  private long b;
  private long c;
  private long d = 0L;
  private int e;
  private long f = -1L;
  private FrameSpeedRateCallback g;
  
  public void setFrameSpeedRateCallback(FrameSpeedRateCallback paramFrameSpeedRateCallback)
  {
    this.g = paramFrameSpeedRateCallback;
  }
  
  public void prepare()
  {
    if (this.c <= 0L) {
      return;
    }
    this.d = (nanoTimeUs() - this.c);
    this.b = this.c;
    this.e = 0;
    this.f = nanoTimeUs();
  }
  
  public void reset()
  {
    this.e = 0;
    this.b = 0L;
    this.d = 0L;
  }
  
  public void requestAdjustSpeed(MediaCodec.BufferInfo paramBufferInfo)
  {
    if ((this.a < 0.1F) || (this.a > 2.0F))
    {
      TLog.e("invalid speed rate : %f \n in speed rate ： %f \n Max speed rate ：%f ", new Object[] { Float.valueOf(this.a), Float.valueOf(2.0F), Float.valueOf(0.1F) });
      return;
    }
    a();
    if (this.a == 1.0F)
    {
      this.c = calculateCurrentPTS();
      paramBufferInfo.presentationTimeUs = this.c;
      this.g.onAvailableFrameTimeUs(this.c);
    }
    else if (this.a < 1.0F)
    {
      if (b()) {
        return;
      }
      this.c = calculateCurrentPTS();
      paramBufferInfo.presentationTimeUs = this.c;
      this.g.onAvailableFrameTimeUs(this.c);
    }
    else
    {
      int i = (int)(1.0F / (this.a - 1.0F));
      this.c = calculateCurrentPTS();
      paramBufferInfo.presentationTimeUs = this.c;
      this.g.onAvailableFrameTimeUs(this.c);
      if (this.e % i == 0)
      {
        this.c = calculateCurrentPTS();
        paramBufferInfo.presentationTimeUs = this.c;
        this.g.onAvailableFrameTimeUs(this.c);
      }
    }
  }
  
  public void setSpeedRate(float paramFloat)
  {
    this.a = paramFloat;
  }
  
  public long nanoTimeUs()
  {
    return System.nanoTime() / 1000L;
  }
  
  public long nanoTimePTS()
  {
    if (this.f > 0L)
    {
      this.d += nanoTimeUs() - this.f;
      this.f = -1L;
    }
    long l = nanoTimeUs() - this.d;
    return l;
  }
  
  public long calculateCurrentPTS()
  {
    long l = nanoTimePTS();
    if (this.b == 0L) {
      this.b = l;
    }
    return this.b + ((float)(l - this.b) * this.a);
  }
  
  private void a()
  {
    this.e += 1;
  }
  
  private boolean b()
  {
    if (this.e <= 1) {
      return false;
    }
    int i = Math.round(1.0F / this.a);
    return this.e % i != 0;
  }
  
  public static abstract interface FrameSpeedRateCallback
  {
    public abstract void onAvailableFrameTimeUs(long paramLong);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\utils\hardware\TuSDKFrameSpeedRateController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */