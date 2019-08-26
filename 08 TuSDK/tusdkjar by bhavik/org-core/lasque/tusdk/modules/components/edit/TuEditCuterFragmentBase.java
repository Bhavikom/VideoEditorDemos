package org.lasque.tusdk.modules.components.edit;

import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.core.view.TuSdkTouchImageView;
import org.lasque.tusdk.core.view.TuSdkTouchImageViewInterface;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.TuMaskRegionView;
import org.lasque.tusdk.impl.activity.TuImageResultFragment;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.components.ComponentErrorType;

public abstract class TuEditCuterFragmentBase
  extends TuImageResultFragment
{
  private TuSdkTouchImageViewInterface a;
  protected View.OnLayoutChangeListener mRegionLayoutChangeListener = new View.OnLayoutChangeListener()
  {
    public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8)
    {
      if ((paramAnonymousInt1 != paramAnonymousInt5) || (paramAnonymousInt2 != paramAnonymousInt6) || (paramAnonymousInt3 != paramAnonymousInt7) || (paramAnonymousInt4 != paramAnonymousInt8)) {
        TuEditCuterFragmentBase.this.onRegionLayoutChanged(TuEditCuterFragmentBase.this.getCutRegionView());
      }
    }
  };
  private RectF b;
  private float c;
  private ImageOrientation d = ImageOrientation.Up;
  private int e;
  private float f;
  
  public abstract int[] getRatioTypes();
  
  public abstract boolean isOnlyReturnCuter();
  
  public abstract RelativeLayout getImageWrapView();
  
  public abstract TuMaskRegionView getCutRegionView();
  
  public <T extends View,  extends TuSdkTouchImageViewInterface> T getImageView()
  {
    if (this.a == null)
    {
      RelativeLayout localRelativeLayout = getImageWrapView();
      if (localRelativeLayout != null)
      {
        this.a = new TuSdkTouchImageView(getActivity());
        localRelativeLayout.addView((View)this.a, new RelativeLayout.LayoutParams(-1, -1));
        this.a.setInvalidateTargetView(getCutRegionView());
      }
    }
    return (View)this.a;
  }
  
  protected void onRegionLayoutChanged(TuMaskRegionView paramTuMaskRegionView)
  {
    if ((paramTuMaskRegionView == null) || (getImageView() == null)) {
      return;
    }
    ((TuSdkTouchImageViewInterface)getImageView()).changeRegionRatio(paramTuMaskRegionView.getRegionRect(), paramTuMaskRegionView.getRegionRatio());
  }
  
  protected void loadView(ViewGroup paramViewGroup) {}
  
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
    ((TuSdkTouchImageViewInterface)getImageView()).setImageBitmap(getImage(), getImageOrientation());
    if (((getRatioTypes().length == 1) && (getRatioTypes()[0] == 1)) || (getCurrentRatio() == 0.0F) || (getCutRegionView() == null))
    {
      ((TuSdkTouchImageViewInterface)getImageView()).setScaleType(ImageView.ScaleType.FIT_CENTER);
      return;
    }
    Rect localRect = getCutRegionView().setRegionRatio(getCurrentRatio());
    ((TuSdkTouchImageViewInterface)getImageView()).setScaleType(ImageView.ScaleType.CENTER_CROP);
    TuSdkViewHelper.setViewRect(getImageView(), localRect);
    RectF localRectF = getZoomRect();
    if (localRectF == null) {
      ((TuSdkTouchImageViewInterface)getImageView()).setZoom(1.0F);
    } else if (localRectF != null) {
      ((TuSdkTouchImageViewInterface)getImageView()).setZoom(getZoomScale(), (localRectF.left + localRectF.right) * 0.5F, (localRectF.top + localRectF.bottom) * 0.5F);
    }
    StatisticsManger.appendComponent(ComponentActType.editCuterFragment);
  }
  
  public final RectF getZoomRect()
  {
    return this.b;
  }
  
  public final void setZoomRect(RectF paramRectF)
  {
    this.b = paramRectF;
  }
  
  public final float getZoomScale()
  {
    if (this.c <= 0.0F) {
      this.c = 1.0F;
    }
    return this.c;
  }
  
  public final void setZoomScale(float paramFloat)
  {
    this.c = paramFloat;
  }
  
  public final ImageOrientation getImageOrientation()
  {
    return this.d;
  }
  
  public final void setImageOrientation(ImageOrientation paramImageOrientation)
  {
    this.d = paramImageOrientation;
  }
  
  public final int getCurrentRatioType()
  {
    if (this.e < 1)
    {
      int[] arrayOfInt = getRatioTypes();
      if ((arrayOfInt == null) || (arrayOfInt.length == 0)) {
        arrayOfInt = RatioType.ratioTypes;
      }
      setCurrentRatioType(arrayOfInt[0]);
    }
    return this.e;
  }
  
  public final void setCurrentRatioType(int paramInt)
  {
    this.e = paramInt;
    this.f = RatioType.ratio(this.e);
    StatisticsManger.appendComponent(RatioType.ratioActionType(paramInt));
  }
  
  public final float getCurrentRatio()
  {
    return this.f;
  }
  
  public final void setCuterResult(TuSdkResult paramTuSdkResult)
  {
    if (paramTuSdkResult == null) {
      return;
    }
    setZoomRect(paramTuSdkResult.cutRect);
    setZoomScale(paramTuSdkResult.cutScale);
    setImageOrientation(paramTuSdkResult.imageOrientation);
    setCurrentRatioType(paramTuSdkResult.cutRatioType);
  }
  
  protected void handleCompleteButton()
  {
    if ((getImageView() == null) || (((TuSdkTouchImageViewInterface)getImageView()).isInAnimation())) {
      return;
    }
    final TuSdkResult localTuSdkResult = new TuSdkResult();
    localTuSdkResult.imageOrientation = ((TuSdkTouchImageViewInterface)getImageView()).getImageOrientation();
    if (getCurrentRatio() > 0.0F)
    {
      localTuSdkResult.imageSizeRatio = getCurrentRatio();
      localTuSdkResult.cutRect = ((TuSdkTouchImageViewInterface)getImageView()).getZoomedRect();
      localTuSdkResult.cutScale = ((TuSdkTouchImageViewInterface)getImageView()).getCurrentZoom();
    }
    localTuSdkResult.cutRatioType = getCurrentRatioType();
    hubStatus(TuSdkContext.getString("lsq_edit_processing"));
    new Thread(new Runnable()
    {
      public void run()
      {
        TuEditCuterFragmentBase.this.asyncEditWithResult(localTuSdkResult);
      }
    }).start();
  }
  
  protected void asyncEditWithResult(TuSdkResult paramTuSdkResult)
  {
    if (isOnlyReturnCuter())
    {
      backUIThreadNotifyProcessing(paramTuSdkResult);
      return;
    }
    loadOrginImage(paramTuSdkResult);
    paramTuSdkResult.image = getCuterImage(paramTuSdkResult.image, paramTuSdkResult);
    asyncProcessingIfNeedSave(paramTuSdkResult);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\edit\TuEditCuterFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */