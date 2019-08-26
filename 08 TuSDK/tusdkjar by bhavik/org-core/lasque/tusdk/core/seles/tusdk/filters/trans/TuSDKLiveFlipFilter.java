package org.lasque.tusdk.core.seles.tusdk.filters.trans;

import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKLiveFlipFilter
  extends SelesFilter
  implements SelesParameters.FilterParameterInterface
{
  private static int b = 6;
  private int c;
  private IntBuffer d;
  private int e;
  private SelesFramebuffer f;
  private FloatBuffer g;
  private FloatBuffer h;
  private FloatBuffer i;
  private float[] j = { -1.0F, -1.0F, 0.0F, 1.0F, 1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F };
  private float[] k = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
  private long l;
  private float m;
  private long n;
  private float o = p;
  int[] a = { 0, 1, 2, 0, 3, 2 };
  private static float p = 1.0E9F;
  
  public TuSDKLiveFlipFilter()
  {
    super("-svflyin", "-sflip");
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.m = 0.0F;
    this.n = 0L;
    this.l = 0L;
    this.mFilterInputTextureUniform = this.mFilterProgram.uniformIndex("inputImageTexture");
    this.e = this.mFilterProgram.uniformIndex("inputImageTexture2");
    this.c = this.mFilterProgram.attributeIndex("texIndex");
    GLES20.glEnableVertexAttribArray(this.c);
    this.d = ByteBuffer.allocateDirect(this.a.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer().put(this.a);
    this.d.position(0);
    initializeAttributes();
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("duration", p, p, Float.MAX_VALUE);
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
    this.o = Math.max(paramFloat, p);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    runPendingOnDrawTasks();
    if (isPreventRendering())
    {
      inputFramebufferUnlock();
      this.f.unlock();
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
    GLES20.glUniform1i(this.e, 2);
    GLES20.glActiveTexture(33987);
    GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
    GLES20.glUniform1i(this.mFilterInputTextureUniform, 3);
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 4, 5126, false, 0, this.g);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, this.h);
    GLES20.glVertexAttribPointer(this.c, 1, 5126, false, 0, this.i);
    GLES20.glDrawElements(4, this.d.limit(), 5125, this.d);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, 0);
    GLES20.glActiveTexture(33987);
    GLES20.glBindTexture(3553, 0);
    inputFramebufferUnlock();
    cacaptureImageBuffer();
  }
  
  protected void initializeAttributes()
  {
    super.initializeAttributes();
  }
  
  public void setInputFramebuffer(SelesFramebuffer paramSelesFramebuffer, int paramInt)
  {
    if ((paramSelesFramebuffer != null) && (this.f == null))
    {
      this.f = paramSelesFramebuffer;
      this.f.lock();
      return;
    }
    if (paramSelesFramebuffer != null)
    {
      this.mFirstInputFramebuffer = paramSelesFramebuffer;
      this.mFirstInputFramebuffer.lock();
    }
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    this.l = paramLong;
    super.informTargetsAboutNewFrame(paramLong);
  }
  
  protected void inputFramebufferUnlock()
  {
    super.inputFramebufferUnlock();
  }
  
  private void a()
  {
    if (this.n == 0L)
    {
      this.n = this.l;
      this.m = 0.0F;
    }
    else
    {
      this.m = ((float)Math.abs(this.l - this.n) / this.o);
      if (this.m > 1.0F) {
        this.m = 1.0F;
      } else if (this.m < 0.0F) {
        this.m = 0.0F;
      }
    }
    float[] arrayOfFloat1 = { 0.0F, 0.0F, 0.0F, 0.0F };
    float[] arrayOfFloat2 = { -1.0F, -1.0F, 0.0F, 1.0F, 1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F };
    float[] arrayOfFloat3 = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
    float f1 = (float)(Math.sin(Math.abs(this.m) * 3.141592653589793D) * 0.10000000149011612D);
    arrayOfFloat2[3] += f1;
    arrayOfFloat2[7] -= f1;
    arrayOfFloat2[11] -= f1;
    arrayOfFloat2[15] += f1;
    float[] arrayOfFloat4 = new float[16];
    float[] arrayOfFloat5 = new float[16];
    Matrix.setIdentityM(arrayOfFloat5, 0);
    Matrix.rotateM(arrayOfFloat5, 0, Math.abs(this.m) * 180.0F, 0.0F, 1.0F, 0.0F);
    Matrix.multiplyMM(arrayOfFloat4, 0, arrayOfFloat5, 0, arrayOfFloat2, 0);
    if (Math.abs(this.m) > 0.5D)
    {
      arrayOfFloat3[0] = 1.0F;
      arrayOfFloat3[2] = 0.0F;
      arrayOfFloat3[4] = 0.0F;
      arrayOfFloat3[6] = 1.0F;
      for (int i1 = 0; i1 < arrayOfFloat1.length; i1++) {
        arrayOfFloat1[i1] = 1.0F;
      }
    }
    this.g = buildBuffer(arrayOfFloat4);
    this.h = buildBuffer(arrayOfFloat3);
    this.i = buildBuffer(arrayOfFloat1);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\trans\TuSDKLiveFlipFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */