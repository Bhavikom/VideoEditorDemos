package org.lasque.tusdk.core.media.codec;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.view.Surface;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;

public abstract interface TuSdkCodecOutput
{
  public abstract void outputFormatChanged(MediaFormat paramMediaFormat);
  
  public abstract void updated(MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract boolean updatedToEOS(MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void onCatchedException(Exception paramException);
  
  public static abstract interface TuSdkDecodecVideoSurfaceOutput
    extends TuSdkCodecOutput.TuSdkDecodecOutput
  {
    public abstract Surface requestSurface();
  }
  
  public static abstract interface TuSdkEncodecOutput
    extends TuSdkCodecOutput
  {
    public abstract void processOutputBuffer(TuSdkMediaMuxer paramTuSdkMediaMuxer, int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  }
  
  public static abstract interface TuSdkDecodecOutput
    extends TuSdkCodecOutput
  {
    public abstract boolean canSupportMediaInfo(int paramInt, MediaFormat paramMediaFormat);
    
    public abstract boolean processExtractor(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec);
    
    public abstract void processOutputBuffer(TuSdkMediaExtractor paramTuSdkMediaExtractor, int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\TuSdkCodecOutput.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */