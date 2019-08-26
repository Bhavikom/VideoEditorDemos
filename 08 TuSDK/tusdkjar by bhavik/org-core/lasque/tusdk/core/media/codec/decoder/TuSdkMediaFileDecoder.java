package org.lasque.tusdk.core.media.codec.decoder;

import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaDecodecSync;
import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public class TuSdkMediaFileDecoder
{
  protected boolean mReleased = false;
  private TuSdkVideoFileSurfaceDecoder a;
  private TuSdkAudioFileDecoder b;
  private TuSdkMediaDecodecSync c;
  private boolean d = false;
  private boolean e = false;
  
  public boolean isVideoStared()
  {
    return this.d;
  }
  
  public boolean isAudioStared()
  {
    return this.e;
  }
  
  public void setMediaSync(TuSdkMediaDecodecSync paramTuSdkMediaDecodecSync)
  {
    if (paramTuSdkMediaDecodecSync == null) {
      return;
    }
    this.c = paramTuSdkMediaDecodecSync;
    if (this.a != null) {
      this.a.setMediaSync(paramTuSdkMediaDecodecSync.buildVideoDecodecSync());
    }
    if (this.b != null) {
      this.b.setMediaSync(paramTuSdkMediaDecodecSync.buildAudioDecodecSync());
    }
  }
  
  public void setMediaDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    if (this.a != null) {
      this.a.setMediaDataSource(paramTuSdkMediaDataSource);
    }
    if (this.b != null) {
      this.b.setMediaDataSource(paramTuSdkMediaDataSource);
    }
  }
  
  public void setSurfaceReceiver(SelesSurfaceReceiver paramSelesSurfaceReceiver)
  {
    if (this.a != null) {
      this.a.setSurfaceHolder(paramSelesSurfaceReceiver);
    }
  }
  
  public void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    if (this.b != null) {
      this.b.setAudioRender(paramTuSdkAudioRender);
    }
  }
  
  public void setListener(TuSdkDecoderListener paramTuSdkDecoderListener1, TuSdkDecoderListener paramTuSdkDecoderListener2)
  {
    if (this.a != null) {
      this.a.setListener(paramTuSdkDecoderListener1);
    }
    if (this.b != null) {
      this.b.setListener(paramTuSdkDecoderListener2);
    }
  }
  
  public TuSdkMediaFileDecoder(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean1) {
      this.a = new TuSdkVideoFileSurfaceDecoder();
    }
    if (paramBoolean2) {
      this.b = new TuSdkAudioFileDecoder();
    }
  }
  
  public void release()
  {
    this.mReleased = true;
    releaseAudioDecoder();
    releaseVideoDecoder();
  }
  
  public void releaseVideoDecoder()
  {
    if (this.a == null) {
      return;
    }
    this.a.release();
    this.a = null;
    if (this.b == null) {
      this.mReleased = true;
    }
  }
  
  public void releaseAudioDecoder()
  {
    if (this.b == null) {
      return;
    }
    this.b.release();
    this.b = null;
    if (this.a == null) {
      this.mReleased = true;
    }
  }
  
  public long seekTo(long paramLong, int paramInt)
  {
    if (this.mReleased) {
      return -1L;
    }
    if (this.a != null)
    {
      this.a.seekTo(paramLong, paramInt);
      paramLong = this.a.getSampleTime();
    }
    if (this.b != null) {
      this.b.seekTo(paramLong, paramInt);
    }
    return paramLong;
  }
  
  public void prepare()
  {
    if ((this.a != null) && (this.a.start())) {
      this.d = true;
    }
    if ((this.b != null) && (this.b.start())) {
      this.e = true;
    }
  }
  
  public void flush()
  {
    if (this.a != null) {
      this.b.flush();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\decoder\TuSdkMediaFileDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */