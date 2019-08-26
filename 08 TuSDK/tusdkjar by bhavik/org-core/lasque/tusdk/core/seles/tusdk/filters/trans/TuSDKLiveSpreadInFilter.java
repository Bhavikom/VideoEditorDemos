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
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKLiveSpreadInFilter
  extends SelesFilter
  implements SelesParameters.FilterParameterInterface
{
  private static int b = 6;
  private int c;
  private FloatBuffer d;
  private IntBuffer e;
  private int f;
  private int g;
  private SelesFramebuffer h;
  private FloatBuffer i;
  private FloatBuffer j;
  private float[] k = { -1.0F, -1.0F, 0.0F, 1.0F, 1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F };
  private float[] l = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
  private long m;
  private float n;
  private long o;
  private float p = q;
  int[] a = { 0, 1, 2, 0, 3, 2 };
  private static float q = 1.0E9F;
  
  public TuSDKLiveSpreadInFilter()
  {
    super("-strans", "-sspread");
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.n = 0.0F;
    this.o = 0L;
    this.m = 0L;
    this.f = this.mFilterProgram.uniformIndex("animationPercent");
    this.mFilterInputTextureUniform = this.mFilterProgram.uniformIndex("inputImageTexture");
    this.g = this.mFilterProgram.uniformIndex("inputImageTexture2");
    this.e = ByteBuffer.allocateDirect(this.a.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer().put(this.a);
    this.e.position(0);
    initializeAttributes();
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("duration", q, q, Float.MAX_VALUE);
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
    this.p = Math.max(paramFloat, q);
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
    setFloat(Math.abs(this.n), this.f, this.mFilterProgram);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
    GLES20.glUniform1i(this.g, 2);
    GLES20.glActiveTexture(33987);
    GLES20.glBindTexture(3553, this.h.getTexture());
    GLES20.glUniform1i(this.mFilterInputTextureUniform, 3);
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 4, 5126, false, 0, this.i);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, this.j);
    GLES20.glVertexAttribPointer(this.c, 2, 5126, false, 0, this.d);
    GLES20.glDrawElements(4, this.e.limit(), 5125, this.e);
    inputFramebufferUnlock();
    cacaptureImageBuffer();
  }
  
  protected void initializeAttributes()
  {
    super.initializeAttributes();
    this.mFilterProgram.addAttribute("inputTexture2Coordinate");
    this.c = this.mFilterProgram.attributeIndex("inputTexture2Coordinate");
    GLES20.glEnableVertexAttribArray(this.c);
  }
  
  public void setInputFramebuffer(SelesFramebuffer paramSelesFramebuffer, int paramInt)
  {
    if ((paramSelesFramebuffer != null) && ((this.h == null) || (!this.h.getSize().equals(paramSelesFramebuffer.getSize()))))
    {
      this.h = paramSelesFramebuffer;
      this.h.lock();
      if ((this.mFirstInputFramebuffer == null) || (this.mFirstInputFramebuffer.getSize().equals(paramSelesFramebuffer.getSize()))) {
        return;
      }
    }
    if (paramSelesFramebuffer != null)
    {
      this.mFirstInputFramebuffer = paramSelesFramebuffer;
      this.mFirstInputFramebuffer.lock();
    }
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    this.m = paramLong;
    super.informTargetsAboutNewFrame(paramLong);
  }
  
  protected void inputFramebufferUnlock()
  {
    super.inputFramebufferUnlock();
  }
  
  private void a()
  {
    if (this.o == 0L)
    {
      this.o = this.m;
      this.n = 0.0F;
    }
    else
    {
      this.n = ((float)Math.abs(this.m - this.o) / this.p);
      if (this.n > 1.0F) {
        this.n = 1.0F;
      } else if (this.n < 0.0F) {
        this.n = 0.0F;
      }
    }
    float[] arrayOfFloat1 = { -1.0F, -1.0F, 0.0F, 1.0F, 1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F };
    float[] arrayOfFloat2 = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
    float[] arrayOfFloat3 = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
    int tmp241_240 = 0;
    float[] tmp241_239 = arrayOfFloat3;
    tmp241_239[tmp241_240] = ((float)(tmp241_239[tmp241_240] + 0.5D * (1.0D - Math.abs(this.n))));
    float[] tmp264_261 = arrayOfFloat3;
    tmp264_261[6] = ((float)(tmp264_261[6] + 0.5D * (1.0D - Math.abs(this.n))));
    this.i = buildBuffer(arrayOfFloat1);
    this.j = buildBuffer(arrayOfFloat2);
    this.d = buildBuffer(arrayOfFloat3);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\trans\TuSDKLiveSpreadInFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */