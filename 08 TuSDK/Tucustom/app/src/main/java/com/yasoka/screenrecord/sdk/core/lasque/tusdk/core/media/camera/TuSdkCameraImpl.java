// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera;

//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.TuSdkResult;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
//import org.lasque.tusdk.core.utils.TLog;
import android.opengl.GLES20;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
//import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;

public final class TuSdkCameraImpl implements TuSdkCamera
{
    private final SelesSurfaceReceiver a;
    private TuSdkCameraStatus b;
    private TuSdkCameraListener c;
    private Camera.PreviewCallback d;
    private SurfaceTexture.OnFrameAvailableListener e;
    private TuSdkCameraBuilder f;
    private TuSdkCameraParameters g;
    private TuSdkCameraOrientation h;
    private TuSdkCameraFocus i;
    private TuSdkCameraSize j;
    private TuSdkCameraShot k;
    private boolean l;
    private boolean m;
    private float n;
    private Camera.PreviewCallback o;
    private SurfaceTexture.OnFrameAvailableListener p;
    private GLSurfaceView.Renderer q;
    
    public TuSdkCameraImpl() {
        this.a = new SelesSurfaceReceiver();
        this.l = false;
        this.m = false;
        this.o = (Camera.PreviewCallback)new Camera.PreviewCallback() {
            public void onPreviewFrame(final byte[] array, final Camera camera) {
                if (TuSdkCameraStatus.CAMERA_PAUSE_PREVIEW == TuSdkCameraImpl.this.b) {
                    return;
                }
                if (TuSdkCameraImpl.this.d != null) {
                    TuSdkCameraImpl.this.d.onPreviewFrame(array, camera);
                    return;
                }
                camera.addCallbackBuffer(array);
            }
        };
        this.p = (SurfaceTexture.OnFrameAvailableListener)new SurfaceTexture.OnFrameAvailableListener() {
            public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
                if (TuSdkCameraStatus.CAMERA_PAUSE_PREVIEW == TuSdkCameraImpl.this.b) {
                    return;
                }
                if (TuSdkCameraStatus.CAMERA_START == TuSdkCameraImpl.this.b) {
                    TuSdkCameraImpl.this.a(TuSdkCameraStatus.CAMERA_START_PREVIEW);
                }
                if (TuSdkCameraImpl.this.a(surfaceTexture)) {
                    return;
                }
                if (surfaceTexture != null) {
                    surfaceTexture.updateTexImage();
                }
            }
        };
        this.q = (GLSurfaceView.Renderer)new GLSurfaceView.Renderer() {
            public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
                GLES20.glDisable(2929);
                TuSdkCameraImpl.this.initInGLThread();
            }
            
            public void onSurfaceChanged(final GL10 gl10, final int n, final int n2) {
                GLES20.glViewport(0, 0, n, n2);
            }
            
            public void onDrawFrame(final GL10 gl10) {
                GLES20.glClear(16640);
                TuSdkCameraImpl.this.newFrameReadyInGLThread();
            }
        };
    }
    
    @Override
    public TuSdkCameraStatus getCameraStatus() {
        return this.b;
    }
    
    @Override
    public void setCameraListener(final TuSdkCameraListener c) {
        this.c = c;
    }
    
    @Override
    public void setPreviewCallback(final Camera.PreviewCallback d) {
        if (this.a("setPreviewCallback")) {
            return;
        }
        this.d = d;
    }
    
    @Override
    public void setSurfaceListener(final SurfaceTexture.OnFrameAvailableListener e) {
        if (this.a("setSurfaceListener")) {
            return;
        }
        this.e = e;
    }
    
    @Override
    public void setCameraBuilder(final TuSdkCameraBuilder f) {
        if (this.a("setCameraBuilder")) {
            return;
        }
        this.f = f;
    }
    
    @Override
    public void setCameraParameters(final TuSdkCameraParameters g) {
        if (this.a("setCameraParameters")) {
            return;
        }
        this.g = g;
    }
    
    @Override
    public void setCameraOrientation(final TuSdkCameraOrientation h) {
        if (this.a("setCameraOrientation")) {
            return;
        }
        this.h = h;
    }
    
    @Override
    public void setCameraFocus(final TuSdkCameraFocus i) {
        if (this.a("setCameraFocus")) {
            return;
        }
        this.i = i;
    }
    
    @Override
    public void setCameraSize(final TuSdkCameraSize j) {
        if (this.a("setCameraSize")) {
            return;
        }
        this.j = j;
    }
    
    @Override
    public void setCameraShot(final TuSdkCameraShot k) {
        if (this.a("setCameraShot")) {
            return;
        }
        this.k = k;
    }
    
    private boolean a(final String s) {
        if (this.l) {
            TLog.w("%s %s has released.", "TuSdkCameraImpl", s);
            return true;
        }
        if (this.m) {
            TLog.w("%s %s need before prepare.", "TuSdkCameraImpl", s);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean prepare() {
        if (this.m) {
            TLog.w("%s prepare is allready.", "TuSdkCameraImpl");
            return false;
        }
        if (this.a == null) {
            TLog.w("%s prepare need setSurfaceHolder first.", "TuSdkCameraImpl");
            return false;
        }
        this.m = true;
        if (this.f == null) {
            this.f = new TuSdkCameraBuilderImpl();
        }
        if (this.g == null) {
            this.g = new TuSdkCameraParametersImpl();
        }
        if (this.h == null) {
            this.h = new TuSdkCameraOrientationImpl();
        }
        if (this.i == null) {
            this.i = new TuSdkCameraFocusImpl();
        }
        if (this.j == null) {
            this.j = new TuSdkCameraSizeImpl();
        }
        if (this.k == null) {
            this.k = new TuSdkCameraShotImpl();
        }
        return true;
    }
    
    @Override
    public boolean rotateCamera() {
        if (this.l) {
            TLog.w("%s rotateCamera has released.", "TuSdkCameraImpl");
            return false;
        }
        CameraConfigs.CameraFacing cameraFacing = CameraConfigs.CameraFacing.Back;
        if (cameraFacing == this.f.getFacing()) {
            cameraFacing = CameraConfigs.CameraFacing.Front;
        }
        return this.startPreview(cameraFacing);
    }
    
    @Override
    public CameraConfigs.CameraFacing getFacing() {
        return this.f.getFacing();
    }
    
    @Override
    public boolean startPreview() {
        return this.startPreview(this.f.getFacing());
    }
    
    @Override
    public boolean startPreview(final CameraConfigs.CameraFacing cameraFacing) {
        if (this.l) {
            TLog.w("%s startPreview has released.", "TuSdkCameraImpl");
            return false;
        }
        if (cameraFacing == null) {
            TLog.e("%s startPreview need a CameraFacing", "TuSdkCameraImpl");
            return false;
        }
        if (!this.m) {
            TLog.w("%s startPreview need prepare first.", "TuSdkCameraImpl");
            return false;
        }
        this.stopPreview();
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                final long currentTimeMillis = System.currentTimeMillis();
                SurfaceTexture requestSurfaceTexture;
                while (!TuSdkCameraImpl.this.a.isInited() || (requestSurfaceTexture = TuSdkCameraImpl.this.a.requestSurfaceTexture()) == null) {
                    if (System.currentTimeMillis() - currentTimeMillis > 2000L) {
                        TLog.e("%s startPreview failed, request surfaceTexture timeout: %s", "TuSdkCameraImpl", TuSdkCameraImpl.this.a);
                        TuSdkCameraImpl.this.stopPreview();
                        return;
                    }
                }
                if (!TuSdkCameraImpl.this.f.open(cameraFacing)) {
                    TLog.e("%s startPreview failed, can not open Camera: %s", "TuSdkCameraImpl", TuSdkCameraImpl.this.f);
                    TuSdkCameraImpl.this.stopPreview();
                    return;
                }
                TuSdkCameraImpl.this.g.configure(TuSdkCameraImpl.this.f);
                TuSdkCameraImpl.this.h.configure(TuSdkCameraImpl.this.f);
                TuSdkCameraImpl.this.j.configure(TuSdkCameraImpl.this.f);
                TuSdkCameraImpl.this.i.configure(TuSdkCameraImpl.this.f, TuSdkCameraImpl.this.h, TuSdkCameraImpl.this.j);
                TuSdkCameraImpl.this.k.configure(TuSdkCameraImpl.this.f);
                if (TuSdkCameraImpl.this.d != null) {
                    TuSdkCameraImpl.this.a();
                    TuSdkCameraImpl.this.f.setPreviewCallbackWithBuffer(TuSdkCameraImpl.this.o);
                }
                if (TuSdkCameraImpl.this.e != null) {
                    requestSurfaceTexture.setOnFrameAvailableListener(TuSdkCameraImpl.this.p);
                }
                TuSdkCameraImpl.this.f.setPreviewTexture(requestSurfaceTexture);
                if (!TuSdkCameraImpl.this.f.startPreview()) {
                    TLog.e("%s startPreview error, can not open Camera: %s", "TuSdkCameraImpl", TuSdkCameraImpl.this.f);
                    TuSdkCameraImpl.this.stopPreview();
                    return;
                }
                TuSdkCameraImpl.this.a(TuSdkCameraStatus.CAMERA_START);
            }
        });
        return true;
    }
    
    @Override
    public void stopPreview() {
        if (!this.m) {
            return;
        }
        this.f.releaseCamera();
        this.a(TuSdkCameraStatus.CAMERA_STOP);
    }
    
    @Override
    public boolean pausePreview() {
        if (!this.m || this.l || (this.b != TuSdkCameraStatus.CAMERA_START_PREVIEW && this.b != TuSdkCameraStatus.CAMERA_SHOTED)) {
            TLog.w("%s pausePreview had incorrect status: %s, release: %b", "TuSdkCameraImpl", this.b, this.l);
            return false;
        }
        this.a(TuSdkCameraStatus.CAMERA_PAUSE_PREVIEW);
        return true;
    }
    
    @Override
    public boolean resumePreview() {
        if (!this.m || this.l || this.b != TuSdkCameraStatus.CAMERA_PAUSE_PREVIEW) {
            TLog.w("%s resumePreview had incorrect status: %s, release: %b", "TuSdkCameraImpl", this.b, this.l);
            return false;
        }
        this.a(false);
        return true;
    }
    
    private boolean a(final boolean b) {
        this.a(TuSdkCameraStatus.CAMERA_START_PREVIEW);
        this.f.startPreview();
        this.a();
        this.a((SurfaceTexture)null);
        return true;
    }
    
    @Override
    public boolean shotPhoto() {
        if (!this.m || this.l || this.b != TuSdkCameraStatus.CAMERA_START_PREVIEW) {
            TLog.w("%s captureImage had incorrect status: %s, release: %b", "TuSdkCameraImpl", this.b, this.l);
            return false;
        }
        final boolean allowFocusToShot = this.i.allowFocusToShot();
        this.a(TuSdkCameraStatus.CAMERA_PREPARE_SHOT);
        if (allowFocusToShot) {
            this.i.autoFocus(new TuSdkCameraFocus.TuSdkCameraFocusListener() {
                @Override
                public void onFocusStart(final TuSdkCameraFocus tuSdkCameraFocus) {
                }
                
                @Override
                public void onAutoFocus(final boolean b, final TuSdkCameraFocus tuSdkCameraFocus) {
                    TuSdkCameraImpl.this.b(b);
                }
            });
        }
        else {
            this.b(false);
        }
        return true;
    }
    
    @Override
    public void release() {
        if (this.l) {
            return;
        }
        this.l = true;
        this.stopPreview();
        this.a.destroy();
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
    
    private void a(final TuSdkCameraStatus b) {
        this.b = b;
        this.g.changeStatus(b);
        this.h.changeStatus(b);
        this.i.changeStatus(b);
        this.j.changeStatus(b);
        this.k.changeStatus(b);
        if (this.c != null) {
            this.c.onStatusChanged(b, this);
        }
        if (b == TuSdkCameraStatus.CAMERA_START_PREVIEW) {
            this.a.setInputRotation(this.h.previewOrientation());
            this.a.setInputSize(this.j.previewOptimalSize());
        }
    }
    
    private void a() {
        if (this.d == null || this.f == null) {
            return;
        }
        this.f.addCallbackBuffer(new byte[this.j.previewBufferLength()]);
        this.f.addCallbackBuffer(new byte[this.j.previewBufferLength()]);
    }
    
    private void b(final boolean b) {
        final TuSdkResult tuSdkResult = new TuSdkResult();
        tuSdkResult.imageOrientation = this.h.captureOrientation();
        tuSdkResult.outputSize = this.j.getOutputSize();
        tuSdkResult.imageSizeRatio = this.n;
        this.k.takeJpegPicture(tuSdkResult, new TuSdkCameraShot.TuSdkCameraShotResultListener() {
            @Override
            public void onShotResule(final byte[] imageData) {
                tuSdkResult.imageData = imageData;
                TuSdkCameraImpl.this.a(tuSdkResult);
            }
        });
    }
    
    private void a(final TuSdkResult tuSdkResult) {
        this.k.processData(tuSdkResult);
        if (tuSdkResult.imageData == null) {
            this.startPreview();
            return;
        }
        this.a(TuSdkCameraStatus.CAMERA_SHOTED);
        if (this.k.isAutoReleaseAfterCaptured()) {
            this.stopPreview();
        }
        else {
            this.pausePreview();
        }
    }
    
    private boolean a(final SurfaceTexture surfaceTexture) {
        if (this.e != null) {
            this.e.onFrameAvailable(surfaceTexture);
            return true;
        }
        return false;
    }
    
    @Override
    public void addTarget(final SelesContext.SelesInput selesInput, final int n) {
        this.a.addTarget(selesInput, n);
    }
    
    @Override
    public void removeTarget(final SelesContext.SelesInput selesInput) {
        this.a.removeTarget(selesInput);
    }
    
    @Override
    public GLSurfaceView.Renderer getExtenalRenderer() {
        return this.q;
    }
    
    @Override
    public void initInGLThread() {
        this.a.initInGLThread();
    }
    
    @Override
    public void updateSurfaceTexImage() {
        if (this.l) {
            return;
        }
        if (TuSdkCameraStatus.CAMERA_START_PREVIEW == this.b) {
            this.a.forceUpdateSurfaceTexImage();
            return;
        }
        this.a.updateSurfaceTexImage();
    }
    
    @Override
    public void newFrameReadyInGLThread(final long n) {
        if (this.l) {
            return;
        }
        this.a.newFrameReadyInGLThread(n);
    }
    
    @Override
    public long newFrameReadyInGLThread() {
        if (this.l) {
            return -1L;
        }
        final long nanoTime = System.nanoTime();
        this.updateSurfaceTexImage();
        this.newFrameReadyInGLThread(nanoTime);
        return nanoTime;
    }
    
    public void setShotRegionRatio(final float n) {
        this.n = n;
    }
}
