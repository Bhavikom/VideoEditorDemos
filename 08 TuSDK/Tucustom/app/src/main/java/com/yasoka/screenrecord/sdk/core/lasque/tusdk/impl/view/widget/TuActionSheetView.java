// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget;

//import org.lasque.tusdk.impl.TuAnimType;
//import org.lasque.tusdk.core.type.ActivityAnimType;
import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.TuSdkContext;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ActivityAnimType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.TuSdkActionSheetView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.TuAnimType;
//import org.lasque.tusdk.core.view.widget.TuSdkActionSheetView;

public class TuActionSheetView extends TuSdkActionSheetView
{
    private View a;
    private LinearLayout b;
    private TextView c;
    private Button d;
    
    public static int getLayoutId() {
        return TuSdkContext.getLayoutResId("tusdk_view_widget_actionsheet");
    }
    
    public TuActionSheetView(final Context context) {
        super(context);
    }
    
    public TuActionSheetView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuActionSheetView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    @Override
    public View getMaskBg() {
        if (this.a == null) {
            this.a = this.getViewById("lsq_maskBg");
        }
        return this.a;
    }
    
    @Override
    public LinearLayout getSheetTable() {
        if (this.b == null) {
            this.b = this.getViewById("lsq_sheetTable");
        }
        return this.b;
    }
    
    @Override
    public TextView getTitleView() {
        if (this.c == null) {
            this.c = this.getViewById("lsq_titleView");
        }
        return this.c;
    }
    
    @Override
    public Button getCancelButton() {
        if (this.d == null) {
            this.d = this.getViewById("lsq_cancelButton");
        }
        return this.d;
    }
    
    @Override
    public ActivityAnimType getAlphaAnimType() {
        return TuAnimType.fade;
    }
    
    @Override
    public ActivityAnimType getTransAnimType() {
        return TuAnimType.upDownSub;
    }
}
