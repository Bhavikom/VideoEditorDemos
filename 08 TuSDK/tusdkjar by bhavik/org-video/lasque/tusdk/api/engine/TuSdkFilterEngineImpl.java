package org.lasque.tusdk.api.engine;

import android.graphics.RectF;
import android.opengl.GLES20;
import java.util.List;
import org.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate;
import org.lasque.tusdk.core.api.engine.TuSdkEngine;
import org.lasque.tusdk.core.api.engine.TuSdkEngineImpl;
import org.lasque.tusdk.core.api.engine.TuSdkEngineInputSurfaceImpl;
import org.lasque.tusdk.core.api.engine.TuSdkEngineInputTextureImpl;
import org.lasque.tusdk.core.api.engine.TuSdkEngineInputYUVDataImpl;
import org.lasque.tusdk.core.api.engine.TuSdkEngineOrientation;
import org.lasque.tusdk.core.api.engine.TuSdkEngineOrientationImpl;
import org.lasque.tusdk.core.api.engine.TuSdkEngineOutputImage;
import org.lasque.tusdk.core.api.engine.TuSdkEngineOutputImageImpl;
import org.lasque.tusdk.core.api.engine.TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.type.ColorFormatType;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.utils.monitor.TuSdkGLMonitor;
import org.lasque.tusdk.core.utils.monitor.TuSdkMonitor;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData.TuSdkMediaEffectDataType;

public class TuSdkFilterEngineImpl
  implements TuSdkFilterEngine
{
  private TuSdkEngine a;
  private TuSdkEngineOrientation b;
  private TuSdkEngineVideoProcessorImpl c;
  private TuSdkEngineOutputImage d;
  private TuSdkFilterEngine.TuSdkFilterEngineListener e;
  private int f;
  private int g;
  
  public void setListener(TuSdkFilterEngine.TuSdkFilterEngineListener paramTuSdkFilterEngineListener)
  {
    this.e = paramTuSdkFilterEngineListener;
    if (this.c != null) {
      this.c.setFilterChangedListener(this.e);
    }
  }
  
  public TuSdkFilterEngineImpl(boolean paramBoolean)
  {
    TLog.dump("TuSdkFilterEngine create() managedGLLifecycle : %s", new Object[] { Boolean.valueOf(paramBoolean) });
    this.a = new TuSdkEngineImpl(paramBoolean);
    this.b = new TuSdkEngineOrientationImpl();
    this.b.setHorizontallyMirrorFrontFacingCamera(true);
    this.a.setEngineOrientation(this.b);
    this.a.setEngineInputImage(new TuSdkEngineInputYUVDataImpl());
    this.c = new TuSdkEngineVideoProcessorImpl();
    this.a.setEngineProcessor(this.c);
    this.d = new TuSdkEngineOutputImageImpl();
    this.a.setEngineOutputImage(this.d);
  }
  
  public TuSdkFilterEngineImpl(boolean paramBoolean1, boolean paramBoolean2)
  {
    TLog.dump("TuSdkFilterEngine create()  isOESTexture : %s  managedGLLifecycle : %s", new Object[] { Boolean.valueOf(paramBoolean1), Boolean.valueOf(paramBoolean2) });
    this.a = new TuSdkEngineImpl(paramBoolean2);
    this.b = new TuSdkEngineOrientationImpl();
    this.b.setHorizontallyMirrorFrontFacingCamera(true);
    this.a.setEngineOrientation(this.b);
    this.a.setEngineInputImage(paramBoolean1 ? new TuSdkEngineInputSurfaceImpl() : new TuSdkEngineInputTextureImpl());
    this.c = new TuSdkEngineVideoProcessorImpl();
    this.a.setEngineProcessor(this.c);
    this.d = new TuSdkEngineOutputImageImpl();
    this.a.setEngineOutputImage(this.d);
  }
  
  public CameraConfigs.CameraFacing getCameraFacing()
  {
    if (this.b == null) {
      return CameraConfigs.CameraFacing.Back;
    }
    return this.b.getCameraFacing();
  }
  
  public void setCameraFacing(CameraConfigs.CameraFacing paramCameraFacing)
  {
    if (this.b == null) {
      return;
    }
    this.b.setCameraFacing(paramCameraFacing);
  }
  
  public void setInterfaceOrientation(InterfaceOrientation paramInterfaceOrientation)
  {
    if (this.b == null) {
      return;
    }
    this.b.setInterfaceOrientation(paramInterfaceOrientation);
  }
  
  public void setInputImageOrientation(ImageOrientation paramImageOrientation)
  {
    if (this.b == null) {
      return;
    }
    this.b.setInputOrientation(paramImageOrientation);
  }
  
  public void setOutputImageOrientation(ImageOrientation paramImageOrientation)
  {
    if (this.b == null) {
      return;
    }
    this.b.setOutputOrientation(paramImageOrientation);
  }
  
  public void setCordinateBuilder(SelesVerticeCoordinateCorpBuilder paramSelesVerticeCoordinateCorpBuilder)
  {
    if (this.a == null) {
      return;
    }
    this.a.setInputTextureCoordinateBuilder(paramSelesVerticeCoordinateCorpBuilder);
  }
  
  public TuSdkSize getOutputImageSize()
  {
    return this.b.getOutputSize();
  }
  
  public void release()
  {
    if (this.a != null)
    {
      this.c.release();
      this.a.release();
      this.a = null;
      TLog.dump("TuSdkFilterEngine release()", new Object[0]);
    }
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public void onSurfaceCreated()
  {
    if (this.a == null) {
      return;
    }
    this.a.prepareInGlThread();
  }
  
  public void onSurfaceChanged(int paramInt1, int paramInt2)
  {
    this.f = paramInt1;
    this.g = paramInt2;
  }
  
  public void setDisplayRect(RectF paramRectF, float paramFloat)
  {
    this.c.setDisplayRect(paramRectF, paramFloat);
  }
  
  public void setEnableLiveSticker(boolean paramBoolean)
  {
    if (this.c == null) {
      return;
    }
    this.c.setEnableLiveSticker(paramBoolean);
    this.c.setEnableFacePlastic(paramBoolean);
  }
  
  public void setEnableFaceDetection(boolean paramBoolean)
  {
    if (this.c == null) {
      return;
    }
    this.c.setEnableFacePlastic(paramBoolean);
  }
  
  public void setDetectScale(float paramFloat)
  {
    if (this.c == null) {
      return;
    }
    this.c.setDetectScale(paramFloat);
  }
  
  public void switchFilter(String paramString)
  {
    if (this.c == null) {
      return;
    }
    this.c.switchFilter(paramString);
  }
  
  public void removeAllLiveSticker()
  {
    if (this.c == null) {
      return;
    }
    this.c.removeAllLiveSticker();
  }
  
  public void showGroupSticker(StickerGroup paramStickerGroup)
  {
    if (this.c == null) {
      return;
    }
    this.c.showGroupSticker(paramStickerGroup);
  }
  
  public void setEnableOutputYUVData(boolean paramBoolean)
  {
    if (this.d == null) {
      return;
    }
    this.d.setEnableOutputYUVData(paramBoolean);
  }
  
  public void setYuvOutputImageFormat(ColorFormatType paramColorFormatType)
  {
    if (this.d == null) {
      return;
    }
    this.d.setYuvOutputImageFormat(paramColorFormatType);
  }
  
  public void setYuvOutputOrienation(ImageOrientation paramImageOrientation)
  {
    if (this.b == null) {
      return;
    }
    this.b.setYuvOutputOrienation(paramImageOrientation);
  }
  
  public void processFrame(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong)
  {
    if (this.a == null) {
      return;
    }
    this.a.processFrame(paramArrayOfByte, paramInt1, paramInt2, paramLong);
    TuSdkMonitor.glMonitor().checkGLFrameImage(" Engine processFrame yuv ", paramInt1, paramInt2);
    this.d.snatchFrame(paramArrayOfByte);
  }
  
  public int processFrame(int paramInt1, int paramInt2, int paramInt3, long paramLong)
  {
    if (this.a == null) {
      return paramInt1;
    }
    GLES20.glFinish();
    this.a.processFrame(paramInt1, paramInt2, paramInt3, paramLong);
    int i = this.d.getTerminalTexture();
    TuSdkMonitor.glMonitor().checkGLFrameImage(" Engine processFrame  texture ", paramInt2, paramInt3);
    if (i < 1) {
      return paramInt1;
    }
    return i;
  }
  
  public void snatchFrame(byte[] paramArrayOfByte)
  {
    if (this.d == null) {
      return;
    }
    this.d.snatchFrame(paramArrayOfByte);
  }
  
  public InterfaceOrientation getDeviceOrient()
  {
    if (this.b == null) {
      return null;
    }
    return this.b.getDeviceOrient();
  }
  
  public void takeShot()
  {
    if (this.c == null) {
      return;
    }
    this.c.takeShot();
  }
  
  public void setOriginalCaptureOrientation(boolean paramBoolean)
  {
    if (this.b == null) {
      return;
    }
    this.b.setOriginalCaptureOrientation(paramBoolean);
  }
  
  public void setOutputCaptureMirrorEnabled(boolean paramBoolean)
  {
    if (this.b == null) {
      return;
    }
    this.b.setOutputCaptureMirrorEnabled(paramBoolean);
  }
  
  public void asyncProcessPictureData(byte[] paramArrayOfByte, InterfaceOrientation paramInterfaceOrientation)
  {
    if ((this.c == null) && (this.c.getImageEngine() == null)) {
      return;
    }
    if (this.c.getLiveStickerPlayController() != null) {
      this.c.getLiveStickerPlayController().pauseAllStickers();
    }
    this.c.getImageEngine().asyncProcessPictureData(paramArrayOfByte, paramInterfaceOrientation);
  }
  
  public FaceAligment[] getFaceFeatures()
  {
    if (this.c == null) {
      return null;
    }
    return this.c.getFaceFeatures();
  }
  
  public void setFaceDetectionDelegate(TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate paramTuSdkVideoProcesserFaceDetectionDelegate)
  {
    if (this.c == null) {
      return;
    }
    this.c.setFaceDetectionDelegate(paramTuSdkVideoProcesserFaceDetectionDelegate);
  }
  
  public float getDeviceAngle()
  {
    if (this.b == null) {
      return 0.0F;
    }
    return this.b.getDeviceAngle();
  }
  
  public void addTerminalNode(SelesContext.SelesInput paramSelesInput)
  {
    if (this.c == null) {
      return;
    }
    this.c.addTerminalNode(paramSelesInput);
  }
  
  public void setMediaEffectDelegate(TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate paramTuSDKVideoProcessorMediaEffectDelegate)
  {
    if (this.c == null) {
      return;
    }
    this.c.setMediaEffectDelegate(paramTuSDKVideoProcessorMediaEffectDelegate);
  }
  
  public boolean addMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    return (this.c != null) && (this.c.addMediaEffectData(paramTuSdkMediaEffectData));
  }
  
  public boolean removeMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    return (this.c != null) && (this.c.removeMediaEffectData(paramTuSdkMediaEffectData));
  }
  
  public <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    if (this.c == null) {
      return null;
    }
    return this.c.mediaEffectsWithType(paramTuSdkMediaEffectDataType);
  }
  
  public List<TuSdkMediaEffectData> getAllMediaEffectData()
  {
    if (this.c == null) {
      return null;
    }
    return this.c.getAllMediaEffectData();
  }
  
  public void removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    if (this.c == null) {
      return;
    }
    this.c.removeMediaEffectsWithType(paramTuSdkMediaEffectDataType);
  }
  
  public void removeAllMediaEffects()
  {
    if (this.c == null) {
      return;
    }
    this.c.removeAllMediaEffects();
  }
  
  public boolean hasMediaAudioEffects()
  {
    if (this.c == null) {
      return false;
    }
    List localList = this.c.mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio);
    return (localList != null) && (localList.size() > 0);
  }
  
  public void setFilterChangedListener(TuSdkFilterEngine.TuSdkFilterEngineListener paramTuSdkFilterEngineListener)
  {
    if (this.c == null) {
      return;
    }
    this.c.setFilterChangedListener(paramTuSdkFilterEngineListener);
  }
  
  public boolean isGroupStickerUsed(StickerGroup paramStickerGroup)
  {
    return (this.c != null) && (this.c.getLiveStickerPlayController() != null) && (this.c.getLiveStickerPlayController().isGroupStickerUsed(paramStickerGroup));
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\engine\TuSdkFilterEngineImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */