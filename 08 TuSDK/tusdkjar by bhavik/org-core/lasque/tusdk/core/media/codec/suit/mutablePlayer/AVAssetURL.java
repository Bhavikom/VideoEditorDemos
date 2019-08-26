package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaExtractor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import java.io.IOException;

@TargetApi(16)
public class AVAssetURL
  extends AVAsset
{
  private MediaMetadataRetriever a;
  private Uri b;
  private Context c;
  
  public AVAssetURL(Context paramContext, Uri paramUri)
  {
    this.b = paramUri;
    this.c = paramContext;
  }
  
  public Uri fileUri()
  {
    return this.b;
  }
  
  public MediaExtractor createExtractor()
  {
    MediaExtractor localMediaExtractor = new MediaExtractor();
    try
    {
      localMediaExtractor.setDataSource(this.c, this.b, null);
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
      this.a.setDataSource(this.c, this.b);
    }
    return this.a;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVAssetURL.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */