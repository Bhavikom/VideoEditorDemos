// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget;

//import org.lasque.tusdk.core.utils.anim.AnimHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.util.AttributeSet;
import android.content.Context;
import android.view.animation.Animation;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AnimHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkProgressHubView extends TuSdkRelativeLayout implements Animation.AnimationListener
{
    private boolean a;
    private TuSdkProgressHubViewDelegate b;
    
    public TuSdkProgressHubView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuSdkProgressHubView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuSdkProgressHubView(final Context context) {
        super(context);
    }
    
    public abstract ProgressBar getProgressBar();
    
    public abstract ImageView getImageView();
    
    public abstract TextView getTitleView();
    
    public abstract LinearLayout getHubView();
    
    public abstract int getImageSucceedResId();
    
    public abstract int getImageFailedResId();
    
    public void setDelegate(final TuSdkProgressHubViewDelegate b) {
        this.b = b;
    }
    
    public void setArgs(final HubArgCache hubArgCache) {
        this.a(hubArgCache);
        if (hubArgCache.showType == null) {
            hubArgCache.showType = TuSdkHubViewShowType.TypeDefault;
        }
        switch (hubArgCache.showType.ordinal()) {
            case 1: {
                this.c(hubArgCache);
                break;
            }
            case 2: {
                this.d(hubArgCache);
                break;
            }
            case 3: {
                this.e(hubArgCache);
                break;
            }
            default: {
                this.b(hubArgCache);
                break;
            }
        }
    }
    
    private void a(final HubArgCache hubArgCache) {
        final TextView titleView = this.getTitleView();
        if (titleView == null) {
            return;
        }
        if (hubArgCache.text != null) {
            titleView.setText((CharSequence)hubArgCache.text);
        }
        else if (hubArgCache.textResId != 0) {
            titleView.setText(hubArgCache.textResId);
        }
    }
    
    private void b(final HubArgCache hubArgCache) {
        this.a(true, false);
    }
    
    private void c(final HubArgCache hubArgCache) {
        this.a(false, true);
        this.a(this.getImageSucceedResId());
    }
    
    private void d(final HubArgCache hubArgCache) {
        this.a(false, true);
        this.a(this.getImageFailedResId());
    }
    
    private void e(final HubArgCache hubArgCache) {
        this.a(false, true);
        this.a(hubArgCache.imageResId);
    }
    
    private void a(final boolean b, final boolean b2) {
        this.showView((View)this.getProgressBar(), b);
        this.showView((View)this.getImageView(), b2);
    }
    
    private void a(final int imageResource) {
        final ImageView imageView = this.getImageView();
        if (imageView == null || imageResource == 0) {
            imageView.setVisibility(GONE);
            return;
        }
        imageView.setVisibility(VISIBLE);
        imageView.setImageResource(imageResource);
    }
    
    public void runViewShowableAnim(final boolean a) {
        this.a = a;
        this.getHubView().clearAnimation();
        final Animation scaleAlphaAnimation = AnimHelper.scaleAlphaAnimation(260, a);
        scaleAlphaAnimation.setAnimationListener((Animation.AnimationListener)this);
        this.getHubView().setAnimation(scaleAlphaAnimation);
    }
    
    public void onAnimationStart(final Animation animation) {
    }
    
    public void onAnimationEnd(final Animation animation) {
        if (!this.a && this.b != null) {
            this.b.onDismissAnimEnded();
            this.b = null;
        }
    }
    
    public void onAnimationRepeat(final Animation animation) {
    }
    
    @Override
    public void viewWillDestory() {
        super.viewWillDestory();
        this.b = null;
    }
    
    public static class HubArgCache
    {
        public Context context;
        public String text;
        public int textResId;
        public long delay;
        public TuSdkHubViewShowType showType;
        public int imageResId;
        
        public HubArgCache(final Context context, final TuSdkHubViewShowType showType, final String text, final int textResId, final int imageResId, final long delay) {
            this.context = context;
            this.showType = showType;
            this.text = text;
            this.imageResId = imageResId;
            this.textResId = textResId;
            this.delay = delay;
        }
    }
    
    public enum TuSdkHubViewShowType
    {
        TypeDefault, 
        TypeSucceed, 
        TypeFailed, 
        TypeImage;
    }
    
    public interface TuSdkProgressHubViewDelegate
    {
        void onDismissAnimEnded();
    }
}
