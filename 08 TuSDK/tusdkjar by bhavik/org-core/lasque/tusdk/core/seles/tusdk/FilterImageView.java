package org.lasque.tusdk.core.seles.tusdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.seles.output.SelesView;
import org.lasque.tusdk.core.seles.sources.SelesPicture;

public class FilterImageView
  extends SelesView
  implements GLSurfaceView.Renderer, FilterImageViewInterface
{
  private SelesPicture a;
  private FilterWrap b;
  @SuppressLint({"ClickableViewAccessibility"})
  private View.OnTouchListener c = new View.OnTouchListener()
  {
    public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
    {
      if ((FilterImageView.a(FilterImageView.this) == null) || (FilterImageView.b(FilterImageView.this) == null) || (paramAnonymousMotionEvent.getPointerCount() > 1)) {
        return false;
      }
      switch (paramAnonymousMotionEvent.getAction())
      {
      case 0: 
        FilterImageView.c(FilterImageView.this);
        break;
      case 2: 
        break;
      default: 
        FilterImageView.d(FilterImageView.this);
      }
      return true;
    }
  };
  
  public FilterImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public FilterImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public FilterImageView(Context paramContext)
  {
    super(paramContext);
  }
  
  protected void initView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super.initView(paramContext, paramAttributeSet);
    setRenderer(this);
  }
  
  public void requestRender()
  {
    if (this.b != null) {
      this.b.submitFilterParameter();
    }
    super.requestRender();
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  public void enableTouchForOrigin()
  {
    setOnTouchListener(this.c);
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  public void disableTouchForOrigin()
  {
    setOnTouchListener(null);
  }
  
  public FilterWrap getFilterWrap()
  {
    return this.b;
  }
  
  public void setFilterWrap(FilterWrap paramFilterWrap)
  {
    if ((this.b == null) && (paramFilterWrap == null)) {
      return;
    }
    if (paramFilterWrap == null)
    {
      if (!this.b.equalsCode("Normal")) {}
    }
    else if (paramFilterWrap.equals(this.b)) {
      return;
    }
    this.b = paramFilterWrap;
    if (this.b == null) {
      this.b = FilterLocalPackage.shared().getFilterWrap(null);
    }
    this.b.bindWithCameraView(this);
    this.b.processImage();
    if (this.a == null) {
      return;
    }
    this.a.removeAllTargets();
    this.b.addOrgin(this.a);
    this.a.processImage();
    requestRender();
  }
  
  public void setImage(Bitmap paramBitmap)
  {
    if (paramBitmap == null) {
      return;
    }
    this.a = new SelesPicture(paramBitmap);
    if (this.b == null)
    {
      this.b = FilterLocalPackage.shared().getFilterWrap(null);
      this.b.bindWithCameraView(this);
      this.b.processImage();
    }
    this.b.addOrgin(this.a);
    this.a.processImage();
    requestRender();
  }
  
  public void setImageBackgroundColor(int paramInt)
  {
    setBackgroundColor(Color.red(paramInt) / 255.0F, Color.green(paramInt) / 255.0F, Color.blue(paramInt) / 255.0F, Color.alpha(paramInt) / 255.0F);
  }
  
  private void a()
  {
    this.a.removeAllTargets();
    this.a.addTarget(this, 0);
    requestRender();
  }
  
  private void b()
  {
    this.a.removeAllTargets();
    this.b.addOrgin(this.a);
    requestRender();
  }
  
  public void onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig)
  {
    GLES20.glDisable(2929);
  }
  
  public void onSurfaceChanged(GL10 paramGL10, int paramInt1, int paramInt2)
  {
    GLES20.glViewport(0, 0, paramInt1, paramInt2);
  }
  
  public void onDrawFrame(GL10 paramGL10)
  {
    if (this.a == null) {
      return;
    }
    this.a.processImage();
    this.a.onDrawFrame(paramGL10);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\FilterImageView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */