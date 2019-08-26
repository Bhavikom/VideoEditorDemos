package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.annotation.TargetApi;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public abstract class AVAsset
{
  protected List<AVAssetTrack> tracks;
  
  public abstract MediaExtractor createExtractor();
  
  public abstract MediaMetadataRetriever metadataRetriever();
  
  @TargetApi(16)
  public List<AVAssetTrack> tracks()
  {
    if (this.tracks != null) {
      return new ArrayList(this.tracks);
    }
    MediaExtractor localMediaExtractor = createExtractor();
    if (localMediaExtractor == null)
    {
      TLog.e("A subclass must implement the createExtractor method", new Object[0]);
      return Arrays.asList(new AVAssetTrack[0]);
    }
    ArrayList localArrayList = new ArrayList();
    try
    {
      if (localMediaExtractor.getTrackCount() <= 0)
      {
        TLog.e("%s : 资产错误无可读取的轨道信息", new Object[] { this });
        localMediaExtractor.release();
        return localArrayList;
      }
      int i = localMediaExtractor.getTrackCount();
      for (int j = 0; j < i; j++)
      {
        MediaFormat localMediaFormat = localMediaExtractor.getTrackFormat(j);
        if (localMediaFormat.getString("mime").startsWith(AVMediaType.AVMediaTypeAudio.getMime())) {
          localArrayList.add(new AVAssetTrack(this, localMediaFormat, AVMediaType.AVMediaTypeAudio, j));
        } else if (localMediaFormat.getString("mime").startsWith(AVMediaType.AVMediaTypeVideo.getMime())) {
          localArrayList.add(new AVAssetTrack(this, localMediaFormat, AVMediaType.AVMediaTypeVideo, j));
        } else {
          TLog.e("%s 该轨道暂不支持 ： %s", new Object[] { this, localMediaFormat });
        }
      }
    }
    catch (Exception localException)
    {
      TLog.e("%s 轨道解析错误 %s", new Object[] { this, localException });
    }
    return localArrayList;
  }
  
  public List<AVAssetTrack> tracksWithMediaType(AVMediaType paramAVMediaType)
  {
    List localList = tracks();
    ArrayList localArrayList = new ArrayList(1);
    if (localList == null) {
      return localArrayList;
    }
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      AVAssetTrack localAVAssetTrack = (AVAssetTrack)localIterator.next();
      if (localAVAssetTrack.mediaType() == paramAVMediaType) {
        localArrayList.add(localAVAssetTrack);
      }
    }
    return localArrayList;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVAsset.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */