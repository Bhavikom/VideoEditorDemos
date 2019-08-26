// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video;

import java.util.Arrays;

public class TuSDKVideoBuff
{
    public boolean isReadyToFill;
    public int colorFormat;
    public byte[] buff;
    
    public TuSDKVideoBuff(final int colorFormat, final int toIndex) {
        this.colorFormat = -1;
        this.isReadyToFill = true;
        this.colorFormat = colorFormat;
        Arrays.fill(this.buff = new byte[toIndex], toIndex / 2, toIndex, (byte)127);
    }
}
