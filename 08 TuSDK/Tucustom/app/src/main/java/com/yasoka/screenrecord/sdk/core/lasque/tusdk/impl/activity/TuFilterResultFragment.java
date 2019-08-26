// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity;

//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import android.graphics.Bitmap;
import android.os.Handler;
//import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import android.view.ViewGroup;
import android.content.Context;
//import org.lasque.tusdk.core.seles.tusdk.FilterImageView;
import android.widget.RelativeLayout;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.TuSdkResult;
//import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterImageView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterImageViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
//import org.lasque.tusdk.core.seles.tusdk.FilterImageViewInterface;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;

public abstract class TuFilterResultFragment extends TuImageResultFragment implements ParameterConfigViewInterface.ParameterConfigViewDelegate
{
    private TuFilterResultFragmentDelegate a;
    private FilterWrap b;
    private FilterImageViewInterface c;
    protected View.OnClickListener mButtonClickListener;
    
    public TuFilterResultFragment() {
        this.mButtonClickListener = (View.OnClickListener)new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                TuFilterResultFragment.this.dispatcherViewClick(view);
            }
        };
    }
    
    public TuFilterResultFragmentDelegate getDelegate() {
        return this.a;
    }
    
    public void setDelegate(final TuFilterResultFragmentDelegate a) {
        this.setErrorListener(this.a = a);
    }
    
    @Override
    protected void notifyProcessing(final TuSdkResult tuSdkResult) {
        if (this.showResultPreview(tuSdkResult)) {
            return;
        }
        if (this.a == null) {
            return;
        }
        this.a.onTuFilterResultFragmentEdited(this, tuSdkResult);
    }
    
    @Override
    protected boolean asyncNotifyProcessing(final TuSdkResult tuSdkResult) {
        return this.a != null && this.a.onTuFilterResultFragmentEditedAsync(this, tuSdkResult);
    }
    
    private FilterWrap a() {
        return this.b;
    }
    
    protected final void setFilterWrap(final FilterWrap b) {
        this.b = b;
    }
    
    protected SelesParameters getFilterParameter() {
        if (this.a() == null) {
            return null;
        }
        return this.a().getFilterParameter();
    }
    
    public abstract RelativeLayout getImageWrapView();
    
    public abstract View getCancelButton();
    
    public abstract View getCompleteButton();
    
    public abstract <T extends View> T getConfigView();
    
    public <T extends View> T getImageView() {
        if (this.c == null && this.getImageWrapView() != null) {
            (this.c = new FilterImageView((Context)this.getActivity())).enableTouchForOrigin();
            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.addRule(13);
            this.getImageWrapView().addView((View)this.c, 0, (ViewGroup.LayoutParams)layoutParams);
        }
        return (T)this.c;
    }
    
    protected void dispatcherViewClick(final View view) {
        if (this.equalViewIds(view, this.getCancelButton())) {
            this.handleBackButton();
        }
        else if (this.equalViewIds(view, this.getCompleteButton())) {
            this.handleCompleteButton();
        }
    }
    
    protected void handleBackButton() {
        this.navigatorBarBackAction(null);
    }
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
        this.getCancelButton();
        this.getCompleteButton();
        this.getConfigView();
        this.getImageView();
    }
    
    @Override
    protected void viewDidLoad(final ViewGroup viewGroup) {
        this.loadImageWithThread();
        if (this.getConfigView() == null) {
            return;
        }
        this.refreshConfigView();
    }

    protected void refreshConfigView() {
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                SelesParameters var1 = TuFilterResultFragment.this.a().getFilterParameter();
                if (var1 != null && var1.size() != 0) {
                    ((ParameterConfigViewInterface)TuFilterResultFragment.this.getConfigView()).setParams(var1.getArgKeys(), 0);
                }
            }
        }, 1L);
    }
    
    @Override
    protected void asyncLoadImageCompleted(Bitmap var1) {
        super.asyncLoadImageCompleted(var1);
        if (var1 != null) {
            if (this.getImageView() != null && this.getConfigView() != null) {
                ((FilterImageViewInterface)this.getImageView()).setImage(var1);
                ((FilterImageViewInterface)this.getImageView()).setFilterWrap(this.a());
            }
        }
    }

    protected void setImageViewFilter(FilterWrap var1) {
        this.setFilterWrap(var1);
        if (this.getImageView() != null) {
            if (var1 == null) {
                var1 = FilterLocalPackage.shared().getFilterWrap((String)null);
            }

            ((FilterImageViewInterface)this.getImageView()).setFilterWrap(var1);
        }
    }
    
    @Override
    public void onParameterConfigDataChanged(final ParameterConfigViewInterface parameterConfigViewInterface, final int n, final float precentValue) {
        final SelesParameters.FilterArg filterArg = this.getFilterArg(n);
        if (filterArg == null) {
            return;
        }
        filterArg.setPrecentValue(precentValue);
        this.requestRender();
    }
    
    @Override
    public void onParameterConfigRest(final ParameterConfigViewInterface parameterConfigViewInterface, final int n) {
        final SelesParameters.FilterArg filterArg = this.getFilterArg(n);
        if (filterArg == null) {
            return;
        }
        filterArg.reset();
        this.requestRender();
        parameterConfigViewInterface.seekTo(filterArg.getPrecentValue());
    }
    
    @Override
    public float readParameterValue(final ParameterConfigViewInterface parameterConfigViewInterface, final int n) {
        final SelesParameters.FilterArg filterArg = this.getFilterArg(n);
        if (filterArg == null) {
            return 0.0f;
        }
        return filterArg.getPrecentValue();
    }
    
    protected SelesParameters.FilterArg getFilterArg(final int n) {
        if (n < 0) {
            return null;
        }
        final SelesParameters filterParameter = this.a().getFilterParameter();
        if (filterParameter == null || n >= filterParameter.size()) {
            return null;
        }
        return filterParameter.getArgs().get(n);
    }

    protected void requestRender() {
        if (this.getImageView() != null) {
            ((FilterImageViewInterface)this.getImageView()).requestRender();
        }

    }
    
    protected void handleCompleteButton() {
        final TuSdkResult tuSdkResult = new TuSdkResult();
        tuSdkResult.filterWrap = this.a();
        this.hubStatus(TuSdkContext.getString("lsq_edit_processing"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                TuFilterResultFragment.this.asyncEditWithResult(tuSdkResult);
            }
        }).start();
    }
    
    protected void asyncEditWithResult(final TuSdkResult tuSdkResult) {
        this.loadOrginImage(tuSdkResult);
        if (tuSdkResult.filterWrap != null) {
            tuSdkResult.image = BitmapHelper.imageScale(tuSdkResult.image, TuSdkSize.create(tuSdkResult.image).limitScale());
            tuSdkResult.image = tuSdkResult.filterWrap.clone().process(tuSdkResult.image);
        }
        this.asyncProcessingIfNeedSave(tuSdkResult);
    }
    
    public interface TuFilterResultFragmentDelegate extends TuSdkComponentErrorListener
    {
        void onTuFilterResultFragmentEdited(final TuFilterResultFragment p0, final TuSdkResult p1);
        
        boolean onTuFilterResultFragmentEditedAsync(final TuFilterResultFragment p0, final TuSdkResult p1);
    }
}
