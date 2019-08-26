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

public class TuSDKLivePullInFilter extends SelesFilter implements SelesParameters.FilterParameterInterface
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
    private float r;
    int[] a;
    private static float s;
    
    public TuSDKLivePullInFilter() {
        super("-strans", "-spullin");
        this.l = new float[] { -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f };
        this.m = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        this.a = new int[] { 0, 1, 2, 0, 3, 2 };
        this.q = TuSDKLivePullInFilter.s;
    }
    
    public TuSDKLivePullInFilter(final PullInDirection pullInDirection) {
        this();
        this.a(pullInDirection);
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.p = 0L;
        this.n = 0L;
        this.f = this.mFilterProgram.uniformIndex("animationPercent");
        this.mFilterInputTextureUniform = this.mFilterProgram.uniformIndex("inputImageTexture");
        this.g = this.mFilterProgram.uniformIndex("inputImageTexture2");
        this.h = this.mFilterProgram.uniformIndex("animationDirection");
        (this.e = ByteBuffer.allocateDirect(this.a.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer().put(this.a)).position(0);
        this.initializeAttributes();
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("duration", TuSDKLivePullInFilter.s, TuSDKLivePullInFilter.s, Float.MAX_VALUE);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        super.submitFilterArg(filterArg);
        if (filterArg.equalsKey("duration")) {
            this.a(filterArg.getValue());
        }
    }
    
    private void a(final PullInDirection pullInDirection) {
        this.r = pullInDirection.getDirection();
    }
    
    private void a(final float a) {
        this.q = Math.max(a, TuSDKLivePullInFilter.s);
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
        this.setFloat(this.r, this.h, this.mFilterProgram);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, this.i.getTexture());
        if (this.r == PullInDirection.TOP.getDirection() || this.r == PullInDirection.RIGHT.getDirection()) {
            GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
        }
        else {
            GLES20.glUniform1i(this.g, 2);
        }
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
        if (this.r == PullInDirection.TOP.getDirection() || this.r == PullInDirection.RIGHT.getDirection()) {
            GLES20.glUniform1i(this.g, 3);
        }
        else {
            GLES20.glUniform1i(this.mFilterInputTextureUniform, 3);
        }
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 4, 5126, false, 0, (Buffer)this.j);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, true, 0, (Buffer)this.k);
        GLES20.glVertexAttribPointer(this.c, 2, 5126, true, 0, (Buffer)this.d);
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
            if (this.r == PullInDirection.RIGHT.getDirection() || this.r == PullInDirection.TOP.getDirection()) {
                this.o = 0.0f;
            }
            else {
                this.o = 1.0f;
            }
        }
        else if (this.r == PullInDirection.RIGHT.getDirection() || this.r == PullInDirection.TOP.getDirection()) {
            this.o = (this.n - this.p) / this.q;
            if (this.o > 1.0f) {
                this.o = 1.0f;
            }
        }
        else {
            this.o = (this.q - (this.n - this.p)) / this.q;
            if (this.o < 0.0f) {
                this.o = 0.0f;
            }
        }
        this.o = Math.abs(this.o);
        this.o = (float)Math.sin(this.o * 1.5707963267948966);
        final float[] array = { -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f };
        final float[] array2 = { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        final float[] array3 = { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        if (this.r == PullInDirection.RIGHT.getDirection() || this.r == PullInDirection.LEFT.getDirection()) {
            final float[] array4 = array2;
            final int n = 0;
            array4[n] += this.o;
            final float[] array5 = array2;
            final int n2 = 2;
            array5[n2] += this.o;
            final float[] array6 = array2;
            final int n3 = 4;
            array6[n3] += this.o;
            final float[] array7 = array2;
            final int n4 = 6;
            array7[n4] += this.o;
            final float[] array8 = array3;
            final int n5 = 0;
            array8[n5] -= 1.0f - this.o;
            final float[] array9 = array3;
            final int n6 = 2;
            array9[n6] -= 1.0f - this.o;
            final float[] array10 = array3;
            final int n7 = 4;
            array10[n7] -= 1.0f - this.o;
            final float[] array11 = array3;
            final int n8 = 6;
            array11[n8] -= 1.0f - this.o;
        }
        else {
            final float[] array12 = array2;
            final int n9 = 1;
            array12[n9] += this.o;
            final float[] array13 = array2;
            final int n10 = 3;
            array13[n10] += this.o;
            final float[] array14 = array2;
            final int n11 = 5;
            array14[n11] += this.o;
            final float[] array15 = array2;
            final int n12 = 7;
            array15[n12] += this.o;
            final float[] array16 = array3;
            final int n13 = 1;
            array16[n13] -= 1.0f - this.o;
            final float[] array17 = array3;
            final int n14 = 3;
            array17[n14] -= 1.0f - this.o;
            final float[] array18 = array3;
            final int n15 = 5;
            array18[n15] -= 1.0f - this.o;
            final float[] array19 = array3;
            final int n16 = 7;
            array19[n16] -= 1.0f - this.o;
        }
        this.j = SelesFilter.buildBuffer(array);
        this.k = SelesFilter.buildBuffer(array2);
        this.d = SelesFilter.buildBuffer(array3);
    }
    
    static {
        TuSDKLivePullInFilter.b = 6;
        TuSDKLivePullInFilter.s = 1.0E9f;
    }
    
    public enum PullInDirection
    {
        BOTTOM(2.0f), 
        TOP(2.5f), 
        LEFT(1.0f), 
        RIGHT(1.5f);
        
        public float mDirection;
        
        private PullInDirection(final float mDirection) {
            this.mDirection = mDirection;
        }
        
        public float getDirection() {
            return this.mDirection;
        }
    }
}
