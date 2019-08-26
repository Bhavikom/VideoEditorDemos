package org.lasque.tusdk.core.seles.sources;

import java.util.List;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioRenderEntry;
import org.lasque.tusdk.core.decoder.TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public abstract interface TuSdkEditorAudioMixer
{
  public abstract void setDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource);
  
  public abstract void setMasterAudioTrack(float paramFloat);
  
  public abstract void setSecondAudioTrack(float paramFloat);
  
  public abstract void addAudioRenderEntry(TuSDKAudioRenderEntry paramTuSDKAudioRenderEntry);
  
  public abstract void setAudioRenderEntryList(List<TuSDKAudioRenderEntry> paramList);
  
  public abstract void addTaskStateListener(TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener paramTuSDKAudioDecoderTaskStateListener);
  
  public abstract void removeTaskStateListener(TuSDKAudioDecoderTaskManager.TuSDKAudioDecoderTaskStateListener paramTuSDKAudioDecoderTaskStateListener);
  
  public abstract void removeAllTaskStateListener();
  
  public abstract void clearAllAudioData();
  
  public abstract void loadAudio();
  
  public abstract boolean isLoaded();
  
  public abstract void notifyLoadCompleted();
  
  public abstract void destroy();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\sources\TuSdkEditorAudioMixer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */