package org.lasque.tusdk.core.encoder.audio;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.AlbumHelper;

public class TuSDKAACAudioFileEncoder
  extends TuSDKAudioDataEncoder
{
  private String a;
  private FileOutputStream b;
  
  public void setDelegate(TuSDKAACAudioFileEncoderDelegate paramTuSDKAACAudioFileEncoderDelegate)
  {
    super.setDelegate(paramTuSDKAACAudioFileEncoderDelegate);
  }
  
  public TuSDKAACAudioFileEncoderDelegate getDelegate()
  {
    return (TuSDKAACAudioFileEncoderDelegate)super.getDelegate();
  }
  
  public boolean initEncoder(TuSDKAudioEncoderSetting paramTuSDKAudioEncoderSetting)
  {
    paramTuSDKAudioEncoderSetting.enableBuffers = false;
    return super.initEncoder(paramTuSDKAudioEncoderSetting);
  }
  
  public TuSDKAACAudioFileEncoder setOutputFilePath(String paramString)
  {
    this.a = paramString;
    return this;
  }
  
  public String getOutputFilePath()
  {
    if (this.a == null) {
      this.a = new File(AlbumHelper.getAblumPath(), String.format("lsq_%s.aac", new Object[] { StringHelper.timeStampString() })).getPath();
    }
    return this.a;
  }
  
  protected FileOutputStream getMovieWirter()
  {
    if (this.b != null) {
      return this.b;
    }
    try
    {
      this.b = new FileOutputStream(getOutputFilePath());
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      localFileNotFoundException.printStackTrace();
    }
    return this.b;
  }
  
  protected void onStopeed()
  {
    TLog.d("onStopeed====", new Object[0]);
    super.onStopeed();
    try
    {
      if (getMovieWirter() != null) {
        getMovieWirter().close();
      }
      this.b = null;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    if (getDelegate() != null) {
      getDelegate().onAACAudioFileEncoderComplete(this.a);
    }
  }
  
  public final void onAudioEncoderStarted(MediaFormat paramMediaFormat)
  {
    super.onAudioEncoderStarted(paramMediaFormat);
  }
  
  public final void onAudioEncoderFrameDataAvailable(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    super.onAudioEncoderFrameDataAvailable(paramLong, paramByteBuffer, paramBufferInfo);
    paramByteBuffer.position(paramBufferInfo.offset);
    paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
    int i = paramBufferInfo.size;
    int j = i + 7;
    paramByteBuffer.position(paramBufferInfo.offset);
    paramByteBuffer.limit(paramBufferInfo.offset + i);
    byte[] arrayOfByte = new byte[i + 7];
    TuSdkMediaUtils.addADTStoPacket(arrayOfByte, j, getAudioFormat());
    paramByteBuffer.get(arrayOfByte, 7, i);
    try
    {
      getMovieWirter().write(arrayOfByte, 0, arrayOfByte.length);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }
  
  public static abstract interface TuSDKAACAudioFileEncoderDelegate
    extends TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate
  {
    public abstract void onAACAudioFileEncoderComplete(String paramString);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\encoder\audio\TuSDKAACAudioFileEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */