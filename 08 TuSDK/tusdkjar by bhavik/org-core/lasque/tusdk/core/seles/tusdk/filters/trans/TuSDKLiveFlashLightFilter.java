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

public class TuSDKLiveFlashLightFilter
  extends SelesFilter
  implements SelesParameters.FilterParameterInterface
{
  private static int b = 6;
  private int c;
  private FloatBuffer d = buildBuffer(this.m);
  private IntBuffer e;
  private int f;
  private int g;
  private int h;
  private SelesFramebuffer i;
  private FloatBuffer j = buildBuffer(this.l);
  private FloatBuffer k = buildBuffer(this.m);
  private float[] l = { -1.0F, -1.0F, 0.0F, 1.0F, 1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F };
  private float[] m = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
  private long n;
  private float o;
  private long p;
  private float q;
  int[] a = { 0, 1, 2, 0, 3, 2 };
  private float r = 0.0F;
  private float s = 0.0F;
  private float t = 0.0F;
  private static float u = 1.0E9F;
  
  public TuSDKLiveFlashLightFilter()
  {
    super("-strans", "-sflash");
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.o = 0.0F;
    this.p = 0L;
    this.n = 0L;
    this.f = this.mFilterProgram.uniformIndex("animationPercent");
    this.mFilterInputTextureUniform = this.mFilterProgram.uniformIndex("inputImageTexture");
    this.g = this.mFilterProgram.uniformIndex("inputImageTexture2");
    this.h = this.mFilterProgram.uniformIndex("mixColor");
    this.e = ByteBuffer.allocateDirect(this.a.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer().put(this.a);
    this.e.position(0);
    initializeAttributes();
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("duration", u, u, Float.MAX_VALUE);
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
    this.q = Math.max(paramFloat, u);
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
    setFloat(Math.abs(this.o), this.f, this.mFilterProgram);
    setFloatVec3(new float[] { this.r, this.s, this.t }, "mixColor");
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, this.i.getTexture());
    GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
    GLES20.glActiveTexture(33987);
    GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
    GLES20.glUniform1i(this.g, 3);
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 4, 5126, false, 0, this.j);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, this.k);
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
    if ((paramSelesFramebuffer != null) && ((this.i == null) || (!this.i.getSize().equals(paramSelesFramebuffer.getSize()))))
    {
      this.i = paramSelesFramebuffer;
      this.i.lock();
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
    this.n = paramLong;
    super.informTargetsAboutNewFrame(paramLong);
  }
  
  protected void inputFramebufferUnlock()
  {
    super.inputFramebufferUnlock();
  }
  
  private void a()
  {
    if (this.p == 0L)
    {
      this.p = this.n;
      this.o = 0.0F;
    }
    else
    {
      this.o = ((float)Math.abs(this.n - this.p) / this.q);
      if (this.o > 1.0F) {
        this.o = 1.0F;
      } else if (this.o < 0.0F) {
        this.o = 0.0F;
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\trans\TuSDKLiveFlashLightFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */