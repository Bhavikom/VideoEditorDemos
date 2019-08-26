// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

import android.content.ContentValues;
//import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import android.annotation.SuppressLint;
import android.media.MediaFormat;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSDKVideoInfo
{
    public static final int FF_PROFILE_H264_BASELINE = 66;
    public int width;
    public int height;
    public int fps;
    public int profile;
    public int bitrate;
    public int iFrameInterval;
    public ImageOrientation videoOrientation;
    public int degree;
    public boolean existAudioTrack;
    public long durationTimeUs;
    public RectF codecCrop;
    
    public TuSDKVideoInfo() {
        this.videoOrientation = ImageOrientation.Up;
        this.degree = 0;
    }
    
    @SuppressLint({ "InlinedApi" })
    public static TuSDKVideoInfo createWithMediaFormat(final MediaFormat mediaFormat, final boolean existAudioTrack) {
        if (mediaFormat == null) {
            return null;
        }
        final TuSDKVideoInfo tuSDKVideoInfo = new TuSDKVideoInfo();
        tuSDKVideoInfo.existAudioTrack = existAudioTrack;
        tuSDKVideoInfo.width = mediaFormat.getInteger("width");
        tuSDKVideoInfo.height = mediaFormat.getInteger("height");
        if (mediaFormat.containsKey("rotation-degrees")) {
            tuSDKVideoInfo.setVideoRotation(mediaFormat.getInteger("rotation-degrees"));
        }
        if (mediaFormat.containsKey("durationUs")) {
            tuSDKVideoInfo.durationTimeUs = mediaFormat.getLong("durationUs");
        }
        if (mediaFormat.containsKey("frame-rate")) {
            final int integer = mediaFormat.getInteger("frame-rate");
            tuSDKVideoInfo.fps = ((integer == 0) ? 30 : integer);
        }
        if (mediaFormat.containsKey("bitrate")) {
            final int integer2 = mediaFormat.getInteger("bitrate");
            tuSDKVideoInfo.bitrate = ((integer2 == 0) ? 3000 : integer2);
        }
        if (mediaFormat.containsKey("i-frame-interval")) {
            tuSDKVideoInfo.iFrameInterval = mediaFormat.getInteger("i-frame-interval");
        }
        tuSDKVideoInfo.syncCodecCrop(mediaFormat);
        return tuSDKVideoInfo;
    }
    
    public void syncCodecCrop(final MediaFormat mediaFormat) {
        this.codecCrop = TuSdkMediaFormat.getVideoKeyCorpNormalization(mediaFormat);
        final TuSdkSize videoKeySize = TuSdkMediaFormat.getVideoKeySize(mediaFormat);
        if (videoKeySize != null && this.codecCrop != null && !this.codecCrop.contains(0.0f, 0.0f, 1.0f, 1.0f)) {
            this.codecCrop.top = ((this.codecCrop.top > 0.0f) ? (this.codecCrop.top + 2.0f / videoKeySize.height) : 0.0f);
            this.codecCrop.bottom = ((this.codecCrop.bottom < 1.0f) ? (this.codecCrop.bottom - 2.0f / videoKeySize.height) : 1.0f);
            this.codecCrop.left = ((this.codecCrop.left > 0.0f) ? (this.codecCrop.left + 2.0f / videoKeySize.width) : 0.0f);
            this.codecCrop.right = ((this.codecCrop.right < 1.0f) ? (this.codecCrop.right - 2.0f / videoKeySize.width) : 1.0f);
        }
    }
    
    public void setVideoRotation(final int n) {
        if (n == ImageOrientation.Right.getDegree()) {
            this.videoOrientation = ImageOrientation.Right;
        }
        if (n == ImageOrientation.Down.getDegree()) {
            this.videoOrientation = ImageOrientation.Down;
        }
        if (n == ImageOrientation.Left.getDegree()) {
            this.videoOrientation = ImageOrientation.Left;
        }
        if (n == ImageOrientation.Up.getDegree()) {
            this.videoOrientation = ImageOrientation.Up;
        }
    }
    
    public int getBestBitrateMode() {
        if (this.fps <= 20 && this.bitrate < TuSDKVideoEncoderSetting.VideoQuality.RECORD_MEDIUM1.getBitrate()) {
            return 0;
        }
        return 2;
    }
    
    public ContentValues getVideoInfoValues() {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("duration", Long.valueOf(this.durationTimeUs / 1000L));
        contentValues.put("width", Integer.valueOf(this.width));
        contentValues.put("height", Integer.valueOf(this.height));
        contentValues.put("resolution", String.format("%dx%d", this.width, this.height));
        return contentValues;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("width : " + this.width).append("\n").append("height : " + this.height).append("\n").append("fps : " + this.fps).append("\n").append("profile : " + this.profile).append("\n").append("bitrate : " + this.bitrate).append("\n").append("videoOrientation : " + this.videoOrientation).append("\n").append("iFrameInterval : " + this.iFrameInterval).append("\n").append("durationTimeUs : " + this.durationTimeUs).append("\n").append("existAudioTrack : " + this.existAudioTrack);
        return sb.toString();
    }
}
