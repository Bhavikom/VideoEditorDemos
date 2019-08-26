// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget;

import android.view.ViewGroup;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MotionEvent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.view.TuSdkViewInterface;
import android.support.v4.view.ViewPager;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewInterface;

public class TuSdkViewPager extends ViewPager implements TuSdkViewInterface
{
    private boolean a;
    private TuSdkViewPagerDelegate b;
    
    public TuSdkViewPagerDelegate getDelegate() {
        return this.b;
    }
    
    public void setDelegate(final TuSdkViewPagerDelegate b) {
        this.b = b;
    }
    
    public TuSdkViewPager(final Context context) {
        super(context);
        this.initView();
    }
    
    public TuSdkViewPager(final Context context, final AttributeSet set) {
        super(context, set);
        this.initView();
    }
    
    protected void initView() {
    }
    
    public void loadView() {
    }
    
    public void viewDidLoad() {
    }
    
    public void viewNeedRest() {
    }
    
    public void viewWillDestory() {
        this.b = null;
    }
    
    protected void viewWillPrimary(final int n, final Fragment fragment) {
        if (this.b != null) {
            this.b.onTuSdkViewPagerWillPrimary(n, fragment);
        }
    }
    
    public void bindView(final FragmentManager fragmentManager, final TuSdkViewPagerDelegate b) {
        this.b = b;
        this.setAdapter((PagerAdapter)new TuSdkViewPagerAdapter(fragmentManager));
        this.setOnPageChangeListener((OnPageChangeListener)new PagerOnPageChangeListener());
    }
    
    public void reloadData() {
        if (this.getAdapter() != null) {
            this.getAdapter().notifyDataSetChanged();
        }
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return this.a;
    }
    
    private class PagerOnPageChangeListener implements OnPageChangeListener
    {
        private int b;
        private boolean c;
        
        public void onPageScrollStateChanged(final int n) {
        }
        
        public void onPageScrolled(final int n, final float n2, final int n3) {
            if (this.c) {
                return;
            }
            if (n > 0 && n2 == 0.0f && n3 == 0) {
                ++this.b;
            }
            else {
                this.b = 0;
            }
            if (this.b > 10 && TuSdkViewPager.this.b != null) {
                this.c = true;
                TuSdkViewPager.this.b.onTuSdkViewPagerScrolled();
            }
        }
        
        public void onPageSelected(final int n) {
            if (TuSdkViewPager.this.b != null) {
                TuSdkViewPager.this.b.onTuSdkViewPagerChanged(n);
            }
        }
    }
    
    public interface TuSdkViewPagerDelegate
    {
        int tuSdkViewPagerTotal();
        
        void onTuSdkViewPagerWillPrimary(final int p0, final Fragment p1);
        
        Fragment onTuSdkViewPagerBuild(final int p0);
        
        void onTuSdkViewPagerChanged(final int p0);
        
        void onTuSdkViewPagerScrolled();
    }
    
    private class TuSdkViewPagerAdapter extends FragmentStatePagerAdapter
    {
        public TuSdkViewPagerAdapter(final FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
        
        public int getItemPosition(final Object o) {
            return -2;
        }
        
        public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
            super.destroyItem(viewGroup, n, o);
        }
        
        public Fragment getItem(final int n) {
            if (TuSdkViewPager.this.b != null) {
                return TuSdkViewPager.this.b.onTuSdkViewPagerBuild(n);
            }
            return null;
        }
        
        public void setPrimaryItem(final ViewGroup viewGroup, final int n, final Object o) {
            TuSdkViewPager.this.viewWillPrimary(n, (Fragment)o);
            super.setPrimaryItem(viewGroup, n, o);
        }
        
        public int getCount() {
            if (TuSdkViewPager.this.b != null) {
                return TuSdkViewPager.this.b.tuSdkViewPagerTotal();
            }
            return 0;
        }
    }
    
    public abstract static class TuSdkViewPagerDelegateImpl implements TuSdkViewPagerDelegate
    {
        @Override
        public void onTuSdkViewPagerWillPrimary(final int n, final Fragment fragment) {
        }
        
        @Override
        public void onTuSdkViewPagerChanged(final int n) {
        }
        
        @Override
        public void onTuSdkViewPagerScrolled() {
        }
    }
}
