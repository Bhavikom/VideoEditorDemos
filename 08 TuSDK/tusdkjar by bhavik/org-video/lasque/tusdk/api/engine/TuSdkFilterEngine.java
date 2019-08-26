package org.lasque.tusdk.api.engine;

import android.graphics.Bitmap;
import android.graphics.RectF;
import java.nio.IntBuffer;
import java.util.List;
import org.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate;
import org.lasque.tusdk.core.api.engine.TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate;
import org.lasque.tusdk.core.api.extend.TuSdkFilterListener;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.type.ColorFormatType;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData.TuSdkMediaEffectDataType;

public abstract interface TuSdkFilterEngine
{
  public abstract void release();
  
  public abstract void setListener(TuSdkFilterEngineListener paramTuSdkFilterEngineListener);
  
  public abstract void onSurfaceCreated();
  
  public abstract void onSurfaceChanged(int paramInt1, int paramInt2);
  
  public abstract void setDisplayRect(RectF paramRectF, float paramFloat);
  
  public abstract void setDetectScale(float paramFloat);
  
  public abstract void setEnableLiveSticker(boolean paramBoolean);
  
  public abstract void setEnableFaceDetection(boolean paramBoolean);
  
  public abstract void setEnableOutputYUVData(boolean paramBoolean);
  
  public abstract void setYuvOutputImageFormat(ColorFormatType paramColorFormatType);
  
  public abstract void setYuvOutputOrienation(ImageOrientation paramImageOrientation);
  
  public abstract CameraConfigs.CameraFacing getCameraFacing();
  
  public abstract void setCameraFacing(CameraConfigs.CameraFacing paramCameraFacing);
  
  public abstract void setInterfaceOrientation(InterfaceOrientation paramInterfaceOrientation);
  
  public abstract void takeShot();
  
  public abstract void setInputImageOrientation(ImageOrientation paramImageOrientation);
  
  public abstract void setOutputImageOrientation(ImageOrientation paramImageOrientation);
  
  public abstract void setCordinateBuilder(SelesVerticeCoordinateCorpBuilder paramSelesVerticeCoordinateCorpBuilder);
  
  public abstract TuSdkSize getOutputImageSize();
  
  public abstract void switchFilter(String paramString);
  
  public abstract void removeAllLiveSticker();
  
  public abstract void showGroupSticker(StickerGroup paramStickerGroup);
  
  public abstract void processFrame(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong);
  
  public abstract int processFrame(int paramInt1, int paramInt2, int paramInt3, long paramLong);
  
  public abstract void snatchFrame(byte[] paramArrayOfByte);
  
  public abstract InterfaceOrientation getDeviceOrient();
  
  public abstract void setOriginalCaptureOrientation(boolean paramBoolean);
  
  public abstract void setOutputCaptureMirrorEnabled(boolean paramBoolean);
  
  public abstract void asyncProcessPictureData(byte[] paramArrayOfByte, InterfaceOrientation paramInterfaceOrientation);
  
  public abstract FaceAligment[] getFaceFeatures();
  
  public abstract void setFaceDetectionDelegate(TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate paramTuSdkVideoProcesserFaceDetectionDelegate);
  
  public abstract float getDeviceAngle();
  
  public abstract void addTerminalNode(SelesContext.SelesInput paramSelesInput);
  
  public abstract void setMediaEffectDelegate(TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate paramTuSDKVideoProcessorMediaEffectDelegate);
  
  public abstract boolean addMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData);
  
  public abstract boolean removeMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData);
  
  public abstract <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType);
  
  public abstract List<TuSdkMediaEffectData> getAllMediaEffectData();
  
  public abstract void removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType);
  
  public abstract void removeAllMediaEffects();
  
  public abstract boolean hasMediaAudioEffects();
  
  public abstract void setFilterChangedListener(TuSdkFilterEngineListener paramTuSdkFilterEngineListener);
  
  public abstract boolean isGroupStickerUsed(StickerGroup paramStickerGroup);
  
  public static abstract interface TuSdkFilterEngineListener
    extends TuSdkFilterListener
  {
    public abstract void onPictureDataCompleted(IntBuffer paramIntBuffer, TuSdkSize paramTuSdkSize);
    
    public abstract void onPreviewScreenShot(Bitmap paramBitmap);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\engine\TuSdkFilterEngine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */