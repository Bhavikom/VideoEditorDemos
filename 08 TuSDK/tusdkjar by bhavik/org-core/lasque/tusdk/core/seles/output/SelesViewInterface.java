package org.lasque.tusdk.core.seles.output;

import android.opengl.GLSurfaceView.Renderer;

public abstract interface SelesViewInterface
{
  public abstract boolean isCreatedSurface();
  
  public abstract void setRenderer(GLSurfaceView.Renderer paramRenderer);
  
  public abstract void setRenderMode(int paramInt);
  
  public abstract int getRenderMode();
  
  public abstract void setRenderModeDirty();
  
  public abstract void setRenderModeContinuously();
  
  public abstract void requestRender();
  
  public abstract void onPause();
  
  public abstract void onResume();
  
  public abstract int getRendererFPS();
  
  public abstract void setRendererFPS(int paramInt);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\output\SelesViewInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */