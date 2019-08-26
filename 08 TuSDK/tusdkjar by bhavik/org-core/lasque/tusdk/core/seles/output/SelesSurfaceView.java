package org.lasque.tusdk.core.seles.output;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.utils.ThreadHelper;

public class SelesSurfaceView
  extends GLSurfaceView
{
  public static final int Renderer_Max_FPS = 50;
  private int a = 50;
  private int b = 20;
  private int c = 0;
  private boolean d = true;
  private boolean e;
  private SelesSurfaceViewDelegate f;
  private boolean g;
  private Runnable h = new Runnable()
  {
    public void run()
    {
      SelesSurfaceView.a(SelesSurfaceView.this);
    }
  };
  
  public SelesSurfaceView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    a();
  }
  
  public SelesSurfaceView(Context paramContext)
  {
    super(paramContext);
    a();
  }
  
  private void a()
  {
    setWillNotDraw(false);
  }
  
  public boolean isCreated()
  {
    return this.e;
  }
  
  public boolean isPaused()
  {
    return this.g;
  }
  
  public void setDelegate(SelesSurfaceViewDelegate paramSelesSurfaceViewDelegate)
  {
    this.f = paramSelesSurfaceViewDelegate;
  }
  
  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
  {
    super.surfaceChanged(paramSurfaceHolder, paramInt1, paramInt2, paramInt3);
    postInvalidate();
    if (this.f != null) {
      this.f.onSurfaceChanged(paramSurfaceHolder, paramInt1, paramInt2, paramInt3);
    }
  }
  
  public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
  {
    super.surfaceCreated(paramSurfaceHolder);
    this.g = false;
    this.e = true;
  }
  
  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    this.g = false;
    this.e = false;
    super.surfaceDestroyed(paramSurfaceHolder);
  }
  
  public void onPause()
  {
    this.g = true;
    super.onPause();
  }
  
  public void onResume()
  {
    this.g = false;
    super.onResume();
    b();
  }
  
  public void setEnableFixedFrameRate(boolean paramBoolean)
  {
    this.d = paramBoolean;
  }
  
  public boolean getEnableFixedFrameRate()
  {
    return this.d;
  }
  
  public void setRenderMode(int paramInt)
  {
    this.g = false;
    this.c = paramInt;
    super.setRenderMode(0);
    b();
  }
  
  public int getRenderMode()
  {
    return this.c;
  }
  
  public int getRendererFPS()
  {
    return this.a;
  }
  
  public void setRendererFPS(int paramInt)
  {
    this.a = paramInt;
    if ((paramInt < 1) || (paramInt > 50)) {
      this.a = 50;
    }
    this.b = (1000 / this.a);
  }
  
  private void b()
  {
    if ((this.c == 0) || (this.g) || (this.b < 16)) {
      return;
    }
    requestRender();
    if (getEnableFixedFrameRate()) {
      ThreadHelper.postDelayed(this.h, this.b);
    }
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    SdkValid.shared.vaildAndDraw(paramCanvas);
  }
  
  public static abstract interface SelesSurfaceViewDelegate
  {
    public abstract void onSurfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\output\SelesSurfaceView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */