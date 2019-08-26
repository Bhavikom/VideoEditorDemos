// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend;

//import org.lasque.tusdk.core.struct.TuSdkSize;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;

public enum SelesTextureSizeAlign
{
    Align2MultipleMin(2, false, false), 
    Align4MultipleMin(4, false, false), 
    Align8MultipleMin(8, false, false), 
    Align16MultipleMin(16, false, false), 
    Align2MultipleMax(2, true, false), 
    Align4MultipleMax(4, true, false), 
    Align8MultipleMax(8, true, false), 
    Align16MultipleMax(16, true, false), 
    Align4MultipleNearOrMin(4, false, true), 
    Align8MultipleNearOrMin(8, false, true), 
    Align16MultipleNearOrMin(16, false, true), 
    Align4MultipleNearOrMax(4, true, true), 
    Align8MultipleNearOrMax(8, true, true), 
    Align16MultipleNearOrMax(16, true, true);
    
    private int a;
    private boolean b;
    private boolean c;
    
    private SelesTextureSizeAlign(final int a, final boolean b, final boolean c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    public int getMultiple() {
        return this.a;
    }
    
    public TuSdkSize align(final TuSdkSize tuSdkSize) {
        if (tuSdkSize == null || tuSdkSize.minSide() < this.getMultiple()) {
            return tuSdkSize;
        }
        return TuSdkSize.create(this.align(tuSdkSize.width), this.align(tuSdkSize.height));
    }
    
    public int align(final int n) {
        if (n < this.getMultiple()) {
            return n;
        }
        final int n2 = n - n % this.getMultiple();
        if (n == n2) {
            return n;
        }
        final int n3 = n2 + this.getMultiple();
        final int n4 = this.b ? n3 : n2;
        if (!this.c) {
            return n4;
        }
        final int abs = Math.abs(n - n2);
        final int abs2 = Math.abs(n - n3);
        if (abs == abs2) {
            return n4;
        }
        if (abs > abs2) {
            return n3;
        }
        return n2;
    }
    
    public static SelesTextureSizeAlign getValue(final int n, final boolean b, final boolean b2) {
        for (final SelesTextureSizeAlign selesTextureSizeAlign : values()) {
            if (n == selesTextureSizeAlign.a && b == selesTextureSizeAlign.b && b2 == selesTextureSizeAlign.c) {
                return selesTextureSizeAlign;
            }
        }
        return SelesTextureSizeAlign.Align2MultipleMin;
    }
}
