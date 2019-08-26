package org.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
import android.media.AudioRecord;
import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.HardwareHelper;

@TargetApi(16)
public class TuSdkMicRecord
  implements TuSdkAudioRecord, TuSdkAudioResampleSync
{
  private static final Map<String, String> a = new HashMap();
  private AudioRecord b;
  private TuSdkAudioInfo c;
  private int d = 3;
  private int e = 2;
  private int f = 7;
  private int g;
  private int h = 0;
  private ByteBuffer i;
  private TuSdkAudioRecord.TuSdkAudioRecordListener j;
  private int k;
  private TuSdkAudioEffects l;
  private TuSdkAudioResampleHardImpl m;
  
  public TuSdkMicRecord() {}
  
  public TuSdkMicRecord(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    this();
    setAudioInfo(paramTuSdkAudioInfo);
  }
  
  public void setListener(TuSdkAudioRecord.TuSdkAudioRecordListener paramTuSdkAudioRecordListener)
  {
    this.j = paramTuSdkAudioRecordListener;
  }
  
  public void setAudioInfo(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null) {
      return;
    }
    release();
    this.m = new TuSdkAudioResampleHardImpl(paramTuSdkAudioInfo);
    this.m.setMediaSync(this);
    this.c = paramTuSdkAudioInfo;
    TuSdkAudioInfo localTuSdkAudioInfo = paramTuSdkAudioInfo.clone();
    this.e = (this.c.bitWidth == 8 ? 3 : 2);
    if ((this.c.channelCount < 2) || (b()))
    {
      this.g = 16;
      localTuSdkAudioInfo.channelCount = 1;
    }
    else
    {
      this.g = 12;
      localTuSdkAudioInfo.channelCount = 2;
    }
    this.m.changeFormat(localTuSdkAudioInfo);
    this.k = ('Ѐ' * (this.c.channelCount < 2 ? 1 : 2) * (this.c.bitWidth / 8));
    int n = AudioRecord.getMinBufferSize(this.c.sampleRate, this.g, this.e);
    int i1 = n * 4;
    int i2 = paramTuSdkAudioInfo.channelCount * 2;
    this.h = (i1 / i2 * i2);
    if (this.h < 1)
    {
      TLog.w("%s setAudioInfo existence of invalid parameters: %s", new Object[] { "TuSdkMicRecord", this.c });
      if (this.j != null) {
        this.j.onAudioRecordError(2001);
      }
      return;
    }
    this.b = new AudioRecord(this.f, this.c.sampleRate, this.g, this.e, this.h);
    this.i = ByteBuffer.allocateDirect(this.h).order(ByteOrder.nativeOrder());
    if (this.b.getState() != 1)
    {
      TLog.e("%s can not init, please check the recording permission.", new Object[] { "TuSdkMicRecord" });
      if (this.j != null) {
        this.j.onAudioRecordError(2002);
      }
      release();
      return;
    }
    this.l = new TuSdkAudioEffectsImpl(this.b.getAudioSessionId());
    this.l.enableAcousticEchoCanceler();
    this.l.enableNoiseSuppressor();
  }
  
  public void startRecording()
  {
    if ((this.b == null) || (this.b.getState() != 1)) {
      return;
    }
    try
    {
      this.b.startRecording();
      a();
    }
    catch (Exception localException)
    {
      TLog.e(localException, "%s startRecording failed.", new Object[] { "TuSdkMicRecord" });
    }
  }
  
  public void stop()
  {
    if ((this.b == null) || (this.b.getState() != 1)) {
      return;
    }
    try
    {
      this.b.stop();
    }
    catch (Exception localException)
    {
      TLog.e(localException, "%s stop failed.", new Object[] { "TuSdkMicRecord" });
    }
  }
  
  public void release()
  {
    if (this.b == null) {
      return;
    }
    try
    {
      this.b.release();
    }
    catch (Exception localException) {}
    if (this.l != null)
    {
      this.l.release();
      this.l = null;
    }
    this.b = null;
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  private void a()
  {
    final AudioRecord localAudioRecord = this.b;
    final ByteBuffer localByteBuffer = this.i;
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        if ((localAudioRecord == null) || (localByteBuffer == null)) {
          return;
        }
        while (localAudioRecord.getRecordingState() == 3)
        {
          int i = TuSdkMicRecord.a(TuSdkMicRecord.this, localAudioRecord, localByteBuffer);
          TuSdkMicRecord.a(TuSdkMicRecord.this, localByteBuffer, i);
        }
      }
    });
  }
  
  private int a(AudioRecord paramAudioRecord, ByteBuffer paramByteBuffer)
  {
    int n = 0;
    try
    {
      paramByteBuffer.clear();
      n = paramAudioRecord.read(paramByteBuffer, this.k);
    }
    catch (Exception localException)
    {
      TLog.e(localException, "%s read failed.", new Object[] { "TuSdkMicRecord" });
    }
    if (n < 0) {
      TLog.e("%s AudioRecord error: %d, if stop can ignore.", new Object[] { "TuSdkMicRecord", Integer.valueOf(n) });
    }
    return n;
  }
  
  private boolean b()
  {
    boolean bool = false;
    Iterator localIterator = a.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      bool = HardwareHelper.isMatchDeviceModelAndManuFacturer((String)localEntry.getKey(), (String)localEntry.getValue());
      if (bool) {
        break;
      }
    }
    return bool;
  }
  
  private void a(ByteBuffer paramByteBuffer, int paramInt)
  {
    if ((paramInt < 1) || (this.j == null)) {
      return;
    }
    paramByteBuffer.position(0);
    paramByteBuffer.limit(paramInt);
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    localBufferInfo.size = paramInt;
    localBufferInfo.presentationTimeUs = (System.nanoTime() / 1000L);
    this.m.queueInputBuffer(paramByteBuffer, localBufferInfo);
  }
  
  public void syncAudioResampleOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    this.j.onAudioRecordOutputBuffer(paramByteBuffer, paramBufferInfo);
  }
  
  static
  {
    a.put("PAFM00", "OPPO");
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkMicRecord.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */