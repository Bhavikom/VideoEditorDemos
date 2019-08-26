// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import java.io.OutputStream;
import java.io.InputStream;

public class HttpEntityWrapper implements HttpEntity
{
    protected HttpEntity mWrappedEntity;
    
    public HttpEntityWrapper(final HttpEntity mWrappedEntity) {
        if (mWrappedEntity == null) {
            throw new IllegalArgumentException("wrapped entity must not be null");
        }
        this.mWrappedEntity = mWrappedEntity;
    }
    
    @Override
    public boolean isRepeatable() {
        return this.mWrappedEntity.isRepeatable();
    }
    
    @Override
    public boolean isChunked() {
        return this.mWrappedEntity.isChunked();
    }
    
    @Override
    public long getContentLength() {
        return this.mWrappedEntity.getContentLength();
    }
    
    @Override
    public HttpHeader getContentType() {
        return this.mWrappedEntity.getContentType();
    }
    
    @Override
    public HttpHeader getContentEncoding() {
        return this.mWrappedEntity.getContentEncoding();
    }
    
    @Override
    public InputStream getContent() {
        return this.mWrappedEntity.getContent();
    }
    
    @Override
    public void writeTo(final OutputStream outputStream) {
        this.mWrappedEntity.writeTo(outputStream);
    }
    
    @Override
    public boolean isStreaming() {
        return this.mWrappedEntity.isStreaming();
    }
    
    @Override
    public void consumeContent() {
        this.consumeWrappedEntity();
    }
    
    public void consumeWrappedEntity() {
        this.mWrappedEntity.consumeContent();
    }
}
