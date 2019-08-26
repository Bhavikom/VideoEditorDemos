// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.seles.tusdk.TuSDKMonsterFaceWrap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.TuSDKMonsterFaceWrap;

public class TuSDKMediaMonsterFaceEffect extends TuSdkMediaEffectData
{
    private TuSDKMonsterFaceWrap.TuSDKMonsterFaceType a;
    
    public TuSDKMediaMonsterFaceEffect(final TuSDKMonsterFaceWrap.TuSDKMonsterFaceType a) {
        this.setMediaEffectType(TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeMonsterFace);
        this.a = a;
        this.setVaild(true);
    }
    
    private TuSDKMediaMonsterFaceEffect() {
    }
    
    @Override
    public TuSdkMediaEffectData clone() {
        return new TuSDKMediaMonsterFaceEffect(this.a);
    }
    
    @Override
    public synchronized FilterWrap getFilterWrap() {
        if (this.mFilterWrap == null) {
            (this.mFilterWrap = (FilterWrap)new TuSDKMonsterFaceWrap(this.a)).processImage();
        }
        return this.mFilterWrap;
    }
}
