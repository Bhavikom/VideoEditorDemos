package org.lasque.tusdk.core.utils.monitor;

import org.lasque.tusdk.core.utils.TuSdkThreadExecutor;

public class TuSdkMonitor
{
  private static TuSdkMonitor a = new TuSdkMonitor();
  private static TuSdkThreadExecutor b;
  private static TuSdkGLMonitor c;
  
  private TuSdkMonitor()
  {
    b = new TuSdkThreadExecutor();
    c = new TuSdkGLMonitor(b);
  }
  
  public static TuSdkGLMonitor glMonitor()
  {
    return c;
  }
  
  public static TuSdkMonitor setEnableCheckFrameImage(boolean paramBoolean)
  {
    c.setEnableCheckFrameImage(paramBoolean);
    return a;
  }
  
  public static TuSdkMonitor setEnableCheckGLError(boolean paramBoolean)
  {
    c.setEnableCheckGLError(paramBoolean);
    return a;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\monitor\TuSdkMonitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */