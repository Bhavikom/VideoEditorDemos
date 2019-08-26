package org.lasque.tusdk.core.decoder;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.graphics.RectF;
import android.media.MediaFormat;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting.VideoQuality;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSDKVideoInfo
{
  public static final int FF_PROFILE_H264_BASELINE = 66;
  public int width;
  public int height;
  public int fps;
  public int profile;
  public int bitrate;
  public int iFrameInterval;
  public ImageOrientation videoOrientation = ImageOrientation.Up;
  public int degree = 0;
  public boolean existAudioTrack;
  public long durationTimeUs;
  public RectF codecCrop;
  
  @SuppressLint({"InlinedApi"})
  public static TuSDKVideoInfo createWithMediaFormat(MediaFormat paramMediaFormat, boolean paramBoolean)
  {
    if (paramMediaFormat == null) {
      return null;
    }
    TuSDKVideoInfo localTuSDKVideoInfo = new TuSDKVideoInfo();
    localTuSDKVideoInfo.existAudioTrack = paramBoolean;
    localTuSDKVideoInfo.width = paramMediaFormat.getInteger("width");
    localTuSDKVideoInfo.height = paramMediaFormat.getInteger("height");
    if (paramMediaFormat.containsKey("rotation-degrees")) {
      localTuSDKVideoInfo.setVideoRotation(paramMediaFormat.getInteger("rotation-degrees"));
    }
    if (paramMediaFormat.containsKey("durationUs")) {
      localTuSDKVideoInfo.durationTimeUs = paramMediaFormat.getLong("durationUs");
    }
    int i;
    if (paramMediaFormat.containsKey("frame-rate"))
    {
      i = paramMediaFormat.getInteger("frame-rate");
      localTuSDKVideoInfo.fps = (i == 0 ? 30 : i);
    }
    if (paramMediaFormat.containsKey("bitrate"))
    {
      i = paramMediaFormat.getInteger("bitrate");
      localTuSDKVideoInfo.bitrate = (i == 0 ? 3000 : i);
    }
    if (paramMediaFormat.containsKey("i-frame-interval")) {
      localTuSDKVideoInfo.iFrameInterval = paramMediaFormat.getInteger("i-frame-interval");
    }
    localTuSDKVideoInfo.syncCodecCrop(paramMediaFormat);
    return localTuSDKVideoInfo;
  }
  
  public void syncCodecCrop(MediaFormat paramMediaFormat)
  {
    this.codecCrop = TuSdkMediaFormat.getVideoKeyCorpNormalization(paramMediaFormat);
    TuSdkSize localTuSdkSize = TuSdkMediaFormat.getVideoKeySize(paramMediaFormat);
    if ((localTuSdkSize != null) && (this.codecCrop != null) && (!this.codecCrop.contains(0.0F, 0.0F, 1.0F, 1.0F)))
    {
      this.codecCrop.top = (this.codecCrop.top > 0.0F ? this.codecCrop.top + 2.0F / localTuSdkSize.height : 0.0F);
      this.codecCrop.bottom = (this.codecCrop.bottom < 1.0F ? this.codecCrop.bottom - 2.0F / localTuSdkSize.height : 1.0F);
      this.codecCrop.left = (this.codecCrop.left > 0.0F ? this.codecCrop.left + 2.0F / localTuSdkSize.width : 0.0F);
      this.codecCrop.right = (this.codecCrop.right < 1.0F ? this.codecCrop.right - 2.0F / localTuSdkSize.width : 1.0F);
    }
  }
  
  public void setVideoRotation(int paramInt)
  {
    if (paramInt == ImageOrientation.Right.getDegree()) {
      this.videoOrientation = ImageOrientation.Right;
    }
    if (paramInt == ImageOrientation.Down.getDegree()) {
      this.videoOrientation = ImageOrientation.Down;
    }
    if (paramInt == ImageOrientation.Left.getDegree()) {
      this.videoOrientation = ImageOrientation.Left;
    }
    if (paramInt == ImageOrientation.Up.getDegree()) {
      this.videoOrientation = ImageOrientation.Up;
    }
  }
  
  public int getBestBitrateMode()
  {
    if ((this.fps <= 20) && (this.bitrate < TuSDKVideoEncoderSetting.VideoQuality.RECORD_MEDIUM1.getBitrate())) {
      return 0;
    }
    return 2;
  }
  
  public ContentValues getVideoInfoValues()
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("duration", Long.valueOf(this.durationTimeUs / 1000L));
    localContentValues.put("width", Integer.valueOf(this.width));
    localContentValues.put("height", Integer.valueOf(this.height));
    localContentValues.put("resolution", String.format("%dx%d", new Object[] { Integer.valueOf(this.width), Integer.valueOf(this.height) }));
    return localContentValues;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("width : " + this.width).append("\n").append("height : " + this.height).append("\n").append("fps : " + this.fps).append("\n").append("profile : " + this.profile).append("\n").append("bitrate : " + this.bitrate).append("\n").append("videoOrientation : " + this.videoOrientation).append("\n").append("iFrameInterval : " + this.iFrameInterval).append("\n").append("durationTimeUs : " + this.durationTimeUs).append("\n").append("existAudioTrack : " + this.existAudioTrack);
    return localStringBuilder.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKVideoInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */