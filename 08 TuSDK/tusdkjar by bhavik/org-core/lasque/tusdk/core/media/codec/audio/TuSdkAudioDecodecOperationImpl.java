package org.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender.TuSdkAudioRenderCallback;
import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput.TuSdkDecodecOutput;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class TuSdkAudioDecodecOperationImpl
  implements TuSdkAudioDecodecOperation
{
  private int a = -1;
  private TuSdkMediaCodec b;
  private boolean c;
  private ByteBuffer[] d;
  private final long e = 10000L;
  private TuSdkCodecOutput.TuSdkDecodecOutput f;
  private TuSdkMediaExtractor g;
  private MediaFormat h;
  private TuSdkAudioInfo i;
  private TuSdkAudioRender j;
  private boolean k = false;
  private TuSdkAudioRender.TuSdkAudioRenderCallback l = new TuSdkAudioRender.TuSdkAudioRenderCallback()
  {
    public boolean isEncodec()
    {
      return false;
    }
    
    public TuSdkAudioInfo getAudioInfo()
    {
      return TuSdkAudioDecodecOperationImpl.a(TuSdkAudioDecodecOperationImpl.this);
    }
    
    public void returnRenderBuffer(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkAudioDecodecOperationImpl.a(TuSdkAudioDecodecOperationImpl.this, paramAnonymousByteBuffer, paramAnonymousBufferInfo);
    }
  };
  
  public TuSdkAudioInfo getAudioInfo()
  {
    return this.i;
  }
  
  public void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    this.j = paramTuSdkAudioRender;
  }
  
  public TuSdkAudioDecodecOperationImpl(TuSdkCodecOutput.TuSdkDecodecOutput paramTuSdkDecodecOutput)
  {
    if (paramTuSdkDecodecOutput == null) {
      throw new NullPointerException(String.format("%s init failed, codecOutput is NULL", new Object[] { "TuSdkAudioDecodecOperationImpl" }));
    }
    this.f = paramTuSdkDecodecOutput;
  }
  
  public void flush()
  {
    if ((this.b == null) || (this.k)) {
      return;
    }
    this.b.flush();
  }
  
  public boolean decodecInit(TuSdkMediaExtractor paramTuSdkMediaExtractor)
  {
    this.a = TuSdkMediaUtils.getMediaTrackIndex(paramTuSdkMediaExtractor, "audio/");
    if (this.a < 0)
    {
      decodecException(new TuSdkNoMediaTrackException(String.format("%s decodecInit can not find media track: %s", new Object[] { "TuSdkAudioDecodecOperationImpl", "audio/" })));
      TLog.e("%s Audio decodecInit mTrackIndex reulst false", new Object[] { "TuSdkAudioDecodecOperationImpl" });
      return false;
    }
    this.h = paramTuSdkMediaExtractor.getTrackFormat(this.a);
    paramTuSdkMediaExtractor.selectTrack(this.a);
    if (!this.f.canSupportMediaInfo(this.a, this.h))
    {
      TLog.e("%s decodecInit can not Support MediaInfo: %s", new Object[] { "TuSdkAudioDecodecOperationImpl", this.h });
      return false;
    }
    this.i = new TuSdkAudioInfo(this.h);
    this.b = TuSdkMediaCodecImpl.createDecoderByType(this.h.getString("mime"));
    if ((this.b.configureError() != null) || (!this.b.configure(this.h, null, null, 0)))
    {
      decodecException(this.b.configureError());
      this.b = null;
      TLog.e("%s Audio decodecInit mMediaCodec reulst false", new Object[] { "TuSdkAudioDecodecOperationImpl" });
      return false;
    }
    this.g = paramTuSdkMediaExtractor;
    this.b.start();
    this.d = this.b.getOutputBuffers();
    this.c = false;
    return true;
  }
  
  public boolean decodecProcessUntilEnd(TuSdkMediaExtractor paramTuSdkMediaExtractor)
  {
    TuSdkMediaCodec localTuSdkMediaCodec = this.b;
    if (localTuSdkMediaCodec == null) {
      return true;
    }
    if (!this.c) {
      this.c = this.f.processExtractor(paramTuSdkMediaExtractor, localTuSdkMediaCodec);
    }
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    int m = localTuSdkMediaCodec.dequeueOutputBuffer(localBufferInfo, 10000L);
    switch (m)
    {
    case -2: 
      this.f.outputFormatChanged(localTuSdkMediaCodec.getOutputFormat());
      break;
    case -1: 
      break;
    case -3: 
      this.d = localTuSdkMediaCodec.getOutputBuffers();
      break;
    default: 
      if (m >= 0)
      {
        if (localBufferInfo.size > 0)
        {
          ByteBuffer localByteBuffer = this.d[m];
          if ((this.j == null) || (!this.j.onAudioSliceRender(localByteBuffer, localBufferInfo, this.l))) {
            this.f.processOutputBuffer(paramTuSdkMediaExtractor, this.a, localByteBuffer, localBufferInfo);
          }
        }
        if (!this.k) {
          localTuSdkMediaCodec.releaseOutputBuffer(m, false);
        }
        this.f.updated(localBufferInfo);
      }
      break;
    }
    if ((localBufferInfo.flags & 0x4) != 0)
    {
      TLog.d("%s process Audio Buffer Stream end", new Object[] { "TuSdkAudioDecodecOperationImpl" });
      return this.f.updatedToEOS(localBufferInfo);
    }
    return false;
  }
  
  private void a(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if ((this.k) || (this.f == null)) {
      return;
    }
    this.f.processOutputBuffer(this.g, this.a, paramByteBuffer, paramBufferInfo);
  }
  
  public void decodecRelease()
  {
    this.k = true;
    if (this.b == null) {
      return;
    }
    this.b.stop();
    this.b.release();
    this.b = null;
  }
  
  protected void finalize()
  {
    decodecRelease();
    super.finalize();
  }
  
  public void decodecException(Exception paramException)
  {
    this.f.onCatchedException(paramException);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioDecodecOperationImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */