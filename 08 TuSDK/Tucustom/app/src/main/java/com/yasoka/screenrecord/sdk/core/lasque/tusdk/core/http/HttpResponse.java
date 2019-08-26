// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;

import java.io.IOException;
import java.util.Map;
//import org.lasque.tusdk.core.utils.FileHelper;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class HttpResponse
{
    private final HttpURLConnection a;
    private OutputStream b;
    private InputStream c;
    private HttpEntity d;
    private List<HttpHeader> e;
    private int f;
    private String g;
    private long h;
    private String i;
    private String j;
    
    public HttpResponse(final HttpURLConnection a) {
        this.e = new ArrayList<HttpHeader>();
        this.f = -1;
        this.h = -1L;
        this.a = a;
    }
    
    public int getResponseCode() {
        return this.f;
    }
    
    public String getResponseMessage() {
        return this.g;
    }
    
    public long getContentLength() {
        return this.h;
    }
    
    public String getContentType() {
        return this.i;
    }
    
    public String getContentEncoding() {
        return this.j;
    }
    
    public List<HttpHeader> getAllHeaders() {
        return this.e;
    }
    
    public List<HttpHeader> getHeaders(final String s) {
        if (s == null) {
            return null;
        }
        final List<HttpHeader> allHeaders = this.getAllHeaders();
        if (allHeaders == null) {
            return null;
        }
        final ArrayList<HttpHeader> list = new ArrayList<HttpHeader>();
        for (final HttpHeader httpHeader : allHeaders) {
            if (httpHeader.equalsName(s)) {
                list.add(httpHeader);
            }
        }
        return list;
    }
    
    public HttpHeader getFirstHeader(final String s) {
        final List<HttpHeader> headers = this.getHeaders(s);
        if (headers == null || headers.isEmpty()) {
            return null;
        }
        return headers.get(0);
    }
    
    public HttpEntity getEntity() {
        return this.d;
    }
    
    public void setEntity(final HttpEntity d) {
        if (d == null) {
            return;
        }
        this.d = d;
    }
    
    public OutputStream getOutputStream() {
        return this.b;
    }
    
    public void openOutputStream() {
        if (this.b != null) {
            return;
        }
        try {
            this.b = this.a.getOutputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    public InputStream getInputStream() {
        return this.c;
    }
    
    public void openInputStream() {
        if (this.b != null) {
            try {
                this.b.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            FileHelper.safeClose(this.b);
            this.b = null;
        }
        this.a();
        if (this.c != null) {
            return;
        }
        try {
            this.c = this.a.getInputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    private void a() {
        try {
            this.f = this.a.getResponseCode();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            this.g = this.a.getResponseMessage();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        this.h = this.a.getContentLength();
        this.i = this.a.getContentType();
        this.j = this.a.getContentEncoding();
        this.d = new HttpResponseEntity();
        final Map<String, List<String>> headerFields = this.a.getHeaderFields();
        if (headerFields == null) {
            return;
        }
        for (final Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
            if (entry.getValue() != null) {
                if (entry.getValue().isEmpty()) {
                    continue;
                }
                final Iterator<String> iterator2 = entry.getValue().iterator();
                while (iterator2.hasNext()) {
                    this.e.add(new HttpHeader(entry.getKey(), iterator2.next()));
                }
            }
        }
    }
    
    public void disconnect() {
        if (this.c != null) {
            FileHelper.safeClose(this.c);
            this.c = null;
        }
        if (this.getEntity() != null) {
            this.getEntity().consumeContent();
        }
        this.a.disconnect();
    }
    
    private class HttpResponseEntity implements HttpEntity
    {
        @Override
        public boolean isRepeatable() {
            return false;
        }
        
        @Override
        public boolean isChunked() {
            return false;
        }
        
        @Override
        public long getContentLength() {
            return HttpResponse.this.h;
        }
        
        @Override
        public HttpHeader getContentType() {
            if (HttpResponse.this.i == null) {
                return null;
            }
            return new HttpHeader("Content-Type", HttpResponse.this.i);
        }
        
        @Override
        public HttpHeader getContentEncoding() {
            if (HttpResponse.this.j == null) {
                return null;
            }
            return new HttpHeader("Content-Encoding", HttpResponse.this.j);
        }
        
        @Override
        public InputStream getContent() {
            return HttpResponse.this.getInputStream();
        }
        
        @Override
        public void writeTo(final OutputStream outputStream) {
        }
        
        @Override
        public boolean isStreaming() {
            return true;
        }
        
        @Override
        public void consumeContent() {
        }
    }
}
