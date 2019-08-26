package org.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.media.codec.extend.TuSdkBufferCache;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.utils.ByteUtils;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class TuSdkAudioPitchSoftImpl
  implements TuSdkAudioPitch
{
  private TuSdkAudioInfo a;
  private TuSdkAudioInfo b;
  private final Object c = new Object();
  private final List<TuSdkBufferCache> d = new ArrayList();
  private TuSdkBufferCache e;
  private ShortBuffer f;
  private ShortBuffer g;
  private SampleInfo h;
  private TuSdkAudioPitchSync i;
  private long j;
  private float k = 1.0F;
  private float l = 1.0F;
  private boolean m = false;
  private boolean n = false;
  
  public TuSdkAudioPitchSoftImpl(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null) {
      throw new NullPointerException(String.format("%s inputInfo is empty.", new Object[] { "TuSdkAudioPitchSoftImpl" }));
    }
    this.a = paramTuSdkAudioInfo;
  }
  
  public void release()
  {
    if (this.n) {
      return;
    }
    this.n = true;
    flush();
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public void setMediaSync(TuSdkAudioPitchSync paramTuSdkAudioPitchSync)
  {
    this.i = paramTuSdkAudioPitchSync;
  }
  
  public void changeFormat(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null)
    {
      TLog.w("%s changeFormat need inputInfo.", new Object[] { "TuSdkAudioPitchSoftImpl" });
      return;
    }
    this.a = paramTuSdkAudioInfo;
    b();
  }
  
  public void changePitch(float paramFloat)
  {
    if (!SdkValid.shared.audioPitchEffectsSupport())
    {
      TLog.e("You are not allowed to use audio pitch effect , please see https://tutucloud.com", new Object[0]);
      return;
    }
    if ((paramFloat <= 0.0F) || (this.l == paramFloat)) {
      return;
    }
    this.l = paramFloat;
    this.k = 1.0F;
    b();
  }
  
  public void changeSpeed(float paramFloat)
  {
    if ((paramFloat <= 0.0F) || (this.k == paramFloat)) {
      return;
    }
    this.k = paramFloat;
    this.l = 1.0F;
    b();
  }
  
  public boolean needPitch()
  {
    return this.m;
  }
  
  public void reset()
  {
    this.l = 1.0F;
    this.k = 1.0F;
    b();
  }
  
  public void flush()
  {
    a();
  }
  
  private void a()
  {
    this.j = System.nanoTime();
    synchronized (this.c)
    {
      this.h = null;
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
      this.f = ByteBuffer.allocate(paramSampleInfo.j * 1024 * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
      this.g = ByteBuffer.allocate(this.f.capacity() * paramSampleInfo.j * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
      this.d.clear();
      for (int i1 = 0; i1 < paramSampleInfo.c; i1++) {
        this.d.add(new TuSdkBufferCache(ByteBuffer.allocate(paramSampleInfo.b).order(ByteOrder.nativeOrder()), new MediaCodec.BufferInfo()));
      }
    }
  }
  
  private void b()
  {
    a();
    this.m = ((this.k != 1.0F) || (this.l != 1.0F));
    if (!this.m) {
      return;
    }
    SampleInfo localSampleInfo = new SampleInfo(null);
    localSampleInfo.h = this.j;
    localSampleInfo.k = a(this.l, this.k);
    localSampleInfo.d = (this.l * this.k);
    this.b = this.a.clone();
    this.b.bitWidth = 16;
    this.b.channelCount = 1;
    TuSdkAudioInfo tmp118_115 = this.b;
    tmp118_115.sampleRate = ((int)(tmp118_115.sampleRate / localSampleInfo.d));
    localSampleInfo.i = TuSdkAudioConvertFactory.build(this.a, this.b);
    if (localSampleInfo.i == null)
    {
      TLog.w("%s unsupport audio format: input - %s, output - %s", new Object[] { "TuSdkAudioPitchSoftImpl", this.a, this.b });
      return;
    }
    localSampleInfo.a = this.a.sampleRate;
    localSampleInfo.b = (1024 * (this.a.channelCount * (this.a.bitWidth / 8)));
    localSampleInfo.j = ((int)Math.ceil(localSampleInfo.d < 1.0F ? 1.0F / localSampleInfo.d : localSampleInfo.d));
    localSampleInfo.c = (localSampleInfo.j * 4);
    a(localSampleInfo);
    this.h = localSampleInfo;
  }
  
  private void a(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (this.i == null) {
      return;
    }
    this.i.syncAudioPitchOutputBuffer(paramByteBuffer, paramBufferInfo);
  }
  
  public boolean queueInputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (!this.m)
    {
      a(paramByteBuffer, paramBufferInfo);
      return true;
    }
    SampleInfo localSampleInfo = this.h;
    if ((paramByteBuffer == null) || (paramBufferInfo == null) || (paramBufferInfo.size < 1) || (localSampleInfo == null) || (localSampleInfo.h != this.j)) {
      return true;
    }
    if (!localSampleInfo.e)
    {
      localSampleInfo.e = true;
      localSampleInfo.g = 0L;
      localSampleInfo.f = paramBufferInfo.presentationTimeUs;
    }
    paramByteBuffer.position(paramBufferInfo.offset);
    paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
    return a(paramByteBuffer, paramBufferInfo, localSampleInfo);
  }
  
  private void a(TuSdkBufferCache paramTuSdkBufferCache, SampleInfo paramSampleInfo)
  {
    if (paramSampleInfo.h != this.j) {
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
      a(paramTuSdkBufferCache.buffer, localBufferInfo);
      this.d.add(paramTuSdkBufferCache);
      paramSampleInfo.g += 1L;
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
    if (paramSampleInfo.h != this.j) {
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
    long l1 = paramSampleInfo.g * 1024000000L / paramSampleInfo.a + paramSampleInfo.f;
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
    if (!paramByteBuffer.hasRemaining()) {
      return true;
    }
    paramSampleInfo.i.outputShorts(paramByteBuffer, this.f, ByteOrder.nativeOrder());
    boolean bool = false;
    if (!paramByteBuffer.hasRemaining()) {
      bool = (paramBufferInfo.flags & 0x4) != 0;
    }
    if ((!this.f.hasRemaining()) || (bool))
    {
      this.f.flip();
      ShortBuffer localShortBuffer = paramSampleInfo.k.a(this.f, this.g, paramSampleInfo.a, paramSampleInfo.d);
      if (!a(localShortBuffer, paramBufferInfo, paramSampleInfo, bool)) {
        return true;
      }
    }
    return a(paramByteBuffer, paramBufferInfo, paramSampleInfo);
  }
  
  private boolean a(ShortBuffer paramShortBuffer, MediaCodec.BufferInfo paramBufferInfo, SampleInfo paramSampleInfo, boolean paramBoolean)
  {
    TuSdkBufferCache localTuSdkBufferCache = d();
    if (localTuSdkBufferCache == null) {
      localTuSdkBufferCache = b(paramSampleInfo);
    }
    if (localTuSdkBufferCache == null)
    {
      TLog.w("%s can not queueInputBuffer, is forgot releaseOutputBuffer?", new Object[] { "TuSdkAudioPitchSoftImpl" });
      return false;
    }
    paramShortBuffer.position(0);
    while (paramShortBuffer.hasRemaining())
    {
      paramSampleInfo.i.restoreBytes(paramShortBuffer, localTuSdkBufferCache.buffer, ByteOrder.nativeOrder());
      if (localTuSdkBufferCache.buffer.hasRemaining())
      {
        if (paramShortBuffer.hasRemaining()) {
          TLog.e("%s convertToOutput count error: input[%s], output[%s]", new Object[] { "TuSdkAudioPitchSoftImpl", paramShortBuffer, localTuSdkBufferCache.buffer });
        }
      }
      else if (paramShortBuffer.hasRemaining())
      {
        a(localTuSdkBufferCache, paramSampleInfo);
        localTuSdkBufferCache = b(paramSampleInfo);
        if (localTuSdkBufferCache == null) {
          return true;
        }
      }
    }
    paramShortBuffer.clear();
    if ((paramBoolean) || (!localTuSdkBufferCache.buffer.hasRemaining()))
    {
      localTuSdkBufferCache.info.flags = paramBufferInfo.flags;
      a(localTuSdkBufferCache, paramSampleInfo);
      return true;
    }
    a(localTuSdkBufferCache);
    return true;
  }
  
  private TuSdkAudioPitchCalc a(float paramFloat1, float paramFloat2)
  {
    if (paramFloat2 != 1.0F) {
      return new TuSdkAudioPitchSpeed(null);
    }
    if (paramFloat1 > 1.0F) {
      return new TuSdkAudioPitchUp(null);
    }
    if (paramFloat1 < 1.0F) {
      return new TuSdkAudioPitchDown(null);
    }
    return null;
  }
  
  private static abstract class TuSdkAudioPitchCalc
  {
    abstract ShortBuffer a(ShortBuffer paramShortBuffer1, ShortBuffer paramShortBuffer2, int paramInt, float paramFloat);
    
    static void a(ShortBuffer paramShortBuffer1, ShortBuffer paramShortBuffer2, float paramFloat)
    {
      paramShortBuffer1.position(0);
      int i = paramShortBuffer1.remaining();
      int j = paramShortBuffer2.remaining();
      int k;
      if (paramFloat < 1.0F) {
        k = (int)Math.floor(i / paramFloat);
      } else {
        k = (int)Math.ceil(i / paramFloat);
      }
      int m = 0;
      int n = Math.min(k, j);
      int i1 = n - 1;
      while (m < n)
      {
        float f1 = m * paramFloat;
        int i2 = (int)Math.floor(f1);
        int i3 = (int)Math.ceil(f1);
        if ((m == i1) || (i2 == i3) || (i3 == i))
        {
          paramShortBuffer2.put(paramShortBuffer1.get(i2));
        }
        else
        {
          int i4 = paramShortBuffer1.get(i2);
          int i5 = paramShortBuffer1.get(i3);
          float f2 = i4 + (i5 - i4) * (f1 - i2);
          paramShortBuffer2.put(ByteUtils.safeShort((int)f2));
        }
        m++;
      }
      paramShortBuffer1.clear();
      paramShortBuffer2.flip();
    }
  }
  
  private static class TuSdkAudioPitchSpeed
    extends TuSdkAudioPitchSoftImpl.TuSdkAudioPitchCalc
  {
    private TuSdkAudioPitchSpeed()
    {
      super();
    }
    
    ShortBuffer a(ShortBuffer paramShortBuffer1, ShortBuffer paramShortBuffer2, int paramInt, float paramFloat)
    {
      TuSdkAudioStretch.process(paramShortBuffer1, paramShortBuffer2, paramInt, paramFloat);
      paramShortBuffer1.clear();
      return paramShortBuffer2;
    }
  }
  
  private static class TuSdkAudioPitchUp
    extends TuSdkAudioPitchSoftImpl.TuSdkAudioPitchCalc
  {
    private TuSdkAudioPitchUp()
    {
      super();
    }
    
    ShortBuffer a(ShortBuffer paramShortBuffer1, ShortBuffer paramShortBuffer2, int paramInt, float paramFloat)
    {
      TuSdkAudioStretch.process(paramShortBuffer1, paramShortBuffer2, paramInt, 1.0F / paramFloat);
      paramShortBuffer1.clear();
      a(paramShortBuffer2, paramShortBuffer1, paramFloat);
      return paramShortBuffer1;
    }
  }
  
  private static class TuSdkAudioPitchDown
    extends TuSdkAudioPitchSoftImpl.TuSdkAudioPitchCalc
  {
    private TuSdkAudioPitchDown()
    {
      super();
    }
    
    ShortBuffer a(ShortBuffer paramShortBuffer1, ShortBuffer paramShortBuffer2, int paramInt, float paramFloat)
    {
      a(paramShortBuffer1, paramShortBuffer2, paramFloat);
      TuSdkAudioStretch.process(paramShortBuffer2, paramShortBuffer1, paramInt, 1.0F / paramFloat);
      paramShortBuffer2.clear();
      return paramShortBuffer1;
    }
  }
  
  private class SampleInfo
  {
    int a;
    int b;
    int c = 0;
    float d;
    boolean e = false;
    long f = -1L;
    long g = 0L;
    long h;
    TuSdkAudioConvert i;
    int j;
    TuSdkAudioPitchSoftImpl.TuSdkAudioPitchCalc k;
    
    private SampleInfo() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioPitchSoftImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */