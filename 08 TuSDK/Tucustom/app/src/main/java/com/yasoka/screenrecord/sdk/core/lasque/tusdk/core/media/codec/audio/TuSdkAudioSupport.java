// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

import android.media.MediaCodecInfo;
import java.util.List;

public class TuSdkAudioSupport
{
    public String name;
    public String mimeType;
    public boolean isEncoder;
    public List<MediaCodecInfo.CodecProfileLevel> profileLevel;
    public int maxChannelCount;
    public List<Integer> sampleRates;
    public int bitrateRangeMax;
    public int bitrateRangeMin;
    public boolean bitrateCQ;
    public boolean bitrateVBR;
    public boolean bitrateCBR;
    
    @Override
    public String toString() {
        final StringBuffer append = new StringBuffer("TuSdkAudioSupport").append("{ \n");
        append.append("name: ").append(this.name).append(", \n");
        append.append("mime: ").append(this.mimeType).append(", \n");
        append.append("isEncoder: ").append(this.isEncoder).append(", \n");
        append.append("maxChannelCount: ").append(this.maxChannelCount).append(", \n");
        append.append("bitrateRange: [").append(this.bitrateRangeMin).append("-").append(this.bitrateRangeMax).append("] , \n");
        if (this.sampleRates != null) {
            append.append("sampleRates: [");
            for (int i = 0; i < this.sampleRates.size(); ++i) {
                append.append(this.sampleRates.get(i)).append(", ");
            }
            append.append("], \n");
        }
        if (this.profileLevel != null) {
            append.append("profileLevel: [");
            for (final MediaCodecInfo.CodecProfileLevel codecProfileLevel : this.profileLevel) {
                append.append("{Profile: ").append(String.format("0x%X", codecProfileLevel.profile)).append(", Level: ").append(String.format("0x%X", codecProfileLevel.level)).append("}, ");
            }
            append.append("], \n");
        }
        append.append("bitrateCQ: ").append(this.bitrateCQ).append(", \n");
        append.append("bitrateVBR: ").append(this.bitrateVBR).append(", \n");
        append.append("bitrateCBR: ").append(this.bitrateCBR).append(", \n");
        append.append("}");
        return append.toString();
    }
}
