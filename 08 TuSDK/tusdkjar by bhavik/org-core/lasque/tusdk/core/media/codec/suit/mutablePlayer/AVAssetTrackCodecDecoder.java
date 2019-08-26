package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class AVAssetTrackCodecDecoder
  implements AVAssetTrackDecoder
{
  private List<AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput> a;
  private MediaCodec b;
  private Surface c;
  private MediaFormat d;
  private boolean e;
  private AVAssetTrackOutputSouce f;
  private long g;
  private PositionSeeker h = new PositionSeeker();
  
  public AVAssetTrackCodecDecoder(AVAssetTrackOutputSouce paramAVAssetTrackOutputSouce)
  {
    if ((paramAVAssetTrackOutputSouce == null) || (paramAVAssetTrackOutputSouce.inputTrack() == null))
    {
      TLog.i("%s No tracks are available in the data source.", new Object[] { paramAVAssetTrackOutputSouce });
      return;
    }
    this.f = paramAVAssetTrackOutputSouce;
    this.a = new ArrayList();
    this.d = this.f.inputTrack().mediaFormat();
  }
  
  public void setOutputSurface(Surface paramSurface)
  {
    if ((this.f.inputTrack() == null) || (this.f.inputTrack().mediaType() != AVMediaType.AVMediaTypeVideo))
    {
      TLog.i("%s Only video tracks support surface.", new Object[] { this });
      return;
    }
    this.c = paramSurface;
  }
  
  protected void maybeInitCodec()
  {
    if (this.b != null) {
      return;
    }
    try
    {
      String str = TuSdkMediaFormat.getString(this.d, "mime", null);
      if (str == null)
      {
        TLog.e("%s ：The mCodecFormat is invalid. ", new Object[] { this });
        return;
      }
      this.b = MediaCodec.createDecoderByType(str);
      this.b.configure(this.d, this.c, null, 0);
      this.b.start();
      this.e = false;
    }
    catch (Exception localException) {}
  }
  
  protected void releaseCodec()
  {
    if (this.b == null) {
      return;
    }
    this.b.stop();
    this.b.release();
    this.b = null;
  }
  
  public void onInputFormatChanged(MediaFormat paramMediaFormat)
  {
    this.d = paramMediaFormat;
    releaseCodec();
    maybeInitCodec();
  }
  
  public long durationTimeUs()
  {
    return this.f.durationTimeUs();
  }
  
  public long outputTimeUs()
  {
    return this.g;
  }
  
  public boolean seekTo(long paramLong, boolean paramBoolean)
  {
    return PositionSeeker.a(this.h, paramLong, paramBoolean);
  }
  
  public boolean isDecodeCompleted()
  {
    return this.e;
  }
  
  public void reset()
  {
    this.g = 0L;
    this.f.reset();
    releaseCodec();
    this.d = this.f.inputTrack().mediaFormat();
  }
  
  public boolean renderOutputBuffers()
  {
    if (this.f.isOutputDone()) {
      return false;
    }
    maybeInitCodec();
    while (drainOutputBuffer()) {}
    while (feedInputBuffer()) {}
    return true;
  }
  
  public boolean renderOutputBuffer()
  {
    if (this.f.isOutputDone()) {
      return false;
    }
    maybeInitCodec();
    while ((!drainOutputBuffer()) || (feedInputBuffer())) {}
    return true;
  }
  
  public boolean feedInputBuffer()
  {
    try
    {
      if (this.b == null) {
        return false;
      }
      AVSampleBuffer localAVSampleBuffer = this.f.readSampleBuffer(0);
      if ((this.f.isOutputDone()) || (localAVSampleBuffer == null))
      {
        i = this.b.dequeueInputBuffer(0L);
        if (i >= 0) {
          this.b.queueInputBuffer(i, 0, 0, 0L, 4);
        }
        while (drainOutputBuffer()) {}
        return false;
      }
      if (((this.d != null) && (this.d != localAVSampleBuffer.format())) || (localAVSampleBuffer.isFormat()))
      {
        while (drainOutputBuffer()) {}
        onInputFormatChanged(localAVSampleBuffer.format());
        if ((localAVSampleBuffer.isFormat()) && (!localAVSampleBuffer.isKeyFrame())) {
          this.f.advance();
        }
        return true;
      }
      int i = this.b.dequeueInputBuffer(0L);
      if (i < 0) {
        return false;
      }
      switch (i)
      {
      case -1: 
        return false;
      }
      ByteBuffer localByteBuffer = this.b.getInputBuffers()[i];
      localByteBuffer.position(0);
      localByteBuffer.put(localAVSampleBuffer.buffer());
      this.b.queueInputBuffer(i, localAVSampleBuffer.info().offset, localAVSampleBuffer.info().size, localAVSampleBuffer.info().presentationTimeUs, localAVSampleBuffer.info().flags);
      this.f.advance();
      return true;
    }
    catch (Exception localException)
    {
      TLog.e(localException);
    }
    return false;
  }
  
  public boolean drainOutputBuffer()
  {
    try
    {
      if (this.b == null) {
        return false;
      }
      MediaCodec.BufferInfo localBufferInfo1 = new MediaCodec.BufferInfo();
      int i = this.b.dequeueOutputBuffer(localBufferInfo1, 0L);
      if ((localBufferInfo1.flags & 0x4) != 0)
      {
        this.b.releaseOutputBuffer(i, false);
        releaseCodec();
        this.e = true;
        return false;
      }
      switch (i)
      {
      case -3: 
        return true;
      case -1: 
        return false;
      case -2: 
        a(this.b.getOutputFormat());
        return true;
      }
      if ((i >= 0) && (localBufferInfo1.size > 0))
      {
        ByteBuffer localByteBuffer = this.b.getOutputBuffers()[i];
        MediaCodec.BufferInfo localBufferInfo2 = new MediaCodec.BufferInfo();
        localBufferInfo2.presentationTimeUs = localBufferInfo1.presentationTimeUs;
        localBufferInfo2.size = localBufferInfo1.size;
        localBufferInfo2.flags = localBufferInfo1.flags;
        localBufferInfo2.offset = localBufferInfo1.offset;
        AVSampleBuffer localAVSampleBuffer = new AVSampleBuffer(localByteBuffer, localBufferInfo1, null);
        if (this.f.isDecodeOnly(localAVSampleBuffer.info().presentationTimeUs))
        {
          this.b.releaseOutputBuffer(i, false);
          return true;
        }
        boolean bool = PositionSeeker.a(this.h);
        PositionSeeker.b(this.h);
        a(localAVSampleBuffer, i);
        return !bool;
      }
      return true;
    }
    catch (Exception localException)
    {
      TLog.e(localException);
    }
    return false;
  }
  
  @TargetApi(21)
  private void a(AVSampleBuffer paramAVSampleBuffer, int paramInt)
  {
    if (paramInt < 0)
    {
      TLog.i("maybeRender index < 0", new Object[0]);
      return;
    }
    this.g = this.f.calOutputTimeUs(paramAVSampleBuffer.info().presentationTimeUs);
    paramAVSampleBuffer.setRenderTimeUs(this.g);
    if (this.f.inputTrack().mediaType() == AVMediaType.AVMediaTypeAudio)
    {
      a(paramAVSampleBuffer);
      this.b.releaseOutputBuffer(paramInt, true);
    }
    else
    {
      this.b.releaseOutputBuffer(paramInt, true);
      a(paramAVSampleBuffer);
    }
  }
  
  private void a(AVSampleBuffer paramAVSampleBuffer)
  {
    List localList = targets();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput localAVAssetTrackSampleBufferInput = (AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput)localIterator.next();
      localAVAssetTrackSampleBufferInput.newFrameReady(paramAVSampleBuffer);
    }
  }
  
  private void a(MediaFormat paramMediaFormat)
  {
    List localList = targets();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput localAVAssetTrackSampleBufferInput = (AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput)localIterator.next();
      localAVAssetTrackSampleBufferInput.outputFormatChaned(paramMediaFormat, this.f.inputTrack());
    }
  }
  
  public List<AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput> targets()
  {
    return this.a;
  }
  
  public void addTarget(AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput paramAVAssetTrackSampleBufferInput, int paramInt)
  {
    this.a.add(paramInt, paramAVAssetTrackSampleBufferInput);
  }
  
  public void addTarget(AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput paramAVAssetTrackSampleBufferInput)
  {
    this.a.add(paramAVAssetTrackSampleBufferInput);
  }
  
  public void removeTarget(AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput paramAVAssetTrackSampleBufferInput)
  {
    this.a.remove(paramAVAssetTrackSampleBufferInput);
  }
  
  private class PositionSeeker
  {
    private long b;
    
    PositionSeeker()
    {
      b();
    }
    
    private boolean a()
    {
      return this.b > 0L;
    }
    
    private boolean a(long paramLong, boolean paramBoolean)
    {
      if (!paramBoolean) {
        return a(paramLong, 2);
      }
      this.b = paramLong;
      AVAssetTrackCodecDecoder.a(AVAssetTrackCodecDecoder.this).seekTo(paramLong, 0);
      AVAssetTrackCodecDecoder.this.maybeInitCodec();
      AVAssetTrackCodecDecoder.b(AVAssetTrackCodecDecoder.this).flush();
      while ((AVAssetTrackCodecDecoder.c(AVAssetTrackCodecDecoder.this).a()) && (AVAssetTrackCodecDecoder.b(AVAssetTrackCodecDecoder.this) != null))
      {
        while (AVAssetTrackCodecDecoder.this.feedInputBuffer()) {}
        while (AVAssetTrackCodecDecoder.this.drainOutputBuffer()) {}
      }
      return this.b < 0L;
    }
    
    private boolean a(long paramLong, int paramInt)
    {
      this.b = paramLong;
      AVAssetTrackCodecDecoder.a(AVAssetTrackCodecDecoder.this).seekTo(paramLong, paramInt);
      return AVAssetTrackCodecDecoder.this.renderOutputBuffers();
    }
    
    private void b()
    {
      this.b = -1L;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVAssetTrackCodecDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */