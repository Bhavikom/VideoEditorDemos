package org.lasque.tusdk.core.encoder.audio;

class TuSDKAudioBuff
{
  public boolean isReadyToFill = true;
  public byte[] buff;
  
  public TuSDKAudioBuff(int paramInt)
  {
    this.buff = new byte[paramInt];
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\encoder\audio\TuSDKAudioBuff.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */