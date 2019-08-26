package org.lasque.tusdk.core.media.codec.video;

import android.annotation.TargetApi;
import java.util.Random;
import org.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFileExtractor;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFrameInfo;
import org.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class TuSdkVideoFileFrame
  extends TuSdkMediaFrameInfo
{
  public static TuSdkVideoFileFrame sync(String paramString)
  {
    _TuSdkVideoFileFrame local_TuSdkVideoFileFrame = new _TuSdkVideoFileFrame(null);
    _TuSdkVideoFileFrame.a(local_TuSdkVideoFileFrame, paramString);
    return _TuSdkVideoFileFrame.a(local_TuSdkVideoFileFrame);
  }
  
  public static TuSdkVideoFileFrame sync(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    _TuSdkVideoFileFrame local_TuSdkVideoFileFrame = new _TuSdkVideoFileFrame(null);
    _TuSdkVideoFileFrame.a(local_TuSdkVideoFileFrame, paramTuSdkMediaDataSource);
    return _TuSdkVideoFileFrame.a(local_TuSdkVideoFileFrame);
  }
  
  public static void async(String paramString, AysncTest paramAysncTest)
  {
    _TuSdkVideoFileFrame local_TuSdkVideoFileFrame = new _TuSdkVideoFileFrame(null);
    _TuSdkVideoFileFrame.a(local_TuSdkVideoFileFrame, paramString);
    _TuSdkVideoFileFrame.a(local_TuSdkVideoFileFrame, paramAysncTest);
  }
  
  public static TuSdkVideoFileFrame keyFrameInfo(TuSdkMediaExtractor paramTuSdkMediaExtractor)
  {
    _TuSdkVideoFileFrame local_TuSdkVideoFileFrame = new _TuSdkVideoFileFrame(null);
    _TuSdkVideoFileFrame.a(local_TuSdkVideoFileFrame, paramTuSdkMediaExtractor);
    return local_TuSdkVideoFileFrame;
  }
  
  private static class _TuSdkVideoFileFrame
    extends TuSdkVideoFileFrame
  {
    private TuSdkMediaExtractor a;
    private TuSdkMediaDataSource b;
    private boolean c = false;
    private TuSdkVideoFileFrame.AysncTest d;
    private TuSdkDecodecOperation e = new TuSdkDecodecOperation()
    {
      private boolean b = false;
      
      public void flush() {}
      
      public boolean decodecInit(TuSdkMediaExtractor paramAnonymousTuSdkMediaExtractor)
      {
        int i = TuSdkMediaUtils.getMediaTrackIndex(paramAnonymousTuSdkMediaExtractor, "video/");
        if (i < 0)
        {
          decodecException(new TuSdkNoMediaTrackException(String.format("%s decodecInit can not find media track: %s", new Object[] { "TuSdkVideoFileFrame", "video/" })));
          return false;
        }
        paramAnonymousTuSdkMediaExtractor.selectTrack(i);
        return TuSdkVideoFileFrame._TuSdkVideoFileFrame.a(TuSdkVideoFileFrame._TuSdkVideoFileFrame.this, paramAnonymousTuSdkMediaExtractor);
      }
      
      public boolean decodecProcessUntilEnd(TuSdkMediaExtractor paramAnonymousTuSdkMediaExtractor)
      {
        return true;
      }
      
      public void decodecRelease()
      {
        TuSdkVideoFileFrame._TuSdkVideoFileFrame.b(TuSdkVideoFileFrame._TuSdkVideoFileFrame.this);
      }
      
      public void decodecException(Exception paramAnonymousException)
      {
        if (this.b) {
          return;
        }
        this.b = true;
        TuSdkVideoFileFrame._TuSdkVideoFileFrame.b(TuSdkVideoFileFrame._TuSdkVideoFileFrame.this);
        if (TuSdkVideoFileFrame._TuSdkVideoFileFrame.c(TuSdkVideoFileFrame._TuSdkVideoFileFrame.this) != null) {
          TuSdkVideoFileFrame._TuSdkVideoFileFrame.c(TuSdkVideoFileFrame._TuSdkVideoFileFrame.this).onTestResult(TuSdkVideoFileFrame._TuSdkVideoFileFrame.this);
        }
        if (paramAnonymousException == null) {
          return;
        }
        TLog.w(paramAnonymousException.getMessage(), new Object[0]);
      }
    };
    
    private void a(String paramString)
    {
      a(new TuSdkMediaDataSource(paramString));
    }
    
    private void a(TuSdkMediaDataSource paramTuSdkMediaDataSource)
    {
      this.b = paramTuSdkMediaDataSource;
    }
    
    private void a()
    {
      if (this.c) {
        return;
      }
      this.c = true;
      if (this.a != null)
      {
        this.a.release();
        this.a = null;
      }
    }
    
    protected void finalize()
    {
      a();
      super.finalize();
    }
    
    private TuSdkVideoFileFrame b()
    {
      this.a = new TuSdkMediaFileExtractor().setDataSource(this.b);
      this.a.syncPlay();
      this.e.decodecInit(this.a);
      a();
      return this;
    }
    
    private void a(TuSdkVideoFileFrame.AysncTest paramAysncTest)
    {
      if ((this.b == null) || (!this.b.isValid()))
      {
        TLog.w("%s file path is not exists.", new Object[] { "TuSdkVideoFileFrame" });
        if (paramAysncTest != null) {
          paramAysncTest.onTestResult(this);
        }
        return;
      }
      this.d = paramAysncTest;
      this.a = new TuSdkMediaFileExtractor().setDecodecOperation(this.e).setDataSource(this.b);
      this.a.play();
    }
    
    private boolean a(TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if (paramTuSdkMediaExtractor == null) {
        return false;
      }
      long l = paramTuSdkMediaExtractor.getSampleTime();
      boolean bool = b(paramTuSdkMediaExtractor);
      paramTuSdkMediaExtractor.seekTo(l);
      return bool;
    }
    
    private boolean b(TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if (paramTuSdkMediaExtractor == null) {
        return false;
      }
      long l1 = 21474836470L;
      this.endTimeUs = paramTuSdkMediaExtractor.seekTo(l1);
      this.startTimeUs = paramTuSdkMediaExtractor.seekTo(0L);
      if ((this.startTimeUs < 0L) || (this.startTimeUs == this.endTimeUs) || (!paramTuSdkMediaExtractor.advance()))
      {
        this.e.decodecException(new TuSdkNoMediaTrackException(String.format("%s nothing frame.", new Object[] { "TuSdkVideoFileFrame" })));
        return false;
      }
      long l2 = paramTuSdkMediaExtractor.getSampleTime();
      if ((l2 < 0L) || (l2 == this.startTimeUs))
      {
        this.e.decodecException(new TuSdkNoMediaTrackException(String.format("%s only one frame: next[%d]", new Object[] { "TuSdkVideoFileFrame", Long.valueOf(l2) })));
        return false;
      }
      this.intervalUs = (l2 - this.startTimeUs);
      if (!c(paramTuSdkMediaExtractor)) {
        return false;
      }
      if (this.d != null) {
        this.d.onTestResult(this);
      }
      return true;
    }
    
    private boolean c(TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      long l1 = 0L;
      if (this.endTimeUs > this.startTimeUs) {
        l1 = new Random().nextInt((int)(this.endTimeUs / 2L));
      }
      long l2 = paramTuSdkMediaExtractor.seekTo(l1);
      paramTuSdkMediaExtractor.advance();
      long l3 = paramTuSdkMediaExtractor.getSampleTime();
      long l4 = paramTuSdkMediaExtractor.seekTo(l3);
      if ((l4 == l3) && ((paramTuSdkMediaExtractor.getSampleFlags() & 0x1) != 0))
      {
        this.keyFrameRate = 0;
        this.keyFrameIntervalUs = this.intervalUs;
        a(paramTuSdkMediaExtractor, l4);
        return true;
      }
      long[] arrayOfLong = { 1L, 1000L, this.intervalUs / 2L, this.intervalUs, this.endTimeUs };
      l4 = l2;
      for (int i = 0; (i < arrayOfLong.length) && (l4 == l2); i++)
      {
        this.skipMinUs = arrayOfLong[i];
        l4 = paramTuSdkMediaExtractor.seekTo(l2 + this.skipMinUs, 1);
      }
      if (l4 != l2)
      {
        this.keyFrameIntervalUs = (l4 - l2);
        this.keyFrameRate = ((int)(this.keyFrameIntervalUs / this.intervalUs) - 1);
        return true;
      }
      this.skipMinUs = -1L;
      return true;
    }
    
    private void a(TuSdkMediaExtractor paramTuSdkMediaExtractor, long paramLong)
    {
      long[] arrayOfLong = { 1L, 1000L, this.intervalUs - 999L, this.intervalUs - 100L, this.intervalUs, this.intervalUs + 1000L };
      long l1 = paramLong;
      for (int i = 0; (i < arrayOfLong.length) && (l1 == paramLong); i++)
      {
        this.skipMinUs = arrayOfLong[i];
        l1 = paramTuSdkMediaExtractor.seekTo(paramLong + this.skipMinUs, 1);
      }
      if (l1 == paramLong)
      {
        TLog.e("%s all key frame seek to next failed.", new Object[] { "TuSdkVideoFileFrame" });
        this.skipMinUs = -1L;
        return;
      }
      long l2 = paramLong;
      for (int j = 0; (j < arrayOfLong.length) && (l2 == paramLong); j++)
      {
        this.skipPreviousMinUs = arrayOfLong[j];
        l2 = paramTuSdkMediaExtractor.seekTo(paramLong - this.skipPreviousMinUs, 0);
      }
      if (l2 == paramLong)
      {
        TLog.e("%s all key frame seek to previous failed.", new Object[] { "TuSdkVideoFileFrame" });
        this.skipPreviousMinUs = -1L;
      }
    }
  }
  
  public static abstract interface AysncTest
  {
    public abstract void onTestResult(TuSdkVideoFileFrame paramTuSdkVideoFileFrame);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\video\TuSdkVideoFileFrame.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */