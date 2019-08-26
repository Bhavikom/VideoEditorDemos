package org.lasque.tusdk.core.decoder;

import java.util.LinkedList;
import org.lasque.tusdk.core.common.TuSDKAVPacket;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public abstract class TuSDKVideoTimeEffectController
  implements TuSDKVideoTimeEffectControllerInterface
{
  private TimeEffectMode a = TimeEffectMode.NoMode;
  protected LinkedList<TuSDKAVPacket> mCachePackets = new LinkedList();
  protected TuSdkTimeRange mTimeRange;
  protected int mTimes = 3;
  protected int mCounter = 0;
  protected int mCopyTimes = 1;
  
  public TuSDKVideoTimeEffectController()
  {
    if (this.mTimeRange == null) {
      this.mTimeRange = TuSdkTimeRange.makeRange(0.0F, 1.0F);
    }
  }
  
  public static TuSDKVideoTimeEffectController create(TimeEffectMode paramTimeEffectMode)
  {
    if (TimeEffectMode.RepeatMode == paramTimeEffectMode) {
      return new TuSDKVideoTimeEffectRepeatController();
    }
    if (TimeEffectMode.SlowMode == paramTimeEffectMode) {
      return new TuSDKVideoTimeEffectSlowController();
    }
    return new TuSDKVideoTimeEffectNoController();
  }
  
  public TuSdkTimeRange getTimeRange()
  {
    return this.mTimeRange;
  }
  
  public void setTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
  {
    this.mTimeRange = paramTuSdkTimeRange;
  }
  
  public void setTimeEffectMode(TimeEffectMode paramTimeEffectMode)
  {
    this.a = paramTimeEffectMode;
  }
  
  public TimeEffectMode getTimeEffectMode()
  {
    return this.a;
  }
  
  public void setTimes(int paramInt)
  {
    this.mTimes = paramInt;
  }
  
  public int getTimes()
  {
    return this.mTimes;
  }
  
  public void doPacketTimeEffectExtract(LinkedList<TuSDKAVPacket> paramLinkedList)
  {
    if (!this.mTimeRange.isValid()) {}
  }
  
  public void reset()
  {
    this.mCounter = 0;
    this.mCopyTimes = 1;
    this.mCachePackets.clear();
  }
  
  public static enum TimeEffectMode
  {
    private TimeEffectMode() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKVideoTimeEffectController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */