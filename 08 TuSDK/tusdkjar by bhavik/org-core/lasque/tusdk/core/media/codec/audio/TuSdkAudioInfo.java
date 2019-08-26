package org.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
import android.media.MediaFormat;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;

@TargetApi(18)
public class TuSdkAudioInfo
{
  public int channelCount = 0;
  public long durationUs = 0L;
  public int sampleRate = 0;
  public long intervalUs;
  public int bitWidth = 16;
  public byte[] header;
  
  public TuSdkAudioInfo clone()
  {
    TuSdkAudioInfo localTuSdkAudioInfo = new TuSdkAudioInfo();
    localTuSdkAudioInfo.channelCount = this.channelCount;
    localTuSdkAudioInfo.durationUs = this.durationUs;
    localTuSdkAudioInfo.sampleRate = this.sampleRate;
    localTuSdkAudioInfo.intervalUs = this.intervalUs;
    localTuSdkAudioInfo.bitWidth = this.bitWidth;
    localTuSdkAudioInfo.header = this.header;
    return localTuSdkAudioInfo;
  }
  
  public TuSdkAudioInfo() {}
  
  public TuSdkAudioInfo(MediaFormat paramMediaFormat)
  {
    this();
    setMediaFormat(paramMediaFormat);
  }
  
  public void setMediaFormat(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return;
    }
    this.channelCount = TuSdkMediaFormat.getAudioChannelCount(paramMediaFormat);
    this.durationUs = TuSdkMediaFormat.getKeyDurationUs(paramMediaFormat);
    this.sampleRate = TuSdkMediaFormat.getAudioSampleRate(paramMediaFormat);
    this.intervalUs = (1024000000 / this.sampleRate);
    this.bitWidth = TuSdkMediaFormat.getAudioBitWidth(paramMediaFormat, this.bitWidth);
    if (paramMediaFormat.containsKey("csd-0"))
    {
      ByteBuffer localByteBuffer = paramMediaFormat.getByteBuffer("csd-0");
      this.header = new byte[localByteBuffer.capacity()];
      localByteBuffer.position(0);
      localByteBuffer.get(this.header);
      localByteBuffer.position(0);
    }
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("TuSdkAudioInfo").append("{ \n");
    localStringBuffer.append("channelCount: ").append(this.channelCount).append(", \n");
    localStringBuffer.append("durationUs: ").append(this.durationUs).append(", \n");
    localStringBuffer.append("sampleRate: ").append(this.sampleRate).append(", \n");
    localStringBuffer.append("intervalUs: ").append(this.intervalUs).append(", \n");
    localStringBuffer.append("bitWidth: ").append(this.bitWidth).append(", \n");
    if (this.header != null) {
      localStringBuffer.append("header: ").append(Arrays.toString(this.header)).append(", \n");
    }
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */