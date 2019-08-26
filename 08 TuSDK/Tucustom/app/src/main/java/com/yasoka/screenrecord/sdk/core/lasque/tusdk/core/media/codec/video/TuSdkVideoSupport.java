// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video;

//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
//import org.lasque.tusdk.core.seles.extend.SelesTextureSizeAlign;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.media.MediaCodecInfo;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesTextureSizeAlign;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;

import java.util.List;

public class TuSdkVideoSupport
{
    public String name;
    public String mimeType;
    public boolean isEncoder;
    public List<Integer> colorFormats;
    public List<MediaCodecInfo.CodecProfileLevel> profileLevel;
    public int widthAlignment;
    public int heightAlignment;
    public int widthRangeMax;
    public int widthRangeMin;
    public int heightRangeMax;
    public int heightRangeMin;
    public int bitrateRangeMax;
    public int bitrateRangeMin;
    public int frameRatesMax;
    public int frameRatesMin;
    public boolean bitrateCQ;
    public boolean bitrateVBR;
    public boolean bitrateCBR;
    
    public TuSdkVideoSupport() {
        this.widthAlignment = 2;
        this.heightAlignment = 2;
    }
    
    public boolean isSupportSize(final TuSdkSize tuSdkSize) {
        return tuSdkSize != null && tuSdkSize.width >= this.widthRangeMin && tuSdkSize.width <= this.widthRangeMax && tuSdkSize.height >= this.heightRangeMin && tuSdkSize.height <= this.heightRangeMax;
    }
    
    public TuSdkSize getSupportSize(final TuSdkSize tuSdkSize) {
        if (tuSdkSize == null) {
            return tuSdkSize;
        }
        return this.getSupportSize(tuSdkSize.width, tuSdkSize.height);
    }
    
    public TuSdkSize getSupportSize(final int n, final int n2) {
        final TuSdkSize create = TuSdkSize.create((n < 1) ? this.widthRangeMin : n, (n2 < 1) ? this.heightRangeMin : n2);
        if (create.minSide() < this.widthRangeMin) {
            int widthRangeMin;
            int widthRangeMin2;
            if (create.minSide() == create.width) {
                widthRangeMin = this.widthRangeMin;
                widthRangeMin2 = widthRangeMin / create.width * create.height;
            }
            else {
                widthRangeMin2 = this.widthRangeMin;
                widthRangeMin = widthRangeMin2 / create.height * create.width;
            }
            create.width = widthRangeMin;
            create.height = widthRangeMin2;
        }
        final SelesTextureSizeAlign value = SelesTextureSizeAlign.getValue(this.widthAlignment, true, false);
        final SelesTextureSizeAlign value2 = SelesTextureSizeAlign.getValue(this.heightAlignment, true, false);
        create.width = value.align(create.width);
        create.height = value2.align(create.height);
        final Rect rectWithAspectRatioInsideRect = RectHelper.makeRectWithAspectRatioInsideRect(create, new Rect(0, 0, this.widthRangeMax, this.heightRangeMax));
        if (create.width > rectWithAspectRatioInsideRect.width() || create.height > rectWithAspectRatioInsideRect.height()) {
            create.width = rectWithAspectRatioInsideRect.width();
            create.height = rectWithAspectRatioInsideRect.height();
        }
        final SelesTextureSizeAlign value3 = SelesTextureSizeAlign.getValue(this.widthAlignment, false, false);
        final SelesTextureSizeAlign value4 = SelesTextureSizeAlign.getValue(this.heightAlignment, false, false);
        create.width = value3.align(create.width);
        create.height = value4.align(create.height);
        return create;
    }
    
    @Override
    public String toString() {
        final StringBuffer append = new StringBuffer("TuSdkVideoSupport").append("{ \n");
        append.append("name: ").append(this.name).append(", \n");
        append.append("mime: ").append(this.mimeType).append(", \n");
        append.append("isEncoder: ").append(this.isEncoder).append(", \n");
        append.append("Alignment: [").append(this.widthAlignment).append(", ").append(this.heightAlignment).append("] , \n");
        append.append("widthRange: [").append(this.widthRangeMin).append("-").append(this.widthRangeMax).append("] , \n");
        append.append("heightRange: [").append(this.heightRangeMin).append("-").append(this.heightRangeMax).append("] , \n");
        append.append("bitrateRange: [").append(this.bitrateRangeMin).append("-").append(this.bitrateRangeMax).append("] , \n");
        append.append("frameRates: [").append(this.frameRatesMin).append("-").append(this.frameRatesMax).append("] , \n");
        if (this.colorFormats != null) {
            append.append("colorFormats: [");
            for (int i = 0; i < this.colorFormats.size(); ++i) {
                append.append(String.format("0x%X", this.colorFormats.get(i))).append(", ");
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
