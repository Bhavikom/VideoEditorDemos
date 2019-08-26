package org.lasque.tusdk.core.seles.tusdk.filters.colors;

import android.graphics.Color;
import android.graphics.PointF;
import java.util.HashMap;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSDKColorMixCoverFilter
  extends SelesThreeInputFilter
  implements SelesParameters.FilterParameterInterface
{
  private int a;
  private int b;
  private int c;
  private int d;
  private PointF e = new PointF(0.5F, 0.5F);
  private int f;
  private float[] g = { 0.0F, 0.0F, 0.0F };
  private int h;
  private float i = 0.25F;
  private int j;
  private float k = 1.0F;
  private float l = 1.0F;
  private float m = 1.0F;
  private float n;
  private ImageOrientation o = ImageOrientation.Up;
  
  public TuSDKColorMixCoverFilter()
  {
    super("-scmc");
    disableSecondFrameCheck();
    disableThirdFrameCheck();
  }
  
  public TuSDKColorMixCoverFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null))
    {
      float f1;
      if (paramFilterOption.args.containsKey("mixied"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("mixied"));
        if (f1 > 0.0F) {
          setMixed(f1);
        }
      }
      if (paramFilterOption.args.containsKey("vignette"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("vignette"));
        if (f1 > 0.0F) {
          a(f1);
        }
      }
      if (paramFilterOption.args.containsKey("texture"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("texture"));
        if (f1 > 0.0F) {
          setCover(f1);
        }
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.a = this.mFilterProgram.uniformIndex("mixturePercent");
    this.b = this.mFilterProgram.uniformIndex("coverPercent");
    this.d = this.mFilterProgram.uniformIndex("vignetteCenter");
    this.f = this.mFilterProgram.uniformIndex("vignetteColor");
    this.h = this.mFilterProgram.uniformIndex("vignetteStart");
    this.j = this.mFilterProgram.uniformIndex("vignetteEnd");
    this.c = this.mFilterProgram.uniformIndex("aspectRatio");
    setMixed(this.l);
    setCover(this.m);
    a(this.e);
    a(this.g);
    a(this.i);
    b(this.k);
    a();
  }
  
  public float getMixed()
  {
    return this.l;
  }
  
  public void setMixed(float paramFloat)
  {
    this.l = paramFloat;
    setFloat(this.l, this.a, this.mFilterProgram);
  }
  
  public float getCover()
  {
    return this.m;
  }
  
  public void setCover(float paramFloat)
  {
    this.m = paramFloat;
    setFloat(this.m, this.b, this.mFilterProgram);
  }
  
  private void a(PointF paramPointF)
  {
    this.e = paramPointF;
    setPoint(this.e, this.d, this.mFilterProgram);
  }
  
  public void setVignetteColor(int paramInt)
  {
    float[] arrayOfFloat = new float[3];
    arrayOfFloat[0] = (Color.red(paramInt) / 255.0F);
    arrayOfFloat[1] = (Color.green(paramInt) / 255.0F);
    arrayOfFloat[2] = (Color.blue(paramInt) / 255.0F);
    a(arrayOfFloat);
  }
  
  private void a(float[] paramArrayOfFloat)
  {
    this.g = paramArrayOfFloat;
    setVec3(this.g, this.f, this.mFilterProgram);
  }
  
  private void a(float paramFloat)
  {
    this.i = paramFloat;
    setFloat(this.i, this.h, this.mFilterProgram);
  }
  
  private void b(float paramFloat)
  {
    this.k = paramFloat;
    setFloat(this.k, this.j, this.mFilterProgram);
  }
  
  private void c(float paramFloat)
  {
    this.n = paramFloat;
    if (this.n > 0.0F) {
      setFloat(this.n, this.c, this.mFilterProgram);
    }
  }
  
  private void a()
  {
    if ((!this.mInputTextureSize.isSize()) || (this.o == null)) {
      return;
    }
    if (this.o.isTransposed()) {
      c(this.mInputTextureSize.width / this.mInputTextureSize.height);
    } else {
      c(this.mInputTextureSize.height / this.mInputTextureSize.width);
    }
  }
  
  public void forceProcessingAtSize(TuSdkSize paramTuSdkSize)
  {
    super.forceProcessingAtSize(paramTuSdkSize);
    a();
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    TuSdkSize localTuSdkSize = this.mInputTextureSize.copy();
    super.setInputSize(paramTuSdkSize, paramInt);
    if ((paramInt == 0) && (!localTuSdkSize.equals(this.mInputTextureSize)) && (paramTuSdkSize.isSize())) {
      a();
    }
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    super.setInputRotation(paramImageOrientation, paramInt);
    if ((paramInt > 0) && (this.o != paramImageOrientation))
    {
      this.o = paramImageOrientation;
      a();
    }
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("mixied", getMixed());
    paramSelesParameters.appendFloatArg("texture", getCover());
    paramSelesParameters.appendFloatArg("vignette", this.i, 1.0F, 0.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("mixied")) {
      setMixed(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("texture")) {
      setCover(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("vignette")) {
      a(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\colors\TuSDKColorMixCoverFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */