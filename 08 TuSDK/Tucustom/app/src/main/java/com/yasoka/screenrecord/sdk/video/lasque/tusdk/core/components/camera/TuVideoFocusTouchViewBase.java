// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.components.camera;

//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.hardware.TuSdkFace;
import java.util.Iterator;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
//import org.lasque.tusdk.core.struct.ViewSize;
//import org.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
//import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.TuSdkBundle;
import java.util.Calendar;
import android.hardware.SensorEvent;
import java.util.ArrayList;
import android.util.AttributeSet;
import android.content.Context;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.View;
import java.util.List;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkBundle;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.ViewSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkFace;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources.SelesVideoCameraInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.utils.hardware.TuSDKVideoCameraFocusViewInterface;
//import org.lasque.tusdk.core.seles.sources.SelesVideoCameraInterface;
//import org.lasque.tusdk.core.utils.hardware.TuSDKVideoCameraFocusViewInterface;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class
TuVideoFocusTouchViewBase extends TuSdkRelativeLayout implements TuSDKVideoCameraFocusViewInterface
{
    public static final long SamplingDistance = 2000L;
    public static final float SamplingRange = 50.0f;
    public static final long FaceDetectionDistance = 5000L;
    private long a;
    private SelesVideoCameraInterface b;
    private PointF c;
    private MediaPlayer d;
    private boolean e;
    private boolean f;
    private boolean g;
    private RectF h;
    private boolean i;
    private boolean j;
    private GestureListener k;
    private boolean l;
    private float m;
    private float n;
    protected final List<View> mFaceViews;
    private SensorManager o;
    private Sensor p;
    private boolean q;
    private long r;
    private float s;
    private SensorEventListener t;
    
    public TuVideoFocusTouchViewBase(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.a = 0L;
        this.j = true;
        this.mFaceViews = new ArrayList<View>();
        this.r = 0L;
        this.s = 0.0f;
        this.t = (SensorEventListener)new SensorEventListener() {
            public void onAccuracyChanged(final Sensor sensor, final int n) {
            }
            
            public void onSensorChanged(final SensorEvent sensorEvent) {
                final float n = sensorEvent.values[0];
                final long timeInMillis = Calendar.getInstance().getTimeInMillis();
                if (timeInMillis - TuVideoFocusTouchViewBase.this.r > 2000L && Math.abs(TuVideoFocusTouchViewBase.this.s - n) > 50.0f) {
                    TuVideoFocusTouchViewBase.this.r = timeInMillis;
                    TuVideoFocusTouchViewBase.this.s = n;
                    TuVideoFocusTouchViewBase.this.notifyFoucs(TuVideoFocusTouchViewBase.this.getLastPoint(), false);
                }
            }
        };
    }
    
    public TuVideoFocusTouchViewBase(final Context context, final AttributeSet set) {
        super(context, set);
        this.a = 0L;
        this.j = true;
        this.mFaceViews = new ArrayList<View>();
        this.r = 0L;
        this.s = 0.0f;
        this.t = (SensorEventListener)new SensorEventListener() {
            public void onAccuracyChanged(final Sensor sensor, final int n) {
            }
            
            public void onSensorChanged(final SensorEvent sensorEvent) {
                final float n = sensorEvent.values[0];
                final long timeInMillis = Calendar.getInstance().getTimeInMillis();
                if (timeInMillis - TuVideoFocusTouchViewBase.this.r > 2000L && Math.abs(TuVideoFocusTouchViewBase.this.s - n) > 50.0f) {
                    TuVideoFocusTouchViewBase.this.r = timeInMillis;
                    TuVideoFocusTouchViewBase.this.s = n;
                    TuVideoFocusTouchViewBase.this.notifyFoucs(TuVideoFocusTouchViewBase.this.getLastPoint(), false);
                }
            }
        };
    }
    
    public TuVideoFocusTouchViewBase(final Context context) {
        super(context);
        this.a = 0L;
        this.j = true;
        this.mFaceViews = new ArrayList<View>();
        this.r = 0L;
        this.s = 0.0f;
        this.t = (SensorEventListener)new SensorEventListener() {
            public void onAccuracyChanged(final Sensor sensor, final int n) {
            }
            
            public void onSensorChanged(final SensorEvent sensorEvent) {
                final float n = sensorEvent.values[0];
                final long timeInMillis = Calendar.getInstance().getTimeInMillis();
                if (timeInMillis - TuVideoFocusTouchViewBase.this.r > 2000L && Math.abs(TuVideoFocusTouchViewBase.this.s - n) > 50.0f) {
                    TuVideoFocusTouchViewBase.this.r = timeInMillis;
                    TuVideoFocusTouchViewBase.this.s = n;
                    TuVideoFocusTouchViewBase.this.notifyFoucs(TuVideoFocusTouchViewBase.this.getLastPoint(), false);
                }
            }
        };
    }
    
    public abstract void setRangeViewFoucsState(final boolean p0);
    
    public SelesVideoCameraInterface getCamera() {
        return this.b;
    }
    
    public void setCamera(final SelesVideoCameraInterface b) {
        this.b = b;
    }
    
    public boolean isDisableFocusBeep() {
        return this.f;
    }
    
    public void setDisableFocusBeep(final boolean f) {
        this.f = f;
    }
    
    public boolean isDisableContinueFoucs() {
        return this.g;
    }
    
    public void setDisableContinueFoucs(final boolean g) {
        this.g = g;
    }
    
    public boolean isEnableFaceFeatureDetection() {
        return this.i;
    }
    
    public void setEnableFaceFeatureDetection(final boolean i) {
        this.i = i;
    }
    
    private MediaPlayer a() {
        if (this.isDisableFocusBeep()) {
            return null;
        }
        if (this.d == null) {
            this.d = TuSdkContext.loadMediaAsset(TuSdkBundle.sdkBundleOther("camera_focus_beep.mp3"));
        }
        return this.d;
    }
    
    protected PointF getLastPoint() {
        if (this.c == null) {
            this.c = new PointF(this.getWidth() * 0.5f, this.getHeight() * 0.5f);
        }
        return this.c;
    }
    
    private void a(final PointF c) {
        this.c = c;
    }
    
    public void setRegionPercent(final RectF h) {
        this.h = h;
    }
    
    public RectF getRegionPercent() {
        if (this.h == null) {
            this.h = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        }
        return this.h;
    }
    
    public void viewWillDestory() {
        super.viewWillDestory();
        this.b = null;
        if (this.d != null) {
            this.d.release();
            this.d = null;
        }
    }
    
    protected boolean notifyFoucs(final PointF pointF, final boolean b) {
        if (this.b == null || !this.b.canSupportAutoFocus() || this.b.getState() != TuSdkStillCameraAdapter.CameraState.StateStarted || !this.b(pointF)) {
            return false;
        }
        this.setAutoContinueFocus(false);
        this.b.autoFocus(CameraConfigs.CameraAutoFocus.Auto, this.getRatioPoint(pointF), (SelesBaseCameraInterface.TuSdkAutoFocusCallback)new SelesBaseCameraInterface.TuSdkAutoFocusCallback() {
            public void onAutoFocus(final boolean b, final SelesBaseCameraInterface selesBaseCameraInterface) {
                TuVideoFocusTouchViewBase.this.a(b);
            }
        });
        return true;
    }
    
    private void a(final boolean rangeViewFoucsState) {
        this.setRangeViewFoucsState(rangeViewFoucsState);
        this.b();
        this.setAutoContinueFocus(true);
    }
    
    protected RectF getRegionRectF() {
        final ViewSize create = ViewSize.create((View)this);
        return new RectF(this.getRegionPercent().left * ((TuSdkSize)create).width, this.getRegionPercent().top * ((TuSdkSize)create).height, this.getRegionPercent().right * ((TuSdkSize)create).width, this.getRegionPercent().bottom * ((TuSdkSize)create).height);
    }
    
    private boolean b(final PointF pointF) {
        return this.getRegionRectF().contains(pointF.x, pointF.y);
    }
    
    public void isShowFoucusView(final boolean j) {
        this.j = j;
    }
    
    public void setGestureListener(final GestureListener k) {
        this.k = k;
    }
    
    @SuppressLint({ "ClickableViewAccessibility" })
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() > 1) {
            return super.onTouchEvent(motionEvent);
        }
        this.a = Calendar.getInstance().getTimeInMillis();
        switch (motionEvent.getAction()) {
            case 0: {
                this.b(motionEvent);
                break;
            }
            case 1: {
                this.c(motionEvent);
                break;
            }
            case 2: {
                this.d(motionEvent);
                break;
            }
            default: {
                this.e = true;
                this.l = false;
                break;
            }
        }
        return true;
    }
    
    private void a(final MotionEvent motionEvent) {
        final PointF lastPoint = this.getLastPoint();
        lastPoint.x = motionEvent.getX();
        lastPoint.y = motionEvent.getY();
    }
    
    private void b(final MotionEvent motionEvent) {
        this.e = false;
        this.l = true;
        final float x = motionEvent.getX();
        this.m = x;
        this.n = x;
        this.a(motionEvent);
    }
    
    private void c(final MotionEvent motionEvent) {
        this.l = false;
        if (this.k != null && Math.abs(this.n - this.m) < 50.0f) {
            this.k.onClick();
        }
        if (this.e) {
            return;
        }
        this.e = true;
        this.a(motionEvent);
        this.notifyFoucs(this.getLastPoint(), false);
        if (this.j) {
            this.showFocusView(this.getLastPoint());
        }
    }
    
    public abstract void showFocusView(final PointF p0);
    
    private void d(final MotionEvent motionEvent) {
        this.n = motionEvent.getX();
        if (this.n - this.m > 0.0f && Math.abs(this.n - this.m) > 50.0f && this.l) {
            this.l = false;
            if (this.k != null) {
                this.k.onRightGesture();
            }
        }
        else if (this.n - this.m < 0.0f && Math.abs(this.n - this.m) > 50.0f && this.l) {
            this.l = false;
            if (this.k != null) {
                this.k.onLeftGesture();
            }
        }
        if (this.e) {
            return;
        }
        this.a(motionEvent);
    }
    
    protected final PointF getRatioPoint(final PointF pointF) {
        if (pointF == null) {
            return null;
        }
        final PointF pointF2 = new PointF();
        pointF2.x = pointF.x / this.getWidth();
        pointF2.y = pointF.y / this.getHeight();
        return pointF2;
    }
    
    private void b() {
        final MediaPlayer a = this.a();
        if (a == null) {
            return;
        }
        a.start();
    }
    
    public void cameraStateChanged(final SelesVideoCameraInterface selesVideoCameraInterface, final TuSdkStillCameraAdapter.CameraState cameraState) {
        if (cameraState == TuSdkStillCameraAdapter.CameraState.StateStarted) {
            this.a((PointF)null);
        }
        else {
            this.hiddenFaceViews();
        }
        if (selesVideoCameraInterface == null || !selesVideoCameraInterface.canSupportAutoFocus()) {
            return;
        }
        this.setAutoContinueFocus(cameraState == TuSdkStillCameraAdapter.CameraState.StateStarted);
    }
    
    protected void hiddenFaceViews() {
        final Iterator<View> iterator = this.mFaceViews.iterator();
        while (iterator.hasNext()) {
            this.showView((View)iterator.next(), false);
        }
    }
    
    protected abstract View buildFaceDetectionView();
    
    public void setCameraFaceDetection(final List<TuSdkFace> list, final TuSdkSize tuSdkSize) {
        this.hiddenFaceViews();
        if (list == null || tuSdkSize == null || list.isEmpty()) {
            return;
        }
        if (this.mFaceViews.size() < list.size()) {
            for (int i = 0; i < list.size() - this.mFaceViews.size(); ++i) {
                final View buildFaceDetectionView = this.buildFaceDetectionView();
                if (buildFaceDetectionView != null) {
                    this.showView(buildFaceDetectionView, false);
                    this.addView(buildFaceDetectionView);
                    this.mFaceViews.add(buildFaceDetectionView);
                }
            }
        }
        if (this.mFaceViews.size() > 0 && this.mFaceViews.size() < list.size()) {
            return;
        }
        final RectF rectRelativeImage = this.makeRectRelativeImage(tuSdkSize);
        int n = 0;
        final Iterator<TuSdkFace> iterator = list.iterator();
        while (iterator.hasNext()) {
            final Rect a = this.a(iterator.next().rect, rectRelativeImage);
            if (a == null) {
                continue;
            }
            View view = null;
            if (this.mFaceViews.size() > 0) {
                view = this.mFaceViews.get(n);
            }
            if (view != null) {
                this.setRect(view, a);
                this.showView(view, true);
            }
            if (n == 0) {
                this.a(a);
            }
            ++n;
        }
    }
    
    protected final RectF makeRectRelativeImage(final TuSdkSize tuSdkSize) {
        if (tuSdkSize == null || !tuSdkSize.isSize()) {
            return null;
        }
        return RectHelper.makeRectWithAspectRatioOutsideRect(tuSdkSize, this.getRegionRectF());
    }
    
    private Rect a(final RectF rectF, final RectF rectF2) {
        if (rectF == null || rectF2 == null) {
            return null;
        }
        final Rect rect = new Rect();
        rect.left = (int)(rectF.left * rectF2.width() - rectF2.left);
        rect.right = (int)(rectF.right * rectF2.width() - rectF2.left);
        rect.top = (int)(rectF.top * rectF2.height() - rectF2.top);
        rect.bottom = (int)(rectF.bottom * rectF2.height() - rectF2.top);
        return rect;
    }
    
    private void a(final Rect rect) {
        final long timeInMillis = Calendar.getInstance().getTimeInMillis();
        if (rect == null || timeInMillis - this.a < 5000L) {
            return;
        }
        this.a = timeInMillis;
        this.c = new PointF((float)rect.centerX(), (float)rect.centerY());
        if (this.b == null) {
            return;
        }
        if (this.b.canSupportAutoFocus()) {
            this.notifyFoucs(this.getLastPoint(), false);
        }
        else {
            this.b.autoMetering(this.getLastPoint());
        }
    }
    
    public SensorManager getSensorManager() {
        if (this.o == null) {
            this.o = (SensorManager) ContextUtils.getSystemService(this.getContext(), "sensor");
        }
        return this.o;
    }
    
    public void setSensorManager(final SensorManager o) {
        this.o = o;
    }
    
    public Sensor getSensor() {
        if (this.p == null) {
            final SensorManager sensorManager = this.getSensorManager();
            if (sensorManager != null) {
                this.p = sensorManager.getDefaultSensor(5);
            }
        }
        return this.p;
    }
    
    public void setSensor(final Sensor p) {
        this.p = p;
    }
    
    protected void setAutoContinueFocus(final boolean b) {
        if (this.isDisableContinueFoucs() || this.getSensor() == null) {
            if (this.getSensor() != null) {
                this.o.unregisterListener(this.t);
                this.q = false;
            }
            return;
        }
        if (!b) {
            this.o.unregisterListener(this.t);
            this.q = false;
        }
        else if (!this.q) {
            this.q = true;
            this.r = Calendar.getInstance().getTimeInMillis();
            this.o.registerListener(this.t, this.p, 3);
        }
    }
    
    public interface GestureListener
    {
        void onLeftGesture();
        
        void onRightGesture();
        
        void onClick();
    }
}
