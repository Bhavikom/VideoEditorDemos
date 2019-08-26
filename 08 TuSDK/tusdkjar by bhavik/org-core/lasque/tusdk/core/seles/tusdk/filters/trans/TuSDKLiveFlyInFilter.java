package org.lasque.tusdk.core.seles.tusdk.filters.trans;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
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
import org.lasque.tusdk.core.struct.TuSdkSizeF;

public class TuSDKLiveFlyInFilter
  extends SelesFilter
  implements SelesParameters.FilterParameterInterface
{
  private static int b = 6;
  private int c;
  private IntBuffer d;
  private int e;
  private SelesFramebuffer f;
  private FloatBuffer g = buildBuffer(this.j);
  private FloatBuffer h = buildBuffer(this.k);
  private FloatBuffer i;
  private float[] j = { -1.0F, -1.0F, 0.0F, 1.0F, 1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F };
  private float[] k = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
  private long l;
  private float m;
  private long n;
  private float o = p;
  int[] a = { 0, 1, 2, 0, 3, 2 };
  private static float p = 1.0E9F;
  
  public TuSDKLiveFlyInFilter()
  {
    super("-svflyin", "-sflyin");
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.m = 0.0F;
    this.n = 0L;
    this.l = 0L;
    this.mFilterInputTextureUniform = this.mFilterProgram.uniformIndex("inputImageTexture");
    this.e = this.mFilterProgram.uniformIndex("inputImageTexture2");
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
    GLES20.glEnableVertexAttribArray(this.c);
    GLES20.glVertexAttribPointer(this.c, 1, 5126, false, 0, this.i);
    GLES20.glDrawElements(4, this.d.limit(), 5125, this.d);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, 0);
    GLES20.glActiveTexture(33987);
    GLES20.glBindTexture(3553, 0);
    GLES20.glDisableVertexAttribArray(this.c);
    inputFramebufferUnlock();
    cacaptureImageBuffer();
  }
  
  protected void initializeAttributes()
  {
    super.initializeAttributes();
    this.c = this.mFilterProgram.attributeIndex("texIndex");
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
      this.m = ((float)(this.l - this.n) / this.o);
      this.m = Math.abs(this.m);
      if (this.m > 1.0F) {
        this.m = 1.0F;
      }
      if (this.m < 0.0F) {
        this.m = 0.0F;
      }
    }
    float[] arrayOfFloat1 = { 0.0F, 0.0F, 0.0F, 0.0F };
    float[] arrayOfFloat2 = { -1.0F, -1.0F, 0.0F, 1.0F, 1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F };
    float[] arrayOfFloat3 = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
    this.g = buildBuffer(arrayOfFloat2);
    this.h = buildBuffer(arrayOfFloat3);
    this.i = buildBuffer(arrayOfFloat1);
    float[] arrayOfFloat4 = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
    int[] arrayOfInt = (int[])this.a.clone();
    float[] arrayOfFloat5 = (float[])arrayOfFloat1.clone();
    PointF localPointF = new PointF(1.0F, -1.0F);
    localPointF.x -= Math.abs(this.m);
    localPointF.y += Math.abs(this.m);
    TuSdkSizeF localTuSdkSizeF = new TuSdkSizeF(0.25F, 0.25F);
    localTuSdkSizeF.width += (1.0F - localTuSdkSizeF.width) * Math.abs(this.m);
    localTuSdkSizeF.height += (1.0F - localTuSdkSizeF.height) * Math.abs(this.m);
    float f1 = Math.abs(this.m) * 360.0F;
    float[] arrayOfFloat6 = new float[16];
    float[] arrayOfFloat7 = new float[16];
    Matrix.setIdentityM(arrayOfFloat7, 0);
    Matrix.translateM(arrayOfFloat7, 0, localPointF.x, localPointF.y, 0.0F);
    Matrix.scaleM(arrayOfFloat7, 0, localTuSdkSizeF.width, localTuSdkSizeF.height, 1.0F);
    Matrix.rotateM(arrayOfFloat7, 0, f1, 0.0F, 0.0F, 1.0F);
    Matrix.multiplyMM(arrayOfFloat6, 0, arrayOfFloat7, 0, arrayOfFloat2, 0);
    for (int i1 = 0; i1 < arrayOfFloat5.length; i1++) {
      arrayOfFloat5[i1] = 1.0F;
    }
    for (i1 = 0; i1 < arrayOfInt.length; i1++) {
      arrayOfInt[i1] += 4;
    }
    this.g = buildBuffer(concat(arrayOfFloat2, arrayOfFloat6));
    this.h = buildBuffer(concat(arrayOfFloat3, arrayOfFloat3));
    this.i = buildBuffer(concat(arrayOfFloat1, arrayOfFloat5));
    this.d = ByteBuffer.allocateDirect((this.a.length + arrayOfInt.length) * 4).order(ByteOrder.nativeOrder()).asIntBuffer().put(concat(this.a, arrayOfInt));
    this.d.position(0);
  }
  
  public static float[] concat(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    float[] arrayOfFloat = Arrays.copyOf(paramArrayOfFloat1, paramArrayOfFloat1.length + paramArrayOfFloat2.length);
    System.arraycopy(paramArrayOfFloat2, 0, arrayOfFloat, paramArrayOfFloat1.length, paramArrayOfFloat2.length);
    return arrayOfFloat;
  }
  
  public static int[] concat(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int[] arrayOfInt = Arrays.copyOf(paramArrayOfInt1, paramArrayOfInt1.length + paramArrayOfInt2.length);
    System.arraycopy(paramArrayOfInt2, 0, arrayOfInt, paramArrayOfInt1.length, paramArrayOfInt2.length);
    return arrayOfInt;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\trans\TuSDKLiveFlyInFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */