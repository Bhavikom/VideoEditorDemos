package org.lasque.tusdk.api.audio.preproc.mixer;

import android.annotation.SuppressLint;
import android.media.MediaFormat;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.utils.TLog;

@SuppressLint({"InlinedApi"})
public class TuSDKAudioRenderInfoWrap
  extends TuSdkMediaDataSource
{
  private TuSdkAudioInfo a;
  
  public static TuSDKAudioRenderInfoWrap createWithMediaFormat(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return null;
    }
    TuSDKAudioRenderInfoWrap localTuSDKAudioRenderInfoWrap = new TuSDKAudioRenderInfoWrap();
    localTuSDKAudioRenderInfoWrap.a = new TuSdkAudioInfo(paramMediaFormat);
    return localTuSDKAudioRenderInfoWrap;
  }
  
  public static TuSDKAudioRenderInfoWrap createWithMediaDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    MediaFormat localMediaFormat = TuSDKMediaUtils.getAudioFormat(paramTuSdkMediaDataSource);
    return createWithMediaFormat(localMediaFormat);
  }
  
  public static TuSDKAudioRenderInfoWrap createWithAudioInfo(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null)
    {
      TLog.e("%s audioInfo is null  ! ", new Object[] { "TuSDKAudioRenderInfoWrap" });
      return null;
    }
    TuSDKAudioRenderInfoWrap localTuSDKAudioRenderInfoWrap = new TuSDKAudioRenderInfoWrap();
    localTuSDKAudioRenderInfoWrap.a = paramTuSdkAudioInfo;
    return localTuSDKAudioRenderInfoWrap;
  }
  
  private int a()
  {
    return this.a.bitWidth;
  }
  
  public int bytesCountOfTime(int paramInt)
  {
    return paramInt * a() * this.a.sampleRate * this.a.channelCount / 8;
  }
  
  public int bytesCountOfTimeUs(long paramLong)
  {
    return (int)(paramLong / 1000000L * a() * this.a.sampleRate * this.a.channelCount / 8L);
  }
  
  public long getAudioBytesPerSample()
  {
    return (this.a.sampleRate <= 0 ? 44100 : this.a.sampleRate) * a() / 8;
  }
  
  public long frameTimeUsWithAudioSize(int paramInt)
  {
    return 1000000 * (paramInt / this.a.channelCount) / getAudioBytesPerSample();
  }
  
  public long getFrameInterval()
  {
    return 1024000000 / (this.a.sampleRate <= 0 ? 44100 : this.a.sampleRate);
  }
  
  public TuSdkAudioInfo getRealAudioInfo()
  {
    return this.a;
  }
  
  public String toString()
  {
    return this.a.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\audio\preproc\mixer\TuSDKAudioRenderInfoWrap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */