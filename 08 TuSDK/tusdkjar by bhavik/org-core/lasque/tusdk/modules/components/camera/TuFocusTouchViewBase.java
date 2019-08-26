package org.lasque.tusdk.modules.components.camera;

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
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.ViewSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraAutoFocus;
import org.lasque.tusdk.core.utils.hardware.TuSdkFace;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter.CameraState;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraInterface;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuFocusTouchViewBase
  extends TuSdkRelativeLayout
{
  public static final long SamplingDistance = 2000L;
  public static final float SamplingRange = 50.0F;
  public static final long FaceDetectionDistance = 5000L;
  private long a = 0L;
  private TuSdkStillCameraInterface b;
  private PointF c;
  private MediaPlayer d;
  private boolean e;
  private RectF f;
  private Runnable g = new Runnable()
  {
    public void run()
    {
      TuFocusTouchViewBase.a(TuFocusTouchViewBase.this, TuFocusTouchViewBase.this.getLastPoint());
    }
  };
  protected final List<View> mFaceViews = new ArrayList();
  private SensorManager h;
  private Sensor i;
  private boolean j;
  private long k = 0L;
  private float l = 0.0F;
  private SensorEventListener m = new SensorEventListener()
  {
    public void onAccuracyChanged(Sensor paramAnonymousSensor, int paramAnonymousInt) {}
    
    public void onSensorChanged(SensorEvent paramAnonymousSensorEvent)
    {
      float f = paramAnonymousSensorEvent.values[0];
      long l = Calendar.getInstance().getTimeInMillis();
      if ((l - TuFocusTouchViewBase.b(TuFocusTouchViewBase.this) > 2000L) && (Math.abs(TuFocusTouchViewBase.c(TuFocusTouchViewBase.this) - f) > 50.0F))
      {
        TuFocusTouchViewBase.a(TuFocusTouchViewBase.this, l);
        TuFocusTouchViewBase.a(TuFocusTouchViewBase.this, f);
        TuFocusTouchViewBase.this.notifyFoucs(TuFocusTouchViewBase.this.getLastPoint(), false);
      }
    }
  };
  
  public TuFocusTouchViewBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuFocusTouchViewBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuFocusTouchViewBase(Context paramContext)
  {
    super(paramContext);
  }
  
  public abstract boolean isDisableFocusBeep();
  
  public abstract boolean isEnableLongTouchCapture();
  
  protected abstract long getLongPressDistance();
  
  protected abstract int getLongPressOffset();
  
  public abstract boolean isDisableContinueFoucs();
  
  public abstract void setRangeViewFoucsState(boolean paramBoolean);
  
  public TuSdkStillCameraInterface getCamera()
  {
    return this.b;
  }
  
  public void setCamera(TuSdkStillCameraInterface paramTuSdkStillCameraInterface)
  {
    this.b = paramTuSdkStillCameraInterface;
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
    this.f = paramRectF;
  }
  
  public RectF getRegionPercent()
  {
    if (this.f == null) {
      this.f = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
    }
    return this.f;
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
  
  protected boolean notifyFoucs(PointF paramPointF, final boolean paramBoolean)
  {
    if ((this.b == null) || (!this.b.canSupportAutoFocus()) || (this.b.getState() != TuSdkStillCameraAdapter.CameraState.StateStarted) || (!b(paramPointF))) {
      return false;
    }
    setAutoContinueFocus(false);
    this.b.autoFocus(CameraConfigs.CameraAutoFocus.Auto, getRatioPoint(paramPointF), new SelesBaseCameraInterface.TuSdkAutoFocusCallback()
    {
      public void onAutoFocus(boolean paramAnonymousBoolean, SelesBaseCameraInterface paramAnonymousSelesBaseCameraInterface)
      {
        TuFocusTouchViewBase.a(TuFocusTouchViewBase.this, paramAnonymousBoolean);
        if (paramBoolean) {
          TuFocusTouchViewBase.a(TuFocusTouchViewBase.this).captureImage();
        }
      }
    });
    return true;
  }
  
  private void a(boolean paramBoolean)
  {
    setRangeViewFoucsState(paramBoolean);
    c();
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
  
  private void c(PointF paramPointF)
  {
    this.e = true;
    notifyFoucs(paramPointF, true);
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getPointerCount() > 1)
    {
      ThreadHelper.cancel(this.g);
      return super.onTouchEvent(paramMotionEvent);
    }
    this.a = Calendar.getInstance().getTimeInMillis();
    switch (paramMotionEvent.getAction())
    {
    case 0: 
      a(paramMotionEvent);
      break;
    case 1: 
      b(paramMotionEvent);
      break;
    case 2: 
      c(paramMotionEvent);
      break;
    default: 
      this.e = true;
      d(paramMotionEvent);
    }
    return true;
  }
  
  private void a(MotionEvent paramMotionEvent)
  {
    this.e = false;
    d(paramMotionEvent);
    b();
  }
  
  private void b()
  {
    if (isEnableLongTouchCapture()) {
      ThreadHelper.postDelayed(this.g, getLongPressDistance());
    }
  }
  
  private void b(MotionEvent paramMotionEvent)
  {
    if (this.e) {
      return;
    }
    this.e = true;
    d(paramMotionEvent);
    notifyFoucs(getLastPoint(), false);
  }
  
  private void c(MotionEvent paramMotionEvent)
  {
    if (this.e) {
      return;
    }
    if (e(paramMotionEvent))
    {
      d(paramMotionEvent);
      b();
    }
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
  
  private void d(MotionEvent paramMotionEvent)
  {
    PointF localPointF = getLastPoint();
    localPointF.x = paramMotionEvent.getX();
    localPointF.y = paramMotionEvent.getY();
    ThreadHelper.cancel(this.g);
  }
  
  private boolean e(MotionEvent paramMotionEvent)
  {
    PointF localPointF = getLastPoint();
    return (Math.abs(localPointF.x - paramMotionEvent.getX()) > getLongPressOffset()) || (Math.abs(localPointF.y - paramMotionEvent.getY()) > getLongPressOffset());
  }
  
  private void c()
  {
    MediaPlayer localMediaPlayer = a();
    if (localMediaPlayer == null) {
      return;
    }
    localMediaPlayer.start();
  }
  
  public void cameraStateChanged(TuSdkStillCameraInterface paramTuSdkStillCameraInterface, TuSdkStillCameraAdapter.CameraState paramCameraState)
  {
    if (paramCameraState == TuSdkStillCameraAdapter.CameraState.StateStarted) {
      a(null);
    } else {
      hiddenFaceViews();
    }
    if ((paramTuSdkStillCameraInterface == null) || (!paramTuSdkStillCameraInterface.canSupportAutoFocus())) {
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
      int n = 0;
      i1 = paramList.size() - this.mFaceViews.size();
      while (n < i1)
      {
        localObject = buildFaceDetectionView();
        if (localObject != null)
        {
          showView((View)localObject, false);
          addView((View)localObject);
          this.mFaceViews.add(localObject);
        }
        n++;
      }
    }
    if (this.mFaceViews.size() < paramList.size()) {
      return;
    }
    RectF localRectF = makeRectRelativeImage(paramTuSdkSize);
    int i1 = 0;
    Object localObject = paramList.iterator();
    while (((Iterator)localObject).hasNext())
    {
      TuSdkFace localTuSdkFace = (TuSdkFace)((Iterator)localObject).next();
      Rect localRect = transforRect(localTuSdkFace.rect, localRectF);
      if (localRect != null)
      {
        View localView = (View)this.mFaceViews.get(i1);
        setRect(localView, localRect);
        showView(localView, true);
        if (i1 == 0) {
          a(localRect);
        }
        i1++;
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
  
  protected final Rect transforRect(RectF paramRectF1, RectF paramRectF2)
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
    if (this.h == null) {
      this.h = ((SensorManager)ContextUtils.getSystemService(getContext(), "sensor"));
    }
    return this.h;
  }
  
  public void setSensorManager(SensorManager paramSensorManager)
  {
    this.h = paramSensorManager;
  }
  
  public Sensor getSensor()
  {
    if (this.i == null)
    {
      SensorManager localSensorManager = getSensorManager();
      if (localSensorManager != null) {
        this.i = localSensorManager.getDefaultSensor(5);
      }
    }
    return this.i;
  }
  
  public void setSensor(Sensor paramSensor)
  {
    this.i = paramSensor;
  }
  
  protected void setAutoContinueFocus(boolean paramBoolean)
  {
    if ((isDisableContinueFoucs()) || (getSensor() == null)) {
      return;
    }
    if (!paramBoolean)
    {
      this.h.unregisterListener(this.m);
      this.j = false;
    }
    else if (!this.j)
    {
      this.j = true;
      this.k = Calendar.getInstance().getTimeInMillis();
      this.h.registerListener(this.m, this.i, 3);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\camera\TuFocusTouchViewBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */