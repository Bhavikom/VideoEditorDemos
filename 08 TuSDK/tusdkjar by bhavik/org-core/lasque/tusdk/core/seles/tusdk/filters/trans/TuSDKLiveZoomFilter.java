package org.lasque.tusdk.core.seles.tusdk.filters.trans;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKLiveZoomFilter
  extends SelesFilter
  implements SelesParameters.FilterParameterInterface
{
  private static int b = 6;
  private IntBuffer c;
  private FloatBuffer d;
  private FloatBuffer e;
  private float[] f = { -1.0F, -1.0F, 0.0F, 1.0F, 1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F };
  private float[] g = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
  private long h;
  private long i;
  private float j = m;
  private float k;
  private float l;
  int[] a = { 0, 1, 2, 0, 3, 2 };
  private static float m = 1.0E9F;
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.i = 0L;
    this.h = 0L;
    this.k = 0.15F;
    this.l = 0.0F;
    this.c = ByteBuffer.allocateDirect(this.a.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer().put(this.a);
    this.c.position(0);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    runPendingOnDrawTasks();
    if (isPreventRendering())
    {
      inputFramebufferUnlock();
      return;
    }
    SelesContext.setActiveShaderProgram(this.mFilterProgram);
    TuSdkSize localTuSdkSize = sizeOfFBO();
    this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, localTuSdkSize, getOutputTextureOptions());
    this.mOutputFramebuffer.activateFramebuffer();
    if (this.mUsingNextFrameForImageCapture) {
      this.mOutputFramebuffer.lock();
    }
    a();
    setUniformsForProgramAtIndex(0);
    GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
    GLES20.glClear(16384);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
    GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, this.d);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, this.e);
    GLES20.glDrawElements(4, this.c.limit(), 5125, this.c);
    inputFramebufferUnlock();
    cacaptureImageBuffer();
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("duration", m, m, Float.MAX_VALUE);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    super.submitFilterArg(paramFilterArg);
    if (paramFilterArg.equalsKey("duration")) {
      a(paramFilterArg.getValue());
    }
  }
  
  private void a(float paramFloat)
  {
    this.j = Math.max(paramFloat, m);
  }
  
  protected void initializeAttributes()
  {
    super.initializeAttributes();
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    this.h = paramLong;
    super.informTargetsAboutNewFrame(paramLong);
  }
  
  private void a()
  {
    this.l = 0.0F;
    if (this.i == 0L)
    {
      this.i = this.h;
    }
    else if ((float)this.h > (float)this.i + this.j)
    {
      this.l = 0.0F;
    }
    else
    {
      this.l = (this.k * ((float)(this.h - this.i) / this.j));
      this.l = Math.abs(this.l);
      this.l = (this.l > this.k ? this.k : this.l);
    }
    float[] arrayOfFloat1 = { -1.0F, -1.0F, 1.0F, -1.0F, 1.0F, 1.0F, -1.0F, 1.0F };
    float[] arrayOfFloat2 = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
    arrayOfFloat2[0] += this.l;
    arrayOfFloat2[1] += this.l;
    arrayOfFloat2[2] -= this.l;
    arrayOfFloat2[3] += this.l;
    arrayOfFloat2[4] -= this.l;
    arrayOfFloat2[5] -= this.l;
    arrayOfFloat2[6] += this.l;
    arrayOfFloat2[7] -= this.l;
    this.d = buildBuffer(arrayOfFloat1);
    this.e = buildBuffer(arrayOfFloat2);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\trans\TuSDKLiveZoomFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */