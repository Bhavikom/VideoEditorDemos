// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveMegrimFilter;
//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.tusdk.FilterManager;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterManager;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveMegrimFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

public class TuSdkMediaSceneEffectData extends TuSdkMediaEffectData
{
    private String a;
    
    public TuSdkMediaSceneEffectData(final String a) {
        this.a = a;
        this.setMediaEffectType(TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeScene);
        this.setVaild(FilterManager.shared().isSceneEffectFilter(a));
        if (!this.isVaild()) {
            TLog.e("Invalid scene effect code \uff1a%s", new Object[] { a });
        }
    }
    
    public final String getEffectCode() {
        return this.a;
    }
    
    @Override
    public TuSdkMediaEffectData clone() {
        final TuSdkMediaSceneEffectData tuSdkMediaSceneEffectData = new TuSdkMediaSceneEffectData(this.a);
        tuSdkMediaSceneEffectData.setAtTimeRange(this.getAtTimeRange());
        tuSdkMediaSceneEffectData.setVaild(true);
        tuSdkMediaSceneEffectData.setMediaEffectType(this.getMediaEffectType());
        tuSdkMediaSceneEffectData.setIsApplied(false);
        return tuSdkMediaSceneEffectData;
    }
    
    @Override
    public synchronized FilterWrap getFilterWrap() {
        if (this.mFilterWrap == null) {
            (this.mFilterWrap = FilterWrap.creat(FilterLocalPackage.shared().option(this.a))).processImage();
            if (this.mFilterWrap.getFilter() instanceof TuSDKLiveMegrimFilter) {
                ((TuSDKLiveMegrimFilter)this.mFilterWrap.getFilter()).enableSeprarate();
            }
        }
        return this.mFilterWrap;
    }
}
