// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.trans;

import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKLiveSpreadInFilter extends SelesFilter implements SelesParameters.FilterParameterInterface
{
    private static int b;
    private int c;
    private FloatBuffer d;
    private IntBuffer e;
    private int f;
    private int g;
    private SelesFramebuffer h;
    private FloatBuffer i;
    private FloatBuffer j;
    private float[] k;
    private float[] l;
    private long m;
    private float n;
    private long o;
    private float p;
    int[] a;
    private static float q;
    
    public TuSDKLiveSpreadInFilter() {
        super("-strans", "-sspread");
        this.k = new float[] { -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f };
        this.l = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        this.a = new int[] { 0, 1, 2, 0, 3, 2 };
        this.p = TuSDKLiveSpreadInFilter.q;
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.n = 0.0f;
        this.o = 0L;
        this.m = 0L;
        this.f = this.mFilterProgram.uniformIndex("animationPercent");
        this.mFilterInputTextureUniform = this.mFilterProgram.uniformIndex("inputImageTexture");
        this.g = this.mFilterProgram.uniformIndex("inputImageTexture2");
        (this.e = ByteBuffer.allocateDirect(this.a.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer().put(this.a)).position(0);
        this.initializeAttributes();
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("duration", TuSDKLiveSpreadInFilter.q, TuSDKLiveSpreadInFilter.q, Float.MAX_VALUE);
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
        this.p = Math.max(a, TuSDKLiveSpreadInFilter.q);
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
        this.setFloat(Math.abs(this.n), this.f, this.mFilterProgram);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
        GLES20.glUniform1i(this.g, 2);
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, this.h.getTexture());
        GLES20.glUniform1i(this.mFilterInputTextureUniform, 3);
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 4, 5126, false, 0, (Buffer)this.i);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)this.j);
        GLES20.glVertexAttribPointer(this.c, 2, 5126, false, 0, (Buffer)this.d);
        GLES20.glDrawElements(4, this.e.limit(), 5125, (Buffer)this.e);
        this.inputFramebufferUnlock();
        this.cacaptureImageBuffer();
    }
    
    @Override
    protected void initializeAttributes() {
        super.initializeAttributes();
        this.mFilterProgram.addAttribute("inputTexture2Coordinate");
        GLES20.glEnableVertexAttribArray(this.c = this.mFilterProgram.attributeIndex("inputTexture2Coordinate"));
    }
    
    @Override
    public void setInputFramebuffer(final SelesFramebuffer selesFramebuffer, final int n) {
        if (selesFramebuffer != null && (this.h == null || !this.h.getSize().equals(selesFramebuffer.getSize()))) {
            (this.h = selesFramebuffer).lock();
            if (this.mFirstInputFramebuffer == null || this.mFirstInputFramebuffer.getSize().equals(selesFramebuffer.getSize())) {
                return;
            }
        }
        if (selesFramebuffer != null) {
            (this.mFirstInputFramebuffer = selesFramebuffer).lock();
        }
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long m) {
        super.informTargetsAboutNewFrame(this.m = m);
    }
    
    @Override
    protected void inputFramebufferUnlock() {
        super.inputFramebufferUnlock();
    }
    
    private void a() {
        if (this.o == 0L) {
            this.o = this.m;
            this.n = 0.0f;
        }
        else {
            this.n = Math.abs(this.m - this.o) / this.p;
            if (this.n > 1.0f) {
                this.n = 1.0f;
            }
            else if (this.n < 0.0f) {
                this.n = 0.0f;
            }
        }
        final float[] array = { -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f };
        final float[] array2 = { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        final float[] array4;
        final float[] array3 = array4 = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        final int n = 0;
        array4[n] += (float)(0.5 * (1.0 - Math.abs(this.n)));
        final float[] array5 = array3;
        final int n2 = 6;
        array5[n2] += (float)(0.5 * (1.0 - Math.abs(this.n)));
        this.i = SelesFilter.buildBuffer(array);
        this.j = SelesFilter.buildBuffer(array2);
        this.d = SelesFilter.buildBuffer(array3);
    }
    
    static {
        TuSDKLiveSpreadInFilter.b = 6;
        TuSDKLiveSpreadInFilter.q = 1.0E9f;
    }
}
