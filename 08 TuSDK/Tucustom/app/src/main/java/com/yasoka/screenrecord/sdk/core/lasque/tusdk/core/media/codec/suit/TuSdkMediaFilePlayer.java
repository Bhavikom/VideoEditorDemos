// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit;

import android.opengl.GLSurfaceView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public interface TuSdkMediaFilePlayer
{
    void setMediaDataSource(final TuSdkMediaDataSource p0);
    
    void setSurfaceDraw(final TuSdkSurfaceDraw p0);
    
    void setAudioRender(final TuSdkAudioRender p0);
    
    void setListener(final TuSdkMediaPlayerListener p0);
    
    TuSdkFilterBridge getFilterBridge();
    
    GLSurfaceView.Renderer getExtenalRenderer();
    
    boolean load(final boolean p0);
    
    void initInGLThread();
    
    void newFrameReadyInGLThread();
    
    void release();
    
    boolean isPause();
    
    void pause();
    
    void resume();
    
    void reset();
    
    boolean isSupportPrecise();
    
    void setSpeed(final float p0);
    
    float speed();
    
    void setReverse(final boolean p0);
    
    boolean isReverse();
    
    long seekToPercentage(final float p0);
    
    long durationUs();
    
    long elapsedUs();
    
    void seekTo(final long p0);
}
