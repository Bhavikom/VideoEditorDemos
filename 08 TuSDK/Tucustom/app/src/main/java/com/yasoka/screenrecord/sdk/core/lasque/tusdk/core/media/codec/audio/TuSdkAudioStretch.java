// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.Arrays;
//import org.lasque.tusdk.core.utils.TLog;
import java.nio.ShortBuffer;

public class TuSdkAudioStretch
{
    private static final TuSdkAudioStretch a;
    private int b;
    private float c;
    private int d;
    private int e;
    private short[] f;
    private short[] g;
    private int h;
    private int i;
    private float[] j;
    private int k;
    
    public static boolean process(final ShortBuffer shortBuffer, final ShortBuffer shortBuffer2, final int n, final float n2) {
        return TuSdkAudioStretch.a.a(shortBuffer, shortBuffer2, n, n2);
    }
    
    private TuSdkAudioStretch() {
        this.k = 1;
    }
    
    private boolean a(final ShortBuffer shortBuffer, final ShortBuffer src, final int i, final float f) {
        if (shortBuffer == null || shortBuffer.limit() < 1 || src == null || src.capacity() < 1 || i < 1 || f <= 0.0f) {
            TLog.e("%s process invalid params: input[%s], output[%s], sampleRate[%d], speedRatio[%f]", "TuSdkAudioStretch", shortBuffer, src, i, f);
            return false;
        }
        src.clear();
        if (f == 1.0f) {
            if (shortBuffer.limit() < src.limit()) {
                src.limit(shortBuffer.limit());
            }
            shortBuffer.put(src);
            return true;
        }
        if (!this.b(shortBuffer, src, i, f)) {
            this.b();
            TLog.w("%s process prepare failed", "TuSdkAudioStretch");
            return false;
        }
        final int n = this.i / this.e;
        this.a(0, 0, 3);
        int n4;
        int n3;
        int n2;
        for (n2 = (n3 = (n4 = 0)); n3 < n && n2 < this.i - this.d; ++n3, n2 += this.e) {
            final int a = this.a(n4, (int)(n2 * this.c));
            if (this.a(a, n2, 1) < this.d) {
                break;
            }
            n4 = a + this.e;
        }
        final int n5 = this.i - n2;
        this.a(this.h - n5, n2, 2);
        this.c(this.h - (n5 - this.e), n2 + this.e);
        src.put(this.g, 0, this.i);
        src.flip();
        return true;
    }
    
    private void a() {
        this.b = 0;
        this.c = -1.0f;
        final int n = 0;
        this.e = n;
        this.d = n;
        final short[] array = null;
        this.g = array;
        this.f = array;
        this.j = null;
        this.k = 5;
    }
    
    private void b() {
        if (this.j != null) {
            this.j = null;
        }
        this.a();
    }
    
    private void c() {
        if (this.f == null || this.f.length < this.h) {
            this.f = new short[this.h];
        }
        if (this.g == null || this.g.length < this.i) {
            this.g = new short[this.i];
        }
        else {
            Arrays.fill(this.g, (short)0);
        }
    }
    
    private float[] a(final int n) {
        final float[] array = new float[n];
        final float n2 = (float)(6.283185307179586 / n);
        for (int i = 0; i < n; ++i) {
            array[i] = 0.5f * (1.0f - (float)Math.cos(i * n2));
        }
        return array;
    }
    
    private boolean b(final ShortBuffer shortBuffer, final ShortBuffer shortBuffer2, final int b, final float c) {
        this.i = (int)Math.ceil(shortBuffer.limit() / c);
        if (this.i % 2 != 0) {
            ++this.i;
        }
        if (shortBuffer2.capacity() < this.i) {
            TLog.e("%s process output buffer length too small: need[%d], out: %s", "TuSdkAudioStretch", this.i, shortBuffer2);
            return false;
        }
        if (b != this.b) {
            this.d = 20 * b / 1000;
            this.e = this.d / 2;
            this.d = this.e * 2;
            this.j = this.a(this.d);
            this.b = b;
        }
        this.c = c;
        this.k = 1;
        this.h = shortBuffer.limit();
        this.c();
        shortBuffer.get(this.f, 0, this.h);
        return true;
    }
    
    private int a(final int n, final int n2, final int n3) {
        int n4 = (n3 == 1) ? this.d : this.e;
        final int n5 = this.h - n;
        final int n6 = this.i - n2;
        if (n4 > n5) {
            n4 = n5;
        }
        if (n4 > n6) {
            n4 = n6;
        }
        if (n4 == 0) {
            return 0;
        }
        final float[] array = (n3 == 3) ? Arrays.copyOfRange(this.j, this.e, this.j.length) : this.j;
        for (int i = 0; i < n4; ++i) {
            final float n7 = array[i];
            final short[] g = this.g;
            final int n8 = n2 + i;
            g[n8] += (short)(this.f[n + i] * n7);
        }
        return n4;
    }
    
    private int a(final int n, final int n2) {
        if (n > this.h - this.d) {
            return n2;
        }
        int n3 = n2 - this.e;
        if (n3 < 0) {
            n3 = 0;
        }
        int n4 = n2 + this.e;
        if (n4 > this.h - this.d) {
            n4 = this.h - this.d;
        }
        if (n3 >= n4) {
            return n2;
        }
        int i = n3;
        int n5 = n2;
        float n6 = -1.0f;
        while (i < n4) {
            final float b = this.b(n, i);
            if (b > n6) {
                n6 = b;
                n5 = i;
            }
            ++i;
        }
        return n5;
    }
    
    private float b(final int n, final int n2) {
        int n3 = n;
        int n4 = n2;
        float n5 = 0.0f;
        for (int i = 0; i < this.d; i += this.k, n3 += this.k, n4 += this.k) {
            n5 += this.f[n3] * this.f[n4];
        }
        return n5;
    }
    
    private int c(final int n, final int n2) {
        final int n3 = this.h - n;
        final int n4 = this.i - n2;
        int n5 = n3;
        if (n5 > n4) {
            n5 = n4;
        }
        if (n5 == 0) {
            return 0;
        }
        for (int i = 0; i < n5; ++i) {
            this.g[n2 + i] = this.f[n + i];
        }
        return n5;
    }
    
    static {
        a = new TuSdkAudioStretch();
    }
}
