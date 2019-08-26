package org.lasque.tusdk.core.seles.sources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioMixerRender.RawAudioTrack;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderEntry;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderInfoWrap;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSdkMixerRender;
import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManager;
import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManager.State;
import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkEditorAudioMixerImpl
  implements TuSdkEditorAudioMixer
{
  private TuSdkMediaDataSource a;
  private TuSDKAudioRenderInfoWrap b;
  private TuSDKAudioDecoderTaskManager c = new TuSDKAudioDecoderTaskManager();
  private TuSdkMixerRender d;
  private List<TuSDKAudioRenderEntry> e = new ArrayList();
  private List<TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener> f = new ArrayList();
  private boolean g = true;
  private TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener h = new TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener()
  {
    public void onStateChanged(TuSDKAudioDecoderTaskManager.State paramAnonymousState)
    {
      Iterator localIterator = TuSdkEditorAudioMixerImpl.a(TuSdkEditorAudioMixerImpl.this).iterator();
      while (localIterator.hasNext())
      {
        TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener localTuSDKAudioDecoderTaskStateListener = (TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener)localIterator.next();
        localTuSDKAudioDecoderTaskStateListener.onStateChanged(paramAnonymousState);
      }
      if (paramAnonymousState == TuSDKAudioDecoderTaskManager.State.Complete) {
        StatisticsManger.appendComponent(9449473L);
      }
    }
  };
  
  public void setDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    this.a = paramTuSdkMediaDataSource;
  }
  
  public void addAudioRenderEntry(TuSDKAudioRenderEntry paramTuSDKAudioRenderEntry)
  {
    if (paramTuSDKAudioRenderEntry == null)
    {
      TLog.e("%s audio entry is null !!! ", new Object[] { "TuSdkEditorAudioMixer" });
      return;
    }
    clearAllAudioData();
    this.e.add(paramTuSDKAudioRenderEntry);
  }
  
  public void setAudioRenderEntryList(List<TuSDKAudioRenderEntry> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0))
    {
      TLog.e("%s mix audio list is null !!!", new Object[] { "TuSdkEditorAudioMixer" });
      return;
    }
    clearAllAudioData();
    this.e.addAll(paramList);
  }
  
  public void addTaskStateListener(TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener paramTuSDKAudioDecoderTaskStateListener)
  {
    if (paramTuSDKAudioDecoderTaskStateListener == null) {
      return;
    }
    this.f.add(paramTuSDKAudioDecoderTaskStateListener);
  }
  
  public void removeTaskStateListener(TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener paramTuSDKAudioDecoderTaskStateListener)
  {
    if (paramTuSDKAudioDecoderTaskStateListener == null) {
      return;
    }
    this.f.remove(paramTuSDKAudioDecoderTaskStateListener);
  }
  
  public void removeAllTaskStateListener()
  {
    this.f.clear();
  }
  
  public void clearAllAudioData()
  {
    this.e.clear();
    if (this.d != null) {
      this.d.clearAllAudioData();
    }
  }
  
  public void loadAudio()
  {
    this.c.setAudioEntry(this.e);
    this.c.setTrunkAudioInfo(a().getRealAudioInfo());
    if (this.h != null) {
      this.c.setDelegate(this.h);
    }
    this.c.start();
  }
  
  public boolean isLoaded()
  {
    return this.c.getState() == TuSDKAudioDecoderTaskManager.State.Complete;
  }
  
  public void setIncludeMasterSound(boolean paramBoolean)
  {
    if (!paramBoolean) {
      setMasterAudioTrack(0.0F);
    }
    this.g = paramBoolean;
  }
  
  public void setMasterAudioTrack(float paramFloat)
  {
    if ((this.d == null) || (paramFloat < 0.0F) || (!this.g)) {
      return;
    }
    this.d.setTrunkAudioVolume(paramFloat);
  }
  
  public void setSecondAudioTrack(float paramFloat)
  {
    if ((this.d == null) || (paramFloat < 0.0F)) {
      return;
    }
    this.d.setSecondAudioTrack(paramFloat);
  }
  
  public TuSdkMixerRender getMixerAudioRender()
  {
    if (this.d == null)
    {
      this.d = new TuSdkMixerRender();
      this.d.setRawAudioTrackList(b());
    }
    return this.d;
  }
  
  public void destroy()
  {
    this.c.cancel();
    if (this.d != null) {
      this.d.clearAllAudioData();
    }
    this.h = null;
  }
  
  private TuSDKAudioRenderInfoWrap a()
  {
    if ((this.a == null) || (!this.a.isValid()))
    {
      TLog.e("%s data source is invalid !!!", new Object[] { "TuSdkEditorAudioMixer" });
      return null;
    }
    if (this.b == null) {
      this.b = TuSDKAudioRenderInfoWrap.createWithMediaDataSource(this.a);
    }
    return this.b;
  }
  
  private List<TuSDKAudioMixerRender.RawAudioTrack> b()
  {
    ArrayList localArrayList = new ArrayList();
    if ((localArrayList == null) || (localArrayList.size() != this.e.size()))
    {
      localArrayList = new ArrayList();
      for (int i = 0; i < this.e.size(); i++) {
        localArrayList.add(new TuSDKAudioMixerRender.RawAudioTrack((TuSDKAudioRenderEntry)this.e.get(i)));
      }
    }
    return localArrayList;
  }
  
  public void notifyLoadCompleted()
  {
    if (this.d == null) {
      return;
    }
    this.d.setRawAudioTrackList(b());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\sources\TuSdkEditorAudioMixerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */