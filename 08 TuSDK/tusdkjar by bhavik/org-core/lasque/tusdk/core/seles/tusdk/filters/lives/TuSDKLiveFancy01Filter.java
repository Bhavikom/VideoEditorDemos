package org.lasque.tusdk.core.seles.tusdk.filters.lives;

import android.graphics.PointF;
import java.util.HashMap;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSDKLiveFancy01Filter
  extends SelesTwoInputFilter
{
  private int h;
  private int i;
  private int j;
  private int k;
  private int l;
  private float[] m = { 0.0F, 0.0F, 0.0F };
  private PointF n = new PointF(0.0F, 0.0F);
  private float[] o = { 0.1F, 0.2F, 0.0F, 0.0F };
  private float p = 0.4F;
  private float[] q = { 0.0F, 0.0F, 0.0F, 0.0F };
  private float r = 1.0F;
  int a = 0;
  long b = -50L;
  long c = -1L;
  int d;
  int e;
  boolean f;
  boolean g;
  
  public TuSDKLiveFancy01Filter()
  {
    super("-slive05f");
    disableSecondFrameCheck();
  }
  
  public TuSDKLiveFancy01Filter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null))
    {
      float f1 = 0.1F;
      float f2 = 0.1F;
      if (paramFilterOption.args.containsKey("flutterX"))
      {
        f3 = Float.parseFloat((String)paramFilterOption.args.get("flutterX"));
        if (f3 > 0.0F) {
          f1 = f3;
        }
      }
      if (paramFilterOption.args.containsKey("flutterX"))
      {
        f3 = Float.parseFloat((String)paramFilterOption.args.get("flutterX"));
        if (f3 > 0.0F) {
          f1 = f3;
        }
      }
      setFlutter(new float[] { f1, f2, 0.0F, 0.0F });
      float f3 = 0.0F;
      float f4 = 0.0F;
      float f5;
      if (paramFilterOption.args.containsKey("noiseX"))
      {
        f5 = Float.parseFloat((String)paramFilterOption.args.get("noiseX"));
        if (f5 > 0.0F) {
          f3 = f5;
        }
      }
      if (paramFilterOption.args.containsKey("noiseY"))
      {
        f5 = Float.parseFloat((String)paramFilterOption.args.get("noiseY"));
        if (f5 > 0.0F) {
          f4 = f5;
        }
      }
      setNoise(new float[] { f3, f4, 0.0F, 0.0F });
      if (paramFilterOption.args.containsKey("animation"))
      {
        f5 = Float.parseFloat((String)paramFilterOption.args.get("animation"));
        if (f5 > 0.0F) {
          setAnimation(f5);
        }
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.h = this.mFilterProgram.uniformIndex("split");
    this.i = this.mFilterProgram.uniformIndex("curve");
    this.j = this.mFilterProgram.uniformIndex("flutter");
    this.k = this.mFilterProgram.uniformIndex("mixturePercent");
    this.l = this.mFilterProgram.uniformIndex("noise");
    setSplit(this.m);
    setCurve(this.n);
    setFlutter(this.o);
    setLineMixed(this.p);
    setNoise(this.q);
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    super.setInputRotation(paramImageOrientation, paramInt);
    if ((paramInt == 0) && (paramImageOrientation != null)) {
      setSplitRotat(paramImageOrientation.isTransposed() ? 1.0F : 0.0F);
    }
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    a(System.currentTimeMillis());
    super.informTargetsAboutNewFrame(paramLong);
  }
  
  public float[] getSplit()
  {
    return this.m;
  }
  
  public void setSplit(float[] paramArrayOfFloat)
  {
    this.m = paramArrayOfFloat;
  }
  
  public void setSplitX(float paramFloat)
  {
    float[] arrayOfFloat = getSplit();
    arrayOfFloat[0] = paramFloat;
  }
  
  public void setSplitY(float paramFloat)
  {
    float[] arrayOfFloat = getSplit();
    arrayOfFloat[1] = paramFloat;
  }
  
  public void setSplitRotat(float paramFloat)
  {
    float[] arrayOfFloat = getSplit();
    arrayOfFloat[2] = paramFloat;
    setSplit(arrayOfFloat);
  }
  
  public PointF getCurve()
  {
    return this.n;
  }
  
  public void setCurve(PointF paramPointF)
  {
    this.n = paramPointF;
    setPoint(this.n, this.i, this.mFilterProgram);
  }
  
  public void setCurveStrength(float paramFloat)
  {
    PointF localPointF = getCurve();
    localPointF.x = paramFloat;
    setCurve(localPointF);
  }
  
  public void setCurveTone(float paramFloat)
  {
    PointF localPointF = getCurve();
    localPointF.y = paramFloat;
    setCurve(localPointF);
  }
  
  public float[] getFlutter()
  {
    return this.o;
  }
  
  public void setFlutter(float[] paramArrayOfFloat)
  {
    this.o = paramArrayOfFloat;
    setVec4(this.o, this.j, this.mFilterProgram);
  }
  
  public void setFlutterX(float paramFloat)
  {
    float[] arrayOfFloat = getFlutter();
    arrayOfFloat[0] = paramFloat;
    setFlutter(arrayOfFloat);
  }
  
  public void setFlutterY(float paramFloat)
  {
    float[] arrayOfFloat = getFlutter();
    arrayOfFloat[1] = paramFloat;
    setFlutter(arrayOfFloat);
  }
  
  public void setFlutterStrength(float paramFloat)
  {
    float[] arrayOfFloat = getFlutter();
    arrayOfFloat[2] = paramFloat;
    setFlutter(arrayOfFloat);
  }
  
  public void setFlutterMixed(float paramFloat)
  {
    float[] arrayOfFloat = getFlutter();
    arrayOfFloat[3] = paramFloat;
    setFlutter(arrayOfFloat);
  }
  
  public float getLineMixed()
  {
    return this.p;
  }
  
  public void setLineMixed(float paramFloat)
  {
    this.p = paramFloat;
    setFloat(this.p, this.k, this.mFilterProgram);
  }
  
  public float[] getNoise()
  {
    return this.q;
  }
  
  public void setNoise(float[] paramArrayOfFloat)
  {
    this.q = paramArrayOfFloat;
    setVec4(this.q, this.l, this.mFilterProgram);
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
    return this.r;
  }
  
  public void setAnimation(float paramFloat)
  {
    this.r = paramFloat;
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("splitX", getSplit()[0], -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("splitY", getSplit()[1], -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("curveStrength", getCurve().x, -2.0F, 2.0F);
    paramSelesParameters.appendFloatArg("curveTone", getCurve().y, 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("flutterX", getFlutter()[0], -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("flutterY", getFlutter()[1], -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("flutterStrength", getFlutter()[2], 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("flutterMixed", getFlutter()[3], 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("lineMixed", getLineMixed(), 0.0F, 1.0F);
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
    if (paramFilterArg.equalsKey("splitX")) {
      setSplitX(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("splitY")) {
      setSplitY(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("curveStrength")) {
      setCurveStrength(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("curveTone")) {
      setCurveTone(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("flutterX")) {
      setFlutterX(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("flutterY")) {
      setFlutterY(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("flutterStrength")) {
      setFlutterStrength(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("flutterMixed")) {
      setFlutterMixed(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("lineMixed")) {
      setLineMixed(paramFilterArg.getValue());
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
  
  private void a(long paramLong)
  {
    if (getAnimation() < 0.5D) {
      return;
    }
    long l1 = paramLong / 50L;
    if (this.c == -1L) {
      this.c = l1;
    } else {
      l1 -= this.c;
    }
    if (l1 == this.b) {
      return;
    }
    this.b = l1;
    int[] arrayOfInt = { 0, 2, 10, 12, 18, 20, 60, 64, 94, 101, 103, 105, 107, 109, 129, 131, 149, 165, 169, 189, 193, 223, 235, 249, 253, 273 };
    float[] arrayOfFloat1 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.04F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F };
    float[] arrayOfFloat2 = { 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.4F, 0.3F, 0.2F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F };
    float[] arrayOfFloat3 = { 0.0F, 0.0025F, 0.02F, -0.07F, 0.02F, 5.0E-4F, 0.1F, 6.7E-4F, 0.0027F, 0.02F, 0.1F, 0.3F, 0.1F, 0.001F, 0.3F, 0.001F, 0.0125F, 0.1F, 0.001F, 0.0F, 6.7E-4F, 0.002F, 0.00125F, 0.13F, 0.001F };
    float[] arrayOfFloat4 = { 0.6F, 0.49F, 0.49F, 0.7F, 0.49F, 0.49F, 0.5F, 0.49F, 0.49F, 0.49F, 0.5F, 0.5F, 0.5F, 0.49F, 0.5F, 0.49F, 0.49F, 0.5F, 0.49F, 0.6F, 0.49F, 0.49F, 0.49F, 0.5F, 0.49F };
    float[] arrayOfFloat5 = { 0.0F, 0.0025F, 0.2F, 0.04F, 0.2F, 0.005F, 0.0F, 0.0065F, 0.027F, 0.2F, 0.2F, 0.0F, 0.0F, 0.02F, 0.2F, 0.02F, 0.0F, 0.0F, 0.1F, 0.0F, 0.1F, 0.0F, 0.0125F, 0.067F, 0.01F };
    float[] arrayOfFloat6 = { 0.4F, 0.4F, 0.4F, 0.4F, 0.4F, 0.4F, 0.7F, 0.4F, 0.4F, 0.4F, 0.4F, 0.3F, 0.3F, 0.4F, 0.4F, 0.4F, 0.5F, 0.5F, 0.4F, 0.6F, 0.4F, 0.5F, 0.4F, 0.4F, 0.4F };
    float[] arrayOfFloat7 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.025F, -0.16F, 0.0F, 0.0F, 0.0F, 0.03F, 0.0F, 0.0F, -0.02F };
    float[] arrayOfFloat8 = { 0.5F, 0.5F, 0.6F, 0.6F, 0.6F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F };
    float[] arrayOfFloat9 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.025F, 0.16F, 0.0F, 0.0F, 0.0F, 0.03F, 0.0F, 0.0F, 0.02F };
    float[] arrayOfFloat10 = { 0.5F, 0.5F, 0.4F, 0.4F, 0.4F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F };
    float[] arrayOfFloat11 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.05F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F };
    float[] arrayOfFloat12 = { 0.0F, 0.0F, 0.1F, 0.8F, 0.8F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.4F, 0.0F, 0.6F };
    float[] arrayOfFloat13 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.05F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.03F };
    float[] arrayOfFloat14 = { 0.0F, 0.0F, 0.2F, 0.7F, 0.7F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6F, 0.0F, 0.0F, 0.0F, 0.4F, 0.0F, 0.0F };
    float[] arrayOfFloat15 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0013F, 0.0067F, 0.03F, 0.0F, 0.03F, 0.003F, 0.03F, 0.03F, 0.03F };
    float[] arrayOfFloat16 = { 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.49F, 0.49F, 0.4F, 0.0F, 0.4F, 0.4F, 0.4F, 0.4F, 0.4F };
    float[] arrayOfFloat17 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0013F, 0.0067F, 0.03F, 0.0F, 0.03F, 0.003F, 0.03F, 0.03F, 0.03F };
    float[] arrayOfFloat18 = { 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.49F, 0.49F, 0.5F, 0.0F, 0.4F, 0.4F, 0.4F, 0.4F, 0.4F };
    float[] arrayOfFloat19 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.6F, 0.0F, 0.0F, 0.0F, 0.3F, 0.6F, 0.6F, 0.6F, 0.6F, 0.0F, 0.0F, 0.0F, 0.6F, 0.15F, 0.15F, 0.45F, 0.45F, 0.6F, 0.6F, 0.15F, 0.6F };
    float[] arrayOfFloat20 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.6F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 0.6F, 0.6F, 0.0F, 0.0F, 0.0F, 0.0F, 0.08F, 0.03F, 0.0F, 0.0F, 0.03F, 0.0F, 0.08F, 0.0F };
    float[] arrayOfFloat21 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.3F, 0.0F, 0.0F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.01F, 0.3F, 0.3F, 0.0F, 0.3F, 0.3F, 0.0F, 0.3F, 0.3F };
    if ((this.d == 0) || (this.d >= arrayOfInt[(arrayOfInt.length - 1)] - 1))
    {
      this.d = 0;
      this.a = 0;
      this.b = -1L;
      getParameter().setFilterArg("splitX", 0.5F);
      getParameter().setFilterArg("lineMixed", 0.2F);
    }
    this.d += 1;
    int i1 = arrayOfInt[(this.a + 1)];
    if ((this.d >= i1) || (this.a == 0))
    {
      if (this.d != 1) {
        this.a += 1;
      }
      this.f = false;
      this.g = false;
      this.e = 0;
      getParameter().setFilterArg("splitY", arrayOfFloat2[this.a]);
      getParameter().setFilterArg("curveStrength", arrayOfFloat4[this.a]);
      getParameter().setFilterArg("flutterX", arrayOfFloat8[this.a]);
      getParameter().setFilterArg("flutterY", arrayOfFloat10[this.a]);
      getParameter().setFilterArg("flutterStrength", arrayOfFloat12[this.a]);
      getParameter().setFilterArg("curveTone", arrayOfFloat6[this.a]);
      getParameter().setFilterArg("flutterMixed", arrayOfFloat14[this.a]);
      getParameter().setFilterArg("noiseX", arrayOfFloat16[this.a]);
      getParameter().setFilterArg("noiseY", arrayOfFloat18[this.a]);
      getParameter().setFilterArg("noiseType", arrayOfFloat19[this.a]);
      getParameter().setFilterArg("noiseMixed", arrayOfFloat21[this.a]);
    }
    else
    {
      this.e += 1;
      getParameter().setFilterArg("splitY", arrayOfFloat1[this.a] * this.e + arrayOfFloat2[this.a]);
      getParameter().setFilterArg("curveStrength", arrayOfFloat3[this.a] * this.e + arrayOfFloat4[this.a]);
      getParameter().setFilterArg("curveTone", arrayOfFloat5[this.a] * this.e + arrayOfFloat6[this.a]);
      float f1 = arrayOfFloat7[this.a];
      if (f1 != 0.0F)
      {
        if (this.f)
        {
          f1 *= -1.0F;
          f2 = f1 * this.e / 2.0F + 0.3F;
        }
        else
        {
          f2 = f1 * this.e + arrayOfFloat8[this.a];
        }
        getParameter().setFilterArg("flutterX", f2);
        if (f2 <= 0.3D) {
          this.f = true;
        }
      }
      float f2 = arrayOfFloat9[this.a];
      if (f2 != 0.0F)
      {
        float f3;
        if (this.g)
        {
          f2 *= -1.0F;
          f3 = f2 * this.e / 2.0F + 0.7F;
        }
        else
        {
          f3 = f2 * this.e + arrayOfFloat10[this.a];
        }
        getParameter().setFilterArg("flutterY", f3);
        if ((f3 >= 0.7D) && ((this.d < 218) || (this.d > 228))) {
          this.g = true;
        }
      }
      getParameter().setFilterArg("noiseX", arrayOfFloat15[this.a] * this.e + arrayOfFloat16[this.a]);
      getParameter().setFilterArg("flutterStrength", arrayOfFloat11[this.a] * this.e + arrayOfFloat12[this.a]);
      getParameter().setFilterArg("flutterMixed", arrayOfFloat13[this.a] * this.e + arrayOfFloat14[this.a]);
      getParameter().setFilterArg("noiseY", arrayOfFloat17[this.a] * this.e + arrayOfFloat18[this.a]);
      getParameter().setFilterArg("noiseMixed", arrayOfFloat20[this.a] * this.e + arrayOfFloat21[this.a]);
    }
    submitParameter();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lives\TuSDKLiveFancy01Filter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */