// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.movie.postproc.muxer;

//import org.lasque.tusdk.core.secret.StatisticsManger;
import android.media.MediaCodec;
import java.nio.ByteBuffer;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import java.io.File;
import java.util.Iterator;
import android.media.MediaExtractor;
//import org.lasque.tusdk.core.common.TuSDKMediaUtils;
//import org.lasque.tusdk.core.TuSdkContext;
import java.util.List;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.media.MediaMuxer;
import android.annotation.SuppressLint;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaUtils;

@SuppressLint({ "InlinedApi" })
public class TuSDKMovieSplicer
{
    public static final int INVALID_START_TIME = -1;
    public static final int INVALID_END_TIME = 0;
    public static final int INVALID_TRACK_INDEX = -1;
    private TuSDKMovieSplicerOption a;
    private MediaMuxer b;
    private HandlerThread c;
    private Handler d;
    private final int e = 1048576;
    private int f;
    private int g;
    private int h;
    private final long i = 1L;
    private long j;
    private long k;
    private long l;
    private long m;
    private MediaFormat n;
    private MediaFormat o;
    
    public TuSDKMovieSplicer(final TuSDKMovieSplicerOption a) {
        this.f = 1048576;
        this.g = -1;
        this.h = -1;
        if (a == null) {
            TLog.e("option is null", new Object[] { new RuntimeException() });
        }
        this.a = a;
    }
    
    private void a(final ErrorCode errorCode) {
        if (this.a == null || this.a.listener == null || errorCode == null) {
            return;
        }
        TLog.e("%s : %s", new Object[] { this, errorCode.getMessage() });
        this.a.listener.onError(errorCode);
    }
    
    private boolean a(final List<TuSDKMovieSegment> list) {
        if (list.get(0) == null || list.get(0).sourceUri == null) {
            this.a(ErrorCode.InvalidMovieSegmentError);
            return false;
        }
        final MediaExtractor extractor = TuSDKMediaUtils.createExtractor(TuSdkContext.context(), list.get(0).sourceUri);
        for (int trackCount = extractor.getTrackCount(), i = 0; i < trackCount; ++i) {
            final MediaFormat trackFormat = extractor.getTrackFormat(i);
            if (TuSDKMediaUtils.isAudioFormat(trackFormat) && this.a.splicerType != TuSDKMovieSplicerType.SplicerVideoType) {
                this.o = trackFormat;
            }
            else if (TuSDKMediaUtils.isVideoFormat(trackFormat) && this.a.splicerType != TuSDKMovieSplicerType.SplicerAudioType) {
                this.n = trackFormat;
            }
        }
        final Iterator<TuSDKMovieSegment> iterator = list.iterator();
        while (iterator.hasNext()) {
            final MediaExtractor extractor2 = TuSDKMediaUtils.createExtractor(TuSdkContext.context(), iterator.next().sourceUri);
            if (this.a.splicerType != TuSDKMovieSplicerType.SplicerVideoType && this.o != null && !TuSDKMediaUtils.isSameAudioFormat(TuSDKMediaUtils.getAudioFormat(extractor2), this.o)) {
                return false;
            }
            if (this.a.splicerType != TuSDKMovieSplicerType.SplicerAudioType && this.n != null && !TuSDKMediaUtils.isSameVideoFormat(TuSDKMediaUtils.getVideoFormat(extractor2), this.n)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean a(final String pathname) {
        return pathname != null && new File(pathname).getParentFile().isDirectory();
    }
    
    private void a(final Uri uri) {
        this.j = 1L;
        this.k = 1L;
        this.l = 1L;
        this.m = 23219L;
        this.b = TuSDKMediaUtils.createMuxer(this.a.savePath, 0);
        final MediaExtractor extractor = TuSDKMediaUtils.createExtractor(TuSdkContext.context(), uri);
        for (int trackCount = extractor.getTrackCount(), i = 0; i < trackCount; ++i) {
            final MediaFormat trackFormat = extractor.getTrackFormat(i);
            if (TuSDKMediaUtils.isAudioFormat(trackFormat) && this.a.splicerType != TuSDKMovieSplicerType.SplicerVideoType) {
                if (this.o == null) {
                    this.o = trackFormat;
                }
                this.h = this.b.addTrack(this.o);
                TLog.d("Initializing Splicer audio track ", new Object[0]);
                if (trackFormat.containsKey("max-input-size")) {
                    final int integer = trackFormat.getInteger("max-input-size");
                    this.f = ((integer > this.f) ? integer : this.f);
                }
                if (trackFormat.containsKey("sample-rate") && trackFormat.getInteger("sample-rate") > 0) {
                    this.m = 1024000000 / trackFormat.getInteger("sample-rate");
                }
            }
            else if (TuSDKMediaUtils.isVideoFormat(trackFormat) && this.a.splicerType != TuSDKMovieSplicerType.SplicerAudioType) {
                if (this.n == null) {
                    this.n = trackFormat;
                }
                this.g = this.b.addTrack(this.n);
                TLog.d("Initializing Splicer video track ", new Object[0]);
                if (trackFormat.containsKey("max-input-size")) {
                    final int integer2 = trackFormat.getInteger("max-input-size");
                    this.f = ((integer2 > this.f) ? integer2 : this.f);
                }
                if (trackFormat.containsKey("frame-rate")) {
                    this.l = 1000000 / trackFormat.getInteger("frame-rate");
                }
            }
        }
        final MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(TuSdkContext.context(), uri);
        final String metadata = mediaMetadataRetriever.extractMetadata(24);
        if (metadata != null) {
            this.b.setOrientationHint(Integer.parseInt(metadata));
        }
        this.b.start();
    }
    
    public void start(final List<TuSDKMovieSegment> list) {
        if (this.c == null || this.d == null) {
            (this.c = new HandlerThread("TuSDKMovieSplicerThread")).start();
            this.d = new Handler(this.c.getLooper());
        }
        this.d.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (TuSDKMovieSplicer.this.a == null || list == null || list.size() == 0) {
                    TuSDKMovieSplicer.this.a(ErrorCode.InvalidMovieSegmentError);
                    return;
                }
                if (!TuSDKMovieSplicer.this.a(TuSDKMovieSplicer.this.a.savePath)) {
                    TuSDKMovieSplicer.this.a(ErrorCode.InvalidOutputPathError);
                    return;
                }
                if (!TuSDKMovieSplicer.this.a(list)) {
                    TuSDKMovieSplicer.this.a(ErrorCode.InvalidVideoFormatError);
                    return;
                }
                TuSDKMovieSplicer.this.a(list.get(0).sourceUri);
                if (TuSDKMovieSplicer.this.a.listener != null) {
                    TuSDKMovieSplicer.this.a.listener.onStart();
                }
                for (final TuSDKMovieSegment tuSDKMovieSegment : list) {
                    if (tuSDKMovieSegment == null) {
                        TLog.e("Segment is empty", new Object[0]);
                    }
                    else {
                        TuSDKMovieSplicer.this.a(tuSDKMovieSegment);
                    }
                }
                TuSDKMovieSplicer.this.a();
                if (TuSDKMovieSplicer.this.a.listener != null) {
                    TuSDKMovieSplicer.this.a.listener.onDone();
                }
            }
        });
    }
    
    private void a() {
        if (this.b != null) {
            this.b.stop();
            this.b.release();
            this.b = null;
        }
        if (this.c != null) {
            this.c.quit();
            this.d = null;
        }
    }
    
    private void a(final TuSDKMovieSegment tuSDKMovieSegment) {
        final boolean b = tuSDKMovieSegment.startTime == -1L || tuSDKMovieSegment.endTime == 0L;
        long n = 0L;
        final MediaExtractor extractor = TuSDKMediaUtils.createExtractor(TuSdkContext.context(), tuSDKMovieSegment.sourceUri);
        if (b) {
            extractor.seekTo(0L, 2);
        }
        else {
            final long n2 = tuSDKMovieSegment.startTime * 1000L;
            n = tuSDKMovieSegment.endTime * 1000L;
            extractor.seekTo(n2, 2);
        }
        final int offset = 0;
        int andSelectVideoTrackIndex = -1;
        int andSelectAudioTrackIndex = -1;
        if (this.a.splicerType != TuSDKMovieSplicerType.SplicerAudioType) {
            andSelectVideoTrackIndex = TuSDKMediaUtils.getAndSelectVideoTrackIndex(extractor);
        }
        if (this.a.splicerType != TuSDKMovieSplicerType.SplicerVideoType) {
            andSelectAudioTrackIndex = TuSDKMediaUtils.getAndSelectAudioTrackIndex(extractor);
        }
        final ByteBuffer allocate = ByteBuffer.allocate(this.f);
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        try {
            while (true) {
                bufferInfo.offset = offset;
                bufferInfo.size = extractor.readSampleData(allocate, offset);
                final long sampleTime = extractor.getSampleTime();
                if (n > 0L && sampleTime >= n && !b) {
                    TLog.d("The current sample is over the trim end time.", new Object[0]);
                    break;
                }
                if (bufferInfo.size < 0) {
                    TLog.d("Saw input EOS.", new Object[0]);
                    bufferInfo.size = 0;
                    break;
                }
                bufferInfo.presentationTimeUs = sampleTime;
                bufferInfo.flags = extractor.getSampleFlags();
                int n3 = extractor.getSampleTrackIndex();
                if (n3 == andSelectVideoTrackIndex && this.a(andSelectVideoTrackIndex)) {
                    n3 = this.g;
                }
                if (n3 == andSelectAudioTrackIndex && this.a(andSelectAudioTrackIndex)) {
                    n3 = this.h;
                }
                if (n3 == this.g) {
                    if (this.l <= 1L && this.j != 1L) {
                        this.l = this.a(this.j, bufferInfo.presentationTimeUs);
                    }
                    if (!this.a(this.l)) {
                        this.l = 1L;
                    }
                    if (bufferInfo.presentationTimeUs <= this.j) {
                        bufferInfo.presentationTimeUs = this.j + this.l;
                    }
                    this.j = bufferInfo.presentationTimeUs;
                }
                else if (n3 == this.h) {
                    if (this.m <= 1L && this.k != 1L) {
                        this.m = this.a(this.k, bufferInfo.presentationTimeUs);
                    }
                    if (!this.a(this.m)) {
                        this.m = 1L;
                    }
                    if (bufferInfo.presentationTimeUs <= this.k) {
                        bufferInfo.presentationTimeUs = this.k + this.m;
                    }
                    this.k = bufferInfo.presentationTimeUs;
                }
                this.b.writeSampleData(n3, allocate, bufferInfo);
                extractor.advance();
            }
        }
        catch (IllegalStateException ex) {
            this.a(ErrorCode.UnknowError);
        }
        StatisticsManger.appendComponent(9449472L);
    }
    
    private boolean a(final int n) {
        return n != -1;
    }
    
    private boolean a(final long n) {
        return n > 1L;
    }
    
    private long a(final long n, final long n2) {
        final long n3 = n2 - n;
        return (n3 > 1L) ? n3 : 1L;
    }
    
    public enum ErrorCode
    {
        InvalidMovieSegmentError("Invalid video file."), 
        InvalidVideoFormatError("Invalid file format."), 
        InvalidOutputPathError("Invalid output path."), 
        UnknowError("An unknown error.");
        
        private String a;
        
        private ErrorCode(final String a) {
            this.a = a;
        }
        
        public String getMessage() {
            return this.a;
        }
    }
    
    public enum TuSDKMovieSplicerType
    {
        SplicerMovieType, 
        SplicerVideoType, 
        SplicerAudioType;
    }
    
    public static class TuSDKMovieSplicerOption
    {
        public TuSDKMovieSplicerType splicerType;
        public String savePath;
        public TuSDKMovieSplicerListener listener;
        
        public TuSDKMovieSplicerOption() {
            this.splicerType = TuSDKMovieSplicerType.SplicerMovieType;
        }
    }
    
    public interface TuSDKMovieSplicerListener
    {
        void onStart();
        
        void onDone();
        
        void onError(final ErrorCode p0);
    }
    
    public static class TuSDKMovieSegment
    {
        public Uri sourceUri;
        public long startTime;
        public long endTime;
        
        public TuSDKMovieSegment() {
            this.startTime = -1L;
            this.endTime = 0L;
        }
    }
}
