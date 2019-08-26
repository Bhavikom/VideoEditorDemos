// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware;

//import org.lasque.tusdk.core.seles.sources.SelesStillCameraInterface;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.exif.ExifInterface;
//import org.lasque.tusdk.core.utils.image.ExifHelper;
import android.graphics.Bitmap;
import java.util.Calendar;
import java.util.List;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import android.opengl.GLSurfaceView;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
//import org.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface;
import android.graphics.RectF;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.view.ViewGroup;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.view.View;
//import org.lasque.tusdk.core.struct.ViewSize;
//import org.lasque.tusdk.impl.view.widget.RegionDefaultHandler;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.TuSdkResult;
import android.media.MediaPlayer;
//import org.lasque.tusdk.impl.view.widget.RegionHandler;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.seles.output.SelesSmartView;
//import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener;
import android.widget.RelativeLayout;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif.ExifInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener.TuSdkOrientationEventListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesSmartView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesStillCameraInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.ViewSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ExifHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.RegionDefaultHandler;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.RegionHandler;

public class TuSdkStillCameraAdapter<T extends SelesOutput>
{
    public static final long FocusSamplingDistance = 2000L;
    private final Context a;
    private final RelativeLayout b;
    private final T c;
    private CameraState d;
    private boolean e;
    private TuSdkStillCameraInterface.TuSdkStillCameraListener f;
    private InterfaceOrientation g;
    private TuSdkOrientationEventListener h;
    private SelesSmartView i;
    private FilterWrap j;
    private RegionHandler k;
    private boolean l;
    private TuSdkVideoCameraExtendViewInterface m;
    private boolean n;
    private int o;
    private MediaPlayer p;
    private boolean q;
    private boolean r;
    private boolean s;
    private int t;
    private float u;
    private boolean v;
    private boolean w;
    private TuSdkOrientationEventListener.TuSdkOrientationDelegate x;
    private TuSdkResult y;
    
    public InterfaceOrientation getDeviceOrient() {
        return this.g;
    }
    
    public int getDeviceAngle() {
        return this.h.getDeviceAngle();
    }
    
    public CameraState getState() {
        return this.d;
    }
    
    private void a(final CameraState d) {
        this.d = d;
        if (!ThreadHelper.isMainThread()) {
            ThreadHelper.post(new Runnable() {
                @Override
                public void run() {
                    TuSdkStillCameraAdapter.this.a(d);
                }
            });
            return;
        }
        if (this.f != null) {
            this.f.onStillCameraStateChanged((TuSdkStillCameraInterface)this.c, this.d);
        }
        final TuSdkVideoCameraExtendViewInterface focusTouchView = this.getFocusTouchView();
        if (focusTouchView != null) {
            focusTouchView.cameraStateChanged((TuSdkStillCameraInterface)this.c, this.d);
        }
    }
    
    public RegionHandler getRegionHandler() {
        if (this.k == null) {
            this.k = new RegionDefaultHandler();
        }
        this.k.setWrapSize(ViewSize.create((View)this.b));
        return this.k;
    }
    
    public void setRegionHandler(final RegionHandler k) {
        this.k = k;
    }
    
    public final boolean isFilterChanging() {
        return this.e;
    }
    
    public TuSdkStillCameraInterface.TuSdkStillCameraListener getCameraListener() {
        return this.f;
    }
    
    public void setCameraListener(final TuSdkStillCameraInterface.TuSdkStillCameraListener f) {
        this.f = f;
    }
    
    public TuSdkVideoCameraExtendViewInterface getFocusTouchView() {
        return this.m;
    }
    
    public void setFocusTouchView(final TuSdkVideoCameraExtendViewInterface m) {
        if (m == null || this.i == null) {
            return;
        }
        if (this.m != null) {
            this.i.removeView((View)this.m);
            this.m.viewWillDestory();
        }
        this.getRegionHandler().setRatio(this.getRegionRatio());
        this.i.setBackgroundColor(this.getRegionViewColor());
        this.i.setDisplayRect(this.getRegionHandler().getRectPercent());
        (this.m = m).setCamera((TuSdkStillCameraInterface)this.c);
        this.m.setEnableLongTouchCapture(this.isEnableLongTouchCapture());
        this.m.setDisableFocusBeep(this.isDisableFocusBeep());
        this.m.setDisableContinueFoucs(this.isDisableContinueFoucs());
        this.m.setGuideLineViewState(this.isDisplayGuideLine());
        this.m.setEnableFilterConfig(this.isEnableFilterConfig());
        this.m.setRegionPercent(this.getRegionHandler().getRectPercent());
        this.i.addView((View)this.m, (ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
    }
    
    public void setFocusTouchView(final int n) {
        if (n == 0) {
            return;
        }
        final TuSdkVideoCameraExtendViewInterface buildView = TuSdkViewHelper.buildView(this.a, n, (ViewGroup)this.i);
        if (buildView == null || !(buildView instanceof TuSdkVideoCameraExtendViewInterface)) {
            TLog.w("The setFocusTouchView must extend TuFocusTouchView: %s", buildView);
            return;
        }
        this.setFocusTouchView(buildView);
    }
    
    public boolean isOutputImageData() {
        return this.l;
    }
    
    public void setOutputImageData(final boolean l) {
        this.l = l;
    }
    
    public boolean isEnableLongTouchCapture() {
        return this.q;
    }
    
    public void setEnableLongTouchCapture(final boolean q) {
        this.q = q;
        if (this.getFocusTouchView() != null) {
            this.getFocusTouchView().setEnableLongTouchCapture(this.q);
        }
    }
    
    public boolean isDisableCaptureSound() {
        return this.n;
    }
    
    public void setDisableCaptureSound(final boolean n) {
        this.n = n;
    }
    
    public boolean isDisableFocusBeep() {
        return this.r;
    }
    
    public void setDisableFocusBeep(final boolean r) {
        this.r = r;
        if (this.getFocusTouchView() != null) {
            this.getFocusTouchView().setDisableFocusBeep(this.r);
        }
    }
    
    public boolean isDisableContinueFoucs() {
        return this.s;
    }
    
    public void setDisableContinueFoucs(final boolean s) {
        this.s = s;
        if (this.getFocusTouchView() != null) {
            this.getFocusTouchView().setDisableContinueFoucs(this.s);
        }
    }
    
    public int getRegionViewColor() {
        return this.t;
    }
    
    public void setRegionViewColor(final int t) {
        this.t = t;
        if (this.i != null) {
            this.i.setBackgroundColor(this.t);
        }
    }
    
    public float getRegionRatio() {
        if (this.u < 0.0f) {
            this.u = 0.0f;
        }
        return this.u;
    }
    
    public void setRegionRatio(final float u) {
        this.u = u;
        if (this.getRegionHandler() != null) {
            this.k.setRatio(this.u);
            if (this.i != null) {
                this.i.setDisplayRect(this.k.getRectPercent());
            }
        }
    }
    
    public boolean isDisplayGuideLine() {
        return this.v;
    }
    
    public void setDisplayGuideLine(final boolean b) {
        this.v = b;
        if (this.getFocusTouchView() != null) {
            this.getFocusTouchView().setGuideLineViewState(b);
        }
    }
    
    public void changeRegionRatio(final float u) {
        this.u = u;
        this.getRegionHandler().changeWithRatio(this.u, new RegionHandler.RegionChangerListener() {
            @Override
            public void onRegionChanged(final RectF rectF) {
                if (TuSdkStillCameraAdapter.this.i != null) {
                    TuSdkStillCameraAdapter.this.i.setDisplayRect(rectF);
                }
                if (TuSdkStillCameraAdapter.this.getFocusTouchView() != null) {
                    TuSdkStillCameraAdapter.this.getFocusTouchView().setRegionPercent(rectF);
                }
            }
        });
    }
    
    public boolean isEnableFilterConfig() {
        return this.w;
    }
    
    public void setEnableFilterConfig(final boolean w) {
        this.w = w;
        if (this.getFocusTouchView() != null) {
            this.getFocusTouchView().setEnableFilterConfig(this.w);
        }
    }
    
    public int getCaptureSoundRawId() {
        return this.o;
    }
    
    public void setCaptureSoundRawId(final int o) {
        this.o = o;
        if (this.p != null) {
            this.p.release();
            this.p = null;
        }
        if (this.o != 0) {
            this.setDisableCaptureSound(true);
            this.p = MediaPlayer.create(this.a, this.o);
        }
    }
    
    public TuSdkStillCameraAdapter(final Context a, final RelativeLayout b, final T c) {
        this.d = CameraState.StateUnknow;
        this.g = InterfaceOrientation.Portrait;
        this.t = -16777216;
        this.u = 0.0f;
        this.v = false;
        this.x = new TuSdkOrientationEventListener.TuSdkOrientationDelegate() {
            @Override
            public void onOrientationChanged(final InterfaceOrientation interfaceOrientation) {
                TuSdkStillCameraAdapter.this.g = interfaceOrientation;
                TuSdkStillCameraAdapter.this.c();
            }
        };
        this.a = a;
        this.b = b;
        this.c = c;
        this.a();
    }
    
    private void a() {
        final ViewSize create = ViewSize.create((View)this.b);
        if (create != null && create.isSize()) {
            ((SelesBaseCameraInterface)this.c).setPreviewMaxSize(create.maxSide());
        }
        this.d = CameraState.StateUnknow;
        (this.h = new TuSdkOrientationEventListener(this.a)).setDelegate(this.x, null);
        this.i = this.b();
        (this.j = FilterLocalPackage.shared().getFilterWrap(null)).bindWithCameraView(this.i);
        ((SelesOutput)this.c).addTarget(this.j.getFilter(), 0);
    }
    
    public void onDestroy() {
        if (this.p != null) {
            this.p.release();
            this.p = null;
        }
        if (this.j != null) {
            this.j.destroy();
            this.j = null;
        }
    }
    
    private SelesSmartView b() {
        if (this.b == null) {
            TLog.e("Can not find cameraView", new Object[0]);
            return this.i;
        }
        if (this.i == null) {
            (this.i = new SelesSmartView(this.a)).setRenderer((GLSurfaceView.Renderer)this.c);
            this.b.addView((View)this.i, 0, (ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
        }
        return this.i;
    }
    
    private void c() {
        if (this.j == null) {
            return;
        }
        this.j.rotationTextures(this.g);
    }
    
    private void d() {
        if (this.j == null || this.getFocusTouchView() == null) {
            return;
        }
        this.getFocusTouchView().notifyFilterConfigView(this.j.getFilter());
    }
    
    public void onMainThreadStart() {
        if (this.i != null) {
            this.i.requestRender();
        }
    }
    
    public void pauseCameraCapture() {
        this.a(false);
    }
    
    public void resumeCameraCapture() {
        this.a(true);
        if (((SelesBaseCameraInterface)this.c).isCapturing()) {
            this.d = CameraState.StateStarted;
        }
    }
    
    public void stopCameraCapture() {
        this.a(this.e = false);
        this.a(CameraState.StateUnknow);
        if (this.getFocusTouchView() != null) {
            this.getFocusTouchView().notifyFilterConfigView(null);
        }
        this.h.disable();
    }
    
    private void a(final boolean b) {
        if (this.i == null) {
            return;
        }
        if (b) {
            this.i.setRenderModeContinuously();
        }
        else {
            this.i.setRenderModeDirty();
        }
    }
    
    public void setRendererFPS(final int rendererFPS) {
        if (rendererFPS < 1 || this.i == null) {
            return;
        }
        this.i.setRendererFPS(rendererFPS);
    }
    
    protected void onCameraStarted() {
        this.h.enable();
        this.a(CameraState.StateStarted);
        this.d();
        if (this.i != null) {
            this.resumeCameraCapture();
        }
    }
    
    public void onCameraFaceDetection(final List<TuSdkFace> list, final TuSdkSize tuSdkSize) {
        final TuSdkVideoCameraExtendViewInterface focusTouchView = this.getFocusTouchView();
        if (focusTouchView != null) {
            focusTouchView.setCameraFaceDetection(list, tuSdkSize);
        }
    }
    
    public FilterWrap getFilterWrap() {
        return this.j;
    }
    
    public Runnable switchFilter(final String s) {
        if (s == null || this.e || this.getState() == CameraState.StateCapturing || this.j.equalsCode(s)) {
            return null;
        }
        this.e = true;
        return new Runnable() {
            @Override
            public void run() {
                final FilterWrap filterWrap = FilterLocalPackage.shared().getFilterWrap(s);
                ((SelesOutput)TuSdkStillCameraAdapter.this.c).removeTarget(TuSdkStillCameraAdapter.this.j.getFilter());
                filterWrap.bindWithCameraView(TuSdkStillCameraAdapter.this.i);
                ((SelesOutput)TuSdkStillCameraAdapter.this.c).addTarget(filterWrap.getFilter(), 0);
                filterWrap.processImage();
                TuSdkStillCameraAdapter.this.j.destroy();
                TuSdkStillCameraAdapter.this.j = filterWrap;
                ThreadHelper.post(new Runnable() {
                    @Override
                    public void run() {
                        TuSdkStillCameraAdapter.this.e();
                    }
                });
            }
        };
    }
    
    private void e() {
        this.d();
        if (this.f != null && this.j != null) {
            this.f.onFilterChanged(this.j.getFilter());
        }
        this.c();
        this.e = false;
    }
    
    public boolean isCreatedSurface() {
        return this.i != null && this.i.isCreatedSurface();
    }
    
    public void captureImage() {
        if (this.d != CameraState.StateStarted) {
            return;
        }
        this.a(CameraState.StateCapturing);
        if (this.f() && ((SelesBaseCameraInterface)this.c).canSupportAutoFocus()) {
            this.g();
            ((SelesBaseCameraInterface)this.c).autoFocus(new SelesBaseCameraInterface.TuSdkAutoFocusCallback() {
                @Override
                public void onAutoFocus(final boolean b, final SelesBaseCameraInterface selesBaseCameraInterface) {
                    TuSdkStillCameraAdapter.this.b(b);
                    TuSdkStillCameraAdapter.this.c(b);
                }
            });
        }
        else {
            this.c(false);
        }
    }
    
    private boolean f() {
        return Calendar.getInstance().getTimeInMillis() - ((SelesBaseCameraInterface)this.c).getLastFocusTime() > 2000L;
    }
    
    private void g() {
        final TuSdkVideoCameraExtendViewInterface focusTouchView = this.getFocusTouchView();
        if (focusTouchView != null) {
            focusTouchView.showRangeView();
        }
    }
    
    private void b(final boolean rangeViewFoucsState) {
        final TuSdkVideoCameraExtendViewInterface focusTouchView = this.getFocusTouchView();
        if (focusTouchView != null) {
            focusTouchView.setRangeViewFoucsState(rangeViewFoucsState);
        }
    }
    
    private void h() {
        this.a(CameraState.StateCaptured);
        if (this.p != null && this.isDisableCaptureSound()) {
            this.p.start();
        }
    }
    
    public void onTakePictured(final byte[] imageData) {
        this.h();
        if (this.isOutputImageData() && this.y != null) {
            this.y.imageData = imageData;
            this.a(this.y);
        }
    }
    
    protected Bitmap decodeToBitmapAtAsync(final byte[] array, final Bitmap bitmap) {
        if (this.y != null) {
            this.a(array);
        }
        return bitmap;
    }
    
    private void a(final byte[] array) {
        if (this.y == null || array == null) {
            return;
        }
        this.y.metadata = ExifHelper.getExifInterface(array);
    }
    
    private void a(final Bitmap image) {
        if (this.y == null || image == null) {
            return;
        }
        this.y.image = image;
        this.y.outputSize = TuSdkSize.create(image);
        if (this.y.metadata == null) {
            return;
        }
        this.y.metadata.setTagValue(ExifInterface.TAG_IMAGE_WIDTH, this.y.outputSize.width);
        this.y.metadata.setTagValue(ExifInterface.TAG_IMAGE_LENGTH, this.y.outputSize.height);
        this.y.metadata.setTagValue(ExifInterface.TAG_ORIENTATION, ImageOrientation.Up.getExifOrientation());
        this.y.metadata.setTag(this.y.metadata.buildTag(ExifInterface.TAG_IMAGE_DESCRIPTION, String.format("TuSDK[filter:%s]", this.y.filterCode)));
    }
    
    private void c(final boolean b) {
        this.y = new TuSdkResult();
        this.y.imageOrientation = ((SelesBaseCameraInterface)this.c).capturePhotoOrientation();
        this.y.outputSize = ((SelesBaseCameraInterface)this.c).getOutputSize();
        this.y.imageSizeRatio = this.getRegionRatio();
        this.y.filterCode = this.j.getCode();
        this.y.filterParams = this.j.getFilterParameter();
        if (this.isOutputImageData()) {
            ((SelesStillCameraInterface)this.c).capturePhotoAsJPEGData(null);
            return;
        }
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                final FilterWrap clone = TuSdkStillCameraAdapter.this.j.clone();
                clone.processImage();
                ((SelesStillCameraInterface)TuSdkStillCameraAdapter.this.c).capturePhotoAsBitmap(clone.getFilter(), TuSdkStillCameraAdapter.this.y.imageOrientation, new SelesStillCameraInterface.CapturePhotoAsBitmapCallback() {
                    @Override
                    public void onCapturePhotoAsBitmap(final Bitmap bitmap) {
                        if (TuSdkStillCameraAdapter.this.y != null) {
                            TuSdkStillCameraAdapter.this.a(bitmap);
                            clone.destroy();
                            TuSdkStillCameraAdapter.this.a(TuSdkStillCameraAdapter.this.y);
                        }
                    }
                });
            }
        });
    }
    
    private void a(final TuSdkResult tuSdkResult) {
        this.y = null;
        if (this.f != null) {
            this.f.onStillCameraTakedPicture((TuSdkStillCameraInterface)this.c, tuSdkResult);
        }
    }
    
    public enum CameraState
    {
        StateUnknow, 
        StateStarting, 
        StateStarted, 
        StateCapturing, 
        StateCaptured;
    }
}
