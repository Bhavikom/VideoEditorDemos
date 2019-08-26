// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network;

//import org.lasque.tusdk.core.http.RequestHandle;
//import org.lasque.tusdk.core.http.RequestParams;
//import org.lasque.tusdk.core.http.BlackholeHttpResponseHandler;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http.BlackholeHttpResponseHandler;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http.ClearHttpClient;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http.HttpHeader;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http.RequestHandle;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http.RequestParams;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http.ResponseHandlerInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;

import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.http.HttpHeader;
import java.util.List;
//import org.lasque.tusdk.core.http.ResponseHandlerInterface;
//import org.lasque.tusdk.core.http.ClearHttpClient;

public class TuSdkHttp extends ClearHttpClient
{
    public static final String Content_Disposition = "Content-Disposition";
    private ResponseHandlerInterface a;
    
    public static String attachmentFileName(final List<HttpHeader> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        for (final HttpHeader httpHeader : list) {
            if (httpHeader.equalsName("Content-Disposition")) {
                final ArrayList<String> matchStrings = StringHelper.matchStrings(httpHeader.getValue(), "attachment; filename=(.*)$");
                if (matchStrings == null) {
                    break;
                }
                if (matchStrings.size() == 1) {
                    break;
                }
                final String s = matchStrings.get(1);
                if (s == null) {
                    break;
                }
                return s.replace("\"", "");
            }
        }
        return null;
    }
    
    public TuSdkHttp(final int n) {
        super(n);
        this.a = new BlackholeHttpResponseHandler();
    }
    
    @Override
    public RequestHandle get(final String s, final RequestParams requestParams, ResponseHandlerInterface a) {
        if (a == null) {
            a = this.a;
        }
        return super.get(s, requestParams, a);
    }
    
    @Override
    public RequestHandle post(final String s, final RequestParams requestParams, ResponseHandlerInterface a) {
        if (a == null) {
            a = this.a;
        }
        return super.post(s, requestParams, a);
    }
}
