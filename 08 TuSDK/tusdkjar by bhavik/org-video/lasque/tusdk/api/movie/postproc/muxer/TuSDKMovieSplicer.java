package org.lasque.tusdk.api.movie.postproc.muxer;

import android.annotation.SuppressLint;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.utils.TLog;

@SuppressLint({"InlinedApi"})
public class TuSDKMovieSplicer
{
  public static final int INVALID_START_TIME = -1;
  public static final int INVALID_END_TIME = 0;
  public static final int INVALID_TRACK_INDEX = -1;
  private TuSDKMovieSplicerOption a;
  private MediaMuxer b;
  private HandlerThread c;
  private Handler d;
  private final int e = 1048576;
  private int f = 1048576;
  private int g = -1;
  private int h = -1;
  private final long i = 1L;
  private long j;
  private long k;
  private long l;
  private long m;
  private MediaFormat n;
  private MediaFormat o;
  
  public TuSDKMovieSplicer(TuSDKMovieSplicerOption paramTuSDKMovieSplicerOption)
  {
    if (paramTuSDKMovieSplicerOption == null) {
      TLog.e("option is null", new Object[] { new RuntimeException() });
    }
    this.a = paramTuSDKMovieSplicerOption;
  }
  
  private void a(ErrorCode paramErrorCode)
  {
    if ((this.a == null) || (this.a.listener == null) || (paramErrorCode == null)) {
      return;
    }
    TLog.e("%s : %s", new Object[] { this, paramErrorCode.getMessage() });
    this.a.listener.onError(paramErrorCode);
  }
  
  private boolean a(List<TuSDKMovieSegment> paramList)
  {
    if ((paramList.get(0) == null) || (((TuSDKMovieSegment)paramList.get(0)).sourceUri == null))
    {
      a(ErrorCode.InvalidMovieSegmentError);
      return false;
    }
    MediaExtractor localMediaExtractor1 = TuSDKMediaUtils.createExtractor(TuSdkContext.context(), ((TuSDKMovieSegment)paramList.get(0)).sourceUri);
    int i1 = localMediaExtractor1.getTrackCount();
    Object localObject;
    for (int i2 = 0; i2 < i1; i2++)
    {
      localObject = localMediaExtractor1.getTrackFormat(i2);
      if ((TuSDKMediaUtils.isAudioFormat((MediaFormat)localObject)) && (this.a.splicerType != TuSDKMovieSplicerType.SplicerVideoType)) {
        this.o = ((MediaFormat)localObject);
      } else if ((TuSDKMediaUtils.isVideoFormat((MediaFormat)localObject)) && (this.a.splicerType != TuSDKMovieSplicerType.SplicerAudioType)) {
        this.n = ((MediaFormat)localObject);
      }
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      localObject = (TuSDKMovieSegment)localIterator.next();
      MediaExtractor localMediaExtractor2 = TuSDKMediaUtils.createExtractor(TuSdkContext.context(), ((TuSDKMovieSegment)localObject).sourceUri);
      MediaFormat localMediaFormat;
      boolean bool;
      if ((this.a.splicerType != TuSDKMovieSplicerType.SplicerVideoType) && (this.o != null))
      {
        localMediaFormat = TuSDKMediaUtils.getAudioFormat(localMediaExtractor2);
        bool = TuSDKMediaUtils.isSameAudioFormat(localMediaFormat, this.o);
        if (!bool) {
          return false;
        }
      }
      if ((this.a.splicerType != TuSDKMovieSplicerType.SplicerAudioType) && (this.n != null))
      {
        localMediaFormat = TuSDKMediaUtils.getVideoFormat(localMediaExtractor2);
        bool = TuSDKMediaUtils.isSameVideoFormat(localMediaFormat, this.n);
        if (!bool) {
          return false;
        }
      }
    }
    return true;
  }
  
  private boolean a(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    File localFile = new File(paramString);
    return localFile.getParentFile().isDirectory();
  }
  
  private void a(Uri paramUri)
  {
    this.j = 1L;
    this.k = 1L;
    this.l = 1L;
    this.m = 23219L;
    this.b = TuSDKMediaUtils.createMuxer(this.a.savePath, 0);
    MediaExtractor localMediaExtractor = TuSDKMediaUtils.createExtractor(TuSdkContext.context(), paramUri);
    int i1 = localMediaExtractor.getTrackCount();
    int i3;
    for (int i2 = 0; i2 < i1; i2++)
    {
      localObject = localMediaExtractor.getTrackFormat(i2);
      if ((TuSDKMediaUtils.isAudioFormat((MediaFormat)localObject)) && (this.a.splicerType != TuSDKMovieSplicerType.SplicerVideoType))
      {
        if (this.o == null) {
          this.o = ((MediaFormat)localObject);
        }
        this.h = this.b.addTrack(this.o);
        TLog.d("Initializing Splicer audio track ", new Object[0]);
        if (((MediaFormat)localObject).containsKey("max-input-size"))
        {
          i3 = ((MediaFormat)localObject).getInteger("max-input-size");
          this.f = (i3 > this.f ? i3 : this.f);
        }
        if ((((MediaFormat)localObject).containsKey("sample-rate")) && (((MediaFormat)localObject).getInteger("sample-rate") > 0)) {
          this.m = (1024000000 / ((MediaFormat)localObject).getInteger("sample-rate"));
        }
      }
      else if ((TuSDKMediaUtils.isVideoFormat((MediaFormat)localObject)) && (this.a.splicerType != TuSDKMovieSplicerType.SplicerAudioType))
      {
        if (this.n == null) {
          this.n = ((MediaFormat)localObject);
        }
        this.g = this.b.addTrack(this.n);
        TLog.d("Initializing Splicer video track ", new Object[0]);
        if (((MediaFormat)localObject).containsKey("max-input-size"))
        {
          i3 = ((MediaFormat)localObject).getInteger("max-input-size");
          this.f = (i3 > this.f ? i3 : this.f);
        }
        if (((MediaFormat)localObject).containsKey("frame-rate")) {
          this.l = (1000000 / ((MediaFormat)localObject).getInteger("frame-rate"));
        }
      }
    }
    MediaMetadataRetriever localMediaMetadataRetriever = new MediaMetadataRetriever();
    localMediaMetadataRetriever.setDataSource(TuSdkContext.context(), paramUri);
    Object localObject = localMediaMetadataRetriever.extractMetadata(24);
    if (localObject != null)
    {
      i3 = Integer.parseInt((String)localObject);
      this.b.setOrientationHint(i3);
    }
    this.b.start();
  }
  
  public void start(final List<TuSDKMovieSegment> paramList)
  {
    if ((this.c == null) || (this.d == null))
    {
      this.c = new HandlerThread("TuSDKMovieSplicerThread");
      this.c.start();
      this.d = new Handler(this.c.getLooper());
    }
    this.d.post(new Runnable()
    {
      public void run()
      {
        if ((TuSDKMovieSplicer.a(TuSDKMovieSplicer.this) == null) || (paramList == null) || (paramList.size() == 0))
        {
          TuSDKMovieSplicer.a(TuSDKMovieSplicer.this, TuSDKMovieSplicer.ErrorCode.InvalidMovieSegmentError);
          return;
        }
        if (!TuSDKMovieSplicer.a(TuSDKMovieSplicer.this, TuSDKMovieSplicer.a(TuSDKMovieSplicer.this).savePath))
        {
          TuSDKMovieSplicer.a(TuSDKMovieSplicer.this, TuSDKMovieSplicer.ErrorCode.InvalidOutputPathError);
          return;
        }
        if (!TuSDKMovieSplicer.a(TuSDKMovieSplicer.this, paramList))
        {
          TuSDKMovieSplicer.a(TuSDKMovieSplicer.this, TuSDKMovieSplicer.ErrorCode.InvalidVideoFormatError);
          return;
        }
        try
        {
          TuSDKMovieSplicer.a(TuSDKMovieSplicer.this, ((TuSDKMovieSplicer.TuSDKMovieSegment)paramList.get(0)).sourceUri);
        }
        catch (IOException localIOException1)
        {
          localIOException1.printStackTrace();
        }
        if (TuSDKMovieSplicer.a(TuSDKMovieSplicer.this).listener != null) {
          TuSDKMovieSplicer.a(TuSDKMovieSplicer.this).listener.onStart();
        }
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
          TuSDKMovieSplicer.TuSDKMovieSegment localTuSDKMovieSegment = (TuSDKMovieSplicer.TuSDKMovieSegment)localIterator.next();
          if (localTuSDKMovieSegment == null) {
            TLog.e("Segment is empty", new Object[0]);
          } else {
            try
            {
              TuSDKMovieSplicer.a(TuSDKMovieSplicer.this, localTuSDKMovieSegment);
            }
            catch (IOException localIOException2)
            {
              TuSDKMovieSplicer.a(TuSDKMovieSplicer.this, TuSDKMovieSplicer.ErrorCode.UnknowError);
            }
          }
        }
        TuSDKMovieSplicer.b(TuSDKMovieSplicer.this);
        if (TuSDKMovieSplicer.a(TuSDKMovieSplicer.this).listener != null) {
          TuSDKMovieSplicer.a(TuSDKMovieSplicer.this).listener.onDone();
        }
      }
    });
  }
  
  private void a()
  {
    if (this.b != null)
    {
      this.b.stop();
      this.b.release();
      this.b = null;
    }
    if (this.c != null)
    {
      this.c.quit();
      this.d = null;
    }
  }
  
  private void a(TuSDKMovieSegment paramTuSDKMovieSegment)
  {
    int i1 = (paramTuSDKMovieSegment.startTime == -1L) || (paramTuSDKMovieSegment.endTime == 0L) ? 1 : 0;
    long l1 = 0L;
    long l2 = 0L;
    MediaExtractor localMediaExtractor = TuSDKMediaUtils.createExtractor(TuSdkContext.context(), paramTuSDKMovieSegment.sourceUri);
    if (i1 != 0)
    {
      localMediaExtractor.seekTo(0L, 2);
    }
    else
    {
      l1 = paramTuSDKMovieSegment.startTime * 1000L;
      l2 = paramTuSDKMovieSegment.endTime * 1000L;
      localMediaExtractor.seekTo(l1, 2);
    }
    int i2 = 0;
    int i3 = -1;
    int i4 = -1;
    int i5 = -1;
    if (this.a.splicerType != TuSDKMovieSplicerType.SplicerAudioType) {
      i4 = TuSDKMediaUtils.getAndSelectVideoTrackIndex(localMediaExtractor);
    }
    if (this.a.splicerType != TuSDKMovieSplicerType.SplicerVideoType) {
      i5 = TuSDKMediaUtils.getAndSelectAudioTrackIndex(localMediaExtractor);
    }
    ByteBuffer localByteBuffer = ByteBuffer.allocate(this.f);
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    try
    {
      for (;;)
      {
        localBufferInfo.offset = i2;
        localBufferInfo.size = localMediaExtractor.readSampleData(localByteBuffer, i2);
        long l3 = localMediaExtractor.getSampleTime();
        if ((l2 > 0L) && (l3 >= l2) && (i1 == 0))
        {
          TLog.d("The current sample is over the trim end time.", new Object[0]);
          break;
        }
        if (localBufferInfo.size < 0)
        {
          TLog.d("Saw input EOS.", new Object[0]);
          localBufferInfo.size = 0;
          break;
        }
        localBufferInfo.presentationTimeUs = l3;
        localBufferInfo.flags = localMediaExtractor.getSampleFlags();
        i3 = localMediaExtractor.getSampleTrackIndex();
        if ((i3 == i4) && (a(i4))) {
          i3 = this.g;
        }
        if ((i3 == i5) && (a(i5))) {
          i3 = this.h;
        }
        if (i3 == this.g)
        {
          if ((this.l <= 1L) && (this.j != 1L)) {
            this.l = a(this.j, localBufferInfo.presentationTimeUs);
          }
          if (!a(this.l)) {
            this.l = 1L;
          }
          if (localBufferInfo.presentationTimeUs <= this.j) {
            localBufferInfo.presentationTimeUs = (this.j + this.l);
          }
          this.j = localBufferInfo.presentationTimeUs;
        }
        else if (i3 == this.h)
        {
          if ((this.m <= 1L) && (this.k != 1L)) {
            this.m = a(this.k, localBufferInfo.presentationTimeUs);
          }
          if (!a(this.m)) {
            this.m = 1L;
          }
          if (localBufferInfo.presentationTimeUs <= this.k) {
            localBufferInfo.presentationTimeUs = (this.k + this.m);
          }
          this.k = localBufferInfo.presentationTimeUs;
        }
        this.b.writeSampleData(i3, localByteBuffer, localBufferInfo);
        localMediaExtractor.advance();
      }
    }
    catch (IllegalStateException localIllegalStateException)
    {
      a(ErrorCode.UnknowError);
    }
    StatisticsManger.appendComponent(9449472L);
  }
  
  private boolean a(int paramInt)
  {
    return paramInt != -1;
  }
  
  private boolean a(long paramLong)
  {
    return paramLong > 1L;
  }
  
  private long a(long paramLong1, long paramLong2)
  {
    long l1 = paramLong2 - paramLong1;
    l1 = l1 > 1L ? l1 : 1L;
    return l1;
  }
  
  public static enum ErrorCode
  {
    private String a;
    
    private ErrorCode(String paramString)
    {
      this.a = paramString;
    }
    
    public String getMessage()
    {
      return this.a;
    }
  }
  
  public static enum TuSDKMovieSplicerType
  {
    private TuSDKMovieSplicerType() {}
  }
  
  public static class TuSDKMovieSplicerOption
  {
    public TuSDKMovieSplicer.TuSDKMovieSplicerType splicerType = TuSDKMovieSplicer.TuSDKMovieSplicerType.SplicerMovieType;
    public String savePath;
    public TuSDKMovieSplicer.TuSDKMovieSplicerListener listener;
  }
  
  public static abstract interface TuSDKMovieSplicerListener
  {
    public abstract void onStart();
    
    public abstract void onDone();
    
    public abstract void onError(TuSDKMovieSplicer.ErrorCode paramErrorCode);
  }
  
  public static class TuSDKMovieSegment
  {
    public Uri sourceUri;
    public long startTime = -1L;
    public long endTime = 0L;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\movie\postproc\muxer\TuSDKMovieSplicer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */