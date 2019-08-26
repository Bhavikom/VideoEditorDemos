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

public class TuSDKLivePullInFilter
  extends SelesFilter
  implements SelesParameters.FilterParameterInterface
{
  private static int b = 6;
  private int c;
  private FloatBuffer d;
  private IntBuffer e;
  private int f;
  private int g;
  private int h;
  private SelesFramebuffer i;
  private FloatBuffer j;
  private FloatBuffer k;
  private float[] l = { -1.0F, -1.0F, 0.0F, 1.0F, 1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F };
  private float[] m = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
  private long n;
  private float o;
  private long p;
  private float q = s;
  private float r;
  int[] a = { 0, 1, 2, 0, 3, 2 };
  private static float s = 1.0E9F;
  
  public TuSDKLivePullInFilter()
  {
    super("-strans", "-spullin");
  }
  
  public TuSDKLivePullInFilter(PullInDirection paramPullInDirection)
  {
    this();
    a(paramPullInDirection);
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.p = 0L;
    this.n = 0L;
    this.f = this.mFilterProgram.uniformIndex("animationPercent");
    this.mFilterInputTextureUniform = this.mFilterProgram.uniformIndex("inputImageTexture");
    this.g = this.mFilterProgram.uniformIndex("inputImageTexture2");
    this.h = this.mFilterProgram.uniformIndex("animationDirection");
    this.e = ByteBuffer.allocateDirect(this.a.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer().put(this.a);
    this.e.position(0);
    initializeAttributes();
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("duration", s, s, Float.MAX_VALUE);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    super.submitFilterArg(paramFilterArg);
    if (paramFilterArg.equalsKey("duration")) {
      a(paramFilterArg.getValue());
    }
  }
  
  private void a(PullInDirection paramPullInDirection)
  {
    this.r = paramPullInDirection.getDirection();
  }
  
  private void a(float paramFloat)
  {
    this.q = Math.max(paramFloat, s);
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
    setFloat(this.r, this.h, this.mFilterProgram);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, this.i.getTexture());
    if ((this.r == PullInDirection.TOP.getDirection()) || (this.r == PullInDirection.RIGHT.getDirection())) {
      GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
    } else {
      GLES20.glUniform1i(this.g, 2);
    }
    GLES20.glActiveTexture(33987);
    GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
    if ((this.r == PullInDirection.TOP.getDirection()) || (this.r == PullInDirection.RIGHT.getDirection())) {
      GLES20.glUniform1i(this.g, 3);
    } else {
      GLES20.glUniform1i(this.mFilterInputTextureUniform, 3);
    }
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 4, 5126, false, 0, this.j);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, true, 0, this.k);
    GLES20.glVertexAttribPointer(this.c, 2, 5126, true, 0, this.d);
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
      if ((this.r == PullInDirection.RIGHT.getDirection()) || (this.r == PullInDirection.TOP.getDirection())) {
        this.o = 0.0F;
      } else {
        this.o = 1.0F;
      }
    }
    else if ((this.r == PullInDirection.RIGHT.getDirection()) || (this.r == PullInDirection.TOP.getDirection()))
    {
      this.o = ((float)(this.n - this.p) / this.q);
      if (this.o > 1.0F) {
        this.o = 1.0F;
      }
    }
    else
    {
      this.o = ((this.q - (float)(this.n - this.p)) / this.q);
      if (this.o < 0.0F) {
        this.o = 0.0F;
      }
    }
    this.o = Math.abs(this.o);
    this.o = ((float)Math.sin(this.o * 1.5707963267948966D));
    float[] arrayOfFloat1 = { -1.0F, -1.0F, 0.0F, 1.0F, 1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F };
    float[] arrayOfFloat2 = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
    float[] arrayOfFloat3 = { 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
    if ((this.r == PullInDirection.RIGHT.getDirection()) || (this.r == PullInDirection.LEFT.getDirection()))
    {
      arrayOfFloat2[0] += this.o;
      arrayOfFloat2[2] += this.o;
      arrayOfFloat2[4] += this.o;
      arrayOfFloat2[6] += this.o;
      arrayOfFloat3[0] -= 1.0F - this.o;
      arrayOfFloat3[2] -= 1.0F - this.o;
      arrayOfFloat3[4] -= 1.0F - this.o;
      arrayOfFloat3[6] -= 1.0F - this.o;
    }
    else
    {
      arrayOfFloat2[1] += this.o;
      arrayOfFloat2[3] += this.o;
      arrayOfFloat2[5] += this.o;
      arrayOfFloat2[7] += this.o;
      arrayOfFloat3[1] -= 1.0F - this.o;
      arrayOfFloat3[3] -= 1.0F - this.o;
      arrayOfFloat3[5] -= 1.0F - this.o;
      arrayOfFloat3[7] -= 1.0F - this.o;
    }
    this.j = buildBuffer(arrayOfFloat1);
    this.k = buildBuffer(arrayOfFloat2);
    this.d = buildBuffer(arrayOfFloat3);
  }
  
  public static enum PullInDirection
  {
    public float mDirection;
    
    private PullInDirection(float paramFloat)
    {
      this.mDirection = paramFloat;
    }
    
    public float getDirection()
    {
      return this.mDirection;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\trans\TuSDKLivePullInFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */