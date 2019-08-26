// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters;

//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.nio.Buffer;
import android.opengl.GLES20;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;

public class SelesTwoInputFilter extends SelesFilter
{
    protected SelesFramebuffer mSecondInputFramebuffer;
    protected int mFilterSecondTextureCoordinateAttribute;
    protected int mFilterInputTextureUniform2;
    protected ImageOrientation mInputRotation2;
    protected boolean mHasSetFirstTexture;
    protected boolean mHasReceivedFirstFrame;
    protected boolean mHasReceivedSecondFrame;
    protected boolean mFirstFrameWasVideo;
    protected boolean mSecondFrameWasVideo;
    protected boolean mFirstFrameCheckDisabled;
    protected boolean mSecondFrameCheckDisabled;
    private final FloatBuffer a;
    
    public SelesTwoInputFilter(final String s) {
        this("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nattribute vec4 inputTextureCoordinate2;\n \nvarying vec2 textureCoordinate;\nvarying vec2 textureCoordinate2;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n    textureCoordinate2 = inputTextureCoordinate2.xy;\n}", s);
    }
    
    public SelesTwoInputFilter(final String s, final String s2) {
        super(s, s2);
        this.mInputRotation2 = ImageOrientation.Up;
        this.mHasSetFirstTexture = false;
        this.mHasReceivedFirstFrame = false;
        this.mHasReceivedSecondFrame = false;
        this.mFirstFrameWasVideo = false;
        this.mSecondFrameWasVideo = false;
        this.mFirstFrameCheckDisabled = false;
        this.mSecondFrameCheckDisabled = false;
        this.a = SelesFilter.buildBuffer(SelesTwoInputFilter.noRotationTextureCoordinates);
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.mFilterSecondTextureCoordinateAttribute = this.mFilterProgram.attributeIndex("inputTextureCoordinate2");
        this.mFilterInputTextureUniform2 = this.mFilterProgram.uniformIndex("inputImageTexture2");
        GLES20.glEnableVertexAttribArray(this.mFilterSecondTextureCoordinateAttribute);
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    @Override
    protected void initializeAttributes() {
        super.initializeAttributes();
        this.mFilterProgram.addAttribute("inputTextureCoordinate2");
    }
    
    public void disableFirstFrameCheck() {
        this.mFirstFrameCheckDisabled = true;
    }
    
    public void disableSecondFrameCheck() {
        this.mSecondFrameCheckDisabled = true;
    }
    
    @Override
    protected void inputFramebufferUnlock() {
        super.inputFramebufferUnlock();
        if (this.mSecondInputFramebuffer != null) {
            this.mSecondInputFramebuffer.unlock();
        }
    }
    
    @Override
    protected void inputFramebufferBindTexture() {
        super.inputFramebufferBindTexture();
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, (this.mSecondInputFramebuffer == null) ? 0 : this.mSecondInputFramebuffer.getTexture());
        GLES20.glUniform1i(this.mFilterInputTextureUniform2, 3);
        this.a.clear();
        this.a.put(SelesFilter.textureCoordinates(this.mInputRotation2)).position(0);
        GLES20.glVertexAttribPointer(this.mFilterSecondTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)this.a);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, floatBuffer2);
        this.checkGLError(this.getClass().getSimpleName());
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    }
    
    @Override
    public int nextAvailableTextureIndex() {
        if (this.mHasSetFirstTexture) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public void setInputFramebuffer(final SelesFramebuffer selesFramebuffer, final int n) {
        if (selesFramebuffer == null) {
            return;
        }
        if (n == 0) {
            this.mFirstInputFramebuffer = selesFramebuffer;
            this.mHasSetFirstTexture = true;
            this.mFirstInputFramebuffer.lock();
        }
        else {
            this.setInputFramebufferLast(selesFramebuffer);
        }
    }
    
    protected void setInputFramebufferLast(final SelesFramebuffer mSecondInputFramebuffer) {
        (this.mSecondInputFramebuffer = mSecondInputFramebuffer).lock();
    }
    
    @Override
    public void setInputSize(final TuSdkSize tuSdkSize, final int n) {
        if (n == 0) {
            super.setInputSize(tuSdkSize, n);
            if (tuSdkSize == null || !tuSdkSize.isSize()) {
                this.mHasSetFirstTexture = false;
            }
        }
    }
    
    @Override
    public void setInputRotation(final ImageOrientation imageOrientation, final int n) {
        if (n == 0) {
            this.mInputRotation = imageOrientation;
        }
        else {
            this.setInputRotationLast(imageOrientation);
        }
    }
    
    protected void setInputRotationLast(final ImageOrientation mInputRotation2) {
        this.mInputRotation2 = mInputRotation2;
    }
    
    @Override
    public TuSdkSize rotatedSize(TuSdkSize tuSdkSize, final int n) {
        if (tuSdkSize == null) {
            tuSdkSize = new TuSdkSize();
        }
        final TuSdkSize copy = tuSdkSize.copy();
        if (this.getRotationWithIndex(n).isTransposed()) {
            copy.width = tuSdkSize.height;
            copy.height = tuSdkSize.width;
        }
        return copy;
    }
    
    protected ImageOrientation getRotationWithIndex(final int n) {
        if (n == 0) {
            return this.mInputRotation;
        }
        return this.getRotationLast();
    }
    
    protected ImageOrientation getRotationLast() {
        return this.mInputRotation2;
    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        if (this.receivedFrames()) {
            return;
        }
        this.receivedFrame(n2);
        this.receivedFramesCheck();
        if (this.receivedFrames()) {
            super.newFrameReady(n, 0);
            this.receivedFramesResume();
        }
    }
    
    protected void receivedFrame(final int n) {
        if (n == 0) {
            this.mHasReceivedFirstFrame = true;
        }
        else {
            this.receivedFrameLast();
        }
    }
    
    protected void receivedFrameLast() {
        this.mHasReceivedSecondFrame = true;
    }
    
    protected void receivedFramesCheck() {
        if (this.mFirstFrameCheckDisabled) {
            this.mHasReceivedFirstFrame = true;
        }
        if (this.mSecondFrameCheckDisabled) {
            this.mHasReceivedSecondFrame = true;
        }
    }
    
    protected boolean receivedFrames() {
        return this.mHasReceivedFirstFrame && this.mHasReceivedSecondFrame;
    }
    
    protected void receivedFramesResume() {
        this.mHasReceivedFirstFrame = false;
        this.mHasReceivedSecondFrame = false;
    }
}
