package org.lasque.tusdk.core.utils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TuSdkSemaphore
  extends Semaphore
{
  private final long a = 0L;
  
  public TuSdkSemaphore(int paramInt)
  {
    super(paramInt);
  }
  
  public void signal()
  {
    release();
  }
  
  public boolean waitSignal(long paramLong)
  {
    boolean bool = false;
    try
    {
      bool = tryAcquire(paramLong, TimeUnit.MILLISECONDS);
    }
    catch (Exception localException) {}
    return bool;
  }
  
  public boolean waitSignal(int paramInt, long paramLong)
  {
    boolean bool = false;
    try
    {
      bool = tryAcquire(paramInt, paramLong, TimeUnit.MILLISECONDS);
    }
    catch (Exception localException) {}
    return bool;
  }
  
  public void log(String paramString)
  {
    TLog.d("%s %s: available: %d, queueLength: %d", new Object[] { paramString, "TuSdkSemaphore", Integer.valueOf(availablePermits()), Integer.valueOf(getQueueLength()) });
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\TuSdkSemaphore.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */