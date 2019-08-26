package org.lasque.tusdk.core.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;

public class TuSdkLinearLayout
  extends LinearLayout
  implements TuSdkViewInterface
{
  protected boolean mIsLayouted;
  private ViewTreeObserver.OnPreDrawListener a = new ViewTreeObserver.OnPreDrawListener()
  {
    public boolean onPreDraw()
    {
      TuSdkLinearLayout.this.getViewTreeObserver().removeOnPreDrawListener(TuSdkLinearLayout.a(TuSdkLinearLayout.this));
      if (!TuSdkLinearLayout.this.mIsLayouted)
      {
        TuSdkLinearLayout.this.mIsLayouted = true;
        TuSdkLinearLayout.this.onLayouted();
      }
      return false;
    }
  };
  private TuSdkViewDrawer b;
  
  public TuSdkLinearLayout(Context paramContext)
  {
    super(paramContext);
    initView();
  }
  
  public TuSdkLinearLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }
  
  @TargetApi(11)
  public TuSdkLinearLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  protected void initView()
  {
    a();
  }
  
  private void a()
  {
    ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
    localViewTreeObserver.addOnPreDrawListener(this.a);
  }
  
  public void loadView() {}
  
  public void viewDidLoad() {}
  
  public boolean isLayouted()
  {
    return this.mIsLayouted;
  }
  
  protected void onLayouted() {}
  
  public <T extends View> T getViewById(int paramInt)
  {
    View localView = findViewById(paramInt);
    return TuSdkViewHelper.loadView(localView);
  }
  
  public <T extends View> T getViewById(String paramString)
  {
    int i = TuSdkContext.getIDResId(paramString);
    if (i == 0) {
      return null;
    }
    return getViewById(i);
  }
  
  public int getViewId(View paramView)
  {
    if (paramView == null) {
      return 0;
    }
    return paramView.getId();
  }
  
  public boolean equalViewIds(View paramView1, View paramView2)
  {
    return getViewId(paramView1) == getViewId(paramView2);
  }
  
  public <T extends View> T buildView(int paramInt)
  {
    return buildView(paramInt, this);
  }
  
  public <T extends View> T buildView(int paramInt, ViewGroup paramViewGroup)
  {
    return TuSdkViewHelper.buildView(getContext(), paramInt, paramViewGroup);
  }
  
  public void showView(boolean paramBoolean)
  {
    showView(this, paramBoolean);
  }
  
  public void showView(View paramView, boolean paramBoolean)
  {
    TuSdkViewHelper.showView(paramView, paramBoolean);
  }
  
  public void showViewIn(boolean paramBoolean)
  {
    TuSdkViewHelper.showViewIn(this, paramBoolean);
  }
  
  public void showViewIn(View paramView, boolean paramBoolean)
  {
    TuSdkViewHelper.showViewIn(paramView, paramBoolean);
  }
  
  public String getTextViewText(TextView paramTextView)
  {
    return TuSdkViewHelper.getTextViewText(paramTextView);
  }
  
  public void setTextViewText(TextView paramTextView, String paramString)
  {
    TuSdkViewHelper.setTextViewText(paramTextView, paramString);
  }
  
  public int locationInWindowTop()
  {
    return TuSdkViewHelper.locationInWindowTop(this);
  }
  
  public int locationInWindowTop(View paramView)
  {
    return TuSdkViewHelper.locationInWindowTop(paramView);
  }
  
  public int viewInTop(View paramView)
  {
    return locationInWindowTop(paramView) - locationInWindowTop();
  }
  
  public String getResString(int paramInt)
  {
    return ContextUtils.getResString(getContext(), paramInt);
  }
  
  public String getResString(int paramInt, Object... paramVarArgs)
  {
    return ContextUtils.getResString(getContext(), paramInt, paramVarArgs);
  }
  
  public String getResString(String paramString)
  {
    return TuSdkContext.getString(paramString);
  }
  
  public String getResString(String paramString, Object... paramVarArgs)
  {
    return TuSdkContext.getString(paramString, paramVarArgs);
  }
  
  public int getResColor(int paramInt)
  {
    return ContextUtils.getResColor(getContext(), paramInt);
  }
  
  public LinearLayout.LayoutParams getViewParams(View paramView)
  {
    if (paramView == null) {
      return null;
    }
    return (LinearLayout.LayoutParams)paramView.getLayoutParams();
  }
  
  public void setMarginLeft(int paramInt)
  {
    setMarginLeft(this, paramInt);
  }
  
  public void setMarginLeft(View paramView, int paramInt)
  {
    TuSdkViewHelper.setViewMarginLeft(paramView, paramInt);
  }
  
  public void setMarginTop(int paramInt)
  {
    setMarginTop(this, paramInt);
  }
  
  public void setMarginTop(View paramView, int paramInt)
  {
    TuSdkViewHelper.setViewMarginTop(paramView, paramInt);
  }
  
  public void setMarginRight(int paramInt)
  {
    setMarginRight(this, paramInt);
  }
  
  public void setMarginRight(View paramView, int paramInt)
  {
    TuSdkViewHelper.setViewMarginRight(paramView, paramInt);
  }
  
  public void setMarginBottom(int paramInt)
  {
    setMarginBottom(this, paramInt);
  }
  
  public void setMarginBottom(View paramView, int paramInt)
  {
    TuSdkViewHelper.setViewMarginBottom(paramView, paramInt);
  }
  
  public void setMargin(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    TuSdkViewHelper.setViewMargin(this, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setMargin(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramView == null) {
      return;
    }
    LinearLayout.LayoutParams localLayoutParams = getViewParams(paramView);
    if (localLayoutParams == null) {
      return;
    }
    localLayoutParams.setMargins(paramInt1, paramInt2, paramInt3, paramInt4);
    paramView.setLayoutParams(localLayoutParams);
  }
  
  public void setSize(View paramView, TuSdkSize paramTuSdkSize)
  {
    if (paramTuSdkSize == null) {
      return;
    }
    setViewSize(paramView, paramTuSdkSize.width, paramTuSdkSize.height);
  }
  
  public void setViewSize(View paramView, int paramInt1, int paramInt2)
  {
    if (paramView == null) {
      return;
    }
    LinearLayout.LayoutParams localLayoutParams = getViewParams(paramView);
    if (localLayoutParams == null)
    {
      localLayoutParams = new LinearLayout.LayoutParams(paramInt1, paramInt2);
    }
    else
    {
      localLayoutParams.width = paramInt1;
      localLayoutParams.height = paramInt2;
    }
    paramView.setLayoutParams(localLayoutParams);
  }
  
  public void setHeight(int paramInt)
  {
    setHeight(this, paramInt);
  }
  
  public void setWidth(int paramInt)
  {
    setWidth(this, paramInt);
  }
  
  public void setHeight(View paramView, int paramInt)
  {
    TuSdkViewHelper.setViewHeight(paramView, paramInt);
  }
  
  public void setWidth(View paramView, int paramInt)
  {
    TuSdkViewHelper.setViewWidth(paramView, paramInt);
  }
  
  public void postRequestLayout()
  {
    post(new Runnable()
    {
      public void run()
      {
        TuSdkLinearLayout.this.requestLayout();
      }
    });
  }
  
  public void viewWillDestory() {}
  
  public void viewNeedRest() {}
  
  public TuSdkViewDrawer drawer()
  {
    if (this.b == null) {
      this.b = new TuSdkViewDrawer(this);
    }
    return this.b;
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    dispatchDrawBefore(paramCanvas);
    super.dispatchDraw(paramCanvas);
    dispatchDrawAfter(paramCanvas);
  }
  
  protected void dispatchDrawBefore(Canvas paramCanvas)
  {
    if (this.b != null) {
      this.b.dispatchDrawBefore(paramCanvas);
    }
  }
  
  protected void dispatchDrawAfter(Canvas paramCanvas)
  {
    if (this.b != null) {
      this.b.dispatchDrawAfter(paramCanvas);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\TuSdkLinearLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */