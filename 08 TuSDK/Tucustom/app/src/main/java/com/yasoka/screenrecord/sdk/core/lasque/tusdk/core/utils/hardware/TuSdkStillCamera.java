// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware;

//import org.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.util.List;
import android.widget.RelativeLayout;
import android.content.Context;
import android.hardware.Camera;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesStillCamera;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.sources.SelesStillCamera;

public class TuSdkStillCamera extends SelesStillCamera implements TuSdkStillCameraInterface
{
    private final TuSdkStillCameraAdapter<TuSdkStillCamera> b;
    private Camera.ShutterCallback c;
    
    @Override
    public TuSdkStillCameraAdapter<?> adapter() {
        return this.b;
    }
    
    @Override
    public TuSdkStillCameraAdapter.CameraState getState() {
        return this.b.getState();
    }
    
    @Override
    public void setCameraListener(final TuSdkStillCameraListener cameraListener) {
        this.b.setCameraListener(cameraListener);
    }
    
    @Override
    public float getRegionRatio() {
        return this.b.getRegionRatio();
    }
    
    public TuSdkStillCamera(final Context context, final CameraConfigs.CameraFacing cameraFacing, final RelativeLayout relativeLayout) {
        super(context, cameraFacing);
        this.c = (Camera.ShutterCallback)new Camera.ShutterCallback() {
            public void onShutter() {
            }
        };
        this.a();
        this.b = new TuSdkStillCameraAdapter<TuSdkStillCamera>(context, relativeLayout, this);
    }
    
    private void a() {
        this.setOutputImageOrientation(InterfaceOrientation.Portrait);
        this.setHorizontallyMirrorFrontFacingCamera(true);
    }
    
    @Override
    protected void onDestroy() {
        this.b.onDestroy();
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
        if (this.inputCamera() != null) {
            this.b.stopCameraCapture();
        }
        super.stopCameraCapture();
    }
    
    @Override
    protected void onInitConfig(final Camera camera) {
        if (camera == null) {
            return;
        }
        super.onInitConfig(camera);
        final Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            return;
        }
        final int[] array = new int[2];
        parameters.getPreviewFpsRange(array);
        this.b.setRendererFPS(Math.max(array[0] / 1000, array[1] / 1000));
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
        if (this.inputCamera() == null || !this.canSupportAutoFocus() || !this.b.isCreatedSurface()) {
            if (tuSdkAutoFocusCallback != null) {
                tuSdkAutoFocusCallback.onAutoFocus(false, this);
            }
            return;
        }
        super.autoFocus(tuSdkAutoFocusCallback);
    }
    
    @Override
    protected Camera.ShutterCallback getShutterCallback() {
        if (this.b.isDisableCaptureSound()) {
            return null;
        }
        return this.c;
    }
    
    @Override
    public void captureImage() {
        if (this.inputCamera() == null) {
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
        return TuSdkCameraOrientationImpl.computerOutputOrientation(this.inputCameraInfo(), interfaceOrientation, this.isHorizontallyMirrorRearFacingCamera(), b, this.getOutputImageOrientation());
    }
}
