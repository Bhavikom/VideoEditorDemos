package org.lasque.tusdk.core.media.codec.video;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.Surface;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput.TuSdkEncodecOutput;
import org.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;
import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodecImpl;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class TuSdkVideoSurfaceEncodecOperation
  implements TuSdkEncodecOperation
{
  private Surface a;
  private TuSdkMediaCodec b;
  private boolean c = false;
  private int d = -1;
  private final long e = 10000L;
  private TuSdkCodecOutput.TuSdkEncodecOutput f;
  private ByteBuffer[] g;
  private TuSdkVideoSurfaceEncodecOperationPatch h;
  private final TuSdkVideoInfo i = new TuSdkVideoInfo();
  private boolean j = false;
  
  public TuSdkVideoSurfaceEncodecOperation(TuSdkCodecOutput.TuSdkEncodecOutput paramTuSdkEncodecOutput)
  {
    if (paramTuSdkEncodecOutput == null) {
      throw new NullPointerException(String.format("%s init failed, encodecOutput is NULL", new Object[] { "TuSdkVideoSurfaceEncodecOperation" }));
    }
    this.f = paramTuSdkEncodecOutput;
  }
  
  public Surface getSurface()
  {
    if (this.a == null)
    {
      TLog.w("%s requestSurface need setMediaFormat first", new Object[] { "TuSdkVideoSurfaceEncodecOperation" });
      return null;
    }
    return this.a;
  }
  
  public TuSdkVideoSurfaceEncodecOperationPatch getCodecPatch()
  {
    if (this.h == null) {
      this.h = new TuSdkVideoSurfaceEncodecOperationPatch();
    }
    return this.h;
  }
  
  public void setCodecPatch(TuSdkVideoSurfaceEncodecOperationPatch paramTuSdkVideoSurfaceEncodecOperationPatch)
  {
    this.h = paramTuSdkVideoSurfaceEncodecOperationPatch;
  }
  
  public TuSdkVideoInfo getVideoInfo()
  {
    return this.i;
  }
  
  public int setMediaFormat(MediaFormat paramMediaFormat)
  {
    int k = TuSdkMediaFormat.checkVideoEncodec(paramMediaFormat);
    if (k != 0) {
      return k;
    }
    TuSdkMediaCodec localTuSdkMediaCodec = null;
    if (getCodecPatch() != null)
    {
      this.c = getCodecPatch().patchRequestKeyFrame(paramMediaFormat);
      localTuSdkMediaCodec = getCodecPatch().patchMediaCodec();
    }
    if (localTuSdkMediaCodec == null) {
      localTuSdkMediaCodec = TuSdkMediaCodecImpl.createEncoderByType(paramMediaFormat.getString("mime"));
    }
    if (localTuSdkMediaCodec.configureError() != null)
    {
      TLog.e(localTuSdkMediaCodec.configureError(), "%s setMediaFormat create MediaCodec failed", new Object[] { "TuSdkVideoSurfaceEncodecOperation" });
      return 256;
    }
    this.b = localTuSdkMediaCodec;
    this.b.configure(paramMediaFormat, null, null, 1);
    this.a = localTuSdkMediaCodec.createInputSurface();
    this.i.setEncodecInfo(paramMediaFormat);
    return 0;
  }
  
  public void requestKeyFrame()
  {
    if ((this.b == null) || (this.j) || (Build.VERSION.SDK_INT < 19)) {
      return;
    }
    Bundle localBundle = new Bundle();
    localBundle.putInt("request-sync", 0);
    this.b.setParameters(localBundle);
  }
  
  public void notifyNewFrameReady()
  {
    if (this.c) {
      requestKeyFrame();
    }
  }
  
  public void signalEndOfInputStream()
  {
    if ((this.b == null) || (this.j)) {
      return;
    }
    this.b.signalEndOfInputStream();
  }
  
  public void encodecRelease()
  {
    this.j = true;
    if (this.b == null) {
      return;
    }
    this.b.stop();
    this.b.release();
    this.b = null;
  }
  
  protected void finalize()
  {
    encodecRelease();
    super.finalize();
  }
  
  public boolean isEncodecStarted()
  {
    if (this.b != null) {
      return this.b.isStarted();
    }
    return false;
  }
  
  public void encodecException(Exception paramException)
  {
    if (this.f == null)
    {
      TLog.e(paramException, "%s the Thread catch exception, The thread wil exit.", new Object[] { "TuSdkVideoSurfaceEncodecOperation" });
      return;
    }
    this.f.onCatchedException(paramException);
  }
  
  public void flush()
  {
    if (this.b != null) {
      this.b.flush();
    }
  }
  
  public boolean encodecInit(TuSdkMediaMuxer paramTuSdkMediaMuxer)
  {
    TuSdkMediaCodec localTuSdkMediaCodec = this.b;
    TuSdkCodecOutput.TuSdkEncodecOutput localTuSdkEncodecOutput = this.f;
    if ((localTuSdkMediaCodec == null) || (localTuSdkEncodecOutput == null))
    {
      TLog.w("%s decodecInit need setMediaFormat first", new Object[] { "TuSdkVideoSurfaceEncodecOperation" });
      return false;
    }
    localTuSdkMediaCodec.start();
    this.g = localTuSdkMediaCodec.getOutputBuffers();
    return true;
  }
  
  public boolean encodecProcessUntilEnd(TuSdkMediaMuxer paramTuSdkMediaMuxer)
  {
    TuSdkMediaCodec localTuSdkMediaCodec = this.b;
    TuSdkCodecOutput.TuSdkEncodecOutput localTuSdkEncodecOutput = this.f;
    if ((localTuSdkMediaCodec == null) || (localTuSdkEncodecOutput == null)) {
      return true;
    }
    if (this.c) {
      requestKeyFrame();
    }
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    int k = localTuSdkMediaCodec.dequeueOutputBuffer(localBufferInfo, 10000L);
    switch (k)
    {
    case -2: 
      this.i.setMediaFormat(localTuSdkMediaCodec.getOutputFormat());
      this.d = paramTuSdkMediaMuxer.addTrack(localTuSdkMediaCodec.getOutputFormat());
      localTuSdkEncodecOutput.outputFormatChanged(localTuSdkMediaCodec.getOutputFormat());
      break;
    case -1: 
      break;
    case -3: 
      this.g = localTuSdkMediaCodec.getOutputBuffers();
      break;
    default: 
      if (k >= 0)
      {
        if (localBufferInfo.size > 0)
        {
          ByteBuffer localByteBuffer = this.g[k];
          localTuSdkEncodecOutput.processOutputBuffer(paramTuSdkMediaMuxer, this.d, localByteBuffer, localBufferInfo);
        }
        if (!this.j) {
          localTuSdkMediaCodec.releaseOutputBuffer(k, false);
        }
        localTuSdkEncodecOutput.updated(localBufferInfo);
      }
      break;
    }
    if ((localBufferInfo.flags & 0x4) != 0) {
      return localTuSdkEncodecOutput.updatedToEOS(localBufferInfo);
    }
    return false;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\video\TuSdkVideoSurfaceEncodecOperation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */