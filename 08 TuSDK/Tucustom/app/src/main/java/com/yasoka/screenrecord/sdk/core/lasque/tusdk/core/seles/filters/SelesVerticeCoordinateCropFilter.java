// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters;

//import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import java.nio.Buffer;
import android.opengl.GLES20;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.seles.SelesContext;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebufferCache;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;

public class SelesVerticeCoordinateCropFilter extends SelesFilter
{
    private SelesVerticeCoordinateCropBuilderImpl a;
    private TuSdkSize b;
    private boolean c;
    
    public SelesVerticeCoordinateCropFilter() {
        this.c = false;
        this.a = new SelesVerticeCoordinateCropBuilderImpl(false);
    }
    
    public void setManualFBO(final boolean c) {
        this.c = c;
    }
    
    public void setEnableClip(final boolean enableClip) {
        this.a.setEnableClip(enableClip);
    }
    
    public void setOutputSize(final TuSdkSize outputSize) {
        this.a.setOutputSize(outputSize);
    }
    
    public void setCanvasRect(final RectF canvasRect) {
        this.a.setCanvasRect(canvasRect);
    }
    
    public void setCropRect(final RectF cropRect) {
        this.a.setCropRect(cropRect);
    }
    
    public void setUsingNextFrameForImageCapture(final boolean mUsingNextFrameForImageCapture) {
        this.mUsingNextFrameForImageCapture = mUsingNextFrameForImageCapture;
    }
    
    public void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    @Override
    public void setInputSize(final TuSdkSize tuSdkSize, final int n) {
        super.setInputSize(tuSdkSize, n);
        if (this.mOutputFramebuffer != null) {
            SelesContext.sharedFramebufferCache().recycleFramebuffer(this.mOutputFramebuffer);
        }
        (this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE_AND_RENDER, tuSdkSize)).disableReferenceCounting();
    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        super.newFrameReady(n, n2);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        if (this.a != null && this.a.calculate(this.mInputTextureSize, this.mInputRotation, floatBuffer, floatBuffer2)) {
            this.b = this.a.outputSize();
        }
        else {
            floatBuffer2.clear();
            floatBuffer2.put(SelesFilter.textureCoordinates(this.mInputRotation)).position(0);
            this.b = this.mInputTextureSize;
        }
        this.runPendingOnDrawTasks();
        if (this.isPreventRendering()) {
            this.inputFramebufferUnlock();
            return;
        }
        SelesContext.setActiveShaderProgram(this.mFilterProgram);
        final TuSdkSize sizeOfFBO = this.sizeOfFBO();
        if (this.mOutputFramebuffer == null) {
            final SelesFramebufferCache sharedFramebufferCache = SelesContext.sharedFramebufferCache();
            if (sharedFramebufferCache == null) {
                return;
            }
            this.mOutputFramebuffer = sharedFramebufferCache.fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, sizeOfFBO, this.getOutputTextureOptions());
        }
        this.mOutputFramebuffer.activateFramebuffer();
        if (this.mUsingNextFrameForImageCapture) {
            this.mOutputFramebuffer.lock();
        }
        this.setUniformsForProgramAtIndex(0);
        GLES20.glClear(16640);
        this.inputFramebufferBindTexture();
        GLES20.glEnableVertexAttribArray(this.mFilterPositionAttribute);
        GLES20.glEnableVertexAttribArray(this.mFilterTextureCoordinateAttribute);
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, (Buffer)floatBuffer);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)floatBuffer2);
        GLES20.glDrawArrays(5, 0, 4);
        this.inputFramebufferUnlock();
        this.cacaptureImageBuffer();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
