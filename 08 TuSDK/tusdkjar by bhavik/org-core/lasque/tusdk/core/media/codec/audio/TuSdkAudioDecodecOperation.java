package org.lasque.tusdk.core.media.codec.audio;

import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;

public abstract interface TuSdkAudioDecodecOperation
  extends TuSdkDecodecOperation
{
  public abstract void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender);
  
  public abstract TuSdkAudioInfo getAudioInfo();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioDecodecOperation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */