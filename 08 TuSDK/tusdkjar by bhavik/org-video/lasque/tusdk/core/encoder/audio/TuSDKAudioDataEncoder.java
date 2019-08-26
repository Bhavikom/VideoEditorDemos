package org.lasque.tusdk.core.encoder.audio;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.utils.TLog;

@SuppressLint({"InlinedApi"})
public class TuSDKAudioDataEncoder
  implements TuSDKAudioDataEncoderInterface
{
  private MediaCodec a;
  private MediaFormat b;
  private HandlerThread c = new HandlerThread("AudioEncoderHandlerThread");
  private AudioEncoderHandler d;
  private AudioEncoderThread e;
  private TuSDKAudioBuff[] f;
  private TuSDKAudioBuff g;
  private int h;
  private TuSDKAudioEncoderSetting i;
  private TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate j;
  private long k = 0L;
  private long l;
  private AudioEncoderState m = AudioEncoderState.UnKnow;
  
  public TuSDKAudioDataEncoder()
  {
    this.c.start();
    this.d = new AudioEncoderHandler(this.c.getLooper());
  }
  
  public boolean initEncoder(TuSDKAudioEncoderSetting paramTuSDKAudioEncoderSetting)
  {
    this.i = paramTuSDKAudioEncoderSetting;
    this.b = new MediaFormat();
    this.a = a(getEncoderSetting(), this.b);
    if (this.a == null)
    {
      TLog.e("create Audio MediaCodec failed", new Object[0]);
      return false;
    }
    if (paramTuSDKAudioEncoderSetting.enableBuffers)
    {
      int n = getEncoderSetting().audioBufferQueueNum;
      int i1 = getEncoderSetting().audioQuality.getSampleRate() / 5;
      this.f = new TuSDKAudioBuff[n];
      for (int i2 = 0; i2 < n; i2++) {
        this.f[i2] = new TuSDKAudioBuff(i1);
      }
      this.g = new TuSDKAudioBuff(i1);
    }
    return true;
  }
  
  private MediaCodec a(TuSDKAudioEncoderSetting paramTuSDKAudioEncoderSetting, MediaFormat paramMediaFormat)
  {
    paramMediaFormat.setString("mime", "audio/mp4a-latm");
    paramMediaFormat.setInteger("aac-profile", paramTuSDKAudioEncoderSetting.mediacodecAACProfile);
    paramMediaFormat.setInteger("channel-count", paramTuSDKAudioEncoderSetting.mediacodecAACChannelCount);
    paramMediaFormat.setInteger("sample-rate", paramTuSDKAudioEncoderSetting.sampleRate);
    paramMediaFormat.setInteger("bitrate", paramTuSDKAudioEncoderSetting.audioQuality.getBitrate());
    paramMediaFormat.setInteger("max-input-size", paramTuSDKAudioEncoderSetting.mediacodecAACMaxInputSize);
    MediaCodec localMediaCodec;
    try
    {
      localMediaCodec = MediaCodec.createEncoderByType("audio/mp4a-latm");
    }
    catch (Exception localException)
    {
      return null;
    }
    return localMediaCodec;
  }
  
  public void start()
  {
    this.d.sendMessage(this.d.obtainMessage(2));
  }
  
  public void stop()
  {
    this.d.sendMessage(this.d.obtainMessage(3));
  }
  
  protected void onStopeed()
  {
    if (this.j != null) {
      this.j.onAudioEncoderStoped();
    }
  }
  
  public void queueAudio(byte[] paramArrayOfByte)
  {
    this.d.sendMessage(this.d.obtainMessage(1, paramArrayOfByte));
  }
  
  public void setEncoderSetting(TuSDKAudioEncoderSetting paramTuSDKAudioEncoderSetting)
  {
    this.i = paramTuSDKAudioEncoderSetting;
  }
  
  public TuSDKAudioEncoderSetting getEncoderSetting()
  {
    if (this.i == null) {
      this.i = new TuSDKAudioEncoderSetting();
    }
    return this.i;
  }
  
  public MediaFormat getAudioFormat()
  {
    return this.b;
  }
  
  public TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate getDelegate()
  {
    return this.j;
  }
  
  public void setDelegate(TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate paramTuSDKAudioDataEncoderDelegate)
  {
    this.j = paramTuSDKAudioDataEncoderDelegate;
  }
  
  private long a(MediaCodec.BufferInfo paramBufferInfo)
  {
    if (paramBufferInfo == null) {
      return 0L;
    }
    if (this.k == 0L) {
      this.k = (paramBufferInfo.presentationTimeUs / 1000L);
    }
    return paramBufferInfo.presentationTimeUs / 1000L - this.k;
  }
  
  public void onAudioEncoderStarted(MediaFormat paramMediaFormat)
  {
    if (getDelegate() != null) {
      getDelegate().onAudioEncoderStarted(paramMediaFormat);
    }
  }
  
  public void onAudioEncoderFrameDataAvailable(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (this.l <= 0L)
    {
      this.l = paramBufferInfo.presentationTimeUs;
    }
    else
    {
      if (this.l > paramBufferInfo.presentationTimeUs) {
        return;
      }
      this.l = paramBufferInfo.presentationTimeUs;
    }
    if (getDelegate() != null) {
      getDelegate().onAudioEncoderFrameDataAvailable(paramLong, paramByteBuffer, paramBufferInfo);
    }
  }
  
  protected void onAudioEncoderCodecConfig(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (getDelegate() != null) {
      getDelegate().onAudioEncoderCodecConfig(paramLong, paramByteBuffer, paramBufferInfo);
    }
  }
  
  private class AudioEncoderHandler
    extends Handler
  {
    AudioEncoderHandler(Looper paramLooper)
    {
      super();
    }
    
    private void a(byte[] paramArrayOfByte)
    {
      if ((TuSDKAudioDataEncoder.b(TuSDKAudioDataEncoder.this) != TuSDKAudioDataEncoder.AudioEncoderState.Runing) || (paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
        return;
      }
      long l = SystemClock.uptimeMillis();
      int i;
      if (TuSDKAudioDataEncoder.c(TuSDKAudioDataEncoder.this).enableBuffers)
      {
        i = (TuSDKAudioDataEncoder.d(TuSDKAudioDataEncoder.this) + 1) % TuSDKAudioDataEncoder.e(TuSDKAudioDataEncoder.this).length;
        if (TuSDKAudioDataEncoder.e(TuSDKAudioDataEncoder.this)[i].isReadyToFill)
        {
          System.arraycopy(paramArrayOfByte, 0, TuSDKAudioDataEncoder.e(TuSDKAudioDataEncoder.this)[i].buff, 0, TuSDKAudioDataEncoder.this.getEncoderSetting().bufferSize);
          TuSDKAudioDataEncoder.e(TuSDKAudioDataEncoder.this)[i].isReadyToFill = false;
          TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this, i);
          TuSDKAudioDataEncoder.e(TuSDKAudioDataEncoder.this)[i].isReadyToFill = true;
        }
        System.arraycopy(TuSDKAudioDataEncoder.e(TuSDKAudioDataEncoder.this)[i].buff, 0, TuSDKAudioDataEncoder.f(TuSDKAudioDataEncoder.this).buff, 0, TuSDKAudioDataEncoder.f(TuSDKAudioDataEncoder.this).buff.length);
        int j = TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this).dequeueInputBuffer(5000L);
        if (j >= 0)
        {
          ByteBuffer localByteBuffer2 = TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this).getInputBuffers()[j];
          localByteBuffer2.position(0);
          localByteBuffer2.put(TuSDKAudioDataEncoder.f(TuSDKAudioDataEncoder.this).buff, 0, TuSDKAudioDataEncoder.f(TuSDKAudioDataEncoder.this).buff.length);
          TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this).queueInputBuffer(j, 0, TuSDKAudioDataEncoder.f(TuSDKAudioDataEncoder.this).buff.length, l * 1000L, 0);
        }
      }
      else
      {
        i = TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this).dequeueInputBuffer(5000L);
        if (i >= 0)
        {
          ByteBuffer localByteBuffer1 = TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this).getInputBuffers()[i];
          localByteBuffer1.position(0);
          localByteBuffer1.put(paramArrayOfByte, 0, paramArrayOfByte.length);
          TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this).queueInputBuffer(i, 0, paramArrayOfByte.length, l * 1000L, 0);
        }
      }
    }
    
    private void a()
    {
      if (TuSDKAudioDataEncoder.b(TuSDKAudioDataEncoder.this) == TuSDKAudioDataEncoder.AudioEncoderState.Runing) {
        return;
      }
      TuSDKAudioDataEncoder.g(TuSDKAudioDataEncoder.this).removeMessages(2);
      TuSDKAudioDataEncoder.g(TuSDKAudioDataEncoder.this).removeMessages(3);
      TuSDKAudioDataEncoder.g(TuSDKAudioDataEncoder.this).removeMessages(1);
      TuSDKAudioDataEncoder.g(TuSDKAudioDataEncoder.this).removeCallbacksAndMessages(null);
      if (TuSDKAudioDataEncoder.e(TuSDKAudioDataEncoder.this) != null) {
        for (TuSDKAudioBuff localTuSDKAudioBuff : TuSDKAudioDataEncoder.e(TuSDKAudioDataEncoder.this)) {
          localTuSDKAudioBuff.isReadyToFill = true;
        }
      }
      if (TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this) == null) {
        try
        {
          TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this, MediaCodec.createEncoderByType(TuSDKAudioDataEncoder.h(TuSDKAudioDataEncoder.this).getString("mime")));
        }
        catch (IOException localIOException)
        {
          localIOException.printStackTrace();
        }
      }
      TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this, 0L);
      TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this).configure(TuSDKAudioDataEncoder.h(TuSDKAudioDataEncoder.this), null, null, 1);
      TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this).start();
      TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this, new TuSDKAudioDataEncoder.AudioEncoderThread(TuSDKAudioDataEncoder.this, null));
      TuSDKAudioDataEncoder.i(TuSDKAudioDataEncoder.this).start();
      TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this, TuSDKAudioDataEncoder.AudioEncoderState.Runing);
    }
    
    private void b()
    {
      if (TuSDKAudioDataEncoder.b(TuSDKAudioDataEncoder.this) != TuSDKAudioDataEncoder.AudioEncoderState.Runing) {
        return;
      }
      TuSDKAudioDataEncoder.g(TuSDKAudioDataEncoder.this).removeMessages(2);
      TuSDKAudioDataEncoder.g(TuSDKAudioDataEncoder.this).removeMessages(3);
      TuSDKAudioDataEncoder.g(TuSDKAudioDataEncoder.this).removeMessages(1);
      TuSDKAudioDataEncoder.g(TuSDKAudioDataEncoder.this).removeCallbacksAndMessages(null);
      if (TuSDKAudioDataEncoder.i(TuSDKAudioDataEncoder.this) != null)
      {
        TuSDKAudioDataEncoder.i(TuSDKAudioDataEncoder.this).quit();
        try
        {
          TuSDKAudioDataEncoder.i(TuSDKAudioDataEncoder.this).join();
          TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this, null);
        }
        catch (InterruptedException localInterruptedException)
        {
          localInterruptedException.printStackTrace();
        }
      }
      if (TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this) != null)
      {
        TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this).stop();
        TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this).release();
        TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this, null);
      }
      TuSDKAudioDataEncoder.b(TuSDKAudioDataEncoder.this, 0L);
      TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this, TuSDKAudioDataEncoder.AudioEncoderState.Stopped);
      TuSDKAudioDataEncoder.this.onStopeed();
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 1)
      {
        byte[] arrayOfByte = (byte[])paramMessage.obj;
        a(arrayOfByte);
      }
      else if (paramMessage.what == 2)
      {
        a();
      }
      else if (paramMessage.what == 3)
      {
        b();
      }
    }
  }
  
  private class AudioEncoderThread
    extends Thread
  {
    private boolean b = false;
    
    private AudioEncoderThread() {}
    
    public void quit()
    {
      this.b = true;
      interrupt();
    }
    
    public void run()
    {
      while (!this.b)
      {
        MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
        int i = TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this).dequeueOutputBuffer(localBufferInfo, 5000L);
        switch (i)
        {
        case -3: 
          break;
        case -1: 
          break;
        case -2: 
          TuSDKAudioDataEncoder.this.onAudioEncoderStarted(TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this).getOutputFormat());
          break;
        default: 
          if (localBufferInfo.size > 0)
          {
            ByteBuffer localByteBuffer = TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this).getOutputBuffers()[i];
            localByteBuffer.position(localBufferInfo.offset);
            localByteBuffer.limit(localBufferInfo.offset + localBufferInfo.size);
            if ((localBufferInfo.flags & 0x2) != 0) {
              TuSDKAudioDataEncoder.this.onAudioEncoderCodecConfig(TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this, localBufferInfo), localByteBuffer.duplicate(), localBufferInfo);
            } else {
              TuSDKAudioDataEncoder.this.onAudioEncoderFrameDataAvailable(TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this, localBufferInfo), localByteBuffer.duplicate(), localBufferInfo);
            }
          }
          TuSDKAudioDataEncoder.a(TuSDKAudioDataEncoder.this).releaseOutputBuffer(i, false);
        }
      }
    }
  }
  
  private static enum AudioEncoderState
  {
    private AudioEncoderState() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\encoder\audio\TuSDKAudioDataEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */