package org.lasque.tusdk.modules.view.widget.filter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.util.AttributeSet;
import android.view.View;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.anim.AnimHelper.TuSdkViewAnimatorAdapter;
import org.lasque.tusdk.core.view.TuSdkLinearLayout;

public abstract class FilterSubtitleViewBase
  extends TuSdkLinearLayout
  implements FilterSubtitleViewInterface
{
  private Runnable a = new Runnable()
  {
    public void run()
    {
      FilterSubtitleViewBase.b(FilterSubtitleViewBase.this);
    }
  };
  
  public FilterSubtitleViewBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public FilterSubtitleViewBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public FilterSubtitleViewBase(Context paramContext)
  {
    super(paramContext);
  }
  
  protected String getGroupName(FilterOption paramFilterOption)
  {
    if (paramFilterOption == null) {
      return null;
    }
    return TuSdkContext.getString(FilterLocalPackage.shared().getGroupNameKey(paramFilterOption.groupId));
  }
  
  protected void startScaleAnimation()
  {
    ThreadHelper.cancel(this.a);
    ViewCompat.setAlpha(this, 0.0F);
    ViewCompat.setScaleX(this, 2.0F);
    ViewCompat.setScaleY(this, 2.0F);
    showViewIn(true);
    ViewCompat.animate(this).alpha(1.0F).scaleX(1.0F).scaleY(1.0F).setDuration(220L).setListener(new AnimHelper.TuSdkViewAnimatorAdapter()
    {
      public void onAnimationEnd(View paramAnonymousView, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean) {
          return;
        }
        ThreadHelper.postDelayed(FilterSubtitleViewBase.a(FilterSubtitleViewBase.this), 500L);
      }
    });
  }
  
  private void a()
  {
    ViewCompat.animate(this).alpha(0.0F).setDuration(200L).setListener(new AnimHelper.TuSdkViewAnimatorAdapter()
    {
      public void onAnimationEnd(View paramAnonymousView, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean) {
          return;
        }
        paramAnonymousView.setVisibility(4);
      }
    });
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\filter\FilterSubtitleViewBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */