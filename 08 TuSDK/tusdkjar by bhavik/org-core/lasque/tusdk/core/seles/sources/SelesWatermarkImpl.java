package org.lasque.tusdk.core.seles.sources;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.opengl.GLES20;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.output.SelesSurfacePusher;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.WaterMarkPosition;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesWatermarkImpl
  extends SelesFilter
  implements SelesWatermark
{
  private final FloatBuffer a = buildBuffer(imageVertices);
  private final FloatBuffer b;
  private boolean c = false;
  private TuSdkWaterMarkOption.WaterMarkPosition d = TuSdkWaterMarkOption.WaterMarkPosition.TopRight;
  private float e = 0.12F;
  private float f = 0.02F;
  private boolean g = false;
  
  public void setWaterPostion(TuSdkWaterMarkOption.WaterMarkPosition paramWaterMarkPosition)
  {
    if (paramWaterMarkPosition == null) {
      return;
    }
    this.d = paramWaterMarkPosition;
    this.g = true;
  }
  
  public void setScale(float paramFloat)
  {
    if ((paramFloat <= 0.0F) || (paramFloat > 1.0F))
    {
      TLog.w("%s setScale need 0 < scaleWithWidth <= 1, %f", new Object[] { "SelesWatermarkImpl", Float.valueOf(paramFloat) });
      return;
    }
    this.e = paramFloat;
    this.g = true;
  }
  
  public void setPadding(float paramFloat)
  {
    if ((paramFloat > 0.0F) || (paramFloat > 1.0F))
    {
      TLog.w("%s setPadding need 0 <= paddingWithWidth < 1, %f", new Object[] { "SelesWatermarkImpl", Float.valueOf(paramFloat) });
      return;
    }
    this.f = paramFloat;
    this.g = true;
  }
  
  public SelesWatermarkImpl(boolean paramBoolean)
  {
    useNextFrameForImageCapture();
    this.c = paramBoolean;
    this.b = SelesFilter.buildBuffer(this.c ? SelesSurfacePusher.noRotationTextureCoordinates : SelesFilter.noRotationTextureCoordinates);
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    checkGLError("SelesWatermarkImpl onInitOnGLThread");
  }
  
  public void setImage(final Bitmap paramBitmap, final boolean paramBoolean)
  {
    if ((paramBitmap == null) || (paramBitmap.isRecycled()))
    {
      TLog.w("%s setImage is Null or Recycled, %s", new Object[] { "SelesWatermarkImpl", paramBitmap });
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        TuSdkSize localTuSdkSize = SelesContext.sizeThatFitsWithinATexture(TuSdkSize.create(paramBitmap));
        if ((localTuSdkSize == null) || (!localTuSdkSize.isSize()))
        {
          TLog.w("%s setImage is too small, %s", new Object[] { "SelesWatermarkImpl", paramBitmap });
          return;
        }
        SelesWatermarkImpl.this.removeOutputFramebuffer();
        SelesWatermarkImpl.a(SelesWatermarkImpl.this, SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.TEXTURE_ACTIVE, localTuSdkSize));
        SelesWatermarkImpl.a(SelesWatermarkImpl.this).bindTexture(paramBitmap, SelesWatermarkImpl.this.isShouldSmoothlyScaleOutput(), paramBoolean);
        SelesWatermarkImpl.a(SelesWatermarkImpl.this, true);
      }
    });
  }
  
  public void removeOutputFramebuffer()
  {
    if (this.mFirstInputFramebuffer != null)
    {
      this.mFirstInputFramebuffer.enableReferenceCounting();
      this.mFirstInputFramebuffer.unlock();
      this.mFirstInputFramebuffer = null;
    }
    super.removeOutputFramebuffer();
  }
  
  protected void onDestroy()
  {
    removeOutputFramebuffer();
    super.onDestroy();
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    if ((paramImageOrientation == null) || (this.mInputRotation == paramImageOrientation)) {
      return;
    }
    this.mInputRotation = ImageOrientation.getValue(this.mInputRotation.getDegree(), false);
    this.b.clear();
    if (this.c) {
      this.b.put(SelesSurfacePusher.textureCoordinates(this.mInputRotation)).position(0);
    } else {
      this.b.put(SelesFilter.textureCoordinates(this.mInputRotation)).position(0);
    }
    this.g = true;
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    if ((paramTuSdkSize == null) || (this.mInputTextureSize.equals(paramTuSdkSize))) {
      return;
    }
    this.mInputTextureSize = paramTuSdkSize;
    this.g = true;
  }
  
  public void drawInGLThread(long paramLong, TuSdkSize paramTuSdkSize, ImageOrientation paramImageOrientation)
  {
    setInputSize(paramTuSdkSize, 0);
    setInputRotation(paramImageOrientation, 0);
    newFrameReady(paramLong, 0);
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    if (!isEnabled()) {
      return;
    }
    renderToTexture(this.a, this.b);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    runPendingOnDrawTasks();
    if (this.mFirstInputFramebuffer == null) {
      return;
    }
    a();
    GLES20.glEnable(3042);
    GLES20.glBlendFunc(772, 771);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, this.mFirstInputFramebuffer == null ? 0 : this.mFirstInputFramebuffer.getTexture());
    GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
    checkGLError("SelesWatermarkImpl");
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, paramFloatBuffer1);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, paramFloatBuffer2);
    GLES20.glDrawArrays(5, 0, 4);
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    GLES20.glDisable(3042);
  }
  
  private void a()
  {
    if ((!this.g) || (this.mFirstInputFramebuffer == null)) {
      return;
    }
    this.g = false;
    TuSdkSize localTuSdkSize1 = this.mFirstInputFramebuffer.getSize();
    float f1 = localTuSdkSize1.getRatioFloat();
    float f2 = localTuSdkSize1.diagonal();
    TuSdkSize localTuSdkSize2 = this.mInputTextureSize.transforOrientation(this.mInputRotation);
    float f3 = this.mInputTextureSize.diagonal();
    if ((f2 == 0.0F) || (f3 == 0.0F)) {
      return;
    }
    float f4 = f3 * this.e / localTuSdkSize2.width;
    float f5 = f4 / f1 * localTuSdkSize2.getRatioFloat();
    float f6 = f3 * this.f / localTuSdkSize2.width;
    float f7 = f3 * this.f / localTuSdkSize2.height;
    RectF localRectF = new RectF();
    switch (2.a[this.d.ordinal()])
    {
    case 1: 
      localRectF.set(f6, f7, f4 + f6, f5 + f7);
      break;
    case 2: 
      localRectF.set(1.0F - f4 - f6, f7, 1.0F - f6, f5 + f7);
      break;
    case 3: 
      localRectF.set(f6, 1.0F - f5 - f7, f4 + f6, 1.0F - f7);
      break;
    case 4: 
      localRectF.set(1.0F - f4 - f6, 1.0F - f5 - f7, 1.0F - f6, 1.0F - f7);
      break;
    case 5: 
      localRectF.set(0.5F - f4 * 0.5F, 0.5F - f5 * 0.5F, 0.5F + f4 * 0.5F, 0.5F + f5 * 0.5F);
    }
    float[] arrayOfFloat;
    if (this.c) {
      arrayOfFloat = RectHelper.displayVertices(this.mInputRotation, localRectF);
    } else {
      arrayOfFloat = RectHelper.textureVertices(this.mInputRotation, localRectF);
    }
    this.a.put(arrayOfFloat).position(0);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesWatermarkImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */