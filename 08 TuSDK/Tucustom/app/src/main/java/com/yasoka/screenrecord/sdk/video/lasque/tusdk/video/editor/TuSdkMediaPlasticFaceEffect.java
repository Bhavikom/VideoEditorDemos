// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.seles.tusdk.TuSDKPlasticFaceWrap;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.TuSDKPlasticFaceWrap;

public class TuSdkMediaPlasticFaceEffect extends TuSdkMediaEffectData
{
    public TuSdkMediaPlasticFaceEffect() {
        this.setMediaEffectType(TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypePlasticFace);
        this.setVaild(true);
    }
    
    @Override
    public synchronized FilterWrap getFilterWrap() {
        if (this.mFilterWrap == null) {
            (this.mFilterWrap = (FilterWrap)new TuSDKPlasticFaceWrap()).processImage();
        }
        return this.mFilterWrap;
    }
    
    @Override
    public TuSdkMediaEffectData clone() {
        if (!this.isVaild()) {
            return null;
        }
        final TuSdkMediaPlasticFaceEffect tuSdkMediaPlasticFaceEffect = new TuSdkMediaPlasticFaceEffect();
        tuSdkMediaPlasticFaceEffect.setAtTimeRange(this.getAtTimeRange());
        tuSdkMediaPlasticFaceEffect.setVaild(true);
        tuSdkMediaPlasticFaceEffect.setMediaEffectType(this.getMediaEffectType());
        tuSdkMediaPlasticFaceEffect.setIsApplied(false);
        tuSdkMediaPlasticFaceEffect.mFilterWrap = this.getFilterWrap().clone();
        if (tuSdkMediaPlasticFaceEffect.getFilterWrap() != null && this.mFilterWrap != null) {
            tuSdkMediaPlasticFaceEffect.getFilterWrap().getFilter().setParameter(this.mFilterWrap.getFilter().getParameter());
        }
        return tuSdkMediaPlasticFaceEffect;
    }
    
    public void setEyeSize(final float n) {
        this.submitParameter("eyeSize", n);
    }
    
    public void setChinSize(final float n) {
        this.submitParameter("chinSize", n);
    }
    
    public void setNoseSize(final float n) {
        this.submitParameter("noseSize", n);
    }
    
    public void setMouthWidth(final float n) {
        this.submitParameter("mouthWidth", n);
    }
    
    public void setArchEyebrow(final float n) {
        this.submitParameter("archEyebrow", n);
    }
    
    public void setEyeDis(final float n) {
        this.submitParameter("eyeDis", n);
    }
    
    public void setEyeAngle(final float n) {
        this.submitParameter("eyeAngle", n);
    }
    
    public void setJawSize(final float n) {
        this.submitParameter("jawSize", n);
    }
}
