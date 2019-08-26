package org.lasque.tusdk.core.http;

import android.os.Looper;
import java.lang.ref.WeakReference;

public class RequestHandle
{
  private final WeakReference<ClearHttpRequest> a;
  
  public RequestHandle(ClearHttpRequest paramClearHttpRequest)
  {
    this.a = new WeakReference(paramClearHttpRequest);
  }
  
  public boolean cancel(final boolean paramBoolean)
  {
    final ClearHttpRequest localClearHttpRequest = (ClearHttpRequest)this.a.get();
    if (localClearHttpRequest != null)
    {
      if (Looper.myLooper() == Looper.getMainLooper())
      {
        new Thread(new Runnable()
        {
          public void run()
          {
            localClearHttpRequest.cancel(paramBoolean);
          }
        }).start();
        return true;
      }
      return localClearHttpRequest.cancel(paramBoolean);
    }
    return false;
  }
  
  public boolean isFinished()
  {
    ClearHttpRequest localClearHttpRequest = (ClearHttpRequest)this.a.get();
    return (localClearHttpRequest == null) || (localClearHttpRequest.isDone());
  }
  
  public boolean isCancelled()
  {
    ClearHttpRequest localClearHttpRequest = (ClearHttpRequest)this.a.get();
    return (localClearHttpRequest == null) || (localClearHttpRequest.isCancelled());
  }
  
  public boolean shouldBeGarbageCollected()
  {
    boolean bool = (isCancelled()) || (isFinished());
    if (bool) {
      this.a.clear();
    }
    return bool;
  }
  
  public Object getTag()
  {
    ClearHttpRequest localClearHttpRequest = (ClearHttpRequest)this.a.get();
    return localClearHttpRequest == null ? null : localClearHttpRequest.getTag();
  }
  
  public RequestHandle setTag(Object paramObject)
  {
    ClearHttpRequest localClearHttpRequest = (ClearHttpRequest)this.a.get();
    if (localClearHttpRequest != null) {
      localClearHttpRequest.setRequestTag(paramObject);
    }
    return this;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\RequestHandle.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */