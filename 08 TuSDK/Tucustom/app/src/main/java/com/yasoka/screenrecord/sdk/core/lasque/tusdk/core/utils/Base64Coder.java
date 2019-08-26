// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import java.io.UnsupportedEncodingException;
import android.annotation.SuppressLint;

@SuppressLint({ "Assert" })
public class Base64Coder
{
    static final /* synthetic */ boolean a;
    
    public static String encodeToString(final byte[] array, final int n) {
        try {
            return new String(encode(array, n), "US-ASCII");
        }
        catch (UnsupportedEncodingException detailMessage) {
            throw new AssertionError((Object)detailMessage);
        }
    }
    
    public static String encodeToString(final byte[] array, final int n, final int n2, final int n3) {
        try {
            return new String(encode(array, n, n2, n3), "US-ASCII");
        }
        catch (UnsupportedEncodingException detailMessage) {
            throw new AssertionError((Object)detailMessage);
        }
    }
    
    public static byte[] encode(final byte[] array, final int n) {
        return encode(array, 0, array.length, n);
    }
    
    public static byte[] encode(final byte[] array, final int n, final int n2, final int n3) {
        final Encoder encoder = new Encoder(n3, null);
        int n4 = n2 / 3 * 4;
        if (encoder.do_padding) {
            if (n2 % 3 > 0) {
                n4 += 4;
            }
        }
        else {
            switch (n2 % 3) {
                case 1: {
                    n4 += 2;
                    break;
                }
                case 2: {
                    n4 += 3;
                    break;
                }
            }
        }
        if (encoder.do_newline && n2 > 0) {
            n4 += ((n2 - 1) / 57 + 1) * (encoder.do_cr ? 2 : 1);
        }
        encoder.output = new byte[n4];
        encoder.process(array, n, n2, true);
        if (!Base64Coder.a && encoder.op != n4) {
            throw new AssertionError();
        }
        return encoder.output;
    }
    
    static {
        a = !Base64Coder.class.desiredAssertionStatus();
    }
    
    public static class Encoder extends Coder
    {
        public static final int LINE_GROUPS = 19;
        private static final byte[] c;
        private static final byte[] d;
        private final byte[] e;
        int a;
        private int f;
        public final boolean do_padding;
        public final boolean do_newline;
        public final boolean do_cr;
        private final byte[] g;
        static final /* synthetic */ boolean b;
        
        public Encoder(final int n, final byte[] output) {
            this.output = output;
            this.do_padding = ((n & 0x1) == 0x0);
            this.do_newline = ((n & 0x2) == 0x0);
            this.do_cr = ((n & 0x4) != 0x0);
            this.g = (((n & 0x8) == 0x0) ? Encoder.c : Encoder.d);
            this.e = new byte[2];
            this.a = 0;
            this.f = (this.do_newline ? 19 : -1);
        }
        
        @Override
        public int maxOutputSize(final int n) {
            return n * 8 / 5 + 10;
        }
        
        @Override
        public boolean process(final byte[] array, final int n, int n2, final boolean b) {
            final byte[] g = this.g;
            final byte[] output = this.output;
            int op = 0;
            int f = this.f;
            int n3 = n;
            n2 += n;
            int n4 = -1;
            switch (this.a) {
                case 1: {
                    if (n3 + 2 <= n2) {
                        n4 = ((this.e[0] & 0xFF) << 16 | (array[n3++] & 0xFF) << 8 | (array[n3++] & 0xFF));
                        this.a = 0;
                        break;
                    }
                    break;
                }
                case 2: {
                    if (n3 + 1 <= n2) {
                        n4 = ((this.e[0] & 0xFF) << 16 | (this.e[1] & 0xFF) << 8 | (array[n3++] & 0xFF));
                        this.a = 0;
                        break;
                    }
                    break;
                }
            }
            if (n4 != -1) {
                output[op++] = g[n4 >> 18 & 0x3F];
                output[op++] = g[n4 >> 12 & 0x3F];
                output[op++] = g[n4 >> 6 & 0x3F];
                output[op++] = g[n4 & 0x3F];
                if (--f == 0) {
                    if (this.do_cr) {
                        output[op++] = 13;
                    }
                    output[op++] = 10;
                    f = 19;
                }
            }
            while (n3 + 3 <= n2) {
                final int n5 = (array[n3] & 0xFF) << 16 | (array[n3 + 1] & 0xFF) << 8 | (array[n3 + 2] & 0xFF);
                output[op] = g[n5 >> 18 & 0x3F];
                output[op + 1] = g[n5 >> 12 & 0x3F];
                output[op + 2] = g[n5 >> 6 & 0x3F];
                output[op + 3] = g[n5 & 0x3F];
                n3 += 3;
                op += 4;
                if (--f == 0) {
                    if (this.do_cr) {
                        output[op++] = 13;
                    }
                    output[op++] = 10;
                    f = 19;
                }
            }
            if (b) {
                if (n3 - this.a == n2 - 1) {
                    int n6 = 0;
                    final int n7 = (((this.a > 0) ? this.e[n6++] : array[n3++]) & 0xFF) << 4;
                    this.a -= n6;
                    output[op++] = g[n7 >> 6 & 0x3F];
                    output[op++] = g[n7 & 0x3F];
                    if (this.do_padding) {
                        output[op++] = 61;
                        output[op++] = 61;
                    }
                    if (this.do_newline) {
                        if (this.do_cr) {
                            output[op++] = 13;
                        }
                        output[op++] = 10;
                    }
                }
                else if (n3 - this.a == n2 - 2) {
                    int n8 = 0;
                    final int n9 = (((this.a > 1) ? this.e[n8++] : array[n3++]) & 0xFF) << 10 | (((this.a > 0) ? this.e[n8++] : array[n3++]) & 0xFF) << 2;
                    this.a -= n8;
                    output[op++] = g[n9 >> 12 & 0x3F];
                    output[op++] = g[n9 >> 6 & 0x3F];
                    output[op++] = g[n9 & 0x3F];
                    if (this.do_padding) {
                        output[op++] = 61;
                    }
                    if (this.do_newline) {
                        if (this.do_cr) {
                            output[op++] = 13;
                        }
                        output[op++] = 10;
                    }
                }
                else if (this.do_newline && op > 0 && f != 19) {
                    if (this.do_cr) {
                        output[op++] = 13;
                    }
                    output[op++] = 10;
                }
                if (!Encoder.b && this.a != 0) {
                    throw new AssertionError();
                }
                if (!Encoder.b && n3 != n2) {
                    throw new AssertionError();
                }
            }
            else if (n3 == n2 - 1) {
                this.e[this.a++] = array[n3];
            }
            else if (n3 == n2 - 2) {
                this.e[this.a++] = array[n3];
                this.e[this.a++] = array[n3 + 1];
            }
            this.op = op;
            this.f = f;
            return true;
        }
        
        static {
            b = !Base64Coder.class.desiredAssertionStatus();
            c = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
            d = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
        }
    }
    
    public static class Decoder extends Coder
    {
        private static final int[] a;
        private static final int[] b;
        private int c;
        private int d;
        private final int[] e;
        
        public Decoder(final int n, final byte[] output) {
            this.output = output;
            this.e = (((n & 0x8) == 0x0) ? Decoder.a : Decoder.b);
            this.c = 0;
            this.d = 0;
        }
        
        @Override
        public int maxOutputSize(final int n) {
            return n * 3 / 4 + 10;
        }
        
        @Override
        public boolean process(final byte[] array, final int n, int n2, final boolean b) {
            if (this.c == 6) {
                return false;
            }
            int i = n;
            n2 += n;
            int c = this.c;
            int d = this.d;
            int n3 = 0;
            final byte[] output = this.output;
            final int[] e = this.e;
            while (i < n2) {
                if (c == 0) {
                    while (i + 4 <= n2 && (d = (e[array[i] & 0xFF] << 18 | e[array[i + 1] & 0xFF] << 12 | e[array[i + 2] & 0xFF] << 6 | e[array[i + 3] & 0xFF])) >= 0) {
                        output[n3 + 2] = (byte)d;
                        output[n3 + 1] = (byte)(d >> 8);
                        output[n3] = (byte)(d >> 16);
                        n3 += 3;
                        i += 4;
                    }
                    if (i >= n2) {
                        break;
                    }
                }
                final int n4 = e[array[i++] & 0xFF];
                switch (c) {
                    case 0: {
                        if (n4 >= 0) {
                            d = n4;
                            ++c;
                            continue;
                        }
                        if (n4 != -1) {
                            this.c = 6;
                            return false;
                        }
                        continue;
                    }
                    case 1: {
                        if (n4 >= 0) {
                            d = (d << 6 | n4);
                            ++c;
                            continue;
                        }
                        if (n4 != -1) {
                            this.c = 6;
                            return false;
                        }
                        continue;
                    }
                    case 2: {
                        if (n4 >= 0) {
                            d = (d << 6 | n4);
                            ++c;
                            continue;
                        }
                        if (n4 == -2) {
                            output[n3++] = (byte)(d >> 4);
                            c = 4;
                            continue;
                        }
                        if (n4 != -1) {
                            this.c = 6;
                            return false;
                        }
                        continue;
                    }
                    case 3: {
                        if (n4 >= 0) {
                            d = (d << 6 | n4);
                            output[n3 + 2] = (byte)d;
                            output[n3 + 1] = (byte)(d >> 8);
                            output[n3] = (byte)(d >> 16);
                            n3 += 3;
                            c = 0;
                            continue;
                        }
                        if (n4 == -2) {
                            output[n3 + 1] = (byte)(d >> 2);
                            output[n3] = (byte)(d >> 10);
                            n3 += 2;
                            c = 5;
                            continue;
                        }
                        if (n4 != -1) {
                            this.c = 6;
                            return false;
                        }
                        continue;
                    }
                    case 4: {
                        if (n4 == -2) {
                            ++c;
                            continue;
                        }
                        if (n4 != -1) {
                            this.c = 6;
                            return false;
                        }
                        continue;
                    }
                    case 5: {
                        if (n4 != -1) {
                            this.c = 6;
                            return false;
                        }
                        continue;
                    }
                }
            }
            if (!b) {
                this.c = c;
                this.d = d;
                this.op = n3;
                return true;
            }
            switch (c) {
                case 1: {
                    this.c = 6;
                    return false;
                }
                case 2: {
                    output[n3++] = (byte)(d >> 4);
                    break;
                }
                case 3: {
                    output[n3++] = (byte)(d >> 10);
                    output[n3++] = (byte)(d >> 2);
                    break;
                }
                case 4: {
                    this.c = 6;
                    return false;
                }
            }
            this.c = c;
            this.op = n3;
            return true;
        }
        
        static {
            a = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
            b = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
        }
    }
    
    public abstract static class Coder
    {
        public byte[] output;
        public int op;
        
        public abstract boolean process(final byte[] p0, final int p1, final int p2, final boolean p3);
        
        public abstract int maxOutputSize(final int p0);
    }
}
