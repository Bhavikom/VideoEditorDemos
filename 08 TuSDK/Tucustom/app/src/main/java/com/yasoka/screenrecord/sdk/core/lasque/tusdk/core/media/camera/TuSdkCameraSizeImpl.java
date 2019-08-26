// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera;

import android.graphics.ImageFormat;
//import org.lasque.tusdk.core.utils.TLog;
import android.hardware.Camera;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraHelper;
//import org.lasque.tusdk.core.utils.hardware.CameraHelper;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSdkCameraSizeImpl implements TuSdkCameraSize
{
    private int a;
    private float b;
    private float c;
    private TuSdkSize d;
    private int e;
    private float f;
    private TuSdkSize g;
    private TuSdkCameraBuilder h;
    
    private int a() {
        final TuSdkSize screenSize = ContextUtils.getScreenSize(TuSdkContext.context());
        if (this.a < 1 || this.a > screenSize.maxSide()) {
            this.a = screenSize.maxSide();
        }
        return this.a;
    }
    
    public void setPreviewMaxSize(final int a) {
        this.a = a;
    }
    
    public float getPreviewEffectScale() {
        return this.b;
    }
    
    public void setPreviewEffectScale(final float b) {
        if (b <= 0.0f) {
            return;
        }
        if (b > 1.0f) {
            this.b = 1.0f;
        }
        this.b = b;
    }
    
    public void setPreviewRatio(final float c) {
        this.c = c;
    }
    
    public float getPreviewRatio() {
        return this.c;
    }
    
    @Override
    public TuSdkSize previewOptimalSize() {
        return this.d;
    }
    
    @Override
    public int previewBufferLength() {
        return this.e;
    }
    
    public void setOutputPictureRatio(final float f) {
        this.f = f;
    }
    
    public float getOutputPictureRatio() {
        return this.f;
    }
    
    @Override
    public TuSdkSize getOutputSize() {
        if (this.g == null) {
            this.g = ContextUtils.getScreenSize(TuSdkContext.context());
        }
        return this.g;
    }
    
    public void setOutputSize(final TuSdkSize tuSdkSize) {
        if (tuSdkSize == null || !tuSdkSize.isSize()) {
            return;
        }
        this.g = tuSdkSize.limitSize();
        final Camera.Parameters b = this.b();
        if (b == null) {
            return;
        }
        CameraHelper.setPictureSize(TuSdkContext.context(), b, this.g);
        this.a(b);
    }
    
    public TuSdkCameraSizeImpl() {
        if (ContextUtils.getScreenSize(TuSdkContext.context()).maxSide() < 1000) {
            this.b = 0.85f;
        }
        else {
            this.b = 0.75f;
        }
    }
    
    @Override
    public void configure(final TuSdkCameraBuilder h) {
        if (h == null) {
            TLog.e("%s configure builder is empty.", "TuSdkCameraSizeImpl");
            return;
        }
        this.h = h;
        final Camera.Parameters b = this.b();
        if (b == null) {
            TLog.e("%s configure Camera.Parameters is empty.", "TuSdkCameraSizeImpl");
            return;
        }
        CameraHelper.setPreviewSize(TuSdkContext.context(), b, this.a(), this.getPreviewEffectScale(), this.getPreviewRatio());
        CameraHelper.setPictureSize(TuSdkContext.context(), b, this.getOutputSize().limitSize(), this.getOutputPictureRatio());
        this.a(b);
        this.d = CameraHelper.createSize(b.getPreviewSize());
        if (this.d == null) {
            TLog.e("%s configure can not set preview size", "TuSdkCameraSizeImpl");
            return;
        }
        this.e = this.d.width * this.d.height * ImageFormat.getBitsPerPixel(b.getPreviewFormat()) / 8;
    }
    
    @Override
    public void changeStatus(final TuSdkCamera.TuSdkCameraStatus tuSdkCameraStatus) {
    }
    
    private Camera.Parameters b() {
        if (this.h == null) {
            return null;
        }
        return this.h.getParameters();
    }
    
    private void a(final Camera.Parameters parameters) {
        if (this.h == null || this.h.getOrginCamera() == null) {
            return;
        }
        this.h.getOrginCamera().setParameters(parameters);
    }
}
