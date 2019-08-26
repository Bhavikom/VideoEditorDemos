package org.lasque.tusdk.core.http;

import java.io.UnsupportedEncodingException;
import java.util.List;
import org.lasque.tusdk.core.utils.TLog;

public abstract class TextHttpResponseHandler
  extends ClearHttpResponseHandler
{
  public TextHttpResponseHandler()
  {
    this("UTF-8");
  }
  
  public TextHttpResponseHandler(String paramString)
  {
    setCharset(paramString);
  }
  
  public void onProgress(long paramLong1, long paramLong2) {}
  
  public static String getResponseString(byte[] paramArrayOfByte, String paramString)
  {
    try
    {
      String str = paramArrayOfByte == null ? null : new String(paramArrayOfByte, paramString);
      if ((str != null) && (str.startsWith("﻿"))) {
        return str.substring(1);
      }
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      TLog.e("Encoding response into string failed: %s", new Object[] { localUnsupportedEncodingException });
    }
    return null;
  }
  
  public abstract void onFailure(int paramInt, List<HttpHeader> paramList, String paramString, Throwable paramThrowable);
  
  public abstract void onSuccess(int paramInt, List<HttpHeader> paramList, String paramString);
  
  public void onSuccess(int paramInt, List<HttpHeader> paramList, byte[] paramArrayOfByte)
  {
    onSuccess(paramInt, paramList, getResponseString(paramArrayOfByte, getCharset()));
  }
  
  public void onFailure(int paramInt, List<HttpHeader> paramList, byte[] paramArrayOfByte, Throwable paramThrowable)
  {
    onFailure(paramInt, paramList, getResponseString(paramArrayOfByte, getCharset()), paramThrowable);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\TextHttpResponseHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */