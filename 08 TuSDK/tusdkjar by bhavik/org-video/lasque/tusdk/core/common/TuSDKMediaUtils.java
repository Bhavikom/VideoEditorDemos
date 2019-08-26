package org.lasque.tusdk.core.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.VideoCapabilities;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lasque.tusdk.api.TuSDKPostProcessJNI;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class TuSDKMediaUtils
{
  public static final String HEVC_MIMETYPE = "video/hevc";
  
  public static boolean isHEVCSupported()
  {
    int i = MediaCodecList.getCodecCount();
    for (int j = 0; j < i; j++)
    {
      MediaCodecInfo localMediaCodecInfo = MediaCodecList.getCodecInfoAt(j);
      if (localMediaCodecInfo.isEncoder())
      {
        String[] arrayOfString = localMediaCodecInfo.getSupportedTypes();
        for (int k = 0; k < arrayOfString.length; k++) {
          if (arrayOfString[k].equalsIgnoreCase("video/hevc")) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  public static MediaMuxer createMuxer(String paramString, int paramInt)
  {
    try
    {
      return new MediaMuxer(paramString, paramInt);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
  }
  
  public static MediaExtractor createExtractor(String paramString)
  {
    MediaExtractor localMediaExtractor = new MediaExtractor();
    try
    {
      localMediaExtractor.setDataSource(paramString);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return localMediaExtractor;
  }
  
  public static MediaExtractor createExtractor(Context paramContext, Uri paramUri)
  {
    if ((paramContext == null) || (paramUri == null)) {
      return null;
    }
    MediaExtractor localMediaExtractor = new MediaExtractor();
    try
    {
      localMediaExtractor.setDataSource(paramContext, paramUri, null);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return localMediaExtractor;
  }
  
  public static MediaFormat getVideoFormat(MediaExtractor paramMediaExtractor)
  {
    for (int i = 0; i < paramMediaExtractor.getTrackCount(); i++) {
      if (isVideoFormat(paramMediaExtractor.getTrackFormat(i))) {
        return paramMediaExtractor.getTrackFormat(i);
      }
    }
    return null;
  }
  
  public static MediaFormat getVideoFormat(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    if ((paramTuSDKMediaDataSource == null) || (!paramTuSDKMediaDataSource.isValid())) {
      return null;
    }
    MediaExtractor localMediaExtractor = new MediaExtractor();
    try
    {
      if (!TextUtils.isEmpty(paramTuSDKMediaDataSource.getFilePath())) {
        localMediaExtractor.setDataSource(paramTuSDKMediaDataSource.getFilePath());
      } else {
        localMediaExtractor.setDataSource(TuSdkContext.context(), paramTuSDKMediaDataSource.getFileUri(), null);
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      return null;
    }
    MediaFormat localMediaFormat = getVideoFormat(localMediaExtractor);
    localMediaExtractor.release();
    return localMediaFormat;
  }
  
  public static MediaFormat getAudioFormat(MediaExtractor paramMediaExtractor)
  {
    for (int i = 0; i < paramMediaExtractor.getTrackCount(); i++) {
      if (isAudioFormat(paramMediaExtractor.getTrackFormat(i))) {
        return paramMediaExtractor.getTrackFormat(i);
      }
    }
    return null;
  }
  
  public static TuSDKVideoInfo getVideoInfo(String paramString)
  {
    TuSDKMediaDataSource localTuSDKMediaDataSource = new TuSDKMediaDataSource(paramString);
    return getVideoInfo(localTuSDKMediaDataSource);
  }
  
  public static TuSDKVideoInfo getVideoInfo(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    if ((paramTuSDKMediaDataSource == null) || (!paramTuSDKMediaDataSource.isValid())) {
      return null;
    }
    TuSDKVideoInfo localTuSDKVideoInfo = TuSDKVideoInfo.createWithMediaFormat(getVideoFormat(paramTuSDKMediaDataSource), true);
    if (localTuSDKVideoInfo == null) {
      return null;
    }
    MediaMetadataRetriever localMediaMetadataRetriever = new MediaMetadataRetriever();
    if (!TextUtils.isEmpty(paramTuSDKMediaDataSource.getFilePath())) {
      localMediaMetadataRetriever.setDataSource(paramTuSDKMediaDataSource.getFilePath());
    } else {
      localMediaMetadataRetriever.setDataSource(TuSdkContext.context(), paramTuSDKMediaDataSource.getFileUri());
    }
    if (localTuSDKVideoInfo.degree <= 0)
    {
      str = localMediaMetadataRetriever.extractMetadata(24);
      if (!TextUtils.isEmpty(str)) {
        localTuSDKVideoInfo.setVideoRotation(Integer.parseInt(str));
      }
    }
    if (localTuSDKVideoInfo.bitrate <= 0)
    {
      str = localMediaMetadataRetriever.extractMetadata(20);
      int i = Integer.parseInt(str);
      if (!TextUtils.isEmpty(str)) {
        localTuSDKVideoInfo.bitrate = (i == 0 ? 3000 : i);
      }
    }
    if (localTuSDKVideoInfo.fps <= 0) {
      TLog.e(" Video frame rate is invalid. | %s", new Object[] { paramTuSDKMediaDataSource });
    }
    if ((paramTuSDKMediaDataSource.getFile() != null) && (paramTuSDKMediaDataSource.getFile().exists())) {
      TuSDKPostProcessJNI.readVideoInfo(paramTuSDKMediaDataSource.getFile().getAbsolutePath(), localTuSDKVideoInfo);
    } else {
      TuSDKPostProcessJNI.readVideoInfo(paramTuSDKMediaDataSource.getFilePath(), localTuSDKVideoInfo);
    }
    String str = localMediaMetadataRetriever.extractMetadata(16);
    localTuSDKVideoInfo.existAudioTrack = ((!TextUtils.isEmpty(str)) && (str.equalsIgnoreCase("yes")));
    return localTuSDKVideoInfo;
  }
  
  public static TuSDKVideoInfo getVideoInfo(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    if ((paramTuSdkMediaDataSource == null) || (!paramTuSdkMediaDataSource.isValid())) {
      return null;
    }
    TuSDKVideoInfo localTuSDKVideoInfo = TuSDKVideoInfo.createWithMediaFormat(getVideoFormat(paramTuSdkMediaDataSource), true);
    if (localTuSDKVideoInfo == null) {
      return null;
    }
    MediaMetadataRetriever localMediaMetadataRetriever = new MediaMetadataRetriever();
    switch (1.a[paramTuSdkMediaDataSource.getMediaDataType().ordinal()])
    {
    case 1: 
      localMediaMetadataRetriever.setDataSource(paramTuSdkMediaDataSource.getContext(), paramTuSdkMediaDataSource.getUri());
      break;
    case 2: 
      if (paramTuSdkMediaDataSource.getRequestHeaders() == null) {
        localMediaMetadataRetriever.setDataSource(paramTuSdkMediaDataSource.getPath());
      } else {
        localMediaMetadataRetriever.setDataSource(paramTuSdkMediaDataSource.getPath(), paramTuSdkMediaDataSource.getRequestHeaders());
      }
      break;
    case 3: 
      localMediaMetadataRetriever.setDataSource(paramTuSdkMediaDataSource.getFileDescriptor(), paramTuSdkMediaDataSource.getFileDescriptorOffset(), paramTuSdkMediaDataSource.getFileDescriptorLength());
      break;
    case 4: 
      if (Build.VERSION.SDK_INT >= 23) {
        localMediaMetadataRetriever.setDataSource(paramTuSdkMediaDataSource.getMediaDataSource());
      } else {
        TLog.e("Use MediaDataSource system version must be >= M", new Object[0]);
      }
      break;
    }
    if (localTuSDKVideoInfo.degree <= 0)
    {
      str = localMediaMetadataRetriever.extractMetadata(24);
      if (!TextUtils.isEmpty(str)) {
        localTuSDKVideoInfo.setVideoRotation(Integer.parseInt(str));
      }
    }
    if (localTuSDKVideoInfo.bitrate <= 0)
    {
      str = localMediaMetadataRetriever.extractMetadata(20);
      if (!TextUtils.isEmpty(str)) {
        localTuSDKVideoInfo.bitrate = Integer.parseInt(str);
      }
    }
    if (localTuSDKVideoInfo.fps <= 0) {
      TLog.e(" Video frame rate is invalid. | %s", new Object[] { paramTuSdkMediaDataSource });
    }
    if (paramTuSdkMediaDataSource.isValid()) {
      TuSDKPostProcessJNI.readVideoInfo(paramTuSdkMediaDataSource.getPath(), localTuSDKVideoInfo);
    } else {
      TuSDKPostProcessJNI.readVideoInfo(paramTuSdkMediaDataSource.getPath(), localTuSDKVideoInfo);
    }
    String str = localMediaMetadataRetriever.extractMetadata(16);
    localTuSDKVideoInfo.existAudioTrack = ((!TextUtils.isEmpty(str)) && (str.equalsIgnoreCase("yes")));
    return localTuSDKVideoInfo;
  }
  
  public static MediaFormat getVideoFormat(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    if ((paramTuSdkMediaDataSource == null) || (!paramTuSdkMediaDataSource.isValid())) {
      return null;
    }
    MediaExtractor localMediaExtractor = new MediaExtractor();
    try
    {
      switch (1.a[paramTuSdkMediaDataSource.getMediaDataType().ordinal()])
      {
      case 1: 
        localMediaExtractor.setDataSource(paramTuSdkMediaDataSource.getContext(), paramTuSdkMediaDataSource.getUri(), paramTuSdkMediaDataSource.getRequestHeaders());
        break;
      case 2: 
        if (paramTuSdkMediaDataSource.getRequestHeaders() == null) {
          localMediaExtractor.setDataSource(paramTuSdkMediaDataSource.getPath());
        } else {
          localMediaExtractor.setDataSource(paramTuSdkMediaDataSource.getPath(), paramTuSdkMediaDataSource.getRequestHeaders());
        }
        break;
      case 3: 
        localMediaExtractor.setDataSource(paramTuSdkMediaDataSource.getFileDescriptor(), paramTuSdkMediaDataSource.getFileDescriptorOffset(), paramTuSdkMediaDataSource.getFileDescriptorLength());
        break;
      case 4: 
        if (Build.VERSION.SDK_INT >= 23) {
          localMediaExtractor.setDataSource(paramTuSdkMediaDataSource.getMediaDataSource());
        } else {
          TLog.e("Use MediaDataSource system version must be >= M", new Object[0]);
        }
        break;
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    MediaFormat localMediaFormat = getVideoFormat(localMediaExtractor);
    localMediaExtractor.release();
    return localMediaFormat;
  }
  
  public static MediaFormat getAudioFormat(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return null;
    }
    MediaExtractor localMediaExtractor = new MediaExtractor();
    try
    {
      localMediaExtractor.setDataSource(paramString);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      return null;
    }
    MediaFormat localMediaFormat = getAudioFormat(localMediaExtractor);
    localMediaExtractor.release();
    return localMediaFormat;
  }
  
  public static MediaFormat getAudioFormat(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    if ((paramTuSDKMediaDataSource == null) || (!paramTuSDKMediaDataSource.isValid())) {
      return null;
    }
    MediaExtractor localMediaExtractor = new MediaExtractor();
    try
    {
      if (!TextUtils.isEmpty(paramTuSDKMediaDataSource.getFilePath())) {
        localMediaExtractor.setDataSource(paramTuSDKMediaDataSource.getFilePath());
      } else {
        localMediaExtractor.setDataSource(TuSdkContext.context(), paramTuSDKMediaDataSource.getFileUri(), null);
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      return null;
    }
    MediaFormat localMediaFormat = getAudioFormat(localMediaExtractor);
    localMediaExtractor.release();
    return localMediaFormat;
  }
  
  public static MediaFormat getAudioFormat(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    if ((paramTuSdkMediaDataSource == null) || (!paramTuSdkMediaDataSource.isValid())) {
      return null;
    }
    MediaExtractor localMediaExtractor = new MediaExtractor();
    try
    {
      if (!TextUtils.isEmpty(paramTuSdkMediaDataSource.getPath())) {
        localMediaExtractor.setDataSource(paramTuSdkMediaDataSource.getPath());
      } else {
        localMediaExtractor.setDataSource(TuSdkContext.context(), paramTuSdkMediaDataSource.getUri(), null);
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      return null;
    }
    MediaFormat localMediaFormat = getAudioFormat(localMediaExtractor);
    localMediaExtractor.release();
    return localMediaFormat;
  }
  
  public static int getAndSelectVideoTrackIndex(MediaExtractor paramMediaExtractor)
  {
    for (int i = 0; i < paramMediaExtractor.getTrackCount(); i++) {
      if (isVideoFormat(paramMediaExtractor.getTrackFormat(i)))
      {
        paramMediaExtractor.selectTrack(i);
        return i;
      }
    }
    return -1;
  }
  
  public static int getAndSelectAudioTrackIndex(MediaExtractor paramMediaExtractor)
  {
    for (int i = 0; i < paramMediaExtractor.getTrackCount(); i++) {
      if (isAudioFormat(paramMediaExtractor.getTrackFormat(i)))
      {
        paramMediaExtractor.selectTrack(i);
        return i;
      }
    }
    return -1;
  }
  
  public static int getVideoTrack(MediaExtractor paramMediaExtractor)
  {
    int i = paramMediaExtractor.getTrackCount();
    for (int j = 0; j < i; j++)
    {
      MediaFormat localMediaFormat = paramMediaExtractor.getTrackFormat(j);
      String str = localMediaFormat.getString("mime");
      if (str.startsWith("video/")) {
        return j;
      }
    }
    return -1;
  }
  
  public static int getAudioTrack(MediaExtractor paramMediaExtractor)
  {
    int i = paramMediaExtractor.getTrackCount();
    for (int j = 0; j < i; j++)
    {
      MediaFormat localMediaFormat = paramMediaExtractor.getTrackFormat(j);
      String str = localMediaFormat.getString("mime");
      if (str.startsWith("audio/")) {
        return j;
      }
    }
    return -1;
  }
  
  public static MediaFormat createVideoFormat(String paramString, int paramInt1, int paramInt2)
  {
    return MediaFormat.createVideoFormat(paramString, paramInt1, paramInt2);
  }
  
  public static MediaFormat createAudioFormat(String paramString, int paramInt1, int paramInt2)
  {
    return MediaFormat.createAudioFormat(paramString, paramInt1, paramInt2);
  }
  
  public static boolean isVideoFormat(MediaFormat paramMediaFormat)
  {
    return getMimeTypeFor(paramMediaFormat).startsWith("video/");
  }
  
  public static boolean isAudioFormat(MediaFormat paramMediaFormat)
  {
    return getMimeTypeFor(paramMediaFormat).startsWith("audio/");
  }
  
  public static String getMimeTypeFor(MediaFormat paramMediaFormat)
  {
    return paramMediaFormat.getString("mime");
  }
  
  public static boolean isSameVideoFormat(MediaFormat paramMediaFormat1, MediaFormat paramMediaFormat2)
  {
    if ((paramMediaFormat1 == null) || (paramMediaFormat2 == null)) {
      return false;
    }
    String str1 = paramMediaFormat1.getString("mime");
    int i = paramMediaFormat1.getInteger("width");
    int j = paramMediaFormat1.getInteger("height");
    String str2 = paramMediaFormat2.getString("mime");
    int k = paramMediaFormat2.getInteger("width");
    int m = paramMediaFormat2.getInteger("height");
    return (str1.equals(str2)) && (i == k) && (j == m);
  }
  
  public static boolean isSameAudioFormat(MediaFormat paramMediaFormat1, MediaFormat paramMediaFormat2)
  {
    if ((paramMediaFormat1 == null) || (paramMediaFormat2 == null)) {
      return false;
    }
    String str1 = paramMediaFormat1.getString("mime");
    int i = paramMediaFormat1.getInteger("sample-rate");
    String str2 = paramMediaFormat2.getString("mime");
    int j = paramMediaFormat2.getInteger("sample-rate");
    return (str1.equals(str2)) && (i == j);
  }
  
  public static long getSafePts(long paramLong1, long paramLong2)
  {
    if (paramLong1 >= paramLong2)
    {
      TLog.e("Calibrate the video timestamp prevTime : %d  nextTime : %d   After the calibration : %d", new Object[] { Long.valueOf(paramLong1), Long.valueOf(paramLong2), Long.valueOf(paramLong1 + 9643L) });
      return paramLong1 + 9643L;
    }
    return paramLong2;
  }
  
  public static boolean containsKeyFrameRate(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return false;
    }
    return paramMediaFormat.containsKey("frame-rate");
  }
  
  public static int getVideoFps(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return 0;
    }
    if (!isVideoFormat(paramMediaFormat)) {
      TLog.e("Is not a video format", new Object[0]);
    }
    if (paramMediaFormat.containsKey("frame-rate")) {
      return paramMediaFormat.getInteger("frame-rate");
    }
    return 0;
  }
  
  public static int getAudioSampleRate(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return 0;
    }
    if (!isAudioFormat(paramMediaFormat)) {
      TLog.e("Is not a audio format", new Object[0]);
    }
    if (paramMediaFormat.containsKey("sample-rate")) {
      return paramMediaFormat.getInteger("sample-rate");
    }
    return 0;
  }
  
  public static long getVideoDefaultInterval()
  {
    return getVideoInterval(10);
  }
  
  public static long getVideoInterval(int paramInt)
  {
    if (paramInt <= 0) {
      return 0L;
    }
    return 1000000 / paramInt;
  }
  
  public static long getVideoInterval(MediaFormat paramMediaFormat)
  {
    return getVideoInterval(getVideoFps(paramMediaFormat));
  }
  
  public static long getVideoFrameIntervalTimeUs(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    if ((paramTuSDKMediaDataSource == null) || (!paramTuSDKMediaDataSource.isValid())) {
      return 0L;
    }
    int i = 0;
    MediaExtractor localMediaExtractor = new MediaExtractor();
    long l1 = 0L;
    long l2 = 0L;
    try
    {
      if (!TextUtils.isEmpty(paramTuSDKMediaDataSource.getFilePath())) {
        localMediaExtractor.setDataSource(paramTuSDKMediaDataSource.getFilePath());
      } else {
        localMediaExtractor.setDataSource(TuSdkContext.context(), paramTuSDKMediaDataSource.getFileUri(), null);
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    int j = localMediaExtractor.getTrackCount();
    int k = -1;
    for (int m = 0; m < j; m++)
    {
      localObject = localMediaExtractor.getTrackFormat(m);
      String str = ((MediaFormat)localObject).getString("mime");
      if (str.startsWith("video/"))
      {
        localMediaExtractor.selectTrack(m);
        if (((MediaFormat)localObject).containsKey("max-input-size"))
        {
          int n = ((MediaFormat)localObject).getInteger("max-input-size");
          k = n > k ? n : k;
        }
        i = 1;
      }
    }
    if (i == 0) {
      return 0L;
    }
    if (k < 0) {
      k = 1048576;
    }
    ByteBuffer localByteBuffer = ByteBuffer.allocate(k);
    Object localObject = new MediaCodec.BufferInfo();
    long l3 = 0L;
    for (;;)
    {
      ((MediaCodec.BufferInfo)localObject).offset = 0;
      ((MediaCodec.BufferInfo)localObject).size = localMediaExtractor.readSampleData(localByteBuffer, 0);
      if (((MediaCodec.BufferInfo)localObject).size < 0)
      {
        ((MediaCodec.BufferInfo)localObject).size = 0;
        break;
      }
      long l4 = localMediaExtractor.getSampleTime();
      if (l1 == 0L) {
        l1 = l4;
      }
      l2 = l4;
      l3 += 1L;
      localMediaExtractor.advance();
    }
    if (l3 == 0L) {
      return 0L;
    }
    return (l2 - l1) / l3;
  }
  
  public static long getAudioDefaultInterval()
  {
    return getAudioInterval(1024, 44100);
  }
  
  public static long getAudioInterval(int paramInt1, int paramInt2)
  {
    if ((paramInt1 <= 0) || (paramInt2 <= 0)) {
      return getAudioDefaultInterval();
    }
    return paramInt1 * 1000000 / paramInt2;
  }
  
  public static long getAudioInterval(int paramInt, MediaFormat paramMediaFormat)
  {
    int i = getAudioSampleRate(paramMediaFormat);
    return getAudioInterval(paramInt, i);
  }
  
  public static MediaCodecInfo getEncoderCodecInfo(String paramString)
  {
    int i = MediaCodecList.getCodecCount();
    for (int j = 0; j < i; j++)
    {
      MediaCodecInfo localMediaCodecInfo = MediaCodecList.getCodecInfoAt(j);
      if (localMediaCodecInfo.isEncoder())
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
  
  @TargetApi(21)
  public static boolean isVideoSizeSupported(TuSdkSize paramTuSdkSize, String paramString)
  {
    if (Build.VERSION.SDK_INT < 21) {
      return false;
    }
    MediaCodecInfo localMediaCodecInfo = getEncoderCodecInfo(paramString);
    if (localMediaCodecInfo == null) {
      return false;
    }
    String[] arrayOfString = localMediaCodecInfo.getSupportedTypes();
    for (String str : arrayOfString)
    {
      MediaCodecInfo.CodecCapabilities localCodecCapabilities = localMediaCodecInfo.getCapabilitiesForType(str);
      MediaCodecInfo.VideoCapabilities localVideoCapabilities = localCodecCapabilities.getVideoCapabilities();
      if (localVideoCapabilities != null)
      {
        boolean bool = localVideoCapabilities.isSizeSupported(paramTuSdkSize.width, paramTuSdkSize.height);
        if (bool) {
          return true;
        }
      }
    }
    ??? = TuSdkContext.getDisplaySize();
    return (paramTuSdkSize.width <= ((TuSdkSize)???).width) && (paramTuSdkSize.height <= ((TuSdkSize)???).height);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\common\TuSDKMediaUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */