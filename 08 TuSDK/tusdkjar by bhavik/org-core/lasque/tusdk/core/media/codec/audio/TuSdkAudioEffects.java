package org.lasque.tusdk.core.media.codec.audio;

public abstract interface TuSdkAudioEffects
{
  public abstract void release();
  
  public abstract boolean enableAcousticEchoCanceler();
  
  public abstract boolean enableAutomaticGainControl();
  
  public abstract boolean enableNoiseSuppressor();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioEffects.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */