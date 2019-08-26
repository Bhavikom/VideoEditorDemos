package org.lasque.tusdk.api.movie.postproc.speed;

import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.api.postpro.TuSDKPostProcess;
import org.lasque.tusdk.api.postpro.TuSDKPostProcess.PostProcessArg;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import org.lasque.tusdk.core.utils.TLog;

final class TuSDKMovieSpeedProcesser
  extends TuSDKPostProcess
{
  public final boolean process(TuSDKMediaDataSource paramTuSDKMediaDataSource, File paramFile, SpeedMode paramSpeedMode)
  {
    if ((paramTuSDKMediaDataSource == null) || (!paramTuSDKMediaDataSource.isValid()))
    {
      TLog.e("%s : Invalid data source", new Object[] { this });
      return false;
    }
    TuSDKVideoInfo localTuSDKVideoInfo = TuSDKMediaUtils.getVideoInfo(paramTuSDKMediaDataSource);
    if (localTuSDKVideoInfo == null)
    {
      TLog.e("%s : Invalid data source", new Object[] { this });
      return false;
    }
    a(localTuSDKVideoInfo, paramTuSDKMediaDataSource);
    ArrayList localArrayList = new ArrayList();
    if (localTuSDKVideoInfo.fps > 0) {
      localArrayList.add(new TuSDKPostProcess.PostProcessArg("-r", String.valueOf(localTuSDKVideoInfo.fps)));
    }
    if (localTuSDKVideoInfo.bitrate > 0) {
      localArrayList.add(new TuSDKPostProcess.PostProcessArg("-b:v", String.valueOf(localTuSDKVideoInfo.bitrate) + "K"));
    }
    localArrayList.add(new TuSDKPostProcess.PostProcessArg("-filter:v", SpeedMode.a(paramSpeedMode)));
    localArrayList.add(new TuSDKPostProcess.PostProcessArg("-filter:a", SpeedMode.b(paramSpeedMode)));
    return process(paramTuSDKMediaDataSource, paramFile, localArrayList);
  }
  
  private void a(TuSDKVideoInfo paramTuSDKVideoInfo, TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    MediaMetadataRetriever localMediaMetadataRetriever = new MediaMetadataRetriever();
    if (!TextUtils.isEmpty(paramTuSDKMediaDataSource.getFilePath())) {
      localMediaMetadataRetriever.setDataSource(paramTuSDKMediaDataSource.getFilePath());
    } else {
      localMediaMetadataRetriever.setDataSource(TuSdkContext.context(), paramTuSDKMediaDataSource.getFileUri());
    }
    String str;
    if (paramTuSDKVideoInfo.degree <= 0)
    {
      str = localMediaMetadataRetriever.extractMetadata(24);
      if (!TextUtils.isEmpty(str)) {
        paramTuSDKVideoInfo.setVideoRotation(Integer.parseInt(str));
      }
    }
    if (paramTuSDKVideoInfo.bitrate <= 0)
    {
      str = localMediaMetadataRetriever.extractMetadata(20);
      if (!TextUtils.isEmpty(str)) {
        paramTuSDKVideoInfo.bitrate = Integer.parseInt(str);
      }
    }
  }
  
  public static enum SpeedMode
  {
    private float a;
    private float b;
    
    private SpeedMode(float paramFloat1, float paramFloat2)
    {
      this.a = paramFloat1;
      this.b = paramFloat2;
    }
    
    private String a()
    {
      return "setpts=" + this.a + "*PTS";
    }
    
    private String b()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      float f = this.b;
      do
      {
        if (f != f % 2.0F)
        {
          f -= 2.0F;
          localStringBuilder.append("atempo").append("=").append(2).append(",");
        }
        else
        {
          localStringBuilder.append("atempo").append("=").append(f).append(",");
          break;
        }
      } while (f > 0.0F);
      localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
      return localStringBuilder.toString();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\movie\postproc\speed\TuSDKMovieSpeedProcesser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */