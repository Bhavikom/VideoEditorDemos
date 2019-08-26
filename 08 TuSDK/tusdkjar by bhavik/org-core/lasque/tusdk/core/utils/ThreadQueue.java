package org.lasque.tusdk.core.utils;

import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;

public class ThreadQueue
{
  private HandlerThread a;
  private Handler b;
  
  public ThreadQueue(String paramString)
  {
    this.a = new HandlerThread(paramString == null ? "ThreadQueue" : paramString);
    this.a.start();
    this.b = new Handler(this.a.getLooper());
  }
  
  public void release()
  {
    if (this.a == null) {
      return;
    }
    if (Build.VERSION.SDK_INT < 18) {
      this.a.quit();
    } else {
      this.a.quitSafely();
    }
    this.b = null;
    this.a = null;
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public void post(Runnable paramRunnable)
  {
    if ((this.b == null) || (paramRunnable == null)) {
      return;
    }
    this.b.post(paramRunnable);
  }
  
  public void postAtFrontOfQueue(Runnable paramRunnable)
  {
    if ((this.b == null) || (paramRunnable == null)) {
      return;
    }
    this.b.postAtFrontOfQueue(paramRunnable);
  }
  
  public void postDelayed(Runnable paramRunnable, long paramLong)
  {
    if ((this.b == null) || (paramRunnable == null)) {
      return;
    }
    this.b.postDelayed(paramRunnable, paramLong);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\ThreadQueue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */