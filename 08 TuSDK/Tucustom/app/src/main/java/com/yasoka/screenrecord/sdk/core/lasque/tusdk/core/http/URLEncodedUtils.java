// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.net.MalformedURLException;
//import org.lasque.tusdk.core.utils.TLog;
import java.net.URL;
import java.util.BitSet;

public class URLEncodedUtils
{
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final BitSet a;
    private static final BitSet b;
    private static final BitSet c;
    private static final BitSet d;
    private static final BitSet e;
    private static final BitSet f;
    private static final BitSet g;
    
    public static URL getURL(final String spec) {
        try {
            return new URL(spec);
        }
        catch (MalformedURLException ex) {
            TLog.e("getURI: %s", ex);
            return null;
        }
    }
    
    public static String format(final List<BasicNameValuePair> list, final String s) {
        return format(list, '&', s);
    }
    
    public static String format(final List<BasicNameValuePair> list, final char c, final String s) {
        final StringBuilder sb = new StringBuilder();
        for (final BasicNameValuePair basicNameValuePair : list) {
            final String a = a(basicNameValuePair.getName(), s);
            final String a2 = a(basicNameValuePair.getValue(), s);
            if (sb.length() > 0) {
                sb.append(c);
            }
            sb.append(a);
            if (a2 != null) {
                sb.append("=");
                sb.append(a2);
            }
        }
        return sb.toString();
    }
    
    private static String a(final String s, final String s2) {
        if (s == null) {
            return null;
        }
        return a(s, Charset.forName((s2 != null) ? s2 : "UTF-8"), URLEncodedUtils.g, true);
    }
    
    private static String a(final String str, final Charset charset, final BitSet set, final boolean b) {
        if (str == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        final ByteBuffer encode = charset.encode(str);
        while (encode.hasRemaining()) {
            final int bitIndex = encode.get() & 0xFF;
            if (set.get(bitIndex)) {
                sb.append((char)bitIndex);
            }
            else if (b && bitIndex == 32) {
                sb.append('+');
            }
            else {
                sb.append("%");
                final char upperCase = Character.toUpperCase(Character.forDigit(bitIndex >> 4 & 0xF, 16));
                final char upperCase2 = Character.toUpperCase(Character.forDigit(bitIndex & 0xF, 16));
                sb.append(upperCase);
                sb.append(upperCase2);
            }
        }
        return sb.toString();
    }
    
    static {
        a = new BitSet(256);
        b = new BitSet(256);
        c = new BitSet(256);
        d = new BitSet(256);
        e = new BitSet(256);
        f = new BitSet(256);
        g = new BitSet(256);
        for (int i = 97; i <= 122; ++i) {
            URLEncodedUtils.a.set(i);
        }
        for (int j = 65; j <= 90; ++j) {
            URLEncodedUtils.a.set(j);
        }
        for (int k = 48; k <= 57; ++k) {
            URLEncodedUtils.a.set(k);
        }
        URLEncodedUtils.a.set(95);
        URLEncodedUtils.a.set(45);
        URLEncodedUtils.a.set(46);
        URLEncodedUtils.a.set(42);
        URLEncodedUtils.g.or(URLEncodedUtils.a);
        URLEncodedUtils.a.set(33);
        URLEncodedUtils.a.set(126);
        URLEncodedUtils.a.set(39);
        URLEncodedUtils.a.set(40);
        URLEncodedUtils.a.set(41);
        URLEncodedUtils.b.set(44);
        URLEncodedUtils.b.set(59);
        URLEncodedUtils.b.set(58);
        URLEncodedUtils.b.set(36);
        URLEncodedUtils.b.set(38);
        URLEncodedUtils.b.set(43);
        URLEncodedUtils.b.set(61);
        URLEncodedUtils.c.or(URLEncodedUtils.a);
        URLEncodedUtils.c.or(URLEncodedUtils.b);
        URLEncodedUtils.d.or(URLEncodedUtils.a);
        URLEncodedUtils.d.set(47);
        URLEncodedUtils.d.set(59);
        URLEncodedUtils.d.set(58);
        URLEncodedUtils.d.set(64);
        URLEncodedUtils.d.set(38);
        URLEncodedUtils.d.set(61);
        URLEncodedUtils.d.set(43);
        URLEncodedUtils.d.set(36);
        URLEncodedUtils.d.set(44);
        URLEncodedUtils.f.set(59);
        URLEncodedUtils.f.set(47);
        URLEncodedUtils.f.set(63);
        URLEncodedUtils.f.set(58);
        URLEncodedUtils.f.set(64);
        URLEncodedUtils.f.set(38);
        URLEncodedUtils.f.set(61);
        URLEncodedUtils.f.set(43);
        URLEncodedUtils.f.set(36);
        URLEncodedUtils.f.set(44);
        URLEncodedUtils.f.set(91);
        URLEncodedUtils.f.set(93);
        URLEncodedUtils.e.or(URLEncodedUtils.f);
        URLEncodedUtils.e.or(URLEncodedUtils.a);
    }
    
    public static class BasicNameValuePair
    {
        public final String mName;
        public final String mValue;
        
        public BasicNameValuePair(final String mName, final String mValue) {
            this.mName = mName;
            this.mValue = mValue;
        }
        
        public String getName() {
            return this.mName;
        }
        
        public String getValue() {
            return this.mValue;
        }
    }
}
