// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import android.graphics.RectF;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import android.media.MediaFormat;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.util.List;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public interface TuSdkMediaFilesCuter
{
    void setMediaDataSource(final TuSdkMediaDataSource p0);
    
    void setMediaDataSources(final List<TuSdkMediaDataSource> p0);
    
    void setOutputFilePath(final String p0);
    
    int setOutputVideoFormat(final MediaFormat p0);
    
    int setOutputAudioFormat(final MediaFormat p0);
    
    void setSurfaceRender(final TuSdkSurfaceRender p0);
    
    void setAudioRender(final TuSdkAudioRender p0);
    
    boolean run(final TuSdkMediaProgress p0);
    
    void stop();
    
    TuSdkSize preferredOutputSize();
    
    void setOutputOrientation(final ImageOrientation p0);
    
    void setCanvasRect(final RectF p0);
    
    void setCropRect(final RectF p0);
    
    void setTimeSlice(final TuSdkMediaTimeSlice p0);
}
