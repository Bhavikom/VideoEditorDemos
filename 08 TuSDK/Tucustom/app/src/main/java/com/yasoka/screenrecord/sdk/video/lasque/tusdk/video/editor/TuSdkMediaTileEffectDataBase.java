// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.seles.tusdk.TuSDK2DImageFilterWrap;
//import org.lasque.tusdk.core.seles.tusdk.textSticker.Image2DStickerData;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.TuSDK2DImageFilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.textSticker.Image2DStickerData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;

public abstract class TuSdkMediaTileEffectDataBase extends TuSdkMediaEffectData
{
    private TuSdkImage2DSticker a;
    
    public TuSdkMediaTileEffectDataBase(final TuSdkImage2DSticker a) {
        if (a == null) {
            TLog.e("%s,Invalid sticker data", new Object[] { this });
        }
        this.a = a;
        this.setVaild(true);
    }
    
    public TuSdkMediaTileEffectDataBase(final Bitmap bitmap, final float n, final float n2, final float n3, final TuSdkSize tuSdkSize, final TuSdkSize tuSdkSize2) {
        if (bitmap == null || bitmap.isRecycled()) {
            TLog.e("%s bitmap is null or recycled", new Object[] { this });
            return;
        }
        this.a = this.initStickerImageEffectData(bitmap, n, n2, n3, tuSdkSize, tuSdkSize2);
        this.setVaild(true);
    }
    
    public TuSdkImage2DSticker getStickerData() {
        return this.a;
    }
    
    protected TuSdkImage2DSticker initStickerImageEffectData(final Bitmap bitmap, final float n, final float n2, final float n3, final TuSdkSize tuSdkSize, final TuSdkSize designScreenSize) {
        final TuSdkImage2DSticker tuSdkImage2DSticker = new TuSdkImage2DSticker();
        tuSdkImage2DSticker.setCurrentSticker(new Image2DStickerData(bitmap, tuSdkSize.width, tuSdkSize.height, 0.0f, n, n2, n3));
        tuSdkImage2DSticker.setDesignScreenSize(designScreenSize);
        return tuSdkImage2DSticker;
    }
    
    @Override
    public TuSdkMediaEffectData clone() {
        final TuSdkMediaTextEffectData tuSdkMediaTextEffectData = new TuSdkMediaTextEffectData(this.a);
        tuSdkMediaTextEffectData.setAtTimeRange(this.getAtTimeRange());
        tuSdkMediaTextEffectData.setVaild(true);
        tuSdkMediaTextEffectData.setMediaEffectType(this.getMediaEffectType());
        tuSdkMediaTextEffectData.setIsApplied(false);
        return tuSdkMediaTextEffectData;
    }
    
    public synchronized TuSDK2DImageFilterWrap getFilterWrap() {
        if (this.mFilterWrap == null) {
            (this.mFilterWrap = (FilterWrap)TuSDK2DImageFilterWrap.creat()).processImage();
        }
        return (TuSDK2DImageFilterWrap)this.mFilterWrap;
    }
}
