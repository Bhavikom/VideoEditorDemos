// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.seles.SelesParameters;
import android.widget.TextView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.TuSeekBar;
//import org.lasque.tusdk.impl.view.widget.TuSeekBar;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public class FilterConfigSeekbar extends TuSdkRelativeLayout
{
    private TuSeekBar a;
    private TextView b;
    private TextView c;
    private SelesParameters.FilterArg d;
    private String e;
    private FilterConfigSeekbarDelegate f;
    private TuSeekBar.TuSeekBarDelegate g;
    
    public static int getLayoutId() {
        return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_filter_config_seekbar");
    }
    
    public FilterConfigSeekbar(final Context context) {
        super(context);
        this.e = "%02d";
        this.g = new TuSeekBar.TuSeekBarDelegate() {
            @Override
            public void onTuSeekBarChanged(final TuSeekBar tuSeekBar, final float n) {
                FilterConfigSeekbar.this.a(n);
            }
        };
    }
    
    public FilterConfigSeekbar(final Context context, final AttributeSet set) {
        super(context, set);
        this.e = "%02d";
        this.g = new TuSeekBar.TuSeekBarDelegate() {
            @Override
            public void onTuSeekBarChanged(final TuSeekBar tuSeekBar, final float n) {
                FilterConfigSeekbar.this.a(n);
            }
        };
    }
    
    public FilterConfigSeekbar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.e = "%02d";
        this.g = new TuSeekBar.TuSeekBarDelegate() {
            @Override
            public void onTuSeekBarChanged(final TuSeekBar tuSeekBar, final float n) {
                FilterConfigSeekbar.this.a(n);
            }
        };
    }
    
    public TuSeekBar getSeekbar() {
        if (this.a == null) {
            this.a = this.getViewById("lsq_seekView");
            if (this.a != null) {
                this.a.setDelegate(this.g);
            }
        }
        return this.a;
    }
    
    public void setValueFormatString(final String e) {
        if (TextUtils.isEmpty((CharSequence)e)) {
            return;
        }
        this.e = e;
    }
    
    private void a(final float n) {
        this.b(n);
        if (this.f != null) {
            this.f.onSeekbarDataChanged(this, this.d);
        }
    }
    
    public final TextView getTitleView() {
        if (this.b == null) {
            this.b = this.getViewById("lsq_titleView");
        }
        return this.b;
    }
    
    public final TextView getNumberView() {
        if (this.c == null) {
            this.c = this.getViewById("lsq_numberView");
        }
        return this.c;
    }
    
    public FilterConfigSeekbarDelegate getDelegate() {
        return this.f;
    }
    
    public void setDelegate(final FilterConfigSeekbarDelegate f) {
        this.f = f;
    }
    
    public void setFilterArg(final SelesParameters.FilterArg d) {
        this.d = d;
        if (this.d == null) {
            return;
        }
        final TuSeekBar seekbar = this.getSeekbar();
        if (seekbar == null) {
            return;
        }
        seekbar.setProgress(d.getPrecentValue());
        if (this.getTitleView() != null) {
            this.getTitleView().setText((CharSequence)TuSdkContext.getString("lsq_filter_set_" + d.getKey()));
        }
        this.b(d.getPrecentValue());
    }
    
    private void b(final float precentValue) {
        if (this.d != null) {
            this.d.setPrecentValue(precentValue);
        }
        if (this.getNumberView() != null) {
            this.getNumberView().setText((CharSequence)String.format(this.e, (int)(precentValue * 100.0f)));
        }
    }
    
    public void reset() {
        if (this.d == null) {
            return;
        }
        this.d.reset();
        this.setFilterArg(this.d);
    }
    
    public interface FilterConfigSeekbarDelegate
    {
        void onSeekbarDataChanged(final FilterConfigSeekbar p0, final SelesParameters.FilterArg p1);
    }
}
