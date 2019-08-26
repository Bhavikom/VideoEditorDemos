package org.lasque.tusdk.api.postpro;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.api.TuSDKPostProcessJNI;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.utils.TLog;

public abstract class TuSDKPostProcess
{
  protected final boolean process(TuSDKMediaDataSource paramTuSDKMediaDataSource, File paramFile, List<PostProcessArg> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0))
    {
      TLog.e("%s : Please set input parameters.", new Object[] { this });
      return false;
    }
    if ((paramTuSDKMediaDataSource == null) || (!paramTuSDKMediaDataSource.isValid()))
    {
      TLog.e("%s : Please set valid data source.", new Object[] { this });
      return false;
    }
    if (paramFile == null)
    {
      TLog.e("%s :Please set a file output path.", new Object[] { this });
      return false;
    }
    if (paramTuSDKMediaDataSource.getFile().getAbsolutePath().equals(paramFile.getAbsolutePath()))
    {
      TLog.e("%s :Please set a valid output.", new Object[] { this });
      return false;
    }
    ArrayList localArrayList = new ArrayList();
    File localFile = null;
    if (paramTuSDKMediaDataSource.getFilePath() != null)
    {
      localArrayList.add(new PostProcessArg("-i", paramTuSDKMediaDataSource.getFilePath()));
    }
    else
    {
      localFile = new File(TuSdk.getAppTempPath() + "/" + System.currentTimeMillis());
      a(paramTuSDKMediaDataSource.getFileUri(), localFile);
      if (!localFile.exists()) {
        return false;
      }
      localArrayList.add(new PostProcessArg("-i", localFile.getAbsolutePath()));
    }
    localArrayList.addAll(paramList);
    localArrayList.add(new PostProcessArg(null, paramFile.getAbsolutePath()));
    boolean bool = process(localArrayList);
    if (localFile != null) {
      localFile.delete();
    }
    return bool;
  }
  
  private void a(Uri paramUri, File paramFile)
  {
    InputStream localInputStream = null;
    BufferedOutputStream localBufferedOutputStream = null;
    try
    {
      localInputStream = TuSdkContext.context().getContentResolver().openInputStream(paramUri);
      localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramFile, false));
      byte[] arrayOfByte = new byte['Ѐ'];
      localInputStream.read(arrayOfByte);
      do
      {
        localBufferedOutputStream.write(arrayOfByte);
      } while (localInputStream.read(arrayOfByte) != -1);
      return;
    }
    catch (IOException localIOException2)
    {
      localIOException2.printStackTrace();
    }
    finally
    {
      try
      {
        if (localInputStream != null) {
          localInputStream.close();
        }
        if (localBufferedOutputStream != null) {
          localBufferedOutputStream.close();
        }
      }
      catch (IOException localIOException4)
      {
        localIOException4.printStackTrace();
      }
    }
  }
  
  protected boolean process(List<PostProcessArg> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0)) {
      return false;
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("ffmpeg");
    for (int i = 0; i < paramList.size(); i++)
    {
      PostProcessArg localPostProcessArg = (PostProcessArg)paramList.get(i);
      if (!TextUtils.isEmpty(localPostProcessArg.getKey())) {
        localArrayList.add(((PostProcessArg)paramList.get(i)).getKey());
      }
      if (!TextUtils.isEmpty(localPostProcessArg.getValue())) {
        localArrayList.add(((PostProcessArg)paramList.get(i)).getValue());
      }
    }
    return TuSDKPostProcessJNI.runVideoCommands((String[])localArrayList.toArray(new String[localArrayList.size()]));
  }
  
  public static class PostProcessArg
  {
    private String a;
    private String b;
    
    public PostProcessArg(String paramString1, String paramString2)
    {
      this.a = paramString1;
      this.b = paramString2;
    }
    
    public String getKey()
    {
      return this.a;
    }
    
    public String getValue()
    {
      return this.b;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\postpro\TuSDKPostProcess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */