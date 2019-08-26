package org.lasque.tusdk.core.seles.egl;

import android.opengl.GLSurfaceView.Renderer;
import javax.microedition.khronos.opengles.GL10;

public abstract interface SelesRenderer
  extends GLSurfaceView.Renderer
{
  public abstract void onSurfaceDestory(GL10 paramGL10);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\egl\SelesRenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */