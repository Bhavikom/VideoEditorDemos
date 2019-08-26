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

public class TuSDKLiveFlashLightFilter extends SelesFilter implements SelesParameters.FilterParameterInterface
{
    private static int b;
    private int c;
    private FloatBuffer d;
    private IntBuffer e;
    private int f;
    private int g;
    private int h;
    private SelesFramebuffer i;
    private FloatBuffer j;
    private FloatBuffer k;
    private float[] l;
    private float[] m;
    private long n;
    private float o;
    private long p;
    private float q;
    int[] a;
    private float r;
    private float s;
    private float t;
    private static float u;
    
    public TuSDKLiveFlashLightFilter() {
        super("-strans", "-sflash");
        this.l = new float[] { -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f };
        this.m = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        this.a = new int[] { 0, 1, 2, 0, 3, 2 };
        this.r = 0.0f;
        this.s = 0.0f;
        this.t = 0.0f;
        this.j = SelesFilter.buildBuffer(this.l);
        this.k = SelesFilter.buildBuffer(this.m);
        this.d = SelesFilter.buildBuffer(this.m);
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.o = 0.0f;
        this.p = 0L;
        this.n = 0L;
        this.f = this.mFilterProgram.uniformIndex("animationPercent");
        this.mFilterInputTextureUniform = this.mFilterProgram.uniformIndex("inputImageTexture");
        this.g = this.mFilterProgram.uniformIndex("inputImageTexture2");
        this.h = this.mFilterProgram.uniformIndex("mixColor");
        (this.e = ByteBuffer.allocateDirect(this.a.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer().put(this.a)).position(0);
        this.initializeAttributes();
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("duration", TuSDKLiveFlashLightFilter.u, TuSDKLiveFlashLightFilter.u, Float.MAX_VALUE);
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
        this.q = Math.max(a, TuSDKLiveFlashLightFilter.u);
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
        this.setFloat(Math.abs(this.o), this.f, this.mFilterProgram);
        this.setFloatVec3(new float[] { this.r, this.s, this.t }, "mixColor");
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, this.i.getTexture());
        GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
        GLES20.glUniform1i(this.g, 3);
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 4, 5126, false, 0, (Buffer)this.j);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)this.k);
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
        if (selesFramebuffer != null && (this.i == null || !this.i.getSize().equals(selesFramebuffer.getSize()))) {
            (this.i = selesFramebuffer).lock();
            if (this.mFirstInputFramebuffer == null || this.mFirstInputFramebuffer.getSize().equals(selesFramebuffer.getSize())) {
                return;
            }
        }
        if (selesFramebuffer != null) {
            (this.mFirstInputFramebuffer = selesFramebuffer).lock();
        }
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long n) {
        super.informTargetsAboutNewFrame(this.n = n);
    }
    
    @Override
    protected void inputFramebufferUnlock() {
        super.inputFramebufferUnlock();
    }
    
    private void a() {
        if (this.p == 0L) {
            this.p = this.n;
            this.o = 0.0f;
        }
        else {
            this.o = Math.abs(this.n - this.p) / this.q;
            if (this.o > 1.0f) {
                this.o = 1.0f;
            }
            else if (this.o < 0.0f) {
                this.o = 0.0f;
            }
        }
    }
    
    static {
        TuSDKLiveFlashLightFilter.b = 6;
        TuSDKLiveFlashLightFilter.u = 1.0E9f;
    }
}
