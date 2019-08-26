package org.lasque.tusdk.core.media.codec.decoder;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.view.Surface;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput.TuSdkDecodecVideoSurfaceOutput;
import org.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.exception.TuSdkTaskExitException;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.media.codec.sync.TuSdkVideoDecodecSync;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoSurfaceDecodecOperation;
import org.lasque.tusdk.core.seles.sources.SelesSurfaceHolder;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class TuSdkVideoFileSurfaceDecoder
{
  private TuSdkDecodecOperation a;
  private TuSdkMediaExtractor b;
  private TuSdkMediaDataSource c;
  private boolean d = false;
  private TuSdkVideoInfo e;
  private SelesSurfaceHolder f;
  private TuSdkDecoderListener g;
  private TuSdkVideoDecodecSync h;
  private TuSdkCodecOutput.TuSdkDecodecVideoSurfaceOutput i = new TuSdkCodecOutput.TuSdkDecodecVideoSurfaceOutput()
  {
    public boolean canSupportMediaInfo(int paramAnonymousInt, MediaFormat paramAnonymousMediaFormat)
    {
      int i = TuSdkMediaFormat.checkVideoDecodec(paramAnonymousMediaFormat);
      if (i != 0)
      {
        TLog.w("%s can not support decodec Video file [error code: 0x%x]: %s - MediaFormat: %s", new Object[] { "TuSdkVideoFileSurfaceDecoder", Integer.valueOf(i), TuSdkVideoFileSurfaceDecoder.a(TuSdkVideoFileSurfaceDecoder.this), paramAnonymousMediaFormat });
        return false;
      }
      TuSdkVideoFileSurfaceDecoder.b(TuSdkVideoFileSurfaceDecoder.this).setMediaFormat(paramAnonymousMediaFormat);
      if (TuSdkVideoFileSurfaceDecoder.c(TuSdkVideoFileSurfaceDecoder.this) != null) {
        TuSdkVideoFileSurfaceDecoder.c(TuSdkVideoFileSurfaceDecoder.this).syncVideoDecodecInfo(TuSdkVideoFileSurfaceDecoder.b(TuSdkVideoFileSurfaceDecoder.this), TuSdkVideoFileSurfaceDecoder.d(TuSdkVideoFileSurfaceDecoder.this));
      }
      return true;
    }
    
    public Surface requestSurface()
    {
      if ((TuSdkVideoFileSurfaceDecoder.e(TuSdkVideoFileSurfaceDecoder.this) == null) || (!TuSdkVideoFileSurfaceDecoder.e(TuSdkVideoFileSurfaceDecoder.this).isInited())) {
        return null;
      }
      SurfaceTexture localSurfaceTexture = TuSdkVideoFileSurfaceDecoder.e(TuSdkVideoFileSurfaceDecoder.this).requestSurfaceTexture();
      if (localSurfaceTexture == null) {
        return null;
      }
      TuSdkVideoFileSurfaceDecoder.e(TuSdkVideoFileSurfaceDecoder.this).setInputSize(TuSdkVideoFileSurfaceDecoder.b(TuSdkVideoFileSurfaceDecoder.this).size);
      TuSdkVideoFileSurfaceDecoder.e(TuSdkVideoFileSurfaceDecoder.this).setInputRotation(TuSdkVideoFileSurfaceDecoder.b(TuSdkVideoFileSurfaceDecoder.this).orientation);
      Surface localSurface = new Surface(localSurfaceTexture);
      return localSurface;
    }
    
    public void outputFormatChanged(MediaFormat paramAnonymousMediaFormat)
    {
      if (TuSdkVideoFileSurfaceDecoder.b(TuSdkVideoFileSurfaceDecoder.this) == null) {
        return;
      }
      TuSdkVideoFileSurfaceDecoder.b(TuSdkVideoFileSurfaceDecoder.this).setCorp(paramAnonymousMediaFormat);
      TLog.d("%s outputFormatChanged: %s | %s", new Object[] { "TuSdkVideoFileSurfaceDecoder", paramAnonymousMediaFormat, TuSdkVideoFileSurfaceDecoder.b(TuSdkVideoFileSurfaceDecoder.this) });
      if (TuSdkVideoFileSurfaceDecoder.e(TuSdkVideoFileSurfaceDecoder.this) == null) {
        return;
      }
      TuSdkVideoFileSurfaceDecoder.e(TuSdkVideoFileSurfaceDecoder.this).setInputSize(TuSdkVideoFileSurfaceDecoder.b(TuSdkVideoFileSurfaceDecoder.this).codecSize);
      TuSdkVideoFileSurfaceDecoder.e(TuSdkVideoFileSurfaceDecoder.this).setPreCropRect(TuSdkVideoFileSurfaceDecoder.b(TuSdkVideoFileSurfaceDecoder.this).codecCrop);
    }
    
    public boolean processExtractor(TuSdkMediaExtractor paramAnonymousTuSdkMediaExtractor, TuSdkMediaCodec paramAnonymousTuSdkMediaCodec)
    {
      if (TuSdkVideoFileSurfaceDecoder.c(TuSdkVideoFileSurfaceDecoder.this) != null) {
        return TuSdkVideoFileSurfaceDecoder.c(TuSdkVideoFileSurfaceDecoder.this).syncVideoDecodecExtractor(paramAnonymousTuSdkMediaExtractor, paramAnonymousTuSdkMediaCodec);
      }
      return TuSdkMediaUtils.putBufferToCoderUntilEnd(paramAnonymousTuSdkMediaExtractor, paramAnonymousTuSdkMediaCodec);
    }
    
    public void processOutputBuffer(TuSdkMediaExtractor paramAnonymousTuSdkMediaExtractor, int paramAnonymousInt, ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkVideoFileSurfaceDecoder.c(TuSdkVideoFileSurfaceDecoder.this) != null) {
        TuSdkVideoFileSurfaceDecoder.c(TuSdkVideoFileSurfaceDecoder.this).syncVideoDecodecOutputBuffer(paramAnonymousByteBuffer, paramAnonymousBufferInfo, TuSdkVideoFileSurfaceDecoder.b(TuSdkVideoFileSurfaceDecoder.this));
      }
    }
    
    public void updated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkVideoFileSurfaceDecoder.f(TuSdkVideoFileSurfaceDecoder.this))
      {
        onCatchedException(new TuSdkTaskExitException(String.format("%s stopped", new Object[] { "TuSdkVideoFileSurfaceDecoder" })));
        return;
      }
      if (TuSdkVideoFileSurfaceDecoder.e(TuSdkVideoFileSurfaceDecoder.this) != null)
      {
        long l = TuSdkVideoFileSurfaceDecoder.c(TuSdkVideoFileSurfaceDecoder.this).calcEffectFrameTimeUs(paramAnonymousBufferInfo.presentationTimeUs);
        TuSdkVideoFileSurfaceDecoder.e(TuSdkVideoFileSurfaceDecoder.this).setSurfaceTexTimestampNs(l * 1000L);
      }
      TuSdkVideoFileSurfaceDecoder.g(TuSdkVideoFileSurfaceDecoder.this).onDecoderUpdated(paramAnonymousBufferInfo);
      if (TuSdkVideoFileSurfaceDecoder.c(TuSdkVideoFileSurfaceDecoder.this) != null) {
        TuSdkVideoFileSurfaceDecoder.c(TuSdkVideoFileSurfaceDecoder.this).syncVideoDecodecUpdated(paramAnonymousBufferInfo);
      }
    }
    
    public boolean updatedToEOS(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkVideoFileSurfaceDecoder.g(TuSdkVideoFileSurfaceDecoder.this).onDecoderCompleted(null);
      return true;
    }
    
    public void onCatchedException(Exception paramAnonymousException)
    {
      if (TuSdkVideoFileSurfaceDecoder.c(TuSdkVideoFileSurfaceDecoder.this) != null) {
        TuSdkVideoFileSurfaceDecoder.c(TuSdkVideoFileSurfaceDecoder.this).syncVideoDecodeCrashed(paramAnonymousException);
      }
      TuSdkVideoFileSurfaceDecoder.g(TuSdkVideoFileSurfaceDecoder.this).onDecoderCompleted(paramAnonymousException);
    }
  };
  
  public TuSdkVideoInfo getVideoInfo()
  {
    return this.e;
  }
  
  public void setMediaDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    this.c = paramTuSdkMediaDataSource;
  }
  
  public void setSurfaceHolder(SelesSurfaceHolder paramSelesSurfaceHolder)
  {
    if (paramSelesSurfaceHolder == null)
    {
      TLog.w("%s setSurfaceHolder can not empty.", new Object[] { "TuSdkVideoFileSurfaceDecoder" });
      return;
    }
    if (this.b != null)
    {
      TLog.w("%s setSurfaceHolder need before start.", new Object[] { "TuSdkVideoFileSurfaceDecoder" });
      return;
    }
    this.f = paramSelesSurfaceHolder;
  }
  
  public void setListener(TuSdkDecoderListener paramTuSdkDecoderListener)
  {
    if (paramTuSdkDecoderListener == null)
    {
      TLog.w("%s setListener can not empty.", new Object[] { "TuSdkVideoFileSurfaceDecoder" });
      return;
    }
    if (this.b != null)
    {
      TLog.w("%s setListener need before start.", new Object[] { "TuSdkVideoFileSurfaceDecoder" });
      return;
    }
    this.g = paramTuSdkDecoderListener;
  }
  
  public void setMediaSync(TuSdkVideoDecodecSync paramTuSdkVideoDecodecSync)
  {
    this.h = paramTuSdkVideoDecodecSync;
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
      TLog.w("%s has released.", new Object[] { "TuSdkVideoFileSurfaceDecoder" });
      return false;
    }
    if (this.b != null)
    {
      TLog.w("%s has been running.", new Object[] { "TuSdkVideoFileSurfaceDecoder" });
      return false;
    }
    if ((this.c == null) || (!this.c.isValid()))
    {
      TLog.w("%s file path is not exists.", new Object[] { "TuSdkVideoFileSurfaceDecoder" });
      return false;
    }
    if (this.g == null)
    {
      TLog.w("%s need setListener first.", new Object[] { "TuSdkVideoFileSurfaceDecoder" });
      return false;
    }
    if (this.f == null)
    {
      TLog.w("%s need setSurfaceHolder first.", new Object[] { "TuSdkVideoFileSurfaceDecoder" });
      return false;
    }
    this.a = new TuSdkVideoSurfaceDecodecOperation(this.i);
    this.b = new TuSdkMediaFileExtractor().setDecodecOperation(this.a).setDataSource(this.c);
    ((TuSdkMediaFileExtractor)this.b).setThreadType(1);
    MediaMetadataRetriever localMediaMetadataRetriever = this.c.getMediaMetadataRetriever();
    this.e = new TuSdkVideoInfo();
    this.e.size = TuSdkMediaFormat.getVideoKeySize(localMediaMetadataRetriever);
    this.e.orientation = TuSdkMediaFormat.getVideoRotation(localMediaMetadataRetriever);
    localMediaMetadataRetriever.release();
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
  
  public long getSampleTime()
  {
    if (this.b == null) {
      return -1L;
    }
    return this.b.getSampleTime();
  }
  
  public boolean advance()
  {
    if (this.b == null) {
      return false;
    }
    return this.b.advance();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\decoder\TuSdkVideoFileSurfaceDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */