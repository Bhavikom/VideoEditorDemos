package org.lasque.tusdk.core.seles.output;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.seles.filters.SelesCropFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;

@Deprecated
public class SelesScreenShot
  extends SelesCropFilter
{
  private SelesScreenShotDelegate a;
  private IntBuffer b;
  
  public void setDelegate(SelesScreenShotDelegate paramSelesScreenShotDelegate)
  {
    this.a = paramSelesScreenShotDelegate;
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    TuSdkSize localTuSdkSize = this.mInputTextureSize;
    super.setInputSize(paramTuSdkSize, paramInt);
    if (!localTuSdkSize.equals(this.mInputTextureSize)) {
      this.b = null;
    }
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    if (this.a != null) {
      setEnabled(this.a.onFrameRendered(this));
    }
  }
  
  public IntBuffer renderedBuffer()
  {
    EGL10 localEGL10 = (EGL10)EGLContext.getEGL();
    EGLContext localEGLContext = localEGL10.eglGetCurrentContext();
    if ((localEGLContext == null) || (localEGLContext == EGL10.EGL_NO_CONTEXT)) {
      return null;
    }
    GL10 localGL10 = (GL10)localEGLContext.getGL();
    TuSdkSize localTuSdkSize = getOutputSize();
    if (this.b == null) {
      this.b = IntBuffer.allocate(localTuSdkSize.width * localTuSdkSize.height);
    }
    this.b.position(0);
    localGL10.glReadPixels(0, 0, localTuSdkSize.width, localTuSdkSize.height, 6408, 5121, this.b);
    return this.b;
  }
  
  public static abstract interface SelesScreenShotDelegate
  {
    public abstract boolean onFrameRendered(SelesScreenShot paramSelesScreenShot);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\output\SelesScreenShot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */