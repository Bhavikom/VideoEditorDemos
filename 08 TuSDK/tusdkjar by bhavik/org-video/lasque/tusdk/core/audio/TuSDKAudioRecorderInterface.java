package org.lasque.tusdk.core.audio;

import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;

public abstract interface TuSDKAudioRecorderInterface
{
  public abstract void startRecording();
  
  public abstract void stopRecording();
  
  public abstract boolean isRecording();
  
  public abstract void mute(boolean paramBoolean);
  
  public abstract TuSDKAudioDataEncoderInterface getAudioEncoder();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\audio\TuSDKAudioRecorderInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */