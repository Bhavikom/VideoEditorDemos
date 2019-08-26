// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.trans;

import android.opengl.Matrix;
import java.nio.Buffer;
//import org.lasque.tusdk.core.seles.SelesContext;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import android.opengl.GLES20;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
import java.nio.IntBuffer;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKLiveFlipFilter extends SelesFilter implements SelesParameters.FilterParameterInterface
{
    private static int b;
    private int c;
    private IntBuffer d;
    private int e;
    private SelesFramebuffer f;
    private FloatBuffer g;
    private FloatBuffer h;
    private FloatBuffer i;
    private float[] j;
    private float[] k;
    private long l;
    private float m;
    private long n;
    private float o;
    int[] a;
    private static float p;
    
    public TuSDKLiveFlipFilter() {
        super("-svflyin", "-sflip");
        this.j = new float[] { -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f };
        this.k = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        this.a = new int[] { 0, 1, 2, 0, 3, 2 };
        this.o = TuSDKLiveFlipFilter.p;
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.m = 0.0f;
        this.n = 0L;
        this.l = 0L;
        this.mFilterInputTextureUniform = this.mFilterProgram.uniformIndex("inputImageTexture");
        this.e = this.mFilterProgram.uniformIndex("inputImageTexture2");
        GLES20.glEnableVertexAttribArray(this.c = this.mFilterProgram.attributeIndex("texIndex"));
        (this.d = ByteBuffer.allocateDirect(this.a.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer().put(this.a)).position(0);
        this.initializeAttributes();
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("duration", TuSDKLiveFlipFilter.p, TuSDKLiveFlipFilter.p, Float.MAX_VALUE);
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
        this.o = Math.max(a, TuSDKLiveFlipFilter.p);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        this.runPendingOnDrawTasks();
        if (this.isPreventRendering()) {
            this.inputFramebufferUnlock();
            this.f.unlock();
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
        GLES20.glUniform1i(this.e, 2);
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
        GLES20.glUniform1i(this.mFilterInputTextureUniform, 3);
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 4, 5126, false, 0, (Buffer)this.g);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)this.h);
        GLES20.glVertexAttribPointer(this.c, 1, 5126, false, 0, (Buffer)this.i);
        GLES20.glDrawElements(4, this.d.limit(), 5125, (Buffer)this.d);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, 0);
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, 0);
        this.inputFramebufferUnlock();
        this.cacaptureImageBuffer();
    }
    
    @Override
    protected void initializeAttributes() {
        super.initializeAttributes();
    }
    
    @Override
    public void setInputFramebuffer(final SelesFramebuffer selesFramebuffer, final int n) {
        if (selesFramebuffer != null && this.f == null) {
            (this.f = selesFramebuffer).lock();
            return;
        }
        if (selesFramebuffer != null) {
            (this.mFirstInputFramebuffer = selesFramebuffer).lock();
        }
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long l) {
        super.informTargetsAboutNewFrame(this.l = l);
    }
    
    @Override
    protected void inputFramebufferUnlock() {
        super.inputFramebufferUnlock();
    }
    
    private void a() {
        if (this.n == 0L) {
            this.n = this.l;
            this.m = 0.0f;
        }
        else {
            this.m = Math.abs(this.l - this.n) / this.o;
            if (this.m > 1.0f) {
                this.m = 1.0f;
            }
            else if (this.m < 0.0f) {
                this.m = 0.0f;
            }
        }
        final float[] array = { 0.0f, 0.0f, 0.0f, 0.0f };
        final float[] array2 = { -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f };
        final float[] array3 = { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        final float n = (float)(Math.sin(Math.abs(this.m) * 3.141592653589793) * 0.10000000149011612);
        final float[] array4 = array2;
        final int n2 = 3;
        array4[n2] += n;
        final float[] array5 = array2;
        final int n3 = 7;
        array5[n3] -= n;
        final float[] array6 = array2;
        final int n4 = 11;
        array6[n4] -= n;
        final float[] array7 = array2;
        final int n5 = 15;
        array7[n5] += n;
        final float[] array8 = new float[16];
        final float[] array9 = new float[16];
        Matrix.setIdentityM(array9, 0);
        Matrix.rotateM(array9, 0, Math.abs(this.m) * 180.0f, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(array8, 0, array9, 0, array2, 0);
        if (Math.abs(this.m) > 0.5) {
            array3[0] = 1.0f;
            array3[4] = (array3[2] = 0.0f);
            array3[6] = 1.0f;
            for (int i = 0; i < array.length; ++i) {
                array[i] = 1.0f;
            }
        }
        this.g = SelesFilter.buildBuffer(array8);
        this.h = SelesFilter.buildBuffer(array3);
        this.i = SelesFilter.buildBuffer(array);
    }
    
    static {
        TuSDKLiveFlipFilter.b = 6;
        TuSDKLiveFlipFilter.p = 1.0E9f;
    }
}
