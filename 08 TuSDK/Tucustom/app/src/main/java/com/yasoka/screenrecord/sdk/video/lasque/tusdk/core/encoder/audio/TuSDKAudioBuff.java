// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio;

class TuSDKAudioBuff
{
    public boolean isReadyToFill;
    public byte[] buff;
    
    public TuSDKAudioBuff(final int n) {
        this.isReadyToFill = true;
        this.buff = new byte[n];
    }
}
