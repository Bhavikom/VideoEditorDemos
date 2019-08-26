package org.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender.TuSdkAudioRenderCallback;
import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput.TuSdkEncodecOutput;
import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

@TargetApi(18)
public class TuSdkAudioEncodecOperationImpl
  implements TuSdkAudioEncodecOperation
{
  private TuSdkMediaCodec a;
  private boolean b;
  private int c = -1;
  private final long d = 10000L;
  private TuSdkCodecOutput.TuSdkEncodecOutput e;
  private ByteBuffer[] f;
  private ByteBuffer[] g;
  private boolean h = false;
  private TuSdkMediaMuxer i;
  private int j = -1;
  private ByteBuffer k;
  private TuSdkAudioInfo l;
  private TuSdkAudioRender m;
  private long n = 0L;
  private TuSdkAudioRender.TuSdkAudioRenderCallback o = new TuSdkAudioRender.TuSdkAudioRenderCallback()
  {
    public boolean isEncodec()
    {
      return true;
    }
    
    public TuSdkAudioInfo getAudioInfo()
    {
      return TuSdkAudioEncodecOperationImpl.a(TuSdkAudioEncodecOperationImpl.this);
    }
    
    public void returnRenderBuffer(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkAudioEncodecOperationImpl.a(TuSdkAudioEncodecOperationImpl.this, TuSdkAudioEncodecOperationImpl.b(TuSdkAudioEncodecOperationImpl.this), paramAnonymousByteBuffer, paramAnonymousBufferInfo);
    }
  };
  
  public TuSdkAudioEncodecOperationImpl(TuSdkCodecOutput.TuSdkEncodecOutput paramTuSdkEncodecOutput)
  {
    if (paramTuSdkEncodecOutput == null) {
      throw new NullPointerException(String.format("%s init failed, encodecOutput is NULL", new Object[] { "TuSdkAudioEncodecOperationImpl" }));
    }
    this.e = paramTuSdkEncodecOutput;
  }
  
  public TuSdkAudioInfo getAudioInfo()
  {
    return this.l;
  }
  
  public void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    this.m = paramTuSdkAudioRender;
  }
  
  public int setMediaFormat(MediaFormat paramMediaFormat)
  {
    int i1 = TuSdkMediaFormat.checkAudioEncodec(paramMediaFormat);
    if (i1 != 0) {
      return i1;
    }
    TuSdkMediaCodec localTuSdkMediaCodec = TuSdkMediaCodecImpl.createEncoderByType(paramMediaFormat.getString("mime"));
    if (localTuSdkMediaCodec.configureError() != null)
    {
      TLog.e(localTuSdkMediaCodec.configureError(), "%s setMediaFormat create MediaCodec failed", new Object[] { "TuSdkAudioEncodecOperationImpl" });
      return 256;
    }
    this.l = new TuSdkAudioInfo(paramMediaFormat);
    this.a = localTuSdkMediaCodec;
    this.a.configure(paramMediaFormat, null, null, 1);
    return 0;
  }
  
  public void encodecRelease()
  {
    this.h = true;
    if (this.a == null) {
      return;
    }
    this.a.stop();
    this.a.release();
    this.a = null;
  }
  
  protected void finalize()
  {
    encodecRelease();
    super.finalize();
  }
  
  public void encodecException(Exception paramException)
  {
    if (this.e == null)
    {
      TLog.e(paramException, "%s the Thread catch exception, The thread wil exit.", new Object[] { "TuSdkAudioEncodecOperationImpl" });
      return;
    }
    this.e.onCatchedException(paramException);
  }
  
  public void flush()
  {
    if (this.a != null) {
      this.a.flush();
    }
  }
  
  public boolean isEncodecStarted()
  {
    if (this.a != null) {
      return this.a.isStarted();
    }
    return false;
  }
  
  public boolean encodecInit(TuSdkMediaMuxer paramTuSdkMediaMuxer)
  {
    this.i = paramTuSdkMediaMuxer;
    TuSdkMediaCodec localTuSdkMediaCodec = this.a;
    TuSdkCodecOutput.TuSdkEncodecOutput localTuSdkEncodecOutput = this.e;
    if ((localTuSdkMediaCodec == null) || (localTuSdkEncodecOutput == null))
    {
      TLog.w("%s decodecInit need setMediaFormat first", new Object[] { "TuSdkAudioEncodecOperationImpl" });
      return false;
    }
    localTuSdkMediaCodec.start();
    this.f = localTuSdkMediaCodec.getInputBuffers();
    this.g = localTuSdkMediaCodec.getOutputBuffers();
    this.b = false;
    return true;
  }
  
  public boolean encodecProcessUntilEnd(TuSdkMediaMuxer paramTuSdkMediaMuxer)
  {
    TuSdkMediaCodec localTuSdkMediaCodec = this.a;
    TuSdkCodecOutput.TuSdkEncodecOutput localTuSdkEncodecOutput = this.e;
    if ((localTuSdkMediaCodec == null) || (localTuSdkEncodecOutput == null)) {
      return true;
    }
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    int i1 = localTuSdkMediaCodec.dequeueOutputBuffer(localBufferInfo, 10000L);
    switch (i1)
    {
    case -2: 
      TLog.d("[debug] out put format changed", new Object[0]);
      this.l = new TuSdkAudioInfo(localTuSdkMediaCodec.getOutputFormat());
      this.c = paramTuSdkMediaMuxer.addTrack(localTuSdkMediaCodec.getOutputFormat());
      localTuSdkEncodecOutput.outputFormatChanged(localTuSdkMediaCodec.getOutputFormat());
      break;
    case -1: 
      break;
    case -3: 
      TLog.d("%s process Output Buffers Changed", new Object[] { "TuSdkAudioEncodecOperationImpl" });
      this.g = localTuSdkMediaCodec.getOutputBuffers();
      break;
    default: 
      if (i1 >= 0)
      {
        if (localBufferInfo.size > 0)
        {
          ByteBuffer localByteBuffer = this.g[i1];
          localTuSdkEncodecOutput.processOutputBuffer(paramTuSdkMediaMuxer, this.c, localByteBuffer, localBufferInfo);
        }
        if (!this.h) {
          localTuSdkMediaCodec.releaseOutputBuffer(i1, false);
        }
        localTuSdkEncodecOutput.updated(localBufferInfo);
      }
      break;
    }
    if ((localBufferInfo.flags & 0x4) != 0) {
      return localTuSdkEncodecOutput.updatedToEOS(localBufferInfo);
    }
    return false;
  }
  
  public int writeBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    TuSdkMediaCodec localTuSdkMediaCodec = this.a;
    if ((this.h) || (localTuSdkMediaCodec == null) || (this.f == null))
    {
      TLog.w("%s writeBuffer can not run after release.", new Object[] { "TuSdkAudioEncodecOperationImpl" });
      return -1;
    }
    if (paramBufferInfo == null)
    {
      TLog.w("%s writeBuffer can not allow empty input: buffer[%s], bufferInfo[%s]", new Object[] { "TuSdkAudioEncodecOperationImpl", paramByteBuffer, paramBufferInfo });
      return -1;
    }
    int i1 = localTuSdkMediaCodec.dequeueInputBuffer(10000L);
    if (i1 < 0) {
      return 0;
    }
    if ((paramByteBuffer == null) || (paramBufferInfo.size < 1))
    {
      localTuSdkMediaCodec.queueInputBuffer(i1, 0, 0, paramBufferInfo.presentationTimeUs, 4);
      return 2;
    }
    this.j = i1;
    if ((this.m == null) || (!this.m.onAudioSliceRender(paramByteBuffer, paramBufferInfo, this.o))) {
      a(i1, paramByteBuffer, paramBufferInfo);
    }
    this.j = -1;
    return 1;
  }
  
  private void a(int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    TuSdkMediaCodec localTuSdkMediaCodec = this.a;
    if ((this.h) || (localTuSdkMediaCodec == null) || (this.f == null) || (paramInt < 0)) {
      return;
    }
    ByteBuffer localByteBuffer1 = this.f[paramInt];
    int i1 = 0;
    if (paramByteBuffer.limit() > localByteBuffer1.limit())
    {
      try
      {
        i1 = paramByteBuffer.limit();
        paramBufferInfo.size = Math.min(paramBufferInfo.size, localByteBuffer1.capacity());
        paramByteBuffer.position(paramBufferInfo.offset);
        paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
        localByteBuffer1.clear();
        if (paramByteBuffer.limit() < localByteBuffer1.limit()) {
          localByteBuffer1.limit(paramByteBuffer.limit());
        }
        localByteBuffer1.put(paramByteBuffer);
        localTuSdkMediaCodec.queueInputBuffer(paramInt, 0, paramBufferInfo.size, paramBufferInfo.presentationTimeUs, 0);
        long l1 = i1 - localByteBuffer1.limit();
        int i2 = paramBufferInfo.size;
        int i3 = localByteBuffer1.capacity();
        do
        {
          int i4 = localTuSdkMediaCodec.dequeueInputBuffer(10000L);
          if (i4 > -1)
          {
            ByteBuffer localByteBuffer2 = this.f[i4];
            paramByteBuffer.position(i2);
            if (l1 > localByteBuffer2.capacity())
            {
              i3 += localByteBuffer2.capacity();
              paramByteBuffer.limit(i3);
              i2 = i3;
            }
            else
            {
              paramByteBuffer.limit(i1);
            }
            localByteBuffer2.clear();
            localByteBuffer2.put(paramByteBuffer);
            paramBufferInfo.presentationTimeUs += ((i1 - localByteBuffer1.limit()) / this.l.sampleRate * 1000000.0F);
            localTuSdkMediaCodec.queueInputBuffer(i4, 0, i1 - localByteBuffer1.limit(), paramBufferInfo.presentationTimeUs, 0);
            l1 -= localByteBuffer2.limit();
          }
          if (Thread.interrupted()) {
            break;
          }
        } while (l1 > 0L);
      }
      catch (Exception localException)
      {
        TLog.e(localException);
      }
    }
    else
    {
      paramBufferInfo.size = Math.min(paramBufferInfo.size, localByteBuffer1.capacity());
      paramByteBuffer.position(paramBufferInfo.offset);
      paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
      localByteBuffer1.clear();
      if (paramByteBuffer.limit() < localByteBuffer1.limit()) {
        localByteBuffer1.limit(paramByteBuffer.limit());
      }
      localByteBuffer1.put(paramByteBuffer);
      localTuSdkMediaCodec.queueInputBuffer(paramInt, 0, paramBufferInfo.size, paramBufferInfo.presentationTimeUs, 0);
    }
    this.n += 1L;
  }
  
  private long a(long paramLong)
  {
    if (this.l == null) {
      return 0L;
    }
    return paramLong * 1024000000L / this.l.sampleRate;
  }
  
  public void signalEndOfInputStream(long paramLong)
  {
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    localBufferInfo.size = 0;
    localBufferInfo.presentationTimeUs = paramLong;
    while (writeBuffer(null, localBufferInfo) == 0) {}
  }
  
  public void autoFillMuteData(long paramLong1, long paramLong2, boolean paramBoolean)
  {
    TuSdkMediaCodec localTuSdkMediaCodec = this.a;
    if ((paramLong1 > paramLong2) || (this.h) || (localTuSdkMediaCodec == null) || (this.l == null)) {
      return;
    }
    if (this.k == null) {
      this.k = ByteBuffer.allocate(this.l.channelCount * 128 * this.l.bitWidth);
    }
    long l1 = paramLong1;
    long l2 = 0L;
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    localBufferInfo.size = this.k.capacity();
    localBufferInfo.offset = 0;
    while ((!ThreadHelper.isInterrupted()) && (l1 < paramLong2))
    {
      localBufferInfo.presentationTimeUs = l1;
      while ((!ThreadHelper.isInterrupted()) && (writeBuffer(this.k, localBufferInfo) == 0)) {}
      l1 = a(l2) + paramLong1;
      l2 += 1L;
    }
    localBufferInfo.presentationTimeUs = paramLong2;
    if (paramBoolean)
    {
      signalEndOfInputStream(paramLong2);
      return;
    }
    while ((!ThreadHelper.isInterrupted()) && (writeBuffer(this.k, localBufferInfo) == 0)) {}
  }
  
  public void autoFillMuteDataWithinEnd(long paramLong1, long paramLong2)
  {
    if (this.l == null) {
      return;
    }
    autoFillMuteData(paramLong1, paramLong2 - this.l.intervalUs, false);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioEncodecOperationImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */