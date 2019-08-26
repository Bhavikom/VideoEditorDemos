// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl;

//import org.lasque.tusdk.core.seles.SelesContext;
import java.nio.Buffer;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.SelesEGLContextFactory;
//import org.lasque.tusdk.core.utils.TLog;
import java.nio.IntBuffer;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGL10;
import android.graphics.Rect;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.opengl.GLSurfaceView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesEGLContextFactory;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

public class SelesEGL10Core
{
    private GLSurfaceView.Renderer a;
    private TuSdkSize b;
    private Rect c;
    private EGL10 d;
    private EGLDisplay e;
    private EGLConfig[] f;
    private EGLConfig g;
    private EGLContext h;
    private EGLSurface i;
    private GL10 j;
    private GLSurfaceView.EGLContextFactory k;
    private IntBuffer l;
    private long m;
    
    public TuSdkSize getSize() {
        return this.b;
    }
    
    public long getThreadID() {
        return this.m;
    }
    
    public static SelesEGL10Core create(final TuSdkSize tuSdkSize) {
        return new SelesEGL10Core(tuSdkSize, null);
    }
    
    public static SelesEGL10Core create(final TuSdkSize tuSdkSize, final EGLContext eglContext) {
        return new SelesEGL10Core(tuSdkSize, eglContext);
    }
    
    private SelesEGL10Core(final TuSdkSize b, final EGLContext eglContext) {
        if (b == null || b.minSide() < 1) {
            TLog.e("SelesEGL10Core: Passed image must not be empty - it should be at least 1px tall and wide : %s", b);
            return;
        }
        this.b = b;
        this.c = new Rect(0, 0, b.width, b.height);
        final int[] array = new int[2];
        final int[] array2 = { 12375, this.b.width, 12374, this.b.height, 12344 };
        this.d = (EGL10)EGLContext.getEGL();
        this.e = this.d.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        this.d.eglInitialize(this.e, array);
        this.g = this.a();
        this.k = (GLSurfaceView.EGLContextFactory)new SelesEGLContextFactory(2, eglContext);
        this.h = this.k.createContext(this.d, this.e, this.g);
        this.i = this.d.eglCreatePbufferSurface(this.e, this.g, array2);
        this.d.eglMakeCurrent(this.e, this.i, this.i, this.h);
        this.j = (GL10)this.h.getGL();
        this.m = Thread.currentThread().getId();
    }
    
    public void setRenderer(final GLSurfaceView.Renderer a) {
        this.a = a;
        if (Thread.currentThread().getId() != this.m) {
            TLog.e("setRenderer: This thread does not own the OpenGL context.", new Object[0]);
            return;
        }
        this.a.onSurfaceCreated(this.j, this.g);
        this.a.onSurfaceChanged(this.j, this.b.width, this.b.height);
    }
    
    @Override
    protected void finalize() {
        this.destroy();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    public void destroy() {
        if (this.e == null) {
            return;
        }
        try {
            this.d.eglDestroySurface(this.e, this.i);
            this.k.destroyContext(this.d, this.e, this.h);
            this.d.eglTerminate(this.e);
            this.e = null;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private EGLConfig a() {
        final int[] array = { 12325, 0, 12326, 0, 12324, 8, 12323, 8, 12322, 8, 12321, 0, 12352, 4, 12344 };
        final int[] array2 = { 0 };
        this.d.eglChooseConfig(this.e, array, (EGLConfig[])null, 0, array2);
        final int n = (array2[0] > 0) ? array2[0] : 1;
        this.f = new EGLConfig[n];
        this.d.eglChooseConfig(this.e, array, this.f, n, array2);
        return this.f[0];
    }
    
    public Bitmap getBitmap() {
        final IntBuffer imageBuffer = this.getImageBuffer();
        if (imageBuffer == null) {
            return null;
        }
        final Bitmap bitmap = Bitmap.createBitmap(this.c.width(), this.c.height(), Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer((Buffer)imageBuffer);
        return bitmap;
    }
    
    public IntBuffer getImageBuffer() {
        if (Thread.currentThread().getId() != this.m) {
            TLog.e("getBitmap: This thread does not own the OpenGL context.", new Object[0]);
            return null;
        }
        if (this.a != null) {
            this.a.onDrawFrame(this.j);
        }
        return this.b();
    }
    
    private IntBuffer b() {
        if (this.l == null) {
            this.l = IntBuffer.allocate(this.c.width() * this.c.height());
        }
        this.l.position(0);
        this.j.glReadPixels(this.c.left, this.c.top, this.c.width(), this.c.height(), 6408, 5121, (Buffer)this.l);
        return this.l;
    }
    
    public void setOutputRect(final Rect c) {
        if (c == null || c.isEmpty()) {
            return;
        }
        if (this.c != null && this.c.equals((Object)c)) {
            return;
        }
        this.c = c;
        if (this.l != null) {
            this.l = IntBuffer.allocate(this.c.width() * this.c.height());
        }
    }
    
    public static void checkGLError(final String s) {
        final int glGetError = SelesContext.currentGL().glGetError();
        if (glGetError == 0) {
            return;
        }
        TLog.e("%s %s: checkGLError[0x%s]", s, "SelesEGL10Core", Integer.toHexString(glGetError));
    }
}
