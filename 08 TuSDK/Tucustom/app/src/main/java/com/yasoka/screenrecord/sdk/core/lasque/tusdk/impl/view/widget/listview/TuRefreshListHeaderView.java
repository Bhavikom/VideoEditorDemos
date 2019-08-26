// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.listview;

//import org.lasque.tusdk.impl.TuDateHelper;
import java.util.Calendar;
import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.TuSdkContext;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkRefreshListHeaderView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.TuDateHelper;
//import org.lasque.tusdk.core.view.listview.TuSdkRefreshListHeaderView;

public class TuRefreshListHeaderView extends TuSdkRefreshListHeaderView
{
    private RelativeLayout a;
    private TextView b;
    private TextView c;
    private ImageView d;
    
    public static int getLayoutId() {
        return TuSdkContext.getLayoutResId("tusdk_view_widget_list_view_refresh_header_view");
    }
    
    public TuRefreshListHeaderView(final Context context) {
        super(context);
    }
    
    public TuRefreshListHeaderView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuRefreshListHeaderView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    @Override
    public RelativeLayout getHeadWrap() {
        if (this.a == null) {
            this.a = this.getViewById("lsq_headWrap");
        }
        return this.a;
    }
    
    public void setHeadWrap(final RelativeLayout a) {
        this.a = a;
    }
    
    public TextView getTitleLabel() {
        if (this.b == null) {
            this.b = this.getViewById("lsq_titleLabel");
        }
        return this.b;
    }
    
    public void setTitleLabel(final TextView b) {
        this.b = b;
    }
    
    public TextView getLastLoadTime() {
        if (this.c == null) {
            this.c = this.getViewById("lsq_lastLoadTime");
        }
        return this.c;
    }
    
    public void setLastLoadTime(final TextView c) {
        this.c = c;
    }
    
    @Override
    public ImageView getLoadIcon() {
        if (this.d == null) {
            this.d = this.getViewById("lsq_loadIcon");
        }
        return this.d;
    }
    
    public void setLoadIcon(final ImageView d) {
        this.d = d;
    }
    
    @Override
    public void setLastDate(final Calendar lastDate) {
        super.setLastDate(lastDate);
        final TextView lastLoadTime = this.getLastLoadTime();
        if (lastDate == null || lastLoadTime == null) {
            return;
        }
        lastLoadTime.setText((CharSequence)(TuSdkContext.getString("lsq_refresh_list_view_state_lasttime") + TuDateHelper.timestampSNS(lastDate)));
    }
    
    @Override
    public void setState(final TuSdkRefreshState state) {
        super.setState(state);
        if (state == null || this.b == null) {
            return;
        }
        int text = 0;
        switch (state.ordinal()) {
            case 1: {
                text = TuSdkContext.getStringResId("lsq_refresh_list_view_state_hidden");
                break;
            }
            case 2: {
                text = TuSdkContext.getStringResId("lsq_refresh_list_view_state_visible");
                break;
            }
            case 3: {
                text = TuSdkContext.getStringResId("lsq_refresh_list_view_state_triggered");
                break;
            }
            case 4: {
                text = TuSdkContext.getStringResId("lsq_refresh_list_view_state_loading");
                break;
            }
        }
        this.b.setText(text);
    }
}
