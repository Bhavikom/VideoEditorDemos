// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.filter;

//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.os.Looper;
//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
//import org.lasque.tusdk.core.TuSdkContext;
import android.os.Handler;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.TuSdkResult;
//import org.lasque.tusdk.modules.components.ComponentErrorType;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
//import org.lasque.tusdk.core.seles.tusdk.FilterImageView;
import android.widget.RelativeLayout;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterImageView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterImageViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuImageResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentErrorType;
//import org.lasque.tusdk.core.seles.tusdk.FilterImageViewInterface;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.impl.activity.TuImageResultFragment;

public abstract class TuEditFilterFragmentBase extends TuImageResultFragment {
    private FilterWrap a;
    private FilterImageViewInterface b;

    public TuEditFilterFragmentBase() {
    }

    public abstract RelativeLayout getImageWrapView();

    public abstract void notifyFilterConfigView();

    public abstract boolean isOnlyReturnFilter();

    public FilterWrap getFilterWrap() {
        return this.a;
    }

    public void setFilterWrap(FilterWrap var1) {
        this.a = var1;
    }

    public <T extends View & FilterImageViewInterface> T getImageView() {
        if (this.b == null && this.getImageWrapView() != null) {
            this.b = new FilterImageView(this.getActivity());
            this.b.enableTouchForOrigin();
            RelativeLayout.LayoutParams var1 = new RelativeLayout.LayoutParams(-1, -1);
            var1.addRule(13);
            this.getImageWrapView().addView((View)this.b, 0, var1);
        }

        return (T) this.b;
    }

    protected void loadView(ViewGroup var1) {
        this.getImageView();
    }

    protected void viewDidLoad(ViewGroup var1) {
        StatisticsManger.appendComponent(ComponentActType.editFilterFragment);
        if (this.getImage() == null) {
            this.notifyError((TuSdkResult)null, ComponentErrorType.TypeInputImageEmpty);
            TLog.e("Can not find input image.", new Object[0]);
        } else if (this.getImageView() != null) {
            ((FilterImageViewInterface)this.getImageView()).setImage(this.getImage());
            if (this.getFilterWrap() != null) {
                this.a = this.getFilterWrap().clone();
                ((FilterImageViewInterface)this.getImageView()).setFilterWrap(this.a);
            }

            (new Handler()).postDelayed(new Runnable() {
                public void run() {
                    TuEditFilterFragmentBase.this.notifyFilterConfigView();
                }
            }, 1L);
        }
    }

    protected boolean handleSwitchFilter(final String var1) {
        if (var1 != null && this.getImageView() != null) {
            if (this.a != null && this.a.equalsCode(var1)) {
                return false;
            } else {
                this.hubStatus(TuSdkContext.getString("lsq_edit_filter_processing"));
                (new Thread(new Runnable() {
                    public void run() {
                        TuEditFilterFragmentBase.this.asyncProcessingFilter(var1);
                    }
                })).start();
                return true;
            }
        } else {
            return false;
        }
    }

    protected void asyncProcessingFilter(String var1) {
        this.a = FilterLocalPackage.shared().getFilterWrap(var1);
        if (this.a != null) {
            ((FilterImageViewInterface)this.getImageView()).setFilterWrap(this.a);
        }

        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            public void run() {
                TuEditFilterFragmentBase.this.processedFilter();
            }
        });
    }

    protected void processedFilter() {
        this.hubDismiss();
        this.notifyFilterConfigView();
    }

    protected void handleCompleteButton() {
        final TuSdkResult var1 = new TuSdkResult();
        var1.filterWrap = this.getFilterWrap();
        this.hubStatus(TuSdkContext.getString("lsq_edit_processing"));
        (new Thread(new Runnable() {
            public void run() {
                TuEditFilterFragmentBase.this.asyncEditWithResult(var1);
            }
        })).start();
    }

    protected void asyncEditWithResult(TuSdkResult var1) {
        if (this.isOnlyReturnFilter()) {
            this.backUIThreadNotifyProcessing(var1);
        } else {
            this.loadOrginImage(var1);
            if (var1.filterWrap != null && var1.image != null) {
                float var2 = TuSdkSize.create(var1.image).limitScale();
                var1.image = BitmapHelper.imageScale(var1.image, var2);
                var1.image = var1.filterWrap.process(var1.image);
            }

            this.asyncProcessingIfNeedSave(var1);
        }
    }
}
