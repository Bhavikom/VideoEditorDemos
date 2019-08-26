// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker;

import android.os.Build;
import java.util.concurrent.Executors;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import javax.microedition.khronos.egl.EGLContext;
import java.util.concurrent.ExecutorService;
import android.os.Handler;
import android.os.HandlerThread;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.egl.SelesEGL10Core;

public class LiveStickerLoader
{
    private SelesEGL10Core a;
    private HandlerThread b;
    private Handler c;
    private ExecutorService d;
    
    public LiveStickerLoader(final EGLContext eglContext) {
        (this.b = new HandlerThread("com.tusdk.asyncLiveStickerLoader")).start();
        this.c = new Handler(this.b.getLooper());
        this.a(eglContext);
    }
    
    private void a(final EGLContext eglContext) {
        if (this.a != null) {
            return;
        }
        this.c.post((Runnable)new Runnable() {
            @Override
            public void run() {
                LiveStickerLoader.this.a = SelesEGL10Core.create(TuSdkSize.create(1, 1), eglContext);
            }
        });
    }
    
    public void finalize() {
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    public void loadImage(final Runnable runnable) {
        if (this.d == null) {
            this.d = Executors.newFixedThreadPool(1);
        }
        this.d.execute(runnable);
    }
    
    public void uploadTexture(final Runnable runnable) {
        if (this.c != null) {
            this.c.post(runnable);
        }
    }
    
    public void destroy() {
        if (this.d != null) {
            this.d.shutdown();
            this.d = null;
        }
        if (this.b != null) {
            this.c.post((Runnable)new Runnable() {
                private SelesEGL10Core b = LiveStickerLoader.this.a;
                
                @Override
                public void run() {
                    if (this.b != null) {
                        this.b.destroy();
                        this.b = null;
                    }
                }
            });
            if (Build.VERSION.SDK_INT < 18) {
                this.b.quit();
            }
            else {
                this.b.quitSafely();
            }
            this.b = null;
        }
    }
}
