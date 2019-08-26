// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.audio;

//import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSyncBase;
import java.util.Iterator;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.utils.image.AlbumHelper;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackImpl;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
//import org.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSync;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioRecord;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrack;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkMicRecord;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkAudioEncoder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkEncoderListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileMuxer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSyncBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.AlbumHelper;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioEngine;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncoderListener;
//import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioEngine;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrack;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioRecord;
import java.util.LinkedList;
//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
//import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine;
import java.io.File;
//import org.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileMuxer;
//import org.lasque.tusdk.core.media.codec.encoder.TuSdkAudioEncoder;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkMicRecord;

public class TuSdkAudioRecorder
{
    public static final int UNINITIALIZED_RECORD = 0;
    public static final int INIT_RECORD = 1;
    public static final int START_RECORD = 2;
    public static final int RESUME_RECORD = 3;
    public static final int PAUSE_RECORD = 4;
    public static final int STOP_RECORD = 5;
    private TuSdkAudioRecorderSetting a;
    private TuSdkMicRecord b;
    private TuSdkAudioResample c;
    private TuSdkAudioEncoder d;
    private _AudioSync e;
    private TuSdkMediaFileMuxer f;
    private File g;
    private TuSdkAudioPitchEngine h;
    private TuSdkAudioPitchEngine.TuSdkSoundPitchType i;
    private Object j;
    private Object k;
    private int l;
    private boolean m;
    private boolean n;
    private long o;
    private long p;
    private long q;
    private long r;
    private long s;
    private long t;
    private long u;
    private TuSdkTimeRange v;
    private LinkedList<TuSdkTimeRange> w;
    private LinkedList<TuSdkTimeRange> x;
    private TuSdkAudioRecorderListener y;
    private TuSdkAudioRecord.TuSdkAudioRecordListener z;
    private TuSdkAudioResampleSync A;
    private TuSdkAudioTrack B;
    private TuSdkAudioEngine.TuSdKAudioEngineOutputBufferDelegate C;
    private TuSdkEncoderListener D;
    
    public TuSdkAudioRecorder(final TuSdkAudioRecorderSetting a, final TuSdkAudioRecorderListener y) {
        this.i = TuSdkAudioPitchEngine.TuSdkSoundPitchType.Normal;
        this.j = new Object();
        this.k = new Object();
        this.l = 0;
        this.n = false;
        this.u = 10000000L;
        this.v = null;
        this.z = (TuSdkAudioRecord.TuSdkAudioRecordListener)new TuSdkAudioRecord.TuSdkAudioRecordListener() {
            public void onAudioRecordOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                synchronized (TuSdkAudioRecorder.this.j) {
                    if (TuSdkAudioRecorder.this.o == 0L) {
                        TuSdkAudioRecorder.this.o = bufferInfo.presentationTimeUs;
                    }
                    TuSdkAudioRecorder.this.p = bufferInfo.presentationTimeUs;
                    if (TuSdkAudioRecorder.this.n) {
                        if (TuSdkAudioRecorder.this.q == 0L) {
                            TuSdkAudioRecorder.this.q = bufferInfo.presentationTimeUs;
                        }
                        return;
                    }
                    bufferInfo.presentationTimeUs -= TuSdkAudioRecorder.this.r;
                    TuSdkAudioRecorder.this.s = bufferInfo.presentationTimeUs - TuSdkAudioRecorder.this.o;
                    TuSdkAudioRecorder.this.t = (long)(TuSdkAudioRecorder.this.s * TuSdkAudioRecorder.this.i.getSpeed());
                    bufferInfo.presentationTimeUs = TuSdkAudioRecorder.this.t;
                }
                if (TuSdkAudioRecorder.this.c != null) {
                    TuSdkAudioRecorder.this.c.queueInputBuffer(byteBuffer, bufferInfo);
                }
            }
            
            public void onAudioRecordError(final int n) {
                if (TuSdkAudioRecorder.this.y != null) {
                    TuSdkAudioRecorder.this.y.onRecordError(n);
                }
            }
        };
        this.A = (TuSdkAudioResampleSync)new TuSdkAudioResampleSync() {
            public void syncAudioResampleOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkAudioRecorder.this.h != null) {
                    TuSdkAudioRecorder.this.h.processInputBuffer(byteBuffer, bufferInfo);
                }
            }
            
            public void release() {
            }
        };
        this.C = new TuSdkAudioEngine.TuSdKAudioEngineOutputBufferDelegate() {
            @Override
            public void onProcess(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                TuSdkAudioRecorder.this.d.getOperation().writeBuffer(byteBuffer, bufferInfo);
            }
        };
        this.D = (TuSdkEncoderListener)new TuSdkEncoderListener() {
            public void onEncoderUpdated(final MediaCodec.BufferInfo bufferInfo) {
            }
            
            public void onEncoderCompleted(final Exception ex) {
                TLog.e((Throwable)ex);
            }
        };
        this.a = a;
        this.y = y;
        this.a();
    }
    
    private void a() {
        if (this.a == null || this.a.bitRate == 0 || this.a.channelCount == 0 || this.a.sampleRate == 0) {
            TLog.e("%s Init AudioRecorder Failed \uff0cParameter anomaly AudioInfo is :%s", new Object[] { "TuSdkAudioRecorder", this.a });
            return;
        }
        this.w = new LinkedList<TuSdkTimeRange>();
        this.x = new LinkedList<TuSdkTimeRange>();
        this.d = new TuSdkAudioEncoder();
        this.e = new _AudioSync();
        this.d.setOutputFormat(this.a.getRecordMediaFormat());
        this.d.setMediaSync((TuSdkAudioEncodecSync)this.e);
        this.d.setListener(this.D);
        if (!this.d.prepare()) {
            TLog.e("%s Encoder init failed", new Object[] { "TuSdkAudioRecorder" });
        }
        (this.f = new TuSdkMediaFileMuxer()).setAudioOperation((TuSdkEncodecOperation)this.d.getOperation());
        this.f.setPath(this.getOutputFileTemp().getPath());
        (this.b = new TuSdkMicRecord()).setListener(this.z);
        this.b.setAudioInfo(new TuSdkAudioInfo(this.b()));
        (this.c = (TuSdkAudioResample)new TuSdkAudioResampleHardImpl(new TuSdkAudioInfo(this.a.getRecordMediaFormat()))).changeFormat(new TuSdkAudioInfo(this.b()));
        this.c.setMediaSync(this.A);
        this.B = (TuSdkAudioTrack)new TuSdkAudioTrackImpl(new TuSdkAudioInfo(this.a.getRecordMediaFormat()));
        (this.h = new TuSdkAudioPitchEngine(new TuSdkAudioInfo(this.a.getRecordMediaFormat()), true)).changeAudioInfo(new TuSdkAudioInfo(this.a.getRecordMediaFormat()));
        this.h.setOutputBufferDelegate(this.C);
        this.a(1);
    }
    
    private MediaFormat b() {
        return TuSdkMediaFormat.buildSafeAudioEncodecFormat(44100, 1, 96000, 2);
    }
    
    public void setSoundPitchType(final TuSdkAudioPitchEngine.TuSdkSoundPitchType soundPitchType) {
        if (this.h == null || soundPitchType == null) {
            return;
        }
        this.h.flush();
        this.h.setSoundPitchType(soundPitchType);
    }
    
    public void setSoundPitchTypeChangeListener(final TuSdkAudioPitchEngine.TuSdkAudioEnginePitchTypeChangeDelegate soundTypeChangeListener) {
        if (this.h == null || soundTypeChangeListener == null) {
            return;
        }
        this.h.setSoundTypeChangeListener(soundTypeChangeListener);
    }
    
    public void start() {
        if (this.b == null || this.d == null || this.f == null || this.m) {
            return;
        }
        if (!this.f.prepare()) {
            TLog.e("%s Start Record Failed ! Muxer Prepare Error", new Object[] { "TuSdkAudioRecorder" });
        }
        this.b.startRecording();
        this.m = true;
        this.a(2);
    }
    
    public void resume() {
        synchronized (this.j) {
            if (!this.n) {
                return;
            }
            this.r += this.p - this.q;
            this.n = false;
            this.a(3);
        }
    }
    
    public void pause() {
        synchronized (this.j) {
            if (this.n) {
                return;
            }
            this.q = 0L;
            this.n = true;
            this.a(4);
        }
    }
    
    public void stop() {
        if (this.b == null || !this.m) {
            return;
        }
        this.b.stop();
        this.h.flush();
        this.d.release();
        this.f.release();
        this.h.release();
        this.m = true;
        this.a(5);
        TLog.d(" %s time slice %s", new Object[] { "TuSdkAudioRecorder", this.w });
    }
    
    public void releas() {
        if (this.l != 5) {
            this.stop();
        }
        this.w.clear();
        this.x.clear();
    }
    
    public boolean isPause() {
        return this.n;
    }
    
    public boolean isStart() {
        return this.m;
    }
    
    public void setMaxRecordTime(final long u) {
        this.u = u;
    }
    
    public File getOutputFileTemp() {
        if (this.g == null) {
            this.g = new File(AlbumHelper.getAblumPath(), String.format("lsq_temp_%s.aac", StringHelper.timeStampString()));
        }
        return this.g;
    }
    
    public void setOutputFile(final File g) {
        this.g = g;
        this.f.setPath(this.g.getPath());
    }
    
    private void a(final int l) {
        this.l = l;
        synchronized (this.k) {
            switch (l) {
                case 2: {
                    this.v = TuSdkTimeRange.makeRange(0.0f, -1.0f);
                    break;
                }
                case 3: {
                    this.v = TuSdkTimeRange.makeTimeUsRange(this.t, -1L);
                    break;
                }
                case 4: {
                    if (this.v == null) {
                        return;
                    }
                    this.v.setEndTimeUs(this.t);
                    this.w.addLast(this.v);
                    this.v = null;
                    break;
                }
                case 5: {
                    if (this.v != null && this.v.getEndTimeUS() == -1L) {
                        this.v.setEndTimeUs(this.t);
                        this.w.addLast(this.v);
                        this.v = null;
                        break;
                    }
                    break;
                }
            }
        }
        if (this.y != null) {
            this.y.onStateChanged(l);
        }
    }
    
    public TuSdkTimeRange removeLastRecordRange() {
        if (this.w.size() == 0) {
            return null;
        }
        synchronized (this.k) {
            final TuSdkTimeRange e = this.w.removeLast();
            this.x.addLast(e);
            TLog.d("removeLastRecordRange() : mRecordingTimeRangeList size: %s  mDropTimeRangeList size :%s ", new Object[] { this.w.size(), this.x.size() });
            this.a(this.getValidTimeUs());
            return e;
        }
    }
    
    public long getValidTimeUs() {
        return this.t - this.c();
    }
    
    public LinkedList<TuSdkTimeRange> getRecordingTimeRangeList() {
        return this.w;
    }
    
    public LinkedList<TuSdkTimeRange> getDropTimeRangeList() {
        return this.x;
    }
    
    private void a(final long l) {
        final float f = l / (float)this.u;
        TLog.d("%s notifyProgressChanged() record timeUS %s  mTotalTimeUS \uff1a%s  percent :%s", new Object[] { "TuSdkAudioRecorder", l, this.u, f });
        this.y.onRecordProgress(l, f);
    }
    
    private long c() {
        if (this.x.size() == 0) {
            return 0L;
        }
        long n = 0L;
        final Iterator<TuSdkTimeRange> iterator = this.x.iterator();
        while (iterator.hasNext()) {
            n += iterator.next().durationTimeUS();
        }
        return n;
    }
    
    public int getState() {
        return this.l;
    }
    
    class _AudioSync extends TuSdkAudioEncodecSyncBase
    {
        public void syncAudioEncodecInfo(final TuSdkAudioInfo tuSdkAudioInfo) {
            super.syncAudioEncodecInfo(tuSdkAudioInfo);
            TuSdkAudioRecorder.this.h.changeAudioInfo(tuSdkAudioInfo);
        }
        
        public void syncAudioEncodecOutputBuffer(final TuSdkMediaMuxer tuSdkMediaMuxer, final int n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            super.syncAudioEncodecOutputBuffer(tuSdkMediaMuxer, n, byteBuffer, bufferInfo);
            if (TuSdkAudioRecorder.this.y == null || bufferInfo == null) {
                return;
            }
            TuSdkAudioRecorder.this.a(bufferInfo.presentationTimeUs - TuSdkAudioRecorder.this.c());
            if (TuSdkAudioRecorder.this.getValidTimeUs() >= TuSdkAudioRecorder.this.u + 100000L) {
                TuSdkAudioRecorder.this.pause();
            }
        }
    }
    
    public interface TuSdkAudioRecorderListener
    {
        public static final int PARAMETRTS_ERROR = 2001;
        public static final int PERMISSION_ERROR = 2002;
        
        void onRecordProgress(final long p0, final float p1);
        
        void onStateChanged(final int p0);
        
        void onRecordError(final int p0);
    }
    
    public static class TuSdkAudioRecorderSetting
    {
        public int channelCount;
        public int sampleRate;
        public int bitRate;
        
        public TuSdkAudioRecorderSetting() {
            this.channelCount = 1;
            this.sampleRate = 44100;
            this.bitRate = 32768;
        }
        
        public MediaFormat getRecordMediaFormat() {
            if (this.channelCount == 0 || this.sampleRate == 0 || this.bitRate == 0) {
                TLog.e("%s Audio Setting Parameter error  %s", new Object[] { "TuSdkAudioRecorder", this.toString() });
            }
            return TuSdkMediaFormat.buildSafeAudioEncodecFormat(this.sampleRate, this.channelCount, this.bitRate, 2);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("channelCount : ");
            sb.append(this.channelCount);
            sb.append("\n");
            sb.append("sampleRate : ");
            sb.append(this.sampleRate);
            sb.append("\n");
            sb.append("bitRate : ");
            sb.append(this.bitRate);
            sb.append("}");
            return super.toString();
        }
    }
}
