package org.lasque.tusdk.core.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

public class TuSdkSurfaceView
  extends GLSurfaceView
{
  private CameraSurfaceViewDelegate a;
  private boolean b;
  
  public TuSdkSurfaceView(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuSdkSurfaceView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public boolean isCreated()
  {
    return this.b;
  }
  
  public void setDelegate(CameraSurfaceViewDelegate paramCameraSurfaceViewDelegate)
  {
    this.a = paramCameraSurfaceViewDelegate;
  }
  
  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
  {
    super.surfaceChanged(paramSurfaceHolder, paramInt1, paramInt2, paramInt3);
    if (this.a != null) {
      this.a.onSurfaceChanged(paramSurfaceHolder, paramInt1, paramInt2, paramInt3);
    }
  }
  
  public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
  {
    super.surfaceCreated(paramSurfaceHolder);
    this.b = true;
  }
  
  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    this.b = false;
    super.surfaceDestroyed(paramSurfaceHolder);
  }
  
  public static abstract interface CameraSurfaceViewDelegate
  {
    public abstract void onSurfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\TuSdkSurfaceView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */