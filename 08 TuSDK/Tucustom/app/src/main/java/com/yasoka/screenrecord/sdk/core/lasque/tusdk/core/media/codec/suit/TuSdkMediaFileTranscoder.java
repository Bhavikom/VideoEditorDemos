// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit;

//import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import android.media.MediaFormat;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;

import java.util.List;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public interface TuSdkMediaFileTranscoder
{
    void addInputDataSource(final TuSdkMediaDataSource p0);
    
    void addInputDataSouces(final List<TuSdkMediaDataSource> p0);
    
    void setOutputFilePath(final String p0);
    
    int setOutputVideoFormat(final MediaFormat p0);
    
    int setOutputAudioFormat(final MediaFormat p0);
    
    void setSurfaceRender(final TuSdkSurfaceRender p0);
    
    void setAudioRender(final TuSdkAudioRender p0);
    
    boolean run(final TuSdkMediaProgress p0);
    
    void stop();
}
