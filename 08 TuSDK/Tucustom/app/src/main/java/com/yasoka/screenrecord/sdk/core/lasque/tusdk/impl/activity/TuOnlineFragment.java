// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity;

import android.annotation.SuppressLint;
import android.webkit.JavascriptInterface;
//import org.lasque.tusdk.core.network.TuSdkDownloadItem;
//import org.lasque.tusdk.core.type.OnlineCommandAction;
//import org.lasque.tusdk.core.network.TuSdkHttpEngine;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import android.view.ViewGroup;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkHttpEngine;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.OnlineCommandAction;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkWebView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
//import org.lasque.tusdk.core.view.TuSdkWebView;

public abstract class TuOnlineFragment extends TuFragment
{
    private long a;
    private String b;
    private String c;
    private boolean d;
    private TuSdkWebView.TuSdkWebViewAdapter e;
    
    public TuOnlineFragment() {
        this.e = new TuSdkWebView.TuSdkWebViewAdapter() {
            @Override
            public void onPageStarted(final TuSdkWebView tuSdkWebView, final String s, final Bitmap bitmap) {
                TuOnlineFragment.this.d = false;
            }
            
            @Override
            public void onPageFinished(final TuSdkWebView tuSdkWebView, final String s) {
                TuOnlineFragment.this.d = true;
                tuSdkWebView.setWebPageUrl("javascript:clientBridge.getHandlers().onTuSdkSend(" + TuOnlineFragment.this.getPageFinishedData() + ");");
            }
            
            @Override
            public void onReceivedTitle(final TuSdkWebView tuSdkWebView, final String title) {
                if (title == null) {
                    return;
                }
                TuOnlineFragment.this.setTitle(title);
            }
        };
    }
    
    public abstract TuSdkWebView getWebview();
    
    public long getDetailDataId() {
        return this.a;
    }
    
    public void setDetailDataId(final long a) {
        this.a = a;
    }
    
    public String getArgs() {
        return this.c;
    }
    
    public void setArgs(final String c) {
        this.c = c;
    }
    
    public String getOnlineType() {
        return this.b;
    }
    
    public void setOnlineType(final String b) {
        this.b = b;
    }
    
    @Override
    protected void viewDidLoad(final ViewGroup viewGroup) {
        this.a(this.getWebview());
    }
    
    @Override
    protected void navigatorBarLoaded(final TuSdkNavigatorBar tuSdkNavigatorBar) {
        super.navigatorBarLoaded(tuSdkNavigatorBar);
        this.setIsSupportSlideBack(false);
    }
    
    @Override
    public void navigatorBarRightAction(final TuSdkNavigatorBar.NavigatorBarButtonInterface navigatorBarButtonInterface) {
        this.dismissActivityWithAnim();
    }
    
    @SuppressLint("JavascriptInterface")
    private void a(final TuSdkWebView tuSdkWebView) {
        if (tuSdkWebView == null) {
            return;
        }
        tuSdkWebView.setAdapter(this.e);
        String s;
        if (this.getDetailDataId() > 0L) {
            s = String.format("/%s/item?id=%s", this.getOnlineType(), this.getDetailDataId());
        }
        else if (!StringHelper.isBlank(this.getArgs())) {
            s = String.format("/%s/index?%s", this.getOnlineType(), this.getArgs());
        }
        else {
            s = String.format("/%s/index", this.getOnlineType());
        }
        tuSdkWebView.setWebPageUrl(TuSdkHttpEngine.shared().getWebUrl(s, true));
        tuSdkWebView.addJavascriptInterface((Object)new TuSdkOnlineInteface(), "tusdkBridge");
    }
    
    protected abstract String getPageFinishedData();
    
    private void a(final String s) {
        if (StringHelper.isEmpty(s)) {
            return;
        }
        final String[] split = s.split("/");
        if (split.length < 2 || !this.getOnlineType().equalsIgnoreCase(split[0])) {
            return;
        }
        switch (OnlineCommandAction.getType(Integer.parseInt(split[1])).ordinal()) {
            case 1: {
                this.handleDownload(split);
                break;
            }
            case 2: {
                this.handleCancel(split);
                break;
            }
            case 3: {
                this.handleSelected(split);
                break;
            }
            case 4: {
                this.handleDetail(split);
                break;
            }
        }
    }
    
    protected void handleDownload(final String[] array) {
        if (array.length < 5) {
            return;
        }
        this.onResourceDownload(Long.parseLong(array[2]), array[3], array[4]);
    }
    
    protected abstract void onResourceDownload(final long p0, final String p1, final String p2);
    
    protected void handleCancel(final String[] array) {
        if (array.length < 3) {
            return;
        }
        this.onResourceCancelDownload(Long.parseLong(array[2]));
    }
    
    protected abstract void onResourceCancelDownload(final long p0);
    
    protected void handleSelected(final String[] array) {
    }
    
    protected void handleDetail(final String[] array) {
    }
    
    protected void notifyOnlineData(final TuSdkDownloadItem tuSdkDownloadItem) {
        if (tuSdkDownloadItem == null || !this.d) {
            return;
        }
        this.getWebview().setWebPageUrl("javascript:clientBridge.getHandlers().onTuSdkSend(" + tuSdkDownloadItem.getStatusChangeData().toString() + ");");
    }
    
    private class TuSdkOnlineInteface
    {
        @JavascriptInterface
        public void onTuSdkPush(final String s) {
            TuOnlineFragment.this.a(s);
        }
    }
}
