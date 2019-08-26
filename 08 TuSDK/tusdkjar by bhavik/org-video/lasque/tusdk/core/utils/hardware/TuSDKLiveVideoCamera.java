package org.lasque.tusdk.core.utils.hardware;

import android.content.Context;
import android.graphics.ImageFormat;
import android.widget.RelativeLayout;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lasque.tusdk.core.encoder.video.TuSDKSoftVideoDataEncoderInterface;
import org.lasque.tusdk.core.secret.ColorSpaceConvert;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.output.SelesOffscreen;
import org.lasque.tusdk.core.seles.output.SelesOffscreen.SelesOffscreenDelegate;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.video.SelesSurfaceEncoderInterface;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.type.ColorFormatType;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.video.TuSDKVideoCaptureSetting;
import org.lasque.tusdk.core.video.TuSDKVideoCaptureSetting.AVCodecType;
import org.lasque.tusdk.impl.components.camera.TuVideoFocusTouchView;

public class TuSDKLiveVideoCamera
  extends TuSDKVideoCamera
{
  private SelesOffscreen a;
  private ByteBuffer b;
  private TuSDKLiveVideoCameraDelegate c;
  private SelesOffscreen.SelesOffscreenDelegate d = new SelesOffscreen.SelesOffscreenDelegate()
  {
    public boolean onFrameRendered(SelesOffscreen paramAnonymousSelesOffscreen)
    {
      if (!TuSDKLiveVideoCamera.this.isRecording()) {
        return false;
      }
      IntBuffer localIntBuffer1 = paramAnonymousSelesOffscreen.renderBuffer();
      if (localIntBuffer1 == null) {
        return true;
      }
      IntBuffer localIntBuffer2 = localIntBuffer1;
      if (TuSDKLiveVideoCamera.this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) {
        return true;
      }
      Object localObject;
      if (TuSDKLiveVideoCamera.c(TuSDKLiveVideoCamera.this) == null)
      {
        localObject = TuSDKLiveVideoCamera.this.getVideoCaptureSetting().videoSize;
        int i = ((TuSdkSize)localObject).width * ((TuSdkSize)localObject).height * ImageFormat.getBitsPerPixel(17) / 8;
        TuSDKLiveVideoCamera.a(TuSDKLiveVideoCamera.this, ByteBuffer.allocate(i));
      }
      TuSDKLiveVideoCamera.c(TuSDKLiveVideoCamera.this).position(0);
      if (TuSDKLiveVideoCamera.this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.SW_CODEC)
      {
        TuSDKLiveVideoCamera.a(TuSDKLiveVideoCamera.this, localIntBuffer2, TuSDKLiveVideoCamera.c(TuSDKLiveVideoCamera.this).array(), ColorFormatType.NV21);
        localObject = (TuSDKSoftVideoDataEncoderInterface)TuSDKLiveVideoCamera.this.getVideoDataEncoder();
        if (localObject != null) {
          ((TuSDKSoftVideoDataEncoderInterface)localObject).queueVideo(TuSDKLiveVideoCamera.c(TuSDKLiveVideoCamera.this).array());
        }
      }
      else
      {
        localObject = TuSDKLiveVideoCamera.this.getVideoCaptureSetting().imageFormatType;
        TuSDKLiveVideoCamera.a(TuSDKLiveVideoCamera.this, localIntBuffer2, TuSDKLiveVideoCamera.c(TuSDKLiveVideoCamera.this).array(), (ColorFormatType)localObject);
        ThreadHelper.post(new Runnable()
        {
          public void run()
          {
            if (TuSDKLiveVideoCamera.this.getFrameDelegate() != null) {
              TuSDKLiveVideoCamera.this.getFrameDelegate().onFrameDataAvailable(TuSDKLiveVideoCamera.c(TuSDKLiveVideoCamera.this).array());
            }
          }
        });
      }
      return true;
    }
  };
  
  public TuSDKLiveVideoCamera(Context paramContext, RelativeLayout paramRelativeLayout)
  {
    super(paramContext, new TuSDKVideoCaptureSetting(), paramRelativeLayout);
  }
  
  public TuSDKLiveVideoCamera(Context paramContext, TuSDKVideoCaptureSetting paramTuSDKVideoCaptureSetting, RelativeLayout paramRelativeLayout)
  {
    super(paramContext, paramTuSDKVideoCaptureSetting, paramRelativeLayout);
  }
  
  public TuSDKLiveVideoCamera(Context paramContext, TuSDKVideoCaptureSetting paramTuSDKVideoCaptureSetting, RelativeLayout paramRelativeLayout, Boolean paramBoolean1, Boolean paramBoolean2)
  {
    super(paramContext, paramTuSDKVideoCaptureSetting, paramRelativeLayout, paramBoolean1, paramBoolean2);
  }
  
  public TuSDKLiveVideoCameraDelegate getFrameDelegate()
  {
    return this.c;
  }
  
  public void setFrameDelegate(TuSDKLiveVideoCameraDelegate paramTuSDKLiveVideoCameraDelegate)
  {
    this.c = paramTuSDKLiveVideoCameraDelegate;
  }
  
  protected void initCamera()
  {
    super.initCamera();
    setEnableFaceTrace(true);
    setEnableLiveSticker(false);
  }
  
  protected void onCameraStarted()
  {
    super.onCameraStarted();
    StatisticsManger.appendComponent(9437184L);
  }
  
  public void initOutputSettings()
  {
    super.initOutputSettings();
    TuVideoFocusTouchView localTuVideoFocusTouchView = new TuVideoFocusTouchView(getContext());
    setFocusTouchView(localTuVideoFocusTouchView);
    setEnableAudioCapture(true);
  }
  
  protected void updateOutputFilterSettings()
  {
    super.updateOutputFilterSettings();
    boolean bool = (isDisableMirrorFrontFacing()) && (isFrontFacingCameraPresent()) && (isHorizontallyMirrorFrontFacingCamera());
    if (getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC)
    {
      if (this.a != null) {
        this.a.setEnableHorizontallyFlip(bool);
      }
    }
    else
    {
      SelesSurfaceEncoderInterface localSelesSurfaceEncoderInterface = (SelesSurfaceEncoderInterface)getVideoDataEncoder();
      if (localSelesSurfaceEncoderInterface != null) {
        localSelesSurfaceEncoderInterface.setEnableHorizontallyFlip(bool);
      }
    }
  }
  
  private boolean a()
  {
    if (!SdkValid.shared.videoStreamEnabled())
    {
      TLog.e("The video streaming feature has been expired, please contact us via http://tusdk.com", new Object[0]);
      return false;
    }
    return true;
  }
  
  public void switchFilter(String paramString)
  {
    if ((paramString == null) || (isFilterChanging()) || (this.mFilterWrap.equalsCode(paramString))) {
      return;
    }
    if ((!FilterManager.shared().isFilterEffect(paramString)) || (!a())) {
      return;
    }
    int i = FilterManager.shared().getGroupTypeByCode(paramString);
    if ((!FilterManager.shared().isNormalFilter(paramString)) && (i != 1))
    {
      TLog.d("Only live video filter [%s] could be used here, please visit http://tusdk.com.", new Object[] { paramString });
      return;
    }
    super.switchFilter(paramString);
  }
  
  protected void applyFilterWrap(FilterWrap paramFilterWrap)
  {
    super.applyFilterWrap(paramFilterWrap);
    if ((getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) && (this.a != null)) {
      paramFilterWrap.getFilter().addTarget(this.a);
    }
  }
  
  public void stopCameraCapture()
  {
    super.stopCameraCapture();
    StatisticsManger.appendComponent(9437185L);
  }
  
  protected void startVideoDataEncoder()
  {
    super.startVideoDataEncoder();
    if (getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC)
    {
      c();
      b().startWork();
      updateOutputFilter();
    }
  }
  
  protected void stopVideoDataEncoder()
  {
    super.stopVideoDataEncoder();
    if (getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC)
    {
      if (this.a == null) {
        return;
      }
      runOnDraw(new Runnable()
      {
        public void run()
        {
          TuSDKLiveVideoCamera.a(TuSDKLiveVideoCamera.this);
        }
      });
    }
  }
  
  private SelesOffscreen b()
  {
    if (this.a == null)
    {
      this.a = new SelesOffscreen();
      this.a.setDelegate(this.d);
      this.a.setOutputSize(getVideoCaptureSetting().videoSize);
    }
    return this.a;
  }
  
  private void c()
  {
    if (this.a != null)
    {
      this.a.setEnabled(false);
      this.a.destroy();
      this.a.setDelegate(null);
      this.a = null;
    }
  }
  
  public void startRecording()
  {
    if (isRecording()) {
      return;
    }
    if (getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC)
    {
      b().startWork();
      updateOutputFilter();
    }
    super.startRecording();
  }
  
  public void stopRecording()
  {
    if ((getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) && (this.a != null)) {
      runOnDraw(new Runnable()
      {
        public void run()
        {
          TuSDKLiveVideoCamera.b(TuSDKLiveVideoCamera.this).setEnabled(false);
        }
      });
    }
    super.stopRecording();
  }
  
  private Boolean a(IntBuffer paramIntBuffer, byte[] paramArrayOfByte, ColorFormatType paramColorFormatType)
  {
    if ((paramIntBuffer == null) || (paramArrayOfByte == null)) {
      return Boolean.valueOf(false);
    }
    TuSdkSize localTuSdkSize = getVideoCaptureSetting().videoSize;
    int i = localTuSdkSize.width * localTuSdkSize.height * ImageFormat.getBitsPerPixel(17) / 8;
    if (i != paramArrayOfByte.length)
    {
      TLog.e("bytes size not equal: %d, %d", new Object[] { Integer.valueOf(i), Integer.valueOf(paramArrayOfByte.length) });
      return Boolean.valueOf(false);
    }
    if (paramColorFormatType == ColorFormatType.NV21) {
      ColorSpaceConvert.rgbaToNv21(paramIntBuffer.array(), localTuSdkSize.width, localTuSdkSize.height, paramArrayOfByte);
    } else if (paramColorFormatType == ColorFormatType.YV12) {
      ColorSpaceConvert.rgbaToYv12(paramIntBuffer.array(), localTuSdkSize.width, localTuSdkSize.height, paramArrayOfByte);
    } else if (paramColorFormatType == ColorFormatType.I420) {
      ColorSpaceConvert.rgbaToI420(paramIntBuffer.array(), localTuSdkSize.width, localTuSdkSize.height, paramArrayOfByte);
    }
    paramIntBuffer = null;
    return Boolean.valueOf(true);
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    c();
    if (this.b != null)
    {
      this.b.clear();
      this.b = null;
    }
  }
  
  public static abstract interface TuSDKLiveVideoCameraDelegate
  {
    public abstract void onFrameDataAvailable(byte[] paramArrayOfByte);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\utils\hardware\TuSDKLiveVideoCamera.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */