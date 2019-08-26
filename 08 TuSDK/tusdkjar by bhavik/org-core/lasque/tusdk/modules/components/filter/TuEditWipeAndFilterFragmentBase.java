package org.lasque.tusdk.modules.components.filter;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.filters.blurs.TuSDKBlurFilter;
import org.lasque.tusdk.impl.activity.TuImageResultFragment;
import org.lasque.tusdk.impl.components.widget.smudge.FilterSmudgeView;
import org.lasque.tusdk.impl.components.widget.smudge.SmudgeView;
import org.lasque.tusdk.impl.components.widget.smudge.SmudgeView.SmudgeActionDelegate;
import org.lasque.tusdk.impl.components.widget.smudge.SmudgeView.SmudgeViewDelegate;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize.SizeType;

public abstract class TuEditWipeAndFilterFragmentBase
  extends TuImageResultFragment
  implements SmudgeView.SmudgeActionDelegate, SmudgeView.SmudgeViewDelegate
{
  private float a = 0.2F;
  private boolean b = true;
  
  public abstract SmudgeView getSmudgeView();
  
  public abstract ImageView getZoomInImage();
  
  public boolean isDisplayMagnifier()
  {
    return this.b;
  }
  
  public void setDisplayMagnifier(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  public float getBrushStrength()
  {
    return this.a;
  }
  
  public void setBrushStrength(float paramFloat)
  {
    if ((paramFloat >= 0.0F) && (paramFloat <= 1.0F)) {
      this.a = paramFloat;
    }
  }
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.editWipeAndFilterFragment);
    if (getZoomInImage() != null) {
      showView(getZoomInImage(), false);
    }
  }
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    if (getSmudgeView() != null)
    {
      FilterSmudgeView localFilterSmudgeView = (FilterSmudgeView)getSmudgeView();
      localFilterSmudgeView.setActionDelegate(this);
      FilterWrap localFilterWrap = a();
      SelesParameters localSelesParameters = localFilterWrap.getFilterParameter();
      if (localSelesParameters != null)
      {
        localSelesParameters.setFilterArg("blurSize", getBrushStrength());
        localFilterWrap.setFilterParameter(localSelesParameters);
      }
      localFilterSmudgeView.setFilterWrap(localFilterWrap);
    }
  }
  
  public void onDestroyView()
  {
    super.onDestroyView();
    if (getSmudgeView() != null) {
      getSmudgeView().destroy();
    }
  }
  
  protected void setBrushSize(BrushSize.SizeType paramSizeType)
  {
    if (getSmudgeView() != null) {
      getSmudgeView().setBrushSize(paramSizeType);
    }
  }
  
  protected void handleBackButton()
  {
    navigatorBarBackAction(null);
  }
  
  protected void handleUndoButton()
  {
    if (getSmudgeView() != null) {
      getSmudgeView().undo();
    }
  }
  
  protected void handleRedoButton()
  {
    if (getSmudgeView() != null) {
      getSmudgeView().redo();
    }
  }
  
  protected void handleOrigianlButtonDown()
  {
    if (getSmudgeView() != null) {
      getSmudgeView().showOriginalImage(true);
    }
  }
  
  protected void handleOrigianlButtonUp()
  {
    if (getSmudgeView() != null) {
      getSmudgeView().showOriginalImage(false);
    }
  }
  
  public void onRefreshStepStatesWithHistories(int paramInt1, int paramInt2) {}
  
  public void onSmudgeChanged(PointF paramPointF1, PointF paramPointF2, int paramInt1, int paramInt2)
  {
    if ((getZoomInImage() == null) || (!isDisplayMagnifier())) {
      return;
    }
    ImageView localImageView = getZoomInImage();
    int i = TuSdkContext.dip2px(90.0F);
    if ((paramPointF1.x < 0.0F) || (paramPointF1.x > paramInt1) || (paramPointF1.y < 0.0F) || (paramPointF1.y > paramInt2))
    {
      showView(localImageView, false);
      return;
    }
    if (localImageView.getVisibility() != 0) {
      showView(localImageView, true);
    }
    RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)localImageView.getLayoutParams();
    if ((paramPointF1.x < i + 50) && (paramPointF1.y < i + 50))
    {
      localLayoutParams.removeRule(9);
      localLayoutParams.addRule(11);
    }
    else
    {
      localLayoutParams.removeRule(11);
      localLayoutParams.addRule(9);
    }
    localImageView.setLayoutParams(localLayoutParams);
    localImageView.setImageBitmap(a(paramPointF1, paramInt1));
  }
  
  private Bitmap a(PointF paramPointF, int paramInt)
  {
    int i = TuSdkContext.dip2px(90.0F);
    SmudgeView localSmudgeView = getSmudgeView();
    Bitmap localBitmap1 = localSmudgeView.getOriginalBitmap();
    Bitmap localBitmap2 = localSmudgeView.getSmudgeBitmap();
    Bitmap localBitmap3 = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap3);
    Rect localRect1 = new Rect(0, 0, i, i);
    Rect localRect2 = new Rect();
    float f = localBitmap1.getWidth() / paramInt;
    f /= 2.0F;
    localRect2.left = ((int)(paramPointF.x - i * f / 2.0F));
    localRect2.top = ((int)(paramPointF.y - i * f / 2.0F));
    localRect2.right = (localRect2.left + (int)(i * f));
    localRect2.bottom = (localRect2.top + (int)(i * f));
    localCanvas.drawBitmap(localBitmap1, localRect2, localRect1, null);
    f = localBitmap2.getWidth() / paramInt;
    f /= 2.0F;
    localRect2.left = ((int)(paramPointF.x - i * f / 2.0F));
    localRect2.top = ((int)(paramPointF.y - i * f / 2.0F));
    localRect2.right = (localRect2.left + (int)(i * f));
    localRect2.bottom = (localRect2.top + (int)(i * f));
    localCanvas.drawBitmap(localBitmap2, localRect2, localRect1, null);
    int j = localSmudgeView.getBrushSizePixel();
    Paint localPaint = new Paint();
    localPaint.setAntiAlias(true);
    localPaint.setColor(-1);
    localCanvas.drawCircle(i / 2, i / 2, j / 2, localPaint);
    return localBitmap3;
  }
  
  public void onSmudgeEnd()
  {
    showView(getZoomInImage(), false);
  }
  
  private FilterWrap a()
  {
    FilterOption local1 = new FilterOption()
    {
      public SelesOutInput getFilter()
      {
        return new TuSDKBlurFilter();
      }
    };
    local1.id = Long.MAX_VALUE;
    local1.canDefinition = true;
    local1.isInternal = true;
    FilterWrap localFilterWrap = FilterWrap.creat(local1);
    return localFilterWrap;
  }
  
  protected void handleCompleteButton()
  {
    if (getSmudgeView() == null)
    {
      handleBackButton();
      return;
    }
    final TuSdkResult localTuSdkResult = new TuSdkResult();
    hubStatus(TuSdkContext.getString("lsq_edit_processing"));
    new Thread(new Runnable()
    {
      public void run()
      {
        TuEditWipeAndFilterFragmentBase.this.asyncEditWithResult(localTuSdkResult);
      }
    }).start();
  }
  
  protected void asyncEditWithResult(TuSdkResult paramTuSdkResult)
  {
    loadOrginImage(paramTuSdkResult);
    paramTuSdkResult.image = getSmudgeView().getCanvasImage(paramTuSdkResult.image, !isShowResultPreview());
    asyncProcessingIfNeedSave(paramTuSdkResult);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\filter\TuEditWipeAndFilterFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */