// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

public class HttpHeader
{
    private String a;
    private String b;
    
    public String getName() {
        return this.a;
    }
    
    public void setName(final String a) {
        this.a = a;
    }
    
    public String getValue() {
        return this.b;
    }
    
    public void setValue(final String b) {
        this.b = b;
    }
    
    public HttpHeader() {
    }
    
    public HttpHeader(final String a, final String b) {
        this.a = a;
        this.b = b;
    }
    
    public boolean equalsName(final String anotherString) {
        return this.a != null && anotherString != null && this.a.equalsIgnoreCase(anotherString);
    }
    
    public boolean equalsValue(final String anotherString) {
        return this.b != null && anotherString != null && this.b.equalsIgnoreCase(anotherString);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof HttpHeader)) {
            return false;
        }
        final HttpHeader httpHeader = (HttpHeader)o;
        return this.equalsName(httpHeader.getName()) && this.equalsValue(httpHeader.getValue());
    }
}
