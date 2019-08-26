// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ReflectUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
//import org.lasque.tusdk.core.utils.ReflectUtils;

public class StringEntity extends AbstractHttpEntity implements Cloneable
{
    public static final String TEXT_PLAIN = "text/plain";
    protected byte[] content = null;
    
    public StringEntity(final String s, final String s2, final String s3) {
        ReflectUtils.notNull(s, "Source string");
        final String str = (s2 != null) ? s2 : "text/plain";
        final String s4 = (s3 != null) ? s3 : "UTF-8";
        try {
            this.content = s.getBytes(s4);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.setContentType(str + "; " + s4);
    }
    
    public StringEntity(final String s, final String s2) {
        this(s, null, s2);
        content = new byte[0];
    }
    
    public StringEntity(final String s) {
        this(s, null);
    }
    
    @Override
    public boolean isRepeatable() {
        return true;
    }
    
    @Override
    public long getContentLength() {
        return this.content.length;
    }
    
    @Override
    public InputStream getContent() {
        return new ByteArrayInputStream(this.content);
    }
    
    @Override
    public void writeTo(final OutputStream outputStream) {
        ReflectUtils.notNull(outputStream, "Output stream");
        try {
            outputStream.write(this.content);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    @Override
    public boolean isStreaming() {
        return false;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
