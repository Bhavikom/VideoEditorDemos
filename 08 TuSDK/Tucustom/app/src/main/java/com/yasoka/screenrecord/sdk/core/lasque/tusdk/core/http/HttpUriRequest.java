// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import javax.net.ssl.HttpsURLConnection;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public abstract class HttpUriRequest
{
    private HttpEntity a;
    private final List<HttpHeader> b;
    private URL c;
    private HttpURLConnection d;
    
    public abstract String getMethod();
    
    public abstract boolean canOutput();
    
    public abstract boolean canUseCache();
    
    public HttpUriRequest() {
        this.b = new ArrayList<HttpHeader>();
    }
    
    public HttpUriRequest(final URL url) {
        this.b = new ArrayList<HttpHeader>();
        this.setURL(url);
    }
    
    public HttpUriRequest(final String s) {
        this.b = new ArrayList<HttpHeader>();
        this.setURL(URLEncodedUtils.getURL(s));
    }
    
    public List<HttpHeader> getAllHeaders() {
        return this.b;
    }
    
    public boolean containsHeader(final String s) {
        return s != null && this.getFirstHeader(s) != null;
    }
    
    public HttpHeader getFirstHeader(final String s) {
        if (s == null) {
            return null;
        }
        for (final HttpHeader httpHeader : this.b) {
            if (httpHeader.equalsName(s)) {
                return httpHeader;
            }
        }
        return null;
    }
    
    public void setHeader(final String s, final String value) {
        if (s == null || value == null) {
            return;
        }
        final HttpHeader firstHeader = this.getFirstHeader(s);
        if (firstHeader != null) {
            firstHeader.setValue(value);
        }
        else {
            this.b.add(new HttpHeader(s, value));
        }
    }
    
    public void addHeader(final String s, final String s2) {
        if (s == null || s2 == null) {
            return;
        }
        this.b.add(new HttpHeader(s, s2));
    }
    
    public void setHeaders(final List<HttpHeader> list) {
        if (list == null) {
            return;
        }
        for (final HttpHeader httpHeader : list) {
            this.setHeader(httpHeader.getName(), httpHeader.getValue());
        }
    }
    
    public void addHeaders(final List<HttpHeader> list) {
        if (list == null) {
            return;
        }
        for (final HttpHeader httpHeader : list) {
            this.addHeader(httpHeader.getName(), httpHeader.getValue());
        }
    }
    
    public void removeHeader(final String s) {
        for (final HttpHeader httpHeader : new ArrayList<HttpHeader>(this.b)) {
            if (httpHeader.equalsName(s)) {
                this.b.remove(httpHeader);
            }
        }
    }
    
    public void removeHeader(final HttpHeader httpHeader) {
        if (httpHeader == null) {
            return;
        }
        this.b.remove(httpHeader);
    }
    
    public HttpEntity getEntity() {
        return this.a;
    }
    
    public void setEntity(final HttpEntity a) {
        this.a = a;
    }
    
    public URL getURL() {
        return this.c;
    }
    
    public void setURL(final URL c) {
        this.c = c;
    }
    
    public void abort() {
        if (this.d == null) {
            return;
        }
        this.d.disconnect();
        this.d = null;
    }
    
    public HttpURLConnection openConnection() {
        this.abort();
        if (this.c.getProtocol().toLowerCase().equals("https")) {
            try {
                this.d = (HttpsURLConnection)this.c.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                this.d = (HttpURLConnection)this.c.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.d.setDoOutput(this.canOutput());
        this.d.setDoInput(true);
        try {
            this.d.setRequestMethod(this.getMethod());
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        this.d.setUseCaches(this.canUseCache());
        if (!this.canUseCache() && this.canOutput() && this.getEntity() != null) {
            this.d.setFixedLengthStreamingMode((int)this.getEntity().getContentLength());
        }
        this.a(this.d);
        return this.d;
    }
    
    private void a(final HttpURLConnection httpURLConnection) {
        if (httpURLConnection == null) {
            return;
        }
        for (final HttpHeader httpHeader : this.b) {
            httpURLConnection.setRequestProperty(httpHeader.getName(), httpHeader.getValue());
        }
        if (this.getEntity() == null) {
            return;
        }
        if (this.getEntity().getContentEncoding() != null) {
            httpURLConnection.setRequestProperty(this.getEntity().getContentEncoding().getName(), this.getEntity().getContentEncoding().getValue());
        }
        if (this.getEntity().getContentType() != null) {
            httpURLConnection.setRequestProperty(this.getEntity().getContentType().getName(), this.getEntity().getContentType().getValue());
        }
    }
}
