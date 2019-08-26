package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.annotation.TargetApi;
import android.media.MediaExtractor;
import android.media.MediaMetadataRetriever;
import java.io.File;
import java.io.IOException;

@TargetApi(16)
public class AVAssetFile
  extends AVAsset
{
  private MediaMetadataRetriever a;
  private File b;
  
  public AVAssetFile(File paramFile)
  {
    this.b = paramFile;
  }
  
  public File getFile()
  {
    return this.b;
  }
  
  public String toString()
  {
    return "Asset : " + getFile().getAbsolutePath();
  }
  
  public MediaExtractor createExtractor()
  {
    MediaExtractor localMediaExtractor = new MediaExtractor();
    try
    {
      localMediaExtractor.setDataSource(getFile().getAbsolutePath());
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return localMediaExtractor;
  }
  
  public MediaMetadataRetriever metadataRetriever()
  {
    if (this.a == null)
    {
      this.a = new MediaMetadataRetriever();
      this.a.setDataSource(getFile().getAbsolutePath());
    }
    return this.a;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVAssetFile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */