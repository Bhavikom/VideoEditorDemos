package org.lasque.tusdk.core.seles.tusdk.filters.base;

import android.graphics.Color;
import android.graphics.PointF;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSDKSelectiveFilter
  extends SelesTwoInputFilter
{
  private int a;
  private int b;
  private int c;
  private int d;
  private int e;
  private int f;
  private int g;
  private int h;
  private PointF i = new PointF(0.5F, 0.5F);
  private float j = 0.4F;
  private float k = 0.2F;
  private float l;
  private int m = -1;
  private float n;
  private float o;
  private float p;
  
  public TuSDKSelectiveFilter()
  {
    super("-sb2");
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.a = this.mFilterProgram.uniformIndex("radius");
    this.b = this.mFilterProgram.uniformIndex("center");
    this.c = this.mFilterProgram.uniformIndex("aspectRatio");
    this.d = this.mFilterProgram.uniformIndex("excessive");
    this.e = this.mFilterProgram.uniformIndex("maskAlpha");
    this.f = this.mFilterProgram.uniformIndex("maskColor");
    this.g = this.mFilterProgram.uniformIndex("degree");
    this.h = this.mFilterProgram.uniformIndex("selective");
    setRadius(this.j);
    setCenter(this.i);
    setExcessive(this.k);
    setMaskColor(this.m);
    setMaskAlpha(this.l);
    setDegree(this.n);
    setSelective(this.o);
    a();
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    TuSdkSize localTuSdkSize = this.mInputTextureSize.copy();
    super.setInputSize(paramTuSdkSize, paramInt);
    if ((!localTuSdkSize.equals(this.mInputTextureSize)) && (paramTuSdkSize.isSize())) {
      a();
    }
  }
  
  private void a()
  {
    if ((!this.mInputTextureSize.isSize()) || (this.mInputRotation == null)) {
      return;
    }
    if (this.mInputRotation.isTransposed()) {
      a(this.mInputTextureSize.width / this.mInputTextureSize.height);
    } else {
      a(this.mInputTextureSize.height / this.mInputTextureSize.width);
    }
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    super.setInputRotation(paramImageOrientation, paramInt);
    setCenter(getCenter());
    a();
  }
  
  public void forceProcessingAtSize(TuSdkSize paramTuSdkSize)
  {
    super.forceProcessingAtSize(paramTuSdkSize);
    a();
  }
  
  private void a(float paramFloat)
  {
    this.p = paramFloat;
    if (this.p > 0.0F) {
      setFloat(this.p, this.c, this.mFilterProgram);
    }
  }
  
  public PointF getCenter()
  {
    return this.i;
  }
  
  public void setCenter(PointF paramPointF)
  {
    this.i = paramPointF;
    PointF localPointF = rotatedPoint(this.i, this.mInputRotation);
    setPoint(localPointF, this.b, this.mFilterProgram);
  }
  
  public float getRadius()
  {
    return this.j;
  }
  
  public void setRadius(float paramFloat)
  {
    this.j = paramFloat;
    setFloat(this.j, this.a, this.mFilterProgram);
  }
  
  public float getExcessive()
  {
    return this.k;
  }
  
  public void setExcessive(float paramFloat)
  {
    this.k = paramFloat;
    setFloat(this.k, this.d, this.mFilterProgram);
  }
  
  public int getMaskColor()
  {
    return this.m;
  }
  
  public void setMaskColor(int paramInt)
  {
    this.m = paramInt;
    float[] arrayOfFloat = new float[3];
    arrayOfFloat[0] = (Color.red(paramInt) / 255.0F);
    arrayOfFloat[1] = (Color.green(paramInt) / 255.0F);
    arrayOfFloat[2] = (Color.blue(paramInt) / 255.0F);
    setVec3(arrayOfFloat, this.f, this.mFilterProgram);
  }
  
  public float getMaskAlpha()
  {
    return this.l;
  }
  
  public void setMaskAlpha(float paramFloat)
  {
    this.l = paramFloat;
    setFloat(this.l, this.e, this.mFilterProgram);
  }
  
  public float getDegree()
  {
    return this.n;
  }
  
  public void setDegree(float paramFloat)
  {
    this.n = paramFloat;
    setFloat(this.n, this.g, this.mFilterProgram);
  }
  
  public float getSelective()
  {
    return this.o;
  }
  
  public void setSelective(float paramFloat)
  {
    this.o = paramFloat;
    setFloat(this.o, this.h, this.mFilterProgram);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\base\TuSDKSelectiveFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */