package org.lasque.tusdk.core.seles.extend;

import android.graphics.Rect;
import android.graphics.RectF;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.output.SelesSurfacePusher;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesVerticeCoordinateCropBuilderImpl
  implements SelesVerticeCoordinateCorpBuilder
{
  private TuSdkSize a = TuSdkSize.create(0);
  private TuSdkSize b = TuSdkSize.create(0);
  private RectF c = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
  private RectF d = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
  private RectF e;
  private ImageOrientation f = ImageOrientation.Up;
  private boolean g = false;
  private SelesTextureSizeAlign h = SelesTextureSizeAlign.Align2MultipleMax;
  private boolean i = false;
  private boolean j = false;
  private boolean k = true;
  private float l;
  
  public TuSdkSize outputSize()
  {
    return this.b;
  }
  
  public void setTextureSizeAlign(SelesTextureSizeAlign paramSelesTextureSizeAlign)
  {
    if ((paramSelesTextureSizeAlign == null) || (this.h == paramSelesTextureSizeAlign)) {
      return;
    }
    this.h = paramSelesTextureSizeAlign;
    this.b = this.h.align(this.b);
    this.g = true;
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (paramTuSdkSize.minSide() < this.h.getMultiple()))
    {
      TLog.w("%s setOutputSize is Null or side < %d, size: %s", new Object[] { "SelesVerticeCoordinateCropBuilderImpl", Integer.valueOf(this.h.getMultiple()), paramTuSdkSize });
      return;
    }
    if ((paramTuSdkSize.equals(this.b)) && (this.k)) {
      return;
    }
    this.b = this.h.align(paramTuSdkSize.copy());
    this.g = true;
    this.j = true;
  }
  
  public void setCanvasRect(RectF paramRectF)
  {
    if ((paramRectF == null) || (paramRectF.width() == 0.0F) || (paramRectF.height() == 0.0F) || (paramRectF.equals(this.c))) {
      return;
    }
    this.c = new RectF(paramRectF);
    this.g = true;
  }
  
  public void setCropRect(RectF paramRectF)
  {
    if ((paramRectF == null) || (paramRectF.width() == 0.0F) || (paramRectF.height() == 0.0F) || (paramRectF.equals(this.c))) {
      return;
    }
    this.d = new RectF(paramRectF);
    this.g = true;
  }
  
  public void setPreCropRect(RectF paramRectF)
  {
    if ((paramRectF != null) && (!paramRectF.equals(paramRectF))) {
      paramRectF = new RectF(paramRectF);
    }
    this.e = paramRectF;
    this.g = true;
  }
  
  public void setEnableClip(boolean paramBoolean)
  {
    this.k = paramBoolean;
  }
  
  public TuSdkSize setOutputRatio(float paramFloat)
  {
    this.l = paramFloat;
    if (paramFloat == 0.0F)
    {
      this.c.left = 0.0F;
      this.c.top = 0.0F;
      this.c.right = 1.0F;
      this.c.bottom = 1.0F;
      setOutputSize(this.a);
      return this.a;
    }
    int m = this.a.width > this.a.height ? this.a.height : this.a.width;
    int n = (int)(m / paramFloat);
    TuSdkSize localTuSdkSize = TuSdkSize.create(m, n);
    setOutputSize(localTuSdkSize);
    return localTuSdkSize;
  }
  
  public float getOutputRatio(float paramFloat)
  {
    return this.l;
  }
  
  public SelesVerticeCoordinateCropBuilderImpl(boolean paramBoolean)
  {
    this.i = paramBoolean;
  }
  
  public boolean calculate(TuSdkSize paramTuSdkSize, ImageOrientation paramImageOrientation, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    if ((paramTuSdkSize == null) || (paramTuSdkSize.minSide() < this.h.getMultiple()))
    {
      TLog.w("%s calculate need min side >= %d, Input: %s", new Object[] { "SelesVerticeCoordinateCropBuilderImpl", Integer.valueOf(this.h.getMultiple()), paramTuSdkSize });
      return false;
    }
    if (paramFloatBuffer1 == null)
    {
      TLog.w("%s calculate need verticesBuffer", new Object[] { "SelesVerticeCoordinateCropBuilderImpl" });
      return false;
    }
    if (paramFloatBuffer2 == null)
    {
      TLog.w("%s calculate need textureBuffer", new Object[] { "SelesVerticeCoordinateCropBuilderImpl" });
      return false;
    }
    if (paramImageOrientation == null) {
      paramImageOrientation = ImageOrientation.Up;
    }
    if ((paramTuSdkSize.equals(this.a)) && (paramImageOrientation == this.f) && (!this.g)) {
      return true;
    }
    this.g = false;
    this.a = paramTuSdkSize.copy();
    this.f = paramImageOrientation;
    RectF localRectF1 = this.e == null ? null : RectHelper.rotationWithRotation(this.e, paramImageOrientation);
    RectF localRectF2 = this.d.contains(0.0F, 0.0F, 1.0F, 1.0F) ? null : this.d;
    if (!this.j)
    {
      localTuSdkSize = paramTuSdkSize.copy();
      if (localRectF1 != null)
      {
        TuSdkSize tmp211_209 = localTuSdkSize;
        tmp211_209.width = ((int)(tmp211_209.width * localRectF1.width()));
        TuSdkSize tmp228_226 = localTuSdkSize;
        tmp228_226.height = ((int)(tmp228_226.height * localRectF1.height()));
      }
      if (localRectF2 != null)
      {
        TuSdkSize tmp250_248 = localTuSdkSize;
        tmp250_248.width = ((int)(tmp250_248.width * localRectF2.width()));
        TuSdkSize tmp267_265 = localTuSdkSize;
        tmp267_265.height = ((int)(tmp267_265.height * localRectF2.height()));
      }
      this.b = this.h.align(localTuSdkSize);
    }
    TLog.d("%s Input: %s | Output: %s | Orientation: %s | PreCropRect: %s | CropRect: %s | CanvasRect: %s", new Object[] { "SelesVerticeCoordinateCropBuilderImpl", this.a, this.b, paramImageOrientation, this.e, this.d, this.c });
    TuSdkSize localTuSdkSize = this.b;
    if ((!this.k) && ((!this.a.equals(this.b)) || (this.l != 0.0F)))
    {
      localObject1 = new Rect(0, 0, this.b.width, this.b.height);
      localObject2 = RectHelper.makeRectWithAspectRatioInsideRect(this.a, (Rect)localObject1);
      this.c.left = (((Rect)localObject2).left / this.b.width);
      this.c.right = (((Rect)localObject2).right / this.b.width);
      this.c.top = (((Rect)localObject2).top / this.b.height);
      this.c.bottom = (((Rect)localObject2).bottom / this.b.height);
    }
    Object localObject1 = new RectF(this.c);
    Object localObject2 = TuSdkSize.create((int)(((RectF)localObject1).width() * localTuSdkSize.width), (int)(((RectF)localObject1).height() * localTuSdkSize.height));
    a(paramFloatBuffer1, (RectF)localObject1, ((TuSdkSize)localObject2).equals(localTuSdkSize));
    if ((!this.k) && (this.l != 0.0F)) {
      return true;
    }
    a(paramTuSdkSize, paramImageOrientation, paramFloatBuffer2, (TuSdkSize)localObject2, localRectF1, localRectF2);
    return true;
  }
  
  private void a(FloatBuffer paramFloatBuffer, RectF paramRectF, boolean paramBoolean)
  {
    paramFloatBuffer.clear();
    if (paramBoolean)
    {
      paramFloatBuffer.put(SelesFilter.imageVertices).position(0);
      return;
    }
    float[] arrayOfFloat = new float[8];
    if (this.i)
    {
      arrayOfFloat[0] = (paramRectF.left * 2.0F - 1.0F);
      arrayOfFloat[1] = (1.0F - paramRectF.bottom * 2.0F);
      arrayOfFloat[2] = (paramRectF.right * 2.0F - 1.0F);
      arrayOfFloat[3] = arrayOfFloat[1];
      arrayOfFloat[4] = arrayOfFloat[0];
      arrayOfFloat[5] = (1.0F - paramRectF.top * 2.0F);
      arrayOfFloat[6] = arrayOfFloat[2];
      arrayOfFloat[7] = arrayOfFloat[5];
    }
    else
    {
      arrayOfFloat[0] = (paramRectF.left * 2.0F - 1.0F);
      arrayOfFloat[1] = (paramRectF.top * 2.0F - 1.0F);
      arrayOfFloat[2] = (paramRectF.right * 2.0F - 1.0F);
      arrayOfFloat[3] = arrayOfFloat[1];
      arrayOfFloat[4] = arrayOfFloat[0];
      arrayOfFloat[5] = (paramRectF.bottom * 2.0F - 1.0F);
      arrayOfFloat[6] = arrayOfFloat[2];
      arrayOfFloat[7] = arrayOfFloat[5];
    }
    paramFloatBuffer.put(arrayOfFloat).position(0);
  }
  
  private void a(TuSdkSize paramTuSdkSize1, ImageOrientation paramImageOrientation, FloatBuffer paramFloatBuffer, TuSdkSize paramTuSdkSize2, RectF paramRectF1, RectF paramRectF2)
  {
    if ((paramTuSdkSize1.equals(paramTuSdkSize2)) && (paramRectF1 == null) && (paramRectF2 == null))
    {
      paramFloatBuffer.clear();
      if (this.i) {
        paramFloatBuffer.put(SelesSurfacePusher.textureCoordinates(paramImageOrientation)).position(0);
      } else {
        paramFloatBuffer.put(SelesFilter.textureCoordinates(paramImageOrientation)).position(0);
      }
      return;
    }
    Rect localRect1 = new Rect(0, 0, paramTuSdkSize1.width, paramTuSdkSize1.height);
    if (paramRectF1 != null)
    {
      localRect1.left = ((int)(paramRectF1.left * paramTuSdkSize1.width));
      localRect1.right = ((int)(paramRectF1.right * paramTuSdkSize1.width));
      localRect1.top = ((int)(paramRectF1.top * paramTuSdkSize1.height));
      localRect1.bottom = ((int)(paramRectF1.bottom * paramTuSdkSize1.height));
    }
    if (paramRectF2 != null)
    {
      int m = localRect1.width();
      int n = localRect1.height();
      localRect1.left += (int)(paramRectF2.left * m);
      localRect1.right = (localRect1.left + (int)(paramRectF2.width() * m));
      localRect1.top += (int)(paramRectF2.top * n);
      localRect1.bottom = (localRect1.top + (int)(paramRectF2.height() * n));
    }
    Rect localRect2 = RectHelper.makeRectWithAspectRatioInsideRect(paramTuSdkSize2, localRect1);
    TLog.d("%s size: %s, displaySize: %s, textureRect: %s, displayRect: %s", new Object[] { "SelesVerticeCoordinateCropBuilderImpl", paramTuSdkSize1, paramTuSdkSize2, localRect1, localRect2 });
    RectF localRectF = new RectF();
    localRectF.left = (localRect2.left / paramTuSdkSize1.width);
    localRectF.top = (localRect2.top / paramTuSdkSize1.height);
    localRectF.right = (localRect2.right / paramTuSdkSize1.width);
    localRectF.bottom = (localRect2.bottom / paramTuSdkSize1.height);
    float[] arrayOfFloat;
    if (this.i) {
      arrayOfFloat = RectHelper.displayCoordinates(paramImageOrientation, localRectF);
    } else {
      arrayOfFloat = RectHelper.textureCoordinates(paramImageOrientation, localRectF);
    }
    paramFloatBuffer.clear();
    paramFloatBuffer.put(arrayOfFloat).position(0);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\extend\SelesVerticeCoordinateCropBuilderImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */