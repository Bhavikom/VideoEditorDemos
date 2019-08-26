// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.delegate;

//import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;

import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKVideoInfo;

public interface TuSDKVideoLoadDelegate
{
    void onProgressChaned(final float p0);
    
    void onLoadComplete(final TuSDKVideoInfo p0);
}
