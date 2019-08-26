// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters;

import java.util.Iterator;
//import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebufferCache;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesGLProgram;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.HashMap;
import java.nio.FloatBuffer;
import java.util.Map;
//import org.lasque.tusdk.core.seles.SelesGLProgram;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;

public class SelesTwoPassFilter extends SelesFilter
{
    protected SelesFramebuffer mSecondOutputFramebuffer;
    protected SelesGLProgram mSecondFilterProgram;
    protected int mSecondFilterPositionAttribute;
    protected int mSecondFilterTextureCoordinateAttribute;
    protected int mSecondFilterInputTextureUniform;
    protected int mSecondFilterInputTextureUniform2;
    protected final Map<Integer, Runnable> mSecondProgramUniformStateRestorationBlocks;
    private final String a;
    private final String b;
    protected final FloatBuffer mNoRotationTextureBuffer;
    
    public SelesTwoPassFilter(final String s, final String s2) {
        this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", s, "attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", s2);
    }
    
    public SelesTwoPassFilter(final String s, final String s2, final String a, final String b) {
        super(s, s2);
        this.mSecondProgramUniformStateRestorationBlocks = new HashMap<Integer, Runnable>();
        this.a = a;
        this.b = b;
        this.mNoRotationTextureBuffer = SelesFilter.buildBuffer(SelesTwoPassFilter.noRotationTextureCoordinates);
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.mSecondFilterProgram = SelesContext.program(this.a, this.b);
        if (!this.mSecondFilterProgram.isInitialized()) {
            this.initializeSecondaryAttributes();
            if (!this.mSecondFilterProgram.link()) {
                TLog.i("Program link log: %s", this.mSecondFilterProgram.getProgramLog());
                TLog.i("Fragment shader compile log: %s", this.mSecondFilterProgram.getFragmentShaderLog());
                TLog.i("Vertex link log: %s", this.mSecondFilterProgram.getVertexShaderLog());
                this.mSecondFilterProgram = null;
                TLog.e("Filter shader link failed: %s", this.getClass());
                return;
            }
        }
        this.mSecondFilterPositionAttribute = this.mSecondFilterProgram.attributeIndex("position");
        this.mSecondFilterTextureCoordinateAttribute = this.mSecondFilterProgram.attributeIndex("inputTextureCoordinate");
        this.mSecondFilterInputTextureUniform = this.mSecondFilterProgram.uniformIndex("inputImageTexture");
        this.mSecondFilterInputTextureUniform2 = this.mSecondFilterProgram.uniformIndex("inputImageTexture2");
        SelesContext.setActiveShaderProgram(this.mSecondFilterProgram);
        GLES20.glEnableVertexAttribArray(this.mSecondFilterPositionAttribute);
        GLES20.glEnableVertexAttribArray(this.mSecondFilterTextureCoordinateAttribute);
    }
    
    protected void initializeSecondaryAttributes() {
        this.mSecondFilterProgram.addAttribute("position");
        this.mSecondFilterProgram.addAttribute("inputTextureCoordinate");
    }
    
    @Override
    public SelesFramebuffer framebufferForOutput() {
        return this.mSecondOutputFramebuffer;
    }
    
    @Override
    public void removeOutputFramebuffer() {
        this.mSecondOutputFramebuffer = null;
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        this.runPendingOnDrawTasks();
        if (this.isPreventRendering()) {
            this.mFirstInputFramebuffer.unlock();
            return;
        }
        SelesContext.setActiveShaderProgram(this.mFilterProgram);
        final SelesFramebufferCache sharedFramebufferCache = SelesContext.sharedFramebufferCache();
        if (sharedFramebufferCache == null) {
            return;
        }
        (this.mOutputFramebuffer = sharedFramebufferCache.fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, this.sizeOfFBO(), this.getOutputTextureOptions())).activateFramebuffer();
        this.setUniformsForProgramAtIndex(0);
        GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
        GLES20.glClear(16384);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
        GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, (Buffer)floatBuffer);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)floatBuffer2);
        GLES20.glDrawArrays(5, 0, 4);
        this.mFirstInputFramebuffer.unlock();
        this.mFirstInputFramebuffer = null;
        (this.mSecondOutputFramebuffer = sharedFramebufferCache.fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, this.sizeOfFBO(), this.getOutputTextureOptions())).activateFramebuffer();
        SelesContext.setActiveShaderProgram(this.mSecondFilterProgram);
        if (this.mUsingNextFrameForImageCapture) {
            this.mSecondOutputFramebuffer.lock();
        }
        this.setUniformsForProgramAtIndex(1);
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, this.mOutputFramebuffer.getTexture());
        GLES20.glVertexAttribPointer(this.mSecondFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)this.mNoRotationTextureBuffer);
        GLES20.glUniform1i(this.mSecondFilterInputTextureUniform, 3);
        GLES20.glVertexAttribPointer(this.mSecondFilterPositionAttribute, 2, 5126, false, 0, (Buffer)floatBuffer);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(16384);
        GLES20.glDrawArrays(5, 0, 4);
        this.mOutputFramebuffer.unlock();
        this.mOutputFramebuffer = null;
        this.cacaptureImageBuffer();
    }
    
    @Override
    protected void setAndExecuteUniformStateCallbackAtIndex(final int n, final SelesGLProgram selesGLProgram, final Runnable runnable) {
        if (runnable == null) {
            return;
        }
        if (selesGLProgram == this.mFilterProgram) {
            this.mUniformStateRestorationBlocks.put(n, runnable);
        }
        else {
            this.mSecondProgramUniformStateRestorationBlocks.put(n, runnable);
        }
        runnable.run();
    }
    
    public void setUniformsForProgramAtIndex(final int n) {
        if (n == 0) {
            final Iterator<Runnable> iterator = this.mUniformStateRestorationBlocks.values().iterator();
            while (iterator.hasNext()) {
                iterator.next().run();
            }
        }
        else {
            final Iterator<Runnable> iterator2 = this.mSecondProgramUniformStateRestorationBlocks.values().iterator();
            while (iterator2.hasNext()) {
                iterator2.next().run();
            }
        }
    }
}
