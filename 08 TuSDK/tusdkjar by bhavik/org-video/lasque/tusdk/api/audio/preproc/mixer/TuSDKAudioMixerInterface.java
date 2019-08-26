package org.lasque.tusdk.api.audio.preproc.mixer;

import java.util.List;

public abstract interface TuSDKAudioMixerInterface
{
  public abstract void mixAudios(List<TuSDKAudioEntry> paramList);
  
  public abstract byte[] mixRawAudioBytes(byte[][] paramArrayOfByte);
  
  public abstract void cancel();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\audio\preproc\mixer\TuSDKAudioMixerInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */