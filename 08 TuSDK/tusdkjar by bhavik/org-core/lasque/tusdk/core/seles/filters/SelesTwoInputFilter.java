package org.lasque.tusdk.core.seles.filters;

import android.opengl.GLES20;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesTwoInputFilter
  extends SelesFilter
{
  protected SelesFramebuffer mSecondInputFramebuffer;
  protected int mFilterSecondTextureCoordinateAttribute;
  protected int mFilterInputTextureUniform2;
  protected ImageOrientation mInputRotation2 = ImageOrientation.Up;
  protected boolean mHasSetFirstTexture = false;
  protected boolean mHasReceivedFirstFrame = false;
  protected boolean mHasReceivedSecondFrame = false;
  protected boolean mFirstFrameWasVideo = false;
  protected boolean mSecondFrameWasVideo = false;
  protected boolean mFirstFrameCheckDisabled = false;
  protected boolean mSecondFrameCheckDisabled = false;
  private final FloatBuffer a = buildBuffer(noRotationTextureCoordinates);
  
  public SelesTwoInputFilter(String paramString)
  {
    this("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nattribute vec4 inputTextureCoordinate2;\n \nvarying vec2 textureCoordinate;\nvarying vec2 textureCoordinate2;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n    textureCoordinate2 = inputTextureCoordinate2.xy;\n}", paramString);
  }
  
  public SelesTwoInputFilter(String paramString1, String paramString2)
  {
    super(paramString1, paramString2);
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.mFilterSecondTextureCoordinateAttribute = this.mFilterProgram.attributeIndex("inputTextureCoordinate2");
    this.mFilterInputTextureUniform2 = this.mFilterProgram.uniformIndex("inputImageTexture2");
    GLES20.glEnableVertexAttribArray(this.mFilterSecondTextureCoordinateAttribute);
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  protected void initializeAttributes()
  {
    super.initializeAttributes();
    this.mFilterProgram.addAttribute("inputTextureCoordinate2");
  }
  
  public void disableFirstFrameCheck()
  {
    this.mFirstFrameCheckDisabled = true;
  }
  
  public void disableSecondFrameCheck()
  {
    this.mSecondFrameCheckDisabled = true;
  }
  
  protected void inputFramebufferUnlock()
  {
    super.inputFramebufferUnlock();
    if (this.mSecondInputFramebuffer != null) {
      this.mSecondInputFramebuffer.unlock();
    }
  }
  
  protected void inputFramebufferBindTexture()
  {
    super.inputFramebufferBindTexture();
    GLES20.glActiveTexture(33987);
    GLES20.glBindTexture(3553, this.mSecondInputFramebuffer == null ? 0 : this.mSecondInputFramebuffer.getTexture());
    GLES20.glUniform1i(this.mFilterInputTextureUniform2, 3);
    this.a.clear();
    this.a.put(textureCoordinates(this.mInputRotation2)).position(0);
    GLES20.glVertexAttribPointer(this.mFilterSecondTextureCoordinateAttribute, 2, 5126, false, 0, this.a);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    checkGLError(getClass().getSimpleName());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
  
  public int nextAvailableTextureIndex()
  {
    if (this.mHasSetFirstTexture) {
      return 1;
    }
    return 0;
  }
  
  public void setInputFramebuffer(SelesFramebuffer paramSelesFramebuffer, int paramInt)
  {
    if (paramSelesFramebuffer == null) {
      return;
    }
    if (paramInt == 0)
    {
      this.mFirstInputFramebuffer = paramSelesFramebuffer;
      this.mHasSetFirstTexture = true;
      this.mFirstInputFramebuffer.lock();
    }
    else
    {
      setInputFramebufferLast(paramSelesFramebuffer);
    }
  }
  
  protected void setInputFramebufferLast(SelesFramebuffer paramSelesFramebuffer)
  {
    this.mSecondInputFramebuffer = paramSelesFramebuffer;
    this.mSecondInputFramebuffer.lock();
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    if (paramInt == 0)
    {
      super.setInputSize(paramTuSdkSize, paramInt);
      if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
        this.mHasSetFirstTexture = false;
      }
    }
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    if (paramInt == 0) {
      this.mInputRotation = paramImageOrientation;
    } else {
      setInputRotationLast(paramImageOrientation);
    }
  }
  
  protected void setInputRotationLast(ImageOrientation paramImageOrientation)
  {
    this.mInputRotation2 = paramImageOrientation;
  }
  
  public TuSdkSize rotatedSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    if (paramTuSdkSize == null) {
      paramTuSdkSize = new TuSdkSize();
    }
    TuSdkSize localTuSdkSize = paramTuSdkSize.copy();
    ImageOrientation localImageOrientation = getRotationWithIndex(paramInt);
    if (localImageOrientation.isTransposed())
    {
      localTuSdkSize.width = paramTuSdkSize.height;
      localTuSdkSize.height = paramTuSdkSize.width;
    }
    return localTuSdkSize;
  }
  
  protected ImageOrientation getRotationWithIndex(int paramInt)
  {
    if (paramInt == 0) {
      return this.mInputRotation;
    }
    return getRotationLast();
  }
  
  protected ImageOrientation getRotationLast()
  {
    return this.mInputRotation2;
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    if (receivedFrames()) {
      return;
    }
    receivedFrame(paramInt);
    receivedFramesCheck();
    if (receivedFrames())
    {
      super.newFrameReady(paramLong, 0);
      receivedFramesResume();
    }
  }
  
  protected void receivedFrame(int paramInt)
  {
    if (paramInt == 0) {
      this.mHasReceivedFirstFrame = true;
    } else {
      receivedFrameLast();
    }
  }
  
  protected void receivedFrameLast()
  {
    this.mHasReceivedSecondFrame = true;
  }
  
  protected void receivedFramesCheck()
  {
    if (this.mFirstFrameCheckDisabled) {
      this.mHasReceivedFirstFrame = true;
    }
    if (this.mSecondFrameCheckDisabled) {
      this.mHasReceivedSecondFrame = true;
    }
  }
  
  protected boolean receivedFrames()
  {
    return (this.mHasReceivedFirstFrame) && (this.mHasReceivedSecondFrame);
  }
  
  protected void receivedFramesResume()
  {
    this.mHasReceivedFirstFrame = false;
    this.mHasReceivedSecondFrame = false;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\filters\SelesTwoInputFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */