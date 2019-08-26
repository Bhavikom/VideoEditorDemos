package org.lasque.tusdk.core.utils;

public class JVMUtils
{
  public static void runGC()
  {
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        Runtime.getRuntime().gc();
        try
        {
          Thread.sleep(100L);
        }
        catch (InterruptedException localInterruptedException)
        {
          TLog.e(localInterruptedException);
        }
        System.runFinalization();
      }
    });
  }
  
  public static float[] getMemoryInfo()
  {
    float f1 = (float)Runtime.getRuntime().maxMemory() * 1.0F / 1048576.0F;
    float f2 = (float)Runtime.getRuntime().totalMemory() * 1.0F / 1048576.0F;
    float f3 = (float)Runtime.getRuntime().freeMemory() * 1.0F / 1048576.0F;
    return new float[] { f1, f2, f3 };
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\JVMUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */