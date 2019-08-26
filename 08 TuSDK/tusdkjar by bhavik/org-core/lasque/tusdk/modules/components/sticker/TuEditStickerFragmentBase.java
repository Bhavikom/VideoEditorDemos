package org.lasque.tusdk.modules.components.sticker;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.ViewGroup;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.view.widget.TuMaskRegionView;
import org.lasque.tusdk.impl.activity.TuImageResultFragment;
import org.lasque.tusdk.impl.components.widget.sticker.StickerView;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerFactory;

public abstract class TuEditStickerFragmentBase
  extends TuImageResultFragment
{
  public abstract StickerView getStickerView();
  
  public abstract TuMaskRegionView getCutRegionView();
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.editStickerFragment);
  }
  
  protected void viewDidLoad(ViewGroup paramViewGroup) {}
  
  protected void handleBackButton()
  {
    navigatorBarBackAction(null);
  }
  
  public final void appendStickerItem(StickerData paramStickerData)
  {
    if ((paramStickerData == null) || (getStickerView() == null)) {
      return;
    }
    getStickerView().appendSticker(paramStickerData);
  }
  
  public final void appendStickerItem(Bitmap paramBitmap)
  {
    if ((paramBitmap == null) || (getStickerView() == null)) {
      return;
    }
    getStickerView().appendSticker(paramBitmap);
  }
  
  protected void handleCompleteButton()
  {
    if (getStickerView() == null)
    {
      handleBackButton();
      return;
    }
    final TuSdkResult localTuSdkResult = new TuSdkResult();
    Rect localRect = null;
    if (getCutRegionView() != null) {
      localRect = getCutRegionView().getRegionRect();
    }
    localTuSdkResult.stickers = getStickerView().getResults(localRect);
    if ((localTuSdkResult.stickers == null) || (localTuSdkResult.stickers.size() == 0))
    {
      handleBackButton();
      return;
    }
    hubStatus(TuSdkContext.getString("lsq_edit_processing"));
    new Thread(new Runnable()
    {
      public void run()
      {
        TuEditStickerFragmentBase.this.asyncEditWithResult(localTuSdkResult);
      }
    }).start();
  }
  
  protected void asyncEditWithResult(TuSdkResult paramTuSdkResult)
  {
    loadOrginImage(paramTuSdkResult);
    if (paramTuSdkResult.stickers != null)
    {
      paramTuSdkResult.image = StickerFactory.megerStickers(paramTuSdkResult.image, paramTuSdkResult.stickers, null, !isShowResultPreview());
      paramTuSdkResult.stickers = null;
    }
    asyncProcessingIfNeedSave(paramTuSdkResult);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\sticker\TuEditStickerFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */