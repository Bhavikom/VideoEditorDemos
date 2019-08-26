// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
//import org.lasque.tusdk.core.seles.tusdk.combo.Face2DComboFilterWrap;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.combo.Face2DComboFilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerGroup;

public class TuSdkMediaStickerEffectData extends TuSdkMediaEffectData
{
    private StickerGroup a;
    
    public TuSdkMediaStickerEffectData(final StickerGroup a) {
        if (a == null) {
            TLog.e("%s : Invalid sticker data", new Object[] { this });
            return;
        }
        this.a = a;
        this.setMediaEffectType(TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker);
        this.setVaild(true);
    }
    
    public StickerGroup getStickerGroup() {
        return this.a;
    }
    
    @Override
    public TuSdkMediaEffectData clone() {
        final TuSdkMediaStickerEffectData tuSdkMediaStickerEffectData = new TuSdkMediaStickerEffectData(this.a);
        tuSdkMediaStickerEffectData.mFilterWrap = this.mFilterWrap.clone();
        tuSdkMediaStickerEffectData.setAtTimeRange(this.getAtTimeRange());
        tuSdkMediaStickerEffectData.setVaild(true);
        tuSdkMediaStickerEffectData.setMediaEffectType(this.getMediaEffectType());
        tuSdkMediaStickerEffectData.setIsApplied(false);
        return tuSdkMediaStickerEffectData;
    }
    
    public synchronized Face2DComboFilterWrap getFilterWrap() {
        if (this.mFilterWrap == null) {
            (this.mFilterWrap = (FilterWrap)Face2DComboFilterWrap.creat(FilterLocalPackage.shared().option("Normal"))).processImage();
        }
        return (Face2DComboFilterWrap)this.mFilterWrap;
    }
}
