// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import android.media.MediaCodec;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.ArrayList;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkBufferCache;
import java.util.List;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkBufferCache;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class TuSdkAudioResampleSoftImpl implements TuSdkAudioResample
{
    private TuSdkAudioInfo a;
    private TuSdkAudioInfo b;
    private final Object c;
    private final List<TuSdkBufferCache> d;
    private TuSdkBufferCache e;
    private boolean f;
    private SampleInfo g;
    private TuSdkAudioResampleSync h;
    private long i;
    private float j;
    private boolean k;
    private TuSdkBufferCache l;
    private boolean m;
    private long n;
    
    public TuSdkAudioResampleSoftImpl(final TuSdkAudioInfo b) {
        this.c = new Object();
        this.d = new ArrayList<TuSdkBufferCache>();
        this.f = false;
        this.j = 1.0f;
        this.k = false;
        this.m = false;
        this.n = -1L;
        if (b == null) {
            throw new NullPointerException(String.format("%s outputInfo is empty.", "TuSdkAudioResampleSoftImpl"));
        }
        this.b = b;
    }
    
    @Override
    public long getLastInputTimeUs() {
        final SampleInfo g = this.g;
        if (g == null) {
            return -1L;
        }
        return g.q;
    }
    
    @Override
    public long getPrefixTimeUs() {
        final SampleInfo g = this.g;
        if (g == null) {
            return -1L;
        }
        return g.n;
    }
    
    @Override
    public void setStartPrefixTimeUs(final long n) {
        this.n = n;
        this.b();
    }

    @Override
    public void setMediaSync(TuSdkAudioResampleSync h) {
        this.h = h;
    }

    @Override
    public void changeFormat(final TuSdkAudioInfo a) {
        if (a == null) {
            TLog.w("%s changeFormat need inputInfo.", "TuSdkAudioResampleSoftImpl");
            return;
        }
        this.a = a;
        this.b();
    }
    
    @Override
    public void changeSpeed(final float j) {
        if (j <= 0.0f || this.j == j) {
            return;
        }
        this.j = j;
        this.b();
    }
    
    @Override
    public void changeSequence(final boolean k) {
        if (this.k == k) {
            return;
        }
        this.k = k;
        this.b();
    }
    
    @Override
    public boolean needResample() {
        return this.f;
    }
    
    @Override
    public void flush() {
        this.a();
    }
    
    @Override
    public void reset() {
        this.j = 1.0f;
        this.k = false;
        this.n = -1L;
        this.b();
    }
    
    private void a() {
        this.i = System.nanoTime();
        synchronized (this.c) {
            this.g = null;
            this.e = null;
            this.d.clear();
        }
    }
    
    private void a(final SampleInfo sampleInfo) {
        if (sampleInfo == null) {
            return;
        }
        synchronized (this.c) {
            this.e = null;
            this.d.clear();
            for (int i = 0; i < sampleInfo.k; ++i) {
                this.d.add(new TuSdkBufferCache(ByteBuffer.allocate(sampleInfo.j).order(ByteOrder.nativeOrder()), new MediaCodec.BufferInfo()));
            }
        }
    }
    
    @Override
    public void release() {
        if (this.m) {
            return;
        }
        this.m = true;
        this.flush();
    }
    
    @Override
    protected void finalize() {
        this.release();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    private void b() {
        this.a();
        if (this.a == null) {
            this.a = this.b.clone();
        }
        if (!(this.f = (this.a.sampleRate != this.b.sampleRate || this.a.channelCount != this.b.channelCount || this.a.bitWidth != this.b.bitWidth || this.j != 1.0f || this.k))) {
            return;
        }
        final SampleInfo g = new SampleInfo();
        g.s = this.i;
        g.l = this.a.sampleRate * this.j / this.b.sampleRate;
        g.t = TuSdkAudioConvertFactory.build(this.a, this.b);
        if (g.t == null) {
            TLog.w("%s unsupport audio format: input - %s, output - %s", "TuSdkAudioResampleSoftImpl");
            return;
        }
        g.b = this.a.bitWidth;
        g.c = this.a.channelCount;
        g.d = this.a.sampleRate;
        g.a = this.a.channelCount * (this.a.bitWidth / 8);
        g.e = 1024 * g.a;
        g.g = this.b.bitWidth;
        g.h = this.b.channelCount;
        g.i = this.b.sampleRate;
        g.f = this.b.channelCount * (this.b.bitWidth / 8);
        g.j = 1024 * g.f;
        g.k = (int)Math.ceil(1.0f / g.l) * 4;
        this.a(g);
        this.g = g;
    }
    
    private TuSdkBufferCache a(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        final TuSdkBufferCache tuSdkBufferCache = new TuSdkBufferCache(byteBuffer, bufferInfo);
        if (!this.k || byteBuffer == null || bufferInfo == null) {
            return tuSdkBufferCache;
        }
        if (this.l == null || this.l.buffer.capacity() < bufferInfo.size) {
            this.l = new TuSdkBufferCache();
            this.l.buffer = ByteBuffer.allocate(bufferInfo.size).order(ByteOrder.nativeOrder());
        }
        this.l.info = TuSdkMediaUtils.cloneBufferInfo(bufferInfo);
        this.l.info.offset = 0;
        if (this.g != null) {
            this.g.t.inputReverse(byteBuffer, this.l.buffer);
        }
        return this.l;
    }
    
    private void b(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.h == null) {
            return;
        }
        this.h.syncAudioResampleOutputBuffer(byteBuffer, bufferInfo);
    }
    
    @Override
    public boolean queueInputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (!SdkValid.shared.audioResampleEffectsSupport()) {
            TLog.e("You are not allowed to use resample effect , please see https://tutucloud.com", new Object[0]);
            return false;
        }
        final TuSdkBufferCache a = this.a(byteBuffer, bufferInfo);
        if (!this.f) {
            this.b(a.buffer, a.info);
            return true;
        }
        final SampleInfo g = this.g;
        return a.buffer == null || a.info == null || a.info.size < 1 || g == null || g.s != this.i || this.a(a.buffer, a.info, g);
    }
    
    private void a(final TuSdkBufferCache tuSdkBufferCache, final SampleInfo sampleInfo) {
        if (sampleInfo.s != this.i) {
            return;
        }
        synchronized (this.c) {
            final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            bufferInfo.size = tuSdkBufferCache.buffer.capacity();
            bufferInfo.offset = 0;
            bufferInfo.flags = tuSdkBufferCache.info.flags;
            bufferInfo.presentationTimeUs = tuSdkBufferCache.info.presentationTimeUs;
            tuSdkBufferCache.buffer.position(0);
            tuSdkBufferCache.buffer.limit(bufferInfo.size);
            this.b(tuSdkBufferCache.buffer, bufferInfo);
            this.d.add(tuSdkBufferCache);
            ++sampleInfo.r;
        }
    }
    
    private TuSdkBufferCache c() {
        TuSdkBufferCache tuSdkBufferCache = null;
        synchronized (this.c) {
            if (this.d.size() > 0) {
                tuSdkBufferCache = this.d.remove(0);
                tuSdkBufferCache.clear();
            }
        }
        return tuSdkBufferCache;
    }
    
    private TuSdkBufferCache b(final SampleInfo sampleInfo) {
        if (sampleInfo.s != this.i) {
            return null;
        }
        final TuSdkBufferCache c = this.c();
        if (c != null) {
            c.info.presentationTimeUs = this.c(sampleInfo);
        }
        return c;
    }
    
    private long c(final SampleInfo sampleInfo) {
        return sampleInfo.r * 1024000000L / sampleInfo.i + sampleInfo.n;
    }
    
    private TuSdkBufferCache d() {
        TuSdkBufferCache e = null;
        synchronized (this.c) {
            e = this.e;
            this.e = null;
        }
        return e;
    }
    
    private void a(final TuSdkBufferCache e) {
        if (e == null) {
            return;
        }
        synchronized (this.c) {
            e.info.size = e.buffer.position();
            this.e = e;
        }
    }
    
    private boolean a(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final SampleInfo sampleInfo) {
        TuSdkBufferCache tuSdkBufferCache = this.d();
        if (tuSdkBufferCache == null) {
            tuSdkBufferCache = this.c();
        }
        if (tuSdkBufferCache == null) {
            TLog.w("%s can not queueInputBuffer, is forgot releaseOutputBuffer?", "TuSdkAudioResampleSoftImpl");
            return false;
        }
        if (!sampleInfo.m) {
            sampleInfo.m = true;
            sampleInfo.r = 0L;
            sampleInfo.n = ((this.n < 0L) ? bufferInfo.presentationTimeUs : this.n);
            sampleInfo.q = sampleInfo.n;
            sampleInfo.o = sampleInfo.n;
            tuSdkBufferCache.clear();
            tuSdkBufferCache.info.presentationTimeUs = sampleInfo.n;
        }
        if (tuSdkBufferCache.info.presentationTimeUs < 0L) {
            tuSdkBufferCache.info.presentationTimeUs = this.c(sampleInfo);
        }
        sampleInfo.p = sampleInfo.q;
        sampleInfo.q = bufferInfo.presentationTimeUs;
        sampleInfo.o += (long)Math.abs((sampleInfo.q - sampleInfo.p) / this.j);
        final long b = this.b(tuSdkBufferCache, sampleInfo);
        if (b < sampleInfo.o) {
            final long n = (sampleInfo.o - b) * sampleInfo.i / 1000000L;
            if (n > 100L) {
                tuSdkBufferCache = this.a(tuSdkBufferCache, sampleInfo, n);
            }
        }
        byteBuffer.position(bufferInfo.offset);
        byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
        this.a(byteBuffer, bufferInfo, sampleInfo, tuSdkBufferCache);
        return true;
    }
    
    private void a(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final SampleInfo sampleInfo, TuSdkBufferCache b) {
        if (b == null) {
            return;
        }
        if (!byteBuffer.hasRemaining()) {
            b.info.flags = bufferInfo.flags;
            if ((bufferInfo.flags & 0x4) != 0x0) {
                this.a(b, sampleInfo);
            }
            else if (b.buffer.hasRemaining()) {
                this.a(b);
            }
            return;
        }
        final int n = byteBuffer.remaining() / sampleInfo.a;
        final int position = byteBuffer.position();
        final int b2 = b.buffer.remaining() / sampleInfo.f;
        int a;
        if (sampleInfo.l < 1.0f) {
            a = (int)Math.floor(n / sampleInfo.l);
        }
        else {
            a = (int)Math.ceil(n / sampleInfo.l);
        }
        final byte[] array = new byte[sampleInfo.a * 2];
        int i = 0;
        final int min = Math.min(a, b2);
        final int n2 = min - 1;
        while (i < min) {
            final float n3 = i * sampleInfo.l;
            final int n4 = (int)Math.floor(n3);
            final int n5 = (int)Math.ceil(n3);
            if (i == n2 || n4 == n5 || n5 == n) {
                if (!this.a(byteBuffer, n4 * sampleInfo.a + position, array, 0, sampleInfo.a)) {
                    return;
                }
                b.buffer.put(sampleInfo.t.outputBytes(array, byteBuffer.order(), 0, sampleInfo.a));
            }
            else {
                final int n6 = n4 * sampleInfo.a + position;
                final int n7 = n5 * sampleInfo.a + position;
                if (!this.a(byteBuffer, n6, array, 0, sampleInfo.a)) {
                    return;
                }
                if (!this.a(byteBuffer, n7, array, sampleInfo.a, sampleInfo.a)) {
                    return;
                }
                b.buffer.put(sampleInfo.t.outputResamle(array, n3 - n4, byteBuffer.order()));
            }
            ++i;
        }
        if (!b.buffer.hasRemaining()) {
            this.a(b, sampleInfo);
            b = this.b(sampleInfo);
            if (b == null) {
                return;
            }
        }
        byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
        this.a(byteBuffer, bufferInfo, sampleInfo, b);
    }
    
    private boolean a(final ByteBuffer byteBuffer, final int n, final byte[] dst, final int offset, final int length) {
        try {
            byteBuffer.position(n);
            byteBuffer.get(dst, offset, length);
        }
        catch (Exception ex) {
            return false;
        }
        return true;
    }
    
    private TuSdkBufferCache a(TuSdkBufferCache b, final SampleInfo sampleInfo, long a) {
        if (a < 1L) {
            return b;
        }
        final int n = (int)Math.min(a, b.buffer.remaining() / sampleInfo.f);
        final byte[] src = new byte[n * sampleInfo.f];
        b.buffer.put(src);
        final MediaCodec.BufferInfo info = b.info;
        info.size += src.length;
        if (!b.buffer.hasRemaining()) {
            this.a(b, sampleInfo);
            b = this.b(sampleInfo);
            if (b == null) {
                return null;
            }
        }
        a -= n;
        return this.a(b, sampleInfo, a);
    }
    
    private long b(final TuSdkBufferCache tuSdkBufferCache, final SampleInfo sampleInfo) {
        return tuSdkBufferCache.buffer.position() / sampleInfo.f * 1000000 / sampleInfo.i + tuSdkBufferCache.info.presentationTimeUs;
    }
    
    private class SampleInfo
    {
        int a;
        int b;
        int c;
        int d;
        int e;
        int f;
        int g;
        int h;
        int i;
        int j;
        int k;
        float l;
        boolean m;
        long n;
        long o;
        long p;
        long q;
        long r;
        long s;
        TuSdkAudioConvert t;
        
        private SampleInfo() {
            this.k = 0;
            this.m = false;
            this.n = -1L;
            this.o = 0L;
            this.p = -1L;
            this.q = -1L;
            this.r = 0L;
        }
    }
}
