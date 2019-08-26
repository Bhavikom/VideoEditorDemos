// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

public abstract class AbstractHttpEntity implements HttpEntity
{
    protected static final int OUTPUT_BUFFER_SIZE = 4096;
    protected HttpHeader contentType;
    protected HttpHeader contentEncoding;
    protected boolean chunked;
    
    protected AbstractHttpEntity() {
    }
    
    @Override
    public HttpHeader getContentType() {
        return this.contentType;
    }
    
    @Override
    public HttpHeader getContentEncoding() {
        return this.contentEncoding;
    }
    
    @Override
    public boolean isChunked() {
        return this.chunked;
    }
    
    public void setContentType(final HttpHeader contentType) {
        this.contentType = contentType;
    }
    
    public void setContentType(final String s) {
        HttpHeader contentType = null;
        if (s != null) {
            contentType = new HttpHeader("Content-Type", s);
        }
        this.setContentType(contentType);
    }
    
    public void setContentEncoding(final HttpHeader contentEncoding) {
        this.contentEncoding = contentEncoding;
    }
    
    public void setContentEncoding(final String s) {
        HttpHeader contentEncoding = null;
        if (s != null) {
            contentEncoding = new HttpHeader("Content-Encoding", s);
        }
        this.setContentEncoding(contentEncoding);
    }
    
    public void setChunked(final boolean chunked) {
        this.chunked = chunked;
    }
    
    @Override
    public void consumeContent() {
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        if (this.contentType != null) {
            sb.append("Content-Type: ");
            sb.append(this.contentType.getValue());
            sb.append(',');
        }
        if (this.contentEncoding != null) {
            sb.append("Content-Encoding: ");
            sb.append(this.contentEncoding.getValue());
            sb.append(',');
        }
        final long contentLength = this.getContentLength();
        if (contentLength >= 0L) {
            sb.append("Content-Length: ");
            sb.append(contentLength);
            sb.append(',');
        }
        sb.append("Chunked: ");
        sb.append(this.chunked);
        sb.append(']');
        return sb.toString();
    }
}
