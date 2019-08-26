package org.lasque.tusdk.core.decoder;

import org.lasque.tusdk.core.utils.TLog;

public class TuSDKVideoSpeedControl
  implements TuSDKMoviePacketDecoder.VideoSpeedControlInterface
{
  private long a;
  private boolean b = true;
  private long c;
  private long d;
  
  public void setEnable(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  public void setFrameRate(int paramInt)
  {
    if ((paramInt > 0) && (paramInt < 50)) {
      this.c = (1000000L / paramInt);
    } else {
      this.c = 0L;
    }
  }
  
  public void reset()
  {
    this.a = 0L;
    this.c = 0L;
  }
  
  public void preRender(long paramLong)
  {
    if ((!this.b) || (paramLong <= 0L)) {
      return;
    }
    if (this.a <= 0L)
    {
      this.d = (System.nanoTime() / 1000L);
      this.a = paramLong;
    }
    else
    {
      long l;
      if (this.c != 0L)
      {
        l = this.c;
      }
      else
      {
        l = Math.abs(paramLong - this.a);
        this.a = paramLong;
      }
      if (l <= 0L)
      {
        this.d = 0L;
        this.a = 0L;
      }
      else if (l > 10000000L)
      {
        TLog.w("Inter-frame pause was " + l / 1000000L + "sec, capping at 5 sec", new Object[0]);
        l = 5000000L;
      }
      try
      {
        Thread.sleep(l / 1000L);
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.printStackTrace();
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKVideoSpeedControl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */