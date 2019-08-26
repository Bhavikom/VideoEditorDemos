package org.lasque.tusdk.core.seles.tusdk.filters.lives;

import java.util.HashMap;
import java.util.Random;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSDKLiveOldTVFilter
  extends SelesTwoInputFilter
{
  private int a;
  private int b;
  private int c;
  private float d = 0.0F;
  private float e = 0.0F;
  private float[] f = { 0.0F, 0.0F, 0.0F, 0.0F };
  private float g = 1.0F;
  private int h = 0;
  private int i = 0;
  
  public TuSDKLiveOldTVFilter()
  {
    super("-slive11f");
    disableSecondFrameCheck();
  }
  
  public TuSDKLiveOldTVFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null))
    {
      float f1 = 0.0F;
      float f2 = 0.0F;
      float f3;
      if (paramFilterOption.args.containsKey("noiseX"))
      {
        f3 = Float.parseFloat((String)paramFilterOption.args.get("noiseX"));
        if (f3 > 0.0F) {
          f1 = f3;
        }
      }
      if (paramFilterOption.args.containsKey("noiseY"))
      {
        f3 = Float.parseFloat((String)paramFilterOption.args.get("noiseY"));
        if (f3 > 0.0F) {
          f2 = f3;
        }
      }
      this.f = new float[] { f1, f2, 0.0F, 0.0F };
      setNoise(new float[] { f1, f2, 0.0F, 0.0F });
      if (paramFilterOption.args.containsKey("animation"))
      {
        f3 = Float.parseFloat((String)paramFilterOption.args.get("animation"));
        if (f3 > 0.0F) {
          setAnimation(f3);
        }
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.a = this.mFilterProgram.uniformIndex("screenPercent");
    this.b = this.mFilterProgram.uniformIndex("lineSpeed");
    this.c = this.mFilterProgram.uniformIndex("noise");
    setScreenPercent(this.d);
    setLineSpeed(this.e);
    setNoise(this.f);
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    super.setInputRotation(paramImageOrientation, paramInt);
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    a();
    super.informTargetsAboutNewFrame(paramLong);
  }
  
  public float getScreenPercent()
  {
    return this.d;
  }
  
  public void setScreenPercent(float paramFloat)
  {
    this.d = paramFloat;
    setFloat(this.d, this.a, this.mFilterProgram);
  }
  
  public float getLineSpeed()
  {
    return this.e;
  }
  
  public void setLineSpeed(float paramFloat)
  {
    this.e = paramFloat;
    setFloat(this.e, this.b, this.mFilterProgram);
  }
  
  public float[] getNoise()
  {
    return this.f;
  }
  
  public void setNoise(float[] paramArrayOfFloat)
  {
    this.f = paramArrayOfFloat;
    setVec4(this.f, this.c, this.mFilterProgram);
  }
  
  public void setNoiseX(float paramFloat)
  {
    float[] arrayOfFloat = getNoise();
    arrayOfFloat[0] = paramFloat;
    setNoise(arrayOfFloat);
  }
  
  public void setNoiseY(float paramFloat)
  {
    float[] arrayOfFloat = getNoise();
    arrayOfFloat[1] = paramFloat;
    setNoise(arrayOfFloat);
  }
  
  public void setNoiseType(float paramFloat)
  {
    float[] arrayOfFloat = getNoise();
    arrayOfFloat[2] = paramFloat;
    setNoise(arrayOfFloat);
  }
  
  public void setNoiseMixed(float paramFloat)
  {
    float[] arrayOfFloat = getNoise();
    arrayOfFloat[3] = paramFloat;
    setNoise(arrayOfFloat);
  }
  
  public float getAnimation()
  {
    return this.g;
  }
  
  public void setAnimation(float paramFloat)
  {
    this.g = paramFloat;
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("screenPercent", getScreenPercent(), 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("lineSpeed", getLineSpeed(), 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("noiseX", getNoise()[0], -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("noiseY", getNoise()[1], -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("noiseType", getNoise()[2], 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("noiseMixed", getNoise()[3], 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("animation", getAnimation(), 0.0F, 1.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("screenPercent")) {
      setScreenPercent(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("lineSpeed")) {
      setLineSpeed(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("noiseX")) {
      setNoiseX(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("noiseY")) {
      setNoiseY(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("noiseType")) {
      setNoiseType(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("noiseMixed")) {
      setNoiseMixed(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("animation")) {
      setAnimation(paramFilterArg.getValue());
    }
  }
  
  private void a()
  {
    float[] arrayOfFloat = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.35F, 0.4F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.15F, 0.25F, 0.45F, 0.65F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.55F, 0.15F, 0.25F, 0.3F, 0.35F, 0.3F, 0.28F, 0.25F, 0.25F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3F, 0.35F, 0.3F };
    if (this.h == 103) {
      this.h = 0;
    }
    if (this.h < 102) {
      getParameter().setFilterArg("screenPercent", arrayOfFloat[this.h]);
    }
    if (this.i >= 25) {
      this.i = 0;
    }
    Random localRandom = new Random();
    getParameter().setFilterArg("lineSpeed", this.i / 100.0F);
    getParameter().setFilterArg("noiseX", localRandom.nextInt(100) / 100.0F);
    getParameter().setFilterArg("noiseY", localRandom.nextInt(100) / 100.0F);
    getParameter().setFilterArg("noiseType", 0.41F);
    getParameter().setFilterArg("noiseMixed", 0.8F);
    this.h += 1;
    this.i += 1;
    submitParameter();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lives\TuSDKLiveOldTVFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */