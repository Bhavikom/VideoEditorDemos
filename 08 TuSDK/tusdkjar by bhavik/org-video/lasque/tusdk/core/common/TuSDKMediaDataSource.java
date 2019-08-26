package org.lasque.tusdk.core.common;

import android.net.Uri;
import android.text.TextUtils;
import java.io.File;

public class TuSDKMediaDataSource
{
  private String a;
  private Uri b;
  
  public static TuSDKMediaDataSource create(String paramString)
  {
    return new TuSDKMediaDataSource(paramString);
  }
  
  public static TuSDKMediaDataSource create(Uri paramUri)
  {
    return new TuSDKMediaDataSource(paramUri);
  }
  
  public TuSDKMediaDataSource() {}
  
  public TuSDKMediaDataSource(String paramString)
  {
    this.a = paramString;
  }
  
  public TuSDKMediaDataSource(Uri paramUri)
  {
    this.b = paramUri;
  }
  
  public TuSDKMediaDataSource(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    if ((paramTuSDKMediaDataSource != null) && (paramTuSDKMediaDataSource.isValid())) {
      if (!TextUtils.isEmpty(paramTuSDKMediaDataSource.getFilePath())) {
        setFilePath(paramTuSDKMediaDataSource.getFilePath());
      } else {
        setFileUri(paramTuSDKMediaDataSource.getFileUri());
      }
    }
  }
  
  public String getFilePath()
  {
    return this.a;
  }
  
  public void setFilePath(String paramString)
  {
    this.a = paramString;
  }
  
  public Uri getFileUri()
  {
    return this.b;
  }
  
  public void setFileUri(Uri paramUri)
  {
    this.b = paramUri;
  }
  
  public File getFile()
  {
    if (!TextUtils.isEmpty(getFilePath())) {
      return new File(getFilePath());
    }
    if (getFileUri() != null) {
      return new File(getFileUri().getPath());
    }
    return null;
  }
  
  public boolean isValid()
  {
    if (!TextUtils.isEmpty(this.a))
    {
      File localFile = new File(getFilePath());
      return (localFile.exists()) && (localFile.canRead());
    }
    return this.b != null;
  }
  
  public String toString()
  {
    if (this.a != null) {
      return this.a;
    }
    if (this.b != null) {
      return this.b.toString();
    }
    return toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\common\TuSDKMediaDataSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */