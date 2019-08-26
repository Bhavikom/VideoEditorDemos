package org.lasque.tusdk.core.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TuSdkThreadExecutor
{
  private ExecutorService a;
  
  public TuSdkThreadExecutor()
  {
    this(Runtime.getRuntime().availableProcessors());
  }
  
  public TuSdkThreadExecutor(int paramInt)
  {
    this.a = Executors.newFixedThreadPool(paramInt);
  }
  
  public void exec(Runnable paramRunnable)
  {
    if (paramRunnable == null) {
      return;
    }
    this.a.execute(paramRunnable);
  }
  
  public void release()
  {
    if (this.a != null) {
      this.a.shutdownNow();
    }
    this.a = null;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\TuSdkThreadExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */