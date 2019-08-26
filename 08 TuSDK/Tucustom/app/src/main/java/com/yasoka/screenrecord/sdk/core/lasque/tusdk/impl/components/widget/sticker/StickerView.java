// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.sticker;

//import org.lasque.tusdk.core.struct.TuSdkSizeF;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import java.util.ArrayList;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerResult;
import android.graphics.Rect;
import android.widget.RelativeLayout;
import android.view.View;
//import org.lasque.tusdk.core.utils.ContextUtils;
import android.view.ViewGroup;
import java.util.List;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerImageData;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.TuSdk;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import java.util.Iterator;
import android.util.AttributeSet;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSizeF;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerImageData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerItemViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerResult;

import java.util.LinkedList;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerItemViewInterface;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public final class StickerView extends TuSdkRelativeLayout implements StickerItemViewInterface.StickerItemViewDelegate
{
    private StickerViewDelegate a;
    private int b;
    private LinkedList<StickerItemViewInterface> c;
    private StickerItemViewInterface d;
    private boolean e;
    private StickerType f;
    
    public StickerView(final Context context) {
        super(context);
        this.c = new LinkedList<StickerItemViewInterface>();
        this.f = StickerType.Normal;
    }
    
    public StickerView(final Context context, final AttributeSet set) {
        super(context, set);
        this.c = new LinkedList<StickerItemViewInterface>();
        this.f = StickerType.Normal;
    }
    
    public StickerView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.c = new LinkedList<StickerItemViewInterface>();
        this.f = StickerType.Normal;
    }
    
    public StickerViewDelegate getDelegate() {
        return this.a;
    }
    
    public void setDelegate(final StickerViewDelegate a) {
        this.a = a;
    }
    
    public StickerItemViewInterface getCurrentItemViewSelected() {
        return this.d;
    }
    
    public void changeOrUpdateStickerType(final StickerType stickerType) {
        this.f = stickerType;
        final Iterator<StickerItemViewInterface> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            iterator.next().setStickerViewType(stickerType);
        }
    }
    
    private int a(final StickerData.StickerType stickerType) {
        if (stickerType == StickerData.StickerType.TypeImage) {
            this.b = StickerImageItemView.getLayoutId();
        }
        else if (stickerType == StickerData.StickerType.TypeText) {
            this.b = StickerTextItemView.getLayoutId();
        }
        return this.b;
    }
    
    public int stickersCount() {
        return this.c.size();
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.a = null;
    }
    
    public void updateText(final String s, final boolean b) {
        if (this.d == null) {
            return;
        }
        ((StickerTextItemView)this.d).updateText(s, b);
    }
    
    public void onSelectedColorChanged(final int n, final String s) {
        if (this.d == null) {
            return;
        }
        ((StickerTextItemView)this.d).onSelectedColorChanged(n, s);
    }
    
    public void toggleTextUnderlineStyle() {
        if (this.d == null) {
            return;
        }
        ((StickerTextItemView)this.d).toggleTextUnderlineStyle();
    }
    
    public void toggleTextReverse() {
        if (this.d == null) {
            return;
        }
        ((StickerTextItemView)this.d).toggleTextReverse();
    }
    
    public void changeTextAlignment(final int n) {
        if (this.d == null) {
            return;
        }
        ((StickerTextItemView)this.d).changeTextAlignment(n);
    }
    
    public void changeTextAlpha(final float alpha) {
        if (this.d == null) {
            return;
        }
        ((StickerTextItemView)this.d).getTextView().setAlpha(alpha);
    }
    
    public void changeTextStrokeWidth(final int textStrokeWidth) {
        if (this.d == null) {
            return;
        }
        ((StickerTextItemView)this.d).setTextStrokeWidth(textStrokeWidth);
    }
    
    public void changeTextStrokeColor(final int textStrokeColor) {
        if (this.d == null) {
            return;
        }
        ((StickerTextItemView)this.d).setTextStrokeColor(textStrokeColor);
    }
    
    private boolean a(final StickerData stickerData) {
        if (stickerData == null) {
            return false;
        }
        boolean canAppendSticker = true;
        if (this.a != null) {
            canAppendSticker = this.a.canAppendSticker(this, stickerData);
        }
        if (!canAppendSticker) {
            return false;
        }
        if (this.stickersCount() >= SdkValid.shared.maxStickers()) {
            TuSdkViewHelper.alert(this.getContext(), null, TuSdkContext.getString("lsq_sticker_over_limit", SdkValid.shared.maxStickers()), TuSdkContext.getString("lsq_button_done"));
            return false;
        }
        return true;
    }
    
    public void appendSticker(final StickerData stickerData) {
        if (!this.a(stickerData)) {
            return;
        }
        TuSdk.messageHub().setStatus(this.getContext(), TuSdkContext.getString("lsq_sticker_loading"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                StickerView.this.b(stickerData);
            }
        }).start();
    }
    
    public void appendSticker(final Bitmap image) {
        if (image == null) {
            return;
        }
        final StickerData stickerData = new StickerData();
        stickerData.setImage(image);
        stickerData.height = TuSdkContext.px2dip((float)image.getHeight());
        stickerData.width = TuSdkContext.px2dip((float)image.getWidth());
        if (!this.a(stickerData)) {
            return;
        }
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                StickerView.this.c(stickerData);
            }
        });
    }
    
    public void appendSticker(final StickerImageData stickerImageData) {
        if (stickerImageData == null) {
            return;
        }
        if (!this.a(stickerImageData)) {
            return;
        }
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                StickerView.this.c(stickerImageData);
            }
        });
    }
    
    private void b(final StickerData stickerData) {
        StickerLocalPackage.shared().loadStickerItem(stickerData);
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                StickerView.this.c(stickerData);
            }
        });
    }
    
    private void c(final StickerData stickerData) {
        if (stickerData == null || (stickerData.getImage() == null && stickerData.texts == null)) {
            TuSdk.messageHub().showError(this.getContext(), TuSdkContext.getString("lsq_sticker_load_unexsit"));
            return;
        }
        TuSdk.messageHub().dismissRightNow();
        this.onStickerItemViewSelected(this.d(stickerData));
    }
    
    public List<StickerItemViewInterface> getStickerItems() {
        return this.c;
    }
    
    public List<StickerItemViewInterface> getStickerTextItems() {
        final LinkedList<StickerItemViewInterface> list = new LinkedList<StickerItemViewInterface>();
        for (final StickerItemViewInterface stickerItemViewInterface : this.c) {
            if (stickerItemViewInterface.getStickerType() == StickerType.Text) {
                list.add(stickerItemViewInterface);
            }
        }
        return list;
    }
    
    public List<StickerItemViewInterface> getStickerImageItems() {
        final LinkedList<StickerItemViewInterface> list = new LinkedList<StickerItemViewInterface>();
        for (final StickerItemViewInterface stickerItemViewInterface : this.c) {
            if (stickerItemViewInterface.getStickerType() == StickerType.Image) {
                list.add(stickerItemViewInterface);
            }
        }
        return list;
    }
    
    private StickerItemViewInterface d(final StickerData sticker) {
        if (sticker == null) {
            return null;
        }
        View view = this.buildView(this.a(sticker.getType()), (ViewGroup)this);
        if (!(view instanceof StickerItemViewInterface)) {
            view = this.buildView(this.a(sticker.getType()), (ViewGroup)this);
        }
        final StickerItemViewInterface e = (StickerItemViewInterface)view;
        final StickerType normal = StickerType.Normal;
        StickerType stickerType;
        if (sticker instanceof StickerImageData) {
            stickerType = StickerType.Image;
        }
        else if (sticker.getType().equals(StickerData.StickerType.TypeImage) || sticker.getType().equals(StickerData.StickerType.TypeDynamic)) {
            stickerType = StickerType.Image;
        }
        else {
            stickerType = StickerType.Text;
        }
        e.setStickerViewType(this.f);
        e.setStickerType(stickerType);
        e.setSticker(sticker);
        e.setStroke(-1, ContextUtils.dip2px(this.getContext(), 2.0f));
        e.setParentFrame(TuSdkViewHelper.locationInWindow((View)this));
        e.setDelegate(this);
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(TuSdkContext.dip2px((float)sticker.width), TuSdkContext.dip2px((float)sticker.height));
        if (view instanceof StickerTextItemView && ((StickerTextItemView)view).getTextView() != null) {
            final RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams)((StickerTextItemView)view).getTextView().getLayoutParams();
            final RelativeLayout.LayoutParams layoutParams3 = layoutParams;
            layoutParams3.width += layoutParams2.rightMargin + layoutParams2.leftMargin;
            final RelativeLayout.LayoutParams layoutParams4 = layoutParams;
            layoutParams4.height += TuSdkContext.dip2px(32.0f) + layoutParams2.topMargin + layoutParams2.bottomMargin;
        }
        else if (view instanceof StickerImageItemView) {
            final RelativeLayout.LayoutParams layoutParams5 = (RelativeLayout.LayoutParams)((StickerImageItemView)view).getImageView().getLayoutParams();
            layoutParams.width = sticker.width;
            layoutParams.height = sticker.height;
        }
        this.addView(view, (ViewGroup.LayoutParams)layoutParams);
        this.c.add(e);
        if (this.a != null && (e.getStickerType() == StickerType.Normal || e.getStickerType() == this.f)) {
            this.a.onStickerCountChanged(sticker, (StickerItemViewInterface)view, 1, this.c.size());
        }
        return e;
    }
    
    public void addView(final View view, final ViewGroup.LayoutParams layoutParams) {
        super.addView(view, layoutParams);
    }
    
    @Override
    public void onStickerItemViewClose(final StickerItemViewInterface o) {
        if (o == null) {
            return;
        }
        if (o.getStickerType() != StickerType.Normal && o.getStickerType() != this.f) {
            return;
        }
        if (this.c.remove(o)) {
            this.d = null;
            this.removeView((View)o);
            this.cancelAllStickerSelected();
            if (this.a != null) {
                this.a.onStickerCountChanged(o.getStickerData(), o, 0, this.c.size());
            }
        }
    }
    
    @Override
    public void onStickerItemViewSelected(final StickerItemViewInterface d) {
        if (d == null) {
            return;
        }
        if (d.getStickerType() != StickerType.Normal && d.getStickerType() != this.f) {
            return;
        }
        this.e = false;
        if (d.equals(this.d)) {
            this.e = true;
        }
        this.d = d;
        for (final StickerItemViewInterface obj : this.c) {
            obj.setSelected(d.equals(obj));
        }
        if (this.a == null) {
            return;
        }
        final StickerData mSticker = ((StickerItemViewBase)d).mSticker;
        if (d instanceof StickerTextItemView) {
            this.a.onStickerItemViewSelected(mSticker, ((StickerTextItemView)d).getTextView().getText().toString(), ((StickerTextItemView)d).isNeedReverse());
        }
        else {
            this.a.onStickerItemViewSelected(mSticker, null, false);
        }
    }
    
    @Override
    public void onStickerItemViewReleased(final StickerItemViewInterface stickerItemViewInterface) {
        if (stickerItemViewInterface.getStickerType() != StickerType.Normal && stickerItemViewInterface.getStickerType() != this.f) {
            return;
        }
        if (!this.e) {
            return;
        }
        if (this.a != null) {
            this.a.onStickerItemViewReleased();
        }
    }
    
    public List<StickerResult> getResults(final Rect rect) {
        final ArrayList<StickerResult> list = new ArrayList<StickerResult>(this.c.size());
        final Iterator<StickerItemViewInterface> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getResult(rect));
        }
        return list;
    }
    
    public void cancelAllStickerSelected() {
        final Iterator<StickerItemViewInterface> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            iterator.next().setSelected(false);
        }
        this.d = null;
        if (this.a == null) {
            return;
        }
        this.a.onCancelAllStickerSelected();
    }
    
    @SuppressLint({ "ClickableViewAccessibility" })
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.cancelAllStickerSelected();
        }
        return super.onTouchEvent(motionEvent);
    }
    
    public void resizeForVideo(final TuSdkSize tuSdkSize, final boolean b) {
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.getLayoutParams();
        if (b) {
            final TuSdkSizeF tuSdkSizeF = new TuSdkSizeF();
            final float n = TuSdkContext.getScreenSize().width / (float)TuSdkContext.getScreenSize().height;
            final float n2 = tuSdkSize.width / (float)tuSdkSize.height;
            if (n == n2) {
                tuSdkSizeF.width = (float)TuSdkContext.getScreenSize().width;
                tuSdkSizeF.height = (float)TuSdkContext.getScreenSize().height;
            }
            else if (n2 > n) {
                final float n3 = TuSdkContext.getScreenSize().width / (float)tuSdkSize.width;
                tuSdkSizeF.width = (float)TuSdkContext.getScreenSize().width;
                tuSdkSizeF.height = tuSdkSize.height * n3;
            }
            else if (n2 < n) {
                tuSdkSizeF.width = tuSdkSize.width * (TuSdkContext.getScreenSize().height / (float)tuSdkSize.height);
                tuSdkSizeF.height = (float)TuSdkContext.getScreenSize().height;
            }
            layoutParams.width = (int)tuSdkSizeF.width;
            layoutParams.height = (int)tuSdkSizeF.height;
            layoutParams.leftMargin = (TuSdkContext.getScreenSize().width - layoutParams.width) / 2;
            layoutParams.topMargin = (TuSdkContext.getScreenSize().height - layoutParams.height) / 2;
        }
        else {
            final TuSdkSizeF tuSdkSizeF2 = new TuSdkSizeF();
            final float n4 = tuSdkSize.width / (float)tuSdkSize.height;
            if (n4 < 1.0f) {
                tuSdkSizeF2.width = TuSdkContext.getScreenSize().width * n4;
                tuSdkSizeF2.height = (float)TuSdkContext.getScreenSize().width;
            }
            else if (n4 > 1.0f) {
                tuSdkSizeF2.width = (float)TuSdkContext.getScreenSize().width;
                tuSdkSizeF2.height = TuSdkContext.getScreenSize().width / n4;
            }
            else if (n4 == 1.0f) {
                tuSdkSizeF2.width = (float)TuSdkContext.getScreenSize().width;
                tuSdkSizeF2.height = (float)TuSdkContext.getScreenSize().width;
            }
            layoutParams.width = (int)tuSdkSizeF2.width;
            layoutParams.height = (int)tuSdkSizeF2.height;
            layoutParams.leftMargin = (TuSdkContext.getScreenSize().width - layoutParams.width) / 2;
            layoutParams.topMargin = (TuSdkContext.getScreenSize().width - layoutParams.height) / 2;
        }
    }
    
    public void addSticker(final StickerItemViewInterface e) {
        this.c.add(e);
    }
    
    public void resize(final TuSdkSize tuSdkSize, final ViewGroup viewGroup) {
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.getLayoutParams();
        layoutParams.width = tuSdkSize.width;
        layoutParams.height = tuSdkSize.height;
        layoutParams.leftMargin = (viewGroup.getWidth() - layoutParams.width) / 2;
        layoutParams.topMargin = (viewGroup.getHeight() - layoutParams.height) / 2;
        final Iterator<StickerItemViewInterface> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            iterator.next().setParentFrame(TuSdkViewHelper.locationInWindow((View)this));
        }
    }
    
    public void removeAllSticker() {
        this.c.clear();
        this.removeAllViews();
    }
    
    public enum StickerType
    {
        Normal, 
        Text, 
        Image;
    }
    
    public interface StickerViewDelegate
    {
        boolean canAppendSticker(final StickerView p0, final StickerData p1);
        
        void onStickerItemViewSelected(final StickerData p0, final String p1, final boolean p2);
        
        void onStickerItemViewReleased();
        
        void onCancelAllStickerSelected();
        
        void onStickerCountChanged(final StickerData p0, final StickerItemViewInterface p1, final int p2, final int p3);
    }
}
