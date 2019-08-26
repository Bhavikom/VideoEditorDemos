// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import java.net.URL;

public class HttpGet extends HttpUriRequest
{
    public static final String METHOD_NAME = "GET";
    
    public HttpGet() {
    }
    
    public HttpGet(final String s) {
        super(s);
    }
    
    public HttpGet(final URL url) {
        super(url);
    }
    
    @Override
    public boolean canOutput() {
        return false;
    }
    
    @Override
    public boolean canUseCache() {
        return true;
    }
    
    @Override
    public String getMethod() {
        return "GET";
    }
}
