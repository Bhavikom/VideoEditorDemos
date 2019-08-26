package org.lasque.tusdk.core.audio;

import android.annotation.TargetApi;
import android.media.AudioRecord;
import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioEngine.TuSdKAudioEngineOutputBufferDelegate;
import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine;
import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine.TuSdkAudioEnginePitchTypeChangeDelegate;
import org.lasque.tusdk.api.audio.preproc.processor.TuSdkAudioPitchEngine.TuSdkSoundPitchType;
import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoder;
import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;
import org.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.utils.TLog;

public class TuSDKAudioRecorderCore
  implements TuSDKAudioRecorderInterface
{
  private Object a = new Object();
  private AudioRecordThread b;
  private AudioRecord c;
  private TuSDKAudioEffects d = null;
  private byte[] e;
  private boolean f = true;
  protected TuSDKAudioDataEncoderInterface mAudioEncoder;
  private TuSdkAudioRecorderDelegate g;
  private TuSDKAudioCaptureSetting h;
  private TuSDKAudioEncoderSetting i;
  private long j = 0L;
  private TuSdkAudioPitchEngine k;
  private TuSdkAudioEngine.TuSdKAudioEngineOutputBufferDelegate l = new TuSdkAudioEngine.TuSdKAudioEngineOutputBufferDelegate()
  {
    public void onProcess(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSDKAudioRecorderCore.this.onRawAudioFrameDataAvailable(paramAnonymousByteBuffer.array());
    }
  };
  
  public TuSDKAudioRecorderCore()
  {
    this(TuSDKAudioCaptureSetting.defaultCaptureSetting(), TuSDKAudioEncoderSetting.defaultEncoderSetting());
  }
  
  public TuSDKAudioRecorderCore(TuSDKAudioCaptureSetting paramTuSDKAudioCaptureSetting, TuSDKAudioEncoderSetting paramTuSDKAudioEncoderSetting)
  {
    a(paramTuSDKAudioCaptureSetting == null ? TuSDKAudioCaptureSetting.defaultCaptureSetting() : paramTuSDKAudioCaptureSetting);
    a(paramTuSDKAudioEncoderSetting == null ? TuSDKAudioEncoderSetting.defaultEncoderSetting() : paramTuSDKAudioEncoderSetting);
    prepare();
    TuSdkAudioInfo localTuSdkAudioInfo = new TuSdkAudioInfo();
    localTuSdkAudioInfo.bitWidth = (getCaptureSetting().audioRecoderFormat == 2 ? 16 : 8);
    localTuSdkAudioInfo.channelCount = (getCaptureSetting().audioRecoderChannelConfig == 12 ? 2 : 1);
    localTuSdkAudioInfo.sampleRate = getCaptureSetting().audioRecoderSampleRate;
    this.k = new TuSdkAudioPitchEngine(localTuSdkAudioInfo, false);
    this.k.setOutputBufferDelegate(this.l);
  }
  
  public void setInputAudioInfo(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    this.k.changeAudioInfo(paramTuSdkAudioInfo);
  }
  
  public void setSoundType(TuSdkAudioPitchEngine.TuSdkSoundPitchType paramTuSdkSoundPitchType)
  {
    if (paramTuSdkSoundPitchType == null) {
      return;
    }
    this.k.setSoundPitchType(paramTuSdkSoundPitchType);
  }
  
  public void setSoundTypeChangeListener(TuSdkAudioPitchEngine.TuSdkAudioEnginePitchTypeChangeDelegate paramTuSdkAudioEnginePitchTypeChangeDelegate)
  {
    this.k.setSoundTypeChangeListener(paramTuSdkAudioEnginePitchTypeChangeDelegate);
  }
  
  protected boolean prepare()
  {
    if ((isEnableAudioEncoder()) && (getAudioEncoder() != null) && (!this.mAudioEncoder.initEncoder(getEncoderSetting()))) {
      return false;
    }
    return b(getCaptureSetting());
  }
  
  public boolean isPrepared()
  {
    if (this.c == null) {
      return false;
    }
    if (1 != this.c.getState())
    {
      TLog.e("TuSDKAudioRecorderCore | Please check the recording permission", new Object[0]);
      return false;
    }
    if (0 != this.c.setPositionNotificationPeriod(this.h.audioRecoderSliceSize))
    {
      TLog.e("AudioRecord.SUCCESS != audioRecord.setPositionNotificationPeriod(" + this.h.audioRecoderSliceSize + ")", new Object[0]);
      return false;
    }
    return true;
  }
  
  public TuSDKAudioCaptureSetting getCaptureSetting()
  {
    if (this.h == null) {
      this.h = new TuSDKAudioCaptureSetting();
    }
    return this.h;
  }
  
  private void a(TuSDKAudioCaptureSetting paramTuSDKAudioCaptureSetting)
  {
    this.h = paramTuSDKAudioCaptureSetting;
  }
  
  public TuSDKAudioEncoderSetting getEncoderSetting()
  {
    if (this.i == null) {
      this.i = new TuSDKAudioEncoderSetting();
    }
    return this.i;
  }
  
  private void a(TuSDKAudioEncoderSetting paramTuSDKAudioEncoderSetting)
  {
    this.i = paramTuSDKAudioEncoderSetting;
  }
  
  public void setEnableAudioEncoder(boolean paramBoolean)
  {
    this.f = paramBoolean;
  }
  
  public boolean isEnableAudioEncoder()
  {
    return this.f;
  }
  
  private boolean b(TuSDKAudioCaptureSetting paramTuSDKAudioCaptureSetting)
  {
    synchronized (this.a)
    {
      if (this.c == null)
      {
        int m = AudioRecord.getMinBufferSize(paramTuSDKAudioCaptureSetting.audioRecoderSampleRate, paramTuSDKAudioCaptureSetting.audioRecoderChannelConfig, paramTuSDKAudioCaptureSetting.audioRecoderFormat);
        this.c = new AudioRecord(paramTuSDKAudioCaptureSetting.audioRecoderSource, paramTuSDKAudioCaptureSetting.audioRecoderSampleRate, paramTuSDKAudioCaptureSetting.audioRecoderChannelConfig, paramTuSDKAudioCaptureSetting.audioRecoderFormat, m * 4);
        this.e = new byte[paramTuSDKAudioCaptureSetting.audioRecoderBufferSize];
      }
      return isPrepared();
    }
  }
  
  @TargetApi(16)
  private int a()
  {
    if ((this.c == null) || (getCaptureSetting().audioRecoderSource != 7)) {
      return -1;
    }
    return this.c.getAudioSessionId();
  }
  
  private void b()
  {
    if (getCaptureSetting().audioRecoderSource != 7) {
      return;
    }
    if (((!getCaptureSetting().shouldEnableAec) && (!getCaptureSetting().shouldEnableNs)) || (a() == -1)) {
      return;
    }
    if (this.d == null) {
      this.d = TuSDKAudioEffects.a();
    }
    if (this.d == null) {
      return;
    }
    this.d.setAEC(getCaptureSetting().shouldEnableAec);
    this.d.setNS(getCaptureSetting().shouldEnableNs);
    this.d.enable(a());
  }
  
  public void startRecording()
  {
    synchronized (this.a)
    {
      if ((!isPrepared()) || (isRecording())) {
        return;
      }
      try
      {
        if (this.c != null)
        {
          this.j = (System.nanoTime() / 1000L);
          this.c.startRecording();
          if (!isRecording())
          {
            this.c = null;
            TLog.e("TuSDKAudioRecorderCore | Please check the recording permission", new Object[0]);
            return;
          }
          b();
        }
        if ((isEnableAudioEncoder()) && (this.mAudioEncoder != null)) {
          this.mAudioEncoder.start();
        }
        if ((this.g != null) && (3 == this.c.getRecordingState())) {
          this.g.onAudioStarted();
        }
        this.b = new AudioRecordThread();
        this.b.start();
      }
      catch (Error localError)
      {
        TLog.e("%s | " + localError, new Object[0]);
      }
    }
  }
  
  public void stopRecording()
  {
    synchronized (this.a)
    {
      if (!isPrepared()) {
        return;
      }
      if ((this.c != null) && (1 == this.c.getState())) {
        this.c.stop();
      }
      if (this.mAudioEncoder != null) {
        this.mAudioEncoder.stop();
      }
      if (this.b != null) {
        this.b.quit();
      }
      if (this.d != null)
      {
        this.d.release();
        this.d = null;
      }
    }
  }
  
  public void mute(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      stopRecording();
    }
    else
    {
      if (isRecording()) {
        return;
      }
      startRecording();
    }
  }
  
  public boolean isRecording()
  {
    synchronized (this.a)
    {
      if (this.c == null) {
        return false;
      }
      return this.c.getRecordingState() == 3;
    }
  }
  
  private void a(byte[] paramArrayOfByte)
  {
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    localBufferInfo.size = paramArrayOfByte.length;
    localBufferInfo.presentationTimeUs = (System.nanoTime() / 1000L - this.j);
    this.k.processInputBuffer(ByteBuffer.wrap(paramArrayOfByte), localBufferInfo);
  }
  
  protected void onRawAudioFrameDataAvailable(byte[] paramArrayOfByte)
  {
    if ((isEnableAudioEncoder()) && (this.mAudioEncoder != null)) {
      this.mAudioEncoder.queueAudio(paramArrayOfByte);
    }
    if (getDelegate() != null) {
      getDelegate().onRawAudioFrameDataAvailable(paramArrayOfByte);
    }
  }
  
  public TuSdkAudioRecorderDelegate getDelegate()
  {
    return this.g;
  }
  
  public void setDelegate(TuSdkAudioRecorderDelegate paramTuSdkAudioRecorderDelegate)
  {
    this.g = paramTuSdkAudioRecorderDelegate;
  }
  
  public TuSDKAudioDataEncoderInterface getAudioEncoder()
  {
    if (this.mAudioEncoder == null) {
      this.mAudioEncoder = new TuSDKAudioDataEncoder();
    }
    return this.mAudioEncoder;
  }
  
  public void setAudioEncoder(TuSDKAudioDataEncoderInterface paramTuSDKAudioDataEncoderInterface)
  {
    this.mAudioEncoder = paramTuSDKAudioDataEncoderInterface;
  }
  
  protected class AudioRecordThread
    extends Thread
  {
    private boolean b = true;
    
    public AudioRecordThread() {}
    
    public void quit()
    {
      this.b = false;
    }
    
    public void run()
    {
      while ((this.b) && (!Thread.interrupted()))
      {
        int i = TuSDKAudioRecorderCore.b(TuSDKAudioRecorderCore.this).read(TuSDKAudioRecorderCore.a(TuSDKAudioRecorderCore.this), 0, TuSDKAudioRecorderCore.a(TuSDKAudioRecorderCore.this).length);
        if ((this.b) && (i > 0)) {
          TuSDKAudioRecorderCore.a(TuSDKAudioRecorderCore.this, TuSDKAudioRecorderCore.a(TuSDKAudioRecorderCore.this));
        }
      }
    }
  }
  
  public static abstract interface TuSdkAudioRecorderDelegate
  {
    public abstract void onRawAudioFrameDataAvailable(byte[] paramArrayOfByte);
    
    public abstract void onAudioStarted();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\audio\TuSDKAudioRecorderCore.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */