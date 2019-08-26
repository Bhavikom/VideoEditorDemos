// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives;

//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;

public class TuSDKLiveFancy01Filter extends SelesTwoInputFilter
{
    private int h;
    private int i;
    private int j;
    private int k;
    private int l;
    private float[] m;
    private PointF n;
    private float[] o;
    private float p;
    private float[] q;
    private float r;
    int a;
    long b;
    long c;
    int d;
    int e;
    boolean f;
    boolean g;
    
    public TuSDKLiveFancy01Filter() {
        super("-slive05f");
        this.m = new float[] { 0.0f, 0.0f, 0.0f };
        this.n = new PointF(0.0f, 0.0f);
        this.o = new float[] { 0.1f, 0.2f, 0.0f, 0.0f };
        this.p = 0.4f;
        this.q = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
        this.r = 1.0f;
        this.a = 0;
        this.b = -50L;
        this.c = -1L;
        this.disableSecondFrameCheck();
    }
    
    public TuSDKLiveFancy01Filter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null) {
            float n = 0.1f;
            final float n2 = 0.1f;
            if (filterOption.args.containsKey("flutterX")) {
                final float float1 = Float.parseFloat(filterOption.args.get("flutterX"));
                if (float1 > 0.0f) {
                    n = float1;
                }
            }
            if (filterOption.args.containsKey("flutterX")) {
                final float float2 = Float.parseFloat(filterOption.args.get("flutterX"));
                if (float2 > 0.0f) {
                    n = float2;
                }
            }
            this.setFlutter(new float[] { n, n2, 0.0f, 0.0f });
            float n3 = 0.0f;
            float n4 = 0.0f;
            if (filterOption.args.containsKey("noiseX")) {
                final float float3 = Float.parseFloat(filterOption.args.get("noiseX"));
                if (float3 > 0.0f) {
                    n3 = float3;
                }
            }
            if (filterOption.args.containsKey("noiseY")) {
                final float float4 = Float.parseFloat(filterOption.args.get("noiseY"));
                if (float4 > 0.0f) {
                    n4 = float4;
                }
            }
            this.setNoise(new float[] { n3, n4, 0.0f, 0.0f });
            if (filterOption.args.containsKey("animation")) {
                final float float5 = Float.parseFloat(filterOption.args.get("animation"));
                if (float5 > 0.0f) {
                    this.setAnimation(float5);
                }
            }
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.h = this.mFilterProgram.uniformIndex("split");
        this.i = this.mFilterProgram.uniformIndex("curve");
        this.j = this.mFilterProgram.uniformIndex("flutter");
        this.k = this.mFilterProgram.uniformIndex("mixturePercent");
        this.l = this.mFilterProgram.uniformIndex("noise");
        this.setSplit(this.m);
        this.setCurve(this.n);
        this.setFlutter(this.o);
        this.setLineMixed(this.p);
        this.setNoise(this.q);
    }
    
    @Override
    public void setInputRotation(final ImageOrientation imageOrientation, final int n) {
        super.setInputRotation(imageOrientation, n);
        if (n == 0 && imageOrientation != null) {
            this.setSplitRotat(imageOrientation.isTransposed() ? 1.0f : 0.0f);
        }
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long n) {
        this.a(System.currentTimeMillis());
        super.informTargetsAboutNewFrame(n);
    }
    
    public float[] getSplit() {
        return this.m;
    }
    
    public void setSplit(final float[] m) {
        this.m = m;
    }
    
    public void setSplitX(final float n) {
        this.getSplit()[0] = n;
    }
    
    public void setSplitY(final float n) {
        this.getSplit()[1] = n;
    }
    
    public void setSplitRotat(final float n) {
        final float[] split = this.getSplit();
        split[2] = n;
        this.setSplit(split);
    }
    
    public PointF getCurve() {
        return this.n;
    }
    
    public void setCurve(final PointF n) {
        this.setPoint(this.n = n, this.i, this.mFilterProgram);
    }
    
    public void setCurveStrength(final float x) {
        final PointF curve = this.getCurve();
        curve.x = x;
        this.setCurve(curve);
    }
    
    public void setCurveTone(final float y) {
        final PointF curve = this.getCurve();
        curve.y = y;
        this.setCurve(curve);
    }
    
    public float[] getFlutter() {
        return this.o;
    }
    
    public void setFlutter(final float[] o) {
        this.setVec4(this.o = o, this.j, this.mFilterProgram);
    }
    
    public void setFlutterX(final float n) {
        final float[] flutter = this.getFlutter();
        flutter[0] = n;
        this.setFlutter(flutter);
    }
    
    public void setFlutterY(final float n) {
        final float[] flutter = this.getFlutter();
        flutter[1] = n;
        this.setFlutter(flutter);
    }
    
    public void setFlutterStrength(final float n) {
        final float[] flutter = this.getFlutter();
        flutter[2] = n;
        this.setFlutter(flutter);
    }
    
    public void setFlutterMixed(final float n) {
        final float[] flutter = this.getFlutter();
        flutter[3] = n;
        this.setFlutter(flutter);
    }
    
    public float getLineMixed() {
        return this.p;
    }
    
    public void setLineMixed(final float p) {
        this.setFloat(this.p = p, this.k, this.mFilterProgram);
    }
    
    public float[] getNoise() {
        return this.q;
    }
    
    public void setNoise(final float[] q) {
        this.setVec4(this.q = q, this.l, this.mFilterProgram);
    }
    
    public void setNoiseX(final float n) {
        final float[] noise = this.getNoise();
        noise[0] = n;
        this.setNoise(noise);
    }
    
    public void setNoiseY(final float n) {
        final float[] noise = this.getNoise();
        noise[1] = n;
        this.setNoise(noise);
    }
    
    public void setNoiseType(final float n) {
        final float[] noise = this.getNoise();
        noise[2] = n;
        this.setNoise(noise);
    }
    
    public void setNoiseMixed(final float n) {
        final float[] noise = this.getNoise();
        noise[3] = n;
        this.setNoise(noise);
    }
    
    public float getAnimation() {
        return this.r;
    }
    
    public void setAnimation(final float r) {
        this.r = r;
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("splitX", this.getSplit()[0], -1.0f, 1.0f);
        initParams.appendFloatArg("splitY", this.getSplit()[1], -1.0f, 1.0f);
        initParams.appendFloatArg("curveStrength", this.getCurve().x, -2.0f, 2.0f);
        initParams.appendFloatArg("curveTone", this.getCurve().y, 0.0f, 1.0f);
        initParams.appendFloatArg("flutterX", this.getFlutter()[0], -1.0f, 1.0f);
        initParams.appendFloatArg("flutterY", this.getFlutter()[1], -1.0f, 1.0f);
        initParams.appendFloatArg("flutterStrength", this.getFlutter()[2], 0.0f, 1.0f);
        initParams.appendFloatArg("flutterMixed", this.getFlutter()[3], 0.0f, 1.0f);
        initParams.appendFloatArg("lineMixed", this.getLineMixed(), 0.0f, 1.0f);
        initParams.appendFloatArg("noiseX", this.getNoise()[0], -1.0f, 1.0f);
        initParams.appendFloatArg("noiseY", this.getNoise()[1], -1.0f, 1.0f);
        initParams.appendFloatArg("noiseType", this.getNoise()[2], 0.0f, 1.0f);
        initParams.appendFloatArg("noiseMixed", this.getNoise()[3], 0.0f, 1.0f);
        initParams.appendFloatArg("animation", this.getAnimation(), 0.0f, 1.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("splitX")) {
            this.setSplitX(filterArg.getValue());
        }
        else if (filterArg.equalsKey("splitY")) {
            this.setSplitY(filterArg.getValue());
        }
        else if (filterArg.equalsKey("curveStrength")) {
            this.setCurveStrength(filterArg.getValue());
        }
        else if (filterArg.equalsKey("curveTone")) {
            this.setCurveTone(filterArg.getValue());
        }
        else if (filterArg.equalsKey("flutterX")) {
            this.setFlutterX(filterArg.getValue());
        }
        else if (filterArg.equalsKey("flutterY")) {
            this.setFlutterY(filterArg.getValue());
        }
        else if (filterArg.equalsKey("flutterStrength")) {
            this.setFlutterStrength(filterArg.getValue());
        }
        else if (filterArg.equalsKey("flutterMixed")) {
            this.setFlutterMixed(filterArg.getValue());
        }
        else if (filterArg.equalsKey("lineMixed")) {
            this.setLineMixed(filterArg.getValue());
        }
        else if (filterArg.equalsKey("noiseX")) {
            this.setNoiseX(filterArg.getValue());
        }
        else if (filterArg.equalsKey("noiseY")) {
            this.setNoiseY(filterArg.getValue());
        }
        else if (filterArg.equalsKey("noiseType")) {
            this.setNoiseType(filterArg.getValue());
        }
        else if (filterArg.equalsKey("noiseMixed")) {
            this.setNoiseMixed(filterArg.getValue());
        }
        else if (filterArg.equalsKey("animation")) {
            this.setAnimation(filterArg.getValue());
        }
    }
    
    private void a(final long n) {
        if (this.getAnimation() < 0.5) {
            return;
        }
        long n2 = n / 50L;
        if (this.c == -1L) {
            this.c = n2;
        }
        else {
            n2 -= this.c;
        }
        if (n2 == this.b) {
            return;
        }
        this.b = n2;
        final int[] array = { 0, 2, 10, 12, 18, 20, 60, 64, 94, 101, 103, 105, 107, 109, 129, 131, 149, 165, 169, 189, 193, 223, 235, 249, 253, 273 };
        final float[] array2 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.04f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
        final float[] array3 = { 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.4f, 0.3f, 0.2f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f };
        final float[] array4 = { 0.0f, 0.0025f, 0.02f, -0.07f, 0.02f, 5.0E-4f, 0.1f, 6.7E-4f, 0.0027f, 0.02f, 0.1f, 0.3f, 0.1f, 0.001f, 0.3f, 0.001f, 0.0125f, 0.1f, 0.001f, 0.0f, 6.7E-4f, 0.002f, 0.00125f, 0.13f, 0.001f };
        final float[] array5 = { 0.6f, 0.49f, 0.49f, 0.7f, 0.49f, 0.49f, 0.5f, 0.49f, 0.49f, 0.49f, 0.5f, 0.5f, 0.5f, 0.49f, 0.5f, 0.49f, 0.49f, 0.5f, 0.49f, 0.6f, 0.49f, 0.49f, 0.49f, 0.5f, 0.49f };
        final float[] array6 = { 0.0f, 0.0025f, 0.2f, 0.04f, 0.2f, 0.005f, 0.0f, 0.0065f, 0.027f, 0.2f, 0.2f, 0.0f, 0.0f, 0.02f, 0.2f, 0.02f, 0.0f, 0.0f, 0.1f, 0.0f, 0.1f, 0.0f, 0.0125f, 0.067f, 0.01f };
        final float[] array7 = { 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, 0.7f, 0.4f, 0.4f, 0.4f, 0.4f, 0.3f, 0.3f, 0.4f, 0.4f, 0.4f, 0.5f, 0.5f, 0.4f, 0.6f, 0.4f, 0.5f, 0.4f, 0.4f, 0.4f };
        final float[] array8 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.025f, -0.16f, 0.0f, 0.0f, 0.0f, 0.03f, 0.0f, 0.0f, -0.02f };
        final float[] array9 = { 0.5f, 0.5f, 0.6f, 0.6f, 0.6f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f };
        final float[] array10 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.025f, 0.16f, 0.0f, 0.0f, 0.0f, 0.03f, 0.0f, 0.0f, 0.02f };
        final float[] array11 = { 0.5f, 0.5f, 0.4f, 0.4f, 0.4f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f };
        final float[] array12 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.05f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
        final float[] array13 = { 0.0f, 0.0f, 0.1f, 0.8f, 0.8f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.4f, 0.0f, 0.6f };
        final float[] array14 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.05f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.03f };
        final float[] array15 = { 0.0f, 0.0f, 0.2f, 0.7f, 0.7f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.6f, 0.0f, 0.0f, 0.0f, 0.4f, 0.0f, 0.0f };
        final float[] array16 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0013f, 0.0067f, 0.03f, 0.0f, 0.03f, 0.003f, 0.03f, 0.03f, 0.03f };
        final float[] array17 = { 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.49f, 0.49f, 0.4f, 0.0f, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f };
        final float[] array18 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0013f, 0.0067f, 0.03f, 0.0f, 0.03f, 0.003f, 0.03f, 0.03f, 0.03f };
        final float[] array19 = { 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.49f, 0.49f, 0.5f, 0.0f, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f };
        final float[] array20 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.6f, 0.0f, 0.0f, 0.0f, 0.3f, 0.6f, 0.6f, 0.6f, 0.6f, 0.0f, 0.0f, 0.0f, 0.6f, 0.15f, 0.15f, 0.45f, 0.45f, 0.6f, 0.6f, 0.15f, 0.6f };
        final float[] array21 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.6f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.6f, 0.6f, 0.0f, 0.0f, 0.0f, 0.0f, 0.08f, 0.03f, 0.0f, 0.0f, 0.03f, 0.0f, 0.08f, 0.0f };
        final float[] array22 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.3f, 0.0f, 0.0f, 0.0f, 0.2f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.01f, 0.3f, 0.3f, 0.0f, 0.3f, 0.3f, 0.0f, 0.3f, 0.3f };
        if (this.d == 0 || this.d >= array[array.length - 1] - 1) {
            this.d = 0;
            this.a = 0;
            this.b = -1L;
            this.getParameter().setFilterArg("splitX", 0.5f);
            this.getParameter().setFilterArg("lineMixed", 0.2f);
        }
        ++this.d;
        if (this.d >= array[this.a + 1] || this.a == 0) {
            if (this.d != 1) {
                ++this.a;
            }
            this.f = false;
            this.g = false;
            this.e = 0;
            this.getParameter().setFilterArg("splitY", array3[this.a]);
            this.getParameter().setFilterArg("curveStrength", array5[this.a]);
            this.getParameter().setFilterArg("flutterX", array9[this.a]);
            this.getParameter().setFilterArg("flutterY", array11[this.a]);
            this.getParameter().setFilterArg("flutterStrength", array13[this.a]);
            this.getParameter().setFilterArg("curveTone", array7[this.a]);
            this.getParameter().setFilterArg("flutterMixed", array15[this.a]);
            this.getParameter().setFilterArg("noiseX", array17[this.a]);
            this.getParameter().setFilterArg("noiseY", array19[this.a]);
            this.getParameter().setFilterArg("noiseType", array20[this.a]);
            this.getParameter().setFilterArg("noiseMixed", array22[this.a]);
        }
        else {
            ++this.e;
            this.getParameter().setFilterArg("splitY", array2[this.a] * this.e + array3[this.a]);
            this.getParameter().setFilterArg("curveStrength", array4[this.a] * this.e + array5[this.a]);
            this.getParameter().setFilterArg("curveTone", array6[this.a] * this.e + array7[this.a]);
            final float n3 = array8[this.a];
            if (n3 != 0.0f) {
                float n4;
                if (this.f) {
                    n4 = n3 * -1.0f * this.e / 2.0f + 0.3f;
                }
                else {
                    n4 = n3 * this.e + array9[this.a];
                }
                this.getParameter().setFilterArg("flutterX", n4);
                if (n4 <= 0.3) {
                    this.f = true;
                }
            }
            final float n5 = array10[this.a];
            if (n5 != 0.0f) {
                float n6;
                if (this.g) {
                    n6 = n5 * -1.0f * this.e / 2.0f + 0.7f;
                }
                else {
                    n6 = n5 * this.e + array11[this.a];
                }
                this.getParameter().setFilterArg("flutterY", n6);
                if (n6 >= 0.7 && (this.d < 218 || this.d > 228)) {
                    this.g = true;
                }
            }
            this.getParameter().setFilterArg("noiseX", array16[this.a] * this.e + array17[this.a]);
            this.getParameter().setFilterArg("flutterStrength", array12[this.a] * this.e + array13[this.a]);
            this.getParameter().setFilterArg("flutterMixed", array14[this.a] * this.e + array15[this.a]);
            this.getParameter().setFilterArg("noiseY", array18[this.a] * this.e + array19[this.a]);
            this.getParameter().setFilterArg("noiseMixed", array21[this.a] * this.e + array22[this.a]);
        }
        this.submitParameter();
    }
}
