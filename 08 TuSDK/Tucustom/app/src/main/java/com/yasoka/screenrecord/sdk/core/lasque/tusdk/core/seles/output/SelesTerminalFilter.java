// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output;

//import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebufferCache;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class SelesTerminalFilter extends SelesFilter
{
    private boolean a;
    
    public SelesTerminalFilter() {
        this.a = false;
    }
    
    @Override
    public void setInputSize(final TuSdkSize tuSdkSize, final int n) {
        if (tuSdkSize == null) {
            return;
        }
        super.setInputSize(tuSdkSize, n);
        this.a = !tuSdkSize.equals(this.mInputTextureSize);
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        this.runPendingOnDrawTasks();
        if (this.isPreventRendering()) {
            this.inputFramebufferUnlock();
            return;
        }
        SelesContext.setActiveShaderProgram(this.mFilterProgram);
        this.checkGLError(this.getClass().getSimpleName() + " setActiveShaderProgram");
        if (this.mOutputFramebuffer == null || this.a) {
            this.a();
            final TuSdkSize sizeOfFBO = this.sizeOfFBO();
            final SelesFramebufferCache sharedFramebufferCache = SelesContext.sharedFramebufferCache();
            if (sharedFramebufferCache == null) {
                return;
            }
            (this.mOutputFramebuffer = sharedFramebufferCache.fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, sizeOfFBO, this.getOutputTextureOptions())).disableReferenceCounting();
        }
        this.mOutputFramebuffer.activateFramebuffer();
        this.checkGLError(this.getClass().getSimpleName() + " activateFramebuffer");
        if (this.mUsingNextFrameForImageCapture) {
            this.mOutputFramebuffer.lock();
        }
        this.setUniformsForProgramAtIndex(0);
        GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
        GLES20.glClear(16384);
        this.inputFramebufferBindTexture();
        this.checkGLError(this.getClass().getSimpleName() + " bindFramebuffer");
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, (Buffer)floatBuffer);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)floatBuffer2);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glFinish();
        this.inputFramebufferUnlock();
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
        this.cacaptureImageBuffer();
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long n) {
    }
    
    @Override
    protected void onDestroy() {
        this.a();
    }
    
    private void a() {
        this.a = false;
        if (this.mOutputFramebuffer == null) {
            return;
        }
        this.mOutputFramebuffer.enableReferenceCounting();
        SelesContext.recycleFramebuffer(this.mOutputFramebuffer);
        this.mOutputFramebuffer = null;
    }
}
