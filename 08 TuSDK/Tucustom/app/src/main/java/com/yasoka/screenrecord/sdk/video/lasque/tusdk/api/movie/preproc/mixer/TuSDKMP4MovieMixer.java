// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.movie.preproc.mixer;

import android.os.AsyncTask;
import android.media.MediaCrypto;
import android.view.Surface;
import java.io.File;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.core.video.TuSDKVideoResult;
import java.util.Map;
//import org.lasque.tusdk.core.TuSdkContext;
import android.text.TextUtils;
import java.nio.ByteBuffer;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
//import org.lasque.tusdk.video.editor.TuSDKMovieWriterInterface;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.TuSdk;
import java.util.Iterator;
import android.media.MediaFormat;
import java.io.FileNotFoundException;
import java.util.ArrayList;
//import org.lasque.tusdk.core.common.TuSDKMediaUtils;
//import org.lasque.tusdk.core.utils.TLog;
import java.io.IOException;
//import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAverageAudioMixer;
//import org.lasque.tusdk.video.editor.TuSDKMovieWriter;
import java.util.List;
import java.io.RandomAccessFile;
import java.io.FileOutputStream;
import android.media.MediaCodec;
//import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioMixer;
//import org.lasque.tusdk.core.decoder.TuSDKAudioInfo;
//import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import android.media.MediaExtractor;
//import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioEntry;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import android.annotation.TargetApi;
import android.annotation.SuppressLint;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioEntry;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioMixer;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.mixer.TuSDKAverageAudioMixer;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaUtils;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKAudioInfo;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.video.TuSDKVideoResult;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSDKMovieWriter;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSDKMovieWriterInterface;

@SuppressLint({ "InlinedApi" })
@TargetApi(16)
public class TuSDKMP4MovieMixer extends TuSDKMovieMixer
{
    private OnMP4MovieMixerDelegate a;
    private State b;
    private float c;
    private TuSDKMediaDataSource d;
    private String e;
    private TuSDKAudioEntry f;
    private boolean g;
    private boolean h;
    private AsyncVideoMixTask i;
    private MediaExtractor j;
    private TuSDKVideoInfo k;
    private TuSDKAudioInfo l;
    private TuSDKAudioMixer m;
    private MediaCodec n;
    private FileOutputStream o;
    private String p;
    private RandomAccessFile q;
    private List<TuSDKAudioEntry> r;
    private byte[] s;
    private int t;
    private long u;
    private long v;
    private TuSDKAudioMixer.OnAudioMixerDelegate w;
    private TuSDKMovieWriter.TuSDKMovieWriterDelegate x;
    
    public TuSDKMP4MovieMixer() {
        this.b = State.Idle;
        this.c = 1.0f;
        this.g = false;
        this.h = true;
        this.m = new TuSDKAverageAudioMixer();
        this.s = new byte[4096];
        this.t = 0;
        this.u = 0L;
        this.v = 0L;
        this.w = new TuSDKAudioMixer.OnAudioMixerDelegate() {
            @Override
            public void onMixed(final byte[] b) {
                try {
                    if (TuSDKMP4MovieMixer.this.o != null) {
                        TuSDKMP4MovieMixer.this.o.write(b);
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            
            @Override
            public void onMixingError(final int n) {
                try {
                    if (TuSDKMP4MovieMixer.this.o != null) {
                        TuSDKMP4MovieMixer.this.o.close();
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            
            @Override
            public void onReayTrunkTrackInfo(final TuSDKAudioInfo tuSDKAudioInfo) {
                TuSDKMP4MovieMixer.this.l = tuSDKAudioInfo;
            }
            
            @Override
            public void onStateChanged(final TuSDKAudioMixer.State state) {
                if (state == TuSDKAudioMixer.State.Decoding) {
                    TuSDKMP4MovieMixer.this.a(State.Decoding);
                }
                else if (state == TuSDKAudioMixer.State.Decoded) {
                    TuSDKMP4MovieMixer.this.a(State.Decoded);
                }
                else if (state == TuSDKAudioMixer.State.Mixing) {
                    TuSDKMP4MovieMixer.this.a(State.Mixing);
                }
                else if (state == TuSDKAudioMixer.State.Cancelled) {
                    TuSDKMP4MovieMixer.this.a(State.Cancelled);
                }
                else if (state == TuSDKAudioMixer.State.Complete) {
                    try {
                        if (TuSDKMP4MovieMixer.this.o != null) {
                            TuSDKMP4MovieMixer.this.o.close();
                        }
                        TuSDKMP4MovieMixer.this.a();
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                        TuSDKMP4MovieMixer.this.a(State.Failed);
                    }
                }
            }
        };
        this.x = new TuSDKMovieWriter.TuSDKMovieWriterDelegate() {
            @Override
            public void onFirstVideoSampleDataWrited(final long n) {
            }
            
            @Override
            public void onProgressChanged(final float n, final long n2) {
                if (TuSDKMP4MovieMixer.this.g) {
                    return;
                }
                if (TuSDKMP4MovieMixer.this.u == 0L && TuSDKMP4MovieMixer.this.f != null && TuSDKMP4MovieMixer.this.f.validateTimeRange() && n >= TuSDKMP4MovieMixer.this.f.getTimeRange().getStartTime()) {
                    TuSDKMP4MovieMixer.this.u = n2;
                    TuSDKMP4MovieMixer.this.v = n2;
                    TuSDKMP4MovieMixer.this.getMediaWriter().setWriteMuteAudioPlaceholderData(false);
                }
            }
        };
    }
    
    @Override
    public TuSDKMP4MovieMixer setVideoSoundVolume(final float n) {
        if (this.f != null && this.f.isTrunk()) {
            this.f.setVolume(n);
        }
        this.c = n;
        return this;
    }
    
    public TuSDKMP4MovieMixer setIgnoreTrunkAudioTimeRange(final boolean g) {
        this.g = g;
        return this;
    }
    
    public TuSDKMP4MovieMixer setClearAudioDecodeCacheInfoOnCompleted(final boolean h) {
        this.h = h;
        return this;
    }
    
    @Override
    public void mix(final TuSDKMediaDataSource d, final List<TuSDKAudioEntry> list, final boolean b) {
        if (d == null || !d.isValid()) {
            TLog.e("%s : Please set a valid video file path", new Object[] { this });
            this.a(State.Failed);
            return;
        }
        if (list == null || list.size() == 0) {
            TLog.e("%s : Please set a valid audio file path", new Object[] { this });
            this.a(State.Failed);
            return;
        }
        final MediaFormat videoFormat = TuSDKMediaUtils.getVideoFormat(d);
        if (videoFormat == null || !this.a(videoFormat)) {
            this.a(State.Failed);
            this.a(ErrorCode.UnsupportedVideoFormat);
            if (videoFormat != null) {
                TLog.e("%s | The device does not support this video format : %s", new Object[] { this, videoFormat.getString("mime") });
            }
            return;
        }
        this.d = d;
        this.r = new ArrayList<TuSDKAudioEntry>();
        if (list != null) {
            this.r.addAll(list);
        }
        if (b) {
            final Iterator<TuSDKAudioEntry> iterator = this.r.iterator();
            while (iterator.hasNext()) {
                iterator.next().setTrunk(false);
            }
            (this.f = new TuSDKAudioEntry(d)).setTrunk(true);
            this.f.setVolume(this.c);
            this.r.add(this.f);
        }
        else {
            for (final TuSDKAudioEntry f : this.r) {
                if (f.isTrunk()) {
                    this.f = f;
                }
            }
            if (this.f == null && this.r.size() == 1) {
                this.f = this.r.get(0);
            }
        }
        try {
            this.o = new FileOutputStream(this.b());
            this.q = new RandomAccessFile(this.b(), "rw");
        }
        catch (FileNotFoundException ex) {
            TLog.e("%s : Please set a valid file path", new Object[] { this });
            ex.printStackTrace();
            this.a(State.Failed);
            return;
        }
        this.getMediaWriter().setDelegate(this.x);
        this.a(this.r);
    }
    
    public void cancle() {
        if (this.m != null) {
            this.m.cancel();
        }
        if (this.i != null) {
            this.i.cancel();
        }
    }
    
    protected void onStopeed() {
        this.stopMovieWriter();
        if (this.n != null) {
            this.n.stop();
            this.n.release();
            this.n = null;
        }
        if (this.j != null) {
            this.j.release();
            this.j = null;
        }
        this.n();
    }
    
    private void a() {
        this.cancle();
        (this.i = new AsyncVideoMixTask()).execute(new Void[0]);
    }
    
    private void a(final List<TuSDKAudioEntry> list) {
        this.m.setOnAudioMixDelegate(this.w);
        this.m.mixAudios(list);
    }
    
    @Override
    public String getOutputFilePah() {
        if (this.e == null) {
            this.e = TuSdk.getAppTempPath() + "/" + String.format("lsq_%s.mp4", StringHelper.timeStampString());
        }
        return this.e;
    }
    
    public TuSDKMP4MovieMixer setOutputFilePath(final String e) {
        this.e = e;
        return this;
    }
    
    private String b() {
        if (this.p == null) {
            this.p = TuSdk.getAppTempPath() + "/" + String.format("lsq_%s", StringHelper.timeStampString());
        }
        return this.p;
    }
    
    private long c() {
        return this.v - this.u;
    }
    
    private long d() {
        return (this.k != null) ? this.k.durationTimeUs : 0L;
    }
    
    private boolean e() {
        if (this.f == null) {
            return false;
        }
        if (this.f.validateTimeRange() && !this.g) {
            return this.c() < (float)Math.min(this.d() - this.f.getTimeRange().getStartTimeUS(), this.f.getTimeRange().durationTimeUS());
        }
        return this.f.isLooping() && this.c() < this.d();
    }
    
    @Override
    protected TuSDKMovieWriterInterface.MovieWriterOutputFormat getOutputFormat() {
        return TuSDKMovieWriterInterface.MovieWriterOutputFormat.MPEG_4;
    }
    
    private MediaFormat f() {
        final MediaFormat mediaFormat = null;
        MediaCodec.BufferInfo bufferInfo;
        int i;
        for (bufferInfo = new MediaCodec.BufferInfo(), i = 0; i != -2; i = this.k().dequeueOutputBuffer(bufferInfo, 0L)) {}
        if (i == -2) {
            return this.k().getOutputFormat();
        }
        return mediaFormat;
    }
    
    private MediaFormat g() {
        final int videoTrack = this.findVideoTrack();
        if (videoTrack < 0) {
            return null;
        }
        return this.getMediaExtractor().getTrackFormat(videoTrack);
    }
    
    public int findVideoTrack() {
        if (this.getMediaExtractor() == null) {
            return -1;
        }
        for (int trackCount = this.getMediaExtractor().getTrackCount(), i = 0; i < trackCount; ++i) {
            if (this.getMediaExtractor().getTrackFormat(i).getString("mime").startsWith("video/")) {
                return i;
            }
        }
        return -1;
    }
    
    private void h() {
        if (this.getMediaExtractor() == null) {
            this.onStopeed();
            this.a(State.Failed);
            TLog.d("%s : Please check the video file path", new Object[] { this });
            return;
        }
        final MediaFormat g = this.g();
        if (g == null) {
            this.onStopeed();
            this.a(State.Failed);
            TLog.e("%s Invalid video format", new Object[] { this });
            return;
        }
        this.addVideoTrack(g);
        this.k = TuSDKVideoInfo.createWithMediaFormat(g, true);
        this.getMediaWriter().setOrientationHint(this.k.degree);
        this.k().start();
        this.addAudioTrack(this.f());
        this.startMovieWriter();
        this.i();
        this.j();
        this.onStopeed();
    }
    
    private boolean a(final MediaFormat mediaFormat) {
        for (int codecCount = MediaCodecList.getCodecCount(), i = 0; i < codecCount; ++i) {
            final MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (codecInfo.isEncoder()) {
                final String[] supportedTypes = codecInfo.getSupportedTypes();
                for (int j = 0; j < supportedTypes.length; ++j) {
                    if (supportedTypes[j].equalsIgnoreCase(mediaFormat.getString("mime"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void i() {
        final MediaExtractor mediaExtractor = this.getMediaExtractor();
        final MediaFormat g = this.g();
        int n = 0;
        TuSDKMediaUtils.getAndSelectVideoTrackIndex(this.getMediaExtractor());
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(g.getInteger("width") * g.getInteger("height"));
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        if (!this.g && this.f != null && this.f.validateTimeRange()) {
            this.getMediaWriter().setWriteMuteAudioPlaceholderData(true);
        }
        while (n == 0 && this.b == State.Mixing) {
            allocateDirect.clear();
            final int sampleData = mediaExtractor.readSampleData(allocateDirect, 0);
            if (sampleData < 0) {
                n = 1;
            }
            else {
                bufferInfo.presentationTimeUs = mediaExtractor.getSampleTime();
                bufferInfo.flags = mediaExtractor.getSampleFlags();
                allocateDirect.limit(bufferInfo.size = sampleData);
                this.writeVideoSampleData(allocateDirect, bufferInfo);
                mediaExtractor.advance();
            }
        }
    }
    
    private void j() {
        if (!this.getMediaWriter().hasAudioTrack()) {
            return;
        }
        boolean l = false;
        for (boolean m = false; !m && this.b == State.Mixing; m = this.m()) {
            if (!l) {
                l = this.l();
            }
        }
    }
    
    private MediaCodec k() {
        if (this.n == null) {
            this.n = this.q();
        }
        return this.n;
    }
    
    private boolean l() {
        final ByteBuffer[] inputBuffers = this.n.getInputBuffers();
        final int dequeueInputBuffer = this.n.dequeueInputBuffer(500L);
        if (dequeueInputBuffer >= 0) {
            final ByteBuffer byteBuffer = inputBuffers[dequeueInputBuffer];
            byteBuffer.clear();
            try {
                if (this.q.read(this.s) == -1) {
                    if (!this.e()) {
                        this.n.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                        return true;
                    }
                    this.q.seek(0L);
                    this.q.read(this.s);
                }
                byteBuffer.put(this.s);
                this.t += this.s.length;
                this.n.queueInputBuffer(dequeueInputBuffer, 0, this.s.length, this.p().frameTimeUsWithAudioSize(this.t), 0);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
    
    private boolean m() {
        final ByteBuffer[] outputBuffers = this.n.getOutputBuffers();
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        final int dequeueOutputBuffer = this.n.dequeueOutputBuffer(bufferInfo, 500L);
        if (dequeueOutputBuffer >= 0) {
            if ((bufferInfo.flags & 0x2) != 0x0) {
                this.n.releaseOutputBuffer(dequeueOutputBuffer, false);
                return false;
            }
            if (bufferInfo.size != 0) {
                final ByteBuffer byteBuffer = outputBuffers[dequeueOutputBuffer];
                byteBuffer.position(bufferInfo.offset);
                byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
                if (this.u == 0L) {
                    this.u = bufferInfo.presentationTimeUs;
                }
                if (this.v == 0L) {
                    this.v = this.u;
                }
                this.v += this.p().getFrameInterval();
                bufferInfo.presentationTimeUs = this.v;
                this.writeAudioSampleData(byteBuffer, bufferInfo);
            }
            this.n.releaseOutputBuffer(dequeueOutputBuffer, false);
            if ((bufferInfo.flags & 0x4) != 0x0) {
                return true;
            }
            if (this.c() >= this.d()) {
                return true;
            }
            if (this.f != null && ((this.f.validateTimeRange() && !this.g) || this.f.isLooping()) && !this.e()) {
                return true;
            }
        }
        else if (dequeueOutputBuffer == -3) {
            this.n.getOutputBuffers();
        }
        else if (dequeueOutputBuffer == -2) {}
        return false;
    }
    
    public MediaExtractor getMediaExtractor() {
        if (this.j != null) {
            return this.j;
        }
        this.j = new MediaExtractor();
        try {
            if (!TextUtils.isEmpty((CharSequence)this.d.getFilePath())) {
                this.j.setDataSource(this.d.getFilePath());
            }
            else {
                this.j.setDataSource(TuSdkContext.context(), this.d.getFileUri(), (Map)null);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            this.j = null;
        }
        return this.j;
    }
    
    private void a(final State b) {
        if (this.b == b) {
            return;
        }
        this.b = b;
        if (this.a != null) {
            this.a.onStateChanged(b);
        }
    }
    
    private void a(final ErrorCode errorCode) {
        if (this.a != null) {
            this.a.onErrrCode(errorCode);
        }
    }
    
    private void a(final TuSDKVideoResult tuSDKVideoResult) {
        if (this.a == null) {
            return;
        }
        this.a.onMixerComplete(tuSDKVideoResult);
        StatisticsManger.appendComponent(9449474L);
    }
    
    private void n() {
        if (this.p != null) {
            new File(this.p).delete();
        }
        if (this.h) {
            this.m.clearDecodeCahceInfo();
        }
    }
    
    private TuSDKVideoResult o() {
        if (this.k == null) {
            return null;
        }
        final TuSDKVideoResult tuSDKVideoResult = new TuSDKVideoResult();
        tuSDKVideoResult.videoPath = new File(this.getOutputFilePah());
        tuSDKVideoResult.duration = (int)(this.k.durationTimeUs / 1000000L);
        tuSDKVideoResult.videoInfo = this.k;
        return tuSDKVideoResult;
    }
    
    public TuSDKMP4MovieMixer setDelegate(final OnMP4MovieMixerDelegate a) {
        this.a = a;
        return this;
    }
    
    public OnMP4MovieMixerDelegate getDelegate() {
        return this.a;
    }
    
    private TuSDKAudioInfo p() {
        if (this.l == null) {
            this.l = TuSDKAudioInfo.defaultAudioInfo();
        }
        return this.l;
    }
    
    private MediaCodec q() {
         MediaCodec encoderByType = null;
        try {
            encoderByType = MediaCodec.createEncoderByType("audio/mp4a-latm");
            final MediaFormat mediaFormat = new MediaFormat();
            mediaFormat.setString("mime", "audio/mp4a-latm");
            mediaFormat.setInteger("bitrate", this.p().bitrate);
            mediaFormat.setInteger("channel-count", this.p().channel);
            mediaFormat.setInteger("sample-rate", this.p().sampleRate);
            mediaFormat.setInteger("aac-profile", 2);
            encoderByType.configure(mediaFormat, (Surface)null, (MediaCrypto)null, 1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return encoderByType;
    }
    
    private class AsyncVideoMixTask extends AsyncTask<Void, Double, Void>
    {
        protected Void doInBackground(final Void... array) {
            TuSDKMP4MovieMixer.this.h();
            return null;
        }
        
        protected void onPreExecute() {
            super.onPreExecute();
            TuSDKMP4MovieMixer.this.a(State.Mixing);
        }
        
        protected void onPostExecute(final Void void1) {
            super.onPostExecute(void1);
            TuSDKMP4MovieMixer.this.onStopeed();
            TuSDKMP4MovieMixer.this.a(TuSDKMP4MovieMixer.this.o());
        }
        
        public void cancel() {
            TuSDKMP4MovieMixer.this.b = State.Cancelled;
            this.cancel(true);
        }
        
        protected void onCancelled(final Void void1) {
            super.onCancelled(void1);
            TuSDKMP4MovieMixer.this.onStopeed();
            TuSDKMP4MovieMixer.this.a(State.Cancelled);
        }
    }
    
    public enum State
    {
        Idle, 
        Decoding, 
        Decoded, 
        Mixing, 
        Cancelled, 
        Failed;
    }
    
    public interface OnMP4MovieMixerDelegate
    {
        void onStateChanged(final State p0);
        
        void onErrrCode(final ErrorCode p0);
        
        void onMixerComplete(final TuSDKVideoResult p0);
    }
    
    public enum ErrorCode
    {
        UnsupportedVideoFormat;
    }
}
