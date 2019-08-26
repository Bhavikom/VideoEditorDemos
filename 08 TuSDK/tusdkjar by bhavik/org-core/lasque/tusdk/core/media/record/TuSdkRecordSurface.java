package org.lasque.tusdk.core.media.record;

import org.lasque.tusdk.core.seles.SelesContext.SelesInput;

public abstract interface TuSdkRecordSurface
{
  public abstract void addTarget(SelesContext.SelesInput paramSelesInput, int paramInt);
  
  public abstract void removeTarget(SelesContext.SelesInput paramSelesInput);
  
  public abstract void initInGLThread();
  
  public abstract void updateSurfaceTexImage();
  
  public abstract void newFrameReadyInGLThread(long paramLong);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\record\TuSdkRecordSurface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */