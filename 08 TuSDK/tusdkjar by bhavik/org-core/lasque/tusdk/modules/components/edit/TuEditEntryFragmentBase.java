package org.lasque.tusdk.modules.components.edit;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.tusdk.FilterImageView;
import org.lasque.tusdk.core.seles.tusdk.FilterImageViewInterface;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.core.view.widget.TuMaskRegionView;
import org.lasque.tusdk.impl.activity.TuImageResultFragment;
import org.lasque.tusdk.impl.components.widget.sticker.StickerView;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerFactory;

public abstract class TuEditEntryFragmentBase
  extends TuImageResultFragment
{
  private FilterImageViewInterface a;
  private FilterWrap b;
  
  public abstract RelativeLayout getImageWrapView();
  
  public abstract TuMaskRegionView getCutRegionView();
  
  public abstract StickerView getStickerView();
  
  public abstract TuSdkResult getCuterResult();
  
  public abstract int[] getRatioTypes();
  
  public abstract int getLimitSideSize();
  
  public abstract boolean isLimitForScreen();
  
  public <T extends View,  extends FilterImageViewInterface> T getImageView()
  {
    if ((this.a == null) && (getImageWrapView() != null))
    {
      this.a = new FilterImageView(getActivity());
      RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
      localLayoutParams.addRule(13);
      getImageWrapView().addView((View)this.a, 0, localLayoutParams);
    }
    return (View)this.a;
  }
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    getImageView();
  }
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.editTurnAndCutFragment);
  }
  
  public FilterWrap getFilterWrap()
  {
    return this.b;
  }
  
  public void setFilterWrap(FilterWrap paramFilterWrap)
  {
    this.b = paramFilterWrap;
    if ((this.b != null) && (getImageView() != null))
    {
      this.b = this.b.clone();
      ((FilterImageViewInterface)getImageView()).setFilterWrap(this.b);
    }
  }
  
  public Bitmap getFilterImage()
  {
    Bitmap localBitmap = getImage();
    if (localBitmap == null) {
      return null;
    }
    if (getFilterWrap() != null) {
      localBitmap = getFilterWrap().clone().process(localBitmap);
    }
    return localBitmap;
  }
  
  public Bitmap getCuterImage()
  {
    Bitmap localBitmap = getImage();
    TuSdkResult localTuSdkResult = getCuterResult();
    float f = RatioType.firstRatio(getRatioTypes()[0]);
    if (localTuSdkResult != null) {
      return getCuterImage(localBitmap, localTuSdkResult);
    }
    if (f > 0.0F) {
      return BitmapHelper.imageCorp(localBitmap, f);
    }
    return localBitmap;
  }
  
  private int a()
  {
    int i = 0;
    if (getLimitSideSize() > 0) {
      i = getLimitSideSize();
    } else {
      i = ContextUtils.getScreenSize(getActivity()).maxSide();
    }
    Integer localInteger = Integer.valueOf(SdkValid.shared.maxImageSide());
    if (localInteger.intValue() == 0) {
      return i;
    }
    i = Math.min(i, localInteger.intValue());
    return i;
  }
  
  protected void handleCompleteButton()
  {
    final TuSdkResult localTuSdkResult = new TuSdkResult();
    localTuSdkResult.filterWrap = getFilterWrap();
    if (getCuterResult() != null)
    {
      localTuSdkResult.cutRect = getCuterResult().cutRect;
      localTuSdkResult.imageOrientation = getCuterResult().imageOrientation;
    }
    else
    {
      localTuSdkResult.imageSizeRatio = RatioType.firstRatio(getRatioTypes()[0]);
    }
    if (getStickerView() != null)
    {
      Rect localRect = null;
      if (getCutRegionView() != null) {
        localRect = getCutRegionView().getRegionRect();
      }
      localTuSdkResult.stickers = getStickerView().getResults(localRect);
    }
    hubStatus(TuSdkContext.getString("lsq_edit_processing"));
    new Thread(new Runnable()
    {
      public void run()
      {
        TuEditEntryFragmentBase.this.asyncEditWithResult(localTuSdkResult);
      }
    }).start();
  }
  
  protected void asyncEditWithResult(TuSdkResult paramTuSdkResult)
  {
    loadOrginImage(paramTuSdkResult);
    paramTuSdkResult.image = getCuterImage(paramTuSdkResult.image, paramTuSdkResult.cutRect, paramTuSdkResult.imageOrientation, paramTuSdkResult.imageSizeRatio);
    int i = a();
    paramTuSdkResult.image = BitmapHelper.imageLimit(paramTuSdkResult.image, i);
    if (paramTuSdkResult.filterWrap != null) {
      paramTuSdkResult.image = paramTuSdkResult.filterWrap.process(paramTuSdkResult.image);
    }
    if (paramTuSdkResult.stickers != null)
    {
      paramTuSdkResult.image = StickerFactory.megerStickers(paramTuSdkResult.image, paramTuSdkResult.stickers);
      paramTuSdkResult.stickers = null;
    }
    asyncProcessingIfNeedSave(paramTuSdkResult);
  }
  
  public final void appendStickerItem(StickerData paramStickerData)
  {
    if ((paramStickerData == null) || (getStickerView() == null)) {
      return;
    }
    getStickerView().appendSticker(paramStickerData);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\edit\TuEditEntryFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */