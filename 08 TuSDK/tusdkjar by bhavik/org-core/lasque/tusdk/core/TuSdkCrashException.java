package org.lasque.tusdk.core;

import android.content.Context;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkCrashException
  extends Exception
  implements Thread.UncaughtExceptionHandler
{
  private Context a;
  private Thread.UncaughtExceptionHandler b;
  
  public static void bindExceptionHandler(Context paramContext)
  {
    Thread.setDefaultUncaughtExceptionHandler(new TuSdkCrashException(paramContext));
  }
  
  private TuSdkCrashException(Context paramContext)
  {
    this.a = paramContext;
    this.b = Thread.getDefaultUncaughtExceptionHandler();
  }
  
  protected void initException() {}
  
  public void uncaughtException(Thread paramThread, Throwable paramThrowable)
  {
    if ((!a(paramThrowable)) && (this.b != null)) {
      this.b.uncaughtException(paramThread, paramThrowable);
    }
  }
  
  private boolean a(Throwable paramThrowable)
  {
    if (paramThrowable == null) {
      return false;
    }
    TLog.enableLogging("TuSdk");
    TLog.e(paramThrowable);
    new Thread()
    {
      public void run()
      {
        Looper.prepare();
        Toast.makeText(TuSdkCrashException.a(TuSdkCrashException.this), "应用程序发生错误， 即将退出", 1).show();
        Looper.loop();
      }
    }.start();
    try
    {
      Thread.sleep(3000L);
    }
    catch (InterruptedException localInterruptedException)
    {
      TLog.e(localInterruptedException);
    }
    Process.killProcess(Process.myPid());
    System.exit(0);
    return true;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\TuSdkCrashException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */