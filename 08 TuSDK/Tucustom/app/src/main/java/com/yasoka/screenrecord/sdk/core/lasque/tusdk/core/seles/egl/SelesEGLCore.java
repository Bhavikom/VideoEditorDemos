// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl;

import android.os.Build;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.utils.TLog;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class SelesEGLCore
{
    public static final int FLAG_NONE = 0;
    public static final int FLAG_RECORDABLE = 1;
    public static final int FLAG_TRY_GLES3 = 2;
    public static final int EGL_RECORDABLE_ANDROID;
    private EGLDisplay a;
    private EGLContext b;
    private EGLConfig c;
    private int d;
    private long e;
    
    @TargetApi(26)
    private static int a() {
        return 12610;
    }
    
    public long getThreadID() {
        return this.e;
    }
    
    public SelesEGLCore(final EGLContext eglContext) {
        this(eglContext, 0);
    }
    
    public SelesEGLCore(EGLContext egl_NO_CONTEXT, final int n) {
        this.a = EGL14.EGL_NO_DISPLAY;
        this.b = EGL14.EGL_NO_CONTEXT;
        this.c = null;
        this.d = -1;
        if (this.a != EGL14.EGL_NO_DISPLAY) {
            TLog.w("%s EGL already set up", "SelesEGLCore");
            return;
        }
        if (egl_NO_CONTEXT == null) {
            egl_NO_CONTEXT = EGL14.EGL_NO_CONTEXT;
        }
        this.a = EGL14.eglGetDisplay(0);
        if (this.a == EGL14.EGL_NO_DISPLAY) {
            this.a = null;
            TLog.w("%s Unable to get EGL display", "SelesEGLCore");
            return;
        }
        final int[] array = new int[2];
        if (!EGL14.eglInitialize(this.a, array, 0, array, 1)) {
            this.a = null;
            TLog.w("%s Unable to initialize EGL", "SelesEGLCore");
            return;
        }
        boolean b = false;
        if ((n & 0x2) != 0x0) {
            b = this.a(egl_NO_CONTEXT, n, 3);
        }
        if (!b) {
            b = this.a(egl_NO_CONTEXT, n, 2);
        }
        if (!b) {
            TLog.w("%s Unable to Create ELGS Context", "SelesEGLCore");
            return;
        }
        this.e = Thread.currentThread().getId();
        EGL14.eglQueryContext(this.a, this.b, 12440, new int[1], 0);
    }
    
    private boolean a(final EGLContext eglContext, final int n, final int d) {
        final EGLConfig a = this.a(n, d);
        if (a == null) {
            return false;
        }
        final EGLContext eglCreateContext = EGL14.eglCreateContext(this.a, a, eglContext, new int[] { 12440, d, 12344 }, 0);
        if (EGL14.eglGetError() != 12288) {
            return false;
        }
        this.c = a;
        this.b = eglCreateContext;
        this.d = d;
        return true;
    }
    
    private EGLConfig a(final int n, final int i) {
        int n2 = 4;
        if (i > 2) {
            n2 |= 0x40;
        }
        final int[] array = { 12325, 0, 12326, 0, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, n2, 12344, 0, 12344 };
        if ((n & 0x1) != 0x0) {
            array[array.length - 3] = SelesEGLCore.EGL_RECORDABLE_ANDROID;
            array[array.length - 2] = 1;
        }
        final EGLConfig[] array2 = { null };
        if (!EGL14.eglChooseConfig(this.a, array, 0, array2, 0, array2.length, new int[1], 0)) {
            TLog.w("%s Unable to find find RGBA8888 | version %d EGLConfig", "SelesEGLCore", i);
            return null;
        }
        return array2[0];
    }
    
    public void destroy() {
        if (this.a == null) {
            return;
        }
        if (this.a != EGL14.EGL_NO_DISPLAY) {
            SelesContext.destroyContext(SelesContext.currentEGLContext());
            this.makeNothingCurrent();
            this._cleanEGLWhenDestory();
            EGL14.eglDestroyContext(this.a, this.b);
            EGL14.eglReleaseThread();
            EGL14.eglTerminate(this.a);
        }
        this.a = EGL14.EGL_NO_DISPLAY;
        this.b = EGL14.EGL_NO_CONTEXT;
        this.c = null;
    }
    
    protected void _cleanEGLWhenDestory() {
    }
    
    public void releaseSurface(final EGLSurface eglSurface) {
        if (this.emptyEGLDisplay() || eglSurface == null || eglSurface == EGL14.EGL_NO_SURFACE) {
            return;
        }
        EGL14.eglDestroySurface(this.a, eglSurface);
    }
    
    @Override
    protected void finalize() {
        try {
            if (this.a != EGL14.EGL_NO_DISPLAY) {
                TLog.w("%s WARNING: EglCore was not explicitly released -- state may be leaked", "SelesEGLCore");
                this.destroy();
            }
        }
        finally {
            try {
                super.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
    
    public EGLSurface createWindowSurface(final Object o) {
        if (this.emptyEGLDisplay()) {
            return null;
        }
        final EGLSurface eglCreateWindowSurface = EGL14.eglCreateWindowSurface(this.a, this.c, o, new int[] { 12344 }, 0);
        this.a("eglCreateWindowSurface");
        if (eglCreateWindowSurface == null) {
            TLog.w("%s surface was null", "SelesEGLCore");
        }
        return eglCreateWindowSurface;
    }
    
    public EGLSurface createOffscreenSurface(final int n, final int n2) {
        if (this.emptyEGLDisplay()) {
            return null;
        }
        final EGLSurface eglCreatePbufferSurface = EGL14.eglCreatePbufferSurface(this.a, this.c, new int[] { 12375, n, 12374, n2, 12344 }, 0);
        this.a("eglCreatePbufferSurface");
        if (eglCreatePbufferSurface == null) {
            TLog.w("%s surface was null", "SelesEGLCore");
        }
        return eglCreatePbufferSurface;
    }
    
    public boolean makeCurrent(final EGLSurface eglSurface) {
        return this.makeCurrent(eglSurface, eglSurface);
    }
    
    public boolean makeCurrent(final EGLSurface eglSurface, final EGLSurface eglSurface2) {
        if (this.emptyEGLDisplay()) {
            TLog.w("%s NOTE: makeCurrent w/o display EGLDisplay is empty", "SelesEGLCore");
            return false;
        }
        if (this.b == null || this.b == EGL14.EGL_NO_CONTEXT) {
            TLog.w("%s NOTE: makeCurrent w/o display EGLContext is empty", "SelesEGLCore");
            return false;
        }
        if (!EGL14.eglMakeCurrent(this.a, eglSurface, eglSurface2, this.b)) {
            TLog.w("%s eglMakeCurrent(draw,read) failed", "SelesEGLCore");
            return false;
        }
        SelesContext.createEGLContext(SelesContext.currentEGLContext());
        return true;
    }
    
    public void makeNothingCurrent() {
        if (this.emptyEGLDisplay()) {
            return;
        }
        if (!EGL14.eglMakeCurrent(this.a, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)) {
            TLog.w("%s eglMakeCurrent failed", "SelesEGLCore");
        }
    }
    
    public boolean swapBuffers(final EGLSurface eglSurface) {
        if (this.emptyEGLDisplay()) {
            TLog.w("%s swapBuffers EGLDisplay is empty", "SelesEGLCore");
            return false;
        }
        return EGL14.eglSwapBuffers(this.a, eglSurface);
    }
    
    public void setPresentationTime(final EGLSurface eglSurface, final long n) {
        EGLExt.eglPresentationTimeANDROID(this.a, eglSurface, n);
    }
    
    public boolean isCurrent(final EGLSurface eglSurface) {
        return this.b.equals((Object)EGL14.eglGetCurrentContext()) && eglSurface.equals((Object)EGL14.eglGetCurrentSurface(12377));
    }
    
    public int querySurface(final EGLSurface eglSurface, final int n) {
        final int[] array = { 0 };
        EGL14.eglQuerySurface(this.a, eglSurface, n, array, 0);
        return array[0];
    }
    
    public int getGlVersion() {
        return this.d;
    }
    
    public boolean emptyEGLDisplay() {
        return this.a == null || this.a == EGL14.EGL_NO_DISPLAY;
    }
    
    private boolean a(final String s) {
        final int eglGetError;
        if ((eglGetError = EGL14.eglGetError()) == 12288) {
            return true;
        }
        TLog.w("%s: %s checkEglError[0x%s]", "SelesEGLCore", s, Integer.toHexString(eglGetError));
        return false;
    }
    
    public static void logCurrent(final String s) {
        TLog.d("%s Current EGL (%s): display=%s, context=%s, surface=%s", "SelesEGLCore", s, EGL14.eglGetCurrentDisplay(), EGL14.eglGetCurrentContext(), EGL14.eglGetCurrentSurface(12377));
    }
    
    static {
        if (Build.VERSION.SDK_INT < 26) {
            EGL_RECORDABLE_ANDROID = 12610;
        }
        else {
            EGL_RECORDABLE_ANDROID = a();
        }
    }
}
