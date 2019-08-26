package org.lasque.tusdk.core.media.codec.decoder;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput.TuSdkDecodecOutput;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioDecodecOperation;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioDecodecOperationImpl;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.exception.TuSdkTaskExitException;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioDecodecSync;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkAudioFileDecoder
{
  private TuSdkAudioDecodecOperation a;
  private TuSdkMediaExtractor b;
  private TuSdkMediaDataSource c;
  private boolean d = false;
  private TuSdkAudioInfo e;
  private TuSdkDecoderListener f;
  private TuSdkAudioDecodecSync g;
  private TuSdkAudioRender h;
  private TuSdkCodecOutput.TuSdkDecodecOutput i = new TuSdkCodecOutput.TuSdkDecodecOutput()
  {
    public boolean canSupportMediaInfo(int paramAnonymousInt, MediaFormat paramAnonymousMediaFormat)
    {
      int i = TuSdkMediaFormat.checkAudioDecodec(paramAnonymousMediaFormat);
      if (i != 0)
      {
        TLog.w("%s can not support decodec Audio file [error code: %d]: %s - MediaFormat: %s", new Object[] { "TuSdkAudioFileDecoder", Integer.valueOf(i), TuSdkAudioFileDecoder.a(TuSdkAudioFileDecoder.this), paramAnonymousMediaFormat });
        return false;
      }
      TuSdkAudioFileDecoder.a(TuSdkAudioFileDecoder.this, new TuSdkAudioInfo(paramAnonymousMediaFormat));
      if (TuSdkAudioFileDecoder.b(TuSdkAudioFileDecoder.this) != null) {
        TuSdkAudioFileDecoder.b(TuSdkAudioFileDecoder.this).syncAudioDecodecInfo(TuSdkAudioFileDecoder.c(TuSdkAudioFileDecoder.this), TuSdkAudioFileDecoder.d(TuSdkAudioFileDecoder.this));
      }
      return true;
    }
    
    public void outputFormatChanged(MediaFormat paramAnonymousMediaFormat)
    {
      TLog.d("%s outputFormatChanged: %s | %s", new Object[] { "TuSdkAudioFileDecoder", paramAnonymousMediaFormat, TuSdkAudioFileDecoder.c(TuSdkAudioFileDecoder.this) });
    }
    
    public boolean processExtractor(TuSdkMediaExtractor paramAnonymousTuSdkMediaExtractor, TuSdkMediaCodec paramAnonymousTuSdkMediaCodec)
    {
      if (TuSdkAudioFileDecoder.b(TuSdkAudioFileDecoder.this) != null) {
        return TuSdkAudioFileDecoder.b(TuSdkAudioFileDecoder.this).syncAudioDecodecExtractor(paramAnonymousTuSdkMediaExtractor, paramAnonymousTuSdkMediaCodec);
      }
      return TuSdkMediaUtils.putBufferToCoderUntilEnd(paramAnonymousTuSdkMediaExtractor, paramAnonymousTuSdkMediaCodec);
    }
    
    public void processOutputBuffer(TuSdkMediaExtractor paramAnonymousTuSdkMediaExtractor, int paramAnonymousInt, ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkAudioFileDecoder.b(TuSdkAudioFileDecoder.this) != null) {
        TuSdkAudioFileDecoder.b(TuSdkAudioFileDecoder.this).syncAudioDecodecOutputBuffer(paramAnonymousByteBuffer, paramAnonymousBufferInfo, TuSdkAudioFileDecoder.c(TuSdkAudioFileDecoder.this));
      }
    }
    
    public void updated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkAudioFileDecoder.e(TuSdkAudioFileDecoder.this))
      {
        onCatchedException(new TuSdkTaskExitException(String.format("%s stopped", new Object[] { "TuSdkAudioFileDecoder" })));
        return;
      }
      TuSdkAudioFileDecoder.f(TuSdkAudioFileDecoder.this).onDecoderUpdated(paramAnonymousBufferInfo);
      if (TuSdkAudioFileDecoder.b(TuSdkAudioFileDecoder.this) != null) {
        TuSdkAudioFileDecoder.b(TuSdkAudioFileDecoder.this).syncAudioDecodecUpdated(paramAnonymousBufferInfo);
      }
    }
    
    public boolean updatedToEOS(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkAudioFileDecoder.f(TuSdkAudioFileDecoder.this).onDecoderCompleted(null);
      return true;
    }
    
    public void onCatchedException(Exception paramAnonymousException)
    {
      if (TuSdkAudioFileDecoder.b(TuSdkAudioFileDecoder.this) != null) {
        TuSdkAudioFileDecoder.b(TuSdkAudioFileDecoder.this).syncAudioDecodeCrashed(paramAnonymousException);
      }
      TuSdkAudioFileDecoder.f(TuSdkAudioFileDecoder.this).onDecoderCompleted(paramAnonymousException);
    }
  };
  
  public TuSdkAudioInfo getAudioInfo()
  {
    return this.e;
  }
  
  public void setMediaDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    this.c = paramTuSdkMediaDataSource;
  }
  
  public void setListener(TuSdkDecoderListener paramTuSdkDecoderListener)
  {
    if (paramTuSdkDecoderListener == null)
    {
      TLog.w("%s setListener can not empty.", new Object[] { "TuSdkAudioFileDecoder" });
      return;
    }
    if (this.b != null)
    {
      TLog.w("%s setListener need before start.", new Object[] { "TuSdkAudioFileDecoder" });
      return;
    }
    this.f = paramTuSdkDecoderListener;
  }
  
  public void setMediaSync(TuSdkAudioDecodecSync paramTuSdkAudioDecodecSync)
  {
    this.g = paramTuSdkAudioDecodecSync;
  }
  
  public void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    this.h = paramTuSdkAudioRender;
    if (this.a != null) {
      this.a.setAudioRender(paramTuSdkAudioRender);
    }
  }
  
  public void release()
  {
    if (this.d) {
      return;
    }
    this.d = true;
    if (this.b != null)
    {
      this.b.release();
      this.b = null;
    }
    this.a = null;
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public boolean restart()
  {
    release();
    this.d = false;
    return start();
  }
  
  public boolean start()
  {
    if (this.d)
    {
      TLog.w("%s has released.", new Object[] { "TuSdkAudioFileDecoder" });
      return false;
    }
    if (this.b != null)
    {
      TLog.w("%s has been running.", new Object[] { "TuSdkAudioFileDecoder" });
      return false;
    }
    if ((this.c == null) || (!this.c.isValid()))
    {
      TLog.w("%s file path is not exists.", new Object[] { "TuSdkAudioFileDecoder" });
      return false;
    }
    if (this.f == null)
    {
      TLog.w("%s need setListener first.", new Object[] { "TuSdkAudioFileDecoder" });
      return false;
    }
    this.a = new TuSdkAudioDecodecOperationImpl(this.i);
    this.a.setAudioRender(this.h);
    this.b = new TuSdkMediaFileExtractor().setDecodecOperation(this.a).setDataSource(this.c);
    ((TuSdkMediaFileExtractor)this.b).setThreadType(2);
    this.b.play();
    return true;
  }
  
  public boolean isPlaying()
  {
    if (this.b != null) {
      return this.b.isPlaying();
    }
    return false;
  }
  
  public void pause()
  {
    if (this.b == null) {
      return;
    }
    this.b.pause();
  }
  
  public void resume()
  {
    if (this.b == null) {
      return;
    }
    this.b.resume();
  }
  
  public void flush()
  {
    if ((this.a == null) || (this.d)) {
      return;
    }
    this.a.flush();
  }
  
  public void seekTo(long paramLong, int paramInt)
  {
    if (this.b == null) {
      return;
    }
    if ((this.e != null) && (paramLong > this.e.durationUs))
    {
      paramLong = this.e.durationUs;
      paramInt = 2;
    }
    this.b.seekTo(paramLong, paramInt);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\decoder\TuSdkAudioFileDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */