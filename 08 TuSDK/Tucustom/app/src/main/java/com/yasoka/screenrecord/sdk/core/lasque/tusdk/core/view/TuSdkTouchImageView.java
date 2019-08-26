// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view;

//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.ViewCompat;
//import org.lasque.tusdk.core.struct.ViewSize;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.ViewSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkTouchImageView extends TouchImageView implements TuSdkTouchImageViewInterface
{
    private ImageOrientation a;
    private boolean b;
    private View c;
    private OnTouchImageViewListener d;
    private OnTouchImageViewListener e;
    
    public TuSdkTouchImageView(final Context context) {
        super(context);
        this.a = ImageOrientation.Up;
        this.d = null;
        this.e = new OnTouchImageViewListener() {
            @Override
            public void onMove() {
                TuSdkTouchImageView.this.invalidateTargetView();
            }
        };
        this.initView();
    }
    
    public TuSdkTouchImageView(final Context context, final AttributeSet set) {
        super(context, set);
        this.a = ImageOrientation.Up;
        this.d = null;
        this.e = new OnTouchImageViewListener() {
            @Override
            public void onMove() {
                TuSdkTouchImageView.this.invalidateTargetView();
            }
        };
        this.initView();
    }
    
    public TuSdkTouchImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.a = ImageOrientation.Up;
        this.d = null;
        this.e = new OnTouchImageViewListener() {
            @Override
            public void onMove() {
                TuSdkTouchImageView.this.invalidateTargetView();
            }
        };
        this.initView();
    }
    
    protected void initView() {
    }
    
    @Override
    public ImageOrientation getImageOrientation() {
        return this.a;
    }
    
    @Override
    public boolean isInAnimation() {
        return this.b;
    }
    
    @Override
    public void setInvalidateTargetView(final View c) {
        this.c = c;
        super.setOnTouchImageViewListener(this.e);
    }
    
    @Override
    public void setOnTouchImageViewListener(final OnTouchImageViewListener d) {
        this.d = d;
        super.setOnTouchImageViewListener(this.e);
    }
    
    public void invalidateTargetView() {
        if (this.d != null) {
            this.d.onMove();
        }
        if (this.c != null) {
            this.c.invalidate();
        }
    }
    
    @Override
    public void setImageBitmap(Bitmap imageRotaing, ImageOrientation up) {
        if (up == null) {
            up = ImageOrientation.Up;
        }
        this.a = up;
        if (imageRotaing != null) {
            imageRotaing = BitmapHelper.imageRotaing(imageRotaing, this.a);
        }
        super.setImageBitmap(imageRotaing);
    }
    
    @Override
    public void setImageBitmapWithAnim(final Bitmap imageBitmapWithAnim) {
        super.setImageBitmapWithAnim(imageBitmapWithAnim);
    }
    
    @Override
    public void changeImageAnimation(final LsqImageChangeType lsqImageChangeType) {
        if (lsqImageChangeType == null || this.b) {
            return;
        }
        long n = 0L;
        switch (lsqImageChangeType.ordinal()) {
            case 1: {
                this.a(-90);
                n = ComponentActType.editCuter_action_trun_left;
                break;
            }
            case 2: {
                this.a(90);
                n = ComponentActType.editCuter_action_trun_right;
                break;
            }
            case 3: {
                this.a(true);
                n = ComponentActType.editCuter_action_mirror_horizontal;
                break;
            }
            case 4: {
                this.a(false);
                n = ComponentActType.editCuter_action_mirror_vertical;
                break;
            }
        }
        if (n != 0L) {
            StatisticsManger.appendComponent(n);
        }
    }
    
    private void a(final int n) {
        final Bitmap drawableBitmap = BitmapHelper.getDrawableBitmap(this);
        if (drawableBitmap == null) {
            return;
        }
        this.b = true;
        float n2 = 1.0f;
        final TuSdkSize create = TuSdkSize.create(drawableBitmap);
        final ViewSize create2 = ViewSize.create((View)this);
        if (this.getScaleType() == ScaleType.FIT_CENTER) {
            if (create.width > create.height) {
                n2 = create2.height / (float)create2.width;
            }
            else if (create2.width < create2.height) {
                n2 = create2.width / (float)create2.height;
            }
        }
        else if (this.getScaleType() == ScaleType.CENTER_CROP) {
            if (create.width > create.height) {
                n2 = create2.width / (float)create2.height;
            }
            else if (create2.width < create2.height) {
                n2 = create2.height / (float)create2.width;
            }
        }
        this.resetZoom();
        ViewCompat.animate((View)this).scaleX(n2).setDuration(200L);
        ViewCompat.animate((View)this).scaleY(n2).setDuration(200L);
        ViewCompat.animate((View)this).rotation((float)n).setDuration(190L).setListener((ViewPropertyAnimatorListener)new ViewPropertyAnimatorListenerAdapter() {
            public void onAnimationEnd(final View view) {
                TuSdkTouchImageView.this.a();
            }
        }).setUpdateListener((ViewPropertyAnimatorUpdateListener)new ViewPropertyAnimatorUpdateListener() {
            public void onAnimationUpdate(final View view) {
                TuSdkTouchImageView.this.invalidateTargetView();
            }
        });
    }
    
    private void a() {
        final Bitmap drawableBitmap = BitmapHelper.getDrawableBitmap(this);
        if (drawableBitmap == null) {
            return;
        }
        final ImageOrientation value = ImageOrientation.getValue((int)ViewCompat.getRotation((View)this), false);
        final Bitmap imageRotaing = BitmapHelper.imageRotaing(drawableBitmap, value);
        int n = this.a.getDegree() + value.getDegree();
        if (this.a.isMirrored()) {
            n -= 180;
        }
        this.a = ImageOrientation.getValue(n, this.a.isMirrored());
        ViewCompat.setScaleX((View)this, 1.0f);
        ViewCompat.setScaleY((View)this, 1.0f);
        ViewCompat.setRotation((View)this, 0.0f);
        this.setImageBitmap(imageRotaing);
        this.invalidateTargetView();
        this.b = false;
    }
    
    private void a(final boolean b) {
        if (BitmapHelper.getDrawableBitmap(this) == null) {
            return;
        }
        this.b = true;
        this.resetZoom();
        int[] array;
        if (b) {
            array = new int[] { -1, 1 };
        }
        else {
            array = new int[] { 1, -1 };
        }
        ViewCompat.animate((View)this).scaleX((float)array[0]).setDuration(200L);
        ViewCompat.animate((View)this).scaleY((float)array[1]).setDuration(190L).setListener((ViewPropertyAnimatorListener)new ViewPropertyAnimatorListenerAdapter() {
            public void onAnimationEnd(final View view) {
                TuSdkTouchImageView.this.b(b);
            }
        }).setUpdateListener((ViewPropertyAnimatorUpdateListener)new ViewPropertyAnimatorUpdateListener() {
            public void onAnimationUpdate(final View view) {
                TuSdkTouchImageView.this.invalidateTargetView();
            }
        });
    }
    
    private void b(final boolean b) {
        final Bitmap drawableBitmap = BitmapHelper.getDrawableBitmap(this);
        if (drawableBitmap == null) {
            return;
        }
        final ImageOrientation imageOrientation = b ? ImageOrientation.UpMirrored : ImageOrientation.DownMirrored;
        final Bitmap imageRotaing = BitmapHelper.imageRotaing(drawableBitmap, imageOrientation);
        this.a = ImageOrientation.getValue((imageOrientation.getDegree() + this.a.getDegree()) % 360, !this.a.isMirrored());
        ViewCompat.setScaleX((View)this, 1.0f);
        ViewCompat.setScaleY((View)this, 1.0f);
        this.setImageBitmap(imageRotaing);
        this.invalidateTargetView();
        this.b = false;
    }
    
    @Override
    public void changeRegionRatio(final Rect rect, final float n) {
        final Bitmap drawableBitmap = BitmapHelper.getDrawableBitmap(this);
        if (drawableBitmap == null || this.b) {
            return;
        }
        this.b = true;
        final TuSdkSize create = TuSdkSize.create(drawableBitmap);
        float n2;
        if (n > 0.0f && this.getScaleType() == ScaleType.FIT_CENTER) {
            n2 = RectHelper.computerOutScale(rect, create.getRatioFloat(), false);
        }
        else if (n <= 0.0f && this.getScaleType() == ScaleType.CENTER_CROP) {
            n2 = RectHelper.computerOutScale(rect, create.getRatioFloat(), true);
        }
        else {
            final float n3 = rect.width() / (float)this.getWidth();
            final float n4 = rect.height() / (float)this.getHeight();
            if (RectHelper.computerOutScale(rect, create.getRatioFloat(), false) > RectHelper.computerOutScale(TuSdkViewHelper.getViewRect((View)this), create.getRatioFloat(), false)) {
                n2 = Math.max(n3, n4);
            }
            else {
                n2 = Math.min(n3, n4);
            }
        }
        this.resetZoom();
        ViewCompat.animate((View)this).scaleX(n2).setDuration(200L);
        ViewCompat.animate((View)this).scaleY(n2).setDuration(190L).setListener((ViewPropertyAnimatorListener)new ViewPropertyAnimatorListenerAdapter() {
            public void onAnimationEnd(final View view) {
                TuSdkTouchImageView.this.onRegionRatioAnimationEnd(rect, n);
            }
        }).setUpdateListener((ViewPropertyAnimatorUpdateListener)new ViewPropertyAnimatorUpdateListener() {
            public void onAnimationUpdate(final View view) {
                TuSdkTouchImageView.this.invalidateTargetView();
            }
        });
    }
    
    protected void onRegionRatioAnimationEnd(final Rect rect, final float n) {
        ViewCompat.setScaleX((View)this, 1.0f);
        ViewCompat.setScaleY((View)this, 1.0f);
        if (n > 0.0f) {
            this.setScaleType(ScaleType.CENTER_CROP);
        }
        else {
            this.setScaleType(ScaleType.FIT_CENTER);
        }
        TuSdkViewHelper.setViewRect((View)this, rect);
        this.setZoom(1.0f);
        this.invalidateTargetView();
        this.b = false;
    }
}
