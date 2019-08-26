// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles;

import java.nio.Buffer;
import android.opengl.GLES20;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesEGLContext;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.egl.SelesEGLContext;

public class SelesVertexbuffer
{
    private int a;
    private SelesEGLContext b;
    private FloatBuffer c;
    private boolean d;
    
    public int getVertexbuffer() {
        return this.a;
    }
    
    public SelesEGLContext getEglContext() {
        return this.b;
    }
    
    public int length() {
        if (this.c == null) {
            return 0;
        }
        return this.c.limit();
    }
    
    public void flagDestory() {
        this.d = true;
    }
    
    public SelesVertexbuffer(final FloatBuffer c) {
        this.a = 0;
        this.d = false;
        if (c == null) {
            return;
        }
        this.c = c;
        this.b = new SelesEGLContext();
        final int[] array = { 0 };
        GLES20.glGenBuffers(1, array, 0);
        GLES20.glBindBuffer(34962, this.a = array[0]);
        GLES20.glBufferData(34962, this.c.limit() * 4, (Buffer)c, 35048);
    }
    
    public void fresh(final int n, final int n2) {
        this.fresh(n, n2, this.c);
    }
    
    public void fresh(final int n, final int n2, FloatBuffer c) {
        if (c == null) {
            c = this.c;
        }
        GLES20.glBindBuffer(34962, this.a);
        GLES20.glBufferSubData(34962, n * 4, n2 * 4, (Buffer)c);
    }
    
    public void activateVertexbuffer() {
        GLES20.glBindBuffer(34962, this.a);
    }
    
    public void disableVertexbuffer() {
        GLES20.glBindBuffer(34962, 0);
    }
    
    public void destory() {
        if (this.d) {
            return;
        }
        this.d = true;
        SelesContext.recycleVertexbuffer(this);
    }
    
    @Override
    protected void finalize() {
        this.destory();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
