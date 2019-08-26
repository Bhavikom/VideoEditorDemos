// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import java.util.regex.PatternSyntaxException;
import java.util.regex.Pattern;
import java.util.List;
import android.os.Looper;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.TLog;

public abstract class BinaryHttpResponseHandler extends ClearHttpResponseHandler
{
    private String[] a;
    
    public BinaryHttpResponseHandler() {
        this.a = new String[] { "application/octet-stream", "image/jpeg", "image/png", "image/gif" };
    }
    
    public BinaryHttpResponseHandler(final String[] a) {
        this.a = new String[] { "application/octet-stream", "image/jpeg", "image/png", "image/gif" };
        if (a != null) {
            this.a = a;
        }
        else {
            TLog.e("Constructor passed allowedContentTypes was null !", new Object[0]);
        }
    }
    
    public BinaryHttpResponseHandler(final String[] a, final Looper looper) {
        super(looper);
        this.a = new String[] { "application/octet-stream", "image/jpeg", "image/png", "image/gif" };
        if (a != null) {
            this.a = a;
        }
        else {
            TLog.e("Constructor passed allowedContentTypes was null !", new Object[0]);
        }
    }
    
    public String[] getAllowedContentTypes() {
        return this.a;
    }
    
    @Override
    public abstract void onSuccess(final int p0, final List<HttpHeader> p1, final byte[] p2);
    
    @Override
    public abstract void onFailure(final int p0, final List<HttpHeader> p1, final byte[] p2, final Throwable p3);
    
    @Override
    public final void sendResponseMessage(final HttpResponse httpResponse) {
        final List<HttpHeader> headers = httpResponse.getHeaders("Content-Type");
        if (headers == null || headers.size() != 1) {
            this.sendFailureMessage(httpResponse.getResponseCode(), httpResponse.getAllHeaders(), null, new HttpResponseException(httpResponse.getResponseCode(), "None, or more than one, Content-Type Header found!"));
            return;
        }
        final HttpHeader httpHeader = headers.get(0);
        boolean b = false;
        for (final String regex : this.getAllowedContentTypes()) {
            try {
                if (Pattern.matches(regex, httpHeader.getValue())) {
                    b = true;
                }
            }
            catch (PatternSyntaxException ex) {
                TLog.e("Given pattern is not valid (%s): %s", regex, ex);
            }
        }
        if (!b) {
            this.sendFailureMessage(httpResponse.getResponseCode(), httpResponse.getAllHeaders(), null, new HttpResponseException(httpResponse.getResponseCode(), "Content-Type (" + httpHeader.getValue() + ") not allowed!"));
            return;
        }
        super.sendResponseMessage(httpResponse);
    }
}
