// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.List;
import java.io.UnsupportedEncodingException;
//import org.lasque.tusdk.core.utils.TLog;

public abstract class TextHttpResponseHandler extends ClearHttpResponseHandler
{
    public TextHttpResponseHandler() {
        this("UTF-8");
    }
    
    public TextHttpResponseHandler(final String charset) {
        this.setCharset(charset);
    }
    
    @Override
    public void onProgress(final long n, final long n2) {
    }
    
    public static String getResponseString(final byte[] bytes, final String charsetName) {
        try {
            final String s = (bytes == null) ? null : new String(bytes, charsetName);
            if (s != null && s.startsWith("\ufeff")) {
                return s.substring(1);
            }
            return s;
        }
        catch (UnsupportedEncodingException ex) {
            TLog.e("Encoding response into string failed: %s", ex);
            return null;
        }
    }
    
    public abstract void onFailure(final int p0, final List<HttpHeader> p1, final String p2, final Throwable p3);
    
    public abstract void onSuccess(final int p0, final List<HttpHeader> p1, final String p2);
    
    @Override
    public void onSuccess(final int n, final List<HttpHeader> list, final byte[] array) {
        this.onSuccess(n, list, getResponseString(array, this.getCharset()));
    }
    
    @Override
    public void onFailure(final int n, final List<HttpHeader> list, final byte[] array, final Throwable t) {
        this.onFailure(n, list, getResponseString(array, this.getCharset()), t);
    }
}
