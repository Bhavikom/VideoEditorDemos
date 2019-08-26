package org.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.media.codec.extend.TuSdkBufferCache;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class TuSdkAudioResampleSoftImpl
  implements TuSdkAudioResample
{
  private TuSdkAudioInfo a;
  private TuSdkAudioInfo b;
  private final Object c = new Object();
  private final List<TuSdkBufferCache> d = new ArrayList();
  private TuSdkBufferCache e;
  private boolean f = false;
  private SampleInfo g;
  private TuSdkAudioResampleSync h;
  private long i;
  private float j = 1.0F;
  private boolean k = false;
  private TuSdkBufferCache l;
  private boolean m = false;
  private long n = -1L;
  
  public TuSdkAudioResampleSoftImpl(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null) {
      throw new NullPointerException(String.format("%s outputInfo is empty.", new Object[] { "TuSdkAudioResampleSoftImpl" }));
    }
    this.b = paramTuSdkAudioInfo;
  }
  
  public long getLastInputTimeUs()
  {
    SampleInfo localSampleInfo = this.g;
    if (localSampleInfo == null) {
      return -1L;
    }
    return localSampleInfo.q;
  }
  
  public long getPrefixTimeUs()
  {
    SampleInfo localSampleInfo = this.g;
    if (localSampleInfo == null) {
      return -1L;
    }
    return localSampleInfo.n;
  }
  
  public void setStartPrefixTimeUs(long paramLong)
  {
    this.n = paramLong;
    b();
  }
  
  public void setMediaSync(TuSdkAudioResampleSync paramTuSdkAudioResampleSync)
  {
    this.h = paramTuSdkAudioResampleSync;
  }
  
  public void changeFormat(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null)
    {
      TLog.w("%s changeFormat need inputInfo.", new Object[] { "TuSdkAudioResampleSoftImpl" });
      return;
    }
    this.a = paramTuSdkAudioInfo;
    b();
  }
  
  public void changeSpeed(float paramFloat)
  {
    if ((paramFloat <= 0.0F) || (this.j == paramFloat)) {
      return;
    }
    this.j = paramFloat;
    b();
  }
  
  public void changeSequence(boolean paramBoolean)
  {
    if (this.k == paramBoolean) {
      return;
    }
    this.k = paramBoolean;
    b();
  }
  
  public boolean needResample()
  {
    return this.f;
  }
  
  public void flush()
  {
    a();
  }
  
  public void reset()
  {
    this.j = 1.0F;
    this.k = false;
    this.n = -1L;
    b();
  }
  
  private void a()
  {
    this.i = System.nanoTime();
    synchronized (this.c)
    {
      this.g = null;
      this.e = null;
      this.d.clear();
    }
  }
  
  private void a(SampleInfo paramSampleInfo)
  {
    if (paramSampleInfo == null) {
      return;
    }
    synchronized (this.c)
    {
      this.e = null;
      this.d.clear();
      for (int i1 = 0; i1 < paramSampleInfo.k; i1++) {
        this.d.add(new TuSdkBufferCache(ByteBuffer.allocate(paramSampleInfo.j).order(ByteOrder.nativeOrder()), new MediaCodec.BufferInfo()));
      }
    }
  }
  
  public void release()
  {
    if (this.m) {
      return;
    }
    this.m = true;
    flush();
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  private void b()
  {
    a();
    if (this.a == null) {
      this.a = this.b.clone();
    }
    this.f = ((this.a.sampleRate != this.b.sampleRate) || (this.a.channelCount != this.b.channelCount) || (this.a.bitWidth != this.b.bitWidth) || (this.j != 1.0F) || (this.k));
    if (!this.f) {
      return;
    }
    SampleInfo localSampleInfo = new SampleInfo(null);
    localSampleInfo.s = this.i;
    localSampleInfo.l = (this.a.sampleRate * this.j / this.b.sampleRate);
    localSampleInfo.t = TuSdkAudioConvertFactory.build(this.a, this.b);
    if (localSampleInfo.t == null)
    {
      TLog.w("%s unsupport audio format: input - %s, output - %s", new Object[] { "TuSdkAudioResampleSoftImpl" });
      return;
    }
    localSampleInfo.b = this.a.bitWidth;
    localSampleInfo.c = this.a.channelCount;
    localSampleInfo.d = this.a.sampleRate;
    localSampleInfo.a = (this.a.channelCount * (this.a.bitWidth / 8));
    localSampleInfo.e = (1024 * localSampleInfo.a);
    localSampleInfo.g = this.b.bitWidth;
    localSampleInfo.h = this.b.channelCount;
    localSampleInfo.i = this.b.sampleRate;
    localSampleInfo.f = (this.b.channelCount * (this.b.bitWidth / 8));
    localSampleInfo.j = (1024 * localSampleInfo.f);
    localSampleInfo.k = ((int)Math.ceil(1.0F / localSampleInfo.l) * 4);
    a(localSampleInfo);
    this.g = localSampleInfo;
  }
  
  private TuSdkBufferCache a(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    TuSdkBufferCache localTuSdkBufferCache = new TuSdkBufferCache(paramByteBuffer, paramBufferInfo);
    if ((!this.k) || (paramByteBuffer == null) || (paramBufferInfo == null)) {
      return localTuSdkBufferCache;
    }
    if ((this.l == null) || (this.l.buffer.capacity() < paramBufferInfo.size))
    {
      this.l = new TuSdkBufferCache();
      this.l.buffer = ByteBuffer.allocate(paramBufferInfo.size).order(ByteOrder.nativeOrder());
    }
    this.l.info = TuSdkMediaUtils.cloneBufferInfo(paramBufferInfo);
    this.l.info.offset = 0;
    if (this.g != null) {
      this.g.t.inputReverse(paramByteBuffer, this.l.buffer);
    }
    return this.l;
  }
  
  private void b(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (this.h == null) {
      return;
    }
    this.h.syncAudioResampleOutputBuffer(paramByteBuffer, paramBufferInfo);
  }
  
  public boolean queueInputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (!SdkValid.shared.audioResampleEffectsSupport())
    {
      TLog.e("You are not allowed to use resample effect , please see https://tutucloud.com", new Object[0]);
      return false;
    }
    TuSdkBufferCache localTuSdkBufferCache = a(paramByteBuffer, paramBufferInfo);
    if (!this.f)
    {
      b(localTuSdkBufferCache.buffer, localTuSdkBufferCache.info);
      return true;
    }
    SampleInfo localSampleInfo = this.g;
    if ((localTuSdkBufferCache.buffer == null) || (localTuSdkBufferCache.info == null) || (localTuSdkBufferCache.info.size < 1) || (localSampleInfo == null) || (localSampleInfo.s != this.i)) {
      return true;
    }
    return a(localTuSdkBufferCache.buffer, localTuSdkBufferCache.info, localSampleInfo);
  }
  
  private void a(TuSdkBufferCache paramTuSdkBufferCache, SampleInfo paramSampleInfo)
  {
    if (paramSampleInfo.s != this.i) {
      return;
    }
    synchronized (this.c)
    {
      MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
      localBufferInfo.size = paramTuSdkBufferCache.buffer.capacity();
      localBufferInfo.offset = 0;
      localBufferInfo.flags = paramTuSdkBufferCache.info.flags;
      localBufferInfo.presentationTimeUs = paramTuSdkBufferCache.info.presentationTimeUs;
      paramTuSdkBufferCache.buffer.position(0);
      paramTuSdkBufferCache.buffer.limit(localBufferInfo.size);
      b(paramTuSdkBufferCache.buffer, localBufferInfo);
      this.d.add(paramTuSdkBufferCache);
      paramSampleInfo.r += 1L;
    }
  }
  
  private TuSdkBufferCache c()
  {
    TuSdkBufferCache localTuSdkBufferCache = null;
    synchronized (this.c)
    {
      if (this.d.size() > 0)
      {
        localTuSdkBufferCache = (TuSdkBufferCache)this.d.remove(0);
        localTuSdkBufferCache.clear();
      }
    }
    return localTuSdkBufferCache;
  }
  
  private TuSdkBufferCache b(SampleInfo paramSampleInfo)
  {
    if (paramSampleInfo.s != this.i) {
      return null;
    }
    TuSdkBufferCache localTuSdkBufferCache = c();
    if (localTuSdkBufferCache != null) {
      localTuSdkBufferCache.info.presentationTimeUs = c(paramSampleInfo);
    }
    return localTuSdkBufferCache;
  }
  
  private long c(SampleInfo paramSampleInfo)
  {
    long l1 = paramSampleInfo.r * 1024000000L / paramSampleInfo.i + paramSampleInfo.n;
    return l1;
  }
  
  private TuSdkBufferCache d()
  {
    TuSdkBufferCache localTuSdkBufferCache = null;
    synchronized (this.c)
    {
      localTuSdkBufferCache = this.e;
      this.e = null;
    }
    return localTuSdkBufferCache;
  }
  
  private void a(TuSdkBufferCache paramTuSdkBufferCache)
  {
    if (paramTuSdkBufferCache == null) {
      return;
    }
    synchronized (this.c)
    {
      paramTuSdkBufferCache.info.size = paramTuSdkBufferCache.buffer.position();
      this.e = paramTuSdkBufferCache;
    }
  }
  
  private boolean a(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, SampleInfo paramSampleInfo)
  {
    TuSdkBufferCache localTuSdkBufferCache = d();
    if (localTuSdkBufferCache == null) {
      localTuSdkBufferCache = c();
    }
    if (localTuSdkBufferCache == null)
    {
      TLog.w("%s can not queueInputBuffer, is forgot releaseOutputBuffer?", new Object[] { "TuSdkAudioResampleSoftImpl" });
      return false;
    }
    if (!paramSampleInfo.m)
    {
      paramSampleInfo.m = true;
      paramSampleInfo.r = 0L;
      paramSampleInfo.n = (this.n < 0L ? paramBufferInfo.presentationTimeUs : this.n);
      paramSampleInfo.q = paramSampleInfo.n;
      paramSampleInfo.o = paramSampleInfo.n;
      localTuSdkBufferCache.clear();
      localTuSdkBufferCache.info.presentationTimeUs = paramSampleInfo.n;
    }
    if (localTuSdkBufferCache.info.presentationTimeUs < 0L) {
      localTuSdkBufferCache.info.presentationTimeUs = c(paramSampleInfo);
    }
    paramSampleInfo.p = paramSampleInfo.q;
    paramSampleInfo.q = paramBufferInfo.presentationTimeUs;
    paramSampleInfo.o += Math.abs((float)(paramSampleInfo.q - paramSampleInfo.p) / this.j);
    long l1 = b(localTuSdkBufferCache, paramSampleInfo);
    if (l1 < paramSampleInfo.o)
    {
      long l2 = (paramSampleInfo.o - l1) * paramSampleInfo.i / 1000000L;
      if (l2 > 100L) {
        localTuSdkBufferCache = a(localTuSdkBufferCache, paramSampleInfo, l2);
      }
    }
    paramByteBuffer.position(paramBufferInfo.offset);
    paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
    a(paramByteBuffer, paramBufferInfo, paramSampleInfo, localTuSdkBufferCache);
    return true;
  }
  
  private void a(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, SampleInfo paramSampleInfo, TuSdkBufferCache paramTuSdkBufferCache)
  {
    if (paramTuSdkBufferCache == null) {
      return;
    }
    if (!paramByteBuffer.hasRemaining())
    {
      paramTuSdkBufferCache.info.flags = paramBufferInfo.flags;
      if ((paramBufferInfo.flags & 0x4) != 0) {
        a(paramTuSdkBufferCache, paramSampleInfo);
      } else if (paramTuSdkBufferCache.buffer.hasRemaining()) {
        a(paramTuSdkBufferCache);
      }
      return;
    }
    int i1 = paramByteBuffer.remaining() / paramSampleInfo.a;
    int i2 = paramByteBuffer.position();
    int i3 = paramTuSdkBufferCache.buffer.remaining() / paramSampleInfo.f;
    int i4;
    if (paramSampleInfo.l < 1.0F) {
      i4 = (int)Math.floor(i1 / paramSampleInfo.l);
    } else {
      i4 = (int)Math.ceil(i1 / paramSampleInfo.l);
    }
    byte[] arrayOfByte1 = new byte[paramSampleInfo.a * 2];
    int i5 = 0;
    int i6 = Math.min(i4, i3);
    int i7 = i6 - 1;
    while (i5 < i6)
    {
      float f1 = i5 * paramSampleInfo.l;
      int i8 = (int)Math.floor(f1);
      int i9 = (int)Math.ceil(f1);
      int i10;
      if ((i5 == i7) || (i8 == i9) || (i9 == i1))
      {
        i10 = i8 * paramSampleInfo.a + i2;
        if (!a(paramByteBuffer, i10, arrayOfByte1, 0, paramSampleInfo.a)) {
          return;
        }
        byte[] arrayOfByte2 = paramSampleInfo.t.outputBytes(arrayOfByte1, paramByteBuffer.order(), 0, paramSampleInfo.a);
        paramTuSdkBufferCache.buffer.put(arrayOfByte2);
      }
      else
      {
        i10 = i8 * paramSampleInfo.a + i2;
        int i11 = i9 * paramSampleInfo.a + i2;
        if (!a(paramByteBuffer, i10, arrayOfByte1, 0, paramSampleInfo.a)) {
          return;
        }
        if (!a(paramByteBuffer, i11, arrayOfByte1, paramSampleInfo.a, paramSampleInfo.a)) {
          return;
        }
        byte[] arrayOfByte3 = paramSampleInfo.t.outputResamle(arrayOfByte1, f1 - i8, paramByteBuffer.order());
        paramTuSdkBufferCache.buffer.put(arrayOfByte3);
      }
      i5++;
    }
    if (!paramTuSdkBufferCache.buffer.hasRemaining())
    {
      a(paramTuSdkBufferCache, paramSampleInfo);
      paramTuSdkBufferCache = b(paramSampleInfo);
      if (paramTuSdkBufferCache == null) {
        return;
      }
    }
    paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
    a(paramByteBuffer, paramBufferInfo, paramSampleInfo, paramTuSdkBufferCache);
  }
  
  private boolean a(ByteBuffer paramByteBuffer, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
  {
    try
    {
      paramByteBuffer.position(paramInt1);
      paramByteBuffer.get(paramArrayOfByte, paramInt2, paramInt3);
    }
    catch (Exception localException)
    {
      return false;
    }
    return true;
  }
  
  private TuSdkBufferCache a(TuSdkBufferCache paramTuSdkBufferCache, SampleInfo paramSampleInfo, long paramLong)
  {
    if (paramLong < 1L) {
      return paramTuSdkBufferCache;
    }
    int i1 = paramTuSdkBufferCache.buffer.remaining() / paramSampleInfo.f;
    int i2 = (int)Math.min(paramLong, i1);
    byte[] arrayOfByte = new byte[i2 * paramSampleInfo.f];
    paramTuSdkBufferCache.buffer.put(arrayOfByte);
    paramTuSdkBufferCache.info.size += arrayOfByte.length;
    if (!paramTuSdkBufferCache.buffer.hasRemaining())
    {
      a(paramTuSdkBufferCache, paramSampleInfo);
      paramTuSdkBufferCache = b(paramSampleInfo);
      if (paramTuSdkBufferCache == null) {
        return null;
      }
    }
    paramLong -= i2;
    return a(paramTuSdkBufferCache, paramSampleInfo, paramLong);
  }
  
  private long b(TuSdkBufferCache paramTuSdkBufferCache, SampleInfo paramSampleInfo)
  {
    int i1 = paramTuSdkBufferCache.buffer.position() / paramSampleInfo.f;
    long l1 = i1 * 1000000 / paramSampleInfo.i;
    l1 += paramTuSdkBufferCache.info.presentationTimeUs;
    return l1;
  }
  
  private class SampleInfo
  {
    int a;
    int b;
    int c;
    int d;
    int e;
    int f;
    int g;
    int h;
    int i;
    int j;
    int k = 0;
    float l;
    boolean m = false;
    long n = -1L;
    long o = 0L;
    long p = -1L;
    long q = -1L;
    long r = 0L;
    long s;
    TuSdkAudioConvert t;
    
    private SampleInfo() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioResampleSoftImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */