// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view;

import android.graphics.Canvas;
import android.graphics.Rect;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.ContextUtils;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.ViewGroup;
//import org.lasque.tusdk.core.TuSdkContext;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;

public class TuSdkRelativeLayout extends RelativeLayout implements TuSdkViewInterface
{
    protected boolean isLayouted;
    private ViewTreeObserver.OnPreDrawListener a;
    private TuSdkViewDrawer b;
    
    public TuSdkRelativeLayout(final Context context) {
        super(context);
        this.a = (ViewTreeObserver.OnPreDrawListener)new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                TuSdkRelativeLayout.this.getViewTreeObserver().removeOnPreDrawListener(TuSdkRelativeLayout.this.a);
                if (!TuSdkRelativeLayout.this.isLayouted) {
                    TuSdkRelativeLayout.this.isLayouted = true;
                    TuSdkRelativeLayout.this.onLayouted();
                }
                return false;
            }
        };
        this.initView();
    }
    
    public TuSdkRelativeLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.a = (ViewTreeObserver.OnPreDrawListener)new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                TuSdkRelativeLayout.this.getViewTreeObserver().removeOnPreDrawListener(TuSdkRelativeLayout.this.a);
                if (!TuSdkRelativeLayout.this.isLayouted) {
                    TuSdkRelativeLayout.this.isLayouted = true;
                    TuSdkRelativeLayout.this.onLayouted();
                }
                return false;
            }
        };
        this.initView();
    }
    
    public TuSdkRelativeLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.a = (ViewTreeObserver.OnPreDrawListener)new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                TuSdkRelativeLayout.this.getViewTreeObserver().removeOnPreDrawListener(TuSdkRelativeLayout.this.a);
                if (!TuSdkRelativeLayout.this.isLayouted) {
                    TuSdkRelativeLayout.this.isLayouted = true;
                    TuSdkRelativeLayout.this.onLayouted();
                }
                return false;
            }
        };
        this.initView();
    }
    
    protected void initView() {
        this.a();
    }
    
    private void a() {
        this.getViewTreeObserver().addOnPreDrawListener(this.a);
    }
    
    public void loadView() {
    }
    
    public void viewDidLoad() {
    }
    
    public boolean isLayouted() {
        return this.isLayouted;
    }
    
    protected void onLayouted() {
    }
    
    public <T extends View> T getViewById(final int n) {
        return TuSdkViewHelper.loadView(this.findViewById(n));
    }
    
    public <T extends View> T getViewById(final String s) {
        final int idResId = TuSdkContext.getIDResId(s);
        if (idResId == 0) {
            return null;
        }
        return (T)this.getViewById(idResId);
    }
    
    public int getViewId(final View view) {
        if (view == null) {
            return 0;
        }
        return view.getId();
    }
    
    public boolean equalViewIds(final View view, final View view2) {
        return this.getViewId(view) == this.getViewId(view2);
    }
    
    public <T extends View> T buildView(final int n) {
        return this.buildView(n, (ViewGroup)this);
    }
    
    public <T extends View> T buildView(final int n, final ViewGroup viewGroup) {
        return TuSdkViewHelper.buildView(this.getContext(), n, viewGroup);
    }
    
    public void setWeight(final float n) {
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.weight = 1.0f;
        this.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }
    
    public void showView(final boolean b) {
        this.showView((View)this, b);
    }
    
    public void showView(final View view, final boolean b) {
        TuSdkViewHelper.showView(view, b);
    }
    
    public void showViewIn(final boolean b) {
        TuSdkViewHelper.showViewIn((View)this, b);
    }
    
    public void showViewIn(final View view, final boolean b) {
        TuSdkViewHelper.showViewIn(view, b);
    }
    
    public String getTextViewText(final TextView textView) {
        return TuSdkViewHelper.getTextViewText(textView);
    }
    
    public void setTextViewText(final TextView textView, final String s) {
        TuSdkViewHelper.setTextViewText(textView, s);
    }
    
    public int locationInWindowTop() {
        return TuSdkViewHelper.locationInWindowTop((View)this);
    }
    
    public int locationInWindowTop(final View view) {
        return TuSdkViewHelper.locationInWindowTop(view);
    }
    
    public int viewInTop(final View view) {
        return this.locationInWindowTop(view) - this.locationInWindowTop();
    }
    
    public String getResString(final int n) {
        return ContextUtils.getResString(this.getContext(), n);
    }
    
    public String getResString(final int n, final Object... array) {
        return ContextUtils.getResString(this.getContext(), n, array);
    }
    
    public String getResString(final String s) {
        return TuSdkContext.getString(s);
    }
    
    public String getResString(final String s, final Object... array) {
        return TuSdkContext.getString(s, array);
    }
    
    public int getResColor(final int n) {
        return ContextUtils.getResColor(this.getContext(), n);
    }
    
    public LayoutParams getViewParams(final View view) {
        if (view == null) {
            return null;
        }
        return (LayoutParams)view.getLayoutParams();
    }
    
    public void setMarginLeft(final int n) {
        this.setMarginLeft((View)this, n);
    }
    
    public void setMarginLeft(final View view, final int n) {
        TuSdkViewHelper.setViewMarginLeft(view, n);
    }
    
    public void setMarginTop(final int n) {
        this.setMarginTop((View)this, n);
    }
    
    public void setMarginTop(final View view, final int n) {
        TuSdkViewHelper.setViewMarginTop(view, n);
    }
    
    public void setMarginRight(final int n) {
        this.setMarginRight((View)this, n);
    }
    
    public void setMarginRight(final View view, final int n) {
        TuSdkViewHelper.setViewMarginRight(view, n);
    }
    
    public void setMarginBottom(final int n) {
        this.setMarginBottom((View)this, n);
    }
    
    public void setMarginBottom(final View view, final int n) {
        TuSdkViewHelper.setViewMarginBottom(view, n);
    }
    
    public void setMargin(final int n, final int n2, final int n3, final int n4) {
        TuSdkViewHelper.setViewMargin((View)this, n, n2, n3, n4);
    }
    
    public void setMargin(final View view, final int n, final int n2, final int n3, final int n4) {
        if (view == null) {
            return;
        }
        final LayoutParams viewParams = this.getViewParams(view);
        if (viewParams == null) {
            return;
        }
        viewParams.setMargins(n, n2, n3, n4);
        view.setLayoutParams((ViewGroup.LayoutParams)viewParams);
    }
    
    public void setSize(final View view, final TuSdkSize tuSdkSize) {
        if (tuSdkSize == null) {
            return;
        }
        this.setViewSize(view, tuSdkSize.width, tuSdkSize.height);
    }
    
    public void setViewSize(final View view, final int width, final int height) {
        if (view == null) {
            return;
        }
        LayoutParams viewParams = this.getViewParams(view);
        if (viewParams == null) {
            viewParams = new LayoutParams(width, height);
        }
        else {
            viewParams.width = width;
            viewParams.height = height;
        }
        view.setLayoutParams((ViewGroup.LayoutParams)viewParams);
    }
    
    public void setHeight(final int n) {
        this.setHeight((View)this, n);
    }
    
    public void setWidth(final int n) {
        this.setWidth((View)this, n);
    }
    
    public void setHeight(final View view, final int n) {
        TuSdkViewHelper.setViewHeight(view, n);
    }
    
    public void setWidth(final View view, final int n) {
        TuSdkViewHelper.setViewWidth(view, n);
    }
    
    public void setRect(final Rect rect) {
        this.setRect((View)this, rect);
    }
    
    public void setRect(final View view, final Rect rect) {
        TuSdkViewHelper.setViewRect(view, rect);
    }
    
    public void postRequestLayout() {
        this.post((Runnable)new Runnable() {
            @Override
            public void run() {
                TuSdkRelativeLayout.this.requestLayout();
            }
        });
    }
    
    public void viewWillDestory() {
    }
    
    public void viewNeedRest() {
    }
    
    public TuSdkViewDrawer drawer() {
        if (this.b == null) {
            this.b = new TuSdkViewDrawer((View)this);
        }
        return this.b;
    }
    
    protected void dispatchDraw(final Canvas canvas) {
        this.dispatchDrawBefore(canvas);
        super.dispatchDraw(canvas);
        this.dispatchDrawAfter(canvas);
    }
    
    protected void dispatchDrawBefore(final Canvas canvas) {
        if (this.b != null) {
            this.b.dispatchDrawBefore(canvas);
        }
    }
    
    protected void dispatchDrawAfter(final Canvas canvas) {
        if (this.b != null) {
            this.b.dispatchDrawAfter(canvas);
        }
    }
}
