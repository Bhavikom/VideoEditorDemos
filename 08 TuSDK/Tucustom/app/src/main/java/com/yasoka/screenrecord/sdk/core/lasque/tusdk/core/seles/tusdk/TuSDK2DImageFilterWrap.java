// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk;

//import org.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDK2DImageFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;

import java.util.List;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDK2DImageFilter;
//import org.lasque.tusdk.core.seles.SelesParameters;

public class TuSDK2DImageFilterWrap extends FilterWrap implements SelesParameters.TileStickerInterface
{
    private TuSDK2DImageFilter a;
    
    public static TuSDK2DImageFilterWrap creat() {
        return new TuSDK2DImageFilterWrap();
    }
    
    protected TuSDK2DImageFilterWrap() {
        this.a = new TuSDK2DImageFilter();
        this.changeOption(FilterLocalPackage.shared().option("Normal"));
        this.a();
    }
    
    private void a() {
        final TuSDK2DImageFilter a = this.a;
        this.mFilter = a;
        this.mLastFilter = a;
    }
    
    @Override
    public void addTarget(final SelesContext.SelesInput selesInput, final int n) {
        super.addTarget(selesInput, n);
    }
    
    @Override
    public void updateTileStickers(final List<TuSdkImage2DSticker> list) {
        this.a.updateStickers(list);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && o instanceof FilterWrap && o == this;
    }
}
