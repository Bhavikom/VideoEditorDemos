package org.lasque.tusdk.core.seles.output;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateFillModeBuilder;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateFillModeBuilder.OnDisplaySizeChangeListener;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateFillModeBuilderImpl;

public class SelesView
  extends SelesBaseView
{
  private SelesFillModeType a;
  private SelesVerticeCoordinateFillModeBuilder b;
  private SelesSurfacePusher c;
  private RectF d;
  
  public SelesFillModeType getFillMode()
  {
    return this.a;
  }
  
  public void setFillMode(SelesFillModeType paramSelesFillModeType)
  {
    if (paramSelesFillModeType == null) {
      return;
    }
    this.a = paramSelesFillModeType;
    if (this.b != null) {
      this.b.setFillMode(paramSelesFillModeType);
    }
  }
  
  public RectF getDisplayRect()
  {
    return this.d;
  }
  
  public void setDisplayRect(RectF paramRectF)
  {
    if (paramRectF == null) {
      return;
    }
    this.d = paramRectF;
    if (this.b != null) {
      this.b.setCanvasRect(paramRectF);
    }
  }
  
  public SelesView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public SelesView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public SelesView(Context paramContext)
  {
    super(paramContext);
  }
  
  protected void initView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this.a = SelesFillModeType.PreserveAspectRatio;
    super.initView(paramContext, paramAttributeSet);
  }
  
  protected SelesSurfacePusher buildWindowDisplay()
  {
    if (this.c == null) {
      this.c = new SelesSurfacePusher();
    }
    return this.c;
  }
  
  protected SelesVerticeCoordinateBuilder buildVerticeCoordinateBuilder()
  {
    if (this.b == null) {
      this.b = new SelesVerticeCoordinateFillModeBuilderImpl(true);
    }
    this.b.setFillMode(this.a);
    return this.b;
  }
  
  public void setOnDisplayChangeListener(SelesVerticeCoordinateFillModeBuilder.OnDisplaySizeChangeListener paramOnDisplaySizeChangeListener)
  {
    if (this.b == null) {
      return;
    }
    this.b.setOnDisplaySizeChangeListener(paramOnDisplaySizeChangeListener);
  }
  
  public static enum SelesFillModeType
  {
    private SelesFillModeType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\output\SelesView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */