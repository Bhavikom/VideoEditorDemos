// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder;

//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoFileFrame;
import java.nio.ByteBuffer;
import java.util.UUID;
import android.os.PersistableBundle;
import android.media.DrmInitData;
import android.media.MediaCodec;
import android.media.MediaFormat;
import java.io.IOException;
import android.os.Build;
import java.io.File;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.utils.TLog;
import java.io.FileDescriptor;
import android.content.res.AssetFileDescriptor;
import java.util.Map;
import android.net.Uri;
import android.content.Context;
import android.media.MediaDataSource;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import android.media.MediaExtractor;
//import org.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoFileFrame;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;

@TargetApi(18)
public class TuSdkMediaFileExtractor implements TuSdkMediaExtractor
{
    public static final int VIDEO_THREAD = 1;
    public static final int AUDIO_THREAD = 2;
    private Thread a;
    private boolean b;
    private TuSdkDecodecOperation c;
    private MediaExtractor d;
    private TuSdkMediaDataSource e;
    private long f;
    private boolean g;
    private TuSdkMediaFrameInfo h;
    private int i;
    
    public TuSdkMediaFileExtractor() {
        this.f = 0L;
        this.g = false;
        this.i = 0;
    }
    
    @TargetApi(23)
    public final TuSdkMediaFileExtractor setDataSource(final MediaDataSource mediaDataSource) {
        if (mediaDataSource == null) {
            return this;
        }
        return this.setDataSource(new TuSdkMediaDataSource(mediaDataSource));
    }
    
    public final TuSdkMediaFileExtractor setDataSource(final Context context, final Uri uri, final Map<String, String> map) {
        if (context == null || uri == null) {
            return this;
        }
        return this.setDataSource(new TuSdkMediaDataSource(context, uri, map));
    }
    
    public final TuSdkMediaFileExtractor setDataSource(final String s) {
        return this.setDataSource(s, null);
    }
    
    public final TuSdkMediaFileExtractor setDataSource(final String s, final Map<String, String> map) {
        if (s == null) {
            return this;
        }
        return this.setDataSource(new TuSdkMediaDataSource(s, map));
    }
    
    public final TuSdkMediaFileExtractor setDataSource(final AssetFileDescriptor assetFileDescriptor) {
        if (assetFileDescriptor == null) {
            return this;
        }
        if (assetFileDescriptor.getDeclaredLength() < 0L) {
            return this.setDataSource(assetFileDescriptor.getFileDescriptor());
        }
        return this.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getDeclaredLength());
    }
    
    public final TuSdkMediaFileExtractor setDataSource(final FileDescriptor fileDescriptor) {
        return this.setDataSource(fileDescriptor, 0L, 576460752303423487L);
    }
    
    public final TuSdkMediaFileExtractor setDataSource(final FileDescriptor fileDescriptor, final long n, final long n2) {
        if (fileDescriptor == null) {
            return this;
        }
        return this.setDataSource(new TuSdkMediaDataSource(fileDescriptor, n, n2));
    }
    
    public final TuSdkMediaFileExtractor setDataSource(final TuSdkMediaDataSource e) {
        if (e == null || e.getMediaDataType() == null) {
            TLog.e("%s TuSdkMediaDataSource or TuSdkMediaDataSourceType must be not null !", "TuSdkMediaFileExtractor");
            return null;
        }
        this.e = e;
        return this;
    }
    
    public TuSdkMediaFileExtractor setDecodecOperation(final TuSdkDecodecOperation c) {
        if (this.d != null) {
            TLog.w("%s setDecodecOperation need before play", "TuSdkMediaFileExtractor");
            return this;
        }
        this.c = c;
        return this;
    }
    
    @Override
    public void release() {
        this.pause();
        this.g = true;
        if (this.a != null && !this.a.isInterrupted()) {
            this.a.interrupt();
        }
        this.a = null;
        if (this.d != null) {
            try {
                this.d.release();
            }
            catch (Exception ex) {}
            this.d = null;
        }
        this.c = null;
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
    public void syncPlay() {
        if (this.g) {
            TLog.w("%s play has released", "TuSdkMediaFileExtractor");
            return;
        }
        if (this.e == null || !this.e.isValid()) {
            TLog.w("%s play need setDataSource", "TuSdkMediaFileExtractor");
            return;
        }
        this.d = this.buildExtractor();
    }
    
    @Override
    public void play() {
        if (this.g) {
            TLog.w("%s play has released", "TuSdkMediaFileExtractor");
            return;
        }
        if (this.e == null || !this.e.isValid()) {
            TLog.w("%s play need setDataSource", "TuSdkMediaFileExtractor");
            return;
        }
        if (this.c == null) {
            TLog.w("%s play need before setDecodecOperation!", "TuSdkMediaFileExtractor");
            return;
        }
        if (this.a != null && !this.a.isInterrupted()) {
            TLog.w("%s is running", "TuSdkMediaFileExtractor");
            return;
        }
        this.b = true;
        (this.a = new MediaThread()).start();
    }
    
    public void setThreadType(final int i) {
        this.i = i;
    }
    
    protected void _asyncMediaThread() {
        final TuSdkDecodecOperation c = this.c;
        if (c == null) {
            TLog.e("%s play need before setDecodecOperation", "TuSdkMediaFileExtractor");
            return;
        }
        final MediaExtractor buildExtractor = this.buildExtractor();
        this.d = buildExtractor;
        final MediaExtractor mediaExtractor = buildExtractor;
        if (mediaExtractor == null) {
            TLog.e("%s run failed!", "TuSdkMediaFileExtractor");
            return;
        }
        try {
            if (!c.decodecInit(this)) {
                c.decodecException(new Exception(String.format("%s decodec Init failed", "TuSdkMediaFileExtractor")));
                return;
            }
        }
        catch (Exception ex) {
            c.decodecException(ex);
            return;
        }
        while (!ThreadHelper.interrupted() && !this.g) {
            if (!this.isPlaying()) {
                continue;
            }
            try {
                if (!c.decodecProcessUntilEnd(this)) {
                    continue;
                }
            }
            catch (Exception ex2) {
                TLog.e(ex2);
                c.decodecException(ex2);
            }
            break;
        }
        c.decodecRelease();
        mediaExtractor.release();
        this.release();
    }
    
    protected MediaExtractor buildExtractor() {
        final MediaExtractor mediaExtractor = new MediaExtractor();
        if (this.e == null) {
            TLog.e("%s MediaDataSource must be not null !", "TuSdkMediaFileExtractor");
            return null;
        }
        try {
            if (this.e.getMediaDataType() == TuSdkMediaDataSource.TuSdkMediaDataSourceType.PATH) {
                if (!new File(this.e.getPath()).exists()) {
                    TLog.e("%s buildExtractor setDataSource path is incorrect", "TuSdkMediaFileExtractor");
                    return null;
                }
                if (this.e.getRequestHeaders() != null) {
                    mediaExtractor.setDataSource(this.e.getPath(), (Map)this.e.getRequestHeaders());
                }
                else {
                    mediaExtractor.setDataSource(this.e.getPath());
                }
            }
            else if (this.e.getMediaDataType() == TuSdkMediaDataSource.TuSdkMediaDataSourceType.URI) {
                mediaExtractor.setDataSource(this.e.getContext(), this.e.getUri(), (Map)this.e.getRequestHeaders());
            }
            else if (this.e.getMediaDataType() == TuSdkMediaDataSource.TuSdkMediaDataSourceType.FILE_DESCRIPTOR) {
                mediaExtractor.setDataSource(this.e.getFileDescriptor(), this.e.getFileDescriptorOffset(), this.e.getFileDescriptorLength());
            }
            else if (this.e.getMediaDataType() == TuSdkMediaDataSource.TuSdkMediaDataSourceType.MEDIA_DATA_SOURCE && Build.VERSION.SDK_INT >= 23) {
                mediaExtractor.setDataSource(this.e.getMediaDataSource());
            }
        }
        catch (IOException ex) {
            TLog.e(ex, "%s buildExtractor need setDataSource", "TuSdkMediaFileExtractor");
            return null;
        }
        return mediaExtractor;
    }
    
    @Override
    public MediaFormat getTrackFormat(final int n) {
        if (this.d == null || this.g) {
            return null;
        }
        return this.d.getTrackFormat(n);
    }
    
    @Override
    public int getTrackCount() {
        if (this.d == null || this.g) {
            return 0;
        }
        return this.d.getTrackCount();
    }
    
    @Override
    public void selectTrack(final int n) {
        if (this.d == null || this.g) {
            return;
        }
        this.d.selectTrack(n);
    }
    
    @Override
    public long getSampleTime() {
        if (this.d == null || this.g) {
            return -1L;
        }
        return this.d.getSampleTime();
    }
    
    @Override
    public int getSampleFlags() {
        if (this.d == null || this.g) {
            return -1;
        }
        return this.d.getSampleFlags();
    }
    
    @Override
    public int getSampleTrackIndex() {
        if (this.d == null || this.g) {
            return -1;
        }
        return this.d.getSampleTrackIndex();
    }
    
    @Override
    public boolean getSampleCryptoInfo(final MediaCodec.CryptoInfo cryptoInfo) {
        return this.d != null && !this.g && this.d.getSampleCryptoInfo(cryptoInfo);
    }
    
    @Override
    public long getCachedDuration() {
        if (this.d == null || this.g) {
            return -1L;
        }
        return this.d.getCachedDuration();
    }
    
    @TargetApi(26)
    @Override
    public MediaExtractor.CasInfo getCasInfo(final int n) {
        if (this.d == null || this.g) {
            return null;
        }
        return this.d.getCasInfo(n);
    }
    
    @TargetApi(24)
    @Override
    public DrmInitData getDrmInitData() {
        if (this.d == null || this.g) {
            return null;
        }
        return this.d.getDrmInitData();
    }
    
    @TargetApi(26)
    @Override
    public PersistableBundle getMetrics() {
        if (this.d == null || this.g) {
            return null;
        }
        return this.d.getMetrics();
    }
    
    @Override
    public Map<UUID, byte[]> getPsshInfo() {
        if (this.d == null || this.g) {
            return null;
        }
        return (Map<UUID, byte[]>)this.d.getPsshInfo();
    }
    
    @Override
    public boolean hasCacheReachedEndOfStream() {
        return this.d != null && !this.g && this.d.hasCacheReachedEndOfStream();
    }
    
    @Override
    public boolean isPlaying() {
        return this.b;
    }
    
    @Override
    public void pause() {
        this.b = false;
    }
    
    @Override
    public void resume() {
        this.b = true;
    }
    
    @Override
    public long seekTo(final long n) {
        return this.seekTo(n, 2);
    }
    
    @Override
    public long seekTo(final long n, final boolean b) {
        return this.seekTo(n, b ? 0 : 1);
    }
    
    @Override
    public long seekTo(long n, int n2) {
        if (this.d == null || this.g) {
            return -1L;
        }
        if (n < 0L) {
            n = 0L;
        }
        if (n < 1L && n2 == 0) {
            n2 = 2;
        }
        if (n2 == 0 && n > 0L) {
            --n;
        }
        this.d.seekTo(n, n2);
        return this.d.getSampleTime();
    }
    
    @Override
    public boolean advance() {
        if (this.d == null || this.g) {
            return false;
        }
        final long sampleTime = this.d.getSampleTime();
        final boolean advance = this.d.advance();
        final long sampleTime2 = this.d.getSampleTime();
        if (!advance || sampleTime < 0L || sampleTime2 < 0L || sampleTime2 < sampleTime) {
            return advance;
        }
        this.f = sampleTime2 - sampleTime;
        return advance;
    }
    
    @Override
    public int readSampleData(final ByteBuffer byteBuffer, final int n) {
        if (this.d == null || this.g) {
            return -1;
        }
        return this.d.readSampleData(byteBuffer, n);
    }
    
    @Override
    public long getFrameIntervalUs() {
        return this.f;
    }
    
    @Override
    public long advanceNestest(final long n) {
        if (this.d == null || this.g) {
            return -1L;
        }
        long seekTo = this.seekTo(n, 0);
        if (seekTo < 0L) {
            return -1L;
        }
        if (seekTo == n) {
            return seekTo;
        }
        long sampleTime;
        long abs2;
        for (long abs = Math.abs(n - seekTo); this.advance() && abs != 0L; abs = abs2, seekTo = sampleTime) {
            sampleTime = this.getSampleTime();
            if (sampleTime < 0L) {
                break;
            }
            abs2 = Math.abs(n - sampleTime);
            if (abs < abs2) {
                break;
            }
        }
        return seekTo;
    }
    
    @Override
    public TuSdkMediaFrameInfo getFrameInfo() {
        if (this.h != null) {
            return this.h;
        }
        if (this.d == null || this.g) {
            return null;
        }
        return this.h = TuSdkVideoFileFrame.keyFrameInfo(this);
    }
    
    private class MediaThread extends Thread
    {
        @Override
        public void run() {
            if (TuSdkMediaFileExtractor.this.i == 1) {
                this.setName("TuSdkVideoDecodeThread");
            }
            else if (TuSdkMediaFileExtractor.this.i == 2) {
                this.setName("TuSdkAudioDecodeThread");
            }
            TuSdkMediaFileExtractor.this._asyncMediaThread();
        }
    }
}
