// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.delegate;

//import org.lasque.tusdk.core.video.TuSDKVideoResult;

import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.video.TuSDKVideoResult;

public interface TuSDKVideoSaveDelegate
{
    void onProgressChaned(final float p0);
    
    void onSaveResult(final TuSDKVideoResult p0);
    
    void onResultFail(final Exception p0);
}
