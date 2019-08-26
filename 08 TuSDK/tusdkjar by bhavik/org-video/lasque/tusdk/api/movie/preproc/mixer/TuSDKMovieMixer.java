package org.lasque.tusdk.api.movie.preproc.mixer;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;
import org.lasque.tusdk.video.editor.TuSDKMovieWriter;
import org.lasque.tusdk.video.editor.TuSDKMovieWriterInterface.ByteBufferData;
import org.lasque.tusdk.video.editor.TuSDKMovieWriterInterface.MovieWriterOutputFormat;

public abstract class TuSDKMovieMixer
  implements TuSDKMovieMixerInterface
{
  private TuSDKMovieWriter a;
  
  public TuSDKMovieMixer setMediaWriter(TuSDKMovieWriter paramTuSDKMovieWriter)
  {
    this.a = paramTuSDKMovieWriter;
    return this;
  }
  
  public TuSDKMovieWriter getMediaWriter()
  {
    if (this.a == null) {
      this.a = new TuSDKMovieWriter(getOutputFilePah(), getOutputFormat());
    }
    return this.a;
  }
  
  protected void startMovieWriter()
  {
    getMediaWriter().start();
  }
  
  protected void stopMovieWriter()
  {
    if (this.a != null) {
      this.a.stop();
    }
    this.a = null;
  }
  
  public int addAudioTrack(MediaFormat paramMediaFormat)
  {
    return getMediaWriter().addAudioTrack(paramMediaFormat);
  }
  
  public int addVideoTrack(MediaFormat paramMediaFormat)
  {
    return getMediaWriter().addVideoTrack(paramMediaFormat);
  }
  
  public void writeSampleData(TuSDKMovieWriterInterface.ByteBufferData paramByteBufferData)
  {
    getMediaWriter().writeSampleData(paramByteBufferData);
  }
  
  public void writeAudioSampleData(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    getMediaWriter().writeAudioSampleData(paramByteBuffer, paramBufferInfo);
  }
  
  public void writeVideoSampleData(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    getMediaWriter().writeVideoSampleData(paramByteBuffer, paramBufferInfo);
  }
  
  public abstract String getOutputFilePah();
  
  protected abstract TuSDKMovieWriterInterface.MovieWriterOutputFormat getOutputFormat();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\movie\preproc\mixer\TuSDKMovieMixer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */