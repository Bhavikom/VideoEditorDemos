// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.video.preproc.filter;

//import org.lasque.tusdk.core.seles.SelesEGLContextFactory;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import javax.microedition.khronos.egl.EGLSurface;
import android.opengl.GLSurfaceView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesEGLContextFactory;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGL10;

public class TuSDKGLContextMaker
{
    private EGL10 a;
    private EGLDisplay b;
    private EGLConfig[] c;
    private EGLConfig d;
    private EGLContext e;
    private GL10 f;
    private GLSurfaceView.EGLContextFactory g;
    private EGLSurface h;
    
    public void bindGLContext(final TuSdkSize tuSdkSize, final EGLContext eglContext) {
        final int[] array = new int[2];
        final int[] array2 = { 12375, tuSdkSize.width, 12374, tuSdkSize.height, 12344 };
        this.a = (EGL10)EGLContext.getEGL();
        this.b = this.a.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        this.a.eglInitialize(this.b, array);
        this.d = this.a();
        this.g = (GLSurfaceView.EGLContextFactory)new SelesEGLContextFactory(2, eglContext);
        this.e = this.g.createContext(this.a, this.b, this.d);
        this.h = this.a.eglCreatePbufferSurface(this.b, this.d, array2);
        this.a.eglMakeCurrent(this.b, this.h, this.h, this.e);
        this.f = (GL10)this.e.getGL();
    }
    
    private EGLConfig a() {
        final int[] array = { 12325, 0, 12326, 0, 12324, 8, 12323, 8, 12322, 8, 12321, 0, 12352, 4, 12344 };
        final int[] array2 = { 0 };
        this.a.eglChooseConfig(this.b, array, (EGLConfig[])null, 0, array2);
        final int n = (array2[0] > 0) ? array2[0] : 1;
        this.c = new EGLConfig[n];
        this.a.eglChooseConfig(this.b, array, this.c, n, array2);
        return this.c[0];
    }
    
    public void destory() {
        if (this.a != null) {
            this.a.eglMakeCurrent(this.b, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            this.a.eglDestroySurface(this.b, this.h);
            this.g.destroyContext(this.a, this.b, this.e);
            this.a.eglTerminate(this.b);
            this.b = null;
        }
    }
}
