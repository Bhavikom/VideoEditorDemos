package org.lasque.tusdk.core.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.lasque.tusdk.core.utils.TLog;

public abstract class RangeFileHttpResponseHandler
  extends FileHttpResponseHandler
{
  public static final int HTTP_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
  private long a = 0L;
  private boolean b = false;
  
  public RangeFileHttpResponseHandler(File paramFile)
  {
    super(paramFile);
  }
  
  public void sendResponseMessage(HttpResponse paramHttpResponse)
  {
    if (!Thread.currentThread().isInterrupted())
    {
      int i = paramHttpResponse.getResponseCode();
      if (i == 416)
      {
        if (!Thread.currentThread().isInterrupted()) {
          sendSuccessMessage(i, paramHttpResponse.getAllHeaders(), null);
        }
      }
      else if (i >= 300)
      {
        if (!Thread.currentThread().isInterrupted()) {
          sendFailureMessage(i, paramHttpResponse.getAllHeaders(), null, new HttpResponseException(i, paramHttpResponse.getResponseMessage()));
        }
      }
      else if (!Thread.currentThread().isInterrupted())
      {
        HttpHeader localHttpHeader = paramHttpResponse.getFirstHeader("Content-Range");
        if (localHttpHeader == null)
        {
          this.b = false;
          this.a = 0L;
        }
        else
        {
          TLog.w("%s : %s", new Object[] { "Content-Range", localHttpHeader.getValue() });
        }
        sendSuccessMessage(i, paramHttpResponse.getAllHeaders(), getResponseData(paramHttpResponse.getEntity()));
      }
    }
  }
  
  protected byte[] getResponseData(HttpEntity paramHttpEntity)
  {
    if (paramHttpEntity != null)
    {
      InputStream localInputStream = paramHttpEntity.getContent();
      long l = paramHttpEntity.getContentLength() + this.a;
      FileOutputStream localFileOutputStream = new FileOutputStream(getTargetFile(), this.b);
      if (localInputStream != null) {
        try
        {
          byte[] arrayOfByte = new byte['က'];
          int i;
          while ((this.a < l) && ((i = localInputStream.read(arrayOfByte)) != -1) && (!Thread.currentThread().isInterrupted()))
          {
            this.a += i;
            localFileOutputStream.write(arrayOfByte, 0, i);
            sendProgressMessage(this.a, l);
          }
        }
        finally
        {
          localInputStream.close();
          localFileOutputStream.flush();
          localFileOutputStream.close();
        }
      }
    }
    return null;
  }
  
  public void updateRequestHeaders(HttpUriRequest paramHttpUriRequest)
  {
    if ((this.file.exists()) && (this.file.canWrite())) {
      this.a = this.file.length();
    }
    if (this.a > 0L)
    {
      this.b = true;
      paramHttpUriRequest.setHeader("Range", "bytes=" + this.a + "-");
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\RangeFileHttpResponseHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */