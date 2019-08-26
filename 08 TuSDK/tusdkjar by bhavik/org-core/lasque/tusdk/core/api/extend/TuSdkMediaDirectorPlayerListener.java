package org.lasque.tusdk.core.api.extend;

import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public abstract interface TuSdkMediaDirectorPlayerListener
  extends TuSdkMediaPlayerListener
{
  public abstract void onProgress(long paramLong1, long paramLong2, TuSdkMediaDataSource paramTuSdkMediaDataSource, TuSdkMediaFileCuterTimeline paramTuSdkMediaFileCuterTimeline);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\extend\TuSdkMediaDirectorPlayerListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */