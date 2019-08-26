package org.lasque.tusdk.core.decoder;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;
import java.io.IOException;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public abstract class TuSDKMediaDecoder<T extends TuSDKMovieReader>
  implements TuSDKMediaDecoderInterface
{
  public static final long TIME_US_BASE = 1000000L;
  public static final int INVALID_TRACK_FLAG = -1;
  protected static final int TIMEOUT_USEC = 500;
  protected T mMovieReader;
  protected MediaCodec mVideoDecoder;
  protected MediaCodec mAudioDecoder;
  protected TuSDKMediaDataSource mDataSource;
  private long a;
  
  public TuSDKMediaDecoder(String paramString)
  {
    this(TuSDKMediaDataSource.create(paramString));
  }
  
  public TuSDKMediaDecoder(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    this.mDataSource = paramTuSDKMediaDataSource;
  }
  
  protected void onDecoderError(TuSDKMediaDecoderError paramTuSDKMediaDecoderError)
  {
    TLog.e("decoding error", new Object[0]);
  }
  
  public void start()
  {
    MediaCodec localMediaCodec1 = getVideoDecoder();
    if (localMediaCodec1 != null) {
      localMediaCodec1.start();
    }
    MediaCodec localMediaCodec2 = getAudioDecoder();
    if (localMediaCodec2 != null) {
      localMediaCodec2.start();
    }
  }
  
  public void stop()
  {
    destroyMediaReader();
    MediaCodec localMediaCodec1 = getVideoDecoder();
    if (localMediaCodec1 != null)
    {
      localMediaCodec1.stop();
      localMediaCodec1.release();
      this.mVideoDecoder = null;
    }
    MediaCodec localMediaCodec2 = getAudioDecoder();
    if (localMediaCodec2 != null)
    {
      localMediaCodec2.stop();
      localMediaCodec2.release();
      this.mAudioDecoder = null;
    }
  }
  
  public long getCurrentSampleTimeUs()
  {
    if (this.mMovieReader == null) {
      return 0L;
    }
    return this.mMovieReader.getSampleTime();
  }
  
  public long getVideoFrameIntervalTimeUs()
  {
    if (this.a <= 0L) {
      this.a = TuSDKMediaUtils.getVideoFrameIntervalTimeUs(this.mDataSource);
    }
    return this.a;
  }
  
  public void seekTo(long paramLong)
  {
    seekTo(paramLong, 2);
  }
  
  public void seekTo(long paramLong, int paramInt)
  {
    if (this.mMovieReader == null) {
      return;
    }
    this.mMovieReader.seekTo(Math.max(0L, paramLong), paramInt);
  }
  
  public T createMovieReader()
  {
    if (this.mDataSource == null)
    {
      TLog.e("Please set the data source", new Object[0]);
      onDecoderError(TuSDKMediaDecoderError.InvalidDataSource);
      return null;
    }
    if (!this.mDataSource.isValid())
    {
      TLog.e("Unable to read media file: %s", new Object[] { this.mDataSource.getFilePath() });
      onDecoderError(TuSDKMediaDecoderError.InvalidDataSource);
      return null;
    }
    return new TuSDKMovieReader(this.mDataSource);
  }
  
  public MediaCodec createMediaDecoder(String paramString)
  {
    try
    {
      MediaCodec localMediaCodec = MediaCodec.createDecoderByType(paramString);
      return localMediaCodec;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
  }
  
  public T getMediaReader()
  {
    return this.mMovieReader;
  }
  
  public MediaCodec createAudioDecoder()
  {
    MediaFormat localMediaFormat = getAudioTrackFormat();
    if (localMediaFormat == null) {
      return null;
    }
    return createMediaDecoder(localMediaFormat.getString("mime"));
  }
  
  public MediaCodec createVideoDecoder(Surface paramSurface)
  {
    MediaFormat localMediaFormat = getVideoTrackFormat();
    if (localMediaFormat == null) {
      return null;
    }
    try
    {
      MediaCodec localMediaCodec = createMediaDecoder(localMediaFormat.getString("mime"));
      localMediaCodec.configure(localMediaFormat, paramSurface, null, 0);
      return localMediaCodec;
    }
    catch (Exception localException)
    {
      TLog.e("TuSDKMovieDecoder : Video decoding failed %s", new Object[] { localException.getMessage() });
      this.mVideoDecoder = null;
      onDecoderError(TuSDKMediaDecoderError.UnsupportedVideoFormat);
    }
    return null;
  }
  
  public int findVideoTrack()
  {
    if (getMediaReader() == null) {
      return -1;
    }
    return getMediaReader().findVideoTrack();
  }
  
  public int selectVideoTrack()
  {
    if (getMediaReader() == null) {
      return -1;
    }
    return getMediaReader().selectVideoTrack();
  }
  
  public void unselectVideoTrack()
  {
    if (getMediaReader() == null) {
      return;
    }
    getMediaReader().unselectVideoTrack();
  }
  
  public MediaFormat getVideoTrackFormat()
  {
    if (getMediaReader() == null) {
      return null;
    }
    return getMediaReader().getVideoTrackFormat();
  }
  
  public int findAudioTrack()
  {
    if (getMediaReader() == null) {
      return -1;
    }
    return getMediaReader().findVideoTrack();
  }
  
  public int selectAudioTrack()
  {
    if (getMediaReader() == null) {
      return -1;
    }
    return getMediaReader().selectAudioTrack();
  }
  
  public void unselectAudioTrack()
  {
    if (getMediaReader() == null) {
      return;
    }
    getMediaReader().unselectAudioTrack();
  }
  
  public MediaFormat getAudioTrackFormat()
  {
    if (getMediaReader() == null) {
      return null;
    }
    return getMediaReader().getAudioTrackFormat();
  }
  
  protected void destroyMediaReader()
  {
    if (this.mMovieReader != null)
    {
      this.mMovieReader.destroy();
      this.mMovieReader = null;
    }
  }
  
  public void destroy()
  {
    stop();
  }
  
  public static enum TuSDKMediaDecoderError
  {
    private TuSDKMediaDecoderError() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKMediaDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */