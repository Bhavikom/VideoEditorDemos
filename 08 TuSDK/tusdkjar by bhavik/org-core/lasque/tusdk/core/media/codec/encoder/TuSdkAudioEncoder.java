package org.lasque.tusdk.core.media.codec.encoder;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput.TuSdkEncodecOutput;
import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperationImpl;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSync;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkAudioEncoder
{
  private int a = -1;
  private TuSdkAudioEncodecOperation b;
  private TuSdkEncoderListener c;
  private TuSdkAudioEncodecSync d;
  private TuSdkAudioRender e;
  private TuSdkCodecOutput.TuSdkEncodecOutput f = new TuSdkCodecOutput.TuSdkEncodecOutput()
  {
    public void outputFormatChanged(MediaFormat paramAnonymousMediaFormat)
    {
      TLog.d("%s encodec Audio outputFormatChanged: %s", new Object[] { "TuSdkAudioEncoder", paramAnonymousMediaFormat });
      if ((TuSdkAudioEncoder.a(TuSdkAudioEncoder.this) != null) && (TuSdkAudioEncoder.b(TuSdkAudioEncoder.this) != null)) {
        TuSdkAudioEncoder.a(TuSdkAudioEncoder.this).syncAudioEncodecInfo(TuSdkAudioEncoder.b(TuSdkAudioEncoder.this).getAudioInfo());
      }
    }
    
    public void processOutputBuffer(TuSdkMediaMuxer paramAnonymousTuSdkMediaMuxer, int paramAnonymousInt, ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkAudioEncoder.a(TuSdkAudioEncoder.this) != null) {
        TuSdkAudioEncoder.a(TuSdkAudioEncoder.this).syncAudioEncodecOutputBuffer(paramAnonymousTuSdkMediaMuxer, paramAnonymousInt, paramAnonymousByteBuffer, paramAnonymousBufferInfo);
      } else {
        TuSdkMediaUtils.processOutputBuffer(paramAnonymousTuSdkMediaMuxer, paramAnonymousInt, paramAnonymousByteBuffer, paramAnonymousBufferInfo);
      }
    }
    
    public void updated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkAudioEncoder.a(TuSdkAudioEncoder.this) != null) {
        TuSdkAudioEncoder.a(TuSdkAudioEncoder.this).syncAudioEncodecUpdated(paramAnonymousBufferInfo);
      }
      TuSdkAudioEncoder.c(TuSdkAudioEncoder.this).onEncoderUpdated(paramAnonymousBufferInfo);
    }
    
    public boolean updatedToEOS(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkAudioEncoder.a(TuSdkAudioEncoder.this) != null) {
        TuSdkAudioEncoder.a(TuSdkAudioEncoder.this).syncAudioEncodecCompleted();
      }
      TuSdkAudioEncoder.c(TuSdkAudioEncoder.this).onEncoderCompleted(null);
      return true;
    }
    
    public void onCatchedException(Exception paramAnonymousException)
    {
      if (TuSdkAudioEncoder.a(TuSdkAudioEncoder.this) != null) {
        TuSdkAudioEncoder.a(TuSdkAudioEncoder.this).syncAudioEncodecCompleted();
      }
      TuSdkAudioEncoder.c(TuSdkAudioEncoder.this).onEncoderCompleted(paramAnonymousException);
    }
  };
  
  public void setListener(TuSdkEncoderListener paramTuSdkEncoderListener)
  {
    if (paramTuSdkEncoderListener == null)
    {
      TLog.w("%s setListener can not empty.", new Object[] { "TuSdkAudioEncoder" });
      return;
    }
    if (this.a != -1)
    {
      TLog.w("%s setListener need before prepare.", new Object[] { "TuSdkAudioEncoder" });
      return;
    }
    this.c = paramTuSdkEncoderListener;
  }
  
  public int setOutputFormat(MediaFormat paramMediaFormat)
  {
    if (this.a != -1)
    {
      TLog.w("%s setOutputFormat need before prepare.", new Object[] { "TuSdkAudioEncoder" });
      return -1;
    }
    TuSdkAudioEncodecOperationImpl localTuSdkAudioEncodecOperationImpl = new TuSdkAudioEncodecOperationImpl(this.f);
    int i = localTuSdkAudioEncodecOperationImpl.setMediaFormat(paramMediaFormat);
    if (i != 0)
    {
      TLog.w("%s setOutputFormat has a error code: %d, %s", new Object[] { "TuSdkAudioEncoder", Integer.valueOf(i), paramMediaFormat });
      return i;
    }
    this.b = localTuSdkAudioEncodecOperationImpl;
    this.b.setAudioRender(this.e);
    return 0;
  }
  
  public TuSdkAudioInfo getAudioInfo()
  {
    if (this.b == null) {
      return null;
    }
    return this.b.getAudioInfo();
  }
  
  public TuSdkAudioEncodecOperation getOperation()
  {
    if (this.b == null) {
      TLog.w("%s getOperation need setOutputFormat first", new Object[] { "TuSdkAudioEncoder" });
    }
    return this.b;
  }
  
  public void setMediaSync(TuSdkAudioEncodecSync paramTuSdkAudioEncodecSync)
  {
    this.d = paramTuSdkAudioEncodecSync;
  }
  
  public void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    this.e = paramTuSdkAudioRender;
    if (this.b != null) {
      this.b.setAudioRender(paramTuSdkAudioRender);
    }
  }
  
  public void release()
  {
    if (this.a == 1) {
      return;
    }
    this.a = 1;
    this.b = null;
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public boolean prepare()
  {
    if (this.a > -1)
    {
      TLog.w("%s has prepared.", new Object[] { "TuSdkAudioEncoder" });
      return false;
    }
    if (this.b == null)
    {
      TLog.w("%s run need set setOutputFormat first.", new Object[] { "TuSdkAudioEncoder" });
      return false;
    }
    if (this.c == null)
    {
      TLog.w("%s need setListener first.", new Object[] { "TuSdkAudioEncoder" });
      return false;
    }
    this.a = 0;
    return true;
  }
  
  public void signalEndOfInputStream(long paramLong)
  {
    if (this.b != null) {
      this.b.signalEndOfInputStream(paramLong);
    }
  }
  
  public void autoFillMuteData(long paramLong1, long paramLong2, boolean paramBoolean)
  {
    if (this.b != null) {
      this.b.autoFillMuteData(paramLong1, paramLong2, paramBoolean);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\encoder\TuSdkAudioEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */