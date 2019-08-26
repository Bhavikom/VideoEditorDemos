package org.lasque.tusdk.api.movie.postproc.muxer;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore.Video.Media;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.utils.TLog;

@SuppressLint({"InlinedApi"})
public class TuSDKMovieClipper
{
  private TuSDKMovieClipperOption a;
  private MediaExtractor b;
  private MediaMuxer c;
  private HandlerThread d;
  private Handler e;
  private long f = 0L;
  private int g = -1;
  private HashMap<Integer, Integer> h;
  private int i = -1;
  private int j = -1;
  private long k;
  private long l;
  private long m;
  private long n;
  private int o;
  private boolean p = false;
  
  public TuSDKMovieClipper(TuSDKMovieClipperOption paramTuSDKMovieClipperOption)
  {
    this.a = paramTuSDKMovieClipperOption;
    if (this.a.srcUri != null)
    {
      MediaPlayer localMediaPlayer = MediaPlayer.create(TuSdkContext.context(), this.a.srcUri);
      this.f = (localMediaPlayer.getDuration() * 1000);
      localMediaPlayer.release();
    }
  }
  
  private boolean a(long paramLong1, long paramLong2)
  {
    paramLong1 = Math.min(paramLong1, this.f);
    paramLong2 = Math.min(paramLong2, this.f);
    if ((paramLong1 >= paramLong2) || (paramLong1 < 0L) || (paramLong2 <= 0L))
    {
      TLog.e("create segment is invalid", new Object[0]);
      return false;
    }
    return true;
  }
  
  public TuSDKMovieSegment getTotalSegment(long paramLong)
  {
    return createSegment(0L, paramLong);
  }
  
  public TuSDKMovieSegment createSegment(long paramLong1, long paramLong2)
  {
    if (!a(paramLong1, paramLong2)) {
      return null;
    }
    TuSDKMovieSegment localTuSDKMovieSegment = new TuSDKMovieSegment();
    localTuSDKMovieSegment.startTime = paramLong1;
    localTuSDKMovieSegment.endTime = paramLong2;
    return localTuSDKMovieSegment;
  }
  
  public void addSegment(TuSDKMovieSegment paramTuSDKMovieSegment, List<TuSDKMovieSegment> paramList)
  {
    if ((paramTuSDKMovieSegment == null) || (paramList == null)) {
      TLog.e("addSegment", new Object[] { new RuntimeException("segment==" + paramTuSDKMovieSegment + "list==" + paramList) });
    }
    if ((paramTuSDKMovieSegment != null) && (paramList != null)) {
      paramList.add(paramTuSDKMovieSegment);
    }
  }
  
  public List<TuSDKMovieSegment> getSegmentList(long paramLong, List<TuSDKMovieSegment> paramList)
  {
    Object localObject = new ArrayList();
    TuSDKMovieSegment localTuSDKMovieSegment1 = getTotalSegment(paramLong);
    ((List)localObject).add(localTuSDKMovieSegment1);
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TuSDKMovieSegment localTuSDKMovieSegment2 = (TuSDKMovieSegment)localIterator.next();
      localObject = getNewList(localTuSDKMovieSegment2, localTuSDKMovieSegment1, (List)localObject);
    }
    if ((localObject == null) || (((List)localObject).size() == 0))
    {
      TLog.e("newlist", new Object[] { new RuntimeException("newlist== null || newlist.size() == 0") });
      return null;
    }
    Collections.sort((List)localObject, new Comparator()
    {
      public int compare(TuSDKMovieClipper.TuSDKMovieSegment paramAnonymousTuSDKMovieSegment1, TuSDKMovieClipper.TuSDKMovieSegment paramAnonymousTuSDKMovieSegment2)
      {
        long l1 = paramAnonymousTuSDKMovieSegment1.startTime;
        long l2 = paramAnonymousTuSDKMovieSegment2.startTime;
        return (int)(l1 - l2);
      }
    });
    return (List<TuSDKMovieSegment>)localObject;
  }
  
  public List<TuSDKMovieSegment> getNewList(TuSDKMovieSegment paramTuSDKMovieSegment1, TuSDKMovieSegment paramTuSDKMovieSegment2, List<TuSDKMovieSegment> paramList)
  {
    long l1 = paramTuSDKMovieSegment1.startTime;
    long l2 = paramTuSDKMovieSegment1.endTime;
    ArrayList localArrayList = new ArrayList();
    if (paramList.size() == 0)
    {
      if (l1 != paramTuSDKMovieSegment2.startTime)
      {
        localObject = createSegment(paramTuSDKMovieSegment2.startTime, l2);
        addSegment((TuSDKMovieSegment)localObject, localArrayList);
      }
      if (l2 != paramTuSDKMovieSegment2.endTime)
      {
        localObject = createSegment(l1, paramTuSDKMovieSegment2.endTime);
        addSegment((TuSDKMovieSegment)localObject, localArrayList);
      }
      return localArrayList;
    }
    localArrayList.addAll(paramList);
    Object localObject = paramList.iterator();
    while (((Iterator)localObject).hasNext())
    {
      TuSDKMovieSegment localTuSDKMovieSegment1 = (TuSDKMovieSegment)((Iterator)localObject).next();
      if ((l1 < localTuSDKMovieSegment1.endTime) && (l2 > localTuSDKMovieSegment1.startTime))
      {
        localArrayList.remove(localTuSDKMovieSegment1);
        TuSDKMovieSegment localTuSDKMovieSegment2;
        if (localTuSDKMovieSegment1.startTime == l1)
        {
          localTuSDKMovieSegment2 = createSegment(l2, localTuSDKMovieSegment1.endTime);
          addSegment(localTuSDKMovieSegment2, localArrayList);
        }
        else if (localTuSDKMovieSegment1.endTime == l2)
        {
          localTuSDKMovieSegment2 = createSegment(localTuSDKMovieSegment1.startTime, l1);
          addSegment(localTuSDKMovieSegment2, localArrayList);
        }
        else
        {
          localTuSDKMovieSegment2 = createSegment(localTuSDKMovieSegment1.startTime, l1);
          addSegment(localTuSDKMovieSegment2, localArrayList);
          TuSDKMovieSegment localTuSDKMovieSegment3 = createSegment(l2, localTuSDKMovieSegment1.endTime);
          addSegment(localTuSDKMovieSegment3, localArrayList);
        }
      }
    }
    return localArrayList;
  }
  
  @SuppressLint({"UseSparseArrays"})
  private void a()
  {
    this.p = false;
    this.l = 0L;
    this.k = 0L;
    this.c = TuSDKMediaUtils.createMuxer(this.a.savePath, 0);
    this.b = TuSDKMediaUtils.createExtractor(TuSdkContext.context(), this.a.srcUri);
    int i1 = this.b.getTrackCount();
    this.h = new HashMap(i1);
    for (int i2 = 0; i2 < i1; i2++)
    {
      localObject = this.b.getTrackFormat(i2);
      String str = ((MediaFormat)localObject).getString("mime");
      int i4;
      int i5;
      if (str.startsWith("audio/"))
      {
        this.b.selectTrack(i2);
        i4 = this.c.addTrack((MediaFormat)localObject);
        this.j = i2;
        this.h.put(Integer.valueOf(i2), Integer.valueOf(i4));
        if (((MediaFormat)localObject).containsKey("max-input-size"))
        {
          i5 = ((MediaFormat)localObject).getInteger("max-input-size");
          this.g = (i5 > this.g ? i5 : this.g);
        }
        this.n = TuSDKMediaUtils.getAudioInterval(1024, (MediaFormat)localObject);
      }
      else if (str.startsWith("video/"))
      {
        this.b.selectTrack(i2);
        i4 = this.c.addTrack((MediaFormat)localObject);
        this.i = i2;
        this.h.put(Integer.valueOf(i2), Integer.valueOf(i4));
        if (((MediaFormat)localObject).containsKey("max-input-size"))
        {
          i5 = ((MediaFormat)localObject).getInteger("max-input-size");
          this.g = (i5 > this.g ? i5 : this.g);
        }
        boolean bool = TuSDKMediaUtils.containsKeyFrameRate((MediaFormat)localObject);
        if (bool) {
          this.o = TuSDKMediaUtils.getVideoFps((MediaFormat)localObject);
        } else if ((!bool) && (this.a != null)) {
          this.o = this.a.fps;
        } else {
          TLog.e("MediaFormat is not contains KEY_FRAME_RATE", new Object[0]);
        }
      }
    }
    if (this.g < 0) {
      this.g = 1048576;
    }
    MediaMetadataRetriever localMediaMetadataRetriever = new MediaMetadataRetriever();
    localMediaMetadataRetriever.setDataSource(TuSdkContext.context(), this.a.srcUri);
    Object localObject = localMediaMetadataRetriever.extractMetadata(24);
    if (localObject != null)
    {
      int i3 = Integer.parseInt((String)localObject);
      if (i3 >= 0) {
        this.c.setOrientationHint(i3);
      }
    }
    this.c.start();
  }
  
  @Deprecated
  public void startEdit(List<TuSDKMovieSegment> paramList)
  {
    removeSegments(paramList);
  }
  
  public void removeSegments(final List<TuSDKMovieSegment> paramList)
  {
    if ((this.a == null) || (paramList == null) || (paramList.size() == 0))
    {
      TLog.e("is invalid option or segment", new Object[0]);
      return;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TuSDKMovieSegment localTuSDKMovieSegment = (TuSDKMovieSegment)localIterator.next();
      if ((localTuSDKMovieSegment == null) || (!a(localTuSDKMovieSegment.startTime, localTuSDKMovieSegment.endTime)))
      {
        TLog.e("is invalid segment", new Object[0]);
        return;
      }
    }
    if ((this.d == null) || (this.e == null))
    {
      this.d = new HandlerThread("TuSDKMovieEditThread");
      this.d.start();
      this.e = new Handler(this.d.getLooper());
    }
    this.e.post(new Runnable()
    {
      public void run()
      {
        TuSDKMovieClipper.a(TuSDKMovieClipper.this);
        List localList = TuSDKMovieClipper.this.getSegmentList(TuSDKMovieClipper.b(TuSDKMovieClipper.this), paramList);
        if ((localList == null) || (localList.size() == 0))
        {
          TLog.e("newList == null  || newList.size() == 0" + new RuntimeException(), new Object[0]);
          return;
        }
        if (TuSDKMovieClipper.c(TuSDKMovieClipper.this).listener != null) {
          TuSDKMovieClipper.c(TuSDKMovieClipper.this).listener.onStart();
        }
        Iterator localIterator = localList.iterator();
        while (localIterator.hasNext())
        {
          TuSDKMovieClipper.TuSDKMovieSegment localTuSDKMovieSegment = (TuSDKMovieClipper.TuSDKMovieSegment)localIterator.next();
          try
          {
            TuSDKMovieClipper.a(TuSDKMovieClipper.this, TuSDKMovieClipper.c(TuSDKMovieClipper.this).srcUri, TuSDKMovieClipper.c(TuSDKMovieClipper.this).savePath, localTuSDKMovieSegment.startTime, localTuSDKMovieSegment.endTime);
          }
          catch (IOException localIOException)
          {
            localIOException.printStackTrace();
          }
        }
        TuSDKMovieClipper.d(TuSDKMovieClipper.this);
        if (TuSDKMovieClipper.c(TuSDKMovieClipper.this).listener == null) {
          return;
        }
        if (TuSDKMovieClipper.e(TuSDKMovieClipper.this)) {
          TuSDKMovieClipper.c(TuSDKMovieClipper.this).listener.onCancel();
        } else {
          TuSDKMovieClipper.c(TuSDKMovieClipper.this).listener.onDone(TuSDKMovieClipper.c(TuSDKMovieClipper.this).savePath);
        }
      }
    });
  }
  
  public void saveSegments(final List<TuSDKMovieSegment> paramList)
  {
    if ((this.a == null) || (paramList == null) || (paramList.size() == 0))
    {
      TLog.e("is invalid option or segmentList", new Object[0]);
      return;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TuSDKMovieSegment localTuSDKMovieSegment = (TuSDKMovieSegment)localIterator.next();
      if ((localTuSDKMovieSegment == null) || (!a(localTuSDKMovieSegment.startTime, localTuSDKMovieSegment.endTime)))
      {
        TLog.e("is invalid segment", new Object[0]);
        return;
      }
    }
    if ((this.d == null) || (this.e == null))
    {
      this.d = new HandlerThread("TuSDKMovieEditThread");
      this.d.start();
      this.e = new Handler(this.d.getLooper());
    }
    this.e.post(new Runnable()
    {
      public void run()
      {
        TuSDKMovieClipper.a(TuSDKMovieClipper.this);
        if (TuSDKMovieClipper.c(TuSDKMovieClipper.this).listener != null) {
          TuSDKMovieClipper.c(TuSDKMovieClipper.this).listener.onStart();
        }
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
          TuSDKMovieClipper.TuSDKMovieSegment localTuSDKMovieSegment = (TuSDKMovieClipper.TuSDKMovieSegment)localIterator.next();
          try
          {
            TuSDKMovieClipper.a(TuSDKMovieClipper.this, TuSDKMovieClipper.c(TuSDKMovieClipper.this).srcUri, TuSDKMovieClipper.c(TuSDKMovieClipper.this).savePath, localTuSDKMovieSegment.startTime, localTuSDKMovieSegment.endTime);
          }
          catch (IOException localIOException)
          {
            localIOException.printStackTrace();
          }
        }
        TuSDKMovieClipper.d(TuSDKMovieClipper.this);
        if (TuSDKMovieClipper.c(TuSDKMovieClipper.this).listener == null) {
          return;
        }
        if (TuSDKMovieClipper.e(TuSDKMovieClipper.this)) {
          TuSDKMovieClipper.c(TuSDKMovieClipper.this).listener.onCancel();
        } else {
          TuSDKMovieClipper.c(TuSDKMovieClipper.this).listener.onDone(TuSDKMovieClipper.c(TuSDKMovieClipper.this).savePath);
        }
      }
    });
  }
  
  public void cancel()
  {
    this.p = true;
  }
  
  private void b()
  {
    if (this.c != null)
    {
      this.c.stop();
      this.c.release();
      this.c = null;
    }
    if (this.b != null)
    {
      this.b.release();
      this.b = null;
    }
    a(this.a.savePath);
    if (this.d != null)
    {
      this.d.quit();
      this.e = null;
    }
  }
  
  private void a(Uri paramUri, String paramString, long paramLong1, long paramLong2)
  {
    long l1 = a(paramUri, paramLong1, paramLong2);
    if (this.o == 0) {
      this.o = 15;
    }
    if ((l1 > 0L) && (l1 < 500000L)) {
      this.m = l1;
    } else {
      this.m = TuSDKMediaUtils.getVideoInterval(this.o);
    }
    l1 = b(paramUri, paramLong1, paramLong2);
    if ((l1 > 0L) && (l1 < 500000L)) {
      this.n = l1;
    } else {
      this.n = TuSDKMediaUtils.getAudioDefaultInterval();
    }
    this.b.seekTo(paramLong1, 2);
    int i1 = -1;
    ByteBuffer localByteBuffer = ByteBuffer.allocate(this.g);
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    try
    {
      while (!this.p)
      {
        localBufferInfo.offset = 0;
        localBufferInfo.size = this.b.readSampleData(localByteBuffer, 0);
        if (localBufferInfo.size < 0)
        {
          TLog.d("Saw input EOS.", new Object[0]);
          localBufferInfo.size = 0;
          break;
        }
        long l2 = this.b.getSampleTime();
        if ((paramLong2 > 0L) && (l2 >= paramLong2))
        {
          TLog.d("The current sample is over the trim end time.", new Object[0]);
          break;
        }
        localBufferInfo.flags = this.b.getSampleFlags();
        i1 = this.b.getSampleTrackIndex();
        if (i1 == this.i)
        {
          localBufferInfo.presentationTimeUs = (this.k + this.m);
          this.k = localBufferInfo.presentationTimeUs;
        }
        else if (i1 == this.j)
        {
          localBufferInfo.presentationTimeUs = (this.l + this.n);
          this.l = localBufferInfo.presentationTimeUs;
        }
        this.c.writeSampleData(((Integer)this.h.get(Integer.valueOf(i1))).intValue(), localByteBuffer, localBufferInfo);
        this.b.advance();
      }
    }
    catch (IllegalStateException localIllegalStateException)
    {
      TLog.w("The source video file is malformed", new Object[0]);
    }
    StatisticsManger.appendComponent(9449475L);
  }
  
  private void a(String paramString)
  {
    MediaPlayer localMediaPlayer = MediaPlayer.create(TuSdkContext.context(), Uri.parse(paramString));
    int i1 = localMediaPlayer.getDuration();
    localMediaPlayer.release();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("mime_type", "video/mp4");
    localContentValues.put("_data", paramString);
    localContentValues.put("duration", Integer.valueOf(i1));
    TuSdkContext.context().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
    Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
    Uri localUri = Uri.fromFile(new File(paramString));
    localIntent.setData(localUri);
    TuSdkContext.context().sendBroadcast(localIntent);
  }
  
  private long a(Uri paramUri, long paramLong1, long paramLong2)
  {
    return b(paramUri, "video/", paramLong1, paramLong2);
  }
  
  private long b(Uri paramUri, long paramLong1, long paramLong2)
  {
    return b(paramUri, "audio/", paramLong1, paramLong2);
  }
  
  private long b(Uri paramUri, String paramString, long paramLong1, long paramLong2)
  {
    int i1 = 0;
    MediaExtractor localMediaExtractor = TuSDKMediaUtils.createExtractor(TuSdkContext.context(), paramUri);
    int i2 = localMediaExtractor.getTrackCount();
    int i3 = -1;
    for (int i4 = 0; i4 < i2; i4++)
    {
      localObject = localMediaExtractor.getTrackFormat(i4);
      String str = ((MediaFormat)localObject).getString("mime");
      if (str.startsWith(paramString))
      {
        localMediaExtractor.selectTrack(i4);
        if (((MediaFormat)localObject).containsKey("max-input-size"))
        {
          int i5 = ((MediaFormat)localObject).getInteger("max-input-size");
          i3 = i5 > i3 ? i5 : i3;
        }
        i1 = 1;
      }
    }
    if (i1 == 0) {
      return 0L;
    }
    if (i3 < 0) {
      i3 = 1048576;
    }
    localMediaExtractor.seekTo(paramLong1, 2);
    ByteBuffer localByteBuffer = ByteBuffer.allocate(i3);
    Object localObject = new MediaCodec.BufferInfo();
    long l1 = 0L;
    for (;;)
    {
      ((MediaCodec.BufferInfo)localObject).offset = 0;
      ((MediaCodec.BufferInfo)localObject).size = localMediaExtractor.readSampleData(localByteBuffer, 0);
      if (((MediaCodec.BufferInfo)localObject).size < 0)
      {
        ((MediaCodec.BufferInfo)localObject).size = 0;
        break;
      }
      long l2 = localMediaExtractor.getSampleTime();
      if ((paramLong2 > 0L) && (l2 >= paramLong2)) {
        break;
      }
      l1 += 1L;
      localMediaExtractor.advance();
    }
    if (l1 == 0L) {
      return 0L;
    }
    return (paramLong2 - paramLong1) / l1;
  }
  
  public static abstract interface TuSDKMovieClipperListener
  {
    public abstract void onStart();
    
    public abstract void onCancel();
    
    public abstract void onDone(String paramString);
    
    public abstract void onError(Exception paramException);
  }
  
  public static class TuSDKMovieClipperOption
  {
    public String savePath;
    public Uri srcUri;
    public TuSDKMovieClipper.TuSDKMovieClipperListener listener;
    public int fps;
  }
  
  public static class TuSDKMovieSegment
  {
    protected long startTime;
    protected long endTime;
    
    public String toString()
    {
      return "segment start = " + this.startTime + " end = " + this.endTime;
    }
    
    public long getStartTime()
    {
      return this.startTime;
    }
    
    public long getEndTime()
    {
      return this.endTime;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\movie\postproc\muxer\TuSDKMovieClipper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */