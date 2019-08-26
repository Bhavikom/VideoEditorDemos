package org.lasque.tusdk.core.http;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.lasque.tusdk.core.utils.TLog;

public class JsonHttpResponseHandler
  extends TextHttpResponseHandler
{
  private boolean a = true;
  
  public JsonHttpResponseHandler()
  {
    super("UTF-8");
  }
  
  public JsonHttpResponseHandler(String paramString)
  {
    super(paramString);
  }
  
  public JsonHttpResponseHandler(boolean paramBoolean)
  {
    super("UTF-8");
    this.a = paramBoolean;
  }
  
  public JsonHttpResponseHandler(String paramString, boolean paramBoolean)
  {
    super(paramString);
    this.a = paramBoolean;
  }
  
  public void onSuccess(int paramInt, List<HttpHeader> paramList, JSONObject paramJSONObject)
  {
    TLog.w("onSuccess(int, List<HttpHeader>, JSONObject) was not overriden, but callback was received", new Object[0]);
  }
  
  public void onSuccess(int paramInt, List<HttpHeader> paramList, JSONArray paramJSONArray)
  {
    TLog.w("onSuccess(int, List<HttpHeader>, JSONArray) was not overriden, but callback was received", new Object[0]);
  }
  
  public void onFailure(int paramInt, List<HttpHeader> paramList, Throwable paramThrowable, JSONObject paramJSONObject)
  {
    TLog.w("onFailure(int, List<HttpHeader>, Throwable, JSONObject) was not overriden, but callback was received: %s", new Object[] { paramThrowable });
  }
  
  public void onFailure(int paramInt, List<HttpHeader> paramList, Throwable paramThrowable, JSONArray paramJSONArray)
  {
    TLog.w("onFailure(int, List<HttpHeader>, Throwable, JSONArray) was not overriden, but callback was received: %s", new Object[] { paramThrowable });
  }
  
  public void onFailure(int paramInt, List<HttpHeader> paramList, String paramString, Throwable paramThrowable)
  {
    TLog.w("onFailure(int, List<HttpHeader>, String, Throwable) was not overriden, but callback was received: %s", new Object[] { paramThrowable });
  }
  
  public void onSuccess(int paramInt, List<HttpHeader> paramList, String paramString)
  {
    TLog.w("onSuccess(int, List<HttpHeader>, String) was not overriden, but callback was received", new Object[0]);
  }
  
  public final void onSuccess(final int paramInt, final List<HttpHeader> paramList, final byte[] paramArrayOfByte)
  {
    if (paramInt != 204)
    {
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          try
          {
            final Object localObject = JsonHttpResponseHandler.this.parseResponse(paramArrayOfByte);
            JsonHttpResponseHandler.this.postRunnable(new Runnable()
            {
              public void run()
              {
                if ((!JsonHttpResponseHandler.a(JsonHttpResponseHandler.this)) && (localObject == null)) {
                  JsonHttpResponseHandler.this.onSuccess(JsonHttpResponseHandler.1.this.b, JsonHttpResponseHandler.1.this.c, (String)null);
                } else if ((localObject instanceof JSONObject)) {
                  JsonHttpResponseHandler.this.onSuccess(JsonHttpResponseHandler.1.this.b, JsonHttpResponseHandler.1.this.c, (JSONObject)localObject);
                } else if ((localObject instanceof JSONArray)) {
                  JsonHttpResponseHandler.this.onSuccess(JsonHttpResponseHandler.1.this.b, JsonHttpResponseHandler.1.this.c, (JSONArray)localObject);
                } else if ((localObject instanceof String))
                {
                  if (JsonHttpResponseHandler.a(JsonHttpResponseHandler.this)) {
                    JsonHttpResponseHandler.this.onFailure(JsonHttpResponseHandler.1.this.b, JsonHttpResponseHandler.1.this.c, (String)localObject, new JSONException("Response cannot be parsed as JSON data"));
                  } else {
                    JsonHttpResponseHandler.this.onSuccess(JsonHttpResponseHandler.1.this.b, JsonHttpResponseHandler.1.this.c, (String)localObject);
                  }
                }
                else {
                  JsonHttpResponseHandler.this.onFailure(JsonHttpResponseHandler.1.this.b, JsonHttpResponseHandler.1.this.c, new JSONException("Unexpected response type " + localObject.getClass().getName()), (JSONObject)null);
                }
              }
            });
          }
          catch (JSONException localJSONException)
          {
            JsonHttpResponseHandler.this.postRunnable(new Runnable()
            {
              public void run()
              {
                JsonHttpResponseHandler.this.onFailure(JsonHttpResponseHandler.1.this.b, JsonHttpResponseHandler.1.this.c, localJSONException, (JSONObject)null);
              }
            });
          }
        }
      };
      if ((!getUseSynchronousMode()) && (!getUsePoolThread())) {
        new Thread(local1).start();
      } else {
        local1.run();
      }
    }
    else
    {
      onSuccess(paramInt, paramList, new JSONObject());
    }
  }
  
  public final void onFailure(final int paramInt, final List<HttpHeader> paramList, final byte[] paramArrayOfByte, final Throwable paramThrowable)
  {
    if (paramArrayOfByte != null)
    {
      Runnable local2 = new Runnable()
      {
        public void run()
        {
          try
          {
            final Object localObject = JsonHttpResponseHandler.this.parseResponse(paramArrayOfByte);
            JsonHttpResponseHandler.this.postRunnable(new Runnable()
            {
              public void run()
              {
                if ((!JsonHttpResponseHandler.a(JsonHttpResponseHandler.this)) && (localObject == null)) {
                  JsonHttpResponseHandler.this.onFailure(JsonHttpResponseHandler.2.this.b, JsonHttpResponseHandler.2.this.c, (String)null, JsonHttpResponseHandler.2.this.d);
                } else if ((localObject instanceof JSONObject)) {
                  JsonHttpResponseHandler.this.onFailure(JsonHttpResponseHandler.2.this.b, JsonHttpResponseHandler.2.this.c, JsonHttpResponseHandler.2.this.d, (JSONObject)localObject);
                } else if ((localObject instanceof JSONArray)) {
                  JsonHttpResponseHandler.this.onFailure(JsonHttpResponseHandler.2.this.b, JsonHttpResponseHandler.2.this.c, JsonHttpResponseHandler.2.this.d, (JSONArray)localObject);
                } else if ((localObject instanceof String)) {
                  JsonHttpResponseHandler.this.onFailure(JsonHttpResponseHandler.2.this.b, JsonHttpResponseHandler.2.this.c, (String)localObject, JsonHttpResponseHandler.2.this.d);
                } else {
                  JsonHttpResponseHandler.this.onFailure(JsonHttpResponseHandler.2.this.b, JsonHttpResponseHandler.2.this.c, new JSONException("Unexpected response type " + localObject.getClass().getName()), (JSONObject)null);
                }
              }
            });
          }
          catch (JSONException localJSONException)
          {
            JsonHttpResponseHandler.this.postRunnable(new Runnable()
            {
              public void run()
              {
                JsonHttpResponseHandler.this.onFailure(JsonHttpResponseHandler.2.this.b, JsonHttpResponseHandler.2.this.c, localJSONException, (JSONObject)null);
              }
            });
          }
        }
      };
      if ((!getUseSynchronousMode()) && (!getUsePoolThread())) {
        new Thread(local2).start();
      } else {
        local2.run();
      }
    }
    else
    {
      TLog.w("response body is null, calling onFailure(Throwable, JSONObject)", new Object[0]);
      onFailure(paramInt, paramList, paramThrowable, (JSONObject)null);
    }
  }
  
  protected Object parseResponse(byte[] paramArrayOfByte)
  {
    if (null == paramArrayOfByte) {
      return null;
    }
    Object localObject = null;
    String str = getResponseString(paramArrayOfByte, getCharset());
    if (str != null)
    {
      str = str.trim();
      if (this.a)
      {
        if ((str.startsWith("{")) || (str.startsWith("["))) {
          localObject = new JSONTokener(str).nextValue();
        }
      }
      else if (((str.startsWith("{")) && (str.endsWith("}"))) || ((str.startsWith("[")) && (str.endsWith("]")))) {
        localObject = new JSONTokener(str).nextValue();
      } else if ((str.startsWith("\"")) && (str.endsWith("\""))) {
        localObject = str.substring(1, str.length() - 1);
      }
    }
    if (localObject == null) {
      localObject = str;
    }
    return localObject;
  }
  
  public boolean isUseRFC5179CompatibilityMode()
  {
    return this.a;
  }
  
  public void setUseRFC5179CompatibilityMode(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\JsonHttpResponseHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */