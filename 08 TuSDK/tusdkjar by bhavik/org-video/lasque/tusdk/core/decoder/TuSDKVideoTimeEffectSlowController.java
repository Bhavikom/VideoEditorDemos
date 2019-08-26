package org.lasque.tusdk.core.decoder;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import org.lasque.tusdk.core.common.TuSDKAVPacket;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public class TuSDKVideoTimeEffectSlowController
  extends TuSDKVideoTimeEffectController
{
  public void doPacketTimeEffectExtract(LinkedList<TuSDKAVPacket> paramLinkedList)
  {
    super.doPacketTimeEffectExtract(paramLinkedList);
    for (int i = 0; i < paramLinkedList.size(); i++)
    {
      if (((TuSDKAVPacket)paramLinkedList.get(i)).getSampleTimeUs() >= this.mTimeRange.getStartTimeUS())
      {
        TuSDKAVPacket localTuSDKAVPacket = new TuSDKAVPacket(((TuSDKAVPacket)paramLinkedList.get(i)).getByteBuffer(), ((TuSDKAVPacket)paramLinkedList.get(i)).getSampleTimeUs(), ((TuSDKAVPacket)paramLinkedList.get(i)).getPacketType());
        localTuSDKAVPacket.setChunkSize(((TuSDKAVPacket)paramLinkedList.get(i)).getChunkSize());
        this.mCachePackets.addLast(localTuSDKAVPacket);
      }
      if (this.mCachePackets.size() > 0) {
        if (((TuSDKAVPacket)this.mCachePackets.getFirst()).getSampleTimeUs() <= this.mTimeRange.getEndTimeUS())
        {
          ((TuSDKAVPacket)paramLinkedList.get(i)).setByteBuffer(ByteBuffer.wrap(((TuSDKAVPacket)this.mCachePackets.getFirst()).getByteBuffer().array()));
          ((TuSDKAVPacket)paramLinkedList.get(i)).setChunkSize(((TuSDKAVPacket)this.mCachePackets.getFirst()).getChunkSize());
          if (this.mCounter == this.mTimes)
          {
            this.mCachePackets.removeFirst();
            this.mCounter = 0;
          }
          else
          {
            this.mCounter += 1;
          }
        }
        else
        {
          ((TuSDKAVPacket)paramLinkedList.get(i)).setByteBuffer(ByteBuffer.wrap(((TuSDKAVPacket)this.mCachePackets.getFirst()).getByteBuffer().array()));
          ((TuSDKAVPacket)paramLinkedList.get(i)).setChunkSize(((TuSDKAVPacket)this.mCachePackets.getFirst()).getChunkSize());
          this.mCachePackets.removeFirst();
        }
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKVideoTimeEffectSlowController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */