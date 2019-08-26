package org.lasque.tusdk.core.audio;

import org.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting.AudioQuality;

public class TuSDKAudioCaptureSetting
{
  public int audioBufferQueueNum = 10;
  public int audioRecoderFormat = 2;
  public int audioRecoderSampleRate = this.audioQuality.getSampleRate();
  public int audioRecoderChannelConfig = 12;
  public int audioRecoderSliceSize = this.audioQuality.getSampleRate() / 10;
  public int audioRecoderSource = 7;
  public int audioRecoderBufferSize = this.audioRecoderSliceSize * 2;
  public TuSDKAudioEncoderSetting.AudioQuality audioQuality = TuSDKAudioEncoderSetting.AudioQuality.MEDIUM1;
  public boolean shouldEnableAec = true;
  public boolean shouldEnableNs = true;
  
  public static TuSDKAudioCaptureSetting defaultCaptureSetting()
  {
    return new TuSDKAudioCaptureSetting();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\audio\TuSDKAudioCaptureSetting.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */