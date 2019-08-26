// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;

import java.io.File;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;

public class TuSDKMovieEditorOptions
{
    @Deprecated
    public String moviePath;
    public TuSDKMediaDataSource videoDataSource;
    public File movieOutputFilePath;
    public TuSdkTimeRange cutTimeRange;
    public boolean includeAudioInVideo;
    public boolean clearAudioDecodeCacheInfoOnDestory;
    public boolean autoPlay;
    public boolean loopingPlay;
    public int minCutDuration;
    public int maxCutDuration;
    public Boolean saveToAlbum;
    public String saveToAlbumName;
    public RectF outputRegion;
    public TuSdkSize outputSize;
    
    public TuSDKMovieEditorOptions() {
        this.clearAudioDecodeCacheInfoOnDestory = false;
        this.autoPlay = false;
        this.loopingPlay = false;
        this.saveToAlbum = true;
        this.includeAudioInVideo = true;
        this.autoPlay = false;
        this.loopingPlay = false;
        this.minCutDuration = -1;
        this.maxCutDuration = -1;
        this.outputRegion = null;
        this.outputSize = null;
        this.movieOutputFilePath = null;
    }
    
    public static TuSDKMovieEditorOptions defaultOptions() {
        return new TuSDKMovieEditorOptions();
    }
    
    public TuSDKMovieEditorOptions setMoviePath(final String moviePath) {
        this.moviePath = moviePath;
        return this;
    }
    
    public TuSDKMovieEditorOptions setVideoDataSource(final TuSDKMediaDataSource videoDataSource) {
        this.videoDataSource = videoDataSource;
        return this;
    }
    
    public TuSDKMovieEditorOptions setMovieOutputFilePath(final File movieOutputFilePath) {
        this.movieOutputFilePath = movieOutputFilePath;
        return this;
    }
    
    public TuSDKMovieEditorOptions setCutTimeRange(final TuSdkTimeRange cutTimeRange) {
        this.cutTimeRange = cutTimeRange;
        return this;
    }
    
    public TuSDKMovieEditorOptions setIncludeAudioInVideo(final boolean includeAudioInVideo) {
        this.includeAudioInVideo = includeAudioInVideo;
        return this;
    }
    
    public TuSDKMovieEditorOptions setAutoPlay(final boolean autoPlay) {
        this.autoPlay = autoPlay;
        return this;
    }
    
    public TuSDKMovieEditorOptions setLoopingPlay(final boolean loopingPlay) {
        this.loopingPlay = loopingPlay;
        return this;
    }
    
    public TuSDKMovieEditorOptions setMaxCutDuration(final int maxCutDuration) {
        this.maxCutDuration = maxCutDuration;
        return this;
    }
    
    public TuSDKMovieEditorOptions setMinCutDuration(final int minCutDuration) {
        this.minCutDuration = minCutDuration;
        return this;
    }
    
    public TuSDKMovieEditorOptions setOutputRegion(final RectF outputRegion) {
        this.outputRegion = outputRegion;
        return this;
    }
    
    public TuSDKMovieEditorOptions setOutputSize(final TuSdkSize outputSize) {
        this.outputSize = outputSize;
        return this;
    }
    
    public TuSDKMovieEditorOptions setSaveToAlbum(final Boolean saveToAlbum) {
        this.saveToAlbum = saveToAlbum;
        return this;
    }
    
    public TuSDKMovieEditorOptions setSaveToAlbumName(final String saveToAlbumName) {
        this.saveToAlbumName = saveToAlbumName;
        return this;
    }
    
    public TuSDKMovieEditorOptions setClearAudioDecodeCacheInfoOnDestory(final boolean clearAudioDecodeCacheInfoOnDestory) {
        this.clearAudioDecodeCacheInfoOnDestory = clearAudioDecodeCacheInfoOnDestory;
        return this;
    }
}
