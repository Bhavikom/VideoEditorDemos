// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

//import org.lasque.tusdk.core.media.codec.TuSdkMediaSync;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaSync;

public interface TuSdkMediaDecodecSync extends TuSdkMediaSync
{
    TuSdkAudioDecodecSync buildAudioDecodecSync();
    
    TuSdkVideoDecodecSync buildVideoDecodecSync();
    
    TuSdkVideoDecodecSync getVideoDecodecSync();
    
    TuSdkAudioDecodecSync getAudioDecodecSync();
}
