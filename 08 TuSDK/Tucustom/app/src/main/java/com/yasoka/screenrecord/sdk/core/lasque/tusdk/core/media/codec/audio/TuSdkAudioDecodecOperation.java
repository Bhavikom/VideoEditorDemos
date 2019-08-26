// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;

public interface TuSdkAudioDecodecOperation extends TuSdkDecodecOperation
{
    void setAudioRender(final TuSdkAudioRender p0);
    
    TuSdkAudioInfo getAudioInfo();
}
