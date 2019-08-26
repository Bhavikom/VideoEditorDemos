// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output;

//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

@Deprecated
public class SelesSurfaceTextureOutput extends SelesFilter
{
    private FloatBuffer a;
    private FloatBuffer b;
    private RectF c;
    private ImageOrientation d;
    
    public SelesSurfaceTextureOutput() {
        this.c = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        this.d = ImageOrientation.Up;
        this.a = SelesFilter.buildBuffer(SelesSurfaceTextureOutput.imageVertices);
        this.b = SelesFilter.buildBuffer(SelesSurfaceTextureOutput.noRotationTextureCoordinates);
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    public void setOutputOrientation(final ImageOrientation d) {
        this.d = d;
    }
    
    public ImageOrientation getOutputOrientation() {
        return this.d;
    }
    
    @Override
    public void setInputFramebuffer(final SelesFramebuffer selesFramebuffer, final int n) {
        super.setInputFramebuffer(selesFramebuffer, n);
    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        if (this.mFirstInputFramebuffer == null) {
            return;
        }
        this.b.clear();
        this.b.put(SelesFilter.textureCoordinates(this.d)).position(0);
        this.renderToTexture(this.a, this.b);
        this.informTargetsAboutNewFrame(n);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        this.runPendingOnDrawTasks();
        if (this.isPreventRendering()) {
            this.inputFramebufferUnlock();
            return;
        }
        SelesContext.setActiveShaderProgram(this.mFilterProgram);
        final TuSdkSize sizeOfFBO = this.sizeOfFBO();
        if (this.mOutputFramebuffer == null) {
            (this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, sizeOfFBO, this.getOutputTextureOptions())).disableReferenceCounting();
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
        this.inputFramebufferUnlock();
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
        GLES20.glBindTexture(3553, 0);
        this.cacaptureImageBuffer();
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long n) {
    }
    
    @Override
    public void setInputSize(TuSdkSize create, final int n) {
        if (this.isPreventRendering()) {
            return;
        }
        if (this.mOverrideInputSize) {
            if (this.mForcedMaximumSize == null || this.mForcedMaximumSize.minSide() < 1) {
                this.setupFilterForSize(this.sizeOfFBO());
                return;
            }
            create = TuSdkSize.create(RectHelper.makeRectWithAspectRatioInsideRect(create, new Rect(0, 0, this.mForcedMaximumSize.width, this.mForcedMaximumSize.height)));
        }
        final TuSdkSize rotatedSize = this.rotatedSize(create, n);
        if (rotatedSize.minSide() < 1) {
            this.mInputTextureSize = rotatedSize;
        }
        else if (!rotatedSize.equals(this.mInputTextureSize)) {
            this.mInputTextureSize = rotatedSize;
        }
        final TuSdkSize tuSdkSize = new TuSdkSize();
        tuSdkSize.width = (int)(this.mInputTextureSize.width * this.c.width());
        tuSdkSize.height = (int)(this.mInputTextureSize.height * this.c.height());
        if (tuSdkSize.isSize()) {
            this.mInputTextureSize = tuSdkSize;
        }
        else if (!this.mInputTextureSize.equals(tuSdkSize)) {
            this.mInputTextureSize = tuSdkSize;
        }
        this.setupFilterForSize(this.sizeOfFBO());
    }
    
    @Override
    public void setInputRotation(final ImageOrientation imageOrientation, final int n) {
        super.setInputRotation(imageOrientation, n);
        this.a();
    }
    
    public void setCropRegion(final RectF c) {
        if (this.c.left == c.left && this.c.right == c.right && this.c.top == c.top && this.c.bottom == c.bottom) {
            return;
        }
        this.c = c;
        this.a();
    }
    
    private void a() {
        final float n = 0.5f - this.c.right / 2.0f;
        final float n2 = 0.5f - this.c.bottom / 2.0f;
        final float n3 = 0.5f + this.c.right / 2.0f;
        final float n4 = 0.5f + this.c.bottom / 2.0f;
        final float[] src = { 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f };
        src[0] = n;
        src[1] = n2;
        src[2] = n3;
        src[3] = n2;
        src[4] = n;
        src[5] = n4;
        src[6] = n3;
        src[7] = n4;
        this.b.clear();
        this.b.put(src).position(0);
    }
    
    @Override
    protected void onDestroy() {
        this.b();
    }
    
    private void b() {
        if (this.mOutputFramebuffer == null) {
            return;
        }
        this.mOutputFramebuffer.enableReferenceCounting();
        SelesContext.recycleFramebuffer(this.mOutputFramebuffer);
        this.mOutputFramebuffer = null;
    }
}
