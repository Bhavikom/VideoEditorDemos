// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.media.MediaFormat;
import java.io.IOException;
import android.media.MediaCrypto;
import android.view.Surface;
import java.util.Map;
//import org.lasque.tusdk.core.TuSdkContext;
import java.util.LinkedList;
import java.io.File;
//import org.lasque.tusdk.core.utils.TLog;
import java.nio.ByteBuffer;
import android.media.MediaCodec;
import java.util.Queue;
import android.media.MediaExtractor;
import android.net.Uri;
import android.annotation.SuppressLint;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;

@SuppressLint({ "InlinedApi" })
public class TuSDKAudioConverter
{
    private String a;
    private Uri b;
    private MediaExtractor c;
    private Queue<byte[]> d;
    private long e;
    private MediaCodec f;
    private ByteBuffer[] g;
    private ByteBuffer[] h;
    private AudioFormat i;
    private MediaCodec j;
    private ByteBuffer[] k;
    private ByteBuffer[] l;
    private Thread m;
    private boolean n;
    private TuSDKAudioConverterDelegate o;
    private volatile State p;
    private volatile boolean q;
    private long r;
    private long s;
    private long t;
    private long u;
    
    public TuSDKAudioConverter(final String a, final AudioFormat i) {
        this.n = false;
        this.p = State.UnKnow;
        this.q = false;
        this.u = 23219L;
        this.a = a;
        this.i = i;
    }
    
    public TuSDKAudioConverter(final Uri b, final AudioFormat i) {
        this.n = false;
        this.p = State.UnKnow;
        this.q = false;
        this.u = 23219L;
        this.b = b;
        this.i = i;
    }
    
    public static TuSDKAudioConverter create(final String s, final AudioFormat audioFormat) {
        return new TuSDKAudioConverter(s, audioFormat);
    }
    
    public static TuSDKAudioConverter create(final Uri uri, final AudioFormat audioFormat) {
        return new TuSDKAudioConverter(uri, audioFormat);
    }
    
    public long getAudioFrameInterval() {
        return this.u;
    }
    
    public boolean prepare() {
        if (this.i == null || this.i != AudioFormat.AAC) {
            TLog.e("Only supports aac format", new Object[0]);
            return false;
        }
        if (this.a == null && this.b == null) {
            TLog.e("Please set a valid audio path", new Object[0]);
            return false;
        }
        if (this.a != null) {
            if (!new File(this.a).exists()) {
                TLog.e("Please set a valid audio path", new Object[0]);
                return false;
            }
        }
        else if (this.b != null) {}
        this.d = new LinkedList<byte[]>();
        if (!this.a()) {
            return false;
        }
        if (!this.b()) {
            return false;
        }
        this.p = State.Ready;
        return true;
    }
    
    public long getDuraitonTimeUs() {
        return this.e;
    }
    
    public void syncAudioTimeUs(final long n) {
        if (this.isStarted() || this.r > 0L) {
            return;
        }
        this.r = n;
        this.s = n;
    }
    
    public void setLooping(final boolean n) {
        this.n = n;
    }
    
    public void setDelegate(final TuSDKAudioConverterDelegate o) {
        this.o = o;
    }
    
    private boolean a() {
        if (this.c != null || this.f != null) {
            return true;
        }
        try {
            this.c = new MediaExtractor();
            if (this.a != null) {
                this.c.setDataSource(this.a);
            }
            else if (this.b != null) {
                this.c.setDataSource(TuSdkContext.context(), this.b, (Map)null);
            }
            int i = 0;
            while (i < this.c.getTrackCount()) {
                final MediaFormat trackFormat = this.c.getTrackFormat(i);
                final String string = trackFormat.getString("mime");
                if (string.startsWith("audio")) {
                    this.c.selectTrack(i);
                    (this.f = MediaCodec.createDecoderByType(string)).configure(trackFormat, (Surface)null, (MediaCrypto)null, 0);
                    if (trackFormat.containsKey("durationUs")) {
                        this.e = trackFormat.getLong("durationUs");
                        break;
                    }
                    break;
                }
                else {
                    ++i;
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        if (this.f == null) {
            TLog.e("create mediaDecode failed", new Object[0]);
            return false;
        }
        this.f.start();
        this.g = this.f.getInputBuffers();
        this.h = this.f.getOutputBuffers();
        TLog.d("buffers:" + this.g.length, new Object[0]);
        return true;
    }
    
    private boolean b() {
        if (this.j != null) {
            return true;
        }
        try {
            final MediaFormat audioFormat = MediaFormat.createAudioFormat(this.i.getMeimeType(), 44100, 2);
            audioFormat.setInteger("bitrate", 96000);
            audioFormat.setInteger("aac-profile", 2);
            audioFormat.setInteger("max-input-size", 102400);
            audioFormat.setByteBuffer("csd-0", ByteBuffer.wrap(new byte[] { 17, -112 }));
            (this.j = MediaCodec.createEncoderByType(this.i.getMeimeType())).configure(audioFormat, null, null, 1);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        if (this.j == null) {
            TLog.e("create mediaEncoder failed", new Object[0]);
            return false;
        }
        this.j.start();
        MediaCodec.BufferInfo bufferInfo;
        int i;
        for (bufferInfo = new MediaCodec.BufferInfo(), i = 0; i != -2; i = this.j.dequeueOutputBuffer(bufferInfo, 0L)) {}
        if (i == -2) {
            final MediaFormat outputFormat = this.j.getOutputFormat();
            if (outputFormat.containsKey("sample-rate")) {
                this.u = 1024000000 / outputFormat.getInteger("sample-rate");
            }
            if (this.o != null) {
                this.o.onReady(outputFormat);
            }
        }
        this.k = this.j.getInputBuffers();
        this.l = this.j.getOutputBuffers();
        return true;
    }
    
    public boolean isStarted() {
        return this.p == State.Started;
    }
    
    public boolean isStoped() {
        return this.p == State.Stoped;
    }
    
    public void start() {
        if (!ThreadHelper.isMainThread()) {
            this.a(new Runnable() {
                @Override
                public void run() {
                    TuSDKAudioConverter.this.start();
                }
            });
            return;
        }
        if (this.isStarted()) {
            return;
        }
        if (!this.prepare()) {
            this.p = State.UnKnow;
        }
        if (this.p != State.Ready) {
            TLog.e("TuSDKAudioConverter start failed\uff0cPlease check the configuration information", new Object[0]);
            return;
        }
        this.p = State.Started;
        this.q = false;
        (this.m = new Thread(new DecoderRunnable())).start();
    }
    
    private void c() {
        if (this.isStoped()) {
            if (this.o != null) {
                this.o.onDone(this);
            }
            return;
        }
        this.q = true;
        this.p = State.Stoped;
        if (this.m != null && !this.m.isInterrupted()) {
            try {
                this.m.join(100L);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        this.release();
    }
    
    public void stop() {
        if (!ThreadHelper.isMainThread()) {
            this.a(new Runnable() {
                @Override
                public void run() {
                    TuSDKAudioConverter.this.stop();
                }
            });
            return;
        }
        this.q = true;
        this.n = false;
        this.c();
    }
    
    private void a(final Runnable runnable) {
        if (runnable == null) {
            return;
        }
        ThreadHelper.post(runnable);
    }
    
    private void a(final byte[] array) {
        this.d.add(array);
    }
    
    private byte[] d() {
        if (this.isStoped()) {
            return null;
        }
        if (this.d == null || this.d.isEmpty()) {
            return null;
        }
        return this.d.poll();
    }
    
    private void e() {
        if (this.isStoped()) {
            return;
        }
        final long n = 5000L;
        for (int i = 0; i < this.g.length; ++i) {
            final int dequeueInputBuffer = this.f.dequeueInputBuffer(n);
            if (dequeueInputBuffer >= 0) {
                final ByteBuffer byteBuffer = this.g[dequeueInputBuffer];
                byteBuffer.clear();
                final int sampleData = this.c.readSampleData(byteBuffer, 0);
                if (this.s == 0L) {
                    this.s = this.c.getSampleTime();
                }
                if (sampleData < 0) {
                    this.q = true;
                    TLog.d("sampleSize < 0", new Object[0]);
                }
                else {
                    this.f.queueInputBuffer(dequeueInputBuffer, 0, sampleData, this.c.getSampleTime(), 0);
                    this.c.advance();
                    this.t += sampleData;
                }
            }
        }
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        for (int j = this.f.dequeueOutputBuffer(bufferInfo, n); j >= 0; j = this.f.dequeueOutputBuffer(bufferInfo, n)) {
            final ByteBuffer byteBuffer2 = this.h[j];
            final byte[] dst = new byte[bufferInfo.size];
            byteBuffer2.get(dst);
            byteBuffer2.clear();
            this.a(dst);
            this.f.releaseOutputBuffer(j, false);
        }
    }
    
    private void f() {
        if (this.isStoped()) {
            return;
        }
        final long n = 5000L;
        for (int i = 0; i < this.k.length; ++i) {
            final byte[] d = this.d();
            if (d == null) {
                break;
            }
            final int dequeueInputBuffer = this.j.dequeueInputBuffer(n);
            if (dequeueInputBuffer >= 0) {
                final ByteBuffer byteBuffer = this.k[dequeueInputBuffer];
                byteBuffer.clear();
                byteBuffer.limit(d.length);
                byteBuffer.put(d);
                this.j.queueInputBuffer(dequeueInputBuffer, 0, d.length, 0L, 0);
            }
        }
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        for (int j = this.j.dequeueOutputBuffer(bufferInfo, n); j >= 0; j = this.j.dequeueOutputBuffer(bufferInfo, n)) {
            final int size = bufferInfo.size;
            final ByteBuffer byteBuffer2 = this.l[j];
            byteBuffer2.position(bufferInfo.offset);
            byteBuffer2.limit(bufferInfo.offset + size);
            bufferInfo.flags = 1;
            if (this.s == 0L) {
                this.s = bufferInfo.presentationTimeUs;
            }
            else {
                bufferInfo.presentationTimeUs = this.s + this.u;
            }
            if (bufferInfo.presentationTimeUs > this.s) {
                this.s = bufferInfo.presentationTimeUs;
                if (this.r == 0L) {
                    this.r = this.s;
                }
                if (this.o != null) {
                    this.o.onNewSampleData(bufferInfo.presentationTimeUs, byteBuffer2.duplicate(), bufferInfo);
                    this.o.onProgressChanged((this.s - this.r) / 1000L / 1000L);
                }
            }
            this.j.releaseOutputBuffer(j, false);
            bufferInfo = new MediaCodec.BufferInfo();
        }
    }
    
    public void release() {
        if (this.c != null) {
            this.c.release();
            this.c = null;
        }
        if (this.j != null) {
            this.j.stop();
            this.j.release();
            this.j = null;
        }
        if (this.f != null) {
            this.f.stop();
            this.f.release();
            this.f = null;
        }
        if (this.d != null) {
            this.d.clear();
            this.d = null;
        }
        this.g = null;
        this.k = null;
    }
    
    private void g() {
        if (!this.isStarted()) {
            return;
        }
        this.c();
        if (this.n) {
            this.start();
        }
        else if (this.o != null) {
            this.o.onDone(this);
        }
    }
    
    private class DecoderRunnable implements Runnable
    {
        @Override
        public void run() {
            final long currentTimeMillis = System.currentTimeMillis();
            while (!TuSDKAudioConverter.this.q) {
                TuSDKAudioConverter.this.e();
                while (!TuSDKAudioConverter.this.isStoped() && TuSDKAudioConverter.this.d.size() > 0) {
                    TuSDKAudioConverter.this.f();
                }
            }
            TuSDKAudioConverter.this.a(new Runnable() {
                @Override
                public void run() {
                    TuSDKAudioConverter.this.g();
                }
            });
            TLog.d("DecodeSize:" + TuSDKAudioConverter.this.t + "time:" + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
        }
    }
    
    public enum AudioFormat
    {
        AAC("audio/mp4a-latm");
        
        private String a;
        
        private AudioFormat(final String a) {
            this.a = a;
        }
        
        public String getMeimeType() {
            return this.a;
        }
    }
    
    public interface TuSDKAudioConverterDelegate
    {
        void onReady(final MediaFormat p0);
        
        void onNewSampleData(final long p0, final ByteBuffer p1, final MediaCodec.BufferInfo p2);
        
        void onDone(final TuSDKAudioConverter p0);
        
        boolean onProgressChanged(final long p0);
    }
    
    public enum State
    {
        UnKnow, 
        Ready, 
        Started, 
        Stoped;
    }
}
