package org.lasque.tusdk.core.encoder.audio;

import android.annotation.SuppressLint;

@SuppressLint({"InlinedApi"})
public class TuSDKAudioEncoderSetting
{
  protected int audioBufferQueueNum = 10;
  public int audioFormat = 2;
  public int sampleRate = this.audioQuality.getSampleRate();
  public int channelConfig = 12;
  protected int sliceSize = this.audioQuality.getSampleRate() / 10;
  protected int bufferSize = this.sliceSize * 2;
  public AudioQuality audioQuality = AudioQuality.MEDIUM1;
  public int mediacodecAACProfile = 2;
  public int mediacodecAACChannelCount = 2;
  protected int mediacodecAACMaxInputSize = 8820;
  public boolean enableBuffers = false;
  
  public static TuSDKAudioEncoderSetting defaultEncoderSetting()
  {
    return new TuSDKAudioEncoderSetting();
  }
  
  public static enum AudioQuality
  {
    private int a;
    private int b;
    
    private AudioQuality(int paramInt1, int paramInt2)
    {
      this.a = paramInt1;
      this.b = paramInt2;
    }
    
    public int getSampleRate()
    {
      return this.a;
    }
    
    public int getBitrate()
    {
      return this.b;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\encoder\audio\TuSDKAudioEncoderSetting.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */