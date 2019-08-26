package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import java.nio.ByteBuffer;
import junit.framework.Assert;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
class AVAssetTrackMediaExtractor
  implements AVAssetTrackExtractor
{
  private AVAssetTrack a;
  private MediaExtractor b;
  private ByteBuffer c;
  private AVTimeRange d;
  private long e = -1L;
  private boolean f;
  private boolean g;
  
  public AVAssetTrackMediaExtractor(AVAssetTrack paramAVAssetTrack)
  {
    Assert.assertNotNull(String.format("%s : The track parameter cannot be null ", new Object[] { this }), paramAVAssetTrack);
    Assert.assertEquals(String.format("%s : Please check whether the track information is valid", new Object[] { this }), true, paramAVAssetTrack.getTrackID() >= 0);
    this.a = paramAVAssetTrack;
    if (this.a.timeRange() != null) {
      this.d = this.a.timeRange();
    } else {
      this.d = AVTimeRange.AVTimeRangeMake(0L, this.a.durationTimeUs());
    }
    prepare();
  }
  
  public AVTimeRange timeRange()
  {
    return this.d;
  }
  
  public void setTimeRange(AVTimeRange paramAVTimeRange)
  {
    if (paramAVTimeRange == null) {
      return;
    }
    if (paramAVTimeRange.durationUs() <= 0L) {
      return;
    }
    this.d = AVTimeRange.AVTimeRangeMake(paramAVTimeRange.startUs(), Math.min(paramAVTimeRange.durationUs(), inputTrack().durationTimeUs()));
  }
  
  protected boolean prepare()
  {
    if (this.b != null) {
      return true;
    }
    reset();
    this.b = this.a.getAsset().createExtractor();
    try
    {
      if (this.b.getTrackCount() <= 0)
      {
        TLog.e("%s 资产错误无可读取的轨道信息", new Object[] { this });
        this.b.release();
        return false;
      }
      this.b.selectTrack(this.a.getTrackID());
      if (this.a.mediaFormat() == null)
      {
        TLog.e("%s : %d 轨道索引错误", new Object[] { Integer.valueOf(this.a.getTrackID()) });
        this.b.release();
        return false;
      }
      this.b.seekTo(this.d.startUs(), 0);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      return false;
    }
    return false;
  }
  
  public AVAssetTrack inputTrack()
  {
    return this.a;
  }
  
  public boolean isDecodeOnly(long paramLong)
  {
    if (this.e > 0L) {
      return paramLong < this.e;
    }
    return !this.d.containsTimeUs(paramLong);
  }
  
  public long durationTimeUs()
  {
    if (inputTrack() == null) {
      return 0L;
    }
    if (this.d != null) {
      return this.d.durationUs();
    }
    return inputTrack().durationTimeUs();
  }
  
  public long outputTimeUs()
  {
    return this.b.getSampleTime();
  }
  
  public long calOutputTimeUs(long paramLong)
  {
    return paramLong;
  }
  
  public boolean seekTo(long paramLong, int paramInt)
  {
    if ((this.b == null) || (paramLong < 0L)) {
      return false;
    }
    this.e = paramLong;
    this.b.seekTo(paramLong, paramInt);
    return true;
  }
  
  public AVSampleBuffer readNextSampleBuffer(int paramInt)
  {
    Assert.assertNotNull(String.format("%s : The track parameter cannot be null ", new Object[] { this }), this.a);
    AVSampleBuffer localAVSampleBuffer = readSampleBuffer(paramInt);
    advance();
    return localAVSampleBuffer;
  }
  
  public AVSampleBuffer readSampleBuffer(int paramInt)
  {
    Assert.assertNotNull(String.format("%s : The track parameter cannot be null ", new Object[] { this }), this.a);
    if (!a())
    {
      TLog.i("readSampleBuffer read done. ", new Object[0]);
      this.f = true;
      return null;
    }
    if ((this.c == null) || (this.g)) {
      try
      {
        int i = this.a.mediaFormat().getInteger("max-input-size");
        this.c = ByteBuffer.allocate(i);
      }
      catch (Exception localException)
      {
        TLog.w("get max input size error", new Object[0]);
        this.c = ByteBuffer.allocate(4096);
      }
    }
    this.c.clear();
    int j = this.b.readSampleData(this.c, paramInt);
    if (j <= 0) {
      return null;
    }
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    localBufferInfo.offset = 0;
    localBufferInfo.presentationTimeUs = this.b.getSampleTime();
    localBufferInfo.size = j;
    localBufferInfo.flags = this.b.getSampleFlags();
    int k = isDecodeOnly(localBufferInfo.presentationTimeUs) ? 2 : 0;
    AVSampleBuffer localAVSampleBuffer = new AVSampleBuffer(this.c, localBufferInfo, this.a.mediaFormat(), k);
    return localAVSampleBuffer;
  }
  
  private boolean a()
  {
    return outputTimeUs() <= this.d.endUs();
  }
  
  public boolean advance()
  {
    Assert.assertNotNull(String.format("%s : The track parameter cannot be null ", new Object[] { this }), this.a);
    if (!a())
    {
      this.f = true;
      TLog.i("readSampleBuffer read done. can't advance", new Object[0]);
      return false;
    }
    boolean bool = this.b.advance();
    this.f = (!bool);
    return bool;
  }
  
  public boolean isOutputDone()
  {
    if (this.b == null) {
      return false;
    }
    return this.f;
  }
  
  public void reset()
  {
    this.f = false;
    this.e = -1L;
    if (this.b != null) {
      this.b.seekTo(this.d.startUs(), 2);
    }
  }
  
  public void setAlwaysCopiesSampleData(boolean paramBoolean)
  {
    this.g = paramBoolean;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVAssetTrackMediaExtractor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */