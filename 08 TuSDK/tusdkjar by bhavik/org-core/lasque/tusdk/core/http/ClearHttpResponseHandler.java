package org.lasque.tusdk.core.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.ReflectUtils;
import org.lasque.tusdk.core.utils.TLog;

public abstract class ClearHttpResponseHandler
  implements ResponseHandlerInterface
{
  public static final String DEFAULT_CHARSET = "UTF-8";
  public static final String UTF8_BOM = "﻿";
  protected static final int SUCCESS_MESSAGE = 0;
  protected static final int FAILURE_MESSAGE = 1;
  protected static final int START_MESSAGE = 2;
  protected static final int FINISH_MESSAGE = 3;
  protected static final int PROGRESS_MESSAGE = 4;
  protected static final int RETRY_MESSAGE = 5;
  protected static final int CANCEL_MESSAGE = 6;
  protected static final int BUFFER_SIZE = 4096;
  private String a = "UTF-8";
  private Handler b;
  private boolean c;
  private boolean d;
  private URL e = null;
  private List<HttpHeader> f = null;
  private Looper g = null;
  private WeakReference<Object> h = new WeakReference(null);
  
  public ClearHttpResponseHandler()
  {
    this(null);
  }
  
  public ClearHttpResponseHandler(Looper paramLooper)
  {
    this.g = (paramLooper == null ? Looper.myLooper() : paramLooper);
    setUseSynchronousMode(false);
    setUsePoolThread(false);
  }
  
  public ClearHttpResponseHandler(boolean paramBoolean)
  {
    setUsePoolThread(paramBoolean);
    if (!getUsePoolThread())
    {
      this.g = Looper.myLooper();
      setUseSynchronousMode(false);
    }
  }
  
  public Object getTag()
  {
    return this.h.get();
  }
  
  public void setTag(Object paramObject)
  {
    this.h = new WeakReference(paramObject);
  }
  
  public URL getRequestURL()
  {
    return this.e;
  }
  
  public void setRequestURL(URL paramURL)
  {
    this.e = paramURL;
  }
  
  public List<HttpHeader> getRequestHeaders()
  {
    return this.f;
  }
  
  public void setRequestHeaders(List<HttpHeader> paramList)
  {
    this.f = paramList;
  }
  
  public boolean getUseSynchronousMode()
  {
    return this.c;
  }
  
  public void setUseSynchronousMode(boolean paramBoolean)
  {
    if ((!paramBoolean) && (this.g == null))
    {
      paramBoolean = true;
      TLog.w("Current thread has not called Looper.prepare(). Forcing synchronous mode.", new Object[0]);
    }
    if ((!paramBoolean) && (this.b == null)) {
      this.b = new ResponderHandler(this, this.g);
    } else if ((paramBoolean) && (this.b != null)) {
      this.b = null;
    }
    this.c = paramBoolean;
  }
  
  public boolean getUsePoolThread()
  {
    return this.d;
  }
  
  public void setUsePoolThread(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.g = null;
      this.b = null;
    }
    this.d = paramBoolean;
  }
  
  public String getCharset()
  {
    return this.a == null ? "UTF-8" : this.a;
  }
  
  public void setCharset(String paramString)
  {
    this.a = paramString;
  }
  
  public void onProgress(long paramLong1, long paramLong2)
  {
    TLog.i("Progress %d from %d (%2.0f%%)", new Object[] { Long.valueOf(paramLong1), Long.valueOf(paramLong2), Double.valueOf(paramLong2 > 0L ? paramLong1 * 1.0D / paramLong2 * 100.0D : -1.0D) });
  }
  
  public void onStart() {}
  
  public void onFinish() {}
  
  public void onPreProcessResponse(ResponseHandlerInterface paramResponseHandlerInterface, HttpResponse paramHttpResponse) {}
  
  public void onPostProcessResponse(ResponseHandlerInterface paramResponseHandlerInterface, HttpResponse paramHttpResponse) {}
  
  public abstract void onSuccess(int paramInt, List<HttpHeader> paramList, byte[] paramArrayOfByte);
  
  public abstract void onFailure(int paramInt, List<HttpHeader> paramList, byte[] paramArrayOfByte, Throwable paramThrowable);
  
  public void onRetry(int paramInt)
  {
    TLog.d("Request retry no. %d", new Object[] { Integer.valueOf(paramInt) });
  }
  
  public void onCancel()
  {
    TLog.d("Request got cancelled", new Object[0]);
  }
  
  public void onUserException(Throwable paramThrowable)
  {
    TLog.e("User-space exception detected! : %s", new Object[] { paramThrowable });
    throw new RuntimeException(paramThrowable);
  }
  
  public final void sendProgressMessage(long paramLong1, long paramLong2)
  {
    sendMessage(obtainMessage(4, new Object[] { Long.valueOf(paramLong1), Long.valueOf(paramLong2) }));
  }
  
  public final void sendSuccessMessage(int paramInt, List<HttpHeader> paramList, byte[] paramArrayOfByte)
  {
    sendMessage(obtainMessage(0, new Object[] { Integer.valueOf(paramInt), paramList, paramArrayOfByte }));
  }
  
  public final void sendFailureMessage(int paramInt, List<HttpHeader> paramList, byte[] paramArrayOfByte, Throwable paramThrowable)
  {
    sendMessage(obtainMessage(1, new Object[] { Integer.valueOf(paramInt), paramList, paramArrayOfByte, paramThrowable }));
  }
  
  public final void sendStartMessage()
  {
    sendMessage(obtainMessage(2, null));
  }
  
  public final void sendFinishMessage()
  {
    sendMessage(obtainMessage(3, null));
  }
  
  public final void sendRetryMessage(int paramInt)
  {
    sendMessage(obtainMessage(5, new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public final void sendCancelMessage()
  {
    sendMessage(obtainMessage(6, null));
  }
  
  protected void handleMessage(Message paramMessage)
  {
    try
    {
      Object[] arrayOfObject;
      switch (paramMessage.what)
      {
      case 0: 
        arrayOfObject = (Object[])paramMessage.obj;
        if ((arrayOfObject != null) && (arrayOfObject.length >= 3)) {
          onSuccess(((Integer)arrayOfObject[0]).intValue(), (List)arrayOfObject[1], (byte[])arrayOfObject[2]);
        } else {
          TLog.e("SUCCESS_MESSAGE didn't got enough params", new Object[0]);
        }
        break;
      case 1: 
        arrayOfObject = (Object[])paramMessage.obj;
        if ((arrayOfObject != null) && (arrayOfObject.length >= 4)) {
          onFailure(((Integer)arrayOfObject[0]).intValue(), (List)arrayOfObject[1], (byte[])arrayOfObject[2], (Throwable)arrayOfObject[3]);
        } else {
          TLog.e("FAILURE_MESSAGE didn't got enough params", new Object[0]);
        }
        break;
      case 2: 
        onStart();
        break;
      case 3: 
        onFinish();
        break;
      case 4: 
        arrayOfObject = (Object[])paramMessage.obj;
        if ((arrayOfObject != null) && (arrayOfObject.length >= 2)) {
          try
          {
            onProgress(((Long)arrayOfObject[0]).longValue(), ((Long)arrayOfObject[1]).longValue());
          }
          catch (Throwable localThrowable1)
          {
            TLog.e("custom onProgress contains an error: %s", new Object[] { localThrowable1 });
          }
        } else {
          TLog.e("PROGRESS_MESSAGE didn't got enough params", new Object[0]);
        }
        break;
      case 5: 
        arrayOfObject = (Object[])paramMessage.obj;
        if ((arrayOfObject != null) && (arrayOfObject.length == 1)) {
          onRetry(((Integer)arrayOfObject[0]).intValue());
        } else {
          TLog.e("RETRY_MESSAGE didn't get enough params", new Object[0]);
        }
        break;
      case 6: 
        onCancel();
      }
    }
    catch (Throwable localThrowable2)
    {
      onUserException(localThrowable2);
    }
  }
  
  protected void sendMessage(Message paramMessage)
  {
    if ((getUseSynchronousMode()) || (this.b == null))
    {
      handleMessage(paramMessage);
    }
    else if (!Thread.currentThread().isInterrupted())
    {
      ReflectUtils.asserts(this.b != null, "handler should not be null!");
      this.b.sendMessage(paramMessage);
    }
  }
  
  protected void postRunnable(Runnable paramRunnable)
  {
    if (paramRunnable != null) {
      if ((getUseSynchronousMode()) || (this.b == null)) {
        paramRunnable.run();
      } else {
        this.b.post(paramRunnable);
      }
    }
  }
  
  protected Message obtainMessage(int paramInt, Object paramObject)
  {
    return Message.obtain(this.b, paramInt, paramObject);
  }
  
  public void sendResponseMessage(HttpResponse paramHttpResponse)
  {
    if (!Thread.currentThread().isInterrupted())
    {
      int i = paramHttpResponse.getResponseCode();
      byte[] arrayOfByte = getResponseData(paramHttpResponse.getEntity());
      if (!Thread.currentThread().isInterrupted()) {
        if (i >= 300) {
          sendFailureMessage(i, paramHttpResponse.getAllHeaders(), arrayOfByte, new HttpResponseException(i, paramHttpResponse.getResponseMessage()));
        } else {
          sendSuccessMessage(i, paramHttpResponse.getAllHeaders(), arrayOfByte);
        }
      }
    }
  }
  
  byte[] getResponseData(HttpEntity paramHttpEntity)
  {
    byte[] arrayOfByte1 = null;
    if (paramHttpEntity != null)
    {
      InputStream localInputStream = paramHttpEntity.getContent();
      if (localInputStream != null)
      {
        long l1 = paramHttpEntity.getContentLength();
        if (l1 > 2147483647L) {
          throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
        }
        try
        {
          ByteArrayOutputStream localByteArrayOutputStream = null;
          try
          {
            localByteArrayOutputStream = new ByteArrayOutputStream();
            byte[] arrayOfByte2 = new byte['က'];
            long l2 = 0L;
            int i;
            while (((i = localInputStream.read(arrayOfByte2)) != -1) && (!Thread.currentThread().isInterrupted()))
            {
              l2 += i;
              localByteArrayOutputStream.write(arrayOfByte2, 0, i);
              sendProgressMessage(l2, l1 <= 0L ? 1L : l1);
            }
            arrayOfByte1 = localByteArrayOutputStream.toByteArray();
          }
          finally
          {
            FileHelper.safeClose(localByteArrayOutputStream);
            FileHelper.safeClose(localInputStream);
            ClearHttpClient.endEntityViaReflection(paramHttpEntity);
          }
        }
        catch (OutOfMemoryError localOutOfMemoryError)
        {
          System.gc();
          throw new IOException("File too large to fit into available memory");
        }
      }
    }
    return arrayOfByte1;
  }
  
  private static class ResponderHandler
    extends Handler
  {
    private final ClearHttpResponseHandler a;
    
    ResponderHandler(ClearHttpResponseHandler paramClearHttpResponseHandler, Looper paramLooper)
    {
      super();
      this.a = paramClearHttpResponseHandler;
    }
    
    public void handleMessage(Message paramMessage)
    {
      this.a.handleMessage(paramMessage);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\ClearHttpResponseHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */