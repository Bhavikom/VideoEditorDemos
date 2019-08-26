package org.lasque.tusdk.core.network;

import android.os.Handler;
import android.os.Looper;
import java.util.Calendar;
import java.util.List;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.http.HttpHeader;
import org.lasque.tusdk.core.http.TextHttpResponseHandler;
import org.lasque.tusdk.core.utils.DateHelper;
import org.lasque.tusdk.core.utils.TuSdkError;
import org.lasque.tusdk.core.utils.json.JsonHelper;
import org.lasque.tusdk.core.utils.json.JsonWrapper;

public abstract class TuSdkHttpHandler
  extends TextHttpResponseHandler
{
  private TuSdkError a;
  private JsonWrapper b;
  private HttpHandlerProgressListener c;
  private Calendar d;
  private Handler e;
  
  public HttpHandlerProgressListener getProgressListener()
  {
    return this.c;
  }
  
  public void setProgressListener(HttpHandlerProgressListener paramHttpHandlerProgressListener)
  {
    this.c = paramHttpHandlerProgressListener;
  }
  
  public TuSdkError getError()
  {
    return this.a;
  }
  
  public void setError(TuSdkError paramTuSdkError)
  {
    this.a = paramTuSdkError;
  }
  
  public JsonWrapper getJson()
  {
    return this.b;
  }
  
  public Calendar getLastRequestTime()
  {
    return this.d;
  }
  
  public Handler getMainLooperHandler()
  {
    if (this.e == null) {
      this.e = new Handler(Looper.getMainLooper());
    }
    return this.e;
  }
  
  public void postInMainThread(Runnable paramRunnable)
  {
    if (paramRunnable == null) {
      return;
    }
    getMainLooperHandler().post(paramRunnable);
  }
  
  public void onSuccess(int paramInt, List<HttpHeader> paramList, String paramString)
  {
    this.a = null;
    JSONObject localJSONObject = JsonHelper.json(paramString);
    boolean bool = hasError(localJSONObject);
    a(bool);
  }
  
  public void onFailure(int paramInt, List<HttpHeader> paramList, String paramString, Throwable paramThrowable)
  {
    if (!TuSdkContext.isNetworkAvailable()) {
      this.a = new TuSdkError(TuSdkContext.getString("lsq_network_connection_interruption"), paramInt);
    } else if (paramThrowable != null)
    {
      if ((paramThrowable instanceof ConnectTimeoutException)) {
        this.a = new TuSdkError(TuSdkContext.getString("lsq_network_connection_timeout"), paramInt);
      } else {
        this.a = new TuSdkError(paramThrowable.getMessage(), paramInt);
      }
    }
    else {
      this.a = new TuSdkError(TuSdkContext.getString("lsq_network_request_error"), paramInt);
    }
    a(true);
  }
  
  protected boolean hasError(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null)
    {
      this.a = new TuSdkError(TuSdkContext.getString("lsq_network_request_return_error"), 404);
      return true;
    }
    this.b = new JsonWrapper(JsonHelper.getJSONObject(paramJSONObject, "data"));
    long l = paramJSONObject.optLong("ttp", -1L);
    if (l != -1L) {
      this.d = DateHelper.parseCalendar(l);
    }
    int i = paramJSONObject.optInt("ret", -1);
    if (i < 0)
    {
      if ((i == 65330) || (i == 65329)) {
        this.a = new TuSdkError("service error", i);
      } else {
        this.a = new TuSdkError(TuSdkContext.getString("lsq_network_request_error"), i);
      }
      return true;
    }
    return false;
  }
  
  private void a(boolean paramBoolean)
  {
    onRequestedPrepare(this);
    if (paramBoolean) {
      onRequestedFailed(this);
    } else {
      onRequestedSucceed(this);
    }
    onRequestedFinish(this);
  }
  
  protected void onRequestedPrepare(TuSdkHttpHandler paramTuSdkHttpHandler) {}
  
  protected abstract void onRequestedSucceed(TuSdkHttpHandler paramTuSdkHttpHandler);
  
  protected void onRequestedFailed(TuSdkHttpHandler paramTuSdkHttpHandler) {}
  
  protected void onRequestedFinish(TuSdkHttpHandler paramTuSdkHttpHandler) {}
  
  public void onProgress(long paramLong1, long paramLong2)
  {
    if ((this.c != null) && (paramLong2 != -1L)) {
      this.c.onHttpHandlerProgress(paramLong1, paramLong2);
    }
  }
  
  public void destory()
  {
    this.c = null;
  }
  
  public static abstract interface HttpHandlerProgressListener
  {
    public abstract void onHttpHandlerProgress(long paramLong1, long paramLong2);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\TuSdkHttpHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */