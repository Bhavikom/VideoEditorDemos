// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.button;

//import org.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.TuSdkContext;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.button.TuSdkNavigatorButton;
//import org.lasque.tusdk.core.view.widget.button.TuSdkNavigatorButton;

public class TuNavigatorButton extends TuSdkNavigatorButton
{
    protected RelativeLayout buttonBg;
    protected TextView buttonTitle;
    protected ImageView dotView;
    protected TextView badgeView;
    private View.OnClickListener a;
    private View.OnClickListener b;
    
    public static int getLayoutId() {
        return TuSdkContext.getLayoutResId("tusdk_view_widget_navigator_button");
    }
    
    public TuNavigatorButton(final Context context) {
        super(context);
        this.b = (View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                if (TuNavigatorButton.this.a != null) {
                    TuNavigatorButton.this.a.onClick((View)TuNavigatorButton.this);
                }
            }
        };
    }
    
    public TuNavigatorButton(final Context context, final AttributeSet set) {
        super(context, set);
        this.b = (View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                if (TuNavigatorButton.this.a != null) {
                    TuNavigatorButton.this.a.onClick((View)TuNavigatorButton.this);
                }
            }
        };
    }
    
    public TuNavigatorButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.b = (View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                if (TuNavigatorButton.this.a != null) {
                    TuNavigatorButton.this.a.onClick((View)TuNavigatorButton.this);
                }
            }
        };
    }
    
    @Override
    public void loadView() {
        super.loadView();
        this.buttonTitle = this.getViewById("lsq_buttonTitle");
        this.buttonBg = this.getViewById("lsq_buttonBg");
        this.colorChangeListener = TuSdkTouchColorChangeListener.bindTouchDark((View)this.buttonBg);
        (this.dotView = this.getViewById("lsq_dotView")).setVisibility(GONE);
        (this.badgeView = this.getViewById("lsq_badgeView")).setVisibility(GONE);
    }
    
    @Override
    public String getTitle() {
        return this.getTextViewText(this.buttonTitle);
    }
    
    @Override
    public void setTitle(final String s) {
        this.setTextViewText(this.buttonTitle, s);
    }
    
    @Override
    public void showDot(final boolean b) {
        this.showView((View)this.dotView, b);
    }
    
    @Override
    public void setBadge(final String text) {
        this.showView((View)this.badgeView, text != null);
        this.badgeView.setText((CharSequence)text);
    }
    
    public void setBackgroundResource(final int backgroundResource) {
        if (this.buttonBg != null) {
            this.buttonBg.setBackgroundResource(backgroundResource);
        }
    }
    
    @Override
    public void setTextColor(final int textColor) {
        if (textColor != 0) {
            this.buttonTitle.setTextColor(textColor);
        }
    }
    
    @Override
    public void setEnabled(final boolean b) {
        if (this.colorChangeListener != null && this.buttonBg != null) {
            this.colorChangeListener.enabledChanged((View)this.buttonBg, b);
            this.buttonBg.setEnabled(b);
        }
        super.setEnabled(b);
    }
    
    @Override
    public void setOnClickListener(final View.OnClickListener a) {
        this.a = a;
        if (this.buttonBg != null) {
            this.buttonBg.setOnClickListener((a == null) ? null : this.b);
        }
    }
}
