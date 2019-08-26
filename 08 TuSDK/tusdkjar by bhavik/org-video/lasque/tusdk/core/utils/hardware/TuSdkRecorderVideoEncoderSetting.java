package org.lasque.tusdk.core.utils.hardware;

import org.lasque.tusdk.core.media.codec.video.TuSdkVideoQuality;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSdkRecorderVideoEncoderSetting
{
  public TuSdkSize videoSize = new TuSdkSize(320, 480);
  public TuSdkVideoQuality videoQuality = TuSdkVideoQuality.RECORD_HIGH2;
  public int mediacodecAVCIFrameInterval = 1;
  public boolean enableAllKeyFrame = false;
  
  public static TuSdkRecorderVideoEncoderSetting getDefaultRecordSetting()
  {
    TuSdkRecorderVideoEncoderSetting localTuSdkRecorderVideoEncoderSetting = new TuSdkRecorderVideoEncoderSetting();
    localTuSdkRecorderVideoEncoderSetting.videoQuality = TuSdkVideoQuality.safeQuality();
    return localTuSdkRecorderVideoEncoderSetting;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\utils\hardware\TuSdkRecorderVideoEncoderSetting.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */