package org.lasque.tusdk.core.network;

import android.os.Handler;
import android.os.Looper;
import java.io.File;
import java.util.List;
import org.lasque.tusdk.core.http.FileHttpResponseHandler;
import org.lasque.tusdk.core.http.HttpHeader;
import org.lasque.tusdk.core.http.RequestHandle;
import org.lasque.tusdk.core.type.DownloadTaskStatus;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

public class TuSdkDownloadTask
{
  public static final long PROGRESS_INTERVAL = 500L;
  private boolean a;
  private boolean b;
  private long c;
  private TuSdkDownloadItem d;
  private TuSdkDownloadTaskDelegate e;
  private RequestHandle f;
  
  public TuSdkDownloadItem getItem()
  {
    return this.d;
  }
  
  public TuSdkDownloadTaskDelegate getDelegate()
  {
    return this.e;
  }
  
  public void setDelegate(TuSdkDownloadTaskDelegate paramTuSdkDownloadTaskDelegate)
  {
    this.e = paramTuSdkDownloadTaskDelegate;
  }
  
  public TuSdkDownloadTask(TuSdkDownloadItem paramTuSdkDownloadItem)
  {
    this.d = paramTuSdkDownloadItem;
  }
  
  public boolean equals(DownloadTaskType paramDownloadTaskType, long paramLong)
  {
    return (this.d != null) && (this.d.type == paramDownloadTaskType) && (this.d.id == paramLong);
  }
  
  public boolean canRunTask()
  {
    if (this.d == null) {
      return false;
    }
    switch (3.a[this.d.getStatus().ordinal()])
    {
    case 1: 
    case 2: 
    case 3: 
      return false;
    }
    return true;
  }
  
  private void a(DownloadTaskStatus paramDownloadTaskStatus)
  {
    this.d.setStatus(paramDownloadTaskStatus);
    if (!c(paramDownloadTaskStatus)) {
      return;
    }
    b(paramDownloadTaskStatus);
    if (!canRunTask()) {
      onDestory();
    }
  }
  
  private void b(final DownloadTaskStatus paramDownloadTaskStatus)
  {
    if (this.e == null) {
      return;
    }
    if (ThreadHelper.isMainThread())
    {
      this.e.onDownloadTaskStatusChanged(this, paramDownloadTaskStatus);
      return;
    }
    new Handler(Looper.getMainLooper()).post(new Runnable()
    {
      public void run()
      {
        TuSdkDownloadTask.a(TuSdkDownloadTask.this, paramDownloadTaskStatus);
      }
    });
  }
  
  private boolean c(DownloadTaskStatus paramDownloadTaskStatus)
  {
    if (paramDownloadTaskStatus != DownloadTaskStatus.StatusDowning) {
      return true;
    }
    long l = System.currentTimeMillis();
    if (l - this.c < 500L) {
      return false;
    }
    this.c = l;
    return true;
  }
  
  public void onDestory()
  {
    this.e = null;
    clear();
  }
  
  public void clear()
  {
    if (this.d == null) {
      return;
    }
    FileHelper.delete(this.d.localTempPath());
  }
  
  public void start()
  {
    if (this.a) {
      return;
    }
    this.b = false;
    this.a = true;
    a();
  }
  
  private void a()
  {
    DownloadTaskStatus localDownloadTaskStatus = DownloadTaskStatus.StatusInit;
    if (this.d == null) {
      localDownloadTaskStatus = DownloadTaskStatus.StatusDownFailed;
    } else if (this.d.getStatus() != null) {
      localDownloadTaskStatus = this.d.getStatus();
    }
    switch (3.a[localDownloadTaskStatus.ordinal()])
    {
    case 4: 
    case 5: 
      b();
      break;
    case 1: 
    case 2: 
    case 3: 
      this.a = false;
      clear();
      break;
    }
  }
  
  private void b()
  {
    clear();
    a(DownloadTaskStatus.StatusDowning);
    String str = String.format("/%s/download?id=%s", new Object[] { this.d.type.getAct(), this.d.fileId });
    this.f = TuSdkHttpEngine.webAPIEngine().get(str, true, new DownloadFileHandler(this.d.localTempPath()));
  }
  
  private void c()
  {
    if (this.b) {
      return;
    }
    FileHelper.rename(this.d.localTempPath(), this.d.localDownloadPath());
    new Handler(Looper.getMainLooper()).post(new Runnable()
    {
      public void run()
      {
        TuSdkDownloadTask.b(TuSdkDownloadTask.this, DownloadTaskStatus.StatusDowned);
      }
    });
  }
  
  public void cancel()
  {
    this.b = true;
    if (this.f != null)
    {
      this.f.cancel(true);
      this.f = null;
    }
    a(DownloadTaskStatus.StatusCancel);
  }
  
  private class DownloadFileHandler
    extends FileHttpResponseHandler
  {
    public DownloadFileHandler(File paramFile)
    {
      super();
    }
    
    public void onFailure(int paramInt, List<HttpHeader> paramList, Throwable paramThrowable, File paramFile)
    {
      TLog.e(paramThrowable, "TuSdkDownloadTask onFailure: %s(%s) |%s", new Object[] { TuSdkDownloadTask.a(TuSdkDownloadTask.this).type.getAct(), Long.valueOf(TuSdkDownloadTask.a(TuSdkDownloadTask.this).id), paramFile });
      TuSdkDownloadTask.b(TuSdkDownloadTask.this, DownloadTaskStatus.StatusDownFailed);
    }
    
    public void onSuccess(int paramInt, List<HttpHeader> paramList, File paramFile)
    {
      TuSdkDownloadTask.a(TuSdkDownloadTask.this).fileName = TuSdkHttp.attachmentFileName(paramList);
      new Thread(new Runnable()
      {
        public void run()
        {
          TuSdkDownloadTask.b(TuSdkDownloadTask.this);
        }
      }).start();
    }
    
    public void onProgress(long paramLong1, long paramLong2)
    {
      TuSdkDownloadTask.a(TuSdkDownloadTask.this).progress = ((float)paramLong1 / (float)paramLong2);
      TuSdkDownloadTask.b(TuSdkDownloadTask.this, DownloadTaskStatus.StatusDowning);
    }
  }
  
  public static enum DownloadTaskType
  {
    private String a;
    
    private DownloadTaskType(String paramString)
    {
      this.a = paramString;
    }
    
    public String getAct()
    {
      return this.a;
    }
  }
  
  public static abstract interface TuSdkDownloadTaskDelegate
  {
    public abstract void onDownloadTaskStatusChanged(TuSdkDownloadTask paramTuSdkDownloadTask, DownloadTaskStatus paramDownloadTaskStatus);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\TuSdkDownloadTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */