package org.lasque.tusdk.core.utils.image;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;

public final class GifRenderingExecutor
  extends ScheduledThreadPoolExecutor
{
  private static volatile GifRenderingExecutor a = null;
  
  private GifRenderingExecutor()
  {
    super(1, new ThreadPoolExecutor.DiscardPolicy());
  }
  
  public static GifRenderingExecutor getInstance()
  {
    if (a == null) {
      synchronized (GifRenderingExecutor.class)
      {
        if (a == null) {
          a = new GifRenderingExecutor();
        }
      }
    }
    return a;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\image\GifRenderingExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */