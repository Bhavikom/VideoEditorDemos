package org.lasque.tusdk.core.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import org.lasque.tusdk.core.utils.ContextUtils;

public class TuSdkImageView
  extends ImageView
  implements TuSdkViewInterface
{
  private int a;
  private boolean b;
  private int c;
  private int d;
  private boolean e;
  private PorterDuffXfermode f = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
  private int g = 31;
  
  public TuSdkImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  public TuSdkImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }
  
  public TuSdkImageView(Context paramContext)
  {
    super(paramContext);
    initView();
  }
  
  protected void initView() {}
  
  protected void onDraw(Canvas paramCanvas)
  {
    if (!drawRadius(paramCanvas)) {
      super.onDraw(paramCanvas);
    }
    drawStroke(paramCanvas);
  }
  
  @SuppressLint({"WrongCall"})
  protected boolean drawRadius(Canvas paramCanvas)
  {
    Drawable localDrawable = getDrawable();
    if ((localDrawable == null) || (!(localDrawable instanceof BitmapDrawable)) || (!this.b)) {
      return false;
    }
    RectF localRectF = new RectF(1.0F, 1.0F, getWidth() - 1, getHeight() - 1);
    int i = paramCanvas.saveLayer(localRectF, null, this.g);
    Paint localPaint = ((BitmapDrawable)localDrawable).getPaint();
    localPaint.setAntiAlias(true);
    paramCanvas.drawRoundRect(localRectF, this.a, this.a, localPaint);
    Xfermode localXfermode = localPaint.getXfermode();
    localPaint.setXfermode(this.f);
    super.onDraw(paramCanvas);
    localPaint.setXfermode(localXfermode);
    paramCanvas.restoreToCount(i);
    if (this.a == 0) {
      this.b = false;
    }
    return true;
  }
  
  protected void drawStroke(Canvas paramCanvas)
  {
    if (!this.e) {
      return;
    }
    float f1 = this.c * 0.5F;
    RectF localRectF = new RectF(f1, f1, getWidth() - f1, getHeight() - f1);
    Paint localPaint = new Paint(1);
    localPaint.setColor(this.d);
    localPaint.setStrokeWidth(this.c);
    localPaint.setStyle(Paint.Style.STROKE);
    paramCanvas.drawRoundRect(localRectF, this.a, this.a, localPaint);
    if (this.c == 0) {
      this.e = false;
    }
  }
  
  public float getCornerRadius()
  {
    return this.a;
  }
  
  public void setCornerRadius(int paramInt)
  {
    this.a = (paramInt > 0 ? paramInt : 0);
    this.b = true;
    invalidate();
  }
  
  public void setCornerRadiusDP(int paramInt)
  {
    int i = ContextUtils.dip2px(getContext(), paramInt);
    setCornerRadius(i);
  }
  
  public void setStroke(int paramInt1, int paramInt2)
  {
    this.d = paramInt1;
    this.c = (paramInt2 > 0 ? paramInt2 : 0);
    this.e = true;
    invalidate();
  }
  
  public void removeStroke()
  {
    this.d = 0;
    this.c = 0;
  }
  
  public void loadView() {}
  
  public void viewDidLoad() {}
  
  public void viewWillDestory() {}
  
  public void viewNeedRest() {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\TuSdkImageView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */