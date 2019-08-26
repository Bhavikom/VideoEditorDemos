package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.annotation.TargetApi;
import android.media.MediaFormat;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

@TargetApi(16)
public class AVAssetTrack
{
  private AVAsset a;
  private MediaFormat b;
  private AVMediaType c;
  private int d;
  private AVTimeRange e;
  
  public AVAssetTrack(AVAsset paramAVAsset, MediaFormat paramMediaFormat, AVMediaType paramAVMediaType, int paramInt)
  {
    this.a = paramAVAsset;
    this.c = paramAVMediaType;
    this.d = paramInt;
    this.b = paramMediaFormat;
  }
  
  public AVTimeRange timeRange()
  {
    return this.e;
  }
  
  public AVMediaType mediaType()
  {
    return this.c;
  }
  
  public MediaFormat mediaFormat()
  {
    return this.b;
  }
  
  public AVAsset getAsset()
  {
    return this.a;
  }
  
  public int getTrackID()
  {
    return this.d;
  }
  
  public void setTimeRange(AVTimeRange paramAVTimeRange)
  {
    this.e = paramAVTimeRange;
  }
  
  public TuSdkSize naturalSize()
  {
    int i = this.b.getInteger("width");
    int j = this.b.getInteger("height");
    return TuSdkSize.create(i, j);
  }
  
  public TuSdkSize presentSize()
  {
    if ((orientation() == ImageOrientation.Up) || (orientation() == ImageOrientation.Down)) {
      return naturalSize();
    }
    TuSdkSize localTuSdkSize = naturalSize();
    return TuSdkSize.create(localTuSdkSize.height, localTuSdkSize.width);
  }
  
  public long minFrameDuration()
  {
    if (this.b.containsKey("frame-rate")) {
      return 1 / this.b.getInteger("frame-rate") * 1000L;
    }
    return 0L;
  }
  
  public ImageOrientation orientation()
  {
    return TuSdkMediaFormat.getVideoRotation(getAsset().metadataRetriever());
  }
  
  public long durationTimeUs()
  {
    if ((this.b != null) && (this.b.containsKey("durationUs"))) {
      return TuSdkMediaFormat.getKeyDurationUs(this.b) - minFrameDuration() * 10L;
    }
    return TuSdkMediaFormat.getKeyDuration(getAsset().metadataRetriever()) * 1000L - minFrameDuration();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVAssetTrack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */