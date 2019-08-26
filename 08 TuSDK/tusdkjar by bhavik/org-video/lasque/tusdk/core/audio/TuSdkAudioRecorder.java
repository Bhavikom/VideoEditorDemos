package org.lasque.tusdk.core.audio;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioEngine.TuSdKAudioEngineOutputBufferDelegate;
import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine;
import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine.TuSdkAudioEnginePitchTypeChangeDelegate;
import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine.TuSdkSoundPitchType;
import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioRecord.TuSdkAudioRecordListener;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrack;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackImpl;
import org.lasque.tusdk.core.media.codec.audio.TuSdkMicRecord;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkAudioEncoder;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncoderListener;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileMuxer;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSyncBase;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.AlbumHelper;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public class TuSdkAudioRecorder
{
  public static final int UNINITIALIZED_RECORD = 0;
  public static final int INIT_RECORD = 1;
  public static final int START_RECORD = 2;
  public static final int RESUME_RECORD = 3;
  public static final int PAUSE_RECORD = 4;
  public static final int STOP_RECORD = 5;
  private TuSdkAudioRecorderSetting a;
  private TuSdkMicRecord b;
  private TuSdkAudioResample c;
  private TuSdkAudioEncoder d;
  private _AudioSync e;
  private TuSdkMediaFileMuxer f;
  private File g;
  private TuSdkAudioPitchEngine h;
  private TuSdkAudioPitchEngine.TuSdkSoundPitchType i = TuSdkAudioPitchEngine.TuSdkSoundPitchType.Normal;
  private Object j = new Object();
  private Object k = new Object();
  private int l = 0;
  private boolean m;
  private boolean n = false;
  private long o;
  private long p;
  private long q;
  private long r;
  private long s;
  private long t;
  private long u = 10000000L;
  private TuSdkTimeRange v = null;
  private LinkedList<TuSdkTimeRange> w;
  private LinkedList<TuSdkTimeRange> x;
  private TuSdkAudioRecorderListener y;
  private TuSdkAudioRecord.TuSdkAudioRecordListener z = new TuSdkAudioRecord.TuSdkAudioRecordListener()
  {
    public void onAudioRecordOutputBuffer(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      synchronized (TuSdkAudioRecorder.a(TuSdkAudioRecorder.this))
      {
        if (TuSdkAudioRecorder.b(TuSdkAudioRecorder.this) == 0L) {
          TuSdkAudioRecorder.a(TuSdkAudioRecorder.this, paramAnonymousBufferInfo.presentationTimeUs);
        }
        TuSdkAudioRecorder.b(TuSdkAudioRecorder.this, paramAnonymousBufferInfo.presentationTimeUs);
        if (TuSdkAudioRecorder.c(TuSdkAudioRecorder.this))
        {
          if (TuSdkAudioRecorder.d(TuSdkAudioRecorder.this) == 0L) {
            TuSdkAudioRecorder.c(TuSdkAudioRecorder.this, paramAnonymousBufferInfo.presentationTimeUs);
          }
          return;
        }
        paramAnonymousBufferInfo.presentationTimeUs -= TuSdkAudioRecorder.e(TuSdkAudioRecorder.this);
        TuSdkAudioRecorder.d(TuSdkAudioRecorder.this, paramAnonymousBufferInfo.presentationTimeUs - TuSdkAudioRecorder.b(TuSdkAudioRecorder.this));
        TuSdkAudioRecorder.e(TuSdkAudioRecorder.this, ((float)TuSdkAudioRecorder.f(TuSdkAudioRecorder.this) * TuSdkAudioRecorder.g(TuSdkAudioRecorder.this).getSpeed()));
        paramAnonymousBufferInfo.presentationTimeUs = TuSdkAudioRecorder.h(TuSdkAudioRecorder.this);
      }
      if (TuSdkAudioRecorder.i(TuSdkAudioRecorder.this) != null) {
        TuSdkAudioRecorder.i(TuSdkAudioRecorder.this).queueInputBuffer(paramAnonymousByteBuffer, paramAnonymousBufferInfo);
      }
    }
    
    public void onAudioRecordError(int paramAnonymousInt)
    {
      if (TuSdkAudioRecorder.j(TuSdkAudioRecorder.this) != null) {
        TuSdkAudioRecorder.j(TuSdkAudioRecorder.this).onRecordError(paramAnonymousInt);
      }
    }
  };
  private TuSdkAudioResampleSync A = new TuSdkAudioResampleSync()
  {
    public void syncAudioResampleOutputBuffer(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkAudioRecorder.k(TuSdkAudioRecorder.this) != null) {
        TuSdkAudioRecorder.k(TuSdkAudioRecorder.this).processInputBuffer(paramAnonymousByteBuffer, paramAnonymousBufferInfo);
      }
    }
    
    public void release() {}
  };
  private TuSdkAudioTrack B;
  private TuSdkAudioEngine.TuSdKAudioEngineOutputBufferDelegate C = new TuSdkAudioEngine.TuSdKAudioEngineOutputBufferDelegate()
  {
    public void onProcess(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkAudioRecorder.l(TuSdkAudioRecorder.this).getOperation().writeBuffer(paramAnonymousByteBuffer, paramAnonymousBufferInfo);
    }
  };
  private TuSdkEncoderListener D = new TuSdkEncoderListener()
  {
    public void onEncoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo) {}
    
    public void onEncoderCompleted(Exception paramAnonymousException)
    {
      TLog.e(paramAnonymousException);
    }
  };
  
  public TuSdkAudioRecorder(TuSdkAudioRecorderSetting paramTuSdkAudioRecorderSetting, TuSdkAudioRecorderListener paramTuSdkAudioRecorderListener)
  {
    this.a = paramTuSdkAudioRecorderSetting;
    this.y = paramTuSdkAudioRecorderListener;
    a();
  }
  
  private void a()
  {
    if ((this.a == null) || (this.a.bitRate == 0) || (this.a.channelCount == 0) || (this.a.sampleRate == 0))
    {
      TLog.e("%s Init AudioRecorder Failed ，Parameter anomaly AudioInfo is :%s", new Object[] { "TuSdkAudioRecorder", this.a });
      return;
    }
    this.w = new LinkedList();
    this.x = new LinkedList();
    this.d = new TuSdkAudioEncoder();
    this.e = new _AudioSync();
    this.d.setOutputFormat(this.a.getRecordMediaFormat());
    this.d.setMediaSync(this.e);
    this.d.setListener(this.D);
    if (!this.d.prepare()) {
      TLog.e("%s Encoder init failed", new Object[] { "TuSdkAudioRecorder" });
    }
    this.f = new TuSdkMediaFileMuxer();
    this.f.setAudioOperation(this.d.getOperation());
    this.f.setPath(getOutputFileTemp().getPath());
    this.b = new TuSdkMicRecord();
    this.b.setListener(this.z);
    this.b.setAudioInfo(new TuSdkAudioInfo(b()));
    this.c = new TuSdkAudioResampleHardImpl(new TuSdkAudioInfo(this.a.getRecordMediaFormat()));
    this.c.changeFormat(new TuSdkAudioInfo(b()));
    this.c.setMediaSync(this.A);
    this.B = new TuSdkAudioTrackImpl(new TuSdkAudioInfo(this.a.getRecordMediaFormat()));
    this.h = new TuSdkAudioPitchEngine(new TuSdkAudioInfo(this.a.getRecordMediaFormat()), true);
    this.h.changeAudioInfo(new TuSdkAudioInfo(this.a.getRecordMediaFormat()));
    this.h.setOutputBufferDelegate(this.C);
    a(1);
  }
  
  private MediaFormat b()
  {
    MediaFormat localMediaFormat = TuSdkMediaFormat.buildSafeAudioEncodecFormat(44100, 1, 96000, 2);
    return localMediaFormat;
  }
  
  public void setSoundPitchType(TuSdkAudioPitchEngine.TuSdkSoundPitchType paramTuSdkSoundPitchType)
  {
    if ((this.h == null) || (paramTuSdkSoundPitchType == null)) {
      return;
    }
    this.h.flush();
    this.h.setSoundPitchType(paramTuSdkSoundPitchType);
  }
  
  public void setSoundPitchTypeChangeListener(TuSdkAudioPitchEngine.TuSdkAudioEnginePitchTypeChangeDelegate paramTuSdkAudioEnginePitchTypeChangeDelegate)
  {
    if ((this.h == null) || (paramTuSdkAudioEnginePitchTypeChangeDelegate == null)) {
      return;
    }
    this.h.setSoundTypeChangeListener(paramTuSdkAudioEnginePitchTypeChangeDelegate);
  }
  
  public void start()
  {
    if ((this.b == null) || (this.d == null) || (this.f == null) || (this.m)) {
      return;
    }
    if (!this.f.prepare()) {
      TLog.e("%s Start Record Failed ! Muxer Prepare Error", new Object[] { "TuSdkAudioRecorder" });
    }
    this.b.startRecording();
    this.m = true;
    a(2);
  }
  
  public void resume()
  {
    synchronized (this.j)
    {
      if (!this.n) {
        return;
      }
      this.r += this.p - this.q;
      this.n = false;
      a(3);
    }
  }
  
  public void pause()
  {
    synchronized (this.j)
    {
      if (this.n) {
        return;
      }
      this.q = 0L;
      this.n = true;
      a(4);
    }
  }
  
  public void stop()
  {
    if ((this.b == null) || (!this.m)) {
      return;
    }
    this.b.stop();
    this.h.flush();
    this.d.release();
    this.f.release();
    this.h.release();
    this.m = true;
    a(5);
    TLog.d(" %s time slice %s", new Object[] { "TuSdkAudioRecorder", this.w });
  }
  
  public void releas()
  {
    if (this.l != 5) {
      stop();
    }
    this.w.clear();
    this.x.clear();
  }
  
  public boolean isPause()
  {
    return this.n;
  }
  
  public boolean isStart()
  {
    return this.m;
  }
  
  public void setMaxRecordTime(long paramLong)
  {
    this.u = paramLong;
  }
  
  public File getOutputFileTemp()
  {
    if (this.g == null) {
      this.g = new File(AlbumHelper.getAblumPath(), String.format("lsq_temp_%s.aac", new Object[] { StringHelper.timeStampString() }));
    }
    return this.g;
  }
  
  public void setOutputFile(File paramFile)
  {
    this.g = paramFile;
    this.f.setPath(this.g.getPath());
  }
  
  private void a(int paramInt)
  {
    this.l = paramInt;
    synchronized (this.k)
    {
      switch (paramInt)
      {
      case 2: 
        this.v = TuSdkTimeRange.makeRange(0.0F, -1.0F);
        break;
      case 3: 
        this.v = TuSdkTimeRange.makeTimeUsRange(this.t, -1L);
        break;
      case 4: 
        if (this.v == null) {
          return;
        }
        this.v.setEndTimeUs(this.t);
        this.w.addLast(this.v);
        this.v = null;
        break;
      case 5: 
        if ((this.v != null) && (this.v.getEndTimeUS() == -1L))
        {
          this.v.setEndTimeUs(this.t);
          this.w.addLast(this.v);
          this.v = null;
        }
        break;
      }
    }
    if (this.y != null) {
      this.y.onStateChanged(paramInt);
    }
  }
  
  public TuSdkTimeRange removeLastRecordRange()
  {
    if (this.w.size() == 0) {
      return null;
    }
    synchronized (this.k)
    {
      TuSdkTimeRange localTuSdkTimeRange = (TuSdkTimeRange)this.w.removeLast();
      this.x.addLast(localTuSdkTimeRange);
      TLog.d("removeLastRecordRange() : mRecordingTimeRangeList size: %s  mDropTimeRangeList size :%s ", new Object[] { Integer.valueOf(this.w.size()), Integer.valueOf(this.x.size()) });
      a(getValidTimeUs());
      return localTuSdkTimeRange;
    }
  }
  
  public long getValidTimeUs()
  {
    return this.t - c();
  }
  
  public LinkedList<TuSdkTimeRange> getRecordingTimeRangeList()
  {
    return this.w;
  }
  
  public LinkedList<TuSdkTimeRange> getDropTimeRangeList()
  {
    return this.x;
  }
  
  private void a(long paramLong)
  {
    long l1 = paramLong;
    float f1 = (float)l1 / (float)this.u;
    TLog.d("%s notifyProgressChanged() record timeUS %s  mTotalTimeUS ：%s  percent :%s", new Object[] { "TuSdkAudioRecorder", Long.valueOf(l1), Long.valueOf(this.u), Float.valueOf(f1) });
    this.y.onRecordProgress(l1, f1);
  }
  
  private long c()
  {
    if (this.x.size() == 0) {
      return 0L;
    }
    long l1 = 0L;
    Iterator localIterator = this.x.iterator();
    while (localIterator.hasNext())
    {
      TuSdkTimeRange localTuSdkTimeRange = (TuSdkTimeRange)localIterator.next();
      l1 += localTuSdkTimeRange.durationTimeUS();
    }
    return l1;
  }
  
  public int getState()
  {
    return this.l;
  }
  
  class _AudioSync
    extends TuSdkAudioEncodecSyncBase
  {
    _AudioSync() {}
    
    public void syncAudioEncodecInfo(TuSdkAudioInfo paramTuSdkAudioInfo)
    {
      super.syncAudioEncodecInfo(paramTuSdkAudioInfo);
      TuSdkAudioRecorder.k(TuSdkAudioRecorder.this).changeAudioInfo(paramTuSdkAudioInfo);
    }
    
    public void syncAudioEncodecOutputBuffer(TuSdkMediaMuxer paramTuSdkMediaMuxer, int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      super.syncAudioEncodecOutputBuffer(paramTuSdkMediaMuxer, paramInt, paramByteBuffer, paramBufferInfo);
      if ((TuSdkAudioRecorder.j(TuSdkAudioRecorder.this) == null) || (paramBufferInfo == null)) {
        return;
      }
      TuSdkAudioRecorder.f(TuSdkAudioRecorder.this, paramBufferInfo.presentationTimeUs - TuSdkAudioRecorder.m(TuSdkAudioRecorder.this));
      if (TuSdkAudioRecorder.this.getValidTimeUs() >= TuSdkAudioRecorder.n(TuSdkAudioRecorder.this) + 100000L) {
        TuSdkAudioRecorder.this.pause();
      }
    }
  }
  
  public static abstract interface TuSdkAudioRecorderListener
  {
    public static final int PARAMETRTS_ERROR = 2001;
    public static final int PERMISSION_ERROR = 2002;
    
    public abstract void onRecordProgress(long paramLong, float paramFloat);
    
    public abstract void onStateChanged(int paramInt);
    
    public abstract void onRecordError(int paramInt);
  }
  
  public static class TuSdkAudioRecorderSetting
  {
    public int channelCount = 1;
    public int sampleRate = 44100;
    public int bitRate = 32768;
    
    public MediaFormat getRecordMediaFormat()
    {
      if ((this.channelCount == 0) || (this.sampleRate == 0) || (this.bitRate == 0)) {
        TLog.e("%s Audio Setting Parameter error  %s", new Object[] { "TuSdkAudioRecorder", toString() });
      }
      return TuSdkMediaFormat.buildSafeAudioEncodecFormat(this.sampleRate, this.channelCount, this.bitRate, 2);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("{");
      localStringBuilder.append("channelCount : ");
      localStringBuilder.append(this.channelCount);
      localStringBuilder.append("\n");
      localStringBuilder.append("sampleRate : ");
      localStringBuilder.append(this.sampleRate);
      localStringBuilder.append("\n");
      localStringBuilder.append("bitRate : ");
      localStringBuilder.append(this.bitRate);
      localStringBuilder.append("}");
      return super.toString();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\audio\TuSdkAudioRecorder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */