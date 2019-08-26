package org.lasque.tusdk.modules.components.filter;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.tusdk.FilterImageView;
import org.lasque.tusdk.core.seles.tusdk.FilterImageViewInterface;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.impl.activity.TuImageResultFragment;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.components.ComponentErrorType;

public abstract class TuEditFilterFragmentBase
  extends TuImageResultFragment
{
  private FilterWrap a;
  private FilterImageViewInterface b;
  
  public abstract RelativeLayout getImageWrapView();
  
  public abstract void notifyFilterConfigView();
  
  public abstract boolean isOnlyReturnFilter();
  
  public FilterWrap getFilterWrap()
  {
    return this.a;
  }
  
  public void setFilterWrap(FilterWrap paramFilterWrap)
  {
    this.a = paramFilterWrap;
  }
  
  public <T extends View,  extends FilterImageViewInterface> T getImageView()
  {
    if ((this.b == null) && (getImageWrapView() != null))
    {
      this.b = new FilterImageView(getActivity());
      this.b.enableTouchForOrigin();
      RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
      localLayoutParams.addRule(13);
      getImageWrapView().addView((View)this.b, 0, localLayoutParams);
    }
    return (View)this.b;
  }
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    getImageView();
  }
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.editFilterFragment);
    if (getImage() == null)
    {
      notifyError(null, ComponentErrorType.TypeInputImageEmpty);
      TLog.e("Can not find input image.", new Object[0]);
      return;
    }
    if (getImageView() == null) {
      return;
    }
    ((FilterImageViewInterface)getImageView()).setImage(getImage());
    if (getFilterWrap() != null)
    {
      this.a = getFilterWrap().clone();
      ((FilterImageViewInterface)getImageView()).setFilterWrap(this.a);
    }
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        TuEditFilterFragmentBase.this.notifyFilterConfigView();
      }
    }, 1L);
  }
  
  protected boolean handleSwitchFilter(final String paramString)
  {
    if ((paramString == null) || (getImageView() == null)) {
      return false;
    }
    if ((this.a != null) && (this.a.equalsCode(paramString))) {
      return false;
    }
    hubStatus(TuSdkContext.getString("lsq_edit_filter_processing"));
    new Thread(new Runnable()
    {
      public void run()
      {
        TuEditFilterFragmentBase.this.asyncProcessingFilter(paramString);
      }
    }).start();
    return true;
  }
  
  protected void asyncProcessingFilter(String paramString)
  {
    this.a = FilterLocalPackage.shared().getFilterWrap(paramString);
    if (this.a != null) {
      ((FilterImageViewInterface)getImageView()).setFilterWrap(this.a);
    }
    new Handler(Looper.getMainLooper()).post(new Runnable()
    {
      public void run()
      {
        TuEditFilterFragmentBase.this.processedFilter();
      }
    });
  }
  
  protected void processedFilter()
  {
    hubDismiss();
    notifyFilterConfigView();
  }
  
  protected void handleCompleteButton()
  {
    final TuSdkResult localTuSdkResult = new TuSdkResult();
    localTuSdkResult.filterWrap = getFilterWrap();
    hubStatus(TuSdkContext.getString("lsq_edit_processing"));
    new Thread(new Runnable()
    {
      public void run()
      {
        TuEditFilterFragmentBase.this.asyncEditWithResult(localTuSdkResult);
      }
    }).start();
  }
  
  protected void asyncEditWithResult(TuSdkResult paramTuSdkResult)
  {
    if (isOnlyReturnFilter())
    {
      backUIThreadNotifyProcessing(paramTuSdkResult);
      return;
    }
    loadOrginImage(paramTuSdkResult);
    if ((paramTuSdkResult.filterWrap != null) && (paramTuSdkResult.image != null))
    {
      float f = TuSdkSize.create(paramTuSdkResult.image).limitScale();
      paramTuSdkResult.image = BitmapHelper.imageScale(paramTuSdkResult.image, f);
      paramTuSdkResult.image = paramTuSdkResult.filterWrap.process(paramTuSdkResult.image);
    }
    asyncProcessingIfNeedSave(paramTuSdkResult);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\filter\TuEditFilterFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */