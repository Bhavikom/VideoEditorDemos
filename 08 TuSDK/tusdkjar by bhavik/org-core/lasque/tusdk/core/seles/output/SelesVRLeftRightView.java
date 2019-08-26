package org.lasque.tusdk.core.seles.output;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.util.AttributeSet;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;

public class SelesVRLeftRightView
  extends SelesBaseView
{
  private _VRLeftRightSurfacePusher a;
  
  public SelesVRLeftRightView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public SelesVRLeftRightView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public SelesVRLeftRightView(Context paramContext)
  {
    super(paramContext);
  }
  
  protected void initView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super.initView(paramContext, paramAttributeSet);
  }
  
  protected SelesSurfacePusher buildWindowDisplay()
  {
    if (this.a == null) {
      this.a = new _VRLeftRightSurfacePusher();
    }
    return this.a;
  }
  
  protected SelesVerticeCoordinateBuilder buildVerticeCoordinateBuilder()
  {
    return null;
  }
  
  public void setRadius(float paramFloat)
  {
    if (this.a == null) {
      return;
    }
    this.a.setRadius(paramFloat);
  }
  
  public void setScale(float paramFloat)
  {
    if (this.a == null) {
      return;
    }
    this.a.setScale(paramFloat);
  }
  
  public void setCenter(PointF paramPointF)
  {
    if (this.a == null) {
      return;
    }
    this.a.setCenter(paramPointF);
  }
  
  private class _VRLeftRightSurfacePusher
    extends SelesSurfacePusher
  {
    private int b;
    private int c;
    private int d;
    private float e = 1.0F;
    private float f;
    private PointF g = new PointF(0.5F, 0.5F);
    
    public _VRLeftRightSurfacePusher()
    {
      super();
    }
    
    protected void onInitOnGLThread()
    {
      super.onInitOnGLThread();
      this.b = this.mDisplayProgram.uniformIndex("aspectRatio");
      this.c = this.mDisplayProgram.uniformIndex("aspheric");
      this.d = this.mDisplayProgram.uniformIndex("aspectRegion");
      this.g = new PointF(0.5F, 0.5F);
      this.e = 1.0F;
      this.f = -0.12F;
      a();
      recalculateViewGeometry();
    }
    
    protected void recalculateViewGeometry()
    {
      SelesContext.setActiveShaderProgram(this.mDisplayProgram);
      GLES20.glUniform1f(this.b, this.mInputImageSize.height / this.mInputImageSize.width);
      TuSdkSize localTuSdkSize1 = SelesVRLeftRightView.this.mSizeInPixels.copy();
      TuSdkSize localTuSdkSize2 = TuSdkSize.create(localTuSdkSize1.width, localTuSdkSize1.height / 2);
      Rect localRect = RectHelper.makeRectWithAspectRatioInsideRect(this.mInputImageSize, new Rect(0, 0, localTuSdkSize2.width, localTuSdkSize2.height));
      float[] arrayOfFloat = new float[4];
      arrayOfFloat[0] = (localRect.left / localTuSdkSize2.width);
      arrayOfFloat[1] = (localRect.top / localTuSdkSize2.height);
      arrayOfFloat[2] = (localRect.width() / localTuSdkSize2.width);
      arrayOfFloat[3] = (localRect.height() / localTuSdkSize2.height);
      GLES20.glUniform4fv(this.d, 1, FloatBuffer.wrap(arrayOfFloat));
    }
    
    public void setRadius(float paramFloat)
    {
      this.e = paramFloat;
      a();
    }
    
    public void setScale(float paramFloat)
    {
      this.f = paramFloat;
      a();
    }
    
    public void setCenter(PointF paramPointF)
    {
      if (paramPointF == null) {
        return;
      }
      this.g = paramPointF;
      a();
    }
    
    private void a()
    {
      runOnDraw(new Runnable()
      {
        public void run()
        {
          SelesContext.setActiveShaderProgram(SelesVRLeftRightView._VRLeftRightSurfacePusher.this.mDisplayProgram);
          float[] arrayOfFloat = new float[4];
          arrayOfFloat[0] = SelesVRLeftRightView._VRLeftRightSurfacePusher.a(SelesVRLeftRightView._VRLeftRightSurfacePusher.this).x;
          arrayOfFloat[1] = SelesVRLeftRightView._VRLeftRightSurfacePusher.a(SelesVRLeftRightView._VRLeftRightSurfacePusher.this).y;
          arrayOfFloat[2] = SelesVRLeftRightView._VRLeftRightSurfacePusher.b(SelesVRLeftRightView._VRLeftRightSurfacePusher.this);
          arrayOfFloat[3] = SelesVRLeftRightView._VRLeftRightSurfacePusher.c(SelesVRLeftRightView._VRLeftRightSurfacePusher.this);
          GLES20.glUniform4fv(SelesVRLeftRightView._VRLeftRightSurfacePusher.d(SelesVRLeftRightView._VRLeftRightSurfacePusher.this), 1, FloatBuffer.wrap(arrayOfFloat));
        }
      });
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\output\SelesVRLeftRightView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */