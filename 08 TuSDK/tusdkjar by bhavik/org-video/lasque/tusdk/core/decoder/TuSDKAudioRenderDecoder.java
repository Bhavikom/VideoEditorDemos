package org.lasque.tusdk.core.decoder;

import android.media.MediaCodec.BufferInfo;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderEntry;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkAudioFileDecoder;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkDecoderListener;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioDecodecSyncBase;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public class TuSDKAudioRenderDecoder
  extends TuSdkAudioDecodecSyncBase
{
  private TuSdkAudioFileDecoder a;
  private TuSdkAudioResample b;
  private TuSDKAudioRenderEntry c;
  private TuSdkAudioInfo d;
  private String e;
  private FileOutputStream f;
  
  public TuSDKAudioRenderDecoder(TuSDKAudioRenderEntry paramTuSDKAudioRenderEntry, TuSdkAudioInfo paramTuSdkAudioInfo, String paramString)
  {
    this.c = paramTuSDKAudioRenderEntry;
    this.d = paramTuSdkAudioInfo;
    this.e = paramString;
    a();
    b();
    c();
  }
  
  private void a()
  {
    this.a = new TuSdkAudioFileDecoder();
    this.a.setMediaDataSource(this.c);
    this.a.setMediaSync(this);
  }
  
  private void b()
  {
    this.b = new TuSdkAudioResampleHardImpl(this.d);
    setAudioResample(this.b);
  }
  
  private void c()
  {
    try
    {
      this.f = new FileOutputStream(this.e);
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      TLog.e(localFileNotFoundException);
    }
  }
  
  public void syncAudioDecodecInfo(TuSdkAudioInfo paramTuSdkAudioInfo, TuSdkMediaExtractor paramTuSdkMediaExtractor)
  {
    super.syncAudioDecodecInfo(paramTuSdkAudioInfo, paramTuSdkMediaExtractor);
    if (this.b != null) {
      this.b.changeFormat(paramTuSdkAudioInfo);
    }
  }
  
  public void syncAudioDecodecOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    super.syncAudioDecodecOutputBuffer(paramByteBuffer, paramBufferInfo, paramTuSdkAudioInfo);
  }
  
  public void syncAudioResampleOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    try
    {
      if (a(paramBufferInfo.presentationTimeUs)) {
        this.f.getChannel().write(paramByteBuffer);
      }
    }
    catch (IOException localIOException)
    {
      TLog.e("%s out put raw file error!", new Object[] { "TuSDKAudioRenderDecoder" });
      TLog.e(localIOException);
    }
    super.syncAudioResampleOutputBuffer(paramByteBuffer, paramBufferInfo);
  }
  
  private boolean a(long paramLong)
  {
    if (this.c.getCutTimeRange() == null) {
      return true;
    }
    return this.c.getCutTimeRange().contains(paramLong);
  }
  
  public void setDecodeListener(TuSdkDecoderListener paramTuSdkDecoderListener)
  {
    if (paramTuSdkDecoderListener == null) {
      return;
    }
    this.a.setListener(paramTuSdkDecoderListener);
  }
  
  public void start()
  {
    this.a.start();
  }
  
  public void release()
  {
    super.release();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKAudioRenderDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */