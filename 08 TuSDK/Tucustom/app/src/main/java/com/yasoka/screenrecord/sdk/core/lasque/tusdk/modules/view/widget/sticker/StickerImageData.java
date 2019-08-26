// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker;

public class StickerImageData extends StickerData
{
    public long starTimeUs;
    public long stopTimeUs;
    
    public boolean isContains(final float n) {
        return this.isContains((long)n * 100000L);
    }
    
    public boolean isContains(final long n) {
        return this.starTimeUs <= n && this.stopTimeUs >= n;
    }
}
