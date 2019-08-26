package org.lasque.tusdk.core.media.codec.sync;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

public abstract class TuSdkAudioEncodecSyncBase
  implements TuSdkAudioEncodecSync
{
  protected boolean mReleased = false;
  protected TuSdkAudioInfo mAudioInfo;
  private TuSdkAudioResample a;
  private boolean b = false;
  private long c = 0L;
  
  public TuSdkAudioResample getAudioResample()
  {
    return this.a;
  }
  
  public boolean isAudioEncodeCompleted()
  {
    return this.b;
  }
  
  public void release()
  {
    this.mReleased = true;
    if (this.a != null)
    {
      this.a.release();
      this.a = null;
    }
  }
  
  private long a(long paramLong)
  {
    if (this.mAudioInfo == null) {
      return 0L;
    }
    long l = paramLong * 1024000000L / this.mAudioInfo.sampleRate;
    return l;
  }
  
  public long lastStandardPtsUs()
  {
    return a(this.c);
  }
  
  public long nextStandardPtsUs()
  {
    return a(this.c + 1L);
  }
  
  protected long getAndAddCountPtsUs()
  {
    long l = lastStandardPtsUs();
    this.c += 1L;
    return l;
  }
  
  protected long getAndAddCountPtsUs(long paramLong)
  {
    for (long l = -1L; nextStandardPtsUs() < paramLong; l = getAndAddCountPtsUs()) {}
    return l;
  }
  
  protected boolean isInterrupted()
  {
    return (ThreadHelper.isInterrupted()) || (this.mReleased);
  }
  
  public void syncAudioEncodecInfo(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    this.mAudioInfo = paramTuSdkAudioInfo;
    if ((this.a == null) && (paramTuSdkAudioInfo != null)) {
      this.a = new TuSdkAudioResampleHardImpl(paramTuSdkAudioInfo);
    }
  }
  
  public void syncAudioEncodecOutputBuffer(TuSdkMediaMuxer paramTuSdkMediaMuxer, int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    TuSdkMediaUtils.processOutputBuffer(paramTuSdkMediaMuxer, paramInt, paramByteBuffer, paramBufferInfo);
  }
  
  public void syncAudioEncodecUpdated(MediaCodec.BufferInfo paramBufferInfo)
  {
    if (TLog.LOG_AUDIO_ENCODEC_INFO) {
      TuSdkCodecCapabilities.logBufferInfo(String.format("%s syncAudioEncodecUpdated", new Object[] { "TuSdkAudioEncodecSyncBase" }), paramBufferInfo);
    }
  }
  
  public void syncAudioEncodecCompleted()
  {
    if (TLog.LOG_AUDIO_ENCODEC_INFO) {
      TLog.d("%s syncAudioEncodecCompleted", new Object[] { "TuSdkAudioEncodecSyncBase" });
    }
    this.b = true;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkAudioEncodecSyncBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */