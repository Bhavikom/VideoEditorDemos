// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.engine;

import android.graphics.Bitmap;
import java.util.Arrays;
//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.video.editor.TuSdkMediaFilterEffectData;
//import org.lasque.tusdk.video.editor.TuSdkMediaParticleEffectData;
//import org.lasque.tusdk.video.editor.TuSdkMediaSceneEffectData;
//import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import android.graphics.RectF;
//import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
//import org.lasque.tusdk.video.editor.TuSdkMediaStickerEffectData;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
//import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineOutputImage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineProcessor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFrameDelayFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterManager;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker.LiveStickerPlayController;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.detector.FrameDetectProcessor;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSDKMediaEffectsManager;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSDKMediaEffectsManagerImpl;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaFilterEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaParticleEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaSceneEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaStickerEffectData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;

import java.nio.IntBuffer;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import java.util.Iterator;
import java.util.List;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.video.editor.TuSDKMediaEffectsManagerImpl;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser;
//import org.lasque.tusdk.video.editor.TuSDKMediaEffectsManager;
//import org.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.seles.filters.SelesFrameDelayFilter;
//import org.lasque.tusdk.core.detector.FrameDetectProcessor;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineOutputImage;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineOrientation;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineProcessor;

public class TuSdkEngineVideoProcessorImpl implements TuSdkEngineProcessor
{
    private TuSdkEngineOrientation a;
    private TuSdkEngineOutputImage b;
    private TuSdkImageEngine c;
    private FrameDetectProcessor d;
    private SelesFrameDelayFilter e;
    private FaceAligment[] f;
    private long g;
    private boolean h;
    private boolean i;
    private boolean j;
    private TuSDKComboFilterWrapChain k;
    private TuSDKMediaEffectsManager l;
    private TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate m;
    private TuSdkFilterEngine.TuSdkFilterEngineListener n;
    private TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate o;
    private TuSDKMediaEffectsManager.OnFilterChangeListener p;
    private TuSdkImageEngine.TuSdkPictureDataCompletedListener q;
    private FrameDetectProcessor.FrameDetectProcessorDelegate r;
    
    public void setEngineRotation(final TuSdkEngineOrientation a) {
        if (a == null) {
            return;
        }
        this.a = a;
        this.b();
    }
    
    public void bindEngineOutput(final TuSdkEngineOutputImage b) {
        if (this.e == null) {
            TLog.w("%s bindEngineOutput has released.", new Object[] { "TuSdkEngineVideoProcessorImpl" });
            return;
        }
        if (b == null) {
            return;
        }
        this.b = b;
        if (this.l == null) {
            final List inputs = this.b.getInputs();
            if (inputs == null || inputs.size() < 1) {
                TLog.d("%s bindEngineOutput has not output", new Object[] { "TuSdkEngineVideoProcessorImpl" });
                return;
            }
            (this.l = new TuSDKMediaEffectsManagerImpl()).setMediaEffectDelegate(this.m);
            this.k = this.l.getFilterWrapChain();
            final Iterator<SelesContext.SelesInput> iterator = inputs.iterator();
            while (iterator.hasNext()) {
                this.l.addTerminalNode(iterator.next());
            }
            this.e.addTarget((SelesContext.SelesInput)this.k.getFilter(), 0);
            this.b();
        }
    }
    
    public void setMediaEffectDelegate(final TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate m) {
        this.m = m;
        if (this.l != null) {
            this.l.setMediaEffectDelegate(this.m);
        }
    }
    
    public void setFilterChangedListener(final TuSdkFilterEngine.TuSdkFilterEngineListener n) {
        this.n = n;
    }
    
    public TuSdkImageEngine getImageEngine() {
        if (this.k == null || this.a == null) {
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
    
    public void setDetectScale(final float detectScale) {
        FrameDetectProcessor.setDetectScale(detectScale);
    }
    
    public void setEnableLiveSticker(final boolean h) {
        if (!TuSdkGPU.isLiveStickerSupported() && h) {
            TLog.w("%s setEnableLiveSticker Sorry, face feature is not supported on this device.", new Object[] { "TuSdkEngineVideoProcessorImpl" });
            return;
        }
        this.h = h;
        this.b();
    }
    
    public void setEnableFacePlastic(final boolean i) {
        if (!TuSdkGPU.isLiveStickerSupported() && i) {
            TLog.w("%s setEnableFacePlastic Sorry, face feature is not supported on this device.", new Object[] { "TuSdkEngineVideoProcessorImpl" });
            return;
        }
        this.i = i;
        this.b();
    }
    
    private boolean a() {
        return this.h || this.i;
    }
    
    public void setSyncOutput(final boolean b) {
        this.j = b;
        if (this.d != null) {
            this.d.setSyncOutput(b);
        }
    }
    
    public SelesContext.SelesInput getInput() {
        return (SelesContext.SelesInput)this.e;
    }
    
    public void setFaceDetectionDelegate(final TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate o) {
        this.o = o;
    }
    
    public TuSdkEngineVideoProcessorImpl() {
        this.g = 0L;
        this.j = false;
        this.p = new TuSDKMediaEffectsManager.OnFilterChangeListener() {
            @Override
            public void onFilterChanged(final FilterWrap filterWrap) {
                TuSdkEngineVideoProcessorImpl.this.c();
                ThreadHelper.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (TuSdkEngineVideoProcessorImpl.this.n != null) {
                            TuSdkEngineVideoProcessorImpl.this.n.onFilterChanged(filterWrap);
                        }
                    }
                });
            }
        };
        this.q = new TuSdkImageEngine.TuSdkPictureDataCompletedListener() {
            @Override
            public void onPictureDataCompleted(final IntBuffer intBuffer, final TuSdkSize tuSdkSize) {
                ThreadHelper.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (TuSdkEngineVideoProcessorImpl.this.getLiveStickerPlayController() != null) {
                            TuSdkEngineVideoProcessorImpl.this.getLiveStickerPlayController().resumeAllStickers();
                        }
                        if (TuSdkEngineVideoProcessorImpl.this.n != null) {
                            TuSdkEngineVideoProcessorImpl.this.n.onPictureDataCompleted(intBuffer, tuSdkSize);
                        }
                    }
                });
            }
        };
        this.r = new FrameDetectProcessor.FrameDetectProcessorDelegate() {
            @Override
            public void onFrameDetectedResult(final FaceAligment[] array, final TuSdkSize tuSdkSize, final float n, final boolean b) {
                TuSdkEngineVideoProcessorImpl.this.a(array, tuSdkSize, n, b);
            }
            
            public void onOrientationChanged(final InterfaceOrientation deviceOrient) {
                if (TuSdkEngineVideoProcessorImpl.this.a != null) {
                    TuSdkEngineVideoProcessorImpl.this.a.setDeviceOrient(deviceOrient);
                }
                TuSdkEngineVideoProcessorImpl.this.c();
            }
        };
        this.e = new SelesFrameDelayFilter();
    }
    
    public void release() {
        if (this.l != null) {
            this.l.release();
            this.l = null;
        }
        if (this.k != null) {
            this.k.destroy();
            this.k = null;
        }
        if (this.d != null) {
            this.d.setEnabled(false);
            this.d.destroy();
            this.d = null;
        }
        if (this.e != null) {
            this.e.destroy();
            this.e = null;
        }
        if (this.c != null) {
            this.c.release();
            this.c = null;
        }
    }
    
    @Override
    protected void finalize() {
        this.release();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    public void willProcessFrame(final long n) {
        if (this.a == null) {
            TLog.d("%s willProcessFrame need setEngineRotation first.", new Object[] { "TuSdkEngineVideoProcessorImpl" });
            return;
        }
        if (this.b == null) {
            TLog.d("%s willProcessFrame need bindEngineOutput first.", new Object[] { "TuSdkEngineVideoProcessorImpl" });
            return;
        }
        this.b.willProcessFrame(n);
        if (this.d != null) {
            this.d.setInputTextureSize(this.a.getOutputSize());
            this.d.setInterfaceOrientation(this.a.getInterfaceOrientation());
        }
        this.l.updateEffectTimeLine(n / 1000L, this.p);
    }
    
    private void b() {
        if (this.a == null) {
            return;
        }
        final boolean a = this.a();
        if (!a && this.d == null) {
            return;
        }
        if (a) {
            if (this.d == null) {
                (this.d = new FrameDetectProcessor(TuSdkGPU.getGpuType().getPerformance())).setSyncOutput(this.j);
                this.d.setDelegate(this.r);
                this.d.setEnabled(true);
                this.e.setFirstTarget((SelesContext.SelesInput)this.d.getSelesRotateShotOutput(), 0);
            }
            this.e.setDelaySize(1);
            this.g = System.currentTimeMillis();
        }
        else {
            if (this.e != null) {
                this.e.setDelaySize(0);
            }
            this.a(null, 0.0f);
        }
        if (this.k instanceof SelesParameters.FilterStickerInterface) {
            ((SelesParameters.FilterStickerInterface)this.k).setStickerVisibility(this.h);
        }
        if (this.k instanceof TuSDKComboFilterWrapChain) {
            this.k.setIsEnablePlastic(this.i);
        }
        if (this.d != null) {
            this.d.setEnabled(a);
        }
    }
    
    private void a(final FaceAligment[] array, final TuSdkSize tuSdkSize, final float n, final boolean b) {
        this.a(array, n);
    }
    
    private void c() {
        if (this.k == null || this.a == null) {
            return;
        }
        this.k.rotationTextures(this.a.getDeviceOrient());
    }
    
    private void a(final FaceAligment[] f, final float deviceAngle) {
        if (this.a()) {
            if (f == null || f.length == 0) {
                this.a(TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionResultType.TuSDKVideoProcesserFaceDetectionResultTypeNoFaceDetected, 0);
            }
            else {
                this.a(TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionResultType.TuSDKVideoProcesserFaceDetectionResultTypeFaceDetected, f.length);
            }
        }
        this.f = f;
        if (this.a != null) {
            this.a.setDeviceAngle(deviceAngle);
        }
        if (this.k != null && this.k instanceof SelesParameters.FilterFacePositionInterface) {
            this.k.updateFaceFeatures(f, deviceAngle);
        }
    }
    
    public FaceAligment[] getFaceFeatures() {
        return this.f;
    }
    
    public void addTerminalNode(final SelesContext.SelesInput selesInput) {
        if (this.k == null) {
            return;
        }
        this.k.addTerminalNode(selesInput);
    }
    
    private void a(final TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionResultType tuSdkVideoProcesserFaceDetectionResultType, final int n) {
        if (this.o == null) {
            return;
        }
        this.o.onFaceDetectionResult(tuSdkVideoProcesserFaceDetectionResultType, n);
    }
    
    public LiveStickerPlayController getLiveStickerPlayController() {
        if (this.l == null) {
            return null;
        }
        return this.l.getLiveStickerPlayController();
    }
    
    public void removeAllLiveSticker() {
        if (this.l == null) {
            return;
        }
        this.l.removeAllLiveSticker();
    }
    
    public void showGroupSticker(final StickerGroup stickerGroup) {
        if (this.l == null) {
            return;
        }
        if (!this.h) {
            TLog.e("%s Please set setEnableLiveSticker to true before use live sticker", new Object[] { "TuSdkEngineVideoProcessorImpl" });
            return;
        }
        final TuSdkMediaStickerEffectData tuSdkMediaStickerEffectData = new TuSdkMediaStickerEffectData(stickerGroup);
        tuSdkMediaStickerEffectData.setAtTimeRange(TuSdkTimeRange.makeRange(0.0f, Float.MAX_VALUE));
        this.l.addMediaEffectData(tuSdkMediaStickerEffectData);
    }
    
    public void setDisplayRect(final RectF rectF, final float n) {
        if (this.k == null) {
            return;
        }
        this.k.setDisplayRect(rectF, n);
    }
    
    public final synchronized void switchFilter(final String s) {
        if (s == null) {
            return;
        }
        if (!this.a(s)) {
            return;
        }
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
    
    public final boolean addMediaEffectData(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        return this.l != null && this.l.addMediaEffectData(tuSdkMediaEffectData);
    }
    
    public boolean removeMediaEffectData(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        if (tuSdkMediaEffectData == null) {
            return false;
        }
        if (this.m != null) {
            this.m.didRemoveMediaEffect(Arrays.asList(tuSdkMediaEffectData));
        }
        return this.l.removeMediaEffectData(tuSdkMediaEffectData);
    }
    
    public void removeMediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType tuSdkMediaEffectDataType) {
        if (this.l == null) {
            return;
        }
        final List<TuSdkMediaEffectData> mediaEffectsWithType = this.l.mediaEffectsWithType(tuSdkMediaEffectDataType);
        if (this.m != null && mediaEffectsWithType != null && mediaEffectsWithType.size() > 0) {
            this.m.didRemoveMediaEffect(mediaEffectsWithType);
        }
        this.l.removeMediaEffectsWithType(tuSdkMediaEffectDataType);
    }
    
    public <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType tuSdkMediaEffectDataType) {
        if (this.l == null) {
            return null;
        }
        return this.l.mediaEffectsWithType(tuSdkMediaEffectDataType);
    }
    
    public List<TuSdkMediaEffectData> getAllMediaEffectData() {
        if (this.l == null) {
            return null;
        }
        return this.l.getAllMediaEffectData();
    }
    
    public void removeAllMediaEffects() {
        if (this.l == null) {
            return;
        }
        this.l.removeAllMediaEffects();
    }
    
    public void takeShot() {
        if (this.k == null) {
            return;
        }
        final Bitmap captureVideoImage = this.k.captureVideoImage();
        if (this.n != null) {
            this.n.onPreviewScreenShot(captureVideoImage);
        }
    }
}
