package org.lasque.tusdk.api.movie.compresser;

import android.media.MediaFormat;
import java.io.File;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import org.lasque.tusdk.core.delegate.TuSDKVideoSaveDelegate;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting.VideoQuality;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileTranscoder;
import org.lasque.tusdk.core.media.suit.TuSdkMediaSuit;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.video.TuSDKVideoResult;

public class TuSDKMovieCompresser
{
  private TuSdkMediaFileTranscoder a;
  private TuSDKMediaDataSource b;
  private MediaFormat c;
  private MediaFormat d;
  private TuSDKMovieCompresserSetting e;
  private TuSDKVideoSaveDelegate f;
  private String g;
  
  public TuSDKMovieCompresser(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    this.b = paramTuSDKMediaDataSource;
  }
  
  public TuSDKMovieCompresserSetting getCompresserSetting()
  {
    if (this.e == null) {
      this.e = new TuSDKMovieCompresserSetting();
    }
    return this.e;
  }
  
  public void setCompresserSetting(TuSDKMovieCompresserSetting paramTuSDKMovieCompresserSetting)
  {
    this.e = paramTuSDKMovieCompresserSetting;
  }
  
  public TuSDKMovieCompresser setDelegate(TuSDKVideoSaveDelegate paramTuSDKVideoSaveDelegate)
  {
    this.f = paramTuSDKVideoSaveDelegate;
    return this;
  }
  
  private String a()
  {
    return TuSdk.getAppTempPath() + "/" + String.format("lsq_%s.mp4", new Object[] { StringHelper.timeStampString() });
  }
  
  public String getOutputFilePah()
  {
    if (this.g == null) {
      this.g = a();
    }
    return this.g;
  }
  
  public TuSDKMovieCompresser setOutputFilePath(String paramString)
  {
    this.g = paramString;
    return this;
  }
  
  protected MediaFormat getOutputVideoFormat()
  {
    if (this.c != null) {
      return this.c;
    }
    if ((this.b == null) || (!this.b.isValid())) {
      return null;
    }
    TuSDKVideoInfo localTuSDKVideoInfo = TuSDKMediaUtils.getVideoInfo(this.b);
    int i = localTuSDKVideoInfo.bitrate;
    int j = localTuSDKVideoInfo.fps;
    TuSdkSize localTuSdkSize = TuSdkSize.create(localTuSDKVideoInfo.width, localTuSDKVideoInfo.height);
    if ((localTuSDKVideoInfo.videoOrientation == ImageOrientation.Left) || (localTuSDKVideoInfo.videoOrientation == ImageOrientation.Right)) {
      localTuSdkSize = TuSdkSize.create(localTuSdkSize.height, localTuSdkSize.width);
    }
    if (TuSDKMovieCompresserSetting.a(getCompresserSetting()) != null)
    {
      i = TuSDKMovieCompresserSetting.a(getCompresserSetting()).getBitrate();
      j = TuSDKMovieCompresserSetting.a(getCompresserSetting()).getFps();
    }
    else
    {
      i = (int)(TuSDKMovieCompresserSetting.b(getCompresserSetting()) * i);
    }
    MediaFormat localMediaFormat = TuSdkMediaFormat.buildSafeVideoEncodecFormat(localTuSdkSize.width, localTuSdkSize.height, j, i, 2130708361, 0, 1);
    return localMediaFormat;
  }
  
  public void setOutputVideoFormat(MediaFormat paramMediaFormat)
  {
    this.c = paramMediaFormat;
  }
  
  protected MediaFormat getOutputAudioFormat()
  {
    if (this.d != null) {
      return this.d;
    }
    if ((this.b == null) || (!this.b.isValid())) {
      return null;
    }
    MediaFormat localMediaFormat = TuSdkMediaFormat.buildSafeAudioEncodecFormat();
    return localMediaFormat;
  }
  
  public void setOutputAudioFormat(MediaFormat paramMediaFormat)
  {
    this.d = paramMediaFormat;
  }
  
  private void a(TuSDKVideoResult paramTuSDKVideoResult)
  {
    if (this.f != null) {
      this.f.onSaveResult(paramTuSDKVideoResult);
    }
    StatisticsManger.appendComponent(9449478L);
  }
  
  private void a(float paramFloat)
  {
    if ((this.f == null) || (paramFloat < 0.0F) || (paramFloat > 1.0F)) {
      return;
    }
    this.f.onProgressChaned(paramFloat);
  }
  
  public void start()
  {
    if ((this.b == null) || (!this.b.isValid()))
    {
      TLog.e("Invalidate data source", new Object[0]);
      return;
    }
    MediaFormat localMediaFormat = getOutputVideoFormat();
    if (localMediaFormat == null)
    {
      TLog.e("Invalidate data source", new Object[0]);
      return;
    }
    this.a = TuSdkMediaSuit.transcoding(new TuSdkMediaDataSource(this.b.getFilePath()), getOutputFilePah(), localMediaFormat, getOutputAudioFormat(), new TuSdkMediaProgress()
    {
      public void onProgress(float paramAnonymousFloat, TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        TuSDKMovieCompresser.a(TuSDKMovieCompresser.this, paramAnonymousFloat);
      }
      
      public void onCompleted(Exception paramAnonymousException, TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource, int paramAnonymousInt)
      {
        if (paramAnonymousException == null)
        {
          TuSDKVideoResult localTuSDKVideoResult = new TuSDKVideoResult();
          localTuSDKVideoResult.videoPath = new File(paramAnonymousTuSdkMediaDataSource.getPath());
          TuSDKMovieCompresser.a(TuSDKMovieCompresser.this, localTuSDKVideoResult);
        }
        else
        {
          if (TuSDKMovieCompresser.a(TuSDKMovieCompresser.this) == null) {
            return;
          }
          TuSDKMovieCompresser.a(TuSDKMovieCompresser.this).onResultFail(paramAnonymousException);
        }
      }
    });
  }
  
  public void stop()
  {
    if (this.a != null) {
      this.a.stop();
    }
    this.a = null;
  }
  
  public static class TuSDKMovieCompresserSetting
  {
    private TuSDKVideoEncoderSetting.VideoQuality a;
    private float b = 0.5F;
    
    public TuSDKMovieCompresserSetting setVideoQuality(TuSDKVideoEncoderSetting.VideoQuality paramVideoQuality)
    {
      this.a = paramVideoQuality;
      return this;
    }
    
    public TuSDKMovieCompresserSetting setScale(float paramFloat)
    {
      if (paramFloat <= 0.0F) {
        return this;
      }
      this.b = Math.min(paramFloat, 2.0F);
      return this;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\movie\compresser\TuSDKMovieCompresser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */