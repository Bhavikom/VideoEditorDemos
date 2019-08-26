package org.lasque.tusdk.core.media.codec.extend;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.AudioCapabilities;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.media.MediaCodecInfo.EncoderCapabilities;
import android.media.MediaCodecInfo.VideoCapabilities;
import android.media.MediaCodecList;
import android.os.Build.VERSION;
import android.util.Range;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioSupport;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoSupport;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class TuSdkCodecCapabilities
{
  public static MediaCodecInfo getCodecInfo(String paramString, boolean paramBoolean)
  {
    if (paramString == null) {
      return null;
    }
    int i = MediaCodecList.getCodecCount();
    for (int j = 0; j < i; j++)
    {
      MediaCodecInfo localMediaCodecInfo = MediaCodecList.getCodecInfoAt(j);
      if (localMediaCodecInfo.isEncoder() == paramBoolean)
      {
        String[] arrayOfString = localMediaCodecInfo.getSupportedTypes();
        for (int k = 0; k < arrayOfString.length; k++) {
          if (arrayOfString[k].equalsIgnoreCase(paramString)) {
            return localMediaCodecInfo;
          }
        }
      }
    }
    return null;
  }
  
  public static MediaCodecInfo getEncoderCodecInfo(String paramString)
  {
    return getCodecInfo(paramString, true);
  }
  
  public static MediaCodecInfo getDecoderCodecInfo(String paramString)
  {
    return getCodecInfo(paramString, false);
  }
  
  public static TuSdkVideoSupport getVideoEncoderSupport(String paramString)
  {
    return getVideoSupport(paramString, true);
  }
  
  public static TuSdkVideoSupport getVideoDecoderSupport(String paramString)
  {
    return getVideoSupport(paramString, false);
  }
  
  public static TuSdkVideoSupport getVideoSupport(String paramString, boolean paramBoolean)
  {
    MediaCodecInfo localMediaCodecInfo = getCodecInfo(paramString, paramBoolean);
    return getVideoSupport(paramString, localMediaCodecInfo);
  }
  
  public static TuSdkVideoSupport getVideoSupport(String paramString, MediaCodecInfo paramMediaCodecInfo)
  {
    if (paramMediaCodecInfo == null) {
      return null;
    }
    MediaCodecInfo.CodecCapabilities localCodecCapabilities = getCodecCapabilities(paramString, paramMediaCodecInfo);
    if (localCodecCapabilities == null) {
      return null;
    }
    TuSdkVideoSupport localTuSdkVideoSupport = new TuSdkVideoSupport();
    localTuSdkVideoSupport.name = paramMediaCodecInfo.getName();
    localTuSdkVideoSupport.mimeType = paramString;
    localTuSdkVideoSupport.isEncoder = paramMediaCodecInfo.isEncoder();
    if (localCodecCapabilities.colorFormats != null)
    {
      localTuSdkVideoSupport.colorFormats = new ArrayList(localCodecCapabilities.colorFormats.length);
      for (int k : localCodecCapabilities.colorFormats) {
        localTuSdkVideoSupport.colorFormats.add(Integer.valueOf(k));
      }
    }
    if (localCodecCapabilities.profileLevels != null)
    {
      localTuSdkVideoSupport.profileLevel = new ArrayList(localCodecCapabilities.profileLevels.length);
      for (Object localObject2 : localCodecCapabilities.profileLevels) {
        localTuSdkVideoSupport.profileLevel.add(localObject2);
      }
    }
    if (Build.VERSION.SDK_INT < 21) {
      a(localTuSdkVideoSupport, localCodecCapabilities);
    } else {
      b(localTuSdkVideoSupport, localCodecCapabilities);
    }
    return localTuSdkVideoSupport;
  }
  
  private static void a(TuSdkVideoSupport paramTuSdkVideoSupport, MediaCodecInfo.CodecCapabilities paramCodecCapabilities)
  {
    if ((paramTuSdkVideoSupport == null) || (paramCodecCapabilities == null)) {
      return;
    }
    if (paramTuSdkVideoSupport.isEncoder) {
      paramTuSdkVideoSupport.bitrateCBR = true;
    }
    paramTuSdkVideoSupport.widthAlignment = 2;
    paramTuSdkVideoSupport.heightAlignment = 2;
    paramTuSdkVideoSupport.widthRangeMin = 96;
    paramTuSdkVideoSupport.heightRangeMin = 96;
    TuSdkSize localTuSdkSize = TuSdkContext.getDisplaySize();
    paramTuSdkVideoSupport.widthRangeMax = localTuSdkSize.maxSide();
    paramTuSdkVideoSupport.heightRangeMax = localTuSdkSize.minSide();
    paramTuSdkVideoSupport.frameRatesMin = 1;
    paramTuSdkVideoSupport.frameRatesMax = 30;
    paramTuSdkVideoSupport.bitrateRangeMin = 1;
    paramTuSdkVideoSupport.bitrateRangeMax = 18000000;
  }
  
  @TargetApi(21)
  private static void b(TuSdkVideoSupport paramTuSdkVideoSupport, MediaCodecInfo.CodecCapabilities paramCodecCapabilities)
  {
    if ((paramTuSdkVideoSupport == null) || (paramCodecCapabilities == null)) {
      return;
    }
    MediaCodecInfo.EncoderCapabilities localEncoderCapabilities = paramCodecCapabilities.getEncoderCapabilities();
    if ((paramTuSdkVideoSupport.isEncoder) && (localEncoderCapabilities != null))
    {
      paramTuSdkVideoSupport.bitrateCQ = localEncoderCapabilities.isBitrateModeSupported(0);
      paramTuSdkVideoSupport.bitrateVBR = localEncoderCapabilities.isBitrateModeSupported(1);
      paramTuSdkVideoSupport.bitrateCBR = localEncoderCapabilities.isBitrateModeSupported(2);
    }
    MediaCodecInfo.VideoCapabilities localVideoCapabilities = paramCodecCapabilities.getVideoCapabilities();
    if (localVideoCapabilities == null) {
      return;
    }
    paramTuSdkVideoSupport.widthAlignment = localVideoCapabilities.getWidthAlignment();
    paramTuSdkVideoSupport.heightAlignment = localVideoCapabilities.getHeightAlignment();
    Range localRange1 = localVideoCapabilities.getSupportedWidths();
    if (localRange1 != null)
    {
      paramTuSdkVideoSupport.widthRangeMin = ((Integer)localRange1.getLower()).intValue();
      paramTuSdkVideoSupport.widthRangeMax = ((Integer)localRange1.getUpper()).intValue();
    }
    Range localRange2 = localVideoCapabilities.getSupportedHeights();
    if (localRange2 != null)
    {
      paramTuSdkVideoSupport.heightRangeMin = ((Integer)localRange2.getLower()).intValue();
      paramTuSdkVideoSupport.heightRangeMax = ((Integer)localRange2.getUpper()).intValue();
    }
    Range localRange3 = localVideoCapabilities.getBitrateRange();
    if (localRange3 != null)
    {
      paramTuSdkVideoSupport.bitrateRangeMin = ((Integer)localRange3.getLower()).intValue();
      paramTuSdkVideoSupport.bitrateRangeMax = ((Integer)localRange3.getUpper()).intValue();
    }
    Range localRange4 = localVideoCapabilities.getSupportedFrameRates();
    if (localRange3 != null)
    {
      paramTuSdkVideoSupport.frameRatesMin = ((Integer)localRange4.getLower()).intValue();
      paramTuSdkVideoSupport.frameRatesMax = ((Integer)localRange4.getUpper()).intValue();
    }
  }
  
  public static TuSdkAudioSupport getAudioEncoderSupport(String paramString)
  {
    return getAudioSupport(paramString, true);
  }
  
  public static TuSdkAudioSupport getAudioDecoderSupport(String paramString)
  {
    return getAudioSupport(paramString, false);
  }
  
  public static TuSdkAudioSupport getAudioSupport(String paramString, boolean paramBoolean)
  {
    MediaCodecInfo localMediaCodecInfo = getCodecInfo(paramString, paramBoolean);
    return getAudioSupport(paramString, localMediaCodecInfo);
  }
  
  public static TuSdkAudioSupport getAudioSupport(String paramString, MediaCodecInfo paramMediaCodecInfo)
  {
    if (paramMediaCodecInfo == null) {
      return null;
    }
    MediaCodecInfo.CodecCapabilities localCodecCapabilities = getCodecCapabilities(paramString, paramMediaCodecInfo);
    if (localCodecCapabilities == null) {
      return null;
    }
    TuSdkAudioSupport localTuSdkAudioSupport = new TuSdkAudioSupport();
    localTuSdkAudioSupport.name = paramMediaCodecInfo.getName();
    localTuSdkAudioSupport.mimeType = paramString;
    localTuSdkAudioSupport.isEncoder = paramMediaCodecInfo.isEncoder();
    if (localCodecCapabilities.profileLevels != null)
    {
      localTuSdkAudioSupport.profileLevel = new ArrayList(localCodecCapabilities.profileLevels.length);
      for (MediaCodecInfo.CodecProfileLevel localCodecProfileLevel : localCodecCapabilities.profileLevels) {
        localTuSdkAudioSupport.profileLevel.add(localCodecProfileLevel);
      }
    }
    if (Build.VERSION.SDK_INT < 21) {
      a(localTuSdkAudioSupport, localCodecCapabilities);
    } else {
      b(localTuSdkAudioSupport, localCodecCapabilities);
    }
    return localTuSdkAudioSupport;
  }
  
  private static void a(TuSdkAudioSupport paramTuSdkAudioSupport, MediaCodecInfo.CodecCapabilities paramCodecCapabilities)
  {
    if ((paramTuSdkAudioSupport == null) || (paramCodecCapabilities == null)) {
      return;
    }
    if (paramTuSdkAudioSupport.isEncoder) {
      paramTuSdkAudioSupport.bitrateCBR = true;
    }
    paramTuSdkAudioSupport.maxChannelCount = 2;
    paramTuSdkAudioSupport.sampleRates = Arrays.asList(new Integer[] { Integer.valueOf(8000), Integer.valueOf(11025), Integer.valueOf(12000), Integer.valueOf(16000), Integer.valueOf(22050), Integer.valueOf(24000), Integer.valueOf(32000), Integer.valueOf(44100), Integer.valueOf(48000) });
    paramTuSdkAudioSupport.bitrateRangeMax = 510000;
    paramTuSdkAudioSupport.bitrateRangeMin = 8000;
  }
  
  @TargetApi(21)
  private static void b(TuSdkAudioSupport paramTuSdkAudioSupport, MediaCodecInfo.CodecCapabilities paramCodecCapabilities)
  {
    if ((paramTuSdkAudioSupport == null) || (paramCodecCapabilities == null)) {
      return;
    }
    MediaCodecInfo.EncoderCapabilities localEncoderCapabilities = paramCodecCapabilities.getEncoderCapabilities();
    if ((paramTuSdkAudioSupport.isEncoder) && (localEncoderCapabilities != null))
    {
      paramTuSdkAudioSupport.bitrateCQ = localEncoderCapabilities.isBitrateModeSupported(0);
      paramTuSdkAudioSupport.bitrateVBR = localEncoderCapabilities.isBitrateModeSupported(1);
      paramTuSdkAudioSupport.bitrateCBR = localEncoderCapabilities.isBitrateModeSupported(2);
    }
    MediaCodecInfo.AudioCapabilities localAudioCapabilities = paramCodecCapabilities.getAudioCapabilities();
    if (localAudioCapabilities == null) {
      return;
    }
    paramTuSdkAudioSupport.maxChannelCount = localAudioCapabilities.getMaxInputChannelCount();
    Range localRange1 = localAudioCapabilities.getBitrateRange();
    if (localRange1 != null)
    {
      paramTuSdkAudioSupport.bitrateRangeMin = ((Integer)localRange1.getLower()).intValue();
      paramTuSdkAudioSupport.bitrateRangeMax = ((Integer)localRange1.getUpper()).intValue();
    }
    Range[] arrayOfRange1 = localAudioCapabilities.getSupportedSampleRateRanges();
    if (arrayOfRange1 != null)
    {
      ArrayList localArrayList = new ArrayList(arrayOfRange1.length);
      for (Range localRange2 : arrayOfRange1) {
        localArrayList.add(localRange2.getUpper());
      }
      paramTuSdkAudioSupport.sampleRates = localArrayList;
    }
  }
  
  public static MediaCodecInfo.CodecCapabilities getCodecCapabilities(String paramString, MediaCodecInfo paramMediaCodecInfo)
  {
    if ((paramString == null) || (paramMediaCodecInfo == null)) {
      return null;
    }
    MediaCodecInfo.CodecCapabilities localCodecCapabilities = null;
    String[] arrayOfString1 = paramMediaCodecInfo.getSupportedTypes();
    for (String str : arrayOfString1) {
      if (str.equalsIgnoreCase(paramString))
      {
        localCodecCapabilities = paramMediaCodecInfo.getCapabilitiesForType(str);
        break;
      }
    }
    return localCodecCapabilities;
  }
  
  public static void logMediaCodecInfo()
  {
    int i = MediaCodecList.getCodecCount();
    TLog.d("++++++++++++++++++++ %s prepare CodecCount: %d ++++++++++++++++++++", new Object[] { "TuSdkCodecCapabilities", Integer.valueOf(i) });
    for (int j = 0; j < i; j++)
    {
      MediaCodecInfo localMediaCodecInfo = MediaCodecList.getCodecInfoAt(j);
      TLog.d("++++++++ prepare codecInfo[%d]", new Object[] { Integer.valueOf(j) });
      logMediaCodecInfo(localMediaCodecInfo);
      TLog.d("-------- end codecInfo[%d]", new Object[] { Integer.valueOf(j) });
    }
    TLog.d("-------------------- %s end CodecCount: %d --------------------", new Object[] { "TuSdkCodecCapabilities", Integer.valueOf(i) });
  }
  
  public static void logMediaCodecInfo(MediaCodecInfo paramMediaCodecInfo)
  {
    if (paramMediaCodecInfo == null) {
      return;
    }
    String[] arrayOfString = paramMediaCodecInfo.getSupportedTypes();
    TLog.d("%s(%d): %s", new Object[] { paramMediaCodecInfo.isEncoder() ? "Encoder" : "DeCoder", Integer.valueOf(arrayOfString.length), paramMediaCodecInfo.getName() });
    for (int i = 0; i < arrayOfString.length; i++)
    {
      String str = arrayOfString[0];
      TLog.d("%s[%d/%d : %s]", new Object[] { paramMediaCodecInfo.getName(), Integer.valueOf(i), Integer.valueOf(arrayOfString.length), str });
      MediaCodecInfo.CodecCapabilities localCodecCapabilities = paramMediaCodecInfo.getCapabilitiesForType(str);
      logCodecCapabilities(localCodecCapabilities);
    }
  }
  
  public static void logCodecCapabilities(MediaCodecInfo.CodecCapabilities paramCodecCapabilities)
  {
    if (paramCodecCapabilities == null) {
      return;
    }
    logStepArray("colorFormats: ", paramCodecCapabilities.colorFormats);
    Object localObject;
    if (paramCodecCapabilities.profileLevels != null) {
      for (int i = 0; i < paramCodecCapabilities.profileLevels.length; i++)
      {
        localObject = paramCodecCapabilities.profileLevels[i];
        TLog.d("profileLevels[%d/%d]: profile[%d], level[%d]", new Object[] { Integer.valueOf(i), Integer.valueOf(paramCodecCapabilities.profileLevels.length), Integer.valueOf(((MediaCodecInfo.CodecProfileLevel)localObject).profile), Integer.valueOf(((MediaCodecInfo.CodecProfileLevel)localObject).level) });
      }
    }
    if (Build.VERSION.SDK_INT >= 21)
    {
      MediaCodecInfo.AudioCapabilities localAudioCapabilities = paramCodecCapabilities.getAudioCapabilities();
      logAudioCapabilities(localAudioCapabilities);
      localObject = paramCodecCapabilities.getVideoCapabilities();
      logVideoCapabilities((MediaCodecInfo.VideoCapabilities)localObject);
      MediaCodecInfo.EncoderCapabilities localEncoderCapabilities = paramCodecCapabilities.getEncoderCapabilities();
      logEncoderCapabilities(localEncoderCapabilities);
    }
  }
  
  @TargetApi(21)
  public static void logAudioCapabilities(MediaCodecInfo.AudioCapabilities paramAudioCapabilities)
  {
    if (paramAudioCapabilities == null) {
      return;
    }
    TLog.d("AudioCapabilities MaxInputChannelCount: %d", new Object[] { Integer.valueOf(paramAudioCapabilities.getMaxInputChannelCount()) });
    Range localRange = paramAudioCapabilities.getBitrateRange();
    logRange("BitrateRange", localRange);
    try
    {
      int[] arrayOfInt = paramAudioCapabilities.getSupportedSampleRates();
      logStepArray("SupportedSampleRates", arrayOfInt);
    }
    catch (Exception localException) {}
    Range[] arrayOfRange = paramAudioCapabilities.getSupportedSampleRateRanges();
    logRangeArray("SupportedSampleRateRanges", arrayOfRange);
  }
  
  @TargetApi(21)
  public static void logVideoCapabilities(MediaCodecInfo.VideoCapabilities paramVideoCapabilities)
  {
    if (paramVideoCapabilities == null) {
      return;
    }
    TLog.d("VideoCapabilities Alignment: width: %d | height: %d", new Object[] { Integer.valueOf(paramVideoCapabilities.getWidthAlignment()), Integer.valueOf(paramVideoCapabilities.getHeightAlignment()) });
    Range localRange1 = paramVideoCapabilities.getSupportedWidths();
    logRange("SupportedWidths", localRange1);
    Range localRange2 = paramVideoCapabilities.getSupportedHeights();
    logRange("SupportedHeights", localRange2);
    Range localRange3 = paramVideoCapabilities.getBitrateRange();
    logRange("BitrateRange", localRange3);
    Range localRange4 = paramVideoCapabilities.getSupportedFrameRates();
    logRange("SupportedFrameRates", localRange4);
  }
  
  @TargetApi(21)
  public static void logEncoderCapabilities(MediaCodecInfo.EncoderCapabilities paramEncoderCapabilities)
  {
    if (paramEncoderCapabilities == null) {
      return;
    }
    TLog.d("EncoderCapabilities: CQ[%b], VBR[%b], CBR[%b]", new Object[] { Boolean.valueOf(paramEncoderCapabilities.isBitrateModeSupported(0)), Boolean.valueOf(paramEncoderCapabilities.isBitrateModeSupported(1)), Boolean.valueOf(paramEncoderCapabilities.isBitrateModeSupported(2)) });
    Range localRange = paramEncoderCapabilities.getComplexityRange();
    logRange("ComplexityRange", localRange);
  }
  
  @TargetApi(21)
  public static void logRangeArray(String paramString, Range<Integer>[] paramArrayOfRange)
  {
    if (paramArrayOfRange == null) {
      return;
    }
    for (int i = 0; i < paramArrayOfRange.length; i++)
    {
      Range<Integer> localRange = paramArrayOfRange[i];
      TLog.d("%s[%d/%d]: %d - %d", new Object[] { paramString, Integer.valueOf(i), Integer.valueOf(paramArrayOfRange.length), localRange.getLower(), localRange.getUpper() });
    }
  }
  
  @TargetApi(21)
  public static void logRange(String paramString, Range<Integer> paramRange)
  {
    if (paramRange == null) {
      return;
    }
    TLog.d("%s: %d - %d", new Object[] { paramString, paramRange.getLower(), paramRange.getUpper() });
  }
  
  public static void logStepArray(String paramString, int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null) {
      return;
    }
    TLog.d("%s: %s", new Object[] { paramString, Arrays.toString(paramArrayOfInt) });
  }
  
  public static void logBufferInfo(String paramString, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (paramBufferInfo == null) {
      return;
    }
    TLog.d("%s bufferInfo: flags[%d], offset[%d], presentationTimeUs[%d], size[%d]", new Object[] { paramString, Integer.valueOf(paramBufferInfo.flags), Integer.valueOf(paramBufferInfo.offset), Long.valueOf(paramBufferInfo.presentationTimeUs), Integer.valueOf(paramBufferInfo.size) });
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\extend\TuSdkCodecCapabilities.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */