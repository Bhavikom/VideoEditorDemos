package org.lasque.tusdk.core.api.extend;

import android.support.annotation.Nullable;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public abstract interface TuSdkMediaProgress
{
  public abstract void onProgress(float paramFloat, TuSdkMediaDataSource paramTuSdkMediaDataSource, int paramInt1, int paramInt2);
  
  public abstract void onCompleted(@Nullable Exception paramException, TuSdkMediaDataSource paramTuSdkMediaDataSource, int paramInt);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\extend\TuSdkMediaProgress.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */