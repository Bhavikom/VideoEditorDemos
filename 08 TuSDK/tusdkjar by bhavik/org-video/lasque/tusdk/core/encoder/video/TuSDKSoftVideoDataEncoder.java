package org.lasque.tusdk.core.encoder.video;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaFormat;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import org.lasque.tusdk.core.secret.ColorSpaceConvert;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

@SuppressLint({"InlinedApi"})
public class TuSDKSoftVideoDataEncoder
  implements TuSDKSoftVideoDataEncoderInterface
{
  private final Object b = new Object();
  private MediaCodec c;
  private MediaFormat d;
  private boolean e = true;
  private boolean f;
  private long g = 0L;
  private TuSDKVideoBuff[] h;
  private TuSDKVideoBuff i;
  private int j;
  private TuSDKVideoBuff k;
  private long l;
  private HandlerThread m;
  private VideoDataEncoderHandler n;
  private VideoDequeueOutputBufferThread o;
  private TuSDKVideoEncoderSetting p;
  private TuSDKVideoDataEncoderDelegate q;
  int a = 0;
  
  public boolean initEncoder(TuSDKVideoEncoderSetting paramTuSDKVideoEncoderSetting)
  {
    synchronized (this.b)
    {
      this.p = paramTuSDKVideoEncoderSetting;
      if (Build.VERSION.SDK_INT >= 21)
      {
        boolean bool = TuSDKMediaUtils.isVideoSizeSupported(this.p.videoSize, "video/avc");
        if (!bool) {
          TLog.w("TuSDKSoftVideoDataEncoder | May not support video size for " + this.p.videoSize, new Object[0]);
        }
      }
      this.d = new MediaFormat();
      this.c = a(getVideoEncoderSetting(), this.d);
      this.f = false;
      if (this.c == null) {
        return false;
      }
      int i1 = TuSDKBuffSizeCalculator.calculator(getVideoEncoderSetting().videoSize.width, getVideoEncoderSetting().videoSize.height, getVideoEncoderSetting().previewColorFormat);
      int i2 = getVideoEncoderSetting().videoSize.width;
      int i3 = getVideoEncoderSetting().videoSize.height;
      int i4 = getVideoEncoderSetting().videoBufferQueueNum;
      this.h = new TuSDKVideoBuff[i4];
      for (int i5 = 0; i5 < i4; i5++) {
        this.h[i5] = new TuSDKVideoBuff(getVideoEncoderSetting().previewColorFormat, i1);
      }
      this.j = 0;
      this.i = new TuSDKVideoBuff(21, TuSDKBuffSizeCalculator.calculator(i2, i3, 21));
      this.k = new TuSDKVideoBuff(getVideoEncoderSetting().mediacodecAVCColorFormat, TuSDKBuffSizeCalculator.calculator(i2, i3, getVideoEncoderSetting().mediacodecAVCColorFormat));
      return true;
    }
  }
  
  public boolean start()
  {
    synchronized (this.b)
    {
      try
      {
        if (this.c == null) {
          this.c = MediaCodec.createEncoderByType(this.d.getString("mime"));
        }
        this.c.configure(this.d, null, null, 1);
        this.c.start();
        this.f = true;
        this.g = 0L;
        this.o = new VideoDequeueOutputBufferThread("AudioDequeueOutputBufferThread");
        this.o.start();
        this.m = new HandlerThread("videoFilterHandlerThread");
        this.m.start();
        this.n = new VideoDataEncoderHandler(this.m.getLooper());
      }
      catch (Exception localException)
      {
        return false;
      }
      return true;
    }
  }
  
  public void stop()
  {
    synchronized (this.b)
    {
      if (!this.f) {
        return;
      }
      this.f = false;
      if (this.c != null)
      {
        this.c.stop();
        this.c.release();
        this.c = null;
      }
      this.o.a();
      try
      {
        this.o.join();
      }
      catch (InterruptedException localInterruptedException1)
      {
        TLog.e(localInterruptedException1);
      }
      this.o = null;
      this.n.removeCallbacks(null);
      this.n.removeMessages(1);
      this.m.quit();
      try
      {
        this.m.join();
      }
      catch (InterruptedException localInterruptedException2)
      {
        TLog.e(localInterruptedException2);
      }
      this.l = 0L;
      this.a = 0;
    }
  }
  
  public void queueVideo(byte[] paramArrayOfByte)
  {
    synchronized (this.b)
    {
      if (!this.f) {
        return;
      }
      int i1 = (this.j + 1) % this.h.length;
      if (this.h[i1].isReadyToFill)
      {
        this.h[i1].buff = paramArrayOfByte;
        this.h[i1].isReadyToFill = false;
        this.j = i1;
        this.n.sendMessage(this.n.obtainMessage(1, i1, 0));
      }
      else
      {
        TLog.d("queueVideo,abandon,targetIndex" + i1, new Object[0]);
      }
    }
  }
  
  public void onVideoEncoderStarted(MediaFormat paramMediaFormat)
  {
    if (getDelegate() != null) {
      getDelegate().onVideoEncoderStarted(paramMediaFormat);
    }
  }
  
  public void setPTSUseSystemClock(boolean paramBoolean)
  {
    this.e = paramBoolean;
  }
  
  public MediaFormat getVideoFormat()
  {
    return this.d;
  }
  
  public void onVideoEncoderFrameDataAvailable(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (this.l == 0L)
    {
      this.l = paramBufferInfo.presentationTimeUs;
    }
    else
    {
      if (this.l > paramBufferInfo.presentationTimeUs) {
        paramBufferInfo.presentationTimeUs = (this.l + 1L);
      }
      this.l = paramBufferInfo.presentationTimeUs;
    }
    if (getDelegate() != null) {
      getDelegate().onVideoEncoderFrameDataAvailable(paramLong, paramByteBuffer, paramBufferInfo);
    }
  }
  
  protected void onVideoEncoderCodecConfig(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (getDelegate() != null) {
      getDelegate().onVideoEncoderCodecConfig(paramLong, paramByteBuffer, paramBufferInfo);
    }
  }
  
  public void setDelegate(TuSDKVideoDataEncoderDelegate paramTuSDKVideoDataEncoderDelegate)
  {
    this.q = paramTuSDKVideoDataEncoderDelegate;
  }
  
  public TuSDKVideoDataEncoderDelegate getDelegate()
  {
    return this.q;
  }
  
  public TuSDKVideoEncoderSetting getVideoEncoderSetting()
  {
    if (this.p == null) {
      this.p = new TuSDKVideoEncoderSetting();
    }
    return this.p;
  }
  
  public void setVideoEncoderSetting(TuSDKVideoEncoderSetting paramTuSDKVideoEncoderSetting)
  {
    this.p = paramTuSDKVideoEncoderSetting;
  }
  
  public boolean requestKeyFrame()
  {
    if ((this.c == null) || (Build.VERSION.SDK_INT < 19)) {
      return false;
    }
    Bundle localBundle = new Bundle();
    localBundle.putInt("request-sync", 0);
    this.c.setParameters(localBundle);
    return true;
  }
  
  @SuppressLint({"InlinedApi"})
  private MediaCodec a(TuSDKVideoEncoderSetting paramTuSDKVideoEncoderSetting, MediaFormat paramMediaFormat)
  {
    paramMediaFormat.setString("mime", "video/avc");
    paramMediaFormat.setInteger("width", paramTuSDKVideoEncoderSetting.videoSize.width);
    paramMediaFormat.setInteger("height", paramTuSDKVideoEncoderSetting.videoSize.height);
    paramMediaFormat.setInteger("bitrate", paramTuSDKVideoEncoderSetting.videoQuality.getBitrate());
    paramMediaFormat.setInteger("frame-rate", paramTuSDKVideoEncoderSetting.videoQuality.getFps());
    paramMediaFormat.setInteger("i-frame-interval", paramTuSDKVideoEncoderSetting.mediacodecAVCIFrameInterval);
    paramMediaFormat.setInteger("profile", 1);
    paramMediaFormat.setInteger("level", 512);
    paramMediaFormat.setInteger("bitrate-mode", paramTuSDKVideoEncoderSetting.bitrateMode);
    MediaCodec localMediaCodec = null;
    try
    {
      localMediaCodec = MediaCodec.createEncoderByType(paramMediaFormat.getString("mime"));
      int[] arrayOfInt = localMediaCodec.getCodecInfo().getCapabilitiesForType(paramMediaFormat.getString("mime")).colorFormats;
      int i1 = -1;
      if (a(arrayOfInt, 21))
      {
        i1 = 21;
        paramTuSDKVideoEncoderSetting.mediacodecAVCColorFormat = 21;
      }
      if ((i1 == -1) && (a(arrayOfInt, 19)))
      {
        i1 = 19;
        paramTuSDKVideoEncoderSetting.mediacodecAVCColorFormat = 19;
      }
      if (i1 == -1)
      {
        TLog.e("!!!!!!!!!!!UnSupport,mediaCodecColorFormat", new Object[0]);
        return null;
      }
      paramMediaFormat.setInteger("color-format", i1);
    }
    catch (IOException localIOException)
    {
      return null;
    }
    return localMediaCodec;
  }
  
  private static boolean a(int[] paramArrayOfInt, int paramInt)
  {
    for (int i3 : paramArrayOfInt) {
      if (i3 == paramInt) {
        return true;
      }
    }
    return false;
  }
  
  private long a(MediaCodec.BufferInfo paramBufferInfo)
  {
    if (paramBufferInfo == null) {
      return 0L;
    }
    if (this.g == 0L) {
      this.g = (paramBufferInfo.presentationTimeUs / 1000L);
    }
    return paramBufferInfo.presentationTimeUs / 1000L - this.g;
  }
  
  private long a(int paramInt)
  {
    long l1 = 1000000000L;
    return paramInt * 1000000000L / Math.max(this.p.videoQuality.getFps(), 25);
  }
  
  private class VideoDataEncoderHandler
    extends Handler
  {
    public static final int WHAT_INCOMING_BUFF = 1;
    
    VideoDataEncoderHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (!TuSDKSoftVideoDataEncoder.a(TuSDKSoftVideoDataEncoder.this)) {
        return;
      }
      switch (paramMessage.what)
      {
      case 1: 
        TuSDKSoftVideoDataEncoder.this.requestKeyFrame();
        int i = paramMessage.arg1;
        System.arraycopy(TuSDKSoftVideoDataEncoder.d(TuSDKSoftVideoDataEncoder.this)[i].buff, 0, TuSDKSoftVideoDataEncoder.e(TuSDKSoftVideoDataEncoder.this).buff, 0, TuSDKSoftVideoDataEncoder.e(TuSDKSoftVideoDataEncoder.this).buff.length);
        TuSDKSoftVideoDataEncoder.d(TuSDKSoftVideoDataEncoder.this)[i].isReadyToFill = true;
        if (TuSDKSoftVideoDataEncoder.this.getVideoEncoderSetting().mediacodecAVCColorFormat == 21) {
          ColorSpaceConvert.nv21ToYuv420sp(TuSDKSoftVideoDataEncoder.e(TuSDKSoftVideoDataEncoder.this).buff, TuSDKSoftVideoDataEncoder.f(TuSDKSoftVideoDataEncoder.this).buff, TuSDKSoftVideoDataEncoder.this.getVideoEncoderSetting().videoSize.width * TuSDKSoftVideoDataEncoder.this.getVideoEncoderSetting().videoSize.height);
        } else if (TuSDKSoftVideoDataEncoder.this.getVideoEncoderSetting().mediacodecAVCColorFormat == 19) {
          ColorSpaceConvert.nv21TOYuv420p(TuSDKSoftVideoDataEncoder.e(TuSDKSoftVideoDataEncoder.this).buff, TuSDKSoftVideoDataEncoder.f(TuSDKSoftVideoDataEncoder.this).buff, TuSDKSoftVideoDataEncoder.this.getVideoEncoderSetting().videoSize.width * TuSDKSoftVideoDataEncoder.this.getVideoEncoderSetting().videoSize.height);
        }
        TuSDKSoftVideoDataEncoder.e(TuSDKSoftVideoDataEncoder.this).isReadyToFill = true;
        long l = TuSDKSoftVideoDataEncoder.g(TuSDKSoftVideoDataEncoder.this) ? SystemClock.uptimeMillis() * 1000L : TuSDKSoftVideoDataEncoder.a(TuSDKSoftVideoDataEncoder.this, TuSDKSoftVideoDataEncoder.this.a++) / 1000L;
        synchronized (TuSDKSoftVideoDataEncoder.h(TuSDKSoftVideoDataEncoder.this))
        {
          if ((TuSDKSoftVideoDataEncoder.b(TuSDKSoftVideoDataEncoder.this) != null) && (TuSDKSoftVideoDataEncoder.a(TuSDKSoftVideoDataEncoder.this)))
          {
            int j = TuSDKSoftVideoDataEncoder.b(TuSDKSoftVideoDataEncoder.this).dequeueInputBuffer(-1L);
            if (j >= 0)
            {
              ByteBuffer localByteBuffer = TuSDKSoftVideoDataEncoder.b(TuSDKSoftVideoDataEncoder.this).getInputBuffers()[j];
              localByteBuffer.position(0);
              localByteBuffer.put(TuSDKSoftVideoDataEncoder.f(TuSDKSoftVideoDataEncoder.this).buff, 0, TuSDKSoftVideoDataEncoder.f(TuSDKSoftVideoDataEncoder.this).buff.length);
              TuSDKSoftVideoDataEncoder.b(TuSDKSoftVideoDataEncoder.this).queueInputBuffer(j, 0, TuSDKSoftVideoDataEncoder.f(TuSDKSoftVideoDataEncoder.this).buff.length, l, 0);
            }
            else
            {
              TLog.d("dstVideoEncoder.dequeueInputBuffer(-1)<0", new Object[0]);
            }
          }
        }
      }
    }
  }
  
  private class VideoDequeueOutputBufferThread
    extends Thread
  {
    private boolean b = false;
    
    public VideoDequeueOutputBufferThread(String paramString)
    {
      super();
    }
    
    void a()
    {
      this.b = true;
      interrupt();
    }
    
    public void run()
    {
      while ((!this.b) && (TuSDKSoftVideoDataEncoder.a(TuSDKSoftVideoDataEncoder.this))) {
        if (TuSDKSoftVideoDataEncoder.b(TuSDKSoftVideoDataEncoder.this) != null)
        {
          MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
          int i = -1;
          try
          {
            i = TuSDKSoftVideoDataEncoder.b(TuSDKSoftVideoDataEncoder.this).dequeueOutputBuffer(localBufferInfo, 5000L);
          }
          catch (Exception localException) {}
          switch (i)
          {
          case -3: 
            break;
          case -1: 
            break;
          case -2: 
            TuSDKSoftVideoDataEncoder.this.onVideoEncoderStarted(TuSDKSoftVideoDataEncoder.b(TuSDKSoftVideoDataEncoder.this).getOutputFormat());
            break;
          default: 
            if ((i >= 0) && (localBufferInfo.size > 0))
            {
              ByteBuffer localByteBuffer = TuSDKSoftVideoDataEncoder.b(TuSDKSoftVideoDataEncoder.this).getOutputBuffers()[i];
              localByteBuffer.position(localBufferInfo.offset + 4);
              localByteBuffer.limit(localBufferInfo.offset + localBufferInfo.size);
              if ((localBufferInfo.flags & 0x2) != 0)
              {
                TuSDKSoftVideoDataEncoder.this.onVideoEncoderCodecConfig(TuSDKSoftVideoDataEncoder.a(TuSDKSoftVideoDataEncoder.this, localBufferInfo), localByteBuffer, localBufferInfo);
              }
              else
              {
                TLog.i("flags= " + localBufferInfo.flags, new Object[0]);
                if (TuSDKSoftVideoDataEncoder.c(TuSDKSoftVideoDataEncoder.this).enableAllKeyFrame)
                {
                  if ((localBufferInfo.flags & 0x1) != 0) {
                    TuSDKSoftVideoDataEncoder.this.onVideoEncoderFrameDataAvailable(TuSDKSoftVideoDataEncoder.a(TuSDKSoftVideoDataEncoder.this, localBufferInfo), localByteBuffer, localBufferInfo);
                  }
                }
                else {
                  TuSDKSoftVideoDataEncoder.this.onVideoEncoderFrameDataAvailable(TuSDKSoftVideoDataEncoder.a(TuSDKSoftVideoDataEncoder.this, localBufferInfo), localByteBuffer, localBufferInfo);
                }
              }
            }
            TuSDKSoftVideoDataEncoder.b(TuSDKSoftVideoDataEncoder.this).releaseOutputBuffer(i, false);
          }
        }
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\encoder\video\TuSDKSoftVideoDataEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */