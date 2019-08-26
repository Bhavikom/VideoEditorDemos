package org.lasque.tusdk.api.video.preproc.filter;

import android.hardware.Camera.CameraInfo;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.microedition.khronos.egl.EGLContext;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.detector.FrameDetectProcessor;
import org.lasque.tusdk.core.detector.FrameDetectProcessor.FrameDetectProcessorDelegate;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.SelesParameters.FilterFacePositionInterface;
import org.lasque.tusdk.core.seles.SelesParameters.FilterStickerInterface;
import org.lasque.tusdk.core.seles.filters.SelesFrameDelayFilter;
import org.lasque.tusdk.core.seles.output.SelesSurfaceTextureOutput;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesOutput;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU.GpuType;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import org.lasque.tusdk.video.editor.TuSDKMediaEffectsManager.OnFilterChangeListener;
import org.lasque.tusdk.video.editor.TuSDKMediaEffectsManagerImpl;
import org.lasque.tusdk.video.editor.TuSDKVideoProcessInterface;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData.TuSdkMediaEffectDataType;
import org.lasque.tusdk.video.editor.TuSdkMediaFilterEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaParticleEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaSceneEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaStickerEffectData;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public abstract class TuSDKVideoProcesser
  extends SelesOutput
  implements TuSDKMediaEffectsManager.OnFilterChangeListener, TuSDKVideoProcessInterface
{
  protected EGLContext mCurrentEGLContext;
  protected TuSDKComboFilterWrapChain mFilterWrap;
  protected TuSDKMediaEffectsManagerImpl mMediaEffectsManager;
  protected boolean mIsFilterChanging;
  protected boolean mIsProcessingPictureData;
  protected FaceAligment[] mFaces;
  protected float mDeviceAngle;
  protected boolean mEnableLiveSticker;
  protected long mLastFaceDetection = 0L;
  protected boolean mEnableFaceDetection;
  protected TuSDKVideoProcesserFaceDetectionResultType mFaceDetectionResultType;
  protected InterfaceOrientation mInterfaceOrientation = InterfaceOrientation.Portrait;
  protected InterfaceOrientation mDeviceOrient = InterfaceOrientation.Portrait;
  protected FrameDetectProcessor mFrameDetector;
  protected SelesSurfaceTextureOutput mOutputFilter;
  private SelesFrameDelayFilter a;
  protected ImageOrientation mInputImageOrientation;
  protected ImageOrientation mOutputImageOrientation = ImageOrientation.Up;
  protected boolean mIsOutputOriginalImageOrientation = true;
  protected ImageOrientation mOutputRotation = ImageOrientation.Up;
  protected boolean mIsOriginalCaptureOrientation = true;
  protected boolean mIsOutputCaptureMirrorEnabled = false;
  protected boolean mHorizontallyMirrorFrontFacingCamera = true;
  protected boolean mHorizontallyMirrorRearFacingCamera = false;
  protected CameraConfigs.CameraFacing mCameraFacing = CameraConfigs.CameraFacing.Front;
  private boolean b = true;
  private final Queue<Runnable> c = new LinkedList();
  private final Queue<Runnable> d = new LinkedList();
  private TuSDKVideoProcesserFaceDetectionDelegate e;
  private TuSDKFilterChangedListener f;
  private TuSDKVideoProcessorMediaEffectDelegate g;
  private FrameDetectProcessor.FrameDetectProcessorDelegate h = new FrameDetectProcessor.FrameDetectProcessorDelegate()
  {
    public void onFrameDetectedResult(FaceAligment[] paramAnonymousArrayOfFaceAligment, TuSdkSize paramAnonymousTuSdkSize, float paramAnonymousFloat, boolean paramAnonymousBoolean)
    {
      TuSDKVideoProcesser.a(TuSDKVideoProcesser.this, paramAnonymousArrayOfFaceAligment, paramAnonymousTuSdkSize, paramAnonymousFloat, paramAnonymousBoolean);
    }
    
    public void onOrientationChanged(InterfaceOrientation paramAnonymousInterfaceOrientation)
    {
      TuSDKVideoProcesser.this.mDeviceOrient = paramAnonymousInterfaceOrientation;
      TuSDKVideoProcesser.b(TuSDKVideoProcesser.this);
    }
  };
  
  protected abstract void updateOutputFilterOutputOrientation();
  
  public TuSDKVideoProcesser()
  {
    updateOutputImageOrientation();
  }
  
  protected void init()
  {
    this.mCurrentEGLContext = SelesContext.currentEGLContext();
    b();
    this.mOutputFilter = new SelesSurfaceTextureOutput();
    if (this.mMediaEffectsManager == null)
    {
      this.mMediaEffectsManager = new TuSDKMediaEffectsManagerImpl();
      this.mMediaEffectsManager.addTerminalNode(this.mOutputFilter);
      this.mFilterWrap = this.mMediaEffectsManager.getFilterWrapChain();
      this.a.addTarget(this.mFilterWrap.getFilter(), 0);
      if (this.g != null) {
        this.mMediaEffectsManager.setMediaEffectDelegate(this.g);
      }
    }
    if (this.mFrameDetector != null)
    {
      this.mFrameDetector.setEnabled(true);
      this.mFrameDetector.setInputTextureSize(getOutputImageSize());
    }
    updateOutputImageOrientation();
  }
  
  public SelesOutInput getOutput()
  {
    return this.mOutputFilter;
  }
  
  protected void reset()
  {
    if (this.mMediaEffectsManager != null) {
      this.mMediaEffectsManager.release();
    }
    removeAllTargets();
    if (this.mFilterWrap != null) {
      this.mFilterWrap.destroy();
    }
    if (this.mOutputFilter != null)
    {
      this.mOutputFilter.destroy();
      this.mOutputFilter = null;
    }
    if (this.mFrameDetector != null)
    {
      this.mFrameDetector.setEnabled(false);
      this.mFrameDetector.destroy();
      this.mFrameDetector = null;
    }
    if (this.a != null)
    {
      removeTarget(this.a);
      this.a.destroy();
      this.a = null;
    }
    this.mInputTextureSize = TuSdkSize.create(0);
    destroyFramebuffer();
    SelesContext.destroyContext(this.mCurrentEGLContext);
  }
  
  public LiveStickerPlayController getLiveStickerPlayController()
  {
    return this.mMediaEffectsManager.getLiveStickerPlayController();
  }
  
  public void setInputImageSize(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize()) || (paramTuSdkSize.equals(this.mInputTextureSize))) {
      return;
    }
    this.mInputTextureSize = paramTuSdkSize;
    destroyFramebuffer();
    c();
  }
  
  public void setFaceDetectionDelegate(TuSDKVideoProcesserFaceDetectionDelegate paramTuSDKVideoProcesserFaceDetectionDelegate)
  {
    this.e = paramTuSDKVideoProcesserFaceDetectionDelegate;
  }
  
  public void setFilterChangedListener(TuSDKFilterChangedListener paramTuSDKFilterChangedListener)
  {
    this.f = paramTuSDKFilterChangedListener;
  }
  
  public void setFilterProcessorMediaEffectDelegate(TuSDKVideoProcessorMediaEffectDelegate paramTuSDKVideoProcessorMediaEffectDelegate)
  {
    if (paramTuSDKVideoProcessorMediaEffectDelegate == null) {
      return;
    }
    this.g = paramTuSDKVideoProcessorMediaEffectDelegate;
  }
  
  public TuSDKVideoProcessorMediaEffectDelegate getFilterProcessorMediaEffectDelegate()
  {
    return this.g;
  }
  
  public TuSdkSize getInputImageSize()
  {
    return this.mInputTextureSize;
  }
  
  protected TuSdkSize getTargetInputImageSize()
  {
    return this.mInputTextureSize;
  }
  
  public void setHorizontallyMirrorFrontFacingCamera(boolean paramBoolean)
  {
    this.mHorizontallyMirrorFrontFacingCamera = paramBoolean;
    updateOutputImageOrientation();
  }
  
  public void setHorizontallyMirrorRearFacingCamera(boolean paramBoolean)
  {
    this.mHorizontallyMirrorRearFacingCamera = paramBoolean;
    updateOutputImageOrientation();
  }
  
  public void setOutputOriginalImageOrientation(boolean paramBoolean)
  {
    this.mIsOutputOriginalImageOrientation = paramBoolean;
    updateOutputImageOrientation();
  }
  
  public void setInputImageOrientation(ImageOrientation paramImageOrientation)
  {
    this.mInputImageOrientation = paramImageOrientation;
    updateOutputImageOrientation();
  }
  
  public void setOutputImageOrientation(ImageOrientation paramImageOrientation)
  {
    this.mOutputImageOrientation = paramImageOrientation;
    this.mIsOutputOriginalImageOrientation = false;
    updateOutputImageOrientation();
  }
  
  public void setInterfaceOrientation(InterfaceOrientation paramInterfaceOrientation)
  {
    this.mInterfaceOrientation = paramInterfaceOrientation;
    updateOutputImageOrientation();
  }
  
  public void setIsOriginalCaptureOrientation(boolean paramBoolean)
  {
    this.mIsOriginalCaptureOrientation = paramBoolean;
  }
  
  public boolean isIsOriginalCaptureOrientation()
  {
    return this.mIsOriginalCaptureOrientation;
  }
  
  public void setCameraFacing(CameraConfigs.CameraFacing paramCameraFacing)
  {
    this.mCameraFacing = paramCameraFacing;
    updateOutputImageOrientation();
  }
  
  public CameraConfigs.CameraFacing getCameraFacing()
  {
    return this.mCameraFacing;
  }
  
  public void switchCameraFacing()
  {
    setCameraFacing(this.mCameraFacing == CameraConfigs.CameraFacing.Front ? CameraConfigs.CameraFacing.Back : CameraConfigs.CameraFacing.Front);
  }
  
  public void setIsOutputCaptureMirrorEnabled(boolean paramBoolean)
  {
    this.mIsOutputCaptureMirrorEnabled = paramBoolean;
  }
  
  public void addTarget(SelesContext.SelesInput paramSelesInput, int paramInt)
  {
    super.addTarget(paramSelesInput, paramInt);
    if (paramSelesInput != null) {
      paramSelesInput.setInputRotation(this.mOutputRotation, paramInt);
    }
  }
  
  protected void updateTargetsForVideoCameraUsingCacheTexture(long paramLong)
  {
    int i = 0;
    int j = this.mTargets.size();
    SelesContext.SelesInput localSelesInput;
    int k;
    while (i < j)
    {
      localSelesInput = (SelesContext.SelesInput)this.mTargets.get(i);
      if (localSelesInput.isEnabled())
      {
        k = ((Integer)this.mTargetTextureIndices.get(i)).intValue();
        localSelesInput.setInputRotation(this.mOutputRotation, k);
        if (localSelesInput != getTargetToIgnoreForUpdates())
        {
          localSelesInput.setInputSize(getTargetInputImageSize(), k);
          localSelesInput.setCurrentlyReceivingMonochromeInput(localSelesInput.wantsMonochromeInput());
        }
        localSelesInput.setInputFramebuffer(this.mOutputFramebuffer, k);
      }
      i++;
    }
    i = 0;
    j = this.mTargets.size();
    while (i < j)
    {
      localSelesInput = (SelesContext.SelesInput)this.mTargets.get(i);
      if ((localSelesInput.isEnabled()) && (localSelesInput != getTargetToIgnoreForUpdates()))
      {
        k = ((Integer)this.mTargetTextureIndices.get(i)).intValue();
        localSelesInput.newFrameReady(paramLong, k);
      }
      i++;
    }
  }
  
  public void updateEffectTimeLine(long paramLong, TuSDKMediaEffectsManager.OnFilterChangeListener paramOnFilterChangeListener)
  {
    this.mMediaEffectsManager.updateEffectTimeLine(paramLong, paramOnFilterChangeListener);
  }
  
  protected void activateFramebuffer()
  {
    if ((this.b) || (this.mOutputFramebuffer == null))
    {
      this.mOutputFramebuffer = a();
      this.b = false;
    }
    this.mOutputFramebuffer.activateFramebuffer();
  }
  
  private SelesFramebuffer a()
  {
    if (this.mOutputFramebuffer != null) {
      destroyFramebuffer();
    }
    SelesFramebuffer localSelesFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, this.mInputTextureSize);
    localSelesFramebuffer.disableReferenceCounting();
    return localSelesFramebuffer;
  }
  
  public void destroyFramebuffer()
  {
    if (this.mOutputFramebuffer != null)
    {
      this.mOutputFramebuffer.clearAllLocks();
      SelesContext.returnFramebufferToCache(this.mOutputFramebuffer);
      this.mOutputFramebuffer = null;
      this.b = true;
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
  
  public boolean isFilterChanging()
  {
    return this.mIsFilterChanging;
  }
  
  public TuSDKComboFilterWrapChain getFilterWrap()
  {
    return this.mFilterWrap;
  }
  
  public final synchronized void switchFilter(String paramString)
  {
    if (paramString == null) {
      return;
    }
    if (!a(paramString)) {
      return;
    }
    this.mIsFilterChanging = true;
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
  
  public final boolean addMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    if (this.mMediaEffectsManager == null) {
      return false;
    }
    return this.mMediaEffectsManager.addMediaEffectData(paramTuSdkMediaEffectData);
  }
  
  public boolean removeMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    if (paramTuSdkMediaEffectData == null) {
      return false;
    }
    if (getFilterProcessorMediaEffectDelegate() != null) {
      getFilterProcessorMediaEffectDelegate().didRemoveMediaEffect(Arrays.asList(new TuSdkMediaEffectData[] { paramTuSdkMediaEffectData }));
    }
    return this.mMediaEffectsManager.removeMediaEffectData(paramTuSdkMediaEffectData);
  }
  
  public void removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    if (this.mMediaEffectsManager == null) {
      return;
    }
    List localList = this.mMediaEffectsManager.mediaEffectsWithType(paramTuSdkMediaEffectDataType);
    if ((localList == null) || (localList.size() == 0)) {
      return;
    }
    this.mMediaEffectsManager.removeMediaEffectsWithType(paramTuSdkMediaEffectDataType);
    if (this.g != null) {
      this.g.didRemoveMediaEffect(localList);
    }
  }
  
  public <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    if (this.mMediaEffectsManager == null) {
      return null;
    }
    return this.mMediaEffectsManager.mediaEffectsWithType(paramTuSdkMediaEffectDataType);
  }
  
  public List<TuSdkMediaEffectData> getAllMediaEffectData()
  {
    if (this.mMediaEffectsManager == null) {
      return null;
    }
    return this.mMediaEffectsManager.getAllMediaEffectData();
  }
  
  public void removeAllMediaEffects()
  {
    if (this.mMediaEffectsManager == null) {
      return;
    }
    this.mMediaEffectsManager.removeAllMediaEffects();
  }
  
  public void onFilterChanged(FilterWrap paramFilterWrap)
  {
    this.mIsFilterChanging = false;
    d();
    if (this.f != null) {
      this.f.onFilterChanged(paramFilterWrap);
    }
  }
  
  public final boolean isEnableLiveSticker()
  {
    return this.mEnableLiveSticker;
  }
  
  public final boolean hasMediaAudioEffects()
  {
    return (this.mMediaEffectsManager != null) && (this.mMediaEffectsManager.mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio).size() > 0);
  }
  
  public final void setEnableLiveSticker(boolean paramBoolean)
  {
    if ((!isLiveStickerSupported()) && (paramBoolean))
    {
      TLog.w("Sorry, face feature is not supported on this device", new Object[0]);
      return;
    }
    this.mEnableLiveSticker = paramBoolean;
    setEnableFaceDetection(paramBoolean);
  }
  
  public final void setEnableFaceDetection(boolean paramBoolean)
  {
    this.mEnableFaceDetection = paramBoolean;
    runOnDraw(new Runnable()
    {
      public void run()
      {
        TuSDKVideoProcesser.a(TuSDKVideoProcesser.this);
      }
    });
  }
  
  public final boolean isEnableFaceDetection()
  {
    return this.mEnableFaceDetection;
  }
  
  public final boolean isLiveStickerSupported()
  {
    return TuSdkGPU.isLiveStickerSupported();
  }
  
  public final boolean isFaceBeautySupported()
  {
    return TuSdkGPU.isFaceBeautySupported();
  }
  
  private SelesFrameDelayFilter b()
  {
    if (this.a == null)
    {
      this.a = new SelesFrameDelayFilter();
      addTarget(this.a);
    }
    return this.a;
  }
  
  private void c()
  {
    boolean bool = isEnableFaceDetection();
    if ((!bool) && (this.mFrameDetector == null)) {
      return;
    }
    if (bool)
    {
      if (this.mFrameDetector == null)
      {
        this.mFrameDetector = new FrameDetectProcessor(TuSdkGPU.getGpuType().getPerformance());
        this.mFrameDetector.setDelegate(this.h);
        this.mFrameDetector.setEnabled(true);
        b().setFirstTarget(this.mFrameDetector.getSelesRotateShotOutput(), 0);
        b().setDelaySize(1);
      }
      this.mFrameDetector.setInputTextureSize(getOutputImageSize());
      this.mFrameDetector.setInterfaceOrientation(this.mInterfaceOrientation);
      this.mLastFaceDetection = System.currentTimeMillis();
    }
    else
    {
      if (this.a != null) {
        this.a.setDelaySize(0);
      }
      updateFaceFeatures(null, 0.0F);
    }
    if ((this.mFilterWrap instanceof SelesParameters.FilterStickerInterface)) {
      ((SelesParameters.FilterStickerInterface)this.mFilterWrap).setStickerVisibility(this.mEnableLiveSticker);
    }
    if ((this.mFilterWrap instanceof TuSDKComboFilterWrapChain)) {
      this.mFilterWrap.setIsEnablePlastic(bool);
    }
    if (this.mFrameDetector != null) {
      this.mFrameDetector.setEnabled(bool);
    }
  }
  
  public void setDetectScale(float paramFloat)
  {
    FrameDetectProcessor.setDetectScale(paramFloat);
  }
  
  public final FaceAligment[] getFaceFeatures()
  {
    return this.mFaces;
  }
  
  public final float getDeviceAngle()
  {
    return this.mDeviceAngle;
  }
  
  private void a(FaceAligment[] paramArrayOfFaceAligment, TuSdkSize paramTuSdkSize, float paramFloat, boolean paramBoolean)
  {
    updateFaceFeatures(paramArrayOfFaceAligment, paramFloat);
  }
  
  private void d()
  {
    if (this.mFilterWrap == null) {
      return;
    }
    this.mFilterWrap.rotationTextures(this.mDeviceOrient);
  }
  
  public InterfaceOrientation getDeviceOrient()
  {
    return this.mDeviceOrient;
  }
  
  public TuSdkSize getOutputImageSize()
  {
    TuSdkSize localTuSdkSize = new TuSdkSize(this.mInputTextureSize.width, this.mInputTextureSize.height);
    if ((this.mOutputRotation != null) && (this.mOutputRotation.isTransposed()))
    {
      localTuSdkSize.width = this.mInputTextureSize.height;
      localTuSdkSize.height = this.mInputTextureSize.width;
    }
    return localTuSdkSize;
  }
  
  public ImageOrientation getOutputRotation()
  {
    return this.mOutputRotation;
  }
  
  public void updateFaceFeatures(FaceAligment[] paramArrayOfFaceAligment, float paramFloat)
  {
    if (isEnableFaceDetection()) {
      if ((paramArrayOfFaceAligment == null) || (paramArrayOfFaceAligment.length == 0)) {
        a(TuSDKVideoProcesserFaceDetectionResultType.TuSDKVideoProcesserFaceDetectionResultTypeNoFaceDetected, 0);
      } else {
        a(TuSDKVideoProcesserFaceDetectionResultType.TuSDKVideoProcesserFaceDetectionResultTypeFaceDetected, paramArrayOfFaceAligment.length);
      }
    }
    this.mFaces = paramArrayOfFaceAligment;
    this.mDeviceAngle = paramFloat;
    if ((this.mFilterWrap != null) && ((this.mFilterWrap instanceof SelesParameters.FilterFacePositionInterface))) {
      this.mFilterWrap.updateFaceFeatures(paramArrayOfFaceAligment, this.mDeviceAngle);
    }
  }
  
  private void a(TuSDKVideoProcesserFaceDetectionResultType paramTuSDKVideoProcesserFaceDetectionResultType, int paramInt)
  {
    if (this.e == null) {
      return;
    }
    this.mFaceDetectionResultType = paramTuSDKVideoProcesserFaceDetectionResultType;
    this.e.onFaceDetectionResult(paramTuSDKVideoProcesserFaceDetectionResultType, paramInt);
  }
  
  @Deprecated
  public void showGroupSticker(StickerGroup paramStickerGroup)
  {
    if (!isEnableLiveSticker())
    {
      TLog.e("Please set setEnableLiveSticker to true before use live sticker", new Object[0]);
      return;
    }
    TuSdkMediaStickerEffectData localTuSdkMediaStickerEffectData = new TuSdkMediaStickerEffectData(paramStickerGroup);
    localTuSdkMediaStickerEffectData.setAtTimeRange(TuSdkTimeRange.makeRange(0.0F, Float.MAX_VALUE));
    this.mMediaEffectsManager.addMediaEffectData(localTuSdkMediaStickerEffectData);
  }
  
  public void showGroupSticker(TuSdkMediaStickerEffectData paramTuSdkMediaStickerEffectData)
  {
    if (!isEnableLiveSticker())
    {
      TLog.e("Please set setEnableLiveSticker to true before use live sticker", new Object[0]);
      return;
    }
    this.mMediaEffectsManager.showGroupSticker(paramTuSdkMediaStickerEffectData);
  }
  
  public boolean isGroupStickerUsed(StickerGroup paramStickerGroup)
  {
    if (this.mMediaEffectsManager.getLiveStickerPlayController() != null) {
      return this.mMediaEffectsManager.getLiveStickerPlayController().isGroupStickerUsed(paramStickerGroup);
    }
    return false;
  }
  
  public void removeAllLiveSticker()
  {
    if (this.mMediaEffectsManager != null) {
      this.mMediaEffectsManager.removeAllLiveSticker();
    }
  }
  
  protected void updateOutputImageOrientation()
  {
    if (this.mInputImageOrientation != null)
    {
      this.mOutputRotation = this.mInputImageOrientation;
    }
    else
    {
      Camera.CameraInfo localCameraInfo = CameraHelper.firstCameraInfo(this.mCameraFacing);
      this.mOutputRotation = a(localCameraInfo, ContextUtils.getInterfaceRotation(TuSdkContext.context()), this.mHorizontallyMirrorRearFacingCamera, this.mHorizontallyMirrorFrontFacingCamera, InterfaceOrientation.Portrait);
    }
    updateOutputFilterOutputOrientation();
    c();
    if (this.a != null) {
      this.a.flush();
    }
  }
  
  private ImageOrientation a(Camera.CameraInfo paramCameraInfo, InterfaceOrientation paramInterfaceOrientation1, boolean paramBoolean1, boolean paramBoolean2, InterfaceOrientation paramInterfaceOrientation2)
  {
    if (paramInterfaceOrientation1 == null) {
      paramInterfaceOrientation1 = InterfaceOrientation.Portrait;
    }
    if (paramInterfaceOrientation2 == null) {
      paramInterfaceOrientation2 = InterfaceOrientation.Portrait;
    }
    int i = 0;
    int j = 1;
    if (paramCameraInfo != null)
    {
      i = paramCameraInfo.orientation;
      j = paramCameraInfo.facing == 0 ? 1 : 0;
    }
    int k = paramInterfaceOrientation1.getDegree();
    if (paramInterfaceOrientation2 != null) {
      k += paramInterfaceOrientation2.getDegree();
    }
    if (j != 0)
    {
      localInterfaceOrientation = InterfaceOrientation.getWithDegrees(i - k);
      if (paramBoolean1)
      {
        switch (3.a[localInterfaceOrientation.ordinal()])
        {
        case 1: 
          return ImageOrientation.DownMirrored;
        case 2: 
          return ImageOrientation.LeftMirrored;
        case 3: 
          return ImageOrientation.RightMirrored;
        }
        return ImageOrientation.UpMirrored;
      }
      switch (3.a[localInterfaceOrientation.ordinal()])
      {
      case 1: 
        return ImageOrientation.Up;
      case 2: 
        return ImageOrientation.Left;
      case 3: 
        return ImageOrientation.Right;
      }
      return ImageOrientation.Down;
    }
    InterfaceOrientation localInterfaceOrientation = InterfaceOrientation.getWithDegrees(i + k);
    if (paramBoolean2)
    {
      switch (3.a[localInterfaceOrientation.ordinal()])
      {
      case 1: 
        return ImageOrientation.UpMirrored;
      case 2: 
        return ImageOrientation.LeftMirrored;
      case 3: 
        return ImageOrientation.RightMirrored;
      }
      return ImageOrientation.DownMirrored;
    }
    switch (3.a[localInterfaceOrientation.ordinal()])
    {
    case 1: 
      return ImageOrientation.Down;
    case 2: 
      return ImageOrientation.Left;
    case 3: 
      return ImageOrientation.Right;
    }
    return ImageOrientation.Up;
  }
  
  protected void runPendingOnDrawTasks()
  {
    a(this.c);
  }
  
  protected void runPendingOnDrawEndTasks()
  {
    a(this.d);
  }
  
  protected boolean isOnDrawTasksEmpty()
  {
    boolean bool = false;
    synchronized (this.c)
    {
      bool = this.c.isEmpty();
    }
    return bool;
  }
  
  private void a(Queue<Runnable> paramQueue)
  {
    synchronized (paramQueue)
    {
      while (!paramQueue.isEmpty()) {
        ((Runnable)paramQueue.poll()).run();
      }
    }
  }
  
  public void runOnDraw(Runnable paramRunnable)
  {
    synchronized (this.c)
    {
      this.c.add(paramRunnable);
    }
  }
  
  public void runOnDrawEnd(Runnable paramRunnable)
  {
    synchronized (this.d)
    {
      this.d.add(paramRunnable);
    }
  }
  
  public static abstract interface TuSDKVideoProcessorMediaEffectDelegate
  {
    public abstract void didApplyingMediaEffect(TuSdkMediaEffectData paramTuSdkMediaEffectData);
    
    public abstract void didRemoveMediaEffect(List<TuSdkMediaEffectData> paramList);
  }
  
  public static abstract interface TuSDKFilterChangedListener
  {
    public abstract void onFilterChanged(FilterWrap paramFilterWrap);
  }
  
  public static abstract interface TuSDKVideoProcesserFaceDetectionDelegate
  {
    public abstract void onFaceDetectionResult(TuSDKVideoProcesser.TuSDKVideoProcesserFaceDetectionResultType paramTuSDKVideoProcesserFaceDetectionResultType, int paramInt);
  }
  
  public static enum TuSDKVideoProcesserFaceDetectionResultType
  {
    private TuSDKVideoProcesserFaceDetectionResultType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\video\preproc\filter\TuSDKVideoProcesser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */