// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

import android.media.MediaCrypto;
import android.view.Surface;
import android.media.MediaFormat;
import java.io.IOException;
//import org.lasque.tusdk.core.common.TuSDKMediaUtils;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import android.media.MediaCodec;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaUtils;

@TargetApi(16)
public abstract class TuSDKMediaDecoder<T extends TuSDKMovieReader> implements TuSDKMediaDecoderInterface
{
    public static final long TIME_US_BASE = 1000000L;
    public static final int INVALID_TRACK_FLAG = -1;
    protected static final int TIMEOUT_USEC = 500;
    protected T mMovieReader;
    protected MediaCodec mVideoDecoder;
    protected MediaCodec mAudioDecoder;
    protected TuSDKMediaDataSource mDataSource;
    private long a;
    
    public TuSDKMediaDecoder(final String s) {
        this(TuSDKMediaDataSource.create(s));
    }
    
    public TuSDKMediaDecoder(final TuSDKMediaDataSource mDataSource) {
        this.mDataSource = mDataSource;
    }
    
    protected void onDecoderError(final TuSDKMediaDecoderError tuSDKMediaDecoderError) {
        TLog.e("decoding error", new Object[0]);
    }
    
    @Override
    public void start() {
        final MediaCodec videoDecoder = this.getVideoDecoder();
        if (videoDecoder != null) {
            videoDecoder.start();
        }
        final MediaCodec audioDecoder = this.getAudioDecoder();
        if (audioDecoder != null) {
            audioDecoder.start();
        }
    }
    
    @Override
    public void stop() {
        this.destroyMediaReader();
        final MediaCodec videoDecoder = this.getVideoDecoder();
        if (videoDecoder != null) {
            videoDecoder.stop();
            videoDecoder.release();
            this.mVideoDecoder = null;
        }
        final MediaCodec audioDecoder = this.getAudioDecoder();
        if (audioDecoder != null) {
            audioDecoder.stop();
            audioDecoder.release();
            this.mAudioDecoder = null;
        }
    }
    
    @Override
    public long getCurrentSampleTimeUs() {
        if (this.mMovieReader == null) {
            return 0L;
        }
        return this.mMovieReader.getSampleTime();
    }
    
    public long getVideoFrameIntervalTimeUs() {
        if (this.a <= 0L) {
            this.a = TuSDKMediaUtils.getVideoFrameIntervalTimeUs(this.mDataSource);
        }
        return this.a;
    }
    
    public void seekTo(final long n) {
        this.seekTo(n, 2);
    }
    
    public void seekTo(final long b, final int n) {
        if (this.mMovieReader == null) {
            return;
        }
        this.mMovieReader.seekTo(Math.max(0L, b), n);
    }
    
    public T createMovieReader() {
        if (this.mDataSource == null) {
            TLog.e("Please set the data source", new Object[0]);
            this.onDecoderError(TuSDKMediaDecoderError.InvalidDataSource);
            return null;
        }
        if (!this.mDataSource.isValid()) {
            TLog.e("Unable to read media file: %s", new Object[] { this.mDataSource.getFilePath() });
            this.onDecoderError(TuSDKMediaDecoderError.InvalidDataSource);
            return null;
        }
        return (T)new TuSDKMovieReader(this.mDataSource);
    }
    
    public MediaCodec createMediaDecoder(final String s) {
        try {
            return MediaCodec.createDecoderByType(s);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    @Override
    public T getMediaReader() {
        return this.mMovieReader;
    }
    
    public MediaCodec createAudioDecoder() {
        final MediaFormat audioTrackFormat = this.getAudioTrackFormat();
        if (audioTrackFormat == null) {
            return null;
        }
        return this.createMediaDecoder(audioTrackFormat.getString("mime"));
    }
    
    public MediaCodec createVideoDecoder(final Surface surface) {
        final MediaFormat videoTrackFormat = this.getVideoTrackFormat();
        if (videoTrackFormat == null) {
            return null;
        }
        try {
            final MediaCodec mediaDecoder = this.createMediaDecoder(videoTrackFormat.getString("mime"));
            mediaDecoder.configure(videoTrackFormat, surface, (MediaCrypto)null, 0);
            return mediaDecoder;
        }
        catch (Exception ex) {
            TLog.e("TuSDKMovieDecoder : Video decoding failed %s", new Object[] { ex.getMessage() });
            this.mVideoDecoder = null;
            this.onDecoderError(TuSDKMediaDecoderError.UnsupportedVideoFormat);
            return null;
        }
    }
    
    @Override
    public int findVideoTrack() {
        if (this.getMediaReader() == null) {
            return -1;
        }
        return this.getMediaReader().findVideoTrack();
    }
    
    @Override
    public int selectVideoTrack() {
        if (this.getMediaReader() == null) {
            return -1;
        }
        return this.getMediaReader().selectVideoTrack();
    }
    
    @Override
    public void unselectVideoTrack() {
        if (this.getMediaReader() == null) {
            return;
        }
        this.getMediaReader().unselectVideoTrack();
    }
    
    @Override
    public MediaFormat getVideoTrackFormat() {
        if (this.getMediaReader() == null) {
            return null;
        }
        return this.getMediaReader().getVideoTrackFormat();
    }
    
    @Override
    public int findAudioTrack() {
        if (this.getMediaReader() == null) {
            return -1;
        }
        return this.getMediaReader().findVideoTrack();
    }
    
    @Override
    public int selectAudioTrack() {
        if (this.getMediaReader() == null) {
            return -1;
        }
        return this.getMediaReader().selectAudioTrack();
    }
    
    @Override
    public void unselectAudioTrack() {
        if (this.getMediaReader() == null) {
            return;
        }
        this.getMediaReader().unselectAudioTrack();
    }
    
    @Override
    public MediaFormat getAudioTrackFormat() {
        if (this.getMediaReader() == null) {
            return null;
        }
        return this.getMediaReader().getAudioTrackFormat();
    }
    
    protected void destroyMediaReader() {
        if (this.mMovieReader != null) {
            this.mMovieReader.destroy();
            this.mMovieReader = null;
        }
    }
    
    @Override
    public void destroy() {
        this.stop();
    }
    
    public enum TuSDKMediaDecoderError
    {
        UnsupportedVideoFormat, 
        InvalidDataSource;
    }
}
