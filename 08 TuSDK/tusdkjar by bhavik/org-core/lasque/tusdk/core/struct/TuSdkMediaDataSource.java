package org.lasque.tusdk.core.struct;

import android.content.Context;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build.VERSION;
import java.io.File;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkMediaDataSource
{
  private MediaDataSource a;
  private Uri b;
  private Context c;
  private String d;
  private FileDescriptor e;
  private long f = 0L;
  private long g = 0L;
  private Map<String, String> h;
  private TuSdkMediaDataSourceType i;
  
  public static List<TuSdkMediaDataSource> create(String... paramVarArgs)
  {
    ArrayList localArrayList = new ArrayList();
    if ((paramVarArgs == null) || (paramVarArgs.length == 0))
    {
      TLog.w("%s create List<TuSdkMediaDataSource> path is null", new Object[] { "TuSdkMediaDataSource" });
      return localArrayList;
    }
    for (String str : paramVarArgs) {
      localArrayList.add(new TuSdkMediaDataSource(str));
    }
    return localArrayList;
  }
  
  public TuSdkMediaDataSource() {}
  
  public TuSdkMediaDataSource(String paramString)
  {
    setPath(paramString);
  }
  
  public TuSdkMediaDataSource(String paramString, Map<String, String> paramMap)
  {
    setPath(paramString, paramMap);
  }
  
  public TuSdkMediaDataSource(Context paramContext, Uri paramUri)
  {
    setUri(paramContext, paramUri);
  }
  
  public TuSdkMediaDataSource(Context paramContext, Uri paramUri, Map<String, String> paramMap)
  {
    setUri(paramContext, paramUri, paramMap);
  }
  
  public TuSdkMediaDataSource(MediaDataSource paramMediaDataSource)
  {
    setMediaDataSource(paramMediaDataSource);
  }
  
  public TuSdkMediaDataSource(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2)
  {
    setFileDescriptor(paramFileDescriptor, paramLong1, paramLong2);
  }
  
  public boolean isValid()
  {
    switch (1.a[getMediaDataType().ordinal()])
    {
    case 1: 
      if (getMediaDataSource() != null) {
        return true;
      }
    case 2: 
      if (getFileDescriptor() != null) {
        return true;
      }
    case 3: 
      if (new File(getPath()).exists()) {
        return true;
      }
    case 4: 
      if (getUri() != null) {
        return true;
      }
      break;
    }
    return false;
  }
  
  public MediaMetadataRetriever getMediaMetadataRetriever()
  {
    MediaMetadataRetriever localMediaMetadataRetriever = new MediaMetadataRetriever();
    switch (1.a[getMediaDataType().ordinal()])
    {
    case 4: 
      localMediaMetadataRetriever.setDataSource(getContext(), getUri());
      return localMediaMetadataRetriever;
    case 3: 
      if ((getRequestHeaders() == null) || (Build.VERSION.SDK_INT <= 13)) {
        localMediaMetadataRetriever.setDataSource(getPath());
      } else {
        localMediaMetadataRetriever.setDataSource(getPath(), getRequestHeaders());
      }
      return localMediaMetadataRetriever;
    case 2: 
      if (getFileDescriptorLength() == 0L) {
        localMediaMetadataRetriever.setDataSource(getFileDescriptor());
      } else {
        localMediaMetadataRetriever.setDataSource(getFileDescriptor(), getFileDescriptorOffset(), getFileDescriptorLength());
      }
      return localMediaMetadataRetriever;
    case 1: 
      if (Build.VERSION.SDK_INT >= 23)
      {
        localMediaMetadataRetriever.setDataSource(getMediaDataSource());
        return localMediaMetadataRetriever;
      }
      TLog.e("%s if use MediaDataSource,current SDK VERSION must >= 23 ", new Object[] { "TuSdkMediaDataSource" });
      return null;
    }
    TLog.e("%s unkwon MediaDataType", new Object[] { "TuSdkMediaDataSource" });
    return null;
  }
  
  public MediaDataSource getMediaDataSource()
  {
    return this.a;
  }
  
  public void setMediaDataSource(MediaDataSource paramMediaDataSource)
  {
    this.i = TuSdkMediaDataSourceType.MEDIA_DATA_SOURCE;
    this.a = paramMediaDataSource;
  }
  
  public Uri getUri()
  {
    return this.b;
  }
  
  public void setUri(Context paramContext, Uri paramUri)
  {
    setUri(paramContext, paramUri, null);
  }
  
  public void setUri(Context paramContext, Uri paramUri, Map<String, String> paramMap)
  {
    this.i = TuSdkMediaDataSourceType.URI;
    this.c = paramContext;
    this.b = paramUri;
    this.h = paramMap;
  }
  
  public Context getContext()
  {
    return this.c;
  }
  
  public String getPath()
  {
    return this.d;
  }
  
  public void setPath(String paramString)
  {
    setPath(paramString, null);
  }
  
  public void setPath(String paramString, Map<String, String> paramMap)
  {
    this.i = TuSdkMediaDataSourceType.PATH;
    this.d = paramString;
    this.h = paramMap;
  }
  
  public FileDescriptor getFileDescriptor()
  {
    return this.e;
  }
  
  public void setFileDescriptor(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2)
  {
    this.i = TuSdkMediaDataSourceType.FILE_DESCRIPTOR;
    this.e = paramFileDescriptor;
    this.f = paramLong1;
    this.g = paramLong2;
  }
  
  public long getFileDescriptorOffset()
  {
    return this.f;
  }
  
  public void setFileDescriptorOffset(long paramLong)
  {
    this.f = paramLong;
  }
  
  public long getFileDescriptorLength()
  {
    return this.g;
  }
  
  public void setFileDescriptorLength(long paramLong)
  {
    this.g = paramLong;
  }
  
  public Map<String, String> getRequestHeaders()
  {
    return this.h;
  }
  
  public void setRequestHeaders(Map<String, String> paramMap)
  {
    this.h = paramMap;
  }
  
  public TuSdkMediaDataSourceType getMediaDataType()
  {
    return this.i;
  }
  
  public void setMediaDataType(TuSdkMediaDataSourceType paramTuSdkMediaDataSourceType)
  {
    this.i = paramTuSdkMediaDataSourceType;
  }
  
  public void deleted()
  {
    if (isValid()) {
      return;
    }
    File localFile = null;
    switch (1.a[this.i.ordinal()])
    {
    case 3: 
      localFile = new File(this.d);
    }
    if ((localFile == null) || (!localFile.isFile())) {
      return;
    }
    localFile.delete();
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("TuSdkMediaDataSource").append("{ \n");
    if (this.a != null) {
      localStringBuffer.append("MediaDataSource: ").append(this.a).append(", \n");
    }
    if (this.b != null) {
      localStringBuffer.append("Uri: ").append(this.b).append(", \n");
    }
    if (this.c != null) {
      localStringBuffer.append("Context: ").append(this.c).append(", \n");
    }
    if (this.d != null) {
      localStringBuffer.append("Path: ").append(this.d).append(", \n");
    }
    if (this.e != null)
    {
      localStringBuffer.append("FileDescriptor: ").append(this.e).append(", \n");
      localStringBuffer.append("FileDescriptorOffset: ").append(this.f).append(", \n");
      localStringBuffer.append("FileDescriptorLength: ").append(this.g).append(", \n");
    }
    if (this.h != null) {
      localStringBuffer.append("RequestHeaders: ").append(this.h).append(", \n");
    }
    localStringBuffer.append("MediaDataType: ").append(this.i).append(", \n");
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
  
  public static enum TuSdkMediaDataSourceType
  {
    private TuSdkMediaDataSourceType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\struct\TuSdkMediaDataSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */