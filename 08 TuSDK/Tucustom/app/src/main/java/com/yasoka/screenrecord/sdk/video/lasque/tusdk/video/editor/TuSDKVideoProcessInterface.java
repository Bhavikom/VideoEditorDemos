// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

import java.util.List;

public interface TuSDKVideoProcessInterface
{
    boolean addMediaEffectData(final TuSdkMediaEffectData p0);
    
    boolean removeMediaEffectData(final TuSdkMediaEffectData p0);
    
    void removeMediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType p0);
    
     <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType p0);
    
    void removeAllMediaEffects();
    
    void removeAllLiveSticker();
    
    void updateEffectTimeLine(final long p0, final TuSDKMediaEffectsManager.OnFilterChangeListener p1);
}
