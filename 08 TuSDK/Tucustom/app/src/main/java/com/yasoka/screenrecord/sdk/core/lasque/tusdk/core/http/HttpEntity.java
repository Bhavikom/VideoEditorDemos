// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import java.io.OutputStream;
import java.io.InputStream;

public interface HttpEntity
{
    boolean isRepeatable();
    
    boolean isChunked();
    
    long getContentLength();
    
    HttpHeader getContentType();
    
    HttpHeader getContentEncoding();
    
    InputStream getContent();
    
    void writeTo(final OutputStream p0);
    
    boolean isStreaming();
    
    void consumeContent();
}
