package org.lasque.tusdk.core.video;

import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting.VideoQuality;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.type.ColorFormatType;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;

public class TuSDKVideoCaptureSetting
{
  public CameraConfigs.CameraFacing facing = CameraConfigs.CameraFacing.Front;
  public TuSdkSize videoSize = new TuSdkSize(320, 480);
  public int fps = TuSDKVideoEncoderSetting.VideoQuality.LIVE_MEDIUM3.getFps();
  public AVCodecType videoAVCodecType = AVCodecType.HW_CODEC;
  public ColorFormatType imageFormatType = ColorFormatType.NV21;
  
  public static enum AVCodecType
  {
    private AVCodecType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\video\TuSDKVideoCaptureSetting.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */