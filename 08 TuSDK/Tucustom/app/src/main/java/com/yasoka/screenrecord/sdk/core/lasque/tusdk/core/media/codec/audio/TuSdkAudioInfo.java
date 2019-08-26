// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

import java.util.Arrays;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import android.media.MediaFormat;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;

@TargetApi(18)
public class TuSdkAudioInfo
{
    public int channelCount;
    public long durationUs;
    public int sampleRate;
    public long intervalUs;
    public int bitWidth;
    public byte[] header;
    
    public TuSdkAudioInfo clone() {
        final TuSdkAudioInfo tuSdkAudioInfo = new TuSdkAudioInfo();
        tuSdkAudioInfo.channelCount = this.channelCount;
        tuSdkAudioInfo.durationUs = this.durationUs;
        tuSdkAudioInfo.sampleRate = this.sampleRate;
        tuSdkAudioInfo.intervalUs = this.intervalUs;
        tuSdkAudioInfo.bitWidth = this.bitWidth;
        tuSdkAudioInfo.header = this.header;
        return tuSdkAudioInfo;
    }
    
    public TuSdkAudioInfo() {
        this.channelCount = 0;
        this.durationUs = 0L;
        this.sampleRate = 0;
        this.bitWidth = 16;
    }
    
    public TuSdkAudioInfo(final MediaFormat mediaFormat) {
        this();
        this.setMediaFormat(mediaFormat);
    }
    
    public void setMediaFormat(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return;
        }
        this.channelCount = TuSdkMediaFormat.getAudioChannelCount(mediaFormat);
        this.durationUs = TuSdkMediaFormat.getKeyDurationUs(mediaFormat);
        this.sampleRate = TuSdkMediaFormat.getAudioSampleRate(mediaFormat);
        this.intervalUs = 1024000000 / this.sampleRate;
        this.bitWidth = TuSdkMediaFormat.getAudioBitWidth(mediaFormat, this.bitWidth);
        if (mediaFormat.containsKey("csd-0")) {
            final ByteBuffer byteBuffer = mediaFormat.getByteBuffer("csd-0");
            this.header = new byte[byteBuffer.capacity()];
            byteBuffer.position(0);
            byteBuffer.get(this.header);
            byteBuffer.position(0);
        }
    }
    
    @Override
    public String toString() {
        final StringBuffer append = new StringBuffer("TuSdkAudioInfo").append("{ \n");
        append.append("channelCount: ").append(this.channelCount).append(", \n");
        append.append("durationUs: ").append(this.durationUs).append(", \n");
        append.append("sampleRate: ").append(this.sampleRate).append(", \n");
        append.append("intervalUs: ").append(this.intervalUs).append(", \n");
        append.append("bitWidth: ").append(this.bitWidth).append(", \n");
        if (this.header != null) {
            append.append("header: ").append(Arrays.toString(this.header)).append(", \n");
        }
        append.append("}");
        return append.toString();
    }
}
