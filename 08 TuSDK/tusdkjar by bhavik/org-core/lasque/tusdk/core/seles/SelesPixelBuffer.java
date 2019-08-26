package org.lasque.tusdk.core.seles;

import android.annotation.TargetApi;
import android.opengl.GLES30;
import java.nio.Buffer;
import org.lasque.tusdk.core.secret.TuSdkImageNative;
import org.lasque.tusdk.core.seles.egl.SelesEGLContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class SelesPixelBuffer
{
  private final int a = 128;
  private int[] b;
  private SelesEGLContext c;
  private int d = 0;
  private int e = 0;
  private TuSdkSize f;
  private int[] g;
  private int h = 0;
  private boolean i = false;
  
  public int[] getPixelbuffers()
  {
    return this.b;
  }
  
  public SelesEGLContext getEglContext()
  {
    return this.c;
  }
  
  public int length()
  {
    return this.d;
  }
  
  public TuSdkSize getSize()
  {
    return this.f;
  }
  
  public void flagDestory()
  {
    this.i = true;
  }
  
  public SelesPixelBuffer(TuSdkSize paramTuSdkSize, int paramInt)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize()) || (paramInt < 1)) {
      return;
    }
    this.f = TuSdkSize.create(paramTuSdkSize);
    this.c = new SelesEGLContext();
    this.e = (this.f.width * 4 + 127 & 0xFFFFFF80);
    this.d = (this.e * this.f.height);
    this.b = new int[paramInt];
    GLES30.glGenBuffers(paramInt, this.b, 0);
    for (int j = 0; j < this.b.length; j++)
    {
      GLES30.glBindBuffer(34962, this.b[j]);
      GLES30.glBufferData(34962, this.d, null, 35049);
    }
    GLES30.glBindBuffer(34962, 0);
  }
  
  public void bindPackIndex(int paramInt)
  {
    if ((this.b == null) || (paramInt >= this.b.length))
    {
      TLog.e("%s bindPackIndex faile[%d]: %s", new Object[] { "SelesPixelBuffer", Integer.valueOf(paramInt), this.b });
      return;
    }
    GLES30.glBindBuffer(35051, this.b[paramInt]);
  }
  
  public void disablePackBuffer()
  {
    GLES30.glBindBuffer(35051, 0);
  }
  
  public void preparePackBuffer()
  {
    if ((this.b == null) || (this.d < 128) || (this.i)) {
      return;
    }
    next();
    GLES30.glBindBuffer(35051, this.g[5]);
    TuSdkImageNative.glReadPixels(this.g[3], this.g[4]);
    disablePackBuffer();
  }
  
  public Buffer readPackBuffer()
  {
    if ((this.b == null) || (this.d < 128) || (this.i)) {
      return null;
    }
    if ((this.g == null) || (this.g[0] == 0)) {
      return null;
    }
    GLES30.glBindBuffer(35051, this.g[6]);
    Buffer localBuffer = GLES30.glMapBufferRange(35051, 0, this.d, 1);
    if (localBuffer == null)
    {
      TLog.w("%s readPackBuffer can not read data.", new Object[] { "SelesPixelBuffer" });
      return null;
    }
    GLES30.glUnmapBuffer(35051);
    disablePackBuffer();
    return localBuffer;
  }
  
  public void next()
  {
    if ((this.i) || (this.b == null)) {
      return;
    }
    if (this.g == null)
    {
      this.g = new int[7];
      this.g[0] = 0;
      this.g[1] = this.d;
      this.g[2] = this.e;
      this.g[3] = this.f.width;
      this.g[4] = this.f.height;
    }
    else
    {
      this.g[0] = 1;
    }
    this.h %= this.b.length;
    this.g[5] = this.b[this.h];
    this.g[6] = this.b[((this.h + 1) % this.b.length)];
    this.h += 1;
  }
  
  public int[] getBefferInfo()
  {
    return this.g;
  }
  
  public void destory()
  {
    if (this.i) {
      return;
    }
    this.i = true;
    SelesContext.recyclePixelbuffer(this);
  }
  
  protected void finalize()
  {
    destory();
    super.finalize();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\SelesPixelBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */