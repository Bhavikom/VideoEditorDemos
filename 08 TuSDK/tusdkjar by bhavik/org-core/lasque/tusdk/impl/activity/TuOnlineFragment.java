package org.lasque.tusdk.impl.activity;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import org.json.JSONObject;
import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.lasque.tusdk.core.network.TuSdkHttpEngine;
import org.lasque.tusdk.core.type.OnlineCommandAction;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.view.TuSdkWebView;
import org.lasque.tusdk.core.view.TuSdkWebView.TuSdkWebViewAdapter;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;

public abstract class TuOnlineFragment
  extends TuFragment
{
  private long a;
  private String b;
  private String c;
  private boolean d;
  private TuSdkWebView.TuSdkWebViewAdapter e = new TuSdkWebView.TuSdkWebViewAdapter()
  {
    public void onPageStarted(TuSdkWebView paramAnonymousTuSdkWebView, String paramAnonymousString, Bitmap paramAnonymousBitmap)
    {
      TuOnlineFragment.a(TuOnlineFragment.this, false);
    }
    
    public void onPageFinished(TuSdkWebView paramAnonymousTuSdkWebView, String paramAnonymousString)
    {
      TuOnlineFragment.a(TuOnlineFragment.this, true);
      paramAnonymousTuSdkWebView.setWebPageUrl("javascript:clientBridge.getHandlers().onTuSdkSend(" + TuOnlineFragment.this.getPageFinishedData() + ");");
    }
    
    public void onReceivedTitle(TuSdkWebView paramAnonymousTuSdkWebView, String paramAnonymousString)
    {
      if (paramAnonymousString == null) {
        return;
      }
      TuOnlineFragment.this.setTitle(paramAnonymousString);
    }
  };
  
  public abstract TuSdkWebView getWebview();
  
  public long getDetailDataId()
  {
    return this.a;
  }
  
  public void setDetailDataId(long paramLong)
  {
    this.a = paramLong;
  }
  
  public String getArgs()
  {
    return this.c;
  }
  
  public void setArgs(String paramString)
  {
    this.c = paramString;
  }
  
  public String getOnlineType()
  {
    return this.b;
  }
  
  public void setOnlineType(String paramString)
  {
    this.b = paramString;
  }
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    a(getWebview());
  }
  
  protected void navigatorBarLoaded(TuSdkNavigatorBar paramTuSdkNavigatorBar)
  {
    super.navigatorBarLoaded(paramTuSdkNavigatorBar);
    setIsSupportSlideBack(false);
  }
  
  public void navigatorBarRightAction(TuSdkNavigatorBar.NavigatorBarButtonInterface paramNavigatorBarButtonInterface)
  {
    dismissActivityWithAnim();
  }
  
  private void a(TuSdkWebView paramTuSdkWebView)
  {
    if (paramTuSdkWebView == null) {
      return;
    }
    paramTuSdkWebView.setAdapter(this.e);
    String str = null;
    if (getDetailDataId() > 0L) {
      str = String.format("/%s/item?id=%s", new Object[] { getOnlineType(), Long.valueOf(getDetailDataId()) });
    } else if (!StringHelper.isBlank(getArgs())) {
      str = String.format("/%s/index?%s", new Object[] { getOnlineType(), getArgs() });
    } else {
      str = String.format("/%s/index", new Object[] { getOnlineType() });
    }
    paramTuSdkWebView.setWebPageUrl(TuSdkHttpEngine.shared().getWebUrl(str, true));
    paramTuSdkWebView.addJavascriptInterface(new TuSdkOnlineInteface(null), "tusdkBridge");
  }
  
  protected abstract String getPageFinishedData();
  
  private void a(String paramString)
  {
    if (StringHelper.isEmpty(paramString)) {
      return;
    }
    String[] arrayOfString = paramString.split("/");
    if ((arrayOfString.length < 2) || (!getOnlineType().equalsIgnoreCase(arrayOfString[0]))) {
      return;
    }
    OnlineCommandAction localOnlineCommandAction = OnlineCommandAction.getType(Integer.parseInt(arrayOfString[1]));
    switch (2.a[localOnlineCommandAction.ordinal()])
    {
    case 1: 
      handleDownload(arrayOfString);
      break;
    case 2: 
      handleCancel(arrayOfString);
      break;
    case 3: 
      handleSelected(arrayOfString);
      break;
    case 4: 
      handleDetail(arrayOfString);
      break;
    }
  }
  
  protected void handleDownload(String[] paramArrayOfString)
  {
    if (paramArrayOfString.length < 5) {
      return;
    }
    long l = Long.parseLong(paramArrayOfString[2]);
    String str1 = paramArrayOfString[3];
    String str2 = paramArrayOfString[4];
    onResourceDownload(l, str1, str2);
  }
  
  protected abstract void onResourceDownload(long paramLong, String paramString1, String paramString2);
  
  protected void handleCancel(String[] paramArrayOfString)
  {
    if (paramArrayOfString.length < 3) {
      return;
    }
    long l = Long.parseLong(paramArrayOfString[2]);
    onResourceCancelDownload(l);
  }
  
  protected abstract void onResourceCancelDownload(long paramLong);
  
  protected void handleSelected(String[] paramArrayOfString) {}
  
  protected void handleDetail(String[] paramArrayOfString) {}
  
  protected void notifyOnlineData(TuSdkDownloadItem paramTuSdkDownloadItem)
  {
    if ((paramTuSdkDownloadItem == null) || (!this.d)) {
      return;
    }
    getWebview().setWebPageUrl("javascript:clientBridge.getHandlers().onTuSdkSend(" + paramTuSdkDownloadItem.getStatusChangeData().toString() + ");");
  }
  
  private class TuSdkOnlineInteface
  {
    private TuSdkOnlineInteface() {}
    
    @JavascriptInterface
    public void onTuSdkPush(String paramString)
    {
      TuOnlineFragment.a(TuOnlineFragment.this, paramString);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\activity\TuOnlineFragment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */