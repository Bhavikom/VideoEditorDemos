package org.lasque.tusdk.core.seles.extend;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.os.Handler;
import org.lasque.tusdk.core.utils.TLog;

public class SelesSurfaceTexture
  extends SurfaceTexture
{
  private long a = -1L;
  private boolean b = false;
  private SurfaceTexture.OnFrameAvailableListener c;
  private int d = 0;
  private Object e = new Object();
  private SurfaceTexture.OnFrameAvailableListener f = new SurfaceTexture.OnFrameAvailableListener()
  {
    public void onFrameAvailable(SurfaceTexture paramAnonymousSurfaceTexture)
    {
      SelesSurfaceTexture.a(SelesSurfaceTexture.this);
      if (SelesSurfaceTexture.b(SelesSurfaceTexture.this) != null) {
        SelesSurfaceTexture.b(SelesSurfaceTexture.this).onFrameAvailable(SelesSurfaceTexture.this);
      }
    }
  };
  
  public long getDefindTimestamp()
  {
    return this.a;
  }
  
  public void setDefindTimestamp(long paramLong)
  {
    this.a = paramLong;
    this.b = true;
  }
  
  public boolean hasNewFrameNeedUpdate()
  {
    int i;
    synchronized (this.e)
    {
      i = this.d;
    }
    return i > 0;
  }
  
  public SelesSurfaceTexture(int paramInt)
  {
    super(paramInt);
  }
  
  @TargetApi(21)
  public SelesSurfaceTexture(int paramInt, boolean paramBoolean)
  {
    super(paramInt, paramBoolean);
  }
  
  @TargetApi(26)
  public SelesSurfaceTexture(boolean paramBoolean)
  {
    super(paramBoolean);
  }
  
  public long getTimestamp()
  {
    if (this.b) {
      return getDefindTimestamp();
    }
    return super.getTimestamp();
  }
  
  public void updateTexImage()
  {
    if (!hasNewFrameNeedUpdate()) {
      return;
    }
    b();
    try
    {
      super.updateTexImage();
    }
    catch (Exception localException) {}
  }
  
  public void forceUpdateTexImage()
  {
    if (hasNewFrameNeedUpdate()) {
      b();
    }
    try
    {
      super.updateTexImage();
    }
    catch (Exception localException) {}
  }
  
  private void a()
  {
    synchronized (this.e)
    {
      this.d += 1;
    }
  }
  
  private void b()
  {
    synchronized (this.e)
    {
      this.d -= 1;
    }
  }
  
  public void setOnFrameAvailableListener(SurfaceTexture.OnFrameAvailableListener paramOnFrameAvailableListener, Handler paramHandler)
  {
    this.c = paramOnFrameAvailableListener;
    SurfaceTexture.OnFrameAvailableListener localOnFrameAvailableListener = null;
    if (this.c != null) {
      localOnFrameAvailableListener = this.f;
    }
    super.setOnFrameAvailableListener(localOnFrameAvailableListener, paramHandler);
  }
  
  public void release()
  {
    try
    {
      super.release();
    }
    catch (Exception localException)
    {
      TLog.e("%s release error.", new Object[] { "SelesSurfaceTexture" });
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\extend\SelesSurfaceTexture.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */