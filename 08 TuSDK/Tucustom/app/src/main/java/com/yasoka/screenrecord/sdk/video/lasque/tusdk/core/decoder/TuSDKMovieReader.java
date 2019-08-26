// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

import android.media.MediaFormat;
import java.io.IOException;
import java.util.Map;
//import org.lasque.tusdk.core.TuSdk;
import android.text.TextUtils;
//import org.lasque.tusdk.core.utils.TLog;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.common.TuSDKMediaUtils;
//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import android.media.MediaExtractor;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaUtils;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;

@TargetApi(16)
public class TuSDKMovieReader
{
    public static final int INVALID_TRACK_FLAG = -1;
    protected MediaExtractor mMediaExtractor;
    protected int mVideoTrackIndex;
    protected int mAudioTrackIndex;
    private TuSDKMediaDataSource a;
    private TuSDKVideoInfo b;
    private TuSDKAudioInfo c;
    private TuSdkTimeRange d;
    
    public TuSDKMovieReader(final TuSDKMediaDataSource a) {
        this.mVideoTrackIndex = -1;
        this.mAudioTrackIndex = -1;
        this.a = a;
        this.mMediaExtractor = this.createMediaExtractor();
    }
    
    public TuSDKVideoInfo getVideoInfo() {
        if (this.b == null) {
            this.b = TuSDKMediaUtils.getVideoInfo(this.a);
        }
        return this.b;
    }
    
    public TuSDKAudioInfo getAudioInfo() {
        if (this.c == null) {
            this.c = TuSDKAudioInfo.createWithMediaFormat(this.getAudioTrackFormat());
        }
        return this.c;
    }
    
    public void setTimeRange(final TuSdkTimeRange d) {
        this.d = d;
    }
    
    public TuSdkTimeRange getTimeRange() {
        if (this.d != null) {
            return this.d;
        }
        if (this.d == null && this.getVideoInfo() != null) {
            this.d = TuSdkTimeRange.makeTimeUsRange(0L, this.getVideoInfo().durationTimeUs);
        }
        if (this.d == null && this.getAudioInfo() != null) {
            this.d = TuSdkTimeRange.makeTimeUsRange(0L, this.getAudioInfo().durationTimeUs);
        }
        return this.d;
    }
    
    protected void destroy() {
        if (this.mMediaExtractor != null) {
            this.mMediaExtractor.release();
            this.mMediaExtractor = null;
        }
        this.mVideoTrackIndex = -1;
        this.mAudioTrackIndex = -1;
    }
    
    public long getSampleTime() {
        if (this.mMediaExtractor == null) {
            return 0L;
        }
        return this.mMediaExtractor.getSampleTime();
    }
    
    public void seekTo(final long n) {
        if (n < 0L) {
            return;
        }
        this.seekTo(n, 2);
    }
    
    public void seekTo(final long b, final int n) {
        if (this.mMediaExtractor == null) {
            return;
        }
        this.mMediaExtractor.seekTo(Math.max(0L, b), n);
    }
    
    public boolean advance() {
        return this.mMediaExtractor != null && this.mMediaExtractor.advance();
    }
    
    public int readSampleData(final ByteBuffer byteBuffer, final int n) {
        if (this.mMediaExtractor == null) {
            return 0;
        }
        final int sampleData = this.mMediaExtractor.readSampleData(byteBuffer, n);
        if (sampleData <= 0) {
            return sampleData;
        }
        if (this.getTimeRange() != null && this.getSampleTime() >= this.getTimeRange().getEndTimeUS()) {
            return -1;
        }
        this.advance();
        return sampleData;
    }
    
    public int getSampleTrackIndex() {
        if (this.mMediaExtractor == null) {
            return -1;
        }
        return this.mMediaExtractor.getSampleTrackIndex();
    }
    
    public int getSampleFlags() {
        return this.mMediaExtractor.getSampleFlags();
    }
    
    public boolean isVideoSampleTrackIndex() {
        return this.getSampleTrackIndex() == this.mVideoTrackIndex;
    }
    
    protected MediaExtractor createMediaExtractor() {
        if (this.a == null) {
            TLog.e("Please set the data source", new Object[0]);
            return null;
        }
        if (!this.a.isValid()) {
            TLog.e("Unable to read media file: %s", new Object[] { this.a.getFilePath() });
            return null;
        }
        final MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            if (!TextUtils.isEmpty((CharSequence)this.a.getFilePath())) {
                mediaExtractor.setDataSource(this.a.getFilePath());
            }
            else {
                mediaExtractor.setDataSource(TuSdk.appContext().getContext(), this.a.getFileUri(), (Map)null);
            }
        }
        catch (IOException ex) {
            this.destroy();
        }
        return mediaExtractor;
    }
    
    public MediaExtractor getMediaExtractor() {
        return this.mMediaExtractor;
    }
    
    public int findVideoTrack() {
        if (this.getMediaExtractor() == null) {
            return -1;
        }
        if (this.mVideoTrackIndex == -1) {
            for (int trackCount = this.mMediaExtractor.getTrackCount(), i = 0; i < trackCount; ++i) {
                if (this.mMediaExtractor.getTrackFormat(i).getString("mime").startsWith("video/")) {
                    return this.mVideoTrackIndex = i;
                }
            }
        }
        return this.mVideoTrackIndex;
    }
    
    public int selectVideoTrack() {
        if (this.getMediaExtractor() == null) {
            return -1;
        }
        this.unselectAudioTrack();
        final int videoTrack = this.findVideoTrack();
        if (videoTrack == -1) {
            return -1;
        }
        this.mMediaExtractor.selectTrack(videoTrack);
        return videoTrack;
    }
    
    public void unselectVideoTrack() {
        final int videoTrack = this.findVideoTrack();
        if (videoTrack == -1) {
            return;
        }
        this.mMediaExtractor.unselectTrack(videoTrack);
    }
    
    public MediaFormat getVideoTrackFormat() {
        if (this.getMediaExtractor() == null) {
            return null;
        }
        final int videoTrack = this.findVideoTrack();
        if (videoTrack == -1) {
            return null;
        }
        return this.mMediaExtractor.getTrackFormat(videoTrack);
    }
    
    public int findAudioTrack() {
        if (this.getMediaExtractor() == null) {
            return -1;
        }
        if (this.mAudioTrackIndex == -1) {
            for (int trackCount = this.mMediaExtractor.getTrackCount(), i = 0; i < trackCount; ++i) {
                if (this.mMediaExtractor.getTrackFormat(i).getString("mime").startsWith("audio/")) {
                    return this.mAudioTrackIndex = i;
                }
            }
        }
        return this.mAudioTrackIndex;
    }
    
    public int selectAudioTrack() {
        if (this.getMediaExtractor() == null) {
            return -1;
        }
        this.unselectVideoTrack();
        final int audioTrack = this.findAudioTrack();
        if (audioTrack == -1) {
            return -1;
        }
        this.mMediaExtractor.selectTrack(audioTrack);
        return audioTrack;
    }
    
    public void unselectAudioTrack() {
        if (this.getMediaExtractor() == null) {
            return;
        }
        final int audioTrack = this.findAudioTrack();
        if (audioTrack == -1) {
            return;
        }
        this.mMediaExtractor.unselectTrack(audioTrack);
    }
    
    public void unselectTrack(final int n) {
        if (this.getMediaExtractor() == null) {
            return;
        }
        this.mMediaExtractor.unselectTrack(n);
    }
    
    public MediaFormat getAudioTrackFormat() {
        if (this.getMediaExtractor() == null) {
            return null;
        }
        final int audioTrack = this.findAudioTrack();
        if (audioTrack == -1) {
            return null;
        }
        return this.mMediaExtractor.getTrackFormat(audioTrack);
    }
}
