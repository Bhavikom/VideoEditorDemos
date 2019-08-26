// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.utils.hardware;

//import org.lasque.tusdk.core.seles.sources.VideoFilterDelegate;
//import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import android.opengl.EGL14;
//import org.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoder;
//import org.lasque.tusdk.core.seles.video.SelesSurfaceTextureEncoder;
//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
//import org.lasque.tusdk.core.encoder.video.TuSDKSoftVideoDataEncoder;
//import org.lasque.tusdk.core.encoder.video.TuSDKVideoDataEncoderInterface;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
//import org.lasque.tusdk.impl.components.camera.TuVideoFocusTouchView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
//import org.lasque.tusdk.core.seles.sources.SelesOutput;
//import org.lasque.tusdk.core.seles.tusdk.FilterManager;
//import org.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import java.util.List;
import android.opengl.GLSurfaceView;
import android.os.Build;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.tusdk.combo.Face2DComboFilterWrap;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.content.Context;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.view.ViewGroup;
import android.annotation.TargetApi;
import java.util.Calendar;
//import org.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface;
import android.graphics.PointF;
import android.hardware.Camera;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.utils.TLog;
import android.graphics.RectF;
import android.view.View;
//import org.lasque.tusdk.core.struct.ViewSize;
//import org.lasque.tusdk.impl.view.widget.RegionDefaultHandler;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
//import org.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;
//import org.lasque.tusdk.core.audio.TuSDKAudioCaptureSetting;
//import org.lasque.tusdk.core.seles.filters.SelesFrameDelayFilter;
//import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine;
//import org.lasque.tusdk.impl.view.widget.RegionHandler;
//import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
//import org.lasque.tusdk.core.detector.FrameDetectProcessor;
//import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;
//import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
//import org.lasque.tusdk.core.audio.TuSDKAudioRecorderCore;
//import org.lasque.tusdk.core.encoder.video.TuSDKSoftVideoDataEncoderInterface;
//import org.lasque.tusdk.core.seles.video.SelesSurfaceEncoderInterface;
//import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
//import org.lasque.tusdk.core.video.TuSDKVideoCaptureSetting;
//import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.seles.output.SelesSmartView;
import android.widget.RelativeLayout;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFrameDelayFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesSmartView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesVideoCamera;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterManager;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.combo.Face2DComboFilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker.LiveStickerPlayController;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.ViewSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkFace;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkVideoCameraExtendViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.RegionDefaultHandler;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.RegionHandler;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.audio.TuSDKAudioCaptureSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.audio.TuSDKAudioRecorderCore;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.detector.FrameDetectProcessor;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoder;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKSoftVideoDataEncoder;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKSoftVideoDataEncoderInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKVideoDataEncoderInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources.SelesVideoCameraInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources.VideoFilterDelegate;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.video.SelesSurfaceEncoderInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.video.SelesSurfaceTextureEncoder;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.video.TuSDKVideoCaptureSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.impl.components.camera.TuVideoFocusTouchView;

import javax.microedition.khronos.egl.EGLContext;
//import org.lasque.tusdk.core.seles.sources.SelesVideoCameraInterface;
//import org.lasque.tusdk.core.seles.sources.SelesVideoCamera;

public abstract class TuSDKVideoCamera extends SelesVideoCamera implements SelesVideoCameraInterface
{
    private EGLContext a;
    private boolean b;
    private boolean c;
    private int d;
    private float e;
    private float f;
    private CameraConfigs.CameraFlash g;
    private CameraConfigs.CameraAutoFocus h;
    private long i;
    private CameraConfigs.CameraAntibanding j;
    private final RelativeLayout k;
    private boolean l;
    private boolean m;
    private SelesSmartView n;
    protected final FilterWrap mFilterWrap;
    private boolean o;
    private boolean p;
    private TuSdkStillCameraAdapter.CameraState q;
    private InterfaceOrientation r;
    private boolean s;
    private boolean t;
    private boolean u;
    private Bitmap v;
    private TuSdkWaterMarkOption.WaterMarkPosition w;
    private boolean x;
    private boolean y;
    private TuSDKVideoCaptureSetting z;
    protected TuSDKVideoEncoderSetting mVideoEncoderSetting;
    private SelesSurfaceEncoderInterface A;
    private TuSDKSoftVideoDataEncoderInterface B;
    private TuSDKAudioRecorderCore C;
    private TuSDKVideoDataEncoderDelegate D;
    private TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate E;
    private boolean F;
    private FrameDetectProcessor G;
    private TuSDKVideoCameraFaceDetectionDelegate H;
    private FaceDetectionResultType I;
    private int J;
    private LiveStickerPlayController K;
    private RegionHandler L;
    private TuSDKVideoCameraFocusViewInterface M;
    private boolean N;
    private int O;
    private float P;
    private boolean Q;
    private TuSdkAudioPitchEngine.TuSdkSoundPitchType R;
    private TuSDKVideoCameraDelegate S;
    private SelesFrameDelayFilter T;
    private FrameDetectProcessor.FrameDetectProcessorDelegate U;
    private TuSDKAudioCaptureSetting V;
    private TuSDKAudioEncoderSetting W;
    private SelesFilter.FrameProcessingDelegate X;
    
    protected EGLContext getCurrentEGLContext() {
        return this.a;
    }
    
    public boolean isUnifiedParameters() {
        return this.b;
    }
    
    public void setUnifiedParameters(final boolean b) {
        this.b = b;
    }
    
    public boolean isDisableMirrorFrontFacing() {
        return this.c;
    }
    
    public void setDisableMirrorFrontFacing(final boolean c) {
        this.c = c;
        this.updateOutputFilterSettings();
    }
    
    private int a() {
        final TuSdkSize screenSize = ContextUtils.getScreenSize(this.getContext());
        if (this.d < 1 || this.d > screenSize.maxSide()) {
            this.d = screenSize.maxSide();
        }
        return this.d;
    }
    
    public void setPreviewMaxSize(final int d) {
        this.d = d;
    }
    
    public TuSdkSize getOutputSize() {
        return null;
    }
    
    public void setOutputSize(final TuSdkSize tuSdkSize) {
    }
    
    public float getPreviewEffectScale() {
        return this.f;
    }
    
    public void setPreviewEffectScale(final float f) {
        if (f <= 0.0f) {
            return;
        }
        if (f > 1.0f) {
            this.f = 1.0f;
        }
        this.f = f;
    }
    
    public float getPreviewRatio() {
        return this.e;
    }
    
    public void setPreviewRatio(final float e) {
        this.e = e;
    }
    
    public boolean isDisableContinueFoucs() {
        return this.N;
    }
    
    public void setDisableContinueFoucs(final boolean n) {
        this.N = n;
        if (this.getFocusTouchView() != null) {
            this.getFocusTouchView().setDisableContinueFoucs(this.N);
        }
    }
    
    public int getRegionViewColor() {
        return this.O;
    }
    
    public void setRegionViewColor(final int o) {
        this.O = o;
        if (this.n != null) {
            this.n.setBackgroundColor(this.O);
        }
    }
    
    public float getRegionRatio() {
        if (this.P < 0.0f) {
            this.P = 0.0f;
        }
        return this.P;
    }
    
    public void setRegionRatio(final float p) {
        if (!this.canChangeRatio()) {
            return;
        }
        this.P = p;
        if (this.L != null) {
            this.L.setRatio(this.P);
        }
    }
    
    public boolean canChangeRatio() {
        return !this.x && !this.y;
    }
    
    public void setRegionHandler(final RegionHandler l) {
        this.L = l;
    }
    
    public RegionHandler getRegionHandler() {
        if (this.L == null) {
            this.L = (RegionHandler)new RegionDefaultHandler();
        }
        this.L.setWrapSize((TuSdkSize) ViewSize.create((View)this.k));
        return this.L;
    }
    
    public boolean isDisplayGuideLine() {
        return this.Q;
    }
    
    public void setDisplayGuideLine(final boolean b) {
        this.Q = b;
        if (this.getFocusTouchView() != null) {
            this.getFocusTouchView().setGuideLineViewState(b);
        }
    }
    
    public void changeRegionRatio(final float p) {
        if (!this.canChangeRatio()) {
            return;
        }
        this.P = p;
        this.getRegionHandler().changeWithRatio(this.P, (RegionHandler.RegionChangerListener)new RegionHandler.RegionChangerListener() {
            public void onRegionChanged(final RectF rectF) {
                if (TuSDKVideoCamera.this.n != null) {
                    TuSDKVideoCamera.this.n.setDisplayRect(rectF);
                }
                if (TuSDKVideoCamera.this.getFocusTouchView() != null) {
                    TuSDKVideoCamera.this.getFocusTouchView().setRegionPercent(rectF);
                }
                TuSDKVideoCamera.this.f();
            }
        });
    }
    
    public long getLastFocusTime() {
        return this.i;
    }
    
    public boolean isEnableFaceDetection() {
        return this.s;
    }
    
    public void setEnableFaceDetection(final boolean s) {
        this.s = s;
        this.c();
    }
    
    public boolean isEnableLiveStickr() {
        return this.t;
    }
    
    public void setEnableLiveSticker(final boolean b) {
        if (this.getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) {
            TLog.w("Sorry, Live sticker could only be used with video hardware encoder.", new Object[0]);
            return;
        }
        if (!this.isLiveStickerSupported() && b) {
            TLog.w("Sorry, face feature is not supported on this device", new Object[0]);
            return;
        }
        if (!(this.t = b)) {
            this.updateFaceFeatures(null, 0.0f);
        }
        if (this.t && this.G == null) {
            (this.G = new FrameDetectProcessor()).setDelegate(this.U);
        }
        if (this.mFilterWrap instanceof SelesParameters.FilterStickerInterface) {
            ((SelesParameters.FilterStickerInterface)this.mFilterWrap).setStickerVisibility(b);
        }
        this.c();
    }
    
    public void setDetectScale(final float detectScale) {
        FrameDetectProcessor.setDetectScale(detectScale);
    }
    
    public final boolean isLiveStickerSupported() {
        return TuSdkGPU.isLiveStickerSupported();
    }
    
    public static boolean isFaceBeautySupported() {
        return TuSdkGPU.isFaceBeautySupported();
    }
    
    public void setWaterMarkImage(final Bitmap v) {
        this.v = v;
    }
    
    public Bitmap getWaterMarkImage() {
        return this.v;
    }
    
    public void setWaterMarkPosition(final TuSdkWaterMarkOption.WaterMarkPosition w) {
        this.w = w;
    }
    
    public TuSdkWaterMarkOption.WaterMarkPosition getWaterMarkPosition() {
        return this.w;
    }
    
    public CameraConfigs.CameraFlash getFlashMode() {
        if (this.inputCamera() == null) {
            return CameraConfigs.CameraFlash.Off;
        }
        final Camera.Parameters parameters = this.inputCamera().getParameters();
        if (parameters != null) {
            return CameraHelper.getFlashMode(parameters);
        }
        if (this.g == null) {
            this.g = CameraConfigs.CameraFlash.Off;
        }
        return this.g;
    }
    
    public void setFlashMode(final CameraConfigs.CameraFlash g) {
        if (g == null) {
            return;
        }
        this.g = g;
        if (!CameraHelper.canSupportFlash(this.getContext()) || this.inputCamera() == null) {
            return;
        }
        final Camera.Parameters parameters = this.inputCamera().getParameters();
        if (parameters == null) {
            return;
        }
        CameraHelper.setFlashMode(parameters, g);
        this.inputCamera().setParameters(parameters);
    }
    
    public boolean canSupportFlash() {
        return this.inputCamera() != null && CameraHelper.canSupportFlash(this.getContext()) && CameraHelper.supportFlash(this.inputCamera().getParameters());
    }
    
    public void autoMetering(final PointF pointF) {
    }
    
    public void setFocusMode(final CameraConfigs.CameraAutoFocus h, final PointF pointF) {
        if (h == null) {
            return;
        }
        this.h = h;
        if (this.inputCamera() == null) {
            return;
        }
        final Camera.Parameters parameters = this.inputCamera().getParameters();
        if (parameters == null) {
            return;
        }
        CameraHelper.setFocusMode(parameters, this.h, this.getCenterIfNull(pointF), this.mOutputRotation);
        this.inputCamera().setParameters(parameters);
    }
    
    public void setFocusPoint(final PointF pointF) {
        if (this.inputCamera() == null) {
            return;
        }
        final Camera.Parameters parameters = this.inputCamera().getParameters();
        if (parameters == null) {
            return;
        }
        CameraHelper.setFocusPoint(parameters, this.getCenterIfNull(pointF), this.mOutputRotation);
        this.inputCamera().setParameters(parameters);
    }
    
    public CameraConfigs.CameraAutoFocus getFocusMode() {
        if (this.inputCamera() == null) {
            return this.h;
        }
        final Camera.Parameters parameters = this.inputCamera().getParameters();
        if (parameters != null) {
            return CameraHelper.focusModeType(parameters.getFocusMode());
        }
        return this.h;
    }
    
    public boolean canSupportAutoFocus() {
        if (this.inputCamera() == null) {
            return false;
        }
        boolean canSupportAutofocus;
        try {
            canSupportAutofocus = CameraHelper.canSupportAutofocus(this.getContext(), this.inputCamera().getParameters());
        }
        catch (RuntimeException ex) {
            canSupportAutofocus = false;
            ex.printStackTrace();
        }
        return canSupportAutofocus;
    }
    
    public void cancelAutoFocus() {
        if (this.inputCamera() == null || !CameraHelper.canSupportAutofocus(this.getContext())) {
            return;
        }
        this.inputCamera().cancelAutoFocus();
    }
    
    public void autoFocus(final CameraConfigs.CameraAutoFocus cameraAutoFocus, final PointF pointF, final SelesBaseCameraInterface.TuSdkAutoFocusCallback tuSdkAutoFocusCallback) {
        this.setFocusMode(cameraAutoFocus, pointF);
        this.autoFocus(tuSdkAutoFocusCallback);
    }
    
    public void autoFocus(final SelesBaseCameraInterface.TuSdkAutoFocusCallback tuSdkAutoFocusCallback) {
        if (this.inputCamera() == null || !this.canSupportAutoFocus()) {
            if (tuSdkAutoFocusCallback != null) {
                tuSdkAutoFocusCallback.onAutoFocus(false, (SelesBaseCameraInterface)null);
            }
            return;
        }
        this.i = Calendar.getInstance().getTimeInMillis();
        Object o = null;
        if (tuSdkAutoFocusCallback != null) {
            o = new Camera.AutoFocusCallback() {
                public void onAutoFocus(final boolean b, final Camera camera) {
                    if (tuSdkAutoFocusCallback != null) {
                        tuSdkAutoFocusCallback.onAutoFocus(false, (SelesBaseCameraInterface)null);
                    }
                    TuSDKVideoCamera.this.cancelAutoFocus();
                }
            };
        }
        try {
            this.inputCamera().autoFocus((Camera.AutoFocusCallback)o);
        }
        catch (Exception ex) {
            TLog.e("autoFocus", new Object[] { ex });
        }
    }
    
    @TargetApi(16)
    public void setAutoFocusMoveCallback(final Camera.AutoFocusMoveCallback autoFocusMoveCallback) {
        if (this.inputCamera() != null || !CameraHelper.canSupportAutofocus(this.getContext())) {
            this.inputCamera().setAutoFocusMoveCallback(autoFocusMoveCallback);
        }
    }
    
    protected PointF getCenterIfNull(PointF pointF) {
        if (pointF == null) {
            pointF = new PointF(0.5f, 0.5f);
        }
        return pointF;
    }
    
    public void setAntibandingMode(final CameraConfigs.CameraAntibanding j) {
        this.j = j;
        if (this.inputCamera() == null) {
            return;
        }
        final Camera.Parameters parameters = this.inputCamera().getParameters();
        if (parameters == null) {
            return;
        }
        CameraHelper.setAntibanding(parameters, this.j);
        this.inputCamera().setParameters(parameters);
    }
    
    public CameraConfigs.CameraAntibanding getAntiBandingMode() {
        if (this.inputCamera() == null) {
            return this.j;
        }
        final Camera.Parameters parameters = this.inputCamera().getParameters();
        if (parameters != null) {
            return CameraHelper.antiBandingType(parameters.getAntibanding());
        }
        return this.j;
    }
    
    public TuSDKVideoCameraFocusViewInterface getFocusTouchView() {
        return this.M;
    }
    
    public void setFocusTouchView(final TuSDKVideoCameraFocusViewInterface m) {
        if (m == null || this.n == null) {
            return;
        }
        if (this.M != null) {
            this.n.removeView((View)this.M);
            this.M.viewWillDestory();
        }
        this.getRegionHandler().setRatio(this.getRegionRatio());
        this.n.setBackgroundColor(this.getRegionViewColor());
        this.n.setDisplayRect(this.getRegionHandler().getRectPercent());
        (this.M = m).setCamera(this);
        this.M.setDisableFocusBeep(true);
        this.M.setDisableContinueFoucs(this.isDisableContinueFoucs());
        this.M.setGuideLineViewState(false);
        this.M.setRegionPercent(this.getRegionHandler().getRectPercent());
        this.n.addView((View)this.M, (ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
    }
    
    public void setFocusTouchView(final int n) {
        if (n == 0) {
            return;
        }
        final View buildView = TuSdkViewHelper.buildView(this.getContext(), n, (ViewGroup)this.n);
        if (buildView == null || !(buildView instanceof TuSdkVideoCameraExtendViewInterface)) {
            TLog.w("The setFocusTouchView must extend TuFocusTouchView: %s", new Object[] { buildView });
            return;
        }
        this.setFocusTouchView((TuSDKVideoCameraFocusViewInterface)buildView);
    }
    
    public TuSDKVideoCamera(final Context context, final TuSDKVideoCaptureSetting z, final RelativeLayout k, final Boolean b, final Boolean b2) {
        super(context, (z == null) ? null : z.facing);
        this.b = false;
        this.e = 0.0f;
        this.f = 0.75f;
        this.q = TuSdkStillCameraAdapter.CameraState.StateUnknow;
        this.r = InterfaceOrientation.Portrait;
        this.u = true;
        this.F = false;
        this.J = 0;
        this.O = -16777216;
        this.P = 0.0f;
        this.Q = false;
        this.R = TuSdkAudioPitchEngine.TuSdkSoundPitchType.Normal;
        this.U = new FrameDetectProcessor.FrameDetectProcessorDelegate() {
            @Override
            public void onFrameDetectedResult(final FaceAligment[] array, final TuSdkSize tuSdkSize, final float n, final boolean b) {
                TuSDKVideoCamera.this.handleDetectResult(array, tuSdkSize, n, b);
            }
            
            public void onOrientationChanged(final InterfaceOrientation interfaceOrientation) {
                TuSDKVideoCamera.this.r = interfaceOrientation;
                TuSDKVideoCamera.this.e();
            }
        };
        this.X = (SelesFilter.FrameProcessingDelegate)new SelesFilter.FrameProcessingDelegate() {
            public void onFrameCompletion(final SelesFilter selesFilter, final long n) {
                final Bitmap imageFromCurrentlyProcessedOutput = selesFilter.imageFromCurrentlyProcessedOutput();
                selesFilter.setFrameProcessingDelegate((SelesFilter.FrameProcessingDelegate)null);
                ThreadHelper.post((Runnable)new Runnable() {
                    final /* synthetic */ Bitmap a = BitmapHelper.imageCorp(imageFromCurrentlyProcessedOutput, TuSDKVideoCamera.this.getRegionRatio());
                    
                    @Override
                    public void run() {
                        TuSDKVideoCamera.this.a(this.a);
                    }
                });
            }
        };
        this.k = k;
        this.l = b;
        this.m = b2;
        this.z = z;
        this.mFilterWrap = (FilterWrap) Face2DComboFilterWrap.creat();
        ((Face2DComboFilterWrap)this.mFilterWrap).setIsEnablePlastic(true);
        if (ContextUtils.getScreenSize(context).maxSide() < 1000) {
            this.f = 0.85f;
        }
        else {
            this.f = 0.75f;
        }
        this.initCamera();
    }
    
    public TuSDKVideoCamera(final Context context, final TuSDKVideoCaptureSetting tuSDKVideoCaptureSetting, final RelativeLayout relativeLayout) {
        this(context, tuSDKVideoCaptureSetting, relativeLayout, false, false);
    }
    
    public TuSDKVideoCameraDelegate getDelegate() {
        return this.S;
    }
    
    public void setDelegate(final TuSDKVideoCameraDelegate s) {
        this.S = s;
    }
    
    public TuSdkStillCameraAdapter.CameraState getState() {
        return this.q;
    }
    
    protected void setState(final TuSdkStillCameraAdapter.CameraState q) {
        this.q = q;
        if (!ThreadHelper.isMainThread()) {
            ThreadHelper.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    TuSDKVideoCamera.this.setState(q);
                }
            });
            return;
        }
        if (this.getDelegate() != null) {
            this.getDelegate().onVideoCameraStateChanged(this, this.q);
        }
        final TuSDKVideoCameraFocusViewInterface focusTouchView = this.getFocusTouchView();
        if (focusTouchView != null) {
            focusTouchView.cameraStateChanged(this, this.q);
        }
        if (this.G != null && q == TuSdkStillCameraAdapter.CameraState.StateStarted) {
            if (this.T == null) {
                (this.T = new SelesFrameDelayFilter()).setDelaySize(1);
            }
            this.G.setInputTextureSize(this.getOutputImageSize());
            this.addTarget((SelesContext.SelesInput)this.T);
            this.T.setFirstTarget((SelesContext.SelesInput)this.G.getSelesRotateShotOutput(), 0);
        }
    }
    
    protected boolean getEnableFixedFramerate() {
        return this.u;
    }
    
    public void setEnableFixedFramerate(final boolean u) {
        if (!u && Build.VERSION.SDK_INT <= 14) {
            return;
        }
        this.u = u;
    }
    
    protected void initCamera() {
        final ViewSize create = ViewSize.create((View)this.k);
        if (create != null && ((TuSdkSize)create).isSize()) {
            this.setPreviewMaxSize(((TuSdkSize)create).maxSide());
        }
        this.q = TuSdkStillCameraAdapter.CameraState.StateUnknow;
        this.setEnableFixedFramerate(false);
        this.n = this.buildSelesView();
        if (this.l) {
            this.n.setZOrderOnTop(Boolean.valueOf(true));
        }
        else {
            this.n.setZOrderMediaOverlay(Boolean.valueOf(this.m));
        }
        this.n.setDisplayRect(this.getRegionHandler().getRectPercent());
        this.applyFilterWrap(this.mFilterWrap);
        this.mFilterWrap.processImage();
        this.c();
        this.setOutputImageOrientation(InterfaceOrientation.Portrait);
        this.setHorizontallyMirrorFrontFacingCamera(true);
    }
    
    private boolean b() {
        return this instanceof TuSDKRecordVideoCamera;
    }
    
    protected SelesSmartView buildSelesView() {
        if (this.k == null) {
            TLog.e("Can not find holderView", new Object[0]);
            return this.n;
        }
        if (this.n == null) {
            (this.n = new SelesSmartView(this.getContext())).setRenderer((GLSurfaceView.Renderer)this);
            this.n.setEnableFixedFrameRate(this.getEnableFixedFramerate());
            this.k.addView((View)this.n, 0, (ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
        }
        return this.n;
    }
    
    public void initOutputSettings() {
    }
    
    protected void onMainThreadStart() {
        super.onMainThreadStart();
        if (this.n != null) {
            this.n.requestRender();
        }
    }
    
    protected void onCameraStarted() {
        super.onCameraStarted();
        this.updateOutputFilterSettings();
        if (this.G != null) {
            this.G.setEnabled(true);
        }
        this.setState(TuSdkStillCameraAdapter.CameraState.StateStarted);
        if (this.n != null) {
            this.resumeCameraCapture();
        }
        this.c();
    }
    
    public void onCameraFaceDetection(final List<TuSdkFace> list, final TuSdkSize tuSdkSize) {
        super.onCameraFaceDetection((List)list, tuSdkSize);
        if (this.getFocusTouchView() != null) {
            this.getFocusTouchView().setCameraFaceDetection(list, tuSdkSize);
        }
    }
    
    public void updateFaceFeatures(final FaceAligment[] array, final float n) {
        if (this.mFilterWrap != null && this.mFilterWrap instanceof SelesParameters.FilterFacePositionInterface) {
            ((SelesParameters.FilterFacePositionInterface)this.mFilterWrap).updateFaceFeatures(array, n);
        }
    }
    
    public int getDeviceAngle() {
        if (this.G != null) {
            return this.G.getDeviceAngle();
        }
        return 0;
    }
    
    protected void onInitConfig(final Camera camera) {
        if (camera == null) {
            return;
        }
        super.onInitConfig(camera);
        final Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            return;
        }
        if (this.isUnifiedParameters()) {
            CameraHelper.unifiedParameters(parameters);
        }
        CameraHelper.setPreviewSize(this.getContext(), parameters, this.a(), this.getPreviewEffectScale(), this.getPreviewRatio());
        CameraHelper.setFocusMode(parameters, CameraHelper.videoFocusModes);
        this.h = CameraHelper.focusModeType(parameters.getFocusMode());
        CameraHelper.setFlashMode(parameters, this.getFlashMode());
        if (Build.VERSION.SDK_INT >= 14) {
            CameraHelper.setFocusArea(parameters, this.getCenterIfNull(null), (ImageOrientation)null, this.isBackFacingCameraPresent());
        }
        camera.setParameters(parameters);
    }
    
    public TuSdkSize getCameraPreviewSize() {
        final Camera.Parameters inputCameraParameters = this.inputCameraParameters();
        if (inputCameraParameters == null || inputCameraParameters.getPreviewSize() == null) {
            return TuSdkSize.create(0);
        }
        return TuSdkSize.create(inputCameraParameters.getPreviewSize().width, inputCameraParameters.getPreviewSize().height);
    }
    
    protected void updateCameraView() {
        if (this.n != null) {
            this.n.requestRender();
        }
    }
    
    protected void updateOutputFilterSettings() {
    }
    
    private void a(final boolean b) {
        if (this.n == null) {
            return;
        }
        if (b) {
            this.n.setRenderModeContinuously();
        }
        else {
            this.n.setRenderModeDirty();
        }
    }
    
    public void rotateCamera() {
        this.p = true;
        super.rotateCamera();
        StatisticsManger.appendComponent(this.isFrontFacingCameraPresent() ? 9441314L : 9441313L);
    }
    
    protected boolean isCameraFacingChangeing() {
        return this.p;
    }
    
    protected void onPreviewStarted() {
        super.onPreviewStarted();
        if (this.y) {
            this.startRecording();
            this.y = false;
        }
        if (this.isRecording() && this.p) {
            this.startVideoDataEncoder();
            this.p = false;
        }
    }
    
    public void pauseCameraCapture() {
        super.pauseCameraCapture();
        this.a(false);
    }
    
    public void resumeCameraCapture() {
        super.resumeCameraCapture();
        this.a(true);
        this.setState(TuSdkStillCameraAdapter.CameraState.StateStarted);
    }
    
    public void stopCameraCapture() {
        if (this.getState() == TuSdkStillCameraAdapter.CameraState.StateStarted) {
            this.y = this.isRecording();
        }
        this.a(this.o = false);
        this.setState(TuSdkStillCameraAdapter.CameraState.StateUnknow);
        if (this.G != null) {
            this.G.setEnabled(false);
        }
        if (this.inputCamera() != null) {
            this.h = null;
        }
        if (this.p) {
            this.stopVideoDataEncoder();
        }
        if (!this.p && this.isRecordingPaused()) {
            this.stopRecording();
        }
        super.stopCameraCapture();
    }
    
    public ImageOrientation capturePhotoOrientation() {
        if (!this.isDisableMirrorFrontFacing() || this.isBackFacingCameraPresent() || !this.isHorizontallyMirrorFrontFacingCamera()) {
            return this.mOutputRotation;
        }
        return TuSdkCameraOrientationImpl.computerOutputOrientation(this.getContext(), this.inputCameraInfo(), this.isHorizontallyMirrorRearFacingCamera(), false, this.getOutputImageOrientation());
    }
    
    public void setRendererFPS(final int rendererFPS) {
        if (rendererFPS < 1 || this.n == null) {
            return;
        }
        this.n.setRendererFPS(rendererFPS);
    }
    
    public int getRendererFPS() {
        if (this.n == null) {
            return 0;
        }
        return this.n.getRendererFPS();
    }
    
    public final boolean isFilterChanging() {
        return this.o;
    }
    
    public void switchFilter(final String s) {
        if (s == null || this.isFilterChanging() || !this.isCapturing()) {
            return;
        }
        if (FilterManager.shared().isSceneEffectFilter(s) || FilterManager.shared().isParticleEffectFilter(s)) {
            TLog.e("Invalid filter code , please contact us via http://tusdk.com", new Object[0]);
            return;
        }
        if (this.mFilterWrap.equalsCode(s)) {
            return;
        }
        this.o = true;
        this.runOnDraw((Runnable)new Runnable() {
            @Override
            public void run() {
                TuSDKVideoCamera.this.a(s);
            }
        });
    }
    
    private void a(final String s) {
        if (this.t) {
            this.mFilterWrap.removeOrgin((SelesOutput)this.T);
            this.mFilterWrap.changeFilter(s);
            this.mFilterWrap.addOrgin((SelesOutput)this.T);
        }
        else {
            this.mFilterWrap.removeOrgin((SelesOutput)this);
            this.mFilterWrap.changeFilter(s);
            this.mFilterWrap.addOrgin((SelesOutput)this);
        }
        this.applyFilterWrap(this.mFilterWrap);
        this.mFilterWrap.processImage();
        this.f();
        ThreadHelper.post((Runnable)new Runnable() {
            @Override
            public void run() {
                TuSDKVideoCamera.this.d();
            }
        });
    }
    
    protected void applyFilterWrap(final FilterWrap filterWrap) {
        filterWrap.bindWithCameraView((SelesContext.SelesInput)this.n);
        if (this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC && this.A != null) {
            filterWrap.addTarget((SelesContext.SelesInput)this.A, 0);
        }
    }
    
    protected void updateOutputFilter() {
        if (this.mFilterWrap.getFilter() == null) {
            return;
        }
        this.updateOutputFilterSettings();
        this.applyFilterWrap(this.mFilterWrap);
    }
    
    private void c() {
        final boolean enableFaceDetection = this.isEnableFaceDetection();
        if (this.G != null) {
            this.G.setEnabled(enableFaceDetection);
        }
        super.setEnableFaceTrace(!this.isEnableLiveStickr() && this.isEnableFaceTrace());
    }
    
    private void d() {
        if (this.getDelegate() != null) {
            this.getDelegate().onFilterChanged(this.mFilterWrap);
        }
        this.e();
        this.o = false;
    }
    
    private void e() {
        this.mFilterWrap.rotationTextures(this.r);
    }
    
    public InterfaceOrientation getDeviceOrient() {
        return this.r;
    }
    
    public TuSdkSize getOutputImageSize() {
        final ImageOrientation computerOutputOrientation = this.computerOutputOrientation();
        final TuSdkSize tuSdkSize = new TuSdkSize(this.mInputTextureSize.width, this.mInputTextureSize.height);
        if (computerOutputOrientation.isTransposed()) {
            tuSdkSize.width = this.mInputTextureSize.height;
            tuSdkSize.height = this.mInputTextureSize.width;
        }
        return tuSdkSize;
    }
    
    public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
        super.onSurfaceCreated(gl10, eglConfig);
        this.a = SelesContext.currentEGLContext();
        if (this.K != null) {
            this.K.destroy();
        }
        this.K = new LiveStickerPlayController(this.a);
    }
    
    public void mountAtGLThread(final Runnable runnable) {
        if (runnable == null) {
            return;
        }
        this.runOnDraw(runnable);
    }
    
    public void setFaceDetectionDelegate(final TuSDKVideoCameraFaceDetectionDelegate h) {
        this.H = h;
    }
    
    private void a(final FaceAligment[] array, final TuSdkSize tuSdkSize, final boolean b, final boolean b2) {
        ThreadHelper.post((Runnable)new Runnable() {
            @Override
            public void run() {
                ((TuVideoFocusTouchView)TuSDKVideoCamera.this.M).onFaceAligmented(array, tuSdkSize, b, TuSDKVideoCamera.this.isEnableFaceTrace());
            }
        });
    }
    
    protected void handleDetectResult(final FaceAligment[] array, final TuSdkSize tuSdkSize, final float n, final boolean b) {
        if (!this.isEnableLiveStickr()) {
            return;
        }
        if (array == null || array.length == 0) {
            this.a(FaceDetectionResultType.NoFaceDetected, 0);
        }
        else {
            this.a(FaceDetectionResultType.FaceDetected, array.length);
        }
        this.updateFaceFeatures(array, n);
        this.a(array, tuSdkSize, b, this.isEnableFaceTrace());
    }
    
    private void a(final FaceDetectionResultType i, final int j) {
        if (this.H == null) {
            return;
        }
        if (this.I != i || this.J != j) {
            this.H.onFaceDetectionResult(i, j);
        }
        this.J = j;
        this.I = i;
    }
    
    private void f() {
        if (this.K == null || this.mFilterWrap == null || !(this.mFilterWrap instanceof SelesParameters.FilterStickerInterface)) {
            return;
        }
        ((SelesParameters.FilterStickerInterface)this.mFilterWrap).updateStickers(this.K.getStickers());
        ((SelesParameters.FilterStickerInterface)this.mFilterWrap).setDisplayRect((RectF)null, this.getRegionRatio());
    }
    
    public final void showGroupSticker(final StickerGroup stickerGroup) {
        if (this.b() && !SdkValid.shared.videoCameraStickerEnabled()) {
            TLog.e("You are not allowed to user camera sticker, please see http://tusdk.com", new Object[0]);
            return;
        }
        if (!this.isEnableLiveStickr()) {
            TLog.e("Please set setEnableLiveSticker to true before use live sticker", new Object[0]);
            return;
        }
        if (stickerGroup == null || stickerGroup.stickers == null || stickerGroup.categoryId != StickerCategory.StickerCategoryType.StickerCategorySmart.getValue()) {
            TLog.e("Only live sticker could be used here", new Object[0]);
            return;
        }
        if (stickerGroup.stickers.size() > 5) {
            TLog.e("Too many live stickers in the group, please try to remove some stickers first.", new Object[0]);
            return;
        }
        if (this.K == null) {
            this.K = new LiveStickerPlayController(this.getCurrentEGLContext());
        }
        this.K.showGroupSticker(stickerGroup);
        this.f();
    }
    
    public boolean isGroupStickerUsed(final StickerGroup stickerGroup) {
        return this.K != null && this.K.isGroupStickerUsed(stickerGroup);
    }
    
    public void removeAllLiveSticker() {
        if (this.K != null) {
            this.K.removeAllStickers();
            this.f();
        }
    }
    
    public final void setVideoEncoderSetting(final TuSDKVideoEncoderSetting mVideoEncoderSetting) {
        this.mVideoEncoderSetting = mVideoEncoderSetting;
        if (this.b() && !SdkValid.shared.videoCameraBitrateEnabled() && mVideoEncoderSetting != null) {
            TLog.e("You are not allowed to change camera bitrate, please see http://tusdk.com", new Object[0]);
            this.mVideoEncoderSetting.videoQuality = TuSDKVideoEncoderSetting.getDefaultRecordSetting().videoQuality;
        }
    }
    
    public TuSDKVideoEncoderSetting getVideoEncoderSetting() {
        if (this.mVideoEncoderSetting == null) {
            this.mVideoEncoderSetting = new TuSDKVideoEncoderSetting();
        }
        if (this.mVideoEncoderSetting.videoSize == null || !this.mVideoEncoderSetting.videoSize.isSize()) {
            this.mVideoEncoderSetting.videoSize = this.getOutputImageSize();
        }
        return this.mVideoEncoderSetting;
    }
    
    public TuSDKVideoCaptureSetting getVideoCaptureSetting() {
        if (this.z == null) {
            this.z = new TuSDKVideoCaptureSetting();
        }
        return this.z;
    }
    
    public void setVideoCaptureSetting(final TuSDKVideoCaptureSetting z) {
        this.z = z;
    }
    
    public void setVideoDataDelegate(final TuSDKVideoDataEncoderDelegate d) {
        this.D = d;
    }
    
    public TuSDKVideoDataEncoderDelegate getVideoDataDelegate() {
        return this.D;
    }
    
    protected TuSDKVideoDataEncoderInterface getVideoDataEncoder() {
        if (this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.SW_CODEC) {
            return this.B;
        }
        return this.A;
    }
    
    protected TuSDKSoftVideoDataEncoderInterface getSoftVideoDataEncoder() {
        if (this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.SW_CODEC && this.B == null) {
            final TuSDKSoftVideoDataEncoder b = new TuSDKSoftVideoDataEncoder();
            if (b.initEncoder(this.getVideoEncoderSetting())) {
                (this.B = b).setDelegate(this.getVideoDataDelegate());
            }
        }
        return this.B;
    }
    
    protected RectF calculateCenterRectPercent(final float n, final TuSdkSize tuSdkSize) {
        if (n == 0.0f || tuSdkSize == null || !tuSdkSize.isSize()) {
            return new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        }
        final TuSdkSize create = TuSdkSize.create(tuSdkSize);
        create.width = (int)(tuSdkSize.height * n);
        final Rect rectWithAspectRatioInsideRect = RectHelper.makeRectWithAspectRatioInsideRect(create, new Rect(0, 0, tuSdkSize.width, tuSdkSize.height));
        return new RectF(rectWithAspectRatioInsideRect.left / (float)tuSdkSize.width, rectWithAspectRatioInsideRect.top / (float)tuSdkSize.height, rectWithAspectRatioInsideRect.right / (float)tuSdkSize.width, rectWithAspectRatioInsideRect.bottom / (float)tuSdkSize.height);
    }
    
    protected RectF getRecordOutputRegion() {
        if (this.getRegionRatio() != this.getVideoEncoderSetting().videoSize.getRatioFloat()) {
            final RectF calculateCenterRectPercent = this.calculateCenterRectPercent(this.getRegionRatio(), this.getOutputImageSize());
            final TuSdkSize create = TuSdkSize.create((int)(this.getOutputImageSize().width * calculateCenterRectPercent.width()), (int)(this.getOutputImageSize().height * calculateCenterRectPercent.height()));
            final RectF calculateCenterRectPercent2 = this.calculateCenterRectPercent(this.getVideoEncoderSetting().videoSize.getRatioFloat(), create);
            final float n = calculateCenterRectPercent2.height() * create.height / this.getOutputImageSize().height;
            final float n2 = calculateCenterRectPercent2.width() * create.width / this.getOutputImageSize().width;
            final float n3 = (1.0f - n2) * 0.5f;
            final float n4 = (1.0f - n) * 0.5f;
            return new RectF(n3, n4, n3 + n2, n4 + n);
        }
        return this.calculateCenterRectPercent(this.getVideoEncoderSetting().videoSize.getRatioFloat(), this.getOutputImageSize());
    }
    
    protected SelesSurfaceEncoderInterface getHardVideoDataEncoder() {
        if (this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC && this.A == null) {
            final SelesSurfaceTextureEncoder a = new SelesSurfaceTextureEncoder() {
                @Override
                protected void prepareEncoder(final TuSDKVideoEncoderSetting tuSDKVideoEncoderSetting) {
                    final TuSDKHardVideoDataEncoder mVideoEncoder = new TuSDKHardVideoDataEncoder();
                    if (mVideoEncoder.initCodec(tuSDKVideoEncoderSetting)) {
                        this.mVideoEncoder = mVideoEncoder;
                    }
                }
            };
            a.setVideoEncoderSetting(this.getVideoEncoderSetting());
            a.setDelegate(this.getVideoDataDelegate());
            a.setWaterMarkStickerPlayController(this.K);
            this.A = a;
        }
        this.A.setCropRegion(this.getRecordOutputRegion());
        this.A.updateWaterMark(this.v, this.getDeviceOrient().getDegree(), this.w);
        return this.A;
    }
    
    protected void setHardVideoDataEncoder(final SelesSurfaceEncoderInterface a) {
        if (this.getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) {
            return;
        }
        this.A = a;
    }
    
    public void setAudioDataDelegate(final TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate e) {
        this.E = e;
    }
    
    public TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate getAudioDataDelegate() {
        return this.E;
    }
    
    public TuSDKAudioRecorderCore getAudioRecoder() {
        return this.C;
    }
    
    public void mute(final boolean b) {
        if (this.C == null) {
            return;
        }
        this.C.mute(b);
        this.F = !b;
    }
    
    public void setEnableAudioCapture(final boolean f) {
        this.F = f;
    }
    
    public boolean isEnableAudioCapture() {
        return this.F;
    }
    
    protected boolean isCanCaptureAudio() {
        return this.isEnableAudioCapture() && this.C != null && this.C.isPrepared();
    }
    
    public void resetVideoQuality(final TuSDKVideoEncoderSetting.VideoQuality videoQuality) {
        if (this.getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.CUSTOM_CODEC) {
            if (this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) {
                this.b(videoQuality);
            }
            else {
                this.a(videoQuality);
            }
        }
    }
    
    private void a(final TuSDKVideoEncoderSetting.VideoQuality videoQuality) {
        if (!this.isRecording() || this.B == null || this.getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.SW_CODEC) {
            return;
        }
        this.B.getVideoEncoderSetting().videoQuality = videoQuality;
        this.getVideoEncoderSetting().videoQuality = videoQuality;
        this.stopVideoDataEncoder();
        this.startVideoDataEncoder();
    }
    
    private void b(final TuSDKVideoEncoderSetting.VideoQuality videoQuality) {
        if (!this.isRecording() || this.A == null || this.getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) {
            return;
        }
        this.A.getVideoEncoderSetting().videoQuality = videoQuality;
        this.getVideoEncoderSetting().videoQuality = videoQuality;
        this.stopVideoDataEncoder();
        this.startVideoDataEncoder();
    }
    
    protected void startVideoDataEncoder() {
        if (this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.CUSTOM_CODEC) {
            return;
        }
        if (this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.SW_CODEC) {
            if (this.getSoftVideoDataEncoder() != null) {
                this.B.start();
            }
            this.updateOutputFilter();
        }
        else {
            if (this.getHardVideoDataEncoder() == null) {
                return;
            }
            this.runOnDraw((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (!TuSDKVideoCamera.this.isRecording()) {
                        return;
                    }
                    TuSDKVideoCamera.this.updateOutputFilter();
                    TuSDKVideoCamera.this.A.startRecording(EGL14.eglGetCurrentContext(), TuSDKVideoCamera.this.getSurfaceTexture());
                }
            });
        }
    }
    
    protected void stopVideoDataEncoder() {
        if (this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.CUSTOM_CODEC) {
            return;
        }
        if (this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.SW_CODEC) {
            if (this.B != null) {
                this.B.stop();
            }
        }
        else if (this.A != null) {
            this.A.stopRecording();
        }
    }
    
    public void setAudioCaptureSetting(final TuSDKAudioCaptureSetting v) {
        this.V = v;
    }
    
    public void setAudioCaptureSetting(final TuSDKAudioEncoderSetting w) {
        this.W = w;
    }
    
    public void setSoundPitchType(final TuSdkAudioPitchEngine.TuSdkSoundPitchType r) {
        if (r == null) {
            return;
        }
        this.R = r;
    }
    
    private void g() {
        if (this.isEnableAudioCapture()) {
            if (this.C == null) {
                this.C = new TuSDKAudioRecorderCore(this.V, this.W);
                this.C.getAudioEncoder().setDelegate(this.getAudioDataDelegate());
            }
            this.C.setSoundType(this.R);
            if (this.C.isPrepared()) {
                this.getAudioRecoder().startRecording();
            }
            else {
                this.C = null;
                TLog.d("Can not record audio", new Object[0]);
            }
        }
    }
    
    protected void stopAudioRecording() {
        if (this.getAudioRecoder() != null) {
            this.getAudioRecoder().stopRecording();
        }
    }
    
    public final void captureImage() {
        if (this.b() && !SdkValid.shared.videoCameraShotEnabled()) {
            TLog.e("You are not allowed to capture image, please see http://tusdk.com", new Object[0]);
        } else {
            final SelesFilter var1;
            if (this.mFilterWrap.getLastFilter() instanceof SelesFilterGroup) {
                var1 = (SelesFilter)((SelesFilterGroup)this.mFilterWrap.getLastFilter()).getTerminalFilter();
            } else {
                var1 = (SelesFilter)this.mFilterWrap.getLastFilter();
            }

            if (var1 != null) {
                this.runOnDraw(new Runnable() {
                    public void run() {
                        var1.useNextFrameForImageCapture();
                        var1.setFrameProcessingDelegate(TuSDKVideoCamera.this.X);
                    }
                });
            }
        }
    }
    
    private void a(final Bitmap bitmap) {
        if (this.getDelegate() != null) {
            this.getDelegate().onVideoCameraScreenShot(this, bitmap);
        }
    }
    
    public void startRecording() {
        if (this.x && !this.y) {
            return;
        }
        this.x = true;
        this.y = false;
        if (this.getRegionRatio() != this.getVideoEncoderSetting().videoSize.getRatioFloat()) {
            TLog.w("Output video size ratio not be same as regionRatio, regionRatio will be ignored.", new Object[0]);
        }
        this.g();
        this.startVideoDataEncoder();
    }
    
    public void stopRecording() {
        if (!this.x && !this.y) {
            return;
        }
        this.x = false;
        this.y = false;
        this.stopAudioRecording();
        this.stopVideoDataEncoder();
    }
    
    protected void pauseEncoder() {
        if (this.y) {
            return;
        }
        if (!this.x) {
            return;
        }
        this.y = true;
        this.x = false;
        if (this.getHardVideoDataEncoder() != null) {
            this.getHardVideoDataEncoder().pausdRecording();
        }
        this.stopAudioRecording();
    }
    
    public boolean isRecording() {
        return this.x;
    }
    
    protected boolean isRecordingPaused() {
        return this.y;
    }
    
    protected void finalize() {
        this.removeAllTargets();
    }
    
    protected void onDestroy() {
        super.onDestroy();
        this.stopRecording();
        this.removeAllLiveSticker();
        if (this.K != null) {
            this.K.destroy();
            this.K = null;
        }
        if (this.v != null) {
            if (!this.v.isRecycled()) {
                this.v.recycle();
            }
            this.v = null;
        }
        if (this.getVideoDataEncoder() != null) {
            this.getVideoDataEncoder().setDelegate(null);
        }
        if (this.getAudioRecoder() != null && this.getAudioRecoder().getAudioEncoder() != null) {
            this.getAudioRecoder().getAudioEncoder().setDelegate(null);
        }
        if (this.k != null && this.n != null) {
            this.n.setRenderer((GLSurfaceView.Renderer)null);
            this.k.removeAllViews();
            this.n = null;
        }
        this.mFilterWrap.destroy();
        if (this.G != null) {
            this.G.destroy();
            this.G = null;
        }
    }
    
    public enum FaceDetectionResultType
    {
        FaceDetected, 
        NoFaceDetected;
    }
    
    public interface TuSDKVideoCameraFaceDetectionDelegate
    {
        void onFaceDetectionResult(final FaceDetectionResultType p0, final int p1);
    }
    
    public interface TuSDKVideoCameraDelegate extends VideoFilterDelegate
    {
        void onVideoCameraStateChanged(final SelesVideoCameraInterface p0, final TuSdkStillCameraAdapter.CameraState p1);
        
        void onVideoCameraScreenShot(final SelesVideoCameraInterface p0, final Bitmap p1);
    }
}
