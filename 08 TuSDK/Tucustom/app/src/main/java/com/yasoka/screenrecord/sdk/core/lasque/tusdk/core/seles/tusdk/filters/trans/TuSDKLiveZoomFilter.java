// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.trans;

import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKLiveZoomFilter extends SelesFilter implements SelesParameters.FilterParameterInterface
{
    private static int b;
    private IntBuffer c;
    private FloatBuffer d;
    private FloatBuffer e;
    private float[] f;
    private float[] g;
    private long h;
    private long i;
    private float j;
    private float k;
    private float l;
    int[] a;
    private static float m;
    
    public TuSDKLiveZoomFilter() {
        this.f = new float[] { -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f };
        this.g = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        this.a = new int[] { 0, 1, 2, 0, 3, 2 };
        this.j = TuSDKLiveZoomFilter.m;
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.i = 0L;
        this.h = 0L;
        this.k = 0.15f;
        this.l = 0.0f;
        (this.c = ByteBuffer.allocateDirect(this.a.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer().put(this.a)).position(0);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        this.runPendingOnDrawTasks();
        if (this.isPreventRendering()) {
            this.inputFramebufferUnlock();
            return;
        }
        SelesContext.setActiveShaderProgram(this.mFilterProgram);
        (this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, this.sizeOfFBO(), this.getOutputTextureOptions())).activateFramebuffer();
        if (this.mUsingNextFrameForImageCapture) {
            this.mOutputFramebuffer.lock();
        }
        this.a();
        this.setUniformsForProgramAtIndex(0);
        GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
        GLES20.glClear(16384);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
        GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, (Buffer)this.d);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)this.e);
        GLES20.glDrawElements(4, this.c.limit(), 5125, (Buffer)this.c);
        this.inputFramebufferUnlock();
        this.cacaptureImageBuffer();
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("duration", TuSDKLiveZoomFilter.m, TuSDKLiveZoomFilter.m, Float.MAX_VALUE);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        super.submitFilterArg(filterArg);
        if (filterArg.equalsKey("duration")) {
            this.a(filterArg.getValue());
        }
    }
    
    private void a(final float a) {
        this.j = Math.max(a, TuSDKLiveZoomFilter.m);
    }
    
    @Override
    protected void initializeAttributes() {
        super.initializeAttributes();
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long h) {
        super.informTargetsAboutNewFrame(this.h = h);
    }
    
    private void a() {
        this.l = 0.0f;
        if (this.i == 0L) {
            this.i = this.h;
        }
        else if (this.h > this.i + this.j) {
            this.l = 0.0f;
        }
        else {
            this.l = this.k * ((this.h - this.i) / this.j);
            this.l = Math.abs(this.l);
            this.l = ((this.l > this.k) ? this.k : this.l);
        }
        final float[] array = { -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f };
        final float[] array3;
        final float[] array2 = array3 = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        final int n = 0;
        array3[n] += this.l;
        final float[] array4 = array2;
        final int n2 = 1;
        array4[n2] += this.l;
        final float[] array5 = array2;
        final int n3 = 2;
        array5[n3] -= this.l;
        final float[] array6 = array2;
        final int n4 = 3;
        array6[n4] += this.l;
        final float[] array7 = array2;
        final int n5 = 4;
        array7[n5] -= this.l;
        final float[] array8 = array2;
        final int n6 = 5;
        array8[n6] -= this.l;
        final float[] array9 = array2;
        final int n7 = 6;
        array9[n7] += this.l;
        final float[] array10 = array2;
        final int n8 = 7;
        array10[n8] -= this.l;
        this.d = SelesFilter.buildBuffer(array);
        this.e = SelesFilter.buildBuffer(array2);
    }
    
    static {
        TuSDKLiveZoomFilter.b = 6;
        TuSDKLiveZoomFilter.m = 1.0E9f;
    }
}
