// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import java.util.List;

public class UrlEncodedFormEntity extends StringEntity
{
    public UrlEncodedFormEntity(final List<URLEncodedUtils.BasicNameValuePair> list, final String s) {
        super(URLEncodedUtils.format(list, s), "application/x-www-form-urlencoded", s);
    }
    
    public UrlEncodedFormEntity(final List<URLEncodedUtils.BasicNameValuePair> list) {
        this(list, null);
    }
}
