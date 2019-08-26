package org.lasque.tusdk.core.seles.filters;

import android.opengl.GLES20;
import java.nio.FloatBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.utils.TLog;

public class SelesTwoPassFilter
  extends SelesFilter
{
  protected SelesFramebuffer mSecondOutputFramebuffer;
  protected SelesGLProgram mSecondFilterProgram;
  protected int mSecondFilterPositionAttribute;
  protected int mSecondFilterTextureCoordinateAttribute;
  protected int mSecondFilterInputTextureUniform;
  protected int mSecondFilterInputTextureUniform2;
  protected final Map<Integer, Runnable> mSecondProgramUniformStateRestorationBlocks = new HashMap();
  private final String a;
  private final String b;
  protected final FloatBuffer mNoRotationTextureBuffer;
  
  public SelesTwoPassFilter(String paramString1, String paramString2)
  {
    this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", paramString1, "attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", paramString2);
  }
  
  public SelesTwoPassFilter(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    super(paramString1, paramString2);
    this.a = paramString3;
    this.b = paramString4;
    this.mNoRotationTextureBuffer = buildBuffer(noRotationTextureCoordinates);
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.mSecondFilterProgram = SelesContext.program(this.a, this.b);
    if (!this.mSecondFilterProgram.isInitialized())
    {
      initializeSecondaryAttributes();
      if (!this.mSecondFilterProgram.link())
      {
        TLog.i("Program link log: %s", new Object[] { this.mSecondFilterProgram.getProgramLog() });
        TLog.i("Fragment shader compile log: %s", new Object[] { this.mSecondFilterProgram.getFragmentShaderLog() });
        TLog.i("Vertex link log: %s", new Object[] { this.mSecondFilterProgram.getVertexShaderLog() });
        this.mSecondFilterProgram = null;
        TLog.e("Filter shader link failed: %s", new Object[] { getClass() });
        return;
      }
    }
    this.mSecondFilterPositionAttribute = this.mSecondFilterProgram.attributeIndex("position");
    this.mSecondFilterTextureCoordinateAttribute = this.mSecondFilterProgram.attributeIndex("inputTextureCoordinate");
    this.mSecondFilterInputTextureUniform = this.mSecondFilterProgram.uniformIndex("inputImageTexture");
    this.mSecondFilterInputTextureUniform2 = this.mSecondFilterProgram.uniformIndex("inputImageTexture2");
    SelesContext.setActiveShaderProgram(this.mSecondFilterProgram);
    GLES20.glEnableVertexAttribArray(this.mSecondFilterPositionAttribute);
    GLES20.glEnableVertexAttribArray(this.mSecondFilterTextureCoordinateAttribute);
  }
  
  protected void initializeSecondaryAttributes()
  {
    this.mSecondFilterProgram.addAttribute("position");
    this.mSecondFilterProgram.addAttribute("inputTextureCoordinate");
  }
  
  public SelesFramebuffer framebufferForOutput()
  {
    return this.mSecondOutputFramebuffer;
  }
  
  public void removeOutputFramebuffer()
  {
    this.mSecondOutputFramebuffer = null;
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    runPendingOnDrawTasks();
    if (isPreventRendering())
    {
      this.mFirstInputFramebuffer.unlock();
      return;
    }
    SelesContext.setActiveShaderProgram(this.mFilterProgram);
    SelesFramebufferCache localSelesFramebufferCache = SelesContext.sharedFramebufferCache();
    if (localSelesFramebufferCache == null) {
      return;
    }
    this.mOutputFramebuffer = localSelesFramebufferCache.fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, sizeOfFBO(), getOutputTextureOptions());
    this.mOutputFramebuffer.activateFramebuffer();
    setUniformsForProgramAtIndex(0);
    GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
    GLES20.glClear(16384);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
    GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, paramFloatBuffer1);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, paramFloatBuffer2);
    GLES20.glDrawArrays(5, 0, 4);
    this.mFirstInputFramebuffer.unlock();
    this.mFirstInputFramebuffer = null;
    this.mSecondOutputFramebuffer = localSelesFramebufferCache.fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, sizeOfFBO(), getOutputTextureOptions());
    this.mSecondOutputFramebuffer.activateFramebuffer();
    SelesContext.setActiveShaderProgram(this.mSecondFilterProgram);
    if (this.mUsingNextFrameForImageCapture) {
      this.mSecondOutputFramebuffer.lock();
    }
    setUniformsForProgramAtIndex(1);
    GLES20.glActiveTexture(33987);
    GLES20.glBindTexture(3553, this.mOutputFramebuffer.getTexture());
    GLES20.glVertexAttribPointer(this.mSecondFilterTextureCoordinateAttribute, 2, 5126, false, 0, this.mNoRotationTextureBuffer);
    GLES20.glUniform1i(this.mSecondFilterInputTextureUniform, 3);
    GLES20.glVertexAttribPointer(this.mSecondFilterPositionAttribute, 2, 5126, false, 0, paramFloatBuffer1);
    GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
    GLES20.glClear(16384);
    GLES20.glDrawArrays(5, 0, 4);
    this.mOutputFramebuffer.unlock();
    this.mOutputFramebuffer = null;
    cacaptureImageBuffer();
  }
  
  protected void setAndExecuteUniformStateCallbackAtIndex(int paramInt, SelesGLProgram paramSelesGLProgram, Runnable paramRunnable)
  {
    if (paramRunnable == null) {
      return;
    }
    if (paramSelesGLProgram == this.mFilterProgram) {
      this.mUniformStateRestorationBlocks.put(Integer.valueOf(paramInt), paramRunnable);
    } else {
      this.mSecondProgramUniformStateRestorationBlocks.put(Integer.valueOf(paramInt), paramRunnable);
    }
    paramRunnable.run();
  }
  
  public void setUniformsForProgramAtIndex(int paramInt)
  {
    Iterator localIterator;
    Runnable localRunnable;
    if (paramInt == 0)
    {
      localIterator = this.mUniformStateRestorationBlocks.values().iterator();
      while (localIterator.hasNext())
      {
        localRunnable = (Runnable)localIterator.next();
        localRunnable.run();
      }
    }
    else
    {
      localIterator = this.mSecondProgramUniformStateRestorationBlocks.values().iterator();
      while (localIterator.hasNext())
      {
        localRunnable = (Runnable)localIterator.next();
        localRunnable.run();
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\filters\SelesTwoPassFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */