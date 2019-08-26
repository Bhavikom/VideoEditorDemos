// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video;

//import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;

import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;

public interface TuSDKVideoDataEncoderInterface
{
    TuSDKVideoEncoderSetting getVideoEncoderSetting();
    
    void setVideoEncoderSetting(final TuSDKVideoEncoderSetting p0);
    
    void setDelegate(final TuSDKVideoDataEncoderDelegate p0);
}
