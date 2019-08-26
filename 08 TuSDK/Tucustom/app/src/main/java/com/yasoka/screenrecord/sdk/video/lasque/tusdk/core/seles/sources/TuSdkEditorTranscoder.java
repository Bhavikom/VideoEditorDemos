// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;
//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public interface TuSdkEditorTranscoder
{
    void setVideoDataSource(final TuSdkMediaDataSource p0);
    
    TuSdkMediaDataSource getVideoDataSource();
    
    void setTimeRange(final TuSdkTimeRange p0);
    
    void setCanvasRect(final RectF p0);
    
    void setCropRect(final RectF p0);
    
    float getVideoOutputDuration();
    
    long getVideoOutputDurationTimeUs();
    
    float getVideoInputDuration();
    
    long getVideoInputDurationTimeUS();
    
    TuSDKVideoInfo getInputVideoInfo();
    
    TuSDKVideoInfo getOutputVideoInfo();
    
    void addTransCoderProgressListener(final TuSdkTranscoderProgressListener p0);
    
    void removeTransCoderProgressListener(final TuSdkTranscoderProgressListener p0);
    
    void removeAllTransCoderProgressListener();
    
    void startTransCoder();
    
    int getStatus();
    
    void stopTransCoder();
    
    void destroy();
    
    public interface TuSdkTranscoderProgressListener
    {
        void onProgressChanged(final float p0);
        
        void onLoadComplete(final TuSDKVideoInfo p0, final TuSdkMediaDataSource p1);
        
        void onError(final Exception p0);
    }
    
    public static class TuSdkTranscoderStatus
    {
        public static final int Init = 0;
        public static final int Loading = 1;
        public static final int Loaded = 2;
        public static final int Error = 3;
        public static final int Stop = 4;
    }
}
