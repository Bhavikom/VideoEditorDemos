// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.video.preproc.filter;

import android.hardware.Camera;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.utils.hardware.CameraHelper;
//import org.lasque.tusdk.video.editor.TuSdkMediaStickerEffectData;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFrameDelayFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesSurfaceTextureOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterManager;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker.LiveStickerPlayController;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.detector.FrameDetectProcessor;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSDKMediaEffectsManager;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSDKMediaEffectsManagerImpl;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSDKVideoProcessInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaFilterEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaParticleEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaSceneEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaStickerEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;

import java.util.List;
import java.util.Arrays;
//import org.lasque.tusdk.video.editor.TuSdkMediaFilterEffectData;
//import org.lasque.tusdk.video.editor.TuSdkMediaParticleEffectData;
//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
//import org.lasque.tusdk.video.editor.TuSdkMediaSceneEffectData;
//import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.seles.tusdk.FilterManager;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.SelesContext;
import java.util.LinkedList;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.util.Queue;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.filters.SelesFrameDelayFilter;
//import org.lasque.tusdk.core.seles.output.SelesSurfaceTextureOutput;
//import org.lasque.tusdk.core.detector.FrameDetectProcessor;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.video.editor.TuSDKMediaEffectsManagerImpl;
//import org.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;
import javax.microedition.khronos.egl.EGLContext;
//import org.lasque.tusdk.video.editor.TuSDKVideoProcessInterface;
//import org.lasque.tusdk.video.editor.TuSDKMediaEffectsManager;
//import org.lasque.tusdk.core.seles.sources.SelesOutput;

public abstract class TuSDKVideoProcesser extends SelesOutput implements TuSDKMediaEffectsManager.OnFilterChangeListener, TuSDKVideoProcessInterface
{
    protected EGLContext mCurrentEGLContext;
    protected TuSDKComboFilterWrapChain mFilterWrap;
    protected TuSDKMediaEffectsManagerImpl mMediaEffectsManager;
    protected boolean mIsFilterChanging;
    protected boolean mIsProcessingPictureData;
    protected FaceAligment[] mFaces;
    protected float mDeviceAngle;
    protected boolean mEnableLiveSticker;
    protected long mLastFaceDetection;
    protected boolean mEnableFaceDetection;
    protected TuSDKVideoProcesserFaceDetectionResultType mFaceDetectionResultType;
    protected InterfaceOrientation mInterfaceOrientation;
    protected InterfaceOrientation mDeviceOrient;
    protected FrameDetectProcessor mFrameDetector;
    protected SelesSurfaceTextureOutput mOutputFilter;
    private SelesFrameDelayFilter a;
    protected ImageOrientation mInputImageOrientation;
    protected ImageOrientation mOutputImageOrientation;
    protected boolean mIsOutputOriginalImageOrientation;
    protected ImageOrientation mOutputRotation;
    protected boolean mIsOriginalCaptureOrientation;
    protected boolean mIsOutputCaptureMirrorEnabled;
    protected boolean mHorizontallyMirrorFrontFacingCamera;
    protected boolean mHorizontallyMirrorRearFacingCamera;
    protected CameraConfigs.CameraFacing mCameraFacing;
    private boolean b;
    private final Queue<Runnable> c;
    private final Queue<Runnable> d;
    private TuSDKVideoProcesserFaceDetectionDelegate e;
    private TuSDKFilterChangedListener f;
    private TuSDKVideoProcessorMediaEffectDelegate g;
    private FrameDetectProcessor.FrameDetectProcessorDelegate h;
    
    protected abstract void updateOutputFilterOutputOrientation();
    
    public TuSDKVideoProcesser() {
        this.mLastFaceDetection = 0L;
        this.mInterfaceOrientation = InterfaceOrientation.Portrait;
        this.mDeviceOrient = InterfaceOrientation.Portrait;
        this.mOutputImageOrientation = ImageOrientation.Up;
        this.mIsOutputOriginalImageOrientation = true;
        this.mOutputRotation = ImageOrientation.Up;
        this.mIsOriginalCaptureOrientation = true;
        this.mIsOutputCaptureMirrorEnabled = false;
        this.mCameraFacing = CameraConfigs.CameraFacing.Front;
        this.b = true;
        this.h = new FrameDetectProcessor.FrameDetectProcessorDelegate() {
            @Override
            public void onFrameDetectedResult(final FaceAligment[] array, final TuSdkSize tuSdkSize, final float n, final boolean b) {
                TuSDKVideoProcesser.this.a(array, tuSdkSize, n, b);
            }
            
            public void onOrientationChanged(final InterfaceOrientation mDeviceOrient) {
                TuSDKVideoProcesser.this.mDeviceOrient = mDeviceOrient;
                TuSDKVideoProcesser.this.d();
            }
        };
        this.c = new LinkedList<Runnable>();
        this.d = new LinkedList<Runnable>();
        this.mHorizontallyMirrorFrontFacingCamera = true;
        this.mHorizontallyMirrorRearFacingCamera = false;
        this.updateOutputImageOrientation();
    }
    
    protected void init() {
        this.mCurrentEGLContext = SelesContext.currentEGLContext();
        this.b();
        this.mOutputFilter = new SelesSurfaceTextureOutput();
        if (this.mMediaEffectsManager == null) {
            (this.mMediaEffectsManager = new TuSDKMediaEffectsManagerImpl()).addTerminalNode((SelesContext.SelesInput)this.mOutputFilter);
            this.mFilterWrap = this.mMediaEffectsManager.getFilterWrapChain();
            this.a.addTarget((SelesContext.SelesInput)this.mFilterWrap.getFilter(), 0);
            if (this.g != null) {
                this.mMediaEffectsManager.setMediaEffectDelegate(this.g);
            }
        }
        if (this.mFrameDetector != null) {
            this.mFrameDetector.setEnabled(true);
            this.mFrameDetector.setInputTextureSize(this.getOutputImageSize());
        }
        this.updateOutputImageOrientation();
    }
    
    public SelesOutInput getOutput() {
        return (SelesOutInput)this.mOutputFilter;
    }
    
    protected void reset() {
        if (this.mMediaEffectsManager != null) {
            this.mMediaEffectsManager.release();
        }
        this.removeAllTargets();
        if (this.mFilterWrap != null) {
            this.mFilterWrap.destroy();
        }
        if (this.mOutputFilter != null) {
            this.mOutputFilter.destroy();
            this.mOutputFilter = null;
        }
        if (this.mFrameDetector != null) {
            this.mFrameDetector.setEnabled(false);
            this.mFrameDetector.destroy();
            this.mFrameDetector = null;
        }
        if (this.a != null) {
            this.removeTarget((SelesContext.SelesInput)this.a);
            this.a.destroy();
            this.a = null;
        }
        this.mInputTextureSize = TuSdkSize.create(0);
        this.destroyFramebuffer();
        SelesContext.destroyContext(this.mCurrentEGLContext);
    }
    
    public LiveStickerPlayController getLiveStickerPlayController() {
        return this.mMediaEffectsManager.getLiveStickerPlayController();
    }
    
    public void setInputImageSize(final TuSdkSize mInputTextureSize) {
        if (mInputTextureSize == null || !mInputTextureSize.isSize() || mInputTextureSize.equals((Object)this.mInputTextureSize)) {
            return;
        }
        this.mInputTextureSize = mInputTextureSize;
        this.destroyFramebuffer();
        this.c();
    }
    
    public void setFaceDetectionDelegate(final TuSDKVideoProcesserFaceDetectionDelegate e) {
        this.e = e;
    }
    
    public void setFilterChangedListener(final TuSDKFilterChangedListener f) {
        this.f = f;
    }
    
    public void setFilterProcessorMediaEffectDelegate(final TuSDKVideoProcessorMediaEffectDelegate g) {
        if (g == null) {
            return;
        }
        this.g = g;
    }
    
    public TuSDKVideoProcessorMediaEffectDelegate getFilterProcessorMediaEffectDelegate() {
        return this.g;
    }
    
    public TuSdkSize getInputImageSize() {
        return this.mInputTextureSize;
    }
    
    protected TuSdkSize getTargetInputImageSize() {
        return this.mInputTextureSize;
    }
    
    public void setHorizontallyMirrorFrontFacingCamera(final boolean mHorizontallyMirrorFrontFacingCamera) {
        this.mHorizontallyMirrorFrontFacingCamera = mHorizontallyMirrorFrontFacingCamera;
        this.updateOutputImageOrientation();
    }
    
    public void setHorizontallyMirrorRearFacingCamera(final boolean mHorizontallyMirrorRearFacingCamera) {
        this.mHorizontallyMirrorRearFacingCamera = mHorizontallyMirrorRearFacingCamera;
        this.updateOutputImageOrientation();
    }
    
    public void setOutputOriginalImageOrientation(final boolean mIsOutputOriginalImageOrientation) {
        this.mIsOutputOriginalImageOrientation = mIsOutputOriginalImageOrientation;
        this.updateOutputImageOrientation();
    }
    
    public void setInputImageOrientation(final ImageOrientation mInputImageOrientation) {
        this.mInputImageOrientation = mInputImageOrientation;
        this.updateOutputImageOrientation();
    }
    
    public void setOutputImageOrientation(final ImageOrientation mOutputImageOrientation) {
        this.mOutputImageOrientation = mOutputImageOrientation;
        this.mIsOutputOriginalImageOrientation = false;
        this.updateOutputImageOrientation();
    }
    
    public void setInterfaceOrientation(final InterfaceOrientation mInterfaceOrientation) {
        this.mInterfaceOrientation = mInterfaceOrientation;
        this.updateOutputImageOrientation();
    }
    
    public void setIsOriginalCaptureOrientation(final boolean mIsOriginalCaptureOrientation) {
        this.mIsOriginalCaptureOrientation = mIsOriginalCaptureOrientation;
    }
    
    public boolean isIsOriginalCaptureOrientation() {
        return this.mIsOriginalCaptureOrientation;
    }
    
    public void setCameraFacing(final CameraConfigs.CameraFacing mCameraFacing) {
        this.mCameraFacing = mCameraFacing;
        this.updateOutputImageOrientation();
    }
    
    public CameraConfigs.CameraFacing getCameraFacing() {
        return this.mCameraFacing;
    }
    
    public void switchCameraFacing() {
        this.setCameraFacing((this.mCameraFacing == CameraConfigs.CameraFacing.Front) ? CameraConfigs.CameraFacing.Back : CameraConfigs.CameraFacing.Front);
    }
    
    public void setIsOutputCaptureMirrorEnabled(final boolean mIsOutputCaptureMirrorEnabled) {
        this.mIsOutputCaptureMirrorEnabled = mIsOutputCaptureMirrorEnabled;
    }
    
    public void addTarget(final SelesContext.SelesInput selesInput, final int n) {
        super.addTarget(selesInput, n);
        if (selesInput != null) {
            selesInput.setInputRotation(this.mOutputRotation, n);
        }
    }
    
    protected void updateTargetsForVideoCameraUsingCacheTexture(final long n) {
        for (int i = 0; i < this.mTargets.size(); ++i) {
            final SelesContext.SelesInput selesInput = this.mTargets.get(i);
            if (selesInput.isEnabled()) {
                final int intValue = this.mTargetTextureIndices.get(i);
                selesInput.setInputRotation(this.mOutputRotation, intValue);
                if (selesInput != this.getTargetToIgnoreForUpdates()) {
                    selesInput.setInputSize(this.getTargetInputImageSize(), intValue);
                    selesInput.setCurrentlyReceivingMonochromeInput(selesInput.wantsMonochromeInput());
                }
                selesInput.setInputFramebuffer(this.mOutputFramebuffer, intValue);
            }
        }
        for (int j = 0; j < this.mTargets.size(); ++j) {
            final SelesContext.SelesInput selesInput2 = this.mTargets.get(j);
            if (selesInput2.isEnabled()) {
                if (selesInput2 != this.getTargetToIgnoreForUpdates()) {
                    selesInput2.newFrameReady(n, (int)this.mTargetTextureIndices.get(j));
                }
            }
        }
    }
    
    public void updateEffectTimeLine(final long n, final TuSDKMediaEffectsManager.OnFilterChangeListener onFilterChangeListener) {
        this.mMediaEffectsManager.updateEffectTimeLine(n, onFilterChangeListener);
    }
    
    protected void activateFramebuffer() {
        if (this.b || this.mOutputFramebuffer == null) {
            this.mOutputFramebuffer = this.a();
            this.b = false;
        }
        this.mOutputFramebuffer.activateFramebuffer();
    }
    
    private SelesFramebuffer a() {
        if (this.mOutputFramebuffer != null) {
            this.destroyFramebuffer();
        }
        final SelesFramebuffer fetchFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, this.mInputTextureSize);
        fetchFramebuffer.disableReferenceCounting();
        return fetchFramebuffer;
    }
    
    public void destroyFramebuffer() {
        if (this.mOutputFramebuffer != null) {
            this.mOutputFramebuffer.clearAllLocks();
            SelesContext.returnFramebufferToCache(this.mOutputFramebuffer);
            this.mOutputFramebuffer = null;
            this.b = true;
        }
    }
    
    private final boolean a(final String s) {
        if (FilterManager.shared().isSceneEffectFilter(s) && !SdkValid.shared.videoEditorEffectsfilterEnabled()) {
            TLog.e("You are not allowed to use effect filter, please see http://tusdk.com", new Object[0]);
            return false;
        }
        if (FilterManager.shared().isParticleEffectFilter(s) && !SdkValid.shared.videoEditorParticleEffectsFilterEnabled()) {
            TLog.e("You are not allowed to use effect filter, please see http://tusdk.com", new Object[0]);
            return false;
        }
        return true;
    }
    
    public boolean isFilterChanging() {
        return this.mIsFilterChanging;
    }
    
    public TuSDKComboFilterWrapChain getFilterWrap() {
        return this.mFilterWrap;
    }
    
    public final synchronized void switchFilter(final String s) {
        if (s == null) {
            return;
        }
        if (!this.a(s)) {
            return;
        }
        this.mIsFilterChanging = true;
        this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeParticle);
        this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeScene);
        this.removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter);
        if (FilterManager.shared().isSceneEffectFilter(s)) {
            final TuSdkMediaSceneEffectData tuSdkMediaSceneEffectData = new TuSdkMediaSceneEffectData(s);
            tuSdkMediaSceneEffectData.setAtTimeRange(TuSdkTimeRange.makeRange(0.0f, Float.MAX_VALUE));
            this.addMediaEffectData(tuSdkMediaSceneEffectData);
        }
        else if (FilterManager.shared().isParticleEffectFilter(s)) {
            final TuSdkMediaParticleEffectData tuSdkMediaParticleEffectData = new TuSdkMediaParticleEffectData(s);
            tuSdkMediaParticleEffectData.setAtTimeRange(TuSdkTimeRange.makeRange(0.0f, Float.MAX_VALUE));
            this.addMediaEffectData(tuSdkMediaParticleEffectData);
        }
        else {
            final TuSdkMediaFilterEffectData tuSdkMediaFilterEffectData = new TuSdkMediaFilterEffectData(s);
            tuSdkMediaFilterEffectData.setAtTimeRange(TuSdkTimeRange.makeRange(0.0f, Float.MAX_VALUE));
            this.addMediaEffectData(tuSdkMediaFilterEffectData);
        }
    }
    
    public final boolean addMediaEffectData(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        return this.mMediaEffectsManager != null && this.mMediaEffectsManager.addMediaEffectData(tuSdkMediaEffectData);
    }
    
    public boolean removeMediaEffectData(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        if (tuSdkMediaEffectData == null) {
            return false;
        }
        if (this.getFilterProcessorMediaEffectDelegate() != null) {
            this.getFilterProcessorMediaEffectDelegate().didRemoveMediaEffect(Arrays.asList(tuSdkMediaEffectData));
        }
        return this.mMediaEffectsManager.removeMediaEffectData(tuSdkMediaEffectData);
    }
    
    public void removeMediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType tuSdkMediaEffectDataType) {
        if (this.mMediaEffectsManager == null) {
            return;
        }
        final List<TuSdkMediaEffectData> mediaEffectsWithType = this.mMediaEffectsManager.mediaEffectsWithType(tuSdkMediaEffectDataType);
        if (mediaEffectsWithType == null || mediaEffectsWithType.size() == 0) {
            return;
        }
        this.mMediaEffectsManager.removeMediaEffectsWithType(tuSdkMediaEffectDataType);
        if (this.g != null) {
            this.g.didRemoveMediaEffect(mediaEffectsWithType);
        }
    }
    
    public <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType tuSdkMediaEffectDataType) {
        if (this.mMediaEffectsManager == null) {
            return null;
        }
        return this.mMediaEffectsManager.mediaEffectsWithType(tuSdkMediaEffectDataType);
    }
    
    public List<TuSdkMediaEffectData> getAllMediaEffectData() {
        if (this.mMediaEffectsManager == null) {
            return null;
        }
        return this.mMediaEffectsManager.getAllMediaEffectData();
    }
    
    public void removeAllMediaEffects() {
        if (this.mMediaEffectsManager == null) {
            return;
        }
        this.mMediaEffectsManager.removeAllMediaEffects();
    }
    
    public void onFilterChanged(final FilterWrap filterWrap) {
        this.mIsFilterChanging = false;
        this.d();
        if (this.f != null) {
            this.f.onFilterChanged(filterWrap);
        }
    }
    
    public final boolean isEnableLiveSticker() {
        return this.mEnableLiveSticker;
    }
    
    public final boolean hasMediaAudioEffects() {
        return this.mMediaEffectsManager != null && this.mMediaEffectsManager.mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio).size() > 0;
    }
    
    public final void setEnableLiveSticker(final boolean mEnableLiveSticker) {
        if (!this.isLiveStickerSupported() && mEnableLiveSticker) {
            TLog.w("Sorry, face feature is not supported on this device", new Object[0]);
            return;
        }
        this.setEnableFaceDetection(this.mEnableLiveSticker = mEnableLiveSticker);
    }
    
    public final void setEnableFaceDetection(final boolean mEnableFaceDetection) {
        this.mEnableFaceDetection = mEnableFaceDetection;
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                TuSDKVideoProcesser.this.c();
            }
        });
    }
    
    public final boolean isEnableFaceDetection() {
        return this.mEnableFaceDetection;
    }
    
    public final boolean isLiveStickerSupported() {
        return TuSdkGPU.isLiveStickerSupported();
    }
    
    public final boolean isFaceBeautySupported() {
        return TuSdkGPU.isFaceBeautySupported();
    }
    
    private SelesFrameDelayFilter b() {
        if (this.a == null) {
            this.addTarget((SelesContext.SelesInput)(this.a = new SelesFrameDelayFilter()));
        }
        return this.a;
    }
    
    private void c() {
        final boolean enableFaceDetection = this.isEnableFaceDetection();
        if (!enableFaceDetection && this.mFrameDetector == null) {
            return;
        }
        if (enableFaceDetection) {
            if (this.mFrameDetector == null) {
                (this.mFrameDetector = new FrameDetectProcessor(TuSdkGPU.getGpuType().getPerformance())).setDelegate(this.h);
                this.mFrameDetector.setEnabled(true);
                this.b().setFirstTarget((SelesContext.SelesInput)this.mFrameDetector.getSelesRotateShotOutput(), 0);
                this.b().setDelaySize(1);
            }
            this.mFrameDetector.setInputTextureSize(this.getOutputImageSize());
            this.mFrameDetector.setInterfaceOrientation(this.mInterfaceOrientation);
            this.mLastFaceDetection = System.currentTimeMillis();
        }
        else {
            if (this.a != null) {
                this.a.setDelaySize(0);
            }
            this.updateFaceFeatures(null, 0.0f);
        }
        if (this.mFilterWrap instanceof SelesParameters.FilterStickerInterface) {
            ((SelesParameters.FilterStickerInterface)this.mFilterWrap).setStickerVisibility(this.mEnableLiveSticker);
        }
        if (this.mFilterWrap instanceof TuSDKComboFilterWrapChain) {
            this.mFilterWrap.setIsEnablePlastic(enableFaceDetection);
        }
        if (this.mFrameDetector != null) {
            this.mFrameDetector.setEnabled(enableFaceDetection);
        }
    }
    
    public void setDetectScale(final float detectScale) {
        FrameDetectProcessor.setDetectScale(detectScale);
    }
    
    public final FaceAligment[] getFaceFeatures() {
        return this.mFaces;
    }
    
    public final float getDeviceAngle() {
        return this.mDeviceAngle;
    }
    
    private void a(final FaceAligment[] array, final TuSdkSize tuSdkSize, final float n, final boolean b) {
        this.updateFaceFeatures(array, n);
    }
    
    private void d() {
        if (this.mFilterWrap == null) {
            return;
        }
        this.mFilterWrap.rotationTextures(this.mDeviceOrient);
    }
    
    public InterfaceOrientation getDeviceOrient() {
        return this.mDeviceOrient;
    }
    
    public TuSdkSize getOutputImageSize() {
        final TuSdkSize tuSdkSize = new TuSdkSize(this.mInputTextureSize.width, this.mInputTextureSize.height);
        if (this.mOutputRotation != null && this.mOutputRotation.isTransposed()) {
            tuSdkSize.width = this.mInputTextureSize.height;
            tuSdkSize.height = this.mInputTextureSize.width;
        }
        return tuSdkSize;
    }
    
    public ImageOrientation getOutputRotation() {
        return this.mOutputRotation;
    }
    
    public void updateFaceFeatures(final FaceAligment[] mFaces, final float mDeviceAngle) {
        if (this.isEnableFaceDetection()) {
            if (mFaces == null || mFaces.length == 0) {
                this.a(TuSDKVideoProcesserFaceDetectionResultType.TuSDKVideoProcesserFaceDetectionResultTypeNoFaceDetected, 0);
            }
            else {
                this.a(TuSDKVideoProcesserFaceDetectionResultType.TuSDKVideoProcesserFaceDetectionResultTypeFaceDetected, mFaces.length);
            }
        }
        this.mFaces = mFaces;
        this.mDeviceAngle = mDeviceAngle;
        if (this.mFilterWrap != null && this.mFilterWrap instanceof SelesParameters.FilterFacePositionInterface) {
            ((SelesParameters.FilterFacePositionInterface)this.mFilterWrap).updateFaceFeatures(mFaces, this.mDeviceAngle);
        }
    }
    
    private void a(final TuSDKVideoProcesserFaceDetectionResultType mFaceDetectionResultType, final int n) {
        if (this.e == null) {
            return;
        }
        this.mFaceDetectionResultType = mFaceDetectionResultType;
        this.e.onFaceDetectionResult(mFaceDetectionResultType, n);
    }
    
    @Deprecated
    public void showGroupSticker(final StickerGroup stickerGroup) {
        if (!this.isEnableLiveSticker()) {
            TLog.e("Please set setEnableLiveSticker to true before use live sticker", new Object[0]);
            return;
        }
        final TuSdkMediaStickerEffectData tuSdkMediaStickerEffectData = new TuSdkMediaStickerEffectData(stickerGroup);
        tuSdkMediaStickerEffectData.setAtTimeRange(TuSdkTimeRange.makeRange(0.0f, Float.MAX_VALUE));
        this.mMediaEffectsManager.addMediaEffectData(tuSdkMediaStickerEffectData);
    }
    
    public void showGroupSticker(final TuSdkMediaStickerEffectData tuSdkMediaStickerEffectData) {
        if (!this.isEnableLiveSticker()) {
            TLog.e("Please set setEnableLiveSticker to true before use live sticker", new Object[0]);
            return;
        }
        this.mMediaEffectsManager.showGroupSticker(tuSdkMediaStickerEffectData);
    }
    
    public boolean isGroupStickerUsed(final StickerGroup stickerGroup) {
        return this.mMediaEffectsManager.getLiveStickerPlayController() != null && this.mMediaEffectsManager.getLiveStickerPlayController().isGroupStickerUsed(stickerGroup);
    }
    
    public void removeAllLiveSticker() {
        if (this.mMediaEffectsManager != null) {
            this.mMediaEffectsManager.removeAllLiveSticker();
        }
    }
    
    protected void updateOutputImageOrientation() {
        if (this.mInputImageOrientation != null) {
            this.mOutputRotation = this.mInputImageOrientation;
        }
        else {
            this.mOutputRotation = this.a(CameraHelper.firstCameraInfo(this.mCameraFacing), ContextUtils.getInterfaceRotation(TuSdkContext.context()), this.mHorizontallyMirrorRearFacingCamera, this.mHorizontallyMirrorFrontFacingCamera, InterfaceOrientation.Portrait);
        }
        this.updateOutputFilterOutputOrientation();
        this.c();
        if (this.a != null) {
            this.a.flush();
        }
    }
    
    private ImageOrientation a(final Camera.CameraInfo cameraInfo, InterfaceOrientation portrait, final boolean b, final boolean b2, InterfaceOrientation portrait2) {
        if (portrait == null) {
            portrait = InterfaceOrientation.Portrait;
        }
        if (portrait2 == null) {
            portrait2 = InterfaceOrientation.Portrait;
        }
        int orientation = 0;
        int n = 1;
        if (cameraInfo != null) {
            orientation = cameraInfo.orientation;
            n = ((cameraInfo.facing == 0) ? 1 : 0);
        }
        int degree = portrait.getDegree();
        if (portrait2 != null) {
            degree += portrait2.getDegree();
        }
        if (n != 0) {
            final InterfaceOrientation withDegrees = InterfaceOrientation.getWithDegrees(orientation - degree);
            if (b) {
                switch (withDegrees.ordinal()) {
                    case 1: {
                        return ImageOrientation.DownMirrored;
                    }
                    case 2: {
                        return ImageOrientation.LeftMirrored;
                    }
                    case 3: {
                        return ImageOrientation.RightMirrored;
                    }
                    default: {
                        return ImageOrientation.UpMirrored;
                    }
                }
            }
            else {
                switch (withDegrees.ordinal()) {
                    case 1: {
                        return ImageOrientation.Up;
                    }
                    case 2: {
                        return ImageOrientation.Left;
                    }
                    case 3: {
                        return ImageOrientation.Right;
                    }
                    default: {
                        return ImageOrientation.Down;
                    }
                }
            }
        }
        else {
            final InterfaceOrientation withDegrees2 = InterfaceOrientation.getWithDegrees(orientation + degree);
            if (b2) {
                switch (withDegrees2.ordinal()) {
                    case 1: {
                        return ImageOrientation.UpMirrored;
                    }
                    case 2: {
                        return ImageOrientation.LeftMirrored;
                    }
                    case 3: {
                        return ImageOrientation.RightMirrored;
                    }
                    default: {
                        return ImageOrientation.DownMirrored;
                    }
                }
            }
            else {
                switch (withDegrees2.ordinal()) {
                    case 1: {
                        return ImageOrientation.Down;
                    }
                    case 2: {
                        return ImageOrientation.Left;
                    }
                    case 3: {
                        return ImageOrientation.Right;
                    }
                    default: {
                        return ImageOrientation.Up;
                    }
                }
            }
        }
    }
    
    protected void runPendingOnDrawTasks() {
        this.a(this.c);
    }
    
    protected void runPendingOnDrawEndTasks() {
        this.a(this.d);
    }
    
    protected boolean isOnDrawTasksEmpty() {
        boolean empty = false;
        synchronized (this.c) {
            empty = this.c.isEmpty();
        }
        return empty;
    }
    
    private void a(final Queue<Runnable> queue) {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
        }
    }
    
    public void runOnDraw(final Runnable runnable) {
        synchronized (this.c) {
            this.c.add(runnable);
        }
    }
    
    public void runOnDrawEnd(final Runnable runnable) {
        synchronized (this.d) {
            this.d.add(runnable);
        }
    }
    
    public interface TuSDKVideoProcessorMediaEffectDelegate
    {
        void didApplyingMediaEffect(final TuSdkMediaEffectData p0);
        
        void didRemoveMediaEffect(final List<TuSdkMediaEffectData> p0);
    }
    
    public interface TuSDKFilterChangedListener
    {
        void onFilterChanged(final FilterWrap p0);
    }
    
    public interface TuSDKVideoProcesserFaceDetectionDelegate
    {
        void onFaceDetectionResult(final TuSDKVideoProcesserFaceDetectionResultType p0, final int p1);
    }
    
    public enum TuSDKVideoProcesserFaceDetectionResultType
    {
        TuSDKVideoProcesserFaceDetectionResultTypeFaceDetected, 
        TuSDKVideoProcesserFaceDetectionResultTypeNoFaceDetected;
    }
}
