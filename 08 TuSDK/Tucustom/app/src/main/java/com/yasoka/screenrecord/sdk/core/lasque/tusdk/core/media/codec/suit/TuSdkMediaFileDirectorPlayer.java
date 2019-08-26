// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.opengl.GLSurfaceView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public interface TuSdkMediaFileDirectorPlayer
{
    void setMediaDataSource(final TuSdkMediaDataSource p0);
    
    void setSurfaceDraw(final TuSdkSurfaceDraw p0);
    
    void setAudioRender(final TuSdkAudioRender p0);
    
    void setAudioMixerRender(final TuSdkAudioRender p0);
    
    void setListener(final TuSdkMediaPlayerListener p0);
    
    TuSdkFilterBridge getFilterBridge();
    
    GLSurfaceView.Renderer getExtenalRenderer();
    
    void setCanvasColor(final float p0, final float p1, final float p2, final float p3);
    
    void setCanvasColor(final int p0);
    
    boolean load(final boolean p0);
    
    void initInGLThread();
    
    void newFrameReadyInGLThread();
    
    void release();
    
    boolean isPause();
    
    void pause();
    
    void resume();
    
    void reset();
    
    long seekToPercentage(final float p0);
    
    void seekTo(final long p0);
    
    long durationUs();
    
    long outputTimeUs();
    
    void setEnableClip(final boolean p0);
    
    TuSdkSize setOutputRatio(final float p0);
    
    void setOutputSize(final TuSdkSize p0);
    
    void preview(final TuSdkMediaTimeline p0);
    
    int setVolume(final float p0);
}
