package org.lasque.tusdk.modules.components.edit;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.view.TuSdkTouchImageView;
import org.lasque.tusdk.core.view.TuSdkTouchImageViewInterface;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.TuMaskRegionView;
import org.lasque.tusdk.impl.activity.TuImageResultFragment;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.components.ComponentErrorType;

public abstract class TuEditTurnAndCutFragmentBase
  extends TuImageResultFragment
{
  private String a;
  private TuSdkTouchImageViewInterface b;
  protected View.OnLayoutChangeListener mRegionLayoutChangeListener = new View.OnLayoutChangeListener()
  {
    public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8)
    {
      if ((paramAnonymousInt1 != paramAnonymousInt5) || (paramAnonymousInt2 != paramAnonymousInt6) || (paramAnonymousInt3 != paramAnonymousInt7) || (paramAnonymousInt4 != paramAnonymousInt8)) {
        TuEditTurnAndCutFragmentBase.this.onRegionLayoutChanged(TuEditTurnAndCutFragmentBase.this.getCutRegionView());
      }
    }
  };
  
  public abstract TuSdkSize getCutSize();
  
  public abstract RelativeLayout getImageWrapView();
  
  public abstract TuMaskRegionView getCutRegionView();
  
  public String getSelectedFilterCode()
  {
    return this.a;
  }
  
  public <T extends View,  extends TuSdkTouchImageViewInterface> T getImageView()
  {
    if (this.b == null)
    {
      RelativeLayout localRelativeLayout = getImageWrapView();
      if (localRelativeLayout != null)
      {
        this.b = new TuSdkTouchImageView(getActivity());
        localRelativeLayout.addView((View)this.b, new RelativeLayout.LayoutParams(-1, -1));
        this.b.setInvalidateTargetView(getCutRegionView());
      }
    }
    return (View)this.b;
  }
  
  protected void onRegionLayoutChanged(TuMaskRegionView paramTuMaskRegionView)
  {
    if ((paramTuMaskRegionView == null) || (getImageView() == null)) {
      return;
    }
    ((TuSdkTouchImageViewInterface)getImageView()).changeRegionRatio(paramTuMaskRegionView.getRegionRect(), getCutSize() == null ? 0.0F : paramTuMaskRegionView.getRegionRatio());
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
    if ((getCutSize() == null) || (getCutRegionView() == null))
    {
      ((TuSdkTouchImageViewInterface)getImageView()).setScaleType(ImageView.ScaleType.FIT_CENTER);
      return;
    }
    getCutRegionView().setRegionSize(getCutSize());
    ((TuSdkTouchImageViewInterface)getImageView()).setScaleType(ImageView.ScaleType.CENTER_CROP);
    Rect localRect = getCutRegionView().getRegionRect();
    TuSdkViewHelper.setViewRect(getImageView(), localRect);
    ((TuSdkTouchImageViewInterface)getImageView()).setZoom(1.0F);
  }
  
  protected boolean handleSwitchFilter(final String paramString)
  {
    if ((getImageView() == null) || (((TuSdkTouchImageViewInterface)getImageView()).isInAnimation())) {
      return false;
    }
    final Bitmap localBitmap = getImage();
    if ((localBitmap == null) || (paramString == null) || (paramString.equalsIgnoreCase(this.a))) {
      return false;
    }
    this.a = paramString;
    hubStatus(TuSdkContext.getString("lsq_edit_filter_processing"));
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        TuEditTurnAndCutFragmentBase.a(TuEditTurnAndCutFragmentBase.this, paramString, localBitmap);
      }
    });
    return true;
  }
  
  private void a(String paramString, Bitmap paramBitmap)
  {
    final Bitmap localBitmap = a(paramBitmap, paramString, ((TuSdkTouchImageViewInterface)getImageView()).getImageOrientation());
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuEditTurnAndCutFragmentBase.this.processedFilter(localBitmap);
      }
    });
  }
  
  private Bitmap a(Bitmap paramBitmap, String paramString, ImageOrientation paramImageOrientation)
  {
    FilterWrap localFilterWrap = FilterLocalPackage.shared().getFilterWrap(paramString);
    if (localFilterWrap == null) {
      return paramBitmap;
    }
    localFilterWrap.setFilterParameter(null);
    return localFilterWrap.process(paramBitmap, paramImageOrientation, 0.0F);
  }
  
  protected void processedFilter(Bitmap paramBitmap)
  {
    if (getImageView() == null) {
      return;
    }
    ((TuSdkTouchImageViewInterface)getImageView()).setImageBitmapWithAnim(paramBitmap);
    hubDismiss();
  }
  
  protected void handleCompleteButton()
  {
    if ((getImageView() == null) || (((TuSdkTouchImageViewInterface)getImageView()).isInAnimation())) {
      return;
    }
    final TuSdkResult localTuSdkResult = new TuSdkResult();
    localTuSdkResult.cutRect = ((TuSdkTouchImageViewInterface)getImageView()).getZoomedRect();
    localTuSdkResult.imageOrientation = ((TuSdkTouchImageViewInterface)getImageView()).getImageOrientation();
    localTuSdkResult.outputSize = getCutSize();
    localTuSdkResult.filterCode = getSelectedFilterCode();
    hubStatus(TuSdkContext.getString("lsq_edit_processing"));
    new Thread(new Runnable()
    {
      public void run()
      {
        TuEditTurnAndCutFragmentBase.this.asyncEditWithResult(localTuSdkResult);
      }
    }).start();
  }
  
  protected void asyncEditWithResult(TuSdkResult paramTuSdkResult)
  {
    loadOrginImage(paramTuSdkResult);
    if (paramTuSdkResult.outputSize != null) {
      paramTuSdkResult.image = BitmapHelper.imageCorp(paramTuSdkResult.image, paramTuSdkResult.cutRect, paramTuSdkResult.outputSize, paramTuSdkResult.imageOrientation);
    } else {
      paramTuSdkResult.image = BitmapHelper.imageRotaing(paramTuSdkResult.image, paramTuSdkResult.imageOrientation);
    }
    if (paramTuSdkResult.filterCode != null) {
      paramTuSdkResult.image = a(paramTuSdkResult.image, paramTuSdkResult.filterCode, ImageOrientation.Up);
    }
    asyncProcessingIfNeedSave(paramTuSdkResult);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\edit\TuEditTurnAndCutFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */