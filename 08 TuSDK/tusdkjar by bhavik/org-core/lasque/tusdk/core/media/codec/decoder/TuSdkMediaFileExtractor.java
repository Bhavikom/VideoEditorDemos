package org.lasque.tusdk.core.media.codec.decoder;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.DrmInitData;
import android.media.MediaCodec.CryptoInfo;
import android.media.MediaDataSource;
import android.media.MediaExtractor;
import android.media.MediaExtractor.CasInfo;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.PersistableBundle;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;
import org.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoFileFrame;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource.TuSdkMediaDataSourceType;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

@TargetApi(18)
public class TuSdkMediaFileExtractor
  implements TuSdkMediaExtractor
{
  public static final int VIDEO_THREAD = 1;
  public static final int AUDIO_THREAD = 2;
  private Thread a;
  private boolean b;
  private TuSdkDecodecOperation c;
  private MediaExtractor d;
  private TuSdkMediaDataSource e;
  private long f = 0L;
  private boolean g = false;
  private TuSdkMediaFrameInfo h;
  private int i = 0;
  
  @TargetApi(23)
  public final TuSdkMediaFileExtractor setDataSource(MediaDataSource paramMediaDataSource)
  {
    if (paramMediaDataSource == null) {
      return this;
    }
    return setDataSource(new TuSdkMediaDataSource(paramMediaDataSource));
  }
  
  public final TuSdkMediaFileExtractor setDataSource(Context paramContext, Uri paramUri, Map<String, String> paramMap)
  {
    if ((paramContext == null) || (paramUri == null)) {
      return this;
    }
    return setDataSource(new TuSdkMediaDataSource(paramContext, paramUri, paramMap));
  }
  
  public final TuSdkMediaFileExtractor setDataSource(String paramString)
  {
    return setDataSource(paramString, null);
  }
  
  public final TuSdkMediaFileExtractor setDataSource(String paramString, Map<String, String> paramMap)
  {
    if (paramString == null) {
      return this;
    }
    return setDataSource(new TuSdkMediaDataSource(paramString, paramMap));
  }
  
  public final TuSdkMediaFileExtractor setDataSource(AssetFileDescriptor paramAssetFileDescriptor)
  {
    if (paramAssetFileDescriptor == null) {
      return this;
    }
    if (paramAssetFileDescriptor.getDeclaredLength() < 0L) {
      return setDataSource(paramAssetFileDescriptor.getFileDescriptor());
    }
    return setDataSource(paramAssetFileDescriptor.getFileDescriptor(), paramAssetFileDescriptor.getStartOffset(), paramAssetFileDescriptor.getDeclaredLength());
  }
  
  public final TuSdkMediaFileExtractor setDataSource(FileDescriptor paramFileDescriptor)
  {
    return setDataSource(paramFileDescriptor, 0L, 576460752303423487L);
  }
  
  public final TuSdkMediaFileExtractor setDataSource(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2)
  {
    if (paramFileDescriptor == null) {
      return this;
    }
    return setDataSource(new TuSdkMediaDataSource(paramFileDescriptor, paramLong1, paramLong2));
  }
  
  public final TuSdkMediaFileExtractor setDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    if ((paramTuSdkMediaDataSource == null) || (paramTuSdkMediaDataSource.getMediaDataType() == null))
    {
      TLog.e("%s TuSdkMediaDataSource or TuSdkMediaDataSourceType must be not null !", new Object[] { "TuSdkMediaFileExtractor" });
      return null;
    }
    this.e = paramTuSdkMediaDataSource;
    return this;
  }
  
  public TuSdkMediaFileExtractor setDecodecOperation(TuSdkDecodecOperation paramTuSdkDecodecOperation)
  {
    if (this.d != null)
    {
      TLog.w("%s setDecodecOperation need before play", new Object[] { "TuSdkMediaFileExtractor" });
      return this;
    }
    this.c = paramTuSdkDecodecOperation;
    return this;
  }
  
  public void release()
  {
    pause();
    this.g = true;
    if ((this.a != null) && (!this.a.isInterrupted())) {
      this.a.interrupt();
    }
    this.a = null;
    if (this.d != null)
    {
      try
      {
        this.d.release();
      }
      catch (Exception localException) {}
      this.d = null;
    }
    this.c = null;
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public void syncPlay()
  {
    if (this.g)
    {
      TLog.w("%s play has released", new Object[] { "TuSdkMediaFileExtractor" });
      return;
    }
    if ((this.e == null) || (!this.e.isValid()))
    {
      TLog.w("%s play need setDataSource", new Object[] { "TuSdkMediaFileExtractor" });
      return;
    }
    this.d = buildExtractor();
  }
  
  public void play()
  {
    if (this.g)
    {
      TLog.w("%s play has released", new Object[] { "TuSdkMediaFileExtractor" });
      return;
    }
    if ((this.e == null) || (!this.e.isValid()))
    {
      TLog.w("%s play need setDataSource", new Object[] { "TuSdkMediaFileExtractor" });
      return;
    }
    if (this.c == null)
    {
      TLog.w("%s play need before setDecodecOperation!", new Object[] { "TuSdkMediaFileExtractor" });
      return;
    }
    if ((this.a != null) && (!this.a.isInterrupted()))
    {
      TLog.w("%s is running", new Object[] { "TuSdkMediaFileExtractor" });
      return;
    }
    this.b = true;
    this.a = new MediaThread(null);
    this.a.start();
  }
  
  public void setThreadType(int paramInt)
  {
    this.i = paramInt;
  }
  
  protected void _asyncMediaThread()
  {
    TuSdkDecodecOperation localTuSdkDecodecOperation = this.c;
    if (localTuSdkDecodecOperation == null)
    {
      TLog.e("%s play need before setDecodecOperation", new Object[] { "TuSdkMediaFileExtractor" });
      return;
    }
    MediaExtractor localMediaExtractor = this.d = buildExtractor();
    if (localMediaExtractor == null)
    {
      TLog.e("%s run failed!", new Object[] { "TuSdkMediaFileExtractor" });
      return;
    }
    try
    {
      if (!localTuSdkDecodecOperation.decodecInit(this))
      {
        localTuSdkDecodecOperation.decodecException(new Exception(String.format("%s decodec Init failed", new Object[] { "TuSdkMediaFileExtractor" })));
        return;
      }
    }
    catch (Exception localException1)
    {
      localTuSdkDecodecOperation.decodecException(localException1);
      return;
    }
    for (;;)
    {
      if ((!ThreadHelper.interrupted()) && (!this.g))
      {
        if (!isPlaying()) {
          continue;
        }
        try
        {
          if (localTuSdkDecodecOperation.decodecProcessUntilEnd(this)) {
            break label157;
          }
        }
        catch (Exception localException2)
        {
          TLog.e(localException2);
          localTuSdkDecodecOperation.decodecException(localException2);
        }
      }
    }
    label157:
    localTuSdkDecodecOperation.decodecRelease();
    localMediaExtractor.release();
    release();
  }
  
  protected MediaExtractor buildExtractor()
  {
    MediaExtractor localMediaExtractor = new MediaExtractor();
    if (this.e == null)
    {
      TLog.e("%s MediaDataSource must be not null !", new Object[] { "TuSdkMediaFileExtractor" });
      return null;
    }
    try
    {
      if (this.e.getMediaDataType() == TuSdkMediaDataSource.TuSdkMediaDataSourceType.PATH)
      {
        if (!new File(this.e.getPath()).exists())
        {
          TLog.e("%s buildExtractor setDataSource path is incorrect", new Object[] { "TuSdkMediaFileExtractor" });
          return null;
        }
        if (this.e.getRequestHeaders() != null) {
          localMediaExtractor.setDataSource(this.e.getPath(), this.e.getRequestHeaders());
        } else {
          localMediaExtractor.setDataSource(this.e.getPath());
        }
      }
      else if (this.e.getMediaDataType() == TuSdkMediaDataSource.TuSdkMediaDataSourceType.URI)
      {
        localMediaExtractor.setDataSource(this.e.getContext(), this.e.getUri(), this.e.getRequestHeaders());
      }
      else if (this.e.getMediaDataType() == TuSdkMediaDataSource.TuSdkMediaDataSourceType.FILE_DESCRIPTOR)
      {
        localMediaExtractor.setDataSource(this.e.getFileDescriptor(), this.e.getFileDescriptorOffset(), this.e.getFileDescriptorLength());
      }
      else if ((this.e.getMediaDataType() == TuSdkMediaDataSource.TuSdkMediaDataSourceType.MEDIA_DATA_SOURCE) && (Build.VERSION.SDK_INT >= 23))
      {
        localMediaExtractor.setDataSource(this.e.getMediaDataSource());
      }
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "%s buildExtractor need setDataSource", new Object[] { "TuSdkMediaFileExtractor" });
      return null;
    }
    return localMediaExtractor;
  }
  
  public MediaFormat getTrackFormat(int paramInt)
  {
    if ((this.d == null) || (this.g)) {
      return null;
    }
    return this.d.getTrackFormat(paramInt);
  }
  
  public int getTrackCount()
  {
    if ((this.d == null) || (this.g)) {
      return 0;
    }
    return this.d.getTrackCount();
  }
  
  public void selectTrack(int paramInt)
  {
    if ((this.d == null) || (this.g)) {
      return;
    }
    this.d.selectTrack(paramInt);
  }
  
  public long getSampleTime()
  {
    if ((this.d == null) || (this.g)) {
      return -1L;
    }
    return this.d.getSampleTime();
  }
  
  public int getSampleFlags()
  {
    if ((this.d == null) || (this.g)) {
      return -1;
    }
    return this.d.getSampleFlags();
  }
  
  public int getSampleTrackIndex()
  {
    if ((this.d == null) || (this.g)) {
      return -1;
    }
    return this.d.getSampleTrackIndex();
  }
  
  public boolean getSampleCryptoInfo(MediaCodec.CryptoInfo paramCryptoInfo)
  {
    if ((this.d == null) || (this.g)) {
      return false;
    }
    return this.d.getSampleCryptoInfo(paramCryptoInfo);
  }
  
  public long getCachedDuration()
  {
    if ((this.d == null) || (this.g)) {
      return -1L;
    }
    return this.d.getCachedDuration();
  }
  
  @TargetApi(26)
  public MediaExtractor.CasInfo getCasInfo(int paramInt)
  {
    if ((this.d == null) || (this.g)) {
      return null;
    }
    return this.d.getCasInfo(paramInt);
  }
  
  @TargetApi(24)
  public DrmInitData getDrmInitData()
  {
    if ((this.d == null) || (this.g)) {
      return null;
    }
    return this.d.getDrmInitData();
  }
  
  @TargetApi(26)
  public PersistableBundle getMetrics()
  {
    if ((this.d == null) || (this.g)) {
      return null;
    }
    return this.d.getMetrics();
  }
  
  public Map<UUID, byte[]> getPsshInfo()
  {
    if ((this.d == null) || (this.g)) {
      return null;
    }
    return this.d.getPsshInfo();
  }
  
  public boolean hasCacheReachedEndOfStream()
  {
    if ((this.d == null) || (this.g)) {
      return false;
    }
    return this.d.hasCacheReachedEndOfStream();
  }
  
  public boolean isPlaying()
  {
    return this.b;
  }
  
  public void pause()
  {
    this.b = false;
  }
  
  public void resume()
  {
    this.b = true;
  }
  
  public long seekTo(long paramLong)
  {
    return seekTo(paramLong, 2);
  }
  
  public long seekTo(long paramLong, boolean paramBoolean)
  {
    return seekTo(paramLong, paramBoolean ? 0 : 1);
  }
  
  public long seekTo(long paramLong, int paramInt)
  {
    if ((this.d == null) || (this.g)) {
      return -1L;
    }
    if (paramLong < 0L) {
      paramLong = 0L;
    }
    if ((paramLong < 1L) && (paramInt == 0)) {
      paramInt = 2;
    }
    if ((paramInt == 0) && (paramLong > 0L)) {
      paramLong -= 1L;
    }
    this.d.seekTo(paramLong, paramInt);
    long l = this.d.getSampleTime();
    return l;
  }
  
  public boolean advance()
  {
    if ((this.d == null) || (this.g)) {
      return false;
    }
    long l1 = this.d.getSampleTime();
    boolean bool = this.d.advance();
    long l2 = this.d.getSampleTime();
    if ((!bool) || (l1 < 0L) || (l2 < 0L) || (l2 < l1)) {
      return bool;
    }
    this.f = (l2 - l1);
    return bool;
  }
  
  public int readSampleData(ByteBuffer paramByteBuffer, int paramInt)
  {
    if ((this.d == null) || (this.g)) {
      return -1;
    }
    return this.d.readSampleData(paramByteBuffer, paramInt);
  }
  
  public long getFrameIntervalUs()
  {
    return this.f;
  }
  
  public long advanceNestest(long paramLong)
  {
    if ((this.d == null) || (this.g)) {
      return -1L;
    }
    long l1 = seekTo(paramLong, 0);
    if (l1 < 0L) {
      return -1L;
    }
    if (l1 == paramLong) {
      return l1;
    }
    long l2 = Math.abs(paramLong - l1);
    long l3 = 0L;
    while ((advance()) && (l2 != 0L))
    {
      l3 = getSampleTime();
      if (l3 < 0L) {
        break;
      }
      long l4 = Math.abs(paramLong - l3);
      if (l2 < l4) {
        break;
      }
      l2 = l4;
      l1 = l3;
    }
    return l1;
  }
  
  public TuSdkMediaFrameInfo getFrameInfo()
  {
    if (this.h != null) {
      return this.h;
    }
    if ((this.d == null) || (this.g)) {
      return null;
    }
    this.h = TuSdkVideoFileFrame.keyFrameInfo(this);
    return this.h;
  }
  
  private class MediaThread
    extends Thread
  {
    private MediaThread() {}
    
    public void run()
    {
      if (TuSdkMediaFileExtractor.a(TuSdkMediaFileExtractor.this) == 1) {
        setName("TuSdkVideoDecodeThread");
      } else if (TuSdkMediaFileExtractor.a(TuSdkMediaFileExtractor.this) == 2) {
        setName("TuSdkAudioDecodeThread");
      }
      TuSdkMediaFileExtractor.this._asyncMediaThread();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\decoder\TuSdkMediaFileExtractor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */