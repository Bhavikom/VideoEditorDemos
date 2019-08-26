// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;

public class SelesEGLContext
{
    private EGLContext a;
    private long b;
    private String c;
    
    public SelesEGLContext() {
        this.a = currentEGLContext();
        this.b = Thread.currentThread().getId();
        this.c = String.format("%s-%d", this.a, this.b);
    }
    
    public EGLContext getEGLContext() {
        return this.a;
    }
    
    public long getThreadID() {
        return this.b;
    }
    
    public String getHashKey() {
        return this.c;
    }
    
    public boolean equalsCurrent() {
        return this.a.equals(currentEGLContext()) && this.b == Thread.currentThread().getId();
    }
    
    public boolean equalsCurrentThread() {
        return this.b == Thread.currentThread().getId();
    }
    
    public static boolean equalsCurrent(final EGLContext eglContext) {
        return eglContext != null && eglContext.equals(currentEGLContext());
    }
    
    public static EGLContext currentEGLContext() {
        return ((EGL10)EGLContext.getEGL()).eglGetCurrentContext();
    }
    
    public static GL10 currentGL() {
        final EGLContext currentEGLContext = currentEGLContext();
        if (currentEGLContext == null || currentEGLContext == EGL10.EGL_NO_CONTEXT) {
            return null;
        }
        return (GL10)currentEGLContext.getGL();
    }
    
    public static String currentHashKey() {
        return currentHashKey(currentEGLContext());
    }
    
    public static String currentHashKey(final EGLContext eglContext) {
        return String.format("%s-%d", eglContext, Thread.currentThread().getId());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof SelesEGLContext)) {
            return false;
        }
        final SelesEGLContext selesEGLContext = (SelesEGLContext)o;
        return this.a == selesEGLContext.a && this.b == selesEGLContext.b;
    }
    
    @Override
    public String toString() {
        final StringBuffer append = new StringBuffer("SelesEGLContext").append("{");
        append.append("EGL: ").append(this.a).append(", ");
        append.append("Thread: ").append(this.b).append(", ");
        append.append("Current EGL: ").append(currentEGLContext()).append(",");
        append.append("Current Thread: ").append(Thread.currentThread().getId()).append(",");
        append.append("}");
        return append.toString();
    }
}
