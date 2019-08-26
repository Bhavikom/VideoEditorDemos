// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.opengl.EGLContext;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
//import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import android.media.MediaFormat;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesWatermark;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.core.seles.sources.SelesWatermark;

public interface TuSdkMediaFileRecorder
{
    void setRecordProgress(final TuSdkMediaFileRecorderProgress p0);
    
    void setOutputFilePath(final String p0);
    
    void setWatermark(final SelesWatermark p0);
    
    int setOutputVideoFormat(final MediaFormat p0);
    
    int setOutputAudioFormat(final MediaFormat p0);
    
    TuSdkAudioInfo getOutputAudioInfo();
    
    TuSdkFilterBridge getFilterBridge();
    
    void setFilterBridge(final TuSdkFilterBridge p0);
    
    void disconnect();
    
    void setSurfaceRender(final TuSdkSurfaceRender p0);
    
    void setAudioRender(final TuSdkAudioRender p0);
    
    void changeSpeed(final float p0);
    
    boolean startRecord(final EGLContext p0);
    
    void stopRecord();
    
    void pauseRecord();
    
    void resumeRecord();
    
    void newFrameReadyInGLThread(final long p0);
    
    void newFrameReadyWithAudio(final ByteBuffer p0, final MediaCodec.BufferInfo p1);
    
    void release();
    
    public interface TuSdkMediaFileRecorderProgress
    {
        void onProgress(final long p0, final TuSdkMediaDataSource p1);
        
        void onCompleted(final Exception p0, final TuSdkMediaDataSource p1, final TuSdkMediaTimeline p2);
    }
}
