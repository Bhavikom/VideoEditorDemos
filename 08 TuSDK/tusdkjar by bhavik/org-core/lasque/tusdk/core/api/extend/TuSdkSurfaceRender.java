package org.lasque.tusdk.core.api.extend;

public abstract interface TuSdkSurfaceRender
  extends TuSdkSurfaceDraw
{
  public abstract void onSurfaceCreated();
  
  public abstract void onSurfaceChanged(int paramInt1, int paramInt2);
  
  public abstract void onSurfaceDestory();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\extend\TuSdkSurfaceRender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */