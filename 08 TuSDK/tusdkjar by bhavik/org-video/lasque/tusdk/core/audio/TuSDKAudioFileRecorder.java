package org.lasque.tusdk.core.audio;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;
import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate;
import org.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.AlbumHelper;
import org.lasque.tusdk.video.editor.TuSDKMovieWriter;
import org.lasque.tusdk.video.editor.TuSDKMovieWriterInterface.MovieWriterOutputFormat;

public class TuSDKAudioFileRecorder
  extends TuSDKAudioRecorderCore
{
  private OutputFormat a = OutputFormat.AAC;
  private RecordState b;
  private File c;
  private FileOutputStream d;
  private TuSDKMovieWriter e;
  private TuSDKRecordAudioDelegate f;
  private boolean g = false;
  private boolean h = false;
  private TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate i = new TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate()
  {
    public void onAudioEncoderStarted(MediaFormat paramAnonymousMediaFormat)
    {
      if ((!TuSDKAudioFileRecorder.this.isRecording()) || (TuSDKAudioFileRecorder.this.getMovieWriter() == null) || (!TuSDKAudioFileRecorder.this.getMovieWriter().canAddAudioTrack())) {
        return;
      }
      TuSDKAudioFileRecorder.this.getMovieWriter().addAudioTrack(paramAnonymousMediaFormat);
      TuSDKAudioFileRecorder.this.getMovieWriter().start();
    }
    
    public void onAudioEncoderStoped() {}
    
    public void onAudioEncoderFrameDataAvailable(long paramAnonymousLong, ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSDKAudioFileRecorder.a(TuSDKAudioFileRecorder.this, paramAnonymousByteBuffer, paramAnonymousBufferInfo);
    }
    
    public void onAudioEncoderCodecConfig(long paramAnonymousLong, ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo) {}
  };
  
  public TuSDKAudioFileRecorder()
  {
    this(TuSDKAudioCaptureSetting.defaultCaptureSetting(), TuSDKAudioEncoderSetting.defaultEncoderSetting());
  }
  
  public TuSDKAudioFileRecorder(TuSDKAudioCaptureSetting paramTuSDKAudioCaptureSetting, TuSDKAudioEncoderSetting paramTuSDKAudioEncoderSetting)
  {
    super(paramTuSDKAudioCaptureSetting, paramTuSDKAudioEncoderSetting);
    getAudioEncoder().setDelegate(this.i);
  }
  
  public void enableSplicing(boolean paramBoolean)
  {
    this.g = paramBoolean;
  }
  
  @TargetApi(16)
  protected void onRawAudioFrameDataAvailable(byte[] paramArrayOfByte)
  {
    if (this.b != RecordState.Recording) {
      return;
    }
    if (this.a == OutputFormat.PCM) {
      a(paramArrayOfByte);
    } else {
      super.onRawAudioFrameDataAvailable(paramArrayOfByte);
    }
  }
  
  public void start()
  {
    if ((isRecording()) && (this.b == RecordState.Recording)) {
      return;
    }
    initWriter();
    super.startRecording();
    if (!isPrepared())
    {
      notifyRecordingError(RecordError.InitializationFailed);
      return;
    }
    notifyRecordingState(RecordState.Recording);
    TLog.d("Recording start", new Object[0]);
  }
  
  public void pauseRecord()
  {
    this.h = true;
  }
  
  public void resumeRecord()
  {
    this.h = false;
  }
  
  public boolean isPauseRecord()
  {
    return this.h;
  }
  
  public void stop()
  {
    if ((!isRecording()) || (this.b != RecordState.Recording)) {
      return;
    }
    super.stopRecording();
    stopWriter();
    notifyRecordingState(RecordState.Stoped);
    TLog.d("Recording stoped", new Object[0]);
    StatisticsManger.appendComponent(9449476L);
  }
  
  protected void notifyRecordingError(final RecordError paramRecordError)
  {
    if (this.f == null) {
      return;
    }
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuSDKAudioFileRecorder.a(TuSDKAudioFileRecorder.this).onAudioRecordError(paramRecordError);
      }
    });
  }
  
  protected void notifyRecordingState(final RecordState paramRecordState)
  {
    this.b = paramRecordState;
    if (this.f == null) {
      return;
    }
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuSDKAudioFileRecorder.a(TuSDKAudioFileRecorder.this).onAudioRecordStateChanged(paramRecordState);
      }
    });
  }
  
  public void setAudioRecordDelegate(TuSDKRecordAudioDelegate paramTuSDKRecordAudioDelegate)
  {
    this.f = paramTuSDKRecordAudioDelegate;
  }
  
  protected void initWriter()
  {
    if (this.a == OutputFormat.PCM) {
      a();
    }
    if (this.a == OutputFormat.AAC) {
      c();
    }
  }
  
  protected void stopWriter()
  {
    if (this.a == OutputFormat.PCM) {
      b();
    }
    if (this.a == OutputFormat.AAC) {
      d();
    }
    if (this.f != null) {
      this.f.onAudioRecordComplete(this.c);
    }
    if (!this.g) {
      this.c = null;
    }
  }
  
  private void a()
  {
    if ((getOutputFile().exists()) && (!this.g)) {
      getOutputFile().delete();
    }
    try
    {
      if (this.d == null) {
        this.d = new FileOutputStream(getOutputFile(), true);
      }
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      localFileNotFoundException.printStackTrace();
    }
  }
  
  private void a(byte[] paramArrayOfByte)
  {
    if (this.d == null) {
      return;
    }
    try
    {
      this.d.write(paramArrayOfByte);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }
  
  private void b()
  {
    if (this.d == null) {
      return;
    }
    try
    {
      this.d.close();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    this.d = null;
  }
  
  private void c()
  {
    if (this.e == null)
    {
      if ((getOutputFile().exists()) && (!this.g)) {
        getOutputFile().delete();
      }
      this.e = TuSDKMovieWriter.create(getOutputFile().getPath(), TuSDKMovieWriterInterface.MovieWriterOutputFormat.MPEG_4);
    }
  }
  
  private void a(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (isPauseRecord()) {
      return;
    }
    if ((!isRecording()) || (getMovieWriter() == null) || (!getMovieWriter().isStarted()) || (!getMovieWriter().hasAudioTrack())) {
      return;
    }
    TLog.e("BufferInfoTime :%s", new Object[] { Long.valueOf(paramBufferInfo.presentationTimeUs) });
    getMovieWriter().writeAudioSampleData(paramByteBuffer, paramBufferInfo);
  }
  
  private void d()
  {
    if (this.e == null) {
      return;
    }
    this.e.stop();
    if (this.g) {
      return;
    }
    this.e = null;
  }
  
  public void setOutputFormat(OutputFormat paramOutputFormat)
  {
    this.a = paramOutputFormat;
  }
  
  public File getOutputFile()
  {
    if ((this.c == null) && (this.a == OutputFormat.PCM)) {
      this.c = new File(AlbumHelper.getAblumPath(), String.format("lsq_%s.pcm", new Object[] { StringHelper.timeStampString() }));
    }
    if ((this.c == null) && (this.a == OutputFormat.AAC)) {
      this.c = new File(AlbumHelper.getAblumPath(), String.format("lsq_%s.aac", new Object[] { StringHelper.timeStampString() }));
    }
    return this.c;
  }
  
  public void setOutputFile(File paramFile)
  {
    this.c = paramFile;
  }
  
  protected TuSDKMovieWriter getMovieWriter()
  {
    return this.e;
  }
  
  public static abstract interface TuSDKRecordAudioDelegate
  {
    public abstract void onAudioRecordComplete(File paramFile);
    
    public abstract void onAudioRecordStateChanged(TuSDKAudioFileRecorder.RecordState paramRecordState);
    
    public abstract void onAudioRecordError(TuSDKAudioFileRecorder.RecordError paramRecordError);
  }
  
  public static enum OutputFormat
  {
    private OutputFormat() {}
  }
  
  public static enum RecordError
  {
    private RecordError() {}
  }
  
  public static enum RecordState
  {
    private RecordState() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\audio\TuSDKAudioFileRecorder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */