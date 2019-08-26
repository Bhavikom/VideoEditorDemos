// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.utils.hardware;

//import org.lasque.tusdk.core.video.TuSDKVideoResult;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import java.io.File;
//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
//import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine;
import java.util.List;
//import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraFocus;
import android.graphics.PointF;
//import org.lasque.tusdk.impl.view.widget.RegionHandler;
//import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraFocus;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.RegionHandler;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.video.TuSDKVideoResult;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;

public interface TuSdkRecorderVideoCamera
{
    boolean isDirectEdit();
    
    void enableDirectEdit(final boolean p0);
    
    void setVideoEncoderSetting(final TuSdkRecorderVideoEncoderSetting p0);
    
    TuSdkRecorderVideoEncoderSetting getVideoEncoderSetting();
    
    void setRecorderVideoCameraCallback(final TuSdkRecorderVideoCameraCallback p0);
    
    void setCameraListener(final TuSdkCameraListener p0);
    
    TuSdkStillCameraAdapter.CameraState getCameraState();
    
    RecordState getRecordState();
    
    void setFaceDetectionCallback(final TuSdkFaceDetectionCallback p0);
    
    void setMediaEffectChangeListener(final TuSdkMediaEffectChangeListener p0);
    
    void setEnableLiveSticker(final boolean p0);
    
    void setEnableFaceDetection(final boolean p0);
    
    void setWaterMarkImage(final Bitmap p0);
    
    void setWaterMarkPosition(final TuSdkWaterMarkOption.WaterMarkPosition p0);
    
    RegionHandler getRegionHandler();
    
    void setRegionRatio(final float p0);
    
    boolean canChangeRatio();
    
    void changeRegionRatio(final float p0);
    
    int getRegionViewColor();
    
    void setRegionViewColor(final int p0);
    
    void setFlashMode(final CameraConfigs.CameraFlash p0);
    
    CameraConfigs.CameraFlash getFlashMode();
    
    boolean canSupportFlash();
    
    boolean isFrontFacingCameraPresent();
    
    boolean isBackFacingCameraPresent();
    
    void setFocusMode(final CameraConfigs.CameraAutoFocus p0, final PointF p1);
    
    boolean canSupportAutoFocus();
    
    void setAntibandingMode(final CameraConfigs.CameraAntibanding p0);
    
    void setDisableContinueFocus(final boolean p0);
    
    void autoFocus(final CameraConfigs.CameraAutoFocus p0, final PointF p1, final TuSdkCameraFocus.TuSdkCameraFocusListener p2);
    
    void autoFocus(final TuSdkCameraFocus.TuSdkCameraFocusListener p0);
    
    TuSDKVideoCameraFocusViewInterface getFocusTouchView();
    
    TuSdkSize getCameraPreviewSize();
    
    void rotateCamera();
    
    void startCameraCapture();
    
    void pauseCameraCapture();
    
    void resumeCameraCapture();
    
    void stopCameraCapture();
    
    InterfaceOrientation getDeviceOrient();
    
    TuSdkSize getOutputImageSize();
    
    void setFaceDetectionDelegate(final TuSdkCameraFocus.TuSdkCameraFocusFaceListener p0);
    
    boolean addMediaEffectData(final TuSdkMediaEffectData p0);
    
    boolean removeMediaEffectData(final TuSdkMediaEffectData p0);
    
     <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType p0);
    
    List<TuSdkMediaEffectData> getAllMediaEffectData();
    
    void removeMediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType p0);
    
    void removeAllMediaEffects();
    
    void setEnableAudioCapture(final boolean p0);
    
    boolean isEnableAudioCapture();
    
    void setSoundPitchType(final TuSdkAudioPitchEngine.TuSdkSoundPitchType p0);
    
    void captureImage();
    
    void startRecording();
    
    void resumeRecording();
    
    void pauseRecording();
    
    void stopRecording();
    
    void cancelRecording();
    
    boolean isRecording();
    
    void setMinAvailableSpaceBytes(final long p0);
    
    long getMinAvailableSpaceBytes();
    
    void setSaveToAlbum(final boolean p0);
    
    boolean isSaveToAlbum();
    
    void setSaveToAlbumName(final String p0);
    
    String getSaveToAlbumName();
    
    void setMinRecordingTime(final int p0);
    
    int getMinRecordingTime();
    
    void setMaxRecordingTime(final int p0);
    
    int getMaxRecordingTime();
    
    void setSpeedMode(final SpeedMode p0);
    
    float getMovieDuration();
    
    int getRecordingFragmentSize();
    
    TuSdkTimeRange popVideoFragment();
    
    TuSdkTimeRange lastVideoFragmentRange();
    
    File getMovieOutputPath();
    
    void destroy();
    
    public enum SpeedMode
    {
        NORMAL(1.0f), 
        FAST1(1.5f), 
        FAST2(2.0f), 
        Slow1(0.75f), 
        Slow2(0.5f);
        
        private float a;
        
        private SpeedMode(final float a) {
            this.a = a;
        }
        
        public float getSpeedRate() {
            return this.a;
        }
    }
    
    public enum RecordState
    {
        Recording, 
        Saving, 
        Paused, 
        RecordCompleted, 
        SaveCompleted, 
        Canceled;
    }
    
    public enum RecordError
    {
        Unknow, 
        NotEnoughSpace, 
        InvalidRecordingTime, 
        LessMinRecordingTime, 
        MoreMaxDuration, 
        SaveFailed;
    }
    
    public interface TuSdkMediaEffectChangeListener
    {
        void didApplyingMediaEffect(final TuSdkMediaEffectData p0);
        
        void didRemoveMediaEffect(final List<TuSdkMediaEffectData> p0);
    }
    
    public interface TuSdkCameraListener
    {
        void onFilterChanged(final FilterWrap p0);
        
        void onVideoCameraStateChanged(final TuSdkStillCameraAdapter.CameraState p0);
        
        void onVideoCameraScreenShot(final Bitmap p0);
    }
    
    public enum FaceDetectionResultType
    {
        FaceDetected, 
        NoFaceDetected;
    }
    
    public interface TuSdkFaceDetectionCallback
    {
        void onFaceDetectionResult(final FaceDetectionResultType p0, final int p1);
    }
    
    public interface TuSdkRecorderVideoCameraCallback
    {
        void onMovieRecordComplete(final TuSDKVideoResult p0);
        
        void onMovieRecordProgressChanged(final float p0, final float p1);
        
        void onMovieRecordStateChanged(final RecordState p0);
        
        void onMovieRecordFailed(final RecordError p0);
    }
}
