package org.lasque.tusdk.core.sticker;

import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.microedition.khronos.egl.EGLContext;
import org.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class LiveStickerLoader
{
  private SelesEGL10Core a;
  private HandlerThread b = new HandlerThread("com.tusdk.asyncLiveStickerLoader");
  private Handler c;
  private ExecutorService d;
  
  public LiveStickerLoader(EGLContext paramEGLContext)
  {
    this.b.start();
    this.c = new Handler(this.b.getLooper());
    a(paramEGLContext);
  }
  
  private void a(final EGLContext paramEGLContext)
  {
    if (this.a != null) {
      return;
    }
    this.c.post(new Runnable()
    {
      public void run()
      {
        LiveStickerLoader.a(LiveStickerLoader.this, SelesEGL10Core.create(TuSdkSize.create(1, 1), paramEGLContext));
      }
    });
  }
  
  public void finalize()
  {
    super.finalize();
  }
  
  public void loadImage(Runnable paramRunnable)
  {
    if (this.d == null) {
      this.d = Executors.newFixedThreadPool(1);
    }
    this.d.execute(paramRunnable);
  }
  
  public void uploadTexture(Runnable paramRunnable)
  {
    if (this.c != null) {
      this.c.post(paramRunnable);
    }
  }
  
  public void destroy()
  {
    if (this.d != null)
    {
      this.d.shutdown();
      this.d = null;
    }
    if (this.b != null)
    {
      this.c.post(new Runnable()
      {
        private SelesEGL10Core b = LiveStickerLoader.a(LiveStickerLoader.this);
        
        public void run()
        {
          if (this.b != null)
          {
            this.b.destroy();
            this.b = null;
          }
        }
      });
      if (Build.VERSION.SDK_INT < 18) {
        this.b.quit();
      } else {
        this.b.quitSafely();
      }
      this.b = null;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\sticker\LiveStickerLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */