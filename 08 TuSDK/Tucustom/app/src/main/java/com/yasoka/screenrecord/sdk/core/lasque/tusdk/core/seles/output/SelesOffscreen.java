// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output;

import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.Rect;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesCropFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadQueue;

import javax.microedition.khronos.egl.EGLContext;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.egl.SelesEGL10Core;
//import org.lasque.tusdk.core.utils.ThreadQueue;
//import org.lasque.tusdk.core.seles.filters.SelesCropFilter;

public class SelesOffscreen extends SelesCropFilter
{
    private final ThreadQueue a;
    private SelesEGL10Core b;
    private SelesOffscreenDelegate c;
    private boolean d;
    
    public void setDelegate(final SelesOffscreenDelegate c) {
        this.c = c;
    }
    
    public boolean isWorking() {
        return this.d;
    }
    
    public void stopWork() {
        this.d = false;
    }
    
    public void resetEnabled() {
        this.d = false;
        this.setEnabled(true);
    }
    
    public void startWork() {
        if (this.isEnabled() || this.d) {
            return;
        }
        this.setEnabled(this.d = true);
    }
    
    public SelesOffscreen() {
        this.a = new ThreadQueue("com.tusdk.SelesAsyncOutput");
        this.setEnabled(false);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.a.post(new Runnable() {
            @Override
            public void run() {
                if (SelesOffscreen.this.b != null) {
                    SelesOffscreen.this.b.destroy();
                }
                SelesOffscreen.this.b = null;
            }
        });
        this.a.release();
    }
    
    private void a() {
        if (this.mInputTextureSize == null || !this.mInputTextureSize.isSize()) {
            return;
        }
        final EGLContext currentEGLContext = SelesContext.currentEGLContext();
        if (currentEGLContext == null) {
            return;
        }
        this.a.post(new Runnable() {
            @Override
            public void run() {
                SelesOffscreen.this.a(currentEGLContext);
            }
        });
    }
    
    private void a(final EGLContext eglContext) {
        this.d = true;
        if (this.b != null) {
            this.b.setOutputRect(new Rect(0, 0, this.mInputTextureSize.width, this.mInputTextureSize.height));
            return;
        }
        this.b = SelesEGL10Core.create(this.getOutputSize(), eglContext);
        this.runPendingOnDrawTasks();
    }
    
    @Override
    public void setInputSize(final TuSdkSize tuSdkSize, final int n) {
        final TuSdkSize mInputTextureSize = this.mInputTextureSize;
        super.setInputSize(tuSdkSize, n);
        if (!mInputTextureSize.equals(this.mInputTextureSize)) {
            this.a();
        }
    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        if (this.mFirstInputFramebuffer != null) {
            this.setEnabled(false);
        }
        GLES20.glFinish();
        this.d = true;
        this.a.post(new Runnable() {
            @Override
            public void run() {
                SelesOffscreen.this.asyncNewFrameReady(n, n2);
            }
        });
    }
    
    protected void asyncNewFrameReady(final long n, final int n2) {
        super.newFrameReady(n, n2);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, floatBuffer2);
        if (this.c != null) {
            this.setEnabled(this.c.onFrameRendered(this));
        }
        this.d = false;
    }
    
    public IntBuffer renderBuffer() {
        if (this.b == null) {
            return null;
        }
        return this.b.getImageBuffer();
    }
    
    public interface SelesOffscreenDelegate
    {
        boolean onFrameRendered(final SelesOffscreen p0);
    }
}
