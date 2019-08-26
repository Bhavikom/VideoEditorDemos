package org.lasque.tusdk.core.decoder;

import android.annotation.TargetApi;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.text.TextUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

@TargetApi(16)
public class TuSDKMovieReader
{
  public static final int INVALID_TRACK_FLAG = -1;
  protected MediaExtractor mMediaExtractor;
  protected int mVideoTrackIndex = -1;
  protected int mAudioTrackIndex = -1;
  private TuSDKMediaDataSource a;
  private TuSDKVideoInfo b;
  private TuSDKAudioInfo c;
  private TuSdkTimeRange d;
  
  public TuSDKMovieReader(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    this.a = paramTuSDKMediaDataSource;
    this.mMediaExtractor = createMediaExtractor();
  }
  
  public TuSDKVideoInfo getVideoInfo()
  {
    if (this.b == null) {
      this.b = TuSDKMediaUtils.getVideoInfo(this.a);
    }
    return this.b;
  }
  
  public TuSDKAudioInfo getAudioInfo()
  {
    if (this.c == null) {
      this.c = TuSDKAudioInfo.createWithMediaFormat(getAudioTrackFormat());
    }
    return this.c;
  }
  
  public void setTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
  {
    this.d = paramTuSdkTimeRange;
  }
  
  public TuSdkTimeRange getTimeRange()
  {
    if (this.d != null) {
      return this.d;
    }
    if ((this.d == null) && (getVideoInfo() != null)) {
      this.d = TuSdkTimeRange.makeTimeUsRange(0L, getVideoInfo().durationTimeUs);
    }
    if ((this.d == null) && (getAudioInfo() != null)) {
      this.d = TuSdkTimeRange.makeTimeUsRange(0L, getAudioInfo().durationTimeUs);
    }
    return this.d;
  }
  
  protected void destroy()
  {
    if (this.mMediaExtractor != null)
    {
      this.mMediaExtractor.release();
      this.mMediaExtractor = null;
    }
    this.mVideoTrackIndex = -1;
    this.mAudioTrackIndex = -1;
  }
  
  public long getSampleTime()
  {
    if (this.mMediaExtractor == null) {
      return 0L;
    }
    return this.mMediaExtractor.getSampleTime();
  }
  
  public void seekTo(long paramLong)
  {
    if (paramLong < 0L) {
      return;
    }
    seekTo(paramLong, 2);
  }
  
  public void seekTo(long paramLong, int paramInt)
  {
    if (this.mMediaExtractor == null) {
      return;
    }
    this.mMediaExtractor.seekTo(Math.max(0L, paramLong), paramInt);
  }
  
  public boolean advance()
  {
    if (this.mMediaExtractor == null) {
      return false;
    }
    return this.mMediaExtractor.advance();
  }
  
  public int readSampleData(ByteBuffer paramByteBuffer, int paramInt)
  {
    if (this.mMediaExtractor == null) {
      return 0;
    }
    int i = this.mMediaExtractor.readSampleData(paramByteBuffer, paramInt);
    if (i <= 0) {
      return i;
    }
    if ((getTimeRange() != null) && (getSampleTime() >= getTimeRange().getEndTimeUS())) {
      return -1;
    }
    advance();
    return i;
  }
  
  public int getSampleTrackIndex()
  {
    if (this.mMediaExtractor == null) {
      return -1;
    }
    return this.mMediaExtractor.getSampleTrackIndex();
  }
  
  public int getSampleFlags()
  {
    return this.mMediaExtractor.getSampleFlags();
  }
  
  public boolean isVideoSampleTrackIndex()
  {
    return getSampleTrackIndex() == this.mVideoTrackIndex;
  }
  
  protected MediaExtractor createMediaExtractor()
  {
    if (this.a == null)
    {
      TLog.e("Please set the data source", new Object[0]);
      return null;
    }
    if (!this.a.isValid())
    {
      TLog.e("Unable to read media file: %s", new Object[] { this.a.getFilePath() });
      return null;
    }
    MediaExtractor localMediaExtractor = new MediaExtractor();
    try
    {
      if (!TextUtils.isEmpty(this.a.getFilePath())) {
        localMediaExtractor.setDataSource(this.a.getFilePath());
      } else {
        localMediaExtractor.setDataSource(TuSdk.appContext().getContext(), this.a.getFileUri(), null);
      }
    }
    catch (IOException localIOException)
    {
      destroy();
    }
    return localMediaExtractor;
  }
  
  public MediaExtractor getMediaExtractor()
  {
    return this.mMediaExtractor;
  }
  
  public int findVideoTrack()
  {
    if (getMediaExtractor() == null) {
      return -1;
    }
    if (this.mVideoTrackIndex == -1)
    {
      int i = this.mMediaExtractor.getTrackCount();
      for (int j = 0; j < i; j++)
      {
        MediaFormat localMediaFormat = this.mMediaExtractor.getTrackFormat(j);
        String str = localMediaFormat.getString("mime");
        if (str.startsWith("video/"))
        {
          this.mVideoTrackIndex = j;
          return j;
        }
      }
    }
    return this.mVideoTrackIndex;
  }
  
  public int selectVideoTrack()
  {
    if (getMediaExtractor() == null) {
      return -1;
    }
    unselectAudioTrack();
    int i = findVideoTrack();
    if (i == -1) {
      return -1;
    }
    this.mMediaExtractor.selectTrack(i);
    return i;
  }
  
  public void unselectVideoTrack()
  {
    int i = findVideoTrack();
    if (i == -1) {
      return;
    }
    this.mMediaExtractor.unselectTrack(i);
  }
  
  public MediaFormat getVideoTrackFormat()
  {
    if (getMediaExtractor() == null) {
      return null;
    }
    int i = findVideoTrack();
    if (i == -1) {
      return null;
    }
    return this.mMediaExtractor.getTrackFormat(i);
  }
  
  public int findAudioTrack()
  {
    if (getMediaExtractor() == null) {
      return -1;
    }
    if (this.mAudioTrackIndex == -1)
    {
      int i = this.mMediaExtractor.getTrackCount();
      for (int j = 0; j < i; j++)
      {
        MediaFormat localMediaFormat = this.mMediaExtractor.getTrackFormat(j);
        String str = localMediaFormat.getString("mime");
        if (str.startsWith("audio/"))
        {
          this.mAudioTrackIndex = j;
          return j;
        }
      }
    }
    return this.mAudioTrackIndex;
  }
  
  public int selectAudioTrack()
  {
    if (getMediaExtractor() == null) {
      return -1;
    }
    unselectVideoTrack();
    int i = findAudioTrack();
    if (i == -1) {
      return -1;
    }
    this.mMediaExtractor.selectTrack(i);
    return i;
  }
  
  public void unselectAudioTrack()
  {
    if (getMediaExtractor() == null) {
      return;
    }
    int i = findAudioTrack();
    if (i == -1) {
      return;
    }
    this.mMediaExtractor.unselectTrack(i);
  }
  
  public void unselectTrack(int paramInt)
  {
    if (getMediaExtractor() == null) {
      return;
    }
    this.mMediaExtractor.unselectTrack(paramInt);
  }
  
  public MediaFormat getAudioTrackFormat()
  {
    if (getMediaExtractor() == null) {
      return null;
    }
    int i = findAudioTrack();
    if (i == -1) {
      return null;
    }
    return this.mMediaExtractor.getTrackFormat(i);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKMovieReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */