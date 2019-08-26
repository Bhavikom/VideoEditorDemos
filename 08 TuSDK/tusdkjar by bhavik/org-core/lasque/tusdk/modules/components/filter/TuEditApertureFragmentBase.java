package org.lasque.tusdk.modules.components.filter;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.FilterImageViewInterface;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.filters.blurs.TuSDKApertureFilter;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.TuSdkGestureRecognizer;
import org.lasque.tusdk.core.utils.TuSdkGestureRecognizer.StepData;
import org.lasque.tusdk.impl.activity.TuFilterResultFragment;
import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import org.lasque.tusdk.modules.components.ComponentActType;

public abstract class TuEditApertureFragmentBase
  extends TuFilterResultFragment
{
  private int a;
  private MaskAnimation b;
  private boolean c;
  private Runnable d = new Runnable()
  {
    public void run()
    {
      TuEditApertureFragmentBase.a(TuEditApertureFragmentBase.this);
    }
  };
  private TuSdkGestureRecognizer e = new TuSdkGestureRecognizer()
  {
    public void onTouchBegin(TuSdkGestureRecognizer paramAnonymousTuSdkGestureRecognizer, View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
    {
      TuEditApertureFragmentBase.a(TuEditApertureFragmentBase.this, true);
    }
    
    public void onTouchEnd(TuSdkGestureRecognizer paramAnonymousTuSdkGestureRecognizer, View paramAnonymousView, MotionEvent paramAnonymousMotionEvent, TuSdkGestureRecognizer.StepData paramAnonymousStepData)
    {
      TuEditApertureFragmentBase.a(TuEditApertureFragmentBase.this, false);
    }
    
    public void onTouchSingleMove(TuSdkGestureRecognizer paramAnonymousTuSdkGestureRecognizer, View paramAnonymousView, MotionEvent paramAnonymousMotionEvent, TuSdkGestureRecognizer.StepData paramAnonymousStepData)
    {
      SelesParameters localSelesParameters = TuEditApertureFragmentBase.b(TuEditApertureFragmentBase.this);
      if (localSelesParameters == null) {
        return;
      }
      localSelesParameters.stepFilterArg("centerX", paramAnonymousTuSdkGestureRecognizer.getStepPoint().x / paramAnonymousView.getWidth());
      localSelesParameters.stepFilterArg("centerY", paramAnonymousTuSdkGestureRecognizer.getStepPoint().y / paramAnonymousView.getHeight());
      TuEditApertureFragmentBase.c(TuEditApertureFragmentBase.this);
    }
    
    public void onTouchMultipleMove(TuSdkGestureRecognizer paramAnonymousTuSdkGestureRecognizer, View paramAnonymousView, MotionEvent paramAnonymousMotionEvent, TuSdkGestureRecognizer.StepData paramAnonymousStepData)
    {
      SelesParameters localSelesParameters = TuEditApertureFragmentBase.d(TuEditApertureFragmentBase.this);
      if (localSelesParameters == null) {
        return;
      }
      localSelesParameters.stepFilterArg("radius", paramAnonymousTuSdkGestureRecognizer.getStepSpace() / paramAnonymousTuSdkGestureRecognizer.getSpace());
      SelesParameters.FilterArg localFilterArg = localSelesParameters.getFilterArg("degree");
      if (localFilterArg != null)
      {
        float f = localFilterArg.getPrecentValue() + paramAnonymousTuSdkGestureRecognizer.getStepDegree() / 360.0F;
        if (f < 0.0F) {
          f += 1.0F;
        } else if (f > 1.0F) {
          f -= 1.0F;
        }
        localFilterArg.setPrecentValue(f);
      }
      TuEditApertureFragmentBase.e(TuEditApertureFragmentBase.this);
    }
  };
  
  protected abstract void setConfigViewShowState(boolean paramBoolean);
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    super.loadView(paramViewGroup);
    StatisticsManger.appendComponent(ComponentActType.editApertureFragment);
    setFilterWrap(f());
    if (getImageView() != null) {
      ((FilterImageViewInterface)getImageView()).disableTouchForOrigin();
    }
    if (getImageWrapView() != null) {
      getImageWrapView().setOnTouchListener(this.e);
    }
  }
  
  public void onParameterConfigDataChanged(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt, float paramFloat)
  {
    super.onParameterConfigDataChanged(paramParameterConfigViewInterface, 0, paramFloat);
  }
  
  public void onParameterConfigRest(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt)
  {
    super.onParameterConfigRest(paramParameterConfigViewInterface, 0);
  }
  
  public float readParameterValue(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt)
  {
    return super.readParameterValue(paramParameterConfigViewInterface, 0);
  }
  
  protected void handleSelectiveAction(int paramInt, float paramFloat)
  {
    if (this.a == paramInt)
    {
      if (this.a > 0) {
        a();
      }
      return;
    }
    this.a = paramInt;
    a(paramFloat);
  }
  
  private void a(float paramFloat)
  {
    SelesParameters localSelesParameters = getFilterParameter();
    if (localSelesParameters == null) {
      return;
    }
    localSelesParameters.reset();
    localSelesParameters.setFilterArg("selective", paramFloat);
    onParameterConfigRest((ParameterConfigViewInterface)getConfigView(), 0);
    if (paramFloat > 0.0F) {
      a();
    }
  }
  
  private void a()
  {
    if (getConfigView() == null) {
      return;
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("aperture");
    ((ParameterConfigViewInterface)getConfigView()).setParams(localArrayList, 0);
    setConfigViewShowState(true);
    a(true);
    c();
  }
  
  private void b()
  {
    a(false);
  }
  
  private void c()
  {
    ThreadHelper.postDelayed(this.d, 1000L);
  }
  
  private void d()
  {
    ThreadHelper.cancel(this.d);
  }
  
  private void a(boolean paramBoolean)
  {
    d();
    if (this.c == paramBoolean) {
      return;
    }
    this.c = paramBoolean;
    getImageWrapView().startAnimation(e());
  }
  
  private MaskAnimation e()
  {
    if (this.b == null)
    {
      this.b = new MaskAnimation(null);
      this.b.setDuration(260L);
      this.b.setInterpolator(new AccelerateDecelerateInterpolator());
    }
    this.b.cancel();
    this.b.reset();
    return this.b;
  }
  
  private void b(float paramFloat)
  {
    SelesParameters localSelesParameters = getFilterParameter();
    if (localSelesParameters == null) {
      return;
    }
    if (!this.c) {
      paramFloat = 1.0F - paramFloat;
    }
    localSelesParameters.setFilterArg("maskAlpha", paramFloat);
    requestRender();
  }
  
  protected void handleConfigCompeleteButton()
  {
    setConfigViewShowState(false);
  }
  
  protected void handleCompleteButton()
  {
    d();
    e().cancel();
    SelesParameters localSelesParameters = getFilterParameter();
    if (localSelesParameters == null) {
      return;
    }
    localSelesParameters.reset("maskAlpha");
    requestRender();
    super.handleCompleteButton();
  }
  
  private FilterWrap f()
  {
    FilterOption local3 = new FilterOption()
    {
      public SelesOutInput getFilter()
      {
        return new TuSDKApertureFilter();
      }
    };
    local3.id = Long.MAX_VALUE;
    local3.canDefinition = true;
    local3.isInternal = true;
    FilterWrap localFilterWrap = FilterWrap.creat(local3);
    return localFilterWrap;
  }
  
  private class MaskAnimation
    extends Animation
  {
    private MaskAnimation() {}
    
    protected void applyTransformation(float paramFloat, Transformation paramTransformation)
    {
      TuEditApertureFragmentBase.a(TuEditApertureFragmentBase.this, paramFloat);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\filter\TuEditApertureFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */