package org.lasque.tusdk.core.seles.output;

import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLES20;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

@Deprecated
public class SelesSurfaceTextureOutput
  extends SelesFilter
{
  private FloatBuffer a = buildBuffer(imageVertices);
  private FloatBuffer b = buildBuffer(noRotationTextureCoordinates);
  private RectF c = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
  private ImageOrientation d = ImageOrientation.Up;
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  public void setOutputOrientation(ImageOrientation paramImageOrientation)
  {
    this.d = paramImageOrientation;
  }
  
  public ImageOrientation getOutputOrientation()
  {
    return this.d;
  }
  
  public void setInputFramebuffer(SelesFramebuffer paramSelesFramebuffer, int paramInt)
  {
    super.setInputFramebuffer(paramSelesFramebuffer, paramInt);
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    if (this.mFirstInputFramebuffer == null) {
      return;
    }
    this.b.clear();
    this.b.put(textureCoordinates(this.d)).position(0);
    renderToTexture(this.a, this.b);
    informTargetsAboutNewFrame(paramLong);
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
    if (this.mOutputFramebuffer == null)
    {
      this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, localTuSdkSize, getOutputTextureOptions());
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
    inputFramebufferUnlock();
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    GLES20.glBindTexture(3553, 0);
    cacaptureImageBuffer();
  }
  
  protected void informTargetsAboutNewFrame(long paramLong) {}
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    if (isPreventRendering()) {
      return;
    }
    if (this.mOverrideInputSize)
    {
      if ((this.mForcedMaximumSize == null) || (this.mForcedMaximumSize.minSide() < 1))
      {
        setupFilterForSize(sizeOfFBO());
        return;
      }
      localObject = RectHelper.makeRectWithAspectRatioInsideRect(paramTuSdkSize, new Rect(0, 0, this.mForcedMaximumSize.width, this.mForcedMaximumSize.height));
      paramTuSdkSize = TuSdkSize.create((Rect)localObject);
    }
    Object localObject = rotatedSize(paramTuSdkSize, paramInt);
    if (((TuSdkSize)localObject).minSide() < 1) {
      this.mInputTextureSize = ((TuSdkSize)localObject);
    } else if (!((TuSdkSize)localObject).equals(this.mInputTextureSize)) {
      this.mInputTextureSize = ((TuSdkSize)localObject);
    }
    TuSdkSize localTuSdkSize = new TuSdkSize();
    localTuSdkSize.width = ((int)(this.mInputTextureSize.width * this.c.width()));
    localTuSdkSize.height = ((int)(this.mInputTextureSize.height * this.c.height()));
    if (localTuSdkSize.isSize()) {
      this.mInputTextureSize = localTuSdkSize;
    } else if (!this.mInputTextureSize.equals(localTuSdkSize)) {
      this.mInputTextureSize = localTuSdkSize;
    }
    setupFilterForSize(sizeOfFBO());
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    super.setInputRotation(paramImageOrientation, paramInt);
    a();
  }
  
  public void setCropRegion(RectF paramRectF)
  {
    if ((this.c.left == paramRectF.left) && (this.c.right == paramRectF.right) && (this.c.top == paramRectF.top) && (this.c.bottom == paramRectF.bottom)) {
      return;
    }
    this.c = paramRectF;
    a();
  }
  
  private void a()
  {
    float f1 = 0.5F - this.c.right / 2.0F;
    float f2 = 0.5F - this.c.bottom / 2.0F;
    float f3 = 0.5F + this.c.right / 2.0F;
    float f4 = 0.5F + this.c.bottom / 2.0F;
    float[] arrayOfFloat = { 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F };
    arrayOfFloat[0] = f1;
    arrayOfFloat[1] = f2;
    arrayOfFloat[2] = f3;
    arrayOfFloat[3] = f2;
    arrayOfFloat[4] = f1;
    arrayOfFloat[5] = f4;
    arrayOfFloat[6] = f3;
    arrayOfFloat[7] = f4;
    this.b.clear();
    this.b.put(arrayOfFloat).position(0);
  }
  
  protected void onDestroy()
  {
    b();
  }
  
  private void b()
  {
    if (this.mOutputFramebuffer == null) {
      return;
    }
    this.mOutputFramebuffer.enableReferenceCounting();
    SelesContext.recycleFramebuffer(this.mOutputFramebuffer);
    this.mOutputFramebuffer = null;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\output\SelesSurfaceTextureOutput.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */