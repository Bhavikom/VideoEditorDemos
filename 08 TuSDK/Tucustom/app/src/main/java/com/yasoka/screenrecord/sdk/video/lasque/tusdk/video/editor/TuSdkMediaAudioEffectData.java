// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderEntry;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderEntry;

public class TuSdkMediaAudioEffectData extends TuSdkMediaEffectData
{
    private TuSDKAudioRenderEntry a;
    private float b;
    
    public TuSdkMediaAudioEffectData(final TuSdkMediaDataSource tuSdkMediaDataSource) {
        if (tuSdkMediaDataSource == null || !tuSdkMediaDataSource.isValid()) {
            TLog.e("%s : Invalid audio data", new Object[] { this });
            this.setVaild(false);
            return;
        }
        this.a = new TuSDKAudioRenderEntry(tuSdkMediaDataSource);
        this.setMediaEffectType(TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio);
        this.setVaild(true);
    }
    
    public TuSdkMediaAudioEffectData(final TuSdkAudioInfo tuSdkAudioInfo) {
        if (tuSdkAudioInfo == null) {
            TLog.w("%s : Invalid audio data", new Object[] { this });
            this.setVaild(false);
            return;
        }
        this.a = new TuSDKAudioRenderEntry(tuSdkAudioInfo);
        this.setVaild(true);
    }
    
    public final TuSDKAudioRenderEntry getAudioEntry() {
        return this.a;
    }
    
    @Override
    public void setAtTimeRange(final TuSdkTimeRange tuSdkTimeRange) {
        if (!this.isVaild()) {
            return;
        }
        super.setAtTimeRange(tuSdkTimeRange);
        this.a.setTimeRange(tuSdkTimeRange);
    }
    
    public final void setVolume(final float n) {
        if (!this.isVaild()) {
            return;
        }
        this.b = n;
        this.a.setVolume(n);
    }
    
    public final float getVolume() {
        return this.b;
    }
    
    @Override
    public TuSdkMediaEffectData clone() {
        final TuSdkMediaAudioEffectData tuSdkMediaAudioEffectData = new TuSdkMediaAudioEffectData(this.a);
        tuSdkMediaAudioEffectData.setAtTimeRange(this.getAtTimeRange());
        tuSdkMediaAudioEffectData.setVaild(true);
        tuSdkMediaAudioEffectData.setMediaEffectType(this.getMediaEffectType());
        tuSdkMediaAudioEffectData.setIsApplied(false);
        return tuSdkMediaAudioEffectData;
    }
    
    @Override
    public synchronized FilterWrap getFilterWrap() {
        if (this.mFilterWrap == null) {
            (this.mFilterWrap = FilterWrap.creat(FilterLocalPackage.shared().option("Normal"))).processImage();
        }
        return this.mFilterWrap;
    }
}
