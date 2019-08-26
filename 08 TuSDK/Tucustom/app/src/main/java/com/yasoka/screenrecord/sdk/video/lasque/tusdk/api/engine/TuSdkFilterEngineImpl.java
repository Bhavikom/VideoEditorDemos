// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.engine;

import java.util.List;
//import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
//import org.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.face.FaceAligment;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.utils.monitor.TuSdkMonitor;
//import org.lasque.tusdk.core.type.ColorFormatType;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngine;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineInputImage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineInputSurfaceImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineInputTextureImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineInputYUVDataImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineOrientationImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineOutputImage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineOutputImageImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineProcessor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ColorFormatType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.monitor.TuSdkMonitor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaEffectData;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineInputTextureImpl;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineInputSurfaceImpl;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineOutputImageImpl;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineProcessor;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineInputImage;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineInputYUVDataImpl;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineOrientationImpl;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineImpl;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineOutputImage;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineOrientation;
//import org.lasque.tusdk.core.api.engine.TuSdkEngine;

public class TuSdkFilterEngineImpl implements TuSdkFilterEngine
{
    private TuSdkEngine a;
    private TuSdkEngineOrientation b;
    private TuSdkEngineVideoProcessorImpl c;
    private TuSdkEngineOutputImage d;
    private TuSdkFilterEngineListener e;
    private int f;
    private int g;
    
    @Override
    public void setListener(final TuSdkFilterEngineListener e) {
        this.e = e;
        if (this.c != null) {
            this.c.setFilterChangedListener(this.e);
        }
    }
    
    public TuSdkFilterEngineImpl(final boolean b) {
        TLog.dump("TuSdkFilterEngine create() managedGLLifecycle : %s", new Object[] { b });
        this.a = (TuSdkEngine)new TuSdkEngineImpl(b);
        (this.b = (TuSdkEngineOrientation)new TuSdkEngineOrientationImpl()).setHorizontallyMirrorFrontFacingCamera(true);
        this.a.setEngineOrientation(this.b);
        this.a.setEngineInputImage((TuSdkEngineInputImage)new TuSdkEngineInputYUVDataImpl());
        this.c = new TuSdkEngineVideoProcessorImpl();
        this.a.setEngineProcessor((TuSdkEngineProcessor)this.c);
        this.d = (TuSdkEngineOutputImage)new TuSdkEngineOutputImageImpl();
        this.a.setEngineOutputImage(this.d);
    }
    
    public TuSdkFilterEngineImpl(final boolean b, final boolean b2) {
        TLog.dump("TuSdkFilterEngine create()  isOESTexture : %s  managedGLLifecycle : %s", new Object[] { b, b2 });
        this.a = (TuSdkEngine)new TuSdkEngineImpl(b2);
        (this.b = (TuSdkEngineOrientation)new TuSdkEngineOrientationImpl()).setHorizontallyMirrorFrontFacingCamera(true);
        this.a.setEngineOrientation(this.b);
        this.a.setEngineInputImage((TuSdkEngineInputImage)(b ? new TuSdkEngineInputSurfaceImpl() : new TuSdkEngineInputTextureImpl()));
        this.c = new TuSdkEngineVideoProcessorImpl();
        this.a.setEngineProcessor((TuSdkEngineProcessor)this.c);
        this.d = (TuSdkEngineOutputImage)new TuSdkEngineOutputImageImpl();
        this.a.setEngineOutputImage(this.d);
    }
    
    @Override
    public CameraConfigs.CameraFacing getCameraFacing() {
        if (this.b == null) {
            return CameraConfigs.CameraFacing.Back;
        }
        return this.b.getCameraFacing();
    }
    
    @Override
    public void setCameraFacing(final CameraConfigs.CameraFacing cameraFacing) {
        if (this.b == null) {
            return;
        }
        this.b.setCameraFacing(cameraFacing);
    }
    
    @Override
    public void setInterfaceOrientation(final InterfaceOrientation interfaceOrientation) {
        if (this.b == null) {
            return;
        }
        this.b.setInterfaceOrientation(interfaceOrientation);
    }
    
    @Override
    public void setInputImageOrientation(final ImageOrientation inputOrientation) {
        if (this.b == null) {
            return;
        }
        this.b.setInputOrientation(inputOrientation);
    }
    
    @Override
    public void setOutputImageOrientation(final ImageOrientation outputOrientation) {
        if (this.b == null) {
            return;
        }
        this.b.setOutputOrientation(outputOrientation);
    }
    
    @Override
    public void setCordinateBuilder(final SelesVerticeCoordinateCorpBuilder inputTextureCoordinateBuilder) {
        if (this.a == null) {
            return;
        }
        this.a.setInputTextureCoordinateBuilder(inputTextureCoordinateBuilder);
    }
    
    @Override
    public TuSdkSize getOutputImageSize() {
        return this.b.getOutputSize();
    }
    
    @Override
    public void release() {
        if (this.a != null) {
            this.c.release();
            this.a.release();
            this.a = null;
            TLog.dump("TuSdkFilterEngine release()", new Object[0]);
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
    
    @Override
    public void onSurfaceCreated() {
        if (this.a == null) {
            return;
        }
        this.a.prepareInGlThread();
    }
    
    @Override
    public void onSurfaceChanged(final int f, final int g) {
        this.f = f;
        this.g = g;
    }
    
    @Override
    public void setDisplayRect(final RectF rectF, final float n) {
        this.c.setDisplayRect(rectF, n);
    }
    
    @Override
    public void setEnableLiveSticker(final boolean b) {
        if (this.c == null) {
            return;
        }
        this.c.setEnableLiveSticker(b);
        this.c.setEnableFacePlastic(b);
    }
    
    @Override
    public void setEnableFaceDetection(final boolean enableFacePlastic) {
        if (this.c == null) {
            return;
        }
        this.c.setEnableFacePlastic(enableFacePlastic);
    }
    
    @Override
    public void setDetectScale(final float detectScale) {
        if (this.c == null) {
            return;
        }
        this.c.setDetectScale(detectScale);
    }
    
    @Override
    public void switchFilter(final String s) {
        if (this.c == null) {
            return;
        }
        this.c.switchFilter(s);
    }
    
    @Override
    public void removeAllLiveSticker() {
        if (this.c == null) {
            return;
        }
        this.c.removeAllLiveSticker();
    }
    
    @Override
    public void showGroupSticker(final StickerGroup stickerGroup) {
        if (this.c == null) {
            return;
        }
        this.c.showGroupSticker(stickerGroup);
    }
    
    @Override
    public void setEnableOutputYUVData(final boolean enableOutputYUVData) {
        if (this.d == null) {
            return;
        }
        this.d.setEnableOutputYUVData(enableOutputYUVData);
    }
    
    @Override
    public void setYuvOutputImageFormat(final ColorFormatType yuvOutputImageFormat) {
        if (this.d == null) {
            return;
        }
        this.d.setYuvOutputImageFormat(yuvOutputImageFormat);
    }
    
    @Override
    public void setYuvOutputOrienation(final ImageOrientation yuvOutputOrienation) {
        if (this.b == null) {
            return;
        }
        this.b.setYuvOutputOrienation(yuvOutputOrienation);
    }
    
    @Override
    public void processFrame(final byte[] array, final int n, final int n2, final long n3) {
        if (this.a == null) {
            return;
        }
        this.a.processFrame(array, n, n2, n3);
        TuSdkMonitor.glMonitor().checkGLFrameImage(" Engine processFrame yuv ", n, n2);
        this.d.snatchFrame(array);
    }
    
    @Override
    public int processFrame(final int n, final int n2, final int n3, final long n4) {
        if (this.a == null) {
            return n;
        }
        GLES20.glFinish();
        this.a.processFrame(n, n2, n3, n4);
        final int terminalTexture = this.d.getTerminalTexture();
        TuSdkMonitor.glMonitor().checkGLFrameImage(" Engine processFrame  texture ", n2, n3);
        if (terminalTexture < 1) {
            return n;
        }
        return terminalTexture;
    }
    
    @Override
    public void snatchFrame(final byte[] array) {
        if (this.d == null) {
            return;
        }
        this.d.snatchFrame(array);
    }
    
    @Override
    public InterfaceOrientation getDeviceOrient() {
        if (this.b == null) {
            return null;
        }
        return this.b.getDeviceOrient();
    }
    
    @Override
    public void takeShot() {
        if (this.c == null) {
            return;
        }
        this.c.takeShot();
    }
    
    @Override
    public void setOriginalCaptureOrientation(final boolean originalCaptureOrientation) {
        if (this.b == null) {
            return;
        }
        this.b.setOriginalCaptureOrientation(originalCaptureOrientation);
    }
    
    @Override
    public void setOutputCaptureMirrorEnabled(final boolean outputCaptureMirrorEnabled) {
        if (this.b == null) {
            return;
        }
        this.b.setOutputCaptureMirrorEnabled(outputCaptureMirrorEnabled);
    }
    
    @Override
    public void asyncProcessPictureData(final byte[] array, final InterfaceOrientation interfaceOrientation) {
        if (this.c == null && this.c.getImageEngine() == null) {
            return;
        }
        if (this.c.getLiveStickerPlayController() != null) {
            this.c.getLiveStickerPlayController().pauseAllStickers();
        }
        this.c.getImageEngine().asyncProcessPictureData(array, interfaceOrientation);
    }
    
    @Override
    public FaceAligment[] getFaceFeatures() {
        if (this.c == null) {
            return null;
        }
        return this.c.getFaceFeatures();
    }
    
    @Override
    public void setFaceDetectionDelegate(final TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate faceDetectionDelegate) {
        if (this.c == null) {
            return;
        }
        this.c.setFaceDetectionDelegate(faceDetectionDelegate);
    }
    
    @Override
    public float getDeviceAngle() {
        if (this.b == null) {
            return 0.0f;
        }
        return this.b.getDeviceAngle();
    }
    
    @Override
    public void addTerminalNode(final SelesContext.SelesInput selesInput) {
        if (this.c == null) {
            return;
        }
        this.c.addTerminalNode(selesInput);
    }
    
    @Override
    public void setMediaEffectDelegate(final TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate mediaEffectDelegate) {
        if (this.c == null) {
            return;
        }
        this.c.setMediaEffectDelegate(mediaEffectDelegate);
    }
    
    @Override
    public boolean addMediaEffectData(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        return this.c != null && this.c.addMediaEffectData(tuSdkMediaEffectData);
    }
    
    @Override
    public boolean removeMediaEffectData(final TuSdkMediaEffectData tuSdkMediaEffectData) {
        return this.c != null && this.c.removeMediaEffectData(tuSdkMediaEffectData);
    }
    
    @Override
    public <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType tuSdkMediaEffectDataType) {
        if (this.c == null) {
            return null;
        }
        return this.c.mediaEffectsWithType(tuSdkMediaEffectDataType);
    }
    
    @Override
    public List<TuSdkMediaEffectData> getAllMediaEffectData() {
        if (this.c == null) {
            return null;
        }
        return this.c.getAllMediaEffectData();
    }
    
    @Override
    public void removeMediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType tuSdkMediaEffectDataType) {
        if (this.c == null) {
            return;
        }
        this.c.removeMediaEffectsWithType(tuSdkMediaEffectDataType);
    }
    
    @Override
    public void removeAllMediaEffects() {
        if (this.c == null) {
            return;
        }
        this.c.removeAllMediaEffects();
    }
    
    @Override
    public boolean hasMediaAudioEffects() {
        if (this.c == null) {
            return false;
        }
        final List<TuSdkMediaEffectData> mediaEffectsWithType = this.c.mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio);
        return mediaEffectsWithType != null && mediaEffectsWithType.size() > 0;
    }
    
    @Override
    public void setFilterChangedListener(final TuSdkFilterEngineListener filterChangedListener) {
        if (this.c == null) {
            return;
        }
        this.c.setFilterChangedListener(filterChangedListener);
    }
    
    @Override
    public boolean isGroupStickerUsed(final StickerGroup stickerGroup) {
        return this.c != null && this.c.getLiveStickerPlayController() != null && this.c.getLiveStickerPlayController().isGroupStickerUsed(stickerGroup);
    }
}
