// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task;

import android.view.View;
//import org.lasque.tusdk.core.utils.anim.AnimHelper;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AnimHelper;

import java.lang.ref.WeakReference;

public abstract class ImageViewTaskWare
{
    private WeakReference<ImageView> a;
    private boolean b;
    private boolean c;
    private int d;
    
    public ImageViewTaskWare() {
        this.b = true;
        this.d = 90;
    }
    
    public int getImageCompress() {
        return this.d;
    }
    
    public void setImageCompress(final int d) {
        this.d = d;
    }
    
    public void cancel() {
        this.c = true;
    }
    
    public boolean isCancel() {
        return this.c;
    }
    
    public boolean isSaveToDisk() {
        return this.b;
    }
    
    public void setSaveToDisk(final boolean b) {
        this.b = b;
    }
    
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
    
    public boolean isEqualView(final ImageView imageView) {
        final ImageView imageView2 = this.getImageView();
        return imageView != null && imageView2 != null && imageView.equals(imageView2);
    }
    
    public void imageLoaded(final Bitmap imageBitmap, final LoadType loadType) {
        if (imageBitmap == null || this.c) {
            return;
        }
        final ImageView imageView = this.getImageView();
        if (imageView == null) {
            return;
        }
        imageView.setImageBitmap(imageBitmap);
        if (loadType != LoadType.TypeMomery) {
            AnimHelper.alphaAnimator((View)imageView, 120, 0.3f, 1.0f);
        }
    }
    
    public enum LoadType
    {
        TypeMomery, 
        TypeDisk, 
        TypeBuild;
    }
}
