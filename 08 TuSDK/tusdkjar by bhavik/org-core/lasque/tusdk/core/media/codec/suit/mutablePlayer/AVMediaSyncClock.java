package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.os.SystemClock;
import java.util.concurrent.TimeUnit;

class AVMediaSyncClock
{
  private long a;
  private float b = 1.0F;
  private long c;
  private boolean d;
  
  public void start()
  {
    if (this.d) {
      return;
    }
    a();
    this.d = true;
  }
  
  public void stop()
  {
    this.a = 0L;
    this.d = false;
    this.c = 0L;
  }
  
  private void a()
  {
    this.a = 0L;
    this.c = SystemClock.elapsedRealtime();
  }
  
  public void lock(long paramLong1, long paramLong2)
  {
    if (!this.d) {
      return;
    }
    if (this.a == 0L) {
      this.a = paramLong1;
    }
    long l1 = ((float)(paramLong1 - this.a) * (1.0F / this.b));
    long l2 = usToMs(l1) + paramLong2;
    long l3 = this.c + l2;
    long l4 = l3 - SystemClock.elapsedRealtime();
    if (l4 > 0L) {
      try
      {
        TimeUnit.MILLISECONDS.sleep(l4);
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.printStackTrace();
      }
    }
  }
  
  public void setSpeed(float paramFloat)
  {
    a();
    this.b = paramFloat;
  }
  
  public float getSpeed()
  {
    return this.b;
  }
  
  public static long usToMs(long paramLong)
  {
    return (paramLong == -9223372036854775807L) || (paramLong == Long.MIN_VALUE) ? paramLong : paramLong / 1000L;
  }
  
  public static long msToUs(long paramLong)
  {
    return (paramLong == -9223372036854775807L) || (paramLong == Long.MIN_VALUE) ? paramLong : paramLong * 1000L;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVMediaSyncClock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */