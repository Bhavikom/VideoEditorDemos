// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.gl;

import android.opengl.EGLExt;
//import org.lasque.tusdk.core.utils.TLog;
import android.opengl.EGL14;
import android.opengl.EGLSurface;
import android.opengl.EGLContext;
import android.opengl.EGLConfig;
import android.opengl.EGLDisplay;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(17)
public class EglCore
{
    private EGLDisplay a;
    private EGLConfig b;
    private EGLContext c;
    private EGLSurface d;
    
    public EglCore(final int n) {
        this.changeDisplay(n);
    }
    
    public EglCore() {
        this(0);
    }
    
    public void changeDisplay(final int n) {
        this.a = EGL14.eglGetDisplay(n);
        final int[] array = new int[2];
        EGL14.eglInitialize(this.a, array, 0, array, 1);
    }
    
    public EGLConfig getConfig(final EGLConfigAttrs eglConfigAttrs) {
        final EGLConfig[] array = { null };
        final int[] array2 = { 0 };
        EGL14.eglChooseConfig(this.a, eglConfigAttrs.a(), 0, array, 0, 1, array2, 0);
        if (array2[0] > 0) {
            if (eglConfigAttrs.isDefault()) {
                this.b = array[0];
            }
            return array[0];
        }
        return null;
    }
    
    public EGLConfig getDefaultConfig() {
        return this.b;
    }
    
    public EGLSurface getDefaultSurface() {
        return this.d;
    }
    
    public EGLContext getDefaultContext() {
        return this.c;
    }
    
    public EGLContext createContext(final EGLConfig eglConfig, final EGLContext eglContext, final EGLContextAttrs eglContextAttrs) {
        final EGLContext eglCreateContext = EGL14.eglCreateContext(this.a, eglConfig, eglContext, eglContextAttrs.a(), 0);
        if (eglContextAttrs.isDefault()) {
            this.c = eglCreateContext;
        }
        return eglCreateContext;
    }
    
    public EGLSurface createWindowSurface(final EGLConfig eglConfig, final Object o) {
        return EGL14.eglCreateWindowSurface(this.a, eglConfig, o, new int[] { 12344 }, 0);
    }
    
    public EGLSurface createWindowSurface(final Object o) {
        return this.d = EGL14.eglCreateWindowSurface(this.a, this.b, o, new int[] { 12344 }, 0);
    }
    
    public EGLSurface createPBufferSurface(final EGLConfig eglConfig, final int n, final int n2) {
        return EGL14.eglCreatePbufferSurface(this.a, eglConfig, new int[] { 12375, n, 12374, n2, 12344 }, 0);
    }
    
    public boolean createGLESWithSurface(final EGLConfigAttrs eglConfigAttrs, final EGLContextAttrs eglContextAttrs, final Object o) {
        final EGLConfig config = this.getConfig(eglConfigAttrs.surfaceType(4).makeDefault(true));
        if (config == null) {
            TLog.i("getConfig failed : " + EGL14.eglGetError(), new Object[0]);
            return false;
        }
        this.c = this.createContext(config, EGL14.EGL_NO_CONTEXT, eglContextAttrs.makeDefault(true));
        if (this.c == EGL14.EGL_NO_CONTEXT) {
            TLog.i("createContext failed : " + EGL14.eglGetError(), new Object[0]);
            return false;
        }
        this.d = this.createWindowSurface(o);
        if (this.d == EGL14.EGL_NO_SURFACE) {
            TLog.i("createWindowSurface failed : " + EGL14.eglGetError(), new Object[0]);
            return false;
        }
        if (!EGL14.eglMakeCurrent(this.a, this.d, this.d, this.c)) {
            TLog.i("eglMakeCurrent failed : " + EGL14.eglGetError(), new Object[0]);
            return false;
        }
        return true;
    }
    
    public boolean makeCurrent(final EGLSurface eglSurface, final EGLSurface eglSurface2, final EGLContext eglContext) {
        if (!EGL14.eglMakeCurrent(this.a, eglSurface, eglSurface2, eglContext)) {
            TLog.i("eglMakeCurrent failed : " + EGL14.eglGetError(), new Object[0]);
        }
        return true;
    }
    
    public boolean makeCurrent(final EGLSurface eglSurface, final EGLContext eglContext) {
        return this.makeCurrent(eglSurface, eglSurface, eglContext);
    }
    
    public boolean makeCurrent(final EGLSurface eglSurface) {
        return this.makeCurrent(eglSurface, this.c);
    }
    
    public boolean makeCurrent() {
        return this.makeCurrent(this.d, this.c);
    }
    
    @TargetApi(18)
    public void setPresentationTime(final EGLSurface eglSurface, final long n) {
        EGLExt.eglPresentationTimeANDROID(this.a, eglSurface, n);
    }
    
    public EGLSurface createGLESWithPBuffer(final EGLConfigAttrs eglConfigAttrs, final EGLContextAttrs eglContextAttrs, final int n, final int n2) {
        final EGLConfig config = this.getConfig(eglConfigAttrs.surfaceType(1));
        if (config == null) {
            TLog.i("getConfig failed : " + EGL14.eglGetError(), new Object[0]);
            return null;
        }
        final EGLContext context = this.createContext(config, EGL14.EGL_NO_CONTEXT, eglContextAttrs);
        if (context == EGL14.EGL_NO_CONTEXT) {
            TLog.i("createContext failed : " + EGL14.eglGetError(), new Object[0]);
            return null;
        }
        final EGLSurface pBufferSurface = this.createPBufferSurface(config, n, n2);
        if (pBufferSurface == EGL14.EGL_NO_SURFACE) {
            TLog.i("createWindowSurface failed : " + EGL14.eglGetError(), new Object[0]);
            return null;
        }
        if (!EGL14.eglMakeCurrent(this.a, pBufferSurface, pBufferSurface, context)) {
            TLog.i("eglMakeCurrent failed : " + EGL14.eglGetError(), new Object[0]);
            return null;
        }
        return pBufferSurface;
    }
    
    public void swapBuffers(final EGLSurface eglSurface) {
        EGL14.eglSwapBuffers(this.a, eglSurface);
    }
    
    public boolean destroyGLES(final EGLSurface eglSurface, final EGLContext eglContext) {
        EGL14.eglMakeCurrent(this.a, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        if (eglSurface != null) {
            EGL14.eglDestroySurface(this.a, eglSurface);
        }
        if (eglContext != null) {
            EGL14.eglDestroyContext(this.a, eglContext);
        }
        EGL14.eglTerminate(this.a);
        TLog.i("gl destroy gles", new Object[0]);
        return true;
    }
    
    public void destroySurface(final EGLSurface eglSurface) {
        EGL14.eglDestroySurface(this.a, eglSurface);
    }
    
    public EGLDisplay getDisplay() {
        return this.a;
    }
}
