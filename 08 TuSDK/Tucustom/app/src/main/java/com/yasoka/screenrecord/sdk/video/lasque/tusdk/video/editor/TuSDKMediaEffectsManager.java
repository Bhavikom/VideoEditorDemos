// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker.LiveStickerPlayController;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;

import java.util.List;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;
//import org.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser;

public interface TuSDKMediaEffectsManager
{
    void setMediaEffectDelegate(final TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate p0);
    
    TuSDKComboFilterWrapChain getFilterWrapChain();
    
    boolean addMediaEffectData(final TuSdkMediaEffectData p0);
    
    void addTerminalNode(final SelesContext.SelesInput p0);
    
     <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType p0);
    
    List<TuSdkMediaEffectData> getAllMediaEffectData();
    
    boolean removeMediaEffectData(final TuSdkMediaEffectData p0);
    
    void removeMediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType p0);
    
    void removeAllMediaEffects();
    
    void updateEffectTimeLine(final long p0, final OnFilterChangeListener p1);
    
    void showGroupSticker(final TuSdkMediaStickerEffectData p0);
    
    LiveStickerPlayController getLiveStickerPlayController();
    
    void removeAllLiveSticker();
    
    void release();
    
    public interface OnFilterChangeListener
    {
        void onFilterChanged(final FilterWrap p0);
    }
}
