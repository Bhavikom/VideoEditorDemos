// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base;

import android.graphics.Color;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;

public class TuSDKSelectiveFilter extends SelesTwoInputFilter
{
    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;
    private PointF i;
    private float j;
    private float k;
    private float l;
    private int m;
    private float n;
    private float o;
    private float p;
    
    public TuSDKSelectiveFilter() {
        super("-sb2");
        this.i = new PointF(0.5f, 0.5f);
        this.j = 0.4f;
        this.k = 0.2f;
        this.m = -1;
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.a = this.mFilterProgram.uniformIndex("radius");
        this.b = this.mFilterProgram.uniformIndex("center");
        this.c = this.mFilterProgram.uniformIndex("aspectRatio");
        this.d = this.mFilterProgram.uniformIndex("excessive");
        this.e = this.mFilterProgram.uniformIndex("maskAlpha");
        this.f = this.mFilterProgram.uniformIndex("maskColor");
        this.g = this.mFilterProgram.uniformIndex("degree");
        this.h = this.mFilterProgram.uniformIndex("selective");
        this.setRadius(this.j);
        this.setCenter(this.i);
        this.setExcessive(this.k);
        this.setMaskColor(this.m);
        this.setMaskAlpha(this.l);
        this.setDegree(this.n);
        this.setSelective(this.o);
        this.a();
    }
    
    @Override
    public void setInputSize(final TuSdkSize tuSdkSize, final int n) {
        final TuSdkSize copy = this.mInputTextureSize.copy();
        super.setInputSize(tuSdkSize, n);
        if (!copy.equals(this.mInputTextureSize) && tuSdkSize.isSize()) {
            this.a();
        }
    }
    
    private void a() {
        if (!this.mInputTextureSize.isSize() || this.mInputRotation == null) {
            return;
        }
        if (this.mInputRotation.isTransposed()) {
            this.a(this.mInputTextureSize.width / (float)this.mInputTextureSize.height);
        }
        else {
            this.a(this.mInputTextureSize.height / (float)this.mInputTextureSize.width);
        }
    }
    
    @Override
    public void setInputRotation(final ImageOrientation imageOrientation, final int n) {
        super.setInputRotation(imageOrientation, n);
        this.setCenter(this.getCenter());
        this.a();
    }
    
    @Override
    public void forceProcessingAtSize(final TuSdkSize tuSdkSize) {
        super.forceProcessingAtSize(tuSdkSize);
        this.a();
    }
    
    private void a(final float p) {
        this.p = p;
        if (this.p > 0.0f) {
            this.setFloat(this.p, this.c, this.mFilterProgram);
        }
    }
    
    public PointF getCenter() {
        return this.i;
    }
    
    public void setCenter(final PointF i) {
        this.i = i;
        this.setPoint(this.rotatedPoint(this.i, this.mInputRotation), this.b, this.mFilterProgram);
    }
    
    public float getRadius() {
        return this.j;
    }
    
    public void setRadius(final float j) {
        this.setFloat(this.j = j, this.a, this.mFilterProgram);
    }
    
    public float getExcessive() {
        return this.k;
    }
    
    public void setExcessive(final float k) {
        this.setFloat(this.k = k, this.d, this.mFilterProgram);
    }
    
    public int getMaskColor() {
        return this.m;
    }
    
    public void setMaskColor(final int m) {
        this.m = m;
        this.setVec3(new float[] { Color.red(m) / 255.0f, Color.green(m) / 255.0f, Color.blue(m) / 255.0f }, this.f, this.mFilterProgram);
    }
    
    public float getMaskAlpha() {
        return this.l;
    }
    
    public void setMaskAlpha(final float l) {
        this.setFloat(this.l = l, this.e, this.mFilterProgram);
    }
    
    public float getDegree() {
        return this.n;
    }
    
    public void setDegree(final float n) {
        this.setFloat(this.n = n, this.g, this.mFilterProgram);
    }
    
    public float getSelective() {
        return this.o;
    }
    
    public void setSelective(final float o) {
        this.setFloat(this.o = o, this.h, this.mFilterProgram);
    }
}
