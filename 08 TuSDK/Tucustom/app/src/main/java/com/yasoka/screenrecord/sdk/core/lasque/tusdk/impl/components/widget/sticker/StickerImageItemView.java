// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.sticker;

import android.graphics.PointF;
import android.support.v4.view.ViewCompat;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import android.graphics.Rect;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerItemViewInterface;
import android.view.MotionEvent;
import android.annotation.SuppressLint;
import android.util.AttributeSet;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.content.Context;
//import org.lasque.tusdk.core.TuSdkContext;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkImageView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerData;
//import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
//import org.lasque.tusdk.core.view.TuSdkImageView;

public final class StickerImageItemView extends StickerItemViewBase
{
    private TuSdkImageView a;
    private TuSdkImageButton b;
    private TuSdkImageButton c;
    private View.OnClickListener d;
    private int e;
    private int f;
    
    public static int getLayoutId() {
        return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_sticker_image_item_view");
    }
    
    public StickerImageItemView(final Context context) {
        super(context);
        this.d = (View.OnClickListener)new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                if (StickerImageItemView.this.equalViewIds(view, (View)StickerImageItemView.this.getCancelButton())) {
                    StickerImageItemView.this.a();
                }
            }
        };
    }
    
    public StickerImageItemView(final Context context, final AttributeSet set) {
        super(context, set);
        this.d = (View.OnClickListener)new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                if (StickerImageItemView.this.equalViewIds(view, (View)StickerImageItemView.this.getCancelButton())) {
                    StickerImageItemView.this.a();
                }
            }
        };
    }
    
    public StickerImageItemView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.d = (View.OnClickListener)new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                if (StickerImageItemView.this.equalViewIds(view, (View)StickerImageItemView.this.getCancelButton())) {
                    StickerImageItemView.this.a();
                }
            }
        };
    }
    
    public TuSdkImageView getImageView() {
        if (this.a == null) {
            this.a = this.getViewById("lsq_sticker_imageView");
        }
        return this.a;
    }
    
    public final TuSdkImageButton getCancelButton() {
        if (this.b == null) {
            this.b = this.getViewById("lsq_sticker_cancelButton");
            if (this.b != null) {
                this.b.setOnClickListener(this.d);
            }
        }
        return this.b;
    }
    
    @SuppressLint({ "ClickableViewAccessibility" })
    public final TuSdkImageButton getTurnButton() {
        if (this.c == null) {
            this.c = this.getViewById("lsq_sticker_turnButton");
            if (this.c != null) {
                this.c.setOnTouchListener(this.mOnTouchListener);
            }
        }
        return this.c;
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return (this.mStickerType == StickerView.StickerType.Normal || this.mStickerType == this.mType) && super.onTouchEvent(motionEvent);
    }
    
    @Override
    public void loadView() {
        this.getImageView();
        this.getCancelButton();
        this.getTurnButton();
    }
    
    private void a() {
        if (this.mDelegate == null) {
            return;
        }
        this.mDelegate.onStickerItemViewClose(this);
        if (this.mSticker != null) {
            this.mSticker.setImage(null);
        }
    }
    
    @Override
    public void setStroke(final int f, final int n) {
        this.f = f;
        this.e = ((n > 0) ? n : 0);
        if (this.getImageView() != null) {
            this.getImageView().setStroke(this.f, this.e);
        }
    }
    
    @Override
    protected void onLayouted() {
        super.onLayouted();
        if (this.getImageView() == null) {
            return;
        }
        this.mCMargin.x = this.getWidth() - this.getImageView().getWidth();
        this.mCMargin.y = this.getHeight() - this.getImageView().getHeight();
        final Rect locationInWindow = TuSdkViewHelper.locationInWindow((View)this);
        final Rect locationInWindow2 = TuSdkViewHelper.locationInWindow((View)this.getImageView());
        this.mCOffset.x = locationInWindow2.left - locationInWindow.left;
        this.mCOffset.y = locationInWindow2.top - locationInWindow.top;
        this.initStickerPostion();
    }
    
    protected void onMeasure(final int n, final int n2) {
        View.MeasureSpec.getSize(n2);
        super.onMeasure(n, n2);
    }
    
    @Override
    public void setSticker(final StickerData mSticker) {
        if (mSticker == null) {
            return;
        }
        this.showViewIn((View)this.getTurnButton(), mSticker.getType() == StickerData.StickerType.TypeImage);
        this.showViewIn((View)this, false);
        this.getImageView().post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (StickerImageItemView.this.getWidth() == 0) {
                    StickerImageItemView.this.postInvalidate();
                }
                StickerImageItemView.this.showViewIn((View)StickerImageItemView.this, true);
            }
        });
        this.mSticker = mSticker;
        this.mCSize = mSticker.sizePixies();
        if (this.getImageView() != null) {
            this.getImageView().setImageBitmap(mSticker.getImage());
            if (mSticker.stickerId != 0L && mSticker.categoryId != 0L) {
                mSticker.setImage(null);
            }
        }
        if (this.isLayouted) {
            this.initStickerPostion();
        }
    }
    
    @Override
    public void setSelected(final boolean b) {
        if (this.getImageView() != null) {
            this.getImageView().setStroke(b ? this.f : 0, this.e);
        }
        this.showViewIn((View)this.getCancelButton(), b);
        if (this.mSticker.getType() == StickerData.StickerType.TypeImage || this.mSticker.getType() == StickerData.StickerType.TypeText) {
            this.showViewIn((View)this.getTurnButton(), b);
        }
    }
    
    public void setTranslation(final float n, final float n2) {
        this.post((Runnable)new Runnable() {
            @Override
            public void run() {
                StickerImageItemView.this.mTranslation.x = n;
                StickerImageItemView.this.mTranslation.y = n2;
                ViewCompat.setTranslationX((View)StickerImageItemView.this, StickerImageItemView.this.mTranslation.x);
                ViewCompat.setTranslationY((View)StickerImageItemView.this, StickerImageItemView.this.mTranslation.y);
            }
        });
    }
    
    public PointF getTranslation() {
        return this.mTranslation;
    }
    
    public StickerData getSticker() {
        return this.mSticker;
    }
}
