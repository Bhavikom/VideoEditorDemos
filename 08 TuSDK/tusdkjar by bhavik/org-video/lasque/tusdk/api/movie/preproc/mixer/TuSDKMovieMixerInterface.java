package org.lasque.tusdk.api.movie.preproc.mixer;

import java.util.List;
import org.lasque.tusdk.api.audio.preproc.mixer.TuSDKAudioEntry;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;

public abstract interface TuSDKMovieMixerInterface
{
  public abstract void mix(TuSDKMediaDataSource paramTuSDKMediaDataSource, List<TuSDKAudioEntry> paramList, boolean paramBoolean);
  
  public abstract TuSDKMovieMixerInterface setVideoSoundVolume(float paramFloat);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\movie\preproc\mixer\TuSDKMovieMixerInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */