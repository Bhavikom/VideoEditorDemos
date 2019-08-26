package org.lasque.tusdk.core.media.codec.extend;

import android.media.Image;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodec.Callback;
import android.media.MediaCodec.CryptoInfo;
import android.media.MediaCodec.OnFrameRenderedListener;
import android.media.MediaCodecInfo;
import android.media.MediaCrypto;
import android.media.MediaDescrambler;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.Surface;
import java.nio.ByteBuffer;

public abstract interface TuSdkMediaCodec
{
  public abstract boolean isStarted();
  
  public abstract boolean isReleased();
  
  public abstract Exception configureError();
  
  public abstract boolean reset();
  
  public abstract boolean release();
  
  public abstract boolean configure(MediaFormat paramMediaFormat, Surface paramSurface, MediaCrypto paramMediaCrypto, int paramInt);
  
  public abstract boolean configure(MediaFormat paramMediaFormat, Surface paramSurface, int paramInt, MediaDescrambler paramMediaDescrambler);
  
  public abstract boolean setOutputSurface(Surface paramSurface);
  
  public abstract boolean setInputSurface(Surface paramSurface);
  
  public abstract Surface createInputSurface();
  
  public abstract boolean start();
  
  public abstract boolean stop();
  
  public abstract boolean flush();
  
  public abstract boolean queueInputBuffer(int paramInt1, int paramInt2, int paramInt3, long paramLong, int paramInt4);
  
  public abstract boolean queueSecureInputBuffer(int paramInt1, int paramInt2, MediaCodec.CryptoInfo paramCryptoInfo, long paramLong, int paramInt3);
  
  public abstract int dequeueInputBuffer(long paramLong);
  
  public abstract int dequeueOutputBuffer(MediaCodec.BufferInfo paramBufferInfo, long paramLong);
  
  public abstract boolean releaseOutputBuffer(int paramInt, boolean paramBoolean);
  
  public abstract boolean releaseOutputBuffer(int paramInt, long paramLong);
  
  public abstract boolean signalEndOfInputStream();
  
  public abstract MediaFormat getOutputFormat();
  
  public abstract MediaFormat getInputFormat();
  
  public abstract MediaFormat getOutputFormat(int paramInt);
  
  public abstract ByteBuffer[] getInputBuffers();
  
  public abstract ByteBuffer[] getOutputBuffers();
  
  public abstract ByteBuffer getInputBuffer(int paramInt);
  
  public abstract Image getInputImage(int paramInt);
  
  public abstract ByteBuffer getOutputBuffer(int paramInt);
  
  public abstract Image getOutputImage(int paramInt);
  
  public abstract boolean setVideoScalingMode(int paramInt);
  
  public abstract String getName();
  
  public abstract PersistableBundle getMetrics();
  
  public abstract boolean setParameters(Bundle paramBundle);
  
  public abstract boolean setCallback(MediaCodec.Callback paramCallback, Handler paramHandler);
  
  public abstract boolean setCallback(MediaCodec.Callback paramCallback);
  
  public abstract boolean setOnFrameRenderedListener(MediaCodec.OnFrameRenderedListener paramOnFrameRenderedListener, Handler paramHandler);
  
  public abstract MediaCodecInfo getCodecInfo();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\extend\TuSdkMediaCodec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */