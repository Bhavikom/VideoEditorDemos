package org.lasque.tusdk.core.decoder;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

@SuppressLint({"InlinedApi"})
public class TuSDKAudioConverter
{
  private String a;
  private Uri b;
  private MediaExtractor c;
  private Queue<byte[]> d;
  private long e;
  private MediaCodec f;
  private ByteBuffer[] g;
  private ByteBuffer[] h;
  private AudioFormat i;
  private MediaCodec j;
  private ByteBuffer[] k;
  private ByteBuffer[] l;
  private Thread m;
  private boolean n = false;
  private TuSDKAudioConverterDelegate o;
  private volatile State p = State.UnKnow;
  private volatile boolean q = false;
  private long r;
  private long s;
  private long t;
  private long u = 23219L;
  
  public TuSDKAudioConverter(String paramString, AudioFormat paramAudioFormat)
  {
    this.a = paramString;
    this.i = paramAudioFormat;
  }
  
  public TuSDKAudioConverter(Uri paramUri, AudioFormat paramAudioFormat)
  {
    this.b = paramUri;
    this.i = paramAudioFormat;
  }
  
  public static TuSDKAudioConverter create(String paramString, AudioFormat paramAudioFormat)
  {
    return new TuSDKAudioConverter(paramString, paramAudioFormat);
  }
  
  public static TuSDKAudioConverter create(Uri paramUri, AudioFormat paramAudioFormat)
  {
    return new TuSDKAudioConverter(paramUri, paramAudioFormat);
  }
  
  public long getAudioFrameInterval()
  {
    return this.u;
  }
  
  public boolean prepare()
  {
    if ((this.i == null) || (this.i != AudioFormat.AAC))
    {
      TLog.e("Only supports aac format", new Object[0]);
      return false;
    }
    if ((this.a == null) && (this.b == null))
    {
      TLog.e("Please set a valid audio path", new Object[0]);
      return false;
    }
    if (this.a != null)
    {
      if (!new File(this.a).exists())
      {
        TLog.e("Please set a valid audio path", new Object[0]);
        return false;
      }
    }
    else if (this.b == null) {}
    this.d = new LinkedList();
    if (!a()) {
      return false;
    }
    if (!b()) {
      return false;
    }
    this.p = State.Ready;
    return true;
  }
  
  public long getDuraitonTimeUs()
  {
    return this.e;
  }
  
  public void syncAudioTimeUs(long paramLong)
  {
    if ((isStarted()) || (this.r > 0L)) {
      return;
    }
    this.r = paramLong;
    this.s = paramLong;
  }
  
  public void setLooping(boolean paramBoolean)
  {
    this.n = paramBoolean;
  }
  
  public void setDelegate(TuSDKAudioConverterDelegate paramTuSDKAudioConverterDelegate)
  {
    this.o = paramTuSDKAudioConverterDelegate;
  }
  
  private boolean a()
  {
    if ((this.c != null) || (this.f != null)) {
      return true;
    }
    try
    {
      this.c = new MediaExtractor();
      if (this.a != null) {
        this.c.setDataSource(this.a);
      } else if (this.b != null) {
        this.c.setDataSource(TuSdkContext.context(), this.b, null);
      }
      for (int i1 = 0; i1 < this.c.getTrackCount(); i1++)
      {
        MediaFormat localMediaFormat = this.c.getTrackFormat(i1);
        String str = localMediaFormat.getString("mime");
        if (str.startsWith("audio"))
        {
          this.c.selectTrack(i1);
          this.f = MediaCodec.createDecoderByType(str);
          this.f.configure(localMediaFormat, null, null, 0);
          if (!localMediaFormat.containsKey("durationUs")) {
            break;
          }
          this.e = localMediaFormat.getLong("durationUs");
          break;
        }
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    if (this.f == null)
    {
      TLog.e("create mediaDecode failed", new Object[0]);
      return false;
    }
    this.f.start();
    this.g = this.f.getInputBuffers();
    this.h = this.f.getOutputBuffers();
    TLog.d("buffers:" + this.g.length, new Object[0]);
    return true;
  }
  
  private boolean b()
  {
    if (this.j != null) {
      return true;
    }
    Object localObject;
    try
    {
      MediaFormat localMediaFormat = MediaFormat.createAudioFormat(this.i.getMeimeType(), 44100, 2);
      localMediaFormat.setInteger("bitrate", 96000);
      localMediaFormat.setInteger("aac-profile", 2);
      localMediaFormat.setInteger("max-input-size", 102400);
      byte[] arrayOfByte = { 17, -112 };
      localObject = ByteBuffer.wrap(arrayOfByte);
      localMediaFormat.setByteBuffer("csd-0", (ByteBuffer)localObject);
      this.j = MediaCodec.createEncoderByType(this.i.getMeimeType());
      this.j.configure(localMediaFormat, null, null, 1);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    if (this.j == null)
    {
      TLog.e("create mediaEncoder failed", new Object[0]);
      return false;
    }
    this.j.start();
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    for (int i1 = 0; i1 != -2; i1 = this.j.dequeueOutputBuffer(localBufferInfo, 0L)) {}
    if (i1 == -2)
    {
      localObject = this.j.getOutputFormat();
      if (((MediaFormat)localObject).containsKey("sample-rate"))
      {
        int i2 = ((MediaFormat)localObject).getInteger("sample-rate");
        this.u = (1024000000 / i2);
      }
      if (this.o != null) {
        this.o.onReady((MediaFormat)localObject);
      }
    }
    this.k = this.j.getInputBuffers();
    this.l = this.j.getOutputBuffers();
    return true;
  }
  
  public boolean isStarted()
  {
    return this.p == State.Started;
  }
  
  public boolean isStoped()
  {
    return this.p == State.Stoped;
  }
  
  public void start()
  {
    if (!ThreadHelper.isMainThread())
    {
      a(new Runnable()
      {
        public void run()
        {
          TuSDKAudioConverter.this.start();
        }
      });
      return;
    }
    if (isStarted()) {
      return;
    }
    if (!prepare()) {
      this.p = State.UnKnow;
    }
    if (this.p != State.Ready)
    {
      TLog.e("TuSDKAudioConverter start failed，Please check the configuration information", new Object[0]);
      return;
    }
    this.p = State.Started;
    this.q = false;
    this.m = new Thread(new DecoderRunnable(null));
    this.m.start();
  }
  
  private void c()
  {
    if (isStoped())
    {
      if (this.o != null) {
        this.o.onDone(this);
      }
      return;
    }
    this.q = true;
    this.p = State.Stoped;
    if ((this.m != null) && (!this.m.isInterrupted())) {
      try
      {
        this.m.join(100L);
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.printStackTrace();
      }
    }
    release();
  }
  
  public void stop()
  {
    if (!ThreadHelper.isMainThread())
    {
      a(new Runnable()
      {
        public void run()
        {
          TuSDKAudioConverter.this.stop();
        }
      });
      return;
    }
    this.q = true;
    this.n = false;
    c();
  }
  
  private void a(Runnable paramRunnable)
  {
    if (paramRunnable == null) {
      return;
    }
    ThreadHelper.post(paramRunnable);
  }
  
  private void a(byte[] paramArrayOfByte)
  {
    this.d.add(paramArrayOfByte);
  }
  
  private byte[] d()
  {
    if (isStoped()) {
      return null;
    }
    if ((this.d == null) || (this.d.isEmpty())) {
      return null;
    }
    byte[] arrayOfByte = (byte[])this.d.poll();
    return arrayOfByte;
  }
  
  private void e()
  {
    if (isStoped()) {
      return;
    }
    long l1 = 5000L;
    ByteBuffer localByteBuffer;
    for (int i1 = 0; i1 < this.g.length; i1++)
    {
      i2 = this.f.dequeueInputBuffer(l1);
      if (i2 >= 0)
      {
        localByteBuffer = this.g[i2];
        localByteBuffer.clear();
        int i3 = this.c.readSampleData(localByteBuffer, 0);
        if (this.s == 0L) {
          this.s = this.c.getSampleTime();
        }
        if (i3 < 0)
        {
          this.q = true;
          TLog.d("sampleSize < 0", new Object[0]);
        }
        else
        {
          this.f.queueInputBuffer(i2, 0, i3, this.c.getSampleTime(), 0);
          this.c.advance();
          this.t += i3;
        }
      }
    }
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    for (int i2 = this.f.dequeueOutputBuffer(localBufferInfo, l1); i2 >= 0; i2 = this.f.dequeueOutputBuffer(localBufferInfo, l1))
    {
      localByteBuffer = this.h[i2];
      byte[] arrayOfByte = new byte[localBufferInfo.size];
      localByteBuffer.get(arrayOfByte);
      localByteBuffer.clear();
      a(arrayOfByte);
      this.f.releaseOutputBuffer(i2, false);
    }
  }
  
  private void f()
  {
    if (isStoped()) {
      return;
    }
    long l1 = 5000L;
    int i3;
    ByteBuffer localByteBuffer;
    for (int i1 = 0; i1 < this.k.length; i1++)
    {
      byte[] arrayOfByte = d();
      if (arrayOfByte == null) {
        break;
      }
      i3 = this.j.dequeueInputBuffer(l1);
      if (i3 >= 0)
      {
        localByteBuffer = this.k[i3];
        localByteBuffer.clear();
        localByteBuffer.limit(arrayOfByte.length);
        localByteBuffer.put(arrayOfByte);
        this.j.queueInputBuffer(i3, 0, arrayOfByte.length, 0L, 0);
      }
    }
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    for (int i2 = this.j.dequeueOutputBuffer(localBufferInfo, l1); i2 >= 0; i2 = this.j.dequeueOutputBuffer(localBufferInfo, l1))
    {
      i3 = localBufferInfo.size;
      localByteBuffer = this.l[i2];
      localByteBuffer.position(localBufferInfo.offset);
      localByteBuffer.limit(localBufferInfo.offset + i3);
      localBufferInfo.flags = 1;
      if (this.s == 0L) {
        this.s = localBufferInfo.presentationTimeUs;
      } else {
        localBufferInfo.presentationTimeUs = (this.s + this.u);
      }
      if (localBufferInfo.presentationTimeUs > this.s)
      {
        this.s = localBufferInfo.presentationTimeUs;
        if (this.r == 0L) {
          this.r = this.s;
        }
        if (this.o != null)
        {
          this.o.onNewSampleData(localBufferInfo.presentationTimeUs, localByteBuffer.duplicate(), localBufferInfo);
          this.o.onProgressChanged((this.s - this.r) / 1000L / 1000L);
        }
      }
      this.j.releaseOutputBuffer(i2, false);
      localBufferInfo = new MediaCodec.BufferInfo();
    }
  }
  
  public void release()
  {
    if (this.c != null)
    {
      this.c.release();
      this.c = null;
    }
    if (this.j != null)
    {
      this.j.stop();
      this.j.release();
      this.j = null;
    }
    if (this.f != null)
    {
      this.f.stop();
      this.f.release();
      this.f = null;
    }
    if (this.d != null)
    {
      this.d.clear();
      this.d = null;
    }
    this.g = null;
    this.k = null;
  }
  
  private void g()
  {
    if (!isStarted()) {
      return;
    }
    c();
    if (this.n) {
      start();
    } else if (this.o != null) {
      this.o.onDone(this);
    }
  }
  
  private class DecoderRunnable
    implements Runnable
  {
    private DecoderRunnable() {}
    
    public void run()
    {
      long l = System.currentTimeMillis();
      while (!TuSDKAudioConverter.a(TuSDKAudioConverter.this))
      {
        TuSDKAudioConverter.b(TuSDKAudioConverter.this);
        while ((!TuSDKAudioConverter.this.isStoped()) && (TuSDKAudioConverter.c(TuSDKAudioConverter.this).size() > 0)) {
          TuSDKAudioConverter.d(TuSDKAudioConverter.this);
        }
      }
      TuSDKAudioConverter.a(TuSDKAudioConverter.this, new Runnable()
      {
        public void run()
        {
          TuSDKAudioConverter.e(TuSDKAudioConverter.this);
        }
      });
      TLog.d("DecodeSize:" + TuSDKAudioConverter.f(TuSDKAudioConverter.this) + "time:" + (System.currentTimeMillis() - l), new Object[0]);
    }
  }
  
  public static enum AudioFormat
  {
    private String a;
    
    private AudioFormat(String paramString)
    {
      this.a = paramString;
    }
    
    public String getMeimeType()
    {
      return this.a;
    }
  }
  
  public static abstract interface TuSDKAudioConverterDelegate
  {
    public abstract void onReady(MediaFormat paramMediaFormat);
    
    public abstract void onNewSampleData(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
    
    public abstract void onDone(TuSDKAudioConverter paramTuSDKAudioConverter);
    
    public abstract boolean onProgressChanged(long paramLong);
  }
  
  public static enum State
  {
    private State() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKAudioConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */