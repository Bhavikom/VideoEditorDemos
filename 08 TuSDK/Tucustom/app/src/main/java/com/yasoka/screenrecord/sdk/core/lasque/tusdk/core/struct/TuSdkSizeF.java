// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct;

public class TuSdkSizeF
{
    public float width;
    public float height;
    
    public static TuSdkSizeF create(final float n, final float n2) {
        return new TuSdkSizeF(n, n2);
    }
    
    public TuSdkSizeF() {
    }
    
    public TuSdkSizeF(final float width, final float height) {
        this.width = width;
        this.height = height;
    }
    
    public TuSdkSize toSize() {
        return TuSdkSize.create((int)this.width, (int)this.width);
    }
    
    public TuSdkSize toSizeFloor() {
        return TuSdkSize.create((int)Math.floor(this.width), (int)Math.floor(this.height));
    }
    
    public TuSdkSize toSizeCeil() {
        return TuSdkSize.create((int)Math.ceil(this.width), (int)Math.ceil(this.height));
    }
}
