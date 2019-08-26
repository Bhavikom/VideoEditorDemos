package org.lasque.tusdk.core.decoder;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

@TargetApi(16)
public class TuSDKAudioDecoder
  extends TuSDKMediaDecoder
  implements TuSDKAudioDecoderInterface
{
  private OnAudioDecoderDelegate a;
  private TuSdkTimeRange b;
  private TuSDKAudioInfo c;
  private String d;
  private FileOutputStream e;
  private long f;
  private int g;
  private volatile boolean h = true;
  
  public void setDelegate(OnAudioDecoderDelegate paramOnAudioDecoderDelegate)
  {
    this.a = paramOnAudioDecoderDelegate;
  }
  
  public MediaCodec getAudioDecoder()
  {
    if (this.mAudioDecoder == null)
    {
      this.mAudioDecoder = createAudioDecoder();
      MediaFormat localMediaFormat = getAudioTrackFormat();
      if (localMediaFormat == null) {
        return null;
      }
      this.mAudioDecoder.configure(localMediaFormat, null, null, 0);
    }
    return this.mAudioDecoder;
  }
  
  public MediaCodec getVideoDecoder()
  {
    return null;
  }
  
  public TuSDKAudioDecoder(String paramString)
  {
    super(TuSDKMediaDataSource.create(paramString));
  }
  
  public TuSDKAudioDecoder(String paramString1, String paramString2)
  {
    this(TuSDKMediaDataSource.create(paramString1), paramString2);
  }
  
  public TuSDKAudioDecoder(TuSDKMediaDataSource paramTuSDKMediaDataSource, String paramString)
  {
    super(paramTuSDKMediaDataSource);
    this.d = paramString;
  }
  
  public TuSDKAudioInfo getAudioInfo()
  {
    if (this.c == null) {
      this.c = TuSDKAudioInfo.createWithMediaFormat(getAudioTrackFormat());
    }
    return this.c;
  }
  
  public long getDurationTimes()
  {
    if (getAudioInfo() == null) {
      return 0L;
    }
    return getAudioInfo().durationTimeUs;
  }
  
  public TuSdkTimeRange getDecodeTimeRange()
  {
    return this.b;
  }
  
  public void setDecodeTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
  {
    this.b = paramTuSdkTimeRange;
  }
  
  private boolean a()
  {
    return (getDecodeTimeRange() != null) && (getDecodeTimeRange().isValid());
  }
  
  public void start()
  {
    this.mMovieReader = createMovieReader();
    if (this.mMovieReader == null) {
      return;
    }
    this.mMovieReader.setTimeRange(this.b);
    super.start();
    try
    {
      c();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }
  
  public void stop()
  {
    if (!this.h) {
      return;
    }
    this.h = false;
    if (this.d != null) {
      new File(this.d).delete();
    }
  }
  
  private void b()
  {
    super.stop();
    this.h = false;
    if (this.e != null) {
      try
      {
        this.e.close();
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    }
  }
  
  public void seekTo(long paramLong)
  {
    if (this.mMovieReader == null) {
      return;
    }
    this.mMovieReader.seekTo(paramLong, 2);
  }
  
  protected void writeRawDataToFile(byte[] paramArrayOfByte)
  {
    if (this.e == null) {
      return;
    }
    try
    {
      this.e.write(paramArrayOfByte);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }
  
  private void c()
  {
    if (selectAudioTrack() == -1)
    {
      onDecoderError(TuSDKMediaDecoder.TuSDKMediaDecoderError.InvalidDataSource);
      return;
    }
    this.h = true;
    TuSDKAudioInfo localTuSDKAudioInfo = getAudioInfo();
    localTuSDKAudioInfo.setFilePath(this.d);
    if (!TextUtils.isEmpty(localTuSDKAudioInfo.getFilePath())) {
      this.e = new FileOutputStream(localTuSDKAudioInfo.getFilePath());
    }
    if (this.a != null) {
      this.a.onDecodeRawInfo(localTuSDKAudioInfo);
    }
    boolean bool1 = false;
    boolean bool2 = false;
    if (a()) {
      seekTo(getDecodeTimeRange().getStartTimeUS());
    } else {
      seekTo(0L);
    }
    while ((!bool2) && (this.h))
    {
      if (!bool1) {
        bool1 = d();
      }
      bool2 = e();
    }
    localTuSDKAudioInfo.size = this.g;
    this.c.size = this.g;
    b();
    if ((this.a != null) && (this.h)) {
      this.a.onDecode(null, null, 1.0D);
    }
  }
  
  private boolean d()
  {
    ByteBuffer[] arrayOfByteBuffer = this.mAudioDecoder.getInputBuffers();
    int i = this.mAudioDecoder.dequeueInputBuffer(500L);
    if (i >= 0)
    {
      ByteBuffer localByteBuffer = arrayOfByteBuffer[i];
      int j = this.mMovieReader.readSampleData(localByteBuffer, 0);
      if (j < 0)
      {
        this.mAudioDecoder.queueInputBuffer(i, 0, 0, this.mMovieReader.getSampleTime(), 4);
        return true;
      }
      long l = this.mMovieReader.getSampleTime();
      this.mAudioDecoder.queueInputBuffer(i, 0, j, l, 0);
    }
    return false;
  }
  
  private boolean e()
  {
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    ByteBuffer[] arrayOfByteBuffer = this.mAudioDecoder.getOutputBuffers();
    int i = this.mAudioDecoder.dequeueOutputBuffer(localBufferInfo, 500L);
    if (i >= 0)
    {
      if ((localBufferInfo.flags & 0x2) != 0)
      {
        this.mAudioDecoder.releaseOutputBuffer(i, false);
        return false;
      }
      if (localBufferInfo.size > 0)
      {
        ByteBuffer localByteBuffer = arrayOfByteBuffer[i];
        localByteBuffer.position(localBufferInfo.offset);
        localByteBuffer.limit(localBufferInfo.offset + localBufferInfo.size);
        byte[] arrayOfByte = new byte[localBufferInfo.size];
        localByteBuffer.get(arrayOfByte);
        this.g += arrayOfByte.length;
        this.f += this.c.getFrameInterval();
        float f1 = getDurationTimes() > 0L ? (float)(localBufferInfo.presentationTimeUs / getDurationTimes()) : 0.0F;
        onDecode(arrayOfByte, f1);
        writeRawDataToFile(arrayOfByte);
        if (this.a != null) {
          this.a.onDecode(arrayOfByte, localBufferInfo, f1);
        }
      }
      this.mAudioDecoder.releaseOutputBuffer(i, false);
      if ((localBufferInfo.flags & 0x4) != 0) {
        return true;
      }
      if ((a()) && (this.f >= getDecodeTimeRange().durationTimeUS()))
      {
        unselectAudioTrack();
        return true;
      }
    }
    else if (i == -3)
    {
      arrayOfByteBuffer = this.mAudioDecoder.getOutputBuffers();
    }
    return false;
  }
  
  protected void onDecoderError(TuSDKMediaDecoder.TuSDKMediaDecoderError paramTuSDKMediaDecoderError)
  {
    super.onDecoderError(paramTuSDKMediaDecoderError);
    if (this.a != null) {
      this.a.onDecoderErrorCode(paramTuSDKMediaDecoderError);
    }
  }
  
  public void onDecode(byte[] paramArrayOfByte, double paramDouble) {}
  
  public static abstract interface OnAudioDecoderDelegate
  {
    public abstract void onDecodeRawInfo(TuSDKAudioInfo paramTuSDKAudioInfo);
    
    public abstract void onDecode(byte[] paramArrayOfByte, MediaCodec.BufferInfo paramBufferInfo, double paramDouble);
    
    public abstract void onDecoderErrorCode(TuSDKMediaDecoder.TuSDKMediaDecoderError paramTuSDKMediaDecoderError);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKAudioDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */