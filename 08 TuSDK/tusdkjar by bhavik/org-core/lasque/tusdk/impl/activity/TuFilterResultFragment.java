package org.lasque.tusdk.impl.activity;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.tusdk.FilterImageView;
import org.lasque.tusdk.core.seles.tusdk.FilterImageViewInterface;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface.ParameterConfigViewDelegate;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;

public abstract class TuFilterResultFragment
  extends TuImageResultFragment
  implements ParameterConfigViewInterface.ParameterConfigViewDelegate
{
  private TuFilterResultFragmentDelegate a;
  private FilterWrap b;
  private FilterImageViewInterface c;
  protected View.OnClickListener mButtonClickListener = new TuSdkViewHelper.OnSafeClickListener()
  {
    public void onSafeClick(View paramAnonymousView)
    {
      TuFilterResultFragment.this.dispatcherViewClick(paramAnonymousView);
    }
  };
  
  public TuFilterResultFragmentDelegate getDelegate()
  {
    return this.a;
  }
  
  public void setDelegate(TuFilterResultFragmentDelegate paramTuFilterResultFragmentDelegate)
  {
    this.a = paramTuFilterResultFragmentDelegate;
    setErrorListener(paramTuFilterResultFragmentDelegate);
  }
  
  protected void notifyProcessing(TuSdkResult paramTuSdkResult)
  {
    if (showResultPreview(paramTuSdkResult)) {
      return;
    }
    if (this.a == null) {
      return;
    }
    this.a.onTuFilterResultFragmentEdited(this, paramTuSdkResult);
  }
  
  protected boolean asyncNotifyProcessing(TuSdkResult paramTuSdkResult)
  {
    if (this.a == null) {
      return false;
    }
    return this.a.onTuFilterResultFragmentEditedAsync(this, paramTuSdkResult);
  }
  
  private FilterWrap a()
  {
    return this.b;
  }
  
  protected final void setFilterWrap(FilterWrap paramFilterWrap)
  {
    this.b = paramFilterWrap;
  }
  
  protected SelesParameters getFilterParameter()
  {
    if (a() == null) {
      return null;
    }
    SelesParameters localSelesParameters = a().getFilterParameter();
    return localSelesParameters;
  }
  
  public abstract RelativeLayout getImageWrapView();
  
  public abstract View getCancelButton();
  
  public abstract View getCompleteButton();
  
  public abstract <T extends View,  extends ParameterConfigViewInterface> T getConfigView();
  
  public <T extends View,  extends FilterImageViewInterface> T getImageView()
  {
    if ((this.c == null) && (getImageWrapView() != null))
    {
      this.c = new FilterImageView(getActivity());
      this.c.enableTouchForOrigin();
      RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
      localLayoutParams.addRule(13);
      getImageWrapView().addView((View)this.c, 0, localLayoutParams);
    }
    return (View)this.c;
  }
  
  protected void dispatcherViewClick(View paramView)
  {
    if (equalViewIds(paramView, getCancelButton())) {
      handleBackButton();
    } else if (equalViewIds(paramView, getCompleteButton())) {
      handleCompleteButton();
    }
  }
  
  protected void handleBackButton()
  {
    navigatorBarBackAction(null);
  }
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    getCancelButton();
    getCompleteButton();
    getConfigView();
    getImageView();
  }
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    loadImageWithThread();
    if (getConfigView() == null) {
      return;
    }
    refreshConfigView();
  }
  
  protected void refreshConfigView()
  {
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        SelesParameters localSelesParameters = TuFilterResultFragment.a(TuFilterResultFragment.this).getFilterParameter();
        if ((localSelesParameters == null) || (localSelesParameters.size() == 0)) {
          return;
        }
        ((ParameterConfigViewInterface)TuFilterResultFragment.this.getConfigView()).setParams(localSelesParameters.getArgKeys(), 0);
      }
    }, 1L);
  }
  
  protected void asyncLoadImageCompleted(Bitmap paramBitmap)
  {
    super.asyncLoadImageCompleted(paramBitmap);
    if (paramBitmap == null) {
      return;
    }
    if ((getImageView() == null) || (getConfigView() == null)) {
      return;
    }
    ((FilterImageViewInterface)getImageView()).setImage(paramBitmap);
    ((FilterImageViewInterface)getImageView()).setFilterWrap(a());
  }
  
  protected void setImageViewFilter(FilterWrap paramFilterWrap)
  {
    setFilterWrap(paramFilterWrap);
    if (getImageView() == null) {
      return;
    }
    if (paramFilterWrap == null) {
      paramFilterWrap = FilterLocalPackage.shared().getFilterWrap(null);
    }
    ((FilterImageViewInterface)getImageView()).setFilterWrap(paramFilterWrap);
  }
  
  public void onParameterConfigDataChanged(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt, float paramFloat)
  {
    SelesParameters.FilterArg localFilterArg = getFilterArg(paramInt);
    if (localFilterArg == null) {
      return;
    }
    localFilterArg.setPrecentValue(paramFloat);
    requestRender();
  }
  
  public void onParameterConfigRest(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt)
  {
    SelesParameters.FilterArg localFilterArg = getFilterArg(paramInt);
    if (localFilterArg == null) {
      return;
    }
    localFilterArg.reset();
    requestRender();
    paramParameterConfigViewInterface.seekTo(localFilterArg.getPrecentValue());
  }
  
  public float readParameterValue(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt)
  {
    SelesParameters.FilterArg localFilterArg = getFilterArg(paramInt);
    if (localFilterArg == null) {
      return 0.0F;
    }
    return localFilterArg.getPrecentValue();
  }
  
  protected SelesParameters.FilterArg getFilterArg(int paramInt)
  {
    if (paramInt < 0) {
      return null;
    }
    SelesParameters localSelesParameters = a().getFilterParameter();
    if ((localSelesParameters == null) || (paramInt >= localSelesParameters.size())) {
      return null;
    }
    return (SelesParameters.FilterArg)localSelesParameters.getArgs().get(paramInt);
  }
  
  protected void requestRender()
  {
    if (getImageView() != null) {
      ((FilterImageViewInterface)getImageView()).requestRender();
    }
  }
  
  protected void handleCompleteButton()
  {
    final TuSdkResult localTuSdkResult = new TuSdkResult();
    localTuSdkResult.filterWrap = a();
    hubStatus(TuSdkContext.getString("lsq_edit_processing"));
    new Thread(new Runnable()
    {
      public void run()
      {
        TuFilterResultFragment.this.asyncEditWithResult(localTuSdkResult);
      }
    }).start();
  }
  
  protected void asyncEditWithResult(TuSdkResult paramTuSdkResult)
  {
    loadOrginImage(paramTuSdkResult);
    if (paramTuSdkResult.filterWrap != null)
    {
      float f = TuSdkSize.create(paramTuSdkResult.image).limitScale();
      paramTuSdkResult.image = BitmapHelper.imageScale(paramTuSdkResult.image, f);
      paramTuSdkResult.image = paramTuSdkResult.filterWrap.clone().process(paramTuSdkResult.image);
    }
    asyncProcessingIfNeedSave(paramTuSdkResult);
  }
  
  public static abstract interface TuFilterResultFragmentDelegate
    extends TuSdkComponentErrorListener
  {
    public abstract void onTuFilterResultFragmentEdited(TuFilterResultFragment paramTuFilterResultFragment, TuSdkResult paramTuSdkResult);
    
    public abstract boolean onTuFilterResultFragmentEditedAsync(TuFilterResultFragment paramTuFilterResultFragment, TuSdkResult paramTuSdkResult);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\activity\TuFilterResultFragment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */