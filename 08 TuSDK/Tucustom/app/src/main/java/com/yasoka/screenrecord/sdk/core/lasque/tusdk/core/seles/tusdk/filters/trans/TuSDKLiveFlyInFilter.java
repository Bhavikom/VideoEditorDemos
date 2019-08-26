// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.trans;

import java.util.Arrays;
import android.opengl.Matrix;
//import org.lasque.tusdk.core.struct.TuSdkSizeF;
import android.graphics.PointF;
import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSizeF;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
import java.nio.IntBuffer;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKLiveFlyInFilter extends SelesFilter implements SelesParameters.FilterParameterInterface
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
    
    public TuSDKLiveFlyInFilter() {
        super("-svflyin", "-sflyin");
        this.j = new float[] { -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f };
        this.k = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        this.a = new int[] { 0, 1, 2, 0, 3, 2 };
        this.g = SelesFilter.buildBuffer(this.j);
        this.h = SelesFilter.buildBuffer(this.k);
        this.o = TuSDKLiveFlyInFilter.p;
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.m = 0.0f;
        this.n = 0L;
        this.l = 0L;
        this.mFilterInputTextureUniform = this.mFilterProgram.uniformIndex("inputImageTexture");
        this.e = this.mFilterProgram.uniformIndex("inputImageTexture2");
        (this.d = ByteBuffer.allocateDirect(this.a.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer().put(this.a)).position(0);
        this.initializeAttributes();
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("duration", TuSDKLiveFlyInFilter.p, TuSDKLiveFlyInFilter.p, Float.MAX_VALUE);
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
        this.o = Math.max(a, TuSDKLiveFlyInFilter.p);
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
        GLES20.glUniform1i(this.e, 2);
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
        GLES20.glUniform1i(this.mFilterInputTextureUniform, 3);
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 4, 5126, false, 0, (Buffer)this.g);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)this.h);
        GLES20.glEnableVertexAttribArray(this.c);
        GLES20.glVertexAttribPointer(this.c, 1, 5126, false, 0, (Buffer)this.i);
        GLES20.glDrawElements(4, this.d.limit(), 5125, (Buffer)this.d);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, 0);
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, 0);
        GLES20.glDisableVertexAttribArray(this.c);
        this.inputFramebufferUnlock();
        this.cacaptureImageBuffer();
    }
    
    @Override
    protected void initializeAttributes() {
        super.initializeAttributes();
        this.c = this.mFilterProgram.attributeIndex("texIndex");
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
            this.m = (this.l - this.n) / this.o;
            this.m = Math.abs(this.m);
            if (this.m > 1.0f) {
                this.m = 1.0f;
            }
            if (this.m < 0.0f) {
                this.m = 0.0f;
            }
        }
        final float[] array = { 0.0f, 0.0f, 0.0f, 0.0f };
        final float[] array2 = { -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f };
        final float[] array3 = { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        this.g = SelesFilter.buildBuffer(array2);
        this.h = SelesFilter.buildBuffer(array3);
        this.i = SelesFilter.buildBuffer(array);
        final float[] array4 = { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        final int[] array5 = this.a.clone();
        final float[] array6 = array.clone();
        final PointF pointF2;
        final PointF pointF = pointF2 = new PointF(1.0f, -1.0f);
        pointF2.x -= Math.abs(this.m);
        final PointF pointF3 = pointF;
        pointF3.y += Math.abs(this.m);
        final TuSdkSizeF tuSdkSizeF2;
        final TuSdkSizeF tuSdkSizeF = tuSdkSizeF2 = new TuSdkSizeF(0.25f, 0.25f);
        tuSdkSizeF2.width += (1.0f - tuSdkSizeF.width) * Math.abs(this.m);
        final TuSdkSizeF tuSdkSizeF3 = tuSdkSizeF;
        tuSdkSizeF3.height += (1.0f - tuSdkSizeF.height) * Math.abs(this.m);
        final float n = Math.abs(this.m) * 360.0f;
        final float[] array7 = new float[16];
        final float[] array8 = new float[16];
        Matrix.setIdentityM(array8, 0);
        Matrix.translateM(array8, 0, pointF.x, pointF.y, 0.0f);
        Matrix.scaleM(array8, 0, tuSdkSizeF.width, tuSdkSizeF.height, 1.0f);
        Matrix.rotateM(array8, 0, n, 0.0f, 0.0f, 1.0f);
        Matrix.multiplyMM(array7, 0, array8, 0, array2, 0);
        for (int i = 0; i < array6.length; ++i) {
            array6[i] = 1.0f;
        }
        for (int j = 0; j < array5.length; ++j) {
            final int[] array9 = array5;
            final int n2 = j;
            array9[n2] += 4;
        }
        this.g = SelesFilter.buildBuffer(concat(array2, array7));
        this.h = SelesFilter.buildBuffer(concat(array3, array3));
        this.i = SelesFilter.buildBuffer(concat(array, array6));
        (this.d = ByteBuffer.allocateDirect((this.a.length + array5.length) * 4).order(ByteOrder.nativeOrder()).asIntBuffer().put(concat(this.a, array5))).position(0);
    }
    
    public static float[] concat(final float[] original, final float[] array) {
        final float[] copy = Arrays.copyOf(original, original.length + array.length);
        System.arraycopy(array, 0, copy, original.length, array.length);
        return copy;
    }
    
    public static int[] concat(final int[] original, final int[] array) {
        final int[] copy = Arrays.copyOf(original, original.length + array.length);
        System.arraycopy(array, 0, copy, original.length, array.length);
        return copy;
    }
    
    static {
        TuSDKLiveFlyInFilter.b = 6;
        TuSDKLiveFlyInFilter.p = 1.0E9f;
    }
}
