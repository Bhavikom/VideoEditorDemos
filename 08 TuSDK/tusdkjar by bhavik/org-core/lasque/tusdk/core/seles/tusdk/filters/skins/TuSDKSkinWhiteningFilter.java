package org.lasque.tusdk.core.seles.tusdk.filters.skins;

import android.graphics.PointF;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterFacePositionInterface;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.SelesParameters.FilterTexturesInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSurfaceBlurFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorMixedFilter;

public class TuSDKSkinWhiteningFilter
  extends SelesFilterGroup
  implements SelesParameters.FilterFacePositionInterface, SelesParameters.FilterParameterInterface, SelesParameters.FilterTexturesInterface
{
  private float a;
  private float b;
  private float c;
  private float d;
  private float e;
  private float f = 0.0F;
  private TuSDKColorMixedFilter g = new TuSDKColorMixedFilter();
  private TuSDKSurfaceBlurFilter h;
  private SelesFilter i;
  private _TuSDKSkinWhiteningFilter j;
  private _TuSDKGPUFaceBeautyFilter k;
  private boolean l;
  
  public TuSDKSkinWhiteningFilter()
  {
    addFilter(this.g);
    this.h = new TuSDKSurfaceBlurFilter();
    this.h.setScale(0.5F);
    addFilter(this.h);
    this.i = new SelesFilter();
    this.i.setScale(0.5F);
    addFilter(this.i);
    this.j = new _TuSDKSkinWhiteningFilter();
    addFilter(this.j);
    this.k = new _TuSDKGPUFaceBeautyFilter();
    addFilter(this.k);
    this.j.addTarget(this.g, 0);
    this.h.addTarget(this.j, 1);
    this.i.addTarget(this.j, 2);
    this.g.addTarget(this.k, 0);
    setInitialFilters(new SelesOutInput[] { this.h, this.i, this.j });
    setTerminalFilter(this.k);
    setSmoothing(0.3F);
    setWhitening(0.3F);
    setSkinColor(5000.0F);
    setEyeEnlargeSize(1.045F);
    setChinSize(0.048F);
    setRetouchSize(1.0F);
    this.h.setBlurSize(this.h.getMaxBlursize());
    this.h.setThresholdLevel(this.h.getMaxThresholdLevel());
    this.j.setLightLevel(0.4F);
    this.j.setDetailLevel(0.2F);
  }
  
  public float getSmoothing()
  {
    return this.a;
  }
  
  public void setSmoothing(float paramFloat)
  {
    this.a = paramFloat;
    this.j.setIntensity(1.0F - paramFloat);
  }
  
  public float getWhitening()
  {
    return this.b;
  }
  
  public void setWhitening(float paramFloat)
  {
    this.b = paramFloat;
    this.g.setMixed(this.b);
  }
  
  public float getSkinColor()
  {
    return this.c;
  }
  
  public void setSkinColor(float paramFloat)
  {
    this.c = paramFloat;
    this.j.setTemperature(paramFloat);
  }
  
  public void setEyeEnlargeSize(float paramFloat)
  {
    this.d = paramFloat;
    this.k.setEyeEnlargeSize(paramFloat);
  }
  
  public float getEyeEnlargeSize()
  {
    return this.d;
  }
  
  public void setChinSize(float paramFloat)
  {
    this.e = paramFloat;
    this.k.setChinSize(paramFloat);
  }
  
  public float getChinSize()
  {
    return this.e;
  }
  
  public float getRetouchSize()
  {
    return this.f;
  }
  
  public void setRetouchSize(float paramFloat)
  {
    SelesParameters localSelesParameters = getParameter();
    this.f = paramFloat;
    setSmoothing(paramFloat);
    setWhitening(paramFloat);
    localSelesParameters.setFilterArg("retouchSize", paramFloat);
    localSelesParameters.setFilterArg("smoothing", paramFloat);
    localSelesParameters.setFilterArg("whitening", paramFloat);
    float f1 = 0.20000005F * paramFloat + 1.0F;
    setEyeEnlargeSize(f1);
    localSelesParameters.setFilterArg("eyeSize", (f1 - 1.0F) / 0.2F);
    float f2 = 0.1F * paramFloat;
    setChinSize(f2);
    localSelesParameters.setFilterArg("chinSize", f2 / 0.1F);
  }
  
  public void updateFaceFeatures(FaceAligment[] paramArrayOfFaceAligment, float paramFloat)
  {
    PointF[] arrayOfPointF = null;
    if ((paramArrayOfFaceAligment != null) && (paramArrayOfFaceAligment.length > 0)) {
      arrayOfPointF = paramArrayOfFaceAligment[0].getMarks();
    }
    if ((this.k != null) && (arrayOfPointF != null))
    {
      PointF localPointF1 = arrayOfPointF[0];
      PointF localPointF2 = arrayOfPointF[1];
      PointF localPointF4 = arrayOfPointF[3];
      PointF localPointF5 = arrayOfPointF[4];
      PointF localPointF3 = new PointF((localPointF4.x + localPointF5.x) / 2.0F, (localPointF4.y + localPointF5.y) / 2.0F);
      this.k.setFacePositions(localPointF1, localPointF2, localPointF3);
    }
  }
  
  public void resetPosition()
  {
    if (this.k != null) {
      this.k.resetPosition();
    }
  }
  
  public void appendTextures(List<SelesPicture> paramList)
  {
    if (paramList == null) {
      return;
    }
    int m = 1;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      SelesPicture localSelesPicture = (SelesPicture)localIterator.next();
      localSelesPicture.processImage();
      localSelesPicture.addTarget(this.g, m);
      m++;
    }
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("retouchSize", getRetouchSize(), 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("smoothing", getSmoothing(), 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("whitening", getWhitening());
    paramSelesParameters.appendFloatArg("skinColor", getSkinColor(), 4000.0F, 6000.0F);
    paramSelesParameters.appendFloatArg("eyeSize", getEyeEnlargeSize(), 1.0F, 1.2F);
    paramSelesParameters.appendFloatArg("chinSize", getChinSize(), 0.0F, 0.1F);
    return paramSelesParameters;
  }
  
  public void setParameter(SelesParameters paramSelesParameters)
  {
    this.l = true;
    super.setParameter(paramSelesParameters);
    this.l = false;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("smoothing")) {
      setSmoothing(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("whitening")) {
      setWhitening(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("skinColor")) {
      setSkinColor(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("eyeSize")) {
      setEyeEnlargeSize(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("chinSize")) {
      setChinSize(paramFilterArg.getValue());
    } else if ((paramFilterArg.equalsKey("retouchSize")) && (!this.l)) {
      setRetouchSize(paramFilterArg.getValue());
    }
  }
  
  private static class _TuSDKGPUFaceBeautyFilter
    extends SelesFilter
  {
    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    private float f = 1.05F;
    private float g = 0.048F;
    private PointF h = new PointF(0.0F, 0.0F);
    private PointF i = new PointF(0.0F, 0.0F);
    private PointF j = new PointF(0.0F, 0.0F);
    
    public _TuSDKGPUFaceBeautyFilter()
    {
      super();
    }
    
    protected void onInitOnGLThread()
    {
      super.onInitOnGLThread();
      this.a = this.mFilterProgram.uniformIndex("eyePower");
      this.b = this.mFilterProgram.uniformIndex("chinPower");
      this.c = this.mFilterProgram.uniformIndex("leftEyeCoordinate");
      this.d = this.mFilterProgram.uniformIndex("rightEyeCoordinate");
      this.e = this.mFilterProgram.uniformIndex("mouthCoordinate");
      setFacePositions(this.h, this.i, this.j);
      setEyeEnlargeSize(this.f);
      setChinSize(this.g);
    }
    
    public void setFacePositions(PointF paramPointF1, PointF paramPointF2, PointF paramPointF3)
    {
      this.h = paramPointF1;
      this.i = paramPointF2;
      this.j = paramPointF3;
      setPoint(paramPointF1, this.c, this.mFilterProgram);
      setPoint(paramPointF2, this.d, this.mFilterProgram);
      setPoint(paramPointF3, this.e, this.mFilterProgram);
    }
    
    public void resetPosition()
    {
      setFacePositions(new PointF(0.0F, 0.0F), new PointF(0.0F, 0.0F), new PointF(0.0F, 0.0F));
    }
    
    public void setEyeEnlargeSize(float paramFloat)
    {
      this.f = paramFloat;
      setFloat(this.f, this.a, this.mFilterProgram);
    }
    
    public void setChinSize(float paramFloat)
    {
      this.g = paramFloat;
      setFloat(this.g, this.b, this.mFilterProgram);
    }
  }
  
  private static class _TuSDKSkinWhiteningFilter
    extends SelesThreeInputFilter
  {
    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    private float f = 1.0F;
    private float g = 5000.0F;
    private float h = 0.22F;
    private float i = 0.7F;
    
    public _TuSDKSkinWhiteningFilter()
    {
      super();
    }
    
    protected void onInitOnGLThread()
    {
      super.onInitOnGLThread();
      this.a = this.mFilterProgram.uniformIndex("intensity");
      this.b = this.mFilterProgram.uniformIndex("temperature");
      this.c = this.mFilterProgram.uniformIndex("enableSkinColorDetection");
      this.d = this.mFilterProgram.uniformIndex("lightLevel");
      this.e = this.mFilterProgram.uniformIndex("detailLevel");
      setIntensity(this.f);
      setTemperature(this.g);
      setEnableSkinColorDetection(0.0F);
      setLightLevel(this.h);
      setDetailLevel(this.i);
    }
    
    public void setIntensity(float paramFloat)
    {
      this.f = paramFloat;
      setFloat(paramFloat, this.a, this.mFilterProgram);
    }
    
    public void setTemperature(float paramFloat)
    {
      this.g = paramFloat;
      setFloat(paramFloat < 5000.0F ? (float)(4.0E-4D * (paramFloat - 5000.0D)) : (float)(6.0E-5D * (paramFloat - 5000.0D)), this.b, this.mFilterProgram);
    }
    
    public void setEnableSkinColorDetection(float paramFloat)
    {
      setFloat(paramFloat, this.c, this.mFilterProgram);
    }
    
    public void setLightLevel(float paramFloat)
    {
      this.h = paramFloat;
      setFloat(this.h, this.d, this.mFilterProgram);
    }
    
    public void setDetailLevel(float paramFloat)
    {
      this.i = paramFloat;
      setFloat(this.i, this.e, this.mFilterProgram);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\skins\TuSDKSkinWhiteningFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */