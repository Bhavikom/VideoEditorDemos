package org.lasque.tusdk.core.decoder;

import android.media.MediaCodec.BufferInfo;
import android.os.AsyncTask;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.lasque.tusdk.api.audio.postproc.resample.TuSDKAudioResampler;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioEntry;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.utils.StringHelper;

public class TuSDKAudioDecoderTaskManagaer
{
  private Set<AsyncDecoderTask> a = new HashSet(3);
  private List<TuSDKAudioEntry> b;
  private TuSDKAudioDecoderTaskMangaerDelegate c;
  
  public void setAudioEntry(List<TuSDKAudioEntry> paramList)
  {
    this.b = paramList;
  }
  
  public void setDelegate(TuSDKAudioDecoderTaskMangaerDelegate paramTuSDKAudioDecoderTaskMangaerDelegate)
  {
    this.c = paramTuSDKAudioDecoderTaskMangaerDelegate;
  }
  
  public void start()
  {
    this.a.clear();
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      TuSDKAudioEntry localTuSDKAudioEntry = (TuSDKAudioEntry)localIterator.next();
      AsyncDecoderTask localAsyncDecoderTask = new AsyncDecoderTask(localTuSDKAudioEntry);
      localAsyncDecoderTask.execute(new Void[0]);
      this.a.add(localAsyncDecoderTask);
    }
  }
  
  public void cancel()
  {
    if ((this.a == null) || (this.a.size() == 0)) {
      return;
    }
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      AsyncDecoderTask localAsyncDecoderTask = (AsyncDecoderTask)localIterator.next();
      localAsyncDecoderTask.cancel();
    }
    this.a.clear();
  }
  
  private String a(TuSDKAudioEntry paramTuSDKAudioEntry, int paramInt1, int paramInt2)
  {
    String str1 = "sample=" + paramInt1 + "channel=" + paramInt2;
    String str2 = paramTuSDKAudioEntry.getFingerprint(str1);
    return TuSdk.getAppTempPath() + "/" + StringHelper.md5(str2);
  }
  
  private boolean b(TuSDKAudioEntry paramTuSDKAudioEntry, int paramInt1, int paramInt2)
  {
    String str = a(paramTuSDKAudioEntry, paramInt1, paramInt2);
    if (str == null) {
      return false;
    }
    File localFile = new File(str);
    if ((localFile.exists()) && (localFile.canRead()) && (localFile.isFile()))
    {
      if (localFile.length() == 0L)
      {
        localFile.delete();
        return false;
      }
      paramTuSDKAudioEntry.getRawInfo().setFilePath(str);
      paramTuSDKAudioEntry.getRawInfo().sampleRate = paramInt1;
      paramTuSDKAudioEntry.getRawInfo().channel = paramInt2;
      return true;
    }
    return false;
  }
  
  private TuSDKAudioEntry a()
  {
    if ((this.b == null) || (this.b.size() == 0)) {
      return null;
    }
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      TuSDKAudioEntry localTuSDKAudioEntry = (TuSDKAudioEntry)localIterator.next();
      if ((localTuSDKAudioEntry != null) && (localTuSDKAudioEntry.isTrunk())) {
        return localTuSDKAudioEntry;
      }
    }
    return (TuSDKAudioEntry)this.b.get(0);
  }
  
  private boolean a(TuSDKAudioEntry paramTuSDKAudioEntry1, TuSDKAudioEntry paramTuSDKAudioEntry2)
  {
    if ((paramTuSDKAudioEntry1 == null) || (paramTuSDKAudioEntry1.getRawInfo() == null) || (paramTuSDKAudioEntry2 == null) || (paramTuSDKAudioEntry2.getRawInfo() == null)) {
      return false;
    }
    TuSDKAudioInfo localTuSDKAudioInfo1 = paramTuSDKAudioEntry1.getRawInfo();
    TuSDKAudioInfo localTuSDKAudioInfo2 = paramTuSDKAudioEntry2.getRawInfo();
    if ((localTuSDKAudioInfo1 == null) || (localTuSDKAudioInfo2 == null)) {
      return false;
    }
    return (localTuSDKAudioInfo1.sampleRate != localTuSDKAudioInfo2.sampleRate) || (localTuSDKAudioInfo1.channel != localTuSDKAudioInfo2.channel);
  }
  
  private class AsyncDecoderTask
    extends AsyncTask<Void, Double, TuSDKAudioEntry>
    implements TuSDKAudioDecoder.OnAudioDecoderDelegate
  {
    private TuSDKAudioEntry b;
    private TuSDKAudioDecoder c;
    
    public AsyncDecoderTask(TuSDKAudioEntry paramTuSDKAudioEntry)
    {
      this.b = paramTuSDKAudioEntry;
    }
    
    protected TuSDKAudioEntry doInBackground(Void... paramVarArgs)
    {
      if ((this.b.getRawInfo() != null) && (this.b.getRawInfo().isValid())) {
        return this.b;
      }
      TuSDKAudioEntry localTuSDKAudioEntry = TuSDKAudioDecoderTaskManagaer.a(TuSDKAudioDecoderTaskManagaer.this);
      TuSDKAudioInfo localTuSDKAudioInfo = localTuSDKAudioEntry.getRawInfo() == null ? this.b.getRawInfo() : localTuSDKAudioEntry.getRawInfo();
      if (localTuSDKAudioInfo == null) {
        return this.b;
      }
      if (TuSDKAudioDecoderTaskManagaer.a(TuSDKAudioDecoderTaskManagaer.this, this.b, localTuSDKAudioInfo.sampleRate, localTuSDKAudioInfo.channel)) {
        return this.b;
      }
      String str1 = TuSDKAudioDecoderTaskManagaer.b(TuSDKAudioDecoderTaskManagaer.this, this.b, localTuSDKAudioInfo.sampleRate, localTuSDKAudioInfo.channel);
      if (TuSDKAudioDecoderTaskManagaer.a(TuSDKAudioDecoderTaskManagaer.this, this.b, localTuSDKAudioEntry))
      {
        TuSDKAudioResampler localTuSDKAudioResampler = new TuSDKAudioResampler();
        String str2 = TuSdk.getAppTempPath() + "/" + System.currentTimeMillis();
        boolean bool = localTuSDKAudioResampler.process(this.b, new File(str2), localTuSDKAudioEntry.getRawInfo().sampleRate, localTuSDKAudioEntry.getRawInfo().channel);
        if (bool)
        {
          this.b.getRawInfo().sampleRate = localTuSDKAudioInfo.sampleRate;
          this.b.getRawInfo().channel = localTuSDKAudioInfo.channel;
          this.b.setFilePath(str2);
        }
      }
      this.c = new TuSDKAudioDecoder(this.b, str1);
      this.c.setDecodeTimeRange(this.b.getCutTimeRange());
      this.c.setDelegate(this);
      this.c.start();
      return this.b;
    }
    
    protected void onPreExecute()
    {
      super.onPreExecute();
      if (TuSDKAudioDecoderTaskManagaer.b(TuSDKAudioDecoderTaskManagaer.this) != null) {
        TuSDKAudioDecoderTaskManagaer.b(TuSDKAudioDecoderTaskManagaer.this).onStateChanged(TuSDKAudioDecoderTaskManagaer.State.Decoding);
      }
    }
    
    public void cancel()
    {
      if (isCancelled()) {
        return;
      }
      if (this.c != null) {
        this.c.stop();
      }
      cancel(true);
    }
    
    protected void onPostExecute(TuSDKAudioEntry paramTuSDKAudioEntry)
    {
      super.onPostExecute(paramTuSDKAudioEntry);
      TuSDKAudioDecoderTaskManagaer.c(TuSDKAudioDecoderTaskManagaer.this).remove(this);
      if ((TuSDKAudioDecoderTaskManagaer.b(TuSDKAudioDecoderTaskManagaer.this) != null) && (TuSDKAudioDecoderTaskManagaer.c(TuSDKAudioDecoderTaskManagaer.this).size() == 0)) {
        TuSDKAudioDecoderTaskManagaer.b(TuSDKAudioDecoderTaskManagaer.this).onStateChanged(TuSDKAudioDecoderTaskManagaer.State.Complete);
      }
    }
    
    protected void onCancelled(TuSDKAudioEntry paramTuSDKAudioEntry)
    {
      super.onCancelled(paramTuSDKAudioEntry);
      TuSDKAudioDecoderTaskManagaer.c(TuSDKAudioDecoderTaskManagaer.this).remove(this);
      if ((TuSDKAudioDecoderTaskManagaer.b(TuSDKAudioDecoderTaskManagaer.this) != null) && (TuSDKAudioDecoderTaskManagaer.c(TuSDKAudioDecoderTaskManagaer.this).size() == 0)) {
        TuSDKAudioDecoderTaskManagaer.b(TuSDKAudioDecoderTaskManagaer.this).onStateChanged(TuSDKAudioDecoderTaskManagaer.State.Cancelled);
      }
    }
    
    protected void onCancelled()
    {
      super.onCancelled();
      TuSDKAudioDecoderTaskManagaer.c(TuSDKAudioDecoderTaskManagaer.this).remove(this);
      if ((TuSDKAudioDecoderTaskManagaer.b(TuSDKAudioDecoderTaskManagaer.this) != null) && (TuSDKAudioDecoderTaskManagaer.c(TuSDKAudioDecoderTaskManagaer.this).size() == 0)) {
        TuSDKAudioDecoderTaskManagaer.b(TuSDKAudioDecoderTaskManagaer.this).onStateChanged(TuSDKAudioDecoderTaskManagaer.State.Cancelled);
      }
    }
    
    public void onDecodeRawInfo(TuSDKAudioInfo paramTuSDKAudioInfo)
    {
      this.b.setRawInfo(paramTuSDKAudioInfo);
    }
    
    public void onDecode(byte[] paramArrayOfByte, MediaCodec.BufferInfo paramBufferInfo, double paramDouble) {}
    
    public void onDecoderErrorCode(TuSDKMediaDecoder.TuSDKMediaDecoderError paramTuSDKMediaDecoderError) {}
  }
  
  public static abstract interface TuSDKAudioDecoderTaskMangaerDelegate
  {
    public abstract void onStateChanged(TuSDKAudioDecoderTaskManagaer.State paramState);
  }
  
  public static enum State
  {
    private State() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKAudioDecoderTaskManagaer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */