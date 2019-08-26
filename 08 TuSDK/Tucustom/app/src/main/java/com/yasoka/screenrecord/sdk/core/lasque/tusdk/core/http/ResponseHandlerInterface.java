// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import java.net.URL;
import java.util.List;

public interface ResponseHandlerInterface
{
    void sendResponseMessage(final HttpResponse p0);
    
    void sendStartMessage();
    
    void sendFinishMessage();
    
    void sendProgressMessage(final long p0, final long p1);
    
    void sendCancelMessage();
    
    void sendSuccessMessage(final int p0, final List<HttpHeader> p1, final byte[] p2);
    
    void sendFailureMessage(final int p0, final List<HttpHeader> p1, final byte[] p2, final Throwable p3);
    
    void sendRetryMessage(final int p0);
    
    URL getRequestURL();
    
    void setRequestURL(final URL p0);
    
    List<HttpHeader> getRequestHeaders();
    
    void setRequestHeaders(final List<HttpHeader> p0);
    
    boolean getUseSynchronousMode();
    
    void setUseSynchronousMode(final boolean p0);
    
    boolean getUsePoolThread();
    
    void setUsePoolThread(final boolean p0);
    
    void onPreProcessResponse(final ResponseHandlerInterface p0, final HttpResponse p1);
    
    void onPostProcessResponse(final ResponseHandlerInterface p0, final HttpResponse p1);
    
    Object getTag();
    
    void setTag(final Object p0);
}
