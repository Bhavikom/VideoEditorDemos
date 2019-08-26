package org.lasque.tusdk.core.media.codec.extend;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;

@TargetApi(18)
public class TuSdkMediaUtils
{
  public static final long CODEC_TIMEOUT_US = 10000L;
  
  public static int getMediaTrackIndex(TuSdkMediaExtractor paramTuSdkMediaExtractor, String paramString)
  {
    int i = -1;
    if (paramString == null) {
      return i;
    }
    paramString = paramString.toLowerCase();
    for (int j = 0; j < paramTuSdkMediaExtractor.getTrackCount(); j++)
    {
      MediaFormat localMediaFormat = paramTuSdkMediaExtractor.getTrackFormat(j);
      String str = localMediaFormat.getString("mime");
      if (str.toLowerCase().startsWith(paramString))
      {
        i = j;
        break;
      }
    }
    return i;
  }
  
  public static boolean putBufferToCoderUntilEnd(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec)
  {
    return putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, true);
  }
  
  public static boolean putBufferToCoderUntilEnd(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec, boolean paramBoolean)
  {
    return putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, paramBoolean, -1L);
  }
  
  public static boolean putBufferToCoderUntilEnd(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec, boolean paramBoolean, long paramLong)
  {
    if ((paramTuSdkMediaExtractor == null) || (paramTuSdkMediaCodec == null)) {
      return true;
    }
    ByteBuffer[] arrayOfByteBuffer;
    try
    {
      arrayOfByteBuffer = paramTuSdkMediaCodec.getInputBuffers();
    }
    catch (Exception localException)
    {
      return true;
    }
    return putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, arrayOfByteBuffer, 10000L, paramBoolean, paramLong);
  }
  
  public static boolean putBufferToCoderUntilEnd(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec, ByteBuffer[] paramArrayOfByteBuffer, long paramLong)
  {
    return putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, paramArrayOfByteBuffer, paramLong, true);
  }
  
  public static boolean putBufferToCoderUntilEnd(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec, ByteBuffer[] paramArrayOfByteBuffer, long paramLong, boolean paramBoolean)
  {
    return putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, paramArrayOfByteBuffer, paramLong, paramBoolean, -1L);
  }
  
  public static boolean putBufferToCoderUntilEnd(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec, ByteBuffer[] paramArrayOfByteBuffer, long paramLong1, boolean paramBoolean, long paramLong2)
  {
    int i = paramTuSdkMediaCodec.dequeueInputBuffer(paramLong1);
    if (i < 0) {
      return false;
    }
    ByteBuffer localByteBuffer = paramArrayOfByteBuffer[i];
    int j = paramTuSdkMediaExtractor.readSampleData(localByteBuffer, 0);
    if (j > 0)
    {
      paramLong2 = paramLong2 < 0L ? paramTuSdkMediaExtractor.getSampleTime() : paramLong2;
      paramTuSdkMediaCodec.queueInputBuffer(i, 0, j, paramLong2, 0);
      paramTuSdkMediaExtractor.advance();
      return false;
    }
    if (paramBoolean) {
      paramTuSdkMediaCodec.queueInputBuffer(i, 0, 0, 0L, 4);
    } else {
      paramTuSdkMediaCodec.queueInputBuffer(i, 0, 0, 0L, 0);
    }
    return true;
  }
  
  public static boolean putEosToCoder(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec)
  {
    if ((paramTuSdkMediaExtractor == null) || (paramTuSdkMediaCodec == null)) {
      return true;
    }
    int i = paramTuSdkMediaCodec.dequeueInputBuffer(10000L);
    if (i < 0) {
      return false;
    }
    paramTuSdkMediaCodec.queueInputBuffer(i, 0, 0, 0L, 4);
    return true;
  }
  
  public static void processOutputBuffer(TuSdkMediaMuxer paramTuSdkMediaMuxer, int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if ((paramBufferInfo.flags & 0x2) != 0) {
      return;
    }
    paramByteBuffer.position(paramBufferInfo.offset);
    paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
    paramTuSdkMediaMuxer.writeSampleData(paramInt, paramByteBuffer, paramBufferInfo);
  }
  
  public static void addADTStoPacket(byte[] paramArrayOfByte, int paramInt, MediaFormat paramMediaFormat)
  {
    if ((paramArrayOfByte == null) || (paramMediaFormat == null)) {
      return;
    }
    int i = TuSdkMediaFormat.getInteger(paramMediaFormat, "aac-profile", 0);
    int j = TuSdkMediaFormat.getInteger(paramMediaFormat, "sample-rate", 0);
    int k = samplingFrequencyIndex(j);
    int m = TuSdkMediaFormat.getInteger(paramMediaFormat, "channel-count", 0);
    addADTStoPacket(paramArrayOfByte, paramInt, i, k, m);
  }
  
  public static void addADTStoPacket(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramArrayOfByte[0] = -1;
    paramArrayOfByte[1] = -7;
    paramArrayOfByte[2] = ((byte)((paramInt2 - 1 << 6) + (paramInt3 << 2) + (paramInt4 >> 2)));
    paramArrayOfByte[3] = ((byte)(((paramInt4 & 0x3) << 6) + (paramInt1 >> 11)));
    paramArrayOfByte[4] = ((byte)((paramInt1 & 0x7FF) >> 3));
    paramArrayOfByte[5] = ((byte)(((paramInt1 & 0x7) << 5) + 31));
    paramArrayOfByte[6] = -4;
  }
  
  public static byte samplingFrequencyIndex(int paramInt)
  {
    switch (paramInt)
    {
    case 96000: 
      return 0;
    case 88200: 
      return 1;
    case 64000: 
      return 2;
    case 48000: 
      return 3;
    case 44100: 
      return 4;
    case 32000: 
      return 5;
    case 24000: 
      return 6;
    case 22050: 
      return 7;
    case 16000: 
      return 8;
    case 12000: 
      return 9;
    case 11025: 
      return 10;
    case 8000: 
      return 11;
    case 7350: 
      return 12;
    }
    return 15;
  }
  
  public static MediaCodec.BufferInfo cloneBufferInfo(MediaCodec.BufferInfo paramBufferInfo)
  {
    if (paramBufferInfo == null) {
      return null;
    }
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    localBufferInfo.set(paramBufferInfo.offset, paramBufferInfo.size, paramBufferInfo.presentationTimeUs, paramBufferInfo.flags);
    return localBufferInfo;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\extend\TuSdkMediaUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */