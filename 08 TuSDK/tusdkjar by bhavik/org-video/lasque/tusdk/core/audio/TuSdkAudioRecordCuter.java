package org.lasque.tusdk.core.audio;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFileExtractor;
import org.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.AlbumHelper;
import org.lasque.tusdk.video.editor.TuSDKMovieWriter;
import org.lasque.tusdk.video.editor.TuSDKMovieWriterInterface.MovieWriterOutputFormat;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public class TuSdkAudioRecordCuter
{
  private int a = -1;
  private TuSdkMediaFileExtractor b = new TuSdkMediaFileExtractor().setDecodecOperation(this.c);
  private TuSdkAudioRecordCutOperation c = new TuSdkAudioRecordCutOperation();
  private TuSdkTimeRange d;
  private long e;
  private LinkedList<TuSdkTimeRange> f;
  private String g;
  private File h;
  private OnAudioRecordCuterListener i;
  
  public void setInputPath(String paramString)
  {
    this.g = paramString;
  }
  
  public void setOnAudioRecordCuterListener(OnAudioRecordCuterListener paramOnAudioRecordCuterListener)
  {
    this.i = paramOnAudioRecordCuterListener;
  }
  
  public File getOutputFile()
  {
    if (this.h == null) {
      this.h = new File(AlbumHelper.getAblumPath(), String.format("lsq_audio_%s.aac", new Object[] { StringHelper.timeStampString() }));
    }
    return this.h;
  }
  
  public void start()
  {
    if (this.b == null) {
      return;
    }
    if ((this.g == null) || ("".equals(this.g)))
    {
      TLog.e("%s  input path is invalid", new Object[] { "RecordCuter" });
      return;
    }
    this.b.setDataSource(this.g);
    this.b.play();
  }
  
  public void releas()
  {
    if (this.b != null) {
      this.b.release();
    }
    if (this.f != null) {
      this.f.clear();
    }
    this.b = null;
  }
  
  public void setOutputTimeRangeList(LinkedList<TuSdkTimeRange> paramLinkedList)
  {
    this.f = paramLinkedList;
    if (paramLinkedList == null) {
      return;
    }
    Iterator localIterator = paramLinkedList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkTimeRange localTuSdkTimeRange = (TuSdkTimeRange)localIterator.next();
      this.e += localTuSdkTimeRange.durationTimeUS();
    }
  }
  
  class TuSdkAudioRecordCutOperation
    implements TuSdkDecodecOperation
  {
    private int b = 0;
    private MediaFormat c;
    private TuSDKMovieWriter d;
    private long e;
    private long f = 0L;
    
    TuSdkAudioRecordCutOperation() {}
    
    public void flush() {}
    
    @TargetApi(16)
    public boolean decodecInit(TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      this.b = TuSdkMediaUtils.getMediaTrackIndex(paramTuSdkMediaExtractor, "audio/");
      if (this.b < 0)
      {
        decodecException(new TuSdkNoMediaTrackException(String.format("%s decodecInit can not find media track: %s", new Object[] { "RecordCuter", "audio/" })));
        TLog.e("%s Audio decodecInit mTrackIndex reulst false", new Object[] { "RecordCuter" });
        return false;
      }
      this.c = paramTuSdkMediaExtractor.getTrackFormat(this.b);
      paramTuSdkMediaExtractor.selectTrack(this.b);
      this.d = new TuSDKMovieWriter(TuSdkAudioRecordCuter.this.getOutputFile().getPath(), TuSDKMovieWriterInterface.MovieWriterOutputFormat.MPEG_4);
      this.d.addAudioTrack(this.c);
      this.d.start();
      if (this.c.containsKey("max-input-size"))
      {
        int i = this.c.getInteger("max-input-size");
        TuSdkAudioRecordCuter.a(TuSdkAudioRecordCuter.this, i > TuSdkAudioRecordCuter.a(TuSdkAudioRecordCuter.this) ? i : TuSdkAudioRecordCuter.a(TuSdkAudioRecordCuter.this));
      }
      if (TuSdkAudioRecordCuter.a(TuSdkAudioRecordCuter.this) < 0) {
        TuSdkAudioRecordCuter.a(TuSdkAudioRecordCuter.this, 1048576);
      }
      this.e = TuSDKMediaUtils.getAudioInterval(1024, this.c);
      TuSdkAudioRecordCuter.a(TuSdkAudioRecordCuter.this, (TuSdkTimeRange)TuSdkAudioRecordCuter.b(TuSdkAudioRecordCuter.this).getFirst());
      long l = paramTuSdkMediaExtractor.seekTo(TuSdkAudioRecordCuter.c(TuSdkAudioRecordCuter.this).getStartTimeUS() + 100L, 1);
      return true;
    }
    
    @TargetApi(16)
    public boolean decodecProcessUntilEnd(TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      ByteBuffer localByteBuffer = ByteBuffer.allocate(TuSdkAudioRecordCuter.a(TuSdkAudioRecordCuter.this));
      int i = paramTuSdkMediaExtractor.readSampleData(localByteBuffer, 0);
      if (i > 0)
      {
        long l = paramTuSdkMediaExtractor.getSampleTime();
        switch (a(paramTuSdkMediaExtractor, l))
        {
        case 1: 
          MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
          localBufferInfo.set(0, i, this.f, paramTuSdkMediaExtractor.getSampleFlags());
          this.d.writeAudioSampleData(localByteBuffer, localBufferInfo);
          this.f += this.e;
          a();
          break;
        case 2: 
          return false;
        case 3: 
          this.f = TuSdkAudioRecordCuter.d(TuSdkAudioRecordCuter.this);
          return true;
        }
        paramTuSdkMediaExtractor.advance();
        return false;
      }
      this.f = TuSdkAudioRecordCuter.d(TuSdkAudioRecordCuter.this);
      a();
      return true;
    }
    
    private void a()
    {
      if (TuSdkAudioRecordCuter.e(TuSdkAudioRecordCuter.this) == null) {
        return;
      }
      float f1 = (float)this.f / (float)TuSdkAudioRecordCuter.d(TuSdkAudioRecordCuter.this);
      if (f1 >= 1.0F) {
        f1 = 1.0F;
      }
      TuSdkAudioRecordCuter.e(TuSdkAudioRecordCuter.this).onProgressChanged(f1, this.f, TuSdkAudioRecordCuter.d(TuSdkAudioRecordCuter.this));
    }
    
    public void decodecRelease()
    {
      this.d.stop();
      if (TuSdkAudioRecordCuter.e(TuSdkAudioRecordCuter.this) != null) {
        TuSdkAudioRecordCuter.e(TuSdkAudioRecordCuter.this).onComplete(TuSdkAudioRecordCuter.this.getOutputFile());
      }
    }
    
    public void decodecException(Exception paramException)
    {
      TLog.e(paramException);
    }
    
    private int a(TuSdkMediaExtractor paramTuSdkMediaExtractor, long paramLong)
    {
      if (paramTuSdkMediaExtractor == null) {
        return 3;
      }
      if (TuSdkAudioRecordCuter.b(TuSdkAudioRecordCuter.this) == null) {
        return 1;
      }
      if (TuSdkAudioRecordCuter.c(TuSdkAudioRecordCuter.this).contains(paramLong)) {
        return 1;
      }
      TuSdkAudioRecordCuter.b(TuSdkAudioRecordCuter.this).removeFirst();
      if (TuSdkAudioRecordCuter.b(TuSdkAudioRecordCuter.this).size() == 0) {
        return 3;
      }
      TuSdkAudioRecordCuter.a(TuSdkAudioRecordCuter.this, (TuSdkTimeRange)TuSdkAudioRecordCuter.b(TuSdkAudioRecordCuter.this).getFirst());
      paramTuSdkMediaExtractor.seekTo(TuSdkAudioRecordCuter.c(TuSdkAudioRecordCuter.this).getStartTimeUS(), 1);
      return 2;
    }
  }
  
  public static abstract interface OnAudioRecordCuterListener
  {
    public abstract void onProgressChanged(float paramFloat, long paramLong1, long paramLong2);
    
    public abstract void onComplete(File paramFile);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\audio\TuSdkAudioRecordCuter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */