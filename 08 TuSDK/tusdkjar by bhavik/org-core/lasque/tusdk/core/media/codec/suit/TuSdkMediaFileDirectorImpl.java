package org.lasque.tusdk.core.media.codec.suit;

import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorSync;

public class TuSdkMediaFileDirectorImpl
  extends TuSdkMediaFileCuterImpl
{
  public TuSdkMediaFileDirectorImpl()
  {
    super(new TuSdkMediaFileDirectorSync());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\TuSdkMediaFileDirectorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */