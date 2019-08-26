package org.lasque.tusdk.impl.view.widget.button;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
import org.lasque.tusdk.core.view.widget.button.TuSdkNavigatorButton;

public class TuNavigatorButton
  extends TuSdkNavigatorButton
{
  protected RelativeLayout buttonBg;
  protected TextView buttonTitle;
  protected ImageView dotView;
  protected TextView badgeView;
  private View.OnClickListener a;
  private View.OnClickListener b = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if (TuNavigatorButton.a(TuNavigatorButton.this) != null) {
        TuNavigatorButton.a(TuNavigatorButton.this).onClick(TuNavigatorButton.this);
      }
    }
  };
  
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_view_widget_navigator_button");
  }
  
  public TuNavigatorButton(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuNavigatorButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuNavigatorButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void loadView()
  {
    super.loadView();
    this.buttonTitle = ((TextView)getViewById("lsq_buttonTitle"));
    this.buttonBg = ((RelativeLayout)getViewById("lsq_buttonBg"));
    this.colorChangeListener = TuSdkTouchColorChangeListener.bindTouchDark(this.buttonBg);
    this.dotView = ((ImageView)getViewById("lsq_dotView"));
    this.dotView.setVisibility(8);
    this.badgeView = ((TextView)getViewById("lsq_badgeView"));
    this.badgeView.setVisibility(8);
  }
  
  public String getTitle()
  {
    return getTextViewText(this.buttonTitle);
  }
  
  public void setTitle(String paramString)
  {
    setTextViewText(this.buttonTitle, paramString);
  }
  
  public void showDot(boolean paramBoolean)
  {
    showView(this.dotView, paramBoolean);
  }
  
  public void setBadge(String paramString)
  {
    showView(this.badgeView, paramString != null);
    this.badgeView.setText(paramString);
  }
  
  public void setBackgroundResource(int paramInt)
  {
    if (this.buttonBg != null) {
      this.buttonBg.setBackgroundResource(paramInt);
    }
  }
  
  public void setTextColor(int paramInt)
  {
    if (paramInt != 0) {
      this.buttonTitle.setTextColor(paramInt);
    }
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    if ((this.colorChangeListener != null) && (this.buttonBg != null))
    {
      this.colorChangeListener.enabledChanged(this.buttonBg, paramBoolean);
      this.buttonBg.setEnabled(paramBoolean);
    }
    super.setEnabled(paramBoolean);
  }
  
  public void setOnClickListener(View.OnClickListener paramOnClickListener)
  {
    this.a = paramOnClickListener;
    if (this.buttonBg != null) {
      this.buttonBg.setOnClickListener(paramOnClickListener == null ? null : this.b);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\button\TuNavigatorButton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */