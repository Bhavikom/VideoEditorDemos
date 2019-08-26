// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.movie.preproc.mixer;

//import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioEntry;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioEntry;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;

import java.util.List;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;

public interface TuSDKMovieMixerInterface
{
    void mix(final TuSDKMediaDataSource p0, final List<TuSDKAudioEntry> p1, final boolean p2);
    
    TuSDKMovieMixerInterface setVideoSoundVolume(final float p0);
}
