// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.RectF;
//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
import java.io.File;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;

public interface TuSdkMovieEditor
{
    Context getContext();
    
    void setVideoPath(final String p0);
    
    void setDataSource(final TuSdkMediaDataSource p0);
    
    void setEnableTranscode(final boolean p0);
    
    void loadVideo();
    
    void saveVideo();
    
    TuSdkEditorTranscoder getEditorTransCoder();
    
    TuSdkEditorPlayer getEditorPlayer();
    
    TuSdkEditorEffector getEditorEffector();
    
    TuSdkEditorAudioMixer getEditorMixer();
    
    TuSdkEditorSaver getEditorSaver();
    
    void onDestroy();
    
    public static class TuSdkMovieEditorOptions
    {
        public TuSdkMediaDataSource videoDataSource;
        public File movieOutputFilePath;
        public TuSdkTimeRange cutTimeRange;
        public RectF canvasRect;
        public RectF cropRect;
        public boolean includeAudioInVideo;
        public boolean enableFirstFramePause;
        public TuSdkMediaPictureEffectReferTimelineType timelineType;
        public boolean clearAudioDecodeCacheInfoOnDestory;
        public Boolean saveToAlbum;
        public String saveToAlbumName;
        public TuSdkSize outputSize;
        public Bitmap waterImage;
        public float waterImageScale;
        public boolean isRecycleWaterImage;
        public TuSdkWaterMarkOption.WaterMarkPosition watermarkPosition;
        
        public TuSdkMovieEditorOptions() {
            this.enableFirstFramePause = true;
            this.clearAudioDecodeCacheInfoOnDestory = false;
            this.waterImageScale = 0.09f;
            this.watermarkPosition = TuSdkWaterMarkOption.WaterMarkPosition.TopRight;
            this.saveToAlbum = true;
            this.includeAudioInVideo = true;
            this.outputSize = null;
            this.movieOutputFilePath = null;
            this.timelineType = TuSdkMediaPictureEffectReferTimelineType.TuSdkMediaEffectReferInputTimelineType;
        }
        
        public static TuSdkMovieEditorOptions defaultOptions() {
            return new TuSdkMovieEditorOptions();
        }
        
        public TuSdkMovieEditorOptions setVideoDataSource(final TuSdkMediaDataSource videoDataSource) {
            this.videoDataSource = videoDataSource;
            return this;
        }
        
        public TuSdkMovieEditorOptions setMovieOutputFilePath(final File movieOutputFilePath) {
            this.movieOutputFilePath = movieOutputFilePath;
            return this;
        }
        
        public TuSdkMovieEditorOptions setCutTimeRange(final TuSdkTimeRange cutTimeRange) {
            this.cutTimeRange = cutTimeRange;
            return this;
        }
        
        public TuSdkMovieEditorOptions setCanvasRectF(final RectF canvasRect) {
            this.canvasRect = canvasRect;
            return this;
        }
        
        public TuSdkMovieEditorOptions setIncludeAudioInVideo(final boolean includeAudioInVideo) {
            this.includeAudioInVideo = includeAudioInVideo;
            return this;
        }
        
        public TuSdkMovieEditorOptions setPictureEffectReferTimelineType(final TuSdkMediaPictureEffectReferTimelineType timelineType) {
            this.timelineType = timelineType;
            return this;
        }
        
        public TuSdkMovieEditorOptions setOutputSize(final TuSdkSize outputSize) {
            this.outputSize = outputSize;
            return this;
        }
        
        public TuSdkMovieEditorOptions setSaveToAlbum(final Boolean saveToAlbum) {
            this.saveToAlbum = saveToAlbum;
            return this;
        }
        
        public TuSdkMovieEditorOptions setSaveToAlbumName(final String saveToAlbumName) {
            this.saveToAlbumName = saveToAlbumName;
            return this;
        }
        
        public TuSdkMovieEditorOptions setClearAudioDecodeCacheInfoOnDestory(final boolean clearAudioDecodeCacheInfoOnDestory) {
            this.clearAudioDecodeCacheInfoOnDestory = clearAudioDecodeCacheInfoOnDestory;
            return this;
        }
        
        public TuSdkMovieEditorOptions setWaterImage(final Bitmap waterImage, final TuSdkWaterMarkOption.WaterMarkPosition watermarkPosition, final boolean isRecycleWaterImage) {
            this.isRecycleWaterImage = isRecycleWaterImage;
            this.watermarkPosition = watermarkPosition;
            this.waterImage = waterImage;
            return this;
        }
        
        public enum TuSdkMediaPictureEffectReferTimelineType
        {
            TuSdkMediaEffectReferOutputTimelineType(0), 
            TuSdkMediaEffectReferInputTimelineType(1);
            
            private int a;
            
            private TuSdkMediaPictureEffectReferTimelineType(final int a) {
                this.a = a;
            }
            
            public int getType() {
                return this.a;
            }
        }
    }
}
