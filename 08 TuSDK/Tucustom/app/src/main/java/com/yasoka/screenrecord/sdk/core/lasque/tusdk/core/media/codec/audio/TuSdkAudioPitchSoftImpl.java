// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

//import org.lasque.tusdk.core.utils.ByteUtils;
import android.media.MediaCodec;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.ArrayList;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
import java.nio.ShortBuffer;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkBufferCache;
import java.util.List;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkBufferCache;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ByteUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class TuSdkAudioPitchSoftImpl implements TuSdkAudioPitch
{
    private TuSdkAudioInfo a;
    private TuSdkAudioInfo b;
    private final Object c;
    private final List<TuSdkBufferCache> d;
    private TuSdkBufferCache e;
    private ShortBuffer f;
    private ShortBuffer g;
    private SampleInfo h;
    private TuSdkAudioPitchSync i;
    private long j;
    private float k;
    private float l;
    private boolean m;
    private boolean n;
    
    public TuSdkAudioPitchSoftImpl(final TuSdkAudioInfo a) {
        this.c = new Object();
        this.d = new ArrayList<TuSdkBufferCache>();
        this.k = 1.0f;
        this.l = 1.0f;
        this.m = false;
        this.n = false;
        if (a == null) {
            throw new NullPointerException(String.format("%s inputInfo is empty.", "TuSdkAudioPitchSoftImpl"));
        }
        this.a = a;
    }
    
    @Override
    public void release() {
        if (this.n) {
            return;
        }
        this.n = true;
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
    
    @Override
    public void setMediaSync(final TuSdkAudioPitchSync i) {
        this.i = i;
    }
    
    @Override
    public void changeFormat(final TuSdkAudioInfo a) {
        if (a == null) {
            TLog.w("%s changeFormat need inputInfo.", "TuSdkAudioPitchSoftImpl");
            return;
        }
        this.a = a;
        this.b();
    }
    
    @Override
    public void changePitch(final float l) {
        if (!SdkValid.shared.audioPitchEffectsSupport()) {
            TLog.e("You are not allowed to use audio pitch effect , please see https://tutucloud.com", new Object[0]);
            return;
        }
        if (l <= 0.0f || this.l == l) {
            return;
        }
        this.l = l;
        this.k = 1.0f;
        this.b();
    }
    
    @Override
    public void changeSpeed(final float k) {
        if (k <= 0.0f || this.k == k) {
            return;
        }
        this.k = k;
        this.l = 1.0f;
        this.b();
    }
    
    @Override
    public boolean needPitch() {
        return this.m;
    }
    
    @Override
    public void reset() {
        this.l = 1.0f;
        this.k = 1.0f;
        this.b();
    }
    
    @Override
    public void flush() {
        this.a();
    }
    
    private void a() {
        this.j = System.nanoTime();
        synchronized (this.c) {
            this.h = null;
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
            this.f = ByteBuffer.allocate(sampleInfo.j * 1024 * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
            this.g = ByteBuffer.allocate(this.f.capacity() * sampleInfo.j * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
            this.d.clear();
            for (int i = 0; i < sampleInfo.c; ++i) {
                this.d.add(new TuSdkBufferCache(ByteBuffer.allocate(sampleInfo.b).order(ByteOrder.nativeOrder()), new MediaCodec.BufferInfo()));
            }
        }
    }
    
    private void b() {
        this.a();
        if (!(this.m = (this.k != 1.0f || this.l != 1.0f))) {
            return;
        }
        final SampleInfo h = new SampleInfo();
        h.h = this.j;
        h.k = this.a(this.l, this.k);
        h.d = this.l * this.k;
        this.b = this.a.clone();
        this.b.bitWidth = 16;
        this.b.channelCount = 1;
        final TuSdkAudioInfo b = this.b;
        b.sampleRate /= (int)h.d;
        h.i = TuSdkAudioConvertFactory.build(this.a, this.b);
        if (h.i == null) {
            TLog.w("%s unsupport audio format: input - %s, output - %s", "TuSdkAudioPitchSoftImpl", this.a, this.b);
            return;
        }
        h.a = this.a.sampleRate;
        h.b = 1024 * (this.a.channelCount * (this.a.bitWidth / 8));
        h.j = (int)Math.ceil((h.d < 1.0f) ? ((double)(1.0f / h.d)) : ((double)h.d));
        h.c = h.j * 4;
        this.a(h);
        this.h = h;
    }
    
    private void a(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.i == null) {
            return;
        }
        this.i.syncAudioPitchOutputBuffer(byteBuffer, bufferInfo);
    }
    
    @Override
    public boolean queueInputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (!this.m) {
            this.a(byteBuffer, bufferInfo);
            return true;
        }
        final SampleInfo h = this.h;
        if (byteBuffer == null || bufferInfo == null || bufferInfo.size < 1 || h == null || h.h != this.j) {
            return true;
        }
        if (!h.e) {
            h.e = true;
            h.g = 0L;
            h.f = bufferInfo.presentationTimeUs;
        }
        byteBuffer.position(bufferInfo.offset);
        byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
        return this.a(byteBuffer, bufferInfo, h);
    }
    
    private void a(final TuSdkBufferCache tuSdkBufferCache, final SampleInfo sampleInfo) {
        if (sampleInfo.h != this.j) {
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
            this.a(tuSdkBufferCache.buffer, bufferInfo);
            this.d.add(tuSdkBufferCache);
            ++sampleInfo.g;
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
        if (sampleInfo.h != this.j) {
            return null;
        }
        final TuSdkBufferCache c = this.c();
        if (c != null) {
            c.info.presentationTimeUs = this.c(sampleInfo);
        }
        return c;
    }
    
    private long c(final SampleInfo sampleInfo) {
        return sampleInfo.g * 1024000000L / sampleInfo.a + sampleInfo.f;
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
        if (!byteBuffer.hasRemaining()) {
            return true;
        }
        sampleInfo.i.outputShorts(byteBuffer, this.f, ByteOrder.nativeOrder());
        boolean b = false;
        if (!byteBuffer.hasRemaining()) {
            b = ((bufferInfo.flags & 0x4) != 0x0);
        }
        if (!this.f.hasRemaining() || b) {
            this.f.flip();
            if (!this.a(sampleInfo.k.a(this.f, this.g, sampleInfo.a, sampleInfo.d), bufferInfo, sampleInfo, b)) {
                return true;
            }
        }
        return this.a(byteBuffer, bufferInfo, sampleInfo);
    }
    
    private boolean a(final ShortBuffer shortBuffer, final MediaCodec.BufferInfo bufferInfo, final SampleInfo sampleInfo, final boolean b) {
        TuSdkBufferCache tuSdkBufferCache = this.d();
        if (tuSdkBufferCache == null) {
            tuSdkBufferCache = this.b(sampleInfo);
        }
        if (tuSdkBufferCache == null) {
            TLog.w("%s can not queueInputBuffer, is forgot releaseOutputBuffer?", "TuSdkAudioPitchSoftImpl");
            return false;
        }
        shortBuffer.position(0);
        while (shortBuffer.hasRemaining()) {
            sampleInfo.i.restoreBytes(shortBuffer, tuSdkBufferCache.buffer, ByteOrder.nativeOrder());
            if (tuSdkBufferCache.buffer.hasRemaining()) {
                if (shortBuffer.hasRemaining()) {
                    TLog.e("%s convertToOutput count error: input[%s], output[%s]", "TuSdkAudioPitchSoftImpl", shortBuffer, tuSdkBufferCache.buffer);
                    break;
                }
                break;
            }
            else {
                if (!shortBuffer.hasRemaining()) {
                    break;
                }
                this.a(tuSdkBufferCache, sampleInfo);
                tuSdkBufferCache = this.b(sampleInfo);
                if (tuSdkBufferCache == null) {
                    return true;
                }
                continue;
            }
        }
        shortBuffer.clear();
        if (b || !tuSdkBufferCache.buffer.hasRemaining()) {
            tuSdkBufferCache.info.flags = bufferInfo.flags;
            this.a(tuSdkBufferCache, sampleInfo);
            return true;
        }
        this.a(tuSdkBufferCache);
        return true;
    }
    
    private TuSdkAudioPitchCalc a(final float n, final float n2) {
        if (n2 != 1.0f) {
            return new TuSdkAudioPitchSpeed();
        }
        if (n > 1.0f) {
            return new TuSdkAudioPitchUp();
        }
        if (n < 1.0f) {
            return new TuSdkAudioPitchDown();
        }
        return null;
    }
    
    private abstract static class TuSdkAudioPitchCalc
    {
        abstract ShortBuffer a(final ShortBuffer p0, final ShortBuffer p1, final int p2, final float p3);
        
        static void a(final ShortBuffer shortBuffer, final ShortBuffer shortBuffer2, final float n) {
            shortBuffer.position(0);
            final int remaining = shortBuffer.remaining();
            final int remaining2 = shortBuffer2.remaining();
            int a;
            if (n < 1.0f) {
                a = (int)Math.floor(remaining / n);
            }
            else {
                a = (int)Math.ceil(remaining / n);
            }
            int i = 0;
            final int min = Math.min(a, remaining2);
            final int n2 = min - 1;
            while (i < min) {
                final float n3 = i * n;
                final int n4 = (int)Math.floor(n3);
                final int n5 = (int)Math.ceil(n3);
                if (i == n2 || n4 == n5 || n5 == remaining) {
                    shortBuffer2.put(shortBuffer.get(n4));
                }
                else {
                    final short value = shortBuffer.get(n4);
                    shortBuffer2.put(ByteUtils.safeShort((int)(value + (shortBuffer.get(n5) - value) * (n3 - n4))));
                }
                ++i;
            }
            shortBuffer.clear();
            shortBuffer2.flip();
        }
    }
    
    private static class TuSdkAudioPitchSpeed extends TuSdkAudioPitchCalc
    {
        @Override
        ShortBuffer a(final ShortBuffer shortBuffer, final ShortBuffer shortBuffer2, final int n, final float n2) {
            TuSdkAudioStretch.process(shortBuffer, shortBuffer2, n, n2);
            shortBuffer.clear();
            return shortBuffer2;
        }
    }
    
    private static class TuSdkAudioPitchUp extends TuSdkAudioPitchCalc
    {
        @Override
        ShortBuffer a(final ShortBuffer shortBuffer, final ShortBuffer shortBuffer2, final int n, final float n2) {
            TuSdkAudioStretch.process(shortBuffer, shortBuffer2, n, 1.0f / n2);
            shortBuffer.clear();
            TuSdkAudioPitchCalc.a(shortBuffer2, shortBuffer, n2);
            return shortBuffer;
        }
    }
    
    private static class TuSdkAudioPitchDown extends TuSdkAudioPitchCalc
    {
        @Override
        ShortBuffer a(final ShortBuffer shortBuffer, final ShortBuffer shortBuffer2, final int n, final float n2) {
            TuSdkAudioPitchCalc.a(shortBuffer, shortBuffer2, n2);
            TuSdkAudioStretch.process(shortBuffer2, shortBuffer, n, 1.0f / n2);
            shortBuffer2.clear();
            return shortBuffer;
        }
    }
    
    private class SampleInfo
    {
        int a;
        int b;
        int c;
        float d;
        boolean e;
        long f;
        long g;
        long h;
        TuSdkAudioConvert i;
        int j;
        TuSdkAudioPitchCalc k;
        
        private SampleInfo() {
            this.c = 0;
            this.e = false;
            this.f = -1L;
            this.g = 0L;
        }
    }
}
