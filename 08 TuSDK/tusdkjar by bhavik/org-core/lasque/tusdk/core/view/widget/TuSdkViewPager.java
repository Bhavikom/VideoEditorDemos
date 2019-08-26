package org.lasque.tusdk.core.view.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import org.lasque.tusdk.core.view.TuSdkViewInterface;

public class TuSdkViewPager
  extends ViewPager
  implements TuSdkViewInterface
{
  private boolean a;
  private TuSdkViewPagerDelegate b;
  
  public TuSdkViewPagerDelegate getDelegate()
  {
    return this.b;
  }
  
  public void setDelegate(TuSdkViewPagerDelegate paramTuSdkViewPagerDelegate)
  {
    this.b = paramTuSdkViewPagerDelegate;
  }
  
  public TuSdkViewPager(Context paramContext)
  {
    super(paramContext);
    initView();
  }
  
  public TuSdkViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }
  
  protected void initView() {}
  
  public void loadView() {}
  
  public void viewDidLoad() {}
  
  public void viewNeedRest() {}
  
  public void viewWillDestory()
  {
    this.b = null;
  }
  
  protected void viewWillPrimary(int paramInt, Fragment paramFragment)
  {
    if (this.b != null) {
      this.b.onTuSdkViewPagerWillPrimary(paramInt, paramFragment);
    }
  }
  
  public void bindView(FragmentManager paramFragmentManager, TuSdkViewPagerDelegate paramTuSdkViewPagerDelegate)
  {
    this.b = paramTuSdkViewPagerDelegate;
    setAdapter(new TuSdkViewPagerAdapter(paramFragmentManager));
    setOnPageChangeListener(new PagerOnPageChangeListener(null));
  }
  
  public void reloadData()
  {
    if (getAdapter() != null) {
      getAdapter().notifyDataSetChanged();
    }
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    return this.a;
  }
  
  private class PagerOnPageChangeListener
    implements ViewPager.OnPageChangeListener
  {
    private int b;
    private boolean c;
    
    private PagerOnPageChangeListener() {}
    
    public void onPageScrollStateChanged(int paramInt) {}
    
    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
      if (this.c) {
        return;
      }
      if ((paramInt1 > 0) && (paramFloat == 0.0F) && (paramInt2 == 0)) {
        this.b += 1;
      } else {
        this.b = 0;
      }
      if ((this.b > 10) && (TuSdkViewPager.a(TuSdkViewPager.this) != null))
      {
        this.c = true;
        TuSdkViewPager.a(TuSdkViewPager.this).onTuSdkViewPagerScrolled();
      }
    }
    
    public void onPageSelected(int paramInt)
    {
      if (TuSdkViewPager.a(TuSdkViewPager.this) != null) {
        TuSdkViewPager.a(TuSdkViewPager.this).onTuSdkViewPagerChanged(paramInt);
      }
    }
  }
  
  private class TuSdkViewPagerAdapter
    extends FragmentStatePagerAdapter
  {
    public TuSdkViewPagerAdapter(FragmentManager paramFragmentManager)
    {
      super();
    }
    
    public void notifyDataSetChanged()
    {
      super.notifyDataSetChanged();
    }
    
    public int getItemPosition(Object paramObject)
    {
      return -2;
    }
    
    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      super.destroyItem(paramViewGroup, paramInt, paramObject);
    }
    
    public Fragment getItem(int paramInt)
    {
      if (TuSdkViewPager.a(TuSdkViewPager.this) != null) {
        return TuSdkViewPager.a(TuSdkViewPager.this).onTuSdkViewPagerBuild(paramInt);
      }
      return null;
    }
    
    public void setPrimaryItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      TuSdkViewPager.this.viewWillPrimary(paramInt, (Fragment)paramObject);
      super.setPrimaryItem(paramViewGroup, paramInt, paramObject);
    }
    
    public int getCount()
    {
      if (TuSdkViewPager.a(TuSdkViewPager.this) != null) {
        return TuSdkViewPager.a(TuSdkViewPager.this).tuSdkViewPagerTotal();
      }
      return 0;
    }
  }
  
  public static abstract class TuSdkViewPagerDelegateImpl
    implements TuSdkViewPager.TuSdkViewPagerDelegate
  {
    public void onTuSdkViewPagerWillPrimary(int paramInt, Fragment paramFragment) {}
    
    public void onTuSdkViewPagerChanged(int paramInt) {}
    
    public void onTuSdkViewPagerScrolled() {}
  }
  
  public static abstract interface TuSdkViewPagerDelegate
  {
    public abstract int tuSdkViewPagerTotal();
    
    public abstract void onTuSdkViewPagerWillPrimary(int paramInt, Fragment paramFragment);
    
    public abstract Fragment onTuSdkViewPagerBuild(int paramInt);
    
    public abstract void onTuSdkViewPagerChanged(int paramInt);
    
    public abstract void onTuSdkViewPagerScrolled();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\TuSdkViewPager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */