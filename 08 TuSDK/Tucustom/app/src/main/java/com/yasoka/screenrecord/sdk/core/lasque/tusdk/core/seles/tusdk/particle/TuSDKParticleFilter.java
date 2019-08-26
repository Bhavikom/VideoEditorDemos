// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.particle;

//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.nio.FloatBuffer;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesPicture;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.TuSdkBundle;
import android.opengl.Matrix;
import android.opengl.GLES20;
import org.json.JSONObject;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkBundle;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;

public class TuSDKParticleFilter extends SelesTwoInputFilter implements TuSDKParticleFilterInterface
{
    private ParticleManager a;
    private final float[] b;
    private int c;
    private int d;
    private long e;
    private PointF f;
    
    public TuSDKParticleFilter(final JSONObject jsonObject) {
        super("-sp02v", "-sp02f");
        this.b = new float[16];
        this.disableSecondFrameCheck();
        this.a = new ParticleManager(jsonObject);
    }
    
    @Override
    protected void onDestroy() {
        if (this.a != null) {
            this.a.destory();
            this.a = null;
        }
        super.onDestroy();
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.c = this.mFilterProgram.uniformIndex("uTexMatrix");
        GLES20.glUniform2fv(this.d = this.mFilterProgram.uniformIndex("uTexTilePrams"), 1, this.a.getTextureTile(), 0);
        Matrix.setIdentityM(this.b, 0);
    }
    
    public void loadTexture() {
        final Bitmap assetsBitmap = TuSdkContext.getAssetsBitmap(TuSdkBundle.sdkBundleTexture("a.png"));
        if (assetsBitmap == null) {
            return;
        }
        final SelesPicture selesPicture = new SelesPicture(assetsBitmap, false, true);
        selesPicture.processImage();
        selesPicture.addTarget(this, 1);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        this.runPendingOnDrawTasks();
        if (this.isPreventRendering()) {
            this.inputFramebufferUnlock();
            return;
        }
        SelesContext.setActiveShaderProgram(this.mFilterProgram);
        (this.mOutputFramebuffer = this.mFirstInputFramebuffer).activateFramebuffer();
        if (this.mUsingNextFrameForImageCapture) {
            this.mOutputFramebuffer.lock();
        }
        this.setUniformsForProgramAtIndex(0);
        this.a();
        this.cacaptureImageBuffer();
    }
    
    private void a() {
        if (this.mSecondInputFramebuffer == null) {
            return;
        }
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, this.mSecondInputFramebuffer.getTexture());
        GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
        GLES20.glUniformMatrix4fv(this.c, 1, false, this.b, 0);
        this.a.freshVBO();
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 40, 0);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 4, 5126, false, 40, 8);
        GLES20.glVertexAttribPointer(this.mFilterSecondTextureCoordinateAttribute, 4, 5126, false, 40, 24);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(this.a.config().blendFuncSrc, this.a.config().blendFuncDst);
        GLES20.glDrawArrays(0, 0, this.a.drawTotal());
        GLES20.glDisable(3042);
        GLES20.glBindBuffer(34962, 0);
    }
    
    @Override
    public void setInputSize(final TuSdkSize tuSdkSize, final int n) {
        super.setInputSize(tuSdkSize, n);
        this.a.setTextureSize(this.mInputTextureSize);
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long n) {
        this.a(n / 1000L);
        super.informTargetsAboutNewFrame(n);
    }
    
    private void a(final long e) {
        if (this.f == null) {
            return;
        }
        this.a.config().position = this.f;
        float n = Math.abs(e - this.e) / 1000.0f / 1000.0f;
        this.e = e;
        if (n > 1.0f) {
            n = 0.1f;
        }
        this.a.update(n);
    }
    
    @Override
    public void updateParticleEmitPosition(final PointF f) {
        if (f == null) {
            return;
        }
        this.f = f;
    }
    
    @Override
    public void setParticleSize(final float n) {
        this.a.updateParticleSize(n);
    }
    
    @Override
    public void setParticleColor(final int n) {
        this.a.updateParticleColor(n);
    }
    
    @Override
    public void setActive(final boolean active) {
        this.a.setActive(active);
    }
    
    @Override
    public void reset() {
        this.a.reset(0);
    }
}
