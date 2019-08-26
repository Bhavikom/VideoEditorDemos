package org.lasque.tusdk.core.media.codec.video;

import android.annotation.TargetApi;
import android.graphics.RectF;
import android.media.MediaFormat;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.lasque.tusdk.core.media.codec.extend.TuSdkH264SPS;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

@TargetApi(18)
public class TuSdkVideoInfo
{
  public TuSdkSize size;
  public long durationUs = 0L;
  public ImageOrientation orientation;
  public TuSdkH264SPS sps;
  public byte[] pps;
  public int bitrate;
  public int frameRates;
  public long intervalUs;
  public RectF codecCrop;
  public TuSdkSize codecSize;
  
  public TuSdkVideoInfo() {}
  
  public TuSdkVideoInfo(MediaFormat paramMediaFormat)
  {
    setMediaFormat(paramMediaFormat);
  }
  
  public void setMediaFormat(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return;
    }
    this.size = TuSdkMediaFormat.getVideoKeySize(paramMediaFormat);
    this.durationUs = TuSdkMediaFormat.getKeyDurationUs(paramMediaFormat);
    try
    {
      if (paramMediaFormat.containsKey("csd-0"))
      {
        ByteBuffer localByteBuffer1 = paramMediaFormat.getByteBuffer("csd-0");
        byte[] arrayOfByte = new byte[localByteBuffer1.capacity()];
        localByteBuffer1.position(0);
        localByteBuffer1.get(arrayOfByte);
        localByteBuffer1.position(0);
        this.sps = new TuSdkH264SPS(arrayOfByte);
      }
    }
    catch (Exception localException)
    {
      TLog.w("read sps error", new Object[0]);
    }
    if (paramMediaFormat.containsKey("csd-1"))
    {
      ByteBuffer localByteBuffer2 = paramMediaFormat.getByteBuffer("csd-1");
      this.pps = new byte[localByteBuffer2.capacity()];
      localByteBuffer2.position(0);
      localByteBuffer2.get(this.pps);
      localByteBuffer2.position(0);
    }
  }
  
  public void setCorp(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return;
    }
    this.codecSize = TuSdkMediaFormat.getVideoKeySize(paramMediaFormat);
    this.codecCrop = TuSdkMediaFormat.getVideoKeyCorpNormalization(paramMediaFormat);
    if ((this.codecSize != null) && (this.codecCrop != null) && (!this.codecCrop.contains(0.0F, 0.0F, 1.0F, 1.0F)))
    {
      this.codecCrop.top = (this.codecCrop.top > 0.0F ? this.codecCrop.top + 2.0F / this.codecSize.height : 0.0F);
      this.codecCrop.bottom = (this.codecCrop.bottom < 1.0F ? this.codecCrop.bottom - 2.0F / this.codecSize.height : 1.0F);
      this.codecCrop.left = (this.codecCrop.left > 0.0F ? this.codecCrop.left + 2.0F / this.codecSize.width : 0.0F);
      this.codecCrop.right = (this.codecCrop.right < 1.0F ? this.codecCrop.right - 2.0F / this.codecSize.width : 1.0F);
    }
    if ((this.sps != null) && (this.sps.sar_width > 0) && (this.sps.sar_height > 0)) {
      if (this.codecSize.maxSide() == this.codecSize.width) {
        this.codecSize.height = (this.codecSize.height * this.sps.sar_height / this.sps.sar_width);
      } else {
        this.codecSize.width = (this.codecSize.width * this.sps.sar_width / this.sps.sar_height);
      }
    }
  }
  
  public void setEncodecInfo(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return;
    }
    this.bitrate = TuSdkMediaFormat.getInteger(paramMediaFormat, "bitrate", 0);
    this.frameRates = TuSdkMediaFormat.getInteger(paramMediaFormat, "frame-rate", 0);
    this.intervalUs = (1000000 / this.frameRates);
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("TuSdkVideoInfo").append("{ \n");
    if (this.size != null) {
      localStringBuffer.append("size: ").append(this.size).append(", \n");
    }
    localStringBuffer.append("durationUs: ").append(this.durationUs).append(", \n");
    localStringBuffer.append("bitrate: ").append(this.bitrate).append(", \n");
    localStringBuffer.append("frameRates: ").append(this.frameRates).append(", \n");
    localStringBuffer.append("intervalUs: ").append(this.intervalUs).append(", \n");
    localStringBuffer.append("orientation: ").append(this.orientation).append(", \n");
    if (this.sps != null) {
      localStringBuffer.append("sps: ").append(this.sps).append(", \n");
    }
    if (this.pps != null) {
      localStringBuffer.append("pps: ").append(Arrays.toString(this.pps)).append(", \n");
    }
    localStringBuffer.append("codecCrop: ").append(this.codecCrop).append(", \n");
    localStringBuffer.append("codecSize: ").append(this.codecSize).append(", \n");
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\video\TuSdkVideoInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */