// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.video.preproc.filter;

//import org.lasque.tusdk.core.seles.sources.VideoFilterDelegate;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.secret.ColorSpaceConvert;
//import org.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.ColorSpaceConvert;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ColorFormatType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources.VideoFilterDelegate;

//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.type.ColorFormatType;
import java.nio.IntBuffer;

public class TuSDKFilterProcessor extends TuSDKVideoProcesser
{
    protected IntBuffer mGLRgbBuffer;
    protected ColorFormatType mInputImageFormatType;
    protected boolean mAutoCreateGLContext;
    private TuSDKFilterProcessorDelegate a;
    private TuSDKGLContextMaker b;
    
    public TuSDKFilterProcessor(final ColorFormatType mInputImageFormatType) {
        this.mAutoCreateGLContext = false;
        this.mInputImageFormatType = mInputImageFormatType;
    }
    
    public TuSDKFilterProcessor() {
        this(ColorFormatType.NV21);
    }
    
    public void init(final TuSdkSize tuSdkSize) {
        if (this.mGLRgbBuffer != null) {
            return;
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                TuSDKFilterProcessor.this.reset();
                TuSDKFilterProcessor.this.setInputImageSize(tuSdkSize);
                TuSDKFilterProcessor.this.a();
                init();
            }
        });
    }
    
    @Override
    protected void reset() {
        super.reset();
        if (this.mGLRgbBuffer != null) {
            this.mGLRgbBuffer.clear();
            this.mGLRgbBuffer = null;
        }
        if (this.b != null) {
            this.b.destory();
            this.b = null;
        }
    }
    
    private void a() {
        if (this.mAutoCreateGLContext && this.b == null) {
            (this.b = new TuSDKGLContextMaker()).bindGLContext(this.mInputTextureSize, null);
        }
        SelesContext.createEGLContext(SelesContext.currentEGLContext());
    }
    
    public void setDelegate(final TuSDKFilterProcessorDelegate a) {
        this.a = a;
    }
    
    public TuSDKFilterProcessorDelegate getDelegate() {
        return this.a;
    }
    
    public void setAutoCreateGLContext(final boolean mAutoCreateGLContext) {
        this.mAutoCreateGLContext = mAutoCreateGLContext;
    }
    
    @Override
    public void setInputImageSize(final TuSdkSize inputImageSize) {
        if (inputImageSize == null || !inputImageSize.isSize() || inputImageSize.equals((Object)this.mInputTextureSize)) {
            return;
        }
        super.setInputImageSize(inputImageSize);
        this.mGLRgbBuffer = IntBuffer.allocate(this.mInputTextureSize.width * this.mInputTextureSize.height);
    }
    
    @Override
    public TuSdkSize getOutputImageSize() {
        final TuSdkSize tuSdkSize = new TuSdkSize(this.mInputTextureSize.width, this.mInputTextureSize.height);
        if (this.getOutputRotation() != null && this.getOutputRotation().isTransposed()) {
            tuSdkSize.width = this.mInputTextureSize.height;
            tuSdkSize.height = this.mInputTextureSize.width;
        }
        return tuSdkSize;
    }
    
    protected void convertRGBAToYUV(final int[] array, final byte[] array2, final ColorFormatType colorFormatType) {
        if (colorFormatType == ColorFormatType.I420) {
            ColorSpaceConvert.rgbaToI420(array, this.mInputTextureSize.width, this.mInputTextureSize.height, array2);
        }
        else if (colorFormatType == ColorFormatType.NV21) {
            ColorSpaceConvert.rgbaToNv21(array, this.mInputTextureSize.width, this.mInputTextureSize.height, array2);
        }
        else {
            ColorSpaceConvert.rgbaToYv12(array, this.mInputTextureSize.width, this.mInputTextureSize.height, array2);
        }
    }
    
    protected void convertYUVFrameDataToRGBA(final byte[] array, final int[] array2, final ColorFormatType colorFormatType) {
        if (colorFormatType == ColorFormatType.NV21) {
            ColorSpaceConvert.nv21ToRgba(array, this.mInputTextureSize.width, this.mInputTextureSize.height, array2);
        }
        else {
            TLog.e("%d format is not supported ", new Object[] { colorFormatType });
        }
    }
    
    protected void processVideoSampleBuffer(final byte[] array) {
        this.mGLRgbBuffer.clear();
        this.convertYUVFrameDataToRGBA(array, this.mGLRgbBuffer.array(), this.mInputImageFormatType);
        this.activateFramebuffer();
        GLES20.glBindTexture(3553, this.mOutputFramebuffer.getTexture());
        GLES20.glTexSubImage2D(3553, 0, 0, 0, this.mInputTextureSize.width, this.mInputTextureSize.height, 6408, 5121, (Buffer)this.mGLRgbBuffer);
        GLES20.glBindTexture(3553, 0);
    }
    
    @Override
    public void onFilterChanged(final FilterWrap filterWrap) {
        super.onFilterChanged(filterWrap);
        if (this.getDelegate() != null && this.mFilterWrap != null) {
            this.getDelegate().onFilterChanged(this.mFilterWrap);
        }
    }
    
    @Override
    protected void updateOutputFilterOutputOrientation() {
        if (this.mOutputFilter == null) {
            return;
        }
        if (this.mIsOutputOriginalImageOrientation) {
            if (this.mInputImageOrientation != null) {
                if (this.mInputImageOrientation.isMirrored()) {
                    this.mOutputFilter.setOutputOrientation(this.mInputImageOrientation);
                }
                else if (this.mInputImageOrientation == ImageOrientation.Right) {
                    this.mOutputFilter.setOutputOrientation(ImageOrientation.Left);
                }
                else if (this.mInputImageOrientation == ImageOrientation.Down) {
                    this.mOutputFilter.setOutputOrientation(ImageOrientation.Down);
                }
                else if (this.mInputImageOrientation == ImageOrientation.Left) {
                    this.mOutputFilter.setOutputOrientation(ImageOrientation.Right);
                }
            }
            else if (this.mCameraFacing == CameraConfigs.CameraFacing.Front) {
                this.mOutputFilter.setOutputOrientation(this.mHorizontallyMirrorFrontFacingCamera ? ImageOrientation.LeftMirrored : ImageOrientation.Left);
            }
            else {
                this.mOutputFilter.setOutputOrientation(this.mHorizontallyMirrorRearFacingCamera ? ImageOrientation.LeftMirrored : ImageOrientation.Left);
            }
        }
        else {
            this.mOutputFilter.setOutputOrientation(this.mOutputImageOrientation);
        }
        this.mOutputFilter.forceProcessingAtSize(this.mInputTextureSize);
    }
    
    public void processData(final byte[] array, final long n) {
        if (this.mInputImageFormatType != ColorFormatType.NV21) {
            TLog.e("%s format is not supported ", new Object[] { this.mInputImageFormatType });
            return;
        }
        this.runPendingOnDrawTasks();
        this.processVideoSampleBuffer(array);
        this.updateTargetsForVideoCameraUsingCacheTexture(n);
        final IntBuffer imageBuffer = this.mOutputFilter.readImageBuffer();
        GLES20.glBindFramebuffer(36160, 0);
        this.convertRGBAToYUV(imageBuffer.array(), array, this.mInputImageFormatType);
    }
    
    public void processData(final byte[] array) {
        this.processData(array, System.nanoTime());
    }
    
    public int processDataToTexture(final byte[] array, final long n) {
        if (this.mInputImageFormatType != ColorFormatType.NV21) {
            TLog.e("%s format is not supported ", new Object[] { this.mInputImageFormatType });
            return -1;
        }
        this.runPendingOnDrawTasks();
        this.processVideoSampleBuffer(array);
        this.updateTargetsForVideoCameraUsingCacheTexture(n);
        final int texture = this.mOutputFilter.framebufferForOutput().getTexture();
        GLES20.glBindFramebuffer(36160, 0);
        return texture;
    }
    
    public int processDataToTexture(final byte[] array) {
        return this.processDataToTexture(array, System.nanoTime());
    }
    
    protected void onDestroy() {
        this.reset();
    }
    
    public interface TuSDKFilterProcessorDelegate extends VideoFilterDelegate
    {
    }
}
