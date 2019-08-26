package org.lasque.tusdk.core.seles.tusdk.filters.lights;

import java.util.HashMap;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSDKLightGlareFilter
  extends SelesTwoInputFilter
  implements SelesParameters.FilterParameterInterface
{
  private float a = 0.8F;
  private float b;
  private int c;
  private int d;
  private ImageOrientation e = ImageOrientation.Up;
  
  public TuSDKLightGlareFilter()
  {
    super("-sl1");
    disableSecondFrameCheck();
  }
  
  public TuSDKLightGlareFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null) && (paramFilterOption.args.containsKey("mixied")))
    {
      float f = Float.parseFloat((String)paramFilterOption.args.get("mixied"));
      if (f > 0.0F) {
        setMix(f);
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.c = this.mFilterProgram.uniformIndex("mixturePercent");
    this.d = this.mFilterProgram.uniformIndex("aspectRatio");
    setMix(this.a);
    a();
  }
  
  private void a()
  {
    if ((!this.mInputTextureSize.isSize()) || (this.e == null)) {
      return;
    }
    if (this.e.isTransposed()) {
      a(this.mInputTextureSize.width / this.mInputTextureSize.height);
    } else {
      a(this.mInputTextureSize.height / this.mInputTextureSize.width);
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
    if ((paramInt > 0) && (this.e != paramImageOrientation))
    {
      this.e = paramImageOrientation;
      a();
    }
  }
  
  public float getMix()
  {
    return this.a;
  }
  
  public void setMix(float paramFloat)
  {
    this.a = paramFloat;
    setFloat(this.a, this.c, this.mFilterProgram);
  }
  
  private void a(float paramFloat)
  {
    this.b = paramFloat;
    if (this.b > 0.0F) {
      setFloat(this.b, this.d, this.mFilterProgram);
    }
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("mixied", this.a);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("mixied")) {
      setMix(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lights\TuSDKLightGlareFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */