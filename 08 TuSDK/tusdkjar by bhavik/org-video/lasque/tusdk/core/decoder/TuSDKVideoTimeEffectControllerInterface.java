package org.lasque.tusdk.core.decoder;

import java.util.LinkedList;
import org.lasque.tusdk.core.common.TuSDKAVPacket;

public abstract interface TuSDKVideoTimeEffectControllerInterface
{
  public abstract void doPacketTimeEffectExtract(LinkedList<TuSDKAVPacket> paramLinkedList);
  
  public abstract void reset();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKVideoTimeEffectControllerInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */