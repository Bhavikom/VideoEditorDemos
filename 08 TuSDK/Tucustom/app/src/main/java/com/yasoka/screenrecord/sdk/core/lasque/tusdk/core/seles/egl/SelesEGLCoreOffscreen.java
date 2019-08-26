// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl;

import android.opengl.EGLContext;
import android.opengl.EGL14;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.opengl.EGLSurface;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class SelesEGLCoreOffscreen extends SelesEGLCore
{
    private EGLSurface a;
    private TuSdkSize b;
    
    public TuSdkSize getSurfaceSize() {
        if (this.b == null) {
            TLog.w("%s getSurfaceSize need attachSurface first", "SelesEGLCoreOffscreen");
            return null;
        }
        return this.b;
    }
    
    public boolean isSurfaceAttached() {
        return this.a != null && this.a != EGL14.EGL_NO_SURFACE;
    }
    
    public SelesEGLCoreOffscreen(final EGLContext eglContext) {
        super(eglContext, 0);
        this.a = EGL14.EGL_NO_SURFACE;
    }
    
    public SelesEGLCoreOffscreen(final EGLContext eglContext, final TuSdkSize tuSdkSize) {
        this(eglContext);
        this.attachSurface(tuSdkSize);
    }
    
    public boolean attachSurface(final TuSdkSize tuSdkSize) {
        if (tuSdkSize == null || !tuSdkSize.isSize()) {
            TLog.d("%s attachSurface need size and the side > 0, Size: %s", "SelesEGLCoreOffscreen", tuSdkSize);
            return false;
        }
        return this.attachSurface(tuSdkSize.width, tuSdkSize.height);
    }
    
    public boolean attachSurface(final int n, final int n2) {
        if (this.a != EGL14.EGL_NO_SURFACE) {
            TLog.w("%s surface already created", "SelesEGLCoreOffscreen");
            return false;
        }
        this.a = this.createOffscreenSurface(n, n2);
        if (this.a == null) {
            return false;
        }
        this.b = TuSdkSize.create(n, n2);
        return this.makeCurrent(this.a);
    }
    
    public boolean swapBuffers() {
        return this.swapBuffers(this.a);
    }
    
    public void setPresentationTime(final long n) {
        this.setPresentationTime(this.a, n);
    }
    
    @Override
    protected void _cleanEGLWhenDestory() {
        this.releaseSurface(this.a);
        this.a = EGL14.EGL_NO_SURFACE;
    }
}
