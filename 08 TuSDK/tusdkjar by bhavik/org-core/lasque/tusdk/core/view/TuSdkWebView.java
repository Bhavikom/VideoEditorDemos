package org.lasque.tusdk.core.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.anim.AnimHelper.TuSdkViewAnimatorAdapter;

public class TuSdkWebView
  extends WebView
  implements TuSdkViewInterface
{
  private ProgressBar a;
  private TuSdkWebViewAdapter b;
  private boolean c;
  private AnimHelper.TuSdkViewAnimatorAdapter d = new AnimHelper.TuSdkViewAnimatorAdapter()
  {
    public void onAnimationEnd(View paramAnonymousView, boolean paramAnonymousBoolean)
    {
      if (paramAnonymousBoolean) {
        return;
      }
      if (!TuSdkWebView.a(TuSdkWebView.this)) {
        paramAnonymousView.setVisibility(8);
      }
    }
  };
  
  public TuSdkWebView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  public TuSdkWebView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }
  
  public TuSdkWebView(Context paramContext)
  {
    super(paramContext);
    initView();
  }
  
  public ProgressBar getProgressBar()
  {
    return this.a;
  }
  
  public void setProgressBar(ProgressBar paramProgressBar)
  {
    this.a = paramProgressBar;
    if (this.a != null) {
      this.a.setMax(100);
    }
  }
  
  public TuSdkWebViewAdapter getAdapter()
  {
    return this.b;
  }
  
  public void setAdapter(TuSdkWebViewAdapter paramTuSdkWebViewAdapter)
  {
    this.b = paramTuSdkWebViewAdapter;
  }
  
  protected void initView()
  {
    setWebViewClient(new TuSdkWebViewClient());
    setWebChromeClient(new TuSdkWebChromeClient());
  }
  
  @SuppressLint({"SetJavaScriptEnabled"})
  public void loadView()
  {
    setJavaScriptEnabled(true);
    setSupportZoom(false);
    setLoadsImagesAutomatically(true);
    setLoadWithOverviewMode(true);
  }
  
  public void viewDidLoad() {}
  
  public void viewNeedRest() {}
  
  public void viewWillDestory() {}
  
  private void a(boolean paramBoolean)
  {
    if (getProgressBar() == null) {
      return;
    }
    this.c = paramBoolean;
    float f = paramBoolean ? 1.0F : 0.0F;
    getProgressBar().setVisibility(0);
    ViewCompat.animate(getProgressBar()).alpha(f).setDuration(240L).setListener(this.d);
  }
  
  public void setWebPageUrl(String paramString)
  {
    if (paramString != null) {
      loadUrl(paramString);
    }
  }
  
  @SuppressLint({"SetJavaScriptEnabled"})
  public void setJavaScriptEnabled(boolean paramBoolean)
  {
    getSettings().setJavaScriptEnabled(paramBoolean);
  }
  
  public synchronized void setJavaScriptCanOpenWindowsAutomatically(boolean paramBoolean)
  {
    getSettings().setJavaScriptCanOpenWindowsAutomatically(paramBoolean);
  }
  
  public void setDefaultZoom(WebSettings.ZoomDensity paramZoomDensity)
  {
    getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
  }
  
  public void setDefaultTextEncodingName(String paramString)
  {
    getSettings().setDefaultTextEncodingName(paramString);
  }
  
  public void setLoadsImagesAutomatically(boolean paramBoolean)
  {
    getSettings().setLoadsImagesAutomatically(paramBoolean);
  }
  
  public void setSupportZoom(boolean paramBoolean)
  {
    getSettings().setSupportZoom(paramBoolean);
  }
  
  public void setLoadWithOverviewMode(boolean paramBoolean)
  {
    getSettings().setUseWideViewPort(paramBoolean);
    getSettings().setLoadWithOverviewMode(paramBoolean);
  }
  
  public void disableCache()
  {
    getSettings().setCacheMode(2);
  }
  
  @TargetApi(11)
  public void disableHardwareAccelerated()
  {
    setLayerType(1, null);
  }
  
  public void setSavePassword(boolean paramBoolean)
  {
    if (Build.VERSION.SDK_INT <= 18) {
      getSettings().setSavePassword(paramBoolean);
    }
  }
  
  public static class TuSdkWebViewAdapter
  {
    public boolean shouldOverrideUrlLoading(TuSdkWebView paramTuSdkWebView, String paramString)
    {
      return false;
    }
    
    public void onReceivedError(TuSdkWebView paramTuSdkWebView, int paramInt, String paramString1, String paramString2) {}
    
    public void onPageStarted(TuSdkWebView paramTuSdkWebView, String paramString, Bitmap paramBitmap) {}
    
    public void onPageFinished(TuSdkWebView paramTuSdkWebView, String paramString) {}
    
    public void onReceivedTitle(TuSdkWebView paramTuSdkWebView, String paramString) {}
    
    public void onProgressChanged(TuSdkWebView paramTuSdkWebView, int paramInt) {}
    
    public boolean onJsAlert(TuSdkWebView paramTuSdkWebView, String paramString1, String paramString2, JsResult paramJsResult)
    {
      return true;
    }
    
    public boolean onJsConfirm(TuSdkWebView paramTuSdkWebView, String paramString1, String paramString2, JsResult paramJsResult)
    {
      return true;
    }
    
    public boolean onJsPrompt(TuSdkWebView paramTuSdkWebView, String paramString1, String paramString2, String paramString3, JsPromptResult paramJsPromptResult)
    {
      return true;
    }
  }
  
  public class TuSdkWebChromeClient
    extends WebChromeClient
  {
    public TuSdkWebChromeClient() {}
    
    public void onReceivedTitle(WebView paramWebView, String paramString)
    {
      if (TuSdkWebView.this.getAdapter() != null) {
        TuSdkWebView.this.getAdapter().onReceivedTitle(TuSdkWebView.this, paramString);
      }
    }
    
    public void onProgressChanged(WebView paramWebView, int paramInt)
    {
      if (TuSdkWebView.this.getProgressBar() != null) {
        TuSdkWebView.this.getProgressBar().setProgress(paramInt);
      }
      if (TuSdkWebView.this.getAdapter() != null) {
        TuSdkWebView.this.getAdapter().onProgressChanged(TuSdkWebView.this, paramInt);
      }
    }
    
    public boolean onJsAlert(WebView paramWebView, String paramString1, String paramString2, JsResult paramJsResult)
    {
      TLog.i("TuSdkWebView onJsAlert : %s | %s | %s", new Object[] { paramString1, paramString2, paramJsResult });
      if (TuSdkWebView.this.getAdapter() != null) {
        return TuSdkWebView.this.getAdapter().onJsAlert(TuSdkWebView.this, paramString1, paramString2, paramJsResult);
      }
      return false;
    }
    
    public boolean onJsConfirm(WebView paramWebView, String paramString1, String paramString2, JsResult paramJsResult)
    {
      TLog.i("TuSdkWebView onJsConfirm : %s | %s | %s", new Object[] { paramString1, paramString2, paramJsResult });
      if (TuSdkWebView.this.getAdapter() != null) {
        return TuSdkWebView.this.getAdapter().onJsConfirm(TuSdkWebView.this, paramString1, paramString2, paramJsResult);
      }
      return false;
    }
    
    public boolean onJsPrompt(WebView paramWebView, String paramString1, String paramString2, String paramString3, JsPromptResult paramJsPromptResult)
    {
      TLog.i("TuSdkWebView onJsPrompt : %s | %s | %s", new Object[] { paramString1, paramString2, paramJsPromptResult });
      if (TuSdkWebView.this.getAdapter() != null) {
        return TuSdkWebView.this.getAdapter().onJsPrompt(TuSdkWebView.this, paramString1, paramString2, paramString3, paramJsPromptResult);
      }
      return false;
    }
  }
  
  public class TuSdkWebViewClient
    extends WebViewClient
  {
    public TuSdkWebViewClient() {}
    
    public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
    {
      if (TuSdkWebView.this.getAdapter() != null) {
        return TuSdkWebView.this.getAdapter().shouldOverrideUrlLoading(TuSdkWebView.this, paramString);
      }
      return false;
    }
    
    public void onReceivedError(WebView paramWebView, int paramInt, String paramString1, String paramString2)
    {
      TLog.e("TuSdkWebView onReceivedError: %s | url: %s", new Object[] { paramString1, paramString2 });
      TuSdkWebView.a(TuSdkWebView.this, false);
      if (TuSdkWebView.this.getAdapter() != null) {
        TuSdkWebView.this.getAdapter().onReceivedError(TuSdkWebView.this, paramInt, paramString1, paramString2);
      }
    }
    
    public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap)
    {
      TuSdkWebView.a(TuSdkWebView.this, true);
      if (TuSdkWebView.this.getAdapter() != null) {
        TuSdkWebView.this.getAdapter().onPageStarted(TuSdkWebView.this, paramString, paramBitmap);
      }
    }
    
    public void onPageFinished(WebView paramWebView, String paramString)
    {
      TuSdkWebView.a(TuSdkWebView.this, false);
      if (TuSdkWebView.this.getAdapter() != null) {
        TuSdkWebView.this.getAdapter().onPageFinished(TuSdkWebView.this, paramString);
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\TuSdkWebView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */