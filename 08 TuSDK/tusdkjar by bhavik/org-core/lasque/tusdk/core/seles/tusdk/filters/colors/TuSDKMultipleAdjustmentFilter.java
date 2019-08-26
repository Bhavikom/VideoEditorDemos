package org.lasque.tusdk.core.seles.tusdk.filters.colors;

import android.graphics.Color;
import android.graphics.PointF;
import android.opengl.GLES20;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSDKMultipleAdjustmentFilter
  extends SelesFilter
  implements SelesParameters.FilterParameterInterface
{
  private float a = 0.0F;
  private float b = 1.0F;
  private float c = 1.0F;
  private float d = 0.0F;
  private float e = 0.0F;
  private float f = 1.0F;
  private float g = 5000.0F;
  private int h;
  private int i;
  private int j;
  private int k;
  private int l;
  private int m;
  private int n;
  private int o;
  private PointF p = new PointF(0.5F, 0.5F);
  private int q;
  private float[] r = { 0.0F, 0.0F, 0.0F };
  private int s;
  private float t = 1.0F;
  private int u;
  private float v = 1.0F;
  private int w;
  private int x;
  private int y;
  private float z = 0.0F;
  
  public TuSDKMultipleAdjustmentFilter()
  {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nuniform float imageWidthFactor; \nuniform float imageHeightFactor; \nuniform float sharpness;\n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate; \nvarying vec2 topTextureCoordinate;\nvarying vec2 bottomTextureCoordinate;\n\nvarying float centerMultiplier;\nvarying float edgeMultiplier;\n\nvoid main()\n{\n    gl_Position = position;\n    \n    mediump vec2 widthStep = vec2(imageWidthFactor, 0.0);\n    mediump vec2 heightStep = vec2(0.0, imageHeightFactor);\n    \n    textureCoordinate = inputTextureCoordinate.xy;\n    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n    topTextureCoordinate = inputTextureCoordinate.xy + heightStep;     \n    bottomTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n    \n    centerMultiplier = 1.0 + 4.0 * sharpness;\n    edgeMultiplier = sharpness;\n}", " precision highp float; varying highp vec2 textureCoordinate; uniform sampler2D inputImageTexture; uniform lowp float brightness; uniform lowp float contrast; uniform lowp float saturation; uniform lowp float exposure; uniform lowp float shadows; uniform lowp float highlights; uniform lowp float temperature;uniform lowp vec2 vignetteCenter;uniform lowp vec3 vignetteColor;uniform highp float vignetteStart;uniform highp float vignetteEnd; const highp vec3 weightHighlightsShadows = vec3(0.3, 0.3, 0.3); const highp vec3 weightSaturation = vec3(0.2125, 0.7154, 0.0721); const highp vec3 warmFilter = vec3(0.93, 0.54, 0.0); const highp mat3 RGBtoYIQ = mat3(0.299, 0.587, 0.114, 0.596, -0.274, -0.322, 0.212, -0.523, 0.311); const highp mat3 YIQtoRGB = mat3(1.0, 0.956, 0.621, 1.0, -0.272, -0.647, 1.0, -1.105, 1.702); highp vec3 handleBrightness(highp vec3 color) {     if(brightness == 0.0) return color;     return (color + vec3(brightness)); } highp vec3 handleContrast(highp vec3 color){    if(contrast == 1.0) return color;    return (color - vec3(0.5)) * contrast + vec3(0.5);} highp vec3 handleSaturation(highp vec3 color){    if(saturation == 1.0) return color;        highp float luminance = dot(color, weightSaturation);    return mix(vec3(luminance), color, saturation);} highp vec3 handleExposure(highp vec3 color) {     if(exposure == 0.0) return color;     return color * pow(2.0, exposure); } highp vec3 handleHighlightsShadows(highp vec3 color) {     if(shadows == 0.0 && highlights == 1.0) return color;          highp float luminance = dot(color, weightHighlightsShadows);          highp float shadow = clamp((pow(luminance, 1.0/(shadows+1.0)) + (-0.76)*pow(luminance, 2.0/(shadows+1.0))) - luminance, 0.0, 1.0);          highp float highlight = clamp((1.0 - (pow(1.0-luminance, 1.0/(2.0-highlights)) + (-0.8)*pow(1.0-luminance, 2.0/(2.0-highlights)))) - luminance, -1.0, 0.0);     highp vec3 result = vec3(0.0, 0.0, 0.0);     if(luminance > 0.0) result = result + ((luminance + shadow + highlight) - 0.0) * ((color - result)/(luminance - 0.0));          return result; } highp vec3 handleWhiteBalance(highp vec3 color) {     if(temperature == 0.0) return color;          highp vec3 yiq = RGBtoYIQ * color;     yiq.b = clamp(yiq.b, -0.5226, 0.5226);     highp vec3 rgb = YIQtoRGB * yiq;          highp vec3 processed = vec3(                                 (rgb.r < 0.5 ? (2.0 * rgb.r * warmFilter.r) : (1.0 - 2.0 * (1.0 - rgb.r) * (1.0 - warmFilter.r))),                                 (rgb.g < 0.5 ? (2.0 * rgb.g * warmFilter.g) : (1.0 - 2.0 * (1.0 - rgb.g) * (1.0 - warmFilter.g))),                                 (rgb.b < 0.5 ? (2.0 * rgb.b * warmFilter.b) : (1.0 - 2.0 * (1.0 - rgb.b) * (1.0 - warmFilter.b))));          return mix(rgb, processed, temperature); }highp vec3 handleVignette(highp vec3 textureColor){     float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));     float percent = smoothstep(vignetteStart, vignetteEnd, d);     return vec3(mix(textureColor.rgb, vignetteColor, percent));}\n\n\nvarying highp vec2 leftTextureCoordinate;\nvarying highp vec2 rightTextureCoordinate; \nvarying highp vec2 topTextureCoordinate;\nvarying highp vec2 bottomTextureCoordinate;\n\nvarying highp float centerMultiplier;\nvarying highp float edgeMultiplier;\n\n\nhighp vec4 handleSharpen(highp vec3 textureColor)\n{\n    mediump vec3 leftTextureColor = texture2D(inputImageTexture, leftTextureCoordinate).rgb;\n    mediump vec3 rightTextureColor = texture2D(inputImageTexture, rightTextureCoordinate).rgb;\n    mediump vec3 topTextureColor = texture2D(inputImageTexture, topTextureCoordinate).rgb;\n    mediump vec3 bottomTextureColor = texture2D(inputImageTexture, bottomTextureCoordinate).rgb;\n\n    return vec4((textureColor * centerMultiplier - (leftTextureColor * edgeMultiplier + rightTextureColor * edgeMultiplier + topTextureColor * edgeMultiplier + bottomTextureColor * edgeMultiplier)), texture2D(inputImageTexture, bottomTextureCoordinate).w);\n}\n\n void main(){    highp vec3 textureColor = texture2D(inputImageTexture, textureCoordinate).rgb;     \t textureColor = handleSharpen(textureColor).rgb;    textureColor = handleVignette(textureColor);    textureColor = handleHighlightsShadows(textureColor);    textureColor = handleBrightness(textureColor);    textureColor = handleContrast(textureColor);    textureColor = handleSaturation(textureColor);    textureColor = handleExposure(textureColor);    textureColor = handleWhiteBalance(textureColor);    gl_FragColor = vec4(textureColor,1.0);}");
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.h = this.mFilterProgram.uniformIndex("brightness");
    this.i = this.mFilterProgram.uniformIndex("contrast");
    this.j = this.mFilterProgram.uniformIndex("saturation");
    this.k = this.mFilterProgram.uniformIndex("exposure");
    this.l = this.mFilterProgram.uniformIndex("shadows");
    this.m = this.mFilterProgram.uniformIndex("highlights");
    this.n = this.mFilterProgram.uniformIndex("temperature");
    a(this.a);
    b(this.b);
    c(this.c);
    d(this.d);
    e(this.e);
    f(this.f);
    g(this.g);
    initLightVignetteFilter();
    initSharpnessFilter();
  }
  
  private float a()
  {
    return this.a;
  }
  
  private void a(float paramFloat)
  {
    this.a = paramFloat;
    setFloat(this.a, this.h, this.mFilterProgram);
  }
  
  private float b()
  {
    return this.b;
  }
  
  private void b(float paramFloat)
  {
    this.b = paramFloat;
    setFloat(this.b, this.i, this.mFilterProgram);
  }
  
  private float c()
  {
    return this.c;
  }
  
  private void c(float paramFloat)
  {
    this.c = paramFloat;
    setFloat(this.c, this.j, this.mFilterProgram);
  }
  
  private float d()
  {
    return this.d;
  }
  
  private void d(float paramFloat)
  {
    this.d = paramFloat;
    setFloat(this.d, this.k, this.mFilterProgram);
  }
  
  private float e()
  {
    return this.e;
  }
  
  private void e(float paramFloat)
  {
    this.e = paramFloat;
    setFloat(this.e, this.l, this.mFilterProgram);
  }
  
  private float f()
  {
    return this.f;
  }
  
  private void f(float paramFloat)
  {
    this.f = paramFloat;
    setFloat(this.f, this.m, this.mFilterProgram);
  }
  
  private float g()
  {
    return this.g;
  }
  
  private void g(float paramFloat)
  {
    this.g = paramFloat;
    setFloat(paramFloat < 5000.0F ? (float)(4.0E-4D * (paramFloat - 5000.0D)) : (float)(6.0E-5D * (paramFloat - 5000.0D)), this.n, this.mFilterProgram);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("brightness", a(), -0.4F, 0.5F);
    paramSelesParameters.appendFloatArg("contrast", b(), 0.6F, 1.8F);
    paramSelesParameters.appendFloatArg("saturation", c(), 0.0F, 2.0F);
    paramSelesParameters.appendFloatArg("exposure", d(), -2.5F, 2.0F);
    paramSelesParameters.appendFloatArg("shadows", e());
    paramSelesParameters.appendFloatArg("highlights", f());
    paramSelesParameters.appendFloatArg("temperature", g(), 3500.0F, 7500.0F);
    paramSelesParameters.appendFloatArg("vignette", this.t, 1.0F, 0.0F);
    paramSelesParameters.appendFloatArg("sharpness", getSharpness(), -1.0F, 1.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("brightness")) {
      a(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("contrast")) {
      b(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("saturation")) {
      c(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("exposure")) {
      d(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("shadows")) {
      e(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("highlights")) {
      f(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("temperature")) {
      g(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("vignette")) {
      h(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("sharpness")) {
      setSharpness(paramFilterArg.getValue());
    }
  }
  
  protected void initLightVignetteFilter()
  {
    this.o = this.mFilterProgram.uniformIndex("vignetteCenter");
    this.q = this.mFilterProgram.uniformIndex("vignetteColor");
    this.s = this.mFilterProgram.uniformIndex("vignetteStart");
    this.u = this.mFilterProgram.uniformIndex("vignetteEnd");
    a(this.p);
    a(this.r);
    h(this.t);
    i(this.v);
  }
  
  private void a(PointF paramPointF)
  {
    this.p = paramPointF;
    setPoint(this.p, this.o, this.mFilterProgram);
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
    this.r = paramArrayOfFloat;
    setVec3(this.r, this.q, this.mFilterProgram);
  }
  
  private void h(float paramFloat)
  {
    this.t = paramFloat;
    setFloat(this.t, this.s, this.mFilterProgram);
  }
  
  private void i(float paramFloat)
  {
    this.v = paramFloat;
    setFloat(this.v, this.u, this.mFilterProgram);
  }
  
  protected void initSharpnessFilter()
  {
    this.w = this.mFilterProgram.uniformIndex("sharpness");
    this.x = this.mFilterProgram.uniformIndex("imageWidthFactor");
    this.y = this.mFilterProgram.uniformIndex("imageHeightFactor");
    setSharpness(this.z);
  }
  
  public void setSharpness(float paramFloat)
  {
    this.z = paramFloat;
    setFloat(this.z, this.w, this.mFilterProgram);
  }
  
  public float getSharpness()
  {
    return this.z;
  }
  
  public void setupFilterForSize(final TuSdkSize paramTuSdkSize)
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        if (TuSDKMultipleAdjustmentFilter.a(TuSDKMultipleAdjustmentFilter.this).isTransposed())
        {
          GLES20.glUniform1f(TuSDKMultipleAdjustmentFilter.b(TuSDKMultipleAdjustmentFilter.this), 1.0F / paramTuSdkSize.height);
          GLES20.glUniform1f(TuSDKMultipleAdjustmentFilter.c(TuSDKMultipleAdjustmentFilter.this), 1.0F / paramTuSdkSize.width);
        }
        else
        {
          GLES20.glUniform1f(TuSDKMultipleAdjustmentFilter.b(TuSDKMultipleAdjustmentFilter.this), 1.0F / paramTuSdkSize.width);
          GLES20.glUniform1f(TuSDKMultipleAdjustmentFilter.c(TuSDKMultipleAdjustmentFilter.this), 1.0F / paramTuSdkSize.height);
        }
      }
    });
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\colors\TuSDKMultipleAdjustmentFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */