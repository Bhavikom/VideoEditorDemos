package org.lasque.tusdk.modules.components.edit;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.view.TuSdkTouchImageView;
import org.lasque.tusdk.core.view.TuSdkTouchImageViewInterface;
import org.lasque.tusdk.core.view.TuSdkTouchImageViewInterface.LsqImageChangeType;
import org.lasque.tusdk.impl.activity.TuImageResultFragment;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.components.ComponentErrorType;

public abstract class TuEditTurnFragmentBase
  extends TuImageResultFragment
{
  private TuSdkTouchImageViewInterface a;
  
  public abstract RelativeLayout getImageWrapView();
  
  public <T extends View,  extends TuSdkTouchImageViewInterface> T getImageView()
  {
    if (this.a == null)
    {
      RelativeLayout localRelativeLayout = getImageWrapView();
      if (localRelativeLayout != null)
      {
        this.a = new TuSdkTouchImageView(getActivity());
        localRelativeLayout.addView((View)this.a, new RelativeLayout.LayoutParams(-1, -1));
        this.a.setInvalidateTargetView(getImageWrapView());
      }
    }
    return (View)this.a;
  }
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.editEntryFragment);
  }
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    if (getImage() == null)
    {
      notifyError(null, ComponentErrorType.TypeInputImageEmpty);
      TLog.e("Can not find input image.", new Object[0]);
      return;
    }
    if (getImageView() == null) {
      return;
    }
    ((TuSdkTouchImageViewInterface)getImageView()).setImageBitmap(getImage());
    ((TuSdkTouchImageViewInterface)getImageView()).setScaleType(ImageView.ScaleType.FIT_CENTER);
  }
  
  protected void handleCompleteButton()
  {
    if ((getImageView() == null) || (((TuSdkTouchImageViewInterface)getImageView()).isInAnimation())) {
      return;
    }
    final TuSdkResult localTuSdkResult = new TuSdkResult();
    localTuSdkResult.imageOrientation = ((TuSdkTouchImageViewInterface)getImageView()).getImageOrientation();
    hubStatus(TuSdkContext.getString("lsq_edit_processing"));
    new Thread(new Runnable()
    {
      public void run()
      {
        TuEditTurnFragmentBase.this.asyncEditWithResult(localTuSdkResult);
      }
    }).start();
  }
  
  protected void asyncEditWithResult(TuSdkResult paramTuSdkResult)
  {
    loadOrginImage(paramTuSdkResult);
    paramTuSdkResult.image = BitmapHelper.imageRotaing(paramTuSdkResult.image, paramTuSdkResult.imageOrientation);
    asyncProcessingIfNeedSave(paramTuSdkResult);
  }
  
  protected void changeImageAnimation(TuSdkTouchImageViewInterface.LsqImageChangeType paramLsqImageChangeType)
  {
    if ((getImageView() == null) || (((TuSdkTouchImageViewInterface)getImageView()).isInAnimation())) {
      return;
    }
    ((TuSdkTouchImageViewInterface)getImageView()).changeImageAnimation(paramLsqImageChangeType);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\edit\TuEditTurnFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */