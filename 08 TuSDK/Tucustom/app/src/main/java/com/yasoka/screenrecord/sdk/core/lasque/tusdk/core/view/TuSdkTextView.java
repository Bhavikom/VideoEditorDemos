// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;

//import org.lasque.tusdk.core.TuSdkContext;

public class TuSdkTextView extends AppCompatTextView implements TuSdkViewInterface
{
    private int a;
    private int b;
    private boolean c;
    private TextView d;
    private TextPaint e;
    private int f;
    private int g;
    
    public TuSdkTextView(final Context context) {
        super(context);
        this.d = null;
        this.f = TuSdkContext.dip2px(0.0f);
        this.g = TuSdkContext.getColor(17170445);
        this.d = new TextView(context);
        this.initView();
    }
    
    public TuSdkTextView(final Context context, @Nullable final AttributeSet set) {
        super(context, set);
        this.d = null;
        this.f = TuSdkContext.dip2px(0.0f);
        this.g = TuSdkContext.getColor(17170445);
        this.d = new TextView(context, set);
        this.initView();
    }
    
    public TuSdkTextView(final Context context, @Nullable final AttributeSet set, final int n) {
        super(context, set, n);
        this.d = null;
        this.f = TuSdkContext.dip2px(0.0f);
        this.g = TuSdkContext.getColor(17170445);
        this.d = new TextView(context, set, n);
        this.initView();
    }
    
    public void setText(final CharSequence charSequence, final BufferType bufferType) {
        super.setText(charSequence, bufferType);
        if (this.d == null) {
            return;
        }
        this.d.setText(charSequence, bufferType);
    }
    
    public void setAlpha(final float n) {
        super.setAlpha(n);
        if (this.d == null) {
            return;
        }
        this.d.setAlpha(n);
    }
    
    public void setGravity(final int n) {
        super.setGravity(n);
        if (this.d == null) {
            return;
        }
        this.d.setGravity(n);
    }
    
    public void setPadding(final int n, final int n2, final int n3, final int n4) {
        super.setPadding(n, n2, n3, n4);
        if (this.d == null) {
            return;
        }
        this.d.setPadding(n, n2, n3, n4);
    }
    
    public void setLineSpacing(final float n, final float n2) {
        super.setLineSpacing(n, n2);
        if (this.d == null) {
            return;
        }
        this.d.setLineSpacing(n, n2);
    }
    
    public void setTypeface(final Typeface typeface) {
        super.setTypeface(typeface);
        if (this.d == null) {
            return;
        }
        this.d.setTypeface(typeface);
    }
    
    protected void initView() {
        (this.e = this.d.getPaint()).setStrokeWidth((float)this.f);
        this.e.setStyle(Paint.Style.STROKE);
        this.d.setTextColor(this.g);
        this.d.setGravity(this.getGravity());
    }
    
    public void setTextSize(final float textSize) {
        super.setTextSize(textSize);
    }
    
    public void setTextStrokeWidth(final int f) {
        this.f = f;
        this.e.setStrokeWidth((float)f);
        this.invalidate();
    }
    
    public void setTextStrokeColor(final int n) {
        this.g = n;
        this.d.setTextColor(n);
        this.invalidate();
    }
    
    public void setLayoutParams(final ViewGroup.LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
        this.d.setLayoutParams(layoutParams);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        final CharSequence text = this.d.getText();
        if (text == null || !text.equals(this.getText())) {
            this.d.setText(this.getText());
            this.postInvalidate();
        }
        this.d.setTextSize(TuSdkContext.px2sp(this.getTextSize()));
        this.d.measure(n, n2);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.d.layout(n, n2, n3, n4);
    }
    
    protected void onDraw(final Canvas canvas) {
        this.d.draw(canvas);
        super.onDraw(canvas);
        this.drawStroke(canvas);
    }
    
    protected void drawStroke(final Canvas canvas) {
        if (!this.c) {
            return;
        }
        final float n = this.a * 0.5f;
        final RectF rectF = new RectF(n, n, this.getWidth() - n, this.getHeight() - n);
        final Paint paint = new Paint(1);
        paint.setColor(this.b);
        paint.setStrokeWidth((float)this.a);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF, 0.0f, 0.0f, paint);
        if (this.a == 0) {
            this.c = false;
        }
    }
    
    @TargetApi(21)
    public void setLetterSpacing(final float n) {
        super.setLetterSpacing(n);
        if (this.d == null) {
            return;
        }
        this.d.setLetterSpacing(n);
    }
    
    public void setStroke(final int b, final int n) {
        this.b = b;
        this.a = ((n > 0) ? n : 0);
        this.c = true;
        this.invalidate();
    }
    
    public void removeStroke() {
        this.b = 0;
        this.a = 0;
    }
    
    public void loadView() {
    }
    
    public void viewDidLoad() {
    }
    
    public void viewWillDestory() {
    }
    
    public void viewNeedRest() {
    }
    
    public void setUnderlineText(final boolean b) {
        this.getPaint().setUnderlineText(b);
        this.e.setUnderlineText(b);
    }
}
