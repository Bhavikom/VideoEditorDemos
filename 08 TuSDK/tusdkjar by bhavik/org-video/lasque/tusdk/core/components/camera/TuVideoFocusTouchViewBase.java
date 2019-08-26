package org.lasque.tusdk.core.components.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdkBundle;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface;
import org.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface.TuSdkAutoFocusCallback;
import org.lasque.tusdk.core.seles.sources.SelesVideoCameraInterface;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.ViewSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraAutoFocus;
import org.lasque.tusdk.core.utils.hardware.TuSDKVideoCameraFocusViewInterface;
import org.lasque.tusdk.core.utils.hardware.TuSdkFace;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter.CameraState;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuVideoFocusTouchViewBase
  extends TuSdkRelativeLayout
  implements TuSDKVideoCameraFocusViewInterface
{
  public static final long SamplingDistance = 2000L;
  public static final float SamplingRange = 50.0F;
  public static final long FaceDetectionDistance = 5000L;
  private long a = 0L;
  private SelesVideoCameraInterface b;
  private PointF c;
  private MediaPlayer d;
  private boolean e;
  private boolean f;
  private boolean g;
  private RectF h;
  private boolean i;
  private boolean j = true;
  private GestureListener k;
  private boolean l;
  private float m;
  private float n;
  protected final List<View> mFaceViews = new ArrayList();
  private SensorManager o;
  private Sensor p;
  private boolean q;
  private long r = 0L;
  private float s = 0.0F;
  private SensorEventListener t = new SensorEventListener()
  {
    public void onAccuracyChanged(Sensor paramAnonymousSensor, int paramAnonymousInt) {}
    
    public void onSensorChanged(SensorEvent paramAnonymousSensorEvent)
    {
      float f = paramAnonymousSensorEvent.values[0];
      long l = Calendar.getInstance().getTimeInMillis();
      if ((l - TuVideoFocusTouchViewBase.a(TuVideoFocusTouchViewBase.this) > 2000L) && (Math.abs(TuVideoFocusTouchViewBase.b(TuVideoFocusTouchViewBase.this) - f) > 50.0F))
      {
        TuVideoFocusTouchViewBase.a(TuVideoFocusTouchViewBase.this, l);
        TuVideoFocusTouchViewBase.a(TuVideoFocusTouchViewBase.this, f);
        TuVideoFocusTouchViewBase.this.notifyFoucs(TuVideoFocusTouchViewBase.this.getLastPoint(), false);
      }
    }
  };
  
  public TuVideoFocusTouchViewBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuVideoFocusTouchViewBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuVideoFocusTouchViewBase(Context paramContext)
  {
    super(paramContext);
  }
  
  public abstract void setRangeViewFoucsState(boolean paramBoolean);
  
  public SelesVideoCameraInterface getCamera()
  {
    return this.b;
  }
  
  public void setCamera(SelesVideoCameraInterface paramSelesVideoCameraInterface)
  {
    this.b = paramSelesVideoCameraInterface;
  }
  
  public boolean isDisableFocusBeep()
  {
    return this.f;
  }
  
  public void setDisableFocusBeep(boolean paramBoolean)
  {
    this.f = paramBoolean;
  }
  
  public boolean isDisableContinueFoucs()
  {
    return this.g;
  }
  
  public void setDisableContinueFoucs(boolean paramBoolean)
  {
    this.g = paramBoolean;
  }
  
  public boolean isEnableFaceFeatureDetection()
  {
    return this.i;
  }
  
  public void setEnableFaceFeatureDetection(boolean paramBoolean)
  {
    this.i = paramBoolean;
  }
  
  private MediaPlayer a()
  {
    if (isDisableFocusBeep()) {
      return null;
    }
    if (this.d == null)
    {
      String str = TuSdkBundle.sdkBundleOther("camera_focus_beep.mp3");
      this.d = TuSdkContext.loadMediaAsset(str);
    }
    return this.d;
  }
  
  protected PointF getLastPoint()
  {
    if (this.c == null) {
      this.c = new PointF(getWidth() * 0.5F, getHeight() * 0.5F);
    }
    return this.c;
  }
  
  private void a(PointF paramPointF)
  {
    this.c = paramPointF;
  }
  
  public void setRegionPercent(RectF paramRectF)
  {
    this.h = paramRectF;
  }
  
  public RectF getRegionPercent()
  {
    if (this.h == null) {
      this.h = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
    }
    return this.h;
  }
  
  public void viewWillDestory()
  {
    super.viewWillDestory();
    this.b = null;
    if (this.d != null)
    {
      this.d.release();
      this.d = null;
    }
  }
  
  protected boolean notifyFoucs(PointF paramPointF, boolean paramBoolean)
  {
    if ((this.b == null) || (!this.b.canSupportAutoFocus()) || (this.b.getState() != TuSdkStillCameraAdapter.CameraState.StateStarted) || (!b(paramPointF))) {
      return false;
    }
    setAutoContinueFocus(false);
    this.b.autoFocus(CameraConfigs.CameraAutoFocus.Auto, getRatioPoint(paramPointF), new SelesBaseCameraInterface.TuSdkAutoFocusCallback()
    {
      public void onAutoFocus(boolean paramAnonymousBoolean, SelesBaseCameraInterface paramAnonymousSelesBaseCameraInterface)
      {
        TuVideoFocusTouchViewBase.a(TuVideoFocusTouchViewBase.this, paramAnonymousBoolean);
      }
    });
    return true;
  }
  
  private void a(boolean paramBoolean)
  {
    setRangeViewFoucsState(paramBoolean);
    b();
    setAutoContinueFocus(true);
  }
  
  protected RectF getRegionRectF()
  {
    ViewSize localViewSize = ViewSize.create(this);
    float f1 = getRegionPercent().left * localViewSize.width;
    float f2 = getRegionPercent().top * localViewSize.height;
    float f3 = getRegionPercent().right * localViewSize.width;
    float f4 = getRegionPercent().bottom * localViewSize.height;
    RectF localRectF = new RectF(f1, f2, f3, f4);
    return localRectF;
  }
  
  private boolean b(PointF paramPointF)
  {
    return getRegionRectF().contains(paramPointF.x, paramPointF.y);
  }
  
  public void isShowFoucusView(boolean paramBoolean)
  {
    this.j = paramBoolean;
  }
  
  public void setGestureListener(GestureListener paramGestureListener)
  {
    this.k = paramGestureListener;
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getPointerCount() > 1) {
      return super.onTouchEvent(paramMotionEvent);
    }
    this.a = Calendar.getInstance().getTimeInMillis();
    switch (paramMotionEvent.getAction())
    {
    case 0: 
      b(paramMotionEvent);
      break;
    case 1: 
      c(paramMotionEvent);
      break;
    case 2: 
      d(paramMotionEvent);
      break;
    default: 
      this.e = true;
      this.l = false;
    }
    return true;
  }
  
  private void a(MotionEvent paramMotionEvent)
  {
    PointF localPointF = getLastPoint();
    localPointF.x = paramMotionEvent.getX();
    localPointF.y = paramMotionEvent.getY();
  }
  
  private void b(MotionEvent paramMotionEvent)
  {
    this.e = false;
    this.l = true;
    this.n = (this.m = paramMotionEvent.getX());
    a(paramMotionEvent);
  }
  
  private void c(MotionEvent paramMotionEvent)
  {
    this.l = false;
    if ((this.k != null) && (Math.abs(this.n - this.m) < 50.0F)) {
      this.k.onClick();
    }
    if (this.e) {
      return;
    }
    this.e = true;
    a(paramMotionEvent);
    notifyFoucs(getLastPoint(), false);
    if (this.j) {
      showFocusView(getLastPoint());
    }
  }
  
  public abstract void showFocusView(PointF paramPointF);
  
  private void d(MotionEvent paramMotionEvent)
  {
    this.n = paramMotionEvent.getX();
    if ((this.n - this.m > 0.0F) && (Math.abs(this.n - this.m) > 50.0F) && (this.l))
    {
      this.l = false;
      if (this.k != null) {
        this.k.onRightGesture();
      }
    }
    else if ((this.n - this.m < 0.0F) && (Math.abs(this.n - this.m) > 50.0F) && (this.l))
    {
      this.l = false;
      if (this.k != null) {
        this.k.onLeftGesture();
      }
    }
    if (this.e) {
      return;
    }
    a(paramMotionEvent);
  }
  
  protected final PointF getRatioPoint(PointF paramPointF)
  {
    if (paramPointF == null) {
      return null;
    }
    PointF localPointF = new PointF();
    paramPointF.x /= getWidth();
    paramPointF.y /= getHeight();
    return localPointF;
  }
  
  private void b()
  {
    MediaPlayer localMediaPlayer = a();
    if (localMediaPlayer == null) {
      return;
    }
    localMediaPlayer.start();
  }
  
  public void cameraStateChanged(SelesVideoCameraInterface paramSelesVideoCameraInterface, TuSdkStillCameraAdapter.CameraState paramCameraState)
  {
    if (paramCameraState == TuSdkStillCameraAdapter.CameraState.StateStarted) {
      a(null);
    } else {
      hiddenFaceViews();
    }
    if ((paramSelesVideoCameraInterface == null) || (!paramSelesVideoCameraInterface.canSupportAutoFocus())) {
      return;
    }
    setAutoContinueFocus(paramCameraState == TuSdkStillCameraAdapter.CameraState.StateStarted);
  }
  
  protected void hiddenFaceViews()
  {
    Iterator localIterator = this.mFaceViews.iterator();
    while (localIterator.hasNext())
    {
      View localView = (View)localIterator.next();
      showView(localView, false);
    }
  }
  
  protected abstract View buildFaceDetectionView();
  
  public void setCameraFaceDetection(List<TuSdkFace> paramList, TuSdkSize paramTuSdkSize)
  {
    hiddenFaceViews();
    if ((paramList == null) || (paramTuSdkSize == null) || (paramList.isEmpty())) {
      return;
    }
    if (this.mFaceViews.size() < paramList.size())
    {
      int i1 = 0;
      i2 = paramList.size() - this.mFaceViews.size();
      while (i1 < i2)
      {
        localObject = buildFaceDetectionView();
        if (localObject != null)
        {
          showView((View)localObject, false);
          addView((View)localObject);
          this.mFaceViews.add(localObject);
        }
        i1++;
      }
    }
    if ((this.mFaceViews.size() > 0) && (this.mFaceViews.size() < paramList.size())) {
      return;
    }
    RectF localRectF = makeRectRelativeImage(paramTuSdkSize);
    int i2 = 0;
    Object localObject = paramList.iterator();
    while (((Iterator)localObject).hasNext())
    {
      TuSdkFace localTuSdkFace = (TuSdkFace)((Iterator)localObject).next();
      Rect localRect = a(localTuSdkFace.rect, localRectF);
      if (localRect != null)
      {
        View localView = null;
        if (this.mFaceViews.size() > 0) {
          localView = (View)this.mFaceViews.get(i2);
        }
        if (localView != null)
        {
          setRect(localView, localRect);
          showView(localView, true);
        }
        if (i2 == 0) {
          a(localRect);
        }
        i2++;
      }
    }
  }
  
  protected final RectF makeRectRelativeImage(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return null;
    }
    RectF localRectF1 = getRegionRectF();
    RectF localRectF2 = RectHelper.makeRectWithAspectRatioOutsideRect(paramTuSdkSize, localRectF1);
    return localRectF2;
  }
  
  private Rect a(RectF paramRectF1, RectF paramRectF2)
  {
    if ((paramRectF1 == null) || (paramRectF2 == null)) {
      return null;
    }
    Rect localRect = new Rect();
    localRect.left = ((int)(paramRectF1.left * paramRectF2.width() - paramRectF2.left));
    localRect.right = ((int)(paramRectF1.right * paramRectF2.width() - paramRectF2.left));
    localRect.top = ((int)(paramRectF1.top * paramRectF2.height() - paramRectF2.top));
    localRect.bottom = ((int)(paramRectF1.bottom * paramRectF2.height() - paramRectF2.top));
    return localRect;
  }
  
  private void a(Rect paramRect)
  {
    long l1 = Calendar.getInstance().getTimeInMillis();
    if ((paramRect == null) || (l1 - this.a < 5000L)) {
      return;
    }
    this.a = l1;
    this.c = new PointF(paramRect.centerX(), paramRect.centerY());
    if (this.b == null) {
      return;
    }
    if (this.b.canSupportAutoFocus()) {
      notifyFoucs(getLastPoint(), false);
    } else {
      this.b.autoMetering(getLastPoint());
    }
  }
  
  public SensorManager getSensorManager()
  {
    if (this.o == null) {
      this.o = ((SensorManager)ContextUtils.getSystemService(getContext(), "sensor"));
    }
    return this.o;
  }
  
  public void setSensorManager(SensorManager paramSensorManager)
  {
    this.o = paramSensorManager;
  }
  
  public Sensor getSensor()
  {
    if (this.p == null)
    {
      SensorManager localSensorManager = getSensorManager();
      if (localSensorManager != null) {
        this.p = localSensorManager.getDefaultSensor(5);
      }
    }
    return this.p;
  }
  
  public void setSensor(Sensor paramSensor)
  {
    this.p = paramSensor;
  }
  
  protected void setAutoContinueFocus(boolean paramBoolean)
  {
    if ((isDisableContinueFoucs()) || (getSensor() == null))
    {
      if (getSensor() != null)
      {
        this.o.unregisterListener(this.t);
        this.q = false;
      }
      return;
    }
    if (!paramBoolean)
    {
      this.o.unregisterListener(this.t);
      this.q = false;
    }
    else if (!this.q)
    {
      this.q = true;
      this.r = Calendar.getInstance().getTimeInMillis();
      this.o.registerListener(this.t, this.p, 3);
    }
  }
  
  public static abstract interface GestureListener
  {
    public abstract void onLeftGesture();
    
    public abstract void onRightGesture();
    
    public abstract void onClick();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\components\camera\TuVideoFocusTouchViewBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */