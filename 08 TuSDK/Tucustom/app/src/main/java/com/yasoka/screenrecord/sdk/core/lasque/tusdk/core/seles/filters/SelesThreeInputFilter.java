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

public class SelesThreeInputFilter extends SelesTwoInputFilter
{
    protected SelesFramebuffer mThirdInputFramebuffer;
    protected int mFilterThirdTextureCoordinateAttribute;
    protected int mFilterInputTextureUniform3;
    protected ImageOrientation mInputRotation3;
    protected boolean mHasSetSecondTexture;
    protected boolean mHasReceivedThirdFrame;
    protected boolean mThirdFrameWasVideo;
    protected boolean mThirdFrameCheckDisabled;
    private final FloatBuffer a;
    
    public SelesThreeInputFilter(final String s) {
        this("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nattribute vec4 inputTextureCoordinate2;\nattribute vec4 inputTextureCoordinate3;\n\nvarying vec2 textureCoordinate;\nvarying vec2 textureCoordinate2;\nvarying vec2 textureCoordinate3;\n\nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n    textureCoordinate2 = inputTextureCoordinate2.xy;\n    textureCoordinate3 = inputTextureCoordinate3.xy;\n}\n", s);
    }
    
    public SelesThreeInputFilter(final String s, final String s2) {
        super(s, s2);
        this.mInputRotation3 = ImageOrientation.Up;
        this.mHasSetSecondTexture = false;
        this.mHasReceivedThirdFrame = false;
        this.mThirdFrameWasVideo = false;
        this.mThirdFrameCheckDisabled = false;
        this.a = SelesFilter.buildBuffer(SelesThreeInputFilter.noRotationTextureCoordinates);
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.mFilterThirdTextureCoordinateAttribute = this.mFilterProgram.attributeIndex("inputTextureCoordinate3");
        this.mFilterInputTextureUniform3 = this.mFilterProgram.uniformIndex("inputImageTexture3");
        GLES20.glEnableVertexAttribArray(this.mFilterThirdTextureCoordinateAttribute);
    }
    
    @Override
    protected void initializeAttributes() {
        super.initializeAttributes();
        this.mFilterProgram.addAttribute("inputTextureCoordinate3");
    }
    
    public void disableThirdFrameCheck() {
        this.mThirdFrameCheckDisabled = true;
    }
    
    @Override
    protected void inputFramebufferUnlock() {
        super.inputFramebufferUnlock();
        if (this.mThirdInputFramebuffer != null) {
            this.mThirdInputFramebuffer.unlock();
        }
    }
    
    @Override
    protected void inputFramebufferBindTexture() {
        super.inputFramebufferBindTexture();
        GLES20.glActiveTexture(33988);
        GLES20.glBindTexture(3553, (this.mThirdInputFramebuffer == null) ? 0 : this.mThirdInputFramebuffer.getTexture());
        GLES20.glUniform1i(this.mFilterInputTextureUniform3, 4);
        this.a.clear();
        this.a.put(SelesFilter.textureCoordinates(this.mInputRotation3)).position(0);
        GLES20.glVertexAttribPointer(this.mFilterThirdTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)this.a);
    }
    
    @Override
    public int nextAvailableTextureIndex() {
        if (this.mHasSetSecondTexture) {
            return 2;
        }
        return super.nextAvailableTextureIndex();
    }
    
    @Override
    public void setInputFramebuffer(final SelesFramebuffer mSecondInputFramebuffer, final int n) {
        if (mSecondInputFramebuffer == null) {
            return;
        }
        if (n == 1) {
            this.mSecondInputFramebuffer = mSecondInputFramebuffer;
            this.mHasSetSecondTexture = true;
            this.mSecondInputFramebuffer.lock();
        }
        else {
            super.setInputFramebuffer(mSecondInputFramebuffer, n);
        }
    }
    
    @Override
    protected void setInputFramebufferLast(final SelesFramebuffer mThirdInputFramebuffer) {
        (this.mThirdInputFramebuffer = mThirdInputFramebuffer).lock();
    }
    
    @Override
    public void setInputSize(final TuSdkSize tuSdkSize, final int n) {
        super.setInputSize(tuSdkSize, n);
        if (n == 1 && (tuSdkSize == null || !tuSdkSize.isSize())) {
            this.mHasSetSecondTexture = false;
        }
    }
    
    @Override
    public void setInputRotation(final ImageOrientation inputRotationLast, final int n) {
        if (n == 1) {
            super.setInputRotationLast(inputRotationLast);
        }
        else {
            super.setInputRotation(inputRotationLast, n);
        }
    }
    
    @Override
    protected void setInputRotationLast(final ImageOrientation mInputRotation3) {
        this.mInputRotation3 = mInputRotation3;
    }
    
    @Override
    protected ImageOrientation getRotationWithIndex(final int n) {
        if (n == 1) {
            return super.getRotationLast();
        }
        return super.getRotationWithIndex(n);
    }
    
    @Override
    protected ImageOrientation getRotationLast() {
        return this.mInputRotation3;
    }
    
    @Override
    protected void receivedFrame(final int n) {
        if (n == 1) {
            super.receivedFrameLast();
        }
        else {
            super.receivedFrame(n);
        }
    }
    
    @Override
    protected void receivedFrameLast() {
        this.mHasReceivedThirdFrame = true;
    }
    
    @Override
    protected void receivedFramesCheck() {
        super.receivedFramesCheck();
        if (this.mThirdFrameCheckDisabled) {
            this.mHasReceivedThirdFrame = true;
        }
    }
    
    @Override
    protected boolean receivedFrames() {
        return this.mHasReceivedThirdFrame && super.receivedFrames();
    }
    
    @Override
    protected void receivedFramesResume() {
        super.receivedFramesResume();
        this.mHasReceivedThirdFrame = false;
    }
}
