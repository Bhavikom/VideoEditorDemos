// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget;

import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.animation.Interpolator;
//import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import android.view.ViewGroup;
import java.util.Iterator;
//import org.lasque.tusdk.core.utils.anim.AnimHelper;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import android.support.v4.view.ViewCompat;
//import org.lasque.tusdk.core.utils.ContextUtils;
import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import java.util.ArrayList;
//import org.lasque.tusdk.core.seles.SelesParameters;
import android.widget.LinearLayout;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AnimHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public class FilterConfigView extends TuSdkRelativeLayout
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
    protected FilterConfigSeekbar.FilterConfigSeekbarDelegate mFilterConfigSeekbarDelegate;
    protected TuSdkViewHelper.OnSafeClickListener mOnClickListener;
    private boolean j;
    
    public static int getLayoutId() {
        return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_filter_config_view");
    }
    
    public FilterConfigViewDelegate getDelegate() {
        return this.a;
    }
    
    public void setDelegate(final FilterConfigViewDelegate a) {
        this.a = a;
    }
    
    public FilterConfigView(final Context context) {
        super(context);
        this.mFilterConfigSeekbarDelegate = new FilterConfigSeekbar.FilterConfigSeekbarDelegate() {
            @Override
            public void onSeekbarDataChanged(final FilterConfigSeekbar filterConfigSeekbar, final SelesParameters.FilterArg filterArg) {
                FilterConfigView.this.requestRender();
            }
        };
        this.mOnClickListener = new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                if (FilterConfigView.this.equalViewIds(view, FilterConfigView.this.getResetButton())) {
                    FilterConfigView.this.handleResetAction();
                }
                else if (FilterConfigView.this.equalViewIds(view, FilterConfigView.this.getStateButton())) {
                    FilterConfigView.this.handleShowStateAction();
                }
            }
        };
    }
    
    public FilterConfigView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mFilterConfigSeekbarDelegate = new FilterConfigSeekbar.FilterConfigSeekbarDelegate() {
            @Override
            public void onSeekbarDataChanged(final FilterConfigSeekbar filterConfigSeekbar, final SelesParameters.FilterArg filterArg) {
                FilterConfigView.this.requestRender();
            }
        };
        this.mOnClickListener = new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                if (FilterConfigView.this.equalViewIds(view, FilterConfigView.this.getResetButton())) {
                    FilterConfigView.this.handleResetAction();
                }
                else if (FilterConfigView.this.equalViewIds(view, FilterConfigView.this.getStateButton())) {
                    FilterConfigView.this.handleShowStateAction();
                }
            }
        };
    }
    
    public FilterConfigView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mFilterConfigSeekbarDelegate = new FilterConfigSeekbar.FilterConfigSeekbarDelegate() {
            @Override
            public void onSeekbarDataChanged(final FilterConfigSeekbar filterConfigSeekbar, final SelesParameters.FilterArg filterArg) {
                FilterConfigView.this.requestRender();
            }
        };
        this.mOnClickListener = new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                if (FilterConfigView.this.equalViewIds(view, FilterConfigView.this.getResetButton())) {
                    FilterConfigView.this.handleResetAction();
                }
                else if (FilterConfigView.this.equalViewIds(view, FilterConfigView.this.getStateButton())) {
                    FilterConfigView.this.handleShowStateAction();
                }
            }
        };
    }
    
    public View getResetButton() {
        if (this.b == null) {
            this.b = this.getViewById("lsq_resetButton");
            if (this.b != null) {
                this.b.setOnClickListener((View.OnClickListener)this.mOnClickListener);
            }
        }
        return this.b;
    }
    
    public View getStateButton() {
        if (this.c == null) {
            this.c = this.getViewById("lsq_stateButton");
            if (this.c != null) {
                this.c.setOnClickListener((View.OnClickListener)this.mOnClickListener);
            }
        }
        return this.c;
    }
    
    public View getStateBg() {
        if (this.d == null) {
            this.d = this.getViewById("lsq_stateBg");
        }
        return this.d;
    }
    
    public LinearLayout getConfigWrap() {
        if (this.e == null) {
            this.e = this.getViewById("lsq_configWrap");
        }
        return this.e;
    }
    
    @Override
    public void loadView() {
        super.loadView();
        this.i = ContextUtils.dip2px(this.getContext(), 50.0f);
        this.showViewIn(this.getResetButton(), false);
        ViewCompat.setAlpha(this.getStateButton(), 0.7f);
        ViewCompat.setAlpha(this.getStateBg(), 0.0f);
        this.showViewIn((View)this.getConfigWrap(), false);
    }
    
    public void setSelesFilter(final SelesOutInput selesOutInput) {
        if (selesOutInput == null || !(selesOutInput instanceof SelesParameters.FilterParameterInterface)) {
            this.hiddenDefault();
            return;
        }
        this.showViewIn(true);
        this.a(this.getConfigWrap(), (SelesParameters.FilterParameterInterface)selesOutInput);
        AnimHelper.heightAnimation(this.getStateBg(), this.h);
    }
    
    public void hiddenDefault() {
        this.showViewIn(false);
        this.showViewIn(this.getResetButton(), false);
        this.showViewIn((View)this.getConfigWrap(), false);
        ViewCompat.setAlpha((View)this.getConfigWrap(), 0.0f);
        ViewCompat.setRotation(this.getStateButton(), 0.0f);
        ViewCompat.setAlpha(this.getStateButton(), 0.7f);
        ViewCompat.setAlpha(this.getStateBg(), 0.0f);
        this.setHeight(this.getStateBg(), 0);
    }
    
    private void a(final LinearLayout linearLayout, final SelesParameters.FilterParameterInterface f) {
        this.f = f;
        if (linearLayout == null || this.f == null) {
            return;
        }
        this.h = linearLayout.getTop() + this.i / 2;
        linearLayout.removeAllViews();
        final SelesParameters parameter = this.f.getParameter();
        if (parameter == null || parameter.size() == 0) {
            this.hiddenDefault();
            return;
        }
        this.g = new ArrayList<FilterConfigSeekbar>(parameter.size());
        for (final SelesParameters.FilterArg filterArg : parameter.getArgs()) {
            final FilterConfigSeekbar buildAppendSeekbar = this.buildAppendSeekbar(linearLayout, this.i);
            if (buildAppendSeekbar != null) {
                buildAppendSeekbar.setFilterArg(filterArg);
                buildAppendSeekbar.setDelegate(this.mFilterConfigSeekbarDelegate);
                this.g.add(buildAppendSeekbar);
                this.h += this.i;
            }
        }
    }
    
    public FilterConfigSeekbar buildAppendSeekbar(final LinearLayout linearLayout, final int n) {
        if (linearLayout == null) {
            return null;
        }
        final FilterConfigSeekbar filterConfigSeekbar = TuSdkViewHelper.buildView(this.getContext(), FilterConfigSeekbar.getLayoutId(), (ViewGroup)linearLayout);
        linearLayout.addView((View)filterConfigSeekbar, (ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-1, n));
        return filterConfigSeekbar;
    }
    
    protected void handleShowStateAction() {
        if (this.getConfigWrap() == null || this.j) {
            return;
        }
        this.j = true;
        final boolean b = this.getConfigWrap().getVisibility() == VISIBLE;
        this.showViewIn(this.getResetButton(), !b);
        this.showViewIn((View)this.getConfigWrap(), true);
        ViewCompat.animate((View)this.getConfigWrap()).alpha(b ? 0.0f : 1.0f).setDuration(260L).setInterpolator((Interpolator)new AccelerateDecelerateInterpolator()).setListener((ViewPropertyAnimatorListener)new ViewPropertyAnimatorListenerAdapter() {
            public void onAnimationEnd(final View view) {
                if (b) {
                    FilterConfigView.this.showViewIn((View)FilterConfigView.this.getConfigWrap(), false);
                }
                FilterConfigView.this.j = false;
            }
        });
        ViewCompat.animate(this.getStateButton()).rotation(b ? 0.0f : 90.0f).alpha(b ? 0.7f : 1.0f).setDuration(260L).setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        ViewCompat.animate(this.getStateBg()).alpha(b ? 0.0f : 1.0f).setDuration(260L).setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        AnimHelper.heightAnimation(this.getStateBg(), b ? 0 : this.h);
    }
    
    protected void handleResetAction() {
        if (this.g == null) {
            return;
        }
        final Iterator<FilterConfigSeekbar> iterator = this.g.iterator();
        while (iterator.hasNext()) {
            iterator.next().reset();
        }
        this.requestRender();
    }
    
    protected void requestRender() {
        if (this.f != null) {
            this.f.submitParameter();
        }
        if (this.a != null) {
            this.a.onFilterConfigRequestRender(this);
        }
    }
    
    public interface FilterConfigViewDelegate
    {
        void onFilterConfigRequestRender(final FilterConfigView p0);
    }
}
