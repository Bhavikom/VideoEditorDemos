package org.lasque.tusdk.core.seles.filters;

import android.graphics.Rect;
import android.graphics.RectF;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesCropFilter
  extends SelesFilter
{
  private FloatBuffer a = SelesFilter.buildBuffer(noRotationTextureCoordinates);
  private RectF b = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
  private TuSdkSize c = new TuSdkSize(0, 0);
  private boolean d = false;
  
  public void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize.isSize()) && (!paramTuSdkSize.equals(this.c))) {
      this.c = paramTuSdkSize;
    }
  }
  
  public TuSdkSize getOutputSize()
  {
    if (this.c.isSize()) {
      return this.c;
    }
    return this.mInputTextureSize;
  }
  
  public void setEnableHorizontallyFlip(boolean paramBoolean)
  {
    this.d = paramBoolean;
    a();
  }
  
  public boolean isEnableHorizontallyFlip()
  {
    return this.d;
  }
  
  public void setCropRegion(RectF paramRectF)
  {
    if ((this.b.left == paramRectF.left) && (this.b.right == paramRectF.right) && (this.b.top == paramRectF.top) && (this.b.bottom == paramRectF.bottom)) {
      return;
    }
    this.b = paramRectF;
    a();
  }
  
  protected void updateCropRegion(TuSdkSize paramTuSdkSize)
  {
    RectF localRectF = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
    if ((!paramTuSdkSize.isSize()) || (!this.c.isSize())) {
      return;
    }
    TuSdkSize localTuSdkSize = RectHelper.computerOutSize(paramTuSdkSize, this.c.getRatioFloat(), true);
    localRectF.right = (localTuSdkSize.width / paramTuSdkSize.width);
    localRectF.bottom = (localTuSdkSize.height / paramTuSdkSize.height);
    setCropRegion(localRectF);
  }
  
  private void a()
  {
    float f1 = 0.5F - this.b.right / 2.0F;
    float f2 = 0.5F - this.b.bottom / 2.0F;
    float f3 = 0.5F + this.b.right / 2.0F;
    float f4 = 0.5F + this.b.bottom / 2.0F;
    float[] arrayOfFloat = { 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
    ImageOrientation localImageOrientation = this.mInputRotation;
    if ((isEnableHorizontallyFlip()) && (localImageOrientation == ImageOrientation.Up)) {
      localImageOrientation = ImageOrientation.UpMirrored;
    }
    switch (1.a[localImageOrientation.ordinal()])
    {
    case 1: 
      arrayOfFloat[0] = f3;
      arrayOfFloat[1] = f2;
      arrayOfFloat[2] = f3;
      arrayOfFloat[3] = f4;
      arrayOfFloat[4] = f1;
      arrayOfFloat[5] = f2;
      arrayOfFloat[6] = f1;
      arrayOfFloat[7] = f4;
      break;
    case 2: 
      arrayOfFloat[0] = f1;
      arrayOfFloat[1] = f4;
      arrayOfFloat[2] = f1;
      arrayOfFloat[3] = f2;
      arrayOfFloat[4] = f3;
      arrayOfFloat[5] = f4;
      arrayOfFloat[6] = f3;
      arrayOfFloat[7] = f2;
      break;
    case 3: 
      arrayOfFloat[0] = f1;
      arrayOfFloat[1] = f4;
      arrayOfFloat[2] = f3;
      arrayOfFloat[3] = f4;
      arrayOfFloat[4] = f1;
      arrayOfFloat[5] = f2;
      arrayOfFloat[6] = f3;
      arrayOfFloat[7] = f2;
      break;
    case 4: 
      arrayOfFloat[0] = f3;
      arrayOfFloat[1] = f2;
      arrayOfFloat[2] = f1;
      arrayOfFloat[3] = f2;
      arrayOfFloat[4] = f3;
      arrayOfFloat[5] = f4;
      arrayOfFloat[6] = f1;
      arrayOfFloat[7] = f4;
      break;
    case 5: 
      arrayOfFloat[0] = f1;
      arrayOfFloat[1] = f2;
      arrayOfFloat[2] = f1;
      arrayOfFloat[3] = f4;
      arrayOfFloat[4] = f3;
      arrayOfFloat[5] = f2;
      arrayOfFloat[6] = f3;
      arrayOfFloat[7] = f4;
      break;
    case 6: 
      arrayOfFloat[0] = f3;
      arrayOfFloat[1] = f4;
      arrayOfFloat[2] = f3;
      arrayOfFloat[3] = f2;
      arrayOfFloat[4] = f1;
      arrayOfFloat[5] = f4;
      arrayOfFloat[6] = f1;
      arrayOfFloat[7] = f2;
      break;
    case 7: 
      arrayOfFloat[0] = f3;
      arrayOfFloat[1] = f4;
      arrayOfFloat[2] = f1;
      arrayOfFloat[3] = f4;
      arrayOfFloat[4] = f3;
      arrayOfFloat[5] = f2;
      arrayOfFloat[6] = f1;
      arrayOfFloat[7] = f2;
      break;
    case 8: 
    default: 
      arrayOfFloat[0] = f1;
      arrayOfFloat[1] = f2;
      arrayOfFloat[2] = f3;
      arrayOfFloat[3] = f2;
      arrayOfFloat[4] = f1;
      arrayOfFloat[5] = f4;
      arrayOfFloat[6] = f3;
      arrayOfFloat[7] = f4;
    }
    this.a.clear();
    this.a.put(arrayOfFloat).position(0);
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    super.setInputRotation(paramImageOrientation, paramInt);
    a();
  }
  
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
    if ((((TuSdkSize)localObject).isSize()) && (!((TuSdkSize)localObject).equals(this.mInputTextureSize))) {
      this.mInputTextureSize = ((TuSdkSize)localObject);
    }
    updateCropRegion(this.mInputTextureSize);
    TuSdkSize localTuSdkSize = new TuSdkSize();
    localTuSdkSize.width = ((int)(this.mInputTextureSize.width * this.b.width()));
    localTuSdkSize.height = ((int)(this.mInputTextureSize.height * this.b.height()));
    if ((localTuSdkSize.isSize()) && (!this.mInputTextureSize.equals(localTuSdkSize))) {
      this.mInputTextureSize = localTuSdkSize;
    }
    setupFilterForSize(sizeOfFBO());
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, this.a);
    checkGLError(getClass().getSimpleName());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
  
  public TuSdkSize maximumOutputSize()
  {
    return this.c;
  }
  
  public void setUsingNextFrameForImageCapture(boolean paramBoolean)
  {
    this.mUsingNextFrameForImageCapture = paramBoolean;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\filters\SelesCropFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */