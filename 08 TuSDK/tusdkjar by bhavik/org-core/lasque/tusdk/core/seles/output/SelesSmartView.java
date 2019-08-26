package org.lasque.tusdk.core.seles.output;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;

public class SelesSmartView
  extends SelesBaseView
{
  private SelesVerticeCoordinateCropBuilderImpl a;
  private SelesSurfacePusher b;
  private RectF c;
  
  public RectF getDisplayRect()
  {
    return this.c;
  }
  
  public void setDisplayRect(RectF paramRectF)
  {
    if (paramRectF == null) {
      return;
    }
    this.c = paramRectF;
    if (this.a != null) {
      this.a.setCanvasRect(paramRectF);
    }
  }
  
  public SelesSmartView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public SelesSmartView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public SelesSmartView(Context paramContext)
  {
    super(paramContext);
  }
  
  protected void initView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this.c = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
    super.initView(paramContext, paramAttributeSet);
  }
  
  protected SelesSurfacePusher buildWindowDisplay()
  {
    if (this.b == null) {
      this.b = new SelesSurfacePusher();
    }
    return this.b;
  }
  
  protected SelesVerticeCoordinateBuilder buildVerticeCoordinateBuilder()
  {
    if (this.a == null) {
      this.a = new SelesVerticeCoordinateCropBuilderImpl(true);
    }
    this.a.setCanvasRect(this.c);
    return this.a;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\output\SelesSmartView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */