// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget;

import android.view.animation.AnimationUtils;
//import org.lasque.tusdk.core.type.ActivityAnimType;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.view.animation.Animation;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ActivityAnimType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkActionSheetView extends TuSdkRelativeLayout implements Animation.AnimationListener
{
    private TuSdkActionSheetViewAnimation a;
    private int b;
    private boolean c;
    
    public TuSdkActionSheetView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuSdkActionSheetView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuSdkActionSheetView(final Context context) {
        super(context);
    }
    
    public abstract View getMaskBg();
    
    public abstract LinearLayout getSheetTable();
    
    public abstract TextView getTitleView();
    
    public abstract Button getCancelButton();
    
    public abstract ActivityAnimType getAlphaAnimType();
    
    public abstract ActivityAnimType getTransAnimType();
    
    public TuSdkActionSheetViewAnimation getAnimationListener() {
        return this.a;
    }
    
    public void setAnimationListener(final TuSdkActionSheetViewAnimation a) {
        this.a = a;
    }
    
    public void setDismissClickListener(final View.OnClickListener onClickListener) {
        if (this.getMaskBg() != null) {
            this.getMaskBg().setOnClickListener(onClickListener);
        }
        if (this.getCancelButton() != null) {
            this.getCancelButton().setOnClickListener(onClickListener);
        }
    }
    
    public void setTitle(final String text) {
        if (this.getTitleView() != null) {
            this.getTitleView().setText((CharSequence)text);
        }
    }
    
    @Override
    public void loadView() {
        super.loadView();
        this.showView((View)this.getTitleView(), false);
        this.showView((View)this.getCancelButton(), false);
    }
    
    public void runViewShowableAnim(final boolean c) {
        this.c = c;
        final ActivityAnimType transAnimType = this.getTransAnimType();
        final ActivityAnimType alphaAnimType = this.getAlphaAnimType();
        if (transAnimType == null && alphaAnimType == null) {
            this.onAnimationEnd(null);
            return;
        }
        final LinearLayout sheetTable = this.getSheetTable();
        if (transAnimType != null && sheetTable != null) {
            sheetTable.clearAnimation();
            final Animation loadAnimation = AnimationUtils.loadAnimation(this.getContext(), transAnimType.getAnim(c));
            loadAnimation.setAnimationListener((Animation.AnimationListener)this);
            sheetTable.startAnimation(loadAnimation);
        }
        final View maskBg = this.getMaskBg();
        if (alphaAnimType != null && maskBg != null) {
            maskBg.clearAnimation();
            final Animation loadAnimation2 = AnimationUtils.loadAnimation(this.getContext(), alphaAnimType.getAnim(c));
            loadAnimation2.setFillEnabled(true);
            loadAnimation2.setFillAfter(true);
            maskBg.startAnimation(loadAnimation2);
        }
    }
    
    public void onAnimationStart(final Animation animation) {
        ++this.b;
    }
    
    public void onAnimationEnd(final Animation animation) {
        --this.b;
        if (this.b > 0 || this.a == null) {
            return;
        }
        this.a.onAnimationEnd(this.c);
    }
    
    public void onAnimationRepeat(final Animation animation) {
    }
    
    public interface TuSdkActionSheetViewAnimation
    {
        void onAnimationEnd(final boolean p0);
    }
}
