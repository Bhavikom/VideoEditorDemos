// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record;

//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileRecorder;
import android.opengl.GLSurfaceView;
//import org.lasque.tusdk.core.seles.SelesContext;
import java.io.File;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import android.media.MediaFormat;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileRecorder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesWatermark;
//import org.lasque.tusdk.core.seles.sources.SelesWatermark;

public interface TuSdkMediaRecordHub
{
    void setRecordListener(final TuSdkMediaRecordHubListener p0);
    
    void setWatermark(final SelesWatermark p0);
    
    void setOutputVideoFormat(final MediaFormat p0);
    
    void setOutputAudioFormat(final MediaFormat p0);
    
    void appendRecordSurface(final TuSdkRecordSurface p0);
    
    void setSurfaceRender(final TuSdkSurfaceRender p0);
    
    void setAudioRender(final TuSdkAudioRender p0);
    
    TuSdkMediaRecordHubStatus getState();
    
    boolean start(final File p0);
    
    void stop();
    
    boolean pause();
    
    boolean resume();
    
    void reset();
    
    void changeSpeed(final float p0);
    
    void changePitch(final float p0);
    
    void release();
    
    void addTarget(final SelesContext.SelesInput p0, final int p1);
    
    void removeTarget(final SelesContext.SelesInput p0);
    
    GLSurfaceView.Renderer getExtenalRenderer();
    
    void initInGLThread();
    
    void newFrameReadyInGLThread();
    
    public interface TuSdkMediaRecordHubListener extends TuSdkMediaFileRecorder.TuSdkMediaFileRecorderProgress
    {
        void onStatusChanged(final TuSdkMediaRecordHubStatus p0, final TuSdkMediaRecordHub p1);
    }
    
    public enum TuSdkMediaRecordHubStatus
    {
        UNINITIALIZED, 
        START, 
        STOP, 
        PREPARE_RECORD, 
        PREPARE_STOP, 
        START_RECORD, 
        PAUSE_RECORD, 
        RELEASED;
    }
}
