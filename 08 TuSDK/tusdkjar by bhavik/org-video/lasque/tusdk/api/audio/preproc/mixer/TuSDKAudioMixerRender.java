package org.lasque.tusdk.api.audio.preproc.mixer;

import android.media.MediaCodec.BufferInfo;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender.TuSdkAudioRenderCallback;
import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManager;
import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManager.State;
import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.video.editor.TuSdkMediaAudioEffectData;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public class TuSDKAudioMixerRender
  implements TuSdkAudioRender
{
  private boolean a = true;
  private String b = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lsq_lastTemp_audio_PCM";
  private FileOutputStream c;
  private TuSDKAudioDecoderTaskManager d = new TuSDKAudioDecoderTaskManager();
  private List<TuSDKAudioRenderEntry> e = new ArrayList();
  private List<RawAudioTrack> f;
  private TuSDKAudioRenderInfoWrap g;
  private float h = 1.0F;
  private float i = 1.0F;
  private TuSdkOnMixerRenderStateListener j;
  private TuSDKAudioMixerState k = TuSDKAudioMixerState.None;
  private TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener l = new TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener()
  {
    public void onStateChanged(TuSDKAudioDecoderTaskManager.State paramAnonymousState)
    {
      if (paramAnonymousState == TuSDKAudioDecoderTaskManager.State.Idle) {
        TuSDKAudioMixerRender.this.notifyStateChanged(TuSDKAudioMixerRender.TuSDKAudioMixerState.None);
      } else if (paramAnonymousState == TuSDKAudioDecoderTaskManager.State.Decoding) {
        TuSDKAudioMixerRender.this.notifyStateChanged(TuSDKAudioMixerRender.TuSDKAudioMixerState.Loading);
      } else if (paramAnonymousState == TuSDKAudioDecoderTaskManager.State.Complete) {
        TuSDKAudioMixerRender.this.notifyStateChanged(TuSDKAudioMixerRender.TuSDKAudioMixerState.Loaded);
      } else if (paramAnonymousState == TuSDKAudioDecoderTaskManager.State.Cancelled) {
        TuSDKAudioMixerRender.this.notifyStateChanged(TuSDKAudioMixerRender.TuSDKAudioMixerState.DecodeCancel);
      }
    }
  };
  private long m = -1L;
  
  public void setWirteTempFile(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
  
  public TuSDKAudioMixerRender()
  {
    try
    {
      this.c = new FileOutputStream(this.b);
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      TLog.e(localFileNotFoundException);
    }
  }
  
  public void setAudioRenderEntryList(List<TuSDKAudioRenderEntry> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0))
    {
      TLog.e("%s mix audio list is null ", new Object[] { "TuSDKAudioMixerRender" });
      return;
    }
    this.e.clear();
    this.e.addAll(paramList);
  }
  
  public void notifyAudioDataChanged(List<TuSdkMediaAudioEffectData> paramList)
  {
    if ((paramList != null) || (paramList.size() > 0))
    {
      ArrayList localArrayList = new ArrayList();
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        TuSdkMediaAudioEffectData localTuSdkMediaAudioEffectData = (TuSdkMediaAudioEffectData)localIterator.next();
        localArrayList.add(localTuSdkMediaAudioEffectData.getAudioEntry());
      }
      if (this.e.containsAll(localArrayList)) {
        return;
      }
      this.e.clear();
      this.e.addAll(localArrayList);
      if (this.f != null) {
        this.f.clear();
      }
    }
  }
  
  public void clearAllAudioData()
  {
    this.e.clear();
    if (this.f != null) {
      this.f.clear();
    }
  }
  
  public List<TuSDKAudioRenderEntry> getAudioRenderEntryList()
  {
    return this.e;
  }
  
  public void loadAudio()
  {
    if (this.g == null)
    {
      TLog.e("%s  You have to set up TrunkAudioInfo before this.", new Object[] { "TuSDKAudioMixerRender" });
      return;
    }
    this.d.setAudioEntry(this.e);
    this.d.setDelegate(this.l);
    if (this.g != null) {
      this.d.setTrunkAudioInfo(this.g.getRealAudioInfo());
    }
    this.d.start();
  }
  
  public void cancel()
  {
    this.d.cancel();
  }
  
  public void setMixerRenderStateListener(TuSdkOnMixerRenderStateListener paramTuSdkOnMixerRenderStateListener)
  {
    this.j = paramTuSdkOnMixerRenderStateListener;
  }
  
  public void seekTo(long paramLong)
  {
    Iterator localIterator = a().iterator();
    while (localIterator.hasNext())
    {
      RawAudioTrack localRawAudioTrack = (RawAudioTrack)localIterator.next();
      localRawAudioTrack.seekUs(paramLong);
    }
  }
  
  public void closeReaders()
  {
    Iterator localIterator = a().iterator();
    while (localIterator.hasNext())
    {
      RawAudioTrack localRawAudioTrack = (RawAudioTrack)localIterator.next();
      RawAudioTrack.a(localRawAudioTrack);
    }
  }
  
  private List<RawAudioTrack> a()
  {
    if ((this.f == null) || (this.f.size() != this.e.size()))
    {
      this.f = new ArrayList();
      for (int n = 0; n < this.e.size(); n++) {
        this.f.add(new RawAudioTrack((TuSDKAudioRenderEntry)this.e.get(n)));
      }
    }
    return this.f;
  }
  
  public void setTrunkAudioVolume(float paramFloat)
  {
    this.i = paramFloat;
  }
  
  public void setSecondAudioTrack(float paramFloat)
  {
    this.h = paramFloat;
  }
  
  private byte[] a(byte[] paramArrayOfByte, float paramFloat)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    for (int n = 0; n < arrayOfByte.length; n += 2)
    {
      int i1 = (short)paramArrayOfByte[(n + 1)];
      int i2 = (short)paramArrayOfByte[n];
      i1 = (short)((i1 & 0xFF) << 8);
      i2 = (short)(i2 & 0xFF);
      int i3 = (short)(i1 | i2);
      i3 = (short)(int)(i3 * paramFloat);
      arrayOfByte[n] = ((byte)i3);
      arrayOfByte[(n + 1)] = ((byte)(i3 >> 8));
    }
    return arrayOfByte;
  }
  
  public synchronized boolean onAudioSliceRender(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkAudioRender.TuSdkAudioRenderCallback paramTuSdkAudioRenderCallback)
  {
    if ((paramTuSdkAudioRenderCallback == null) || (paramTuSdkAudioRenderCallback.isEncodec())) {
      return false;
    }
    if ((paramBufferInfo == null) || (paramByteBuffer == null) || (this.e == null) || (this.k == TuSDKAudioMixerState.Loading)) {
      return false;
    }
    if (paramBufferInfo.presentationTimeUs == this.m) {
      return false;
    }
    this.m = paramBufferInfo.presentationTimeUs;
    notifyStateChanged(TuSDKAudioMixerState.Mixing);
    byte[][] arrayOfByte = new byte[a().size() + 1][];
    arrayOfByte[0] = new byte[paramBufferInfo.size];
    paramByteBuffer.get(arrayOfByte[0]);
    arrayOfByte[0] = a(arrayOfByte[0], this.i);
    try
    {
      for (int n = 0; n < a().size(); n++)
      {
        localObject = new byte[paramBufferInfo.size];
        int i1 = ((RawAudioTrack)a().get(n)).a(paramBufferInfo.presentationTimeUs, (byte[])localObject);
        if (i1 != -1) {
          arrayOfByte[(n + 1)] = a((byte[])localObject, this.h);
        } else {
          arrayOfByte[(n + 1)] = localObject;
        }
      }
    }
    catch (IOException localIOException)
    {
      notifyStateChanged(TuSDKAudioMixerState.Error);
      TLog.e(localIOException);
    }
    byte[] arrayOfByte1 = a(arrayOfByte);
    a(arrayOfByte1);
    Object localObject = ByteBuffer.wrap(arrayOfByte1);
    paramTuSdkAudioRenderCallback.returnRenderBuffer((ByteBuffer)localObject, paramBufferInfo);
    return true;
  }
  
  private void a(byte[] paramArrayOfByte)
  {
    if (this.a) {
      try
      {
        this.c.write(paramArrayOfByte);
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    }
  }
  
  private byte[] a(byte[][] paramArrayOfByte)
  {
    if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
      return null;
    }
    byte[] arrayOfByte = paramArrayOfByte[0];
    if (paramArrayOfByte.length == 1) {
      return arrayOfByte;
    }
    for (int n = 0; n < paramArrayOfByte.length; n++) {
      if (paramArrayOfByte[n].length != arrayOfByte.length)
      {
        TLog.e("column of the road of audio + " + n + " is diffrent.", new Object[0]);
        return null;
      }
    }
    n = paramArrayOfByte.length;
    int i1 = arrayOfByte.length / 2;
    short[][] arrayOfShort = new short[n][i1];
    int i3;
    for (int i2 = 0; i2 < n; i2++) {
      for (i3 = 0; i3 < i1; i3++) {
        arrayOfShort[i2][i3] = ((short)(paramArrayOfByte[i2][(i3 * 2)] & 0xFF | (paramArrayOfByte[i2][(i3 * 2 + 1)] & 0xFF) << 8));
      }
    }
    short[] arrayOfShort1 = new short[i1];
    int i4 = 0;
    for (int i5 = 0; i5 < i1; i5++)
    {
      i3 = 0;
      for (i4 = 0; i4 < n; i4++) {
        i3 += arrayOfShort[i4][i5];
      }
      arrayOfShort1[i5] = ((short)(i3 / n));
    }
    for (i4 = 0; i4 < i1; i4++)
    {
      arrayOfByte[(i4 * 2)] = ((byte)(arrayOfShort1[i4] & 0xFF));
      arrayOfByte[(i4 * 2 + 1)] = ((byte)((arrayOfShort1[i4] & 0xFF00) >> 8));
    }
    return arrayOfByte;
  }
  
  public void notifyStateChanged(TuSDKAudioMixerState paramTuSDKAudioMixerState)
  {
    if (this.k == paramTuSDKAudioMixerState) {
      return;
    }
    this.k = paramTuSDKAudioMixerState;
    if (this.j == null) {
      return;
    }
    this.j.onMixerStateChanged(paramTuSDKAudioMixerState);
  }
  
  public TuSDKAudioMixerState getState()
  {
    return this.k;
  }
  
  public void setTrunkAudioInfo(TuSDKAudioRenderInfoWrap paramTuSDKAudioRenderInfoWrap)
  {
    if (paramTuSDKAudioRenderInfoWrap == null)
    {
      TLog.e("%s set trunk audioInfo is null !!!", new Object[] { "TuSDKAudioMixerRender" });
      return;
    }
    this.g = paramTuSDKAudioRenderInfoWrap;
  }
  
  public static class RawAudioTrack
  {
    private RandomAccessFile c;
    TuSDKAudioRenderEntry a;
    private boolean d = false;
    int b = 0;
    
    public RawAudioTrack(TuSDKAudioRenderEntry paramTuSDKAudioRenderEntry)
    {
      this.a = paramTuSDKAudioRenderEntry;
    }
    
    private RandomAccessFile a()
    {
      if (this.c == null) {
        try
        {
          this.c = new RandomAccessFile(this.a.getRawInfo().getPath(), "r");
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          TLog.e(localFileNotFoundException);
        }
      }
      return this.c;
    }
    
    int a(long paramLong, byte[] paramArrayOfByte)
    {
      TuSdkTimeRange localTuSdkTimeRange = this.a.getTimeRange();
      if ((localTuSdkTimeRange != null) && (!localTuSdkTimeRange.contains(paramLong)) && (!this.a.isLooping()))
      {
        TLog.e("Not contains timeRange : %s", new Object[] { localTuSdkTimeRange });
        return -1;
      }
      if (localTuSdkTimeRange != null)
      {
        if ((localTuSdkTimeRange.contains(paramLong)) && (this.b != -1))
        {
          this.b = a().read(paramArrayOfByte);
        }
        else if ((this.b == -1) && (this.a.isLooping()))
        {
          a().seek(0L);
          this.b = a().read(paramArrayOfByte);
        }
      }
      else
      {
        this.b = a().read(paramArrayOfByte);
        if ((this.b == -1) && (this.a.isLooping()))
        {
          a().seek(0L);
          this.b = a().read(paramArrayOfByte);
        }
      }
      return this.b;
    }
    
    public void seekUs(long paramLong)
    {
      this.b = 0;
      try
      {
        int i;
        if (this.a.getTimeRange() == null)
        {
          i = 0;
          if (paramLong > this.a.getRawInfo().getRealAudioInfo().durationUs) {
            i = this.a.getRawInfo().bytesCountOfTimeUs(a().length());
          } else {
            i = this.a.getRawInfo().bytesCountOfTimeUs(paramLong);
          }
          a().seek(i);
        }
        else if (this.a.getTimeRange().contains(paramLong - this.a.getTimeRange().getStartTimeUS()))
        {
          i = this.a.getRawInfo().bytesCountOfTimeUs(paramLong - this.a.getTimeRange().getStartTimeUS());
          a().seek(i);
        }
        else if (paramLong - this.a.getTimeRange().getStartTimeUS() <= 0L)
        {
          a().seek(0L);
        }
        else if (paramLong - this.a.getTimeRange().getStartTimeUS() > this.a.getTimeRange().getStartTimeUS())
        {
          a().seek(a().length());
        }
      }
      catch (IOException localIOException)
      {
        TLog.e(localIOException);
      }
    }
    
    private void b()
    {
      if (this.c != null) {
        try
        {
          this.c.close();
        }
        catch (IOException localIOException)
        {
          localIOException.printStackTrace();
        }
        finally
        {
          this.c = null;
        }
      }
    }
  }
  
  public static abstract interface TuSdkOnMixerRenderStateListener
  {
    public abstract void onMixerStateChanged(TuSDKAudioMixerRender.TuSDKAudioMixerState paramTuSDKAudioMixerState);
  }
  
  public static enum TuSDKAudioMixerState
  {
    private TuSDKAudioMixerState() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\audio\preproc\mixer\TuSDKAudioMixerRender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */