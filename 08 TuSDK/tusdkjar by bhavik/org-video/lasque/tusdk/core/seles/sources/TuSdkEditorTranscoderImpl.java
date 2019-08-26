package org.lasque.tusdk.core.seles.sources;

import android.annotation.TargetApi;
import android.graphics.RectF;
import android.media.MediaFormat;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileCuter;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileCuterImpl;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public class TuSdkEditorTranscoderImpl
  implements TuSdkEditorTranscoder
{
  private TuSdkMediaDataSource a;
  private TuSdkMediaDataSource b;
  private TuSdkMediaFileCuter c;
  private TuSDKVideoInfo d;
  private TuSDKVideoInfo e;
  private TuSdkTimeRange f;
  private RectF g;
  private RectF h;
  private int i;
  private boolean j = true;
  private List<TuSdkEditorTranscoder.TuSdkTranscoderProgressListener> k = new ArrayList();
  protected File mMovieOutputTempFilePath;
  private TuSdkMediaProgress l = new TuSdkMediaProgress()
  {
    public void onProgress(float paramAnonymousFloat, TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      if (TuSdkEditorTranscoderImpl.a(TuSdkEditorTranscoderImpl.this).size() == 0) {
        return;
      }
      Iterator localIterator = TuSdkEditorTranscoderImpl.a(TuSdkEditorTranscoderImpl.this).iterator();
      while (localIterator.hasNext())
      {
        TuSdkEditorTranscoder.TuSdkTranscoderProgressListener localTuSdkTranscoderProgressListener = (TuSdkEditorTranscoder.TuSdkTranscoderProgressListener)localIterator.next();
        if (localTuSdkTranscoderProgressListener != null) {
          localTuSdkTranscoderProgressListener.onProgressChanged(paramAnonymousFloat);
        }
      }
    }
    
    public void onCompleted(Exception paramAnonymousException, TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource, int paramAnonymousInt)
    {
      if (TuSdkEditorTranscoderImpl.a(TuSdkEditorTranscoderImpl.this).size() == 0) {
        return;
      }
      TuSdkEditorTranscoderImpl.a(TuSdkEditorTranscoderImpl.this, paramAnonymousTuSdkMediaDataSource);
      Iterator localIterator;
      TuSdkEditorTranscoder.TuSdkTranscoderProgressListener localTuSdkTranscoderProgressListener;
      if ((paramAnonymousException == null) && (TuSdkEditorTranscoderImpl.this.getOutputVideoInfo() != null))
      {
        TuSdkEditorTranscoderImpl.a(TuSdkEditorTranscoderImpl.this, 2);
        localIterator = TuSdkEditorTranscoderImpl.a(TuSdkEditorTranscoderImpl.this).iterator();
        while (localIterator.hasNext())
        {
          localTuSdkTranscoderProgressListener = (TuSdkEditorTranscoder.TuSdkTranscoderProgressListener)localIterator.next();
          if (localTuSdkTranscoderProgressListener != null) {
            localTuSdkTranscoderProgressListener.onLoadComplete(TuSdkEditorTranscoderImpl.this.getOutputVideoInfo(), paramAnonymousTuSdkMediaDataSource);
          }
        }
      }
      else
      {
        TuSdkEditorTranscoderImpl.a(TuSdkEditorTranscoderImpl.this, 3);
        localIterator = TuSdkEditorTranscoderImpl.a(TuSdkEditorTranscoderImpl.this).iterator();
        while (localIterator.hasNext())
        {
          localTuSdkTranscoderProgressListener = (TuSdkEditorTranscoder.TuSdkTranscoderProgressListener)localIterator.next();
          if (localTuSdkTranscoderProgressListener != null)
          {
            if (TuSdkEditorTranscoderImpl.this.getOutputVideoInfo() == null) {
              paramAnonymousException = new IllegalArgumentException(" Get Video Information Anomaly");
            }
            localTuSdkTranscoderProgressListener.onError(paramAnonymousException);
          }
        }
      }
    }
  };
  
  public TuSdkEditorTranscoderImpl()
  {
    a(0);
  }
  
  protected void setEnableTranscode(boolean paramBoolean)
  {
    this.j = paramBoolean;
  }
  
  public void setVideoDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    if ((paramTuSdkMediaDataSource == null) || (!paramTuSdkMediaDataSource.isValid()))
    {
      TLog.e("%s media source is invalid !!!", new Object[] { "TuSdkEditorTransCoder" });
      return;
    }
    this.a = paramTuSdkMediaDataSource;
  }
  
  public TuSdkMediaDataSource getVideoDataSource()
  {
    return this.a;
  }
  
  public void setTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
  {
    this.f = paramTuSdkTimeRange;
  }
  
  public void setCanvasRect(RectF paramRectF)
  {
    this.g = paramRectF;
  }
  
  public void setCropRect(RectF paramRectF)
  {
    this.h = paramRectF;
  }
  
  public float getVideoOutputDuration()
  {
    return (float)(getInputVideoInfo().durationTimeUs / 1000000L);
  }
  
  public long getVideoOutputDurationTimeUs()
  {
    return getInputVideoInfo().durationTimeUs;
  }
  
  public float getVideoInputDuration()
  {
    return (float)(getOutputVideoInfo().durationTimeUs / 1000000L);
  }
  
  public long getVideoInputDurationTimeUS()
  {
    return getOutputVideoInfo().durationTimeUs;
  }
  
  public TuSDKVideoInfo getInputVideoInfo()
  {
    if (this.d == null) {
      this.d = TuSDKMediaUtils.getVideoInfo(this.a);
    }
    return this.d;
  }
  
  public TuSDKVideoInfo getOutputVideoInfo()
  {
    if (this.e == null) {
      this.e = TuSDKMediaUtils.getVideoInfo(this.b);
    }
    return this.e;
  }
  
  public void addTransCoderProgressListener(TuSdkEditorTranscoder.TuSdkTranscoderProgressListener paramTuSdkTranscoderProgressListener)
  {
    if (paramTuSdkTranscoderProgressListener == null) {
      return;
    }
    this.k.add(paramTuSdkTranscoderProgressListener);
  }
  
  public void removeTransCoderProgressListener(TuSdkEditorTranscoder.TuSdkTranscoderProgressListener paramTuSdkTranscoderProgressListener)
  {
    if ((paramTuSdkTranscoderProgressListener == null) || (this.k.size() == 0)) {
      return;
    }
    this.k.remove(paramTuSdkTranscoderProgressListener);
  }
  
  public void removeAllTransCoderProgressListener()
  {
    if (this.k.size() == 0) {
      return;
    }
    this.k.clear();
  }
  
  public void startTransCoder()
  {
    a(1);
    if ((this.a == null) || (!this.a.isValid()) || (getInputVideoInfo() == null))
    {
      TLog.e("%s invalid data source !!! ", new Object[] { "TuSdkEditorTransCoder" });
      return;
    }
    if (!this.j)
    {
      this.l.onProgress(1.0F, this.a, 0, 1);
      this.l.onCompleted(null, this.a, 1);
      return;
    }
    this.c = new TuSdkMediaFileCuterImpl();
    this.c.setMediaDataSource(this.a);
    this.c.setOutputFilePath(getOutputTempFilePath().getPath());
    this.c.setOutputVideoFormat(a());
    this.c.setOutputAudioFormat(c());
    this.c.setTimeSlice(d());
    this.c.setCanvasRect(this.g);
    this.c.setCropRect(this.h);
    this.c.run(this.l);
  }
  
  public int getStatus()
  {
    return this.i;
  }
  
  public void stopTransCoder()
  {
    a(4);
    this.c.stop();
  }
  
  public void destroy()
  {
    if (this.c != null) {
      this.c.stop();
    }
    FileHelper.delete(this.mMovieOutputTempFilePath);
    this.k.clear();
    this.l = null;
    this.c = null;
  }
  
  private void a(int paramInt)
  {
    this.i = paramInt;
  }
  
  protected File getOutputTempFilePath()
  {
    if (this.mMovieOutputTempFilePath == null) {
      this.mMovieOutputTempFilePath = new File(TuSdk.getAppTempPath(), String.format("lsq_%s.mp4", new Object[] { StringHelper.timeStampString() }));
    }
    return this.mMovieOutputTempFilePath;
  }
  
  private MediaFormat a()
  {
    int m = getInputVideoInfo().bitrate;
    if (getInputVideoInfo().profile >= 66) {
      m *= 2;
    }
    MediaFormat localMediaFormat = TuSdkMediaFormat.buildSafeVideoEncodecFormat(b().width, b().height, getInputVideoInfo().fps, m, 2130708361, 0, 0);
    return localMediaFormat;
  }
  
  private TuSdkSize b()
  {
    if (getInputVideoInfo() == null) {
      return TuSdkSize.create(0);
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create(getInputVideoInfo().width, getInputVideoInfo().height);
    if (localTuSdkSize.minSide() >= 1080) {
      localTuSdkSize = TuSdkSize.create((int)(localTuSdkSize.width * 0.5D), (int)(localTuSdkSize.height * 0.5D));
    } else if ((720 <= localTuSdkSize.minSide()) && (localTuSdkSize.minSide() < 1080)) {
      localTuSdkSize = TuSdkSize.create((int)(localTuSdkSize.width * 0.75D), (int)(localTuSdkSize.height * 0.75D));
    }
    if ((getInputVideoInfo().videoOrientation == ImageOrientation.Right) || (getInputVideoInfo().videoOrientation == ImageOrientation.Left)) {
      localTuSdkSize = TuSdkSize.create(localTuSdkSize.height, localTuSdkSize.width);
    }
    return localTuSdkSize;
  }
  
  @TargetApi(16)
  private MediaFormat c()
  {
    return TuSdkMediaFormat.buildSafeAudioEncodecFormat();
  }
  
  private TuSdkMediaTimeSlice d()
  {
    if ((this.f == null) || (this.f.durationTimeUS() <= 0L)) {
      this.f = TuSdkTimeRange.makeTimeUsRange(0L, getInputVideoInfo().durationTimeUs);
    }
    return new TuSdkMediaTimeSlice(this.f.getStartTimeUS(), this.f.getEndTimeUS());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\sources\TuSdkEditorTranscoderImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */