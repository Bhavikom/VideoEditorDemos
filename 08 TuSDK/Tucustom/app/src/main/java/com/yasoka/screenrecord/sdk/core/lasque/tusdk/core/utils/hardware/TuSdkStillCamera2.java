// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware;

//import org.lasque.tusdk.core.seles.sources.SelesVideoCamera2;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.util.List;
import android.util.Range;
import android.hardware.camera2.CaptureRequest;
import android.widget.RelativeLayout;
import android.content.Context;
import android.media.MediaActionSound;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesStillCamera2;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesVideoCamera2;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.sources.SelesStillCamera2;

@TargetApi(21)
public class TuSdkStillCamera2 extends SelesStillCamera2 implements TuSdkStillCameraInterface
{
    private final TuSdkStillCameraAdapter<TuSdkStillCamera2> b;
    private MediaActionSound c;
    
    @Override
    public TuSdkStillCameraAdapter<?> adapter() {
        return this.b;
    }
    
    @Override
    public TuSdkStillCameraAdapter.CameraState getState() {
        return this.b.getState();
    }
    
    @Override
    public float getRegionRatio() {
        return this.b.getRegionRatio();
    }
    
    @Override
    public void setCameraListener(final TuSdkStillCameraListener cameraListener) {
        this.b.setCameraListener(cameraListener);
    }
    
    private void a() {
        (this.c = new MediaActionSound()).load(0);
    }
    
    @Override
    protected final void playSystemShutter() {
        if (this.c == null || this.b.isDisableCaptureSound()) {
            return;
        }
        this.c.play(0);
    }
    
    public TuSdkStillCamera2(final Context context, final CameraConfigs.CameraFacing cameraFacing, final RelativeLayout relativeLayout) {
        super(context, cameraFacing);
        this.a();
        this.b();
        this.b = new TuSdkStillCameraAdapter<TuSdkStillCamera2>(context, relativeLayout, this);
    }
    
    private void b() {
        this.setOutputImageOrientation(InterfaceOrientation.Portrait);
        this.setHorizontallyMirrorFrontFacingCamera(true);
    }
    
    @Override
    protected void onDestroy() {
        this.b.onDestroy();
        if (this.c != null) {
            this.c.release();
            this.c = null;
        }
        super.onDestroy();
    }
    
    @Override
    public void pauseCameraCapture() {
        super.pauseCameraCapture();
        this.b.pauseCameraCapture();
    }
    
    @Override
    public void resumeCameraCapture() {
        super.resumeCameraCapture();
        this.b.resumeCameraCapture();
    }
    
    @Override
    protected void onMainThreadStart() {
        super.onMainThreadStart();
        this.b.onMainThreadStart();
    }
    
    @Override
    public void stopCameraCapture() {
        if (this.getCameraDevice() != null) {
            this.b.stopCameraCapture();
        }
        super.stopCameraCapture();
    }
    
    @Override
    protected void onCounfigPreview(final CaptureRequest.Builder builder) {
        super.onCounfigPreview(builder);
        if (builder == null) {
            return;
        }
        final Range range = (Range)builder.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
        if (range != null) {
            this.b.setRendererFPS((int)range.getUpper());
        }
    }
    
    @Override
    protected void onCameraStarted() {
        super.onCameraStarted();
        this.b.onCameraStarted();
    }
    
    @Override
    public void onCameraFaceDetection(final List<TuSdkFace> list, final TuSdkSize tuSdkSize) {
        super.onCameraFaceDetection(list, tuSdkSize);
        this.b.onCameraFaceDetection(list, tuSdkSize);
    }
    
    @Override
    public void switchFilter(final String s) {
        final Runnable switchFilter = this.b.switchFilter(s);
        if (switchFilter == null) {
            return;
        }
        this.runOnDraw(switchFilter);
    }
    
    @Override
    public void autoFocus(final SelesBaseCameraInterface.TuSdkAutoFocusCallback tuSdkAutoFocusCallback) {
        if (!this.canSupportAutoFocus() || !this.b.isCreatedSurface()) {
            if (tuSdkAutoFocusCallback != null) {
                tuSdkAutoFocusCallback.onAutoFocus(false, this);
            }
            return;
        }
        super.autoFocus(tuSdkAutoFocusCallback);
    }
    
    @Override
    public void captureImage() {
        if (this.getCameraDevice() == null) {
            return;
        }
        this.b.captureImage();
    }
    
    @Override
    protected void onTakePictured(final byte[] array) {
        super.onTakePictured(array);
        this.b.onTakePictured(array);
    }
    
    @Override
    protected Bitmap decodeToBitmapAtAsync(final byte[] array) {
        return this.b.decodeToBitmapAtAsync(array, super.decodeToBitmapAtAsync(array));
    }
    
    @Override
    public ImageOrientation capturePhotoOrientation() {
        final boolean b = this.isHorizontallyMirrorFrontFacingCamera() && !this.isDisableMirrorFrontFacing();
        InterfaceOrientation interfaceOrientation = this.b.getDeviceOrient();
        if ((this.isFrontFacingCameraPresent() && !b) || (this.isBackFacingCameraPresent() && this.isHorizontallyMirrorRearFacingCamera())) {
            interfaceOrientation = InterfaceOrientation.getWithDegrees(this.b.getDeviceOrient().viewDegree());
        }
        return SelesVideoCamera2.computerOutputOrientation(this.getCameraCharacter(), interfaceOrientation, this.isHorizontallyMirrorRearFacingCamera(), b, this.getOutputImageOrientation());
    }
    
    @Override
    public void setPreviewRatio(final float n) {
    }
    
    @Override
    public void setOutputPictureRatio(final float n) {
    }
}
