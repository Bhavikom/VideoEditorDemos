package org.lasque.tusdk.core.encoder.video;

import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU.GpuType;

public class TuSDKVideoEncoderSetting
{
  public TuSdkSize videoSize = new TuSdkSize(320, 480);
  public VideoQuality videoQuality = VideoQuality.RECORD_HIGH1;
  public int mediacodecAVCIFrameInterval = 1;
  public boolean enableAllKeyFrame = false;
  public int mediacodecAVCColorFormat;
  public int previewColorFormat = 17;
  public int videoBufferQueueNum = 5;
  public int bitrateMode = 2;
  
  public static TuSDKVideoEncoderSetting getDefaultRecordSetting()
  {
    TuSDKVideoEncoderSetting localTuSDKVideoEncoderSetting = new TuSDKVideoEncoderSetting();
    int i = TuSdkGPU.getGpuType().getPerformance();
    if (i <= 2) {
      localTuSDKVideoEncoderSetting.videoQuality = VideoQuality.RECORD_LOW2;
    } else if (i == 3) {
      localTuSDKVideoEncoderSetting.videoQuality = VideoQuality.RECORD_MEDIUM1;
    } else if (i == 4) {
      localTuSDKVideoEncoderSetting.videoQuality = VideoQuality.RECORD_MEDIUM3;
    } else {
      localTuSDKVideoEncoderSetting.videoQuality = VideoQuality.RECORD_HIGH1;
    }
    return localTuSDKVideoEncoderSetting;
  }
  
  public static enum VideoQuality
  {
    private int a;
    private int b;
    
    private VideoQuality(int paramInt1, int paramInt2)
    {
      this.a = paramInt1;
      this.b = paramInt2;
    }
    
    public int getFps()
    {
      return this.a;
    }
    
    public VideoQuality setFps(int paramInt)
    {
      this.a = paramInt;
      return this;
    }
    
    public int getBitrate()
    {
      return this.b;
    }
    
    public VideoQuality setBitrate(int paramInt)
    {
      this.b = paramInt;
      return this;
    }
    
    public VideoQuality upgrade()
    {
      VideoQuality[] arrayOfVideoQuality = values();
      int i = ordinal();
      if (i + 1 > LIVE_HIGH3.ordinal()) {
        return LIVE_HIGH3;
      }
      if (i + 1 > arrayOfVideoQuality.length - 1) {
        return arrayOfVideoQuality[(arrayOfVideoQuality.length - 1)];
      }
      return arrayOfVideoQuality[(i + 1)];
    }
    
    public VideoQuality degrade()
    {
      VideoQuality[] arrayOfVideoQuality = values();
      int i = ordinal();
      if (i - 1 < 0) {
        return arrayOfVideoQuality[0];
      }
      return arrayOfVideoQuality[(i - 1)];
    }
    
    public String toString()
    {
      return String.format("bitrate: %d | fps: %d ", new Object[] { Integer.valueOf(this.b), Integer.valueOf(this.a) });
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\encoder\video\TuSDKVideoEncoderSetting.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */