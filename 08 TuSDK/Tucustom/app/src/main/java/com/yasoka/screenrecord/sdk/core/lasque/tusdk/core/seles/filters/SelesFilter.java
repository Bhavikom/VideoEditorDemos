// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters;

//import org.lasque.tusdk.core.struct.TuSdkSizeF;
//import org.lasque.tusdk.core.utils.monitor.TuSdkMonitor;
//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
import android.graphics.PointF;
import java.util.Iterator;
//import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.nio.Buffer;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebufferCache;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesGLProgram;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSizeF;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkSemaphore;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.monitor.TuSdkMonitor;

import java.util.LinkedHashMap;
import java.util.HashMap;
//import org.lasque.tusdk.core.seles.SelesContext;
import java.nio.IntBuffer;
//import org.lasque.tusdk.core.utils.TuSdkSemaphore;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.SelesGLProgram;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
import java.nio.FloatBuffer;
import java.util.Map;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;

public class SelesFilter extends SelesOutInput
{
    public static final String SELES_VERTEX_SHADER = "attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}";
    public static final String SELES_PASSTHROUGH_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}";
    public static final float[] noRotationTextureCoordinates;
    public static final float[] rotateLeftTextureCoordinates;
    public static final float[] rotateRightTextureCoordinates;
    public static final float[] verticalFlipTextureCoordinates;
    public static final float[] horizontalFlipTextureCoordinates;
    public static final float[] rotateRightVerticalFlipTextureCoordinates;
    public static final float[] rotateRightHorizontalFlipTextureCoordinates;
    public static final float[] rotate180TextureCoordinates;
    public static final float[] imageVertices;
    protected final Map<Integer, Runnable> mUniformStateRestorationBlocks;
    protected final FloatBuffer mVerticesBuffer;
    protected final FloatBuffer mTextureBuffer;
    protected SelesFramebuffer mFirstInputFramebuffer;
    protected SelesGLProgram mFilterProgram;
    protected int mFilterPositionAttribute;
    protected int mFilterTextureCoordinateAttribute;
    protected int mFilterInputTextureUniform;
    protected float mBackgroundColorRed;
    protected float mBackgroundColorGreen;
    protected float mBackgroundColorBlue;
    protected float mBackgroundColorAlpha;
    protected boolean mIsEndProcessing;
    protected ImageOrientation mInputRotation;
    protected boolean mCurrentlyReceivingMonochromeInput;
    private boolean a;
    private FrameProcessingDelegate b;
    protected TuSdkSemaphore mImageCaptureSemaphore;
    private float c;
    private IntBuffer d;
    private final Map<SelesContext.SelesInput, Integer> e;
    
    public float getScale() {
        if (this.c <= 0.0f) {
            this.c = 1.0f;
        }
        return this.c;
    }
    
    public void setScale(final float c) {
        this.c = c;
    }
    
    public boolean isPreventRendering() {
        return this.a;
    }
    
    public void setPreventRendering(final boolean a) {
        this.a = a;
    }
    
    public boolean isCurrentlyReceivingMonochromeInput() {
        return this.mCurrentlyReceivingMonochromeInput;
    }
    
    @Override
    public void setCurrentlyReceivingMonochromeInput(final boolean mCurrentlyReceivingMonochromeInput) {
        this.mCurrentlyReceivingMonochromeInput = mCurrentlyReceivingMonochromeInput;
    }
    
    public void setFrameProcessingDelegate(final FrameProcessingDelegate b) {
        this.b = b;
    }
    
    public FrameProcessingDelegate getFrameProcessingDelegate() {
        return this.b;
    }
    
    public SelesFilter() {
        this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
    }
    
    public SelesFilter(final String s) {
        this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", s);
    }
    
    public SelesFilter(final String s, final String s2) {
        this.mUniformStateRestorationBlocks = new HashMap<Integer, Runnable>();
        this.mBackgroundColorAlpha = 1.0f;
        this.mInputRotation = ImageOrientation.Up;
        this.c = 1.0f;
        this.e = new LinkedHashMap<SelesContext.SelesInput, Integer>();
        this.mVerticesBuffer = buildBuffer(SelesFilter.imageVertices);
        this.mTextureBuffer = buildBuffer(SelesFilter.noRotationTextureCoordinates);
        (this.mImageCaptureSemaphore = new TuSdkSemaphore(0)).signal();
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesFilter.this.a(s, s2);
                SelesFilter.this.onInitOnGLThread();
            }
        });
    }
    
    protected void onInitOnGLThread() {
    }
    
    private void a(final String s, final String s2) {
        this.mFilterProgram = SelesContext.program(s, s2);
        if (!this.mFilterProgram.isInitialized()) {
            this.initializeAttributes();
            if (!this.mFilterProgram.link()) {
                TLog.i("Program link log: %s", this.mFilterProgram.getProgramLog());
                TLog.i("Fragment shader compile log: %s", this.mFilterProgram.getFragmentShaderLog());
                TLog.i("Vertex link log: %s", this.mFilterProgram.getVertexShaderLog());
                this.mFilterProgram = null;
                TLog.e("Filter shader link failed: %s", this.getClass());
                return;
            }
        }
        this.mFilterPositionAttribute = this.mFilterProgram.attributeIndex("position");
        this.mFilterTextureCoordinateAttribute = this.mFilterProgram.attributeIndex("inputTextureCoordinate");
        this.mFilterInputTextureUniform = this.mFilterProgram.uniformIndex("inputImageTexture");
        SelesContext.setActiveShaderProgram(this.mFilterProgram);
        GLES20.glEnableVertexAttribArray(this.mFilterPositionAttribute);
        GLES20.glEnableVertexAttribArray(this.mFilterTextureCoordinateAttribute);
    }
    
    protected void initializeAttributes() {
        this.mFilterProgram.addAttribute("position");
        this.mFilterProgram.addAttribute("inputTextureCoordinate");
    }
    
    @Override
    protected void onDestroy() {
        this.d = null;
        if (this.mImageCaptureSemaphore != null) {
            this.mImageCaptureSemaphore.release();
            this.mImageCaptureSemaphore = null;
        }
    }
    
    public void setupFilterForSize(final TuSdkSize tuSdkSize) {
    }
    
    @Override
    public void useNextFrameForImageCapture() {
        this.mUsingNextFrameForImageCapture = true;
        if (this.mImageCaptureSemaphore == null) {
            return;
        }
        if (!this.mImageCaptureSemaphore.waitSignal(0L)) {
            return;
        }
    }
    
    @Override
    public IntBuffer imageBufferFromCurrentlyProcessedOutput(final TuSdkSize tuSdkSize) {
        if (this.mImageCaptureSemaphore == null) {
            return null;
        }
        if (!this.mImageCaptureSemaphore.waitSignal(3000L)) {
            return null;
        }
        final SelesFramebuffer framebufferForOutput = this.framebufferForOutput();
        IntBuffer imageBufferFromFramebufferContents = null;
        if (framebufferForOutput != null) {
            imageBufferFromFramebufferContents = framebufferForOutput.imageBufferFromFramebufferContents();
            if (tuSdkSize != null) {
                tuSdkSize.set(framebufferForOutput.getSize());
            }
            framebufferForOutput.unlock();
        }
        this.mUsingNextFrameForImageCapture = false;
        if (this.mImageCaptureSemaphore != null) {
            this.mImageCaptureSemaphore.signal();
        }
        return imageBufferFromFramebufferContents;
    }
    
    public IntBuffer readImageBuffer() {
        final TuSdkSize sizeOfFBO = this.sizeOfFBO();
        if (this.d == null) {
            this.d = IntBuffer.allocate(sizeOfFBO.width * sizeOfFBO.height);
        }
        this.d.position(0);
        GLES20.glReadPixels(0, 0, sizeOfFBO.width, sizeOfFBO.height, 6408, 5121, (Buffer)this.d);
        return this.d;
    }
    
    public TuSdkSize sizeOfFBO() {
        final TuSdkSize maximumOutputSize = this.maximumOutputSize();
        if (maximumOutputSize.minSide() < 1 || this.mInputTextureSize.width < maximumOutputSize.width) {
            return this.mInputTextureSize;
        }
        return maximumOutputSize;
    }
    
    public static float[] textureCoordinates(ImageOrientation up) {
        if (up == null) {
            up = ImageOrientation.Up;
        }
        switch (up.ordinal()) {
            case 1: {
                return SelesFilter.rotateLeftTextureCoordinates;
            }
            case 2: {
                return SelesFilter.rotateRightTextureCoordinates;
            }
            case 3: {
                return SelesFilter.verticalFlipTextureCoordinates;
            }
            case 4: {
                return SelesFilter.horizontalFlipTextureCoordinates;
            }
            case 5: {
                return SelesFilter.rotateRightVerticalFlipTextureCoordinates;
            }
            case 6: {
                return SelesFilter.rotateRightHorizontalFlipTextureCoordinates;
            }
            case 7: {
                return SelesFilter.rotate180TextureCoordinates;
            }
            default: {
                return SelesFilter.noRotationTextureCoordinates;
            }
        }
    }
    
    public static FloatBuffer buildBuffer(final float[] src) {
        final FloatBuffer floatBuffer = ByteBuffer.allocateDirect(src.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(src).position(0);
        return floatBuffer;
    }
    
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        this.runPendingOnDrawTasks();
        if (this.isPreventRendering()) {
            this.inputFramebufferUnlock();
            return;
        }
        SelesContext.setActiveShaderProgram(this.mFilterProgram);
        final TuSdkSize sizeOfFBO = this.sizeOfFBO();
        final SelesFramebufferCache sharedFramebufferCache = SelesContext.sharedFramebufferCache();
        if (sharedFramebufferCache == null) {
            return;
        }
        (this.mOutputFramebuffer = sharedFramebufferCache.fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, sizeOfFBO, this.getOutputTextureOptions())).activateFramebuffer();
        if (this.mUsingNextFrameForImageCapture) {
            this.mOutputFramebuffer.lock();
        }
        this.setUniformsForProgramAtIndex(0);
        GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
        GLES20.glClear(16384);
        this.inputFramebufferBindTexture();
        GLES20.glEnableVertexAttribArray(this.mFilterPositionAttribute);
        GLES20.glEnableVertexAttribArray(this.mFilterTextureCoordinateAttribute);
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, (Buffer)floatBuffer);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)floatBuffer2);
        GLES20.glDrawArrays(5, 0, 4);
        this.inputFramebufferUnlock();
        this.cacaptureImageBuffer();
    }
    
    protected void cacaptureImageBuffer() {
        if (this.framebufferForOutput() == null || !this.mUsingNextFrameForImageCapture) {
            return;
        }
        this.framebufferForOutput().captureImageBufferFromFramebufferContents();
        this.mImageCaptureSemaphore.signal();
    }
    
    protected void inputFramebufferUnlock() {
        if (this.mFirstInputFramebuffer == null) {
            return;
        }
        this.mFirstInputFramebuffer.unlock();
    }
    
    protected void inputFramebufferBindTexture() {
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, (this.mFirstInputFramebuffer == null) ? 0 : this.mFirstInputFramebuffer.getTexture());
        GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
    }
    
    protected void informTargetsAboutNewFrame(final long n) {
        if (this.getFrameProcessingDelegate() != null) {
            this.getFrameProcessingDelegate().onFrameCompletion(this, n);
        }
        this.e.clear();
        for (final SelesContext.SelesInput selesInput : this.mTargets) {
            if (!selesInput.isEnabled()) {
                continue;
            }
            if (selesInput == this.getTargetToIgnoreForUpdates()) {
                continue;
            }
            final int intValue = this.mTargetTextureIndices.get(this.mTargets.indexOf(selesInput));
            this.e.put(selesInput, intValue);
            this.setInputFramebufferForTarget(selesInput, intValue);
            selesInput.setInputSize(this.outputFrameSize(), intValue);
        }
        if (this.framebufferForOutput() != null) {
            this.framebufferForOutput().unlock();
        }
        if (!this.mUsingNextFrameForImageCapture) {
            this.removeOutputFramebuffer();
        }
        for (final Map.Entry<SelesContext.SelesInput, Integer> entry : this.e.entrySet()) {
            entry.getKey().newFrameReady(n, entry.getValue());
        }
    }
    
    public TuSdkSize outputFrameSize() {
        return this.mInputTextureSize;
    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        if (this.mFirstInputFramebuffer == null) {
            return;
        }
        this.mTextureBuffer.clear();
        this.mTextureBuffer.put(textureCoordinates(this.mInputRotation)).position(0);
        this.renderToTexture(this.mVerticesBuffer, this.mTextureBuffer);
        this.informTargetsAboutNewFrame(n);
    }
    
    @Override
    public int nextAvailableTextureIndex() {
        return 0;
    }
    
    @Override
    public void setInputFramebuffer(final SelesFramebuffer mFirstInputFramebuffer, final int n) {
        if (mFirstInputFramebuffer == null) {
            return;
        }
        (this.mFirstInputFramebuffer = mFirstInputFramebuffer).lock();
    }
    
    public TuSdkSize rotatedSize(final TuSdkSize tuSdkSize, final int n) {
        final TuSdkSize copy = tuSdkSize.copy();
        if (this.mInputRotation.isTransposed()) {
            copy.width = tuSdkSize.height;
            copy.height = tuSdkSize.width;
        }
        return copy;
    }
    
    public PointF rotatedPoint(final PointF pointF, final ImageOrientation imageOrientation) {
        final PointF pointF2 = new PointF();
        switch (imageOrientation.ordinal()) {
            case 8: {
                return pointF;
            }
            case 4: {
                pointF2.x = 1.0f - pointF.x;
                pointF2.y = pointF.y;
                break;
            }
            case 3: {
                pointF2.x = pointF.x;
                pointF2.y = 1.0f - pointF.y;
                break;
            }
            case 1: {
                pointF2.x = 1.0f - pointF.y;
                pointF2.y = pointF.x;
                break;
            }
            case 2: {
                pointF2.x = pointF.y;
                pointF2.y = 1.0f - pointF.x;
                break;
            }
            case 5: {
                pointF2.x = pointF.y;
                pointF2.y = pointF.x;
                break;
            }
            case 6: {
                pointF2.x = 1.0f - pointF.y;
                pointF2.y = 1.0f - pointF.x;
                break;
            }
            case 7: {
                pointF2.x = 1.0f - pointF.x;
                pointF2.y = 1.0f - pointF.y;
                break;
            }
        }
        return pointF2;
    }
    
    @Override
    public void setInputSize(TuSdkSize tuSdkSize, final int n) {
        if (this.isPreventRendering()) {
            return;
        }
        if (tuSdkSize != null) {
            tuSdkSize = tuSdkSize.scale(this.getScale());
        }
        if (this.mOverrideInputSize) {
            if (this.mForcedMaximumSize == null || this.mForcedMaximumSize.minSide() < 1) {
                this.setupFilterForSize(this.sizeOfFBO());
                return;
            }
            tuSdkSize = TuSdkSize.create(RectHelper.makeRectWithAspectRatioInsideRect(tuSdkSize, new Rect(0, 0, this.mForcedMaximumSize.width, this.mForcedMaximumSize.height)));
        }
        final TuSdkSize rotatedSize = this.rotatedSize(tuSdkSize, n);
        if (rotatedSize.minSide() < 1) {
            this.mInputTextureSize = rotatedSize;
        }
        else if (!rotatedSize.equals(this.mInputTextureSize)) {
            this.mInputTextureSize = rotatedSize;
        }
        this.setupFilterForSize(this.sizeOfFBO());
    }
    
    @Override
    public void setInputRotation(final ImageOrientation mInputRotation, final int n) {
        this.mInputRotation = mInputRotation;
    }
    
    @Override
    public void forceProcessingAtSize(final TuSdkSize tuSdkSize) {
        if (tuSdkSize != null && tuSdkSize.minSide() > 0) {
            this.mOverrideInputSize = true;
            this.mInputTextureSize = tuSdkSize.copy();
            this.mForcedMaximumSize = new TuSdkSize();
        }
        else {
            this.mOverrideInputSize = false;
        }
    }
    
    @Override
    public void forceProcessingAtSizeRespectingAspectRatio(final TuSdkSize tuSdkSize) {
        if (tuSdkSize != null && tuSdkSize.minSide() > 0) {
            this.mOverrideInputSize = true;
            this.mForcedMaximumSize = tuSdkSize.copy();
        }
        else {
            this.mOverrideInputSize = false;
            this.mInputTextureSize = new TuSdkSize();
            this.mForcedMaximumSize = new TuSdkSize();
        }
    }
    
    @Override
    public TuSdkSize maximumOutputSize() {
        return new TuSdkSize();
    }
    
    @Override
    public void endProcessing() {
        if (!this.mIsEndProcessing) {
            this.mIsEndProcessing = true;
            final Iterator<SelesContext.SelesInput> iterator = this.mTargets.iterator();
            while (iterator.hasNext()) {
                iterator.next().endProcessing();
            }
        }
    }
    
    @Override
    public boolean wantsMonochromeInput() {
        return false;
    }
    
    protected void checkGLError(final String s) {
        try {
            TuSdkMonitor.glMonitor().checkGL(s);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    protected void captureFilterImage(final String str, final int n, final int n2) {
        try {
            this.checkGLError(str + " captureFilterImage");
            TuSdkMonitor.glMonitor().checkGLFrameImage(str, n, n2);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void setBackgroundColor(final float mBackgroundColorRed, final float mBackgroundColorGreen, final float mBackgroundColorBlue, final float mBackgroundColorAlpha) {
        this.mBackgroundColorRed = mBackgroundColorRed;
        this.mBackgroundColorGreen = mBackgroundColorGreen;
        this.mBackgroundColorBlue = mBackgroundColorBlue;
        this.mBackgroundColorAlpha = mBackgroundColorAlpha;
    }
    
    public void setInteger(final int n, final String s) {
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesFilter.this.setInteger(n, SelesFilter.this.mFilterProgram.uniformIndex(s), SelesFilter.this.mFilterProgram);
            }
        });
    }
    
    public void setFloat(final float n, final String s) {
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesFilter.this.setFloat(n, SelesFilter.this.mFilterProgram.uniformIndex(s), SelesFilter.this.mFilterProgram);
            }
        });
    }
    
    public void setSize(final TuSdkSizeF tuSdkSizeF, final String s) {
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesFilter.this.setSize(tuSdkSizeF, SelesFilter.this.mFilterProgram.uniformIndex(s), SelesFilter.this.mFilterProgram);
            }
        });
    }
    
    public void setPoint(final PointF pointF, final String s) {
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesFilter.this.setPoint(pointF, SelesFilter.this.mFilterProgram.uniformIndex(s), SelesFilter.this.mFilterProgram);
            }
        });
    }
    
    public void setFloatVec3(final float[] array, final String s) {
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesFilter.this.setVec3(array, SelesFilter.this.mFilterProgram.uniformIndex(s), SelesFilter.this.mFilterProgram);
            }
        });
    }
    
    public void setFloatVec4(final float[] array, final String s) {
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesFilter.this.setVec4(array, SelesFilter.this.mFilterProgram.uniformIndex(s), SelesFilter.this.mFilterProgram);
            }
        });
    }
    
    public void setFloatArray(final float[] array, final String s) {
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesFilter.this.setFloatArray(array, SelesFilter.this.mFilterProgram.uniformIndex(s), SelesFilter.this.mFilterProgram);
            }
        });
    }
    
    public void setMatrix3f(final float[] array, final int n, final SelesGLProgram selesGLProgram) {
        if (selesGLProgram == null) {
            return;
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesContext.setActiveShaderProgram(selesGLProgram);
                SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(n, selesGLProgram, new Runnable() {
                    @Override
                    public void run() {
                        GLES20.glUniformMatrix3fv(n, 1, false, array, 0);
                    }
                });
            }
        });
    }
    
    public void setMatrix4f(final float[] array, final int n, final SelesGLProgram selesGLProgram) {
        if (selesGLProgram == null) {
            return;
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesContext.setActiveShaderProgram(selesGLProgram);
                SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(n, selesGLProgram, new Runnable() {
                    @Override
                    public void run() {
                        GLES20.glUniformMatrix4fv(n, 1, false, array, 0);
                    }
                });
            }
        });
    }
    
    public void setFloat(final float n, final int n2, final SelesGLProgram selesGLProgram) {
        if (selesGLProgram == null) {
            return;
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesContext.setActiveShaderProgram(selesGLProgram);
                SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(n2, selesGLProgram, new Runnable() {
                    @Override
                    public void run() {
                        GLES20.glUniform1f(n2, n);
                    }
                });
            }
        });
    }
    
    public void setPoint(final PointF pointF, final int n, final SelesGLProgram selesGLProgram) {
        this.setVec2(new float[] { pointF.x, pointF.y }, n, selesGLProgram);
    }
    
    public void setSize(final TuSdkSizeF tuSdkSizeF, final int n, final SelesGLProgram selesGLProgram) {
        this.setVec2(new float[] { tuSdkSizeF.width, tuSdkSizeF.height }, n, selesGLProgram);
    }
    
    public void setVec2(final float[] array, final int n, final SelesGLProgram selesGLProgram) {
        if (selesGLProgram == null) {
            return;
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesContext.setActiveShaderProgram(selesGLProgram);
                SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(n, selesGLProgram, new Runnable() {
                    @Override
                    public void run() {
                        GLES20.glUniform2fv(n, 1, array, 0);
                    }
                });
            }
        });
    }
    
    public void setVec3(final float[] array, final int n, final SelesGLProgram selesGLProgram) {
        if (selesGLProgram == null) {
            return;
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesContext.setActiveShaderProgram(selesGLProgram);
                SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(n, selesGLProgram, new Runnable() {
                    @Override
                    public void run() {
                        GLES20.glUniform3fv(n, 1, array, 0);
                    }
                });
            }
        });
    }
    
    public void setVec4(final float[] array, final int n, final SelesGLProgram selesGLProgram) {
        if (selesGLProgram == null) {
            return;
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesContext.setActiveShaderProgram(selesGLProgram);
                SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(n, selesGLProgram, new Runnable() {
                    @Override
                    public void run() {
                        GLES20.glUniform4fv(n, 1, array, 0);
                    }
                });
            }
        });
    }
    
    public void setFloatArray(final float[] array, final int n, final SelesGLProgram selesGLProgram) {
        if (selesGLProgram == null) {
            return;
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesContext.setActiveShaderProgram(selesGLProgram);
                SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(n, selesGLProgram, new Runnable() {
                    @Override
                    public void run() {
                        GLES20.glUniform1fv(n, array.length, array, 0);
                    }
                });
            }
        });
    }
    
    public void setInteger(final int n, final int n2, final SelesGLProgram selesGLProgram) {
        if (selesGLProgram == null) {
            return;
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesContext.setActiveShaderProgram(selesGLProgram);
                SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(n2, selesGLProgram, new Runnable() {
                    @Override
                    public void run() {
                        GLES20.glUniform1i(n2, n);
                    }
                });
            }
        });
    }
    
    protected void setAndExecuteUniformStateCallbackAtIndex(final int i, final SelesGLProgram selesGLProgram, final Runnable runnable) {
        if (runnable == null) {
            return;
        }
        this.mUniformStateRestorationBlocks.put(i, runnable);
        runnable.run();
    }
    
    protected void setUniformsForProgramAtIndex(final int n) {
        final Iterator<Runnable> iterator = this.mUniformStateRestorationBlocks.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().run();
        }
    }
    
    static {
        noRotationTextureCoordinates = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };
        rotateLeftTextureCoordinates = new float[] { 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f };
        rotateRightTextureCoordinates = new float[] { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f };
        verticalFlipTextureCoordinates = new float[] { 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f };
        horizontalFlipTextureCoordinates = new float[] { 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        rotateRightVerticalFlipTextureCoordinates = new float[] { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f };
        rotateRightHorizontalFlipTextureCoordinates = new float[] { 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f };
        rotate180TextureCoordinates = new float[] { 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f };
        imageVertices = new float[] { -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f };
    }
    
    public interface FrameProcessingDelegate
    {
        void onFrameCompletion(final SelesFilter p0, final long p1);
    }
}
