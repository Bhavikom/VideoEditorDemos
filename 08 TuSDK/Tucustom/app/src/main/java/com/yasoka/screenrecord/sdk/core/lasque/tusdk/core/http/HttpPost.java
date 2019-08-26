// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import java.net.URL;

public class HttpPost extends HttpUriRequest
{
    public static final String METHOD_NAME = "POST";
    
    public HttpPost() {
    }
    
    public HttpPost(final String s) {
        super(s);
    }
    
    public HttpPost(final URL url) {
        super(url);
    }
    
    @Override
    public boolean canOutput() {
        return true;
    }
    
    @Override
    public boolean canUseCache() {
        return false;
    }
    
    @Override
    public String getMethod() {
        return "POST";
    }
}
