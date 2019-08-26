// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view;

//import org.lasque.tusdk.core.utils.TLog;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.graphics.Bitmap;
import android.os.Build;
import android.annotation.TargetApi;
import android.graphics.Paint;
import android.webkit.WebSettings;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewCompat;
import android.annotation.SuppressLint;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.utils.anim.AnimHelper;
import android.widget.ProgressBar;
import android.webkit.WebView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AnimHelper;

public class TuSdkWebView extends WebView implements TuSdkViewInterface
{
    private ProgressBar a;
    private TuSdkWebViewAdapter b;
    private boolean c;
    private AnimHelper.TuSdkViewAnimatorAdapter d;
    
    public TuSdkWebView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.d = new AnimHelper.TuSdkViewAnimatorAdapter() {
            @Override
            public void onAnimationEnd(final View view, final boolean b) {
                if (b) {
                    return;
                }
                if (!TuSdkWebView.this.c) {
                    view.setVisibility(GONE);
                }
            }
        };
        this.initView();
    }
    
    public TuSdkWebView(final Context context, final AttributeSet set) {
        super(context, set);
        this.d = new AnimHelper.TuSdkViewAnimatorAdapter() {
            @Override
            public void onAnimationEnd(final View view, final boolean b) {
                if (b) {
                    return;
                }
                if (!TuSdkWebView.this.c) {
                    view.setVisibility(GONE);
                }
            }
        };
        this.initView();
    }
    
    public TuSdkWebView(final Context context) {
        super(context);
        this.d = new AnimHelper.TuSdkViewAnimatorAdapter() {
            @Override
            public void onAnimationEnd(final View view, final boolean b) {
                if (b) {
                    return;
                }
                if (!TuSdkWebView.this.c) {
                    view.setVisibility(GONE);
                }
            }
        };
        this.initView();
    }
    
    public ProgressBar getProgressBar() {
        return this.a;
    }
    
    public void setProgressBar(final ProgressBar a) {
        this.a = a;
        if (this.a != null) {
            this.a.setMax(100);
        }
    }
    
    public TuSdkWebViewAdapter getAdapter() {
        return this.b;
    }
    
    public void setAdapter(final TuSdkWebViewAdapter b) {
        this.b = b;
    }
    
    protected void initView() {
        this.setWebViewClient((WebViewClient)new TuSdkWebViewClient());
        this.setWebChromeClient((WebChromeClient)new TuSdkWebChromeClient());
    }
    
    @SuppressLint({ "SetJavaScriptEnabled" })
    public void loadView() {
        this.setJavaScriptEnabled(true);
        this.setSupportZoom(false);
        this.setLoadsImagesAutomatically(true);
        this.setLoadWithOverviewMode(true);
    }
    
    public void viewDidLoad() {
    }
    
    public void viewNeedRest() {
    }
    
    public void viewWillDestory() {
    }
    
    private void a(final boolean c) {
        if (this.getProgressBar() == null) {
            return;
        }
        this.c = c;
        final float n = c ? 1.0f : 0.0f;
        this.getProgressBar().setVisibility(VISIBLE);
        ViewCompat.animate((View)this.getProgressBar()).alpha(n).setDuration(240L).setListener((ViewPropertyAnimatorListener)this.d);
    }
    
    public void setWebPageUrl(final String s) {
        if (s != null) {
            this.loadUrl(s);
        }
    }
    
    @SuppressLint({ "SetJavaScriptEnabled" })
    public void setJavaScriptEnabled(final boolean javaScriptEnabled) {
        this.getSettings().setJavaScriptEnabled(javaScriptEnabled);
    }
    
    public synchronized void setJavaScriptCanOpenWindowsAutomatically(final boolean javaScriptCanOpenWindowsAutomatically) {
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(javaScriptCanOpenWindowsAutomatically);
    }
    
    public void setDefaultZoom(final WebSettings.ZoomDensity zoomDensity) {
        this.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
    }
    
    public void setDefaultTextEncodingName(final String defaultTextEncodingName) {
        this.getSettings().setDefaultTextEncodingName(defaultTextEncodingName);
    }
    
    public void setLoadsImagesAutomatically(final boolean loadsImagesAutomatically) {
        this.getSettings().setLoadsImagesAutomatically(loadsImagesAutomatically);
    }
    
    public void setSupportZoom(final boolean supportZoom) {
        this.getSettings().setSupportZoom(supportZoom);
    }
    
    public void setLoadWithOverviewMode(final boolean b) {
        this.getSettings().setUseWideViewPort(b);
        this.getSettings().setLoadWithOverviewMode(b);
    }
    
    @SuppressLint("WrongConstant")
    public void disableCache() {
        this.getSettings().setCacheMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
    }
    
    @TargetApi(11)
    public void disableHardwareAccelerated() {
        this.setLayerType(1, (Paint)null);
    }
    
    public void setSavePassword(final boolean savePassword) {
        if (Build.VERSION.SDK_INT <= 18) {
            this.getSettings().setSavePassword(savePassword);
        }
    }
    
    public static class TuSdkWebViewAdapter
    {
        public boolean shouldOverrideUrlLoading(final TuSdkWebView tuSdkWebView, final String s) {
            return false;
        }
        
        public void onReceivedError(final TuSdkWebView tuSdkWebView, final int n, final String s, final String s2) {
        }
        
        public void onPageStarted(final TuSdkWebView tuSdkWebView, final String s, final Bitmap bitmap) {
        }
        
        public void onPageFinished(final TuSdkWebView tuSdkWebView, final String s) {
        }
        
        public void onReceivedTitle(final TuSdkWebView tuSdkWebView, final String s) {
        }
        
        public void onProgressChanged(final TuSdkWebView tuSdkWebView, final int n) {
        }
        
        public boolean onJsAlert(final TuSdkWebView tuSdkWebView, final String s, final String s2, final JsResult jsResult) {
            return true;
        }
        
        public boolean onJsConfirm(final TuSdkWebView tuSdkWebView, final String s, final String s2, final JsResult jsResult) {
            return true;
        }
        
        public boolean onJsPrompt(final TuSdkWebView tuSdkWebView, final String s, final String s2, final String s3, final JsPromptResult jsPromptResult) {
            return true;
        }
    }
    
    public class TuSdkWebChromeClient extends WebChromeClient
    {
        public void onReceivedTitle(final WebView webView, final String s) {
            if (TuSdkWebView.this.getAdapter() != null) {
                TuSdkWebView.this.getAdapter().onReceivedTitle(TuSdkWebView.this, s);
            }
        }
        
        public void onProgressChanged(final WebView webView, final int progress) {
            if (TuSdkWebView.this.getProgressBar() != null) {
                TuSdkWebView.this.getProgressBar().setProgress(progress);
            }
            if (TuSdkWebView.this.getAdapter() != null) {
                TuSdkWebView.this.getAdapter().onProgressChanged(TuSdkWebView.this, progress);
            }
        }
        
        public boolean onJsAlert(final WebView webView, final String s, final String s2, final JsResult jsResult) {
            TLog.i("TuSdkWebView onJsAlert : %s | %s | %s", s, s2, jsResult);
            return TuSdkWebView.this.getAdapter() != null && TuSdkWebView.this.getAdapter().onJsAlert(TuSdkWebView.this, s, s2, jsResult);
        }
        
        public boolean onJsConfirm(final WebView webView, final String s, final String s2, final JsResult jsResult) {
            TLog.i("TuSdkWebView onJsConfirm : %s | %s | %s", s, s2, jsResult);
            return TuSdkWebView.this.getAdapter() != null && TuSdkWebView.this.getAdapter().onJsConfirm(TuSdkWebView.this, s, s2, jsResult);
        }
        
        public boolean onJsPrompt(final WebView webView, final String s, final String s2, final String s3, final JsPromptResult jsPromptResult) {
            TLog.i("TuSdkWebView onJsPrompt : %s | %s | %s", s, s2, jsPromptResult);
            return TuSdkWebView.this.getAdapter() != null && TuSdkWebView.this.getAdapter().onJsPrompt(TuSdkWebView.this, s, s2, s3, jsPromptResult);
        }
    }
    
    public class TuSdkWebViewClient extends WebViewClient
    {
        public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
            return TuSdkWebView.this.getAdapter() != null && TuSdkWebView.this.getAdapter().shouldOverrideUrlLoading(TuSdkWebView.this, s);
        }
        
        public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
            TLog.e("TuSdkWebView onReceivedError: %s | url: %s", s, s2);
            TuSdkWebView.this.a(false);
            if (TuSdkWebView.this.getAdapter() != null) {
                TuSdkWebView.this.getAdapter().onReceivedError(TuSdkWebView.this, n, s, s2);
            }
        }
        
        public void onPageStarted(final WebView webView, final String s, final Bitmap bitmap) {
            TuSdkWebView.this.a(true);
            if (TuSdkWebView.this.getAdapter() != null) {
                TuSdkWebView.this.getAdapter().onPageStarted(TuSdkWebView.this, s, bitmap);
            }
        }
        
        public void onPageFinished(final WebView webView, final String s) {
            TuSdkWebView.this.a(false);
            if (TuSdkWebView.this.getAdapter() != null) {
                TuSdkWebView.this.getAdapter().onPageFinished(TuSdkWebView.this, s);
            }
        }
    }
}
