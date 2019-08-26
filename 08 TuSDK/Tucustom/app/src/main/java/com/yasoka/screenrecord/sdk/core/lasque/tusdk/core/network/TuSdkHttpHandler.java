// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network;

//import org.lasque.tusdk.core.utils.DateHelper;
import org.json.JSONObject;
import org.apache.http.conn.ConnectTimeoutException;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.utils.json.JsonHelper;
//import org.lasque.tusdk.core.http.HttpHeader;
import java.util.List;
import android.os.Looper;
import android.os.Handler;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http.HttpHeader;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http.TextHttpResponseHandler;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.DateHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkError;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonWrapper;

import java.util.Calendar;
//import org.lasque.tusdk.core.utils.json.JsonWrapper;
//import org.lasque.tusdk.core.utils.TuSdkError;
//import org.lasque.tusdk.core.http.TextHttpResponseHandler;

public abstract class TuSdkHttpHandler extends TextHttpResponseHandler
{
    private TuSdkError a;
    private JsonWrapper b;
    private HttpHandlerProgressListener c;
    private Calendar d;
    private Handler e;
    
    public HttpHandlerProgressListener getProgressListener() {
        return this.c;
    }
    
    public void setProgressListener(final HttpHandlerProgressListener c) {
        this.c = c;
    }
    
    public TuSdkError getError() {
        return this.a;
    }
    
    public void setError(final TuSdkError a) {
        this.a = a;
    }
    
    public JsonWrapper getJson() {
        return this.b;
    }
    
    public Calendar getLastRequestTime() {
        return this.d;
    }
    
    public Handler getMainLooperHandler() {
        if (this.e == null) {
            this.e = new Handler(Looper.getMainLooper());
        }
        return this.e;
    }
    
    public void postInMainThread(final Runnable runnable) {
        if (runnable == null) {
            return;
        }
        this.getMainLooperHandler().post(runnable);
    }
    
    @Override
    public void onSuccess(final int n, final List<HttpHeader> list, final String s) {
        this.a = null;
        this.a(this.hasError(JsonHelper.json(s)));
    }
    
    @Override
    public void onFailure(final int n, final List<HttpHeader> list, final String s, final Throwable t) {
        if (!TuSdkContext.isNetworkAvailable()) {
            this.a = new TuSdkError(TuSdkContext.getString("lsq_network_connection_interruption"), n);
        }
        else if (t != null) {
            if (t instanceof ConnectTimeoutException) {
                this.a = new TuSdkError(TuSdkContext.getString("lsq_network_connection_timeout"), n);
            }
            else {
                this.a = new TuSdkError(t.getMessage(), n);
            }
        }
        else {
            this.a = new TuSdkError(TuSdkContext.getString("lsq_network_request_error"), n);
        }
        this.a(true);
    }
    
    protected boolean hasError(final JSONObject jsonObject) {
        if (jsonObject == null) {
            this.a = new TuSdkError(TuSdkContext.getString("lsq_network_request_return_error"), 404);
            return true;
        }
        this.b = new JsonWrapper(JsonHelper.getJSONObject(jsonObject, "data"));
        final long optLong = jsonObject.optLong("ttp", -1L);
        if (optLong != -1L) {
            this.d = DateHelper.parseCalendar(optLong);
        }
        final int optInt = jsonObject.optInt("ret", -1);
        if (optInt < 0) {
            if (optInt == -206 || optInt == -207) {
                this.a = new TuSdkError("service error", optInt);
            }
            else {
                this.a = new TuSdkError(TuSdkContext.getString("lsq_network_request_error"), optInt);
            }
            return true;
        }
        return false;
    }
    
    private void a(final boolean b) {
        this.onRequestedPrepare(this);
        if (b) {
            this.onRequestedFailed(this);
        }
        else {
            this.onRequestedSucceed(this);
        }
        this.onRequestedFinish(this);
    }
    
    protected void onRequestedPrepare(final TuSdkHttpHandler tuSdkHttpHandler) {
    }
    
    protected abstract void onRequestedSucceed(final TuSdkHttpHandler p0);
    
    protected void onRequestedFailed(final TuSdkHttpHandler tuSdkHttpHandler) {
    }
    
    protected void onRequestedFinish(final TuSdkHttpHandler tuSdkHttpHandler) {
    }
    
    @Override
    public void onProgress(final long n, final long n2) {
        if (this.c != null && n2 != -1L) {
            this.c.onHttpHandlerProgress(n, n2);
        }
    }
    
    public void destory() {
        this.c = null;
    }
    
    public interface HttpHandlerProgressListener
    {
        void onHttpHandlerProgress(final long p0, final long p1);
    }
}
