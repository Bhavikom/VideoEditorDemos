package org.lasque.tusdk.impl.components.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import org.lasque.tusdk.core.utils.anim.AnimHelper;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;

public class FilterConfigView
  extends TuSdkRelativeLayout
{
  private FilterConfigViewDelegate a;
  private View b;
  private View c;
  private View d;
  private LinearLayout e;
  private SelesParameters.FilterParameterInterface f;
  private ArrayList<FilterConfigSeekbar> g;
  private int h;
  private int i;
  protected FilterConfigSeekbar.FilterConfigSeekbarDelegate mFilterConfigSeekbarDelegate = new FilterConfigSeekbar.FilterConfigSeekbarDelegate()
  {
    public void onSeekbarDataChanged(FilterConfigSeekbar paramAnonymousFilterConfigSeekbar, SelesParameters.FilterArg paramAnonymousFilterArg)
    {
      FilterConfigView.this.requestRender();
    }
  };
  protected TuSdkViewHelper.OnSafeClickListener mOnClickListener = new TuSdkViewHelper.OnSafeClickListener()
  {
    public void onSafeClick(View paramAnonymousView)
    {
      if (FilterConfigView.this.equalViewIds(paramAnonymousView, FilterConfigView.this.getResetButton())) {
        FilterConfigView.this.handleResetAction();
      } else if (FilterConfigView.this.equalViewIds(paramAnonymousView, FilterConfigView.this.getStateButton())) {
        FilterConfigView.this.handleShowStateAction();
      }
    }
  };
  private boolean j;
  
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_filter_config_view");
  }
  
  public FilterConfigViewDelegate getDelegate()
  {
    return this.a;
  }
  
  public void setDelegate(FilterConfigViewDelegate paramFilterConfigViewDelegate)
  {
    this.a = paramFilterConfigViewDelegate;
  }
  
  public FilterConfigView(Context paramContext)
  {
    super(paramContext);
  }
  
  public FilterConfigView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public FilterConfigView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public View getResetButton()
  {
    if (this.b == null)
    {
      this.b = getViewById("lsq_resetButton");
      if (this.b != null) {
        this.b.setOnClickListener(this.mOnClickListener);
      }
    }
    return this.b;
  }
  
  public View getStateButton()
  {
    if (this.c == null)
    {
      this.c = getViewById("lsq_stateButton");
      if (this.c != null) {
        this.c.setOnClickListener(this.mOnClickListener);
      }
    }
    return this.c;
  }
  
  public View getStateBg()
  {
    if (this.d == null) {
      this.d = getViewById("lsq_stateBg");
    }
    return this.d;
  }
  
  public LinearLayout getConfigWrap()
  {
    if (this.e == null) {
      this.e = ((LinearLayout)getViewById("lsq_configWrap"));
    }
    return this.e;
  }
  
  public void loadView()
  {
    super.loadView();
    this.i = ContextUtils.dip2px(getContext(), 50.0F);
    showViewIn(getResetButton(), false);
    ViewCompat.setAlpha(getStateButton(), 0.7F);
    ViewCompat.setAlpha(getStateBg(), 0.0F);
    showViewIn(getConfigWrap(), false);
  }
  
  public void setSelesFilter(SelesOutInput paramSelesOutInput)
  {
    if ((paramSelesOutInput == null) || (!(paramSelesOutInput instanceof SelesParameters.FilterParameterInterface)))
    {
      hiddenDefault();
      return;
    }
    showViewIn(true);
    a(getConfigWrap(), (SelesParameters.FilterParameterInterface)paramSelesOutInput);
    AnimHelper.heightAnimation(getStateBg(), this.h);
  }
  
  public void hiddenDefault()
  {
    showViewIn(false);
    showViewIn(getResetButton(), false);
    showViewIn(getConfigWrap(), false);
    ViewCompat.setAlpha(getConfigWrap(), 0.0F);
    ViewCompat.setRotation(getStateButton(), 0.0F);
    ViewCompat.setAlpha(getStateButton(), 0.7F);
    ViewCompat.setAlpha(getStateBg(), 0.0F);
    setHeight(getStateBg(), 0);
  }
  
  private void a(LinearLayout paramLinearLayout, SelesParameters.FilterParameterInterface paramFilterParameterInterface)
  {
    this.f = paramFilterParameterInterface;
    if ((paramLinearLayout == null) || (this.f == null)) {
      return;
    }
    this.h = (paramLinearLayout.getTop() + this.i / 2);
    paramLinearLayout.removeAllViews();
    SelesParameters localSelesParameters = this.f.getParameter();
    if ((localSelesParameters == null) || (localSelesParameters.size() == 0))
    {
      hiddenDefault();
      return;
    }
    this.g = new ArrayList(localSelesParameters.size());
    Iterator localIterator = localSelesParameters.getArgs().iterator();
    while (localIterator.hasNext())
    {
      SelesParameters.FilterArg localFilterArg = (SelesParameters.FilterArg)localIterator.next();
      FilterConfigSeekbar localFilterConfigSeekbar = buildAppendSeekbar(paramLinearLayout, this.i);
      if (localFilterConfigSeekbar != null)
      {
        localFilterConfigSeekbar.setFilterArg(localFilterArg);
        localFilterConfigSeekbar.setDelegate(this.mFilterConfigSeekbarDelegate);
        this.g.add(localFilterConfigSeekbar);
        this.h += this.i;
      }
    }
  }
  
  public FilterConfigSeekbar buildAppendSeekbar(LinearLayout paramLinearLayout, int paramInt)
  {
    if (paramLinearLayout == null) {
      return null;
    }
    FilterConfigSeekbar localFilterConfigSeekbar = (FilterConfigSeekbar)TuSdkViewHelper.buildView(getContext(), FilterConfigSeekbar.getLayoutId(), paramLinearLayout);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, paramInt);
    paramLinearLayout.addView(localFilterConfigSeekbar, localLayoutParams);
    return localFilterConfigSeekbar;
  }
  
  protected void handleShowStateAction()
  {
    if ((getConfigWrap() == null) || (this.j)) {
      return;
    }
    this.j = true;
    final boolean bool = getConfigWrap().getVisibility() == 0;
    showViewIn(getResetButton(), !bool);
    showViewIn(getConfigWrap(), true);
    ViewCompat.animate(getConfigWrap()).alpha(bool ? 0.0F : 1.0F).setDuration(260L).setInterpolator(new AccelerateDecelerateInterpolator()).setListener(new ViewPropertyAnimatorListenerAdapter()
    {
      public void onAnimationEnd(View paramAnonymousView)
      {
        if (bool) {
          FilterConfigView.this.showViewIn(FilterConfigView.this.getConfigWrap(), false);
        }
        FilterConfigView.a(FilterConfigView.this, false);
      }
    });
    ViewCompat.animate(getStateButton()).rotation(bool ? 0.0F : 90.0F).alpha(bool ? 0.7F : 1.0F).setDuration(260L).setInterpolator(new AccelerateDecelerateInterpolator());
    ViewCompat.animate(getStateBg()).alpha(bool ? 0.0F : 1.0F).setDuration(260L).setInterpolator(new AccelerateDecelerateInterpolator());
    AnimHelper.heightAnimation(getStateBg(), bool ? 0 : this.h);
  }
  
  protected void handleResetAction()
  {
    if (this.g == null) {
      return;
    }
    Iterator localIterator = this.g.iterator();
    while (localIterator.hasNext())
    {
      FilterConfigSeekbar localFilterConfigSeekbar = (FilterConfigSeekbar)localIterator.next();
      localFilterConfigSeekbar.reset();
    }
    requestRender();
  }
  
  protected void requestRender()
  {
    if (this.f != null) {
      this.f.submitParameter();
    }
    if (this.a != null) {
      this.a.onFilterConfigRequestRender(this);
    }
  }
  
  public static abstract interface FilterConfigViewDelegate
  {
    public abstract void onFilterConfigRequestRender(FilterConfigView paramFilterConfigView);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\FilterConfigView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */