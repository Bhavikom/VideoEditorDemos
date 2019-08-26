// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles;

//import org.lasque.tusdk.core.secret.TuSdkImageNative;
//import org.lasque.tusdk.core.utils.TLog;
import java.nio.Buffer;
import android.opengl.GLES30;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.egl.SelesEGLContext;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.TuSdkImageNative;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesEGLContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class SelesPixelBuffer
{
    private final int a = 128;
    private int[] b;
    private SelesEGLContext c;
    private int d;
    private int e;
    private TuSdkSize f;
    private int[] g;
    private int h;
    private boolean i;
    
    public int[] getPixelbuffers() {
        return this.b;
    }
    
    public SelesEGLContext getEglContext() {
        return this.c;
    }
    
    public int length() {
        return this.d;
    }
    
    public TuSdkSize getSize() {
        return this.f;
    }
    
    public void flagDestory() {
        this.i = true;
    }
    
    public SelesPixelBuffer(final TuSdkSize tuSdkSize, final int n) {
        this.d = 0;
        this.e = 0;
        this.h = 0;
        this.i = false;
        if (tuSdkSize == null || !tuSdkSize.isSize() || n < 1) {
            return;
        }
        this.f = TuSdkSize.create(tuSdkSize);
        this.c = new SelesEGLContext();
        this.e = (this.f.width * 4 + 127 & 0xFFFFFF80);
        this.d = this.e * this.f.height;
        GLES30.glGenBuffers(n, this.b = new int[n], 0);
        for (int i = 0; i < this.b.length; ++i) {
            GLES30.glBindBuffer(34962, this.b[i]);
            GLES30.glBufferData(34962, this.d, (Buffer)null, 35049);
        }
        GLES30.glBindBuffer(34962, 0);
    }
    
    public void bindPackIndex(final int i) {
        if (this.b == null || i >= this.b.length) {
            TLog.e("%s bindPackIndex faile[%d]: %s", "SelesPixelBuffer", i, this.b);
            return;
        }
        GLES30.glBindBuffer(35051, this.b[i]);
    }
    
    public void disablePackBuffer() {
        GLES30.glBindBuffer(35051, 0);
    }
    
    public void preparePackBuffer() {
        if (this.b == null || this.d < 128 || this.i) {
            return;
        }
        this.next();
        GLES30.glBindBuffer(35051, this.g[5]);
        TuSdkImageNative.glReadPixels(this.g[3], this.g[4]);
        this.disablePackBuffer();
    }
    
    public Buffer readPackBuffer() {
        if (this.b == null || this.d < 128 || this.i) {
            return null;
        }
        if (this.g == null || this.g[0] == 0) {
            return null;
        }
        GLES30.glBindBuffer(35051, this.g[6]);
        final Buffer glMapBufferRange = GLES30.glMapBufferRange(35051, 0, this.d, 1);
        if (glMapBufferRange == null) {
            TLog.w("%s readPackBuffer can not read data.", "SelesPixelBuffer");
            return null;
        }
        GLES30.glUnmapBuffer(35051);
        this.disablePackBuffer();
        return glMapBufferRange;
    }
    
    public void next() {
        if (this.i || this.b == null) {
            return;
        }
        if (this.g == null) {
            (this.g = new int[7])[0] = 0;
            this.g[1] = this.d;
            this.g[2] = this.e;
            this.g[3] = this.f.width;
            this.g[4] = this.f.height;
        }
        else {
            this.g[0] = 1;
        }
        this.h %= this.b.length;
        this.g[5] = this.b[this.h];
        this.g[6] = this.b[(this.h + 1) % this.b.length];
        ++this.h;
    }
    
    public int[] getBefferInfo() {
        return this.g;
    }
    
    public void destory() {
        if (this.i) {
            return;
        }
        this.i = true;
        SelesContext.recyclePixelbuffer(this);
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
