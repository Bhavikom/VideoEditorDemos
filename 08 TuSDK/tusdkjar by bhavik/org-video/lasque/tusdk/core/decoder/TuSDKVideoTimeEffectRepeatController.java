package org.lasque.tusdk.core.decoder;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import org.lasque.tusdk.core.common.TuSDKAVPacket;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public class TuSDKVideoTimeEffectRepeatController
  extends TuSDKVideoTimeEffectController
{
  private boolean a = false;
  private LinkedList<TuSDKAVPacket> b = new LinkedList();
  
  public void doPacketTimeEffectExtract(LinkedList<TuSDKAVPacket> paramLinkedList)
  {
    super.doPacketTimeEffectExtract(paramLinkedList);
    for (int i = 0; i < paramLinkedList.size(); i++)
    {
      if (((TuSDKAVPacket)paramLinkedList.get(i)).getSampleTimeUs() > this.mTimeRange.getEndTimeUS()) {
        this.a = true;
      }
      TuSDKAVPacket localTuSDKAVPacket;
      if ((((TuSDKAVPacket)paramLinkedList.get(i)).getSampleTimeUs() >= this.mTimeRange.getStartTimeUS()) && (((TuSDKAVPacket)paramLinkedList.get(i)).getSampleTimeUs() <= this.mTimeRange.getEndTimeUS()))
      {
        localTuSDKAVPacket = new TuSDKAVPacket(((TuSDKAVPacket)paramLinkedList.get(i)).getByteBuffer(), ((TuSDKAVPacket)paramLinkedList.get(i)).getSampleTimeUs(), ((TuSDKAVPacket)paramLinkedList.get(i)).getPacketType());
        localTuSDKAVPacket.setChunkSize(((TuSDKAVPacket)paramLinkedList.get(i)).getChunkSize());
        this.mCachePackets.addLast(localTuSDKAVPacket);
      }
      else if ((this.a) && (this.mCachePackets.size() > 0))
      {
        if (this.mCounter == this.mCachePackets.size() - 1)
        {
          this.mCounter = 0;
          this.mCopyTimes += 1;
        }
        if (this.mCopyTimes >= this.mTimes)
        {
          localTuSDKAVPacket = new TuSDKAVPacket(((TuSDKAVPacket)paramLinkedList.get(i)).getByteBuffer(), ((TuSDKAVPacket)paramLinkedList.get(i)).getSampleTimeUs(), ((TuSDKAVPacket)paramLinkedList.get(i)).getPacketType());
          localTuSDKAVPacket.setChunkSize(((TuSDKAVPacket)paramLinkedList.get(i)).getChunkSize());
          this.b.addLast(localTuSDKAVPacket);
          ((TuSDKAVPacket)paramLinkedList.get(i)).setByteBuffer(ByteBuffer.wrap(((TuSDKAVPacket)this.b.getFirst()).getByteBuffer().array()));
          ((TuSDKAVPacket)paramLinkedList.get(i)).setChunkSize(((TuSDKAVPacket)this.b.getFirst()).getChunkSize());
          this.b.removeFirst();
        }
        else
        {
          localTuSDKAVPacket = new TuSDKAVPacket(((TuSDKAVPacket)paramLinkedList.get(i)).getByteBuffer(), ((TuSDKAVPacket)paramLinkedList.get(i)).getSampleTimeUs(), ((TuSDKAVPacket)paramLinkedList.get(i)).getPacketType());
          localTuSDKAVPacket.setChunkSize(((TuSDKAVPacket)paramLinkedList.get(i)).getChunkSize());
          this.b.addLast(localTuSDKAVPacket);
          ((TuSDKAVPacket)paramLinkedList.get(i)).setByteBuffer(ByteBuffer.wrap(((TuSDKAVPacket)this.mCachePackets.get(this.mCounter)).getByteBuffer().array()));
          ((TuSDKAVPacket)paramLinkedList.get(i)).setChunkSize(((TuSDKAVPacket)this.mCachePackets.get(this.mCounter)).getChunkSize());
          this.mCounter += 1;
        }
      }
    }
  }
  
  public void reset()
  {
    super.reset();
    this.a = false;
    this.b.clear();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKVideoTimeEffectRepeatController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */