// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.album;

import android.graphics.Bitmap;
import android.view.View;
import android.support.v4.view.ViewCompat;
//import org.lasque.tusdk.core.task.AlbumTaskManager;
import android.widget.ImageView;
import android.util.AttributeSet;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task.AlbumTaskManager;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;
//import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
//import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;

public abstract class TuPhotoListGridBase extends TuSdkCellRelativeLayout<ImageSqlInfo>
{
    public TuPhotoListGridBase(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuPhotoListGridBase(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuPhotoListGridBase(final Context context) {
        super(context);
    }
    
    public abstract ImageView getPosterView();
    
    @Override
    protected void bindModel() {
        AlbumTaskManager.shared.loadThumbImage(this.getPosterView(), this.getModel());
    }
    
    @Override
    public void viewNeedRest() {
        ViewCompat.setAlpha((View)this.getPosterView(), 1.0f);
        AlbumTaskManager.shared.cancelLoadImage(this.getPosterView());
        if (this.getPosterView() != null) {
            this.getPosterView().setImageBitmap((Bitmap)null);
        }
        super.viewNeedRest();
    }
    
    @Override
    public void viewWillDestory() {
        this.viewNeedRest();
        super.viewWillDestory();
    }
}
