// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl;

import javax.microedition.khronos.egl.EGL10;
import android.view.Surface;
//import org.lasque.tusdk.core.utils.TLog;
import android.opengl.EGLContext;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;
import java.util.concurrent.Semaphore;
import android.os.Handler;
import android.os.HandlerThread;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class SelesVirtualDisplay
{
    private final HandlerThread a;
    private final Handler b;
    private SelesRenderer c;
    private SelesEGLCoreSingleSurface d;
    private boolean e;
    private long f;
    private boolean g;
    private boolean h;
    private volatile Semaphore i;
    
    public void setSyncRender(final boolean h) {
        this.h = h;
    }
    
    public long lastRenderTimestampNs() {
        return this.f;
    }
    
    public void setRenderer(final SelesRenderer c) {
        if (c == null) {
            return;
        }
        this.c = c;
        if (this.d == null || !this.e) {
            return;
        }
        this.postRender(new Runnable() {
            @Override
            public void run() {
                SelesVirtualDisplay.this.b();
                SelesVirtualDisplay.this.c();
            }
        });
    }
    
    public SelesVirtualDisplay() {
        this.e = false;
        this.f = 0L;
        this.g = false;
        this.h = false;
        this.i = new Semaphore(0);
        (this.a = new HandlerThread("com.tutuclould.SelesVirtualDisplay")).start();
        this.b = new Handler(this.a.getLooper());
    }
    
    public void release() {
        if (this.g) {
            return;
        }
        this.g = true;
        this.postRender(new Runnable() {
            @Override
            public void run() {
                SelesVirtualDisplay.this.a();
            }
        });
        this.e = false;
        this.a.quitSafely();
    }
    
    public void clearTask() {
        this.b.removeMessages(0);
    }
    
    private void a() {
        if (this.c != null) {
            this.c.onSurfaceDestory(this.d());
        }
        if (this.d != null) {
            this.d.destroy();
            this.d = null;
        }
    }
    
    private void b() {
        if (this.c == null || this.d == null) {
            return;
        }
        final GL10 d = this.d();
        if (d == null) {
            return;
        }
        this.c.onSurfaceCreated(d, (EGLConfig)null);
    }
    
    private void c() {
        if (this.c == null || this.d == null || !this.e) {
            return;
        }
        final TuSdkSize surfaceSize = this.d.getSurfaceSize();
        if (surfaceSize == null) {
            return;
        }
        final GL10 d = this.d();
        if (d == null) {
            return;
        }
        this.c.onSurfaceChanged(d, surfaceSize.width, surfaceSize.height);
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.release();
        super.finalize();
    }
    
    public void buildWindowContext(final EGLContext eglContext) {
        this.postRender(new Runnable() {
            @Override
            public void run() {
                SelesVirtualDisplay.this.a(eglContext);
            }
        });
    }
    
    private void a(final EGLContext eglContext) {
        if (this.d != null) {
            TLog.w("%s buildWindowContext exist", "SelesVirtualDisplay");
            return;
        }
        this.d = new SelesEGLCoreSingleSurface(eglContext, 1);
    }
    
    public void attachWindowSurface(final Surface surface, final boolean b) {
        this.postRender(new Runnable() {
            @Override
            public void run() {
                if (SelesVirtualDisplay.this.d == null) {
                    SelesVirtualDisplay.this.a(null);
                }
                if (SelesVirtualDisplay.this.e) {
                    TLog.w("%s attachWindowSurface Surface can not duplicate attach", "SelesVirtualDisplay");
                    return;
                }
                SelesVirtualDisplay.this.e = SelesVirtualDisplay.this.d.attachWindowSurface(surface, b);
                SelesVirtualDisplay.this.b();
                SelesVirtualDisplay.this.c();
            }
        });
    }
    
    public void buildOffsetContext(final EGLContext eglContext) {
        this.postRender(new Runnable() {
            @Override
            public void run() {
                SelesVirtualDisplay.this.b(eglContext);
            }
        });
    }
    
    private void b(final EGLContext eglContext) {
        if (this.d != null) {
            TLog.w("%s buildOffsetContext exist", "SelesVirtualDisplay");
            return;
        }
        this.d = new SelesEGLCoreSingleSurface(eglContext);
    }
    
    public void attachOffscreenSurface(final int n, final int n2) {
        this.postRender(new Runnable() {
            @Override
            public void run() {
                if (SelesVirtualDisplay.this.d == null) {
                    SelesVirtualDisplay.this.b(null);
                }
                if (SelesVirtualDisplay.this.e) {
                    TLog.w("%s attachOffscreenSurface Surface can not duplicate attach", "SelesVirtualDisplay");
                    return;
                }
                SelesVirtualDisplay.this.e = SelesVirtualDisplay.this.d.attachOffscreenSurface(n, n2);
                SelesVirtualDisplay.this.b();
                SelesVirtualDisplay.this.c();
            }
        });
    }
    
    public boolean requestRender() {
        return this.requestRender(System.nanoTime());
    }
    
    public boolean requestRender(final long n) {
        return this.requestRender(n, null);
    }
    
    public boolean requestRender(final long n, final Runnable runnable) {
        return this.requestRender(n, runnable, null);
    }
    
    public boolean requestRender(final long f, final Runnable runnable, final Runnable runnable2) {
        if (this.c == null) {
            TLog.w("%s requestRender need setRenderer first", "SelesVirtualDisplay");
            return false;
        }
        if (this.d == null) {
            TLog.w("%s requestRender need buildContext", "SelesVirtualDisplay");
            return false;
        }
        if (!this.e) {
            TLog.w("%s requestRender need Surface Attached", "SelesVirtualDisplay");
            return false;
        }
        this.f = f;
        this.postRender(runnable);
        this.postRender(new Runnable() {
            @Override
            public void run() {
                final GL10 f = SelesVirtualDisplay.this.d();
                if (f == null) {
                    return;
                }
                SelesVirtualDisplay.this.c.onDrawFrame(f);
                if (SelesVirtualDisplay.this.h) {
                    SelesVirtualDisplay.this.i.release();
                }
            }
        });
        if (this.h) {
            try {
                this.i.acquire();
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        this.postRender(runnable2);
        return true;
    }
    
    public void setPresentationTime(final long presentationTime) {
        if (this.d == null) {
            TLog.w("%s setPresentationTime EglCore is empty", "SelesVirtualDisplay");
            return;
        }
        this.d.setPresentationTime(presentationTime);
    }
    
    public boolean swapBuffers() {
        if (this.d == null) {
            TLog.w("%s swapBuffers EglCore is empty", "SelesVirtualDisplay");
            return false;
        }
        return this.d.swapBuffers();
    }
    
    public boolean swapBuffers(final long presentationTime) {
        this.setPresentationTime(presentationTime);
        return this.swapBuffers();
    }
    
    public void requestSwapBuffers(final long n) {
        this.postRender(new Runnable() {
            @Override
            public void run() {
                SelesVirtualDisplay.this.swapBuffers(n);
            }
        });
    }
    
    public void postRender(final Runnable runnable) {
        if (runnable == null || !this.a.isAlive()) {
            return;
        }
        this.b.post(runnable);
    }
    
    private GL10 d() {
        final EGL10 egl10 = (EGL10)javax.microedition.khronos.egl.EGLContext.getEGL();
        if (egl10 == null) {
            return null;
        }
        final javax.microedition.khronos.egl.EGLContext eglGetCurrentContext = egl10.eglGetCurrentContext();
        if (eglGetCurrentContext == null) {
            return null;
        }
        return (GL10)eglGetCurrentContext.getGL();
    }
}
