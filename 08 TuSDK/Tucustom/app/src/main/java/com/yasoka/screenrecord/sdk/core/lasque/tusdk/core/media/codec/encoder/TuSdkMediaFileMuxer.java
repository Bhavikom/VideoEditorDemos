// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import java.io.IOException;
import android.os.Build;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.HashMap;
import java.util.Map;
//import org.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;
import java.io.FileDescriptor;
import android.media.MediaMuxer;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;

@TargetApi(18)
public class TuSdkMediaFileMuxer implements TuSdkMediaMuxer
{
    private Thread a;
    private Thread b;
    private boolean c;
    private MediaMuxer d;
    private String e;
    private FileDescriptor f;
    private int g;
    private TuSdkEncodecOperation h;
    private TuSdkEncodecOperation i;
    private int j;
    private int k;
    private int l;
    private final Map<Integer, Long> m;
    
    public TuSdkMediaFileMuxer() {
        this.g = 0;
        this.j = 0;
        this.k = 0;
        this.l = -1;
        this.m = new HashMap<Integer, Long>();
    }
    
    public TuSdkMediaFileMuxer setMediaMuxerFormat(final int g) {
        this.g = g;
        return this;
    }
    
    public TuSdkMediaFileMuxer setPath(final String e) {
        this.e = e;
        return this;
    }
    
    @TargetApi(26)
    public TuSdkMediaFileMuxer setFileDescriptor(final FileDescriptor f) {
        this.f = f;
        return this;
    }
    
    public void setVideoOperation(final TuSdkEncodecOperation h) {
        if (this.d != null) {
            TLog.w("%s setVideoOperation can not after prepare", "TuSdkMediaFileMuxer");
            return;
        }
        this.h = h;
    }
    
    public void setAudioOperation(final TuSdkEncodecOperation i) {
        if (this.d != null) {
            TLog.w("%s setAudioOperation can not after prepare", "TuSdkMediaFileMuxer");
            return;
        }
        this.i = i;
    }
    
    public void pause() {
        this.c = false;
    }
    
    public TuSdkMediaFileMuxer resume() {
        this.c = true;
        return this;
    }
    
    public boolean isWorking() {
        return this.l == 1;
    }
    
    public boolean prepare() {
        if (this.l > -1) {
            TLog.w("%s prepare can not after initialized", "TuSdkMediaFileMuxer");
            return false;
        }
        if (this.h == null && this.i == null) {
            TLog.w("%s prepare need setVideoOperation or setAudioOperation first", "TuSdkMediaFileMuxer");
            return false;
        }
        if (this.h == null || this.i == null) {
            this.k = 1;
        }
        else {
            this.k = 2;
        }
        try {
            if (this.e != null) {
                this.d = new MediaMuxer(this.e, this.g);
            }
            else {
                if (Build.VERSION.SDK_INT < 26 || this.f == null) {
                    throw new ExceptionInInitializerError("MediaMuxer create need setPath or setFileDescriptor");
                }
                this.d = new MediaMuxer(this.f, this.g);
            }
        }
        catch (IOException ex) {
            TLog.e(ex, "%s prepare need setPath or setFileDescriptor first", "TuSdkMediaFileMuxer");
            this.d = null;
            return false;
        }
        this.l = 0;
        if (this.h != null) {
            (this.a = new MediaVideoThread()).start();
        }
        if (this.i != null) {
            (this.b = new MediaAudioThread()).start();
        }
        return this.c = true;
    }
    
    public void release() {
        this.pause();
        this.l = 2;
        if (this.a != null && !this.a.isInterrupted()) {
            this.a.interrupt();
        }
        if (this.b != null && !this.b.isInterrupted()) {
            this.b.interrupt();
        }
        if (this.d != null) {
            try {
                this.d.release();
            }
            catch (Exception ex) {}
            this.d = null;
        }
        this.a = null;
        this.b = null;
        this.h = null;
        this.i = null;
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
    
    private void a() {
        this.a(this.d, this.h);
    }
    
    private void b() {
        this.a(this.d, this.i);
    }
    
    private void a(final MediaMuxer mediaMuxer, final TuSdkEncodecOperation tuSdkEncodecOperation) {
        try {
            if (mediaMuxer == null || tuSdkEncodecOperation == null || !tuSdkEncodecOperation.encodecInit(this)) {
                tuSdkEncodecOperation.encodecException(new Exception(String.format("%s encodec Init failed", "TuSdkMediaFileMuxer")));
                return;
            }
        }
        catch (Exception ex) {
            tuSdkEncodecOperation.encodecException(ex);
            return;
        }
        while (!ThreadHelper.interrupted() && this.l != 2) {
            if (!this.c) {
                continue;
            }
            try {
                if (!tuSdkEncodecOperation.encodecProcessUntilEnd(this)) {
                    continue;
                }
            }
            catch (Exception ex2) {
                tuSdkEncodecOperation.encodecException(ex2);
            }
            break;
        }
        tuSdkEncodecOperation.encodecRelease();
    }
    
    @Override
    public int addTrack(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            TLog.w("%s addTrack need format not empty", "TuSdkMediaFileMuxer");
            return -1;
        }
        if (this.l != 0 || this.d == null) {
            TLog.w("%s addTrack need after prepare, before MediaMuxer start: %s", "TuSdkMediaFileMuxer", this.l);
            return -1;
        }
        final int addTrack = this.d.addTrack(mediaFormat);
        if (addTrack > -1) {
            this.m.put(addTrack, -1L);
            ++this.j;
        }
        if (this.k == this.j) {
            this.d.start();
            this.l = 1;
            TLog.d("%s addTrack MediaMuxer started", "TuSdkMediaFileMuxer");
        }
        return addTrack;
    }
    
    private boolean a(final int n, final long n2) {
        if (!this.m.containsKey(n)) {
            return false;
        }
        final long longValue = this.m.get(n);
        if (n2 < longValue) {
            TLog.w("%s skip out of order frames (timestamp: %d < last: %d for track[%d]", "TuSdkMediaFileMuxer", n2, longValue, n);
            return false;
        }
        this.m.put(n, n2);
        return true;
    }
    
    @Override
    public void writeSampleData(final int i, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        final MediaMuxer d = this.d;
        if (d == null || bufferInfo == null) {
            return;
        }
        while (!ThreadHelper.isInterrupted() && this.l < 1) {}
        if (this.l == 1 && this.a(i, bufferInfo.presentationTimeUs)) {
            d.writeSampleData(i, byteBuffer, bufferInfo);
        }
        if (TLog.LOG_MEDIA_MUXER_INFO) {
            TuSdkCodecCapabilities.logBufferInfo(String.format("%s[Track: %d]", "TuSdkMediaFileMuxer", i), bufferInfo);
        }
    }
    
    @TargetApi(19)
    public void setLocation(final float n, final float n2) {
        if (this.l != 0 || this.d == null) {
            TLog.w("%s setLocation need after prepare, before MediaMuxer start: %s", "TuSdkMediaFileMuxer", this.l);
            return;
        }
        this.d.setLocation(n, n2);
    }
    
    public void setOrientationHint(final int orientationHint) {
        if (this.l != 0 || this.d == null) {
            TLog.w("%s setOrientationHint need after prepare, before MediaMuxer start: %s", "TuSdkMediaFileMuxer", this.l);
            return;
        }
        this.d.setOrientationHint(orientationHint);
    }
    
    private class MediaAudioThread extends Thread
    {
        @Override
        public void run() {
            TuSdkMediaFileMuxer.this.b();
        }
    }
    
    private class MediaVideoThread extends Thread
    {
        @Override
        public void run() {
            TuSdkMediaFileMuxer.this.a();
        }
    }
}
