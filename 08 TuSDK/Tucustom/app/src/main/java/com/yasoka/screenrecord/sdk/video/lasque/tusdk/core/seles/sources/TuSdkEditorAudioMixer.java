// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManager;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderEntry;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManager;

import java.util.List;
//import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderEntry;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public interface TuSdkEditorAudioMixer
{
    void setDataSource(final TuSdkMediaDataSource p0);
    
    void setMasterAudioTrack(final float p0);
    
    void setSecondAudioTrack(final float p0);
    
    void addAudioRenderEntry(final TuSDKAudioRenderEntry p0);
    
    void setAudioRenderEntryList(final List<TuSDKAudioRenderEntry> p0);
    
    void addTaskStateListener(final TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener p0);
    
    void removeTaskStateListener(final TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener p0);
    
    void removeAllTaskStateListener();
    
    void clearAllAudioData();
    
    void loadAudio();
    
    boolean isLoaded();
    
    void notifyLoadCompleted();
    
    void destroy();
}
