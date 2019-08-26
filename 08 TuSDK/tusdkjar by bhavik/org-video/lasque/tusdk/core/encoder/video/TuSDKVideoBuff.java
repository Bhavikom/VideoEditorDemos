package org.lasque.tusdk.core.encoder.video;

import java.util.Arrays;

public class TuSDKVideoBuff
{
  public boolean isReadyToFill = true;
  public int colorFormat = -1;
  public byte[] buff;
  
  public TuSDKVideoBuff(int paramInt1, int paramInt2)
  {
    this.colorFormat = paramInt1;
    this.buff = new byte[paramInt2];
    Arrays.fill(this.buff, paramInt2 / 2, paramInt2, (byte)Byte.MAX_VALUE);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\encoder\video\TuSDKVideoBuff.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */