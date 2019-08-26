// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.utils.RectHelper;
import java.nio.ByteBuffer;

import android.graphics.Rect;
import android.media.Image;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.utils.hardware.TuSdkFace;
import android.hardware.camera2.params.Face;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.hardware.camera2.CaptureResult;
import java.util.ArrayList;
import android.view.Surface;
import java.util.List;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.TotalCaptureResult;
import android.content.Context;
import java.util.Calendar;
import android.graphics.PointF;
import android.util.Size;
//import org.lasque.tusdk.core.utils.hardware.Camera2Helper;
import android.hardware.camera2.CameraAccessException;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.media.ImageReader;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.Camera2Helper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkFace;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

@TargetApi(21)
public abstract class SelesStillCamera2 extends SelesVideoCamera2 implements SelesStillCameraInterface
{
    private CameraDevice b;
    private CameraCaptureSession c;
    private CaptureRequest.Builder d;
    private CaptureRequest.Builder e;
    private ImageReader f;
    private boolean g;
    private TuSdkSize h;
    private boolean i;
    private boolean j;
    private boolean k;
    private int l;
    private float m;
    private CameraConfigs.CameraFlash n;
    private CameraConfigs.CameraAutoFocus o;
    private long p;
    private CameraConfigs.CameraAntibanding q;
    private boolean r;
    private SelesBaseCameraInterface.TuSdkAutoFocusCallback s;
    private CameraDevice.StateCallback t;
    private CameraCaptureSession.StateCallback u;
    private CameraCaptureSession.CaptureCallback v;
    private Integer w;
    private Integer x;
    private CameraCaptureSession.StateCallback y;
    private CameraCaptureSession.CaptureCallback z;
    
    public CameraDevice getCameraDevice() {
        return this.b;
    }
    
    public CaptureRequest.Builder getPreviewBuilder() {
        return this.d;
    }
    
    public boolean isCapturePhoto() {
        return this.g;
    }
    
    private void a(final boolean g) {
        this.onCapturePhotoStateChanged(this.g = g);
    }
    
    private int a() {
        final TuSdkSize screenSize = ContextUtils.getScreenSize(this.getContext());
        if (this.l < 1 || this.l > screenSize.maxSide()) {
            this.l = screenSize.maxSide();
        }
        return this.l;
    }
    
    @Override
    public void setPreviewMaxSize(final int l) {
        this.l = l;
    }
    
    @Override
    public final TuSdkSize getOutputSize() {
        if (this.h == null) {
            this.h = ContextUtils.getScreenSize(this.getContext());
        }
        return this.h;
    }
    
    @Override
    public void setOutputSize(final TuSdkSize tuSdkSize) {
        if (tuSdkSize == null || !tuSdkSize.isSize()) {
            return;
        }
        this.h = tuSdkSize.limitSize();
        this.c();
    }
    
    private ImageReader b() {
        if (this.f == null) {
            this.f = this.buildJpegImageReader();
        }
        return this.f;
    }
    
    private void c() {
        if (this.c != null) {
            try {
                this.c.stopRepeating();
            }
            catch (CameraAccessException ex) {
                TLog.e((Throwable)ex, "changeOuputSize", new Object[0]);
            }
        }
        if (this.f != null) {
            this.f.close();
        }
        this.f = this.buildJpegImageReader();
        this.continuePreview();
    }
    
    protected ImageReader buildJpegImageReader() {
        if (this.getCameraCharacter() == null) {
            return this.f;
        }
        final Size pictureOptimalSize = Camera2Helper.pictureOptimalSize(this.getContext(),
                Camera2Helper.streamConfigurationMap(this.getCameraCharacter()).getOutputSizes(256), this.getOutputSize());
        if (pictureOptimalSize == null) {
            return null;
        }
        return ImageReader.newInstance(pictureOptimalSize.getWidth(), pictureOptimalSize.getHeight(), 256, 1);
    }
    
    public float getRegionRatio() {
        return this.getOutputSize().getRatioFloat();
    }
    
    public boolean isUnifiedParameters() {
        return this.i;
    }
    
    @Override
    public void setUnifiedParameters(final boolean i) {
        this.i = i;
    }
    
    public boolean isAutoReleaseAfterCaptured() {
        return this.j;
    }
    
    @Override
    public void setAutoReleaseAfterCaptured(final boolean j) {
        this.j = j;
    }
    
    public boolean isDisableMirrorFrontFacing() {
        return this.k;
    }
    
    @Override
    public void setDisableMirrorFrontFacing(final boolean k) {
        this.k = k;
    }
    
    public float getPreviewEffectScale() {
        return this.m;
    }
    
    @Override
    public void setPreviewEffectScale(final float m) {
        if (m <= 0.0f) {
            return;
        }
        if (m > 1.0f) {
            this.m = 1.0f;
        }
        this.m = m;
    }
    
    @Override
    public long getLastFocusTime() {
        return this.p;
    }
    
    public boolean isEnableFaceTrace() {
        return this._isEnableFaceTrace();
    }
    
    @Override
    public void setEnableFaceTrace(final boolean r) {
        this.r = r;
        if (r) {
            this.g();
        }
        else {
            this.h();
        }
    }
    
    public boolean _isEnableFaceTrace() {
        return this.r;
    }
    
    @Override
    public CameraConfigs.CameraFlash getFlashMode() {
        if (this.getCameraCharacter() == null) {
            return CameraConfigs.CameraFlash.Off;
        }
        if (this.d != null) {
            return Camera2Helper.getFlashMode(this.d);
        }
        if (this.n == null) {
            this.n = CameraConfigs.CameraFlash.Off;
        }
        return this.n;
    }
    
    @Override
    public void setFlashMode(final CameraConfigs.CameraFlash n) {
        if (n == null) {
            return;
        }
        this.n = n;
        if (!this.canSupportFlash() || this.d == null) {
            return;
        }
        Camera2Helper.setFlashMode(this.d, n);
        this.updatePreview();
    }
    
    @Override
    public boolean canSupportFlash() {
        return this.getCameraCharacter() != null && Camera2Helper.canSupportFlash(this.getContext()) && Camera2Helper.supportFlash(this.getCameraCharacter());
    }
    
    @Override
    public void autoMetering(final PointF pointF) {
    }
    
    public void setFocusMode(final CameraConfigs.CameraAutoFocus o, final PointF pointF) {
        if (o == null) {
            return;
        }
        this.o = o;
        if (this.getCameraCharacter() == null || this.d == null) {
            return;
        }
        Camera2Helper.setFocusMode(this.getCameraCharacter(), this.d, this.o, this.getCenterIfNull(pointF), this.mOutputRotation);
        this.updatePreview();
    }
    
    public void setFocusPoint(final PointF pointF) {
        if (this.getCameraCharacter() == null || this.d == null) {
            return;
        }
        Camera2Helper.setFocusPoint(this.getCameraCharacter(), this.d, this.getCenterIfNull(pointF), this.mOutputRotation);
        this.updatePreview();
    }
    
    public CameraConfigs.CameraAutoFocus getFocusMode() {
        if (this.d == null) {
            return this.o;
        }
        return Camera2Helper.focusModeType(this.d);
    }
    
    @Override
    public boolean canSupportAutoFocus() {
        return this.getCameraCharacter() != null && Camera2Helper.canSupportAutofocus(this.getContext()) && Camera2Helper.canSupportAutofocus(this.getCameraCharacter());
    }
    
    @Override
    public void cancelAutoFocus() {
        if (this.d == null || !this.canSupportAutoFocus()) {
            return;
        }
        this.d.set(CaptureRequest.CONTROL_AF_TRIGGER, 2);
        this.updatePreview();
    }
    
    @Override
    public void autoFocus(final CameraConfigs.CameraAutoFocus cameraAutoFocus, final PointF pointF, final SelesBaseCameraInterface.TuSdkAutoFocusCallback tuSdkAutoFocusCallback) {
        this.setFocusMode(cameraAutoFocus, pointF);
        this.autoFocus(tuSdkAutoFocusCallback);
    }
    
    @Override
    public void autoFocus(final SelesBaseCameraInterface.TuSdkAutoFocusCallback s) {
        if (!this.canSupportAutoFocus()) {
            return;
        }
        this.s = s;
        this.p = Calendar.getInstance().getTimeInMillis();
    }
    
    protected PointF getCenterIfNull(PointF pointF) {
        if (pointF == null) {
            pointF = new PointF(0.5f, 0.5f);
        }
        return pointF;
    }
    
    protected abstract void playSystemShutter();
    
    @Override
    public void setAntibandingMode(final CameraConfigs.CameraAntibanding cameraAntibanding) {
    }
    
    @Override
    public CameraConfigs.CameraAntibanding getAntiBandingMode() {
        return this.q;
    }
    
    public SelesStillCamera2(final Context context, final CameraConfigs.CameraFacing cameraFacing) {
        super(context, cameraFacing);
        this.i = false;
        this.m = 0.75f;
        this.t = new CameraDevice.StateCallback() {
            public void onOpened(final CameraDevice cameraDevice) {
                TLog.d("mCameraStateCallback : %s [Thread: %s]", "onOpened", Thread.currentThread().getName());
                SelesStillCamera2.this.b = cameraDevice;
                SelesStillCamera2.this.e();
            }
            
            public void onDisconnected(final CameraDevice cameraDevice) {
                TLog.d("mCameraStateCallback : %s", "onDisconnected");
            }
            
            public void onError(final CameraDevice cameraDevice, final int i) {
                TLog.d("mCameraStateCallback : %s [%s]", "onError", i);
            }
            
            public void onClosed(final CameraDevice cameraDevice) {
                super.onClosed(cameraDevice);
                TLog.d("mCameraStateCallback : %s", "onClosed");
            }
        };
        this.u = new CameraCaptureSession.StateCallback() {
            public void onConfigured(final CameraCaptureSession cameraCaptureSession) {
                try {
                    SelesStillCamera2.this.c = cameraCaptureSession;
                    cameraCaptureSession.setRepeatingRequest(SelesStillCamera2.this.d.build(), SelesStillCamera2.this.getPreviewSessionCallback(), SelesStillCamera2.this.mHandler);
                }
                catch (CameraAccessException ex) {
                    TLog.e((Throwable)ex, "mSessionPreviewStateCallback onConfigured error", new Object[0]);
                }
            }
            
            public void onConfigureFailed(final CameraCaptureSession cameraCaptureSession) {
                TLog.d("mSessionPreviewStateCallback : %s", "onConfigureFailed");
            }
        };
        this.v = new CameraCaptureSession.CaptureCallback() {
            public void onCaptureCompleted(final CameraCaptureSession cameraCaptureSession, final CaptureRequest captureRequest, final TotalCaptureResult totalCaptureResult) {
                super.onCaptureCompleted(cameraCaptureSession, captureRequest, totalCaptureResult);
                SelesStillCamera2.this.a(totalCaptureResult);
                SelesStillCamera2.this.b(totalCaptureResult);
            }
        };
        this.y = new CameraCaptureSession.StateCallback() {
            public void onConfigured(final CameraCaptureSession cameraCaptureSession) {
                if (SelesStillCamera2.this.e == null) {
                    return;
                }
                try {
                    cameraCaptureSession.capture(SelesStillCamera2.this.e.build(), (CameraCaptureSession.CaptureCallback)null, SelesStillCamera2.this.mHandler);
                }
                catch (Exception ex) {
                    TLog.e(ex, "mSessionCaptureStateCallback onConfigured", new Object[0]);
                }
            }
            
            public void onConfigureFailed(final CameraCaptureSession cameraCaptureSession) {
                TLog.d("mSessionCaptureStateCallback onConfigureFailed", new Object[0]);
            }
        };
        this.z = new CameraCaptureSession.CaptureCallback() {
            public void onCaptureCompleted(final CameraCaptureSession cameraCaptureSession, final CaptureRequest captureRequest, final TotalCaptureResult totalCaptureResult) {
                super.onCaptureCompleted(cameraCaptureSession, captureRequest, totalCaptureResult);
                SelesStillCamera2.this.updatePreview();
            }
        };
        if (ContextUtils.getScreenSize(context).maxSide() < 1000) {
            this.m = 0.85f;
        }
        else {
            this.m = 0.75f;
        }
    }
    
    @Override
    protected TuSdkSize computerPreviewOptimalSize() {
        return Camera2Helper.createSize(Camera2Helper.previewOptimalSize(this.getContext(), Camera2Helper.streamConfigurationMap(this.getCameraCharacter()).getOutputSizes((Class)SurfaceTexture.class), this.a(), this.getPreviewEffectScale()));
    }
    
    @Override
    protected void onCameraStarted() {
        super.onCameraStarted();
        this.g();
    }
    
    @Override
    public void stopCameraCapture() {
        super.stopCameraCapture();
        this.g = false;
        this.o = null;
        this.d = null;
        this.e = null;
        this.s = null;
        if (this.c != null) {
            this.c.close();
            this.c = null;
        }
        if (this.b != null) {
            this.b.close();
            this.b = null;
        }
        if (this.f != null) {
            this.f.close();
            this.f = null;
        }
    }
    
    @Override
    public void resumeCameraCapture() {
        this.updatePreview();
        super.resumeCameraCapture();
        this.g();
    }
    
    @Override
    public void pauseCameraCapture() {
        super.pauseCameraCapture();
        if (this.c != null) {
            try {
                this.c.stopRepeating();
            }
            catch (CameraAccessException ex) {
                TLog.e((Throwable)ex, "pauseCameraCapture", new Object[0]);
            }
        }
    }
    
    protected void onCapturePhotoStateChanged(final boolean b) {
    }
    
    @Override
    protected CameraDevice.StateCallback getCameraStateCallback() {
        return this.t;
    }
    
    private List<Surface> d() {
        final ArrayList<Surface> list = new ArrayList<Surface>();
        if (this.getPreviewSurface() != null) {
            list.add(this.getPreviewSurface());
        }
        if (this.b() != null) {
            list.add(this.b().getSurface());
        }
        return list;
    }
    
    private void e() {
        try {
            (this.d = this.b.createCaptureRequest(1)).addTarget(this.getPreviewSurface());
            this.d.set(CaptureRequest.CONTROL_MODE, 1);
            this.d.set(CaptureRequest.CONTROL_AF_MODE,1);
            this.d.set(CaptureRequest.CONTROL_AE_MODE, 1);
            this.d.set(CaptureRequest.CONTROL_AWB_MODE, 1);
            if (this._isEnableFaceTrace()) {
                this.d.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, 1);
            }
            this.onCounfigPreview(this.d);
            this.b.createCaptureSession((List)this.d(), this.getSessionPreviewStateCallback(), this.mHandler);
        }
        catch (CameraAccessException ex) {
            TLog.e((Throwable)ex, "startPreview Error", new Object[0]);
        }
    }
    
    protected void onCounfigPreview(final CaptureRequest.Builder builder) {
    }
    
    protected CameraCaptureSession.StateCallback getSessionPreviewStateCallback() {
        return this.u;
    }
    
    protected CameraCaptureSession.CaptureCallback getPreviewSessionCallback() {
        return this.v;
    }
    
    private void f() {
        this.w = null;
        this.x = null;
    }
    
    private void a(final TotalCaptureResult totalCaptureResult) {
        if (this.s == null || totalCaptureResult == null) {
            return;
        }
        final Integer n = (Integer)totalCaptureResult.get(CaptureResult.CONTROL_AF_MODE);
        if (n == null || n == 0) {
            return;
        }
        final Integer w = (Integer)totalCaptureResult.get(CaptureResult.CONTROL_AF_STATE);
        final Integer x = (Integer)totalCaptureResult.get(CaptureResult.CONTROL_AE_STATE);
        if (w == null || w == 0) {
            this.d.set(CaptureRequest.CONTROL_AF_TRIGGER, 1);
            this.updatePreview();
            return;
        }
        if (w == null || x == null || w == 0 || x == 0 || (this.w == w && this.x == x)) {
            return;
        }
        this.w = w;
        this.x = x;
        boolean b = false;
        switch (x) {
            case 2:
            case 3:
            case 4:
            case 5: {
                b = true;
                break;
            }
        }
        if (!b) {
            return;
        }
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                SelesStillCamera2.this.observableAutofocus(w);
            }
        });
    }
    
    protected void observableAutofocus(final int n) {
        if (n == 0 || this.s == null) {
            return;
        }
        switch (n) {
            case 2:
            case 4: {
                this.b(true);
                break;
            }
            case 5:
            case 6: {
                this.b(false);
                break;
            }
        }
    }
    
    private void b(final boolean b) {
        if (this.s == null) {
            return;
        }
        this.s.onAutoFocus(b, this);
        this.s = null;
        this.f();
        this.d.set(CaptureRequest.CONTROL_AF_TRIGGER, 0);
        this.d.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, 0);
        this.updatePreview();
    }
    
    protected void updatePreview() {
        if (this.c == null) {
            return;
        }
        try {
            this.c.setRepeatingRequest(this.d.build(), this.getPreviewSessionCallback(), this.mHandler);
        }
        catch (Exception ex) {
            TLog.e(ex, "updatePreview error", new Object[0]);
        }
    }
    
    protected void continuePreview() {
        if (this.b == null) {
            return;
        }
        try {
            this.b.createCaptureSession((List)this.d(), this.getSessionPreviewStateCallback(), this.mHandler);
        }
        catch (Exception ex) {
            TLog.e(ex, "continuePreview error", new Object[0]);
        }
    }
    
    @Override
    public ImageOrientation capturePhotoOrientation() {
        if (!this.isDisableMirrorFrontFacing() || this.isBackFacingCameraPresent() || !this.isHorizontallyMirrorFrontFacingCamera()) {
            return this.mOutputRotation;
        }
        return SelesVideoCamera2.computerOutputOrientation(this.getContext(), this.getCameraCharacter(), this.isHorizontallyMirrorRearFacingCamera(), false, this.getOutputImageOrientation());
    }
    
    private void g() {
        if (this.c == null || this.d == null || !this.isCapturing() || !this._isEnableFaceTrace() || !Camera2Helper.canSupportFaceDetection(this.getCameraCharacter())) {
            return;
        }
        this.d.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, 1);
        this.updatePreview();
    }
    
    private void h() {
        if (this.c == null || this.d == null) {
            return;
        }
        this.d.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, 0);
        this.updatePreview();
    }

    private void b(TotalCaptureResult var1) {
        Integer var2 = (Integer)var1.get(CaptureResult.STATISTICS_FACE_DETECT_MODE);
        if (var2 != null && var2 != 0) {
            Face[] var3 = (Face[])var1.get(CaptureResult.STATISTICS_FACES);
            final List var4 = Camera2Helper.transforFaces(this.getCameraCharacter(), this.mInputTextureSize, var3, this.mOutputRotation);
            ThreadHelper.post(new Runnable() {
                public void run() {
                    SelesStillCamera2.this.onCameraFaceDetection(var4, SelesStillCamera2.this.mInputTextureSize.transforOrientation(SelesStillCamera2.this.mOutputRotation));
                }
            });
        }
    }


    public void onCameraFaceDetection(final List<TuSdkFace> list, final TuSdkSize tuSdkSize) {
    }
    
    protected void onTakePictured(final byte[] array) {
        if (this.isAutoReleaseAfterCaptured()) {
            this.stopCameraCapture();
        }
        else {
            this.pauseCameraCapture();
        }
    }
    
    protected void onTakePictureFailed() {
        this.startCameraCapture();
    }
    
    protected Bitmap decodeToBitmapAtAsync(final byte[] array) {
        return BitmapHelper.imageDecode(array, true);
    }
    
    @Override
    public void capturePhotoAsJPEGData(final CapturePhotoAsJPEGDataCallback capturePhotoAsJPEGDataCallback) {
        final boolean b = !this.isCapturing() || this.g;
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                if (b) {
                    if (capturePhotoAsJPEGDataCallback != null) {
                        capturePhotoAsJPEGDataCallback.onCapturePhotoAsJPEGData(null);
                    }
                    return;
                }
                SelesStillCamera2.this.a(true);
            }
        });
        if (b) {
            return;
        }
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                SelesStillCamera2.this.a(capturePhotoAsJPEGDataCallback);
            }
        });
    }
    
    @Override
    public void capturePhotoAsBitmap(final SelesOutInput selesOutInput, final CapturePhotoAsBitmapCallback capturePhotoAsBitmapCallback) {
        this.capturePhotoAsBitmap(selesOutInput, this.capturePhotoOrientation(), capturePhotoAsBitmapCallback);
    }
    
    @Override
    public void capturePhotoAsBitmap(final SelesOutInput selesOutInput, final ImageOrientation imageOrientation, final CapturePhotoAsBitmapCallback capturePhotoAsBitmapCallback) {
        this.capturePhotoAsJPEGData(new CapturePhotoAsJPEGDataCallback() {
            @Override
            public void onCapturePhotoAsJPEGData(final byte[] array) {
                if (array == null) {
                    if (capturePhotoAsBitmapCallback != null) {
                        capturePhotoAsBitmapCallback.onCapturePhotoAsBitmap(null);
                    }
                    return;
                }
                ThreadHelper.runThread(new Runnable() {
                    @Override
                    public void run() {
                        SelesStillCamera2.this.a(array, selesOutInput, imageOrientation, capturePhotoAsBitmapCallback);
                    }
                });
            }
        });
    }
    
    protected CameraCaptureSession.StateCallback getSessionCaptureStateCallback() {
        return this.y;
    }
    
    protected void onImageCaptured(final ImageReader imageReader, final CapturePhotoAsJPEGDataCallback capturePhotoAsJPEGDataCallback) {
        final Image acquireLatestImage = imageReader.acquireLatestImage();
        final ByteBuffer buffer = acquireLatestImage.getPlanes()[0].getBuffer();
        final byte[] dst = new byte[buffer.remaining()];
        buffer.get(dst);
        acquireLatestImage.close();
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                SelesStillCamera2.this.a(false);
                SelesStillCamera2.this.onTakePictured(dst);
                if (capturePhotoAsJPEGDataCallback != null) {
                    capturePhotoAsJPEGDataCallback.onCapturePhotoAsJPEGData(dst);
                }
            }
        });
    }
    
    private void a(final CapturePhotoAsJPEGDataCallback capturePhotoAsJPEGDataCallback) {
        if (this.b() == null || this.b == null || this.c == null) {
            this.b(capturePhotoAsJPEGDataCallback);
            return;
        }
        this.b().setOnImageAvailableListener((ImageReader.OnImageAvailableListener)new ImageReader.OnImageAvailableListener() {
            public void onImageAvailable(final ImageReader imageReader) {
                SelesStillCamera2.this.playSystemShutter();
                ThreadHelper.runThread(new Runnable() {
                    @Override
                    public void run() {
                        SelesStillCamera2.this.onImageCaptured(imageReader, capturePhotoAsJPEGDataCallback);
                    }
                });
            }
        }, this.mHandler);
        try {
            (this.e = this.b.createCaptureRequest(2)).addTarget(this.b().getSurface());
            this.e.set(CaptureRequest.CONTROL_MODE, 1);
            this.i();
            this.onConfigCapture(this.e);
            this.c.stopRepeating();
            this.c.capture(this.e.build(), this.z, this.mHandler);
        }
        catch (Exception ex) {
            TLog.e(ex, "takePictureAtAsync", new Object[0]);
            this.b(capturePhotoAsJPEGDataCallback);
        }
    }
    
    protected void onConfigCapture(final CaptureRequest.Builder builder) {
    }
    
    private void i() {
        Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
        Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.CONTROL_AWB_MODE);
        Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.SENSOR_EXPOSURE_TIME);
        Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION);
        Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.LENS_FOCUS_DISTANCE);
        Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.CONTROL_EFFECT_MODE);
        Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.SENSOR_SENSITIVITY);
        Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.CONTROL_AF_REGIONS);
        Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.CONTROL_AE_REGIONS);
        Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.CONTROL_SCENE_MODE);
        Camera2Helper.mergerBuilder(this.d, this.e, CaptureRequest.SCALER_CROP_REGION);
    }
    
    private void b(final CapturePhotoAsJPEGDataCallback capturePhotoAsJPEGDataCallback) {
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                SelesStillCamera2.this.a(false);
                SelesStillCamera2.this.onTakePictureFailed();
                if (capturePhotoAsJPEGDataCallback != null) {
                    capturePhotoAsJPEGDataCallback.onCapturePhotoAsJPEGData(null);
                }
            }
        });
    }
    
    private void a(final byte[] array, final SelesOutInput selesOutInput, final ImageOrientation imageOrientation, final CapturePhotoAsBitmapCallback capturePhotoAsBitmapCallback) {
        final Bitmap decodeToBitmapAtAsync = this.decodeToBitmapAtAsync(array);
        if (decodeToBitmapAtAsync == null) {
            if (capturePhotoAsBitmapCallback != null) {
                ThreadHelper.post(new Runnable() {
                    @Override
                    public void run() {
                        if (capturePhotoAsBitmapCallback != null) {
                            capturePhotoAsBitmapCallback.onCapturePhotoAsBitmap(null);
                        }
                    }
                });
            }
            return;
        }
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                SelesStillCamera2.this.a(decodeToBitmapAtAsync, selesOutInput, imageOrientation, capturePhotoAsBitmapCallback);
            }
        });
    }

    private void a( Bitmap var1, SelesOutInput var2, ImageOrientation var3, final CapturePhotoAsBitmapCallback var4) {
        if (var2 != null && var1 != null) {
            TuSdkSize var5 = BitmapHelper.computerScaleSize(var1, this.getOutputSize(), false, false);
            SelesPicture var6 = new SelesPicture(var1, false);
            var6.setScaleSize(var5);
            Rect var7 = RectHelper.computerMinMaxSideInRegionRatio(var6.getScaleSize(), this.getRegionRatio());
            var6.setOutputRect(var7);
            var6.setInputRotation(var3);
            var6.addTarget(var2, 0);
            var6.processImage();
            var1 = var6.imageFromCurrentlyProcessedOutput();
        }

        final Bitmap finalVar = var1;
        ThreadHelper.post(new Runnable() {
            public void run() {
                if (var4 != null) {
                    var4.onCapturePhotoAsBitmap(finalVar);
                }

            }
        });
    }
}
