package org.lasque.tusdk.core.video;

import java.io.File;
import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;

public class TuSDKVideoResult
{
  public File videoPath;
  public ImageSqlInfo videoSqlInfo;
  public TuSDKVideoInfo videoInfo;
  @Deprecated
  public int duration;
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("videoPath : " + this.videoPath).append("\n").append("videoInfo : " + this.videoInfo);
    return localStringBuilder.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\video\TuSDKVideoResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */