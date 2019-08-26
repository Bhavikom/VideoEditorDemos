package org.lasque.tusdk.core.media.codec.extend;

import android.annotation.TargetApi;
import android.media.Image;
import android.media.MediaCodec;
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
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public final class TuSdkMediaCodecImpl
  implements TuSdkMediaCodec
{
  private final MediaCodec a;
  private Exception b;
  private boolean c = false;
  private boolean d = false;
  
  public static TuSdkMediaCodec createDecoderByType(String paramString)
  {
    return a(paramString, true, false);
  }
  
  public static TuSdkMediaCodec createEncoderByType(String paramString)
  {
    return a(paramString, true, true);
  }
  
  public static TuSdkMediaCodec createByCodecName(String paramString)
  {
    return a(paramString, false, false);
  }
  
  @TargetApi(23)
  public static Surface createPersistentInputSurface()
  {
    return MediaCodec.createPersistentInputSurface();
  }
  
  private static TuSdkMediaCodec a(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    MediaCodec localMediaCodec = null;
    Object localObject = null;
    try
    {
      if (!paramBoolean1) {
        localMediaCodec = MediaCodec.createByCodecName(paramString);
      } else if (!paramBoolean2) {
        localMediaCodec = MediaCodec.createDecoderByType(paramString);
      } else {
        localMediaCodec = MediaCodec.createEncoderByType(paramString);
      }
    }
    catch (Exception localException)
    {
      localObject = localException;
    }
    TuSdkMediaCodecImpl localTuSdkMediaCodecImpl = new TuSdkMediaCodecImpl(localMediaCodec);
    localTuSdkMediaCodecImpl.b = ((Exception)localObject);
    return localTuSdkMediaCodecImpl;
  }
  
  public boolean isStarted()
  {
    return this.c;
  }
  
  public boolean isReleased()
  {
    return this.d;
  }
  
  public Exception configureError()
  {
    return this.b;
  }
  
  private TuSdkMediaCodecImpl(MediaCodec paramMediaCodec)
  {
    this.a = paramMediaCodec;
  }
  
  @TargetApi(21)
  public final boolean reset()
  {
    try
    {
      this.a.reset();
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  public final boolean release()
  {
    try
    {
      this.a.release();
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    this.c = false;
    this.d = true;
    return true;
  }
  
  public boolean configure(MediaFormat paramMediaFormat, Surface paramSurface, MediaCrypto paramMediaCrypto, int paramInt)
  {
    try
    {
      this.a.configure(paramMediaFormat, paramSurface, paramMediaCrypto, paramInt);
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  @TargetApi(26)
  public boolean configure(MediaFormat paramMediaFormat, Surface paramSurface, int paramInt, MediaDescrambler paramMediaDescrambler)
  {
    try
    {
      this.a.configure(paramMediaFormat, paramSurface, paramInt, paramMediaDescrambler);
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  @TargetApi(23)
  public boolean setOutputSurface(Surface paramSurface)
  {
    try
    {
      this.a.setOutputSurface(paramSurface);
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  @TargetApi(23)
  public boolean setInputSurface(Surface paramSurface)
  {
    try
    {
      this.a.setInputSurface(paramSurface);
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  public final Surface createInputSurface()
  {
    try
    {
      return this.a.createInputSurface();
    }
    catch (Exception localException)
    {
      this.b = localException;
    }
    return null;
  }
  
  public final boolean start()
  {
    try
    {
      this.a.start();
    }
    catch (Exception localException)
    {
      this.b = localException;
      this.d = true;
      return false;
    }
    this.d = false;
    this.c = true;
    return true;
  }
  
  public final boolean stop()
  {
    try
    {
      this.a.stop();
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    this.d = true;
    this.c = false;
    return true;
  }
  
  public final boolean flush()
  {
    try
    {
      this.a.flush();
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  public final boolean queueInputBuffer(int paramInt1, int paramInt2, int paramInt3, long paramLong, int paramInt4)
  {
    try
    {
      this.a.queueInputBuffer(paramInt1, paramInt2, paramInt3, paramLong, paramInt4);
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  public final boolean queueSecureInputBuffer(int paramInt1, int paramInt2, MediaCodec.CryptoInfo paramCryptoInfo, long paramLong, int paramInt3)
  {
    try
    {
      this.a.queueSecureInputBuffer(paramInt1, paramInt2, paramCryptoInfo, paramLong, paramInt3);
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  public final int dequeueInputBuffer(long paramLong)
  {
    try
    {
      return this.a.dequeueInputBuffer(paramLong);
    }
    catch (Exception localException)
    {
      this.b = localException;
      TLog.e(localException, "%s dequeueInputBuffer failed, ignore then try once.", new Object[] { "TuSdkMediaCodecImpl" });
    }
    return -1;
  }
  
  public final int dequeueOutputBuffer(MediaCodec.BufferInfo paramBufferInfo, long paramLong)
  {
    try
    {
      return this.a.dequeueOutputBuffer(paramBufferInfo, paramLong);
    }
    catch (Exception localException)
    {
      this.b = localException;
      TLog.e(localException, "%s dequeueOutputBuffer failed, ignore then try once.", new Object[] { "TuSdkMediaCodecImpl" });
    }
    return -1;
  }
  
  public final boolean releaseOutputBuffer(int paramInt, boolean paramBoolean)
  {
    try
    {
      this.a.releaseOutputBuffer(paramInt, paramBoolean);
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  @TargetApi(21)
  public final boolean releaseOutputBuffer(int paramInt, long paramLong)
  {
    try
    {
      this.a.releaseOutputBuffer(paramInt, paramLong);
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  public final boolean signalEndOfInputStream()
  {
    try
    {
      this.a.signalEndOfInputStream();
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  public final MediaFormat getOutputFormat()
  {
    try
    {
      return this.a.getOutputFormat();
    }
    catch (Exception localException)
    {
      this.b = localException;
      TLog.e(localException, "%s getOutputFormat failed, ignore then try once.", new Object[] { "TuSdkMediaCodecImpl" });
    }
    return null;
  }
  
  @TargetApi(21)
  public final MediaFormat getInputFormat()
  {
    try
    {
      return this.a.getInputFormat();
    }
    catch (Exception localException)
    {
      this.b = localException;
      TLog.e(localException, "%s getInputFormat failed, ignore then try once.", new Object[] { "TuSdkMediaCodecImpl" });
    }
    return null;
  }
  
  @TargetApi(21)
  public final MediaFormat getOutputFormat(int paramInt)
  {
    try
    {
      return this.a.getOutputFormat(paramInt);
    }
    catch (Exception localException)
    {
      this.b = localException;
      TLog.e(localException, "%s getOutputFormat failed, ignore then try once.", new Object[] { "TuSdkMediaCodecImpl" });
    }
    return null;
  }
  
  public ByteBuffer[] getInputBuffers()
  {
    try
    {
      return this.a.getInputBuffers();
    }
    catch (Exception localException)
    {
      this.b = localException;
      TLog.e(localException, "%s getInputBuffers failed, ignore then try once.", new Object[] { "TuSdkMediaCodecImpl" });
    }
    return null;
  }
  
  public ByteBuffer[] getOutputBuffers()
  {
    try
    {
      return this.a.getOutputBuffers();
    }
    catch (Exception localException)
    {
      this.b = localException;
      TLog.e(localException, "%s getOutputBuffers failed, ignore then try once.", new Object[] { "TuSdkMediaCodecImpl" });
    }
    return null;
  }
  
  @TargetApi(21)
  public ByteBuffer getInputBuffer(int paramInt)
  {
    try
    {
      return this.a.getInputBuffer(paramInt);
    }
    catch (Exception localException)
    {
      this.b = localException;
      TLog.e(localException, "%s getInputBuffer failed, ignore then try once.", new Object[] { "TuSdkMediaCodecImpl" });
    }
    return null;
  }
  
  @TargetApi(21)
  public Image getInputImage(int paramInt)
  {
    try
    {
      return this.a.getInputImage(paramInt);
    }
    catch (Exception localException)
    {
      this.b = localException;
      TLog.e(localException, "%s getInputImage failed, ignore then try once.", new Object[] { "TuSdkMediaCodecImpl" });
    }
    return null;
  }
  
  @TargetApi(21)
  public ByteBuffer getOutputBuffer(int paramInt)
  {
    try
    {
      return this.a.getOutputBuffer(paramInt);
    }
    catch (Exception localException)
    {
      this.b = localException;
      TLog.e(localException, "%s getOutputBuffer failed, ignore then try once.", new Object[] { "TuSdkMediaCodecImpl" });
    }
    return null;
  }
  
  @TargetApi(21)
  public Image getOutputImage(int paramInt)
  {
    try
    {
      return this.a.getOutputImage(paramInt);
    }
    catch (Exception localException)
    {
      this.b = localException;
      TLog.e(localException, "%s getOutputImage failed,, ignore then try once.", new Object[] { "TuSdkMediaCodecImpl" });
    }
    return null;
  }
  
  public final boolean setVideoScalingMode(int paramInt)
  {
    try
    {
      this.a.setVideoScalingMode(paramInt);
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  public final String getName()
  {
    try
    {
      return this.a.getName();
    }
    catch (Exception localException)
    {
      this.b = localException;
      TLog.e(localException, "%s getName failed, ignore then try once.", new Object[] { "TuSdkMediaCodecImpl" });
    }
    return null;
  }
  
  @TargetApi(26)
  public PersistableBundle getMetrics()
  {
    try
    {
      return this.a.getMetrics();
    }
    catch (Exception localException)
    {
      this.b = localException;
      TLog.e(localException, "%s getMetrics failed, ignore then try once.", new Object[] { "TuSdkMediaCodecImpl" });
    }
    return null;
  }
  
  @TargetApi(19)
  public final boolean setParameters(Bundle paramBundle)
  {
    try
    {
      this.a.setParameters(paramBundle);
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  @TargetApi(23)
  public boolean setCallback(MediaCodec.Callback paramCallback, Handler paramHandler)
  {
    try
    {
      this.a.setCallback(paramCallback, paramHandler);
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  @TargetApi(21)
  public boolean setCallback(MediaCodec.Callback paramCallback)
  {
    try
    {
      this.a.setCallback(paramCallback);
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  @TargetApi(23)
  public boolean setOnFrameRenderedListener(MediaCodec.OnFrameRenderedListener paramOnFrameRenderedListener, Handler paramHandler)
  {
    try
    {
      this.a.setOnFrameRenderedListener(paramOnFrameRenderedListener, paramHandler);
    }
    catch (Exception localException)
    {
      this.b = localException;
      return false;
    }
    return true;
  }
  
  public MediaCodecInfo getCodecInfo()
  {
    try
    {
      return this.a.getCodecInfo();
    }
    catch (Exception localException)
    {
      this.b = localException;
      TLog.e(localException, "%s getCodecInfo failed, ignore then try once.", new Object[] { "TuSdkMediaCodecImpl" });
    }
    return null;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\extend\TuSdkMediaCodecImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */