package org.lasque.tusdk.core.utils.monitor;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLES20;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import org.lasque.tusdk.core.utils.JVMUtils;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.TuSdkThreadExecutor;
import org.lasque.tusdk.core.utils.image.BitmapHelper;

public class TuSdkGLMonitor
{
  private static final String a = Environment.getExternalStorageDirectory().getPath() + "/log/TuSdk/GLMonitor/";
  private boolean b = false;
  private boolean c = false;
  private FileOutputStream d;
  private TuSdkThreadExecutor e;
  private volatile AtomicLong f = new AtomicLong();
  private int g = 60;
  private int h = 30;
  private String i;
  private Object j = new Object();
  
  public TuSdkGLMonitor(TuSdkThreadExecutor paramTuSdkThreadExecutor)
  {
    this.e = paramTuSdkThreadExecutor;
    if (this.e == null) {
      this.e = new TuSdkThreadExecutor();
    }
  }
  
  public void setEnableCheckGLError(boolean paramBoolean)
  {
    if (this.b == paramBoolean) {
      return;
    }
    this.b = paramBoolean;
    this.f.set(0L);
    if (this.d != null) {
      try
      {
        this.d.flush();
        this.d.close();
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    }
    if (paramBoolean) {
      try
      {
        if (this.i == null) {
          this.i = (a + b());
        }
        File localFile1 = new File(this.i);
        if (!localFile1.exists()) {
          localFile1.mkdirs();
        }
        File localFile2 = new File(this.i, b() + ".log");
        this.d = new FileOutputStream(localFile2);
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        localFileNotFoundException.printStackTrace();
      }
    }
  }
  
  public void setEnableCheckFrameImage(boolean paramBoolean)
  {
    this.c = paramBoolean;
    if (this.i == null) {
      this.i = (a + b());
    }
    File localFile = new File(this.i + "/IMG/");
    if (!localFile.exists()) {
      localFile.mkdirs();
    }
  }
  
  public void checkGL(String paramString)
  {
    if (!this.c) {
      return;
    }
    a(paramString);
    b(paramString);
  }
  
  private void a(String paramString)
  {
    int k = GLES20.glGetError();
    if (k == 0) {
      return;
    }
    try
    {
      String str = String.format("%s : %s  glError: 0x%s  \n", new Object[] { b(), paramString, Integer.toHexString(k) });
      c(str);
    }
    catch (Exception localException) {}
  }
  
  private void b(String paramString)
  {
    int k = GLES20.glCheckFramebufferStatus(36160);
    if (k == 36053) {
      return;
    }
    String str = String.format("%s : %s framebuffer error:[0x%s]", new Object[] { b(), paramString, Integer.toHexString(k) });
    c(str);
  }
  
  public void checkGLFrameImage(final String paramString, final int paramInt1, final int paramInt2)
  {
    if (!this.c) {
      return;
    }
    try
    {
      if (this.f.getAndIncrement() == Long.MAX_VALUE) {
        this.f.set(0L);
      }
      if (this.f.get() % this.g != 0L) {
        return;
      }
      final long l = this.f.get();
      synchronized (this.j)
      {
        checkGL("[checkFramImage] :" + paramString);
        TLog.dump("[checkFramImage]  capture filter image : [%s]  MemoryUse: %s", new Object[] { paramString, a() });
        final IntBuffer localIntBuffer = IntBuffer.allocate(paramInt1 * paramInt2);
        GLES20.glReadPixels(0, 0, paramInt1, paramInt2, 6408, 5121, localIntBuffer);
        boolean bool = c();
        this.e.exec(new Runnable()
        {
          public void run()
          {
            synchronized (TuSdkGLMonitor.a(TuSdkGLMonitor.this))
            {
              try
              {
                Bitmap localBitmap = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
                if (localBitmap == null)
                {
                  localObject1 = String.format(" %s :%s checkGLFrameImage return null \n", new Object[] { paramString, TuSdkGLMonitor.b(TuSdkGLMonitor.this) });
                  TuSdkGLMonitor.a(TuSdkGLMonitor.this, (String)localObject1);
                  return;
                }
                localBitmap.copyPixelsFromBuffer(localIntBuffer);
                Object localObject1 = new File(TuSdkGLMonitor.c(TuSdkGLMonitor.this) + "/IMG/");
                File localFile = new File((File)localObject1, paramString + "_" + l + "_" + TuSdkGLMonitor.b(TuSdkGLMonitor.this) + ".png");
                BitmapHelper.saveBitmap(localFile, localBitmap, TuSdkGLMonitor.d(TuSdkGLMonitor.this));
                localBitmap.recycle();
                if (this.f)
                {
                  String str = String.format(" %s :%s  File: %s \n", new Object[] { paramString, StringHelper.timeStampString(), localFile.getAbsolutePath() });
                  TuSdkGLMonitor.this.checkGL(paramString);
                  TuSdkGLMonitor.a(TuSdkGLMonitor.this, str);
                }
              }
              catch (Exception localException)
              {
                localException.printStackTrace();
              }
            }
          }
        });
      }
    }
    catch (Exception localException)
    {
      if (localException != null) {
        TLog.e(localException);
      }
    }
  }
  
  private static String a()
  {
    float[] arrayOfFloat = JVMUtils.getMemoryInfo();
    String str = "Memory : " + arrayOfFloat[0] + "_" + arrayOfFloat[1] + "_" + arrayOfFloat[2];
    return str;
  }
  
  private String b()
  {
    String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss-SSS").format(new Date());
    return str;
  }
  
  private boolean c()
  {
    int k = GLES20.glGetError();
    if (k != 0) {
      return true;
    }
    int m = GLES20.glCheckFramebufferStatus(36160);
    return m != 36053;
  }
  
  private void c(final String paramString)
  {
    if ((paramString == null) || (this.d == null)) {
      return;
    }
    this.e.exec(new Runnable()
    {
      public void run()
      {
        synchronized (TuSdkGLMonitor.a(TuSdkGLMonitor.this))
        {
          String str = paramString;
          try
          {
            TuSdkGLMonitor.e(TuSdkGLMonitor.this).write(str.getBytes());
            TuSdkGLMonitor.e(TuSdkGLMonitor.this).write("\n".getBytes());
          }
          catch (IOException localIOException)
          {
            localIOException.printStackTrace();
          }
        }
      }
    });
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\monitor\TuSdkGLMonitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */