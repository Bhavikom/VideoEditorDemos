// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.particle.TuSDKParticleFilter;
//import org.lasque.tusdk.core.utils.TLog;
import android.text.TextUtils;
import java.text.DecimalFormat;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.particle.TuSDKParticleFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.concurrent.ConcurrentHashMap;

public class TuSdkMediaParticleEffectData extends TuSdkMediaEffectData
{
    private String a;
    private ConcurrentHashMap<Long, PointF> b;
    private float c;
    private int d;
    private DecimalFormat e;
    
    public TuSdkMediaParticleEffectData(final String a) {
        this.b = new ConcurrentHashMap<Long, PointF>(10);
        this.e = new DecimalFormat(".00");
        this.a = a;
        this.setMediaEffectType(TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeParticle);
        this.setVaild(!TextUtils.isEmpty((CharSequence)a));
        if (!this.isVaild()) {
            TLog.e("Invalid particle effect code \uff1a%s", new Object[] { a });
        }
    }
    
    public TuSdkMediaParticleEffectData(final String s, final TuSdkTimeRange atTimeRange) {
        this(s);
        this.setAtTimeRange(atTimeRange);
    }
    
    public void setSize(final float c) {
        this.c = c;
    }
    
    public float getSize() {
        return this.c;
    }
    
    public void setColor(final int d) {
        this.d = d;
    }
    
    public int getColor() {
        return this.d;
    }
    
    public String getParticleCode() {
        return this.a;
    }
    
    public void putPoint(final long n, final PointF value) {
        this.b.put(n / 1000L, value);
    }
    
    public PointF getPointF(final long n) {
        final PointF pointF = this.b.get(n / 1000L);
        if (pointF != null) {
            return pointF;
        }
        for (final Long key : this.b.keySet()) {
            if (Float.valueOf(this.e.format(key / 1000.0f)) == (float)Float.valueOf(this.e.format(n / 1000.0f / 1000.0f))) {
                return this.b.get(key);
            }
        }
        return null;
    }
    
    public void clearPoints() {
        this.b.clear();
    }
    
    @Override
    public TuSdkMediaEffectData clone() {
        final TuSdkMediaParticleEffectData tuSdkMediaParticleEffectData = new TuSdkMediaParticleEffectData(this.a);
        tuSdkMediaParticleEffectData.setColor(this.d);
        tuSdkMediaParticleEffectData.setSize(this.c);
        tuSdkMediaParticleEffectData.b = new ConcurrentHashMap<Long, PointF>(this.b);
        tuSdkMediaParticleEffectData.setAtTimeRange(this.getAtTimeRange());
        tuSdkMediaParticleEffectData.setVaild(true);
        tuSdkMediaParticleEffectData.setMediaEffectType(this.getMediaEffectType());
        tuSdkMediaParticleEffectData.setIsApplied(false);
        return tuSdkMediaParticleEffectData;
    }
    
    public void resetParticleFilter() {
        if (this.mFilterWrap == null || this.mFilterWrap.getFilter() == null) {
            return;
        }
        final SelesOutInput filter = this.mFilterWrap.getFilter();
        if (filter instanceof TuSDKParticleFilter) {
            ((TuSDKParticleFilter)filter).reset();
        }
    }
    
    @Override
    public synchronized FilterWrap getFilterWrap() {
        if (this.mFilterWrap == null) {
            (this.mFilterWrap = FilterWrap.creat(FilterLocalPackage.shared().option(this.a))).setParticleColor(this.getColor());
            this.mFilterWrap.setParticleSize(this.getSize());
            this.mFilterWrap.processImage();
        }
        return this.mFilterWrap;
    }
}
