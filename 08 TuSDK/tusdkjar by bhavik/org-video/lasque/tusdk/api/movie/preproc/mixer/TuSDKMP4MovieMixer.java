package org.lasque.tusdk.api.movie.preproc.mixer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.AsyncTask;
import android.text.TextUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioEntry;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioMixer;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioMixer.OnAudioMixerDelegate;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioMixer.State;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAverageAudioMixer;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.decoder.TuSDKAudioInfo;
import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.video.TuSDKVideoResult;
import org.lasque.tusdk.video.editor.TuSDKMovieWriter;
import org.lasque.tusdk.video.editor.TuSDKMovieWriter.TuSDKMovieWriterDelegate;
import org.lasque.tusdk.video.editor.TuSDKMovieWriterInterface.MovieWriterOutputFormat;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

@SuppressLint({"InlinedApi"})
@TargetApi(16)
public class TuSDKMP4MovieMixer
  extends TuSDKMovieMixer
{
  private OnMP4MovieMixerDelegate a;
  private State b = State.Idle;
  private float c = 1.0F;
  private TuSDKMediaDataSource d;
  private String e;
  private TuSDKAudioEntry f;
  private boolean g = false;
  private boolean h = true;
  private AsyncVideoMixTask i;
  private MediaExtractor j;
  private TuSDKVideoInfo k;
  private TuSDKAudioInfo l;
  private TuSDKAudioMixer m = new TuSDKAverageAudioMixer();
  private MediaCodec n;
  private FileOutputStream o;
  private String p;
  private RandomAccessFile q;
  private List<TuSDKAudioEntry> r;
  private byte[] s = new byte['က'];
  private int t = 0;
  private long u = 0L;
  private long v = 0L;
  private TuSDKAudioMixer.OnAudioMixerDelegate w = new TuSDKAudioMixer.OnAudioMixerDelegate()
  {
    public void onMixed(byte[] paramAnonymousArrayOfByte)
    {
      try
      {
        if (TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this) != null) {
          TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this).write(paramAnonymousArrayOfByte);
        }
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    }
    
    public void onMixingError(int paramAnonymousInt)
    {
      try
      {
        if (TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this) != null) {
          TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this).close();
        }
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    }
    
    public void onReayTrunkTrackInfo(TuSDKAudioInfo paramAnonymousTuSDKAudioInfo)
    {
      TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this, paramAnonymousTuSDKAudioInfo);
    }
    
    public void onStateChanged(TuSDKAudioMixer.State paramAnonymousState)
    {
      if (paramAnonymousState == TuSDKAudioMixer.State.Decoding) {
        TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this, TuSDKMP4MovieMixer.State.Decoding);
      } else if (paramAnonymousState == TuSDKAudioMixer.State.Decoded) {
        TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this, TuSDKMP4MovieMixer.State.Decoded);
      } else if (paramAnonymousState == TuSDKAudioMixer.State.Mixing) {
        TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this, TuSDKMP4MovieMixer.State.Mixing);
      } else if (paramAnonymousState == TuSDKAudioMixer.State.Cancelled) {
        TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this, TuSDKMP4MovieMixer.State.Cancelled);
      } else if (paramAnonymousState == TuSDKAudioMixer.State.Complete) {
        try
        {
          if (TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this) != null) {
            TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this).close();
          }
          TuSDKMP4MovieMixer.b(TuSDKMP4MovieMixer.this);
        }
        catch (IOException localIOException)
        {
          localIOException.printStackTrace();
          TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this, TuSDKMP4MovieMixer.State.Failed);
        }
      }
    }
  };
  private TuSDKMovieWriter.TuSDKMovieWriterDelegate x = new TuSDKMovieWriter.TuSDKMovieWriterDelegate()
  {
    public void onFirstVideoSampleDataWrited(long paramAnonymousLong) {}
    
    public void onProgressChanged(float paramAnonymousFloat, long paramAnonymousLong)
    {
      if (TuSDKMP4MovieMixer.c(TuSDKMP4MovieMixer.this)) {
        return;
      }
      if ((TuSDKMP4MovieMixer.d(TuSDKMP4MovieMixer.this) == 0L) && (TuSDKMP4MovieMixer.e(TuSDKMP4MovieMixer.this) != null) && (TuSDKMP4MovieMixer.e(TuSDKMP4MovieMixer.this).validateTimeRange()) && (paramAnonymousFloat >= TuSDKMP4MovieMixer.e(TuSDKMP4MovieMixer.this).getTimeRange().getStartTime()))
      {
        TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this, paramAnonymousLong);
        TuSDKMP4MovieMixer.b(TuSDKMP4MovieMixer.this, paramAnonymousLong);
        TuSDKMP4MovieMixer.this.getMediaWriter().setWriteMuteAudioPlaceholderData(false);
      }
    }
  };
  
  public TuSDKMP4MovieMixer setVideoSoundVolume(float paramFloat)
  {
    if ((this.f != null) && (this.f.isTrunk())) {
      this.f.setVolume(paramFloat);
    }
    this.c = paramFloat;
    return this;
  }
  
  public TuSDKMP4MovieMixer setIgnoreTrunkAudioTimeRange(boolean paramBoolean)
  {
    this.g = paramBoolean;
    return this;
  }
  
  public TuSDKMP4MovieMixer setClearAudioDecodeCacheInfoOnCompleted(boolean paramBoolean)
  {
    this.h = paramBoolean;
    return this;
  }
  
  public void mix(TuSDKMediaDataSource paramTuSDKMediaDataSource, List<TuSDKAudioEntry> paramList, boolean paramBoolean)
  {
    if ((paramTuSDKMediaDataSource == null) || (!paramTuSDKMediaDataSource.isValid()))
    {
      TLog.e("%s : Please set a valid video file path", new Object[] { this });
      a(State.Failed);
      return;
    }
    if ((paramList == null) || (paramList.size() == 0))
    {
      TLog.e("%s : Please set a valid audio file path", new Object[] { this });
      a(State.Failed);
      return;
    }
    MediaFormat localMediaFormat = TuSDKMediaUtils.getVideoFormat(paramTuSDKMediaDataSource);
    if ((localMediaFormat == null) || (!a(localMediaFormat)))
    {
      a(State.Failed);
      a(ErrorCode.UnsupportedVideoFormat);
      if (localMediaFormat != null) {
        TLog.e("%s | The device does not support this video format : %s", new Object[] { this, localMediaFormat.getString("mime") });
      }
      return;
    }
    this.d = paramTuSDKMediaDataSource;
    this.r = new ArrayList();
    if (paramList != null) {
      this.r.addAll(paramList);
    }
    Iterator localIterator;
    TuSDKAudioEntry localTuSDKAudioEntry;
    if (paramBoolean)
    {
      localIterator = this.r.iterator();
      while (localIterator.hasNext())
      {
        localTuSDKAudioEntry = (TuSDKAudioEntry)localIterator.next();
        localTuSDKAudioEntry.setTrunk(false);
      }
      this.f = new TuSDKAudioEntry(paramTuSDKMediaDataSource);
      this.f.setTrunk(true);
      this.f.setVolume(this.c);
      this.r.add(this.f);
    }
    else
    {
      localIterator = this.r.iterator();
      while (localIterator.hasNext())
      {
        localTuSDKAudioEntry = (TuSDKAudioEntry)localIterator.next();
        if (localTuSDKAudioEntry.isTrunk()) {
          this.f = localTuSDKAudioEntry;
        }
      }
      if ((this.f == null) && (this.r.size() == 1)) {
        this.f = ((TuSDKAudioEntry)this.r.get(0));
      }
    }
    try
    {
      this.o = new FileOutputStream(b());
      this.q = new RandomAccessFile(b(), "rw");
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      TLog.e("%s : Please set a valid file path", new Object[] { this });
      localFileNotFoundException.printStackTrace();
      a(State.Failed);
      return;
    }
    getMediaWriter().setDelegate(this.x);
    a(this.r);
  }
  
  public void cancle()
  {
    if (this.m != null) {
      this.m.cancel();
    }
    if (this.i != null) {
      this.i.cancel();
    }
  }
  
  protected void onStopeed()
  {
    stopMovieWriter();
    if (this.n != null)
    {
      this.n.stop();
      this.n.release();
      this.n = null;
    }
    if (this.j != null)
    {
      this.j.release();
      this.j = null;
    }
    n();
  }
  
  private void a()
  {
    cancle();
    this.i = new AsyncVideoMixTask(null);
    this.i.execute(new Void[0]);
  }
  
  private void a(List<TuSDKAudioEntry> paramList)
  {
    this.m.setOnAudioMixDelegate(this.w);
    this.m.mixAudios(paramList);
  }
  
  public String getOutputFilePah()
  {
    if (this.e == null) {
      this.e = (TuSdk.getAppTempPath() + "/" + String.format("lsq_%s.mp4", new Object[] { StringHelper.timeStampString() }));
    }
    return this.e;
  }
  
  public TuSDKMP4MovieMixer setOutputFilePath(String paramString)
  {
    this.e = paramString;
    return this;
  }
  
  private String b()
  {
    if (this.p == null) {
      this.p = (TuSdk.getAppTempPath() + "/" + String.format("lsq_%s", new Object[] { StringHelper.timeStampString() }));
    }
    return this.p;
  }
  
  private long c()
  {
    return this.v - this.u;
  }
  
  private long d()
  {
    return this.k != null ? this.k.durationTimeUs : 0L;
  }
  
  private boolean e()
  {
    if (this.f == null) {
      return false;
    }
    if ((this.f.validateTimeRange()) && (!this.g))
    {
      float f1 = (float)Math.min(d() - this.f.getTimeRange().getStartTimeUS(), this.f.getTimeRange().durationTimeUS());
      return (float)c() < f1;
    }
    if (this.f.isLooping()) {
      return c() < d();
    }
    return false;
  }
  
  protected TuSDKMovieWriterInterface.MovieWriterOutputFormat getOutputFormat()
  {
    return TuSDKMovieWriterInterface.MovieWriterOutputFormat.MPEG_4;
  }
  
  private MediaFormat f()
  {
    MediaFormat localMediaFormat = null;
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    for (int i1 = 0; i1 != -2; i1 = k().dequeueOutputBuffer(localBufferInfo, 0L)) {}
    if (i1 == -2) {
      return k().getOutputFormat();
    }
    return localMediaFormat;
  }
  
  private MediaFormat g()
  {
    int i1 = findVideoTrack();
    if (i1 < 0) {
      return null;
    }
    return getMediaExtractor().getTrackFormat(i1);
  }
  
  public int findVideoTrack()
  {
    if (getMediaExtractor() == null) {
      return -1;
    }
    int i1 = getMediaExtractor().getTrackCount();
    for (int i2 = 0; i2 < i1; i2++)
    {
      MediaFormat localMediaFormat = getMediaExtractor().getTrackFormat(i2);
      String str = localMediaFormat.getString("mime");
      if (str.startsWith("video/")) {
        return i2;
      }
    }
    return -1;
  }
  
  private void h()
  {
    MediaExtractor localMediaExtractor = getMediaExtractor();
    if (localMediaExtractor == null)
    {
      onStopeed();
      a(State.Failed);
      TLog.d("%s : Please check the video file path", new Object[] { this });
      return;
    }
    MediaFormat localMediaFormat1 = g();
    if (localMediaFormat1 == null)
    {
      onStopeed();
      a(State.Failed);
      TLog.e("%s Invalid video format", new Object[] { this });
      return;
    }
    addVideoTrack(localMediaFormat1);
    this.k = TuSDKVideoInfo.createWithMediaFormat(localMediaFormat1, true);
    getMediaWriter().setOrientationHint(this.k.degree);
    MediaCodec localMediaCodec = k();
    localMediaCodec.start();
    MediaFormat localMediaFormat2 = f();
    addAudioTrack(localMediaFormat2);
    startMovieWriter();
    i();
    j();
    onStopeed();
  }
  
  private boolean a(MediaFormat paramMediaFormat)
  {
    int i1 = MediaCodecList.getCodecCount();
    for (int i2 = 0; i2 < i1; i2++)
    {
      MediaCodecInfo localMediaCodecInfo = MediaCodecList.getCodecInfoAt(i2);
      if (localMediaCodecInfo.isEncoder())
      {
        String[] arrayOfString = localMediaCodecInfo.getSupportedTypes();
        for (int i3 = 0; i3 < arrayOfString.length; i3++) {
          if (arrayOfString[i3].equalsIgnoreCase(paramMediaFormat.getString("mime"))) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  private void i()
  {
    MediaExtractor localMediaExtractor = getMediaExtractor();
    MediaFormat localMediaFormat = g();
    int i1 = 0;
    TuSDKMediaUtils.getAndSelectVideoTrackIndex(getMediaExtractor());
    ByteBuffer localByteBuffer = ByteBuffer.allocateDirect(localMediaFormat.getInteger("width") * localMediaFormat.getInteger("height"));
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    if ((!this.g) && (this.f != null) && (this.f.validateTimeRange())) {
      getMediaWriter().setWriteMuteAudioPlaceholderData(true);
    }
    while ((i1 == 0) && (this.b == State.Mixing))
    {
      localByteBuffer.clear();
      int i2 = localMediaExtractor.readSampleData(localByteBuffer, 0);
      if (i2 < 0)
      {
        i1 = 1;
      }
      else
      {
        localBufferInfo.presentationTimeUs = localMediaExtractor.getSampleTime();
        localBufferInfo.flags = localMediaExtractor.getSampleFlags();
        localBufferInfo.size = i2;
        localByteBuffer.limit(i2);
        writeVideoSampleData(localByteBuffer, localBufferInfo);
        localMediaExtractor.advance();
      }
    }
  }
  
  private void j()
  {
    if (!getMediaWriter().hasAudioTrack()) {
      return;
    }
    boolean bool1 = false;
    for (boolean bool2 = false; (!bool2) && (this.b == State.Mixing); bool2 = m()) {
      if (!bool1) {
        bool1 = l();
      }
    }
  }
  
  private MediaCodec k()
  {
    if (this.n == null) {
      try
      {
        this.n = q();
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    }
    return this.n;
  }
  
  private boolean l()
  {
    ByteBuffer[] arrayOfByteBuffer = this.n.getInputBuffers();
    int i1 = this.n.dequeueInputBuffer(500L);
    if (i1 >= 0)
    {
      ByteBuffer localByteBuffer = arrayOfByteBuffer[i1];
      localByteBuffer.clear();
      try
      {
        if (this.q.read(this.s) == -1)
        {
          if (!e())
          {
            this.n.queueInputBuffer(i1, 0, 0, 0L, 4);
            return true;
          }
          this.q.seek(0L);
          this.q.read(this.s);
        }
        localByteBuffer.put(this.s);
        this.t += this.s.length;
        long l1 = p().frameTimeUsWithAudioSize(this.t);
        this.n.queueInputBuffer(i1, 0, this.s.length, l1, 0);
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    }
    return false;
  }
  
  private boolean m()
  {
    ByteBuffer[] arrayOfByteBuffer = this.n.getOutputBuffers();
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    int i1 = this.n.dequeueOutputBuffer(localBufferInfo, 500L);
    if (i1 >= 0)
    {
      if ((localBufferInfo.flags & 0x2) != 0)
      {
        this.n.releaseOutputBuffer(i1, false);
        return false;
      }
      if (localBufferInfo.size != 0)
      {
        ByteBuffer localByteBuffer = arrayOfByteBuffer[i1];
        localByteBuffer.position(localBufferInfo.offset);
        localByteBuffer.limit(localBufferInfo.offset + localBufferInfo.size);
        if (this.u == 0L) {
          this.u = localBufferInfo.presentationTimeUs;
        }
        if (this.v == 0L) {
          this.v = this.u;
        }
        this.v += p().getFrameInterval();
        localBufferInfo.presentationTimeUs = this.v;
        writeAudioSampleData(localByteBuffer, localBufferInfo);
      }
      this.n.releaseOutputBuffer(i1, false);
      if ((localBufferInfo.flags & 0x4) != 0) {
        return true;
      }
      if (c() >= d()) {
        return true;
      }
      if ((this.f != null) && (((this.f.validateTimeRange()) && (!this.g)) || ((this.f.isLooping()) && (!e())))) {
        return true;
      }
    }
    else if (i1 == -3)
    {
      arrayOfByteBuffer = this.n.getOutputBuffers();
    }
    else if (i1 != -2) {}
    return false;
  }
  
  public MediaExtractor getMediaExtractor()
  {
    if (this.j != null) {
      return this.j;
    }
    this.j = new MediaExtractor();
    try
    {
      if (!TextUtils.isEmpty(this.d.getFilePath())) {
        this.j.setDataSource(this.d.getFilePath());
      } else {
        this.j.setDataSource(TuSdkContext.context(), this.d.getFileUri(), null);
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      this.j = null;
    }
    return this.j;
  }
  
  private void a(State paramState)
  {
    if (this.b == paramState) {
      return;
    }
    this.b = paramState;
    if (this.a != null) {
      this.a.onStateChanged(paramState);
    }
  }
  
  private void a(ErrorCode paramErrorCode)
  {
    if (this.a != null) {
      this.a.onErrrCode(paramErrorCode);
    }
  }
  
  private void a(TuSDKVideoResult paramTuSDKVideoResult)
  {
    if (this.a == null) {
      return;
    }
    this.a.onMixerComplete(paramTuSDKVideoResult);
    StatisticsManger.appendComponent(9449474L);
  }
  
  private void n()
  {
    if (this.p != null) {
      new File(this.p).delete();
    }
    if (this.h) {
      this.m.clearDecodeCahceInfo();
    }
  }
  
  private TuSDKVideoResult o()
  {
    if (this.k == null) {
      return null;
    }
    TuSDKVideoResult localTuSDKVideoResult = new TuSDKVideoResult();
    localTuSDKVideoResult.videoPath = new File(getOutputFilePah());
    localTuSDKVideoResult.duration = ((int)(this.k.durationTimeUs / 1000000L));
    localTuSDKVideoResult.videoInfo = this.k;
    return localTuSDKVideoResult;
  }
  
  public TuSDKMP4MovieMixer setDelegate(OnMP4MovieMixerDelegate paramOnMP4MovieMixerDelegate)
  {
    this.a = paramOnMP4MovieMixerDelegate;
    return this;
  }
  
  public OnMP4MovieMixerDelegate getDelegate()
  {
    return this.a;
  }
  
  private TuSDKAudioInfo p()
  {
    if (this.l == null) {
      this.l = TuSDKAudioInfo.defaultAudioInfo();
    }
    return this.l;
  }
  
  private MediaCodec q()
  {
    MediaCodec localMediaCodec = MediaCodec.createEncoderByType("audio/mp4a-latm");
    MediaFormat localMediaFormat = new MediaFormat();
    localMediaFormat.setString("mime", "audio/mp4a-latm");
    localMediaFormat.setInteger("bitrate", p().bitrate);
    localMediaFormat.setInteger("channel-count", p().channel);
    localMediaFormat.setInteger("sample-rate", p().sampleRate);
    localMediaFormat.setInteger("aac-profile", 2);
    localMediaCodec.configure(localMediaFormat, null, null, 1);
    return localMediaCodec;
  }
  
  private class AsyncVideoMixTask
    extends AsyncTask<Void, Double, Void>
  {
    private AsyncVideoMixTask() {}
    
    protected Void doInBackground(Void... paramVarArgs)
    {
      TuSDKMP4MovieMixer.f(TuSDKMP4MovieMixer.this);
      return null;
    }
    
    protected void onPreExecute()
    {
      super.onPreExecute();
      TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this, TuSDKMP4MovieMixer.State.Mixing);
    }
    
    protected void onPostExecute(Void paramVoid)
    {
      super.onPostExecute(paramVoid);
      TuSDKMP4MovieMixer.this.onStopeed();
      TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this, TuSDKMP4MovieMixer.g(TuSDKMP4MovieMixer.this));
    }
    
    public void cancel()
    {
      TuSDKMP4MovieMixer.b(TuSDKMP4MovieMixer.this, TuSDKMP4MovieMixer.State.Cancelled);
      cancel(true);
    }
    
    protected void onCancelled(Void paramVoid)
    {
      super.onCancelled(paramVoid);
      TuSDKMP4MovieMixer.this.onStopeed();
      TuSDKMP4MovieMixer.a(TuSDKMP4MovieMixer.this, TuSDKMP4MovieMixer.State.Cancelled);
    }
  }
  
  public static abstract interface OnMP4MovieMixerDelegate
  {
    public abstract void onStateChanged(TuSDKMP4MovieMixer.State paramState);
    
    public abstract void onErrrCode(TuSDKMP4MovieMixer.ErrorCode paramErrorCode);
    
    public abstract void onMixerComplete(TuSDKVideoResult paramTuSDKVideoResult);
  }
  
  public static enum ErrorCode
  {
    private ErrorCode() {}
  }
  
  public static enum State
  {
    private State() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\movie\preproc\mixer\TuSDKMP4MovieMixer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */