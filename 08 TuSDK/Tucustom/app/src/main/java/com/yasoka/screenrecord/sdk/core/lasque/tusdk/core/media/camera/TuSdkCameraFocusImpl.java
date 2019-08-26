// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera;

//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.os.Build;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.TuSdkContext;
import android.graphics.PointF;
import android.annotation.TargetApi;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.hardware.CameraHelper;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.hardware.Camera;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;

public class TuSdkCameraFocusImpl implements TuSdkCameraFocus
{
    public static final long FOCUS_SAMPLING_DISTANCE_MS = 2000L;
    private TuSdkCamera.TuSdkCameraStatus a;
    private boolean b;
    private boolean c;
    private TuSdkCameraFocusFaceListener d;
    private Runnable e;
    private boolean f;
    private long g;
    private CameraConfigs.CameraAutoFocus h;
    private boolean i;
    private boolean j;
    private TuSdkCameraFocusListener k;
    private TuSdkCameraBuilder l;
    private TuSdkCameraOrientation m;
    private TuSdkCameraSize n;
    
    public TuSdkCameraFocusImpl() {
        this.e = null;
        this.f = false;
    }
    
    public void setFaceListener(final TuSdkCameraFocusFaceListener d) {
        this.d = d;
        if (d == null) {
            this.b();
        }
        else {
            this.a();
        }
    }
    
    @TargetApi(14)
    private void a() {
        if (!this.c || this.d == null || this.b || this.a != TuSdkCamera.TuSdkCameraStatus.CAMERA_START_PREVIEW) {
            return;
        }
        final Camera i = this.i();
        final TuSdkSize g = this.g();
        if (i == null || g == null) {
            return;
        }
        this.b();
        this.b = true;
        i.setFaceDetectionListener((Camera.FaceDetectionListener)new Camera.FaceDetectionListener() {
            public void onFaceDetection(final Camera.Face[] array, final Camera camera) {
                TuSdkCameraFocusImpl.this.d.onFocusFaceDetection(CameraHelper.transforFaces(array, TuSdkCameraFocusImpl.this.f()), g.transforOrientation(TuSdkCameraFocusImpl.this.f()));
            }
        });
        try {
            i.startFaceDetection();
        }
        catch (Exception ex) {
            this.b = false;
            TLog.e(ex, "%s startFaceDetection failed, ignore and try again.", "TuSdkCameraFocusImpl");
        }
    }
    
    @TargetApi(14)
    private void b() {
        final Camera i = this.i();
        if (i == null || !this.b) {
            return;
        }
        this.b = false;
        try {
            i.setFaceDetectionListener((Camera.FaceDetectionListener)null);
            i.stopFaceDetection();
        }
        catch (Exception ex) {
            TLog.e(ex, "%s stopFaceDetection failed, ignore and try again.", "TuSdkCameraFocusImpl");
        }
    }
    
    public void autoMetering(final PointF pointF) {
        final Camera.Parameters h = this.h();
        if (h == null) {
            return;
        }
        CameraHelper.setFocusPoint(h, this.a(pointF), this.f());
        this.a(h);
    }
    
    public boolean isDisableFocusBeep() {
        return this.i;
    }
    
    public void setDisableFocusBeep(final boolean i) {
        this.i = i;
    }
    
    public boolean isDisableContinueFoucs() {
        return this.j;
    }
    
    public void setDisableContinueFoucs(final boolean j) {
        this.j = j;
    }
    
    public void setFocusListener(final TuSdkCameraFocusListener k) {
        this.k = k;
    }
    
    public void setFocusMode(final CameraConfigs.CameraAutoFocus h, final PointF pointF) {
        if (h == null) {
            return;
        }
        this.h = h;
        final Camera.Parameters h2 = this.h();
        if (h2 == null) {
            return;
        }
        CameraHelper.setFocusMode(h2, this.h, this.a(pointF), this.f());
        this.a(h2);
    }
    
    public void setFocusPoint(final PointF pointF) {
        final Camera.Parameters h = this.h();
        if (h == null) {
            return;
        }
        CameraHelper.setFocusPoint(h, this.a(pointF), this.f());
        this.a(h);
    }
    
    public CameraConfigs.CameraAutoFocus getFocusMode() {
        final Camera.Parameters h = this.h();
        if (h == null) {
            return this.h;
        }
        return CameraHelper.focusModeType(h.getFocusMode());
    }
    
    public boolean canSupportAutoFocus() {
        boolean canSupportAutofocus;
        try {
            canSupportAutofocus = CameraHelper.canSupportAutofocus(TuSdkContext.context(), this.h());
        }
        catch (RuntimeException ex) {
            canSupportAutofocus = false;
            TLog.e(ex, "%s canSupportAutoFocus catch error, ignore.", "TuSdkCameraFocusImpl");
        }
        return canSupportAutofocus;
    }
    
    private void c() {
        if (this.i() == null || !CameraHelper.canSupportAutofocus(TuSdkContext.context())) {
            return;
        }
        this.i().cancelAutoFocus();
    }
    
    private PointF a(PointF pointF) {
        if (pointF == null) {
            pointF = new PointF(0.5f, 0.5f);
        }
        return pointF;
    }
    
    @TargetApi(16)
    public void setAutoFocusMoveCallback(final Camera.AutoFocusMoveCallback autoFocusMoveCallback) {
        if (this.i() == null || !CameraHelper.canSupportAutofocus(TuSdkContext.context())) {
            return;
        }
        this.i().setAutoFocusMoveCallback(autoFocusMoveCallback);
    }
    
    private void d() {
        if (this.e == null) {
            return;
        }
        ThreadHelper.cancel(this.e);
        this.e = null;
    }
    
    private void a(final TuSdkCameraFocusListener obj) {
        this.d();
        if (this.k != null && !this.k.equals(obj)) {
            this.k.onAutoFocus(this.f, this);
        }
        obj.onAutoFocus(this.f, this);
        this.c();
    }
    
    public void autoFocus(final CameraConfigs.CameraAutoFocus cameraAutoFocus, final PointF pointF, final TuSdkCameraFocusListener tuSdkCameraFocusListener) {
        if (this.a == TuSdkCamera.TuSdkCameraStatus.CAMERA_PREPARE_SHOT) {
            return;
        }
        this.setFocusMode(cameraAutoFocus, pointF);
        this.autoFocus(tuSdkCameraFocusListener);
    }
    
    @Override
    public void autoFocus(final TuSdkCameraFocusListener obj) {
        if (this.k != null && !this.k.equals(obj)) {
            this.k.onFocusStart(this);
        }
        else if (obj != null) {
            obj.onFocusStart(this);
        }
        final Camera i = this.i();
        if (i == null || !this.canSupportAutoFocus()) {
            if (obj != null) {
                obj.onAutoFocus(false, this);
            }
            return;
        }
        this.g = System.currentTimeMillis();
        Object o = null;
        this.f = false;
        if (obj != null) {
            o = new Camera.AutoFocusCallback() {
                public void onAutoFocus(final boolean b, final Camera camera) {
                    TuSdkCameraFocusImpl.this.f = b;
                    TuSdkCameraFocusImpl.this.a(obj);
                }
            };
        }
        try {
            i.autoFocus((Camera.AutoFocusCallback)o);
            ThreadHelper.postDelayed(this.e = new Runnable() {
                @Override
                public void run() {
                    TuSdkCameraFocusImpl.this.a(obj);
                }
            }, 1500L);
        }
        catch (Exception ex) {
            TLog.e(ex, "%s autoFocus failed, ignore and try again.", "TuSdkCameraFocusImpl");
        }
    }
    
    private boolean e() {
        return System.currentTimeMillis() - this.g > 2000L;
    }
    
    @Override
    public boolean allowFocusToShot() {
        return this.e() && this.canSupportAutoFocus();
    }
    
    @Override
    public void configure(final TuSdkCameraBuilder l, final TuSdkCameraOrientation m, final TuSdkCameraSize n) {
        if (l == null) {
            TLog.e("%s configure builder[%s] or orientation[%s] or size[%s] is empty.", "TuSdkCameraFocusImpl", l, m, n);
            return;
        }
        this.l = l;
        this.m = m;
        this.n = n;
        final Camera.Parameters h = this.h();
        if (h == null) {
            TLog.e("%s configure Camera.Parameters is empty.", "TuSdkCameraFocusImpl");
            return;
        }
        CameraHelper.setFocusMode(h, CameraHelper.focusModes);
        this.h = CameraHelper.focusModeType(h.getFocusMode());
        this.c = CameraHelper.canSupportFaceDetection(h);
        if (Build.VERSION.SDK_INT >= 14) {
            CameraHelper.setFocusArea(h, this.a((PointF)null), null, l.isBackFacingCameraPresent());
        }
        this.a(h);
    }
    
    @Override
    public void changeStatus(final TuSdkCamera.TuSdkCameraStatus a) {
        this.a = a;
        if (a == TuSdkCamera.TuSdkCameraStatus.CAMERA_START_PREVIEW) {
            this.a();
        }
        else {
            this.d();
            this.b();
            this.e = null;
            this.f = false;
        }
    }
    
    private ImageOrientation f() {
        if (this.m == null) {
            return ImageOrientation.Up;
        }
        return this.m.previewOrientation();
    }
    
    private TuSdkSize g() {
        if (this.n == null) {
            return null;
        }
        return this.n.previewOptimalSize();
    }
    
    private Camera.Parameters h() {
        if (this.l == null) {
            return null;
        }
        return this.l.getParameters();
    }
    
    private void a(final Camera.Parameters parameters) {
        if (this.l == null || this.l.getOrginCamera() == null) {
            return;
        }
        this.l.getOrginCamera().setParameters(parameters);
    }
    
    private Camera i() {
        if (this.l == null) {
            return null;
        }
        return this.l.getOrginCamera();
    }
}
