package org.lasque.tusdk.api.audio.preproc.mixer;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.decoder.TuSDKAudioInfo;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.video.editor.TuSDKMovieWriter;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

@TargetApi(16)
public class TuSDKAudioPacketMixer
  extends TuSDKAverageAudioMixer
{
  protected MediaCodec mAudioEncoder;
  private TuSDKAudioMixPacketDelegate a;
  private FileOutputStream b;
  private String c;
  private RandomAccessFile d;
  private TuSDKAudioInfo e;
  private long f;
  private byte[] g = new byte['က'];
  private int h = 0;
  private long i = 0L;
  private long j = 0L;
  private TuSDKAudioEntry k;
  private AsyncVideoMixTask l;
  private boolean m = false;
  private TuSDKAudioMixer.OnAudioMixerDelegate n = new TuSDKAudioMixer.OnAudioMixerDelegate()
  {
    public void onMixed(byte[] paramAnonymousArrayOfByte)
    {
      try
      {
        if (TuSDKAudioPacketMixer.a(TuSDKAudioPacketMixer.this) != null) {
          TuSDKAudioPacketMixer.a(TuSDKAudioPacketMixer.this).write(paramAnonymousArrayOfByte);
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
        if (TuSDKAudioPacketMixer.a(TuSDKAudioPacketMixer.this) != null) {
          TuSDKAudioPacketMixer.a(TuSDKAudioPacketMixer.this).close();
        }
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    }
    
    public void onReayTrunkTrackInfo(TuSDKAudioInfo paramAnonymousTuSDKAudioInfo)
    {
      TuSDKAudioPacketMixer.a(TuSDKAudioPacketMixer.this, paramAnonymousTuSDKAudioInfo);
    }
    
    public void onStateChanged(TuSDKAudioMixer.State paramAnonymousState)
    {
      if ((paramAnonymousState != TuSDKAudioMixer.State.Decoding) && (paramAnonymousState != TuSDKAudioMixer.State.Decoded) && (paramAnonymousState != TuSDKAudioMixer.State.Mixing) && (paramAnonymousState != TuSDKAudioMixer.State.Cancelled) && (paramAnonymousState == TuSDKAudioMixer.State.Complete)) {
        try
        {
          if (TuSDKAudioPacketMixer.a(TuSDKAudioPacketMixer.this) != null) {
            TuSDKAudioPacketMixer.a(TuSDKAudioPacketMixer.this).close();
          }
          TuSDKAudioPacketMixer.b(TuSDKAudioPacketMixer.this);
        }
        catch (IOException localIOException)
        {
          localIOException.printStackTrace();
        }
      }
    }
  };
  
  public void setFirstAudioSampleTimeUs(long paramLong)
  {
    this.i = paramLong;
    this.j = paramLong;
  }
  
  public void prepare(List<TuSDKAudioEntry> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0)) {
      return;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TuSDKAudioEntry localTuSDKAudioEntry = (TuSDKAudioEntry)localIterator.next();
      if (localTuSDKAudioEntry.isTrunk()) {
        this.k = localTuSDKAudioEntry;
      }
    }
    if ((this.k == null) && (paramList.size() == 1)) {
      this.k = ((TuSDKAudioEntry)paramList.get(0));
    }
    this.m = (paramList.size() > 1);
    if (this.a != null) {
      this.a.onOutputAudioFormat(e());
    }
  }
  
  public void setAudioDataDelegate(TuSDKAudioMixPacketDelegate paramTuSDKAudioMixPacketDelegate)
  {
    this.a = paramTuSDKAudioMixPacketDelegate;
  }
  
  public void mixAudios(List<TuSDKAudioEntry> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0)) {
      return;
    }
    setOnAudioMixDelegate(this.n);
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TuSDKAudioEntry localTuSDKAudioEntry = (TuSDKAudioEntry)localIterator.next();
      if (localTuSDKAudioEntry.isTrunk()) {
        this.k = localTuSDKAudioEntry;
      }
    }
    if ((this.k == null) && (paramList.size() == 1)) {
      this.k = ((TuSDKAudioEntry)paramList.get(0));
    }
    try
    {
      this.b = new FileOutputStream(a());
      this.d = new RandomAccessFile(a(), "rw");
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      TLog.e("%s : Please set a valid file path", new Object[] { this });
      localFileNotFoundException.printStackTrace();
      return;
    }
    super.mixAudios(paramList);
  }
  
  private String a()
  {
    if (this.c == null) {
      this.c = (TuSdk.getAppTempPath() + "/" + String.format("lsq_%s", new Object[] { StringHelper.timeStampString() }));
    }
    return this.c;
  }
  
  private TuSDKAudioInfo b()
  {
    if ((this.e == null) && (this.k != null)) {
      this.e = this.k.getRawInfo();
    }
    if (this.e == null) {
      this.e = TuSDKAudioInfo.defaultAudioInfo();
    }
    return this.e;
  }
  
  private MediaCodec c()
  {
    if (this.mAudioEncoder == null) {
      try
      {
        this.mAudioEncoder = d();
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    }
    return this.mAudioEncoder;
  }
  
  private MediaCodec d()
  {
    MediaCodec localMediaCodec = MediaCodec.createEncoderByType("audio/mp4a-latm");
    MediaFormat localMediaFormat = new MediaFormat();
    localMediaFormat.setString("mime", "audio/mp4a-latm");
    localMediaFormat.setInteger("bitrate", b().bitrate);
    localMediaFormat.setInteger("channel-count", b().channel);
    localMediaFormat.setInteger("sample-rate", b().sampleRate);
    localMediaFormat.setInteger("aac-profile", 2);
    localMediaCodec.configure(localMediaFormat, null, null, 1);
    return localMediaCodec;
  }
  
  private MediaFormat e()
  {
    MediaCodec localMediaCodec = c();
    localMediaCodec.start();
    MediaFormat localMediaFormat = null;
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    for (int i1 = 0; i1 != -2; i1 = c().dequeueOutputBuffer(localBufferInfo, 0L)) {}
    if (i1 == -2) {
      localMediaFormat = c().getOutputFormat();
    }
    if (i1 >= 0) {
      c().releaseOutputBuffer(i1, false);
    }
    this.mAudioEncoder = null;
    return localMediaFormat;
  }
  
  private long f()
  {
    if (this.f > 0L) {
      return this.f;
    }
    return this.e.durationTimeUs;
  }
  
  public void setMaxDurationTimeUs(long paramLong)
  {
    this.f = paramLong;
  }
  
  private boolean g()
  {
    if (this.k == null) {
      return false;
    }
    if ((this.k.validateTimeRange()) && (!this.m))
    {
      float f1 = (float)Math.min(f() - this.k.getTimeRange().getStartTimeUS(), this.k.getTimeRange().durationTimeUS());
      return (float)this.j < f1;
    }
    if (this.k.isLooping()) {
      return this.j < f();
    }
    return false;
  }
  
  private long h()
  {
    return this.j - this.i;
  }
  
  private void i()
  {
    MediaCodec localMediaCodec = c();
    localMediaCodec.start();
    boolean bool1 = false;
    for (boolean bool2 = false; !bool2; bool2 = k()) {
      if (!bool1) {
        bool1 = j();
      }
    }
  }
  
  private boolean j()
  {
    ByteBuffer[] arrayOfByteBuffer = this.mAudioEncoder.getInputBuffers();
    int i1 = this.mAudioEncoder.dequeueInputBuffer(500L);
    if (i1 >= 0)
    {
      ByteBuffer localByteBuffer = arrayOfByteBuffer[i1];
      localByteBuffer.clear();
      try
      {
        if (this.d.read(this.g) == -1)
        {
          if (!g())
          {
            this.mAudioEncoder.queueInputBuffer(i1, 0, 0, 0L, 4);
            return true;
          }
          this.d.seek(0L);
          this.d.read(this.g);
        }
        localByteBuffer.put(this.g);
        this.h += this.g.length;
        long l1 = b().frameTimeUsWithAudioSize(this.h);
        this.mAudioEncoder.queueInputBuffer(i1, 0, this.g.length, l1, 0);
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    }
    return false;
  }
  
  private boolean k()
  {
    ByteBuffer[] arrayOfByteBuffer = this.mAudioEncoder.getOutputBuffers();
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    int i1 = this.mAudioEncoder.dequeueOutputBuffer(localBufferInfo, 500L);
    if (i1 >= 0)
    {
      if ((localBufferInfo.flags & 0x2) != 0)
      {
        this.mAudioEncoder.releaseOutputBuffer(i1, false);
        return false;
      }
      if (localBufferInfo.size != 0)
      {
        ByteBuffer localByteBuffer = arrayOfByteBuffer[i1];
        localByteBuffer.position(localBufferInfo.offset);
        localByteBuffer.limit(localBufferInfo.offset + localBufferInfo.size);
        this.j += b().getFrameInterval();
        localBufferInfo.presentationTimeUs = this.j;
        if (this.i == 0L) {
          this.i = localBufferInfo.presentationTimeUs;
        }
        this.a.onAudioPacketAvailable(localBufferInfo.presentationTimeUs, localByteBuffer, localBufferInfo);
      }
      this.mAudioEncoder.releaseOutputBuffer(i1, false);
      if ((localBufferInfo.flags & 0x4) != 0) {
        return true;
      }
      if (h() >= f()) {
        return true;
      }
      if ((this.k != null) && (((this.k.validateTimeRange()) && (!this.m)) || ((this.k.isLooping()) && (!g())))) {
        return true;
      }
    }
    else if (i1 == -3)
    {
      arrayOfByteBuffer = this.mAudioEncoder.getOutputBuffers();
    }
    else if (i1 == -2)
    {
      l();
    }
    return false;
  }
  
  private void l()
  {
    if ((this.k == null) || (this.k.getTimeRange() == null) || (this.m)) {
      return;
    }
    while (h() < this.k.getTimeRange().getStartTimeUS())
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(TuSDKMovieWriter.AAC_MUTE_BYTES);
      MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
      localBufferInfo.presentationTimeUs = this.j;
      localBufferInfo.size = TuSDKMovieWriter.AAC_MUTE_BYTES.length;
      localBufferInfo.offset = 0;
      this.a.onAudioPacketAvailable(this.j, localByteBuffer, localBufferInfo);
      this.j += b().getFrameInterval();
      if (this.i == 0L) {
        this.i = this.j;
      }
    }
  }
  
  public boolean isPackaging()
  {
    if (getState() == TuSDKAudioMixer.State.Cancelled) {
      return false;
    }
    if (getState() != TuSDKAudioMixer.State.Complete) {
      return true;
    }
    return (this.l != null) && (this.l.getStatus() == AsyncTask.Status.RUNNING);
  }
  
  private void m()
  {
    this.l = new AsyncVideoMixTask(null);
    this.l.execute(new Void[0]);
  }
  
  public void cancel()
  {
    super.cancel();
    if (isPackaging()) {
      this.l.cancel();
    }
  }
  
  private class AsyncVideoMixTask
    extends AsyncTask<Void, Double, Void>
  {
    private AsyncVideoMixTask() {}
    
    protected Void doInBackground(Void... paramVarArgs)
    {
      TuSDKAudioPacketMixer.c(TuSDKAudioPacketMixer.this);
      return null;
    }
    
    protected void onPreExecute()
    {
      super.onPreExecute();
    }
    
    protected void onPostExecute(Void paramVoid)
    {
      super.onPostExecute(paramVoid);
      if (TuSDKAudioPacketMixer.this.mAudioEncoder != null)
      {
        TuSDKAudioPacketMixer.this.mAudioEncoder.stop();
        TuSDKAudioPacketMixer.this.mAudioEncoder.release();
        TuSDKAudioPacketMixer.this.mAudioEncoder = null;
      }
      if (TuSDKAudioPacketMixer.d(TuSDKAudioPacketMixer.this) != null) {
        TuSDKAudioPacketMixer.d(TuSDKAudioPacketMixer.this).onCompleted();
      }
    }
    
    public void cancel()
    {
      cancel(true);
    }
    
    protected void onCancelled(Void paramVoid)
    {
      super.onCancelled(paramVoid);
      if (TuSDKAudioPacketMixer.this.mAudioEncoder != null)
      {
        TuSDKAudioPacketMixer.this.mAudioEncoder.stop();
        TuSDKAudioPacketMixer.this.mAudioEncoder.release();
        TuSDKAudioPacketMixer.this.mAudioEncoder = null;
      }
    }
  }
  
  public static abstract interface TuSDKAudioMixPacketDelegate
  {
    public abstract void onOutputAudioFormat(MediaFormat paramMediaFormat);
    
    public abstract void onAudioPacketAvailable(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
    
    public abstract void onCompleted();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\audio\preproc\mixer\TuSDKAudioPacketMixer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */