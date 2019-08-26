// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview;

import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
//import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
//import org.lasque.tusdk.core.utils.anim.HeightAnimation;
//import org.lasque.tusdk.core.utils.ContextUtils;
import android.view.View;
//import org.lasque.tusdk.core.struct.ViewSize;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.util.AttributeSet;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.ViewSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.HeightAnimation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;

import java.util.Calendar;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkRefreshListHeaderView extends TuSdkRelativeLayout
{
    public static final float DROG_RESISTANCE = 0.3f;
    public static final float FRESH_OFFSET_DISTANCE = 0.5f;
    private float a;
    private int b;
    private float c;
    private TuSdkRefreshState d;
    private Calendar e;
    
    public TuSdkRefreshListHeaderView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuSdkRefreshListHeaderView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuSdkRefreshListHeaderView(final Context context) {
        super(context);
    }
    
    public abstract RelativeLayout getHeadWrap();
    
    public abstract ImageView getLoadIcon();
    
    @Override
    public void loadView() {
        super.loadView();
        this.b = ViewSize.create((View)this.getHeadWrap()).height;
        this.c = this.b * 0.5f;
    }
    
    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        this.a = ContextUtils.getScreenSize(this.getContext()).height * 0.3f;
        this.setVisiableHeight(0);
    }
    
    public Calendar getLastDate() {
        return this.e;
    }
    
    public void setLastDate(final Calendar e) {
        this.e = e;
    }
    
    public void setState(final TuSdkRefreshState d) {
        this.d = d;
    }
    
    public TuSdkRefreshState getState() {
        return this.d;
    }
    
    public int updateHeight(final float n) {
        if (this.d == TuSdkRefreshState.StateLoading) {
            return 0;
        }
        this.a();
        this.getHeadWrap().clearAnimation();
        final int a = this.a(n);
        if (a > this.c) {
            this.setState(TuSdkRefreshState.StateTriggered);
        }
        else {
            this.setState(TuSdkRefreshState.StateVisible);
        }
        this.setVisiableHeight(a);
        return a;
    }
    
    private void a() {
        if (this.getLastDate() == null) {
            return;
        }
        this.setLastDate(this.getLastDate());
    }
    
    public TuSdkRefreshState submitState() {
        if (this.d == TuSdkRefreshState.StateLoading) {
            return this.d;
        }
        final int visiableHeight = this.getVisiableHeight();
        if (visiableHeight == 0) {
            return this.d;
        }
        if (visiableHeight < this.c) {
            this.setState(TuSdkRefreshState.StateVisible);
            this.a(0);
        }
        else {
            this.refreshStart();
        }
        return this.d;
    }
    
    public void refreshStart() {
        if (this.d == TuSdkRefreshState.StateLoading) {
            return;
        }
        this.a(true);
        this.setState(TuSdkRefreshState.StateLoading);
        this.a(this.b);
    }
    
    public void firstStart() {
        this.a(true);
        this.setState(TuSdkRefreshState.StateLoading);
        this.setVisiableHeight(this.b);
    }
    
    public void refreshEnded() {
        if (this.d != TuSdkRefreshState.StateLoading) {
            return;
        }
        this.a(false);
        this.setState(TuSdkRefreshState.StateHidden);
        this.setLastDate(Calendar.getInstance());
        this.a(0);
    }
    
    private void a(final int n) {
        this.getHeadWrap().clearAnimation();
        final HeightAnimation heightAnimation = new HeightAnimation((View)this.getHeadWrap(), (float)n);
        heightAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        heightAnimation.setDuration(300L);
        this.getHeadWrap().startAnimation((Animation)heightAnimation);
    }
    
    public void setVisiableHeight(final int height) {
        final ViewGroup.LayoutParams layoutParams = this.getHeadWrap().getLayoutParams();
        layoutParams.height = height;
        this.getHeadWrap().setLayoutParams(layoutParams);
    }
    
    public int getVisiableHeight() {
        return this.getHeadWrap().getHeight();
    }
    
    private void a(final boolean b) {
        final ImageView loadIcon = this.getLoadIcon();
        if (loadIcon == null) {
            return;
        }
        loadIcon.clearAnimation();
        if (!b) {
            return;
        }
        final RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(1266L);
        rotateAnimation.setInterpolator((Interpolator)new LinearInterpolator());
        rotateAnimation.setRepeatCount(-1);
        loadIcon.startAnimation((Animation)rotateAnimation);
    }
    
    private int a(final float n) {
        if (n < 0.0f) {
            final int n2 = (int)n + this.getHeadWrap().getHeight();
            return (n2 < 0) ? 0 : n2;
        }
        return (int)Math.ceil(n * countResistance((float)this.getHeadWrap().getHeight(), this.a)) + this.getHeadWrap().getHeight();
    }
    
    public static float countResistance(final float n, final float n2) {
        if (n2 == 0.0f) {
            return 0.0f;
        }
        float n3 = n / n2;
        if (n3 > 1.0f) {
            n3 = 1.0f;
        }
        return 1.0f - n3;
    }
    
    public enum TuSdkRefreshState
    {
        StateHidden, 
        StateVisible, 
        StateTriggered, 
        StateLoading;
    }
}
