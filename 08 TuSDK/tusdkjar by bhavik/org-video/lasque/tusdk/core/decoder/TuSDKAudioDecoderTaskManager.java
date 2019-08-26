package org.lasque.tusdk.core.decoder;

import android.media.MediaCodec.BufferInfo;
import android.os.AsyncTask;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderEntry;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderInfoWrap;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkDecoderListener;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.ThreadHelper;

public class TuSDKAudioDecoderTaskManager
{
  private Set<_AsyncDecoderTask> a = new HashSet(3);
  private List<TuSDKAudioRenderEntry> b;
  private TuSdkAudioInfo c;
  private TuSDKAudioDecoderTaskStateListener d;
  private State e = State.Idle;
  
  public void setAudioEntry(List<TuSDKAudioRenderEntry> paramList)
  {
    this.b = paramList;
  }
  
  public void setDelegate(TuSDKAudioDecoderTaskStateListener paramTuSDKAudioDecoderTaskStateListener)
  {
    this.d = paramTuSDKAudioDecoderTaskStateListener;
  }
  
  public void setTrunkAudioInfo(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    this.c = paramTuSdkAudioInfo;
  }
  
  private void a(final State paramState)
  {
    if ((this.d == null) || (this.e == paramState)) {
      return;
    }
    this.e = paramState;
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuSDKAudioDecoderTaskManager.a(TuSDKAudioDecoderTaskManager.this).onStateChanged(paramState);
      }
    });
  }
  
  public State getState()
  {
    return this.e;
  }
  
  public void start()
  {
    this.a.clear();
    if ((this.b == null) || (this.b.size() == 0)) {
      a(State.Complete);
    }
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      TuSDKAudioRenderEntry localTuSDKAudioRenderEntry = (TuSDKAudioRenderEntry)localIterator.next();
      _AsyncDecoderTask local_AsyncDecoderTask = new _AsyncDecoderTask(localTuSDKAudioRenderEntry);
      local_AsyncDecoderTask.execute(new Void[0]);
      this.a.add(local_AsyncDecoderTask);
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
      _AsyncDecoderTask local_AsyncDecoderTask = (_AsyncDecoderTask)localIterator.next();
      local_AsyncDecoderTask.cancel();
    }
    this.a.clear();
  }
  
  private TuSdkAudioInfo a()
  {
    if (this.c != null) {
      return this.c;
    }
    if ((this.b == null) || (this.b.size() == 0)) {
      return null;
    }
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      TuSDKAudioRenderEntry localTuSDKAudioRenderEntry = (TuSDKAudioRenderEntry)localIterator.next();
      if ((localTuSDKAudioRenderEntry != null) && (localTuSDKAudioRenderEntry.isTrunk())) {
        return localTuSDKAudioRenderEntry.getRawInfo().getRealAudioInfo();
      }
    }
    return ((TuSDKAudioRenderEntry)this.b.get(0)).getRawInfo().getRealAudioInfo();
  }
  
  private String a(TuSDKAudioRenderEntry paramTuSDKAudioRenderEntry, int paramInt1, int paramInt2)
  {
    String str1 = "sample=" + paramInt1 + "channel=" + paramInt2;
    String str2 = paramTuSDKAudioRenderEntry.getFingerprint(str1);
    return TuSdk.getAppTempPath() + "/" + StringHelper.md5(str2);
  }
  
  private boolean b(TuSDKAudioRenderEntry paramTuSDKAudioRenderEntry, int paramInt1, int paramInt2)
  {
    String str = a(paramTuSDKAudioRenderEntry, paramInt1, paramInt2);
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
      paramTuSDKAudioRenderEntry.getRawInfo().setPath(str);
      paramTuSDKAudioRenderEntry.getRawInfo().getRealAudioInfo().sampleRate = paramInt1;
      paramTuSDKAudioRenderEntry.getRawInfo().getRealAudioInfo().channelCount = paramInt2;
      return true;
    }
    return false;
  }
  
  private class _AsyncDecoderTask
    extends AsyncTask<Void, Double, TuSDKAudioRenderEntry>
    implements TuSdkDecoderListener
  {
    private TuSDKAudioRenderEntry b;
    private TuSDKAudioRenderDecoder c;
    private String d;
    private volatile boolean e = false;
    private boolean f = false;
    
    public _AsyncDecoderTask(TuSDKAudioRenderEntry paramTuSDKAudioRenderEntry)
    {
      this.b = paramTuSDKAudioRenderEntry;
    }
    
    protected TuSDKAudioRenderEntry doInBackground(Void... paramVarArgs)
    {
      if ((this.b.getRawInfo() != null) && (this.b.getRawInfo().getMediaDataType() != null) && (this.b.getRawInfo().isValid())) {
        return this.b;
      }
      TuSdkAudioInfo localTuSdkAudioInfo1 = TuSDKAudioDecoderTaskManager.b(TuSDKAudioDecoderTaskManager.this);
      TuSdkAudioInfo localTuSdkAudioInfo2 = localTuSdkAudioInfo1 == null ? this.b.getRawInfo().getRealAudioInfo() : localTuSdkAudioInfo1;
      if (localTuSdkAudioInfo2 == null)
      {
        this.e = false;
        return this.b;
      }
      if (TuSDKAudioDecoderTaskManager.a(TuSDKAudioDecoderTaskManager.this, this.b, localTuSdkAudioInfo2.sampleRate, localTuSdkAudioInfo2.channelCount))
      {
        this.e = false;
        return this.b;
      }
      this.d = TuSDKAudioDecoderTaskManager.b(TuSDKAudioDecoderTaskManager.this, this.b, localTuSdkAudioInfo2.sampleRate, localTuSdkAudioInfo2.channelCount);
      this.b.getRawInfo().setPath(this.d);
      this.e = true;
      this.c = new TuSDKAudioRenderDecoder(this.b, localTuSdkAudioInfo2, this.d);
      this.c.setDecodeListener(this);
      this.c.start();
      return this.b;
    }
    
    protected void onPreExecute()
    {
      super.onPreExecute();
      TuSDKAudioDecoderTaskManager.a(TuSDKAudioDecoderTaskManager.this, TuSDKAudioDecoderTaskManager.State.Decoding);
    }
    
    public void cancel()
    {
      if (isCancelled()) {
        return;
      }
      if (this.c != null) {
        this.c.setPause();
      }
      cancel(true);
    }
    
    protected void onCancelled()
    {
      super.onCancelled();
      if (this.c != null) {
        this.c.setPause();
      }
      TuSDKAudioDecoderTaskManager.c(TuSDKAudioDecoderTaskManager.this).remove(this);
      if (TuSDKAudioDecoderTaskManager.c(TuSDKAudioDecoderTaskManager.this).size() == 0) {
        TuSDKAudioDecoderTaskManager.a(TuSDKAudioDecoderTaskManager.this, TuSDKAudioDecoderTaskManager.State.Cancelled);
      }
    }
    
    protected void onCancelled(TuSDKAudioRenderEntry paramTuSDKAudioRenderEntry)
    {
      super.onCancelled(paramTuSDKAudioRenderEntry);
      TuSDKAudioDecoderTaskManager.c(TuSDKAudioDecoderTaskManager.this).remove(this);
      if (this.c != null) {
        this.c.setPause();
      }
      this.f = false;
      if (TuSDKAudioDecoderTaskManager.c(TuSDKAudioDecoderTaskManager.this).size() == 0) {
        TuSDKAudioDecoderTaskManager.a(TuSDKAudioDecoderTaskManager.this, TuSDKAudioDecoderTaskManager.State.Cancelled);
      }
    }
    
    protected void onPostExecute(TuSDKAudioRenderEntry paramTuSDKAudioRenderEntry)
    {
      super.onPostExecute(paramTuSDKAudioRenderEntry);
      if ((this.e) || (this.f)) {
        return;
      }
      TuSDKAudioDecoderTaskManager.c(TuSDKAudioDecoderTaskManager.this).remove(this);
      if (TuSDKAudioDecoderTaskManager.c(TuSDKAudioDecoderTaskManager.this).size() == 0) {
        TuSDKAudioDecoderTaskManager.a(TuSDKAudioDecoderTaskManager.this, TuSDKAudioDecoderTaskManager.State.Complete);
      }
    }
    
    public void onDecoderUpdated(MediaCodec.BufferInfo paramBufferInfo)
    {
      this.f = true;
    }
    
    public void onDecoderCompleted(Exception paramException)
    {
      if (!this.e) {
        return;
      }
      this.c.release();
      TuSDKAudioDecoderTaskManager.c(TuSDKAudioDecoderTaskManager.this).remove(this);
      this.f = false;
      if (TuSDKAudioDecoderTaskManager.c(TuSDKAudioDecoderTaskManager.this).size() == 0) {
        TuSDKAudioDecoderTaskManager.a(TuSDKAudioDecoderTaskManager.this, TuSDKAudioDecoderTaskManager.State.Complete);
      }
    }
  }
  
  public static abstract interface TuSDKAudioDecoderTaskStateListener
  {
    public abstract void onStateChanged(TuSDKAudioDecoderTaskManager.State paramState);
  }
  
  public static enum State
  {
    private State() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKAudioDecoderTaskManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */