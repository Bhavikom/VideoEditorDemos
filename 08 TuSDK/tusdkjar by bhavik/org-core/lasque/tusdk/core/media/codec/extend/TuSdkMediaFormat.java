package org.lasque.tusdk.core.media.codec.extend;

import android.annotation.TargetApi;
import android.graphics.RectF;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.os.Build.VERSION;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioSupport;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoQuality;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoSupport;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

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
  public static int getVideoKeyRotation(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return 0;
    }
    return getInteger(paramMediaFormat, "rotation-degrees", 0);
  }
  
  public static TuSdkSize getVideoKeySize(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return null;
    }
    int i = getInteger(paramMediaFormat, "width", 0);
    int j = getInteger(paramMediaFormat, "height", 0);
    return TuSdkSize.create(i, j);
  }
  
  public static RectF getVideoKeyCorpNormalization(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return null;
    }
    int i = getInteger(paramMediaFormat, "crop-top", 0);
    int j = getInteger(paramMediaFormat, "crop-bottom", 0);
    int k = getInteger(paramMediaFormat, "crop-left", 0);
    int m = getInteger(paramMediaFormat, "crop-right", 0);
    int n = getInteger(paramMediaFormat, "width", 0);
    int i1 = getInteger(paramMediaFormat, "height", 0);
    if ((j < 1) || (m < 1) || (n < 1) || (i1 < 1)) {
      return null;
    }
    RectF localRectF = new RectF();
    localRectF.top = (i / i1);
    localRectF.bottom = ((j + 1) / i1);
    localRectF.left = (k / n);
    localRectF.right = ((m + 1) / n);
    return localRectF;
  }
  
  public static long getKeyDurationUs(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return 0L;
    }
    return getLong(paramMediaFormat, "durationUs", 0L);
  }
  
  public static boolean isEnableKeyFrameASAP(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return false;
    }
    int i = getInteger(paramMediaFormat, "i-frame-all", 0);
    return i > 0;
  }
  
  public static int getAudioSampleRate(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return 0;
    }
    return getInteger(paramMediaFormat, "sample-rate", 0);
  }
  
  public static int getAudioChannelCount(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return 0;
    }
    return getInteger(paramMediaFormat, "channel-count", 0);
  }
  
  public static int getAudioBitWidth(MediaFormat paramMediaFormat, int paramInt)
  {
    if (paramMediaFormat == null) {
      return 0;
    }
    return getInteger(paramMediaFormat, "bit-width", paramInt);
  }
  
  public static int getVideoKeyRotation(MediaMetadataRetriever paramMediaMetadataRetriever)
  {
    return getInteger(paramMediaMetadataRetriever, 24, 0);
  }
  
  public static ImageOrientation getVideoRotation(MediaMetadataRetriever paramMediaMetadataRetriever)
  {
    int i = getInteger(paramMediaMetadataRetriever, 24, 0);
    return ImageOrientation.getValue(i, false);
  }
  
  public static TuSdkSize getVideoKeySize(MediaMetadataRetriever paramMediaMetadataRetriever)
  {
    if (paramMediaMetadataRetriever == null) {
      return null;
    }
    int i = getInteger(paramMediaMetadataRetriever, 18, 0);
    int j = getInteger(paramMediaMetadataRetriever, 19, 0);
    return TuSdkSize.create(i, j);
  }
  
  public static long getKeyDuration(MediaMetadataRetriever paramMediaMetadataRetriever)
  {
    if (paramMediaMetadataRetriever == null) {
      return 0L;
    }
    long l = getLong(paramMediaMetadataRetriever, 9, 0L);
    return l;
  }
  
  public static long getKeyBitrate(MediaMetadataRetriever paramMediaMetadataRetriever)
  {
    if (paramMediaMetadataRetriever == null) {
      return 0L;
    }
    long l = getLong(paramMediaMetadataRetriever, 20, 0L);
    return l;
  }
  
  public static long getKeyNumTrack(MediaMetadataRetriever paramMediaMetadataRetriever)
  {
    if (paramMediaMetadataRetriever == null) {
      return 0L;
    }
    long l = getInteger(paramMediaMetadataRetriever, 10, 0);
    return l;
  }
  
  public static int checkVideoDecodec(MediaFormat paramMediaFormat)
  {
    return checkVideoCodec(paramMediaFormat, false);
  }
  
  public static int checkVideoEncodec(MediaFormat paramMediaFormat)
  {
    return checkVideoCodec(paramMediaFormat, true);
  }
  
  public static int checkVideoCodec(MediaFormat paramMediaFormat, boolean paramBoolean)
  {
    if (paramMediaFormat == null)
    {
      TLog.w("%s checkVideoCodec MediaFormat Empty", new Object[] { "TuSdkMediaFormat" });
      return 1;
    }
    String str = getString(paramMediaFormat, "mime", null);
    if (str == null)
    {
      TLog.w("%s checkVideoCodec MediaFormat Mime Empty: %s", new Object[] { "TuSdkMediaFormat", paramMediaFormat });
      return 2;
    }
    TuSdkVideoSupport localTuSdkVideoSupport = TuSdkCodecCapabilities.getVideoSupport(str, paramBoolean);
    if (localTuSdkVideoSupport == null)
    {
      TLog.w("%s checkVideoCodec MediaFormat MediaCodecInfo Empty: %s", new Object[] { "TuSdkMediaFormat", paramMediaFormat });
      return 3;
    }
    TuSdkSize localTuSdkSize = getVideoKeySize(paramMediaFormat);
    if ((localTuSdkSize == null) || (!localTuSdkSize.isSize()))
    {
      TLog.w("%s checkVideoCodec can not support Size: %s", new Object[] { "TuSdkMediaFormat", localTuSdkSize });
      return 16;
    }
    if (localTuSdkSize.maxSide() >= 3840)
    {
      TLog.w("%s can not support input Size 4K", new Object[] { "TuSdkMediaFormat" });
      return 16;
    }
    if (!paramBoolean) {
      return 0;
    }
    if (!localTuSdkVideoSupport.isSupportSize(localTuSdkSize))
    {
      TLog.w("%s checkVideoCodec can not support input Size: %s (support: min[%d * %d] - max[%d * %d]), format: %s, support: %s", new Object[] { "TuSdkMediaFormat", localTuSdkSize, Integer.valueOf(localTuSdkVideoSupport.widthRangeMin), Integer.valueOf(localTuSdkVideoSupport.heightRangeMin), Integer.valueOf(localTuSdkVideoSupport.widthRangeMax), Integer.valueOf(localTuSdkVideoSupport.heightRangeMax), paramMediaFormat, localTuSdkVideoSupport });
      return 16;
    }
    if ((localTuSdkSize.width % localTuSdkVideoSupport.widthAlignment != 0) || (localTuSdkSize.height % localTuSdkVideoSupport.heightAlignment != 0))
    {
      TLog.w("%s checkVideoCodec need size Alignment, Size: %s, align[%d * %d], format: %s, support: %s", new Object[] { "TuSdkMediaFormat", localTuSdkSize, Integer.valueOf(localTuSdkVideoSupport.widthAlignment), Integer.valueOf(localTuSdkVideoSupport.heightAlignment), paramMediaFormat, localTuSdkVideoSupport });
      return 17;
    }
    int i = getInteger(paramMediaFormat, "bitrate", 0);
    if ((i < localTuSdkVideoSupport.bitrateRangeMin) || (i > localTuSdkVideoSupport.bitrateRangeMax))
    {
      TLog.w("%s checkVideoCodec out of range support bitrate, intput[%d], range[%d-%d], format: %s, support: %s", new Object[] { "TuSdkMediaFormat", Integer.valueOf(i), Integer.valueOf(localTuSdkVideoSupport.bitrateRangeMin), Integer.valueOf(localTuSdkVideoSupport.bitrateRangeMax), paramMediaFormat, localTuSdkVideoSupport });
      return 20;
    }
    int j = getInteger(paramMediaFormat, "frame-rate", 0);
    if ((j < localTuSdkVideoSupport.frameRatesMin) || (j > localTuSdkVideoSupport.frameRatesMax))
    {
      TLog.w("%s checkVideoCodec out of range support frameRates, intput[%d], range[%d-%d], format: %s, support: %s", new Object[] { "TuSdkMediaFormat", Integer.valueOf(j), Integer.valueOf(localTuSdkVideoSupport.frameRatesMin), Integer.valueOf(localTuSdkVideoSupport.frameRatesMax), paramMediaFormat, localTuSdkVideoSupport });
      return 21;
    }
    if (localTuSdkVideoSupport.colorFormats != null)
    {
      int k = getInteger(paramMediaFormat, "color-format", 0);
      if (!localTuSdkVideoSupport.colorFormats.contains(Integer.valueOf(k)))
      {
        StringBuffer localStringBuffer = new StringBuffer();
        Iterator localIterator = localTuSdkVideoSupport.colorFormats.iterator();
        while (localIterator.hasNext())
        {
          Integer localInteger = (Integer)localIterator.next();
          localStringBuffer.append(String.format("0x%X, ", new Object[] { localInteger }));
        }
        TLog.w("%s checkVideoCodec miss support colorFormats, intput[%d], range[%s], format: %s, support: %s", new Object[] { "TuSdkMediaFormat", Integer.valueOf(k), localStringBuffer.toString(), paramMediaFormat, localTuSdkVideoSupport });
        return 18;
      }
    }
    if (!paramMediaFormat.containsKey("bitrate-mode"))
    {
      TLog.w("%s checkVideoCodec miss KEY_BITRATE_MODE, format: %s, support: %s", new Object[] { "TuSdkMediaFormat", paramMediaFormat, localTuSdkVideoSupport });
      return 22;
    }
    if (!paramMediaFormat.containsKey("i-frame-interval"))
    {
      TLog.w("%s checkVideoCodec miss KEY_I_FRAME_INTERVAL, format: %s, support: %s", new Object[] { "TuSdkMediaFormat", paramMediaFormat, localTuSdkVideoSupport });
      return 23;
    }
    if (!paramMediaFormat.containsKey("profile"))
    {
      TLog.w("%s checkVideoCodec miss KEY_PROFILE, format: %s, support: %s", new Object[] { "TuSdkMediaFormat", paramMediaFormat, localTuSdkVideoSupport });
      return 24;
    }
    if (!paramMediaFormat.containsKey("level"))
    {
      TLog.w("%s checkVideoCodec miss KEY_LEVEL, format: %s, support: %s", new Object[] { "TuSdkMediaFormat", paramMediaFormat, localTuSdkVideoSupport });
      return 25;
    }
    return 0;
  }
  
  public static MediaFormat buildSafeVideoSurfaceEncodecFormat(TuSdkSize paramTuSdkSize, TuSdkVideoQuality paramTuSdkVideoQuality, boolean paramBoolean)
  {
    return buildSafeVideoEncodecFormat(paramTuSdkSize, paramTuSdkVideoQuality, 2130708361, paramBoolean ? 0 : 1);
  }
  
  public static MediaFormat buildSafeVideoEncodecFormat(TuSdkSize paramTuSdkSize, TuSdkVideoQuality paramTuSdkVideoQuality, int paramInt)
  {
    return buildSafeVideoEncodecFormat(paramTuSdkSize.width, paramTuSdkSize.height, paramTuSdkVideoQuality.getFrameRates(), paramTuSdkVideoQuality.getBitrate(), paramInt, paramTuSdkVideoQuality.getRefer(), 1);
  }
  
  public static MediaFormat buildSafeVideoEncodecFormat(TuSdkSize paramTuSdkSize, TuSdkVideoQuality paramTuSdkVideoQuality, int paramInt1, int paramInt2)
  {
    if (paramTuSdkSize == null) {
      return null;
    }
    if (paramTuSdkVideoQuality == null) {
      paramTuSdkVideoQuality = TuSdkVideoQuality.safeQuality();
    }
    return buildSafeVideoEncodecFormat(paramTuSdkSize.width, paramTuSdkSize.height, paramTuSdkVideoQuality.getFrameRates(), paramTuSdkVideoQuality.getBitrate(), paramInt1, paramTuSdkVideoQuality.getRefer(), paramInt2);
  }
  
  public static MediaFormat buildSafeVideoEncodecFormat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
  {
    TuSdkVideoSupport localTuSdkVideoSupport = TuSdkCodecCapabilities.getVideoSupport(MIMETYPE_VIDEO_AVC, true);
    if (localTuSdkVideoSupport == null)
    {
      TLog.w("%s buildSafeVideoMediaFormat can not match encodec: %s", new Object[] { "TuSdkMediaFormat", MIMETYPE_VIDEO_AVC });
      return null;
    }
    TuSdkSize localTuSdkSize = localTuSdkVideoSupport.getSupportSize(paramInt1, paramInt2);
    MediaFormat localMediaFormat = MediaFormat.createVideoFormat(MIMETYPE_VIDEO_AVC, localTuSdkSize.width, localTuSdkSize.height);
    if ((localTuSdkVideoSupport.colorFormats != null) && (localTuSdkVideoSupport.colorFormats.contains(Integer.valueOf(paramInt5)))) {
      localMediaFormat.setInteger("color-format", paramInt5);
    }
    paramInt3 = Math.min(Math.max(paramInt3, localTuSdkVideoSupport.frameRatesMin), localTuSdkVideoSupport.frameRatesMax);
    localMediaFormat.setInteger("frame-rate", paramInt3);
    int i = TuSdkVideoQuality.dynamicBitrate(localTuSdkSize.width, localTuSdkSize.height, paramInt6);
    if (i > 0) {
      paramInt4 = i;
    }
    if (paramInt7 == 0)
    {
      paramInt4 = (int)(paramInt4 * Math.ceil(paramInt3 / 5.0F));
      localMediaFormat.setInteger("i-frame-all", 1);
    }
    localMediaFormat.setInteger("bitrate", Math.min(Math.max(paramInt4, localTuSdkVideoSupport.bitrateRangeMin), localTuSdkVideoSupport.bitrateRangeMax));
    localMediaFormat.setInteger("i-frame-interval", paramInt7);
    localMediaFormat.setInteger("profile", 1);
    localMediaFormat.setInteger("level", 512);
    localMediaFormat.setInteger("bitrate-mode", 2);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("WIDTH : " + localTuSdkSize.width + " HEIGHT : " + localTuSdkSize.height);
    localStringBuilder.append("\n");
    localStringBuilder.append("MIME_TYPE : " + MIMETYPE_VIDEO_AVC);
    localStringBuilder.append("\n");
    localStringBuilder.append("KEY_BIT_RATE : " + localMediaFormat.getInteger("bitrate"));
    localStringBuilder.append("\n");
    localStringBuilder.append("KEY_FRAME_RATE : " + localMediaFormat.getInteger("frame-rate"));
    localStringBuilder.append("\n");
    localStringBuilder.append("KEY_I_FRAME_INTERVAL : " + localMediaFormat.getInteger("i-frame-interval"));
    localStringBuilder.append("\n");
    localStringBuilder.append("KEY_BITRATE_MODE : " + localMediaFormat.getInteger("bitrate-mode"));
    return localMediaFormat;
  }
  
  public static int checkAudioDecodec(MediaFormat paramMediaFormat)
  {
    return checkAudioCodec(paramMediaFormat, false);
  }
  
  public static int checkAudioEncodec(MediaFormat paramMediaFormat)
  {
    return checkAudioCodec(paramMediaFormat, true);
  }
  
  public static int checkAudioCodec(MediaFormat paramMediaFormat, boolean paramBoolean)
  {
    if (paramMediaFormat == null)
    {
      TLog.w("%s checkAudioCodec MediaFormat Empty", new Object[] { "TuSdkMediaFormat" });
      return 1;
    }
    String str = getString(paramMediaFormat, "mime", null);
    if (str == null)
    {
      TLog.w("%s checkAudioCodec MediaFormat Mime Empty: %s", new Object[] { "TuSdkMediaFormat", paramMediaFormat });
      return 2;
    }
    TuSdkAudioSupport localTuSdkAudioSupport = TuSdkCodecCapabilities.getAudioSupport(str, paramBoolean);
    if (localTuSdkAudioSupport == null)
    {
      TLog.w("%s checkAudioCodec MediaFormat MediaCodecInfo Empty: %s", new Object[] { "TuSdkMediaFormat", paramMediaFormat });
      return 3;
    }
    if (!paramBoolean) {
      return 0;
    }
    int i = getInteger(paramMediaFormat, "bitrate", 0);
    if ((i < localTuSdkAudioSupport.bitrateRangeMin) || (i > localTuSdkAudioSupport.bitrateRangeMax))
    {
      TLog.w("%s checkAudioCodec out of range support bitrate, intput[%d], range[%d-%d], format: %s, support: %s", new Object[] { "TuSdkMediaFormat", Integer.valueOf(i), Integer.valueOf(localTuSdkAudioSupport.bitrateRangeMin), Integer.valueOf(localTuSdkAudioSupport.bitrateRangeMax), paramMediaFormat, localTuSdkAudioSupport });
      return 64;
    }
    int j = getInteger(paramMediaFormat, "sample-rate", 0);
    if ((localTuSdkAudioSupport.sampleRates != null) && (!localTuSdkAudioSupport.sampleRates.contains(Integer.valueOf(j))))
    {
      TLog.w("%s checkAudioCodec out of range support sampleRates, intput[%d], format: %s, support: %s", new Object[] { "TuSdkMediaFormat", Integer.valueOf(j), paramMediaFormat, localTuSdkAudioSupport });
      return 65;
    }
    int k = getInteger(paramMediaFormat, "channel-count", 0);
    if (k > localTuSdkAudioSupport.maxChannelCount)
    {
      TLog.w("%s checkAudioCodec out of range support channelCount, intput[%d], max[%d], format: %s, support: %s", new Object[] { "TuSdkMediaFormat", Integer.valueOf(k), Integer.valueOf(localTuSdkAudioSupport.maxChannelCount), paramMediaFormat, localTuSdkAudioSupport });
      return 66;
    }
    if ((str.equalsIgnoreCase("audio/mp4a-latm")) && (!paramMediaFormat.containsKey("aac-profile")))
    {
      TLog.w("%s checkAudioCodec miss KEY_AAC_PROFILE, format: %s, support: %s", new Object[] { "TuSdkMediaFormat", paramMediaFormat, localTuSdkAudioSupport });
      return 67;
    }
    if (!paramMediaFormat.containsKey("max-input-size")) {
      return 0;
    }
    int m = j * i * k / 8;
    int n = getInteger(paramMediaFormat, "max-input-size", 0);
    if (n < m)
    {
      TLog.w("%s checkAudioCodec out of range support KEY_MAX_INPUT_SIZE, intput[%d], min[%d], format: %s, support: %s", new Object[] { "TuSdkMediaFormat", Integer.valueOf(n), Integer.valueOf(m), paramMediaFormat, localTuSdkAudioSupport });
      return 68;
    }
    return 0;
  }
  
  public static MediaFormat buildSafeAudioEncodecFormat()
  {
    return buildSafeAudioEncodecFormat(96000, 2);
  }
  
  public static MediaFormat buildSafeAudioEncodecFormat(int paramInt1, int paramInt2)
  {
    return buildSafeAudioEncodecFormat(44100, 2, paramInt1, paramInt2);
  }
  
  public static MediaFormat buildSafeAudioEncodecFormat(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    TuSdkAudioSupport localTuSdkAudioSupport = TuSdkCodecCapabilities.getAudioSupport(MIMETYPE_AUDIO_AAC, true);
    if (localTuSdkAudioSupport == null)
    {
      TLog.w("%s buildSafeAudioEncodecFormat can not match encodec: %s", new Object[] { "TuSdkMediaFormat", MIMETYPE_AUDIO_AAC });
      return null;
    }
    if ((localTuSdkAudioSupport.sampleRates != null) && (!localTuSdkAudioSupport.sampleRates.contains(Integer.valueOf(paramInt1))))
    {
      int i = Integer.MAX_VALUE;
      int j = paramInt1;
      Iterator localIterator = localTuSdkAudioSupport.sampleRates.iterator();
      while (localIterator.hasNext())
      {
        Integer localInteger = (Integer)localIterator.next();
        int k = Math.abs(paramInt1 - localInteger.intValue());
        if (k <= i)
        {
          j = localInteger.intValue();
          i = k;
        }
      }
      paramInt1 = j;
    }
    paramInt2 = Math.max(Math.min(paramInt2, localTuSdkAudioSupport.maxChannelCount), 1);
    MediaFormat localMediaFormat = MediaFormat.createAudioFormat(MIMETYPE_AUDIO_AAC, paramInt1, paramInt2);
    localMediaFormat.setInteger("bitrate", Math.min(Math.max(paramInt3, localTuSdkAudioSupport.bitrateRangeMin), localTuSdkAudioSupport.bitrateRangeMax));
    localMediaFormat.setInteger("aac-profile", paramInt4);
    return localMediaFormat;
  }
  
  public static int getInteger(MediaFormat paramMediaFormat, String paramString, int paramInt)
  {
    if ((paramMediaFormat == null) || (paramString == null)) {
      return paramInt;
    }
    if (paramMediaFormat.containsKey(paramString)) {
      return paramMediaFormat.getInteger(paramString);
    }
    return paramInt;
  }
  
  public static long getLong(MediaFormat paramMediaFormat, String paramString, long paramLong)
  {
    if ((paramMediaFormat == null) || (paramString == null)) {
      return paramLong;
    }
    if (paramMediaFormat.containsKey(paramString)) {
      return paramMediaFormat.getLong(paramString);
    }
    return paramLong;
  }
  
  public static String getString(MediaFormat paramMediaFormat, String paramString1, String paramString2)
  {
    if ((paramMediaFormat == null) || (paramString1 == null)) {
      return paramString2;
    }
    if (paramMediaFormat.containsKey(paramString1)) {
      return paramMediaFormat.getString(paramString1);
    }
    return paramString2;
  }
  
  public static int getInteger(MediaMetadataRetriever paramMediaMetadataRetriever, int paramInt1, int paramInt2)
  {
    if (paramMediaMetadataRetriever == null) {
      return paramInt2;
    }
    String str = paramMediaMetadataRetriever.extractMetadata(paramInt1);
    if (str == null) {
      return paramInt2;
    }
    int i = StringHelper.parserInt(str);
    return i;
  }
  
  public static long getLong(MediaMetadataRetriever paramMediaMetadataRetriever, int paramInt, long paramLong)
  {
    if (paramMediaMetadataRetriever == null) {
      return paramLong;
    }
    String str = paramMediaMetadataRetriever.extractMetadata(paramInt);
    if (str == null) {
      return paramLong;
    }
    long l = StringHelper.parserLong(str);
    return l;
  }
  
  public static String logMediaFormat(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return "";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append("\n");
    localStringBuilder.append("width:").append(paramMediaFormat.getInteger("width"));
    localStringBuilder.append("\n");
    localStringBuilder.append("height:").append(paramMediaFormat.getInteger("height"));
    localStringBuilder.append("\n");
    localStringBuilder.append("bit-rate:").append(paramMediaFormat.getInteger("bitrate"));
    localStringBuilder.append("\n");
    localStringBuilder.append("frame-rate:").append(paramMediaFormat.getInteger("frame-rate"));
    localStringBuilder.append("\n");
    localStringBuilder.append("profile:").append(paramMediaFormat.getInteger("profile"));
    localStringBuilder.append("\n");
    localStringBuilder.append("frame_inerval:").append(paramMediaFormat.getInteger("i-frame-interval"));
    localStringBuilder.append("\n");
    localStringBuilder.append("color_format:").append(paramMediaFormat.getInteger("color-format"));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  static
  {
    if (Build.VERSION.SDK_INT < 21)
    {
      MIMETYPE_VIDEO_AVC = "video/avc";
      MIMETYPE_AUDIO_AAC = "audio/mp4a-latm";
    }
    else
    {
      MIMETYPE_VIDEO_AVC = "video/avc";
      MIMETYPE_AUDIO_AAC = "audio/mp4a-latm";
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\extend\TuSdkMediaFormat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */