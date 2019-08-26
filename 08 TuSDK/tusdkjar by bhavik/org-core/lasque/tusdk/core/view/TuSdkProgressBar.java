package org.lasque.tusdk.core.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import org.lasque.tusdk.core.utils.anim.AnimHelper.TuSdkViewAnimatorAdapter;

public class TuSdkProgressBar
  extends ProgressBar
  implements TuSdkViewInterface
{
  private boolean a;
  private AnimHelper.TuSdkViewAnimatorAdapter b = new AnimHelper.TuSdkViewAnimatorAdapter()
  {
    public void onAnimationEnd(View paramAnonymousView, boolean paramAnonymousBoolean)
    {
      if (paramAnonymousBoolean) {
        return;
      }
      if (!TuSdkProgressBar.a(TuSdkProgressBar.this)) {
        TuSdkProgressBar.this.setVisibility(8);
      }
    }
  };
  
  public TuSdkProgressBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  public TuSdkProgressBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }
  
  public TuSdkProgressBar(Context paramContext)
  {
    super(paramContext);
    initView();
  }
  
  protected void initView() {}
  
  public void loadView()
  {
    setMax(100);
  }
  
  public void viewDidLoad() {}
  
  public void viewNeedRest() {}
  
  public void viewWillDestory() {}
  
  public void showWithAnim(boolean paramBoolean)
  {
    this.a = paramBoolean;
    float f = 1.0F;
    if (!paramBoolean) {
      f = 0.0F;
    }
    setVisibility(0);
    ViewCompat.animate(this).alpha(f).setDuration(240L).setListener(this.b);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\TuSdkProgressBar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */