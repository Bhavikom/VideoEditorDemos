// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.seles.tusdk.TuSDKSkinMoistWrap;
//import org.lasque.tusdk.core.seles.tusdk.TuSDKSkinNaturalWrap;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.TuSDKSkinMoistWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.TuSDKSkinNaturalWrap;

public final class TuSdkMediaSkinFaceEffect extends TuSdkMediaEffectData
{
    private boolean a;
    
    public TuSdkMediaSkinFaceEffect(final boolean a) {
        this.a = a;
        this.setMediaEffectType(TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSkinFace);
        this.setVaild(true);
    }
    
    @Override
    public synchronized FilterWrap getFilterWrap() {
        if (this.mFilterWrap == null) {
            if (this.a) {
                this.mFilterWrap = (FilterWrap)new TuSDKSkinNaturalWrap();
            }
            else {
                this.mFilterWrap = (FilterWrap)new TuSDKSkinMoistWrap();
            }
            this.mFilterWrap.processImage();
        }
        return this.mFilterWrap;
    }
    
    @Override
    public TuSdkMediaEffectData clone() {
        if (!this.isVaild()) {
            return null;
        }
        final TuSdkMediaSkinFaceEffect tuSdkMediaSkinFaceEffect = new TuSdkMediaSkinFaceEffect(this.a);
        tuSdkMediaSkinFaceEffect.setAtTimeRange(this.getAtTimeRange());
        tuSdkMediaSkinFaceEffect.setIsApplied(false);
        if (tuSdkMediaSkinFaceEffect.getFilterWrap() != null && this.mFilterWrap != null) {
            tuSdkMediaSkinFaceEffect.getFilterWrap().getFilterParameter().syncArgs(this.mFilterWrap.getFilterParameter());
        }
        return tuSdkMediaSkinFaceEffect;
    }
}
