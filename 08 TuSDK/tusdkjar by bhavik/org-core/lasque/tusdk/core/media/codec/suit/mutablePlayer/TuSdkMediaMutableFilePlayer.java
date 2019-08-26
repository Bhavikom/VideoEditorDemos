package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import java.util.List;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFilePlayer;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public abstract interface TuSdkMediaMutableFilePlayer
  extends TuSdkMediaFilePlayer
{
  public abstract int maxInputSize();
  
  public abstract void setMediaDataSources(List<TuSdkMediaDataSource> paramList);
  
  public abstract void setOutputRatio(float paramFloat);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\TuSdkMediaMutableFilePlayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */