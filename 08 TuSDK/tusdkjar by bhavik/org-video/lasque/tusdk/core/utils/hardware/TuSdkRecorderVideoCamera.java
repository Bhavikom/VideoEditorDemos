package org.lasque.tusdk.core.utils.hardware;

import android.graphics.Bitmap;
import android.graphics.PointF;
import java.io.File;
import java.util.List;
import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine.TuSdkSoundPitchType;
import org.lasque.tusdk.core.media.camera.TuSdkCameraFocus.TuSdkCameraFocusFaceListener;
import org.lasque.tusdk.core.media.camera.TuSdkCameraFocus.TuSdkCameraFocusListener;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.WaterMarkPosition;
import org.lasque.tusdk.core.video.TuSDKVideoResult;
import org.lasque.tusdk.impl.view.widget.RegionHandler;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData.TuSdkMediaEffectDataType;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public abstract interface TuSdkRecorderVideoCamera
{
  public abstract boolean isDirectEdit();
  
  public abstract void enableDirectEdit(boolean paramBoolean);
  
  public abstract void setVideoEncoderSetting(TuSdkRecorderVideoEncoderSetting paramTuSdkRecorderVideoEncoderSetting);
  
  public abstract TuSdkRecorderVideoEncoderSetting getVideoEncoderSetting();
  
  public abstract void setRecorderVideoCameraCallback(TuSdkRecorderVideoCameraCallback paramTuSdkRecorderVideoCameraCallback);
  
  public abstract void setCameraListener(TuSdkCameraListener paramTuSdkCameraListener);
  
  public abstract TuSdkStillCameraAdapter.CameraState getCameraState();
  
  public abstract RecordState getRecordState();
  
  public abstract void setFaceDetectionCallback(TuSdkFaceDetectionCallback paramTuSdkFaceDetectionCallback);
  
  public abstract void setMediaEffectChangeListener(TuSdkMediaEffectChangeListener paramTuSdkMediaEffectChangeListener);
  
  public abstract void setEnableLiveSticker(boolean paramBoolean);
  
  public abstract void setEnableFaceDetection(boolean paramBoolean);
  
  public abstract void setWaterMarkImage(Bitmap paramBitmap);
  
  public abstract void setWaterMarkPosition(TuSdkWaterMarkOption.WaterMarkPosition paramWaterMarkPosition);
  
  public abstract RegionHandler getRegionHandler();
  
  public abstract void setRegionRatio(float paramFloat);
  
  public abstract boolean canChangeRatio();
  
  public abstract void changeRegionRatio(float paramFloat);
  
  public abstract int getRegionViewColor();
  
  public abstract void setRegionViewColor(int paramInt);
  
  public abstract void setFlashMode(CameraConfigs.CameraFlash paramCameraFlash);
  
  public abstract CameraConfigs.CameraFlash getFlashMode();
  
  public abstract boolean canSupportFlash();
  
  public abstract boolean isFrontFacingCameraPresent();
  
  public abstract boolean isBackFacingCameraPresent();
  
  public abstract void setFocusMode(CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF);
  
  public abstract boolean canSupportAutoFocus();
  
  public abstract void setAntibandingMode(CameraConfigs.CameraAntibanding paramCameraAntibanding);
  
  public abstract void setDisableContinueFocus(boolean paramBoolean);
  
  public abstract void autoFocus(CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF, TuSdkCameraFocus.TuSdkCameraFocusListener paramTuSdkCameraFocusListener);
  
  public abstract void autoFocus(TuSdkCameraFocus.TuSdkCameraFocusListener paramTuSdkCameraFocusListener);
  
  public abstract TuSDKVideoCameraFocusViewInterface getFocusTouchView();
  
  public abstract TuSdkSize getCameraPreviewSize();
  
  public abstract void rotateCamera();
  
  public abstract void startCameraCapture();
  
  public abstract void pauseCameraCapture();
  
  public abstract void resumeCameraCapture();
  
  public abstract void stopCameraCapture();
  
  public abstract InterfaceOrientation getDeviceOrient();
  
  public abstract TuSdkSize getOutputImageSize();
  
  public abstract void setFaceDetectionDelegate(TuSdkCameraFocus.TuSdkCameraFocusFaceListener paramTuSdkCameraFocusFaceListener);
  
  public abstract boolean addMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData);
  
  public abstract boolean removeMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData);
  
  public abstract <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType);
  
  public abstract List<TuSdkMediaEffectData> getAllMediaEffectData();
  
  public abstract void removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType);
  
  public abstract void removeAllMediaEffects();
  
  public abstract void setEnableAudioCapture(boolean paramBoolean);
  
  public abstract boolean isEnableAudioCapture();
  
  public abstract void setSoundPitchType(TuSdkAudioPitchEngine.TuSdkSoundPitchType paramTuSdkSoundPitchType);
  
  public abstract void captureImage();
  
  public abstract void startRecording();
  
  public abstract void resumeRecording();
  
  public abstract void pauseRecording();
  
  public abstract void stopRecording();
  
  public abstract void cancelRecording();
  
  public abstract boolean isRecording();
  
  public abstract void setMinAvailableSpaceBytes(long paramLong);
  
  public abstract long getMinAvailableSpaceBytes();
  
  public abstract void setSaveToAlbum(boolean paramBoolean);
  
  public abstract boolean isSaveToAlbum();
  
  public abstract void setSaveToAlbumName(String paramString);
  
  public abstract String getSaveToAlbumName();
  
  public abstract void setMinRecordingTime(int paramInt);
  
  public abstract int getMinRecordingTime();
  
  public abstract void setMaxRecordingTime(int paramInt);
  
  public abstract int getMaxRecordingTime();
  
  public abstract void setSpeedMode(SpeedMode paramSpeedMode);
  
  public abstract float getMovieDuration();
  
  public abstract int getRecordingFragmentSize();
  
  public abstract TuSdkTimeRange popVideoFragment();
  
  public abstract TuSdkTimeRange lastVideoFragmentRange();
  
  public abstract File getMovieOutputPath();
  
  public abstract void destroy();
  
  public static enum SpeedMode
  {
    private float a;
    
    private SpeedMode(float paramFloat)
    {
      this.a = paramFloat;
    }
    
    public float getSpeedRate()
    {
      return this.a;
    }
  }
  
  public static enum RecordState
  {
    private RecordState() {}
  }
  
  public static enum RecordError
  {
    private RecordError() {}
  }
  
  public static abstract interface TuSdkMediaEffectChangeListener
  {
    public abstract void didApplyingMediaEffect(TuSdkMediaEffectData paramTuSdkMediaEffectData);
    
    public abstract void didRemoveMediaEffect(List<TuSdkMediaEffectData> paramList);
  }
  
  public static abstract interface TuSdkCameraListener
  {
    public abstract void onFilterChanged(FilterWrap paramFilterWrap);
    
    public abstract void onVideoCameraStateChanged(TuSdkStillCameraAdapter.CameraState paramCameraState);
    
    public abstract void onVideoCameraScreenShot(Bitmap paramBitmap);
  }
  
  public static enum FaceDetectionResultType
  {
    private FaceDetectionResultType() {}
  }
  
  public static abstract interface TuSdkFaceDetectionCallback
  {
    public abstract void onFaceDetectionResult(TuSdkRecorderVideoCamera.FaceDetectionResultType paramFaceDetectionResultType, int paramInt);
  }
  
  public static abstract interface TuSdkRecorderVideoCameraCallback
  {
    public abstract void onMovieRecordComplete(TuSDKVideoResult paramTuSDKVideoResult);
    
    public abstract void onMovieRecordProgressChanged(float paramFloat1, float paramFloat2);
    
    public abstract void onMovieRecordStateChanged(TuSdkRecorderVideoCamera.RecordState paramRecordState);
    
    public abstract void onMovieRecordFailed(TuSdkRecorderVideoCamera.RecordError paramRecordError);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\utils\hardware\TuSdkRecorderVideoCamera.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */