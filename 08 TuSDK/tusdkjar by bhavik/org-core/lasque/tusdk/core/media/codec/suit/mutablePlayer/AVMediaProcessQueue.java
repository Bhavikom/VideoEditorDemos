package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class AVMediaProcessQueue
{
  private Thread a = new Thread(this.d);
  private LinkedBlockingQueue<Runnable> b = new LinkedBlockingQueue(10);
  private Object c = new Object();
  private Runnable d = new Runnable()
  {
    public void run()
    {
      while (!AVMediaProcessQueue.a(AVMediaProcessQueue.this).isInterrupted()) {
        AVMediaProcessQueue.b(AVMediaProcessQueue.this);
      }
    }
  };
  
  public AVMediaProcessQueue()
  {
    this.a.start();
  }
  
  public void runAsynchronouslyOnProcessingQueue(Runnable paramRunnable)
  {
    try
    {
      this.b.put(paramRunnable);
    }
    catch (InterruptedException localInterruptedException)
    {
      localInterruptedException.printStackTrace();
    }
  }
  
  public void runSynchronouslyOnProcessingQueue(final Runnable paramRunnable)
  {
    final Semaphore localSemaphore = new Semaphore(0);
    runAsynchronouslyOnProcessingQueue(new Runnable()
    {
      public void run()
      {
        paramRunnable.run();
        localSemaphore.release();
      }
    });
    try
    {
      localSemaphore.acquire();
    }
    catch (InterruptedException localInterruptedException)
    {
      localInterruptedException.printStackTrace();
    }
  }
  
  public void quit()
  {
    synchronized (this.c)
    {
      this.c.notifyAll();
    }
    this.a.interrupt();
  }
  
  public void clearAll()
  {
    this.b.clear();
  }
  
  private void a()
  {
    Runnable localRunnable;
    while ((localRunnable = (Runnable)this.b.poll()) != null) {
      localRunnable.run();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVMediaProcessQueue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */