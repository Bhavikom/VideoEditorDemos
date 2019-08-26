// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles;

import java.util.Iterator;
//import org.lasque.tusdk.core.utils.TLog;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.utils.ArrayHelper;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ArrayHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class SelesEGLBufferCache
{
    private final List<SelesVertexbuffer> a;
    private final List<SelesPixelBuffer> b;
    private final List<Integer> c;
    
    public SelesEGLBufferCache() {
        this.a = new ArrayList<SelesVertexbuffer>();
        this.b = new ArrayList<SelesPixelBuffer>();
        this.c = new ArrayList<Integer>();
    }
    
    public SelesVertexbuffer fetchVertexbuffer(final FloatBuffer floatBuffer) {
        this.clearRecycle();
        if (floatBuffer == null) {
            return null;
        }
        final SelesVertexbuffer selesVertexbuffer = new SelesVertexbuffer(floatBuffer);
        this.a.add(selesVertexbuffer);
        return selesVertexbuffer;
    }
    
    public void recycleVertexbuffer(final SelesVertexbuffer selesVertexbuffer) {
        if (selesVertexbuffer == null) {
            return;
        }
        this.a.remove(selesVertexbuffer);
        this.a(selesVertexbuffer);
        if (selesVertexbuffer.getEglContext().equalsCurrent()) {
            this.clearRecycle();
        }
    }
    
    public SelesPixelBuffer fetchPixelBuffer(final TuSdkSize tuSdkSize, final int n) {
        this.clearRecycle();
        if (tuSdkSize == null || !tuSdkSize.isSize() || n < 1) {
            return null;
        }
        final SelesPixelBuffer selesPixelBuffer = new SelesPixelBuffer(tuSdkSize, n);
        this.b.add(selesPixelBuffer);
        return selesPixelBuffer;
    }
    
    public void recyclePixelbuffer(final SelesPixelBuffer selesPixelBuffer) {
        if (selesPixelBuffer == null) {
            return;
        }
        this.b.remove(selesPixelBuffer);
        this.a(selesPixelBuffer);
        if (selesPixelBuffer.getEglContext().equalsCurrent()) {
            this.clearRecycle();
        }
    }
    
    private void a(final SelesVertexbuffer selesVertexbuffer) {
        if (selesVertexbuffer == null) {
            return;
        }
        selesVertexbuffer.flagDestory();
        if (selesVertexbuffer.getVertexbuffer() == 0 || this.c.contains(selesVertexbuffer.getVertexbuffer())) {
            return;
        }
        this.c.add(selesVertexbuffer.getVertexbuffer());
    }
    
    private void a(final SelesPixelBuffer selesPixelBuffer) {
        if (selesPixelBuffer == null) {
            return;
        }
        selesPixelBuffer.flagDestory();
        if (selesPixelBuffer.getPixelbuffers() == null) {
            return;
        }
        for (int i = 0; i < selesPixelBuffer.getPixelbuffers().length; ++i) {
            if (this.c.contains(selesPixelBuffer.getPixelbuffers()[i])) {
                this.c.add(selesPixelBuffer.getPixelbuffers()[i]);
            }
        }
    }
    
    public void clearRecycle() {
        if (this.c.size() < 1) {
            return;
        }
        final int[] intArray = ArrayHelper.toIntArray(this.c);
        this.c.clear();
        GLES20.glDeleteBuffers(intArray.length, intArray, 0);
    }
    
    public void destory() {
        final Iterator<SelesVertexbuffer> iterator = this.a.iterator();
        while (iterator.hasNext()) {
            this.a(iterator.next());
        }
        this.a.clear();
        final Iterator<SelesPixelBuffer> iterator2 = this.b.iterator();
        while (iterator2.hasNext()) {
            this.a(iterator2.next());
        }
        this.b.clear();
        this.clearRecycle();
        TLog.dump("%s destory() %s|%s", this.getClass().getSimpleName(), this, SelesContext.currentEGLContext());
    }
}
