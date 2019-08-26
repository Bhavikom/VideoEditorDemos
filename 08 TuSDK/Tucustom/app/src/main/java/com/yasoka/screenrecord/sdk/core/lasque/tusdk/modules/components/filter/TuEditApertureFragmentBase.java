// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.filter;

import android.view.animation.Transformation;
//import org.lasque.tusdk.core.seles.tusdk.filters.blurs.TuSDKApertureFilter;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import android.view.animation.Interpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import java.util.ArrayList;
//import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
//import org.lasque.tusdk.core.seles.tusdk.FilterImageViewInterface;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;
//import org.lasque.tusdk.core.seles.SelesParameters;
import android.view.MotionEvent;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterImageViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.blurs.TuSDKApertureFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkGestureRecognizer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuFilterResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.core.utils.TuSdkGestureRecognizer;
//import org.lasque.tusdk.impl.activity.TuFilterResultFragment;

public abstract class TuEditApertureFragmentBase extends TuFilterResultFragment
{
    private int a;
    private MaskAnimation b;
    private boolean c;
    private Runnable d;
    private TuSdkGestureRecognizer e;
    
    public TuEditApertureFragmentBase() {
        this.d = new Runnable() {
            @Override
            public void run() {
                TuEditApertureFragmentBase.this.b();
            }
        };
        this.e = new TuSdkGestureRecognizer() {
            @Override
            public void onTouchBegin(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent) {
                TuEditApertureFragmentBase.this.a(true);
            }
            
            @Override
            public void onTouchEnd(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent, final StepData stepData) {
                TuEditApertureFragmentBase.this.a(false);
            }
            
            @Override
            public void onTouchSingleMove(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent, final StepData stepData) {
                final SelesParameters b = getFilterParameter();
                if (b == null) {
                    return;
                }
                b.stepFilterArg("centerX", tuSdkGestureRecognizer.getStepPoint().x / view.getWidth());
                b.stepFilterArg("centerY", tuSdkGestureRecognizer.getStepPoint().y / view.getHeight());
                requestRender();
            }
            
            @Override
            public void onTouchMultipleMove(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent, final StepData stepData) {
                final SelesParameters d = getFilterParameter();
                if (d == null) {
                    return;
                }
                d.stepFilterArg("radius", tuSdkGestureRecognizer.getStepSpace() / tuSdkGestureRecognizer.getSpace());
                final SelesParameters.FilterArg filterArg = d.getFilterArg("degree");
                if (filterArg != null) {
                    float precentValue = filterArg.getPrecentValue() + tuSdkGestureRecognizer.getStepDegree() / 360.0f;
                    if (precentValue < 0.0f) {
                        ++precentValue;
                    }
                    else if (precentValue > 1.0f) {
                        --precentValue;
                    }
                    filterArg.setPrecentValue(precentValue);
                }
                requestRender();
            }
        };
    }
    
    protected abstract void setConfigViewShowState(final boolean p0);
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
        super.loadView(viewGroup);
        StatisticsManger.appendComponent(ComponentActType.editApertureFragment);
        this.setFilterWrap(this.f());
        if (this.getImageView() != null) {
            ((FilterImageViewInterface)this.getImageView()).disableTouchForOrigin();
        }
        if (this.getImageWrapView() != null) {
            this.getImageWrapView().setOnTouchListener((View.OnTouchListener)this.e);
        }
    }
    
    @Override
    public void onParameterConfigDataChanged(final ParameterConfigViewInterface parameterConfigViewInterface, final int n, final float n2) {
        super.onParameterConfigDataChanged(parameterConfigViewInterface, 0, n2);
    }
    
    @Override
    public void onParameterConfigRest(final ParameterConfigViewInterface parameterConfigViewInterface, final int n) {
        super.onParameterConfigRest(parameterConfigViewInterface, 0);
    }
    
    @Override
    public float readParameterValue(final ParameterConfigViewInterface parameterConfigViewInterface, final int n) {
        return super.readParameterValue(parameterConfigViewInterface, 0);
    }
    
    protected void handleSelectiveAction(final int a, final float n) {
        if (this.a == a) {
            if (this.a > 0) {
                this.a();
            }
            return;
        }
        this.a = a;
        this.a(n);
    }
    
    private void a(final float n) {
        final SelesParameters filterParameter = this.getFilterParameter();
        if (filterParameter == null) {
            return;
        }
        filterParameter.reset();
        filterParameter.setFilterArg("selective", n);
        this.onParameterConfigRest((ParameterConfigViewInterface)this.getConfigView(), 0);
        if (n > 0.0f) {
            this.a();
        }
    }
    
    private void a() {
        if (this.getConfigView() == null) {
            return;
        }
        final ArrayList<String> list = new ArrayList<String>();
        list.add("aperture");
        ((ParameterConfigViewInterface)this.getConfigView()).setParams(list, 0);
        this.setConfigViewShowState(true);
        this.a(true);
        this.c();
    }
    
    private void b() {
        this.a(false);
    }
    
    private void c() {
        ThreadHelper.postDelayed(this.d, 1000L);
    }
    
    private void d() {
        ThreadHelper.cancel(this.d);
    }
    
    private void a(final boolean c) {
        this.d();
        if (this.c == c) {
            return;
        }
        this.c = c;
        this.getImageWrapView().startAnimation((Animation)this.e());
    }
    
    private MaskAnimation e() {
        if (this.b == null) {
            (this.b = new MaskAnimation()).setDuration(260L);
            this.b.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        }
        this.b.cancel();
        this.b.reset();
        return this.b;
    }
    
    private void b(float n) {
        final SelesParameters filterParameter = this.getFilterParameter();
        if (filterParameter == null) {
            return;
        }
        if (!this.c) {
            n = 1.0f - n;
        }
        filterParameter.setFilterArg("maskAlpha", n);
        this.requestRender();
    }
    
    protected void handleConfigCompeleteButton() {
        this.setConfigViewShowState(false);
    }
    
    @Override
    protected void handleCompleteButton() {
        this.d();
        this.e().cancel();
        final SelesParameters filterParameter = this.getFilterParameter();
        if (filterParameter == null) {
            return;
        }
        filterParameter.reset("maskAlpha");
        this.requestRender();
        super.handleCompleteButton();
    }
    
    private FilterWrap f() {
        final FilterOption filterOption = new FilterOption() {
            @Override
            public SelesOutInput getFilter() {
                return new TuSDKApertureFilter();
            }
        };
        filterOption.id = Long.MAX_VALUE;
        filterOption.canDefinition = true;
        filterOption.isInternal = true;
        return FilterWrap.creat(filterOption);
    }
    
    private class MaskAnimation extends Animation
    {
        protected void applyTransformation(final float n, final Transformation transformation) {
            TuEditApertureFragmentBase.this.b(n);
        }
    }
}
