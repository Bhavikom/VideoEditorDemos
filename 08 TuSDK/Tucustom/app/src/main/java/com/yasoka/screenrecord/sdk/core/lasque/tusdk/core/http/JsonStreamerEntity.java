// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
//import org.lasque.tusdk.core.utils.FileHelper;
//import org.lasque.tusdk.core.utils.TLog;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.zip.GZIPOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.Locale;
import android.text.TextUtils;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.HashMap;
import java.util.Map;

public class JsonStreamerEntity implements HttpEntity
{
    private static final UnsupportedOperationException a;
    private static final byte[] b;
    private static final byte[] c;
    private static final byte[] d;
    private static final byte[] e;
    private static final byte[] f;
    private static final byte[] g;
    private static final HttpHeader h;
    private static final HttpHeader i;
    private final byte[] j;
    private final Map<String, Object> k;
    private final HttpHeader l;
    private final byte[] m;
    private final ResponseHandlerInterface n;
    
    public JsonStreamerEntity(final ResponseHandlerInterface n, final boolean b, final String s) {
        this.j = new byte[4096];
        this.k = new HashMap<String, Object>();
        this.n = n;
        this.l = (b ? JsonStreamerEntity.i : null);
        this.m = (byte[])(TextUtils.isEmpty((CharSequence)s) ? null : a(s));
    }
    
    static byte[] a(final String s) {
        if (s == null) {
            return JsonStreamerEntity.d;
        }
        final StringBuilder sb = new StringBuilder(128);
        sb.append('\"');
        final int length = s.length();
        int index = -1;
        while (++index < length) {
            final char char1 = s.charAt(index);
            switch (char1) {
                case 34: {
                    sb.append("\\\"");
                    continue;
                }
                case 92: {
                    sb.append("\\\\");
                    continue;
                }
                case 8: {
                    sb.append("\\b");
                    continue;
                }
                case 12: {
                    sb.append("\\f");
                    continue;
                }
                case 10: {
                    sb.append("\\n");
                    continue;
                }
                case 13: {
                    sb.append("\\r");
                    continue;
                }
                case 9: {
                    sb.append("\\t");
                    continue;
                }
                default: {
                    if (char1 <= '\u001f' || (char1 >= '\u007f' && char1 <= '\u009f') || (char1 >= '\u2000' && char1 <= '\u20ff')) {
                        final String hexString = Integer.toHexString(char1);
                        sb.append("\\u");
                        for (int n = 4 - hexString.length(), i = 0; i < n; ++i) {
                            sb.append('0');
                        }
                        sb.append(hexString.toUpperCase(Locale.US));
                        continue;
                    }
                    sb.append(char1);
                    continue;
                }
            }
        }
        sb.append('\"');
        return sb.toString().getBytes();
    }
    
    public void addPart(final String s, final Object o) {
        this.k.put(s, o);
    }
    
    @Override
    public boolean isRepeatable() {
        return false;
    }
    
    @Override
    public boolean isChunked() {
        return false;
    }
    
    @Override
    public boolean isStreaming() {
        return false;
    }
    
    @Override
    public long getContentLength() {
        return -1L;
    }
    
    @Override
    public HttpHeader getContentEncoding() {
        return this.l;
    }
    
    @Override
    public HttpHeader getContentType() {
        return JsonStreamerEntity.h;
    }
    
    @Override
    public void consumeContent() {
    }
    
    @Override
    public InputStream getContent() {
        throw JsonStreamerEntity.a;
    }
    
    @Override
    public void writeTo(final OutputStream out) {
        if (out == null) {
            throw new IllegalStateException("Output stream cannot be null.");
        }
        final long currentTimeMillis = System.currentTimeMillis();
         OutputStream outputStream = null;
        try {
            outputStream = (this.l != null) ? new GZIPOutputStream(out, 4096) : out;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            outputStream.write(123);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        final Set<String> keySet = this.k.keySet();
        final int size = keySet.size();
        if (0 < size) {
            int n = 0;
            for (final String s : keySet) {
                ++n;
                try {
                    final Object value = this.k.get(s);
                    outputStream.write(a(s));
                    outputStream.write(58);
                    if (value == null) {
                        outputStream.write(JsonStreamerEntity.d);
                    }
                    else {
                        final boolean b = value instanceof RequestParams.FileWrapper;
                        if (b || value instanceof RequestParams.StreamWrapper) {
                            outputStream.write(123);
                            if (b) {
                                this.a(outputStream, (RequestParams.FileWrapper)value);
                            }
                            else {
                                this.a(outputStream, (RequestParams.StreamWrapper)value);
                            }
                            outputStream.write(125);
                        }
                        else if (value instanceof JsonValueInterface) {
                            outputStream.write(((JsonValueInterface)value).getEscapedJsonValue());
                        }
                        else if (value instanceof JSONObject) {
                            outputStream.write(value.toString().getBytes());
                        }
                        else if (value instanceof JSONArray) {
                            outputStream.write(value.toString().getBytes());
                        }
                        else if (value instanceof Boolean) {
                            outputStream.write(((boolean)value) ? JsonStreamerEntity.b : JsonStreamerEntity.c);
                        }
                        else if (value instanceof Long) {
                            outputStream.write((((Number)value).longValue() + "").getBytes());
                        }
                        else if (value instanceof Double) {
                            outputStream.write((((Number)value).doubleValue() + "").getBytes());
                        }
                        else if (value instanceof Float) {
                            outputStream.write((((Number)value).floatValue() + "").getBytes());
                        }
                        else if (value instanceof Integer) {
                            outputStream.write((((Number)value).intValue() + "").getBytes());
                        }
                        else {
                            outputStream.write(a(value.toString()));
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    if (this.m != null || n < size) {
                        try {
                            outputStream.write(44);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
            final long lng = System.currentTimeMillis() - currentTimeMillis;
            if (this.m != null) {
                try {
                    outputStream.write(this.m);
                    outputStream.write(58);
                    outputStream.write((lng + "").getBytes());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
            TLog.i("Uploaded JSON in %s seconds", Math.floor((double)(lng / 1000L)));
        }
        try {
            outputStream.write(125);
            outputStream.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        FileHelper.safeClose(outputStream);
    }
    
    private void a(final OutputStream outputStream, final RequestParams.StreamWrapper streamWrapper) {
        this.a(outputStream, streamWrapper.name, streamWrapper.contentType);
        final Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 18);
        int read = 0;
        while (true) {
            try {
                if (!((read = streamWrapper.inputStream.read(this.j)) != -1)) break;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            base64OutputStream.write(this.j, 0, read);
        }
        FileHelper.safeClose(base64OutputStream);
        this.a(outputStream);
        if (streamWrapper.autoClose) {
            FileHelper.safeClose(streamWrapper.inputStream);
        }
    }
    
    private void a(final OutputStream outputStream, final RequestParams.FileWrapper fileWrapper) {
        try {
            this.a(outputStream, fileWrapper.file.getName(), fileWrapper.contentType);
            long n = 0L;
            final long length = fileWrapper.file.length();
            final FileInputStream fileInputStream = new FileInputStream(fileWrapper.file);
            final Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 18);
            int read;
            while ((read = fileInputStream.read(this.j)) != -1) {
                base64OutputStream.write(this.j, 0, read);
                n += read;
                this.n.sendProgressMessage(n, length);
            }
            FileHelper.safeClose(base64OutputStream);
            this.a(outputStream);
            FileHelper.safeClose(fileInputStream);
        }catch (Exception e){

        }

    }
    
    private void a(final OutputStream outputStream, final String s, final String s2) {
        try {
            outputStream.write(JsonStreamerEntity.e);
            outputStream.write(58);
            outputStream.write(a(s));
            outputStream.write(44);
            outputStream.write(JsonStreamerEntity.f);
            outputStream.write(58);
            outputStream.write(a(s2));
            outputStream.write(44);
            outputStream.write(JsonStreamerEntity.g);
            outputStream.write(58);
            outputStream.write(34);
        }catch (Exception e){

        }

    }
    
    private void a(final OutputStream outputStream) {
        try {
            outputStream.write(34);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    static {
        a = new UnsupportedOperationException("Unsupported operation in this implementation.");
        b = "true".getBytes();
        c = "false".getBytes();
        d = "null".getBytes();
        e = a("name");
        f = a("type");
        g = a("contents");
        h = new HttpHeader("Content-Type", "application/json");
        i = new HttpHeader("Content-Encoding", "gzip");
    }
}
