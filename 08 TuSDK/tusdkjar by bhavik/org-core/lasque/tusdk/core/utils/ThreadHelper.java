package org.lasque.tusdk.core.utils;

import android.os.Handler;
import android.os.Looper;

public class ThreadHelper
{
  public static final Handler handler = new Handler(Looper.getMainLooper());
  
  public static boolean isMainThread()
  {
    return Looper.myLooper() == Looper.getMainLooper();
  }
  
  public static Thread runThread(Runnable paramRunnable)
  {
    Thread localThread = new Thread(paramRunnable);
    localThread.start();
    return localThread;
  }
  
  public static void post(Runnable paramRunnable)
  {
    if (paramRunnable == null) {
      return;
    }
    handler.post(paramRunnable);
  }
  
  public static void postDelayed(Runnable paramRunnable, long paramLong)
  {
    if (paramRunnable == null) {
      return;
    }
    handler.postDelayed(paramRunnable, paramLong);
  }
  
  public static void cancel(Runnable paramRunnable)
  {
    if (paramRunnable == null) {
      return;
    }
    handler.removeCallbacks(paramRunnable);
  }
  
  public static void sleep(long paramLong)
  {
    try
    {
      Thread.sleep(paramLong);
    }
    catch (InterruptedException localInterruptedException) {}
  }
  
  public static boolean interrupted()
  {
    return Thread.interrupted();
  }
  
  public static boolean isInterrupted()
  {
    return Thread.currentThread().isInterrupted();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\ThreadHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */