// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view;

//import org.lasque.tusdk.core.utils.ContextUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;

public class TuSdkImageView extends AppCompatImageView implements TuSdkViewInterface
{
    private int a;
    private boolean b;
    private int c;
    private int d;
    private boolean e;
    private PorterDuffXfermode f;
    private int g;
    
    public TuSdkImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.f = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        this.g = 31;
        this.initView();
    }
    
    public TuSdkImageView(final Context context, final AttributeSet set) {
        super(context, set);
        this.f = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        this.g = 31;
        this.initView();
    }
    
    public TuSdkImageView(final Context context) {
        super(context);
        this.f = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        this.g = 31;
        this.initView();
    }
    
    protected void initView() {
    }
    
    protected void onDraw(final Canvas canvas) {
        if (!this.drawRadius(canvas)) {
            super.onDraw(canvas);
        }
        this.drawStroke(canvas);
    }
    
    @SuppressLint({ "WrongCall" })
    protected boolean drawRadius(final Canvas canvas) {
        final Drawable drawable = this.getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable) || !this.b) {
            return false;
        }
        final RectF rectF = new RectF(1.0f, 1.0f, (float)(this.getWidth() - 1), (float)(this.getHeight() - 1));
        final int saveLayer = canvas.saveLayer(rectF, (Paint)null, this.g);
        final Paint paint = ((BitmapDrawable)drawable).getPaint();
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, (float)this.a, (float)this.a, paint);
        final Xfermode xfermode = paint.getXfermode();
        paint.setXfermode((Xfermode)this.f);
        super.onDraw(canvas);
        paint.setXfermode(xfermode);
        canvas.restoreToCount(saveLayer);
        if (this.a == 0) {
            this.b = false;
        }
        return true;
    }
    
    protected void drawStroke(final Canvas canvas) {
        if (!this.e) {
            return;
        }
        final float n = this.c * 0.5f;
        final RectF rectF = new RectF(n, n, this.getWidth() - n, this.getHeight() - n);
        final Paint paint = new Paint(1);
        paint.setColor(this.d);
        paint.setStrokeWidth((float)this.c);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF, (float)this.a, (float)this.a, paint);
        if (this.c == 0) {
            this.e = false;
        }
    }
    
    public float getCornerRadius() {
        return (float)this.a;
    }
    
    public void setCornerRadius(final int n) {
        this.a = ((n > 0) ? n : 0);
        this.b = true;
        this.invalidate();
    }
    
    public void setCornerRadiusDP(final int n) {
        this.setCornerRadius(ContextUtils.dip2px(this.getContext(), (float)n));
    }
    
    public void setStroke(final int d, final int n) {
        this.d = d;
        this.c = ((n > 0) ? n : 0);
        this.e = true;
        this.invalidate();
    }
    
    public void removeStroke() {
        this.d = 0;
        this.c = 0;
    }
    
    public void loadView() {
    }
    
    public void viewDidLoad() {
    }
    
    public void viewWillDestory() {
    }
    
    public void viewNeedRest() {
    }
}
