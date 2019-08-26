// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.ArrayList;
import java.util.List;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;

public abstract class TuSdkMediaEffectData implements TuSdkMediaEffectParameterInterface
{
    private boolean a;
    private boolean b;
    private TuSdkTimeRange c;
    private TuSdkMediaEffectDataType d;
    protected FilterWrap mFilterWrap;
    
    public TuSdkMediaEffectData() {
        this.a = false;
        this.b = false;
    }
    
    public TuSdkTimeRange getAtTimeRange() {
        return this.c;
    }
    
    public void setAtTimeRange(final TuSdkTimeRange c) {
        this.c = c;
    }
    
    public boolean validateTimeRange() {
        return this.getAtTimeRange() != null && this.getAtTimeRange().isValid();
    }
    
    protected final void setVaild(final boolean b) {
        this.b = b;
    }
    
    public final boolean isVaild() {
        return this.b;
    }
    
    public final boolean isApplied() {
        return this.a;
    }
    
    public void setIsApplied(final boolean a) {
        this.a = a;
    }
    
    public TuSdkMediaEffectDataType getMediaEffectType() {
        return this.d;
    }
    
    protected void setMediaEffectType(final TuSdkMediaEffectDataType d) {
        this.d = d;
    }
    
    public abstract TuSdkMediaEffectData clone();
    
    public abstract FilterWrap getFilterWrap();
    
    public void destory() {
        if (this.mFilterWrap == null) {
            return;
        }
        this.mFilterWrap.destroy();
        this.mFilterWrap = null;
    }
    
    private SelesParameters a() {
        if (this.getFilterWrap() == null) {
            return null;
        }
        return this.getFilterWrap().getFilterParameter();
    }
    
    @Override
    public final SelesParameters.FilterArg getFilterArg(final String s) {
        if (this.a() == null) {
            return null;
        }
        return this.a().getFilterArg(s);
    }
    
    @Override
    public final List<SelesParameters.FilterArg> getFilterArgs() {
        if (this.a() == null) {
            return new ArrayList<SelesParameters.FilterArg>();
        }
        return (List<SelesParameters.FilterArg>)this.a().getArgs();
    }
    
    @Override
    public final void submitParameter(final String s, final float precentValue) {
        final SelesParameters.FilterArg filterArg = this.getFilterArg(s);
        if (filterArg == null || this.getFilterWrap() == null) {
            return;
        }
        filterArg.setPrecentValue(precentValue);
        this.submitParameters();
    }
    
    @Override
    public final void submitParameter(final int n, final float n2) {
        if (this.mFilterWrap == null || n <= 0 || n > this.getFilterArgs().size() - 1) {
            TLog.i("submitParameter failed", new Object[0]);
            return;
        }
        this.submitParameter(this.getFilterArgs().get(n).getKey(), n2);
    }
    
    @Override
    public final void submitParameters() {
        if (this.getFilterWrap() == null) {
            return;
        }
        this.mFilterWrap.submitFilterParameter();
    }
    
    @Override
    public void resetParameters() {
        if (this.mFilterWrap == null || this.mFilterWrap.getFilterParameter() == null) {
            return;
        }
        this.mFilterWrap.getFilterParameter().reset();
    }
    
    public enum TuSdkMediaEffectDataType
    {
        TuSdkMediaEffectDataTypeParticle, 
        TuSdkMediaEffectDataTypeScene, 
        TuSdkMediaEffectDataTypeStickerAudio, 
        TuSdkMediaEffectDataTypeFilter, 
        TuSdkMediaEffectDataTypeText, 
        TuSdkMediEffectDataTypeStickerImage, 
        TuSdkMediaEffectDataTypeAudio, 
        TuSdkMediaEffectDataTypeSticker, 
        TuSdkMediaEffectDataTypeComic, 
        TuSdkMediaEffectDataTypePlasticFace, 
        TuSdkMediaEffectDataTypeSkinFace, 
        TuSdkMediaEffectDataTypeMonsterFace, 
        TuSdkMediaEffectDataTypeTransition;
    }
}
