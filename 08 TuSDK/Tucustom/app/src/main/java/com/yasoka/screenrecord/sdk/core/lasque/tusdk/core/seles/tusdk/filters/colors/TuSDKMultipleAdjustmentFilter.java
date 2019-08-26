// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors;

//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.Color;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKMultipleAdjustmentFilter extends SelesFilter implements SelesParameters.FilterParameterInterface
{
    private float a;
    private float b;
    private float c;
    private float d;
    private float e;
    private float f;
    private float g;
    private int h;
    private int i;
    private int j;
    private int k;
    private int l;
    private int m;
    private int n;
    private int o;
    private PointF p;
    private int q;
    private float[] r;
    private int s;
    private float t;
    private int u;
    private float v;
    private int w;
    private int x;
    private int y;
    private float z;
    
    public TuSDKMultipleAdjustmentFilter() {
        super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nuniform float imageWidthFactor; \nuniform float imageHeightFactor; \nuniform float sharpness;\n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate; \nvarying vec2 topTextureCoordinate;\nvarying vec2 bottomTextureCoordinate;\n\nvarying float centerMultiplier;\nvarying float edgeMultiplier;\n\nvoid main()\n{\n    gl_Position = position;\n    \n    mediump vec2 widthStep = vec2(imageWidthFactor, 0.0);\n    mediump vec2 heightStep = vec2(0.0, imageHeightFactor);\n    \n    textureCoordinate = inputTextureCoordinate.xy;\n    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n    topTextureCoordinate = inputTextureCoordinate.xy + heightStep;     \n    bottomTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n    \n    centerMultiplier = 1.0 + 4.0 * sharpness;\n    edgeMultiplier = sharpness;\n}", " precision highp float; varying highp vec2 textureCoordinate; uniform sampler2D inputImageTexture; uniform lowp float brightness; uniform lowp float contrast; uniform lowp float saturation; uniform lowp float exposure; uniform lowp float shadows; uniform lowp float highlights; uniform lowp float temperature;uniform lowp vec2 vignetteCenter;uniform lowp vec3 vignetteColor;uniform highp float vignetteStart;uniform highp float vignetteEnd; const highp vec3 weightHighlightsShadows = vec3(0.3, 0.3, 0.3); const highp vec3 weightSaturation = vec3(0.2125, 0.7154, 0.0721); const highp vec3 warmFilter = vec3(0.93, 0.54, 0.0); const highp mat3 RGBtoYIQ = mat3(0.299, 0.587, 0.114, 0.596, -0.274, -0.322, 0.212, -0.523, 0.311); const highp mat3 YIQtoRGB = mat3(1.0, 0.956, 0.621, 1.0, -0.272, -0.647, 1.0, -1.105, 1.702); highp vec3 handleBrightness(highp vec3 color) {     if(brightness == 0.0) return color;     return (color + vec3(brightness)); } highp vec3 handleContrast(highp vec3 color){    if(contrast == 1.0) return color;    return (color - vec3(0.5)) * contrast + vec3(0.5);} highp vec3 handleSaturation(highp vec3 color){    if(saturation == 1.0) return color;        highp float luminance = dot(color, weightSaturation);    return mix(vec3(luminance), color, saturation);} highp vec3 handleExposure(highp vec3 color) {     if(exposure == 0.0) return color;     return color * pow(2.0, exposure); } highp vec3 handleHighlightsShadows(highp vec3 color) {     if(shadows == 0.0 && highlights == 1.0) return color;          highp float luminance = dot(color, weightHighlightsShadows);          highp float shadow = clamp((pow(luminance, 1.0/(shadows+1.0)) + (-0.76)*pow(luminance, 2.0/(shadows+1.0))) - luminance, 0.0, 1.0);          highp float highlight = clamp((1.0 - (pow(1.0-luminance, 1.0/(2.0-highlights)) + (-0.8)*pow(1.0-luminance, 2.0/(2.0-highlights)))) - luminance, -1.0, 0.0);     highp vec3 result = vec3(0.0, 0.0, 0.0);     if(luminance > 0.0) result = result + ((luminance + shadow + highlight) - 0.0) * ((color - result)/(luminance - 0.0));          return result; } highp vec3 handleWhiteBalance(highp vec3 color) {     if(temperature == 0.0) return color;          highp vec3 yiq = RGBtoYIQ * color;     yiq.b = clamp(yiq.b, -0.5226, 0.5226);     highp vec3 rgb = YIQtoRGB * yiq;          highp vec3 processed = vec3(                                 (rgb.r < 0.5 ? (2.0 * rgb.r * warmFilter.r) : (1.0 - 2.0 * (1.0 - rgb.r) * (1.0 - warmFilter.r))),                                 (rgb.g < 0.5 ? (2.0 * rgb.g * warmFilter.g) : (1.0 - 2.0 * (1.0 - rgb.g) * (1.0 - warmFilter.g))),                                 (rgb.b < 0.5 ? (2.0 * rgb.b * warmFilter.b) : (1.0 - 2.0 * (1.0 - rgb.b) * (1.0 - warmFilter.b))));          return mix(rgb, processed, temperature); }highp vec3 handleVignette(highp vec3 textureColor){     float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));     float percent = smoothstep(vignetteStart, vignetteEnd, d);     return vec3(mix(textureColor.rgb, vignetteColor, percent));}\n\n\nvarying highp vec2 leftTextureCoordinate;\nvarying highp vec2 rightTextureCoordinate; \nvarying highp vec2 topTextureCoordinate;\nvarying highp vec2 bottomTextureCoordinate;\n\nvarying highp float centerMultiplier;\nvarying highp float edgeMultiplier;\n\n\nhighp vec4 handleSharpen(highp vec3 textureColor)\n{\n    mediump vec3 leftTextureColor = texture2D(inputImageTexture, leftTextureCoordinate).rgb;\n    mediump vec3 rightTextureColor = texture2D(inputImageTexture, rightTextureCoordinate).rgb;\n    mediump vec3 topTextureColor = texture2D(inputImageTexture, topTextureCoordinate).rgb;\n    mediump vec3 bottomTextureColor = texture2D(inputImageTexture, bottomTextureCoordinate).rgb;\n\n    return vec4((textureColor * centerMultiplier - (leftTextureColor * edgeMultiplier + rightTextureColor * edgeMultiplier + topTextureColor * edgeMultiplier + bottomTextureColor * edgeMultiplier)), texture2D(inputImageTexture, bottomTextureCoordinate).w);\n}\n\n void main(){    highp vec3 textureColor = texture2D(inputImageTexture, textureCoordinate).rgb;     \t textureColor = handleSharpen(textureColor).rgb;    textureColor = handleVignette(textureColor);    textureColor = handleHighlightsShadows(textureColor);    textureColor = handleBrightness(textureColor);    textureColor = handleContrast(textureColor);    textureColor = handleSaturation(textureColor);    textureColor = handleExposure(textureColor);    textureColor = handleWhiteBalance(textureColor);    gl_FragColor = vec4(textureColor,1.0);}");
        this.a = 0.0f;
        this.b = 1.0f;
        this.c = 1.0f;
        this.d = 0.0f;
        this.e = 0.0f;
        this.f = 1.0f;
        this.g = 5000.0f;
        this.p = new PointF(0.5f, 0.5f);
        this.r = new float[] { 0.0f, 0.0f, 0.0f };
        this.t = 1.0f;
        this.v = 1.0f;
        this.z = 0.0f;
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.h = this.mFilterProgram.uniformIndex("brightness");
        this.i = this.mFilterProgram.uniformIndex("contrast");
        this.j = this.mFilterProgram.uniformIndex("saturation");
        this.k = this.mFilterProgram.uniformIndex("exposure");
        this.l = this.mFilterProgram.uniformIndex("shadows");
        this.m = this.mFilterProgram.uniformIndex("highlights");
        this.n = this.mFilterProgram.uniformIndex("temperature");
        this.a(this.a);
        this.b(this.b);
        this.c(this.c);
        this.d(this.d);
        this.e(this.e);
        this.f(this.f);
        this.g(this.g);
        this.initLightVignetteFilter();
        this.initSharpnessFilter();
    }
    
    private float a() {
        return this.a;
    }
    
    private void a(final float a) {
        this.setFloat(this.a = a, this.h, this.mFilterProgram);
    }
    
    private float b() {
        return this.b;
    }
    
    private void b(final float b) {
        this.setFloat(this.b = b, this.i, this.mFilterProgram);
    }
    
    private float c() {
        return this.c;
    }
    
    private void c(final float c) {
        this.setFloat(this.c = c, this.j, this.mFilterProgram);
    }
    
    private float d() {
        return this.d;
    }
    
    private void d(final float d) {
        this.setFloat(this.d = d, this.k, this.mFilterProgram);
    }
    
    private float e() {
        return this.e;
    }
    
    private void e(final float e) {
        this.setFloat(this.e = e, this.l, this.mFilterProgram);
    }
    
    private float f() {
        return this.f;
    }
    
    private void f(final float f) {
        this.setFloat(this.f = f, this.m, this.mFilterProgram);
    }
    
    private float g() {
        return this.g;
    }
    
    private void g(final float g) {
        this.g = g;
        this.setFloat((g < 5000.0f) ? ((float)(4.0E-4 * (g - 5000.0))) : ((float)(6.0E-5 * (g - 5000.0))), this.n, this.mFilterProgram);
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("brightness", this.a(), -0.4f, 0.5f);
        initParams.appendFloatArg("contrast", this.b(), 0.6f, 1.8f);
        initParams.appendFloatArg("saturation", this.c(), 0.0f, 2.0f);
        initParams.appendFloatArg("exposure", this.d(), -2.5f, 2.0f);
        initParams.appendFloatArg("shadows", this.e());
        initParams.appendFloatArg("highlights", this.f());
        initParams.appendFloatArg("temperature", this.g(), 3500.0f, 7500.0f);
        initParams.appendFloatArg("vignette", this.t, 1.0f, 0.0f);
        initParams.appendFloatArg("sharpness", this.getSharpness(), -1.0f, 1.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("brightness")) {
            this.a(filterArg.getValue());
        }
        else if (filterArg.equalsKey("contrast")) {
            this.b(filterArg.getValue());
        }
        else if (filterArg.equalsKey("saturation")) {
            this.c(filterArg.getValue());
        }
        else if (filterArg.equalsKey("exposure")) {
            this.d(filterArg.getValue());
        }
        else if (filterArg.equalsKey("shadows")) {
            this.e(filterArg.getValue());
        }
        else if (filterArg.equalsKey("highlights")) {
            this.f(filterArg.getValue());
        }
        else if (filterArg.equalsKey("temperature")) {
            this.g(filterArg.getValue());
        }
        else if (filterArg.equalsKey("vignette")) {
            this.h(filterArg.getValue());
        }
        else if (filterArg.equalsKey("sharpness")) {
            this.setSharpness(filterArg.getValue());
        }
    }
    
    protected void initLightVignetteFilter() {
        this.o = this.mFilterProgram.uniformIndex("vignetteCenter");
        this.q = this.mFilterProgram.uniformIndex("vignetteColor");
        this.s = this.mFilterProgram.uniformIndex("vignetteStart");
        this.u = this.mFilterProgram.uniformIndex("vignetteEnd");
        this.a(this.p);
        this.a(this.r);
        this.h(this.t);
        this.i(this.v);
    }
    
    private void a(final PointF p) {
        this.setPoint(this.p = p, this.o, this.mFilterProgram);
    }
    
    public void setVignetteColor(final int n) {
        this.a(new float[] { Color.red(n) / 255.0f, Color.green(n) / 255.0f, Color.blue(n) / 255.0f });
    }
    
    private void a(final float[] r) {
        this.setVec3(this.r = r, this.q, this.mFilterProgram);
    }
    
    private void h(final float t) {
        this.setFloat(this.t = t, this.s, this.mFilterProgram);
    }
    
    private void i(final float v) {
        this.setFloat(this.v = v, this.u, this.mFilterProgram);
    }
    
    protected void initSharpnessFilter() {
        this.w = this.mFilterProgram.uniformIndex("sharpness");
        this.x = this.mFilterProgram.uniformIndex("imageWidthFactor");
        this.y = this.mFilterProgram.uniformIndex("imageHeightFactor");
        this.setSharpness(this.z);
    }
    
    public void setSharpness(final float z) {
        this.setFloat(this.z = z, this.w, this.mFilterProgram);
    }
    
    public float getSharpness() {
        return this.z;
    }
    
    @Override
    public void setupFilterForSize(final TuSdkSize tuSdkSize) {
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                if (TuSDKMultipleAdjustmentFilter.this.mInputRotation.isTransposed()) {
                    GLES20.glUniform1f(TuSDKMultipleAdjustmentFilter.this.x, 1.0f / tuSdkSize.height);
                    GLES20.glUniform1f(TuSDKMultipleAdjustmentFilter.this.y, 1.0f / tuSdkSize.width);
                }
                else {
                    GLES20.glUniform1f(TuSDKMultipleAdjustmentFilter.this.x, 1.0f / tuSdkSize.width);
                    GLES20.glUniform1f(TuSDKMultipleAdjustmentFilter.this.y, 1.0f / tuSdkSize.height);
                }
            }
        });
    }
}
