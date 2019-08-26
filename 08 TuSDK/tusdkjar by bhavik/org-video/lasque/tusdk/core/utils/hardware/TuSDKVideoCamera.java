package org.lasque.tusdk.core.utils.hardware;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.AutoFocusMoveCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.opengl.EGL14;
import android.os.Build.VERSION;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine.TuSdkSoundPitchType;
import org.lasque.tusdk.core.audio.TuSDKAudioCaptureSetting;
import org.lasque.tusdk.core.audio.TuSDKAudioRecorderCore;
import org.lasque.tusdk.core.detector.FrameDetectProcessor;
import org.lasque.tusdk.core.detector.FrameDetectProcessor.FrameDetectProcessorDelegate;
import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;
import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate;
import org.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;
import org.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoder;
import org.lasque.tusdk.core.encoder.video.TuSDKSoftVideoDataEncoder;
import org.lasque.tusdk.core.encoder.video.TuSDKSoftVideoDataEncoderInterface;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoDataEncoderInterface;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting.VideoQuality;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesParameters.FilterFacePositionInterface;
import org.lasque.tusdk.core.seles.SelesParameters.FilterStickerInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.filters.SelesFilter.FrameProcessingDelegate;
import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import org.lasque.tusdk.core.seles.filters.SelesFrameDelayFilter;
import org.lasque.tusdk.core.seles.output.SelesSmartView;
import org.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface.TuSdkAutoFocusCallback;
import org.lasque.tusdk.core.seles.sources.SelesVideoCamera;
import org.lasque.tusdk.core.seles.sources.SelesVideoCameraInterface;
import org.lasque.tusdk.core.seles.sources.VideoFilterDelegate;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.combo.Face2DComboFilterWrap;
import org.lasque.tusdk.core.seles.video.SelesSurfaceEncoderInterface;
import org.lasque.tusdk.core.seles.video.SelesSurfaceTextureEncoder;
import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.ViewSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.WaterMarkPosition;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.video.TuSDKVideoCaptureSetting;
import org.lasque.tusdk.core.video.TuSDKVideoCaptureSetting.AVCodecType;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.impl.components.camera.TuVideoFocusTouchView;
import org.lasque.tusdk.impl.view.widget.RegionDefaultHandler;
import org.lasque.tusdk.impl.view.widget.RegionHandler;
import org.lasque.tusdk.impl.view.widget.RegionHandler.RegionChangerListener;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory.StickerCategoryType;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;

public abstract class TuSDKVideoCamera
  extends SelesVideoCamera
  implements SelesVideoCameraInterface
{
  private EGLContext a;
  private boolean b = false;
  private boolean c;
  private int d;
  private float e = 0.0F;
  private float f = 0.75F;
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
  private TuSdkStillCameraAdapter.CameraState q = TuSdkStillCameraAdapter.CameraState.StateUnknow;
  private InterfaceOrientation r = InterfaceOrientation.Portrait;
  private boolean s;
  private boolean t;
  private boolean u = true;
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
  private boolean F = false;
  private FrameDetectProcessor G;
  private TuSDKVideoCameraFaceDetectionDelegate H;
  private FaceDetectionResultType I;
  private int J = 0;
  private LiveStickerPlayController K;
  private RegionHandler L;
  private TuSDKVideoCameraFocusViewInterface M;
  private boolean N;
  private int O = -16777216;
  private float P = 0.0F;
  private boolean Q = false;
  private TuSdkAudioPitchEngine.TuSdkSoundPitchType R = TuSdkAudioPitchEngine.TuSdkSoundPitchType.Normal;
  private TuSDKVideoCameraDelegate S;
  private SelesFrameDelayFilter T;
  private FrameDetectProcessor.FrameDetectProcessorDelegate U = new FrameDetectProcessor.FrameDetectProcessorDelegate()
  {
    public void onFrameDetectedResult(FaceAligment[] paramAnonymousArrayOfFaceAligment, TuSdkSize paramAnonymousTuSdkSize, float paramAnonymousFloat, boolean paramAnonymousBoolean)
    {
      TuSDKVideoCamera.this.handleDetectResult(paramAnonymousArrayOfFaceAligment, paramAnonymousTuSdkSize, paramAnonymousFloat, paramAnonymousBoolean);
    }
    
    public void onOrientationChanged(InterfaceOrientation paramAnonymousInterfaceOrientation)
    {
      TuSDKVideoCamera.a(TuSDKVideoCamera.this, paramAnonymousInterfaceOrientation);
      TuSDKVideoCamera.d(TuSDKVideoCamera.this);
    }
  };
  private TuSDKAudioCaptureSetting V;
  private TuSDKAudioEncoderSetting W;
  private SelesFilter.FrameProcessingDelegate X = new SelesFilter.FrameProcessingDelegate()
  {
    public void onFrameCompletion(SelesFilter paramAnonymousSelesFilter, long paramAnonymousLong)
    {
      Bitmap localBitmap1 = paramAnonymousSelesFilter.imageFromCurrentlyProcessedOutput();
      paramAnonymousSelesFilter.setFrameProcessingDelegate(null);
      final Bitmap localBitmap2 = BitmapHelper.imageCorp(localBitmap1, TuSDKVideoCamera.this.getRegionRatio());
      ThreadHelper.post(new Runnable()
      {
        public void run()
        {
          TuSDKVideoCamera.a(TuSDKVideoCamera.this, localBitmap2);
        }
      });
    }
  };
  
  protected EGLContext getCurrentEGLContext()
  {
    return this.a;
  }
  
  public boolean isUnifiedParameters()
  {
    return this.b;
  }
  
  public void setUnifiedParameters(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  public boolean isDisableMirrorFrontFacing()
  {
    return this.c;
  }
  
  public void setDisableMirrorFrontFacing(boolean paramBoolean)
  {
    this.c = paramBoolean;
    updateOutputFilterSettings();
  }
  
  private int a()
  {
    TuSdkSize localTuSdkSize = ContextUtils.getScreenSize(getContext());
    if ((this.d < 1) || (this.d > localTuSdkSize.maxSide())) {
      this.d = localTuSdkSize.maxSide();
    }
    return this.d;
  }
  
  public void setPreviewMaxSize(int paramInt)
  {
    this.d = paramInt;
  }
  
  public TuSdkSize getOutputSize()
  {
    return null;
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize) {}
  
  public float getPreviewEffectScale()
  {
    return this.f;
  }
  
  public void setPreviewEffectScale(float paramFloat)
  {
    if (paramFloat <= 0.0F) {
      return;
    }
    if (paramFloat > 1.0F) {
      this.f = 1.0F;
    }
    this.f = paramFloat;
  }
  
  public float getPreviewRatio()
  {
    return this.e;
  }
  
  public void setPreviewRatio(float paramFloat)
  {
    this.e = paramFloat;
  }
  
  public boolean isDisableContinueFoucs()
  {
    return this.N;
  }
  
  public void setDisableContinueFoucs(boolean paramBoolean)
  {
    this.N = paramBoolean;
    if (getFocusTouchView() != null) {
      getFocusTouchView().setDisableContinueFoucs(this.N);
    }
  }
  
  public int getRegionViewColor()
  {
    return this.O;
  }
  
  public void setRegionViewColor(int paramInt)
  {
    this.O = paramInt;
    if (this.n != null) {
      this.n.setBackgroundColor(this.O);
    }
  }
  
  public float getRegionRatio()
  {
    if (this.P < 0.0F) {
      this.P = 0.0F;
    }
    return this.P;
  }
  
  public void setRegionRatio(float paramFloat)
  {
    if (!canChangeRatio()) {
      return;
    }
    this.P = paramFloat;
    if (this.L != null) {
      this.L.setRatio(this.P);
    }
  }
  
  public boolean canChangeRatio()
  {
    return (!this.x) && (!this.y);
  }
  
  public void setRegionHandler(RegionHandler paramRegionHandler)
  {
    this.L = paramRegionHandler;
  }
  
  public RegionHandler getRegionHandler()
  {
    if (this.L == null) {
      this.L = new RegionDefaultHandler();
    }
    this.L.setWrapSize(ViewSize.create(this.k));
    return this.L;
  }
  
  public boolean isDisplayGuideLine()
  {
    return this.Q;
  }
  
  public void setDisplayGuideLine(boolean paramBoolean)
  {
    this.Q = paramBoolean;
    if (getFocusTouchView() != null) {
      getFocusTouchView().setGuideLineViewState(paramBoolean);
    }
  }
  
  public void changeRegionRatio(float paramFloat)
  {
    if (!canChangeRatio()) {
      return;
    }
    this.P = paramFloat;
    getRegionHandler().changeWithRatio(this.P, new RegionHandler.RegionChangerListener()
    {
      public void onRegionChanged(RectF paramAnonymousRectF)
      {
        if (TuSDKVideoCamera.a(TuSDKVideoCamera.this) != null) {
          TuSDKVideoCamera.a(TuSDKVideoCamera.this).setDisplayRect(paramAnonymousRectF);
        }
        if (TuSDKVideoCamera.this.getFocusTouchView() != null) {
          TuSDKVideoCamera.this.getFocusTouchView().setRegionPercent(paramAnonymousRectF);
        }
        TuSDKVideoCamera.b(TuSDKVideoCamera.this);
      }
    });
  }
  
  public long getLastFocusTime()
  {
    return this.i;
  }
  
  public boolean isEnableFaceDetection()
  {
    return this.s;
  }
  
  public void setEnableFaceDetection(boolean paramBoolean)
  {
    this.s = paramBoolean;
    c();
  }
  
  public boolean isEnableLiveStickr()
  {
    return this.t;
  }
  
  public void setEnableLiveSticker(boolean paramBoolean)
  {
    if (getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC)
    {
      TLog.w("Sorry, Live sticker could only be used with video hardware encoder.", new Object[0]);
      return;
    }
    if ((!isLiveStickerSupported()) && (paramBoolean))
    {
      TLog.w("Sorry, face feature is not supported on this device", new Object[0]);
      return;
    }
    this.t = paramBoolean;
    if (!this.t) {
      updateFaceFeatures(null, 0.0F);
    }
    if ((this.t) && (this.G == null))
    {
      this.G = new FrameDetectProcessor();
      this.G.setDelegate(this.U);
    }
    if ((this.mFilterWrap instanceof SelesParameters.FilterStickerInterface)) {
      ((SelesParameters.FilterStickerInterface)this.mFilterWrap).setStickerVisibility(paramBoolean);
    }
    c();
  }
  
  public void setDetectScale(float paramFloat)
  {
    FrameDetectProcessor.setDetectScale(paramFloat);
  }
  
  public final boolean isLiveStickerSupported()
  {
    return TuSdkGPU.isLiveStickerSupported();
  }
  
  public static boolean isFaceBeautySupported()
  {
    return TuSdkGPU.isFaceBeautySupported();
  }
  
  public void setWaterMarkImage(Bitmap paramBitmap)
  {
    this.v = paramBitmap;
  }
  
  public Bitmap getWaterMarkImage()
  {
    return this.v;
  }
  
  public void setWaterMarkPosition(TuSdkWaterMarkOption.WaterMarkPosition paramWaterMarkPosition)
  {
    this.w = paramWaterMarkPosition;
  }
  
  public TuSdkWaterMarkOption.WaterMarkPosition getWaterMarkPosition()
  {
    return this.w;
  }
  
  public CameraConfigs.CameraFlash getFlashMode()
  {
    if (inputCamera() == null) {
      return CameraConfigs.CameraFlash.Off;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    if (localParameters != null) {
      return CameraHelper.getFlashMode(localParameters);
    }
    if (this.g == null) {
      this.g = CameraConfigs.CameraFlash.Off;
    }
    return this.g;
  }
  
  public void setFlashMode(CameraConfigs.CameraFlash paramCameraFlash)
  {
    if (paramCameraFlash == null) {
      return;
    }
    this.g = paramCameraFlash;
    if ((!CameraHelper.canSupportFlash(getContext())) || (inputCamera() == null)) {
      return;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setFlashMode(localParameters, paramCameraFlash);
    inputCamera().setParameters(localParameters);
  }
  
  public boolean canSupportFlash()
  {
    if ((inputCamera() == null) || (!CameraHelper.canSupportFlash(getContext()))) {
      return false;
    }
    return CameraHelper.supportFlash(inputCamera().getParameters());
  }
  
  public void autoMetering(PointF paramPointF) {}
  
  public void setFocusMode(CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF)
  {
    if (paramCameraAutoFocus == null) {
      return;
    }
    this.h = paramCameraAutoFocus;
    if (inputCamera() == null) {
      return;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setFocusMode(localParameters, this.h, getCenterIfNull(paramPointF), this.mOutputRotation);
    inputCamera().setParameters(localParameters);
  }
  
  public void setFocusPoint(PointF paramPointF)
  {
    if (inputCamera() == null) {
      return;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setFocusPoint(localParameters, getCenterIfNull(paramPointF), this.mOutputRotation);
    inputCamera().setParameters(localParameters);
  }
  
  public CameraConfigs.CameraAutoFocus getFocusMode()
  {
    if (inputCamera() == null) {
      return this.h;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    if (localParameters != null) {
      return CameraHelper.focusModeType(localParameters.getFocusMode());
    }
    return this.h;
  }
  
  public boolean canSupportAutoFocus()
  {
    if (inputCamera() == null) {
      return false;
    }
    boolean bool = false;
    try
    {
      bool = CameraHelper.canSupportAutofocus(getContext(), inputCamera().getParameters());
    }
    catch (RuntimeException localRuntimeException)
    {
      bool = false;
      localRuntimeException.printStackTrace();
    }
    return bool;
  }
  
  public void cancelAutoFocus()
  {
    if ((inputCamera() == null) || (!CameraHelper.canSupportAutofocus(getContext()))) {
      return;
    }
    inputCamera().cancelAutoFocus();
  }
  
  public void autoFocus(CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF, SelesBaseCameraInterface.TuSdkAutoFocusCallback paramTuSdkAutoFocusCallback)
  {
    setFocusMode(paramCameraAutoFocus, paramPointF);
    autoFocus(paramTuSdkAutoFocusCallback);
  }
  
  public void autoFocus(final SelesBaseCameraInterface.TuSdkAutoFocusCallback paramTuSdkAutoFocusCallback)
  {
    if ((inputCamera() == null) || (!canSupportAutoFocus()))
    {
      if (paramTuSdkAutoFocusCallback != null) {
        paramTuSdkAutoFocusCallback.onAutoFocus(false, null);
      }
      return;
    }
    this.i = Calendar.getInstance().getTimeInMillis();
    Camera.AutoFocusCallback local2 = null;
    if (paramTuSdkAutoFocusCallback != null) {
      local2 = new Camera.AutoFocusCallback()
      {
        public void onAutoFocus(boolean paramAnonymousBoolean, Camera paramAnonymousCamera)
        {
          if (paramTuSdkAutoFocusCallback != null) {
            paramTuSdkAutoFocusCallback.onAutoFocus(false, null);
          }
          TuSDKVideoCamera.this.cancelAutoFocus();
        }
      };
    }
    try
    {
      inputCamera().autoFocus(local2);
    }
    catch (Exception localException)
    {
      TLog.e("autoFocus", new Object[] { localException });
    }
  }
  
  @TargetApi(16)
  public void setAutoFocusMoveCallback(Camera.AutoFocusMoveCallback paramAutoFocusMoveCallback)
  {
    if ((inputCamera() != null) || (!CameraHelper.canSupportAutofocus(getContext()))) {
      inputCamera().setAutoFocusMoveCallback(paramAutoFocusMoveCallback);
    }
  }
  
  protected PointF getCenterIfNull(PointF paramPointF)
  {
    if (paramPointF == null) {
      paramPointF = new PointF(0.5F, 0.5F);
    }
    return paramPointF;
  }
  
  public void setAntibandingMode(CameraConfigs.CameraAntibanding paramCameraAntibanding)
  {
    this.j = paramCameraAntibanding;
    if (inputCamera() == null) {
      return;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    if (localParameters == null) {
      return;
    }
    CameraHelper.setAntibanding(localParameters, this.j);
    inputCamera().setParameters(localParameters);
  }
  
  public CameraConfigs.CameraAntibanding getAntiBandingMode()
  {
    if (inputCamera() == null) {
      return this.j;
    }
    Camera.Parameters localParameters = inputCamera().getParameters();
    if (localParameters != null) {
      return CameraHelper.antiBandingType(localParameters.getAntibanding());
    }
    return this.j;
  }
  
  public TuSDKVideoCameraFocusViewInterface getFocusTouchView()
  {
    return this.M;
  }
  
  public void setFocusTouchView(TuSDKVideoCameraFocusViewInterface paramTuSDKVideoCameraFocusViewInterface)
  {
    if ((paramTuSDKVideoCameraFocusViewInterface == null) || (this.n == null)) {
      return;
    }
    if (this.M != null)
    {
      this.n.removeView((View)this.M);
      this.M.viewWillDestory();
    }
    getRegionHandler().setRatio(getRegionRatio());
    this.n.setBackgroundColor(getRegionViewColor());
    this.n.setDisplayRect(getRegionHandler().getRectPercent());
    this.M = paramTuSDKVideoCameraFocusViewInterface;
    this.M.setCamera(this);
    this.M.setDisableFocusBeep(true);
    this.M.setDisableContinueFoucs(isDisableContinueFoucs());
    this.M.setGuideLineViewState(false);
    this.M.setRegionPercent(getRegionHandler().getRectPercent());
    this.n.addView((View)this.M, new RelativeLayout.LayoutParams(-1, -1));
  }
  
  public void setFocusTouchView(int paramInt)
  {
    if (paramInt == 0) {
      return;
    }
    View localView = TuSdkViewHelper.buildView(getContext(), paramInt, this.n);
    if ((localView == null) || (!(localView instanceof TuSdkVideoCameraExtendViewInterface)))
    {
      TLog.w("The setFocusTouchView must extend TuFocusTouchView: %s", new Object[] { localView });
      return;
    }
    setFocusTouchView((TuSDKVideoCameraFocusViewInterface)localView);
  }
  
  public TuSDKVideoCamera(Context paramContext, TuSDKVideoCaptureSetting paramTuSDKVideoCaptureSetting, RelativeLayout paramRelativeLayout, Boolean paramBoolean1, Boolean paramBoolean2)
  {
    super(paramContext, paramTuSDKVideoCaptureSetting == null ? null : paramTuSDKVideoCaptureSetting.facing);
    this.k = paramRelativeLayout;
    this.l = paramBoolean1.booleanValue();
    this.m = paramBoolean2.booleanValue();
    this.z = paramTuSDKVideoCaptureSetting;
    this.mFilterWrap = Face2DComboFilterWrap.creat();
    ((Face2DComboFilterWrap)this.mFilterWrap).setIsEnablePlastic(true);
    if (ContextUtils.getScreenSize(paramContext).maxSide() < 1000) {
      this.f = 0.85F;
    } else {
      this.f = 0.75F;
    }
    initCamera();
  }
  
  public TuSDKVideoCamera(Context paramContext, TuSDKVideoCaptureSetting paramTuSDKVideoCaptureSetting, RelativeLayout paramRelativeLayout)
  {
    this(paramContext, paramTuSDKVideoCaptureSetting, paramRelativeLayout, Boolean.valueOf(false), Boolean.valueOf(false));
  }
  
  public TuSDKVideoCameraDelegate getDelegate()
  {
    return this.S;
  }
  
  public void setDelegate(TuSDKVideoCameraDelegate paramTuSDKVideoCameraDelegate)
  {
    this.S = paramTuSDKVideoCameraDelegate;
  }
  
  public TuSdkStillCameraAdapter.CameraState getState()
  {
    return this.q;
  }
  
  protected void setState(final TuSdkStillCameraAdapter.CameraState paramCameraState)
  {
    this.q = paramCameraState;
    if (!ThreadHelper.isMainThread())
    {
      ThreadHelper.post(new Runnable()
      {
        public void run()
        {
          TuSDKVideoCamera.this.setState(paramCameraState);
        }
      });
      return;
    }
    if (getDelegate() != null) {
      getDelegate().onVideoCameraStateChanged(this, this.q);
    }
    TuSDKVideoCameraFocusViewInterface localTuSDKVideoCameraFocusViewInterface = getFocusTouchView();
    if (localTuSDKVideoCameraFocusViewInterface != null) {
      localTuSDKVideoCameraFocusViewInterface.cameraStateChanged(this, this.q);
    }
    if ((this.G != null) && (paramCameraState == TuSdkStillCameraAdapter.CameraState.StateStarted))
    {
      if (this.T == null)
      {
        this.T = new SelesFrameDelayFilter();
        this.T.setDelaySize(1);
      }
      this.G.setInputTextureSize(getOutputImageSize());
      addTarget(this.T);
      this.T.setFirstTarget(this.G.getSelesRotateShotOutput(), 0);
    }
  }
  
  protected boolean getEnableFixedFramerate()
  {
    return this.u;
  }
  
  public void setEnableFixedFramerate(boolean paramBoolean)
  {
    if ((!paramBoolean) && (Build.VERSION.SDK_INT <= 14)) {
      return;
    }
    this.u = paramBoolean;
  }
  
  protected void initCamera()
  {
    ViewSize localViewSize = ViewSize.create(this.k);
    if ((localViewSize != null) && (localViewSize.isSize())) {
      setPreviewMaxSize(localViewSize.maxSide());
    }
    this.q = TuSdkStillCameraAdapter.CameraState.StateUnknow;
    setEnableFixedFramerate(false);
    this.n = buildSelesView();
    if (this.l) {
      this.n.setZOrderOnTop(Boolean.valueOf(true));
    } else {
      this.n.setZOrderMediaOverlay(Boolean.valueOf(this.m));
    }
    this.n.setDisplayRect(getRegionHandler().getRectPercent());
    applyFilterWrap(this.mFilterWrap);
    this.mFilterWrap.processImage();
    c();
    setOutputImageOrientation(InterfaceOrientation.Portrait);
    setHorizontallyMirrorFrontFacingCamera(true);
  }
  
  private boolean b()
  {
    return (this instanceof TuSDKRecordVideoCamera);
  }
  
  protected SelesSmartView buildSelesView()
  {
    if (this.k == null)
    {
      TLog.e("Can not find holderView", new Object[0]);
      return this.n;
    }
    if (this.n == null)
    {
      this.n = new SelesSmartView(getContext());
      this.n.setRenderer(this);
      this.n.setEnableFixedFrameRate(getEnableFixedFramerate());
      RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
      this.k.addView(this.n, 0, localLayoutParams);
    }
    return this.n;
  }
  
  public void initOutputSettings() {}
  
  protected void onMainThreadStart()
  {
    super.onMainThreadStart();
    if (this.n != null) {
      this.n.requestRender();
    }
  }
  
  protected void onCameraStarted()
  {
    super.onCameraStarted();
    updateOutputFilterSettings();
    if (this.G != null) {
      this.G.setEnabled(true);
    }
    setState(TuSdkStillCameraAdapter.CameraState.StateStarted);
    if (this.n != null) {
      resumeCameraCapture();
    }
    c();
  }
  
  public void onCameraFaceDetection(List<TuSdkFace> paramList, TuSdkSize paramTuSdkSize)
  {
    super.onCameraFaceDetection(paramList, paramTuSdkSize);
    if (getFocusTouchView() != null) {
      getFocusTouchView().setCameraFaceDetection(paramList, paramTuSdkSize);
    }
  }
  
  public void updateFaceFeatures(FaceAligment[] paramArrayOfFaceAligment, float paramFloat)
  {
    if ((this.mFilterWrap != null) && ((this.mFilterWrap instanceof SelesParameters.FilterFacePositionInterface))) {
      ((SelesParameters.FilterFacePositionInterface)this.mFilterWrap).updateFaceFeatures(paramArrayOfFaceAligment, paramFloat);
    }
  }
  
  public int getDeviceAngle()
  {
    if (this.G != null) {
      return this.G.getDeviceAngle();
    }
    return 0;
  }
  
  protected void onInitConfig(Camera paramCamera)
  {
    if (paramCamera == null) {
      return;
    }
    super.onInitConfig(paramCamera);
    Camera.Parameters localParameters = paramCamera.getParameters();
    if (localParameters == null) {
      return;
    }
    if (isUnifiedParameters()) {
      CameraHelper.unifiedParameters(localParameters);
    }
    CameraHelper.setPreviewSize(getContext(), localParameters, a(), getPreviewEffectScale(), getPreviewRatio());
    CameraHelper.setFocusMode(localParameters, CameraHelper.videoFocusModes);
    this.h = CameraHelper.focusModeType(localParameters.getFocusMode());
    CameraHelper.setFlashMode(localParameters, getFlashMode());
    if (Build.VERSION.SDK_INT >= 14) {
      CameraHelper.setFocusArea(localParameters, getCenterIfNull(null), null, isBackFacingCameraPresent());
    }
    paramCamera.setParameters(localParameters);
  }
  
  public TuSdkSize getCameraPreviewSize()
  {
    Camera.Parameters localParameters = inputCameraParameters();
    if ((localParameters == null) || (localParameters.getPreviewSize() == null)) {
      return TuSdkSize.create(0);
    }
    return TuSdkSize.create(localParameters.getPreviewSize().width, localParameters.getPreviewSize().height);
  }
  
  protected void updateCameraView()
  {
    if (this.n != null) {
      this.n.requestRender();
    }
  }
  
  protected void updateOutputFilterSettings() {}
  
  private void a(boolean paramBoolean)
  {
    if (this.n == null) {
      return;
    }
    if (paramBoolean) {
      this.n.setRenderModeContinuously();
    } else {
      this.n.setRenderModeDirty();
    }
  }
  
  public void rotateCamera()
  {
    this.p = true;
    super.rotateCamera();
    StatisticsManger.appendComponent(isFrontFacingCameraPresent() ? 9441314L : 9441313L);
  }
  
  protected boolean isCameraFacingChangeing()
  {
    return this.p;
  }
  
  protected void onPreviewStarted()
  {
    super.onPreviewStarted();
    if (this.y)
    {
      startRecording();
      this.y = false;
    }
    if ((isRecording()) && (this.p))
    {
      startVideoDataEncoder();
      this.p = false;
    }
  }
  
  public void pauseCameraCapture()
  {
    super.pauseCameraCapture();
    a(false);
  }
  
  public void resumeCameraCapture()
  {
    super.resumeCameraCapture();
    a(true);
    setState(TuSdkStillCameraAdapter.CameraState.StateStarted);
  }
  
  public void stopCameraCapture()
  {
    if (getState() == TuSdkStillCameraAdapter.CameraState.StateStarted) {
      this.y = isRecording();
    }
    this.o = false;
    a(false);
    setState(TuSdkStillCameraAdapter.CameraState.StateUnknow);
    if (this.G != null) {
      this.G.setEnabled(false);
    }
    if (inputCamera() != null) {
      this.h = null;
    }
    if (this.p) {
      stopVideoDataEncoder();
    }
    if ((!this.p) && (isRecordingPaused())) {
      stopRecording();
    }
    super.stopCameraCapture();
  }
  
  public ImageOrientation capturePhotoOrientation()
  {
    if ((!isDisableMirrorFrontFacing()) || (isBackFacingCameraPresent()) || (!isHorizontallyMirrorFrontFacingCamera())) {
      return this.mOutputRotation;
    }
    return TuSdkCameraOrientationImpl.computerOutputOrientation(getContext(), inputCameraInfo(), isHorizontallyMirrorRearFacingCamera(), false, getOutputImageOrientation());
  }
  
  public void setRendererFPS(int paramInt)
  {
    if ((paramInt < 1) || (this.n == null)) {
      return;
    }
    this.n.setRendererFPS(paramInt);
  }
  
  public int getRendererFPS()
  {
    if (this.n == null) {
      return 0;
    }
    return this.n.getRendererFPS();
  }
  
  public final boolean isFilterChanging()
  {
    return this.o;
  }
  
  public void switchFilter(final String paramString)
  {
    if ((paramString == null) || (isFilterChanging()) || (!isCapturing())) {
      return;
    }
    if ((FilterManager.shared().isSceneEffectFilter(paramString)) || (FilterManager.shared().isParticleEffectFilter(paramString)))
    {
      TLog.e("Invalid filter code , please contact us via http://tusdk.com", new Object[0]);
      return;
    }
    if (this.mFilterWrap.equalsCode(paramString)) {
      return;
    }
    this.o = true;
    runOnDraw(new Runnable()
    {
      public void run()
      {
        TuSDKVideoCamera.a(TuSDKVideoCamera.this, paramString);
      }
    });
  }
  
  private void a(String paramString)
  {
    if (this.t)
    {
      this.mFilterWrap.removeOrgin(this.T);
      this.mFilterWrap.changeFilter(paramString);
      this.mFilterWrap.addOrgin(this.T);
    }
    else
    {
      this.mFilterWrap.removeOrgin(this);
      this.mFilterWrap.changeFilter(paramString);
      this.mFilterWrap.addOrgin(this);
    }
    applyFilterWrap(this.mFilterWrap);
    this.mFilterWrap.processImage();
    f();
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuSDKVideoCamera.c(TuSDKVideoCamera.this);
      }
    });
  }
  
  protected void applyFilterWrap(FilterWrap paramFilterWrap)
  {
    paramFilterWrap.bindWithCameraView(this.n);
    if ((getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) && (this.A != null)) {
      paramFilterWrap.addTarget(this.A, 0);
    }
  }
  
  protected void updateOutputFilter()
  {
    if (this.mFilterWrap.getFilter() == null) {
      return;
    }
    updateOutputFilterSettings();
    applyFilterWrap(this.mFilterWrap);
  }
  
  private void c()
  {
    boolean bool = isEnableFaceDetection();
    if (this.G != null) {
      this.G.setEnabled(bool);
    }
    super.setEnableFaceTrace((!isEnableLiveStickr()) && (isEnableFaceTrace()));
  }
  
  private void d()
  {
    if (getDelegate() != null) {
      getDelegate().onFilterChanged(this.mFilterWrap);
    }
    e();
    this.o = false;
  }
  
  private void e()
  {
    this.mFilterWrap.rotationTextures(this.r);
  }
  
  public InterfaceOrientation getDeviceOrient()
  {
    return this.r;
  }
  
  public TuSdkSize getOutputImageSize()
  {
    ImageOrientation localImageOrientation = computerOutputOrientation();
    TuSdkSize localTuSdkSize = new TuSdkSize(this.mInputTextureSize.width, this.mInputTextureSize.height);
    if (localImageOrientation.isTransposed())
    {
      localTuSdkSize.width = this.mInputTextureSize.height;
      localTuSdkSize.height = this.mInputTextureSize.width;
    }
    return localTuSdkSize;
  }
  
  public void onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig)
  {
    super.onSurfaceCreated(paramGL10, paramEGLConfig);
    this.a = SelesContext.currentEGLContext();
    if (this.K != null) {
      this.K.destroy();
    }
    this.K = new LiveStickerPlayController(this.a);
  }
  
  public void mountAtGLThread(Runnable paramRunnable)
  {
    if (paramRunnable == null) {
      return;
    }
    runOnDraw(paramRunnable);
  }
  
  public void setFaceDetectionDelegate(TuSDKVideoCameraFaceDetectionDelegate paramTuSDKVideoCameraFaceDetectionDelegate)
  {
    this.H = paramTuSDKVideoCameraFaceDetectionDelegate;
  }
  
  private void a(final FaceAligment[] paramArrayOfFaceAligment, final TuSdkSize paramTuSdkSize, final boolean paramBoolean1, boolean paramBoolean2)
  {
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        ((TuVideoFocusTouchView)TuSDKVideoCamera.e(TuSDKVideoCamera.this)).onFaceAligmented(paramArrayOfFaceAligment, paramTuSdkSize, paramBoolean1, TuSDKVideoCamera.this.isEnableFaceTrace());
      }
    });
  }
  
  protected void handleDetectResult(FaceAligment[] paramArrayOfFaceAligment, TuSdkSize paramTuSdkSize, float paramFloat, boolean paramBoolean)
  {
    if (!isEnableLiveStickr()) {
      return;
    }
    if ((paramArrayOfFaceAligment == null) || (paramArrayOfFaceAligment.length == 0)) {
      a(FaceDetectionResultType.NoFaceDetected, 0);
    } else {
      a(FaceDetectionResultType.FaceDetected, paramArrayOfFaceAligment.length);
    }
    updateFaceFeatures(paramArrayOfFaceAligment, paramFloat);
    a(paramArrayOfFaceAligment, paramTuSdkSize, paramBoolean, isEnableFaceTrace());
  }
  
  private void a(FaceDetectionResultType paramFaceDetectionResultType, int paramInt)
  {
    if (this.H == null) {
      return;
    }
    if ((this.I != paramFaceDetectionResultType) || (this.J != paramInt)) {
      this.H.onFaceDetectionResult(paramFaceDetectionResultType, paramInt);
    }
    this.J = paramInt;
    this.I = paramFaceDetectionResultType;
  }
  
  private void f()
  {
    if ((this.K == null) || (this.mFilterWrap == null) || (!(this.mFilterWrap instanceof SelesParameters.FilterStickerInterface))) {
      return;
    }
    ((SelesParameters.FilterStickerInterface)this.mFilterWrap).updateStickers(this.K.getStickers());
    ((SelesParameters.FilterStickerInterface)this.mFilterWrap).setDisplayRect(null, getRegionRatio());
  }
  
  public final void showGroupSticker(StickerGroup paramStickerGroup)
  {
    if ((b()) && (!SdkValid.shared.videoCameraStickerEnabled()))
    {
      TLog.e("You are not allowed to user camera sticker, please see http://tusdk.com", new Object[0]);
      return;
    }
    if (!isEnableLiveStickr())
    {
      TLog.e("Please set setEnableLiveSticker to true before use live sticker", new Object[0]);
      return;
    }
    if ((paramStickerGroup == null) || (paramStickerGroup.stickers == null) || (paramStickerGroup.categoryId != StickerCategory.StickerCategoryType.StickerCategorySmart.getValue()))
    {
      TLog.e("Only live sticker could be used here", new Object[0]);
      return;
    }
    if (paramStickerGroup.stickers.size() > 5)
    {
      TLog.e("Too many live stickers in the group, please try to remove some stickers first.", new Object[0]);
      return;
    }
    if (this.K == null) {
      this.K = new LiveStickerPlayController(getCurrentEGLContext());
    }
    this.K.showGroupSticker(paramStickerGroup);
    f();
  }
  
  public boolean isGroupStickerUsed(StickerGroup paramStickerGroup)
  {
    if (this.K != null) {
      return this.K.isGroupStickerUsed(paramStickerGroup);
    }
    return false;
  }
  
  public void removeAllLiveSticker()
  {
    if (this.K != null)
    {
      this.K.removeAllStickers();
      f();
    }
  }
  
  public final void setVideoEncoderSetting(TuSDKVideoEncoderSetting paramTuSDKVideoEncoderSetting)
  {
    this.mVideoEncoderSetting = paramTuSDKVideoEncoderSetting;
    if ((b()) && (!SdkValid.shared.videoCameraBitrateEnabled()) && (paramTuSDKVideoEncoderSetting != null))
    {
      TLog.e("You are not allowed to change camera bitrate, please see http://tusdk.com", new Object[0]);
      this.mVideoEncoderSetting.videoQuality = TuSDKVideoEncoderSetting.getDefaultRecordSetting().videoQuality;
      return;
    }
  }
  
  public TuSDKVideoEncoderSetting getVideoEncoderSetting()
  {
    if (this.mVideoEncoderSetting == null) {
      this.mVideoEncoderSetting = new TuSDKVideoEncoderSetting();
    }
    if ((this.mVideoEncoderSetting.videoSize == null) || (!this.mVideoEncoderSetting.videoSize.isSize())) {
      this.mVideoEncoderSetting.videoSize = getOutputImageSize();
    }
    return this.mVideoEncoderSetting;
  }
  
  public TuSDKVideoCaptureSetting getVideoCaptureSetting()
  {
    if (this.z == null) {
      this.z = new TuSDKVideoCaptureSetting();
    }
    return this.z;
  }
  
  public void setVideoCaptureSetting(TuSDKVideoCaptureSetting paramTuSDKVideoCaptureSetting)
  {
    this.z = paramTuSDKVideoCaptureSetting;
  }
  
  public void setVideoDataDelegate(TuSDKVideoDataEncoderDelegate paramTuSDKVideoDataEncoderDelegate)
  {
    this.D = paramTuSDKVideoDataEncoderDelegate;
  }
  
  public TuSDKVideoDataEncoderDelegate getVideoDataDelegate()
  {
    return this.D;
  }
  
  protected TuSDKVideoDataEncoderInterface getVideoDataEncoder()
  {
    if (getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.SW_CODEC) {
      return this.B;
    }
    return this.A;
  }
  
  protected TuSDKSoftVideoDataEncoderInterface getSoftVideoDataEncoder()
  {
    if ((getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.SW_CODEC) && (this.B == null))
    {
      TuSDKSoftVideoDataEncoder localTuSDKSoftVideoDataEncoder = new TuSDKSoftVideoDataEncoder();
      if (localTuSDKSoftVideoDataEncoder.initEncoder(getVideoEncoderSetting()))
      {
        this.B = localTuSDKSoftVideoDataEncoder;
        this.B.setDelegate(getVideoDataDelegate());
      }
    }
    return this.B;
  }
  
  protected RectF calculateCenterRectPercent(float paramFloat, TuSdkSize paramTuSdkSize)
  {
    if ((paramFloat == 0.0F) || (paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return new RectF(0.0F, 0.0F, 1.0F, 1.0F);
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramTuSdkSize);
    localTuSdkSize.width = ((int)(paramTuSdkSize.height * paramFloat));
    Rect localRect = RectHelper.makeRectWithAspectRatioInsideRect(localTuSdkSize, new Rect(0, 0, paramTuSdkSize.width, paramTuSdkSize.height));
    float f1 = localRect.left / paramTuSdkSize.width;
    float f2 = localRect.top / paramTuSdkSize.height;
    float f3 = localRect.right / paramTuSdkSize.width;
    float f4 = localRect.bottom / paramTuSdkSize.height;
    RectF localRectF = new RectF(f1, f2, f3, f4);
    return localRectF;
  }
  
  protected RectF getRecordOutputRegion()
  {
    if (getRegionRatio() != getVideoEncoderSetting().videoSize.getRatioFloat())
    {
      RectF localRectF1 = calculateCenterRectPercent(getRegionRatio(), getOutputImageSize());
      TuSdkSize localTuSdkSize = TuSdkSize.create((int)(getOutputImageSize().width * localRectF1.width()), (int)(getOutputImageSize().height * localRectF1.height()));
      RectF localRectF2 = calculateCenterRectPercent(getVideoEncoderSetting().videoSize.getRatioFloat(), localTuSdkSize);
      float f1 = localRectF2.height() * localTuSdkSize.height / getOutputImageSize().height;
      float f2 = localRectF2.width() * localTuSdkSize.width / getOutputImageSize().width;
      float f3 = (1.0F - f2) * 0.5F;
      float f4 = (1.0F - f1) * 0.5F;
      return new RectF(f3, f4, f3 + f2, f4 + f1);
    }
    return calculateCenterRectPercent(getVideoEncoderSetting().videoSize.getRatioFloat(), getOutputImageSize());
  }
  
  protected SelesSurfaceEncoderInterface getHardVideoDataEncoder()
  {
    if ((getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) && (this.A == null))
    {
      SelesSurfaceTextureEncoder local8 = new SelesSurfaceTextureEncoder()
      {
        protected void prepareEncoder(TuSDKVideoEncoderSetting paramAnonymousTuSDKVideoEncoderSetting)
        {
          TuSDKHardVideoDataEncoder localTuSDKHardVideoDataEncoder = new TuSDKHardVideoDataEncoder();
          if (localTuSDKHardVideoDataEncoder.initCodec(paramAnonymousTuSDKVideoEncoderSetting)) {
            this.mVideoEncoder = localTuSDKHardVideoDataEncoder;
          }
        }
      };
      local8.setVideoEncoderSetting(getVideoEncoderSetting());
      local8.setDelegate(getVideoDataDelegate());
      local8.setWaterMarkStickerPlayController(this.K);
      this.A = local8;
    }
    this.A.setCropRegion(getRecordOutputRegion());
    this.A.updateWaterMark(this.v, getDeviceOrient().getDegree(), this.w);
    return this.A;
  }
  
  protected void setHardVideoDataEncoder(SelesSurfaceEncoderInterface paramSelesSurfaceEncoderInterface)
  {
    if (getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) {
      return;
    }
    this.A = paramSelesSurfaceEncoderInterface;
  }
  
  public void setAudioDataDelegate(TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate paramTuSDKAudioDataEncoderDelegate)
  {
    this.E = paramTuSDKAudioDataEncoderDelegate;
  }
  
  public TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate getAudioDataDelegate()
  {
    return this.E;
  }
  
  public TuSDKAudioRecorderCore getAudioRecoder()
  {
    return this.C;
  }
  
  public void mute(boolean paramBoolean)
  {
    if (this.C == null) {
      return;
    }
    this.C.mute(paramBoolean);
    this.F = (!paramBoolean);
  }
  
  public void setEnableAudioCapture(boolean paramBoolean)
  {
    this.F = paramBoolean;
  }
  
  public boolean isEnableAudioCapture()
  {
    return this.F;
  }
  
  protected boolean isCanCaptureAudio()
  {
    return (isEnableAudioCapture()) && (this.C != null) && (this.C.isPrepared());
  }
  
  public void resetVideoQuality(TuSDKVideoEncoderSetting.VideoQuality paramVideoQuality)
  {
    if (getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.CUSTOM_CODEC) {
      if (getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) {
        b(paramVideoQuality);
      } else {
        a(paramVideoQuality);
      }
    }
  }
  
  private void a(TuSDKVideoEncoderSetting.VideoQuality paramVideoQuality)
  {
    if ((!isRecording()) || (this.B == null) || (getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.SW_CODEC)) {
      return;
    }
    this.B.getVideoEncoderSetting().videoQuality = paramVideoQuality;
    getVideoEncoderSetting().videoQuality = paramVideoQuality;
    stopVideoDataEncoder();
    startVideoDataEncoder();
  }
  
  private void b(TuSDKVideoEncoderSetting.VideoQuality paramVideoQuality)
  {
    if ((!isRecording()) || (this.A == null) || (getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC)) {
      return;
    }
    this.A.getVideoEncoderSetting().videoQuality = paramVideoQuality;
    getVideoEncoderSetting().videoQuality = paramVideoQuality;
    stopVideoDataEncoder();
    startVideoDataEncoder();
  }
  
  protected void startVideoDataEncoder()
  {
    if (getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.CUSTOM_CODEC) {
      return;
    }
    if (getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.SW_CODEC)
    {
      if (getSoftVideoDataEncoder() != null) {
        this.B.start();
      }
      updateOutputFilter();
    }
    else
    {
      if (getHardVideoDataEncoder() == null) {
        return;
      }
      runOnDraw(new Runnable()
      {
        public void run()
        {
          if (!TuSDKVideoCamera.this.isRecording()) {
            return;
          }
          TuSDKVideoCamera.this.updateOutputFilter();
          TuSDKVideoCamera.f(TuSDKVideoCamera.this).startRecording(EGL14.eglGetCurrentContext(), TuSDKVideoCamera.this.getSurfaceTexture());
        }
      });
    }
  }
  
  protected void stopVideoDataEncoder()
  {
    if (getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.CUSTOM_CODEC) {
      return;
    }
    if (getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.SW_CODEC)
    {
      if (this.B != null) {
        this.B.stop();
      }
    }
    else if (this.A != null) {
      this.A.stopRecording();
    }
  }
  
  public void setAudioCaptureSetting(TuSDKAudioCaptureSetting paramTuSDKAudioCaptureSetting)
  {
    this.V = paramTuSDKAudioCaptureSetting;
  }
  
  public void setAudioCaptureSetting(TuSDKAudioEncoderSetting paramTuSDKAudioEncoderSetting)
  {
    this.W = paramTuSDKAudioEncoderSetting;
  }
  
  public void setSoundPitchType(TuSdkAudioPitchEngine.TuSdkSoundPitchType paramTuSdkSoundPitchType)
  {
    if (paramTuSdkSoundPitchType == null) {
      return;
    }
    this.R = paramTuSdkSoundPitchType;
  }
  
  private void g()
  {
    if (isEnableAudioCapture())
    {
      if (this.C == null)
      {
        this.C = new TuSDKAudioRecorderCore(this.V, this.W);
        this.C.getAudioEncoder().setDelegate(getAudioDataDelegate());
      }
      this.C.setSoundType(this.R);
      if (this.C.isPrepared())
      {
        getAudioRecoder().startRecording();
      }
      else
      {
        this.C = null;
        TLog.d("Can not record audio", new Object[0]);
      }
    }
  }
  
  protected void stopAudioRecording()
  {
    if (getAudioRecoder() != null) {
      getAudioRecoder().stopRecording();
    }
  }
  
  public final void captureImage()
  {
    if ((b()) && (!SdkValid.shared.videoCameraShotEnabled()))
    {
      TLog.e("You are not allowed to capture image, please see http://tusdk.com", new Object[0]);
      return;
    }
    final SelesFilter localSelesFilter;
    if ((this.mFilterWrap.getLastFilter() instanceof SelesFilterGroup)) {
      localSelesFilter = (SelesFilter)((SelesFilterGroup)this.mFilterWrap.getLastFilter()).getTerminalFilter();
    } else {
      localSelesFilter = (SelesFilter)this.mFilterWrap.getLastFilter();
    }
    if (localSelesFilter == null) {
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        localSelesFilter.useNextFrameForImageCapture();
        localSelesFilter.setFrameProcessingDelegate(TuSDKVideoCamera.g(TuSDKVideoCamera.this));
      }
    });
  }
  
  private void a(Bitmap paramBitmap)
  {
    if (getDelegate() != null) {
      getDelegate().onVideoCameraScreenShot(this, paramBitmap);
    }
  }
  
  public void startRecording()
  {
    if ((this.x) && (!this.y)) {
      return;
    }
    this.x = true;
    this.y = false;
    TuSdkSize localTuSdkSize = getVideoEncoderSetting().videoSize;
    if (getRegionRatio() != localTuSdkSize.getRatioFloat()) {
      TLog.w("Output video size ratio not be same as regionRatio, regionRatio will be ignored.", new Object[0]);
    }
    g();
    startVideoDataEncoder();
  }
  
  public void stopRecording()
  {
    if ((!this.x) && (!this.y)) {
      return;
    }
    this.x = false;
    this.y = false;
    stopAudioRecording();
    stopVideoDataEncoder();
  }
  
  protected void pauseEncoder()
  {
    if (this.y) {
      return;
    }
    if (!this.x) {
      return;
    }
    this.y = true;
    this.x = false;
    if (getHardVideoDataEncoder() != null) {
      getHardVideoDataEncoder().pausdRecording();
    }
    stopAudioRecording();
  }
  
  public boolean isRecording()
  {
    return this.x;
  }
  
  protected boolean isRecordingPaused()
  {
    return this.y;
  }
  
  protected void finalize()
  {
    removeAllTargets();
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    stopRecording();
    removeAllLiveSticker();
    if (this.K != null)
    {
      this.K.destroy();
      this.K = null;
    }
    if (this.v != null)
    {
      if (!this.v.isRecycled()) {
        this.v.recycle();
      }
      this.v = null;
    }
    if (getVideoDataEncoder() != null) {
      getVideoDataEncoder().setDelegate(null);
    }
    if ((getAudioRecoder() != null) && (getAudioRecoder().getAudioEncoder() != null)) {
      getAudioRecoder().getAudioEncoder().setDelegate(null);
    }
    if ((this.k != null) && (this.n != null))
    {
      this.n.setRenderer(null);
      this.k.removeAllViews();
      this.n = null;
    }
    this.mFilterWrap.destroy();
    if (this.G != null)
    {
      this.G.destroy();
      this.G = null;
    }
  }
  
  public static enum FaceDetectionResultType
  {
    private FaceDetectionResultType() {}
  }
  
  public static abstract interface TuSDKVideoCameraFaceDetectionDelegate
  {
    public abstract void onFaceDetectionResult(TuSDKVideoCamera.FaceDetectionResultType paramFaceDetectionResultType, int paramInt);
  }
  
  public static abstract interface TuSDKVideoCameraDelegate
    extends VideoFilterDelegate
  {
    public abstract void onVideoCameraStateChanged(SelesVideoCameraInterface paramSelesVideoCameraInterface, TuSdkStillCameraAdapter.CameraState paramCameraState);
    
    public abstract void onVideoCameraScreenShot(SelesVideoCameraInterface paramSelesVideoCameraInterface, Bitmap paramBitmap);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\utils\hardware\TuSDKVideoCamera.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */