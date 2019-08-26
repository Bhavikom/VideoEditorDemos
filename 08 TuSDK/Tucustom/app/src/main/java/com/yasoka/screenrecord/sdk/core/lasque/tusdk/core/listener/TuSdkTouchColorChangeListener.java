// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener;

import android.widget.ImageView;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
//import org.lasque.tusdk.core.utils.ColorUtils;
import android.os.Build;
import android.graphics.drawable.ColorDrawable;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ColorUtils;

public class TuSdkTouchColorChangeListener implements View.OnTouchListener
{
    private TouchColorType a;
    private int b;
    
    public static TuSdkTouchColorChangeListener bindTouchDark(final View view) {
        return bindTouch(view, TouchColorType.DARK);
    }
    
    public static TuSdkTouchColorChangeListener bindTouchLight(final View view) {
        return bindTouch(view, TouchColorType.LIGHT);
    }
    
    public static TuSdkTouchColorChangeListener bindTouch(final View view, final TouchColorType touchColorType) {
        if (view == null || touchColorType == null) {
            return null;
        }
        final TuSdkTouchColorChangeListener tuSdkTouchColorChangeListener = new TuSdkTouchColorChangeListener(touchColorType);
        view.setOnTouchListener((View.OnTouchListener)new TuSdkTouchColorChangeListener(touchColorType));
        return tuSdkTouchColorChangeListener;
    }
    
    public static TuSdkTouchColorChangeListener viewTouchDarkListener() {
        return new TuSdkTouchColorChangeListener(TouchColorType.DARK);
    }
    
    public static TuSdkTouchColorChangeListener viewTouchLightListener() {
        return new TuSdkTouchColorChangeListener(TouchColorType.LIGHT);
    }
    
    public static void setDark(final Drawable drawable) {
        changeFilter(drawable, TouchColorType.DARK);
    }
    
    public static void setLight(final Drawable drawable) {
        changeFilter(drawable, TouchColorType.LIGHT);
    }
    
    public static void clearColorType(final Drawable drawable) {
        changeFilter(drawable, null);
    }
    
    public static void changeFilter(final Drawable drawable, final TouchColorType touchColorType) {
        if (drawable == null) {
            return;
        }
        drawable.clearColorFilter();
        if (touchColorType != null) {
            drawable.setColorFilter((ColorFilter)new ColorMatrixColorFilter(touchColorType.getFilter()));
        }
    }
    
    public static int changeColorFilter(final ColorDrawable colorDrawable, final TouchColorType touchColorType, int color) {
        if (Build.VERSION.SDK_INT < 11) {
            return color;
        }
        if (touchColorType != null) {
            color = ColorUtils.getColor(colorDrawable);
            ColorUtils.setColorFilter(colorDrawable, touchColorType.getFilter());
        }
        else {
            ColorUtils.setColor(colorDrawable, color);
        }
        return color;
    }
    
    public TuSdkTouchColorChangeListener(final TouchColorType a) {
        this.a = a;
    }
    
    @SuppressLint({ "ClickableViewAccessibility" })
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        if (!view.isEnabled() || view.isSelected()) {
            return false;
        }
        switch (motionEvent.getAction()) {
            case 0: {
                this.a(view, this.a);
                break;
            }
            case 1:
            case 3:
            case 4: {
                this.a(view, null);
                break;
            }
        }
        return false;
    }
    
    private void a(final View view, final TouchColorType touchColorType) {
        if (view instanceof ImageView) {
            final ImageView imageView = (ImageView)view;
            imageView.clearColorFilter();
            if (touchColorType != null) {
                imageView.setColorFilter((ColorFilter)new ColorMatrixColorFilter(touchColorType.getFilter()));
            }
            return;
        }
        final Drawable background = view.getBackground();
        if (background == null) {
            return;
        }
        if (background instanceof ColorDrawable) {
            this.b = changeColorFilter((ColorDrawable)background, touchColorType, this.b);
        }
        else {
            changeFilter(background, touchColorType);
        }
    }
    
    public void enabledChanged(final View view, final boolean b) {
        if (b) {
            this.a(view, null);
        }
        else {
            this.a(view, this.a);
        }
    }
    
    public void selectedChanged(final View view, final boolean b) {
        if (b) {
            this.a(view, this.a);
        }
        else {
            this.a(view, null);
        }
    }
    
    public enum TouchColorType
    {
        DARK(new float[] { 1.0f, 0.0f, 0.0f, 0.0f, -50.0f, 0.0f, 1.0f, 0.0f, 0.0f, -50.0f, 0.0f, 0.0f, 1.0f, 0.0f, -50.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f }), 
        LIGHT(new float[] { 1.0f, 0.0f, 0.0f, 0.0f, 50.0f, 0.0f, 1.0f, 0.0f, 0.0f, 50.0f, 0.0f, 0.0f, 1.0f, 0.0f, 50.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f });
        
        private float[] a;
        
        private TouchColorType(final float[] a) {
            this.a = a;
        }
        
        public float[] getFilter() {
            return this.a;
        }
    }
}
