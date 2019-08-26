// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.gl;

import android.opengl.EGLExt;
import android.graphics.SurfaceTexture;
//import org.lasque.tusdk.core.utils.TLog;
import android.opengl.EGL14;
import android.view.Surface;
import android.opengl.EGLSurface;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class SelesWindowsSurface
{
    public static final int FLAG_RECORDABLE = 1;
    public static final int FLAG_TRY_GLES3 = 2;
    private EGLDisplay a;
    private EGLContext b;
    private EGLConfig c;
    private int d;
    private EGLSurface e;
    private Surface f;
    private boolean g;
    
    public SelesWindowsSurface(final EGLContext eglContext, final int n) {
        this.a = EGL14.EGL_NO_DISPLAY;
        this.b = EGL14.EGL_NO_CONTEXT;
        this.c = null;
        this.d = -1;
        this.e = EGL14.EGL_NO_SURFACE;
        this.a = EGL14.eglGetDisplay(0);
        if (this.a == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("unable to get EGL14 display");
        }
        final int[] array = new int[2];
        if (!EGL14.eglInitialize(this.a, array, 0, array, 1)) {
            this.a = null;
            throw new RuntimeException("unable to initialize EGL14");
        }
        final EGLConfig a = this.a(1, 2);
        if (a == null) {
            throw new RuntimeException("Unable to find a suitable EGLConfig");
        }
        final EGLContext eglCreateContext = EGL14.eglCreateContext(this.a, a, eglContext, new int[] { 12440, 2, 12344 }, 0);
        this.a("eglCreateContext");
        this.c = a;
        this.b = eglCreateContext;
        this.d = 2;
    }
    
    private EGLConfig a(final int n, final int i) {
        int n2 = 4;
        if (i >= 3) {
            n2 |= 0x40;
        }
        final int[] array = { 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 0, 12326, 0, 12352, n2, 12344, 0, 12344 };
        if ((n & 0x1) != 0x0) {
            array[array.length - 3] = 12610;
            array[array.length - 2] = 1;
        }
        final EGLConfig[] array2 = { null };
        if (!EGL14.eglChooseConfig(this.a, array, 0, array2, 0, array2.length, new int[1], 0)) {
            TLog.w("unable to find RGB8888 / " + i + " EGLConfig", new Object[0]);
            return null;
        }
        return array2[0];
    }
    
    private void a(final String str) {
        final int eglGetError;
        if ((eglGetError = EGL14.eglGetError()) != 12288) {
            throw new RuntimeException(str + ": EGL error: 0x" + Integer.toHexString(eglGetError));
        }
    }
    
    public void attachSurface(final Surface surface, final boolean g) {
        if (this.e != EGL14.EGL_NO_SURFACE) {
            throw new IllegalStateException("surface already created");
        }
        this.e = this.a(surface);
        this.g = g;
    }
    
    private EGLSurface a(final Object obj) {
        if (!(obj instanceof Surface) && !(obj instanceof SurfaceTexture)) {
            throw new RuntimeException("invalid surface: " + obj);
        }
        final EGLSurface eglCreateWindowSurface = EGL14.eglCreateWindowSurface(this.a, this.c, obj, new int[] { 12344 }, 0);
        this.a("eglCreateWindowSurface");
        if (eglCreateWindowSurface == null) {
            throw new RuntimeException("surface was null");
        }
        return eglCreateWindowSurface;
    }
    
    public void makeCurrent() {
        if (this.a == EGL14.EGL_NO_DISPLAY) {
            TLog.d("NOTE: makeCurrent w/o display", new Object[0]);
        }
        if (!EGL14.eglMakeCurrent(this.a, this.e, this.e, this.b)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }
    
    public int getGlVersion() {
        return this.d;
    }
    
    public void setPresentationTime(final long n) {
        EGLExt.eglPresentationTimeANDROID(this.a, this.e, n);
    }
    
    public boolean swapBuffers() {
        final boolean eglSwapBuffers = EGL14.eglSwapBuffers(this.a, this.e);
        if (!eglSwapBuffers) {
            TLog.d("WARNING: swapBuffers() failed", new Object[0]);
        }
        return eglSwapBuffers;
    }
    
    public void release() {
        this.a();
        if (this.f != null) {
            if (this.g) {
                this.f.release();
            }
            this.f = null;
        }
    }
    
    private void a() {
        EGL14.eglDestroySurface(this.a, this.e);
        this.e = EGL14.EGL_NO_SURFACE;
    }
}
