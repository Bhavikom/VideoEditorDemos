package org.lasque.tusdk.core.media.codec.video;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.view.Surface;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput.TuSdkDecodecVideoSurfaceOutput;
import org.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

@TargetApi(18)
public class TuSdkVideoSurfaceDecodecOperation
  implements TuSdkDecodecOperation
{
  private int a = -1;
  private TuSdkMediaCodec b;
  private boolean c;
  private ByteBuffer[] d;
  private final long e = 10000L;
  private TuSdkCodecOutput.TuSdkDecodecVideoSurfaceOutput f;
  private TuSdkVideoSurfaceDecodecOperationPatch g;
  private MediaFormat h;
  private boolean i = false;
  
  public TuSdkVideoSurfaceDecodecOperation(TuSdkCodecOutput.TuSdkDecodecVideoSurfaceOutput paramTuSdkDecodecVideoSurfaceOutput)
  {
    if (paramTuSdkDecodecVideoSurfaceOutput == null) {
      throw new NullPointerException(String.format("%s init failed, codecOutput is Empty", new Object[] { "TuSdkVideoSurfaceDecodecOperation" }));
    }
    this.f = paramTuSdkDecodecVideoSurfaceOutput;
  }
  
  public void flush()
  {
    if ((this.b == null) || (this.i)) {
      return;
    }
    this.b.flush();
  }
  
  public boolean decodecInit(TuSdkMediaExtractor paramTuSdkMediaExtractor)
  {
    this.a = TuSdkMediaUtils.getMediaTrackIndex(paramTuSdkMediaExtractor, "video/");
    if (this.a < 0)
    {
      decodecException(new TuSdkNoMediaTrackException(String.format("%s decodecInit can not find media track: %s", new Object[] { "TuSdkVideoSurfaceDecodecOperation", "video/" })));
      TLog.e("%s decodecInit mTrackIndex result false  ", new Object[] { "TuSdkVideoSurfaceDecodecOperation" });
      return false;
    }
    this.h = paramTuSdkMediaExtractor.getTrackFormat(this.a);
    paramTuSdkMediaExtractor.selectTrack(this.a);
    if (!this.f.canSupportMediaInfo(this.a, this.h))
    {
      TLog.e("%s decodecInit mDecodecOutput result false  ", new Object[] { "TuSdkVideoSurfaceDecodecOperation" });
      TLog.w("%s decodecInit can not Support MediaInfo: %s", new Object[] { "TuSdkVideoSurfaceDecodecOperation", this.h });
      return false;
    }
    Surface localSurface = null;
    while ((!ThreadHelper.isInterrupted()) && ((localSurface = this.f.requestSurface()) == null)) {}
    this.b = a().patchMediaCodec(this.h.getString("mime"));
    if ((this.b.configureError() != null) || (!this.b.configure(this.h, localSurface, null, 0)))
    {
      decodecException(this.b.configureError());
      this.b = null;
      return false;
    }
    this.b.start();
    this.d = this.b.getOutputBuffers();
    this.c = false;
    return true;
  }
  
  private TuSdkVideoSurfaceDecodecOperationPatch a()
  {
    if (this.g == null) {
      this.g = new TuSdkVideoSurfaceDecodecOperationPatch();
    }
    return this.g;
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
    int j = localTuSdkMediaCodec.dequeueOutputBuffer(localBufferInfo, 10000L);
    switch (j)
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
      if (j >= 0)
      {
        if (localBufferInfo.size > 0)
        {
          ByteBuffer localByteBuffer = this.d[j];
          this.f.processOutputBuffer(paramTuSdkMediaExtractor, this.a, localByteBuffer, localBufferInfo);
        }
        if (!this.i) {
          localTuSdkMediaCodec.releaseOutputBuffer(j, true);
        }
        this.f.updated(localBufferInfo);
      }
      break;
    }
    if ((localBufferInfo.flags & 0x4) != 0) {
      return this.f.updatedToEOS(localBufferInfo);
    }
    return false;
  }
  
  public void decodecRelease()
  {
    this.i = true;
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


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\video\TuSdkVideoSurfaceDecodecOperation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */