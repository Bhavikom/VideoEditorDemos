package org.lasque.tusdk.core.encoder.video;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.Surface;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.TuSdkDeviceInfo;

@TargetApi(18)
@SuppressLint({"InlinedApi"})
public class TuSDKHardVideoDataEncoder
  implements TuSDKHardVideoDataEncoderInterface
{
  private final int a = 500;
  private Surface b;
  private MediaCodec c;
  private TuSDKVideoDataEncoderDelegate d;
  private TuSDKVideoEncoderSetting e;
  private long f = 0L;
  private long g;
  private int h;
  private int i;
  private boolean j;
  
  public void setDelegate(TuSDKVideoDataEncoderDelegate paramTuSDKVideoDataEncoderDelegate)
  {
    this.d = paramTuSDKVideoDataEncoderDelegate;
  }
  
  public TuSDKVideoDataEncoderDelegate getDelegate()
  {
    return this.d;
  }
  
  public void setDefaultVideoQuality(int paramInt1, int paramInt2)
  {
    this.h = paramInt1;
    this.i = paramInt2;
  }
  
  public TuSDKVideoEncoderSetting getVideoEncoderSetting()
  {
    if (this.e == null) {
      this.e = new TuSDKVideoEncoderSetting();
    }
    return this.e;
  }
  
  private MediaCodec a(String paramString)
  {
    String str1 = TuSdkDeviceInfo.getModel();
    String str2 = TuSdkDeviceInfo.getVender();
    if ((("OPPO".equalsIgnoreCase(str2)) && (str1.equalsIgnoreCase("PADM00"))) || (("HUAWEI".equalsIgnoreCase(str2)) && (!str1.equalsIgnoreCase("PLK-TL01H")) && (!str1.equalsIgnoreCase("HUAWEI NXT-AL10")))) {
      return MediaCodec.createByCodecName("OMX.google.h264.encoder");
    }
    return MediaCodec.createEncoderByType(paramString);
  }
  
  private MediaFormat a(TuSDKVideoEncoderSetting paramTuSDKVideoEncoderSetting)
  {
    String str1 = TuSdkDeviceInfo.getModel();
    String str2 = TuSdkDeviceInfo.getVender();
    int k = paramTuSDKVideoEncoderSetting.videoSize.width % 2 != 0 ? paramTuSDKVideoEncoderSetting.videoSize.width + 1 : paramTuSDKVideoEncoderSetting.videoSize.width;
    int m = paramTuSDKVideoEncoderSetting.videoSize.height % 2 != 0 ? paramTuSDKVideoEncoderSetting.videoSize.height + 1 : paramTuSDKVideoEncoderSetting.videoSize.height;
    if (Build.VERSION.SDK_INT >= 21)
    {
      boolean bool = TuSDKMediaUtils.isVideoSizeSupported(TuSdkSize.create(k, m), "video/avc");
      if (!bool) {
        TLog.w("TuSDKHardVideoDataEncoder | May not support video size for " + this.e.videoSize, new Object[0]);
      }
    }
    MediaFormat localMediaFormat = MediaFormat.createVideoFormat("video/avc", k, m);
    localMediaFormat.setInteger("color-format", 2130708361);
    localMediaFormat.setInteger("bitrate", this.i > 0 ? this.i : paramTuSDKVideoEncoderSetting.videoQuality.getBitrate());
    localMediaFormat.setInteger("frame-rate", this.h > 0 ? this.h : paramTuSDKVideoEncoderSetting.videoQuality.getFps());
    localMediaFormat.setInteger("profile", 1);
    localMediaFormat.setInteger("level", 512);
    localMediaFormat.setInteger("i-frame-interval", paramTuSDKVideoEncoderSetting.mediacodecAVCIFrameInterval);
    localMediaFormat.setInteger("bitrate-mode", paramTuSDKVideoEncoderSetting.bitrateMode);
    if (paramTuSDKVideoEncoderSetting.enableAllKeyFrame) {
      if ("OPPO".equalsIgnoreCase(str2)) {
        localMediaFormat.setInteger("i-frame-interval", 0);
      } else if ("XiaoMi".equalsIgnoreCase(str2)) {
        localMediaFormat.setInteger("i-frame-interval", 0);
      }
    }
    return localMediaFormat;
  }
  
  @SuppressLint({"InlinedApi"})
  public boolean initCodec(TuSDKVideoEncoderSetting paramTuSDKVideoEncoderSetting)
  {
    if (this.c != null) {
      return false;
    }
    this.e = paramTuSDKVideoEncoderSetting;
    MediaCodecInfo localMediaCodecInfo = TuSDKMediaUtils.getEncoderCodecInfo("video/avc");
    if (localMediaCodecInfo == null)
    {
      TLog.e("Unable to find an appropriate codec for video/avc", new Object[0]);
      return false;
    }
    TLog.d("choose codec [" + localMediaCodecInfo.getName() + "] for " + "video/avc", new Object[0]);
    try
    {
      MediaFormat localMediaFormat = a(getVideoEncoderSetting());
      this.c = a("video/avc");
      this.c.configure(localMediaFormat, null, null, 1);
      this.b = this.c.createInputSurface();
      this.c.start();
      this.j = false;
    }
    catch (Throwable localThrowable)
    {
      TLog.e("fail to create MediaCodec with name: %s", new Object[] { localMediaCodecInfo.getName() });
      return false;
    }
    return true;
  }
  
  public Surface getInputSurface()
  {
    return this.b;
  }
  
  public void release()
  {
    if (this.c != null)
    {
      this.c.stop();
      this.c.release();
      this.c = null;
    }
  }
  
  public void flush()
  {
    String str = TuSdkDeviceInfo.getModel();
    if ((str.equalsIgnoreCase("PADM00")) || (str.equalsIgnoreCase("MI 6"))) {
      return;
    }
    if (this.c != null) {
      this.c.flush();
    }
  }
  
  private boolean a()
  {
    return (this.c != null) && (Build.VERSION.SDK_INT > 19);
  }
  
  public boolean requestKeyFrame()
  {
    if (!a()) {
      return false;
    }
    Bundle localBundle = new Bundle();
    localBundle.putInt("request-sync", 0);
    this.c.setParameters(localBundle);
    return true;
  }
  
  public void drainEncoder(boolean paramBoolean)
  {
    if (this.c == null)
    {
      TLog.e("Unable to start the encoder", new Object[0]);
      return;
    }
    if (this.e.enableAllKeyFrame) {
      requestKeyFrame();
    }
    if (paramBoolean)
    {
      TLog.d("sending EOS to encoder", new Object[0]);
      if (this.j) {
        this.c.signalEndOfInputStream();
      } else {
        ThreadHelper.postDelayed(new Runnable()
        {
          public void run()
          {
            TuSDKHardVideoDataEncoder.a(TuSDKHardVideoDataEncoder.this).signalEndOfInputStream();
          }
        }, 600L);
      }
    }
    for (;;)
    {
      MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
      int k = this.c.dequeueOutputBuffer(localBufferInfo, 500L);
      if (k == -1)
      {
        if (!paramBoolean) {
          break;
        }
      }
      else if (k != -3)
      {
        Object localObject;
        if (k == -2)
        {
          localObject = this.c.getOutputFormat();
          onEncoderStarted((MediaFormat)localObject);
        }
        else if (k >= 0)
        {
          localObject = this.c.getOutputBuffers()[k];
          if (localObject == null) {
            throw new RuntimeException("encoderOutputBuffer " + k + " was null");
          }
          if (localBufferInfo.size > 0)
          {
            ((ByteBuffer)localObject).position(localBufferInfo.offset);
            ((ByteBuffer)localObject).limit(localBufferInfo.offset + localBufferInfo.size);
            if ((localBufferInfo.flags & 0x2) != 0) {
              onVideoEncoderCodecConfig(a(localBufferInfo), (ByteBuffer)localObject, localBufferInfo);
            } else if (this.e.enableAllKeyFrame)
            {
              if ((localBufferInfo.flags & 0x1) != 0) {
                onVideoEncoderFrameDataAvailable(a(localBufferInfo), (ByteBuffer)localObject, localBufferInfo);
              }
            }
            else {
              onVideoEncoderFrameDataAvailable(a(localBufferInfo), (ByteBuffer)localObject, localBufferInfo);
            }
          }
          this.c.releaseOutputBuffer(k, false);
          this.j = true;
          if ((localBufferInfo.flags & 0x4) != 0)
          {
            if (!paramBoolean)
            {
              TLog.w("reached end of stream unexpectedly", new Object[0]);
              break;
            }
            TLog.d("end of stream reached", new Object[0]);
            break;
          }
        }
      }
    }
  }
  
  private long a(MediaCodec.BufferInfo paramBufferInfo)
  {
    if (paramBufferInfo == null) {
      return 0L;
    }
    if (this.f == 0L) {
      this.f = (paramBufferInfo.presentationTimeUs / 1000L);
    }
    return paramBufferInfo.presentationTimeUs / 1000L - this.f;
  }
  
  protected void onEncoderStarted(MediaFormat paramMediaFormat)
  {
    if (getDelegate() != null) {
      getDelegate().onVideoEncoderStarted(paramMediaFormat);
    }
  }
  
  protected void onVideoEncoderCodecConfig(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (getDelegate() != null) {
      getDelegate().onVideoEncoderCodecConfig(paramLong, paramByteBuffer, paramBufferInfo);
    }
  }
  
  protected void onVideoEncoderFrameDataAvailable(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (this.g <= 0L)
    {
      this.g = paramBufferInfo.presentationTimeUs;
    }
    else
    {
      if (this.g > paramBufferInfo.presentationTimeUs) {
        paramBufferInfo.presentationTimeUs = (this.g - paramBufferInfo.presentationTimeUs + this.g + 1L);
      }
      this.g = paramBufferInfo.presentationTimeUs;
    }
    if (getDelegate() != null) {
      getDelegate().onVideoEncoderFrameDataAvailable(paramLong, paramByteBuffer, paramBufferInfo);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\encoder\video\TuSDKHardVideoDataEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */