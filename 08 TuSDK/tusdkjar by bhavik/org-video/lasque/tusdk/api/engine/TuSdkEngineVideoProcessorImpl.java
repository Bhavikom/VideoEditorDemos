package org.lasque.tusdk.api.engine;

import android.graphics.Bitmap;
import android.graphics.RectF;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate;
import org.lasque.tusdk.core.api.engine.TuSdkEngineOrientation;
import org.lasque.tusdk.core.api.engine.TuSdkEngineOutputImage;
import org.lasque.tusdk.core.api.engine.TuSdkEngineProcessor;
import org.lasque.tusdk.core.api.engine.TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate;
import org.lasque.tusdk.core.api.engine.TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionResultType;
import org.lasque.tusdk.core.detector.FrameDetectProcessor;
import org.lasque.tusdk.core.detector.FrameDetectProcessor.FrameDetectProcessorDelegate;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesParameters.FilterFacePositionInterface;
import org.lasque.tusdk.core.seles.SelesParameters.FilterStickerInterface;
import org.lasque.tusdk.core.seles.filters.SelesFrameDelayFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU.GpuType;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import org.lasque.tusdk.video.editor.TuSDKMediaEffectsManager;
import org.lasque.tusdk.video.editor.TuSDKMediaEffectsManager.OnFilterChangeListener;
import org.lasque.tusdk.video.editor.TuSDKMediaEffectsManagerImpl;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData.TuSdkMediaEffectDataType;
import org.lasque.tusdk.video.editor.TuSdkMediaFilterEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaParticleEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaSceneEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaStickerEffectData;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public class TuSdkEngineVideoProcessorImpl
  implements TuSdkEngineProcessor
{
  private TuSdkEngineOrientation a;
  private TuSdkEngineOutputImage b;
  private TuSdkImageEngine c;
  private FrameDetectProcessor d;
  private SelesFrameDelayFilter e = new SelesFrameDelayFilter();
  private FaceAligment[] f;
  private long g = 0L;
  private boolean h;
  private boolean i;
  private boolean j = false;
  private TuSDKComboFilterWrapChain k;
  private TuSDKMediaEffectsManager l;
  private TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate m;
  private TuSdkFilterEngine.TuSdkFilterEngineListener n;
  private TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate o;
  private TuSDKMediaEffectsManager.OnFilterChangeListener p = new TuSDKMediaEffectsManager.OnFilterChangeListener()
  {
    public void onFilterChanged(final FilterWrap paramAnonymousFilterWrap)
    {
      TuSdkEngineVideoProcessorImpl.a(TuSdkEngineVideoProcessorImpl.this);
      ThreadHelper.post(new Runnable()
      {
        public void run()
        {
          if (TuSdkEngineVideoProcessorImpl.b(TuSdkEngineVideoProcessorImpl.this) != null) {
            TuSdkEngineVideoProcessorImpl.b(TuSdkEngineVideoProcessorImpl.this).onFilterChanged(paramAnonymousFilterWrap);
          }
        }
      });
    }
  };
  private TuSdkImageEngine.TuSdkPictureDataCompletedListener q = new TuSdkImageEngine.TuSdkPictureDataCompletedListener()
  {
    public void onPictureDataCompleted(final IntBuffer paramAnonymousIntBuffer, final TuSdkSize paramAnonymousTuSdkSize)
    {
      ThreadHelper.post(new Runnable()
      {
        public void run()
        {
          if (TuSdkEngineVideoProcessorImpl.this.getLiveStickerPlayController() != null) {
            TuSdkEngineVideoProcessorImpl.this.getLiveStickerPlayController().resumeAllStickers();
          }
          if (TuSdkEngineVideoProcessorImpl.b(TuSdkEngineVideoProcessorImpl.this) != null) {
            TuSdkEngineVideoProcessorImpl.b(TuSdkEngineVideoProcessorImpl.this).onPictureDataCompleted(paramAnonymousIntBuffer, paramAnonymousTuSdkSize);
          }
        }
      });
    }
  };
  private FrameDetectProcessor.FrameDetectProcessorDelegate r = new FrameDetectProcessor.FrameDetectProcessorDelegate()
  {
    public void onFrameDetectedResult(FaceAligment[] paramAnonymousArrayOfFaceAligment, TuSdkSize paramAnonymousTuSdkSize, float paramAnonymousFloat, boolean paramAnonymousBoolean)
    {
      TuSdkEngineVideoProcessorImpl.a(TuSdkEngineVideoProcessorImpl.this, paramAnonymousArrayOfFaceAligment, paramAnonymousTuSdkSize, paramAnonymousFloat, paramAnonymousBoolean);
    }
    
    public void onOrientationChanged(InterfaceOrientation paramAnonymousInterfaceOrientation)
    {
      if (TuSdkEngineVideoProcessorImpl.c(TuSdkEngineVideoProcessorImpl.this) != null) {
        TuSdkEngineVideoProcessorImpl.c(TuSdkEngineVideoProcessorImpl.this).setDeviceOrient(paramAnonymousInterfaceOrientation);
      }
      TuSdkEngineVideoProcessorImpl.a(TuSdkEngineVideoProcessorImpl.this);
    }
  };
  
  public void setEngineRotation(TuSdkEngineOrientation paramTuSdkEngineOrientation)
  {
    if (paramTuSdkEngineOrientation == null) {
      return;
    }
    this.a = paramTuSdkEngineOrientation;
    b();
  }
  
  public void bindEngineOutput(TuSdkEngineOutputImage paramTuSdkEngineOutputImage)
  {
    if (this.e == null)
    {
      TLog.w("%s bindEngineOutput has released.", new Object[] { "TuSdkEngineVideoProcessorImpl" });
      return;
    }
    if (paramTuSdkEngineOutputImage == null) {
      return;
    }
    this.b = paramTuSdkEngineOutputImage;
    if (this.l == null)
    {
      List localList = this.b.getInputs();
      if ((localList == null) || (localList.size() < 1))
      {
        TLog.d("%s bindEngineOutput has not output", new Object[] { "TuSdkEngineVideoProcessorImpl" });
        return;
      }
      this.l = new TuSDKMediaEffectsManagerImpl();
      this.l.setMediaEffectDelegate(this.m);
      this.k = this.l.getFilterWrapChain();
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        SelesContext.SelesInput localSelesInput = (SelesContext.SelesInput)localIterator.next();
        this.l.addTerminalNode(localSelesInput);
      }
      this.e.addTarget(this.k.getFilter(), 0);
      b();
    }
  }
  
  public void setMediaEffectDelegate(TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate paramTuSDKVideoProcessorMediaEffectDelegate)
  {
    this.m = paramTuSDKVideoProcessorMediaEffectDelegate;
    if (this.l != null) {
      this.l.setMediaEffectDelegate(this.m);
    }
  }
  
  public void setFilterChangedListener(TuSdkFilterEngine.TuSdkFilterEngineListener paramTuSdkFilterEngineListener)
  {
    this.n = paramTuSdkFilterEngineListener;
  }
  
  public TuSdkImageEngine getImageEngine()
  {
    if ((this.k == null) || (this.a == null))
    {
      TLog.w("%s getImageEngine need setEngineRotation first or has released.", new Object[] { "TuSdkEngineVideoProcessorImpl" });
      return null;
    }
    if (this.c == null) {
      this.c = new TuSdkImageEngineImpl();
    }
    this.c.setFaceAligments(this.f);
    this.c.setEngineRotation(this.a);
    this.c.setFilter(this.k.clone());
    this.c.setListener(this.q);
    return this.c;
  }
  
  public void setDetectScale(float paramFloat)
  {
    FrameDetectProcessor.setDetectScale(paramFloat);
  }
  
  public void setEnableLiveSticker(boolean paramBoolean)
  {
    if ((!TuSdkGPU.isLiveStickerSupported()) && (paramBoolean))
    {
      TLog.w("%s setEnableLiveSticker Sorry, face feature is not supported on this device.", new Object[] { "TuSdkEngineVideoProcessorImpl" });
      return;
    }
    this.h = paramBoolean;
    b();
  }
  
  public void setEnableFacePlastic(boolean paramBoolean)
  {
    if ((!TuSdkGPU.isLiveStickerSupported()) && (paramBoolean))
    {
      TLog.w("%s setEnableFacePlastic Sorry, face feature is not supported on this device.", new Object[] { "TuSdkEngineVideoProcessorImpl" });
      return;
    }
    this.i = paramBoolean;
    b();
  }
  
  private boolean a()
  {
    return (this.h) || (this.i);
  }
  
  public void setSyncOutput(boolean paramBoolean)
  {
    this.j = paramBoolean;
    if (this.d != null) {
      this.d.setSyncOutput(paramBoolean);
    }
  }
  
  public SelesContext.SelesInput getInput()
  {
    return this.e;
  }
  
  public void setFaceDetectionDelegate(TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate paramTuSdkVideoProcesserFaceDetectionDelegate)
  {
    this.o = paramTuSdkVideoProcesserFaceDetectionDelegate;
  }
  
  public void release()
  {
    if (this.l != null)
    {
      this.l.release();
      this.l = null;
    }
    if (this.k != null)
    {
      this.k.destroy();
      this.k = null;
    }
    if (this.d != null)
    {
      this.d.setEnabled(false);
      this.d.destroy();
      this.d = null;
    }
    if (this.e != null)
    {
      this.e.destroy();
      this.e = null;
    }
    if (this.c != null)
    {
      this.c.release();
      this.c = null;
    }
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public void willProcessFrame(long paramLong)
  {
    if (this.a == null)
    {
      TLog.d("%s willProcessFrame need setEngineRotation first.", new Object[] { "TuSdkEngineVideoProcessorImpl" });
      return;
    }
    if (this.b == null)
    {
      TLog.d("%s willProcessFrame need bindEngineOutput first.", new Object[] { "TuSdkEngineVideoProcessorImpl" });
      return;
    }
    this.b.willProcessFrame(paramLong);
    if (this.d != null)
    {
      this.d.setInputTextureSize(this.a.getOutputSize());
      this.d.setInterfaceOrientation(this.a.getInterfaceOrientation());
    }
    this.l.updateEffectTimeLine(paramLong / 1000L, this.p);
  }
  
  private void b()
  {
    if (this.a == null) {
      return;
    }
    boolean bool = a();
    if ((!bool) && (this.d == null)) {
      return;
    }
    if (bool)
    {
      if (this.d == null)
      {
        this.d = new FrameDetectProcessor(TuSdkGPU.getGpuType().getPerformance());
        this.d.setSyncOutput(this.j);
        this.d.setDelegate(this.r);
        this.d.setEnabled(true);
        this.e.setFirstTarget(this.d.getSelesRotateShotOutput(), 0);
      }
      this.e.setDelaySize(1);
      this.g = System.currentTimeMillis();
    }
    else
    {
      if (this.e != null) {
        this.e.setDelaySize(0);
      }
      a(null, 0.0F);
    }
    if ((this.k instanceof SelesParameters.FilterStickerInterface)) {
      ((SelesParameters.FilterStickerInterface)this.k).setStickerVisibility(this.h);
    }
    if ((this.k instanceof TuSDKComboFilterWrapChain)) {
      this.k.setIsEnablePlastic(this.i);
    }
    if (this.d != null) {
      this.d.setEnabled(bool);
    }
  }
  
  private void a(FaceAligment[] paramArrayOfFaceAligment, TuSdkSize paramTuSdkSize, float paramFloat, boolean paramBoolean)
  {
    a(paramArrayOfFaceAligment, paramFloat);
  }
  
  private void c()
  {
    if ((this.k == null) || (this.a == null)) {
      return;
    }
    this.k.rotationTextures(this.a.getDeviceOrient());
  }
  
  private void a(FaceAligment[] paramArrayOfFaceAligment, float paramFloat)
  {
    if (a()) {
      if ((paramArrayOfFaceAligment == null) || (paramArrayOfFaceAligment.length == 0)) {
        a(TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionResultType.TuSDKVideoProcesserFaceDetectionResultTypeNoFaceDetected, 0);
      } else {
        a(TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionResultType.TuSDKVideoProcesserFaceDetectionResultTypeFaceDetected, paramArrayOfFaceAligment.length);
      }
    }
    this.f = paramArrayOfFaceAligment;
    if (this.a != null) {
      this.a.setDeviceAngle(paramFloat);
    }
    if ((this.k != null) && ((this.k instanceof SelesParameters.FilterFacePositionInterface))) {
      this.k.updateFaceFeatures(paramArrayOfFaceAligment, paramFloat);
    }
  }
  
  public FaceAligment[] getFaceFeatures()
  {
    return this.f;
  }
  
  public void addTerminalNode(SelesContext.SelesInput paramSelesInput)
  {
    if (this.k == null) {
      return;
    }
    this.k.addTerminalNode(paramSelesInput);
  }
  
  private void a(TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionResultType paramTuSdkVideoProcesserFaceDetectionResultType, int paramInt)
  {
    if (this.o == null) {
      return;
    }
    this.o.onFaceDetectionResult(paramTuSdkVideoProcesserFaceDetectionResultType, paramInt);
  }
  
  public LiveStickerPlayController getLiveStickerPlayController()
  {
    if (this.l == null) {
      return null;
    }
    return this.l.getLiveStickerPlayController();
  }
  
  public void removeAllLiveSticker()
  {
    if (this.l == null) {
      return;
    }
    this.l.removeAllLiveSticker();
  }
  
  public void showGroupSticker(StickerGroup paramStickerGroup)
  {
    if (this.l == null) {
      return;
    }
    if (!this.h)
    {
      TLog.e("%s Please set setEnableLiveSticker to true before use live sticker", new Object[] { "TuSdkEngineVideoProcessorImpl" });
      return;
    }
    TuSdkMediaStickerEffectData localTuSdkMediaStickerEffectData = new TuSdkMediaStickerEffectData(paramStickerGroup);
    localTuSdkMediaStickerEffectData.setAtTimeRange(TuSdkTimeRange.makeRange(0.0F, Float.MAX_VALUE));
    this.l.addMediaEffectData(localTuSdkMediaStickerEffectData);
  }
  
  public void setDisplayRect(RectF paramRectF, float paramFloat)
  {
    if (this.k == null) {
      return;
    }
    this.k.setDisplayRect(paramRectF, paramFloat);
  }
  
  public final synchronized void switchFilter(String paramString)
  {
    if (paramString == null) {
      return;
    }
    if (!a(paramString)) {
      return;
    }
    removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeParticle);
    removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeScene);
    removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter);
    Object localObject;
    if (FilterManager.shared().isSceneEffectFilter(paramString))
    {
      localObject = new TuSdkMediaSceneEffectData(paramString);
      ((TuSdkMediaSceneEffectData)localObject).setAtTimeRange(TuSdkTimeRange.makeRange(0.0F, Float.MAX_VALUE));
      addMediaEffectData((TuSdkMediaEffectData)localObject);
    }
    else if (FilterManager.shared().isParticleEffectFilter(paramString))
    {
      localObject = new TuSdkMediaParticleEffectData(paramString);
      ((TuSdkMediaParticleEffectData)localObject).setAtTimeRange(TuSdkTimeRange.makeRange(0.0F, Float.MAX_VALUE));
      addMediaEffectData((TuSdkMediaEffectData)localObject);
    }
    else
    {
      localObject = new TuSdkMediaFilterEffectData(paramString);
      ((TuSdkMediaFilterEffectData)localObject).setAtTimeRange(TuSdkTimeRange.makeRange(0.0F, Float.MAX_VALUE));
      addMediaEffectData((TuSdkMediaEffectData)localObject);
    }
  }
  
  private final boolean a(String paramString)
  {
    if ((FilterManager.shared().isSceneEffectFilter(paramString)) && (!SdkValid.shared.videoEditorEffectsfilterEnabled()))
    {
      TLog.e("You are not allowed to use effect filter, please see http://tusdk.com", new Object[0]);
      return false;
    }
    if ((FilterManager.shared().isParticleEffectFilter(paramString)) && (!SdkValid.shared.videoEditorParticleEffectsFilterEnabled()))
    {
      TLog.e("You are not allowed to use effect filter, please see http://tusdk.com", new Object[0]);
      return false;
    }
    return true;
  }
  
  public final boolean addMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    if (this.l == null) {
      return false;
    }
    return this.l.addMediaEffectData(paramTuSdkMediaEffectData);
  }
  
  public boolean removeMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    if (paramTuSdkMediaEffectData == null) {
      return false;
    }
    if (this.m != null) {
      this.m.didRemoveMediaEffect(Arrays.asList(new TuSdkMediaEffectData[] { paramTuSdkMediaEffectData }));
    }
    return this.l.removeMediaEffectData(paramTuSdkMediaEffectData);
  }
  
  public void removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    if (this.l == null) {
      return;
    }
    List localList = this.l.mediaEffectsWithType(paramTuSdkMediaEffectDataType);
    if ((this.m != null) && (localList != null) && (localList.size() > 0)) {
      this.m.didRemoveMediaEffect(localList);
    }
    this.l.removeMediaEffectsWithType(paramTuSdkMediaEffectDataType);
  }
  
  public <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    if (this.l == null) {
      return null;
    }
    return this.l.mediaEffectsWithType(paramTuSdkMediaEffectDataType);
  }
  
  public List<TuSdkMediaEffectData> getAllMediaEffectData()
  {
    if (this.l == null) {
      return null;
    }
    return this.l.getAllMediaEffectData();
  }
  
  public void removeAllMediaEffects()
  {
    if (this.l == null) {
      return;
    }
    this.l.removeAllMediaEffects();
  }
  
  public void takeShot()
  {
    if (this.k == null) {
      return;
    }
    Bitmap localBitmap = this.k.captureVideoImage();
    if (this.n != null) {
      this.n.onPreviewScreenShot(localBitmap);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\engine\TuSdkEngineVideoProcessorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */