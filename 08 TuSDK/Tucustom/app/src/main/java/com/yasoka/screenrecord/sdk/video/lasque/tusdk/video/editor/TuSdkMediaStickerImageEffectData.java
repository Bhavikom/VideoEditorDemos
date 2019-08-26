// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;

public class TuSdkMediaStickerImageEffectData extends TuSdkMediaTileEffectDataBase
{
    public TuSdkMediaStickerImageEffectData(final TuSdkImage2DSticker tuSdkImage2DSticker) {
        super(tuSdkImage2DSticker);
        this.setMediaEffectType(TuSdkMediaEffectDataType.TuSdkMediEffectDataTypeStickerImage);
    }
    
    public TuSdkMediaStickerImageEffectData(final Bitmap bitmap, final float n, final float n2, final float n3, final TuSdkSize tuSdkSize, final TuSdkSize tuSdkSize2) {
        super(bitmap, n, n2, n3, tuSdkSize, tuSdkSize2);
        this.setMediaEffectType(TuSdkMediaEffectDataType.TuSdkMediEffectDataTypeStickerImage);
    }
}
