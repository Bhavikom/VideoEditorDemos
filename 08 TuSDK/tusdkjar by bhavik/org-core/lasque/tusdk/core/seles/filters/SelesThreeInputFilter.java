package org.lasque.tusdk.core.seles.filters;

import android.opengl.GLES20;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesThreeInputFilter
  extends SelesTwoInputFilter
{
  protected SelesFramebuffer mThirdInputFramebuffer;
  protected int mFilterThirdTextureCoordinateAttribute;
  protected int mFilterInputTextureUniform3;
  protected ImageOrientation mInputRotation3 = ImageOrientation.Up;
  protected boolean mHasSetSecondTexture = false;
  protected boolean mHasReceivedThirdFrame = false;
  protected boolean mThirdFrameWasVideo = false;
  protected boolean mThirdFrameCheckDisabled = false;
  private final FloatBuffer a = buildBuffer(noRotationTextureCoordinates);
  
  public SelesThreeInputFilter(String paramString)
  {
    this("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nattribute vec4 inputTextureCoordinate2;\nattribute vec4 inputTextureCoordinate3;\n\nvarying vec2 textureCoordinate;\nvarying vec2 textureCoordinate2;\nvarying vec2 textureCoordinate3;\n\nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n    textureCoordinate2 = inputTextureCoordinate2.xy;\n    textureCoordinate3 = inputTextureCoordinate3.xy;\n}\n", paramString);
  }
  
  public SelesThreeInputFilter(String paramString1, String paramString2)
  {
    super(paramString1, paramString2);
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.mFilterThirdTextureCoordinateAttribute = this.mFilterProgram.attributeIndex("inputTextureCoordinate3");
    this.mFilterInputTextureUniform3 = this.mFilterProgram.uniformIndex("inputImageTexture3");
    GLES20.glEnableVertexAttribArray(this.mFilterThirdTextureCoordinateAttribute);
  }
  
  protected void initializeAttributes()
  {
    super.initializeAttributes();
    this.mFilterProgram.addAttribute("inputTextureCoordinate3");
  }
  
  public void disableThirdFrameCheck()
  {
    this.mThirdFrameCheckDisabled = true;
  }
  
  protected void inputFramebufferUnlock()
  {
    super.inputFramebufferUnlock();
    if (this.mThirdInputFramebuffer != null) {
      this.mThirdInputFramebuffer.unlock();
    }
  }
  
  protected void inputFramebufferBindTexture()
  {
    super.inputFramebufferBindTexture();
    GLES20.glActiveTexture(33988);
    GLES20.glBindTexture(3553, this.mThirdInputFramebuffer == null ? 0 : this.mThirdInputFramebuffer.getTexture());
    GLES20.glUniform1i(this.mFilterInputTextureUniform3, 4);
    this.a.clear();
    this.a.put(textureCoordinates(this.mInputRotation3)).position(0);
    GLES20.glVertexAttribPointer(this.mFilterThirdTextureCoordinateAttribute, 2, 5126, false, 0, this.a);
  }
  
  public int nextAvailableTextureIndex()
  {
    if (this.mHasSetSecondTexture) {
      return 2;
    }
    return super.nextAvailableTextureIndex();
  }
  
  public void setInputFramebuffer(SelesFramebuffer paramSelesFramebuffer, int paramInt)
  {
    if (paramSelesFramebuffer == null) {
      return;
    }
    if (paramInt == 1)
    {
      this.mSecondInputFramebuffer = paramSelesFramebuffer;
      this.mHasSetSecondTexture = true;
      this.mSecondInputFramebuffer.lock();
    }
    else
    {
      super.setInputFramebuffer(paramSelesFramebuffer, paramInt);
    }
  }
  
  protected void setInputFramebufferLast(SelesFramebuffer paramSelesFramebuffer)
  {
    this.mThirdInputFramebuffer = paramSelesFramebuffer;
    this.mThirdInputFramebuffer.lock();
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    super.setInputSize(paramTuSdkSize, paramInt);
    if ((paramInt == 1) && ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize()))) {
      this.mHasSetSecondTexture = false;
    }
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    if (paramInt == 1) {
      super.setInputRotationLast(paramImageOrientation);
    } else {
      super.setInputRotation(paramImageOrientation, paramInt);
    }
  }
  
  protected void setInputRotationLast(ImageOrientation paramImageOrientation)
  {
    this.mInputRotation3 = paramImageOrientation;
  }
  
  protected ImageOrientation getRotationWithIndex(int paramInt)
  {
    if (paramInt == 1) {
      return super.getRotationLast();
    }
    return super.getRotationWithIndex(paramInt);
  }
  
  protected ImageOrientation getRotationLast()
  {
    return this.mInputRotation3;
  }
  
  protected void receivedFrame(int paramInt)
  {
    if (paramInt == 1) {
      super.receivedFrameLast();
    } else {
      super.receivedFrame(paramInt);
    }
  }
  
  protected void receivedFrameLast()
  {
    this.mHasReceivedThirdFrame = true;
  }
  
  protected void receivedFramesCheck()
  {
    super.receivedFramesCheck();
    if (this.mThirdFrameCheckDisabled) {
      this.mHasReceivedThirdFrame = true;
    }
  }
  
  protected boolean receivedFrames()
  {
    return (this.mHasReceivedThirdFrame) && (super.receivedFrames());
  }
  
  protected void receivedFramesResume()
  {
    super.receivedFramesResume();
    this.mHasReceivedThirdFrame = false;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\filters\SelesThreeInputFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */