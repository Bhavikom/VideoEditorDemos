// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video;

//import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import android.view.Surface;

import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;

public interface TuSDKHardVideoDataEncoderInterface
{
    boolean initCodec(final TuSDKVideoEncoderSetting p0);
    
    Surface getInputSurface();
    
    boolean requestKeyFrame();
    
    void flush();
    
    void drainEncoder(final boolean p0);
    
    void release();
    
    void setDelegate(final TuSDKVideoDataEncoderDelegate p0);
}
