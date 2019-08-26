// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend;

import java.util.List;
import android.media.MediaCodec;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.Arrays;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioSupport;
import android.util.Range;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.TuSdkContext;
import android.os.Build;
import java.util.ArrayList;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoSupport;
import android.media.MediaCodecList;
import android.media.MediaCodecInfo;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioSupport;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoSupport;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class TuSdkCodecCapabilities
{
    public static MediaCodecInfo getCodecInfo(final String anotherString, final boolean b) {
        if (anotherString == null) {
            return null;
        }
        for (int codecCount = MediaCodecList.getCodecCount(), i = 0; i < codecCount; ++i) {
            final MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (codecInfo.isEncoder() == b) {
                final String[] supportedTypes = codecInfo.getSupportedTypes();
                for (int j = 0; j < supportedTypes.length; ++j) {
                    if (supportedTypes[j].equalsIgnoreCase(anotherString)) {
                        return codecInfo;
                    }
                }
            }
        }
        return null;
    }
    
    public static MediaCodecInfo getEncoderCodecInfo(final String s) {
        return getCodecInfo(s, true);
    }
    
    public static MediaCodecInfo getDecoderCodecInfo(final String s) {
        return getCodecInfo(s, false);
    }
    
    public static TuSdkVideoSupport getVideoEncoderSupport(final String s) {
        return getVideoSupport(s, true);
    }
    
    public static TuSdkVideoSupport getVideoDecoderSupport(final String s) {
        return getVideoSupport(s, false);
    }
    
    public static TuSdkVideoSupport getVideoSupport(final String s, final boolean b) {
        return getVideoSupport(s, getCodecInfo(s, b));
    }
    
    public static TuSdkVideoSupport getVideoSupport(final String mimeType, final MediaCodecInfo mediaCodecInfo) {
        if (mediaCodecInfo == null) {
            return null;
        }
        final MediaCodecInfo.CodecCapabilities codecCapabilities = getCodecCapabilities(mimeType, mediaCodecInfo);
        if (codecCapabilities == null) {
            return null;
        }
        final TuSdkVideoSupport tuSdkVideoSupport = new TuSdkVideoSupport();
        tuSdkVideoSupport.name = mediaCodecInfo.getName();
        tuSdkVideoSupport.mimeType = mimeType;
        tuSdkVideoSupport.isEncoder = mediaCodecInfo.isEncoder();
        if (codecCapabilities.colorFormats != null) {
            tuSdkVideoSupport.colorFormats = new ArrayList<Integer>(codecCapabilities.colorFormats.length);
            final int[] colorFormats = codecCapabilities.colorFormats;
            for (int length = colorFormats.length, i = 0; i < length; ++i) {
                tuSdkVideoSupport.colorFormats.add(colorFormats[i]);
            }
        }
        if (codecCapabilities.profileLevels != null) {
            tuSdkVideoSupport.profileLevel = new ArrayList<MediaCodecInfo.CodecProfileLevel>(codecCapabilities.profileLevels.length);
            final MediaCodecInfo.CodecProfileLevel[] profileLevels = codecCapabilities.profileLevels;
            for (int length2 = profileLevels.length, j = 0; j < length2; ++j) {
                tuSdkVideoSupport.profileLevel.add(profileLevels[j]);
            }
        }
        if (Build.VERSION.SDK_INT < 21) {
            a(tuSdkVideoSupport, codecCapabilities);
        }
        else {
            b(tuSdkVideoSupport, codecCapabilities);
        }
        return tuSdkVideoSupport;
    }
    
    private static void a(final TuSdkVideoSupport tuSdkVideoSupport, final MediaCodecInfo.CodecCapabilities codecCapabilities) {
        if (tuSdkVideoSupport == null || codecCapabilities == null) {
            return;
        }
        if (tuSdkVideoSupport.isEncoder) {
            tuSdkVideoSupport.bitrateCBR = true;
        }
        tuSdkVideoSupport.widthAlignment = 2;
        tuSdkVideoSupport.heightAlignment = 2;
        tuSdkVideoSupport.widthRangeMin = 96;
        tuSdkVideoSupport.heightRangeMin = 96;
        final TuSdkSize displaySize = TuSdkContext.getDisplaySize();
        tuSdkVideoSupport.widthRangeMax = displaySize.maxSide();
        tuSdkVideoSupport.heightRangeMax = displaySize.minSide();
        tuSdkVideoSupport.frameRatesMin = 1;
        tuSdkVideoSupport.frameRatesMax = 30;
        tuSdkVideoSupport.bitrateRangeMin = 1;
        tuSdkVideoSupport.bitrateRangeMax = 18000000;
    }
    
    @TargetApi(21)
    private static void b(final TuSdkVideoSupport tuSdkVideoSupport, final MediaCodecInfo.CodecCapabilities codecCapabilities) {
        if (tuSdkVideoSupport == null || codecCapabilities == null) {
            return;
        }
        final MediaCodecInfo.EncoderCapabilities encoderCapabilities = codecCapabilities.getEncoderCapabilities();
        if (tuSdkVideoSupport.isEncoder && encoderCapabilities != null) {
            tuSdkVideoSupport.bitrateCQ = encoderCapabilities.isBitrateModeSupported(0);
            tuSdkVideoSupport.bitrateVBR = encoderCapabilities.isBitrateModeSupported(1);
            tuSdkVideoSupport.bitrateCBR = encoderCapabilities.isBitrateModeSupported(2);
        }
        final MediaCodecInfo.VideoCapabilities videoCapabilities = codecCapabilities.getVideoCapabilities();
        if (videoCapabilities == null) {
            return;
        }
        tuSdkVideoSupport.widthAlignment = videoCapabilities.getWidthAlignment();
        tuSdkVideoSupport.heightAlignment = videoCapabilities.getHeightAlignment();
        final Range supportedWidths = videoCapabilities.getSupportedWidths();
        if (supportedWidths != null) {
            tuSdkVideoSupport.widthRangeMin = (int)supportedWidths.getLower();
            tuSdkVideoSupport.widthRangeMax = (int)supportedWidths.getUpper();
        }
        final Range supportedHeights = videoCapabilities.getSupportedHeights();
        if (supportedHeights != null) {
            tuSdkVideoSupport.heightRangeMin = (int)supportedHeights.getLower();
            tuSdkVideoSupport.heightRangeMax = (int)supportedHeights.getUpper();
        }
        final Range bitrateRange = videoCapabilities.getBitrateRange();
        if (bitrateRange != null) {
            tuSdkVideoSupport.bitrateRangeMin = (int)bitrateRange.getLower();
            tuSdkVideoSupport.bitrateRangeMax = (int)bitrateRange.getUpper();
        }
        final Range supportedFrameRates = videoCapabilities.getSupportedFrameRates();
        if (bitrateRange != null) {
            tuSdkVideoSupport.frameRatesMin = (int)supportedFrameRates.getLower();
            tuSdkVideoSupport.frameRatesMax = (int)supportedFrameRates.getUpper();
        }
    }
    
    public static TuSdkAudioSupport getAudioEncoderSupport(final String s) {
        return getAudioSupport(s, true);
    }
    
    public static TuSdkAudioSupport getAudioDecoderSupport(final String s) {
        return getAudioSupport(s, false);
    }
    
    public static TuSdkAudioSupport getAudioSupport(final String s, final boolean b) {
        return getAudioSupport(s, getCodecInfo(s, b));
    }
    
    public static TuSdkAudioSupport getAudioSupport(final String mimeType, final MediaCodecInfo mediaCodecInfo) {
        if (mediaCodecInfo == null) {
            return null;
        }
        final MediaCodecInfo.CodecCapabilities codecCapabilities = getCodecCapabilities(mimeType, mediaCodecInfo);
        if (codecCapabilities == null) {
            return null;
        }
        final TuSdkAudioSupport tuSdkAudioSupport = new TuSdkAudioSupport();
        tuSdkAudioSupport.name = mediaCodecInfo.getName();
        tuSdkAudioSupport.mimeType = mimeType;
        tuSdkAudioSupport.isEncoder = mediaCodecInfo.isEncoder();
        if (codecCapabilities.profileLevels != null) {
            tuSdkAudioSupport.profileLevel = new ArrayList<MediaCodecInfo.CodecProfileLevel>(codecCapabilities.profileLevels.length);
            final MediaCodecInfo.CodecProfileLevel[] profileLevels = codecCapabilities.profileLevels;
            for (int length = profileLevels.length, i = 0; i < length; ++i) {
                tuSdkAudioSupport.profileLevel.add(profileLevels[i]);
            }
        }
        if (Build.VERSION.SDK_INT < 21) {
            a(tuSdkAudioSupport, codecCapabilities);
        }
        else {
            b(tuSdkAudioSupport, codecCapabilities);
        }
        return tuSdkAudioSupport;
    }
    
    private static void a(final TuSdkAudioSupport tuSdkAudioSupport, final MediaCodecInfo.CodecCapabilities codecCapabilities) {
        if (tuSdkAudioSupport == null || codecCapabilities == null) {
            return;
        }
        if (tuSdkAudioSupport.isEncoder) {
            tuSdkAudioSupport.bitrateCBR = true;
        }
        tuSdkAudioSupport.maxChannelCount = 2;
        tuSdkAudioSupport.sampleRates = Arrays.asList(8000, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000);
        tuSdkAudioSupport.bitrateRangeMax = 510000;
        tuSdkAudioSupport.bitrateRangeMin = 8000;
    }
    
    @TargetApi(21)
    private static void b(final TuSdkAudioSupport tuSdkAudioSupport, final MediaCodecInfo.CodecCapabilities codecCapabilities) {
        if (tuSdkAudioSupport == null || codecCapabilities == null) {
            return;
        }
        final MediaCodecInfo.EncoderCapabilities encoderCapabilities = codecCapabilities.getEncoderCapabilities();
        if (tuSdkAudioSupport.isEncoder && encoderCapabilities != null) {
            tuSdkAudioSupport.bitrateCQ = encoderCapabilities.isBitrateModeSupported(0);
            tuSdkAudioSupport.bitrateVBR = encoderCapabilities.isBitrateModeSupported(1);
            tuSdkAudioSupport.bitrateCBR = encoderCapabilities.isBitrateModeSupported(2);
        }
        final MediaCodecInfo.AudioCapabilities audioCapabilities = codecCapabilities.getAudioCapabilities();
        if (audioCapabilities == null) {
            return;
        }
        tuSdkAudioSupport.maxChannelCount = audioCapabilities.getMaxInputChannelCount();
        final Range bitrateRange = audioCapabilities.getBitrateRange();
        if (bitrateRange != null) {
            tuSdkAudioSupport.bitrateRangeMin = (int)bitrateRange.getLower();
            tuSdkAudioSupport.bitrateRangeMax = (int)bitrateRange.getUpper();
        }
        final Range[] supportedSampleRateRanges = audioCapabilities.getSupportedSampleRateRanges();
        if (supportedSampleRateRanges != null) {
            final ArrayList sampleRates = new ArrayList<Integer>(supportedSampleRateRanges.length);
            final Range[] array = supportedSampleRateRanges;
            for (int length = array.length, i = 0; i < length; ++i) {
                sampleRates.add((Integer)array[i].getUpper());
            }
            tuSdkAudioSupport.sampleRates = (List<Integer>)sampleRates;
        }
    }
    
    public static MediaCodecInfo.CodecCapabilities getCodecCapabilities(final String anotherString, final MediaCodecInfo mediaCodecInfo) {
        if (anotherString == null || mediaCodecInfo == null) {
            return null;
        }
        MediaCodecInfo.CodecCapabilities capabilitiesForType = null;
        for (final String s : mediaCodecInfo.getSupportedTypes()) {
            if (s.equalsIgnoreCase(anotherString)) {
                capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(s);
                break;
            }
        }
        return capabilitiesForType;
    }
    
    public static void logMediaCodecInfo() {
        final int codecCount = MediaCodecList.getCodecCount();
        TLog.d("++++++++++++++++++++ %s prepare CodecCount: %d ++++++++++++++++++++", "TuSdkCodecCapabilities", codecCount);
        for (int i = 0; i < codecCount; ++i) {
            final MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            TLog.d("++++++++ prepare codecInfo[%d]", i);
            logMediaCodecInfo(codecInfo);
            TLog.d("-------- end codecInfo[%d]", i);
        }
        TLog.d("-------------------- %s end CodecCount: %d --------------------", "TuSdkCodecCapabilities", codecCount);
    }
    
    public static void logMediaCodecInfo(final MediaCodecInfo mediaCodecInfo) {
        if (mediaCodecInfo == null) {
            return;
        }
        final String[] supportedTypes = mediaCodecInfo.getSupportedTypes();
        TLog.d("%s(%d): %s", mediaCodecInfo.isEncoder() ? "Encoder" : "DeCoder", supportedTypes.length, mediaCodecInfo.getName());
        for (int i = 0; i < supportedTypes.length; ++i) {
            final String s = supportedTypes[0];
            TLog.d("%s[%d/%d : %s]", mediaCodecInfo.getName(), i, supportedTypes.length, s);
            logCodecCapabilities(mediaCodecInfo.getCapabilitiesForType(s));
        }
    }
    
    public static void logCodecCapabilities(final MediaCodecInfo.CodecCapabilities codecCapabilities) {
        if (codecCapabilities == null) {
            return;
        }
        logStepArray("colorFormats: ", codecCapabilities.colorFormats);
        if (codecCapabilities.profileLevels != null) {
            for (int i = 0; i < codecCapabilities.profileLevels.length; ++i) {
                final MediaCodecInfo.CodecProfileLevel codecProfileLevel = codecCapabilities.profileLevels[i];
                TLog.d("profileLevels[%d/%d]: profile[%d], level[%d]", i, codecCapabilities.profileLevels.length, codecProfileLevel.profile, codecProfileLevel.level);
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            logAudioCapabilities(codecCapabilities.getAudioCapabilities());
            logVideoCapabilities(codecCapabilities.getVideoCapabilities());
            logEncoderCapabilities(codecCapabilities.getEncoderCapabilities());
        }
    }
    
    @TargetApi(21)
    public static void logAudioCapabilities(final MediaCodecInfo.AudioCapabilities audioCapabilities) {
        if (audioCapabilities == null) {
            return;
        }
        TLog.d("AudioCapabilities MaxInputChannelCount: %d", audioCapabilities.getMaxInputChannelCount());
        logRange("BitrateRange", (Range<Integer>)audioCapabilities.getBitrateRange());
        try {
            logStepArray("SupportedSampleRates", audioCapabilities.getSupportedSampleRates());
        }
        catch (Exception ex) {}
        logRangeArray("SupportedSampleRateRanges", (Range<Integer>[])audioCapabilities.getSupportedSampleRateRanges());
    }
    
    @TargetApi(21)
    public static void logVideoCapabilities(final MediaCodecInfo.VideoCapabilities videoCapabilities) {
        if (videoCapabilities == null) {
            return;
        }
        TLog.d("VideoCapabilities Alignment: width: %d | height: %d", videoCapabilities.getWidthAlignment(), videoCapabilities.getHeightAlignment());
        logRange("SupportedWidths", (Range<Integer>)videoCapabilities.getSupportedWidths());
        logRange("SupportedHeights", (Range<Integer>)videoCapabilities.getSupportedHeights());
        logRange("BitrateRange", (Range<Integer>)videoCapabilities.getBitrateRange());
        logRange("SupportedFrameRates", (Range<Integer>)videoCapabilities.getSupportedFrameRates());
    }
    
    @TargetApi(21)
    public static void logEncoderCapabilities(final MediaCodecInfo.EncoderCapabilities encoderCapabilities) {
        if (encoderCapabilities == null) {
            return;
        }
        TLog.d("EncoderCapabilities: CQ[%b], VBR[%b], CBR[%b]", encoderCapabilities.isBitrateModeSupported(0), encoderCapabilities.isBitrateModeSupported(1), encoderCapabilities.isBitrateModeSupported(2));
        logRange("ComplexityRange", (Range<Integer>)encoderCapabilities.getComplexityRange());
    }
    
    @TargetApi(21)
    public static void logRangeArray(final String s, final Range<Integer>[] array) {
        if (array == null) {
            return;
        }
        for (int i = 0; i < array.length; ++i) {
            final Range<Integer> range = array[i];
            TLog.d("%s[%d/%d]: %d - %d", s, i, array.length, range.getLower(), range.getUpper());
        }
    }
    
    @TargetApi(21)
    public static void logRange(final String s, final Range<Integer> range) {
        if (range == null) {
            return;
        }
        TLog.d("%s: %d - %d", s, range.getLower(), range.getUpper());
    }
    
    public static void logStepArray(final String s, final int[] a) {
        if (a == null) {
            return;
        }
        TLog.d("%s: %s", s, Arrays.toString(a));
    }
    
    public static void logBufferInfo(final String s, final MediaCodec.BufferInfo bufferInfo) {
        if (bufferInfo == null) {
            return;
        }
        TLog.d("%s bufferInfo: flags[%d], offset[%d], presentationTimeUs[%d], size[%d]", s, bufferInfo.flags, bufferInfo.offset, bufferInfo.presentationTimeUs, bufferInfo.size);
    }
}
