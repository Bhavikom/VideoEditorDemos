package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.annotation.TargetApi;
import android.media.MediaExtractor;
import android.media.MediaMetadataRetriever;
import android.os.Build.VERSION;
import java.io.File;
import java.io.IOException;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource.TuSdkMediaDataSourceType;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class AVAssetDataSource
  extends AVAsset
{
  private TuSdkMediaDataSource a;
  
  public AVAssetDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    if ((paramTuSdkMediaDataSource == null) || (!paramTuSdkMediaDataSource.isValid())) {
      return;
    }
    this.a = paramTuSdkMediaDataSource;
  }
  
  public TuSdkMediaDataSource dataSource()
  {
    return this.a;
  }
  
  public MediaExtractor createExtractor()
  {
    if (this.a == null) {
      return null;
    }
    MediaExtractor localMediaExtractor = new MediaExtractor();
    try
    {
      if (this.a.getMediaDataType() == TuSdkMediaDataSource.TuSdkMediaDataSourceType.PATH)
      {
        if (!new File(this.a.getPath()).exists())
        {
          TLog.e("%s buildExtractor setDataSource path is incorrect", new Object[] { this });
          return null;
        }
        if (this.a.getRequestHeaders() != null) {
          localMediaExtractor.setDataSource(this.a.getPath(), this.a.getRequestHeaders());
        } else {
          localMediaExtractor.setDataSource(this.a.getPath());
        }
      }
      else if (this.a.getMediaDataType() == TuSdkMediaDataSource.TuSdkMediaDataSourceType.URI)
      {
        localMediaExtractor.setDataSource(this.a.getContext(), this.a.getUri(), this.a.getRequestHeaders());
      }
      else if (this.a.getMediaDataType() == TuSdkMediaDataSource.TuSdkMediaDataSourceType.FILE_DESCRIPTOR)
      {
        localMediaExtractor.setDataSource(this.a.getFileDescriptor(), this.a.getFileDescriptorOffset(), this.a.getFileDescriptorLength());
      }
      else if ((this.a.getMediaDataType() == TuSdkMediaDataSource.TuSdkMediaDataSourceType.MEDIA_DATA_SOURCE) && (Build.VERSION.SDK_INT >= 23))
      {
        localMediaExtractor.setDataSource(this.a.getMediaDataSource());
      }
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "%s buildExtractor need setDataSource", new Object[] { this });
      return null;
    }
    return localMediaExtractor;
  }
  
  public MediaMetadataRetriever metadataRetriever()
  {
    if (this.a == null) {
      return null;
    }
    return this.a.getMediaMetadataRetriever();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVAssetDataSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */