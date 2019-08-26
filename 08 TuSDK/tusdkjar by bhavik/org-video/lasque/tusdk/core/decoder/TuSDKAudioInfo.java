package org.lasque.tusdk.core.decoder;

import android.annotation.SuppressLint;
import android.media.MediaFormat;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting.AudioQuality;

@SuppressLint({"InlinedApi"})
public class TuSDKAudioInfo
  extends TuSDKMediaDataSource
{
  public int size;
  public int sampleRate = TuSDKAudioEncoderSetting.AudioQuality.MEDIUM2.getSampleRate();
  public int bitrate = TuSDKAudioEncoderSetting.AudioQuality.MEDIUM2.getBitrate();
  public int channel = 2;
  public int channelConfig = 12;
  public int audioFormat = 2;
  public long durationTimeUs;
  public String mime;
  
  public static TuSDKAudioInfo createWithMediaFormat(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return null;
    }
    TuSDKAudioInfo localTuSDKAudioInfo = new TuSDKAudioInfo();
    if (paramMediaFormat.containsKey("channel-count")) {
      localTuSDKAudioInfo.channel = paramMediaFormat.getInteger("channel-count");
    }
    if (localTuSDKAudioInfo.channel == 1) {
      localTuSDKAudioInfo.channelConfig = 4;
    } else {
      localTuSDKAudioInfo.channelConfig = 12;
    }
    if (paramMediaFormat.containsKey("sample-rate")) {
      localTuSDKAudioInfo.sampleRate = paramMediaFormat.getInteger("sample-rate");
    }
    if (paramMediaFormat.containsKey("bitrate")) {
      localTuSDKAudioInfo.bitrate = (paramMediaFormat.getInteger("bitrate") == 0 ? TuSDKAudioEncoderSetting.AudioQuality.MEDIUM2.getBitrate() : paramMediaFormat.getInteger("bitrate"));
    }
    if (paramMediaFormat.containsKey("durationUs")) {
      localTuSDKAudioInfo.durationTimeUs = paramMediaFormat.getLong("durationUs");
    }
    if (paramMediaFormat.containsKey("mime")) {
      localTuSDKAudioInfo.mime = paramMediaFormat.getString("mime");
    }
    if (paramMediaFormat.containsKey("bit-width"))
    {
      int i = paramMediaFormat.getInteger("bit-width");
      if (i == 8) {
        localTuSDKAudioInfo.audioFormat = 3;
      } else {
        localTuSDKAudioInfo.audioFormat = 2;
      }
    }
    return localTuSDKAudioInfo;
  }
  
  public static TuSDKAudioInfo createWithMediaDataSource(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    MediaFormat localMediaFormat = TuSDKMediaUtils.getAudioFormat(paramTuSDKMediaDataSource);
    return createWithMediaFormat(localMediaFormat);
  }
  
  public static TuSDKAudioInfo defaultAudioInfo()
  {
    return new TuSDKAudioInfo();
  }
  
  private int a()
  {
    if (this.audioFormat == 2) {
      return 16;
    }
    if (this.audioFormat == 3) {
      return 8;
    }
    return 16;
  }
  
  public int bytesCountOfTime(int paramInt)
  {
    return paramInt * a() * this.sampleRate * this.channel / 8;
  }
  
  public long getAudioBytesPerSample()
  {
    return (this.sampleRate <= 0 ? 44100 : this.sampleRate) * a() / 8;
  }
  
  public long frameTimeUsWithAudioSize(int paramInt)
  {
    return 1000000 * (paramInt / this.channel) / getAudioBytesPerSample();
  }
  
  public long getFrameInterval()
  {
    return 1024000000 / (this.sampleRate <= 0 ? 44100 : this.sampleRate);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("\n size |" + this.size).append("\n mime |" + this.mime).append("\n sampleRate |" + this.sampleRate).append("\n channel |" + this.channel).append("\n bitrate |" + this.bitrate).append("\n audioFormat |" + a());
    return localStringBuilder.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKAudioInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */