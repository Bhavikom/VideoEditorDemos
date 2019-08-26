package org.lasque.tusdk.core.seles.output;

import android.opengl.GLES20;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class SelesTerminalFilter
  extends SelesFilter
{
  private boolean a = false;
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    if (paramTuSdkSize == null) {
      return;
    }
    super.setInputSize(paramTuSdkSize, paramInt);
    this.a = (!paramTuSdkSize.equals(this.mInputTextureSize));
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
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
    checkGLError(getClass().getSimpleName() + " setActiveShaderProgram");
    if ((this.mOutputFramebuffer == null) || (this.a))
    {
      a();
      TuSdkSize localTuSdkSize = sizeOfFBO();
      SelesFramebufferCache localSelesFramebufferCache = SelesContext.sharedFramebufferCache();
      if (localSelesFramebufferCache == null) {
        return;
      }
      this.mOutputFramebuffer = localSelesFramebufferCache.fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, localTuSdkSize, getOutputTextureOptions());
      this.mOutputFramebuffer.disableReferenceCounting();
    }
    this.mOutputFramebuffer.activateFramebuffer();
    checkGLError(getClass().getSimpleName() + " activateFramebuffer");
    if (this.mUsingNextFrameForImageCapture) {
      this.mOutputFramebuffer.lock();
    }
    setUniformsForProgramAtIndex(0);
    GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
    GLES20.glClear(16384);
    inputFramebufferBindTexture();
    checkGLError(getClass().getSimpleName() + " bindFramebuffer");
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, paramFloatBuffer1);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, paramFloatBuffer2);
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glFinish();
    inputFramebufferUnlock();
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    cacaptureImageBuffer();
  }
  
  protected void informTargetsAboutNewFrame(long paramLong) {}
  
  protected void onDestroy()
  {
    a();
  }
  
  private void a()
  {
    this.a = false;
    if (this.mOutputFramebuffer == null) {
      return;
    }
    this.mOutputFramebuffer.enableReferenceCounting();
    SelesContext.recycleFramebuffer(this.mOutputFramebuffer);
    this.mOutputFramebuffer = null;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\output\SelesTerminalFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */