// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera;

//import org.lasque.tusdk.core.utils.ContextUtils;
import android.hardware.Camera;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener.TuSdkOrientationEventListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkCameraOrientationImpl implements TuSdkCameraOrientation
{
    private ImageOrientation a;
    private InterfaceOrientation b;
    private boolean c;
    private boolean d;
    private boolean e;
    private final TuSdkOrientationEventListener f;
    private TuSdkCameraBuilder g;
    
    public InterfaceOrientation getOutputImageOrientation() {
        return this.b;
    }
    
    public void setOutputImageOrientation(final InterfaceOrientation b) {
        if (b == null) {
            return;
        }
        this.b = b;
    }
    
    public boolean isHorizontallyMirrorFrontFacingCamera() {
        return this.c;
    }
    
    public void setHorizontallyMirrorFrontFacingCamera(final boolean c) {
        this.c = c;
    }
    
    public boolean isHorizontallyMirrorRearFacingCamera() {
        return this.d;
    }
    
    public void setHorizontallyMirrorRearFacingCamera(final boolean d) {
        this.d = d;
    }
    
    public boolean isDisableMirrorFrontFacing() {
        return this.e;
    }
    
    public void setDisableMirrorFrontFacing(final boolean e) {
        this.e = e;
    }
    
    @Override
    public ImageOrientation previewOrientation() {
        return this.a;
    }
    
    public TuSdkCameraOrientationImpl() {
        this.a = ImageOrientation.Up;
        this.b = InterfaceOrientation.Portrait;
        this.f = new TuSdkOrientationEventListener(TuSdkContext.context());
    }
    
    public void setDeviceOrientListener(final TuSdkOrientationEventListener.TuSdkOrientationDelegate tuSdkOrientationDelegate, final TuSdkOrientationEventListener.TuSdkOrientationDegreeDelegate tuSdkOrientationDegreeDelegate) {
        this.f.setDelegate(tuSdkOrientationDelegate, tuSdkOrientationDegreeDelegate);
    }
    
    @Override
    public void configure(final TuSdkCameraBuilder g) {
        if (g == null) {
            TLog.e("%s configure builder is empty.", "TuSdkCameraOrientationImpl");
            return;
        }
        this.g = g;
        this.f.enable();
        this.a = this.a();
    }
    
    @Override
    public void changeStatus(final TuSdkCamera.TuSdkCameraStatus tuSdkCameraStatus) {
        if (tuSdkCameraStatus == TuSdkCamera.TuSdkCameraStatus.CAMERA_START_PREVIEW || tuSdkCameraStatus == TuSdkCamera.TuSdkCameraStatus.CAMERA_PREPARE_SHOT) {
            this.f.enable();
        }
        else {
            this.f.disable();
        }
    }
    
    private ImageOrientation a() {
        if (this.g == null) {
            return ImageOrientation.Up;
        }
        return computerOutputOrientation(TuSdkContext.context(), this.g.getInfo(), this.isHorizontallyMirrorRearFacingCamera(), this.isHorizontallyMirrorFrontFacingCamera(), this.getOutputImageOrientation());
    }
    
    @Override
    public ImageOrientation captureOrientation() {
        if (this.g == null) {
            return ImageOrientation.Up;
        }
        final boolean b = this.isHorizontallyMirrorFrontFacingCamera() && !this.isDisableMirrorFrontFacing();
        InterfaceOrientation interfaceOrientation = this.f.getOrien();
        if ((!this.g.isBackFacingCameraPresent() && !b) || (this.g.isBackFacingCameraPresent() && this.isHorizontallyMirrorRearFacingCamera())) {
            interfaceOrientation = InterfaceOrientation.getWithDegrees(this.f.getDeviceAngle());
        }
        return computerOutputOrientation(this.g.getInfo(), interfaceOrientation, this.isHorizontallyMirrorRearFacingCamera(), b, this.getOutputImageOrientation());
    }
    
    public static ImageOrientation computerOutputOrientation(final Context context, final Camera.CameraInfo cameraInfo, final boolean b, final boolean b2, final InterfaceOrientation interfaceOrientation) {
        return computerOutputOrientation(cameraInfo, ContextUtils.getInterfaceRotation(context), b, b2, interfaceOrientation);
    }
    
    public static ImageOrientation computerOutputOrientation(final Camera.CameraInfo cameraInfo, InterfaceOrientation portrait, final boolean b, final boolean b2, InterfaceOrientation portrait2) {
        if (portrait == null) {
            portrait = InterfaceOrientation.Portrait;
        }
        if (portrait2 == null) {
            portrait2 = InterfaceOrientation.Portrait;
        }
        int orientation = 0;
        int n = 1;
        if (cameraInfo != null) {
            orientation = cameraInfo.orientation;
            n = ((cameraInfo.facing == 0) ? 1 : 0);
        }
        int degree = portrait.getDegree();
        if (portrait2 != null) {
            degree += portrait2.getDegree();
        }
        if (n != 0) {
            final InterfaceOrientation withDegrees = InterfaceOrientation.getWithDegrees(orientation - degree);
            if (b) {
                switch (withDegrees.ordinal()) {
                    case 1: {
                        return ImageOrientation.DownMirrored;
                    }
                    case 2: {
                        return ImageOrientation.LeftMirrored;
                    }
                    case 3: {
                        return ImageOrientation.RightMirrored;
                    }
                    default: {
                        return ImageOrientation.UpMirrored;
                    }
                }
            }
            else {
                switch (withDegrees.ordinal()) {
                    case 1: {
                        return ImageOrientation.Up;
                    }
                    case 2: {
                        return ImageOrientation.Left;
                    }
                    case 3: {
                        return ImageOrientation.Right;
                    }
                    default: {
                        return ImageOrientation.Down;
                    }
                }
            }
        }
        else {
            final InterfaceOrientation withDegrees2 = InterfaceOrientation.getWithDegrees(orientation + degree);
            if (b2) {
                switch (withDegrees2.ordinal()) {
                    case 1: {
                        return ImageOrientation.UpMirrored;
                    }
                    case 2: {
                        return ImageOrientation.LeftMirrored;
                    }
                    case 3: {
                        return ImageOrientation.RightMirrored;
                    }
                    default: {
                        return ImageOrientation.DownMirrored;
                    }
                }
            }
            else {
                switch (withDegrees2.ordinal()) {
                    case 1: {
                        return ImageOrientation.Down;
                    }
                    case 2: {
                        return ImageOrientation.Left;
                    }
                    case 3: {
                        return ImageOrientation.Right;
                    }
                    default: {
                        return ImageOrientation.Up;
                    }
                }
            }
        }
    }
}
