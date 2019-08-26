// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video;

import java.util.Arrays;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import android.media.MediaFormat;
import android.graphics.RectF;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkH264SPS;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkH264SPS;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

@TargetApi(18)
public class TuSdkVideoInfo
{
    public TuSdkSize size;
    public long durationUs;
    public ImageOrientation orientation;
    public TuSdkH264SPS sps;
    public byte[] pps;
    public int bitrate;
    public int frameRates;
    public long intervalUs;
    public RectF codecCrop;
    public TuSdkSize codecSize;
    
    public TuSdkVideoInfo() {
        this.durationUs = 0L;
    }
    
    public TuSdkVideoInfo(final MediaFormat mediaFormat) {
        this.durationUs = 0L;
        this.setMediaFormat(mediaFormat);
    }
    
    public void setMediaFormat(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return;
        }
        this.size = TuSdkMediaFormat.getVideoKeySize(mediaFormat);
        this.durationUs = TuSdkMediaFormat.getKeyDurationUs(mediaFormat);
        try {
            if (mediaFormat.containsKey("csd-0")) {
                final ByteBuffer byteBuffer = mediaFormat.getByteBuffer("csd-0");
                final byte[] dst = new byte[byteBuffer.capacity()];
                byteBuffer.position(0);
                byteBuffer.get(dst);
                byteBuffer.position(0);
                this.sps = new TuSdkH264SPS(dst);
            }
        }
        catch (Exception ex) {
            TLog.w("read sps error", new Object[0]);
        }
        if (mediaFormat.containsKey("csd-1")) {
            final ByteBuffer byteBuffer2 = mediaFormat.getByteBuffer("csd-1");
            this.pps = new byte[byteBuffer2.capacity()];
            byteBuffer2.position(0);
            byteBuffer2.get(this.pps);
            byteBuffer2.position(0);
        }
    }
    
    public void setCorp(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return;
        }
        this.codecSize = TuSdkMediaFormat.getVideoKeySize(mediaFormat);
        this.codecCrop = TuSdkMediaFormat.getVideoKeyCorpNormalization(mediaFormat);
        if (this.codecSize != null && this.codecCrop != null && !this.codecCrop.contains(0.0f, 0.0f, 1.0f, 1.0f)) {
            this.codecCrop.top = ((this.codecCrop.top > 0.0f) ? (this.codecCrop.top + 2.0f / this.codecSize.height) : 0.0f);
            this.codecCrop.bottom = ((this.codecCrop.bottom < 1.0f) ? (this.codecCrop.bottom - 2.0f / this.codecSize.height) : 1.0f);
            this.codecCrop.left = ((this.codecCrop.left > 0.0f) ? (this.codecCrop.left + 2.0f / this.codecSize.width) : 0.0f);
            this.codecCrop.right = ((this.codecCrop.right < 1.0f) ? (this.codecCrop.right - 2.0f / this.codecSize.width) : 1.0f);
        }
        if (this.sps != null && this.sps.sar_width > 0 && this.sps.sar_height > 0) {
            if (this.codecSize.maxSide() == this.codecSize.width) {
                this.codecSize.height = this.codecSize.height * this.sps.sar_height / this.sps.sar_width;
            }
            else {
                this.codecSize.width = this.codecSize.width * this.sps.sar_width / this.sps.sar_height;
            }
        }
    }
    
    public void setEncodecInfo(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return;
        }
        this.bitrate = TuSdkMediaFormat.getInteger(mediaFormat, "bitrate", 0);
        this.frameRates = TuSdkMediaFormat.getInteger(mediaFormat, "frame-rate", 0);
        this.intervalUs = 1000000 / this.frameRates;
    }
    
    @Override
    public String toString() {
        final StringBuffer append = new StringBuffer("TuSdkVideoInfo").append("{ \n");
        if (this.size != null) {
            append.append("size: ").append(this.size).append(", \n");
        }
        append.append("durationUs: ").append(this.durationUs).append(", \n");
        append.append("bitrate: ").append(this.bitrate).append(", \n");
        append.append("frameRates: ").append(this.frameRates).append(", \n");
        append.append("intervalUs: ").append(this.intervalUs).append(", \n");
        append.append("orientation: ").append(this.orientation).append(", \n");
        if (this.sps != null) {
            append.append("sps: ").append(this.sps).append(", \n");
        }
        if (this.pps != null) {
            append.append("pps: ").append(Arrays.toString(this.pps)).append(", \n");
        }
        append.append("codecCrop: ").append(this.codecCrop).append(", \n");
        append.append("codecSize: ").append(this.codecSize).append(", \n");
        append.append("}");
        return append.toString();
    }
}
