// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.movie.postproc.muxer;

import java.io.File;
import android.content.Intent;
import android.provider.MediaStore;
import android.content.ContentValues;
//import org.lasque.tusdk.core.secret.StatisticsManger;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.net.Uri;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
//import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaPlayer;
//import org.lasque.tusdk.core.TuSdkContext;
import java.util.HashMap;
import android.os.Handler;
import android.os.HandlerThread;
import android.media.MediaMuxer;
import android.media.MediaExtractor;
import android.annotation.SuppressLint;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaUtils;

@SuppressLint({ "InlinedApi" })
public class TuSDKMovieClipper
{
    private TuSDKMovieClipperOption a;
    private MediaExtractor b;
    private MediaMuxer c;
    private HandlerThread d;
    private Handler e;
    private long f;
    private int g;
    private HashMap<Integer, Integer> h;
    private int i;
    private int j;
    private long k;
    private long l;
    private long m;
    private long n;
    private int o;
    private boolean p;
    
    public TuSDKMovieClipper(final TuSDKMovieClipperOption a) {
        this.f = 0L;
        this.g = -1;
        this.i = -1;
        this.j = -1;
        this.p = false;
        this.a = a;
        if (this.a.srcUri != null) {
            final MediaPlayer create = MediaPlayer.create(TuSdkContext.context(), this.a.srcUri);
            this.f = create.getDuration() * 1000;
            create.release();
        }
    }
    
    private boolean a(long min, long min2) {
        min = Math.min(min, this.f);
        min2 = Math.min(min2, this.f);
        if (min >= min2 || min < 0L || min2 <= 0L) {
            TLog.e("create segment is invalid", new Object[0]);
            return false;
        }
        return true;
    }
    
    public TuSDKMovieSegment getTotalSegment(final long n) {
        return this.createSegment(0L, n);
    }
    
    public TuSDKMovieSegment createSegment(final long startTime, final long endTime) {
        if (!this.a(startTime, endTime)) {
            return null;
        }
        final TuSDKMovieSegment tuSDKMovieSegment = new TuSDKMovieSegment();
        tuSDKMovieSegment.startTime = startTime;
        tuSDKMovieSegment.endTime = endTime;
        return tuSDKMovieSegment;
    }
    
    public void addSegment(final TuSDKMovieSegment obj, final List<TuSDKMovieSegment> obj2) {
        if (obj == null || obj2 == null) {
            TLog.e("addSegment", new Object[] { new RuntimeException("segment==" + obj + "list==" + obj2) });
        }
        if (obj != null && obj2 != null) {
            obj2.add(obj);
        }
    }
    
    public List<TuSDKMovieSegment> getSegmentList(final long n, final List<TuSDKMovieSegment> list) {
        List<TuSDKMovieSegment> newList = new ArrayList<TuSDKMovieSegment>();
        final TuSDKMovieSegment totalSegment = this.getTotalSegment(n);
        newList.add(totalSegment);
        final Iterator<TuSDKMovieSegment> iterator = list.iterator();
        while (iterator.hasNext()) {
            newList = getNewList(iterator.next(), totalSegment, newList);
        }
        if (newList == null || ((List)newList).size() == 0) {
            TLog.e("newlist", new Object[] { new RuntimeException("newlist== null || newlist.size() == 0") });
            return null;
        }
       // Collections.sort();
        Collections.sort(newList, new Comparator<TuSDKMovieSegment>() {
            @Override
            public int compare(final TuSDKMovieSegment tuSDKMovieSegment, final TuSDKMovieSegment tuSDKMovieSegment2) {
                return (int)(tuSDKMovieSegment.startTime - tuSDKMovieSegment2.startTime);
            }
        });
        return (List<TuSDKMovieSegment>)newList;
    }
    
    public List<TuSDKMovieSegment> getNewList(final TuSDKMovieSegment tuSDKMovieSegment, final TuSDKMovieSegment tuSDKMovieSegment2, final List<TuSDKMovieSegment> list) {
        final long startTime = tuSDKMovieSegment.startTime;
        final long endTime = tuSDKMovieSegment.endTime;
        final ArrayList<TuSDKMovieSegment> list2 = new ArrayList<TuSDKMovieSegment>();
        if (list.size() == 0) {
            if (startTime != tuSDKMovieSegment2.startTime) {
                this.addSegment(this.createSegment(tuSDKMovieSegment2.startTime, endTime), list2);
            }
            if (endTime != tuSDKMovieSegment2.endTime) {
                this.addSegment(this.createSegment(startTime, tuSDKMovieSegment2.endTime), list2);
            }
            return list2;
        }
        list2.addAll(list);
        for (final TuSDKMovieSegment tuSDKMovieSegment3 : list) {
            if (startTime < tuSDKMovieSegment3.endTime) {
                if (endTime <= tuSDKMovieSegment3.startTime) {
                    continue;
                }
                list2.remove(tuSDKMovieSegment3);
                if (tuSDKMovieSegment3.startTime == startTime) {
                    this.addSegment(this.createSegment(endTime, tuSDKMovieSegment3.endTime), list2);
                }
                else if (tuSDKMovieSegment3.endTime == endTime) {
                    this.addSegment(this.createSegment(tuSDKMovieSegment3.startTime, startTime), list2);
                }
                else {
                    this.addSegment(this.createSegment(tuSDKMovieSegment3.startTime, startTime), list2);
                    this.addSegment(this.createSegment(endTime, tuSDKMovieSegment3.endTime), list2);
                }
            }
        }
        return list2;
    }
    
    @SuppressLint({ "UseSparseArrays" })
    private void a() {
        this.p = false;
        this.l = 0L;
        this.k = 0L;
        this.c = TuSDKMediaUtils.createMuxer(this.a.savePath, 0);
        this.b = TuSDKMediaUtils.createExtractor(TuSdkContext.context(), this.a.srcUri);
        final int trackCount = this.b.getTrackCount();
        this.h = new HashMap<Integer, Integer>(trackCount);
        for (int i = 0; i < trackCount; ++i) {
            final MediaFormat trackFormat = this.b.getTrackFormat(i);
            final String string = trackFormat.getString("mime");
            if (string.startsWith("audio/")) {
                this.b.selectTrack(i);
                final int addTrack = this.c.addTrack(trackFormat);
                this.j = i;
                this.h.put(i, addTrack);
                if (trackFormat.containsKey("max-input-size")) {
                    final int integer = trackFormat.getInteger("max-input-size");
                    this.g = ((integer > this.g) ? integer : this.g);
                }
                this.n = TuSDKMediaUtils.getAudioInterval(1024, trackFormat);
            }
            else if (string.startsWith("video/")) {
                this.b.selectTrack(i);
                final int addTrack2 = this.c.addTrack(trackFormat);
                this.i = i;
                this.h.put(i, addTrack2);
                if (trackFormat.containsKey("max-input-size")) {
                    final int integer2 = trackFormat.getInteger("max-input-size");
                    this.g = ((integer2 > this.g) ? integer2 : this.g);
                }
                final boolean containsKeyFrameRate = TuSDKMediaUtils.containsKeyFrameRate(trackFormat);
                if (containsKeyFrameRate) {
                    this.o = TuSDKMediaUtils.getVideoFps(trackFormat);
                }
                else if (!containsKeyFrameRate && this.a != null) {
                    this.o = this.a.fps;
                }
                else {
                    TLog.e("MediaFormat is not contains KEY_FRAME_RATE", new Object[0]);
                }
            }
        }
        if (this.g < 0) {
            this.g = 1048576;
        }
        final MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(TuSdkContext.context(), this.a.srcUri);
        final String metadata = mediaMetadataRetriever.extractMetadata(24);
        if (metadata != null) {
            final int int1 = Integer.parseInt(metadata);
            if (int1 >= 0) {
                this.c.setOrientationHint(int1);
            }
        }
        this.c.start();
    }
    
    @Deprecated
    public void startEdit(final List<TuSDKMovieSegment> list) {
        this.removeSegments(list);
    }
    
    public void removeSegments(final List<TuSDKMovieSegment> list) {
        if (this.a == null || list == null || list.size() == 0) {
            TLog.e("is invalid option or segment", new Object[0]);
            return;
        }
        for (final TuSDKMovieSegment tuSDKMovieSegment : list) {
            if (tuSDKMovieSegment == null || !this.a(tuSDKMovieSegment.startTime, tuSDKMovieSegment.endTime)) {
                TLog.e("is invalid segment", new Object[0]);
                return;
            }
        }
        if (this.d == null || this.e == null) {
            (this.d = new HandlerThread("TuSDKMovieEditThread")).start();
            this.e = new Handler(this.d.getLooper());
        }
        this.e.post((Runnable)new Runnable() {
            @Override
            public void run() {
                TuSDKMovieClipper.this.a();
                final List<TuSDKMovieSegment> segmentList = TuSDKMovieClipper.this.getSegmentList(TuSDKMovieClipper.this.f, list);
                if (segmentList == null || segmentList.size() == 0) {
                    TLog.e("newList == null  || newList.size() == 0" + new RuntimeException(), new Object[0]);
                    return;
                }
                if (TuSDKMovieClipper.this.a.listener != null) {
                    TuSDKMovieClipper.this.a.listener.onStart();
                }
                for (final TuSDKMovieSegment tuSDKMovieSegment : segmentList) {
                    TuSDKMovieClipper.this.a(TuSDKMovieClipper.this.a.srcUri, TuSDKMovieClipper.this.a.savePath, tuSDKMovieSegment.startTime, tuSDKMovieSegment.endTime);
                }
                TuSDKMovieClipper.this.b();
                if (TuSDKMovieClipper.this.a.listener == null) {
                    return;
                }
                if (TuSDKMovieClipper.this.p) {
                    TuSDKMovieClipper.this.a.listener.onCancel();
                }
                else {
                    TuSDKMovieClipper.this.a.listener.onDone(TuSDKMovieClipper.this.a.savePath);
                }
            }
        });
    }
    
    public void saveSegments(final List<TuSDKMovieSegment> list) {
        if (this.a == null || list == null || list.size() == 0) {
            TLog.e("is invalid option or segmentList", new Object[0]);
            return;
        }
        for (final TuSDKMovieSegment tuSDKMovieSegment : list) {
            if (tuSDKMovieSegment == null || !this.a(tuSDKMovieSegment.startTime, tuSDKMovieSegment.endTime)) {
                TLog.e("is invalid segment", new Object[0]);
                return;
            }
        }
        if (this.d == null || this.e == null) {
            (this.d = new HandlerThread("TuSDKMovieEditThread")).start();
            this.e = new Handler(this.d.getLooper());
        }
        this.e.post((Runnable)new Runnable() {
            @Override
            public void run() {
                TuSDKMovieClipper.this.a();
                if (TuSDKMovieClipper.this.a.listener != null) {
                    TuSDKMovieClipper.this.a.listener.onStart();
                }
                for (final TuSDKMovieSegment tuSDKMovieSegment : list) {
                    TuSDKMovieClipper.this.a(TuSDKMovieClipper.this.a.srcUri, TuSDKMovieClipper.this.a.savePath, tuSDKMovieSegment.startTime, tuSDKMovieSegment.endTime);
                }
                TuSDKMovieClipper.this.b();
                if (TuSDKMovieClipper.this.a.listener == null) {
                    return;
                }
                if (TuSDKMovieClipper.this.p) {
                    TuSDKMovieClipper.this.a.listener.onCancel();
                }
                else {
                    TuSDKMovieClipper.this.a.listener.onDone(TuSDKMovieClipper.this.a.savePath);
                }
            }
        });
    }
    
    public void cancel() {
        this.p = true;
    }
    
    private void b() {
        if (this.c != null) {
            this.c.stop();
            this.c.release();
            this.c = null;
        }
        if (this.b != null) {
            this.b.release();
            this.b = null;
        }
        this.a(this.a.savePath);
        if (this.d != null) {
            this.d.quit();
            this.e = null;
        }
    }
    
    @SuppressLint("WrongConstant")
    private void a(final Uri uri, final String s, final long n, final long n2) {
        final long a = this.a(uri, n, n2);
        if (this.o == 0) {
            this.o = 15;
        }
        if (a > 0L && a < 500000L) {
            this.m = a;
        }
        else {
            this.m = TuSDKMediaUtils.getVideoInterval(this.o);
        }
        final long b = this.b(uri, n, n2);
        if (b > 0L && b < 500000L) {
            this.n = b;
        }
        else {
            this.n = TuSDKMediaUtils.getAudioDefaultInterval();
        }
        this.b.seekTo(n, MediaExtractor.SAMPLE_FLAG_ENCRYPTED);
        final ByteBuffer allocate = ByteBuffer.allocate(this.g);
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        try {
            while (!this.p) {
                bufferInfo.offset = 0;
                bufferInfo.size = this.b.readSampleData(allocate, 0);
                if (bufferInfo.size < 0) {
                    TLog.d("Saw input EOS.", new Object[0]);
                    bufferInfo.size = 0;
                    break;
                }
                final long sampleTime = this.b.getSampleTime();
                if (n2 > 0L && sampleTime >= n2) {
                    TLog.d("The current sample is over the trim end time.", new Object[0]);
                    break;
                }
                bufferInfo.flags = this.b.getSampleFlags();
                final int sampleTrackIndex = this.b.getSampleTrackIndex();
                if (sampleTrackIndex == this.i) {
                    bufferInfo.presentationTimeUs = this.k + this.m;
                    this.k = bufferInfo.presentationTimeUs;
                }
                else if (sampleTrackIndex == this.j) {
                    bufferInfo.presentationTimeUs = this.l + this.n;
                    this.l = bufferInfo.presentationTimeUs;
                }
                this.c.writeSampleData((int)this.h.get(sampleTrackIndex), allocate, bufferInfo);
                this.b.advance();
            }
        }
        catch (IllegalStateException ex) {
            TLog.w("The source video file is malformed", new Object[0]);
        }
        StatisticsManger.appendComponent(9449475L);
    }
    
    private void a(final String pathname) {
        final MediaPlayer create = MediaPlayer.create(TuSdkContext.context(), Uri.parse(pathname));
        final int duration = create.getDuration();
        create.release();
        final ContentValues contentValues = new ContentValues();
        contentValues.put("mime_type", "video/mp4");
        contentValues.put("_data", pathname);
        contentValues.put("duration", Integer.valueOf(duration));
        TuSdkContext.context().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
        final Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(pathname)));
        TuSdkContext.context().sendBroadcast(intent);
    }
    
    private long a(final Uri uri, final long n, final long n2) {
        return this.b(uri, "video/", n, n2);
    }
    
    private long b(final Uri uri, final long n, final long n2) {
        return this.b(uri, "audio/", n, n2);
    }
    
    @SuppressLint("WrongConstant")
    private long b(final Uri uri, final String prefix, final long n, final long n2) {
        boolean b = false;
        final MediaExtractor extractor = TuSDKMediaUtils.createExtractor(TuSdkContext.context(), uri);
        final int trackCount = extractor.getTrackCount();
        int capacity = -1;
        for (int i = 0; i < trackCount; ++i) {
            final MediaFormat trackFormat = extractor.getTrackFormat(i);
            if (trackFormat.getString("mime").startsWith(prefix)) {
                extractor.selectTrack(i);
                if (trackFormat.containsKey("max-input-size")) {
                    final int integer = trackFormat.getInteger("max-input-size");
                    capacity = ((integer > capacity) ? integer : capacity);
                }
                b = true;
            }
        }
        if (!b) {
            return 0L;
        }
        if (capacity < 0) {
            capacity = 1048576;
        }
        extractor.seekTo(n, MediaExtractor.SAMPLE_FLAG_ENCRYPTED);
        final ByteBuffer allocate = ByteBuffer.allocate(capacity);
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        long n3 = 0L;
        while (true) {
            bufferInfo.offset = 0;
            bufferInfo.size = extractor.readSampleData(allocate, 0);
            if (bufferInfo.size < 0) {
                bufferInfo.size = 0;
                break;
            }
            final long sampleTime = extractor.getSampleTime();
            if (n2 > 0L && sampleTime >= n2) {
                break;
            }
            ++n3;
            extractor.advance();
        }
        if (n3 == 0L) {
            return 0L;
        }
        return (n2 - n) / n3;
    }
    
    public interface TuSDKMovieClipperListener
    {
        void onStart();
        
        void onCancel();
        
        void onDone(final String p0);
        
        void onError(final Exception p0);
    }
    
    public static class TuSDKMovieClipperOption
    {
        public String savePath;
        public Uri srcUri;
        public TuSDKMovieClipperListener listener;
        public int fps;
    }
    
    public static class TuSDKMovieSegment
    {
        protected long startTime;
        protected long endTime;
        
        @Override
        public String toString() {
            return "segment start = " + this.startTime + " end = " + this.endTime;
        }
        
        public long getStartTime() {
            return this.startTime;
        }
        
        public long getEndTime() {
            return this.endTime;
        }
    }
}
