// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import org.json.JSONTokener;
import org.json.JSONException;
import org.json.JSONArray;
//import org.lasque.tusdk.core.utils.TLog;
import org.json.JSONObject;
import java.util.List;

public class JsonHttpResponseHandler extends TextHttpResponseHandler
{
    private boolean a;
    
    public JsonHttpResponseHandler() {
        super("UTF-8");
        this.a = true;
    }
    
    public JsonHttpResponseHandler(final String s) {
        super(s);
        this.a = true;
    }
    
    public JsonHttpResponseHandler(final boolean a) {
        super("UTF-8");
        this.a = true;
        this.a = a;
    }
    
    public JsonHttpResponseHandler(final String s, final boolean a) {
        super(s);
        this.a = true;
        this.a = a;
    }
    
    public void onSuccess(final int n, final List<HttpHeader> list, final JSONObject jsonObject) {
        TLog.w("onSuccess(int, List<HttpHeader>, JSONObject) was not overriden, but callback was received", new Object[0]);
    }
    
    public void onSuccess(final int n, final List<HttpHeader> list, final JSONArray jsonArray) {
        TLog.w("onSuccess(int, List<HttpHeader>, JSONArray) was not overriden, but callback was received", new Object[0]);
    }
    
    public void onFailure(final int n, final List<HttpHeader> list, final Throwable t, final JSONObject jsonObject) {
        TLog.w("onFailure(int, List<HttpHeader>, Throwable, JSONObject) was not overriden, but callback was received: %s", t);
    }
    
    public void onFailure(final int n, final List<HttpHeader> list, final Throwable t, final JSONArray jsonArray) {
        TLog.w("onFailure(int, List<HttpHeader>, Throwable, JSONArray) was not overriden, but callback was received: %s", t);
    }
    
    @Override
    public void onFailure(final int n, final List<HttpHeader> list, final String s, final Throwable t) {
        TLog.w("onFailure(int, List<HttpHeader>, String, Throwable) was not overriden, but callback was received: %s", t);
    }
    
    @Override
    public void onSuccess(final int n, final List<HttpHeader> list, final String s) {
        TLog.w("onSuccess(int, List<HttpHeader>, String) was not overriden, but callback was received", new Object[0]);
    }
    
    @Override
    public final void onSuccess(final int n, final List<HttpHeader> list, final byte[] array) {
        if (n != 204) {
            final Runnable target = new Runnable() {
                @Override
                public void run() {
                    JsonHttpResponseHandler.this.postRunnable(new Runnable() {
                        final /* synthetic */ Object a = JsonHttpResponseHandler.this.parseResponse(array);

                        @Override
                        public void run() {
                            if (!JsonHttpResponseHandler.this.a && this.a == null) {
                                JsonHttpResponseHandler.this.onSuccess(n, list, (String)null);
                            }
                            else if (this.a instanceof JSONObject) {
                                JsonHttpResponseHandler.this.onSuccess(n, list, (JSONObject)this.a);
                            }
                            else if (this.a instanceof JSONArray) {
                                JsonHttpResponseHandler.this.onSuccess(n, list, (JSONArray)this.a);
                            }
                            else if (this.a instanceof String) {
                                if (JsonHttpResponseHandler.this.a) {
                                    JsonHttpResponseHandler.this.onFailure(n, list, (String)this.a, (Throwable)new JSONException("Response cannot be parsed as JSON data"));
                                }
                                else {
                                    JsonHttpResponseHandler.this.onSuccess(n, list, (String)this.a);
                                }
                            }
                            else {
                                JsonHttpResponseHandler.this.onFailure(n, list, (Throwable)new JSONException("Unexpected response type " + this.a.getClass().getName()), (JSONObject)null);
                            }
                        }
                    });
                }
            };
            if (!this.getUseSynchronousMode() && !this.getUsePoolThread()) {
                new Thread(target).start();
            }
            else {
                target.run();
            }
        }
        else {
            this.onSuccess(n, list, new JSONObject());
        }
    }
    
    @Override
    public final void onFailure(final int n, final List<HttpHeader> list, final byte[] array, final Throwable t) {
        if (array != null) {
            final Runnable target = new Runnable() {
                @Override
                public void run() {
                    JsonHttpResponseHandler.this.postRunnable(new Runnable() {
                        final /* synthetic */ Object a = JsonHttpResponseHandler.this.parseResponse(array);

                        @Override
                        public void run() {
                            if (!JsonHttpResponseHandler.this.a && this.a == null) {
                                JsonHttpResponseHandler.this.onFailure(n, list, (String)null, t);
                            }
                            else if (this.a instanceof JSONObject) {
                                JsonHttpResponseHandler.this.onFailure(n, list, t, (JSONObject)this.a);
                            }
                            else if (this.a instanceof JSONArray) {
                                JsonHttpResponseHandler.this.onFailure(n, list, t, (JSONArray)this.a);
                            }
                            else if (this.a instanceof String) {
                                JsonHttpResponseHandler.this.onFailure(n, list, (String)this.a, t);
                            }
                            else {
                                JsonHttpResponseHandler.this.onFailure(n, list, (Throwable)new JSONException("Unexpected response type " + this.a.getClass().getName()), (JSONObject)null);
                            }
                        }
                    });
                }
            };
            if (!this.getUseSynchronousMode() && !this.getUsePoolThread()) {
                new Thread(target).start();
            }
            else {
                target.run();
            }
        }
        else {
            TLog.w("response body is null, calling onFailure(Throwable, JSONObject)", new Object[0]);
            this.onFailure(n, list, t, (JSONObject)null);
        }
    }
    
    protected Object parseResponse(final byte[] array) {
        if (null == array) {
            return null;
        }
        Object o = null;
        String s = TextHttpResponseHandler.getResponseString(array, this.getCharset());
        if (s != null) {
            s = s.trim();
            if (this.a) {
                if (s.startsWith("{") || s.startsWith("[")) {
                    try {
                        o = new JSONTokener(s).nextValue();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if ((s.startsWith("{") && s.endsWith("}")) || (s.startsWith("[") && s.endsWith("]"))) {
                try {
                    o = new JSONTokener(s).nextValue();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (s.startsWith("\"") && s.endsWith("\"")) {
                o = s.substring(1, s.length() - 1);
            }
        }
        if (o == null) {
            o = s;
        }
        return o;
    }
    
    public boolean isUseRFC5179CompatibilityMode() {
        return this.a;
    }
    
    public void setUseRFC5179CompatibilityMode(final boolean a) {
        this.a = a;
    }
}
