// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl;

import android.opengl.EGLContext;
import android.opengl.EGL14;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.view.Surface;
import android.opengl.EGLSurface;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class SelesEGLCoreWindow extends SelesEGLCore
{
    private EGLSurface a;
    private Surface b;
    private boolean c;
    private TuSdkSize d;
    
    public TuSdkSize getSurfaceSize() {
        if (this.d == null) {
            TLog.w("%s getSurfaceSize need attachSurface first", "SelesEGLCoreWindow");
            return null;
        }
        return this.d;
    }
    
    public boolean isSurfaceAttached() {
        return this.a != null && this.a != EGL14.EGL_NO_SURFACE;
    }
    
    public SelesEGLCoreWindow(final EGLContext eglContext) {
        super(eglContext, 1);
        this.a = EGL14.EGL_NO_SURFACE;
        this.c = false;
    }
    
    public boolean attachSurface(final Surface surface, final boolean c) {
        if (this.a != EGL14.EGL_NO_SURFACE) {
            TLog.w("%s surface already created", "SelesEGLCoreWindow");
            return false;
        }
        this.a = this.createWindowSurface(surface);
        if (this.a == null) {
            return false;
        }
        this.d = TuSdkSize.create(this.querySurface(this.a, 12375), this.querySurface(this.a, 12374));
        this.c = c;
        return this.makeCurrent(this.a);
    }
    
    public boolean swapBuffers() {
        return this.swapBuffers(this.a);
    }
    
    public void setPresentationTime(final long n) {
        this.setPresentationTime(this.a, n);
    }
    
    @Override
    public void destroy() {
        if (this.b != null && this.c) {
            this.b.release();
        }
        this.b = null;
        super.destroy();
    }
    
    @Override
    protected void _cleanEGLWhenDestory() {
        this.releaseSurface(this.a);
        this.a = EGL14.EGL_NO_SURFACE;
    }
}
