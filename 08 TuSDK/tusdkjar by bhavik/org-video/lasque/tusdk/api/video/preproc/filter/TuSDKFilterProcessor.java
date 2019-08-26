package org.lasque.tusdk.api.video.preproc.filter;

import android.opengl.GLES20;
import java.nio.IntBuffer;
import org.lasque.tusdk.core.secret.ColorSpaceConvert;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.output.SelesSurfaceTextureOutput;
import org.lasque.tusdk.core.seles.sources.VideoFilterDelegate;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.type.ColorFormatType;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSDKFilterProcessor
  extends TuSDKVideoProcesser
{
  protected IntBuffer mGLRgbBuffer;
  protected ColorFormatType mInputImageFormatType;
  protected boolean mAutoCreateGLContext = false;
  private TuSDKFilterProcessorDelegate a;
  private TuSDKGLContextMaker b;
  
  public TuSDKFilterProcessor(ColorFormatType paramColorFormatType)
  {
    this.mInputImageFormatType = paramColorFormatType;
  }
  
  public TuSDKFilterProcessor()
  {
    this(ColorFormatType.NV21);
  }
  
  public void init(final TuSdkSize paramTuSdkSize)
  {
    if (this.mGLRgbBuffer != null) {
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        TuSDKFilterProcessor.this.reset();
        TuSDKFilterProcessor.this.setInputImageSize(paramTuSdkSize);
        TuSDKFilterProcessor.a(TuSDKFilterProcessor.this);
        TuSDKFilterProcessor.b(TuSDKFilterProcessor.this);
      }
    });
  }
  
  protected void reset()
  {
    super.reset();
    if (this.mGLRgbBuffer != null)
    {
      this.mGLRgbBuffer.clear();
      this.mGLRgbBuffer = null;
    }
    if (this.b != null)
    {
      this.b.destory();
      this.b = null;
    }
  }
  
  private void a()
  {
    if ((this.mAutoCreateGLContext) && (this.b == null))
    {
      this.b = new TuSDKGLContextMaker();
      this.b.bindGLContext(this.mInputTextureSize, null);
    }
    SelesContext.createEGLContext(SelesContext.currentEGLContext());
  }
  
  public void setDelegate(TuSDKFilterProcessorDelegate paramTuSDKFilterProcessorDelegate)
  {
    this.a = paramTuSDKFilterProcessorDelegate;
  }
  
  public TuSDKFilterProcessorDelegate getDelegate()
  {
    return this.a;
  }
  
  public void setAutoCreateGLContext(boolean paramBoolean)
  {
    this.mAutoCreateGLContext = paramBoolean;
  }
  
  public void setInputImageSize(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize()) || (paramTuSdkSize.equals(this.mInputTextureSize))) {
      return;
    }
    super.setInputImageSize(paramTuSdkSize);
    int i = this.mInputTextureSize.width * this.mInputTextureSize.height;
    this.mGLRgbBuffer = IntBuffer.allocate(i);
  }
  
  public TuSdkSize getOutputImageSize()
  {
    TuSdkSize localTuSdkSize = new TuSdkSize(this.mInputTextureSize.width, this.mInputTextureSize.height);
    if ((getOutputRotation() != null) && (getOutputRotation().isTransposed()))
    {
      localTuSdkSize.width = this.mInputTextureSize.height;
      localTuSdkSize.height = this.mInputTextureSize.width;
    }
    return localTuSdkSize;
  }
  
  protected void convertRGBAToYUV(int[] paramArrayOfInt, byte[] paramArrayOfByte, ColorFormatType paramColorFormatType)
  {
    if (paramColorFormatType == ColorFormatType.I420) {
      ColorSpaceConvert.rgbaToI420(paramArrayOfInt, this.mInputTextureSize.width, this.mInputTextureSize.height, paramArrayOfByte);
    } else if (paramColorFormatType == ColorFormatType.NV21) {
      ColorSpaceConvert.rgbaToNv21(paramArrayOfInt, this.mInputTextureSize.width, this.mInputTextureSize.height, paramArrayOfByte);
    } else {
      ColorSpaceConvert.rgbaToYv12(paramArrayOfInt, this.mInputTextureSize.width, this.mInputTextureSize.height, paramArrayOfByte);
    }
  }
  
  protected void convertYUVFrameDataToRGBA(byte[] paramArrayOfByte, int[] paramArrayOfInt, ColorFormatType paramColorFormatType)
  {
    if (paramColorFormatType == ColorFormatType.NV21) {
      ColorSpaceConvert.nv21ToRgba(paramArrayOfByte, this.mInputTextureSize.width, this.mInputTextureSize.height, paramArrayOfInt);
    } else {
      TLog.e("%d format is not supported ", new Object[] { paramColorFormatType });
    }
  }
  
  protected void processVideoSampleBuffer(byte[] paramArrayOfByte)
  {
    this.mGLRgbBuffer.clear();
    convertYUVFrameDataToRGBA(paramArrayOfByte, this.mGLRgbBuffer.array(), this.mInputImageFormatType);
    activateFramebuffer();
    GLES20.glBindTexture(3553, this.mOutputFramebuffer.getTexture());
    GLES20.glTexSubImage2D(3553, 0, 0, 0, this.mInputTextureSize.width, this.mInputTextureSize.height, 6408, 5121, this.mGLRgbBuffer);
    GLES20.glBindTexture(3553, 0);
  }
  
  public void onFilterChanged(FilterWrap paramFilterWrap)
  {
    super.onFilterChanged(paramFilterWrap);
    if ((getDelegate() != null) && (this.mFilterWrap != null)) {
      getDelegate().onFilterChanged(this.mFilterWrap);
    }
  }
  
  protected void updateOutputFilterOutputOrientation()
  {
    if (this.mOutputFilter == null) {
      return;
    }
    if (this.mIsOutputOriginalImageOrientation)
    {
      if (this.mInputImageOrientation != null)
      {
        if (this.mInputImageOrientation.isMirrored()) {
          this.mOutputFilter.setOutputOrientation(this.mInputImageOrientation);
        } else if (this.mInputImageOrientation == ImageOrientation.Right) {
          this.mOutputFilter.setOutputOrientation(ImageOrientation.Left);
        } else if (this.mInputImageOrientation == ImageOrientation.Down) {
          this.mOutputFilter.setOutputOrientation(ImageOrientation.Down);
        } else if (this.mInputImageOrientation == ImageOrientation.Left) {
          this.mOutputFilter.setOutputOrientation(ImageOrientation.Right);
        }
      }
      else if (this.mCameraFacing == CameraConfigs.CameraFacing.Front) {
        this.mOutputFilter.setOutputOrientation(this.mHorizontallyMirrorFrontFacingCamera ? ImageOrientation.LeftMirrored : ImageOrientation.Left);
      } else {
        this.mOutputFilter.setOutputOrientation(this.mHorizontallyMirrorRearFacingCamera ? ImageOrientation.LeftMirrored : ImageOrientation.Left);
      }
    }
    else {
      this.mOutputFilter.setOutputOrientation(this.mOutputImageOrientation);
    }
    this.mOutputFilter.forceProcessingAtSize(this.mInputTextureSize);
  }
  
  public void processData(byte[] paramArrayOfByte, long paramLong)
  {
    if (this.mInputImageFormatType != ColorFormatType.NV21)
    {
      TLog.e("%s format is not supported ", new Object[] { this.mInputImageFormatType });
      return;
    }
    runPendingOnDrawTasks();
    processVideoSampleBuffer(paramArrayOfByte);
    updateTargetsForVideoCameraUsingCacheTexture(paramLong);
    IntBuffer localIntBuffer = this.mOutputFilter.readImageBuffer();
    GLES20.glBindFramebuffer(36160, 0);
    convertRGBAToYUV(localIntBuffer.array(), paramArrayOfByte, this.mInputImageFormatType);
  }
  
  public void processData(byte[] paramArrayOfByte)
  {
    processData(paramArrayOfByte, System.nanoTime());
  }
  
  public int processDataToTexture(byte[] paramArrayOfByte, long paramLong)
  {
    if (this.mInputImageFormatType != ColorFormatType.NV21)
    {
      TLog.e("%s format is not supported ", new Object[] { this.mInputImageFormatType });
      return -1;
    }
    runPendingOnDrawTasks();
    processVideoSampleBuffer(paramArrayOfByte);
    updateTargetsForVideoCameraUsingCacheTexture(paramLong);
    int i = this.mOutputFilter.framebufferForOutput().getTexture();
    GLES20.glBindFramebuffer(36160, 0);
    return i;
  }
  
  public int processDataToTexture(byte[] paramArrayOfByte)
  {
    return processDataToTexture(paramArrayOfByte, System.nanoTime());
  }
  
  protected void onDestroy()
  {
    reset();
  }
  
  public static abstract interface TuSDKFilterProcessorDelegate
    extends VideoFilterDelegate
  {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\video\preproc\filter\TuSDKFilterProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */