package org.lasque.tusdk.core.http;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.ReflectUtils;
import org.lasque.tusdk.core.utils.TLog;

public abstract class FileHttpResponseHandler
  extends ClearHttpResponseHandler
{
  protected final File file;
  protected final boolean append;
  protected final boolean renameIfExists;
  protected File frontendFile;
  
  public FileHttpResponseHandler(File paramFile)
  {
    this(paramFile, false);
  }
  
  public FileHttpResponseHandler(File paramFile, boolean paramBoolean)
  {
    this(paramFile, paramBoolean, false);
  }
  
  public FileHttpResponseHandler(File paramFile, boolean paramBoolean1, boolean paramBoolean2)
  {
    this(paramFile, paramBoolean1, paramBoolean2, false);
  }
  
  public FileHttpResponseHandler(File paramFile, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    super(paramBoolean3);
    ReflectUtils.asserts(paramFile != null, "File passed into FileHttpResponseHandler constructor must not be null");
    if ((!paramFile.isDirectory()) && (!paramFile.getParentFile().isDirectory())) {
      ReflectUtils.asserts(paramFile.getParentFile().mkdirs(), "Cannot create parent directories for requested File location");
    }
    if ((paramFile.isDirectory()) && (!paramFile.mkdirs())) {
      TLog.d("Cannot create directories for requested Directory location, might not be a problem", new Object[0]);
    }
    this.file = paramFile;
    this.append = paramBoolean1;
    this.renameIfExists = paramBoolean2;
  }
  
  public FileHttpResponseHandler(Context paramContext)
  {
    this.file = getTemporaryFile(paramContext);
    this.append = false;
    this.renameIfExists = false;
  }
  
  public boolean deleteTargetFile()
  {
    return (getTargetFile() != null) && (getTargetFile().delete());
  }
  
  protected File getTemporaryFile(Context paramContext)
  {
    ReflectUtils.asserts(paramContext != null, "Tried creating temporary file without having Context");
    try
    {
      return File.createTempFile("temp_", "_handled", paramContext.getCacheDir());
    }
    catch (IOException localIOException)
    {
      TLog.e("Cannot create temporary file: %s", new Object[] { localIOException });
    }
    return null;
  }
  
  protected File getOriginalFile()
  {
    ReflectUtils.asserts(this.file != null, "Target file is null, fatal!");
    return this.file;
  }
  
  public File getTargetFile()
  {
    if (this.frontendFile == null) {
      this.frontendFile = (getOriginalFile().isDirectory() ? getTargetFileByParsingURL() : getOriginalFile());
    }
    return this.frontendFile;
  }
  
  protected File getTargetFileByParsingURL()
  {
    ReflectUtils.asserts(getOriginalFile().isDirectory(), "Target file is not a directory, cannot proceed");
    ReflectUtils.asserts(getRequestURL() != null, "RequestURL is null, cannot proceed");
    String str1 = getRequestURL().toString();
    String str2 = str1.substring(str1.lastIndexOf('/') + 1, str1.length());
    File localFile = new File(getOriginalFile(), str2);
    if ((localFile.exists()) && (this.renameIfExists))
    {
      String str3;
      if (!str2.contains(".")) {
        str3 = str2 + " (%d)";
      } else {
        str3 = str2.substring(0, str2.lastIndexOf('.')) + " (%d)" + str2.substring(str2.lastIndexOf('.'), str2.length());
      }
      for (int i = 0;; i++)
      {
        localFile = new File(getOriginalFile(), String.format(str3, new Object[] { Integer.valueOf(i) }));
        if (!localFile.exists()) {
          return localFile;
        }
      }
    }
    return localFile;
  }
  
  public final void onFailure(int paramInt, List<HttpHeader> paramList, byte[] paramArrayOfByte, Throwable paramThrowable)
  {
    onFailure(paramInt, paramList, paramThrowable, getTargetFile());
  }
  
  public abstract void onFailure(int paramInt, List<HttpHeader> paramList, Throwable paramThrowable, File paramFile);
  
  public final void onSuccess(int paramInt, List<HttpHeader> paramList, byte[] paramArrayOfByte)
  {
    onSuccess(paramInt, paramList, getTargetFile());
  }
  
  public abstract void onSuccess(int paramInt, List<HttpHeader> paramList, File paramFile);
  
  protected byte[] getResponseData(HttpEntity paramHttpEntity)
  {
    if (paramHttpEntity != null)
    {
      InputStream localInputStream = paramHttpEntity.getContent();
      long l = paramHttpEntity.getContentLength();
      FileOutputStream localFileOutputStream = new FileOutputStream(getTargetFile(), this.append);
      if (localInputStream != null) {
        try
        {
          byte[] arrayOfByte = new byte['က'];
          int j = 0;
          int i;
          while (((i = localInputStream.read(arrayOfByte)) != -1) && (!Thread.currentThread().isInterrupted()))
          {
            j += i;
            localFileOutputStream.write(arrayOfByte, 0, i);
            sendProgressMessage(j, l);
          }
        }
        finally
        {
          FileHelper.safeClose(localInputStream);
          localFileOutputStream.flush();
          FileHelper.safeClose(localFileOutputStream);
        }
      }
    }
    return null;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\FileHttpResponseHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */