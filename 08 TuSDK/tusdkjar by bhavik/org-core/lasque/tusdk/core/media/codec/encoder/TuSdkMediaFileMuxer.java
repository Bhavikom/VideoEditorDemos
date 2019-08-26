package org.lasque.tusdk.core.media.codec.encoder;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build.VERSION;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;
import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

@TargetApi(18)
public class TuSdkMediaFileMuxer
  implements TuSdkMediaMuxer
{
  private Thread a;
  private Thread b;
  private boolean c;
  private MediaMuxer d;
  private String e;
  private FileDescriptor f;
  private int g = 0;
  private TuSdkEncodecOperation h;
  private TuSdkEncodecOperation i;
  private int j = 0;
  private int k = 0;
  private int l = -1;
  private final Map<Integer, Long> m = new HashMap();
  
  public TuSdkMediaFileMuxer setMediaMuxerFormat(int paramInt)
  {
    this.g = paramInt;
    return this;
  }
  
  public TuSdkMediaFileMuxer setPath(String paramString)
  {
    this.e = paramString;
    return this;
  }
  
  @TargetApi(26)
  public TuSdkMediaFileMuxer setFileDescriptor(FileDescriptor paramFileDescriptor)
  {
    this.f = paramFileDescriptor;
    return this;
  }
  
  public void setVideoOperation(TuSdkEncodecOperation paramTuSdkEncodecOperation)
  {
    if (this.d != null)
    {
      TLog.w("%s setVideoOperation can not after prepare", new Object[] { "TuSdkMediaFileMuxer" });
      return;
    }
    this.h = paramTuSdkEncodecOperation;
  }
  
  public void setAudioOperation(TuSdkEncodecOperation paramTuSdkEncodecOperation)
  {
    if (this.d != null)
    {
      TLog.w("%s setAudioOperation can not after prepare", new Object[] { "TuSdkMediaFileMuxer" });
      return;
    }
    this.i = paramTuSdkEncodecOperation;
  }
  
  public void pause()
  {
    this.c = false;
  }
  
  public TuSdkMediaFileMuxer resume()
  {
    this.c = true;
    return this;
  }
  
  public boolean isWorking()
  {
    return this.l == 1;
  }
  
  public boolean prepare()
  {
    if (this.l > -1)
    {
      TLog.w("%s prepare can not after initialized", new Object[] { "TuSdkMediaFileMuxer" });
      return false;
    }
    if ((this.h == null) && (this.i == null))
    {
      TLog.w("%s prepare need setVideoOperation or setAudioOperation first", new Object[] { "TuSdkMediaFileMuxer" });
      return false;
    }
    if ((this.h == null) || (this.i == null)) {
      this.k = 1;
    } else {
      this.k = 2;
    }
    try
    {
      if (this.e != null) {
        this.d = new MediaMuxer(this.e, this.g);
      } else if ((Build.VERSION.SDK_INT >= 26) && (this.f != null)) {
        this.d = new MediaMuxer(this.f, this.g);
      } else {
        throw new ExceptionInInitializerError("MediaMuxer create need setPath or setFileDescriptor");
      }
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "%s prepare need setPath or setFileDescriptor first", new Object[] { "TuSdkMediaFileMuxer" });
      this.d = null;
      return false;
    }
    this.l = 0;
    if (this.h != null)
    {
      this.a = new MediaVideoThread(null);
      this.a.start();
    }
    if (this.i != null)
    {
      this.b = new MediaAudioThread(null);
      this.b.start();
    }
    this.c = true;
    return true;
  }
  
  public void release()
  {
    pause();
    this.l = 2;
    if ((this.a != null) && (!this.a.isInterrupted())) {
      this.a.interrupt();
    }
    if ((this.b != null) && (!this.b.isInterrupted())) {
      this.b.interrupt();
    }
    if (this.d != null)
    {
      try
      {
        this.d.release();
      }
      catch (Exception localException) {}
      this.d = null;
    }
    this.a = null;
    this.b = null;
    this.h = null;
    this.i = null;
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  private void a()
  {
    a(this.d, this.h);
  }
  
  private void b()
  {
    a(this.d, this.i);
  }
  
  private void a(MediaMuxer paramMediaMuxer, TuSdkEncodecOperation paramTuSdkEncodecOperation)
  {
    try
    {
      if ((paramMediaMuxer == null) || (paramTuSdkEncodecOperation == null) || (!paramTuSdkEncodecOperation.encodecInit(this)))
      {
        paramTuSdkEncodecOperation.encodecException(new Exception(String.format("%s encodec Init failed", new Object[] { "TuSdkMediaFileMuxer" })));
        return;
      }
    }
    catch (Exception localException1)
    {
      paramTuSdkEncodecOperation.encodecException(localException1);
      return;
    }
    for (;;)
    {
      if ((!ThreadHelper.interrupted()) && (this.l != 2))
      {
        if (!this.c) {
          continue;
        }
        try
        {
          if (paramTuSdkEncodecOperation.encodecProcessUntilEnd(this)) {
            break label109;
          }
        }
        catch (Exception localException2)
        {
          paramTuSdkEncodecOperation.encodecException(localException2);
        }
      }
    }
    label109:
    paramTuSdkEncodecOperation.encodecRelease();
  }
  
  public int addTrack(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null)
    {
      TLog.w("%s addTrack need format not empty", new Object[] { "TuSdkMediaFileMuxer" });
      return -1;
    }
    if ((this.l != 0) || (this.d == null))
    {
      TLog.w("%s addTrack need after prepare, before MediaMuxer start: %s", new Object[] { "TuSdkMediaFileMuxer", Integer.valueOf(this.l) });
      return -1;
    }
    int n = this.d.addTrack(paramMediaFormat);
    if (n > -1)
    {
      this.m.put(Integer.valueOf(n), Long.valueOf(-1L));
      this.j += 1;
    }
    if (this.k == this.j)
    {
      this.d.start();
      this.l = 1;
      TLog.d("%s addTrack MediaMuxer started", new Object[] { "TuSdkMediaFileMuxer" });
    }
    return n;
  }
  
  private boolean a(int paramInt, long paramLong)
  {
    if (!this.m.containsKey(Integer.valueOf(paramInt))) {
      return false;
    }
    long l1 = ((Long)this.m.get(Integer.valueOf(paramInt))).longValue();
    if (paramLong < l1)
    {
      TLog.w("%s skip out of order frames (timestamp: %d < last: %d for track[%d]", new Object[] { "TuSdkMediaFileMuxer", Long.valueOf(paramLong), Long.valueOf(l1), Integer.valueOf(paramInt) });
      return false;
    }
    this.m.put(Integer.valueOf(paramInt), Long.valueOf(paramLong));
    return true;
  }
  
  public void writeSampleData(int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    MediaMuxer localMediaMuxer = this.d;
    if ((localMediaMuxer == null) || (paramBufferInfo == null)) {
      return;
    }
    while ((!ThreadHelper.isInterrupted()) && (this.l < 1)) {}
    if ((this.l == 1) && (a(paramInt, paramBufferInfo.presentationTimeUs))) {
      localMediaMuxer.writeSampleData(paramInt, paramByteBuffer, paramBufferInfo);
    }
    if (TLog.LOG_MEDIA_MUXER_INFO) {
      TuSdkCodecCapabilities.logBufferInfo(String.format("%s[Track: %d]", new Object[] { "TuSdkMediaFileMuxer", Integer.valueOf(paramInt) }), paramBufferInfo);
    }
  }
  
  @TargetApi(19)
  public void setLocation(float paramFloat1, float paramFloat2)
  {
    if ((this.l != 0) || (this.d == null))
    {
      TLog.w("%s setLocation need after prepare, before MediaMuxer start: %s", new Object[] { "TuSdkMediaFileMuxer", Integer.valueOf(this.l) });
      return;
    }
    this.d.setLocation(paramFloat1, paramFloat2);
  }
  
  public void setOrientationHint(int paramInt)
  {
    if ((this.l != 0) || (this.d == null))
    {
      TLog.w("%s setOrientationHint need after prepare, before MediaMuxer start: %s", new Object[] { "TuSdkMediaFileMuxer", Integer.valueOf(this.l) });
      return;
    }
    this.d.setOrientationHint(paramInt);
  }
  
  private class MediaAudioThread
    extends Thread
  {
    private MediaAudioThread() {}
    
    public void run()
    {
      TuSdkMediaFileMuxer.b(TuSdkMediaFileMuxer.this);
    }
  }
  
  private class MediaVideoThread
    extends Thread
  {
    private MediaVideoThread() {}
    
    public void run()
    {
      TuSdkMediaFileMuxer.a(TuSdkMediaFileMuxer.this);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\encoder\TuSdkMediaFileMuxer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */