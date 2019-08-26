package org.lasque.tusdk.core.media.codec.encoder;

import org.lasque.tusdk.core.seles.sources.SelesWatermark;

public abstract interface TuSdkEncodeSurface
{
  public abstract void newFrameReadyInGLThread(long paramLong);
  
  public abstract void duplicateFrameReadyInGLThread(long paramLong);
  
  public abstract boolean swapBuffers(long paramLong);
  
  public abstract void requestKeyFrame();
  
  public abstract void setPause(boolean paramBoolean);
  
  public abstract void setWatermark(SelesWatermark paramSelesWatermark);
  
  public abstract void flush();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\encoder\TuSdkEncodeSurface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */