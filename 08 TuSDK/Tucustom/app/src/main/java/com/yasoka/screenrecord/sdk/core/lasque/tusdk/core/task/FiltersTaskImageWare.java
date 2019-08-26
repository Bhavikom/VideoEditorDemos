// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task;

import android.view.View;
//import org.lasque.tusdk.core.utils.anim.AnimHelper;
import android.widget.ImageView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AnimHelper;

import java.lang.ref.WeakReference;

public class FiltersTaskImageWare
{
    private WeakReference<ImageView> a;
    private String b;
    
    public ImageView getImageView() {
        if (this.a != null) {
            return this.a.get();
        }
        return null;
    }
    
    public void setImageView(final ImageView referent) {
        if (referent != null) {
            this.a = new WeakReference<ImageView>(referent);
        }
        else {
            this.a = null;
        }
    }
    
    public String getFilterName() {
        return this.b;
    }
    
    public void setFilterName(final String b) {
        this.b = b;
    }
    
    public FiltersTaskImageWare() {
    }
    
    public FiltersTaskImageWare(final ImageView imageView, final String b) {
        this.setImageView(imageView);
        this.b = b;
    }
    
    public boolean isEqualView(final ImageView imageView) {
        final ImageView imageView2 = this.getImageView();
        return imageView != null && imageView2 != null && imageView.equals(imageView2);
    }
    
    public boolean setImageResult(final FiltersTaskImageResult filtersTaskImageResult) {
        final ImageView imageView = this.getImageView();
        if (imageView == null) {
            return true;
        }
        if (!filtersTaskImageResult.getFilterName().equalsIgnoreCase(this.b)) {
            return false;
        }
        imageView.setImageBitmap(filtersTaskImageResult.getImage());
        AnimHelper.alphaAnimator((View)imageView, 200, 0.3f, 1.0f);
        return true;
    }
}
