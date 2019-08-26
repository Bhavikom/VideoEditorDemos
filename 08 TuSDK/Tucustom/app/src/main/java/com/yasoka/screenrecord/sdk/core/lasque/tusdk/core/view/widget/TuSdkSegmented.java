// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget;

import java.util.Iterator;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
//import org.lasque.tusdk.core.view.TuSdkLinearLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkLinearLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;

import java.util.ArrayList;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkSegmented extends TuSdkRelativeLayout
{
    protected ArrayList<TuSdkSegmentedButton> buttons;
    protected TuSdkSegmentedButton currentButton;
    protected TuSdkLinearLayout segmentedWrap;
    protected TuSdkSegmentedDelegate mDelegate;
    private TuSdkViewHelper.OnSafeClickListener a;
    
    public TuSdkSegmented(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.buttons = new ArrayList<TuSdkSegmentedButton>();
        this.a = new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                TuSdkSegmented.this.onSegmentedClicked((TuSdkSegmentedButton)view);
            }
        };
    }
    
    public TuSdkSegmented(final Context context, final AttributeSet set) {
        super(context, set);
        this.buttons = new ArrayList<TuSdkSegmentedButton>();
        this.a = new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                TuSdkSegmented.this.onSegmentedClicked((TuSdkSegmentedButton)view);
            }
        };
    }
    
    public TuSdkSegmented(final Context context) {
        super(context);
        this.buttons = new ArrayList<TuSdkSegmentedButton>();
        this.a = new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                TuSdkSegmented.this.onSegmentedClicked((TuSdkSegmentedButton)view);
            }
        };
    }
    
    @Override
    public void loadView() {
        super.loadView();
        (this.segmentedWrap = this.findSegmentedWrap()).removeAllViews();
    }
    
    protected abstract TuSdkLinearLayout findSegmentedWrap();
    
    protected abstract <T extends View> T buildSegmented(final String p0);
    
    protected View buildSplitView() {
        return null;
    }
    
    public void addSegmentedText(final int... array) {
        final String[] array2 = new String[array.length];
        int n = 0;
        for (int length = array.length, i = 0; i < length; ++i) {
            array2[n] = this.getResString(array[i]);
            ++n;
        }
        this.addSegmentedText(array2);
    }
    
    public void addSegmentedText(final String... array) {
        View buildSplitView = null;
        for (final String title : array) {
            final View buildSegmented = this.buildSegmented(title);
            buildSegmented.setOnClickListener((View.OnClickListener)this.a);
            this.segmentedWrap.addView(buildSegmented);
            final TuSdkSegmentedButton e = (TuSdkSegmentedButton)buildSegmented;
            e.setTitle(title);
            this.buttons.add(e);
            buildSplitView = this.buildSplitView();
            if (buildSplitView != null) {
                this.segmentedWrap.addView(buildSplitView);
            }
        }
        if (buildSplitView != null) {
            this.segmentedWrap.removeView(buildSplitView);
        }
    }
    
    protected void onSegmentedClicked(final TuSdkSegmentedButton o) {
        if (o.isSegmentSelected()) {
            return;
        }
        final int index = this.buttons.indexOf(o);
        this.changeSelected(index);
        if (this.mDelegate != null) {
            this.mDelegate.onLasqueSegmentedSelected(this, index);
        }
    }
    
    public void setSegmentedDelegate(final TuSdkSegmentedDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }
    
    public void changeSelected(final int n) {
        if (n >= this.buttons.size()) {
            return;
        }
        int n2 = 0;
        final Iterator<TuSdkSegmentedButton> iterator = this.buttons.iterator();
        while (iterator.hasNext()) {
            iterator.next().onChangeSelected(n2 == n);
            ++n2;
        }
    }
    
    @Override
    public void viewWillDestory() {
        super.viewWillDestory();
        this.mDelegate = null;
        this.buttons.clear();
    }
    
    public interface TuSdkSegmentedButton
    {
        void onChangeSelected(final boolean p0);
        
        boolean isSegmentSelected();
        
        void setTitle(final String p0);
    }
    
    public interface TuSdkSegmentedDelegate
    {
        void onLasqueSegmentedSelected(final TuSdkSegmented p0, final int p1);
    }
}
