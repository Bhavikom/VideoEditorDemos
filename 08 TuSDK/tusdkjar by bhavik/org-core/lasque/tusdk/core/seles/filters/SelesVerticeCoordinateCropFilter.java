package org.lasque.tusdk.core.seles.filters;

import android.graphics.RectF;
import android.opengl.GLES20;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class SelesVerticeCoordinateCropFilter
  extends SelesFilter
{
  private SelesVerticeCoordinateCropBuilderImpl a = new SelesVerticeCoordinateCropBuilderImpl(false);
  private TuSdkSize b;
  private boolean c = false;
  
  public void setManualFBO(boolean paramBoolean)
  {
    this.c = paramBoolean;
  }
  
  public void setEnableClip(boolean paramBoolean)
  {
    this.a.setEnableClip(paramBoolean);
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize)
  {
    this.a.setOutputSize(paramTuSdkSize);
  }
  
  public void setCanvasRect(RectF paramRectF)
  {
    this.a.setCanvasRect(paramRectF);
  }
  
  public void setCropRect(RectF paramRectF)
  {
    this.a.setCropRect(paramRectF);
  }
  
  public void setUsingNextFrameForImageCapture(boolean paramBoolean)
  {
    this.mUsingNextFrameForImageCapture = paramBoolean;
  }
  
  public void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    super.setInputSize(paramTuSdkSize, paramInt);
    if (this.mOutputFramebuffer != null) {
      SelesContext.sharedFramebufferCache().recycleFramebuffer(this.mOutputFramebuffer);
    }
    this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE_AND_RENDER, paramTuSdkSize);
    this.mOutputFramebuffer.disableReferenceCounting();
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    super.newFrameReady(paramLong, paramInt);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    if ((this.a != null) && (this.a.calculate(this.mInputTextureSize, this.mInputRotation, paramFloatBuffer1, paramFloatBuffer2)))
    {
      this.b = this.a.outputSize();
    }
    else
    {
      paramFloatBuffer2.clear();
      paramFloatBuffer2.put(SelesFilter.textureCoordinates(this.mInputRotation)).position(0);
      this.b = this.mInputTextureSize;
    }
    runPendingOnDrawTasks();
    if (isPreventRendering())
    {
      inputFramebufferUnlock();
      return;
    }
    SelesContext.setActiveShaderProgram(this.mFilterProgram);
    TuSdkSize localTuSdkSize = sizeOfFBO();
    if (this.mOutputFramebuffer == null)
    {
      SelesFramebufferCache localSelesFramebufferCache = SelesContext.sharedFramebufferCache();
      if (localSelesFramebufferCache == null) {
        return;
      }
      this.mOutputFramebuffer = localSelesFramebufferCache.fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, localTuSdkSize, getOutputTextureOptions());
    }
    this.mOutputFramebuffer.activateFramebuffer();
    if (this.mUsingNextFrameForImageCapture) {
      this.mOutputFramebuffer.lock();
    }
    setUniformsForProgramAtIndex(0);
    GLES20.glClear(16640);
    inputFramebufferBindTexture();
    GLES20.glEnableVertexAttribArray(this.mFilterPositionAttribute);
    GLES20.glEnableVertexAttribArray(this.mFilterTextureCoordinateAttribute);
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, paramFloatBuffer1);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, paramFloatBuffer2);
    GLES20.glDrawArrays(5, 0, 4);
    inputFramebufferUnlock();
    cacaptureImageBuffer();
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\filters\SelesVerticeCoordinateCropFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */