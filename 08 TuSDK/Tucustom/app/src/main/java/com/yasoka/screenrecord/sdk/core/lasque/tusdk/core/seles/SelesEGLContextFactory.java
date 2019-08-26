// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles;

import android.os.Build;
//import org.lasque.tusdk.core.utils.TLog;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGL10;
import android.annotation.TargetApi;
import javax.microedition.khronos.egl.EGLContext;
import android.opengl.GLSurfaceView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

public class SelesEGLContextFactory implements GLSurfaceView.EGLContextFactory
{
    public static final int EGL_CONTEXT_CLIENT_VERSION;
    private int a;
    private EGLContext b;
    
    @TargetApi(17)
    private static int a() {
        return 12440;
    }
    
    public SelesEGLContextFactory(final int n) {
        this(n, EGL10.EGL_NO_CONTEXT);
    }
    
    public SelesEGLContextFactory(final int a, final EGLContext b) {
        this.b = EGL10.EGL_NO_CONTEXT;
        this.a = a;
        if (b != null) {
            this.b = b;
        }
    }
    
    public EGLContext createContext(final EGL10 egl10, final EGLDisplay eglDisplay, final EGLConfig eglConfig) {
        final int[] array = { SelesEGLContextFactory.EGL_CONTEXT_CLIENT_VERSION, this.a, 12344 };
        final EGLContext eglCreateContext = egl10.eglCreateContext(eglDisplay, eglConfig, this.b, (int[])((this.a != 0) ? array : null));
        if (egl10.eglGetError() != 12288) {
            return null;
        }
        SelesContext.createEGLContext(eglCreateContext);
        return eglCreateContext;
    }
    
    public void destroyContext(final EGL10 egl10, final EGLDisplay eglDisplay, final EGLContext eglContext) {
        if (!egl10.eglQueryContext(eglDisplay, eglContext, 12440, new int[1])) {
            return;
        }
        if (egl10.eglGetError() != 12288) {
            return;
        }
        egl10.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, eglContext);
        SelesContext.destroyContext(eglContext);
        egl10.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        if (!egl10.eglDestroyContext(eglDisplay, eglContext)) {
            TLog.e("SelesEGLContextFactory - tid: %s | display: %s | context: %s | eglDestroyContex: %s", Thread.currentThread().getId(), eglDisplay, eglContext, egl10.eglGetError());
        }
    }
    
    static {
        if (Build.VERSION.SDK_INT < 17) {
            EGL_CONTEXT_CLIENT_VERSION = 12440;
        }
        else {
            EGL_CONTEXT_CLIENT_VERSION = a();
        }
    }
}
