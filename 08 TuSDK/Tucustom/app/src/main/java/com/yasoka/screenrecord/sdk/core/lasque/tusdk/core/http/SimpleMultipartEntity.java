// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

//import org.lasque.tusdk.core.utils.FileHelper;
import java.io.FileInputStream;
import android.text.TextUtils;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.io.OutputStream;
import java.util.Iterator;
import java.io.InputStream;
import java.io.File;
import java.io.IOException;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.Random;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.util.List;

class SimpleMultipartEntity implements HttpEntity
{
    private static final byte[] a;
    private static final byte[] b;
    private static final char[] c;
    private final String d;
    private final byte[] e;
    private final byte[] f;
    private final List<FilePart> g;
    private final ByteArrayOutputStream h;
    private final ResponseHandlerInterface i;
    private boolean j;
    private long k;
    private long l;
    
    public SimpleMultipartEntity(final ResponseHandlerInterface i) {
        this.g = new ArrayList<FilePart>();
        this.h = new ByteArrayOutputStream();
        final StringBuilder sb = new StringBuilder();
        final Random random = new Random();
        for (int j = 0; j < 30; ++j) {
            sb.append(SimpleMultipartEntity.c[random.nextInt(SimpleMultipartEntity.c.length)]);
        }
        this.d = sb.toString();
        this.e = ("--" + this.d + "\r\n").getBytes();
        this.f = ("--" + this.d + "--" + "\r\n").getBytes();
        this.i = i;
    }
    
    public void addPart(final String s, final String s2, final String s3) {
        try {
            this.h.write(this.e);
            this.h.write(this.c(s));
            this.h.write(this.b(s3));
            this.h.write(SimpleMultipartEntity.a);
            this.h.write(s2.getBytes());
            this.h.write(SimpleMultipartEntity.a);
        }
        catch (IOException ex) {
            TLog.e(ex, "addPart ByteArrayOutputStream exception", new Object[0]);
        }
    }
    
    public void addPartWithCharset(final String s, final String s2, String str) {
        if (str == null) {
            str = "UTF-8";
        }
        this.addPart(s, s2, "text/plain; charset=" + str);
    }
    
    public void addPart(final String s, final String s2) {
        this.addPartWithCharset(s, s2, null);
    }
    
    public void addPart(final String s, final File file) {
        this.addPart(s, file, null);
    }
    
    public void addPart(final String s, final File file, final String s2) {
        this.g.add(new FilePart(s, file, this.a(s2)));
    }
    
    public void addPart(final String s, final File file, final String s2, final String s3) {
        this.g.add(new FilePart(s, file, this.a(s2), s3));
    }
    
    public void addPart(final String s, final String s2, final InputStream inputStream, final String s3) {
        try {
            this.h.write(this.e);
            this.h.write(this.a(s, s2));
            this.h.write(this.b(s3));
            this.h.write(SimpleMultipartEntity.b);
            this.h.write(SimpleMultipartEntity.a);
            final byte[] array = new byte[4096];
            int read;
            while ((read = inputStream.read(array)) != -1) {
                this.h.write(array, 0, read);
            }
            this.h.write(SimpleMultipartEntity.a);
            this.h.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
    
    private String a(final String s) {
        return (s == null) ? "application/octet-stream" : s;
    }
    
    private byte[] b(final String s) {
        return ("Content-Type: " + this.a(s) + "\r\n").getBytes();
    }
    
    private byte[] c(final String str) {
        return ("Content-Disposition: form-data; name=\"" + str + "\"" + "\r\n").getBytes();
    }
    
    private byte[] a(final String str, final String str2) {
        return ("Content-Disposition: form-data; name=\"" + str + "\"; filename=\"" + str2 + "\"" + "\r\n").getBytes();
    }
    
    private void a(final long n) {
        this.k += n;
        this.i.sendProgressMessage(this.k, this.l);
    }
    
    @Override
    public long getContentLength() {
        long n = this.h.size();
        final Iterator<FilePart> iterator = this.g.iterator();
        while (iterator.hasNext()) {
            final long totalLength = iterator.next().getTotalLength();
            if (totalLength < 0L) {
                return -1L;
            }
            n += totalLength;
        }
        return n + this.f.length;
    }
    
    @Override
    public HttpHeader getContentType() {
        return new HttpHeader("Content-Type", "multipart/form-data; boundary=" + this.d);
    }
    
    @Override
    public boolean isChunked() {
        return false;
    }
    
    public void setIsRepeatable(final boolean j) {
        this.j = j;
    }
    
    @Override
    public boolean isRepeatable() {
        return this.j;
    }
    
    @Override
    public boolean isStreaming() {
        return false;
    }
    
    @Override
    public void writeTo(final OutputStream out) {
        this.k = 0L;
        this.l = (int)this.getContentLength();
        try {
            this.h.writeTo(out);
            this.a(this.h.size());
            final Iterator<FilePart> iterator = this.g.iterator();
            while (iterator.hasNext()) {
                iterator.next().writeTo(out);
            }
            out.write(this.f);
            this.a(this.f.length);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
    
    @Override
    public HttpHeader getContentEncoding() {
        return null;
    }
    
    @Override
    public void consumeContent() {
        if (this.isStreaming()) {
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        }
    }
    
    @Override
    public InputStream getContent() {
        throw new UnsupportedOperationException("getContent() is not supported. Use writeTo() instead.");
    }
    
    static {
        a = "\r\n".getBytes();
        b = "Content-Transfer-Encoding: binary\r\n".getBytes();
        c = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    }
    
    private class FilePart
    {
        public final File file;
        public final byte[] header;
        
        public FilePart(final String s, final File file, final String s2, final String s3) {
            this.header = this.a(s, TextUtils.isEmpty((CharSequence)s3) ? file.getName() : s3, s2);
            this.file = file;
        }
        
        public FilePart(final String s, final File file, final String s2) {
            this.header = this.a(s, file.getName(), s2);
            this.file = file;
        }
        
        private byte[] a(final String s, final String s2, final String s3) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                byteArrayOutputStream.write(SimpleMultipartEntity.this.e);
                byteArrayOutputStream.write(SimpleMultipartEntity.this.a(s, s2));
                byteArrayOutputStream.write(SimpleMultipartEntity.this.b(s3));
                byteArrayOutputStream.write(SimpleMultipartEntity.b);
                byteArrayOutputStream.write(SimpleMultipartEntity.a);
            }
            catch (IOException ex) {
                TLog.e(ex, "createHeader ByteArrayOutputStream exception", new Object[0]);
            }
            return byteArrayOutputStream.toByteArray();
        }
        
        public long getTotalLength() {
            return this.header.length + (this.file.length() + SimpleMultipartEntity.a.length);
        }
        
        public void writeTo(final OutputStream outputStream) {
            try {
                outputStream.write(this.header);
                SimpleMultipartEntity.this.a(this.header.length);
                final FileInputStream fileInputStream = new FileInputStream(this.file);
                final byte[] array = new byte[4096];
                int read;
                while ((read = fileInputStream.read(array)) != -1) {
                    outputStream.write(array, 0, read);
                    SimpleMultipartEntity.this.a(read);
                }
                outputStream.write(SimpleMultipartEntity.a);
                SimpleMultipartEntity.this.a(SimpleMultipartEntity.a.length);
                outputStream.flush();
                FileHelper.safeClose(fileInputStream);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }
}
