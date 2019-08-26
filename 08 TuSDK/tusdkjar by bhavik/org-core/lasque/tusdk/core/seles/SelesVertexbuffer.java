package org.lasque.tusdk.core.seles;

import android.opengl.GLES20;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.egl.SelesEGLContext;

public class SelesVertexbuffer
{
  private int a = 0;
  private SelesEGLContext b;
  private FloatBuffer c;
  private boolean d = false;
  
  public int getVertexbuffer()
  {
    return this.a;
  }
  
  public SelesEGLContext getEglContext()
  {
    return this.b;
  }
  
  public int length()
  {
    if (this.c == null) {
      return 0;
    }
    return this.c.limit();
  }
  
  public void flagDestory()
  {
    this.d = true;
  }
  
  public SelesVertexbuffer(FloatBuffer paramFloatBuffer)
  {
    if (paramFloatBuffer == null) {
      return;
    }
    this.c = paramFloatBuffer;
    this.b = new SelesEGLContext();
    int[] arrayOfInt = new int[1];
    GLES20.glGenBuffers(1, arrayOfInt, 0);
    this.a = arrayOfInt[0];
    GLES20.glBindBuffer(34962, this.a);
    GLES20.glBufferData(34962, this.c.limit() * 4, paramFloatBuffer, 35048);
  }
  
  public void fresh(int paramInt1, int paramInt2)
  {
    fresh(paramInt1, paramInt2, this.c);
  }
  
  public void fresh(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer)
  {
    if (paramFloatBuffer == null) {
      paramFloatBuffer = this.c;
    }
    GLES20.glBindBuffer(34962, this.a);
    GLES20.glBufferSubData(34962, paramInt1 * 4, paramInt2 * 4, paramFloatBuffer);
  }
  
  public void activateVertexbuffer()
  {
    GLES20.glBindBuffer(34962, this.a);
  }
  
  public void disableVertexbuffer()
  {
    GLES20.glBindBuffer(34962, 0);
  }
  
  public void destory()
  {
    if (this.d) {
      return;
    }
    this.d = true;
    SelesContext.recycleVertexbuffer(this);
  }
  
  protected void finalize()
  {
    destory();
    super.finalize();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\SelesVertexbuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */