package org.lasque.tusdk.core.seles.tusdk.filters.skins;

import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSurfaceBlurFilter;

public class TuSDKSkinMoistFilter
  extends SelesFilterGroup
  implements SelesParameters.FilterParameterInterface
{
  private static String a = "precision highp float;varying vec2 textureCoordinate;varying vec2 textureCoordinate2;uniform sampler2D inputImageTexture;uniform sampler2D inputImageTexture2;uniform float uIntensity;uniform float uFair;uniform float uRuddy;uniform float uLight;uniform float uDetail;float handleHardLight(float color){     if(color > 0.5){          color = 1.0 - pow(1.0 - color, 2.0) * 2.0;     }else{          color = color * color * 2.0;     }     return color;}vec3 handleHardLight3(vec3 color){     return vec3(handleHardLight(color.r), handleHardLight(color.g), handleHardLight(color.b));}const vec3 luminanceWeight = vec3(0.299,0.587,0.114);highp vec3 handleLightDarkBlend(highp vec3 base, highp vec3 overlayer){     vec3 highPass = base - overlayer + 0.5;     highPass = handleHardLight3(handleHardLight3(handleHardLight3(handleHardLight3(highPass))));     float lumance = dot(base, luminanceWeight);     vec3 smoothColor = base + (base - highPass) * lumance * uDetail;     smoothColor = clamp(smoothColor,0.0,1.0);     return smoothColor;}void main(){     vec3 sharpColor = texture2D(inputImageTexture, textureCoordinate).rgb;     vec3 surfaceColor = texture2D(inputImageTexture2, textureCoordinate2).rgb;     surfaceColor = handleLightDarkBlend(sharpColor, surfaceColor);     surfaceColor = mix(sharpColor, surfaceColor, uIntensity);     float dark = dot(surfaceColor, luminanceWeight);     float gb = 1.0 - 0.05 * uRuddy;     surfaceColor = mix(surfaceColor, vec3(1.0, gb, gb), dark * max(uRuddy, uFair));     gl_FragColor = vec4(surfaceColor, 1.0);}";
  private float b;
  private float c;
  private float d;
  private float e;
  private float f;
  private float g;
  private float h;
  private TuSDKSurfaceBlurFilter i = new TuSDKSurfaceBlurFilter();
  private _TuSDKSkinMoistMixFilter j;
  
  public TuSDKSkinMoistFilter()
  {
    this.i.setScale(0.8F);
    this.j = new _TuSDKSkinMoistMixFilter();
    addFilter(this.j);
    this.i.addTarget(this.j, 1);
    setInitialFilters(new SelesOutInput[] { this.i, this.j });
    setTerminalFilter(this.j);
    setSmoothing(0.8F);
    b(this.i.getMaxBlursize());
    c(this.i.getMaxThresholdLevel());
    setFair(0.0F);
    a(0.0F);
    d(1.0F);
    e(0.18F);
  }
  
  private float a()
  {
    return this.b;
  }
  
  public void setSmoothing(float paramFloat)
  {
    this.b = paramFloat;
    this.j.setIntensity(paramFloat);
  }
  
  private float b()
  {
    return this.c;
  }
  
  public void setFair(float paramFloat)
  {
    this.c = paramFloat;
    this.j.setFair(paramFloat);
  }
  
  private float c()
  {
    return this.d;
  }
  
  private void a(float paramFloat)
  {
    this.d = paramFloat;
    this.j.setRuddy(paramFloat);
  }
  
  private float d()
  {
    return this.e;
  }
  
  private void b(float paramFloat)
  {
    this.e = paramFloat;
    this.i.setBlurSize(paramFloat);
  }
  
  private float e()
  {
    return this.f;
  }
  
  private void c(float paramFloat)
  {
    this.f = paramFloat;
    this.i.setThresholdLevel(paramFloat);
  }
  
  private float f()
  {
    return this.g;
  }
  
  private void d(float paramFloat)
  {
    this.g = paramFloat;
    this.j.setLight(paramFloat);
  }
  
  private float g()
  {
    return this.h;
  }
  
  private void e(float paramFloat)
  {
    this.h = paramFloat;
    this.j.setDetail(paramFloat);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("smoothing", a());
    paramSelesParameters.appendFloatArg("whitening", b());
    paramSelesParameters.appendFloatArg("ruddy", c());
    float f1 = paramSelesParameters.getDefaultArg("debug");
    if (f1 > 0.0F)
    {
      paramSelesParameters.appendFloatArg("blurSize", d(), 0.0F, this.i.getMaxBlursize());
      paramSelesParameters.appendFloatArg("thresholdLevel", e(), 0.0F, this.i.getMaxThresholdLevel());
      paramSelesParameters.appendFloatArg("lightLevel", f());
      paramSelesParameters.appendFloatArg("detailLevel", g());
    }
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("smoothing")) {
      setSmoothing(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("whitening")) {
      setFair(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("ruddy")) {
      a(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("blurSize")) {
      b(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("thresholdLevel")) {
      c(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("lightLevel")) {
      d(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("detailLevel")) {
      e(paramFilterArg.getValue());
    }
  }
  
  private class _TuSDKSkinMoistMixFilter
    extends SelesTwoInputFilter
  {
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private float g = 1.0F;
    private float h = 0.0F;
    private float i = 0.0F;
    private float j = 1.0F;
    private float k = 0.18F;
    
    public _TuSDKSkinMoistMixFilter()
    {
      super();
    }
    
    protected void onInitOnGLThread()
    {
      super.onInitOnGLThread();
      this.b = this.mFilterProgram.uniformIndex("uIntensity");
      this.c = this.mFilterProgram.uniformIndex("uFair");
      this.d = this.mFilterProgram.uniformIndex("uRuddy");
      this.e = this.mFilterProgram.uniformIndex("uLight");
      this.f = this.mFilterProgram.uniformIndex("uDetail");
      setIntensity(this.g);
      setFair(this.h);
      setRuddy(this.i);
      setLight(this.j);
      setDetail(this.k);
    }
    
    public void setIntensity(float paramFloat)
    {
      this.g = paramFloat;
      setFloat(paramFloat, this.b, this.mFilterProgram);
    }
    
    public void setFair(float paramFloat)
    {
      this.h = paramFloat;
      setFloat(paramFloat, this.c, this.mFilterProgram);
    }
    
    public void setRuddy(float paramFloat)
    {
      this.i = paramFloat;
      setFloat(paramFloat, this.d, this.mFilterProgram);
    }
    
    public void setLight(float paramFloat)
    {
      this.j = paramFloat;
      setFloat(paramFloat, this.e, this.mFilterProgram);
    }
    
    public void setDetail(float paramFloat)
    {
      this.k = paramFloat;
      setFloat(paramFloat, this.f, this.mFilterProgram);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\skins\TuSDKSkinMoistFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */