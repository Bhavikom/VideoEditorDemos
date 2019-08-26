// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.movie.compresser;

//import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
//import org.lasque.tusdk.core.media.suit.TuSdkMediaSuit;
import java.io.File;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.core.video.TuSDKVideoResult;
//import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.common.TuSDKMediaUtils;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.TuSdk;
//import org.lasque.tusdk.core.delegate.TuSDKVideoSaveDelegate;
import android.media.MediaFormat;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileTranscoder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.suit.TuSdkMediaSuit;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaUtils;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.delegate.TuSDKVideoSaveDelegate;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.video.TuSDKVideoResult;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
//import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileTranscoder;

public class TuSDKMovieCompresser
{
    private TuSdkMediaFileTranscoder a;
    private TuSDKMediaDataSource b;
    private MediaFormat c;
    private MediaFormat d;
    private TuSDKMovieCompresserSetting e;
    private TuSDKVideoSaveDelegate f;
    private String g;
    
    public TuSDKMovieCompresser(final TuSDKMediaDataSource b) {
        this.b = b;
    }
    
    public TuSDKMovieCompresserSetting getCompresserSetting() {
        if (this.e == null) {
            this.e = new TuSDKMovieCompresserSetting();
        }
        return this.e;
    }
    
    public void setCompresserSetting(final TuSDKMovieCompresserSetting e) {
        this.e = e;
    }
    
    public TuSDKMovieCompresser setDelegate(final TuSDKVideoSaveDelegate f) {
        this.f = f;
        return this;
    }
    
    private String a() {
        return TuSdk.getAppTempPath() + "/" + String.format("lsq_%s.mp4", StringHelper.timeStampString());
    }
    
    public String getOutputFilePah() {
        if (this.g == null) {
            this.g = this.a();
        }
        return this.g;
    }
    
    public TuSDKMovieCompresser setOutputFilePath(final String g) {
        this.g = g;
        return this;
    }
    
    protected MediaFormat getOutputVideoFormat() {
        if (this.c != null) {
            return this.c;
        }
        if (this.b == null || !this.b.isValid()) {
            return null;
        }
        final TuSDKVideoInfo videoInfo = TuSDKMediaUtils.getVideoInfo(this.b);
        final int bitrate = videoInfo.bitrate;
        int n = videoInfo.fps;
        TuSdkSize tuSdkSize = TuSdkSize.create(videoInfo.width, videoInfo.height);
        if (videoInfo.videoOrientation == ImageOrientation.Left || videoInfo.videoOrientation == ImageOrientation.Right) {
            tuSdkSize = TuSdkSize.create(tuSdkSize.height, tuSdkSize.width);
        }
        int bitrate2;
        if (this.getCompresserSetting().a != null) {
            bitrate2 = this.getCompresserSetting().a.getBitrate();
            n = this.getCompresserSetting().a.getFps();
        }
        else {
            bitrate2 = (int)(this.getCompresserSetting().b * bitrate);
        }
        return TuSdkMediaFormat.buildSafeVideoEncodecFormat(tuSdkSize.width, tuSdkSize.height, n, bitrate2, 2130708361, 0, 1);
    }
    
    public void setOutputVideoFormat(final MediaFormat c) {
        this.c = c;
    }
    
    protected MediaFormat getOutputAudioFormat() {
        if (this.d != null) {
            return this.d;
        }
        if (this.b == null || !this.b.isValid()) {
            return null;
        }
        return TuSdkMediaFormat.buildSafeAudioEncodecFormat();
    }
    
    public void setOutputAudioFormat(final MediaFormat d) {
        this.d = d;
    }
    
    private void a(final TuSDKVideoResult tuSDKVideoResult) {
        if (this.f != null) {
            this.f.onSaveResult(tuSDKVideoResult);
        }
        StatisticsManger.appendComponent(9449478L);
    }
    
    private void a(final float n) {
        if (this.f == null || n < 0.0f || n > 1.0f) {
            return;
        }
        this.f.onProgressChaned(n);
    }
    
    public void start() {
        if (this.b == null || !this.b.isValid()) {
            TLog.e("Invalidate data source", new Object[0]);
            return;
        }
        final MediaFormat outputVideoFormat = this.getOutputVideoFormat();
        if (outputVideoFormat == null) {
            TLog.e("Invalidate data source", new Object[0]);
            return;
        }
        this.a = TuSdkMediaSuit.transcoding(new TuSdkMediaDataSource(this.b.getFilePath()), this.getOutputFilePah(), outputVideoFormat, this.getOutputAudioFormat(), new TuSdkMediaProgress() {
            public void onProgress(final float n, final TuSdkMediaDataSource tuSdkMediaDataSource, final int n2, final int n3) {
                TuSDKMovieCompresser.this.a(n);
            }
            
            public void onCompleted(final Exception ex, final TuSdkMediaDataSource tuSdkMediaDataSource, final int n) {
                if (ex == null) {
                    final TuSDKVideoResult tuSDKVideoResult = new TuSDKVideoResult();
                    tuSDKVideoResult.videoPath = new File(tuSdkMediaDataSource.getPath());
                    TuSDKMovieCompresser.this.a(tuSDKVideoResult);
                }
                else {
                    if (TuSDKMovieCompresser.this.f == null) {
                        return;
                    }
                    TuSDKMovieCompresser.this.f.onResultFail(ex);
                }
            }
        });
    }
    
    public void stop() {
        if (this.a != null) {
            this.a.stop();
        }
        this.a = null;
    }
    
    public static class TuSDKMovieCompresserSetting
    {
        private TuSDKVideoEncoderSetting.VideoQuality a;
        private float b;
        
        public TuSDKMovieCompresserSetting() {
            this.b = 0.5f;
        }
        
        public TuSDKMovieCompresserSetting setVideoQuality(final TuSDKVideoEncoderSetting.VideoQuality a) {
            this.a = a;
            return this;
        }
        
        public TuSDKMovieCompresserSetting setScale(final float a) {
            if (a <= 0.0f) {
                return this;
            }
            this.b = Math.min(a, 2.0f);
            return this;
        }
    }
}
