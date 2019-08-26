package org.lasque.tusdk.core.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import org.lasque.tusdk.core.TuSdkContext;

public class TuSdkTextView
  extends TextView
  implements TuSdkViewInterface
{
  private int a;
  private int b;
  private boolean c;
  private TextView d = null;
  private TextPaint e;
  private int f = TuSdkContext.dip2px(0.0F);
  private int g = TuSdkContext.getColor(17170445);
  
  public TuSdkTextView(Context paramContext)
  {
    super(paramContext);
    this.d = new TextView(paramContext);
    initView();
  }
  
  public TuSdkTextView(Context paramContext, @Nullable AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.d = new TextView(paramContext, paramAttributeSet);
    initView();
  }
  
  public TuSdkTextView(Context paramContext, @Nullable AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.d = new TextView(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  public void setText(CharSequence paramCharSequence, TextView.BufferType paramBufferType)
  {
    super.setText(paramCharSequence, paramBufferType);
    if (this.d == null) {
      return;
    }
    this.d.setText(paramCharSequence, paramBufferType);
  }
  
  public void setAlpha(float paramFloat)
  {
    super.setAlpha(paramFloat);
    if (this.d == null) {
      return;
    }
    this.d.setAlpha(paramFloat);
  }
  
  public void setGravity(int paramInt)
  {
    super.setGravity(paramInt);
    if (this.d == null) {
      return;
    }
    this.d.setGravity(paramInt);
  }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.setPadding(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.d == null) {
      return;
    }
    this.d.setPadding(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setLineSpacing(float paramFloat1, float paramFloat2)
  {
    super.setLineSpacing(paramFloat1, paramFloat2);
    if (this.d == null) {
      return;
    }
    this.d.setLineSpacing(paramFloat1, paramFloat2);
  }
  
  public void setTypeface(Typeface paramTypeface)
  {
    super.setTypeface(paramTypeface);
    if (this.d == null) {
      return;
    }
    this.d.setTypeface(paramTypeface);
  }
  
  protected void initView()
  {
    this.e = this.d.getPaint();
    this.e.setStrokeWidth(this.f);
    this.e.setStyle(Paint.Style.STROKE);
    this.d.setTextColor(this.g);
    this.d.setGravity(getGravity());
  }
  
  public void setTextSize(float paramFloat)
  {
    super.setTextSize(paramFloat);
  }
  
  public void setTextStrokeWidth(int paramInt)
  {
    this.f = paramInt;
    this.e.setStrokeWidth(paramInt);
    invalidate();
  }
  
  public void setTextStrokeColor(int paramInt)
  {
    this.g = paramInt;
    this.d.setTextColor(paramInt);
    invalidate();
  }
  
  public void setLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    super.setLayoutParams(paramLayoutParams);
    this.d.setLayoutParams(paramLayoutParams);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    CharSequence localCharSequence = this.d.getText();
    if ((localCharSequence == null) || (!localCharSequence.equals(getText())))
    {
      this.d.setText(getText());
      postInvalidate();
    }
    this.d.setTextSize(TuSdkContext.px2sp(getTextSize()));
    this.d.measure(paramInt1, paramInt2);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    this.d.layout(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    this.d.draw(paramCanvas);
    super.onDraw(paramCanvas);
    drawStroke(paramCanvas);
  }
  
  protected void drawStroke(Canvas paramCanvas)
  {
    if (!this.c) {
      return;
    }
    float f1 = this.a * 0.5F;
    RectF localRectF = new RectF(f1, f1, getWidth() - f1, getHeight() - f1);
    Paint localPaint = new Paint(1);
    localPaint.setColor(this.b);
    localPaint.setStrokeWidth(this.a);
    localPaint.setStyle(Paint.Style.STROKE);
    paramCanvas.drawRoundRect(localRectF, 0.0F, 0.0F, localPaint);
    if (this.a == 0) {
      this.c = false;
    }
  }
  
  @TargetApi(21)
  public void setLetterSpacing(float paramFloat)
  {
    super.setLetterSpacing(paramFloat);
    if (this.d == null) {
      return;
    }
    this.d.setLetterSpacing(paramFloat);
  }
  
  public void setStroke(int paramInt1, int paramInt2)
  {
    this.b = paramInt1;
    this.a = (paramInt2 > 0 ? paramInt2 : 0);
    this.c = true;
    invalidate();
  }
  
  public void removeStroke()
  {
    this.b = 0;
    this.a = 0;
  }
  
  public void loadView() {}
  
  public void viewDidLoad() {}
  
  public void viewWillDestory() {}
  
  public void viewNeedRest() {}
  
  public void setUnderlineText(boolean paramBoolean)
  {
    getPaint().setUnderlineText(paramBoolean);
    this.e.setUnderlineText(paramBoolean);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\TuSdkTextView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */