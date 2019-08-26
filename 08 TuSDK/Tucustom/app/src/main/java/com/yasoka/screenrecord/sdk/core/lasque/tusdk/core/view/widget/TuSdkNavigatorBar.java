// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget;

//import org.lasque.tusdk.core.utils.ContextUtils;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
//import org.lasque.tusdk.core.view.widget.button.TuSdkNavigatorButton;
//import org.lasque.tusdk.core.view.widget.button.TuSdkNavigatorBackButton;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.button.TuSdkNavigatorBackButton;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.button.TuSdkNavigatorButton;

import java.util.HashMap;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkNavigatorBar extends TuSdkRelativeLayout
{
    private int a;
    public TuSdkNavigatorBarDelegate delegate;
    private boolean b;
    private TuSdkSegmented c;
    private TuSdkSearchView d;
    private HashMap<NavigatorBarButtonType, NavigatorBarButtonInterface> e;
    private TuSdkViewHelper.OnSafeClickListener f;
    
    public boolean isBackButtonShowed() {
        return this.b;
    }
    
    public TuSdkNavigatorBar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.e = new HashMap<NavigatorBarButtonType, NavigatorBarButtonInterface>();
        this.f = new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                TuSdkNavigatorBar.this.onButtonClicked(view);
            }
        };
    }
    
    public TuSdkNavigatorBar(final Context context, final AttributeSet set) {
        super(context, set);
        this.e = new HashMap<NavigatorBarButtonType, NavigatorBarButtonInterface>();
        this.f = new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                TuSdkNavigatorBar.this.onButtonClicked(view);
            }
        };
    }
    
    public TuSdkNavigatorBar(final Context context) {
        super(context);
        this.e = new HashMap<NavigatorBarButtonType, NavigatorBarButtonInterface>();
        this.f = new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                TuSdkNavigatorBar.this.onButtonClicked(view);
            }
        };
    }
    
    public void setButtonLayoutId(final int a) {
        this.a = a;
    }
    
    public void setBackButtonId(final int n) {
        this.a(n);
    }
    
    private void a(final int n) {
        final TuSdkNavigatorBackButton value = this.getViewById(n);
        if (value == null) {
            return;
        }
        value.setType(NavigatorBarButtonType.back);
        value.setOnClickListener((View.OnClickListener)this.f);
        this.e.put(value.getType(), value);
    }
    
    public NavigatorBarButtonInterface getButton(final NavigatorBarButtonType key) {
        if (key == null) {
            return null;
        }
        return this.e.get(key);
    }
    
    private void a(final TuSdkNavigatorButton value) {
        if (value == null || value.getType() == null || value.getType() == NavigatorBarButtonType.back) {
            return;
        }
        if (value.getType() == NavigatorBarButtonType.left) {
            this.showBackButton(false);
        }
        this.b(value.getType());
        this.e.put(value.getType(), value);
        this.addView((View)value);
        value.setOnClickListener((View.OnClickListener)this.f);
    }
    
    public TuSdkNavigatorButton setButton(final String title, final TuSdkNavButtonStyleInterface tuSdkNavButtonStyleInterface, final NavigatorBarButtonType navigatorBarButtonType) {
        final TuSdkNavigatorButton a = this.a(navigatorBarButtonType);
        if (a == null) {
            return null;
        }
        if (tuSdkNavButtonStyleInterface != null && tuSdkNavButtonStyleInterface.getBackgroundId() != 0) {
            a.setBackgroundResource(tuSdkNavButtonStyleInterface.getBackgroundId());
        }
        a.setTitle(title);
        this.a(a);
        return a;
    }
    
    private TuSdkNavigatorButton a(final NavigatorBarButtonType type) {
        if (this.a == 0 || type == null || type == NavigatorBarButtonType.back) {
            return null;
        }
        final TuSdkNavigatorButton tuSdkNavigatorButton = this.buildView(this.a);
        if (tuSdkNavigatorButton == null) {
            return null;
        }
        this.a(type, tuSdkNavigatorButton);
        tuSdkNavigatorButton.loadView();
        tuSdkNavigatorButton.setType(type);
        return tuSdkNavigatorButton;
    }
    
    private void a(final NavigatorBarButtonType navigatorBarButtonType, final TuSdkNavigatorButton tuSdkNavigatorButton) {
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tuSdkNavigatorButton.getLayoutParams();
        layoutParams.addRule(15);
        switch (navigatorBarButtonType.ordinal()) {
            case 1: {
                layoutParams.addRule(9);
                break;
            }
            case 2: {
                layoutParams.addRule(11);
                break;
            }
        }
        tuSdkNavigatorButton.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }
    
    public void showBackButton(final boolean b) {
        final NavigatorBarButtonInterface button = this.getButton(NavigatorBarButtonType.back);
        if (button == null) {
            return;
        }
        this.b = b;
        button.setVisibility(b ? 0 : 8);
    }
    
    private void b(final NavigatorBarButtonType navigatorBarButtonType) {
        final NavigatorBarButtonInterface button = this.getButton(navigatorBarButtonType);
        if (button == null) {
            return;
        }
        this.removeView((View)button);
    }
    
    public abstract void setTitle(final String p0);
    
    public abstract void setTitle(final int p0);
    
    public abstract String getTitle();
    
    protected void onButtonClicked(final View view) {
        if (this.delegate == null) {
            return;
        }
        this.delegate.onNavigatorBarButtonClicked((NavigatorBarButtonInterface)view);
    }
    
    protected TuSdkSegmented buildSegmented(final int n) {
        if (this.c != null || n == 0) {
            return this.c;
        }
        this.c = this.buildView(n);
        final RelativeLayout.LayoutParams layoutParams3;
        final RelativeLayout.LayoutParams layoutParams2;
        final RelativeLayout.LayoutParams layoutParams = layoutParams2 = (layoutParams3 = new RelativeLayout.LayoutParams(-1, -1));
        final int dip2px = ContextUtils.dip2px(this.getContext(), 7.0f);
        layoutParams2.topMargin = dip2px;
        layoutParams3.bottomMargin = dip2px;
        final RelativeLayout.LayoutParams layoutParams4 = layoutParams;
        final RelativeLayout.LayoutParams layoutParams5 = layoutParams;
        final int dip2px2 = ContextUtils.dip2px(this.getContext(), 70.0f);
        layoutParams5.rightMargin = dip2px2;
        layoutParams4.leftMargin = dip2px2;
        this.c.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        return this.c;
    }
    
    public TuSdkSegmented getSegmented() {
        return this.c;
    }
    
    public void addSegmentedText(final int... array) {
        final TuSdkSegmented segmented = this.getSegmented();
        if (segmented == null) {
            return;
        }
        segmented.addSegmentedText(array);
    }
    
    public void addSegmentedText(final String... array) {
        final TuSdkSegmented segmented = this.getSegmented();
        if (segmented == null) {
            return;
        }
        segmented.addSegmentedText(array);
    }
    
    public void setSegmentedDelegate(final TuSdkSegmented.TuSdkSegmentedDelegate segmentedDelegate) {
        final TuSdkSegmented segmented = this.getSegmented();
        if (segmented == null) {
            return;
        }
        segmented.setSegmentedDelegate(segmentedDelegate);
    }
    
    public void setSegmentedSelected(final int n) {
        final TuSdkSegmented segmented = this.getSegmented();
        if (segmented == null) {
            return;
        }
        segmented.changeSelected(n);
    }
    
    public TuSdkSearchView getSearchView() {
        return this.d;
    }
    
    protected TuSdkSearchView buildSearchView(final int n) {
        if (this.d != null || n == 0) {
            return this.d;
        }
        this.d = this.buildView(n);
        final RelativeLayout.LayoutParams layoutParams3;
        final RelativeLayout.LayoutParams layoutParams2;
        final RelativeLayout.LayoutParams layoutParams = layoutParams2 = (layoutParams3 = new RelativeLayout.LayoutParams(-1, -1));
        final int dip2px = ContextUtils.dip2px(this.getContext(), 5.0f);
        layoutParams2.topMargin = dip2px;
        layoutParams3.bottomMargin = dip2px;
        layoutParams.leftMargin = ContextUtils.dip2px(this.getContext(), 70.0f);
        this.d.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        return this.d;
    }
    
    public void setSearchViewDelegate(final TuSdkSearchView.TuSdkSearchViewDelegate delegate) {
        final TuSdkSearchView searchView = this.getSearchView();
        if (searchView == null) {
            return;
        }
        searchView.setDelegate(delegate);
    }
    
    public void searchKeyword(final String textAndSubmit) {
        final TuSdkSearchView searchView = this.getSearchView();
        if (searchView == null) {
            return;
        }
        searchView.setTextAndSubmit(textAndSubmit);
    }
    
    @Override
    public void viewWillDestory() {
        super.viewWillDestory();
        if (this.c != null) {
            this.c.viewWillDestory();
        }
        if (this.d != null) {
            this.d.viewWillDestory();
        }
    }
    
    public interface TuSdkNavigatorBarDelegate
    {
        void onNavigatorBarButtonClicked(final NavigatorBarButtonInterface p0);
    }
    
    public interface NavigatorBarButtonInterface
    {
        void setVisibility(final int p0);
        
        NavigatorBarButtonType getType();
        
        void setType(final NavigatorBarButtonType p0);
        
        String getTitle();
        
        void setTitle(final String p0);
        
        void showDot(final boolean p0);
        
        void setBadge(final String p0);
        
        void setEnabled(final boolean p0);
        
        void setTextColor(final int p0);
    }
    
    public enum NavigatorBarButtonType
    {
        back, 
        left, 
        right;
    }
    
    public interface TuSdkNavButtonStyleInterface
    {
        int getBackgroundId();
    }
}
