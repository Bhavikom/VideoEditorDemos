package org.lasque.tusdk.core.decoder;

import android.annotation.TargetApi;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.lasque.tusdk.core.common.TuSDKAVPacket;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

@TargetApi(16)
public class TuSDKMoviePacketReader
  extends TuSDKMovieReader
{
  private ReadMode a = ReadMode.SequenceMode;
  protected long mCurrentSampleTime = 0L;
  private boolean b;
  private TuSDKMovieReaderPacketDelegate c;
  private VideoPacketBufferProducer d = new VideoPacketBufferProducer();
  private PacketBufferConsumer e = new PacketBufferConsumer();
  private ThreadPoolExecutor f = a();
  private LinkedBlockingDeque<TuSDKAVPacket> g = new LinkedBlockingDeque();
  private TuSDKVideoTimeEffectController h = TuSDKVideoTimeEffectController.create(TuSDKVideoTimeEffectController.TimeEffectMode.NoMode);
  
  public TuSDKMoviePacketReader(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    super(paramTuSDKMediaDataSource);
  }
  
  private ThreadPoolExecutor a()
  {
    return new ThreadPoolExecutor(2, 2, 2L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(2));
  }
  
  public void setDelegate(TuSDKMovieReaderPacketDelegate paramTuSDKMovieReaderPacketDelegate)
  {
    this.c = paramTuSDKMovieReaderPacketDelegate;
  }
  
  public void setReadMode(ReadMode paramReadMode)
  {
    if (paramReadMode == ReadMode.ReverseMode) {
      this.h.setTimeEffectMode(TuSDKVideoTimeEffectController.TimeEffectMode.NoMode);
    }
    if (this.a == paramReadMode) {
      return;
    }
    this.a = paramReadMode;
    if (!this.g.isEmpty()) {
      this.d.seekToTimeUs(((TuSDKAVPacket)this.g.getFirst()).getSampleTimeUs());
    }
    this.g.clear();
  }
  
  public ReadMode getReadMode()
  {
    return this.a;
  }
  
  public void setTimeEffectController(TuSDKVideoTimeEffectController paramTuSDKVideoTimeEffectController)
  {
    this.h = paramTuSDKVideoTimeEffectController;
    this.d.seekToTimeUs(0L);
    this.g.clear();
  }
  
  public void seekTo(long paramLong, int paramInt)
  {
    super.seekTo(paramLong, paramInt);
    this.d.seekToTimeUs(paramLong);
  }
  
  public long getSampleTime()
  {
    if (this.mMediaExtractor == null) {
      return 0L;
    }
    return this.mCurrentSampleTime;
  }
  
  public int readSampleData(ByteBuffer paramByteBuffer, int paramInt)
  {
    throw new RuntimeException("Please call start medthod");
  }
  
  public void setReadAudioPacketEnable(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  protected synchronized TuSDKAVPacket readSamplePacket(int paramInt)
  {
    if (this.mMediaExtractor == null) {
      return null;
    }
    TuSDKAVPacket localTuSDKAVPacket = new TuSDKAVPacket(paramInt);
    int i = this.mMediaExtractor.readSampleData(localTuSDKAVPacket.getByteBuffer(), 0);
    if (i <= 0) {
      return null;
    }
    localTuSDKAVPacket.setSampleTimeUs(this.mMediaExtractor.getSampleTime());
    localTuSDKAVPacket.setFlags(this.mMediaExtractor.getSampleFlags());
    localTuSDKAVPacket.setChunkSize(i);
    localTuSDKAVPacket.setPacketType(isVideoSampleTrackIndex() ? 1 : 2);
    advance();
    return localTuSDKAVPacket;
  }
  
  public void start()
  {
    this.g.clear();
    this.d.start();
    this.e.start();
  }
  
  public void stop()
  {
    if (this.d != null) {
      this.d.stop();
    }
    if (this.e != null) {
      this.e.stop();
    }
    this.mCurrentSampleTime = 0L;
    try
    {
      if (this.f != null) {
        this.f.awaitTermination(200L, TimeUnit.MILLISECONDS);
      }
    }
    catch (InterruptedException localInterruptedException)
    {
      localInterruptedException.printStackTrace();
    }
    if (this.g != null) {
      this.g.clear();
    }
  }
  
  protected void destroy()
  {
    super.destroy();
    stop();
  }
  
  public class VideoPacketBufferProducer
  {
    private long b = 50L;
    private long c = 2000000L;
    private long d = -1L;
    private boolean e = false;
    private Runnable f;
    
    public VideoPacketBufferProducer() {}
    
    public void seekToTimeUs(long paramLong)
    {
      this.d = Math.min(paramLong, TuSDKMoviePacketReader.this.getVideoInfo().durationTimeUs);
      TuSDKMoviePacketReader.a(TuSDKMoviePacketReader.this).clear();
    }
    
    private void a()
    {
      TuSDKMoviePacketReader.a(TuSDKMoviePacketReader.this).clear();
      if (this.d <= 0L) {
        if (TuSDKMoviePacketReader.this.getReadMode() == TuSDKMoviePacketReader.ReadMode.ReverseMode) {
          this.d = Math.min(TuSDKMoviePacketReader.this.getTimeRange().getEndTimeUS(), TuSDKMoviePacketReader.this.getVideoInfo().durationTimeUs);
        } else {
          this.d = Math.min(TuSDKMoviePacketReader.this.getTimeRange().getStartTimeUS(), TuSDKMoviePacketReader.this.getVideoInfo().durationTimeUs);
        }
      }
    }
    
    public boolean isFinsihed()
    {
      return !this.e;
    }
    
    private boolean b()
    {
      return TuSDKMoviePacketReader.a(TuSDKMoviePacketReader.this).size() < this.b;
    }
    
    private synchronized boolean c()
    {
      if (!this.e) {
        return true;
      }
      TuSDKMoviePacketReader.this.selectVideoTrack();
      if (!b())
      {
        try
        {
          Thread.sleep(500L);
        }
        catch (InterruptedException localInterruptedException)
        {
          localInterruptedException.printStackTrace();
        }
        return false;
      }
      LinkedList localLinkedList = null;
      if (TuSDKMoviePacketReader.e(TuSDKMoviePacketReader.this) == TuSDKMoviePacketReader.ReadMode.ReverseMode) {
        localLinkedList = a(this.d - this.c, this.d, true);
      } else {
        localLinkedList = a(this.d, this.d + this.c, false);
      }
      if (this.d == 0L) {
        TuSDKMoviePacketReader.f(TuSDKMoviePacketReader.this).reset();
      }
      if ((localLinkedList != null) && (localLinkedList.size() > 0))
      {
        if (TuSDKMoviePacketReader.f(TuSDKMoviePacketReader.this).getTimeEffectMode() != TuSDKVideoTimeEffectController.TimeEffectMode.NoMode) {
          TuSDKMoviePacketReader.f(TuSDKMoviePacketReader.this).doPacketTimeEffectExtract(localLinkedList);
        }
        this.d = ((TuSDKAVPacket)localLinkedList.getLast()).getSampleTimeUs();
        TuSDKMoviePacketReader.a(TuSDKMoviePacketReader.this).addAll(localLinkedList);
        if (((TuSDKMoviePacketReader.e(TuSDKMoviePacketReader.this) == TuSDKMoviePacketReader.ReadMode.ReverseMode) && (this.d == 0L)) || (localLinkedList.size() == 1)) {
          return true;
        }
      }
      else
      {
        return true;
      }
      return false;
    }
    
    private void d()
    {
      if (!this.e) {
        return;
      }
      TuSDKMoviePacketReader.this.selectAudioTrack();
      TuSDKMoviePacketReader.this.mMediaExtractor.seekTo(TuSDKMoviePacketReader.this.getTimeRange().getStartTimeUS(), 2);
      while (this.e)
      {
        TuSDKAVPacket localTuSDKAVPacket = TuSDKMoviePacketReader.this.readSamplePacket(262144);
        if (localTuSDKAVPacket == null) {
          break;
        }
        if (TuSDKMoviePacketReader.b(TuSDKMoviePacketReader.this) != null) {
          TuSDKMoviePacketReader.b(TuSDKMoviePacketReader.this).onAVPacketAvailable(localTuSDKAVPacket);
        }
      }
    }
    
    private LinkedList<TuSDKAVPacket> a(long paramLong1, long paramLong2, boolean paramBoolean)
    {
      LinkedList localLinkedList = new LinkedList();
      paramLong1 = Math.max(paramLong1, TuSDKMoviePacketReader.this.getTimeRange().getStartTimeUS());
      paramLong2 = Math.min(paramLong2, TuSDKMoviePacketReader.this.getTimeRange().getEndTimeUS());
      if ((TuSDKMoviePacketReader.this.mMediaExtractor == null) || (paramLong2 <= paramLong1) || (paramLong2 - paramLong1 < 500L))
      {
        TLog.e("can't extract video for startTimeUs : %d  and  endTimeUs : %d ", new Object[] { Long.valueOf(paramLong1), Long.valueOf(paramLong2) });
        return localLinkedList;
      }
      TuSDKMoviePacketReader.this.selectVideoTrack();
      TuSDKMoviePacketReader.this.mMediaExtractor.seekTo(paramLong1, 0);
      while ((TuSDKMoviePacketReader.this.mMediaExtractor.getSampleTime() < paramLong1) && (TuSDKMoviePacketReader.this.mMediaExtractor.getSampleTime() != -1L)) {
        TuSDKMoviePacketReader.this.advance();
      }
      try
      {
        while ((TuSDKMoviePacketReader.this.mMediaExtractor.getSampleTime() >= paramLong1) && (TuSDKMoviePacketReader.this.mMediaExtractor.getSampleTime() <= paramLong2))
        {
          TuSDKAVPacket localTuSDKAVPacket = TuSDKMoviePacketReader.this.readSamplePacket(TuSDKMoviePacketReader.this.getVideoTrackFormat().getInteger("max-input-size"));
          if (localTuSDKAVPacket == null) {
            break;
          }
          if (paramBoolean) {
            localLinkedList.addFirst(localTuSDKAVPacket);
          } else {
            localLinkedList.addLast(localTuSDKAVPacket);
          }
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
      return localLinkedList;
    }
    
    public void start()
    {
      if (this.e) {
        return;
      }
      this.e = true;
      a();
      TuSDKMoviePacketReader.d(TuSDKMoviePacketReader.this).execute(this. = new Runnable()
      {
        public void run()
        {
          for (boolean bool = false; (TuSDKMoviePacketReader.VideoPacketBufferProducer.a(TuSDKMoviePacketReader.VideoPacketBufferProducer.this)) && (!bool); bool = TuSDKMoviePacketReader.VideoPacketBufferProducer.b(TuSDKMoviePacketReader.VideoPacketBufferProducer.this)) {}
          if ((bool) && (TuSDKMoviePacketReader.VideoPacketBufferProducer.a(TuSDKMoviePacketReader.VideoPacketBufferProducer.this)) && (TuSDKMoviePacketReader.g(TuSDKMoviePacketReader.this))) {
            TuSDKMoviePacketReader.VideoPacketBufferProducer.c(TuSDKMoviePacketReader.VideoPacketBufferProducer.this);
          }
          TuSDKMoviePacketReader.VideoPacketBufferProducer.this.stop();
        }
      });
    }
    
    public void stop()
    {
      this.e = false;
      this.d = -1L;
      TuSDKMoviePacketReader.d(TuSDKMoviePacketReader.this).remove(this.f);
      TuSDKMoviePacketReader.this.unselectVideoTrack();
    }
  }
  
  public class PacketBufferConsumer
  {
    private boolean b = false;
    private Runnable c;
    
    public PacketBufferConsumer() {}
    
    public void start()
    {
      if (this.b) {
        return;
      }
      this.b = true;
      TuSDKMoviePacketReader.a(TuSDKMoviePacketReader.this).clear();
      TuSDKMoviePacketReader.d(TuSDKMoviePacketReader.this).execute(this. = new Runnable()
      {
        public void run()
        {
          while (TuSDKMoviePacketReader.PacketBufferConsumer.a(TuSDKMoviePacketReader.PacketBufferConsumer.this))
          {
            TuSDKAVPacket localTuSDKAVPacket = TuSDKMoviePacketReader.PacketBufferConsumer.this.takePacket();
            if (localTuSDKAVPacket != null)
            {
              TuSDKMoviePacketReader.this.mCurrentSampleTime = localTuSDKAVPacket.getSampleTimeUs();
              if (TuSDKMoviePacketReader.b(TuSDKMoviePacketReader.this) != null) {
                TuSDKMoviePacketReader.b(TuSDKMoviePacketReader.this).onAVPacketAvailable(localTuSDKAVPacket);
              }
            }
            if ((TuSDKMoviePacketReader.a(TuSDKMoviePacketReader.this).isEmpty()) && (TuSDKMoviePacketReader.c(TuSDKMoviePacketReader.this).isFinsihed()))
            {
              TuSDKMoviePacketReader.PacketBufferConsumer.this.stop();
              if (TuSDKMoviePacketReader.b(TuSDKMoviePacketReader.this) == null) {
                break;
              }
              TuSDKMoviePacketReader.b(TuSDKMoviePacketReader.this).onReadComplete();
              break;
            }
          }
        }
      });
    }
    
    public void stop()
    {
      this.b = false;
      TuSDKMoviePacketReader.d(TuSDKMoviePacketReader.this).remove(this.c);
    }
    
    public TuSDKAVPacket takePacket()
    {
      try
      {
        return (TuSDKAVPacket)TuSDKMoviePacketReader.a(TuSDKMoviePacketReader.this).take();
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.printStackTrace();
      }
      return null;
    }
  }
  
  public static abstract interface TuSDKMovieReaderPacketDelegate
  {
    public abstract void onAVPacketAvailable(TuSDKAVPacket paramTuSDKAVPacket);
    
    public abstract void onReadComplete();
  }
  
  public static enum ReadMode
  {
    private ReadMode() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKMoviePacketReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */