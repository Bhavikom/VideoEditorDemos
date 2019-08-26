// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend;

import android.os.Build;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioSupport;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoQuality;
import java.util.Iterator;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoSupport;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.media.MediaMetadataRetriever;
import android.graphics.RectF;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.media.MediaFormat;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioSupport;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoQuality;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoSupport;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

@TargetApi(18)
public class TuSdkMediaFormat
{
    public static final String DECODEC_VIDEO_TYPE = "video/";
    public static final String DECODEC_AUDIO_TYPE = "audio/";
    public static final String MIMETYPE_VIDEO_AVC;
    public static final String MIMETYPE_AUDIO_AAC;
    public static final String KEY_CROP_TOP = "crop-top";
    public static final String KEY_CROP_BOTTOM = "crop-bottom";
    public static final String KEY_CROP_LEFT = "crop-left";
    public static final String KEY_CROP_RIGHT = "crop-right";
    public static final int CHECK_RESULT_ILLEGAL_STATE = -1;
    public static final int CHECK_RESULT_SUCCEECE = 0;
    public static final int CHECK_RESULT_MEDIA_FORMAT_EMPTY = 1;
    public static final int CHECK_RESULT_MIME_EMPTY = 2;
    public static final int CHECK_RESULT_MEDIA_CODEC_INFO_EMPTY = 3;
    public static final int CHECK_RESULT_VIDEO_OUT_RANGE_SIZE = 16;
    public static final int CHECK_RESULT_VIDEO_MISS_ALIGN_SIZE = 17;
    public static final int CHECK_RESULT_VIDEO_MISS_COLOR_FORMAT = 18;
    public static final int CHECK_RESULT_VIDEO_MISS_PROFILE_LEVEL = 19;
    public static final int CHECK_RESULT_VIDEO_OUT_RANGE_BITRATE = 20;
    public static final int CHECK_RESULT_VIDEO_OUT_RANGE_FRAME_RATES = 21;
    public static final int CHECK_RESULT_VIDEO_MISS_BITRATE_MODE = 22;
    public static final int CHECK_RESULT_VIDEO_MISS_KEY_I_FRAME_INTERVAL = 23;
    public static final int CHECK_RESULT_VIDEO_MISS_PROFILE = 24;
    public static final int CHECK_RESULT_VIDEO_MISS_LEVEL = 25;
    public static final int CHECK_RESULT_AUDIO_OUT_RANGE_BITRATE = 64;
    public static final int CHECK_RESULT_AUDIO_OUT_RANGE_SAMPLERATE = 65;
    public static final int CHECK_RESULT_AUDIO_OUT_RANGE_CHANNEL_COUNT = 66;
    public static final int CHECK_RESULT_AUDIO_MISS_AAC_PROFILE = 67;
    public static final int CHECK_RESULT_AUDIO_OUT_RANGE_MAX_INPUT_SIZE = 68;
    public static final int CHECK_RESULT_INIT_ENCODEC_FAILED = 256;
    
    @TargetApi(23)
    public static int getVideoKeyRotation(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return 0;
        }
        return getInteger(mediaFormat, "rotation-degrees", 0);
    }
    
    public static TuSdkSize getVideoKeySize(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return null;
        }
        return TuSdkSize.create(getInteger(mediaFormat, "width", 0), getInteger(mediaFormat, "height", 0));
    }
    
    public static RectF getVideoKeyCorpNormalization(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return null;
        }
        final int integer = getInteger(mediaFormat, "crop-top", 0);
        final int integer2 = getInteger(mediaFormat, "crop-bottom", 0);
        final int integer3 = getInteger(mediaFormat, "crop-left", 0);
        final int integer4 = getInteger(mediaFormat, "crop-right", 0);
        final int integer5 = getInteger(mediaFormat, "width", 0);
        final int integer6 = getInteger(mediaFormat, "height", 0);
        if (integer2 < 1 || integer4 < 1 || integer5 < 1 || integer6 < 1) {
            return null;
        }
        final RectF rectF = new RectF();
        rectF.top = integer / (float)integer6;
        rectF.bottom = (integer2 + 1) / (float)integer6;
        rectF.left = integer3 / (float)integer5;
        rectF.right = (integer4 + 1) / (float)integer5;
        return rectF;
    }
    
    public static long getKeyDurationUs(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return 0L;
        }
        return getLong(mediaFormat, "durationUs", 0L);
    }
    
    public static boolean isEnableKeyFrameASAP(final MediaFormat mediaFormat) {
        return mediaFormat != null && getInteger(mediaFormat, "i-frame-all", 0) > 0;
    }
    
    public static int getAudioSampleRate(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return 0;
        }
        return getInteger(mediaFormat, "sample-rate", 0);
    }
    
    public static int getAudioChannelCount(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return 0;
        }
        return getInteger(mediaFormat, "channel-count", 0);
    }
    
    public static int getAudioBitWidth(final MediaFormat mediaFormat, final int n) {
        if (mediaFormat == null) {
            return 0;
        }
        return getInteger(mediaFormat, "bit-width", n);
    }
    
    public static int getVideoKeyRotation(final MediaMetadataRetriever mediaMetadataRetriever) {
        return getInteger(mediaMetadataRetriever, 24, 0);
    }
    
    public static ImageOrientation getVideoRotation(final MediaMetadataRetriever mediaMetadataRetriever) {
        return ImageOrientation.getValue(getInteger(mediaMetadataRetriever, 24, 0), false);
    }
    
    public static TuSdkSize getVideoKeySize(final MediaMetadataRetriever mediaMetadataRetriever) {
        if (mediaMetadataRetriever == null) {
            return null;
        }
        return TuSdkSize.create(getInteger(mediaMetadataRetriever, 18, 0), getInteger(mediaMetadataRetriever, 19, 0));
    }
    
    public static long getKeyDuration(final MediaMetadataRetriever mediaMetadataRetriever) {
        if (mediaMetadataRetriever == null) {
            return 0L;
        }
        return getLong(mediaMetadataRetriever, 9, 0L);
    }
    
    public static long getKeyBitrate(final MediaMetadataRetriever mediaMetadataRetriever) {
        if (mediaMetadataRetriever == null) {
            return 0L;
        }
        return getLong(mediaMetadataRetriever, 20, 0L);
    }
    
    public static long getKeyNumTrack(final MediaMetadataRetriever mediaMetadataRetriever) {
        if (mediaMetadataRetriever == null) {
            return 0L;
        }
        return getInteger(mediaMetadataRetriever, 10, 0);
    }
    
    public static int checkVideoDecodec(final MediaFormat mediaFormat) {
        return checkVideoCodec(mediaFormat, false);
    }
    
    public static int checkVideoEncodec(final MediaFormat mediaFormat) {
        return checkVideoCodec(mediaFormat, true);
    }
    
    public static int checkVideoCodec(final MediaFormat mediaFormat, final boolean b) {
        if (mediaFormat == null) {
            TLog.w("%s checkVideoCodec MediaFormat Empty", "TuSdkMediaFormat");
            return 1;
        }
        final String string = getString(mediaFormat, "mime", null);
        if (string == null) {
            TLog.w("%s checkVideoCodec MediaFormat Mime Empty: %s", "TuSdkMediaFormat", mediaFormat);
            return 2;
        }
        final TuSdkVideoSupport videoSupport = TuSdkCodecCapabilities.getVideoSupport(string, b);
        if (videoSupport == null) {
            TLog.w("%s checkVideoCodec MediaFormat MediaCodecInfo Empty: %s", "TuSdkMediaFormat", mediaFormat);
            return 3;
        }
        final TuSdkSize videoKeySize = getVideoKeySize(mediaFormat);
        if (videoKeySize == null || !videoKeySize.isSize()) {
            TLog.w("%s checkVideoCodec can not support Size: %s", "TuSdkMediaFormat", videoKeySize);
            return 16;
        }
        if (videoKeySize.maxSide() >= 3840) {
            TLog.w("%s can not support input Size 4K", "TuSdkMediaFormat");
            return 16;
        }
        if (!b) {
            return 0;
        }
        if (!videoSupport.isSupportSize(videoKeySize)) {
            TLog.w("%s checkVideoCodec can not support input Size: %s (support: min[%d * %d] - max[%d * %d]), format: %s, support: %s", "TuSdkMediaFormat", videoKeySize, videoSupport.widthRangeMin, videoSupport.heightRangeMin, videoSupport.widthRangeMax, videoSupport.heightRangeMax, mediaFormat, videoSupport);
            return 16;
        }
        if (videoKeySize.width % videoSupport.widthAlignment != 0 || videoKeySize.height % videoSupport.heightAlignment != 0) {
            TLog.w("%s checkVideoCodec need size Alignment, Size: %s, align[%d * %d], format: %s, support: %s", "TuSdkMediaFormat", videoKeySize, videoSupport.widthAlignment, videoSupport.heightAlignment, mediaFormat, videoSupport);
            return 17;
        }
        final int integer = getInteger(mediaFormat, "bitrate", 0);
        if (integer < videoSupport.bitrateRangeMin || integer > videoSupport.bitrateRangeMax) {
            TLog.w("%s checkVideoCodec out of range support bitrate, intput[%d], range[%d-%d], format: %s, support: %s", "TuSdkMediaFormat", integer, videoSupport.bitrateRangeMin, videoSupport.bitrateRangeMax, mediaFormat, videoSupport);
            return 20;
        }
        final int integer2 = getInteger(mediaFormat, "frame-rate", 0);
        if (integer2 < videoSupport.frameRatesMin || integer2 > videoSupport.frameRatesMax) {
            TLog.w("%s checkVideoCodec out of range support frameRates, intput[%d], range[%d-%d], format: %s, support: %s", "TuSdkMediaFormat", integer2, videoSupport.frameRatesMin, videoSupport.frameRatesMax, mediaFormat, videoSupport);
            return 21;
        }
        if (videoSupport.colorFormats != null) {
            final int integer3 = getInteger(mediaFormat, "color-format", 0);
            if (!videoSupport.colorFormats.contains(integer3)) {
                final StringBuffer sb = new StringBuffer();
                final Iterator<Integer> iterator = videoSupport.colorFormats.iterator();
                while (iterator.hasNext()) {
                    sb.append(String.format("0x%X, ", iterator.next()));
                }
                TLog.w("%s checkVideoCodec miss support colorFormats, intput[%d], range[%s], format: %s, support: %s", "TuSdkMediaFormat", integer3, sb.toString(), mediaFormat, videoSupport);
                return 18;
            }
        }
        if (!mediaFormat.containsKey("bitrate-mode")) {
            TLog.w("%s checkVideoCodec miss KEY_BITRATE_MODE, format: %s, support: %s", "TuSdkMediaFormat", mediaFormat, videoSupport);
            return 22;
        }
        if (!mediaFormat.containsKey("i-frame-interval")) {
            TLog.w("%s checkVideoCodec miss KEY_I_FRAME_INTERVAL, format: %s, support: %s", "TuSdkMediaFormat", mediaFormat, videoSupport);
            return 23;
        }
        if (!mediaFormat.containsKey("profile")) {
            TLog.w("%s checkVideoCodec miss KEY_PROFILE, format: %s, support: %s", "TuSdkMediaFormat", mediaFormat, videoSupport);
            return 24;
        }
        if (!mediaFormat.containsKey("level")) {
            TLog.w("%s checkVideoCodec miss KEY_LEVEL, format: %s, support: %s", "TuSdkMediaFormat", mediaFormat, videoSupport);
            return 25;
        }
        return 0;
    }
    
    public static MediaFormat buildSafeVideoSurfaceEncodecFormat(final TuSdkSize tuSdkSize, final TuSdkVideoQuality tuSdkVideoQuality, final boolean b) {
        return buildSafeVideoEncodecFormat(tuSdkSize, tuSdkVideoQuality, 2130708361, b ? 0 : 1);
    }
    
    public static MediaFormat buildSafeVideoEncodecFormat(final TuSdkSize tuSdkSize, final TuSdkVideoQuality tuSdkVideoQuality, final int n) {
        return buildSafeVideoEncodecFormat(tuSdkSize.width, tuSdkSize.height, tuSdkVideoQuality.getFrameRates(), tuSdkVideoQuality.getBitrate(), n, tuSdkVideoQuality.getRefer(), 1);
    }
    
    public static MediaFormat buildSafeVideoEncodecFormat(final TuSdkSize tuSdkSize, TuSdkVideoQuality safeQuality, final int n, final int n2) {
        if (tuSdkSize == null) {
            return null;
        }
        if (safeQuality == null) {
            safeQuality = TuSdkVideoQuality.safeQuality();
        }
        return buildSafeVideoEncodecFormat(tuSdkSize.width, tuSdkSize.height, safeQuality.getFrameRates(), safeQuality.getBitrate(), n, safeQuality.getRefer(), n2);
    }
    
    public static MediaFormat buildSafeVideoEncodecFormat(final int n, final int n2, int min, int a, final int i, final int n3, final int n4) {
        final TuSdkVideoSupport videoSupport = TuSdkCodecCapabilities.getVideoSupport(TuSdkMediaFormat.MIMETYPE_VIDEO_AVC, true);
        if (videoSupport == null) {
            TLog.w("%s buildSafeVideoMediaFormat can not match encodec: %s", "TuSdkMediaFormat", TuSdkMediaFormat.MIMETYPE_VIDEO_AVC);
            return null;
        }
        final TuSdkSize supportSize = videoSupport.getSupportSize(n, n2);
        final MediaFormat videoFormat = MediaFormat.createVideoFormat(TuSdkMediaFormat.MIMETYPE_VIDEO_AVC, supportSize.width, supportSize.height);
        if (videoSupport.colorFormats != null && videoSupport.colorFormats.contains(i)) {
            videoFormat.setInteger("color-format", i);
        }
        min = Math.min(Math.max(min, videoSupport.frameRatesMin), videoSupport.frameRatesMax);
        videoFormat.setInteger("frame-rate", min);
        final int dynamicBitrate = TuSdkVideoQuality.dynamicBitrate(supportSize.width, supportSize.height, n3);
        if (dynamicBitrate > 0) {
            a = dynamicBitrate;
        }
        if (n4 == 0) {
            a *= (int)Math.ceil(min / 5.0f);
            videoFormat.setInteger("i-frame-all", 1);
        }
        videoFormat.setInteger("bitrate", Math.min(Math.max(a, videoSupport.bitrateRangeMin), videoSupport.bitrateRangeMax));
        videoFormat.setInteger("i-frame-interval", n4);
        videoFormat.setInteger("profile", 1);
        videoFormat.setInteger("level", 512);
        videoFormat.setInteger("bitrate-mode", 2);
        final StringBuilder sb = new StringBuilder();
        sb.append("WIDTH : " + supportSize.width + " HEIGHT : " + supportSize.height);
        sb.append("\n");
        sb.append("MIME_TYPE : " + TuSdkMediaFormat.MIMETYPE_VIDEO_AVC);
        sb.append("\n");
        sb.append("KEY_BIT_RATE : " + videoFormat.getInteger("bitrate"));
        sb.append("\n");
        sb.append("KEY_FRAME_RATE : " + videoFormat.getInteger("frame-rate"));
        sb.append("\n");
        sb.append("KEY_I_FRAME_INTERVAL : " + videoFormat.getInteger("i-frame-interval"));
        sb.append("\n");
        sb.append("KEY_BITRATE_MODE : " + videoFormat.getInteger("bitrate-mode"));
        return videoFormat;
    }
    
    public static int checkAudioDecodec(final MediaFormat mediaFormat) {
        return checkAudioCodec(mediaFormat, false);
    }
    
    public static int checkAudioEncodec(final MediaFormat mediaFormat) {
        return checkAudioCodec(mediaFormat, true);
    }
    
    public static int checkAudioCodec(final MediaFormat mediaFormat, final boolean b) {
        if (mediaFormat == null) {
            TLog.w("%s checkAudioCodec MediaFormat Empty", "TuSdkMediaFormat");
            return 1;
        }
        final String string = getString(mediaFormat, "mime", null);
        if (string == null) {
            TLog.w("%s checkAudioCodec MediaFormat Mime Empty: %s", "TuSdkMediaFormat", mediaFormat);
            return 2;
        }
        final TuSdkAudioSupport audioSupport = TuSdkCodecCapabilities.getAudioSupport(string, b);
        if (audioSupport == null) {
            TLog.w("%s checkAudioCodec MediaFormat MediaCodecInfo Empty: %s", "TuSdkMediaFormat", mediaFormat);
            return 3;
        }
        if (!b) {
            return 0;
        }
        final int integer = getInteger(mediaFormat, "bitrate", 0);
        if (integer < audioSupport.bitrateRangeMin || integer > audioSupport.bitrateRangeMax) {
            TLog.w("%s checkAudioCodec out of range support bitrate, intput[%d], range[%d-%d], format: %s, support: %s", "TuSdkMediaFormat", integer, audioSupport.bitrateRangeMin, audioSupport.bitrateRangeMax, mediaFormat, audioSupport);
            return 64;
        }
        final int integer2 = getInteger(mediaFormat, "sample-rate", 0);
        if (audioSupport.sampleRates != null && !audioSupport.sampleRates.contains(integer2)) {
            TLog.w("%s checkAudioCodec out of range support sampleRates, intput[%d], format: %s, support: %s", "TuSdkMediaFormat", integer2, mediaFormat, audioSupport);
            return 65;
        }
        final int integer3 = getInteger(mediaFormat, "channel-count", 0);
        if (integer3 > audioSupport.maxChannelCount) {
            TLog.w("%s checkAudioCodec out of range support channelCount, intput[%d], max[%d], format: %s, support: %s", "TuSdkMediaFormat", integer3, audioSupport.maxChannelCount, mediaFormat, audioSupport);
            return 66;
        }
        if (string.equalsIgnoreCase("audio/mp4a-latm") && !mediaFormat.containsKey("aac-profile")) {
            TLog.w("%s checkAudioCodec miss KEY_AAC_PROFILE, format: %s, support: %s", "TuSdkMediaFormat", mediaFormat, audioSupport);
            return 67;
        }
        if (!mediaFormat.containsKey("max-input-size")) {
            return 0;
        }
        final int i = integer2 * integer * integer3 / 8;
        final int integer4 = getInteger(mediaFormat, "max-input-size", 0);
        if (integer4 < i) {
            TLog.w("%s checkAudioCodec out of range support KEY_MAX_INPUT_SIZE, intput[%d], min[%d], format: %s, support: %s", "TuSdkMediaFormat", integer4, i, mediaFormat, audioSupport);
            return 68;
        }
        return 0;
    }
    
    public static MediaFormat buildSafeAudioEncodecFormat() {
        return buildSafeAudioEncodecFormat(96000, 2);
    }
    
    public static MediaFormat buildSafeAudioEncodecFormat(final int n, final int n2) {
        return buildSafeAudioEncodecFormat(44100, 2, n, n2);
    }
    
    public static MediaFormat buildSafeAudioEncodecFormat(int i, int max, final int a, final int n) {
        final TuSdkAudioSupport audioSupport = TuSdkCodecCapabilities.getAudioSupport(TuSdkMediaFormat.MIMETYPE_AUDIO_AAC, true);
        if (audioSupport == null) {
            TLog.w("%s buildSafeAudioEncodecFormat can not match encodec: %s", "TuSdkMediaFormat", TuSdkMediaFormat.MIMETYPE_AUDIO_AAC);
            return null;
        }
        if (audioSupport.sampleRates != null && !audioSupport.sampleRates.contains(i)) {
            int n2 = Integer.MAX_VALUE;
            int intValue = i;
            for (final Integer n3 : audioSupport.sampleRates) {
                final int abs = Math.abs(i - n3);
                if (abs > n2) {
                    continue;
                }
                intValue = n3;
                n2 = abs;
            }
            i = intValue;
        }
        max = Math.max(Math.min(max, audioSupport.maxChannelCount), 1);
        final MediaFormat audioFormat = MediaFormat.createAudioFormat(TuSdkMediaFormat.MIMETYPE_AUDIO_AAC, i, max);
        audioFormat.setInteger("bitrate", Math.min(Math.max(a, audioSupport.bitrateRangeMin), audioSupport.bitrateRangeMax));
        audioFormat.setInteger("aac-profile", n);
        return audioFormat;
    }
    
    public static int getInteger(final MediaFormat mediaFormat, final String s, final int n) {
        if (mediaFormat == null || s == null) {
            return n;
        }
        if (mediaFormat.containsKey(s)) {
            return mediaFormat.getInteger(s);
        }
        return n;
    }
    
    public static long getLong(final MediaFormat mediaFormat, final String s, final long n) {
        if (mediaFormat == null || s == null) {
            return n;
        }
        if (mediaFormat.containsKey(s)) {
            return mediaFormat.getLong(s);
        }
        return n;
    }
    
    public static String getString(final MediaFormat mediaFormat, final String s, final String s2) {
        if (mediaFormat == null || s == null) {
            return s2;
        }
        if (mediaFormat.containsKey(s)) {
            return mediaFormat.getString(s);
        }
        return s2;
    }
    
    public static int getInteger(final MediaMetadataRetriever mediaMetadataRetriever, final int n, final int n2) {
        if (mediaMetadataRetriever == null) {
            return n2;
        }
        final String metadata = mediaMetadataRetriever.extractMetadata(n);
        if (metadata == null) {
            return n2;
        }
        return StringHelper.parserInt(metadata);
    }
    
    public static long getLong(final MediaMetadataRetriever mediaMetadataRetriever, final int n, final long n2) {
        if (mediaMetadataRetriever == null) {
            return n2;
        }
        final String metadata = mediaMetadataRetriever.extractMetadata(n);
        if (metadata == null) {
            return n2;
        }
        return StringHelper.parserLong(metadata);
    }
    
    public static String logMediaFormat(final MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\n");
        sb.append("width:").append(mediaFormat.getInteger("width"));
        sb.append("\n");
        sb.append("height:").append(mediaFormat.getInteger("height"));
        sb.append("\n");
        sb.append("bit-rate:").append(mediaFormat.getInteger("bitrate"));
        sb.append("\n");
        sb.append("frame-rate:").append(mediaFormat.getInteger("frame-rate"));
        sb.append("\n");
        sb.append("profile:").append(mediaFormat.getInteger("profile"));
        sb.append("\n");
        sb.append("frame_inerval:").append(mediaFormat.getInteger("i-frame-interval"));
        sb.append("\n");
        sb.append("color_format:").append(mediaFormat.getInteger("color-format"));
        sb.append("}");
        return sb.toString();
    }
    
    static {
        if (Build.VERSION.SDK_INT < 21) {
            MIMETYPE_VIDEO_AVC = "video/avc";
            MIMETYPE_AUDIO_AAC = "audio/mp4a-latm";
        }
        else {
            MIMETYPE_VIDEO_AVC = "video/avc";
            MIMETYPE_AUDIO_AAC = "audio/mp4a-latm";
        }
    }
}
