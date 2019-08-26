// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.filter;

import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ImageView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task.FilterTaskInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;
//import org.lasque.tusdk.core.task.FilterTaskInterface;
//import org.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;
//import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;

public abstract class
GroupFilterItemViewBase extends TuSdkCellRelativeLayout<GroupFilterItem> implements TuSdkListSelectableCellViewInterface, GroupFilterItemViewInterface
{
    private boolean a;
    private GroupFilterAction b;
    private boolean c;
    private FilterTaskInterface d;
    private Runnable e;
    
    public abstract ImageView getImageView();
    
    protected abstract void setSelectedIcon(final GroupFilterItem p0, final boolean p1);
    
    protected abstract void handleTypeHistory(final GroupFilterItem p0);
    
    protected abstract void handleTypeOnlie(final GroupFilterItem p0);
    
    public GroupFilterItemViewBase(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.b = GroupFilterAction.ActionNormal;
        this.e = new Runnable() {
            @Override
            public void run() {
                GroupFilterItemViewBase.this.b();
            }
        };
    }
    
    public GroupFilterItemViewBase(final Context context, final AttributeSet set) {
        super(context, set);
        this.b = GroupFilterAction.ActionNormal;
        this.e = new Runnable() {
            @Override
            public void run() {
                GroupFilterItemViewBase.this.b();
            }
        };
    }
    
    public GroupFilterItemViewBase(final Context context) {
        super(context);
        this.b = GroupFilterAction.ActionNormal;
        this.e = new Runnable() {
            @Override
            public void run() {
                GroupFilterItemViewBase.this.b();
            }
        };
    }
    
    @Override
    public boolean isActivating() {
        return this.a;
    }
    
    public GroupFilterAction getAction() {
        if (this.b == null) {
            this.b = GroupFilterAction.ActionNormal;
        }
        return this.b;
    }
    
    @Override
    public void setAction(final GroupFilterAction b) {
        this.b = b;
    }
    
    public boolean isDisplaySelectionIcon() {
        return this.c;
    }
    
    public void setDisplaySelectionIcon(final boolean c) {
        this.c = c;
    }
    
    @Override
    public boolean isCameraAction() {
        return this.getAction() == GroupFilterAction.ActionCamera;
    }
    
    @Override
    public void setFilterTask(final FilterTaskInterface d) {
        this.d = d;
    }
    
    public FilterTaskInterface getFilterTask() {
        return this.d;
    }
    
    public boolean isRenderFilterThumb() {
        return this.d != null && this.d.isRenderFilterThumb();
    }
    
    private void a(final long n) {
        this.a();
        this.a = true;
        ThreadHelper.postDelayed(this.e, n);
    }
    
    private void a() {
        this.a = false;
        ThreadHelper.cancel(this.e);
    }
    
    private void b() {
        this.a = false;
    }
    
    @Override
    protected void bindModel() {
        final GroupFilterItem groupFilterItem = this.getModel();
        if (groupFilterItem == null || groupFilterItem.type == null) {
            return;
        }
        switch (groupFilterItem.type.ordinal()) {
            case 1: {
                this.setSelectedIcon(groupFilterItem, false);
                this.handleTypeGroup(groupFilterItem);
                break;
            }
            case 2: {
                if (groupFilterItem.filterOption == null) {
                    this.setSelectedIcon(groupFilterItem, this.isCameraAction());
                    this.handleTypeOrgin(groupFilterItem);
                    break;
                }
                this.setSelectedIcon(groupFilterItem, this.isCameraAction() || groupFilterItem.filterOption.canDefinition);
                this.handleTypeFilter(groupFilterItem);
                break;
            }
            case 3: {
                this.setSelectedIcon(groupFilterItem, false);
                this.handleTypeHistory(groupFilterItem);
                break;
            }
            case 4: {
                this.setSelectedIcon(groupFilterItem, false);
                this.handleTypeOnlie(groupFilterItem);
                break;
            }
        }
    }
    
    protected void handleTypeOrgin(final GroupFilterItem groupFilterItem) {
        if (this.getImageView() != null && this.isRenderFilterThumb()) {
            this.d.loadImage(this.getImageView(), "Normal");
        }
    }
    
    protected void handleTypeGroup(final GroupFilterItem groupFilterItem) {
        if (groupFilterItem == null || groupFilterItem.filterGroup == null || this.getImageView() == null) {
            return;
        }
        this.getImageView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (this.d != null) {
            this.d.loadImage(this.getImageView(), FilterLocalPackage.shared().getGroupDefaultFilterCode(groupFilterItem.filterGroup));
        }
        else {
            FilterLocalPackage.shared().loadGroupDefaultFilterThumb(this.getImageView(), groupFilterItem.filterGroup);
        }
    }
    
    protected void handleTypeFilter(final GroupFilterItem groupFilterItem) {
        if (groupFilterItem == null || groupFilterItem.filterOption == null || this.getImageView() == null) {
            return;
        }
        this.getImageView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (this.d != null) {
            this.d.loadImage(this.getImageView(), groupFilterItem.filterOption.code);
        }
        else {
            FilterLocalPackage.shared().loadFilterThumb(this.getImageView(), groupFilterItem.filterOption);
        }
    }
    
    protected void handleBlockView(final int n, final int imageResource) {
        if (this.getImageView() != null && imageResource != 0) {
            this.getImageView().setImageResource(imageResource);
            this.getImageView().setScaleType(ImageView.ScaleType.CENTER);
        }
    }
    
    @Override
    public void viewNeedRest() {
        super.viewNeedRest();
        this.a();
        this.setSelected(false);
        if (this.getImageView() != null) {
            this.getImageView().setImageBitmap((Bitmap)null);
            if (this.d != null) {
                this.d.cancelLoadImage(this.getImageView());
            }
            else {
                FilterLocalPackage.shared().cancelLoadImage(this.getImageView());
            }
        }
    }
    
    @Override
    public void onCellSelected(final int n) {
        this.setSelected(true);
    }
    
    @Override
    public void onCellDeselected() {
        this.setSelected(false);
        this.stopActivating();
    }
    
    @Override
    public void waitInActivate(final long n) {
        if (this.a) {
            return;
        }
        this.a(n);
    }
    
    @Override
    public void stopActivating() {
        if (!this.a) {
            return;
        }
        this.a();
    }
}
