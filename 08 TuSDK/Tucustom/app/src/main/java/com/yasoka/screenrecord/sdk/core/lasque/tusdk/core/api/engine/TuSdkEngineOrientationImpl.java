// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine;

//import org.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.utils.hardware.CameraHelper;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
//import org.lasque.tusdk.core.struct.TuSdkSize;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkEngineOrientationImpl implements TuSdkEngineOrientation
{
    private TuSdkSize a;
    private TuSdkSize b;
    private CameraConfigs.CameraFacing c;
    private InterfaceOrientation d;
    private ImageOrientation e;
    private ImageOrientation f;
    private ImageOrientation g;
    private boolean h;
    private boolean i;
    private boolean j;
    private boolean k;
    private float l;
    private InterfaceOrientation m;
    private ImageOrientation n;
    
    public TuSdkEngineOrientationImpl() {
        this.a = TuSdkSize.create(0);
        this.b = TuSdkSize.create(0);
        this.c = CameraConfigs.CameraFacing.Front;
        this.d = InterfaceOrientation.Portrait;
        this.e = ImageOrientation.Up;
        this.f = ImageOrientation.Up;
        this.g = ImageOrientation.Up;
        this.h = false;
        this.i = true;
        this.m = InterfaceOrientation.Portrait;
        this.n = ImageOrientation.Up;
    }
    
    @Override
    public TuSdkSize getInputSize() {
        return this.a;
    }
    
    @Override
    public void setInputSize(final int n, final int n2) {
        if (this.a.width != n || this.a.height != n2) {
            this.a = TuSdkSize.create(n, n2);
            this.a();
        }
    }
    
    @Override
    public CameraConfigs.CameraFacing getCameraFacing() {
        return this.c;
    }

    @Override
    public void setCameraFacing(CameraConfigs.CameraFacing p0) {
        if (c == null) {
            return;
        }
        this.c = c;
        this.a();
    }

    @Override
    public void switchCameraFacing() {
        this.setCameraFacing((this.c == CameraConfigs.CameraFacing.Front) ? CameraConfigs.CameraFacing.Back : CameraConfigs.CameraFacing.Front);
    }
    
    @Override
    public InterfaceOrientation getInterfaceOrientation() {
        return this.d;
    }

    @Override
    public void setInterfaceOrientation(InterfaceOrientation d) {
        if (d == null) {
            return;
        }
        this.d = d;
        this.a();
    }

    @Override
    public void setInputOrientation(ImageOrientation e) {
        if (e == null) {
            return;
        }
        this.e = e;
        this.a();
    }
    @Override
    public void setOutputOrientation(final ImageOrientation f) {
        if (f == null) {
            return;
        }
        this.f = f;
        this.a();
    }
    
    @Override
    public void setHorizontallyMirrorFrontFacingCamera(final boolean j) {
        this.j = j;
        this.a();
    }
    
    @Override
    public void setHorizontallyMirrorRearFacingCamera(final boolean k) {
        this.k = k;
        this.a();
    }
    
    @Override
    public void release() {
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
    
    private void a() {
        if (this.e != null) {
            this.g = this.e;
        }
        else {
            this.g = TuSdkCameraOrientationImpl.computerOutputOrientation(CameraHelper.firstCameraInfo(this.c), ContextUtils.getInterfaceRotation(TuSdkContext.context()), this.k, this.j, InterfaceOrientation.Portrait);
        }
        this.b = this.a.transforOrientation(this.g);
    }
    
    @Override
    public ImageOrientation getInputRotation() {
        return this.g;
    }
    
    @Override
    public ImageOrientation getOutputOrientation() {
        return this.f;
    }
    
    @Override
    public TuSdkSize getOutputSize() {
        return this.b;
    }
    
    @Override
    public float getDeviceAngle() {
        return this.l;
    }
    
    @Override
    public void setDeviceAngle(final float l) {
        this.l = l;
    }
    
    @Override
    public InterfaceOrientation getDeviceOrient() {
        return this.m;
    }
    
    @Override
    public void setDeviceOrient(final InterfaceOrientation m) {
        this.m = m;
    }
    
    @Override
    public boolean isOriginalCaptureOrientation() {
        return this.i;
    }
    
    @Override
    public void setOriginalCaptureOrientation(final boolean i) {
        this.i = i;
    }
    
    @Override
    public void setOutputCaptureMirrorEnabled(final boolean h) {
        this.h = h;
    }
    
    @Override
    public boolean isOutputCaptureMirrorEnabled() {
        return this.h;
    }
    
    @Override
    public ImageOrientation getYuvOutputOrienation() {
        return this.n;
    }
    
    @Override
    public void setYuvOutputOrienation(final ImageOrientation n) {
        if (n == null) {
            return;
        }
        this.n = n;
    }
}
